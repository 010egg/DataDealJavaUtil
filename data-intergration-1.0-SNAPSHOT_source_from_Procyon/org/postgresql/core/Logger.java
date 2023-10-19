// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.io.PrintWriter;
import java.util.Date;
import java.sql.DriverManager;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;

public final class Logger
{
    private final SimpleDateFormat dateFormat;
    private final FieldPosition dummyPosition;
    private final StringBuffer buffer;
    private final String connectionIDString;
    private int level;
    
    public Logger() {
        this.dateFormat = new SimpleDateFormat("HH:mm:ss.SSS ");
        this.dummyPosition = new FieldPosition(0);
        this.buffer = new StringBuffer();
        this.level = 0;
        this.connectionIDString = "(driver) ";
    }
    
    public Logger(final int connectionID) {
        this.dateFormat = new SimpleDateFormat("HH:mm:ss.SSS ");
        this.dummyPosition = new FieldPosition(0);
        this.buffer = new StringBuffer();
        this.level = 0;
        this.connectionIDString = "(" + connectionID + ") ";
    }
    
    public void setLogLevel(final int level) {
        this.level = level;
    }
    
    public int getLogLevel() {
        return this.level;
    }
    
    public boolean logDebug() {
        return this.level >= 2;
    }
    
    public boolean logInfo() {
        return this.level >= 1;
    }
    
    public void debug(final String str) {
        this.debug(str, null);
    }
    
    public void debug(final String str, final Throwable t) {
        if (this.logDebug()) {
            this.log(str, t);
        }
    }
    
    public void info(final String str) {
        this.info(str, null);
    }
    
    public void info(final String str, final Throwable t) {
        if (this.logInfo()) {
            this.log(str, t);
        }
    }
    
    public void log(final String str, final Throwable t) {
        final PrintWriter writer = DriverManager.getLogWriter();
        if (writer == null) {
            return;
        }
        synchronized (this) {
            this.buffer.setLength(0);
            this.dateFormat.format(new Date(), this.buffer, this.dummyPosition);
            this.buffer.append(this.connectionIDString);
            this.buffer.append(str);
            synchronized (writer) {
                writer.println(this.buffer.toString());
                if (t != null) {
                    t.printStackTrace(writer);
                }
            }
        }
    }
}
