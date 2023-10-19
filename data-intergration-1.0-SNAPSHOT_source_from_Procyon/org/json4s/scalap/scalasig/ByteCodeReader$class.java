// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import org.json4s.scalap.Result;
import scala.Function1;
import org.json4s.scalap.Rule;

public abstract class ByteCodeReader$class
{
    public static Rule bytes(final ByteCodeReader $this, final int n) {
        return $this.apply((scala.Function1<Object, Result<Object, Object, Object>>)new ByteCodeReader$$anonfun$bytes.ByteCodeReader$$anonfun$bytes$1($this, n));
    }
    
    public static void $init$(final ByteCodeReader $this) {
        $this.org$json4s$scalap$scalasig$ByteCodeReader$_setter_$byte_$eq($this.apply((scala.Function1<Object, Result<Object, Object, Object>>)new ByteCodeReader$$anonfun.ByteCodeReader$$anonfun$2($this)));
        $this.org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u1_$eq($this.byte().$up$up((scala.Function1<Object, Object>)new ByteCodeReader$$anonfun.ByteCodeReader$$anonfun$3($this)));
        $this.org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u2_$eq($this.bytes(2).$up$up((scala.Function1<ByteCode, Object>)new ByteCodeReader$$anonfun.ByteCodeReader$$anonfun$4($this)));
        $this.org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u4_$eq($this.bytes(4).$up$up((scala.Function1<ByteCode, Object>)new ByteCodeReader$$anonfun.ByteCodeReader$$anonfun$5($this)));
    }
}
