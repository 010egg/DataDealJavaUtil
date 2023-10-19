// 
// Decompiled by Procyon v0.5.36
// 

package resource;

public abstract class LowPriorityResourceImplicits$class
{
    public static Resource reflectiveCloseableResource(final LowPriorityResourceImplicits $this) {
        return (Resource)new LowPriorityResourceImplicits$$anon.LowPriorityResourceImplicits$$anon$1($this);
    }
    
    public static Resource reflectiveDisposableResource(final LowPriorityResourceImplicits $this) {
        return (Resource)new LowPriorityResourceImplicits$$anon.LowPriorityResourceImplicits$$anon$2($this);
    }
    
    public static void $init$(final LowPriorityResourceImplicits $this) {
    }
}
