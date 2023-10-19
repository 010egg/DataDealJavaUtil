// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.Seq;
import scala.collection.immutable.Nil$;
import scala.Predef$;
import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple3;
import scala.reflect.Manifest;
import scala.Option;
import scala.Tuple2;
import scala.PartialFunction;
import scala.Serializable;

public final class FieldSerializer$ implements Serializable
{
    public static final FieldSerializer$ MODULE$;
    
    static {
        new FieldSerializer$();
    }
    
    public PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> renameFrom(final String name, final String newName) {
        return (PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>>)new FieldSerializer$$anonfun$renameFrom.FieldSerializer$$anonfun$renameFrom$1(name, newName);
    }
    
    public PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> ignore(final String name) {
        return (PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>>)new FieldSerializer$$anonfun$ignore.FieldSerializer$$anonfun$ignore$1(name);
    }
    
    public PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> renameTo(final String name, final String newName) {
        return (PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>>)new FieldSerializer$$anonfun$renameTo.FieldSerializer$$anonfun$renameTo$1(name, newName);
    }
    
    public <A> FieldSerializer<A> apply(final PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> serializer, final PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> deserializer, final boolean includeLazyVal, final Manifest<A> mf) {
        return new FieldSerializer<A>(serializer, deserializer, includeLazyVal, mf);
    }
    
    public <A> Option<Tuple3<PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>>, PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>>, Object>> unapply(final FieldSerializer<A> x$0) {
        return (Option<Tuple3<PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>>, PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>>, Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.serializer(), (Object)x$0.deserializer(), (Object)BoxesRunTime.boxToBoolean(x$0.includeLazyVal()))));
    }
    
    public <A> PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> $lessinit$greater$default$1() {
        return (PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>>)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$);
    }
    
    public <A> PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> $lessinit$greater$default$2() {
        return (PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>>)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$);
    }
    
    public <A> boolean $lessinit$greater$default$3() {
        return false;
    }
    
    public <A> PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> apply$default$1() {
        return (PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>>)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$);
    }
    
    public <A> PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> apply$default$2() {
        return (PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>>)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$);
    }
    
    public <A> boolean apply$default$3() {
        return false;
    }
    
    private Object readResolve() {
        return FieldSerializer$.MODULE$;
    }
    
    private FieldSerializer$() {
        MODULE$ = this;
    }
}
