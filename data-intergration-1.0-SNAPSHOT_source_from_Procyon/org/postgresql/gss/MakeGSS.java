// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.gss;

import java.util.Set;
import java.sql.SQLException;
import java.io.IOException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.security.PrivilegedAction;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import org.ietf.jgss.GSSCredential;
import javax.security.auth.Subject;
import java.security.AccessController;
import org.postgresql.core.Logger;
import org.postgresql.core.PGStream;

public class MakeGSS
{
    public static void authenticate(final PGStream pgStream, final String host, final String user, final String password, String jaasApplicationName, String kerberosServerName, final Logger logger, final boolean useSpnego) throws IOException, SQLException {
        if (logger.logDebug()) {
            logger.debug(" <=BE AuthenticationReqGSS");
        }
        Object result = null;
        if (jaasApplicationName == null) {
            jaasApplicationName = "pgjdbc";
        }
        if (kerberosServerName == null) {
            kerberosServerName = "postgres";
        }
        try {
            boolean performAuthentication = true;
            GSSCredential gssCredential = null;
            Subject sub = Subject.getSubject(AccessController.getContext());
            if (sub != null) {
                final Set<GSSCredential> gssCreds = sub.getPrivateCredentials(GSSCredential.class);
                if (gssCreds != null && gssCreds.size() > 0) {
                    gssCredential = gssCreds.iterator().next();
                    performAuthentication = false;
                }
            }
            if (performAuthentication) {
                final LoginContext lc = new LoginContext(jaasApplicationName, new GSSCallbackHandler(user, password));
                lc.login();
                sub = lc.getSubject();
            }
            final PrivilegedAction action = new GssAction(pgStream, gssCredential, host, user, password, kerberosServerName, logger, useSpnego);
            result = Subject.doAs(sub, (PrivilegedAction<Object>)action);
        }
        catch (Exception e) {
            throw new PSQLException(GT.tr("GSS Authentication failed"), PSQLState.CONNECTION_FAILURE, e);
        }
        if (result instanceof IOException) {
            throw (IOException)result;
        }
        if (result instanceof SQLException) {
            throw (SQLException)result;
        }
        if (result != null) {
            throw new PSQLException(GT.tr("GSS Authentication failed"), PSQLState.CONNECTION_FAILURE, (Throwable)result);
        }
    }
}
