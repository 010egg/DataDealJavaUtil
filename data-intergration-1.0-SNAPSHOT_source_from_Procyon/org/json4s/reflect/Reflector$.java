// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import java.math.BigInteger;
import scala.Symbol;
import java.sql.Timestamp;
import java.util.Date;
import scala.math.BigDecimal;
import scala.math.BigInt;
import scala.collection.Seq;
import scala.util.control.Exception$;
import scala.Function0;
import scala.collection.immutable.Nil$;
import scala.Tuple2;
import scala.collection.immutable.List;
import org.json4s.DefaultFormats$;
import org.json4s.Formats;
import scala.Function1;
import scala.reflect.Manifest;
import scala.Predef$;
import scala.collection.GenTraversableOnce;
import scala.Option;
import scala.collection.immutable.Set;
import java.lang.reflect.Type;

public final class Reflector$
{
    public static final Reflector$ MODULE$;
    private final package.Memo<Type, Class<?>> rawClasses;
    private final package.Memo<String, String> unmangledNames;
    private final package.Memo<ScalaType, ObjectDescriptor> descriptors;
    private final Set<Type> primitives;
    private final package.Memo<String, Option<ScalaType>> stringTypes;
    
    static {
        new Reflector$();
    }
    
    public void clearCaches() {
        this.rawClasses.clear();
        this.unmangledNames.clear();
        this.descriptors.clear();
        this.stringTypes.clear();
    }
    
    public boolean isPrimitive(final Type t, final Set<Type> extra) {
        return this.primitives.$plus$plus((GenTraversableOnce)extra).contains((Object)t);
    }
    
    public Set<Type> isPrimitive$default$2() {
        return (Set<Type>)Predef$.MODULE$.Set().empty();
    }
    
    public <T> ScalaType scalaTypeOf(final Manifest<T> mf) {
        return ScalaType$.MODULE$.apply((scala.reflect.Manifest<Object>)mf);
    }
    
    public ScalaType scalaTypeOf(final Class<?> clazz) {
        return ScalaType$.MODULE$.apply(ManifestFactory$.MODULE$.manifestOf(clazz));
    }
    
    public ScalaType scalaTypeOf(final Type t) {
        return ScalaType$.MODULE$.apply(ManifestFactory$.MODULE$.manifestOf(t));
    }
    
    public Option<ScalaType> scalaTypeOf(final String name) {
        return this.stringTypes.apply(name, (scala.Function1<String, Option<ScalaType>>)new Reflector$$anonfun$scalaTypeOf.Reflector$$anonfun$scalaTypeOf$1());
    }
    
    public <T> ObjectDescriptor describe(final Manifest<T> mf, final Formats formats) {
        return this.describe(package$.MODULE$.scalaTypeDescribable(this.scalaTypeOf((scala.reflect.Manifest<Object>)mf), formats));
    }
    
    public ObjectDescriptor describe(final package.ReflectorDescribable<?> st) {
        return this.descriptors.apply(st.scalaType(), (scala.Function1<ScalaType, ObjectDescriptor>)new Reflector$$anonfun$describe.Reflector$$anonfun$describe$1((package.ReflectorDescribable)st));
    }
    
    public <T> Formats describe$default$2() {
        return DefaultFormats$.MODULE$;
    }
    
    public ObjectDescriptor createDescriptor(final ScalaType tpe, final package.ParameterNameReader paramNameReader, final List<Tuple2<Class<?>, Object>> companionMappings) {
        return tpe.isPrimitive() ? new PrimitiveDescriptor(tpe, PrimitiveDescriptor$.MODULE$.apply$default$2()) : new Reflector.ClassDescriptorBuilder(tpe, paramNameReader, companionMappings).result();
    }
    
    public package.ParameterNameReader createDescriptor$default$2() {
        return package.ParanamerReader$.MODULE$;
    }
    
    public List<Tuple2<Class<?>, Object>> createDescriptor$default$3() {
        return (List<Tuple2<Class<?>, Object>>)Nil$.MODULE$;
    }
    
    public Option<Function0<Object>> defaultValue(final Class<?> compClass, final Object compObj, final int argIndex, final String pattern) {
        return (Option<Function0<Object>>)Exception$.MODULE$.allCatch().withApply((Function1)new Reflector$$anonfun$defaultValue.Reflector$$anonfun$defaultValue$1()).apply((Function0)new Reflector$$anonfun$defaultValue.Reflector$$anonfun$defaultValue$2((Class)compClass, compObj, argIndex, pattern));
    }
    
    public Class<?> rawClassOf(final Type t) {
        return this.rawClasses.apply(t, (scala.Function1<Type, Class<?>>)new Reflector$$anonfun$rawClassOf.Reflector$$anonfun$rawClassOf$1());
    }
    
    public String unmangleName(final String name) {
        return this.unmangledNames.apply(name, (scala.Function1<String, String>)new Reflector$$anonfun$unmangleName.Reflector$$anonfun$unmangleName$1());
    }
    
    public Object mkParameterizedType(final Type owner, final Seq<Type> typeArgs) {
        return new Reflector$$anon.Reflector$$anon$1(owner, (Seq)typeArgs);
    }
    
    private Reflector$() {
        MODULE$ = this;
        this.rawClasses = new package.Memo<Type, Class<?>>();
        this.unmangledNames = new package.Memo<String, String>();
        this.descriptors = new package.Memo<ScalaType, ObjectDescriptor>();
        this.primitives = (Set<Type>)Predef$.MODULE$.Set().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Type[] { String.class, Integer.TYPE, Long.TYPE, Double.TYPE, Float.TYPE, Byte.TYPE, BigInt.class, Boolean.TYPE, Short.TYPE, Integer.class, Long.class, Double.class, Float.class, BigDecimal.class, Byte.class, Boolean.class, Number.class, Short.class, Date.class, Timestamp.class, Symbol.class, java.math.BigDecimal.class, BigInteger.class }));
        this.stringTypes = new package.Memo<String, Option<ScalaType>>();
    }
}
