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
import com.sun.jna.Platform;
import waffle.windows.auth.impl.WindowsSecurityContextImpl;
import waffle.windows.auth.IWindowsCredentialsHandle;
import org.postgresql.core.PGStream;
import org.postgresql.core.Logger;

public class SSPIClient
{
    public static String SSPI_DEFAULT_SPN_SERVICE_CLASS;
    private final Logger logger;
    private final PGStream pgStream;
    private final String spnServiceClass;
    private final boolean enableNegotiate;
    private IWindowsCredentialsHandle clientCredentials;
    private WindowsSecurityContextImpl sspiContext;
    private String targetName;
    
    public SSPIClient(final PGStream pgStream, String spnServiceClass, final boolean enableNegotiate, final Logger logger) {
        this.logger = logger;
        this.pgStream = pgStream;
        final String realServiceClass = spnServiceClass;
        if (spnServiceClass != null && spnServiceClass.isEmpty()) {
            spnServiceClass = null;
        }
        if (spnServiceClass == null) {
            spnServiceClass = SSPIClient.SSPI_DEFAULT_SPN_SERVICE_CLASS;
        }
        this.spnServiceClass = spnServiceClass;
        this.enableNegotiate = enableNegotiate;
    }
    
    public boolean isSSPISupported() {
        try {
            if (!Platform.isWindows()) {
                this.logger.debug("SSPI not supported: non-Windows host");
                return false;
            }
            Class.forName("waffle.windows.auth.impl.WindowsSecurityContextImpl");
            return true;
        }
        catch (NoClassDefFoundError ex) {
            if (this.logger.logDebug()) {
                this.logger.debug("SSPI unavailable (no Waffle/JNA libraries?)", ex);
            }
            return false;
        }
        catch (ClassNotFoundException ex2) {
            if (this.logger.logDebug()) {
                this.logger.debug("SSPI unavailable (no Waffle/JNA libraries?)", ex2);
            }
            return false;
        }
    }
    
    private String makeSPN() throws PSQLException {
        final HostSpec hs = this.pgStream.getHostSpec();
        try {
            return NTDSAPIWrapper.instance.DsMakeSpn(this.spnServiceClass, hs.getHost(), null, (short)hs.getPort(), null);
        }
        catch (LastErrorException ex) {
            throw new PSQLException("SSPI setup failed to determine SPN", PSQLState.CONNECTION_UNABLE_TO_CONNECT, (Throwable)ex);
        }
    }
    
    public void startSSPI() throws SQLException, IOException {
        final String securityPackage = this.enableNegotiate ? "negotiate" : "kerberos";
        this.logger.debug("Beginning SSPI/Kerberos negotiation with SSPI package: " + securityPackage);
        try {
            try {
                (this.clientCredentials = WindowsCredentialsHandleImpl.getCurrent(securityPackage)).initialize();
            }
            catch (Win32Exception ex) {
                throw new PSQLException("Could not obtain local Windows credentials for SSPI", PSQLState.CONNECTION_UNABLE_TO_CONNECT, (Throwable)ex);
            }
            try {
                this.targetName = this.makeSPN();
                if (this.logger.logDebug()) {
                    this.logger.debug("SSPI target name: " + this.targetName);
                }
                (this.sspiContext = new WindowsSecurityContextImpl()).setPrincipalName(this.targetName);
                this.sspiContext.setCredentialsHandle(this.clientCredentials.getHandle());
                this.sspiContext.setSecurityPackage(securityPackage);
                this.sspiContext.initialize((Sspi.CtxtHandle)null, (Sspi.SecBufferDesc)null, this.targetName);
            }
            catch (Win32Exception ex) {
                throw new PSQLException("Could not initialize SSPI security context", PSQLState.CONNECTION_UNABLE_TO_CONNECT, (Throwable)ex);
            }
            this.sendSSPIResponse(this.sspiContext.getToken());
            this.logger.debug("Sent first SSPI negotiation message");
        }
        catch (NoClassDefFoundError ex2) {
            throw new PSQLException("SSPI cannot be used, Waffle or its dependencies are missing from the classpath", PSQLState.NOT_IMPLEMENTED, ex2);
        }
    }
    
    public void continueSSPI(final int msgLength) throws SQLException, IOException {
        if (this.sspiContext == null) {
            throw new IllegalStateException("Cannot continue SSPI authentication that we didn't begin");
        }
        this.logger.debug("Continuing SSPI negotiation");
        final byte[] receivedToken = this.pgStream.Receive(msgLength);
        final Sspi.SecBufferDesc continueToken = new Sspi.SecBufferDesc(2, receivedToken);
        this.sspiContext.initialize(this.sspiContext.getHandle(), continueToken, this.targetName);
        final byte[] responseToken = this.sspiContext.getToken();
        if (responseToken.length > 0) {
            this.sendSSPIResponse(responseToken);
            this.logger.debug("Sent SSPI negotiation continuation message");
        }
        else {
            this.logger.debug("SSPI authentication complete, no reply required");
        }
    }
    
    private void sendSSPIResponse(final byte[] outToken) throws IOException {
        this.pgStream.SendChar(112);
        this.pgStream.SendInteger4(4 + outToken.length);
        this.pgStream.Send(outToken);
        this.pgStream.flush();
    }
    
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
        SSPIClient.SSPI_DEFAULT_SPN_SERVICE_CLASS = "POSTGRES";
    }
}
