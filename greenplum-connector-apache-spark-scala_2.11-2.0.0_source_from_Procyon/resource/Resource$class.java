// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.util.control.ControlThrowable;

public abstract class Resource$class
{
    public static void open(final Resource $this, final Object r) {
    }
    
    public static void closeAfterException(final Resource $this, final Object r, final Throwable t) {
        $this.close(r);
    }
    
    public static boolean isFatalException(final Resource $this, final Throwable t) {
        return t instanceof VirtualMachineError;
    }
    
    public static boolean isRethrownException(final Resource $this, final Throwable t) {
        return t instanceof ControlThrowable || t instanceof InterruptedException;
    }
    
    public static void $init$(final Resource $this) {
    }
}
