// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input.concurrent;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

class CharBucket
{
    final char[] data;
    int length;
    
    public CharBucket(final int bucketSize) {
        this.length = -1;
        if (bucketSize > 0) {
            this.data = new char[bucketSize];
        }
        else {
            this.data = new char[0];
        }
    }
    
    public CharBucket(final int bucketSize, final char fillWith) {
        this(bucketSize);
        if (bucketSize > 0) {
            Arrays.fill(this.data, fillWith);
        }
    }
    
    public int fill(final Reader reader) throws IOException {
        return this.length = reader.read(this.data, 0, this.data.length);
    }
    
    public boolean isEmpty() {
        return this.length <= 0;
    }
}
