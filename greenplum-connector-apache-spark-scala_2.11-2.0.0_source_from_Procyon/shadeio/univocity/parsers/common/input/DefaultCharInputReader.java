// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input;

import java.io.IOException;
import java.io.Reader;

public class DefaultCharInputReader extends AbstractCharInputReader
{
    private Reader reader;
    
    public DefaultCharInputReader(final char normalizedLineSeparator, final int bufferSize, final int whitespaceRangeStart) {
        super(normalizedLineSeparator, whitespaceRangeStart);
        super.buffer = new char[bufferSize];
    }
    
    public DefaultCharInputReader(final char[] lineSeparator, final char normalizedLineSeparator, final int bufferSize, final int whitespaceRangeStart) {
        super(lineSeparator, normalizedLineSeparator, whitespaceRangeStart);
        super.buffer = new char[bufferSize];
    }
    
    @Override
    public void stop() {
        try {
            if (this.reader != null) {
                this.reader.close();
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Error closing input", e);
        }
    }
    
    @Override
    protected void setReader(final Reader reader) {
        this.reader = reader;
    }
    
    public void reloadBuffer() {
        try {
            super.length = this.reader.read(this.buffer, 0, this.buffer.length);
        }
        catch (IOException e) {
            throw new IllegalStateException("Error reading from input", e);
        }
        catch (BomInput.BytesProcessedNotification notification) {
            this.stop();
            this.unwrapInputStream(notification);
        }
    }
}
