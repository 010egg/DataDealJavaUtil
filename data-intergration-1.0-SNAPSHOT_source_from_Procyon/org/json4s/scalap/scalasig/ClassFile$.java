// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple4;
import scala.Option;
import scala.Serializable;
import scala.collection.Seq;
import scala.runtime.AbstractFunction4;

public final class ClassFile$ extends AbstractFunction4<ClassFileHeader, Seq<Field>, Seq<Method>, Seq<Attribute>, ClassFile> implements Serializable
{
    public static final ClassFile$ MODULE$;
    
    static {
        new ClassFile$();
    }
    
    public final String toString() {
        return "ClassFile";
    }
    
    public ClassFile apply(final ClassFileHeader header, final Seq<Field> fields, final Seq<Method> methods, final Seq<Attribute> attributes) {
        return new ClassFile(header, fields, methods, attributes);
    }
    
    public Option<Tuple4<ClassFileHeader, Seq<Field>, Seq<Method>, Seq<Attribute>>> unapply(final ClassFile x$0) {
        return (Option<Tuple4<ClassFileHeader, Seq<Field>, Seq<Method>, Seq<Attribute>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple4((Object)x$0.header(), (Object)x$0.fields(), (Object)x$0.methods(), (Object)x$0.attributes())));
    }
    
    private Object readResolve() {
        return ClassFile$.MODULE$;
    }
    
    private ClassFile$() {
        MODULE$ = this;
    }
}
