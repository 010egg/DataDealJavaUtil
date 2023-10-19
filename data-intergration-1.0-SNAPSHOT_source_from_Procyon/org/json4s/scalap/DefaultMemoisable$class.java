// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.collection.mutable.HashMap;
import scala.MatchError;
import scala.Tuple2;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.Function0;

public abstract class DefaultMemoisable$class
{
    public static Object memo(final DefaultMemoisable $this, final Object key, final Function0 a) {
        return $this.map().getOrElseUpdate(key, (Function0)new DefaultMemoisable$$anonfun$memo.DefaultMemoisable$$anonfun$memo$2($this, key, a));
    }
    
    public static Object compute(final DefaultMemoisable $this, final Object key, final Function0 a) {
        final Object apply = a.apply();
        Success<Object, Object> success2;
        if (apply instanceof Success) {
            final Success<Object, Object> success = (Success<Object, Object>)apply;
            $this.onSuccess(key, success);
            success2 = success;
        }
        else {
            if (DefaultMemoisable$.MODULE$.debug()) {
                Predef$.MODULE$.println((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "", " -> ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { key, apply })));
            }
            success2 = (Success<Object, Object>)apply;
        }
        return success2;
    }
    
    public static void onSuccess(final DefaultMemoisable $this, final Object key, final Success result) {
        if (result != null) {
            final Object out = result.out();
            final Object t = result.value();
            final Tuple2 tuple2 = new Tuple2(out, t);
            final Object out2 = tuple2._1();
            final Object t2 = tuple2._2();
            if (DefaultMemoisable$.MODULE$.debug()) {
                Predef$.MODULE$.println((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "", " -> ", " (", ")" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { key, t2, out2 })));
            }
            return;
        }
        throw new MatchError((Object)result);
    }
    
    public static void $init$(final DefaultMemoisable $this) {
        $this.org$json4s$scalap$DefaultMemoisable$_setter_$map_$eq(new HashMap());
    }
}
