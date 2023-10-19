// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.log;

import java.util.logging.Logger;
import java.util.logging.Level;

public class JavaUtilLog extends AbstractLogger
{
    private Level configuredLevel;
    private java.util.logging.Logger _logger;
    
    public JavaUtilLog() {
        this("org.eclipse.jetty.util.log");
    }
    
    public JavaUtilLog(final String name) {
        this._logger = java.util.logging.Logger.getLogger(name);
        if (Boolean.parseBoolean(Log.__props.getProperty("org.eclipse.jetty.util.log.DEBUG", "false"))) {
            this._logger.setLevel(Level.FINE);
        }
        this.configuredLevel = this._logger.getLevel();
    }
    
    @Override
    public String getName() {
        return this._logger.getName();
    }
    
    @Override
    public void warn(final String msg, final Object... args) {
        if (this._logger.isLoggable(Level.WARNING)) {
            this._logger.log(Level.WARNING, this.format(msg, args));
        }
    }
    
    @Override
    public void warn(final Throwable thrown) {
        this.warn("", thrown);
    }
    
    @Override
    public void warn(final String msg, final Throwable thrown) {
        this._logger.log(Level.WARNING, msg, thrown);
    }
    
    @Override
    public void info(final String msg, final Object... args) {
        if (this._logger.isLoggable(Level.INFO)) {
            this._logger.log(Level.INFO, this.format(msg, args));
        }
    }
    
    @Override
    public void info(final Throwable thrown) {
        this.info("", thrown);
    }
    
    @Override
    public void info(final String msg, final Throwable thrown) {
        this._logger.log(Level.INFO, msg, thrown);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this._logger.isLoggable(Level.FINE);
    }
    
    @Override
    public void setDebugEnabled(final boolean enabled) {
        if (enabled) {
            this.configuredLevel = this._logger.getLevel();
            this._logger.setLevel(Level.FINE);
        }
        else {
            this._logger.setLevel(this.configuredLevel);
        }
    }
    
    @Override
    public void debug(final String msg, final Object... args) {
        if (this._logger.isLoggable(Level.FINE)) {
            this._logger.log(Level.FINE, this.format(msg, args));
        }
    }
    
    @Override
    public void debug(final String msg, final long arg) {
        if (this._logger.isLoggable(Level.FINE)) {
            this._logger.log(Level.FINE, this.format(msg, arg));
        }
    }
    
    @Override
    public void debug(final Throwable thrown) {
        this.debug("", thrown);
    }
    
    @Override
    public void debug(final String msg, final Throwable thrown) {
        this._logger.log(Level.FINE, msg, thrown);
    }
    
    @Override
    protected Logger newLogger(final String fullname) {
        return new JavaUtilLog(fullname);
    }
    
    @Override
    public void ignore(final Throwable ignored) {
        if (Log.isIgnored()) {
            this.warn("IGNORED ", ignored);
        }
    }
    
    private String format(String msg, final Object... args) {
        msg = String.valueOf(msg);
        final String braces = "{}";
        final StringBuilder builder = new StringBuilder();
        int start = 0;
        for (final Object arg : args) {
            final int bracesIndex = msg.indexOf(braces, start);
            if (bracesIndex < 0) {
                builder.append(msg.substring(start));
                builder.append(" ");
                builder.append(arg);
                start = msg.length();
            }
            else {
                builder.append(msg.substring(start, bracesIndex));
                builder.append(String.valueOf(arg));
                start = bracesIndex + braces.length();
            }
        }
        builder.append(msg.substring(start));
        return builder.toString();
    }
}
