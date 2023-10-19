// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v2;

import org.postgresql.core.Encoding;
import java.util.StringTokenizer;
import org.postgresql.core.SetupQueryRunner;
import java.sql.SQLWarning;
import org.postgresql.util.MD5Digest;
import org.postgresql.core.Utils;
import org.postgresql.util.UnixCrypt;
import org.postgresql.ssl.jdbc4.AbstractJdbc4MakeSSL;
import java.util.Iterator;
import org.postgresql.hostchooser.HostChooser;
import java.sql.SQLException;
import java.io.IOException;
import java.net.ConnectException;
import org.postgresql.hostchooser.GlobalHostStatusTracker;
import org.postgresql.hostchooser.HostStatus;
import org.postgresql.core.PGStream;
import org.postgresql.hostchooser.HostChooserFactory;
import org.postgresql.hostchooser.HostRequirement;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.PGProperty;
import org.postgresql.core.ProtocolConnection;
import org.postgresql.core.Logger;
import java.util.Properties;
import org.postgresql.util.HostSpec;
import org.postgresql.core.ConnectionFactory;

public class ConnectionFactoryImpl extends ConnectionFactory
{
    private static final int AUTH_REQ_OK = 0;
    private static final int AUTH_REQ_KRB4 = 1;
    private static final int AUTH_REQ_KRB5 = 2;
    private static final int AUTH_REQ_PASSWORD = 3;
    private static final int AUTH_REQ_CRYPT = 4;
    private static final int AUTH_REQ_MD5 = 5;
    private static final int AUTH_REQ_SCM = 6;
    
    @Override
    public ProtocolConnection openConnectionImpl(final HostSpec[] hostSpecs, final String user, final String database, final Properties info, final Logger logger) throws SQLException {
        final String sslmode = PGProperty.SSL_MODE.get(info);
        boolean requireSSL;
        boolean trySSL;
        if (sslmode == null) {
            trySSL = (requireSSL = PGProperty.SSL.isPresent(info));
        }
        else if ("disable".equals(sslmode)) {
            trySSL = (requireSSL = false);
        }
        else {
            if (!"require".equals(sslmode) && !"verify-ca".equals(sslmode) && !"verify-full".equals(sslmode)) {
                throw new PSQLException(GT.tr("Invalid sslmode value: {0}", sslmode), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
            }
            trySSL = (requireSSL = true);
        }
        final boolean requireTCPKeepAlive = PGProperty.TCP_KEEP_ALIVE.getBoolean(info);
        HostRequirement targetServerType;
        try {
            targetServerType = HostRequirement.valueOf(info.getProperty("targetServerType", HostRequirement.any.name()));
        }
        catch (IllegalArgumentException ex) {
            throw new PSQLException(GT.tr("Invalid targetServerType value: {0}", info.getProperty("targetServerType")), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
        }
        final HostChooser hostChooser = HostChooserFactory.createHostChooser(hostSpecs, targetServerType, info);
        final Iterator<HostSpec> hostIter = hostChooser.iterator();
        while (hostIter.hasNext()) {
            final HostSpec hostSpec = hostIter.next();
            if (logger.logDebug()) {
                logger.debug("Trying to establish a protocol version 2 connection to " + hostSpec);
            }
            final int connectTimeout = PGProperty.CONNECT_TIMEOUT.getInt(info) * 1000;
            PGStream newStream = null;
            try {
                newStream = new PGStream(hostSpec, connectTimeout);
                if (trySSL) {
                    newStream = this.enableSSL(newStream, requireSSL, info, logger, connectTimeout);
                }
                final int socketTimeout = PGProperty.SOCKET_TIMEOUT.getInt(info);
                if (socketTimeout > 0) {
                    newStream.getSocket().setSoTimeout(socketTimeout * 1000);
                }
                newStream.getSocket().setKeepAlive(requireTCPKeepAlive);
                this.sendStartupPacket(newStream, user, database, logger);
                this.doAuthentication(newStream, user, PGProperty.PASSWORD.get(info), logger);
                final ProtocolConnectionImpl protoConnection = new ProtocolConnectionImpl(newStream, user, database, logger, connectTimeout);
                this.readStartupMessages(newStream, protoConnection, logger);
                HostStatus hostStatus = HostStatus.ConnectOK;
                if (targetServerType != HostRequirement.any) {
                    hostStatus = (this.isMaster(protoConnection, logger) ? HostStatus.Master : HostStatus.Slave);
                }
                GlobalHostStatusTracker.reportHostStatus(hostSpec, hostStatus);
                if (targetServerType.allowConnectingTo(hostStatus)) {
                    this.runInitialQueries(protoConnection, info, logger);
                    return protoConnection;
                }
                protoConnection.close();
                if (hostIter.hasNext()) {
                    continue;
                }
                throw new PSQLException(GT.tr("Could not find a server with specified targetServerType: {0}", targetServerType), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
            }
            catch (ConnectException cex) {
                GlobalHostStatusTracker.reportHostStatus(hostSpec, HostStatus.ConnectFail);
                if (hostIter.hasNext()) {
                    continue;
                }
                throw new PSQLException(GT.tr("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections."), PSQLState.CONNECTION_UNABLE_TO_CONNECT, cex);
            }
            catch (IOException ioe) {
                this.closeStream(newStream);
                GlobalHostStatusTracker.reportHostStatus(hostSpec, HostStatus.ConnectFail);
                if (hostIter.hasNext()) {
                    continue;
                }
                throw new PSQLException(GT.tr("The connection attempt failed."), PSQLState.CONNECTION_UNABLE_TO_CONNECT, ioe);
            }
            catch (SQLException se) {
                this.closeStream(newStream);
                if (hostIter.hasNext()) {
                    continue;
                }
                throw se;
            }
            break;
        }
        throw new PSQLException(GT.tr("The connection url is invalid."), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
    }
    
    private PGStream enableSSL(final PGStream pgStream, final boolean requireSSL, final Properties info, final Logger logger, final int connectTimeout) throws IOException, SQLException {
        if (logger.logDebug()) {
            logger.debug(" FE=> SSLRequest");
        }
        pgStream.SendInteger4(8);
        pgStream.SendInteger2(1234);
        pgStream.SendInteger2(5679);
        pgStream.flush();
        final int beresp = pgStream.ReceiveChar();
        switch (beresp) {
            case 69: {
                if (logger.logDebug()) {
                    logger.debug(" <=BE SSLError");
                }
                if (requireSSL) {
                    throw new PSQLException(GT.tr("The server does not support SSL."), PSQLState.CONNECTION_REJECTED);
                }
                pgStream.close();
                return new PGStream(pgStream.getHostSpec(), connectTimeout);
            }
            case 78: {
                if (logger.logDebug()) {
                    logger.debug(" <=BE SSLRefused");
                }
                if (requireSSL) {
                    throw new PSQLException(GT.tr("The server does not support SSL."), PSQLState.CONNECTION_REJECTED);
                }
                return pgStream;
            }
            case 83: {
                if (logger.logDebug()) {
                    logger.debug(" <=BE SSLOk");
                }
                AbstractJdbc4MakeSSL.convert(pgStream, info, logger);
                return pgStream;
            }
            default: {
                throw new PSQLException(GT.tr("An error occurred while setting up the SSL connection."), PSQLState.PROTOCOL_VIOLATION);
            }
        }
    }
    
    private void sendStartupPacket(final PGStream pgStream, final String user, final String database, final Logger logger) throws IOException {
        if (logger.logDebug()) {
            logger.debug(" FE=> StartupPacket(user=" + user + ",database=" + database + ")");
        }
        pgStream.SendInteger4(296);
        pgStream.SendInteger2(2);
        pgStream.SendInteger2(0);
        pgStream.Send(database.getBytes("UTF-8"), 64);
        pgStream.Send(user.getBytes("UTF-8"), 32);
        pgStream.Send(new byte[64]);
        pgStream.Send(new byte[64]);
        pgStream.Send(new byte[64]);
        pgStream.flush();
    }
    
    private void doAuthentication(final PGStream pgStream, final String user, final String password, final Logger logger) throws IOException, SQLException {
        while (true) {
            final int beresp = pgStream.ReceiveChar();
            switch (beresp) {
                case 69: {
                    final String errorMsg = pgStream.ReceiveString();
                    if (logger.logDebug()) {
                        logger.debug(" <=BE ErrorMessage(" + errorMsg + ")");
                    }
                    throw new PSQLException(GT.tr("Connection rejected: {0}.", errorMsg), PSQLState.CONNECTION_REJECTED);
                }
                case 82: {
                    final int areq = pgStream.ReceiveInteger4();
                    switch (areq) {
                        case 4: {
                            final byte[] salt = pgStream.Receive(2);
                            if (logger.logDebug()) {
                                logger.debug(" <=BE AuthenticationReqCrypt(salt='" + new String(salt, "US-ASCII") + "')");
                            }
                            if (password == null) {
                                throw new PSQLException(GT.tr("The server requested password-based authentication, but no password was provided."), PSQLState.CONNECTION_REJECTED);
                            }
                            final byte[] encodedResult = UnixCrypt.crypt(salt, password.getBytes("UTF-8"));
                            if (logger.logDebug()) {
                                logger.debug(" FE=> Password(crypt='" + new String(encodedResult, "US-ASCII") + "')");
                            }
                            pgStream.SendInteger4(4 + encodedResult.length + 1);
                            pgStream.Send(encodedResult);
                            pgStream.SendChar(0);
                            pgStream.flush();
                            continue;
                        }
                        case 5: {
                            final byte[] md5Salt = pgStream.Receive(4);
                            if (logger.logDebug()) {
                                logger.debug(" <=BE AuthenticationReqMD5(salt=" + Utils.toHexString(md5Salt) + ")");
                            }
                            if (password == null) {
                                throw new PSQLException(GT.tr("The server requested password-based authentication, but no password was provided."), PSQLState.CONNECTION_REJECTED);
                            }
                            final byte[] digest = MD5Digest.encode(user.getBytes("UTF-8"), password.getBytes("UTF-8"), md5Salt);
                            if (logger.logDebug()) {
                                logger.debug(" FE=> Password(md5digest=" + new String(digest, "US-ASCII") + ")");
                            }
                            pgStream.SendInteger4(4 + digest.length + 1);
                            pgStream.Send(digest);
                            pgStream.SendChar(0);
                            pgStream.flush();
                            continue;
                        }
                        case 3: {
                            if (logger.logDebug()) {
                                logger.debug(" <=BE AuthenticationReqPassword");
                            }
                            if (password == null) {
                                throw new PSQLException(GT.tr("The server requested password-based authentication, but no password was provided."), PSQLState.CONNECTION_REJECTED);
                            }
                            if (logger.logDebug()) {
                                logger.debug(" FE=> Password(password=<not shown>)");
                            }
                            final byte[] encodedPassword = password.getBytes("UTF-8");
                            pgStream.SendInteger4(4 + encodedPassword.length + 1);
                            pgStream.Send(encodedPassword);
                            pgStream.SendChar(0);
                            pgStream.flush();
                            continue;
                        }
                        case 0: {
                            if (logger.logDebug()) {
                                logger.debug(" <=BE AuthenticationOk");
                            }
                            return;
                        }
                        default: {
                            if (logger.logDebug()) {
                                logger.debug(" <=BE AuthenticationReq (unsupported type " + areq + ")");
                            }
                            throw new PSQLException(GT.tr("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", new Integer(areq)), PSQLState.CONNECTION_REJECTED);
                        }
                    }
                    continue;
                }
                default: {
                    throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
                }
            }
        }
    }
    
    private void readStartupMessages(final PGStream pgStream, final ProtocolConnectionImpl protoConnection, final Logger logger) throws IOException, SQLException {
        while (true) {
            final int beresp = pgStream.ReceiveChar();
            switch (beresp) {
                case 90: {
                    if (logger.logDebug()) {
                        logger.debug(" <=BE ReadyForQuery");
                    }
                }
                case 75: {
                    final int pid = pgStream.ReceiveInteger4();
                    final int ckey = pgStream.ReceiveInteger4();
                    if (logger.logDebug()) {
                        logger.debug(" <=BE BackendKeyData(pid=" + pid + ",ckey=" + ckey + ")");
                    }
                    protoConnection.setBackendKeyData(pid, ckey);
                    continue;
                }
                case 69: {
                    final String errorMsg = pgStream.ReceiveString();
                    if (logger.logDebug()) {
                        logger.debug(" <=BE ErrorResponse(" + errorMsg + ")");
                    }
                    throw new PSQLException(GT.tr("Backend start-up failed: {0}.", errorMsg), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
                }
                case 78: {
                    final String warnMsg = pgStream.ReceiveString();
                    if (logger.logDebug()) {
                        logger.debug(" <=BE NoticeResponse(" + warnMsg + ")");
                    }
                    protoConnection.addWarning(new SQLWarning(warnMsg));
                    continue;
                }
                default: {
                    throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
                }
            }
        }
    }
    
    private void runInitialQueries(final ProtocolConnectionImpl protoConnection, final Properties info, final Logger logger) throws SQLException, IOException {
        byte[][] results = SetupQueryRunner.run(protoConnection, "set datestyle = 'ISO'; select version(), case when pg_encoding_to_char(1) = 'SQL_ASCII' then 'UNKNOWN' else getdatabaseencoding() end", true);
        final String rawDbVersion = protoConnection.getEncoding().decode(results[0]);
        final StringTokenizer versionParts = new StringTokenizer(rawDbVersion);
        versionParts.nextToken();
        final String dbVersion = versionParts.nextToken();
        protoConnection.setServerVersion(dbVersion);
        if (dbVersion.compareTo("7.3") >= 0) {
            if (logger.logDebug()) {
                logger.debug("Switching to UTF8 client_encoding");
            }
            String sql = "begin; set autocommit = on; set client_encoding = 'UTF8'; ";
            if (dbVersion.compareTo("9.0") >= 0) {
                sql += "SET extra_float_digits=3; ";
            }
            else if (dbVersion.compareTo("7.4") >= 0) {
                sql += "SET extra_float_digits=2; ";
            }
            sql += "commit";
            SetupQueryRunner.run(protoConnection, sql, false);
            protoConnection.setEncoding(Encoding.getDatabaseEncoding("UTF8"));
        }
        else {
            final String charSet = PGProperty.CHARSET.get(info);
            final String dbEncoding = (results[1] == null) ? null : protoConnection.getEncoding().decode(results[1]);
            if (logger.logDebug()) {
                logger.debug("Specified charset:  " + charSet);
                logger.debug("Database encoding: " + dbEncoding);
            }
            if (charSet != null) {
                protoConnection.setEncoding(Encoding.getJVMEncoding(charSet));
            }
            else if (dbEncoding != null) {
                protoConnection.setEncoding(Encoding.getDatabaseEncoding(dbEncoding));
            }
            else {
                protoConnection.setEncoding(Encoding.defaultEncoding());
            }
        }
        if (logger.logDebug()) {
            logger.debug("Connection encoding (using JVM's nomenclature): " + protoConnection.getEncoding());
        }
        if (dbVersion.compareTo("8.1") >= 0) {
            results = SetupQueryRunner.run(protoConnection, "select current_setting('standard_conforming_strings')", true);
            final String value = protoConnection.getEncoding().decode(results[0]);
            protoConnection.setStandardConformingStrings(value.equalsIgnoreCase("on"));
        }
        else {
            protoConnection.setStandardConformingStrings(false);
        }
        final String appName = PGProperty.APPLICATION_NAME.get(info);
        if (appName != null && dbVersion.compareTo("9.0") >= 0) {
            final StringBuilder sb = new StringBuilder("SET application_name = '");
            Utils.escapeLiteral(sb, appName, protoConnection.getStandardConformingStrings());
            sb.append("'");
            SetupQueryRunner.run(protoConnection, sb.toString(), false);
        }
        final String currentSchema = PGProperty.CURRENT_SCHEMA.get(info);
        if (currentSchema != null) {
            final StringBuilder sb2 = new StringBuilder("SET search_path = '");
            Utils.escapeLiteral(sb2, appName, protoConnection.getStandardConformingStrings());
            sb2.append("'");
            SetupQueryRunner.run(protoConnection, sb2.toString(), false);
        }
    }
    
    private boolean isMaster(final ProtocolConnectionImpl protoConnection, final Logger logger) throws SQLException, IOException {
        final byte[][] results = SetupQueryRunner.run(protoConnection, "show transaction_read_only", true);
        final String value = protoConnection.getEncoding().decode(results[0]);
        return value.equalsIgnoreCase("off");
    }
}
