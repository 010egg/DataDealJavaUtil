// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple7;
import scala.Option;
import scala.Serializable;
import scala.collection.Seq;
import scala.runtime.AbstractFunction7;

public final class ClassFileHeader$ extends AbstractFunction7<Object, Object, ConstantPool, Object, Object, Object, Seq<Object>, ClassFileHeader> implements Serializable
{
    public static final ClassFileHeader$ MODULE$;
    
    static {
        new ClassFileHeader$();
    }
    
    public final String toString() {
        return "ClassFileHeader";
    }
    
    public ClassFileHeader apply(final int minor, final int major, final ConstantPool constants, final int flags, final int classIndex, final int superClassIndex, final Seq<Object> interfaces) {
        return new ClassFileHeader(minor, major, constants, flags, classIndex, superClassIndex, interfaces);
    }
    
    public Option<Tuple7<Object, Object, ConstantPool, Object, Object, Object, Seq<Object>>> unapply(final ClassFileHeader x$0) {
        return (Option<Tuple7<Object, Object, ConstantPool, Object, Object, Object, Seq<Object>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple7((Object)BoxesRunTime.boxToInteger(x$0.minor()), (Object)BoxesRunTime.boxToInteger(x$0.major()), (Object)x$0.constants(), (Object)BoxesRunTime.boxToInteger(x$0.flags()), (Object)BoxesRunTime.boxToInteger(x$0.classIndex()), (Object)BoxesRunTime.boxToInteger(x$0.superClassIndex()), (Object)x$0.interfaces())));
    }
    
    private Object readResolve() {
        return ClassFileHeader$.MODULE$;
    }
    
    private ClassFileHeader$() {
        MODULE$ = this;
    }
}
