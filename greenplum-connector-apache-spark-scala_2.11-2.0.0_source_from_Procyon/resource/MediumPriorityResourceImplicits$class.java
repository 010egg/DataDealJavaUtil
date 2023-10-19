// 
// Decompiled by Procyon v0.5.36
// 

package resource;

public abstract class MediumPriorityResourceImplicits$class
{
    public static Resource closeableResource(final MediumPriorityResourceImplicits $this) {
        return (Resource)new MediumPriorityResourceImplicits$$anon.MediumPriorityResourceImplicits$$anon$3($this);
    }
    
    public static Resource connectionResource(final MediumPriorityResourceImplicits $this) {
        return (Resource)new MediumPriorityResourceImplicits$$anon.MediumPriorityResourceImplicits$$anon$4($this);
    }
    
    public static Resource statementResource(final MediumPriorityResourceImplicits $this) {
        return (Resource)new MediumPriorityResourceImplicits$$anon.MediumPriorityResourceImplicits$$anon$5($this);
    }
    
    public static Resource resultSetResource(final MediumPriorityResourceImplicits $this) {
        return (Resource)new MediumPriorityResourceImplicits$$anon.MediumPriorityResourceImplicits$$anon$6($this);
    }
    
    public static Resource pooledConnectionResource(final MediumPriorityResourceImplicits $this) {
        return (Resource)new MediumPriorityResourceImplicits$$anon.MediumPriorityResourceImplicits$$anon$7($this);
    }
    
    public static void $init$(final MediumPriorityResourceImplicits $this) {
    }
}
