// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util;

import org.eclipse.jetty.util.log.Log;

public interface Promise<C>
{
    void succeeded(final C p0);
    
    void failed(final Throwable p0);
    
    public static class Adapter<C> implements Promise<C>
    {
        @Override
        public void succeeded(final C result) {
        }
        
        @Override
        public void failed(final Throwable x) {
            Log.getLogger(this.getClass()).warn(x);
        }
    }
}
