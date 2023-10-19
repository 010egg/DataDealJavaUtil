// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.log;

public class StacklessLogging implements AutoCloseable
{
    private final Class<?>[] clazzes;
    
    public StacklessLogging(final Class<?>... classesToSquelch) {
        this.clazzes = classesToSquelch;
        this.hideStacks(true);
    }
    
    @Override
    public void close() throws Exception {
        this.hideStacks(false);
    }
    
    private void hideStacks(final boolean hide) {
        for (final Class<?> clazz : this.clazzes) {
            final Logger log = Log.getLogger(clazz);
            if (log != null) {
                if (log instanceof StdErrLog) {
                    ((StdErrLog)log).setHideStacks(hide);
                }
            }
        }
    }
}
