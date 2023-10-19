// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

import ch.qos.logback.core.rolling.RolloverFailure;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;

public class FileUtil extends ContextAwareBase
{
    static final int BUF_SIZE = 32768;
    
    public FileUtil(final Context context) {
        this.setContext(context);
    }
    
    public static URL fileToURL(final File file) {
        try {
            return file.toURI().toURL();
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Unexpected exception on file [" + file + "]", e);
        }
    }
    
    public static boolean createMissingParentDirectories(final File file) {
        final File parent = file.getParentFile();
        if (parent == null) {
            return true;
        }
        parent.mkdirs();
        return parent.exists();
    }
    
    public String resourceAsString(final ClassLoader classLoader, final String resourceName) {
        final URL url = classLoader.getResource(resourceName);
        if (url == null) {
            this.addError("Failed to find resource [" + resourceName + "]");
            return null;
        }
        InputStreamReader isr = null;
        try {
            final URLConnection urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            isr = new InputStreamReader(urlConnection.getInputStream());
            final char[] buf = new char[128];
            final StringBuilder builder = new StringBuilder();
            int count = -1;
            while ((count = isr.read(buf, 0, buf.length)) != -1) {
                builder.append(buf, 0, count);
            }
            return builder.toString();
        }
        catch (IOException e) {
            this.addError("Failed to open " + resourceName, e);
        }
        finally {
            if (isr != null) {
                try {
                    isr.close();
                }
                catch (IOException ex) {}
            }
        }
        return null;
    }
    
    public void copy(final String src, final String destination) throws RolloverFailure {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(src));
            bos = new BufferedOutputStream(new FileOutputStream(destination));
            final byte[] inbuf = new byte[32768];
            int n;
            while ((n = bis.read(inbuf)) != -1) {
                bos.write(inbuf, 0, n);
            }
            bis.close();
            bis = null;
            bos.close();
            bos = null;
        }
        catch (IOException ioe) {
            final String msg = "Failed to copy [" + src + "] to [" + destination + "]";
            this.addError(msg, ioe);
            throw new RolloverFailure(msg);
        }
        finally {
            if (bis != null) {
                try {
                    bis.close();
                }
                catch (IOException ex) {}
            }
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (IOException ex2) {}
            }
        }
    }
}
