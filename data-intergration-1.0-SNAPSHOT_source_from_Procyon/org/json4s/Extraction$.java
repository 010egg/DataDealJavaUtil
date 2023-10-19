// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.immutable.Map$;
import scala.collection.immutable.StringOps;
import scala.runtime.RichChar$;
import scala.reflect.NameTransformer$;
import scala.Some;
import scala.Predef;
import org.json4s.reflect.PropertyDescriptor;
import scala.collection.Seq$;
import org.json4s.reflect.ClassDescriptor;
import scala.None$;
import java.sql.Timestamp;
import scala.reflect.ManifestFactory$;
import scala.Symbol$;
import scala.collection.immutable.$colon$colon;
import scala.Function1;
import scala.Function0;
import scala.util.control.Exception$;
import org.json4s.reflect.ScalaType;
import scala.Function2;
import scala.collection.immutable.Nil$;
import scala.collection.TraversableOnce;
import scala.collection.immutable.List;
import scala.util.matching.Regex;
import scala.math.BigDecimal$;
import scala.math.BigDecimal;
import scala.collection.Iterator;
import scala.PartialFunction;
import scala.MatchError;
import scala.math.BigInt;
import java.util.Date;
import scala.runtime.BoxesRunTime;
import scala.Symbol;
import scala.Tuple2;
import scala.util.Either;
import java.util.Collection;
import scala.collection.Iterable;
import scala.collection.Map;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import scala.collection.mutable.StringBuilder;
import org.json4s.reflect.ScalaType$;
import scala.Option;
import org.json4s.reflect.Reflector$;
import scala.reflect.Manifest;
import scala.runtime.BoxedUnit;
import scala.collection.Seq;
import scala.Predef$;
import scala.collection.immutable.Set;

public final class Extraction$
{
    public static final Extraction$ MODULE$;
    private Set<Class<?>> typesHaveNaN;
    private volatile boolean bitmap$0;
    
    static {
        new Extraction$();
    }
    
    private Set typesHaveNaN$lzycompute() {
        synchronized (this) {
            if (!this.bitmap$0) {
                this.typesHaveNaN = (Set<Class<?>>)Predef$.MODULE$.Set().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Class[] { Double.TYPE, Float.TYPE, Double.class, Float.class }));
                this.bitmap$0 = true;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.typesHaveNaN;
        }
    }
    
    public <A> A extract(final JsonAST.JValue json, final Formats formats, final Manifest<A> mf) {
        try {
            return (A)this.extract(json, Reflector$.MODULE$.scalaTypeOf((scala.reflect.Manifest<Object>)mf), formats);
        }
        catch (Exception cause) {
            throw new package.MappingException("unknown error", cause);
        }
        catch (package.MappingException ex) {
            throw ex;
        }
    }
    
    public <A> Option<A> extractOpt(final JsonAST.JValue json, final Formats formats, final Manifest<A> mf) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: aload_0         /* this */
        //     4: aload_1         /* json */
        //     5: aload_2         /* formats */
        //     6: aload_3         /* mf */
        //     7: invokevirtual   org/json4s/Extraction$.extract:(Lorg/json4s/JsonAST$JValue;Lorg/json4s/Formats;Lscala/reflect/Manifest;)Ljava/lang/Object;
        //    10: invokevirtual   scala/Option$.apply:(Ljava/lang/Object;)Lscala/Option;
        //    13: goto            46
        //    16: astore          4
        //    18: aload           4
        //    20: astore          5
        //    22: aload           5
        //    24: instanceof      Lorg/json4s/package$MappingException;
        //    27: ifeq            47
        //    30: aload_2         /* formats */
        //    31: invokeinterface org/json4s/Formats.strictOptionParsing:()Z
        //    36: ifne            47
        //    39: getstatic       scala/None$.MODULE$:Lscala/None$;
        //    42: astore          6
        //    44: aload           6
        //    46: areturn        
        //    47: aload           4
        //    49: athrow         
        //    Signature:
        //  <A:Ljava/lang/Object;>(Lorg/json4s/JsonAST$JValue;Lorg/json4s/Formats;Lscala/reflect/Manifest<TA;>;)Lscala/Option<TA;>;
        //    StackMapTable: 00 03 50 07 00 42 5D 07 00 78 FD 00 00 07 00 42 07 00 42
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  0      16     16     46     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.remove(ArrayList.java:535)
        //     at com.strobel.assembler.ir.StackMappingVisitor.pop(StackMappingVisitor.java:267)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:595)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
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
    
    public Object extract(final JsonAST.JValue json, final org.json4s.reflect.package.TypeInfo target, final Formats formats) {
        return this.extract(json, ScalaType$.MODULE$.apply(target), formats);
    }
    
    public <T> T decomposeWithBuilder(final Object a, final JsonWriter<T> builder, final Formats formats) {
        this.internalDecomposeWithBuilder(a, builder, formats);
        return builder.result();
    }
    
    public Object loadLazyValValue(final Object a, final String name, final Object defaultValue) {
        Object invoke;
        try {
            final Method method = a.getClass().getDeclaredMethod(new StringBuilder().append((Object)name).append((Object)"$lzycompute").toString(), (Class<?>[])new Class[0]);
            method.setAccessible(true);
            invoke = method.invoke(a, new Object[0]);
        }
        catch (Exception ex) {
            invoke = defaultValue;
        }
        return invoke;
    }
    
    private Set<Class<?>> typesHaveNaN() {
        return (Set<Class<?>>)(this.bitmap$0 ? this.typesHaveNaN : this.typesHaveNaN$lzycompute());
    }
    
    public <T> void internalDecomposeWithBuilder(Object a, JsonWriter<T> builder, Formats formats) {
        JsonWriter current = null;
        Object any = null;
        Class k = null;
        Label_1393: {
            Label_1327: {
                Block_38: {
                    Block_36: {
                        Block_34: {
                            Block_9: {
                                Block_8: {
                                    while (true) {
                                        current = builder;
                                        final PartialFunction serializer = formats.typeHints().serialize();
                                        any = a;
                                        final PartialFunction custom = Formats$.MODULE$.customSerializer(a, formats);
                                        if (custom.isDefinedAt(a)) {
                                            current.addJValue((JsonAST.JValue)custom.apply(a));
                                            final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                                            return;
                                        }
                                        if (serializer.isDefinedAt(a)) {
                                            current.addJValue(this.prependTypeHint$1(any.getClass(), (JsonAST.JObject)serializer.apply(any), formats));
                                            final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                                            return;
                                        }
                                        k = ((any == null) ? null : any.getClass());
                                        if (any == null) {
                                            current.addJValue(package$.MODULE$.JNull());
                                            final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                                            return;
                                        }
                                        if (JsonAST.JValue.class.isAssignableFrom(k)) {
                                            current.addJValue((JsonAST.JValue)any);
                                            final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                                            return;
                                        }
                                        if (this.typesHaveNaN().contains((Object)any.getClass())) {
                                            final String string = any.toString();
                                            final String obj4 = "NaN";
                                            if (string == null) {
                                                if (obj4 == null) {
                                                    break;
                                                }
                                            }
                                            else if (string.equals(obj4)) {
                                                break;
                                            }
                                        }
                                        if (Reflector$.MODULE$.isPrimitive(any.getClass(), Reflector$.MODULE$.isPrimitive$default$2())) {
                                            break Block_8;
                                        }
                                        if (Map.class.isAssignableFrom(k)) {
                                            break Block_9;
                                        }
                                        if (Iterable.class.isAssignableFrom(k)) {
                                            break Block_34;
                                        }
                                        if (Collection.class.isAssignableFrom(k)) {
                                            break Block_36;
                                        }
                                        if (k.isArray()) {
                                            break Block_38;
                                        }
                                        if (Option.class.isAssignableFrom(k)) {
                                            final Option v = (Option)any;
                                            if (!v.isDefined()) {
                                                break Label_1327;
                                            }
                                            final Object value = v.get();
                                            final JsonWriter jsonWriter = current;
                                            formats = formats;
                                            builder = (JsonWriter<T>)jsonWriter;
                                            a = value;
                                        }
                                        else {
                                            if (!Either.class.isAssignableFrom(k)) {
                                                break Label_1393;
                                            }
                                            final Either v2 = (Either)any;
                                            if (v2.isLeft()) {
                                                final Object value2 = v2.left().get();
                                                final JsonWriter jsonWriter2 = current;
                                                formats = formats;
                                                builder = (JsonWriter<T>)jsonWriter2;
                                                a = value2;
                                            }
                                            else {
                                                final Object value3 = v2.right().get();
                                                final JsonWriter jsonWriter3 = current;
                                                formats = formats;
                                                builder = (JsonWriter<T>)jsonWriter3;
                                                a = value3;
                                            }
                                        }
                                    }
                                    current.addJValue(package$.MODULE$.JNull());
                                    final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                                    return;
                                }
                                this.writePrimitive(any, current, formats);
                                final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                                return;
                            }
                            final JsonWriter obj = current.startObject();
                            for (final Tuple2 tuple2 : (Map)any) {
                                if (tuple2 != null) {
                                    final Object i = tuple2._1();
                                    final Object v3 = tuple2._2();
                                    if (i instanceof String) {
                                        this.addField$1((String)i, v3, obj, formats);
                                        final BoxedUnit unit = BoxedUnit.UNIT;
                                        continue;
                                    }
                                }
                                if (tuple2 != null) {
                                    final Object j = tuple2._1();
                                    final Object v4 = tuple2._2();
                                    if (j instanceof Symbol) {
                                        this.addField$1(((Symbol)j).name(), v4, obj, formats);
                                        final BoxedUnit unit2 = BoxedUnit.UNIT;
                                        continue;
                                    }
                                }
                                if (tuple2 != null) {
                                    final Object l = tuple2._1();
                                    final Object v5 = tuple2._2();
                                    if (l instanceof Integer) {
                                        this.addField$1(BoxesRunTime.boxToInteger(BoxesRunTime.unboxToInt(l)).toString(), v5, obj, formats);
                                        final BoxedUnit unit3 = BoxedUnit.UNIT;
                                        continue;
                                    }
                                }
                                if (tuple2 != null) {
                                    final Object m = tuple2._1();
                                    final Object v6 = tuple2._2();
                                    if (m instanceof Long) {
                                        this.addField$1(BoxesRunTime.boxToLong(BoxesRunTime.unboxToLong(m)).toString(), v6, obj, formats);
                                        final BoxedUnit unit4 = BoxedUnit.UNIT;
                                        continue;
                                    }
                                }
                                if (tuple2 != null) {
                                    final Object k2 = tuple2._1();
                                    final Object v7 = tuple2._2();
                                    if (k2 instanceof Date) {
                                        this.addField$1(formats.dateFormat().format((Date)k2), v7, obj, formats);
                                        final BoxedUnit unit5 = BoxedUnit.UNIT;
                                        continue;
                                    }
                                }
                                if (tuple2 != null) {
                                    final Object k3 = tuple2._1();
                                    final Object v8 = tuple2._2();
                                    if (k3 instanceof Integer) {
                                        this.addField$1(((Integer)k3).toString(), v8, obj, formats);
                                        final BoxedUnit unit6 = BoxedUnit.UNIT;
                                        continue;
                                    }
                                }
                                if (tuple2 != null) {
                                    final Object k4 = tuple2._1();
                                    final Object v9 = tuple2._2();
                                    if (k4 instanceof BigInt) {
                                        this.addField$1(((BigInt)k4).toString(), v9, obj, formats);
                                        final BoxedUnit unit7 = BoxedUnit.UNIT;
                                        continue;
                                    }
                                }
                                if (tuple2 != null) {
                                    final Object k5 = tuple2._1();
                                    final Object v10 = tuple2._2();
                                    if (k5 instanceof Long) {
                                        this.addField$1(((Long)k5).toString(), v10, obj, formats);
                                        final BoxedUnit unit8 = BoxedUnit.UNIT;
                                        continue;
                                    }
                                }
                                if (tuple2 != null) {
                                    final Object k6 = tuple2._1();
                                    final Object v11 = tuple2._2();
                                    if (k6 instanceof Short) {
                                        this.addField$1(BoxesRunTime.boxToShort(BoxesRunTime.unboxToShort(k6)).toString(), v11, obj, formats);
                                        final BoxedUnit unit9 = BoxedUnit.UNIT;
                                        continue;
                                    }
                                }
                                if (tuple2 != null) {
                                    final Object k7 = tuple2._1();
                                    final Object v12 = tuple2._2();
                                    if (k7 instanceof Short) {
                                        this.addField$1(((Short)k7).toString(), v12, obj, formats);
                                        final BoxedUnit unit10 = BoxedUnit.UNIT;
                                        continue;
                                    }
                                }
                                if (tuple2 != null) {
                                    final Object k8 = tuple2._1();
                                    final Object v13 = tuple2._2();
                                    if (k8 instanceof Object) {
                                        final Object a2 = k8;
                                        final PartialFunction customKeySerializer = Formats$.MODULE$.customKeySerializer(a2, formats);
                                        if (customKeySerializer.isDefinedAt(a2)) {
                                            this.addField$1((String)customKeySerializer.apply(a2), v13, obj, formats);
                                            final BoxedUnit unit11 = BoxedUnit.UNIT;
                                            continue;
                                        }
                                        throw org.json4s.reflect.package$.MODULE$.fail(new StringBuilder().append((Object)"Do not know how to serialize key of type ").append((Object)a2.getClass()).append((Object)". ").append((Object)"Consider implementing a CustomKeySerializer.").toString(), org.json4s.reflect.package$.MODULE$.fail$default$2());
                                    }
                                }
                                throw new MatchError((Object)tuple2);
                            }
                            obj.endObject();
                            final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                            return;
                        }
                        final JsonWriter arr = current.startArray();
                        final Iterator iter2 = ((Iterable)any).iterator();
                        while (iter2.hasNext()) {
                            this.internalDecomposeWithBuilder(iter2.next(), (JsonWriter<Object>)arr, formats);
                        }
                        arr.endArray();
                        final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                        return;
                    }
                    final JsonWriter arr2 = current.startArray();
                    final java.util.Iterator iter3 = ((Collection)any).iterator();
                    while (iter3.hasNext()) {
                        this.internalDecomposeWithBuilder(iter3.next(), (JsonWriter<Object>)arr2, formats);
                    }
                    arr2.endArray();
                    final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                    return;
                }
                final JsonWriter arr3 = current.startArray();
                final Iterator iter4 = Predef$.MODULE$.genericArrayOps(any).iterator();
                while (iter4.hasNext()) {
                    this.internalDecomposeWithBuilder(iter4.next(), (JsonWriter<Object>)arr3, formats);
                }
                arr3.endArray();
                final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                return;
            }
            final BoxedUnit boxedUnit = BoxedUnit.UNIT;
            return;
        }
        if (Tuple2.class.isAssignableFrom(k)) {
            final Tuple2 tuple3 = (Tuple2)any;
            Label_1580: {
                if (tuple3 != null) {
                    final Object k9 = tuple3._1();
                    final Object v14 = tuple3._2();
                    if (k9 instanceof String) {
                        final String name = (String)k9;
                        final JsonWriter obj2 = current.startObject();
                        this.addField$1(name, v14, obj2, formats);
                        obj2.endObject();
                        final BoxedUnit unit12 = BoxedUnit.UNIT;
                        break Label_1580;
                    }
                }
                if (tuple3 != null) {
                    final Object k10 = tuple3._1();
                    final Object v15 = tuple3._2();
                    if (k10 instanceof Symbol) {
                        final Symbol symbol = (Symbol)k10;
                        final JsonWriter obj3 = current.startObject();
                        this.addField$1(symbol.name(), v15, obj3, formats);
                        obj3.endObject();
                        final BoxedUnit unit13 = BoxedUnit.UNIT;
                        break Label_1580;
                    }
                }
                if (!(tuple3 instanceof Tuple2)) {
                    throw new MatchError((Object)tuple3);
                }
                this.decomposeObject$1(k, a, formats, current, any);
                final BoxedUnit unit14 = BoxedUnit.UNIT;
            }
            final BoxedUnit boxedUnit = BoxedUnit.UNIT;
        }
        else {
            this.decomposeObject$1(k, a, formats, current, any);
            final BoxedUnit boxedUnit = BoxedUnit.UNIT;
        }
    }
    
    public JsonAST.JValue decompose(final Object a, final Formats formats) {
        return this.decomposeWithBuilder(a, formats.wantsBigDecimal() ? JsonWriter$.MODULE$.bigDecimalAst() : JsonWriter$.MODULE$.ast(), formats);
    }
    
    private JsonWriter<?> writePrimitive(final Object a, final JsonWriter<?> builder, final Formats formats) {
        JsonWriter<?> jsonWriter;
        if (a instanceof String) {
            jsonWriter = builder.string((String)a);
        }
        else if (a instanceof Integer) {
            jsonWriter = builder.int(BoxesRunTime.unboxToInt(a));
        }
        else if (a instanceof Long) {
            jsonWriter = builder.long(BoxesRunTime.unboxToLong(a));
        }
        else if (a instanceof Double) {
            jsonWriter = builder.double(BoxesRunTime.unboxToDouble(a));
        }
        else if (a instanceof Float) {
            jsonWriter = builder.float(BoxesRunTime.unboxToFloat(a));
        }
        else if (a instanceof Byte) {
            jsonWriter = builder.byte(BoxesRunTime.unboxToByte(a));
        }
        else if (a instanceof BigInt) {
            jsonWriter = builder.bigInt((BigInt)a);
        }
        else if (a instanceof BigDecimal) {
            jsonWriter = builder.bigDecimal((BigDecimal)a);
        }
        else if (a instanceof Boolean) {
            jsonWriter = builder.boolean(BoxesRunTime.unboxToBoolean(a));
        }
        else if (a instanceof Short) {
            jsonWriter = builder.short(BoxesRunTime.unboxToShort(a));
        }
        else if (a instanceof Integer) {
            jsonWriter = builder.int((int)a);
        }
        else if (a instanceof Long) {
            jsonWriter = builder.long((long)a);
        }
        else if (a instanceof Double) {
            jsonWriter = builder.double((double)a);
        }
        else if (a instanceof Float) {
            jsonWriter = builder.float((float)a);
        }
        else if (a instanceof Byte) {
            jsonWriter = builder.byte((byte)a);
        }
        else if (a instanceof Boolean) {
            jsonWriter = builder.boolean((boolean)a);
        }
        else if (a instanceof Short) {
            jsonWriter = builder.short((short)a);
        }
        else if (a instanceof java.math.BigDecimal) {
            jsonWriter = builder.bigDecimal(BigDecimal$.MODULE$.javaBigDecimal2bigDecimal((java.math.BigDecimal)a));
        }
        else if (a instanceof Date) {
            jsonWriter = builder.string(formats.dateFormat().format((Date)a));
        }
        else {
            if (!(a instanceof Symbol)) {
                throw scala.sys.package$.MODULE$.error(new StringBuilder().append((Object)"not a primitive ").append((Object)a.getClass()).toString());
            }
            jsonWriter = builder.string(((Symbol)a).name());
        }
        return jsonWriter;
    }
    
    public scala.collection.immutable.Map<String, String> flatten(final JsonAST.JValue json, final Formats formats) {
        return (scala.collection.immutable.Map<String, String>)this.org$json4s$Extraction$$flatten0$1("", json, formats);
    }
    
    public Formats flatten$default$2(final JsonAST.JValue json) {
        return DefaultFormats$.MODULE$;
    }
    
    public JsonAST.JValue unflatten(final scala.collection.immutable.Map<String, String> map, final boolean useBigDecimalForDouble, final boolean useBigIntForLong) {
        final Regex ArrayProp = new Regex("^(\\.([^\\.\\[]+))\\[(\\d+)\\].*$", (Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[0]));
        final Regex ArrayElem = new Regex("^(\\[(\\d+)\\]).*$", (Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[0]));
        final Regex OtherProp = new Regex("^(\\.([^\\.\\[]+)).*$", (Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[0]));
        final List uniquePaths = (List)((TraversableOnce)map.keys().foldLeft((Object)Predef$.MODULE$.Set().apply((Seq)Nil$.MODULE$), (Function2)new Extraction$$anonfun.Extraction$$anonfun$6(ArrayProp, ArrayElem, OtherProp))).toList().sortWith((Function2)new Extraction$$anonfun.Extraction$$anonfun$7());
        return (JsonAST.JValue)uniquePaths.foldLeft((Object)package$.MODULE$.JNothing(), (Function2)new Extraction$$anonfun$unflatten.Extraction$$anonfun$unflatten$1((scala.collection.immutable.Map)map, useBigDecimalForDouble, useBigIntForLong, ArrayProp, ArrayElem, OtherProp));
    }
    
    public boolean unflatten$default$2() {
        return false;
    }
    
    public boolean unflatten$default$3() {
        return true;
    }
    
    public Object extract(final JsonAST.JValue json, final ScalaType scalaType, final Formats formats) {
        Object o;
        if (scalaType.isEither()) {
            o = Exception$.MODULE$.allCatch().opt((Function0)new Extraction$$anonfun$extract.Extraction$$anonfun$extract$1(json, scalaType, formats)).orElse((Function0)new Extraction$$anonfun$extract.Extraction$$anonfun$extract$2(json, scalaType, formats)).getOrElse((Function0)new Extraction$$anonfun$extract.Extraction$$anonfun$extract$3(json));
        }
        else if (scalaType.isOption()) {
            o = this.customOrElse(scalaType, json, (Function1<JsonAST.JValue, Object>)new Extraction$$anonfun$extract.Extraction$$anonfun$extract$4(scalaType, formats), formats);
        }
        else if (scalaType.isMap()) {
            o = this.customOrElse(scalaType, json, (Function1<JsonAST.JValue, Object>)new Extraction$$anonfun$extract.Extraction$$anonfun$extract$5(scalaType, formats), formats);
        }
        else if (scalaType.isCollection()) {
            o = this.customOrElse(scalaType, json, (Function1<JsonAST.JValue, Object>)new Extraction$$anonfun$extract.Extraction$$anonfun$extract$6(scalaType, formats), formats);
        }
        else {
            if (Tuple2.class.isAssignableFrom(scalaType.erasure()) && (String.class.isAssignableFrom(((ScalaType)scalaType.typeArgs().head()).erasure()) || Symbol.class.isAssignableFrom(((ScalaType)scalaType.typeArgs().head()).erasure()))) {
                final ScalaType ta = (ScalaType)scalaType.typeArgs().apply(1);
                if (json instanceof JsonAST.JObject) {
                    final List<Tuple2<String, JsonAST.JValue>> obj = ((JsonAST.JObject)json).obj();
                    if (obj instanceof $colon$colon) {
                        final $colon$colon $colon$colon = ($colon$colon)obj;
                        final Tuple2 xs = (Tuple2)$colon$colon.head();
                        if (Nil$.MODULE$.equals($colon$colon.tl$1())) {
                            o = (Symbol.class.isAssignableFrom(((ScalaType)scalaType.typeArgs().head()).erasure()) ? new Tuple2((Object)Symbol$.MODULE$.apply((String)xs._1()), this.extract((JsonAST.JValue)xs._2(), ta, formats)) : new Tuple2(xs._1(), this.extract((JsonAST.JValue)xs._2(), ta, formats)));
                            return o;
                        }
                    }
                }
                throw org.json4s.reflect.package$.MODULE$.fail(new StringBuilder().append((Object)"Expected object with 1 element but got ").append((Object)json).toString(), org.json4s.reflect.package$.MODULE$.fail$default$2());
            }
            o = this.customOrElse(scalaType, json, (Function1<JsonAST.JValue, Object>)new Extraction$$anonfun$extract.Extraction$$anonfun$extract$7(json, scalaType, formats), formats);
        }
        return o;
    }
    
    public Object org$json4s$Extraction$$extractDetectingNonTerminal(final JsonAST.JValue jvalue, final ScalaType typeArg, final Formats formats) {
        boolean b = false;
        JsonAST.JObject object = null;
        Label_0111: {
            if (jvalue instanceof JsonAST.JArray) {
                final JsonAST.JArray json = (JsonAST.JArray)jvalue;
                final Class<?> erasure = typeArg.erasure();
                final Class runtimeClass = scala.reflect.package$.MODULE$.Manifest().Object().runtimeClass();
                if (erasure == null) {
                    if (runtimeClass != null) {
                        break Label_0111;
                    }
                }
                else if (!erasure.equals(runtimeClass)) {
                    break Label_0111;
                }
                return this.extract(json, Reflector$.MODULE$.scalaTypeOf((scala.reflect.Manifest<Object>)ManifestFactory$.MODULE$.classType((Class)List.class, ManifestFactory$.MODULE$.Object(), (Seq)Predef$.MODULE$.wrapRefArray((Object[])new Manifest[0]))), formats);
            }
        }
        Label_0213: {
            if (jvalue instanceof JsonAST.JObject) {
                b = true;
                object = (JsonAST.JObject)jvalue;
                final Class<?> erasure2 = typeArg.erasure();
                final Class runtimeClass2 = scala.reflect.package$.MODULE$.Manifest().Object().runtimeClass();
                if (erasure2 == null) {
                    if (runtimeClass2 != null) {
                        break Label_0213;
                    }
                }
                else if (!erasure2.equals(runtimeClass2)) {
                    break Label_0213;
                }
                if (object.obj().exists((Function1)new Extraction$$anonfun$org$json4s$Extraction$$extractDetectingNonTerminal.Extraction$$anonfun$org$json4s$Extraction$$extractDetectingNonTerminal$1(formats))) {
                    return this.extract(object, Reflector$.MODULE$.scalaTypeOf((scala.reflect.Manifest<Object>)ManifestFactory$.MODULE$.Object()), formats);
                }
            }
        }
        if (b) {
            final Class<?> erasure3 = typeArg.erasure();
            final Class runtimeClass3 = scala.reflect.package$.MODULE$.Manifest().Object().runtimeClass();
            if (erasure3 == null) {
                if (runtimeClass3 != null) {
                    return this.extract(jvalue, typeArg, formats);
                }
            }
            else if (!erasure3.equals(runtimeClass3)) {
                return this.extract(jvalue, typeArg, formats);
            }
            return this.extract(object, Reflector$.MODULE$.scalaTypeOf((scala.reflect.Manifest<Object>)ManifestFactory$.MODULE$.classType((Class)scala.collection.immutable.Map.class, ManifestFactory$.MODULE$.classType((Class)String.class), (Seq)Predef$.MODULE$.wrapRefArray((Object[])new Manifest[] { ManifestFactory$.MODULE$.Object() }))), formats);
        }
        return this.extract(jvalue, typeArg, formats);
    }
    
    private Object customOrElse(final ScalaType target, final JsonAST.JValue json, final Function1<JsonAST.JValue, Object> thunk, final Formats formats) {
        final org.json4s.reflect.package.TypeInfo targetType = target.typeInfo();
        final PartialFunction custom = Formats$.MODULE$.customDeserializer((Tuple2<org.json4s.reflect.package.TypeInfo, JsonAST.JValue>)new Tuple2((Object)targetType, (Object)json), formats);
        return custom.applyOrElse((Object)new Tuple2((Object)targetType, (Object)json), (Function1)new Extraction$$anonfun$customOrElse.Extraction$$anonfun$customOrElse$1((Function1)thunk));
    }
    
    public Object org$json4s$Extraction$$convert(final String key, final ScalaType target, final Formats formats) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/json4s/reflect/ScalaType.erasure:()Ljava/lang/Class;
        //     4: astore          targetType
        //     6: aload           targetType
        //     8: astore          5
        //    10: aload           5
        //    12: ldc_w           Ljava/lang/String;.class
        //    15: astore          6
        //    17: dup            
        //    18: ifnonnull       30
        //    21: pop            
        //    22: aload           6
        //    24: ifnull          38
        //    27: goto            44
        //    30: aload           6
        //    32: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //    35: ifeq            44
        //    38: aload_1         /* key */
        //    39: astore          7
        //    41: goto            607
        //    44: aload           5
        //    46: ldc_w           Lscala/Symbol;.class
        //    49: astore          8
        //    51: dup            
        //    52: ifnonnull       64
        //    55: pop            
        //    56: aload           8
        //    58: ifnull          72
        //    61: goto            84
        //    64: aload           8
        //    66: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //    69: ifeq            84
        //    72: getstatic       scala/Symbol$.MODULE$:Lscala/Symbol$;
        //    75: aload_1         /* key */
        //    76: invokevirtual   scala/Symbol$.apply:(Ljava/lang/String;)Lscala/Symbol;
        //    79: astore          7
        //    81: goto            607
        //    84: aload           5
        //    86: getstatic       java/lang/Integer.TYPE:Ljava/lang/Class;
        //    89: astore          9
        //    91: dup            
        //    92: ifnonnull       104
        //    95: pop            
        //    96: aload           9
        //    98: ifnull          112
        //   101: goto            137
        //   104: aload           9
        //   106: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   109: ifeq            137
        //   112: new             Lscala/collection/immutable/StringOps;
        //   115: dup            
        //   116: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   119: aload_1         /* key */
        //   120: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //   123: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //   126: invokevirtual   scala/collection/immutable/StringOps.toInt:()I
        //   129: invokestatic    scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        //   132: astore          7
        //   134: goto            607
        //   137: aload           5
        //   139: ldc_w           Ljava/lang/Integer;.class
        //   142: astore          10
        //   144: dup            
        //   145: ifnonnull       157
        //   148: pop            
        //   149: aload           10
        //   151: ifnull          165
        //   154: goto            190
        //   157: aload           10
        //   159: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   162: ifeq            190
        //   165: new             Lscala/collection/immutable/StringOps;
        //   168: dup            
        //   169: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   172: aload_1         /* key */
        //   173: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //   176: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //   179: invokevirtual   scala/collection/immutable/StringOps.toInt:()I
        //   182: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   185: astore          7
        //   187: goto            607
        //   190: aload           5
        //   192: ldc_w           Lscala/math/BigInt;.class
        //   195: astore          11
        //   197: dup            
        //   198: ifnonnull       210
        //   201: pop            
        //   202: aload           11
        //   204: ifnull          218
        //   207: goto            243
        //   210: aload           11
        //   212: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   215: ifeq            243
        //   218: new             Lscala/collection/immutable/StringOps;
        //   221: dup            
        //   222: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   225: aload_1         /* key */
        //   226: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //   229: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //   232: invokevirtual   scala/collection/immutable/StringOps.toInt:()I
        //   235: invokestatic    scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        //   238: astore          7
        //   240: goto            607
        //   243: aload           5
        //   245: getstatic       java/lang/Long.TYPE:Ljava/lang/Class;
        //   248: astore          12
        //   250: dup            
        //   251: ifnonnull       263
        //   254: pop            
        //   255: aload           12
        //   257: ifnull          271
        //   260: goto            296
        //   263: aload           12
        //   265: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   268: ifeq            296
        //   271: new             Lscala/collection/immutable/StringOps;
        //   274: dup            
        //   275: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   278: aload_1         /* key */
        //   279: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //   282: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //   285: invokevirtual   scala/collection/immutable/StringOps.toLong:()J
        //   288: invokestatic    scala/runtime/BoxesRunTime.boxToLong:(J)Ljava/lang/Long;
        //   291: astore          7
        //   293: goto            607
        //   296: aload           5
        //   298: ldc_w           Ljava/lang/Long;.class
        //   301: astore          13
        //   303: dup            
        //   304: ifnonnull       316
        //   307: pop            
        //   308: aload           13
        //   310: ifnull          324
        //   313: goto            349
        //   316: aload           13
        //   318: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   321: ifeq            349
        //   324: new             Lscala/collection/immutable/StringOps;
        //   327: dup            
        //   328: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   331: aload_1         /* key */
        //   332: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //   335: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //   338: invokevirtual   scala/collection/immutable/StringOps.toLong:()J
        //   341: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   344: astore          7
        //   346: goto            607
        //   349: aload           5
        //   351: getstatic       java/lang/Short.TYPE:Ljava/lang/Class;
        //   354: astore          14
        //   356: dup            
        //   357: ifnonnull       369
        //   360: pop            
        //   361: aload           14
        //   363: ifnull          377
        //   366: goto            402
        //   369: aload           14
        //   371: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   374: ifeq            402
        //   377: new             Lscala/collection/immutable/StringOps;
        //   380: dup            
        //   381: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   384: aload_1         /* key */
        //   385: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //   388: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //   391: invokevirtual   scala/collection/immutable/StringOps.toShort:()S
        //   394: invokestatic    scala/runtime/BoxesRunTime.boxToShort:(S)Ljava/lang/Short;
        //   397: astore          7
        //   399: goto            607
        //   402: aload           5
        //   404: ldc_w           Ljava/lang/Short;.class
        //   407: astore          15
        //   409: dup            
        //   410: ifnonnull       422
        //   413: pop            
        //   414: aload           15
        //   416: ifnull          430
        //   419: goto            455
        //   422: aload           15
        //   424: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   427: ifeq            455
        //   430: new             Lscala/collection/immutable/StringOps;
        //   433: dup            
        //   434: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   437: aload_1         /* key */
        //   438: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //   441: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //   444: invokevirtual   scala/collection/immutable/StringOps.toShort:()S
        //   447: invokestatic    java/lang/Short.valueOf:(S)Ljava/lang/Short;
        //   450: astore          7
        //   452: goto            607
        //   455: aload           5
        //   457: ldc_w           Ljava/util/Date;.class
        //   460: astore          16
        //   462: dup            
        //   463: ifnonnull       475
        //   466: pop            
        //   467: aload           16
        //   469: ifnull          483
        //   472: goto            494
        //   475: aload           16
        //   477: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   480: ifeq            494
        //   483: aload_0         /* this */
        //   484: aload_1         /* key */
        //   485: aload_3         /* formats */
        //   486: invokespecial   org/json4s/Extraction$.formatDate:(Ljava/lang/String;Lorg/json4s/Formats;)Ljava/util/Date;
        //   489: astore          7
        //   491: goto            607
        //   494: aload           5
        //   496: ldc_w           Ljava/sql/Timestamp;.class
        //   499: astore          17
        //   501: dup            
        //   502: ifnonnull       514
        //   505: pop            
        //   506: aload           17
        //   508: ifnull          522
        //   511: goto            533
        //   514: aload           17
        //   516: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   519: ifeq            533
        //   522: aload_0         /* this */
        //   523: aload_1         /* key */
        //   524: aload_3         /* formats */
        //   525: invokespecial   org/json4s/Extraction$.formatTimestamp:(Ljava/lang/String;Lorg/json4s/Formats;)Ljava/sql/Timestamp;
        //   528: astore          7
        //   530: goto            607
        //   533: getstatic       org/json4s/package$.MODULE$:Lorg/json4s/package$;
        //   536: invokevirtual   org/json4s/package$.TypeInfo:()Lorg/json4s/reflect/package$TypeInfo$;
        //   539: aload           targetType
        //   541: getstatic       scala/None$.MODULE$:Lscala/None$;
        //   544: invokevirtual   org/json4s/reflect/package$TypeInfo$.apply:(Ljava/lang/Class;Lscala/Option;)Lorg/json4s/reflect/package$TypeInfo;
        //   547: astore          typeInfo
        //   549: getstatic       org/json4s/Formats$.MODULE$:Lorg/json4s/Formats$;
        //   552: new             Lscala/Tuple2;
        //   555: dup            
        //   556: aload           typeInfo
        //   558: aload_1         /* key */
        //   559: invokespecial   scala/Tuple2.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   562: aload_3         /* formats */
        //   563: invokevirtual   org/json4s/Formats$.customKeyDeserializer:(Lscala/Tuple2;Lorg/json4s/Formats;)Lscala/PartialFunction;
        //   566: astore          deserializer
        //   568: aload           deserializer
        //   570: new             Lscala/Tuple2;
        //   573: dup            
        //   574: aload           typeInfo
        //   576: aload_1         /* key */
        //   577: invokespecial   scala/Tuple2.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   580: invokeinterface scala/PartialFunction.isDefinedAt:(Ljava/lang/Object;)Z
        //   585: ifeq            610
        //   588: aload           deserializer
        //   590: new             Lscala/Tuple2;
        //   593: dup            
        //   594: aload           typeInfo
        //   596: aload_1         /* key */
        //   597: invokespecial   scala/Tuple2.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   600: invokeinterface scala/PartialFunction.apply:(Ljava/lang/Object;)Ljava/lang/Object;
        //   605: astore          7
        //   607: aload           7
        //   609: areturn        
        //   610: getstatic       org/json4s/reflect/package$.MODULE$:Lorg/json4s/reflect/package$;
        //   613: new             Lscala/collection/mutable/StringBuilder;
        //   616: dup            
        //   617: invokespecial   scala/collection/mutable/StringBuilder.<init>:()V
        //   620: ldc_w           "Do not know how to deserialize key of type "
        //   623: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //   626: aload           4
        //   628: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //   631: ldc_w           ". Consider implementing a CustomKeyDeserializer."
        //   634: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //   637: invokevirtual   scala/collection/mutable/StringBuilder.toString:()Ljava/lang/String;
        //   640: getstatic       org/json4s/reflect/package$.MODULE$:Lorg/json4s/reflect/package$;
        //   643: invokevirtual   org/json4s/reflect/package$.fail$default$2:()Ljava/lang/Exception;
        //   646: invokevirtual   org/json4s/reflect/package$.fail:(Ljava/lang/String;Ljava/lang/Exception;)Lscala/runtime/Nothing$;
        //   649: athrow         
        //    StackMapTable: 00 23 FF 00 1E 00 07 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 01 07 00 20 07 05 FF 00 13 00 09 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 00 01 07 00 20 07 0B FF 00 13 00 0A 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 07 00 20 00 01 07 00 20 07 18 FF 00 13 00 0B 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 07 00 20 07 00 20 00 01 07 00 20 07 18 FF 00 13 00 0C 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 07 00 20 07 00 20 07 00 20 00 01 07 00 20 07 18 FF 00 13 00 0D 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 00 01 07 00 20 07 18 FF 00 13 00 0E 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 00 01 07 00 20 07 18 FF 00 13 00 0F 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 00 01 07 00 20 07 18 FF 00 13 00 10 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 00 01 07 00 20 07 18 FF 00 13 00 11 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 00 01 07 00 20 07 0A FF 00 13 00 12 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 00 01 07 00 20 07 0A FF 00 49 00 08 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 07 00 04 00 00 FF 00 02 00 14 07 00 02 07 01 22 07 02 98 07 00 6D 07 00 20 07 00 20 07 00 20 00 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 00 20 07 03 7E 07 00 D0 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public Object org$json4s$Extraction$$convert(final JsonAST.JValue json, final ScalaType target, final Formats formats, final Option<Function0<Object>> default) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/json4s/reflect/ScalaType.erasure:()Ljava/lang/Class;
        //     4: astore          targetType
        //     6: iconst_0       
        //     7: istore          6
        //     9: aconst_null    
        //    10: astore          7
        //    12: iconst_0       
        //    13: istore          8
        //    15: aconst_null    
        //    16: astore          9
        //    18: iconst_0       
        //    19: istore          10
        //    21: aconst_null    
        //    22: astore          11
        //    24: iconst_0       
        //    25: istore          12
        //    27: aconst_null    
        //    28: astore          13
        //    30: iconst_0       
        //    31: istore          14
        //    33: aconst_null    
        //    34: astore          15
        //    36: iconst_0       
        //    37: istore          16
        //    39: aconst_null    
        //    40: astore          17
        //    42: iconst_0       
        //    43: istore          18
        //    45: aconst_null    
        //    46: astore          19
        //    48: aload_1         /* json */
        //    49: astore          20
        //    51: aload           20
        //    53: instanceof      Lorg/json4s/JsonAST$JInt;
        //    56: ifeq            117
        //    59: iconst_1       
        //    60: istore          6
        //    62: aload           20
        //    64: checkcast       Lorg/json4s/JsonAST$JInt;
        //    67: astore          7
        //    69: aload           7
        //    71: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //    74: astore          x
        //    76: aload           targetType
        //    78: getstatic       java/lang/Integer.TYPE:Ljava/lang/Class;
        //    81: astore          22
        //    83: dup            
        //    84: ifnonnull       96
        //    87: pop            
        //    88: aload           22
        //    90: ifnull          104
        //    93: goto            117
        //    96: aload           22
        //    98: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   101: ifeq            117
        //   104: aload           x
        //   106: invokevirtual   scala/math/BigInt.intValue:()I
        //   109: invokestatic    scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        //   112: astore          23
        //   114: goto            3738
        //   117: iload           6
        //   119: ifeq            170
        //   122: aload           7
        //   124: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   127: astore          x
        //   129: aload           targetType
        //   131: ldc_w           Ljava/lang/Integer;.class
        //   134: astore          25
        //   136: dup            
        //   137: ifnonnull       149
        //   140: pop            
        //   141: aload           25
        //   143: ifnull          157
        //   146: goto            170
        //   149: aload           25
        //   151: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   154: ifeq            170
        //   157: aload           x
        //   159: invokevirtual   scala/math/BigInt.intValue:()I
        //   162: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   165: astore          23
        //   167: goto            3738
        //   170: iload           6
        //   172: ifeq            217
        //   175: aload           7
        //   177: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   180: astore          x
        //   182: aload           targetType
        //   184: ldc_w           Lscala/math/BigInt;.class
        //   187: astore          27
        //   189: dup            
        //   190: ifnonnull       202
        //   193: pop            
        //   194: aload           27
        //   196: ifnull          210
        //   199: goto            217
        //   202: aload           27
        //   204: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   207: ifeq            217
        //   210: aload           x
        //   212: astore          23
        //   214: goto            3738
        //   217: iload           6
        //   219: ifeq            270
        //   222: aload           7
        //   224: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   227: astore          x
        //   229: aload           targetType
        //   231: getstatic       java/lang/Long.TYPE:Ljava/lang/Class;
        //   234: astore          29
        //   236: dup            
        //   237: ifnonnull       249
        //   240: pop            
        //   241: aload           29
        //   243: ifnull          257
        //   246: goto            270
        //   249: aload           29
        //   251: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   254: ifeq            270
        //   257: aload           x
        //   259: invokevirtual   scala/math/BigInt.longValue:()J
        //   262: invokestatic    scala/runtime/BoxesRunTime.boxToLong:(J)Ljava/lang/Long;
        //   265: astore          23
        //   267: goto            3738
        //   270: iload           6
        //   272: ifeq            323
        //   275: aload           7
        //   277: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   280: astore          x
        //   282: aload           targetType
        //   284: ldc_w           Ljava/lang/Long;.class
        //   287: astore          31
        //   289: dup            
        //   290: ifnonnull       302
        //   293: pop            
        //   294: aload           31
        //   296: ifnull          310
        //   299: goto            323
        //   302: aload           31
        //   304: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   307: ifeq            323
        //   310: aload           x
        //   312: invokevirtual   scala/math/BigInt.longValue:()J
        //   315: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   318: astore          23
        //   320: goto            3738
        //   323: iload           6
        //   325: ifeq            376
        //   328: aload           7
        //   330: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   333: astore          x
        //   335: aload           targetType
        //   337: getstatic       java/lang/Double.TYPE:Ljava/lang/Class;
        //   340: astore          33
        //   342: dup            
        //   343: ifnonnull       355
        //   346: pop            
        //   347: aload           33
        //   349: ifnull          363
        //   352: goto            376
        //   355: aload           33
        //   357: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   360: ifeq            376
        //   363: aload           x
        //   365: invokevirtual   scala/math/BigInt.doubleValue:()D
        //   368: invokestatic    scala/runtime/BoxesRunTime.boxToDouble:(D)Ljava/lang/Double;
        //   371: astore          23
        //   373: goto            3738
        //   376: iload           6
        //   378: ifeq            428
        //   381: aload           7
        //   383: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   386: astore          x
        //   388: aload           targetType
        //   390: ldc             Ljava/lang/Double;.class
        //   392: astore          35
        //   394: dup            
        //   395: ifnonnull       407
        //   398: pop            
        //   399: aload           35
        //   401: ifnull          415
        //   404: goto            428
        //   407: aload           35
        //   409: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   412: ifeq            428
        //   415: aload           x
        //   417: invokevirtual   scala/math/BigInt.doubleValue:()D
        //   420: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
        //   423: astore          23
        //   425: goto            3738
        //   428: iload           6
        //   430: ifeq            481
        //   433: aload           7
        //   435: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   438: astore          x
        //   440: aload           targetType
        //   442: getstatic       java/lang/Float.TYPE:Ljava/lang/Class;
        //   445: astore          37
        //   447: dup            
        //   448: ifnonnull       460
        //   451: pop            
        //   452: aload           37
        //   454: ifnull          468
        //   457: goto            481
        //   460: aload           37
        //   462: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   465: ifeq            481
        //   468: aload           x
        //   470: invokevirtual   scala/math/BigInt.floatValue:()F
        //   473: invokestatic    scala/runtime/BoxesRunTime.boxToFloat:(F)Ljava/lang/Float;
        //   476: astore          23
        //   478: goto            3738
        //   481: iload           6
        //   483: ifeq            533
        //   486: aload           7
        //   488: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   491: astore          x
        //   493: aload           targetType
        //   495: ldc             Ljava/lang/Float;.class
        //   497: astore          39
        //   499: dup            
        //   500: ifnonnull       512
        //   503: pop            
        //   504: aload           39
        //   506: ifnull          520
        //   509: goto            533
        //   512: aload           39
        //   514: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   517: ifeq            533
        //   520: aload           x
        //   522: invokevirtual   scala/math/BigInt.floatValue:()F
        //   525: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //   528: astore          23
        //   530: goto            3738
        //   533: iload           6
        //   535: ifeq            586
        //   538: aload           7
        //   540: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   543: astore          x
        //   545: aload           targetType
        //   547: getstatic       java/lang/Short.TYPE:Ljava/lang/Class;
        //   550: astore          41
        //   552: dup            
        //   553: ifnonnull       565
        //   556: pop            
        //   557: aload           41
        //   559: ifnull          573
        //   562: goto            586
        //   565: aload           41
        //   567: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   570: ifeq            586
        //   573: aload           x
        //   575: invokevirtual   scala/math/BigInt.shortValue:()S
        //   578: invokestatic    scala/runtime/BoxesRunTime.boxToShort:(S)Ljava/lang/Short;
        //   581: astore          23
        //   583: goto            3738
        //   586: iload           6
        //   588: ifeq            639
        //   591: aload           7
        //   593: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   596: astore          x
        //   598: aload           targetType
        //   600: ldc_w           Ljava/lang/Short;.class
        //   603: astore          43
        //   605: dup            
        //   606: ifnonnull       618
        //   609: pop            
        //   610: aload           43
        //   612: ifnull          626
        //   615: goto            639
        //   618: aload           43
        //   620: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   623: ifeq            639
        //   626: aload           x
        //   628: invokevirtual   scala/math/BigInt.shortValue:()S
        //   631: invokestatic    java/lang/Short.valueOf:(S)Ljava/lang/Short;
        //   634: astore          23
        //   636: goto            3738
        //   639: iload           6
        //   641: ifeq            692
        //   644: aload           7
        //   646: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   649: astore          x
        //   651: aload           targetType
        //   653: getstatic       java/lang/Byte.TYPE:Ljava/lang/Class;
        //   656: astore          45
        //   658: dup            
        //   659: ifnonnull       671
        //   662: pop            
        //   663: aload           45
        //   665: ifnull          679
        //   668: goto            692
        //   671: aload           45
        //   673: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   676: ifeq            692
        //   679: aload           x
        //   681: invokevirtual   scala/math/BigInt.byteValue:()B
        //   684: invokestatic    scala/runtime/BoxesRunTime.boxToByte:(B)Ljava/lang/Byte;
        //   687: astore          23
        //   689: goto            3738
        //   692: iload           6
        //   694: ifeq            745
        //   697: aload           7
        //   699: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   702: astore          x
        //   704: aload           targetType
        //   706: ldc_w           Ljava/lang/Byte;.class
        //   709: astore          47
        //   711: dup            
        //   712: ifnonnull       724
        //   715: pop            
        //   716: aload           47
        //   718: ifnull          732
        //   721: goto            745
        //   724: aload           47
        //   726: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   729: ifeq            745
        //   732: aload           x
        //   734: invokevirtual   scala/math/BigInt.byteValue:()B
        //   737: invokestatic    java/lang/Byte.valueOf:(B)Ljava/lang/Byte;
        //   740: astore          23
        //   742: goto            3738
        //   745: iload           6
        //   747: ifeq            795
        //   750: aload           7
        //   752: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   755: astore          x
        //   757: aload           targetType
        //   759: ldc_w           Ljava/lang/String;.class
        //   762: astore          49
        //   764: dup            
        //   765: ifnonnull       777
        //   768: pop            
        //   769: aload           49
        //   771: ifnull          785
        //   774: goto            795
        //   777: aload           49
        //   779: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   782: ifeq            795
        //   785: aload           x
        //   787: invokevirtual   scala/math/BigInt.toString:()Ljava/lang/String;
        //   790: astore          23
        //   792: goto            3738
        //   795: iload           6
        //   797: ifeq            848
        //   800: aload           7
        //   802: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   805: astore          x
        //   807: aload           targetType
        //   809: ldc_w           Ljava/lang/Number;.class
        //   812: astore          51
        //   814: dup            
        //   815: ifnonnull       827
        //   818: pop            
        //   819: aload           51
        //   821: ifnull          835
        //   824: goto            848
        //   827: aload           51
        //   829: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   832: ifeq            848
        //   835: aload           x
        //   837: invokevirtual   scala/math/BigInt.longValue:()J
        //   840: invokestatic    scala/runtime/BoxesRunTime.boxToLong:(J)Ljava/lang/Long;
        //   843: astore          23
        //   845: goto            3738
        //   848: iload           6
        //   850: ifeq            904
        //   853: aload           7
        //   855: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   858: astore          x
        //   860: aload           targetType
        //   862: ldc_w           Lscala/math/BigDecimal;.class
        //   865: astore          53
        //   867: dup            
        //   868: ifnonnull       880
        //   871: pop            
        //   872: aload           53
        //   874: ifnull          888
        //   877: goto            904
        //   880: aload           53
        //   882: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   885: ifeq            904
        //   888: getstatic       scala/package$.MODULE$:Lscala/package$;
        //   891: invokevirtual   scala/package$.BigDecimal:()Lscala/math/BigDecimal$;
        //   894: aload           x
        //   896: invokevirtual   scala/math/BigDecimal$.apply:(Lscala/math/BigInt;)Lscala/math/BigDecimal;
        //   899: astore          23
        //   901: goto            3738
        //   904: iload           6
        //   906: ifeq            963
        //   909: aload           7
        //   911: invokevirtual   org/json4s/JsonAST$JInt.num:()Lscala/math/BigInt;
        //   914: astore          x
        //   916: aload           targetType
        //   918: ldc_w           Ljava/math/BigDecimal;.class
        //   921: astore          55
        //   923: dup            
        //   924: ifnonnull       936
        //   927: pop            
        //   928: aload           55
        //   930: ifnull          944
        //   933: goto            963
        //   936: aload           55
        //   938: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   941: ifeq            963
        //   944: getstatic       scala/package$.MODULE$:Lscala/package$;
        //   947: invokevirtual   scala/package$.BigDecimal:()Lscala/math/BigDecimal$;
        //   950: aload           x
        //   952: invokevirtual   scala/math/BigDecimal$.apply:(Lscala/math/BigInt;)Lscala/math/BigDecimal;
        //   955: invokevirtual   scala/math/BigDecimal.bigDecimal:()Ljava/math/BigDecimal;
        //   958: astore          23
        //   960: goto            3738
        //   963: aload           20
        //   965: instanceof      Lorg/json4s/JsonAST$JLong;
        //   968: ifeq            1035
        //   971: iconst_1       
        //   972: istore          8
        //   974: aload           20
        //   976: checkcast       Lorg/json4s/JsonAST$JLong;
        //   979: astore          9
        //   981: aload           9
        //   983: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //   986: lstore          x
        //   988: aload           targetType
        //   990: getstatic       java/lang/Integer.TYPE:Ljava/lang/Class;
        //   993: astore          58
        //   995: dup            
        //   996: ifnonnull       1008
        //   999: pop            
        //  1000: aload           58
        //  1002: ifnull          1016
        //  1005: goto            1035
        //  1008: aload           58
        //  1010: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1013: ifeq            1035
        //  1016: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1019: lload           x
        //  1021: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1024: invokevirtual   java/lang/Long.intValue:()I
        //  1027: invokestatic    scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        //  1030: astore          23
        //  1032: goto            3738
        //  1035: iload           8
        //  1037: ifeq            1094
        //  1040: aload           9
        //  1042: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1045: lstore          x
        //  1047: aload           targetType
        //  1049: ldc_w           Ljava/lang/Integer;.class
        //  1052: astore          61
        //  1054: dup            
        //  1055: ifnonnull       1067
        //  1058: pop            
        //  1059: aload           61
        //  1061: ifnull          1075
        //  1064: goto            1094
        //  1067: aload           61
        //  1069: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1072: ifeq            1094
        //  1075: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1078: lload           x
        //  1080: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1083: invokevirtual   java/lang/Long.intValue:()I
        //  1086: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1089: astore          23
        //  1091: goto            3738
        //  1094: iload           8
        //  1096: ifeq            1144
        //  1099: aload           9
        //  1101: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1104: lstore          x
        //  1106: aload           targetType
        //  1108: ldc_w           Lscala/math/BigInt;.class
        //  1111: astore          64
        //  1113: dup            
        //  1114: ifnonnull       1126
        //  1117: pop            
        //  1118: aload           64
        //  1120: ifnull          1134
        //  1123: goto            1144
        //  1126: aload           64
        //  1128: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1131: ifeq            1144
        //  1134: lload           x
        //  1136: invokestatic    scala/runtime/BoxesRunTime.boxToLong:(J)Ljava/lang/Long;
        //  1139: astore          23
        //  1141: goto            3738
        //  1144: iload           8
        //  1146: ifeq            1203
        //  1149: aload           9
        //  1151: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1154: lstore          x
        //  1156: aload           targetType
        //  1158: getstatic       java/lang/Long.TYPE:Ljava/lang/Class;
        //  1161: astore          67
        //  1163: dup            
        //  1164: ifnonnull       1176
        //  1167: pop            
        //  1168: aload           67
        //  1170: ifnull          1184
        //  1173: goto            1203
        //  1176: aload           67
        //  1178: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1181: ifeq            1203
        //  1184: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1187: lload           x
        //  1189: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1192: invokevirtual   java/lang/Long.longValue:()J
        //  1195: invokestatic    scala/runtime/BoxesRunTime.boxToLong:(J)Ljava/lang/Long;
        //  1198: astore          23
        //  1200: goto            3738
        //  1203: iload           8
        //  1205: ifeq            1253
        //  1208: aload           9
        //  1210: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1213: lstore          x
        //  1215: aload           targetType
        //  1217: ldc_w           Ljava/lang/Long;.class
        //  1220: astore          70
        //  1222: dup            
        //  1223: ifnonnull       1235
        //  1226: pop            
        //  1227: aload           70
        //  1229: ifnull          1243
        //  1232: goto            1253
        //  1235: aload           70
        //  1237: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1240: ifeq            1253
        //  1243: lload           x
        //  1245: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1248: astore          23
        //  1250: goto            3738
        //  1253: iload           8
        //  1255: ifeq            1312
        //  1258: aload           9
        //  1260: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1263: lstore          x
        //  1265: aload           targetType
        //  1267: getstatic       java/lang/Double.TYPE:Ljava/lang/Class;
        //  1270: astore          73
        //  1272: dup            
        //  1273: ifnonnull       1285
        //  1276: pop            
        //  1277: aload           73
        //  1279: ifnull          1293
        //  1282: goto            1312
        //  1285: aload           73
        //  1287: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1290: ifeq            1312
        //  1293: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1296: lload           x
        //  1298: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1301: invokevirtual   java/lang/Long.doubleValue:()D
        //  1304: invokestatic    scala/runtime/BoxesRunTime.boxToDouble:(D)Ljava/lang/Double;
        //  1307: astore          23
        //  1309: goto            3738
        //  1312: iload           8
        //  1314: ifeq            1370
        //  1317: aload           9
        //  1319: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1322: lstore          x
        //  1324: aload           targetType
        //  1326: ldc             Ljava/lang/Double;.class
        //  1328: astore          76
        //  1330: dup            
        //  1331: ifnonnull       1343
        //  1334: pop            
        //  1335: aload           76
        //  1337: ifnull          1351
        //  1340: goto            1370
        //  1343: aload           76
        //  1345: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1348: ifeq            1370
        //  1351: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1354: lload           x
        //  1356: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1359: invokevirtual   java/lang/Long.doubleValue:()D
        //  1362: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
        //  1365: astore          23
        //  1367: goto            3738
        //  1370: iload           8
        //  1372: ifeq            1429
        //  1375: aload           9
        //  1377: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1380: lstore          x
        //  1382: aload           targetType
        //  1384: getstatic       java/lang/Float.TYPE:Ljava/lang/Class;
        //  1387: astore          79
        //  1389: dup            
        //  1390: ifnonnull       1402
        //  1393: pop            
        //  1394: aload           79
        //  1396: ifnull          1410
        //  1399: goto            1429
        //  1402: aload           79
        //  1404: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1407: ifeq            1429
        //  1410: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1413: lload           x
        //  1415: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1418: invokevirtual   java/lang/Long.floatValue:()F
        //  1421: invokestatic    scala/runtime/BoxesRunTime.boxToFloat:(F)Ljava/lang/Float;
        //  1424: astore          23
        //  1426: goto            3738
        //  1429: iload           8
        //  1431: ifeq            1487
        //  1434: aload           9
        //  1436: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1439: lstore          x
        //  1441: aload           targetType
        //  1443: ldc             Ljava/lang/Float;.class
        //  1445: astore          82
        //  1447: dup            
        //  1448: ifnonnull       1460
        //  1451: pop            
        //  1452: aload           82
        //  1454: ifnull          1468
        //  1457: goto            1487
        //  1460: aload           82
        //  1462: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1465: ifeq            1487
        //  1468: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1471: lload           x
        //  1473: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1476: invokevirtual   java/lang/Long.floatValue:()F
        //  1479: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //  1482: astore          23
        //  1484: goto            3738
        //  1487: iload           8
        //  1489: ifeq            1546
        //  1492: aload           9
        //  1494: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1497: lstore          x
        //  1499: aload           targetType
        //  1501: getstatic       java/lang/Short.TYPE:Ljava/lang/Class;
        //  1504: astore          85
        //  1506: dup            
        //  1507: ifnonnull       1519
        //  1510: pop            
        //  1511: aload           85
        //  1513: ifnull          1527
        //  1516: goto            1546
        //  1519: aload           85
        //  1521: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1524: ifeq            1546
        //  1527: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1530: lload           x
        //  1532: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1535: invokevirtual   java/lang/Long.shortValue:()S
        //  1538: invokestatic    scala/runtime/BoxesRunTime.boxToShort:(S)Ljava/lang/Short;
        //  1541: astore          23
        //  1543: goto            3738
        //  1546: iload           8
        //  1548: ifeq            1605
        //  1551: aload           9
        //  1553: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1556: lstore          x
        //  1558: aload           targetType
        //  1560: ldc_w           Ljava/lang/Short;.class
        //  1563: astore          88
        //  1565: dup            
        //  1566: ifnonnull       1578
        //  1569: pop            
        //  1570: aload           88
        //  1572: ifnull          1586
        //  1575: goto            1605
        //  1578: aload           88
        //  1580: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1583: ifeq            1605
        //  1586: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1589: lload           x
        //  1591: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1594: invokevirtual   java/lang/Long.shortValue:()S
        //  1597: invokestatic    java/lang/Short.valueOf:(S)Ljava/lang/Short;
        //  1600: astore          23
        //  1602: goto            3738
        //  1605: iload           8
        //  1607: ifeq            1664
        //  1610: aload           9
        //  1612: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1615: lstore          x
        //  1617: aload           targetType
        //  1619: getstatic       java/lang/Byte.TYPE:Ljava/lang/Class;
        //  1622: astore          91
        //  1624: dup            
        //  1625: ifnonnull       1637
        //  1628: pop            
        //  1629: aload           91
        //  1631: ifnull          1645
        //  1634: goto            1664
        //  1637: aload           91
        //  1639: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1642: ifeq            1664
        //  1645: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1648: lload           x
        //  1650: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1653: invokevirtual   java/lang/Long.byteValue:()B
        //  1656: invokestatic    scala/runtime/BoxesRunTime.boxToByte:(B)Ljava/lang/Byte;
        //  1659: astore          23
        //  1661: goto            3738
        //  1664: iload           8
        //  1666: ifeq            1723
        //  1669: aload           9
        //  1671: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1674: lstore          x
        //  1676: aload           targetType
        //  1678: ldc_w           Ljava/lang/Byte;.class
        //  1681: astore          94
        //  1683: dup            
        //  1684: ifnonnull       1696
        //  1687: pop            
        //  1688: aload           94
        //  1690: ifnull          1704
        //  1693: goto            1723
        //  1696: aload           94
        //  1698: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1701: ifeq            1723
        //  1704: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1707: lload           x
        //  1709: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1712: invokevirtual   java/lang/Long.byteValue:()B
        //  1715: invokestatic    java/lang/Byte.valueOf:(B)Ljava/lang/Byte;
        //  1718: astore          23
        //  1720: goto            3738
        //  1723: iload           8
        //  1725: ifeq            1776
        //  1728: aload           9
        //  1730: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1733: lstore          x
        //  1735: aload           targetType
        //  1737: ldc_w           Ljava/lang/String;.class
        //  1740: astore          97
        //  1742: dup            
        //  1743: ifnonnull       1755
        //  1746: pop            
        //  1747: aload           97
        //  1749: ifnull          1763
        //  1752: goto            1776
        //  1755: aload           97
        //  1757: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1760: ifeq            1776
        //  1763: lload           x
        //  1765: invokestatic    scala/runtime/BoxesRunTime.boxToLong:(J)Ljava/lang/Long;
        //  1768: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  1771: astore          23
        //  1773: goto            3738
        //  1776: iload           8
        //  1778: ifeq            1835
        //  1781: aload           9
        //  1783: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1786: lstore          x
        //  1788: aload           targetType
        //  1790: ldc_w           Ljava/lang/Number;.class
        //  1793: astore          100
        //  1795: dup            
        //  1796: ifnonnull       1808
        //  1799: pop            
        //  1800: aload           100
        //  1802: ifnull          1816
        //  1805: goto            1835
        //  1808: aload           100
        //  1810: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1813: ifeq            1835
        //  1816: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  1819: lload           x
        //  1821: invokevirtual   scala/Predef$.long2Long:(J)Ljava/lang/Long;
        //  1824: invokevirtual   java/lang/Long.longValue:()J
        //  1827: invokestatic    scala/runtime/BoxesRunTime.boxToLong:(J)Ljava/lang/Long;
        //  1830: astore          23
        //  1832: goto            3738
        //  1835: iload           8
        //  1837: ifeq            1891
        //  1840: aload           9
        //  1842: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1845: lstore          x
        //  1847: aload           targetType
        //  1849: ldc_w           Lscala/math/BigDecimal;.class
        //  1852: astore          103
        //  1854: dup            
        //  1855: ifnonnull       1867
        //  1858: pop            
        //  1859: aload           103
        //  1861: ifnull          1875
        //  1864: goto            1891
        //  1867: aload           103
        //  1869: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1872: ifeq            1891
        //  1875: getstatic       scala/package$.MODULE$:Lscala/package$;
        //  1878: invokevirtual   scala/package$.BigDecimal:()Lscala/math/BigDecimal$;
        //  1881: lload           x
        //  1883: invokevirtual   scala/math/BigDecimal$.apply:(J)Lscala/math/BigDecimal;
        //  1886: astore          23
        //  1888: goto            3738
        //  1891: iload           8
        //  1893: ifeq            1950
        //  1896: aload           9
        //  1898: invokevirtual   org/json4s/JsonAST$JLong.num:()J
        //  1901: lstore          x
        //  1903: aload           targetType
        //  1905: ldc_w           Ljava/math/BigDecimal;.class
        //  1908: astore          106
        //  1910: dup            
        //  1911: ifnonnull       1923
        //  1914: pop            
        //  1915: aload           106
        //  1917: ifnull          1931
        //  1920: goto            1950
        //  1923: aload           106
        //  1925: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1928: ifeq            1950
        //  1931: getstatic       scala/package$.MODULE$:Lscala/package$;
        //  1934: invokevirtual   scala/package$.BigDecimal:()Lscala/math/BigDecimal$;
        //  1937: lload           x
        //  1939: invokevirtual   scala/math/BigDecimal$.apply:(J)Lscala/math/BigDecimal;
        //  1942: invokevirtual   scala/math/BigDecimal.bigDecimal:()Ljava/math/BigDecimal;
        //  1945: astore          23
        //  1947: goto            3738
        //  1950: aload           20
        //  1952: instanceof      Lorg/json4s/JsonAST$JDouble;
        //  1955: ifeq            2013
        //  1958: iconst_1       
        //  1959: istore          10
        //  1961: aload           20
        //  1963: checkcast       Lorg/json4s/JsonAST$JDouble;
        //  1966: astore          11
        //  1968: aload           11
        //  1970: invokevirtual   org/json4s/JsonAST$JDouble.num:()D
        //  1973: dstore          x
        //  1975: aload           targetType
        //  1977: getstatic       java/lang/Double.TYPE:Ljava/lang/Class;
        //  1980: astore          109
        //  1982: dup            
        //  1983: ifnonnull       1995
        //  1986: pop            
        //  1987: aload           109
        //  1989: ifnull          2003
        //  1992: goto            2013
        //  1995: aload           109
        //  1997: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2000: ifeq            2013
        //  2003: dload           x
        //  2005: invokestatic    scala/runtime/BoxesRunTime.boxToDouble:(D)Ljava/lang/Double;
        //  2008: astore          23
        //  2010: goto            3738
        //  2013: iload           10
        //  2015: ifeq            2062
        //  2018: aload           11
        //  2020: invokevirtual   org/json4s/JsonAST$JDouble.num:()D
        //  2023: dstore          x
        //  2025: aload           targetType
        //  2027: ldc             Ljava/lang/Double;.class
        //  2029: astore          112
        //  2031: dup            
        //  2032: ifnonnull       2044
        //  2035: pop            
        //  2036: aload           112
        //  2038: ifnull          2052
        //  2041: goto            2062
        //  2044: aload           112
        //  2046: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2049: ifeq            2062
        //  2052: dload           x
        //  2054: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
        //  2057: astore          23
        //  2059: goto            3738
        //  2062: iload           10
        //  2064: ifeq            2121
        //  2067: aload           11
        //  2069: invokevirtual   org/json4s/JsonAST$JDouble.num:()D
        //  2072: dstore          x
        //  2074: aload           targetType
        //  2076: getstatic       java/lang/Float.TYPE:Ljava/lang/Class;
        //  2079: astore          115
        //  2081: dup            
        //  2082: ifnonnull       2094
        //  2085: pop            
        //  2086: aload           115
        //  2088: ifnull          2102
        //  2091: goto            2121
        //  2094: aload           115
        //  2096: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2099: ifeq            2121
        //  2102: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  2105: dload           x
        //  2107: invokevirtual   scala/Predef$.double2Double:(D)Ljava/lang/Double;
        //  2110: invokevirtual   java/lang/Double.floatValue:()F
        //  2113: invokestatic    scala/runtime/BoxesRunTime.boxToFloat:(F)Ljava/lang/Float;
        //  2116: astore          23
        //  2118: goto            3738
        //  2121: iload           10
        //  2123: ifeq            2179
        //  2126: aload           11
        //  2128: invokevirtual   org/json4s/JsonAST$JDouble.num:()D
        //  2131: dstore          x
        //  2133: aload           targetType
        //  2135: ldc             Ljava/lang/Float;.class
        //  2137: astore          118
        //  2139: dup            
        //  2140: ifnonnull       2152
        //  2143: pop            
        //  2144: aload           118
        //  2146: ifnull          2160
        //  2149: goto            2179
        //  2152: aload           118
        //  2154: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2157: ifeq            2179
        //  2160: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  2163: dload           x
        //  2165: invokevirtual   scala/Predef$.double2Double:(D)Ljava/lang/Double;
        //  2168: invokevirtual   java/lang/Double.floatValue:()F
        //  2171: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //  2174: astore          23
        //  2176: goto            3738
        //  2179: iload           10
        //  2181: ifeq            2232
        //  2184: aload           11
        //  2186: invokevirtual   org/json4s/JsonAST$JDouble.num:()D
        //  2189: dstore          x
        //  2191: aload           targetType
        //  2193: ldc_w           Ljava/lang/String;.class
        //  2196: astore          121
        //  2198: dup            
        //  2199: ifnonnull       2211
        //  2202: pop            
        //  2203: aload           121
        //  2205: ifnull          2219
        //  2208: goto            2232
        //  2211: aload           121
        //  2213: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2216: ifeq            2232
        //  2219: dload           x
        //  2221: invokestatic    scala/runtime/BoxesRunTime.boxToDouble:(D)Ljava/lang/Double;
        //  2224: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  2227: astore          23
        //  2229: goto            3738
        //  2232: iload           10
        //  2234: ifeq            2291
        //  2237: aload           11
        //  2239: invokevirtual   org/json4s/JsonAST$JDouble.num:()D
        //  2242: dstore          x
        //  2244: aload           targetType
        //  2246: getstatic       java/lang/Integer.TYPE:Ljava/lang/Class;
        //  2249: astore          124
        //  2251: dup            
        //  2252: ifnonnull       2264
        //  2255: pop            
        //  2256: aload           124
        //  2258: ifnull          2272
        //  2261: goto            2291
        //  2264: aload           124
        //  2266: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2269: ifeq            2291
        //  2272: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  2275: dload           x
        //  2277: invokevirtual   scala/Predef$.double2Double:(D)Ljava/lang/Double;
        //  2280: invokevirtual   java/lang/Double.intValue:()I
        //  2283: invokestatic    scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        //  2286: astore          23
        //  2288: goto            3738
        //  2291: iload           10
        //  2293: ifeq            2350
        //  2296: aload           11
        //  2298: invokevirtual   org/json4s/JsonAST$JDouble.num:()D
        //  2301: dstore          x
        //  2303: aload           targetType
        //  2305: getstatic       java/lang/Long.TYPE:Ljava/lang/Class;
        //  2308: astore          127
        //  2310: dup            
        //  2311: ifnonnull       2323
        //  2314: pop            
        //  2315: aload           127
        //  2317: ifnull          2331
        //  2320: goto            2350
        //  2323: aload           127
        //  2325: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2328: ifeq            2350
        //  2331: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //  2334: dload           x
        //  2336: invokevirtual   scala/Predef$.double2Double:(D)Ljava/lang/Double;
        //  2339: invokevirtual   java/lang/Double.longValue:()J
        //  2342: invokestatic    scala/runtime/BoxesRunTime.boxToLong:(J)Ljava/lang/Long;
        //  2345: astore          23
        //  2347: goto            3738
        //  2350: iload           10
        //  2352: ifeq            2400
        //  2355: aload           11
        //  2357: invokevirtual   org/json4s/JsonAST$JDouble.num:()D
        //  2360: dstore          x
        //  2362: aload           targetType
        //  2364: ldc_w           Ljava/lang/Number;.class
        //  2367: astore          130
        //  2369: dup            
        //  2370: ifnonnull       2382
        //  2373: pop            
        //  2374: aload           130
        //  2376: ifnull          2390
        //  2379: goto            2400
        //  2382: aload           130
        //  2384: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2387: ifeq            2400
        //  2390: dload           x
        //  2392: invokestatic    scala/runtime/BoxesRunTime.boxToDouble:(D)Ljava/lang/Double;
        //  2395: astore          23
        //  2397: goto            3738
        //  2400: iload           10
        //  2402: ifeq            2456
        //  2405: aload           11
        //  2407: invokevirtual   org/json4s/JsonAST$JDouble.num:()D
        //  2410: dstore          x
        //  2412: aload           targetType
        //  2414: ldc_w           Lscala/math/BigDecimal;.class
        //  2417: astore          133
        //  2419: dup            
        //  2420: ifnonnull       2432
        //  2423: pop            
        //  2424: aload           133
        //  2426: ifnull          2440
        //  2429: goto            2456
        //  2432: aload           133
        //  2434: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2437: ifeq            2456
        //  2440: getstatic       scala/package$.MODULE$:Lscala/package$;
        //  2443: invokevirtual   scala/package$.BigDecimal:()Lscala/math/BigDecimal$;
        //  2446: dload           x
        //  2448: invokevirtual   scala/math/BigDecimal$.apply:(D)Lscala/math/BigDecimal;
        //  2451: astore          23
        //  2453: goto            3738
        //  2456: iload           10
        //  2458: ifeq            2515
        //  2461: aload           11
        //  2463: invokevirtual   org/json4s/JsonAST$JDouble.num:()D
        //  2466: dstore          x
        //  2468: aload           targetType
        //  2470: ldc_w           Ljava/math/BigDecimal;.class
        //  2473: astore          136
        //  2475: dup            
        //  2476: ifnonnull       2488
        //  2479: pop            
        //  2480: aload           136
        //  2482: ifnull          2496
        //  2485: goto            2515
        //  2488: aload           136
        //  2490: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2493: ifeq            2515
        //  2496: getstatic       scala/package$.MODULE$:Lscala/package$;
        //  2499: invokevirtual   scala/package$.BigDecimal:()Lscala/math/BigDecimal$;
        //  2502: dload           x
        //  2504: invokevirtual   scala/math/BigDecimal$.apply:(D)Lscala/math/BigDecimal;
        //  2507: invokevirtual   scala/math/BigDecimal.bigDecimal:()Ljava/math/BigDecimal;
        //  2510: astore          23
        //  2512: goto            3738
        //  2515: aload           20
        //  2517: instanceof      Lorg/json4s/JsonAST$JDecimal;
        //  2520: ifeq            2581
        //  2523: iconst_1       
        //  2524: istore          12
        //  2526: aload           20
        //  2528: checkcast       Lorg/json4s/JsonAST$JDecimal;
        //  2531: astore          13
        //  2533: aload           13
        //  2535: invokevirtual   org/json4s/JsonAST$JDecimal.num:()Lscala/math/BigDecimal;
        //  2538: astore          x
        //  2540: aload           targetType
        //  2542: getstatic       java/lang/Double.TYPE:Ljava/lang/Class;
        //  2545: astore          138
        //  2547: dup            
        //  2548: ifnonnull       2560
        //  2551: pop            
        //  2552: aload           138
        //  2554: ifnull          2568
        //  2557: goto            2581
        //  2560: aload           138
        //  2562: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2565: ifeq            2581
        //  2568: aload           x
        //  2570: invokevirtual   scala/math/BigDecimal.doubleValue:()D
        //  2573: invokestatic    scala/runtime/BoxesRunTime.boxToDouble:(D)Ljava/lang/Double;
        //  2576: astore          23
        //  2578: goto            3738
        //  2581: iload           12
        //  2583: ifeq            2633
        //  2586: aload           13
        //  2588: invokevirtual   org/json4s/JsonAST$JDecimal.num:()Lscala/math/BigDecimal;
        //  2591: astore          x
        //  2593: aload           targetType
        //  2595: ldc             Ljava/lang/Double;.class
        //  2597: astore          140
        //  2599: dup            
        //  2600: ifnonnull       2612
        //  2603: pop            
        //  2604: aload           140
        //  2606: ifnull          2620
        //  2609: goto            2633
        //  2612: aload           140
        //  2614: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2617: ifeq            2633
        //  2620: aload           x
        //  2622: invokevirtual   scala/math/BigDecimal.doubleValue:()D
        //  2625: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
        //  2628: astore          23
        //  2630: goto            3738
        //  2633: iload           12
        //  2635: ifeq            2680
        //  2638: aload           13
        //  2640: invokevirtual   org/json4s/JsonAST$JDecimal.num:()Lscala/math/BigDecimal;
        //  2643: astore          x
        //  2645: aload           targetType
        //  2647: ldc_w           Lscala/math/BigDecimal;.class
        //  2650: astore          142
        //  2652: dup            
        //  2653: ifnonnull       2665
        //  2656: pop            
        //  2657: aload           142
        //  2659: ifnull          2673
        //  2662: goto            2680
        //  2665: aload           142
        //  2667: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2670: ifeq            2680
        //  2673: aload           x
        //  2675: astore          23
        //  2677: goto            3738
        //  2680: iload           12
        //  2682: ifeq            2730
        //  2685: aload           13
        //  2687: invokevirtual   org/json4s/JsonAST$JDecimal.num:()Lscala/math/BigDecimal;
        //  2690: astore          x
        //  2692: aload           targetType
        //  2694: ldc_w           Ljava/math/BigDecimal;.class
        //  2697: astore          144
        //  2699: dup            
        //  2700: ifnonnull       2712
        //  2703: pop            
        //  2704: aload           144
        //  2706: ifnull          2720
        //  2709: goto            2730
        //  2712: aload           144
        //  2714: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2717: ifeq            2730
        //  2720: aload           x
        //  2722: invokevirtual   scala/math/BigDecimal.bigDecimal:()Ljava/math/BigDecimal;
        //  2725: astore          23
        //  2727: goto            3738
        //  2730: iload           12
        //  2732: ifeq            2783
        //  2735: aload           13
        //  2737: invokevirtual   org/json4s/JsonAST$JDecimal.num:()Lscala/math/BigDecimal;
        //  2740: astore          x
        //  2742: aload           targetType
        //  2744: getstatic       java/lang/Float.TYPE:Ljava/lang/Class;
        //  2747: astore          146
        //  2749: dup            
        //  2750: ifnonnull       2762
        //  2753: pop            
        //  2754: aload           146
        //  2756: ifnull          2770
        //  2759: goto            2783
        //  2762: aload           146
        //  2764: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2767: ifeq            2783
        //  2770: aload           x
        //  2772: invokevirtual   scala/math/BigDecimal.floatValue:()F
        //  2775: invokestatic    scala/runtime/BoxesRunTime.boxToFloat:(F)Ljava/lang/Float;
        //  2778: astore          23
        //  2780: goto            3738
        //  2783: iload           12
        //  2785: ifeq            2835
        //  2788: aload           13
        //  2790: invokevirtual   org/json4s/JsonAST$JDecimal.num:()Lscala/math/BigDecimal;
        //  2793: astore          x
        //  2795: aload           targetType
        //  2797: ldc             Ljava/lang/Float;.class
        //  2799: astore          148
        //  2801: dup            
        //  2802: ifnonnull       2814
        //  2805: pop            
        //  2806: aload           148
        //  2808: ifnull          2822
        //  2811: goto            2835
        //  2814: aload           148
        //  2816: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2819: ifeq            2835
        //  2822: aload           x
        //  2824: invokevirtual   scala/math/BigDecimal.floatValue:()F
        //  2827: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //  2830: astore          23
        //  2832: goto            3738
        //  2835: iload           12
        //  2837: ifeq            2885
        //  2840: aload           13
        //  2842: invokevirtual   org/json4s/JsonAST$JDecimal.num:()Lscala/math/BigDecimal;
        //  2845: astore          x
        //  2847: aload           targetType
        //  2849: ldc_w           Ljava/lang/String;.class
        //  2852: astore          150
        //  2854: dup            
        //  2855: ifnonnull       2867
        //  2858: pop            
        //  2859: aload           150
        //  2861: ifnull          2875
        //  2864: goto            2885
        //  2867: aload           150
        //  2869: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2872: ifeq            2885
        //  2875: aload           x
        //  2877: invokevirtual   scala/math/BigDecimal.toString:()Ljava/lang/String;
        //  2880: astore          23
        //  2882: goto            3738
        //  2885: iload           12
        //  2887: ifeq            2938
        //  2890: aload           13
        //  2892: invokevirtual   org/json4s/JsonAST$JDecimal.num:()Lscala/math/BigDecimal;
        //  2895: astore          x
        //  2897: aload           targetType
        //  2899: getstatic       java/lang/Integer.TYPE:Ljava/lang/Class;
        //  2902: astore          152
        //  2904: dup            
        //  2905: ifnonnull       2917
        //  2908: pop            
        //  2909: aload           152
        //  2911: ifnull          2925
        //  2914: goto            2938
        //  2917: aload           152
        //  2919: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2922: ifeq            2938
        //  2925: aload           x
        //  2927: invokevirtual   scala/math/BigDecimal.intValue:()I
        //  2930: invokestatic    scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        //  2933: astore          23
        //  2935: goto            3738
        //  2938: iload           12
        //  2940: ifeq            2991
        //  2943: aload           13
        //  2945: invokevirtual   org/json4s/JsonAST$JDecimal.num:()Lscala/math/BigDecimal;
        //  2948: astore          x
        //  2950: aload           targetType
        //  2952: getstatic       java/lang/Long.TYPE:Ljava/lang/Class;
        //  2955: astore          154
        //  2957: dup            
        //  2958: ifnonnull       2970
        //  2961: pop            
        //  2962: aload           154
        //  2964: ifnull          2978
        //  2967: goto            2991
        //  2970: aload           154
        //  2972: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  2975: ifeq            2991
        //  2978: aload           x
        //  2980: invokevirtual   scala/math/BigDecimal.longValue:()J
        //  2983: invokestatic    scala/runtime/BoxesRunTime.boxToLong:(J)Ljava/lang/Long;
        //  2986: astore          23
        //  2988: goto            3738
        //  2991: iload           12
        //  2993: ifeq            3038
        //  2996: aload           13
        //  2998: invokevirtual   org/json4s/JsonAST$JDecimal.num:()Lscala/math/BigDecimal;
        //  3001: astore          x
        //  3003: aload           targetType
        //  3005: ldc_w           Ljava/lang/Number;.class
        //  3008: astore          156
        //  3010: dup            
        //  3011: ifnonnull       3023
        //  3014: pop            
        //  3015: aload           156
        //  3017: ifnull          3031
        //  3020: goto            3038
        //  3023: aload           156
        //  3025: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3028: ifeq            3038
        //  3031: aload           x
        //  3033: astore          23
        //  3035: goto            3738
        //  3038: aload           20
        //  3040: instanceof      Lorg/json4s/JsonAST$JString;
        //  3043: ifeq            3098
        //  3046: iconst_1       
        //  3047: istore          14
        //  3049: aload           20
        //  3051: checkcast       Lorg/json4s/JsonAST$JString;
        //  3054: astore          15
        //  3056: aload           15
        //  3058: invokevirtual   org/json4s/JsonAST$JString.s:()Ljava/lang/String;
        //  3061: astore          s
        //  3063: aload           targetType
        //  3065: ldc_w           Ljava/lang/String;.class
        //  3068: astore          158
        //  3070: dup            
        //  3071: ifnonnull       3083
        //  3074: pop            
        //  3075: aload           158
        //  3077: ifnull          3091
        //  3080: goto            3098
        //  3083: aload           158
        //  3085: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3088: ifeq            3098
        //  3091: aload           s
        //  3093: astore          23
        //  3095: goto            3738
        //  3098: iload           14
        //  3100: ifeq            3151
        //  3103: aload           15
        //  3105: invokevirtual   org/json4s/JsonAST$JString.s:()Ljava/lang/String;
        //  3108: astore          s
        //  3110: aload           targetType
        //  3112: ldc_w           Lscala/Symbol;.class
        //  3115: astore          160
        //  3117: dup            
        //  3118: ifnonnull       3130
        //  3121: pop            
        //  3122: aload           160
        //  3124: ifnull          3138
        //  3127: goto            3151
        //  3130: aload           160
        //  3132: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3135: ifeq            3151
        //  3138: getstatic       scala/Symbol$.MODULE$:Lscala/Symbol$;
        //  3141: aload           s
        //  3143: invokevirtual   scala/Symbol$.apply:(Ljava/lang/String;)Lscala/Symbol;
        //  3146: astore          23
        //  3148: goto            3738
        //  3151: iload           14
        //  3153: ifeq            3203
        //  3156: aload           15
        //  3158: invokevirtual   org/json4s/JsonAST$JString.s:()Ljava/lang/String;
        //  3161: astore          s
        //  3163: aload           targetType
        //  3165: ldc_w           Ljava/util/Date;.class
        //  3168: astore          162
        //  3170: dup            
        //  3171: ifnonnull       3183
        //  3174: pop            
        //  3175: aload           162
        //  3177: ifnull          3191
        //  3180: goto            3203
        //  3183: aload           162
        //  3185: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3188: ifeq            3203
        //  3191: aload_0         /* this */
        //  3192: aload           s
        //  3194: aload_3         /* formats */
        //  3195: invokespecial   org/json4s/Extraction$.formatDate:(Ljava/lang/String;Lorg/json4s/Formats;)Ljava/util/Date;
        //  3198: astore          23
        //  3200: goto            3738
        //  3203: iload           14
        //  3205: ifeq            3255
        //  3208: aload           15
        //  3210: invokevirtual   org/json4s/JsonAST$JString.s:()Ljava/lang/String;
        //  3213: astore          s
        //  3215: aload           targetType
        //  3217: ldc_w           Ljava/sql/Timestamp;.class
        //  3220: astore          164
        //  3222: dup            
        //  3223: ifnonnull       3235
        //  3226: pop            
        //  3227: aload           164
        //  3229: ifnull          3243
        //  3232: goto            3255
        //  3235: aload           164
        //  3237: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3240: ifeq            3255
        //  3243: aload_0         /* this */
        //  3244: aload           s
        //  3246: aload_3         /* formats */
        //  3247: invokespecial   org/json4s/Extraction$.formatTimestamp:(Ljava/lang/String;Lorg/json4s/Formats;)Ljava/sql/Timestamp;
        //  3250: astore          23
        //  3252: goto            3738
        //  3255: aload           20
        //  3257: instanceof      Lorg/json4s/JsonAST$JBool;
        //  3260: ifeq            3318
        //  3263: iconst_1       
        //  3264: istore          16
        //  3266: aload           20
        //  3268: checkcast       Lorg/json4s/JsonAST$JBool;
        //  3271: astore          17
        //  3273: aload           17
        //  3275: invokevirtual   org/json4s/JsonAST$JBool.value:()Z
        //  3278: istore          x
        //  3280: aload           targetType
        //  3282: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
        //  3285: astore          166
        //  3287: dup            
        //  3288: ifnonnull       3300
        //  3291: pop            
        //  3292: aload           166
        //  3294: ifnull          3308
        //  3297: goto            3318
        //  3300: aload           166
        //  3302: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3305: ifeq            3318
        //  3308: iload           x
        //  3310: invokestatic    scala/runtime/BoxesRunTime.boxToBoolean:(Z)Ljava/lang/Boolean;
        //  3313: astore          23
        //  3315: goto            3738
        //  3318: iload           16
        //  3320: ifeq            3368
        //  3323: aload           17
        //  3325: invokevirtual   org/json4s/JsonAST$JBool.value:()Z
        //  3328: istore          x
        //  3330: aload           targetType
        //  3332: ldc_w           Ljava/lang/Boolean;.class
        //  3335: astore          168
        //  3337: dup            
        //  3338: ifnonnull       3350
        //  3341: pop            
        //  3342: aload           168
        //  3344: ifnull          3358
        //  3347: goto            3368
        //  3350: aload           168
        //  3352: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3355: ifeq            3368
        //  3358: iload           x
        //  3360: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //  3363: astore          23
        //  3365: goto            3738
        //  3368: aload           20
        //  3370: ifnull          3411
        //  3373: aload           20
        //  3375: astore          169
        //  3377: aload           targetType
        //  3379: ldc             Lorg/json4s/JsonAST$JValue;.class
        //  3381: astore          170
        //  3383: dup            
        //  3384: ifnonnull       3396
        //  3387: pop            
        //  3388: aload           170
        //  3390: ifnull          3404
        //  3393: goto            3411
        //  3396: aload           170
        //  3398: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3401: ifeq            3411
        //  3404: aload           169
        //  3406: astore          23
        //  3408: goto            3738
        //  3411: aload           20
        //  3413: instanceof      Lorg/json4s/JsonAST$JObject;
        //  3416: ifeq            3460
        //  3419: aload           20
        //  3421: checkcast       Lorg/json4s/JsonAST$JObject;
        //  3424: astore          171
        //  3426: aload           targetType
        //  3428: ldc             Lorg/json4s/JsonAST$JObject;.class
        //  3430: astore          172
        //  3432: dup            
        //  3433: ifnonnull       3445
        //  3436: pop            
        //  3437: aload           172
        //  3439: ifnull          3453
        //  3442: goto            3460
        //  3445: aload           172
        //  3447: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3450: ifeq            3460
        //  3453: aload           171
        //  3455: astore          23
        //  3457: goto            3738
        //  3460: aload           20
        //  3462: instanceof      Lorg/json4s/JsonAST$JArray;
        //  3465: ifeq            3510
        //  3468: aload           20
        //  3470: checkcast       Lorg/json4s/JsonAST$JArray;
        //  3473: astore          173
        //  3475: aload           targetType
        //  3477: ldc_w           Lorg/json4s/JsonAST$JArray;.class
        //  3480: astore          174
        //  3482: dup            
        //  3483: ifnonnull       3495
        //  3486: pop            
        //  3487: aload           174
        //  3489: ifnull          3503
        //  3492: goto            3510
        //  3495: aload           174
        //  3497: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3500: ifeq            3510
        //  3503: aload           173
        //  3505: astore          23
        //  3507: goto            3738
        //  3510: getstatic       org/json4s/package$.MODULE$:Lorg/json4s/package$;
        //  3513: invokevirtual   org/json4s/package$.JNull:()Lorg/json4s/JsonAST$JNull$;
        //  3516: aload           20
        //  3518: astore          175
        //  3520: dup            
        //  3521: ifnonnull       3533
        //  3524: pop            
        //  3525: aload           175
        //  3527: ifnull          3541
        //  3530: goto            3563
        //  3533: aload           175
        //  3535: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3538: ifeq            3563
        //  3541: iconst_1       
        //  3542: istore          18
        //  3544: aload           20
        //  3546: astore          19
        //  3548: aload_3         /* formats */
        //  3549: invokeinterface org/json4s/Formats.allowNull:()Z
        //  3554: ifeq            3563
        //  3557: aconst_null    
        //  3558: astore          23
        //  3560: goto            3738
        //  3563: iload           18
        //  3565: ifeq            3614
        //  3568: aload_3         /* formats */
        //  3569: invokeinterface org/json4s/Formats.allowNull:()Z
        //  3574: ifne            3614
        //  3577: getstatic       org/json4s/reflect/package$.MODULE$:Lorg/json4s/reflect/package$;
        //  3580: new             Lscala/collection/mutable/StringBuilder;
        //  3583: dup            
        //  3584: invokespecial   scala/collection/mutable/StringBuilder.<init>:()V
        //  3587: ldc_w           "Did not find value which can be converted into "
        //  3590: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //  3593: aload           targetType
        //  3595: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //  3598: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //  3601: invokevirtual   scala/collection/mutable/StringBuilder.toString:()Ljava/lang/String;
        //  3604: getstatic       org/json4s/reflect/package$.MODULE$:Lorg/json4s/reflect/package$;
        //  3607: invokevirtual   org/json4s/reflect/package$.fail$default$2:()Ljava/lang/Exception;
        //  3610: invokevirtual   org/json4s/reflect/package$.fail:(Ljava/lang/String;Ljava/lang/Exception;)Lscala/runtime/Nothing$;
        //  3613: athrow         
        //  3614: getstatic       org/json4s/package$.MODULE$:Lorg/json4s/package$;
        //  3617: invokevirtual   org/json4s/package$.JNothing:()Lorg/json4s/JsonAST$JNothing$;
        //  3620: aload           20
        //  3622: astore          176
        //  3624: dup            
        //  3625: ifnonnull       3637
        //  3628: pop            
        //  3629: aload           176
        //  3631: ifnull          3645
        //  3634: goto            3674
        //  3637: aload           176
        //  3639: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3642: ifeq            3674
        //  3645: aload           default
        //  3647: new             Lorg/json4s/Extraction$$anonfun$org$json4s$Extraction$$convert$1;
        //  3650: dup            
        //  3651: invokespecial   org/json4s/Extraction$$anonfun$org$json4s$Extraction$$convert$1.<init>:()V
        //  3654: invokevirtual   scala/Option.map:(Lscala/Function1;)Lscala/Option;
        //  3657: new             Lorg/json4s/Extraction$$anonfun$org$json4s$Extraction$$convert$2;
        //  3660: dup            
        //  3661: aload           targetType
        //  3663: invokespecial   org/json4s/Extraction$$anonfun$org$json4s$Extraction$$convert$2.<init>:(Ljava/lang/Class;)V
        //  3666: invokevirtual   scala/Option.getOrElse:(Lscala/Function0;)Ljava/lang/Object;
        //  3669: astore          23
        //  3671: goto            3738
        //  3674: aload_2         /* target */
        //  3675: invokevirtual   org/json4s/reflect/ScalaType.typeInfo:()Lorg/json4s/reflect/package$TypeInfo;
        //  3678: astore          typeInfo
        //  3680: getstatic       org/json4s/Formats$.MODULE$:Lorg/json4s/Formats$;
        //  3683: new             Lscala/Tuple2;
        //  3686: dup            
        //  3687: aload           typeInfo
        //  3689: aload_1         /* json */
        //  3690: invokespecial   scala/Tuple2.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  3693: aload_3         /* formats */
        //  3694: invokevirtual   org/json4s/Formats$.customDeserializer:(Lscala/Tuple2;Lorg/json4s/Formats;)Lscala/PartialFunction;
        //  3697: astore          custom
        //  3699: aload           custom
        //  3701: new             Lscala/Tuple2;
        //  3704: dup            
        //  3705: aload           typeInfo
        //  3707: aload_1         /* json */
        //  3708: invokespecial   scala/Tuple2.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  3711: invokeinterface scala/PartialFunction.isDefinedAt:(Ljava/lang/Object;)Z
        //  3716: ifeq            3741
        //  3719: aload           custom
        //  3721: new             Lscala/Tuple2;
        //  3724: dup            
        //  3725: aload           typeInfo
        //  3727: aload_1         /* json */
        //  3728: invokespecial   scala/Tuple2.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  3731: invokeinterface scala/PartialFunction.apply:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3736: astore          23
        //  3738: aload           23
        //  3740: areturn        
        //  3741: getstatic       org/json4s/reflect/package$.MODULE$:Lorg/json4s/reflect/package$;
        //  3744: new             Lscala/collection/mutable/StringBuilder;
        //  3747: dup            
        //  3748: invokespecial   scala/collection/mutable/StringBuilder.<init>:()V
        //  3751: ldc_w           "Do not know how to convert "
        //  3754: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //  3757: aload_1         /* json */
        //  3758: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //  3761: ldc_w           " into "
        //  3764: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //  3767: aload           5
        //  3769: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //  3772: invokevirtual   scala/collection/mutable/StringBuilder.toString:()Ljava/lang/String;
        //  3775: getstatic       org/json4s/reflect/package$.MODULE$:Lorg/json4s/reflect/package$;
        //  3778: invokevirtual   org/json4s/reflect/package$.fail$default$2:()Ljava/lang/Exception;
        //  3781: invokevirtual   org/json4s/reflect/package$.fail:(Ljava/lang/String;Ljava/lang/Exception;)Lscala/runtime/Nothing$;
        //  3784: athrow         
        //    Signature:
        //  (Lorg/json4s/JsonAST$JValue;Lorg/json4s/reflect/ScalaType;Lorg/json4s/Formats;Lscala/Option<Lscala/Function0<Ljava/lang/Object;>;>;)Ljava/lang/Object;
        //    StackMapTable: 00 C6 FF 00 60 00 17 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 07 01 4F 07 00 20 00 01 07 00 20 07 F9 00 0C FF 00 1F 00 1A 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 1C 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 06 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 1E 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 20 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 22 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1E 00 24 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 26 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1E 00 28 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 2A 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 2C 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 2E 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 30 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 32 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 09 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 34 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 36 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 0F 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 38 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 4F 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 05 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 2C 00 3A 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 3D 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 40 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 09 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 43 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 46 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 09 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 49 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1E 00 4C 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 4F 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1E 00 52 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 55 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 58 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 5B 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 5E 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 61 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 64 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 67 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 0F 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 6A 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 05 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 2C 00 6D 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 07 00 20 00 01 07 00 20 07 FF 00 09 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1E 00 70 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 07 00 20 00 01 07 00 20 07 FF 00 09 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 73 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1E 00 76 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 79 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 7C 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 7F 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 82 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 07 00 20 00 01 07 00 20 07 FF 00 09 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 85 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 07 00 20 00 01 07 00 20 07 FF 00 0F 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 88 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 07 00 20 00 01 07 00 20 07 FF 00 12 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 05 01 05 01 05 01 05 07 00 D9 00 00 FF 00 2C 00 8B 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 FF 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1E 00 8D 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 FF 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 8F 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 FF 07 00 20 00 01 07 00 20 07 FF 00 06 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 91 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 FF 07 00 20 00 01 07 00 20 07 FF 00 09 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 93 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 FF 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1E 00 95 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 FF 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 97 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 FF 07 00 20 00 01 07 00 20 07 FF 00 09 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 99 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 FF 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 9B 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 FF 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 9D 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 FF 07 00 20 00 01 07 00 20 07 FF 00 06 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 05 01 05 01 05 07 00 D9 00 00 FF 00 2C 00 9F 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 22 07 00 20 00 01 07 00 20 07 FF 00 06 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 A1 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 22 07 00 20 00 01 07 00 20 07 FF 00 0C 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 A3 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 22 07 00 20 00 01 07 00 20 07 FF 00 0B 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 05 01 05 07 00 D9 00 00 FF 00 1F 00 A5 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 05 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 22 07 00 20 00 01 07 00 20 07 FF 00 0B 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 05 01 05 07 00 D9 00 00 FF 00 2C 00 A7 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 07 00 20 00 01 07 00 20 07 FF 00 09 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 FF 00 1F 00 A9 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 07 00 20 00 01 07 00 20 07 FF 00 09 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 FF 00 1B 00 AB 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 D9 07 00 20 00 01 07 00 20 07 FF 00 06 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 FF 00 21 00 AD 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 DF 07 00 20 00 01 07 00 20 07 FF 00 06 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 FF 00 22 00 AF 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 03 05 07 00 20 00 01 07 00 20 07 FF 00 06 00 15 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 FF 00 16 00 B0 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 05 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 D9 00 01 07 04 05 07 FF 00 15 00 B0 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 07 00 D9 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 D9 00 00 32 FF 00 16 00 B1 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 07 00 D9 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 D9 07 00 D9 00 01 07 04 07 07 1C FF 00 3F 00 18 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 07 00 D9 07 00 D9 00 00 07 00 04 00 00 FF 00 02 00 B3 07 00 02 07 00 D9 07 02 98 07 00 6D 07 00 78 07 00 20 01 07 03 81 01 07 03 B1 01 07 03 C0 01 07 03 CD 01 07 03 D7 01 07 03 DC 01 07 00 D9 07 00 D9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 D9 07 00 D9 07 03 7E 07 00 D0 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private Timestamp formatTimestamp(final String s, final Formats formats) {
        return new Timestamp(((Date)formats.dateFormat().parse(s).getOrElse((Function0)new Extraction$$anonfun$formatTimestamp.Extraction$$anonfun$formatTimestamp$1(s))).getTime());
    }
    
    private Date formatDate(final String s, final Formats formats) {
        return (Date)formats.dateFormat().parse(s).getOrElse((Function0)new Extraction$$anonfun$formatDate.Extraction$$anonfun$formatDate$1(s));
    }
    
    private final JsonAST.JObject prependTypeHint$1(final Class clazz, final JsonAST.JObject o, final Formats formats$2) {
        return package$.MODULE$.JObject().apply((List<Tuple2<String, JsonAST.JValue>>)o.obj().$colon$colon((Object)package$.MODULE$.JField().apply(formats$2.typeHintFieldName(), package$.MODULE$.JString().apply(formats$2.typeHints().hintFor(clazz)))));
    }
    
    private final void addField$1(final String name, final Object v, final JsonWriter obj, final Formats formats$2) {
        if (None$.MODULE$.equals(v)) {
            formats$2.emptyValueStrategy().noneValReplacement().foreach((Function1)new Extraction$$anonfun$addField$1.Extraction$$anonfun$addField$1$1(formats$2, name, obj));
            final BoxedUnit unit = BoxedUnit.UNIT;
        }
        else {
            this.internalDecomposeWithBuilder(v, (JsonWriter<Object>)obj.startField(name), formats$2);
            final BoxedUnit unit2 = BoxedUnit.UNIT;
        }
    }
    
    private final JsonWriter decomposeObject$1(final Class k, final Object a$1, final Formats formats$2, final JsonWriter current$1, final Object any$1) {
        final ScalaType klass = Reflector$.MODULE$.scalaTypeOf(k);
        final ClassDescriptor descriptor = (ClassDescriptor)Reflector$.MODULE$.describe(org.json4s.reflect.package$.MODULE$.scalaTypeDescribable(klass, formats$2));
        final Seq ctorParams = (Seq)descriptor.mostComprehensive().map((Function1)new Extraction$$anonfun.Extraction$$anonfun$4(), Seq$.MODULE$.canBuildFrom());
        final Seq methods = (Seq)Predef$.MODULE$.refArrayOps((Object[])k.getMethods()).toSeq().map((Function1)new Extraction$$anonfun.Extraction$$anonfun$5(), Seq$.MODULE$.canBuildFrom());
        final Iterator iter = descriptor.properties().iterator();
        final JsonWriter obj = current$1.startObject();
        if (formats$2.typeHints().containsHint(k)) {
            final JsonWriter f = obj.startField(formats$2.typeHintFieldName());
            f.string(formats$2.typeHints().hintFor(k));
        }
        else {
            final BoxedUnit unit = BoxedUnit.UNIT;
        }
        final Option fs = formats$2.fieldSerializer(k);
        while (iter.hasNext()) {
            final PropertyDescriptor prop = (PropertyDescriptor)iter.next();
            final Object fieldVal = prop.get(any$1);
            final String n = prop.name();
            if (fs.isDefined()) {
                final FieldSerializer fieldSerializer = (FieldSerializer)fs.get();
                final Option ff = (Option)fieldSerializer.serializer().orElse((PartialFunction)Predef$.MODULE$.Map().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)new Tuple2((Object)n, fieldVal)), (Object)new Some((Object)new Tuple2((Object)n, fieldVal))) }))).apply((Object)new Tuple2((Object)n, fieldVal));
                if (ff.isDefined()) {
                    final Option option = ff;
                    if (option instanceof Some) {
                        final Tuple2 tuple2 = (Tuple2)((Some)option).x();
                        if (tuple2 != null) {
                            final String nn = (String)tuple2._1();
                            final Object vv = tuple2._2();
                            final Tuple2 tuple3 = new Tuple2((Object)nn, vv);
                            final String nn2 = (String)tuple3._1();
                            final Object vv2 = tuple3._2();
                            final Object vvv = fieldSerializer.includeLazyVal() ? this.loadLazyValValue(a$1, nn2, vv2) : vv2;
                            this.addField$1(nn2, vvv, obj, formats$2);
                            continue;
                        }
                    }
                    throw new MatchError((Object)option);
                }
                continue;
            }
            else {
                if (!ctorParams.contains((Object)prop.name()) || !methods.contains((Object)NameTransformer$.MODULE$.encode(prop.name()))) {
                    continue;
                }
                this.addField$1(n, fieldVal, obj, formats$2);
            }
        }
        return obj.endObject();
    }
    
    public final String org$json4s$Extraction$$escapePath$1(final String str) {
        return str;
    }
    
    private final scala.collection.immutable.Map array$1(final Iterable arr, final Formats formats$3, final String path$1) {
        scala.collection.immutable.Map map = null;
        switch (arr.size()) {
            default: {
                map = (scala.collection.immutable.Map)((Tuple2)arr.foldLeft((Object)new Tuple2((Object)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$), (Object)BoxesRunTime.boxToInteger(0)), (Function2)new Extraction$$anonfun$array$1.Extraction$$anonfun$array$1$1(formats$3, path$1)))._1();
                break;
            }
            case 0: {
                map = (scala.collection.immutable.Map)Predef$.MODULE$.Map().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)path$1), (Object)"[]") }));
                break;
            }
        }
        return map;
    }
    
    public final scala.collection.immutable.Map org$json4s$Extraction$$flatten0$1(final String path, final JsonAST.JValue json, final Formats formats$3) {
        final JsonAST.JNothing$ jNothing = package$.MODULE$.JNothing();
        boolean b = false;
        Label_0080: {
            Label_0040: {
                if (jNothing == null) {
                    if (json != null) {
                        break Label_0040;
                    }
                }
                else if (!jNothing.equals(json)) {
                    break Label_0040;
                }
                b = true;
                break Label_0080;
            }
            final JsonAST.JNull$ jNull = package$.MODULE$.JNull();
            Label_0077: {
                if (jNull == null) {
                    if (json != null) {
                        break Label_0077;
                    }
                }
                else if (!jNull.equals(json)) {
                    break Label_0077;
                }
                b = true;
                break Label_0080;
            }
            b = false;
        }
        scala.collection.immutable.Map map;
        if (b) {
            map = (scala.collection.immutable.Map)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$);
        }
        else if (json instanceof JsonAST.JString) {
            final String s = ((JsonAST.JString)json).s();
            map = (scala.collection.immutable.Map)Predef$.MODULE$.Map().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)path), (Object)new StringBuilder().append((Object)"\"").append((Object)ParserUtil$.MODULE$.quote(s, formats$3)).append((Object)"\"").toString()) }));
        }
        else if (json instanceof JsonAST.JDouble) {
            final double num = ((JsonAST.JDouble)json).num();
            map = (scala.collection.immutable.Map)Predef$.MODULE$.Map().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)path), (Object)BoxesRunTime.boxToDouble(num).toString()) }));
        }
        else if (json instanceof JsonAST.JDecimal) {
            final BigDecimal num2 = ((JsonAST.JDecimal)json).num();
            map = (scala.collection.immutable.Map)Predef$.MODULE$.Map().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)path), (Object)num2.toString()) }));
        }
        else if (json instanceof JsonAST.JLong) {
            final long num3 = ((JsonAST.JLong)json).num();
            map = (scala.collection.immutable.Map)Predef$.MODULE$.Map().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)path), (Object)BoxesRunTime.boxToLong(num3).toString()) }));
        }
        else if (json instanceof JsonAST.JInt) {
            final BigInt num4 = ((JsonAST.JInt)json).num();
            map = (scala.collection.immutable.Map)Predef$.MODULE$.Map().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)path), (Object)num4.toString()) }));
        }
        else if (json instanceof JsonAST.JBool) {
            final boolean value = ((JsonAST.JBool)json).value();
            map = (scala.collection.immutable.Map)Predef$.MODULE$.Map().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)path), (Object)BoxesRunTime.boxToBoolean(value).toString()) }));
        }
        else if (json instanceof JsonAST.JObject) {
            final List obj = ((JsonAST.JObject)json).obj();
            map = (scala.collection.immutable.Map)obj.foldLeft((Object)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$), (Function2)new Extraction$$anonfun$org$json4s$Extraction$$flatten0$1.Extraction$$anonfun$org$json4s$Extraction$$flatten0$1$1(formats$3, path));
        }
        else if (json instanceof JsonAST.JArray) {
            final List arr = ((JsonAST.JArray)json).arr();
            map = this.array$1((Iterable)arr, formats$3, path);
        }
        else {
            if (!(json instanceof JsonAST.JSet)) {
                throw new MatchError((Object)json);
            }
            final Set s2 = ((JsonAST.JSet)json).set();
            map = this.array$1((Iterable)s2, formats$3, path);
        }
        return map;
    }
    
    public final JsonAST.JValue org$json4s$Extraction$$extractValue$1(final String value, final boolean useBigDecimalForDouble$1, final boolean useBigIntForLong$1) {
        final String lowerCase = value.toLowerCase();
        Object o;
        if ("".equals(lowerCase)) {
            o = package$.MODULE$.JNothing();
        }
        else if ("null".equals(lowerCase)) {
            o = package$.MODULE$.JNull();
        }
        else if ("true".equals(lowerCase)) {
            o = package$.MODULE$.JBool().True();
        }
        else if ("false".equals(lowerCase)) {
            o = package$.MODULE$.JBool().False();
        }
        else if ("[]".equals(lowerCase)) {
            o = package$.MODULE$.JArray().apply((List<JsonAST.JValue>)Nil$.MODULE$);
        }
        else {
            o = (RichChar$.MODULE$.isDigit$extension(Predef$.MODULE$.charWrapper(value.charAt(0))) ? ((value.indexOf(46) == -1) ? (useBigIntForLong$1 ? package$.MODULE$.JInt().apply(scala.package$.MODULE$.BigInt().apply(value)) : package$.MODULE$.JLong().apply(new StringOps(Predef$.MODULE$.augmentString(value)).toLong())) : (useBigDecimalForDouble$1 ? package$.MODULE$.JDecimal().apply(scala.package$.MODULE$.BigDecimal().apply(value)) : package$.MODULE$.JDouble().apply(ParserUtil$.MODULE$.parseDouble(value)))) : package$.MODULE$.JString().apply(ParserUtil$.MODULE$.unquote(value.substring(1))));
        }
        return (JsonAST.JValue)o;
    }
    
    public final scala.collection.immutable.Map org$json4s$Extraction$$submap$1(final String prefix, final scala.collection.immutable.Map map$1, final Regex ArrayProp$1, final Regex ArrayElem$1, final Regex OtherProp$1) {
        return (scala.collection.immutable.Map)map$1.withFilter((Function1)new Extraction$$anonfun$org$json4s$Extraction$$submap$1.Extraction$$anonfun$org$json4s$Extraction$$submap$1$1(ArrayProp$1, ArrayElem$1, OtherProp$1, prefix)).map((Function1)new Extraction$$anonfun$org$json4s$Extraction$$submap$1.Extraction$$anonfun$org$json4s$Extraction$$submap$1$2(prefix), Map$.MODULE$.canBuildFrom());
    }
    
    private Extraction$() {
        MODULE$ = this;
    }
}
