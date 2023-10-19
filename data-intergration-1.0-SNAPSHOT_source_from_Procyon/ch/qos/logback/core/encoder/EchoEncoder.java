// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.encoder;

import java.io.OutputStream;
import java.io.IOException;
import ch.qos.logback.core.CoreConstants;

public class EchoEncoder<E> extends EncoderBase<E>
{
    String fileHeader;
    String fileFooter;
    
    @Override
    public void doEncode(final E event) throws IOException {
        final String val = event + CoreConstants.LINE_SEPARATOR;
        this.outputStream.write(val.getBytes());
        this.outputStream.flush();
    }
    
    @Override
    public void close() throws IOException {
        if (this.fileFooter == null) {
            return;
        }
        this.outputStream.write(this.fileFooter.getBytes());
    }
    
    @Override
    public void init(final OutputStream os) throws IOException {
        super.init(os);
        if (this.fileHeader == null) {
            return;
        }
        this.outputStream.write(this.fileHeader.getBytes());
    }
}
