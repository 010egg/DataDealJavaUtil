// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input.concurrent;

import shadeio.univocity.parsers.common.input.BomInput;
import java.io.Reader;
import shadeio.univocity.parsers.common.input.AbstractCharInputReader;

public class ConcurrentCharInputReader extends AbstractCharInputReader
{
    private ConcurrentCharLoader bucketLoader;
    private final int bucketSize;
    private final int bucketQuantity;
    private boolean unwrapping;
    
    public ConcurrentCharInputReader(final char normalizedLineSeparator, final int bucketSize, final int bucketQuantity, final int whitespaceRangeStart) {
        super(normalizedLineSeparator, whitespaceRangeStart);
        this.unwrapping = false;
        this.bucketSize = bucketSize;
        this.bucketQuantity = bucketQuantity;
    }
    
    public ConcurrentCharInputReader(final char[] lineSeparator, final char normalizedLineSeparator, final int bucketSize, final int bucketQuantity, final int whitespaceRangeStart) {
        super(lineSeparator, normalizedLineSeparator, whitespaceRangeStart);
        this.unwrapping = false;
        this.bucketSize = bucketSize;
        this.bucketQuantity = bucketQuantity;
    }
    
    @Override
    public void stop() {
        if (!this.unwrapping && this.bucketLoader != null) {
            this.bucketLoader.stopReading();
            this.bucketLoader.reportError();
        }
    }
    
    @Override
    protected void setReader(final Reader reader) {
        if (!this.unwrapping) {
            this.stop();
            (this.bucketLoader = new ConcurrentCharLoader(reader, this.bucketSize, this.bucketQuantity)).reportError();
        }
        else {
            this.bucketLoader.reader = reader;
        }
        this.unwrapping = false;
    }
    
    @Override
    protected void reloadBuffer() {
        try {
            final CharBucket currentBucket = this.bucketLoader.nextBucket();
            this.bucketLoader.reportError();
            super.buffer = currentBucket.data;
            super.length = currentBucket.length;
        }
        catch (BomInput.BytesProcessedNotification e) {
            this.unwrapping = true;
            this.unwrapInputStream(e);
        }
    }
}
