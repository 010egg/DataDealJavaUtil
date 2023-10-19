// 
// Decompiled by Procyon v0.5.36
// 

package javax.xml.transform.stream;

import java.io.File;
import java.io.Writer;
import java.io.OutputStream;
import javax.xml.transform.Result;

public class StreamResult implements Result
{
    public static final String FEATURE = "http://javax.xml.transform.stream.StreamResult/feature";
    private String systemId;
    private OutputStream outputStream;
    private Writer writer;
    
    public StreamResult() {
    }
    
    public StreamResult(final OutputStream outputStream) {
        this.setOutputStream(outputStream);
    }
    
    public StreamResult(final Writer writer) {
        this.setWriter(writer);
    }
    
    public StreamResult(final String systemId) {
        this.systemId = systemId;
    }
    
    public StreamResult(final File systemId) {
        this.setSystemId(systemId);
    }
    
    public void setOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    
    public OutputStream getOutputStream() {
        return this.outputStream;
    }
    
    public void setWriter(final Writer writer) {
        this.writer = writer;
    }
    
    public Writer getWriter() {
        return this.writer;
    }
    
    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }
    
    public void setSystemId(final File file) {
        String s = file.getAbsolutePath();
        if (File.separatorChar != '/') {
            s = s.replace(File.separatorChar, '/');
        }
        if (s.startsWith("/")) {
            this.systemId = "file://" + s;
        }
        else {
            this.systemId = "file:///" + s;
        }
    }
    
    public String getSystemId() {
        return this.systemId;
    }
}
