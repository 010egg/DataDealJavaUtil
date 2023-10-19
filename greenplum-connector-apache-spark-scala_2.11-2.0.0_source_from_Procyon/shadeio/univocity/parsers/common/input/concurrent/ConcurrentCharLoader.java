// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input.concurrent;

import shadeio.univocity.parsers.common.ArgumentUtils;
import shadeio.univocity.parsers.common.input.BomInput;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.ArrayBlockingQueue;

class ConcurrentCharLoader implements Runnable
{
    private final ArrayBlockingQueue<Object> buckets;
    private final CharBucket end;
    private final FixedInstancePool<CharBucket> instances;
    private Entry<CharBucket> currentBucket;
    private boolean finished;
    private boolean active;
    Reader reader;
    private Thread activeExecution;
    private Exception error;
    
    public ConcurrentCharLoader(final Reader reader, final int bucketSize, final int bucketQuantity) {
        this.finished = false;
        this.end = new CharBucket(-1);
        this.buckets = new ArrayBlockingQueue<Object>(bucketQuantity);
        this.reader = reader;
        this.instances = new FixedInstancePool<CharBucket>(bucketQuantity) {
            @Override
            protected CharBucket newInstance() {
                return new CharBucket(bucketSize);
            }
        };
        this.finished = false;
        this.active = true;
    }
    
    private int readBucket() throws IOException, InterruptedException {
        final Entry<CharBucket> bucket = this.instances.allocate();
        final int length = bucket.get().fill(this.reader);
        if (length != -1) {
            this.buckets.put(bucket);
        }
        else {
            this.instances.release(bucket);
        }
        return length;
    }
    
    @Override
    public void run() {
        try {
            try {
                while (this.active && this.readBucket() != -1) {}
            }
            finally {
                this.buckets.put(this.end);
            }
        }
        catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
        }
        catch (Exception e) {
            this.finished = true;
            this.setError(e);
        }
        finally {
            this.stopReading();
        }
    }
    
    private void setError(final Exception e) {
        if (this.active) {
            this.error = e;
        }
    }
    
    public synchronized CharBucket nextBucket() {
        if (this.activeExecution == null && !this.finished) {
            int length = -1;
            try {
                length = this.readBucket();
                if (length >= 0 && length <= 4) {
                    length = this.readBucket();
                }
            }
            catch (BomInput.BytesProcessedNotification e) {
                throw e;
            }
            catch (Exception e2) {
                this.setError(e2);
            }
            if (length != -1) {
                (this.activeExecution = new Thread(this, "unVocity-parsers input reading thread")).start();
            }
            else {
                this.finished = true;
                try {
                    this.buckets.put(this.end);
                }
                catch (InterruptedException e3) {
                    Thread.currentThread().interrupt();
                }
                finally {
                    this.stopReading();
                }
            }
        }
        try {
            if (this.finished && this.buckets.size() <= 1) {
                return this.end;
            }
            if (this.currentBucket != null) {
                this.instances.release(this.currentBucket);
            }
            final Object element = this.buckets.take();
            if (element == this.end) {
                this.finished = true;
                return this.end;
            }
            this.currentBucket = (Entry<CharBucket>)element;
            return this.currentBucket.get();
        }
        catch (InterruptedException e4) {
            Thread.currentThread().interrupt();
            this.finished = true;
            return this.end;
        }
    }
    
    public void stopReading() {
        this.active = false;
        try {
            this.reader.close();
        }
        catch (IOException e) {
            throw new IllegalStateException("Error closing input", e);
        }
        finally {
            try {
                if (this.activeExecution != null) {
                    this.activeExecution.interrupt();
                }
            }
            catch (Throwable ex) {
                throw new IllegalStateException("Error stopping input reader thread", ex);
            }
        }
    }
    
    void reportError() {
        if (this.error != null) {
            ArgumentUtils.throwUnchecked(this.error);
        }
    }
}
