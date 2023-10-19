// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.immutable.List$;
import scala.collection.immutable.List;
import scala.collection.Seq;
import scala.collection.immutable.Nil$;
import scala.Predef$;
import scala.PartialFunction;
import scala.Function1;

public abstract class TypeHints$class
{
    public static boolean containsHint(final TypeHints $this, final Class clazz) {
        return $this.hints().exists((Function1)new TypeHints$$anonfun$containsHint.TypeHints$$anonfun$containsHint$1($this, clazz));
    }
    
    public static boolean shouldExtractHints(final TypeHints $this, final Class clazz) {
        return $this.hints().exists((Function1)new TypeHints$$anonfun$shouldExtractHints.TypeHints$$anonfun$shouldExtractHints$1($this, clazz));
    }
    
    public static PartialFunction deserialize(final TypeHints $this) {
        return (PartialFunction)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$);
    }
    
    public static PartialFunction serialize(final TypeHints $this) {
        return (PartialFunction)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$);
    }
    
    public static List components(final TypeHints $this) {
        return List$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new TypeHints[] { $this }));
    }
    
    public static TypeHints $plus(final TypeHints $this, final TypeHints hints) {
        return new TypeHints.CompositeTypeHints2((List<TypeHints>)$this.components().$colon$colon$colon((List)hints.components()));
    }
    
    public static void $init$(final TypeHints $this) {
    }
}
