// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Predef$;
import scala.MatchError;
import org.json4s.scalap.scalasig.AliasSymbol;
import org.json4s.scalap.scalasig.NullaryMethodType;
import org.json4s.scalap.scalasig.TypeBoundsType;
import scala.collection.immutable.Nil$;
import scala.collection.immutable.$colon$colon;
import org.json4s.scalap.scalasig.ThisType;
import org.json4s.scalap.scalasig.TypeRefType;
import scala.collection.mutable.StringBuilder;
import scala.collection.Iterator;
import scala.Some;
import org.json4s.scalap.scalasig.ByteCode$;
import org.json4s.scalap.scalasig.ClassFileParser$;
import scala.Option$;
import scala.None$;
import java.io.Serializable;
import org.json4s.scalap.scalasig.Symbol;
import org.json4s.scalap.scalasig.Type;
import org.json4s.scalap.scalasig.SymbolInfoSymbol;
import scala.collection.TraversableLike;
import scala.collection.Seq;
import scala.PartialFunction;
import scala.collection.Seq$;
import scala.collection.IterableLike;
import org.json4s.scalap.scalasig.ScalaSig;
import scala.Function1;
import org.json4s.scalap.scalasig.ClassSymbol;
import scala.Function0;
import org.json4s.scalap.scalasig.MethodSymbol;
import scala.collection.immutable.List;
import scala.collection.immutable.Vector;
import scala.collection.Iterable;
import scala.Tuple2;
import scala.Option;

public final class ScalaSigReader$
{
    public static final ScalaSigReader$ MODULE$;
    private final package.Memo<String, Option<Class<?>>> localPathMemo;
    private final package.Memo<Tuple2<String, Iterable<ClassLoader>>, Option<Class<?>>> remotePathMemo;
    private final String ModuleFieldName;
    private final String OuterFieldName;
    private final Vector<ClassLoader> ClassLoaders;
    
    static {
        new ScalaSigReader$();
    }
    
    public Class<?> readConstructor(final String argName, final Class<?> clazz, final int typeArgIndex, final List<String> argNames) {
        final ClassSymbol cl = this.findClass(clazz);
        final MethodSymbol cstr = (MethodSymbol)this.findConstructor(cl, argNames).getOrElse((Function0)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$3((Class)clazz));
        return this.findArgType(cstr, argNames.indexOf((Object)argName), typeArgIndex);
    }
    
    public Class<?> readConstructor(final String argName, final Class<?> clazz, final List<Object> typeArgIndexes, final List<String> argNames) {
        final ClassSymbol cl = this.findClass(clazz);
        final MethodSymbol cstr = (MethodSymbol)this.findConstructor(cl, argNames).getOrElse((Function0)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$4((Class)clazz));
        return this.findArgType(cstr, argNames.indexOf((Object)argName), typeArgIndexes);
    }
    
    public Class<?> readConstructor(final String argName, final ScalaType clazz, final int typeArgIndex, final List<String> argNames) {
        final ClassSymbol cl = this.findClass(clazz.erasure());
        final MethodSymbol cstr = (MethodSymbol)this.findConstructor(cl, argNames).orElse((Function0)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$5(clazz, (List)argNames)).getOrElse((Function0)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$6(clazz));
        return this.findArgType(cstr, argNames.indexOf((Object)argName), typeArgIndex);
    }
    
    public Class<?> readConstructor(final String argName, final ScalaType clazz, final List<Object> typeArgIndexes, final List<String> argNames) {
        final ClassSymbol cl = this.findClass(clazz.erasure());
        final Option cstr = this.findConstructor(cl, argNames);
        final Option maybeArgType = cstr.map((Function1)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$7(argName, (List)typeArgIndexes, (List)argNames, cstr)).orElse((Function0)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$8(argName, clazz, (List)typeArgIndexes, (List)argNames));
        return (Class<?>)maybeArgType.getOrElse((Function0)new ScalaSigReader$$anonfun$readConstructor.ScalaSigReader$$anonfun$readConstructor$1(clazz));
    }
    
    public Class<?> readField(final String name, final Class<?> clazz, final int typeArgIndex) {
        return this.findArgTypeForField(this.org$json4s$reflect$ScalaSigReader$$read$1(clazz, name, clazz), typeArgIndex);
    }
    
    public ClassSymbol findClass(final Class<?> clazz) {
        final ScalaSig sig = (ScalaSig)this.findScalaSig(clazz).getOrElse((Function0)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$9((Class)clazz));
        return (ClassSymbol)this.findClass(sig, clazz).getOrElse((Function0)new ScalaSigReader$$anonfun$findClass.ScalaSigReader$$anonfun$findClass$2((Class)clazz));
    }
    
    public Option<ClassSymbol> findClass(final ScalaSig sig, final Class<?> clazz) {
        final String name = package$.MODULE$.safeSimpleName(clazz);
        return (Option<ClassSymbol>)((IterableLike)sig.symbols().collect((PartialFunction)new ScalaSigReader$$anonfun$findClass.ScalaSigReader$$anonfun$findClass$1(), Seq$.MODULE$.canBuildFrom())).find((Function1)new ScalaSigReader$$anonfun$findClass.ScalaSigReader$$anonfun$findClass$3(name)).orElse((Function0)new ScalaSigReader$$anonfun$findClass.ScalaSigReader$$anonfun$findClass$4(sig, name));
    }
    
    public ClassSymbol findCompanionObject(final Class<?> clazz) {
        final ScalaSig sig = (ScalaSig)this.findScalaSig(clazz).getOrElse((Function0)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$10((Class)clazz));
        return (ClassSymbol)this.findCompanionObject(sig, clazz).getOrElse((Function0)new ScalaSigReader$$anonfun$findCompanionObject.ScalaSigReader$$anonfun$findCompanionObject$2((Class)clazz));
    }
    
    public Option<ClassSymbol> findCompanionObject(final ScalaSig sig, final Class<?> clazz) {
        final String name = package$.MODULE$.safeSimpleName(clazz);
        return (Option<ClassSymbol>)((IterableLike)sig.symbols().collect((PartialFunction)new ScalaSigReader$$anonfun$findCompanionObject.ScalaSigReader$$anonfun$findCompanionObject$1(), Seq$.MODULE$.canBuildFrom())).find((Function1)new ScalaSigReader$$anonfun$findCompanionObject.ScalaSigReader$$anonfun$findCompanionObject$3(name));
    }
    
    public Option<MethodSymbol> findConstructor(final ClassSymbol c, final List<String> argNames) {
        final Seq ms = (Seq)c.children().collect((PartialFunction)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$1(), Seq$.MODULE$.canBuildFrom());
        return (Option<MethodSymbol>)ms.find((Function1)new ScalaSigReader$$anonfun$findConstructor.ScalaSigReader$$anonfun$findConstructor$1((List)argNames));
    }
    
    public Option<MethodSymbol> findApply(final ClassSymbol c, final List<String> argNames) {
        final Seq ms = (Seq)c.children().collect((PartialFunction)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$2(), Seq$.MODULE$.canBuildFrom());
        return (Option<MethodSymbol>)ms.find((Function1)new ScalaSigReader$$anonfun$findApply.ScalaSigReader$$anonfun$findApply$1((List)argNames));
    }
    
    public Seq<MethodSymbol> findFields(final ClassSymbol c) {
        return (Seq<MethodSymbol>)c.children().collect((PartialFunction)new ScalaSigReader$$anonfun$findFields.ScalaSigReader$$anonfun$findFields$1(), Seq$.MODULE$.canBuildFrom());
    }
    
    public Option<MethodSymbol> org$json4s$reflect$ScalaSigReader$$findField(final Class<?> clazz, final String name) {
        return this.findField(this.findClass(clazz), name);
    }
    
    private Option<MethodSymbol> findField(final ClassSymbol c, final String name) {
        return (Option<MethodSymbol>)((TraversableLike)c.children().collect((PartialFunction)new ScalaSigReader$$anonfun$findField.ScalaSigReader$$anonfun$findField$1(name), Seq$.MODULE$.canBuildFrom())).headOption();
    }
    
    public Class<?> findArgType(final MethodSymbol s, final int argIdx, final int typeArgIndex) {
        return (Class<?>)this.findPrimitive$1(((SymbolInfoSymbol)s.children().apply(argIdx)).infoType(), typeArgIndex).map((Function1)new ScalaSigReader$$anonfun$findArgType.ScalaSigReader$$anonfun$findArgType$1()).getOrElse((Function0)new ScalaSigReader$$anonfun$findArgType.ScalaSigReader$$anonfun$findArgType$2());
    }
    
    public Class<?> findArgType(final MethodSymbol s, final int argIdx, final List<Object> typeArgIndexes) {
        return this.org$json4s$reflect$ScalaSigReader$$toClass(this.findPrimitive$2(((SymbolInfoSymbol)s.children().apply(argIdx)).infoType(), 0, typeArgIndexes));
    }
    
    private Class<?> findArgTypeForField(final MethodSymbol s, final int typeArgIdx) {
        final Type t = this.getType$1(s, typeArgIdx);
        return this.org$json4s$reflect$ScalaSigReader$$toClass(this.findPrimitive$3(t));
    }
    
    public Class<? super Object> org$json4s$reflect$ScalaSigReader$$toClass(final Symbol s) {
        final String path = s.path();
        Serializable s2;
        if ("scala.Short".equals(path)) {
            s2 = Short.TYPE;
        }
        else if ("scala.Int".equals(path)) {
            s2 = Integer.TYPE;
        }
        else if ("scala.Long".equals(path)) {
            s2 = Long.TYPE;
        }
        else if ("scala.Boolean".equals(path)) {
            s2 = Boolean.TYPE;
        }
        else if ("scala.Float".equals(path)) {
            s2 = Float.TYPE;
        }
        else if ("scala.Double".equals(path)) {
            s2 = Double.TYPE;
        }
        else if ("scala.Byte".equals(path)) {
            s2 = Byte.TYPE;
        }
        else {
            s2 = Object.class;
        }
        return (Class<? super Object>)s2;
    }
    
    private boolean isPrimitive(final Symbol s) {
        final Class<? super Object> org$json4s$reflect$ScalaSigReader$$toClass = this.org$json4s$reflect$ScalaSigReader$$toClass(s);
        final Class<Object> obj = Object.class;
        if (org$json4s$reflect$ScalaSigReader$$toClass == null) {
            if (obj != null) {
                return true;
            }
        }
        else if (!org$json4s$reflect$ScalaSigReader$$toClass.equals(obj)) {
            return true;
        }
        return false;
        b = true;
        return b;
    }
    
    public Option<ScalaSig> findScalaSig(final Class<?> clazz) {
        Object o;
        try {
            o = this.parseClassFileFromByteCode(clazz).orElse((Function0)new ScalaSigReader$$anonfun$findScalaSig.ScalaSigReader$$anonfun$findScalaSig$1((Class)clazz));
        }
        catch (NullPointerException ex) {
            o = None$.MODULE$;
        }
        return (Option<ScalaSig>)o;
    }
    
    private Option<ScalaSig> parseClassFileFromByteCode(final Class<?> clazz) {
        return (Option<ScalaSig>)Option$.MODULE$.apply((Object)ClassFileParser$.MODULE$.parse(ByteCode$.MODULE$.forClass(clazz))).flatMap((Function1)new ScalaSigReader$$anonfun$parseClassFileFromByteCode.ScalaSigReader$$anonfun$parseClassFileFromByteCode$1());
    }
    
    public String ModuleFieldName() {
        return this.ModuleFieldName;
    }
    
    public String OuterFieldName() {
        return this.OuterFieldName;
    }
    
    public Vector<ClassLoader> ClassLoaders() {
        return this.ClassLoaders;
    }
    
    public Option<Tuple2<Class<Object>, Option<Object>>> companions(final String t, final Option<Object> companion, final Iterable<ClassLoader> classLoaders) {
        final Option cc = this.resolveClass(this.org$json4s$reflect$ScalaSigReader$$path$1(t), classLoaders).flatMap((Function1)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$11((Iterable)classLoaders));
        return (Option<Tuple2<Class<Object>, Option<Object>>>)cc.map((Function1)new ScalaSigReader$$anonfun$companions.ScalaSigReader$$anonfun$companions$1((Option)companion));
    }
    
    public Option<Object> companions$default$2() {
        return (Option<Object>)None$.MODULE$;
    }
    
    public Iterable<ClassLoader> companions$default$3() {
        return (Iterable<ClassLoader>)this.ClassLoaders();
    }
    
    public <X> Option<Class<X>> resolveClass(final String c, final Iterable<ClassLoader> classLoaders) {
        return (Option<Class<X>>)((classLoaders == this.ClassLoaders()) ? this.localPathMemo.apply(c, (scala.Function1<String, Option<Class<?>>>)new ScalaSigReader$$anonfun$resolveClass.ScalaSigReader$$anonfun$resolveClass$1((Iterable)classLoaders)) : this.remotePathMemo.apply((Tuple2<String, Iterable<ClassLoader>>)new Tuple2((Object)c, (Object)classLoaders), (scala.Function1<Tuple2<String, Iterable<ClassLoader>>, Option<Class<?>>>)new ScalaSigReader$$anonfun$resolveClass.ScalaSigReader$$anonfun$resolveClass$2()));
    }
    
    public <X> Iterable<ClassLoader> resolveClass$default$2() {
        return (Iterable<ClassLoader>)this.ClassLoaders();
    }
    
    public <X> Option<Class<X>> org$json4s$reflect$ScalaSigReader$$resolveClassCached(final String c, final Iterable<ClassLoader> classLoaders) {
        None$ module$ = null;
        try {
            Class clazz = null;
            for (Iterator iter = classLoaders.iterator().$plus$plus((Function0)new ScalaSigReader$$anonfun.ScalaSigReader$$anonfun$12()); clazz == null && iter.hasNext(); clazz = Class.forName(c, true, (ClassLoader)iter.next())) {}
            if (clazz != null) {
                new Some((Object)clazz);
            }
        }
        finally {
            module$ = None$.MODULE$;
        }
        return (Option<Class<X>>)module$;
    }
    
    public final MethodSymbol org$json4s$reflect$ScalaSigReader$$read$1(final Class current, final String name$1, final Class clazz$1) {
        final Class<Object> obj = Object.class;
        if (current == null) {
            if (obj != null) {
                return (MethodSymbol)this.org$json4s$reflect$ScalaSigReader$$findField(current, name$1).orElse((Function0)new ScalaSigReader$$anonfun$org$json4s$reflect$ScalaSigReader$$read$1.ScalaSigReader$$anonfun$org$json4s$reflect$ScalaSigReader$$read$1$1(name$1, current)).getOrElse((Function0)new ScalaSigReader$$anonfun$org$json4s$reflect$ScalaSigReader$$read$1.ScalaSigReader$$anonfun$org$json4s$reflect$ScalaSigReader$$read$1$2(name$1, clazz$1, current));
            }
        }
        else if (!current.equals(obj)) {
            return (MethodSymbol)this.org$json4s$reflect$ScalaSigReader$$findField(current, name$1).orElse((Function0)new ScalaSigReader$$anonfun$org$json4s$reflect$ScalaSigReader$$read$1.ScalaSigReader$$anonfun$org$json4s$reflect$ScalaSigReader$$read$1$1(name$1, current)).getOrElse((Function0)new ScalaSigReader$$anonfun$org$json4s$reflect$ScalaSigReader$$read$1.ScalaSigReader$$anonfun$org$json4s$reflect$ScalaSigReader$$read$1$2(name$1, clazz$1, current));
        }
        throw package$.MODULE$.fail(new StringBuilder().append((Object)"Can't find field ").append((Object)name$1).append((Object)" from ").append((Object)clazz$1).toString(), package$.MODULE$.fail$default$2());
    }
    
    private final Option findPrimitive$1(Type t, final int typeArgIndex$1) {
        Object module$;
        while (true) {
            boolean b = false;
            TypeRefType typeRefType = null;
            final Type type = t;
            if (type instanceof TypeRefType) {
                b = true;
                typeRefType = (TypeRefType)type;
                final Type prefix = typeRefType.prefix();
                final Symbol symbol = typeRefType.symbol();
                if (prefix instanceof ThisType && this.isPrimitive(symbol)) {
                    module$ = new Some((Object)symbol);
                    break;
                }
            }
            if (b) {
                final Seq<Type> typeArgs = typeRefType.typeArgs();
                if (typeArgs instanceof $colon$colon) {
                    final Type type2 = (Type)(($colon$colon)typeArgs).head();
                    if (type2 instanceof TypeRefType) {
                        final TypeRefType typeRefType2 = (TypeRefType)type2;
                        final Type prefix2 = typeRefType2.prefix();
                        final Symbol symbol2 = typeRefType2.symbol();
                        if (prefix2 instanceof ThisType) {
                            module$ = new Some((Object)symbol2);
                            break;
                        }
                    }
                }
            }
            if (b) {
                final Symbol symbol3 = typeRefType.symbol();
                if (Nil$.MODULE$.equals(typeRefType.typeArgs())) {
                    module$ = new Some((Object)symbol3);
                    break;
                }
            }
            if (b) {
                final Seq args = typeRefType.typeArgs();
                if (typeArgIndex$1 >= args.length()) {
                    t = (Type)args.apply(0);
                    continue;
                }
            }
            if (b) {
                final Seq args2 = typeRefType.typeArgs();
                final Type type3;
                final Type ta = type3 = (Type)args2.apply(typeArgIndex$1);
                if (!(type3 instanceof TypeRefType)) {
                    throw package$.MODULE$.fail(new StringBuilder().append((Object)"Unexpected type info ").append((Object)type3).toString(), package$.MODULE$.fail$default$2());
                }
                t = type3;
            }
            else {
                if (type instanceof TypeBoundsType) {
                    module$ = None$.MODULE$;
                    break;
                }
                throw package$.MODULE$.fail(new StringBuilder().append((Object)"Unexpected type info ").append((Object)type).toString(), package$.MODULE$.fail$default$2());
            }
        }
        return (Option)module$;
    }
    
    private final Symbol findPrimitive$2(final Type t, final int curr, final List typeArgIndexes$2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //     6: aload_3         /* typeArgIndexes$2 */
        //     7: invokevirtual   scala/collection/immutable/List.length:()I
        //    10: iconst_1       
        //    11: isub           
        //    12: invokevirtual   scala/Predef$.intWrapper:(I)I
        //    15: iload_2         /* curr */
        //    16: invokevirtual   scala/runtime/RichInt$.min$extension:(II)I
        //    19: istore          ii
        //    21: iconst_0       
        //    22: istore          6
        //    24: aconst_null    
        //    25: astore          7
        //    27: aload_1         /* t */
        //    28: astore          8
        //    30: aload           8
        //    32: instanceof      Lorg/json4s/scalap/scalasig/TypeRefType;
        //    35: ifeq            86
        //    38: iconst_1       
        //    39: istore          6
        //    41: aload           8
        //    43: checkcast       Lorg/json4s/scalap/scalasig/TypeRefType;
        //    46: astore          7
        //    48: aload           7
        //    50: invokevirtual   org/json4s/scalap/scalasig/TypeRefType.prefix:()Lorg/json4s/scalap/scalasig/Type;
        //    53: astore          9
        //    55: aload           7
        //    57: invokevirtual   org/json4s/scalap/scalasig/TypeRefType.symbol:()Lorg/json4s/scalap/scalasig/Symbol;
        //    60: astore          symbol
        //    62: aload           9
        //    64: instanceof      Lorg/json4s/scalap/scalasig/ThisType;
        //    67: ifeq            86
        //    70: aload_0         /* this */
        //    71: aload           symbol
        //    73: invokespecial   org/json4s/reflect/ScalaSigReader$.isPrimitive:(Lorg/json4s/scalap/scalasig/Symbol;)Z
        //    76: ifeq            86
        //    79: aload           symbol
        //    81: astore          11
        //    83: goto            120
        //    86: iload           6
        //    88: ifeq            123
        //    91: aload           7
        //    93: invokevirtual   org/json4s/scalap/scalasig/TypeRefType.symbol:()Lorg/json4s/scalap/scalasig/Symbol;
        //    96: astore          symbol
        //    98: aload           7
        //   100: invokevirtual   org/json4s/scalap/scalasig/TypeRefType.typeArgs:()Lscala/collection/Seq;
        //   103: astore          13
        //   105: getstatic       scala/collection/immutable/Nil$.MODULE$:Lscala/collection/immutable/Nil$;
        //   108: aload           13
        //   110: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   113: ifeq            123
        //   116: aload           symbol
        //   118: astore          11
        //   120: aload           11
        //   122: areturn        
        //   123: iload           6
        //   125: ifeq            194
        //   128: aload           7
        //   130: invokevirtual   org/json4s/scalap/scalasig/TypeRefType.typeArgs:()Lscala/collection/Seq;
        //   133: astore          args
        //   135: aload_3         /* typeArgIndexes$2 */
        //   136: iload           5
        //   138: invokevirtual   scala/collection/immutable/List.apply:(I)Ljava/lang/Object;
        //   141: invokestatic    scala/runtime/BoxesRunTime.unboxToInt:(Ljava/lang/Object;)I
        //   144: aload           args
        //   146: invokeinterface scala/collection/Seq.length:()I
        //   151: if_icmplt       194
        //   154: aload           args
        //   156: getstatic       scala/runtime/RichInt$.MODULE$:Lscala/runtime/RichInt$;
        //   159: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   162: iconst_0       
        //   163: invokevirtual   scala/Predef$.intWrapper:(I)I
        //   166: aload           args
        //   168: invokeinterface scala/collection/Seq.length:()I
        //   173: iconst_1       
        //   174: isub           
        //   175: invokevirtual   scala/runtime/RichInt$.max$extension:(II)I
        //   178: invokeinterface scala/collection/Seq.apply:(I)Ljava/lang/Object;
        //   183: checkcast       Lorg/json4s/scalap/scalasig/Type;
        //   186: iload_2         /* curr */
        //   187: iconst_1       
        //   188: iadd           
        //   189: istore_2        /* curr */
        //   190: astore_1        /* t */
        //   191: goto            0
        //   194: iload           6
        //   196: ifeq            290
        //   199: aload           7
        //   201: invokevirtual   org/json4s/scalap/scalasig/TypeRefType.typeArgs:()Lscala/collection/Seq;
        //   204: astore          args
        //   206: aload           args
        //   208: aload_3         /* typeArgIndexes$2 */
        //   209: iload           5
        //   211: invokevirtual   scala/collection/immutable/List.apply:(I)Ljava/lang/Object;
        //   214: invokestatic    scala/runtime/BoxesRunTime.unboxToInt:(Ljava/lang/Object;)I
        //   217: invokeinterface scala/collection/Seq.apply:(I)Ljava/lang/Object;
        //   222: checkcast       Lorg/json4s/scalap/scalasig/Type;
        //   225: astore          ta
        //   227: aload           ta
        //   229: astore          17
        //   231: aload           17
        //   233: instanceof      Lorg/json4s/scalap/scalasig/TypeRefType;
        //   236: ifeq            256
        //   239: aload           17
        //   241: checkcast       Lorg/json4s/scalap/scalasig/TypeRefType;
        //   244: astore          18
        //   246: aload           18
        //   248: iload_2         /* curr */
        //   249: iconst_1       
        //   250: iadd           
        //   251: istore_2        /* curr */
        //   252: astore_1        /* t */
        //   253: goto            0
        //   256: getstatic       org/json4s/reflect/package$.MODULE$:Lorg/json4s/reflect/package$;
        //   259: new             Lscala/collection/mutable/StringBuilder;
        //   262: dup            
        //   263: invokespecial   scala/collection/mutable/StringBuilder.<init>:()V
        //   266: ldc_w           "Unexpected type info "
        //   269: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //   272: aload           17
        //   274: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //   277: invokevirtual   scala/collection/mutable/StringBuilder.toString:()Ljava/lang/String;
        //   280: getstatic       org/json4s/reflect/package$.MODULE$:Lorg/json4s/reflect/package$;
        //   283: invokevirtual   org/json4s/reflect/package$.fail$default$2:()Ljava/lang/Exception;
        //   286: invokevirtual   org/json4s/reflect/package$.fail:(Ljava/lang/String;Ljava/lang/Exception;)Lscala/runtime/Nothing$;
        //   289: athrow         
        //   290: getstatic       org/json4s/reflect/package$.MODULE$:Lorg/json4s/reflect/package$;
        //   293: new             Lscala/collection/mutable/StringBuilder;
        //   296: dup            
        //   297: invokespecial   scala/collection/mutable/StringBuilder.<init>:()V
        //   300: ldc_w           "Unexpected type info "
        //   303: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //   306: aload           8
        //   308: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //   311: invokevirtual   scala/collection/mutable/StringBuilder.toString:()Ljava/lang/String;
        //   314: getstatic       org/json4s/reflect/package$.MODULE$:Lorg/json4s/reflect/package$;
        //   317: invokevirtual   org/json4s/reflect/package$.fail$default$2:()Ljava/lang/Exception;
        //   320: invokevirtual   org/json4s/reflect/package$.fail:(Ljava/lang/String;Ljava/lang/Exception;)Lscala/runtime/Nothing$;
        //   323: athrow         
        //    StackMapTable: 00 07 00 FF 00 55 00 09 07 00 02 07 02 26 01 07 00 30 00 01 01 07 02 11 07 02 26 00 00 FE 00 21 00 00 07 01 29 F8 00 02 FB 00 46 FF 00 3D 00 12 07 00 02 07 02 26 01 07 00 30 00 01 01 07 02 11 07 02 26 00 00 00 00 00 00 07 00 B1 07 02 26 07 02 26 00 00 FF 00 21 00 09 07 00 02 07 02 26 01 07 00 30 00 01 01 07 02 11 07 02 26 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private final Type getType$1(SymbolInfoSymbol symbol, final int typeArgIdx$1) {
        boolean b2;
        TypeRefType typeRefType;
        Type infoType;
        while (true) {
            boolean b = false;
            NullaryMethodType nullaryMethodType = null;
            b2 = false;
            typeRefType = null;
            infoType = symbol.infoType();
            if (infoType instanceof NullaryMethodType) {
                b = true;
                nullaryMethodType = (NullaryMethodType)infoType;
                final Type resultType = nullaryMethodType.resultType();
                if (resultType instanceof TypeRefType) {
                    final Symbol alias = ((TypeRefType)resultType).symbol();
                    if (alias instanceof AliasSymbol) {
                        symbol = (AliasSymbol)alias;
                        continue;
                    }
                }
            }
            if (b) {
                final Type resultType2 = nullaryMethodType.resultType();
                if (resultType2 instanceof TypeRefType) {
                    final Seq args = ((TypeRefType)resultType2).typeArgs();
                    return (Type)args.apply(typeArgIdx$1);
                }
            }
            if (!(infoType instanceof TypeRefType)) {
                break;
            }
            b2 = true;
            typeRefType = (TypeRefType)infoType;
            final Symbol alias2 = typeRefType.symbol();
            if (!(alias2 instanceof AliasSymbol)) {
                break;
            }
            symbol = (AliasSymbol)alias2;
        }
        if (!b2) {
            throw new MatchError((Object)infoType);
        }
        final Seq args2 = typeRefType.typeArgs();
        return (Type)args2.apply(typeArgIdx$1);
    }
    
    private final Symbol findPrimitive$3(final Type t) {
        if (t instanceof TypeRefType) {
            final TypeRefType typeRefType = (TypeRefType)t;
            final Type prefix = typeRefType.prefix();
            final Symbol symbol = typeRefType.symbol();
            if (prefix instanceof ThisType) {
                return symbol;
            }
        }
        throw package$.MODULE$.fail(new StringBuilder().append((Object)"Unexpected type info ").append((Object)t).toString(), package$.MODULE$.fail$default$2());
    }
    
    public final String org$json4s$reflect$ScalaSigReader$$path$1(final String tt) {
        return tt.endsWith("$") ? tt : new StringBuilder().append((Object)tt).append((Object)"$").toString();
    }
    
    public final Option org$json4s$reflect$ScalaSigReader$$safeField$1(final Class ccc, final Option companion$1) {
        None$ module$;
        try {
            Option$.MODULE$.apply((Object)ccc.getField(this.ModuleFieldName())).map((Function1)new ScalaSigReader$$anonfun$org$json4s$reflect$ScalaSigReader$$safeField$1.ScalaSigReader$$anonfun$org$json4s$reflect$ScalaSigReader$$safeField$1$1(companion$1));
        }
        finally {
            module$ = None$.MODULE$;
        }
        return (Option)module$;
    }
    
    private ScalaSigReader$() {
        MODULE$ = this;
        this.localPathMemo = new package.Memo<String, Option<Class<?>>>();
        this.remotePathMemo = new package.Memo<Tuple2<String, Iterable<ClassLoader>>, Option<Class<?>>>();
        this.ModuleFieldName = "MODULE$";
        this.OuterFieldName = "$outer";
        this.ClassLoaders = (Vector<ClassLoader>)scala.package$.MODULE$.Vector().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new ClassLoader[] { this.getClass().getClassLoader(), Thread.currentThread().getContextClassLoader() }));
    }
}
