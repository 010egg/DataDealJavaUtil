// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import com.thoughtworks.paranamer.Paranamer;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import scala.collection.Seq;
import scala.Predef$;
import scala.runtime.Nothing$;
import org.json4s.DefaultFormats$;
import org.json4s.Formats;
import scala.runtime.BoxesRunTime;
import scala.Function0;
import scala.Function1;
import scala.Some;
import com.thoughtworks.paranamer.CachingParanamer;
import scala.collection.immutable.Vector;

public final class package$
{
    public static final package$ MODULE$;
    private final String ConstructorDefaultValuePattern;
    private final String ModuleFieldName;
    private final Vector<ClassLoader> ClassLoaders;
    public final CachingParanamer org$json4s$reflect$package$$paranamer;
    
    static {
        new package$();
    }
    
    public String safeSimpleName(final Class<?> clazz) {
        String stripDollar = null;
        try {
            clazz.getSimpleName();
        }
        finally {
            final int packageNameLen = BoxesRunTime.unboxToInt(new Some((Object)clazz.getPackage()).map((Function1)new package$$anonfun.package$$anonfun$2()).getOrElse((Function0)new package$$anonfun.package$$anonfun$1()));
            stripDollar = this.stripDollar(clazz.getName().substring(packageNameLen));
        }
        return stripDollar;
    }
    
    public String stripDollar(String name) {
        String substring;
        while (true) {
            final int index = name.lastIndexOf(36);
            if (index == -1) {
                substring = name;
                break;
            }
            if (index != name.length() - 1) {
                substring = name.substring(index + 1);
                break;
            }
            name = name.substring(0, index);
        }
        return substring;
    }
    
    public String ConstructorDefaultValuePattern() {
        return this.ConstructorDefaultValuePattern;
    }
    
    public String ModuleFieldName() {
        return this.ModuleFieldName;
    }
    
    public Vector<ClassLoader> ClassLoaders() {
        return this.ClassLoaders;
    }
    
    public package.ReflectorDescribable<ScalaType> scalaTypeDescribable(final ScalaType t, final Formats formats) {
        return (package.ReflectorDescribable<ScalaType>)new package$$anon.package$$anon$1(t, formats);
    }
    
    public Formats scalaTypeDescribable$default$2(final ScalaType t) {
        return DefaultFormats$.MODULE$;
    }
    
    public package.ReflectorDescribable<Class<?>> classDescribable(final Class<?> t, final Formats formats) {
        return (package.ReflectorDescribable<Class<?>>)new package$$anon.package$$anon$2((Class)t, formats);
    }
    
    public Formats classDescribable$default$2(final Class<?> t) {
        return DefaultFormats$.MODULE$;
    }
    
    public package.ReflectorDescribable<String> stringDescribable(final String t, final Formats formats) {
        return (package.ReflectorDescribable<String>)new package$$anon.package$$anon$3(t, formats);
    }
    
    public Formats stringDescribable$default$2(final String t) {
        return DefaultFormats$.MODULE$;
    }
    
    public Nothing$ fail(final String msg, final Exception cause) {
        throw new org.json4s.package.MappingException(msg, cause);
    }
    
    public Exception fail$default$2() {
        return null;
    }
    
    private package$() {
        MODULE$ = this;
        this.ConstructorDefaultValuePattern = "$lessinit$greater$default$%d";
        this.ModuleFieldName = "MODULE$";
        this.ClassLoaders = (Vector<ClassLoader>)scala.package$.MODULE$.Vector().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new ClassLoader[] { this.getClass().getClassLoader(), Thread.currentThread().getContextClassLoader() }));
        this.org$json4s$reflect$package$$paranamer = new CachingParanamer(new BytecodeReadingParanamer());
    }
}
