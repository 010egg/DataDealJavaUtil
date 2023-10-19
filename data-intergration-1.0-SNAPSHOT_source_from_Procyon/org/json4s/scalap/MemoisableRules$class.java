// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.runtime.BoxedUnit;
import scala.Function1;
import scala.runtime.VolatileByteRef;
import scala.runtime.ObjectRef;
import scala.Function0;

public abstract class MemoisableRules$class
{
    public static Rule memo(final MemoisableRules $this, final Object key, final Function0 toRule) {
        final ObjectRef rule$lzy = ObjectRef.zero();
        final VolatileByteRef bitmap$0 = VolatileByteRef.create((byte)0);
        return $this.from().apply((Function1)new MemoisableRules$$anonfun$memo.MemoisableRules$$anonfun$memo$1($this, rule$lzy, key, toRule, bitmap$0));
    }
    
    public static Rule ruleWithName(final MemoisableRules $this, final String name, final Function1 f) {
        return $this.org$json4s$scalap$MemoisableRules$$super$ruleWithName(name, (Function1)new MemoisableRules$$anonfun$ruleWithName.MemoisableRules$$anonfun$ruleWithName$1($this, name, f));
    }
    
    private static final Function1 rule$lzycompute$1(final MemoisableRules $this, final ObjectRef rule$lzy$1, final Function0 toRule$1, final VolatileByteRef bitmap$0$1) {
        synchronized ($this) {
            if ((byte)(bitmap$0$1.elem & 0x1) == 0) {
                rule$lzy$1.elem = toRule$1.apply();
                bitmap$0$1.elem |= 0x1;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return (Function1)rule$lzy$1.elem;
        }
    }
    
    public static final Function1 rule$1(final MemoisableRules $this, final ObjectRef rule$lzy$1, final Function0 toRule$1, final VolatileByteRef bitmap$0$1) {
        return (Function1)(((byte)(bitmap$0$1.elem & 0x1) == 0) ? rule$lzycompute$1($this, rule$lzy$1, toRule$1, bitmap$0$1) : rule$lzy$1.elem);
    }
    
    public static void $init$(final MemoisableRules $this) {
    }
}
