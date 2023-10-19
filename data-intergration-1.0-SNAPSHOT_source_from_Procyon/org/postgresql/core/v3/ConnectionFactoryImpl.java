// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import org.postgresql.core.SetupQueryRunner;
import org.postgresql.core.Encoding;
import java.sql.SQLWarning;
import org.postgresql.util.PSQLWarning;
import org.postgresql.gss.MakeGSS;
import org.postgresql.sspi.SSPIClient;
import org.postgresql.util.MD5Digest;
import org.postgresql.util.UnixCrypt;
import org.postgresql.util.ServerErrorMessage;
import org.postgresql.ssl.jdbc4.AbstractJdbc4MakeSSL;
import java.util.TimeZone;
import java.util.List;
import java.util.Iterator;
import org.postgresql.hostchooser.HostChooser;
import java.sql.SQLException;
import java.io.IOException;
import java.net.ConnectException;
import org.postgresql.hostchooser.GlobalHostStatusTracker;
import org.postgresql.hostchooser.HostStatus;
import org.postgresql.core.Utils;
import java.util.ArrayList;
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
    private static final int AUTH_REQ_GSS = 7;
    private static final int AUTH_REQ_GSS_CONTINUE = 8;
    private static final int AUTH_REQ_SSPI = 9;
    
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
        final int connectTimeout = PGProperty.CONNECT_TIMEOUT.getInt(info) * 1000;
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
                logger.debug("Trying to establish a protocol version 3 connection to " + hostSpec);
            }
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
                final int receiveBufferSize = PGProperty.RECEIVE_BUFFER_SIZE.getInt(info);
                if (receiveBufferSize > -1) {
                    if (receiveBufferSize > 0) {
                        newStream.getSocket().setReceiveBufferSize(receiveBufferSize);
                    }
                    else {
                        logger.info("Ignore invalid value for receiveBufferSize: " + receiveBufferSize);
                    }
                }
                final int sendBufferSize = PGProperty.SEND_BUFFER_SIZE.getInt(info);
                if (sendBufferSize > -1) {
                    if (sendBufferSize > 0) {
                        newStream.getSocket().setSendBufferSize(sendBufferSize);
                    }
                    else {
                        logger.info("Ignore invalid value for sendBufferSize: " + sendBufferSize);
                    }
                }
                logger.info("Receive Buffer Size is " + newStream.getSocket().getReceiveBufferSize());
                logger.info("Send Buffer Size is " + newStream.getSocket().getSendBufferSize());
                final List<String[]> paramList = new ArrayList<String[]>();
                paramList.add(new String[] { "user", user });
                paramList.add(new String[] { "database", database });
                paramList.add(new String[] { "client_encoding", "UTF8" });
                paramList.add(new String[] { "DateStyle", "ISO" });
                paramList.add(new String[] { "TimeZone", this.createPostgresTimeZone() });
                final String assumeMinServerVersion = PGProperty.ASSUME_MIN_SERVER_VERSION.get(info);
                if (Utils.parseServerVersionStr(assumeMinServerVersion) >= 90000) {
                    paramList.add(new String[] { "extra_float_digits", "3" });
                    final String appName = PGProperty.APPLICATION_NAME.get(info);
                    if (appName != null) {
                        paramList.add(new String[] { "application_name", appName });
                    }
                }
                else {
                    paramList.add(new String[] { "extra_float_digits", "2" });
                }
                final String currentSchema = PGProperty.CURRENT_SCHEMA.get(info);
                if (currentSchema != null) {
                    paramList.add(new String[] { "search_path", currentSchema });
                }
                final String[][] params = paramList.toArray(new String[0][]);
                this.sendStartupPacket(newStream, params, logger);
                this.doAuthentication(newStream, hostSpec.getHost(), user, info, logger);
                final ProtocolConnectionImpl protoConnection = new ProtocolConnectionImpl(newStream, user, database, info, logger, connectTimeout);
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
            catch (UnsupportedProtocolException upe) {
                if (logger.logDebug()) {
                    logger.debug("Protocol not supported, abandoning connection.");
                }
                this.closeStream(newStream);
                return null;
            }
            catch (ConnectException cex) {
                GlobalHostStatusTracker.reportHostStatus(hostSpec, HostStatus.ConnectFail);
                if (hostIter.hasNext()) {
                    continue;
                }
                throw new PSQLException(GT.tr("Connection to {0} refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", hostSpec), PSQLState.CONNECTION_UNABLE_TO_CONNECT, cex);
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
    
    private String createPostgresTimeZone() {
        final String tz = TimeZone.getDefault().getID();
        if (tz.length() <= 3 || !tz.startsWith("GMT")) {
            return tz;
        }
        final char sign = tz.charAt(3);
        String start;
        if (sign == '+') {
            start = "GMT-";
        }
        else {
            if (sign != '-') {
                return tz;
            }
            start = "GMT+";
        }
        return start + tz.substring(4);
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
    
    private void sendStartupPacket(final PGStream pgStream, final String[][] params, final Logger logger) throws IOException {
        if (logger.logDebug()) {
            final StringBuilder details = new StringBuilder();
            for (int i = 0; i < params.length; ++i) {
                if (i != 0) {
                    details.append(", ");
                }
                details.append(params[i][0]);
                details.append("=");
                details.append(params[i][1]);
            }
            logger.debug(" FE=> StartupPacket(" + (Object)details + ")");
        }
        int length = 8;
        final byte[][] encodedParams = new byte[params.length * 2][];
        for (int j = 0; j < params.length; ++j) {
            encodedParams[j * 2] = params[j][0].getBytes("UTF-8");
            encodedParams[j * 2 + 1] = params[j][1].getBytes("UTF-8");
            length += encodedParams[j * 2].length + 1 + encodedParams[j * 2 + 1].length + 1;
        }
        ++length;
        pgStream.SendInteger4(length);
        pgStream.SendInteger2(3);
        pgStream.SendInteger2(0);
        for (int j = 0; j < encodedParams.length; ++j) {
            pgStream.Send(encodedParams[j]);
            pgStream.SendChar(0);
        }
        pgStream.SendChar(0);
        pgStream.flush();
    }
    
    private void doAuthentication(final PGStream pgStream, final String host, final String user, final Properties info, final Logger logger) throws IOException, SQLException {
        final String password = PGProperty.PASSWORD.get(info);
        SSPIClient sspiClient = null;
        try {
            while (true) {
                final int beresp = pgStream.ReceiveChar();
                switch (beresp) {
                    case 69: {
                        final int l_elen = pgStream.ReceiveInteger4();
                        if (l_elen > 30000) {
                            throw new UnsupportedProtocolException();
                        }
                        final ServerErrorMessage errorMsg = new ServerErrorMessage(pgStream.ReceiveString(l_elen - 4), logger.getLogLevel());
                        if (logger.logDebug()) {
                            logger.debug(" <=BE ErrorMessage(" + errorMsg + ")");
                        }
                        throw new PSQLException(errorMsg);
                    }
                    case 82: {
                        final int l_msgLen = pgStream.ReceiveInteger4();
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
                                pgStream.SendChar(112);
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
                                pgStream.SendChar(112);
                                pgStream.SendInteger4(4 + digest.length + 1);
                                pgStream.Send(digest);
                                pgStream.SendChar(0);
                                pgStream.flush();
                                continue;
                            }
                            case 3: {
                                if (logger.logDebug()) {
                                    logger.debug(" <=BE AuthenticationReqPassword");
                                    logger.debug(" FE=> Password(password=<not shown>)");
                                }
                                if (password == null) {
                                    throw new PSQLException(GT.tr("The server requested password-based authentication, but no password was provided."), PSQLState.CONNECTION_REJECTED);
                                }
                                final byte[] encodedPassword = password.getBytes("UTF-8");
                                pgStream.SendChar(112);
                                pgStream.SendInteger4(4 + encodedPassword.length + 1);
                                pgStream.Send(encodedPassword);
                                pgStream.SendChar(0);
                                pgStream.flush();
                                continue;
                            }
                            case 7:
                            case 9: {
                                final String gsslib = PGProperty.GSS_LIB.get(info);
                                final boolean usespnego = PGProperty.USE_SPNEGO.getBoolean(info);
                                boolean useSSPI = false;
                                if (gsslib.equals("gssapi")) {
                                    logger.debug("Using JSSE GSSAPI, param gsslib=gssapi");
                                }
                                else if (areq == 7 && !gsslib.equals("sspi")) {
                                    logger.debug("Using JSSE GSSAPI, gssapi requested by server and gsslib=sspi not forced");
                                }
                                else {
                                    sspiClient = new SSPIClient(pgStream, PGProperty.SSPI_SERVICE_CLASS.get(info), areq == 9 || (areq == 7 && usespnego), logger);
                                    useSSPI = sspiClient.isSSPISupported();
                                    logger.debug("SSPI support detected: " + useSSPI);
                                    if (!useSSPI) {
                                        sspiClient = null;
                                        if (gsslib.equals("sspi")) {
                                            throw new PSQLException("SSPI forced with gsslib=sspi, but SSPI not available; set loglevel=2 for details", PSQLState.CONNECTION_UNABLE_TO_CONNECT);
                                        }
                                    }
                                    logger.debug("Using SSPI: " + useSSPI + ", gsslib=" + gsslib + " and SSPI support detected");
                                }
                                if (useSSPI) {
                                    sspiClient.startSSPI();
                                    continue;
                                }
                                MakeGSS.authenticate(pgStream, host, user, password, PGProperty.JAAS_APPLICATION_NAME.get(info), PGProperty.KERBEROS_SERVER_NAME.get(info), logger, usespnego);
                                continue;
                            }
                            case 8: {
                                sspiClient.continueSSPI(l_msgLen - 8);
                                continue;
                            }
                            case 0: {
                                if (logger.logDebug()) {
                                    logger.debug(" <=BE AuthenticationOk");
                                }
                                break;
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
        finally {
            if (sspiClient != null) {
                try {
                    sspiClient.dispose();
                }
                catch (RuntimeException ex) {
                    logger.log("Unexpected error during SSPI context disposal", ex);
                }
            }
        }
    }
    
    private void readStartupMessages(final PGStream pgStream, final ProtocolConnectionImpl protoConnection, final Logger logger) throws IOException, SQLException {
        while (true) {
            final int beresp = pgStream.ReceiveChar();
            switch (beresp) {
                case 90: {
                    if (pgStream.ReceiveInteger4() != 5) {
                        throw new IOException("unexpected length of ReadyForQuery packet");
                    }
                    final char tStatus = (char)pgStream.ReceiveChar();
                    if (logger.logDebug()) {
                        logger.debug(" <=BE ReadyForQuery(" + tStatus + ")");
                    }
                    switch (tStatus) {
                        case 'I': {
                            protoConnection.setTransactionState(0);
                            break;
                        }
                        case 'T': {
                            protoConnection.setTransactionState(1);
                            break;
                        }
                        case 'E': {
                            protoConnection.setTransactionState(2);
                            break;
                        }
                    }
                }
                case 75: {
                    final int l_msgLen = pgStream.ReceiveInteger4();
                    if (l_msgLen != 12) {
                        throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
                    }
                    final int pid = pgStream.ReceiveInteger4();
                    final int ckey = pgStream.ReceiveInteger4();
                    if (logger.logDebug()) {
                        logger.debug(" <=BE BackendKeyData(pid=" + pid + ",ckey=" + ckey + ")");
                    }
                    protoConnection.setBackendKeyData(pid, ckey);
                    continue;
                }
                case 69: {
                    final int l_elen = pgStream.ReceiveInteger4();
                    final ServerErrorMessage l_errorMsg = new ServerErrorMessage(pgStream.ReceiveString(l_elen - 4), logger.getLogLevel());
                    if (logger.logDebug()) {
                        logger.debug(" <=BE ErrorMessage(" + l_errorMsg + ")");
                    }
                    throw new PSQLException(l_errorMsg);
                }
                case 78: {
                    final int l_nlen = pgStream.ReceiveInteger4();
                    final ServerErrorMessage l_warnMsg = new ServerErrorMessage(pgStream.ReceiveString(l_nlen - 4), logger.getLogLevel());
                    if (logger.logDebug()) {
                        logger.debug(" <=BE NoticeResponse(" + l_warnMsg + ")");
                    }
                    protoConnection.addWarning(new PSQLWarning(l_warnMsg));
                    continue;
                }
                case 83: {
                    final int l_len = pgStream.ReceiveInteger4();
                    final String name = pgStream.ReceiveString();
                    final String value = pgStream.ReceiveString();
                    if (logger.logDebug()) {
                        logger.debug(" <=BE ParameterStatus(" + name + " = " + value + ")");
                    }
                    if (name.equals("server_version_num")) {
                        protoConnection.setServerVersionNum(Integer.parseInt(value));
                    }
                    if (name.equals("server_version")) {
                        protoConnection.setServerVersion(value);
                        continue;
                    }
                    if (name.equals("client_encoding")) {
                        if (!value.equals("UTF8")) {
                            throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
                        }
                        pgStream.setEncoding(Encoding.getDatabaseEncoding("UTF8"));
                        continue;
                    }
                    else if (name.equals("standard_conforming_strings")) {
                        if (value.equals("on")) {
                            protoConnection.setStandardConformingStrings(true);
                            continue;
                        }
                        if (value.equals("off")) {
                            protoConnection.setStandardConformingStrings(false);
                            continue;
                        }
                        throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
                    }
                    else {
                        if (!name.equals("integer_datetimes")) {
                            continue;
                        }
                        if (value.equals("on")) {
                            protoConnection.setIntegerDateTimes(true);
                            continue;
                        }
                        if (value.equals("off")) {
                            protoConnection.setIntegerDateTimes(false);
                            continue;
                        }
                        throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
                    }
                    break;
                }
                default: {
                    if (logger.logDebug()) {
                        logger.debug("invalid message type=" + (char)beresp);
                    }
                    throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
                }
            }
        }
    }
    
    private void runInitialQueries(final ProtocolConnection protoConnection, final Properties info, final Logger logger) throws SQLException {
        final String assumeMinServerVersion = PGProperty.ASSUME_MIN_SERVER_VERSION.get(info);
        if (Utils.parseServerVersionStr(assumeMinServerVersion) >= 90000) {
            return;
        }
        final int dbVersion = protoConnection.getServerVersionNum();
        if (dbVersion >= 90000) {
            SetupQueryRunner.run(protoConnection, "SET extra_float_digits = 3", false);
        }
        final String appName = PGProperty.APPLICATION_NAME.get(info);
        if (appName != null && dbVersion >= 90000) {
            final StringBuilder sql = new StringBuilder();
            sql.append("SET application_name = '");
            Utils.escapeLiteral(sql, appName, protoConnection.getStandardConformingStrings());
            sql.append("'");
            SetupQueryRunner.run(protoConnection, sql.toString(), false);
        }
    }
    
    private boolean isMaster(final ProtocolConnectionImpl protoConnection, final Logger logger) throws SQLException, IOException {
        final byte[][] results = SetupQueryRunner.run(protoConnection, "show transaction_read_only", true);
        final String value = protoConnection.getEncoding().decode(results[0]);
        return value.equalsIgnoreCase("off");
    }
    
    private static class UnsupportedProtocolException extends IOException
    {
    }
}
