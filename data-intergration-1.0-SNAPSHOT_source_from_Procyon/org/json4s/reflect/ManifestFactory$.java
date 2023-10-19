// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.collection.Seq$;
import scala.MatchError;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import scala.reflect.package$;
import scala.collection.Seq;
import scala.Function1;
import scala.reflect.ClassTag$;
import scala.Array$;
import scala.Predef$;
import java.lang.reflect.ParameterizedType;
import scala.reflect.Manifest;
import java.lang.reflect.Type;

public final class ManifestFactory$
{
    public static final ManifestFactory$ MODULE$;
    
    static {
        new ManifestFactory$();
    }
    
    public Manifest<?> manifestOf(Type t) {
        Manifest manifest;
        while (true) {
            final Type type = t;
            if (type instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)type;
                final Class clazz = this.manifestOf(parameterizedType.getRawType()).runtimeClass();
                final Manifest[] typeArgs = (Manifest[])Predef$.MODULE$.refArrayOps((Object[])parameterizedType.getActualTypeArguments()).map((Function1)new ManifestFactory$$anonfun.ManifestFactory$$anonfun$1(), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)Manifest.class)));
                manifest = ((parameterizedType.getOwnerType() == null) ? this.manifestOf(clazz, (Seq<Manifest<?>>)Predef$.MODULE$.wrapRefArray((Object[])typeArgs)) : package$.MODULE$.Manifest().classType((Manifest)this.manifestOf(parameterizedType.getOwnerType()), clazz, (Seq)Predef$.MODULE$.wrapRefArray((Object[])typeArgs)));
                break;
            }
            if (type instanceof GenericArrayType) {
                final Manifest componentManifest = this.manifestOf(((GenericArrayType)type).getGenericComponentType());
                final Manifest arrayManifest = componentManifest.arrayManifest();
                manifest = package$.MODULE$.Manifest().classType(arrayManifest.runtimeClass(), componentManifest, (Seq)Predef$.MODULE$.wrapRefArray((Object[])new Manifest[0]));
                break;
            }
            if (type instanceof WildcardType) {
                final Type[] upper = ((WildcardType)type).getUpperBounds();
                if (upper != null && upper.length > 0) {
                    t = upper[0];
                }
                else {
                    t = Object.class;
                }
            }
            else if (type instanceof TypeVariable) {
                final Type[] upper2 = ((TypeVariable)type).getBounds();
                if (upper2 != null && upper2.length > 0) {
                    t = upper2[0];
                }
                else {
                    t = Object.class;
                }
            }
            else {
                if (type instanceof Class) {
                    manifest = this.fromClass((Class<?>)type);
                    break;
                }
                throw new MatchError((Object)type);
            }
        }
        return (Manifest<?>)manifest;
    }
    
    public Manifest<?> manifestOf(final Class<?> erasure, final Seq<Manifest<?>> typeArgs) {
        Manifest manifest;
        if (typeArgs.size() == 0) {
            manifest = this.fromClass(erasure);
        }
        else {
            final String name = erasure.getName();
            final String obj = "scala.Array";
            Class<?> runtimeClass = null;
            Label_0071: {
                Label_0070: {
                    if (name == null) {
                        if (obj != null) {
                            break Label_0070;
                        }
                    }
                    else if (!name.equals(obj)) {
                        break Label_0070;
                    }
                    runtimeClass = (Class<?>)((Manifest)typeArgs.apply(0)).arrayManifest().runtimeClass();
                    break Label_0071;
                }
                runtimeClass = erasure;
            }
            final Class normalizedErasure = runtimeClass;
            manifest = package$.MODULE$.Manifest().classType(normalizedErasure, (Manifest)typeArgs.head(), (Seq)typeArgs.tail());
        }
        return (Manifest<?>)manifest;
    }
    
    public Manifest<?> manifestOf(final ScalaType st) {
        final Seq typeArgs = (Seq)st.typeArgs().map((Function1)new ManifestFactory$$anonfun.ManifestFactory$$anonfun$2(), Seq$.MODULE$.canBuildFrom());
        return this.manifestOf(st.erasure(), (Seq<Manifest<?>>)typeArgs);
    }
    
    private Manifest<?> fromClass(final Class<?> clazz) {
        final Class<Byte> type = Byte.TYPE;
        Label_0040: {
            if (type == null) {
                if (clazz != null) {
                    break Label_0040;
                }
            }
            else if (!type.equals(clazz)) {
                break Label_0040;
            }
            final Object o = package$.MODULE$.Manifest().Byte();
            return (Manifest<?>)o;
        }
        final Class<Short> type2 = Short.TYPE;
        Label_0081: {
            if (type2 == null) {
                if (clazz != null) {
                    break Label_0081;
                }
            }
            else if (!type2.equals(clazz)) {
                break Label_0081;
            }
            final Object o = package$.MODULE$.Manifest().Short();
            return (Manifest<?>)o;
        }
        final Class<Character> type3 = Character.TYPE;
        Label_0122: {
            if (type3 == null) {
                if (clazz != null) {
                    break Label_0122;
                }
            }
            else if (!type3.equals(clazz)) {
                break Label_0122;
            }
            final Object o = package$.MODULE$.Manifest().Char();
            return (Manifest<?>)o;
        }
        final Class<Integer> type4 = Integer.TYPE;
        Label_0163: {
            if (type4 == null) {
                if (clazz != null) {
                    break Label_0163;
                }
            }
            else if (!type4.equals(clazz)) {
                break Label_0163;
            }
            final Object o = package$.MODULE$.Manifest().Int();
            return (Manifest<?>)o;
        }
        final Class<Long> type5 = Long.TYPE;
        Label_0204: {
            if (type5 == null) {
                if (clazz != null) {
                    break Label_0204;
                }
            }
            else if (!type5.equals(clazz)) {
                break Label_0204;
            }
            final Object o = package$.MODULE$.Manifest().Long();
            return (Manifest<?>)o;
        }
        final Class<Float> type6 = Float.TYPE;
        Label_0245: {
            if (type6 == null) {
                if (clazz != null) {
                    break Label_0245;
                }
            }
            else if (!type6.equals(clazz)) {
                break Label_0245;
            }
            final Object o = package$.MODULE$.Manifest().Float();
            return (Manifest<?>)o;
        }
        final Class<Double> type7 = Double.TYPE;
        Label_0286: {
            if (type7 == null) {
                if (clazz != null) {
                    break Label_0286;
                }
            }
            else if (!type7.equals(clazz)) {
                break Label_0286;
            }
            final Object o = package$.MODULE$.Manifest().Double();
            return (Manifest<?>)o;
        }
        final Class<Boolean> type8 = Boolean.TYPE;
        Label_0327: {
            if (type8 == null) {
                if (clazz != null) {
                    break Label_0327;
                }
            }
            else if (!type8.equals(clazz)) {
                break Label_0327;
            }
            final Object o = package$.MODULE$.Manifest().Boolean();
            return (Manifest<?>)o;
        }
        final Class<Void> type9 = Void.TYPE;
        if (type9 == null) {
            if (clazz != null) {
                return (Manifest<?>)package$.MODULE$.Manifest().classType((Class)clazz);
            }
        }
        else if (!type9.equals(clazz)) {
            return (Manifest<?>)package$.MODULE$.Manifest().classType((Class)clazz);
        }
        Object o = package$.MODULE$.Manifest().Unit();
        return (Manifest<?>)o;
        o = package$.MODULE$.Manifest().classType((Class)clazz);
        return (Manifest<?>)o;
    }
    
    private ManifestFactory$() {
        MODULE$ = this;
    }
}
