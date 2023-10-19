// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;
import java.util.logging.SimpleFormatter;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.io.Writer;
import java.util.logging.Handler;

public class WriterHandler extends Handler
{
    private boolean doneHeader;
    private Writer writer;
    
    public WriterHandler(final Writer inWriter) {
        this.setLevel(Level.INFO);
        this.setFilter(null);
        this.setFormatter(new SimpleFormatter());
        this.writer = inWriter;
    }
    
    public WriterHandler(final Writer inWriter, final Formatter formatter) {
        this.setLevel(Level.INFO);
        this.setFilter(null);
        this.setFormatter(formatter);
        this.writer = inWriter;
    }
    
    @Override
    public synchronized void publish(final LogRecord record) {
        if (!this.isLoggable(record)) {
            return;
        }
        String msg;
        try {
            msg = this.getFormatter().format(record);
        }
        catch (Exception ex) {
            this.reportError(null, ex, 5);
            return;
        }
        try {
            if (!this.doneHeader) {
                this.writer.write(this.getFormatter().getHead(this));
                this.doneHeader = true;
            }
            this.writer.write(msg);
        }
        catch (Exception ex) {
            this.reportError(null, ex, 1);
        }
    }
    
    @Override
    public boolean isLoggable(final LogRecord record) {
        return this.writer != null && record != null && super.isLoggable(record);
    }
    
    @Override
    public synchronized void flush() {
        if (this.writer != null) {
            try {
                this.writer.flush();
            }
            catch (Exception ex) {
                this.reportError(null, ex, 2);
            }
        }
    }
    
    private synchronized void flushAndClose() throws SecurityException {
        if (this.writer != null) {
            try {
                if (!this.doneHeader) {
                    this.writer.write(this.getFormatter().getHead(this));
                    this.doneHeader = true;
                }
                this.writer.write(this.getFormatter().getTail(this));
                this.writer.flush();
                this.writer.close();
            }
            catch (Exception ex) {
                this.reportError(null, ex, 3);
            }
            this.writer = null;
        }
    }
    
    @Override
    public synchronized void close() throws SecurityException {
        this.flushAndClose();
    }
}
