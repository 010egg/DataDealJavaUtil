// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.log;

public abstract class AbstractLogger implements Logger
{
    @Override
    public final Logger getLogger(final String name) {
        if (isBlank(name)) {
            return this;
        }
        final String basename = this.getName();
        final String fullname = (isBlank(basename) || Log.getRootLogger() == this) ? name : (basename + "." + name);
        Logger logger = Log.getLoggers().get(fullname);
        if (logger == null) {
            final Logger newlog = this.newLogger(fullname);
            logger = Log.getMutableLoggers().putIfAbsent(fullname, newlog);
            if (logger == null) {
                logger = newlog;
            }
        }
        return logger;
    }
    
    protected abstract Logger newLogger(final String p0);
    
    private static boolean isBlank(final String name) {
        if (name == null) {
            return true;
        }
        for (int size = name.length(), i = 0; i < size; ++i) {
            final char c = name.charAt(i);
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void debug(final String msg, final long arg) {
        if (this.isDebugEnabled()) {
            this.debug(msg, new Long(arg));
        }
    }
}
