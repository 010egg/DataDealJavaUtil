// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.sys.package$;
import scala.Predef$;

public final class ClassDelta$
{
    public static final ClassDelta$ MODULE$;
    
    static {
        new ClassDelta$();
    }
    
    public int delta(final Class<?> class1, final Class<?> class2) {
        Label_0026: {
            if (class1 == null) {
                if (class2 != null) {
                    break Label_0026;
                }
            }
            else if (!class1.equals(class2)) {
                break Label_0026;
            }
            return 0;
        }
        int n;
        if (class1 == null) {
            n = 1;
        }
        else if (class2 == null) {
            n = -1;
        }
        else if (Predef$.MODULE$.refArrayOps((Object[])class1.getInterfaces()).contains((Object)class2)) {
            n = 0;
        }
        else if (Predef$.MODULE$.refArrayOps((Object[])class2.getInterfaces()).contains((Object)class1)) {
            n = 0;
        }
        else if (class1.isAssignableFrom(class2)) {
            n = 1 + this.delta(class1, class2.getSuperclass());
        }
        else {
            if (!class2.isAssignableFrom(class1)) {
                throw package$.MODULE$.error("Don't call delta unless one class is assignable from the other");
            }
            n = 1 + this.delta(class1.getSuperclass(), class2);
        }
        return n;
    }
    
    private ClassDelta$() {
        MODULE$ = this;
    }
}
