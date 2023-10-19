// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util;

public interface Callback
{
    void succeeded();
    
    void failed(final Throwable p0);
    
    public static class Adapter implements Callback
    {
        public static final Adapter INSTANCE;
        
        @Override
        public void succeeded() {
        }
        
        @Override
        public void failed(final Throwable x) {
        }
        
        static {
            INSTANCE = new Adapter();
        }
    }
}
