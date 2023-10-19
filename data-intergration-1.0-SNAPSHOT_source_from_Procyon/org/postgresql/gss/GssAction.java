// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.gss;

import org.ietf.jgss.GSSContext;
import java.io.IOException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSCredential;
import org.postgresql.core.Logger;
import org.postgresql.core.PGStream;
import java.security.PrivilegedAction;

class GssAction implements PrivilegedAction
{
    private final PGStream pgStream;
    private final String host;
    private final String user;
    private final String password;
    private final String kerberosServerName;
    private final Logger logger;
    private final boolean useSpnego;
    private final GSSCredential clientCredentials;
    
    public GssAction(final PGStream pgStream, final GSSCredential clientCredentials, final String host, final String user, final String password, final String kerberosServerName, final Logger logger, final boolean useSpnego) {
        this.pgStream = pgStream;
        this.clientCredentials = clientCredentials;
        this.host = host;
        this.user = user;
        this.password = password;
        this.kerberosServerName = kerberosServerName;
        this.logger = logger;
        this.useSpnego = useSpnego;
    }
    
    private static boolean hasSpnegoSupport(final GSSManager manager) throws GSSException {
        final Oid spnego = new Oid("1.3.6.1.5.5.2");
        final Oid[] mechs = manager.getMechs();
        for (int i = 0; i < mechs.length; ++i) {
            if (mechs[i].equals(spnego)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Object run() {
        try {
            final GSSManager manager = GSSManager.getInstance();
            GSSCredential clientCreds = null;
            final Oid[] desiredMechs = { null };
            if (this.clientCredentials == null) {
                if (this.useSpnego && hasSpnegoSupport(manager)) {
                    desiredMechs[0] = new Oid("1.3.6.1.5.5.2");
                }
                else {
                    desiredMechs[0] = new Oid("1.2.840.113554.1.2.2");
                }
                final GSSName clientName = manager.createName(this.user, GSSName.NT_USER_NAME);
                clientCreds = manager.createCredential(clientName, 28800, desiredMechs, 1);
            }
            else {
                desiredMechs[0] = new Oid("1.2.840.113554.1.2.2");
                clientCreds = this.clientCredentials;
            }
            final GSSName serverName = manager.createName(this.kerberosServerName + "@" + this.host, GSSName.NT_HOSTBASED_SERVICE);
            final GSSContext secContext = manager.createContext(serverName, desiredMechs[0], clientCreds, 0);
            secContext.requestMutualAuth(true);
            byte[] inToken = new byte[0];
            byte[] outToken = null;
            boolean established = false;
            while (!established) {
                outToken = secContext.initSecContext(inToken, 0, inToken.length);
                if (outToken != null) {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" FE=> Password(GSS Authentication Token)");
                    }
                    this.pgStream.SendChar(112);
                    this.pgStream.SendInteger4(4 + outToken.length);
                    this.pgStream.Send(outToken);
                    this.pgStream.flush();
                }
                if (!secContext.isEstablished()) {
                    final int response = this.pgStream.ReceiveChar();
                    if (response == 69) {
                        final int l_elen = this.pgStream.ReceiveInteger4();
                        final ServerErrorMessage l_errorMsg = new ServerErrorMessage(this.pgStream.ReceiveString(l_elen - 4), this.logger.getLogLevel());
                        if (this.logger.logDebug()) {
                            this.logger.debug(" <=BE ErrorMessage(" + l_errorMsg + ")");
                        }
                        return new PSQLException(l_errorMsg);
                    }
                    if (response != 82) {
                        return new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
                    }
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE AuthenticationGSSContinue");
                    }
                    final int len = this.pgStream.ReceiveInteger4();
                    final int type = this.pgStream.ReceiveInteger4();
                    inToken = this.pgStream.Receive(len - 8);
                }
                else {
                    established = true;
                }
            }
        }
        catch (IOException e) {
            return e;
        }
        catch (GSSException gsse) {
            return new PSQLException(GT.tr("GSS Authentication failed"), PSQLState.CONNECTION_FAILURE, gsse);
        }
        return null;
    }
}
