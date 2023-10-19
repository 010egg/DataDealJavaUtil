// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.sspi;

import java.io.IOException;
import java.sql.SQLException;
import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.Win32Exception;
import waffle.windows.auth.impl.WindowsCredentialsHandleImpl;
import org.postgresql.util.HostSpec;
import com.sun.jna.LastErrorException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import java.util.logging.Level;
import com.sun.jna.Platform;
import waffle.windows.auth.impl.WindowsSecurityContextImpl;
import waffle.windows.auth.IWindowsCredentialsHandle;
import org.postgresql.core.PGStream;
import java.util.logging.Logger;

public class SSPIClient implements ISSPIClient
{
    public static final String SSPI_DEFAULT_SPN_SERVICE_CLASS = "POSTGRES";
    private static final Logger LOGGER;
    private final PGStream pgStream;
    private final String spnServiceClass;
    private final boolean enableNegotiate;
    private IWindowsCredentialsHandle clientCredentials;
    private WindowsSecurityContextImpl sspiContext;
    private String targetName;
    
    public SSPIClient(final PGStream pgStream, String spnServiceClass, final boolean enableNegotiate) {
        this.pgStream = pgStream;
        if (spnServiceClass == null || spnServiceClass.isEmpty()) {
            spnServiceClass = "POSTGRES";
        }
        this.spnServiceClass = spnServiceClass;
        this.enableNegotiate = enableNegotiate;
    }
    
    @Override
    public boolean isSSPISupported() {
        try {
            if (!Platform.isWindows()) {
                SSPIClient.LOGGER.log(Level.FINE, "SSPI not supported: non-Windows host");
                return false;
            }
            Class.forName("waffle.windows.auth.impl.WindowsSecurityContextImpl");
            return true;
        }
        catch (NoClassDefFoundError ex) {
            SSPIClient.LOGGER.log(Level.WARNING, "SSPI unavailable (no Waffle/JNA libraries?)", ex);
            return false;
        }
        catch (ClassNotFoundException ex2) {
            SSPIClient.LOGGER.log(Level.WARNING, "SSPI unavailable (no Waffle/JNA libraries?)", ex2);
            return false;
        }
    }
    
    private String makeSPN() throws PSQLException {
        final HostSpec hs = this.pgStream.getHostSpec();
        try {
            return NTDSAPIWrapper.instance.DsMakeSpn(this.spnServiceClass, hs.getHost(), null, (short)0, null);
        }
        catch (LastErrorException ex) {
            throw new PSQLException("SSPI setup failed to determine SPN", PSQLState.CONNECTION_UNABLE_TO_CONNECT, (Throwable)ex);
        }
    }
    
    @Override
    public void startSSPI() throws SQLException, IOException {
        final String securityPackage = this.enableNegotiate ? "negotiate" : "kerberos";
        SSPIClient.LOGGER.log(Level.FINEST, "Beginning SSPI/Kerberos negotiation with SSPI package: {0}", securityPackage);
        try {
            try {
                (this.clientCredentials = WindowsCredentialsHandleImpl.getCurrent(securityPackage)).initialize();
            }
            catch (Win32Exception ex) {
                throw new PSQLException("Could not obtain local Windows credentials for SSPI", PSQLState.CONNECTION_UNABLE_TO_CONNECT, (Throwable)ex);
            }
            try {
                this.targetName = this.makeSPN();
                SSPIClient.LOGGER.log(Level.FINEST, "SSPI target name: {0}", this.targetName);
                (this.sspiContext = new WindowsSecurityContextImpl()).setPrincipalName(this.targetName);
                this.sspiContext.setCredentialsHandle(this.clientCredentials);
                this.sspiContext.setSecurityPackage(securityPackage);
                this.sspiContext.initialize((Sspi.CtxtHandle)null, (Sspi.SecBufferDesc)null, this.targetName);
            }
            catch (Win32Exception ex) {
                throw new PSQLException("Could not initialize SSPI security context", PSQLState.CONNECTION_UNABLE_TO_CONNECT, (Throwable)ex);
            }
            this.sendSSPIResponse(this.sspiContext.getToken());
            SSPIClient.LOGGER.log(Level.FINEST, "Sent first SSPI negotiation message");
        }
        catch (NoClassDefFoundError ex2) {
            throw new PSQLException("SSPI cannot be used, Waffle or its dependencies are missing from the classpath", PSQLState.NOT_IMPLEMENTED, ex2);
        }
    }
    
    @Override
    public void continueSSPI(final int msgLength) throws SQLException, IOException {
        if (this.sspiContext == null) {
            throw new IllegalStateException("Cannot continue SSPI authentication that we didn't begin");
        }
        SSPIClient.LOGGER.log(Level.FINEST, "Continuing SSPI negotiation");
        final byte[] receivedToken = this.pgStream.receive(msgLength);
        final Sspi.SecBufferDesc continueToken = new Sspi.SecBufferDesc(2, receivedToken);
        this.sspiContext.initialize(this.sspiContext.getHandle(), continueToken, this.targetName);
        final byte[] responseToken = this.sspiContext.getToken();
        if (responseToken.length > 0) {
            this.sendSSPIResponse(responseToken);
            SSPIClient.LOGGER.log(Level.FINEST, "Sent SSPI negotiation continuation message");
        }
        else {
            SSPIClient.LOGGER.log(Level.FINEST, "SSPI authentication complete, no reply required");
        }
    }
    
    private void sendSSPIResponse(final byte[] outToken) throws IOException {
        this.pgStream.sendChar(112);
        this.pgStream.sendInteger4(4 + outToken.length);
        this.pgStream.send(outToken);
        this.pgStream.flush();
    }
    
    @Override
    public void dispose() {
        if (this.sspiContext != null) {
            this.sspiContext.dispose();
            this.sspiContext = null;
        }
        if (this.clientCredentials != null) {
            this.clientCredentials.dispose();
            this.clientCredentials = null;
        }
    }
    
    static {
        LOGGER = Logger.getLogger(SSPIClient.class.getName());
    }
}
