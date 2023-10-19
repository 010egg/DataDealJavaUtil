// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Predef$;
import scala.Function0;
import scala.collection.immutable.List;
import scala.collection.Seq$;
import scala.collection.Seq;
import scala.Function1;
import org.json4s.JsonAST;
import scala.Symbol;
import java.sql.Timestamp;
import java.util.Date;
import scala.math.BigDecimal;
import java.math.BigInteger;
import scala.math.BigInt;
import scala.reflect.Manifest;

public final class ScalaType$
{
    public static final ScalaType$ MODULE$;
    private final package.Memo<Manifest<?>, ScalaType> org$json4s$reflect$ScalaType$$types;
    private final String org$json4s$reflect$ScalaType$$singletonFieldName;
    private final ScalaType org$json4s$reflect$ScalaType$$IntType;
    private final ScalaType org$json4s$reflect$ScalaType$$NumberType;
    private final ScalaType org$json4s$reflect$ScalaType$$LongType;
    private final ScalaType org$json4s$reflect$ScalaType$$ByteType;
    private final ScalaType org$json4s$reflect$ScalaType$$ShortType;
    private final ScalaType org$json4s$reflect$ScalaType$$BooleanType;
    private final ScalaType org$json4s$reflect$ScalaType$$FloatType;
    private final ScalaType org$json4s$reflect$ScalaType$$DoubleType;
    private final ScalaType org$json4s$reflect$ScalaType$$StringType;
    private final ScalaType org$json4s$reflect$ScalaType$$SymbolType;
    private final ScalaType org$json4s$reflect$ScalaType$$BigDecimalType;
    private final ScalaType org$json4s$reflect$ScalaType$$BigIntType;
    private final ScalaType org$json4s$reflect$ScalaType$$JValueType;
    private final ScalaType org$json4s$reflect$ScalaType$$JObjectType;
    private final ScalaType org$json4s$reflect$ScalaType$$JArrayType;
    private final ScalaType org$json4s$reflect$ScalaType$$DateType;
    private final ScalaType org$json4s$reflect$ScalaType$$TimestampType;
    
    static {
        new ScalaType$();
    }
    
    public package.Memo<Manifest<?>, ScalaType> org$json4s$reflect$ScalaType$$types() {
        return this.org$json4s$reflect$ScalaType$$types;
    }
    
    public String org$json4s$reflect$ScalaType$$singletonFieldName() {
        return this.org$json4s$reflect$ScalaType$$singletonFieldName;
    }
    
    public <T> ScalaType apply(final Manifest<T> mf) {
        final Class runtimeClass = mf.runtimeClass();
        final Class<Integer> type = Integer.TYPE;
        if (runtimeClass == null) {
            if (type == null) {
                return this.org$json4s$reflect$ScalaType$$IntType();
            }
        }
        else if (runtimeClass.equals(type)) {
            return this.org$json4s$reflect$ScalaType$$IntType();
        }
        final Class runtimeClass2 = mf.runtimeClass();
        final Class<Integer> obj = Integer.class;
        if (runtimeClass2 == null) {
            if (obj == null) {
                return this.org$json4s$reflect$ScalaType$$IntType();
            }
        }
        else if (runtimeClass2.equals(obj)) {
            return this.org$json4s$reflect$ScalaType$$IntType();
        }
        final Class runtimeClass3 = mf.runtimeClass();
        final Class<Long> type2 = Long.TYPE;
        if (runtimeClass3 == null) {
            if (type2 == null) {
                return this.org$json4s$reflect$ScalaType$$LongType();
            }
        }
        else if (runtimeClass3.equals(type2)) {
            return this.org$json4s$reflect$ScalaType$$LongType();
        }
        final Class runtimeClass4 = mf.runtimeClass();
        final Class<Long> obj2 = Long.class;
        if (runtimeClass4 == null) {
            if (obj2 == null) {
                return this.org$json4s$reflect$ScalaType$$LongType();
            }
        }
        else if (runtimeClass4.equals(obj2)) {
            return this.org$json4s$reflect$ScalaType$$LongType();
        }
        final Class runtimeClass5 = mf.runtimeClass();
        final Class<Byte> type3 = Byte.TYPE;
        if (runtimeClass5 == null) {
            if (type3 == null) {
                return this.org$json4s$reflect$ScalaType$$ByteType();
            }
        }
        else if (runtimeClass5.equals(type3)) {
            return this.org$json4s$reflect$ScalaType$$ByteType();
        }
        final Class runtimeClass6 = mf.runtimeClass();
        final Class<Byte> obj3 = Byte.class;
        if (runtimeClass6 == null) {
            if (obj3 == null) {
                return this.org$json4s$reflect$ScalaType$$ByteType();
            }
        }
        else if (runtimeClass6.equals(obj3)) {
            return this.org$json4s$reflect$ScalaType$$ByteType();
        }
        final Class runtimeClass7 = mf.runtimeClass();
        final Class<Short> type4 = Short.TYPE;
        if (runtimeClass7 == null) {
            if (type4 == null) {
                return this.org$json4s$reflect$ScalaType$$ShortType();
            }
        }
        else if (runtimeClass7.equals(type4)) {
            return this.org$json4s$reflect$ScalaType$$ShortType();
        }
        final Class runtimeClass8 = mf.runtimeClass();
        final Class<Short> obj4 = Short.class;
        if (runtimeClass8 == null) {
            if (obj4 == null) {
                return this.org$json4s$reflect$ScalaType$$ShortType();
            }
        }
        else if (runtimeClass8.equals(obj4)) {
            return this.org$json4s$reflect$ScalaType$$ShortType();
        }
        final Class runtimeClass9 = mf.runtimeClass();
        final Class<Float> type5 = Float.TYPE;
        if (runtimeClass9 == null) {
            if (type5 == null) {
                return this.org$json4s$reflect$ScalaType$$FloatType();
            }
        }
        else if (runtimeClass9.equals(type5)) {
            return this.org$json4s$reflect$ScalaType$$FloatType();
        }
        final Class runtimeClass10 = mf.runtimeClass();
        final Class<Float> obj5 = Float.class;
        if (runtimeClass10 == null) {
            if (obj5 == null) {
                return this.org$json4s$reflect$ScalaType$$FloatType();
            }
        }
        else if (runtimeClass10.equals(obj5)) {
            return this.org$json4s$reflect$ScalaType$$FloatType();
        }
        final Class runtimeClass11 = mf.runtimeClass();
        final Class<Double> type6 = Double.TYPE;
        if (runtimeClass11 == null) {
            if (type6 == null) {
                return this.org$json4s$reflect$ScalaType$$DoubleType();
            }
        }
        else if (runtimeClass11.equals(type6)) {
            return this.org$json4s$reflect$ScalaType$$DoubleType();
        }
        final Class runtimeClass12 = mf.runtimeClass();
        final Class<Double> obj6 = Double.class;
        if (runtimeClass12 == null) {
            if (obj6 == null) {
                return this.org$json4s$reflect$ScalaType$$DoubleType();
            }
        }
        else if (runtimeClass12.equals(obj6)) {
            return this.org$json4s$reflect$ScalaType$$DoubleType();
        }
        final Class runtimeClass13 = mf.runtimeClass();
        final Class<BigInt> obj7 = BigInt.class;
        if (runtimeClass13 == null) {
            if (obj7 == null) {
                return this.org$json4s$reflect$ScalaType$$BigIntType();
            }
        }
        else if (runtimeClass13.equals(obj7)) {
            return this.org$json4s$reflect$ScalaType$$BigIntType();
        }
        final Class runtimeClass14 = mf.runtimeClass();
        final Class<BigInteger> obj8 = BigInteger.class;
        if (runtimeClass14 == null) {
            if (obj8 == null) {
                return this.org$json4s$reflect$ScalaType$$BigIntType();
            }
        }
        else if (runtimeClass14.equals(obj8)) {
            return this.org$json4s$reflect$ScalaType$$BigIntType();
        }
        final Class runtimeClass15 = mf.runtimeClass();
        final Class<BigDecimal> obj9 = BigDecimal.class;
        if (runtimeClass15 == null) {
            if (obj9 == null) {
                return this.org$json4s$reflect$ScalaType$$BigDecimalType();
            }
        }
        else if (runtimeClass15.equals(obj9)) {
            return this.org$json4s$reflect$ScalaType$$BigDecimalType();
        }
        final Class runtimeClass16 = mf.runtimeClass();
        final Class<java.math.BigDecimal> obj10 = java.math.BigDecimal.class;
        if (runtimeClass16 == null) {
            if (obj10 == null) {
                return this.org$json4s$reflect$ScalaType$$BigDecimalType();
            }
        }
        else if (runtimeClass16.equals(obj10)) {
            return this.org$json4s$reflect$ScalaType$$BigDecimalType();
        }
        final Class runtimeClass17 = mf.runtimeClass();
        final Class<Boolean> type7 = Boolean.TYPE;
        if (runtimeClass17 == null) {
            if (type7 == null) {
                return this.org$json4s$reflect$ScalaType$$BooleanType();
            }
        }
        else if (runtimeClass17.equals(type7)) {
            return this.org$json4s$reflect$ScalaType$$BooleanType();
        }
        final Class runtimeClass18 = mf.runtimeClass();
        final Class<Boolean> obj11 = Boolean.class;
        if (runtimeClass18 == null) {
            if (obj11 == null) {
                return this.org$json4s$reflect$ScalaType$$BooleanType();
            }
        }
        else if (runtimeClass18.equals(obj11)) {
            return this.org$json4s$reflect$ScalaType$$BooleanType();
        }
        final Class runtimeClass19 = mf.runtimeClass();
        final Class<String> obj12 = String.class;
        if (runtimeClass19 == null) {
            if (obj12 == null) {
                return this.org$json4s$reflect$ScalaType$$StringType();
            }
        }
        else if (runtimeClass19.equals(obj12)) {
            return this.org$json4s$reflect$ScalaType$$StringType();
        }
        final Class runtimeClass20 = mf.runtimeClass();
        final Class<String> obj13 = String.class;
        if (runtimeClass20 == null) {
            if (obj13 == null) {
                return this.org$json4s$reflect$ScalaType$$StringType();
            }
        }
        else if (runtimeClass20.equals(obj13)) {
            return this.org$json4s$reflect$ScalaType$$StringType();
        }
        final Class runtimeClass21 = mf.runtimeClass();
        final Class<Date> obj14 = Date.class;
        Label_0729: {
            if (runtimeClass21 == null) {
                if (obj14 != null) {
                    break Label_0729;
                }
            }
            else if (!runtimeClass21.equals(obj14)) {
                break Label_0729;
            }
            return this.org$json4s$reflect$ScalaType$$DateType();
        }
        final Class runtimeClass22 = mf.runtimeClass();
        final Class<Timestamp> obj15 = Timestamp.class;
        Label_0767: {
            if (runtimeClass22 == null) {
                if (obj15 != null) {
                    break Label_0767;
                }
            }
            else if (!runtimeClass22.equals(obj15)) {
                break Label_0767;
            }
            return this.org$json4s$reflect$ScalaType$$TimestampType();
        }
        final Class runtimeClass23 = mf.runtimeClass();
        final Class<Symbol> obj16 = Symbol.class;
        Label_0805: {
            if (runtimeClass23 == null) {
                if (obj16 != null) {
                    break Label_0805;
                }
            }
            else if (!runtimeClass23.equals(obj16)) {
                break Label_0805;
            }
            return this.org$json4s$reflect$ScalaType$$SymbolType();
        }
        final Class runtimeClass24 = mf.runtimeClass();
        final Class<Number> obj17 = Number.class;
        Label_0843: {
            if (runtimeClass24 == null) {
                if (obj17 != null) {
                    break Label_0843;
                }
            }
            else if (!runtimeClass24.equals(obj17)) {
                break Label_0843;
            }
            return this.org$json4s$reflect$ScalaType$$NumberType();
        }
        final Class runtimeClass25 = mf.runtimeClass();
        final Class<JsonAST.JObject> obj18 = JsonAST.JObject.class;
        Label_0881: {
            if (runtimeClass25 == null) {
                if (obj18 != null) {
                    break Label_0881;
                }
            }
            else if (!runtimeClass25.equals(obj18)) {
                break Label_0881;
            }
            return this.org$json4s$reflect$ScalaType$$JObjectType();
        }
        final Class runtimeClass26 = mf.runtimeClass();
        final Class<JsonAST.JArray> obj19 = JsonAST.JArray.class;
        Label_0919: {
            if (runtimeClass26 == null) {
                if (obj19 != null) {
                    break Label_0919;
                }
            }
            else if (!runtimeClass26.equals(obj19)) {
                break Label_0919;
            }
            return this.org$json4s$reflect$ScalaType$$JArrayType();
        }
        final Class runtimeClass27 = mf.runtimeClass();
        final Class<JsonAST.JValue> obj20 = JsonAST.JValue.class;
        if (runtimeClass27 == null) {
            if (obj20 != null) {
                return mf.typeArguments().isEmpty() ? this.org$json4s$reflect$ScalaType$$types().apply(mf, (scala.Function1<scala.reflect.Manifest<?>, ScalaType>)new ScalaType$$anonfun$apply.ScalaType$$anonfun$apply$1()) : new ScalaType(mf);
            }
        }
        else if (!runtimeClass27.equals(obj20)) {
            return mf.typeArguments().isEmpty() ? this.org$json4s$reflect$ScalaType$$types().apply(mf, (scala.Function1<scala.reflect.Manifest<?>, ScalaType>)new ScalaType$$anonfun$apply.ScalaType$$anonfun$apply$1()) : new ScalaType(mf);
        }
        return this.org$json4s$reflect$ScalaType$$JValueType();
        scalaType = (mf.typeArguments().isEmpty() ? this.org$json4s$reflect$ScalaType$$types().apply(mf, (scala.Function1<scala.reflect.Manifest<?>, ScalaType>)new ScalaType$$anonfun$apply.ScalaType$$anonfun$apply$1()) : new ScalaType(mf));
        return scalaType;
        scalaType = this.org$json4s$reflect$ScalaType$$StringType();
        return scalaType;
        scalaType = this.org$json4s$reflect$ScalaType$$BooleanType();
        return scalaType;
        scalaType = this.org$json4s$reflect$ScalaType$$BigDecimalType();
        return scalaType;
        scalaType = this.org$json4s$reflect$ScalaType$$BigIntType();
        return scalaType;
        scalaType = this.org$json4s$reflect$ScalaType$$DoubleType();
        return scalaType;
        scalaType = this.org$json4s$reflect$ScalaType$$FloatType();
        return scalaType;
        scalaType = this.org$json4s$reflect$ScalaType$$ShortType();
        return scalaType;
        scalaType = this.org$json4s$reflect$ScalaType$$ByteType();
        return scalaType;
        scalaType = this.org$json4s$reflect$ScalaType$$LongType();
        return scalaType;
        scalaType = this.org$json4s$reflect$ScalaType$$IntType();
        return scalaType;
    }
    
    public ScalaType apply(final Class<?> erasure, final Seq<ScalaType> typeArgs) {
        final Manifest mf = ManifestFactory$.MODULE$.manifestOf(erasure, (Seq<Manifest<?>>)typeArgs.map((Function1)new ScalaType$$anonfun.ScalaType$$anonfun$1(), Seq$.MODULE$.canBuildFrom()));
        return this.apply((scala.reflect.Manifest<Object>)mf);
    }
    
    public ScalaType apply(final package.TypeInfo target) {
        ScalaType scalaType;
        if (target instanceof package.SourceType) {
            scalaType = ((package.SourceType)target).scalaType();
        }
        else {
            final List tArgs = (List)target.parameterizedType().map((Function1)new ScalaType$$anonfun.ScalaType$$anonfun$2()).getOrElse((Function0)new ScalaType$$anonfun.ScalaType$$anonfun$3());
            scalaType = this.apply(target.clazz(), (Seq<ScalaType>)tArgs);
        }
        return scalaType;
    }
    
    public Seq<ScalaType> apply$default$2() {
        return (Seq<ScalaType>)Seq$.MODULE$.empty();
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$IntType() {
        return this.org$json4s$reflect$ScalaType$$IntType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$NumberType() {
        return this.org$json4s$reflect$ScalaType$$NumberType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$LongType() {
        return this.org$json4s$reflect$ScalaType$$LongType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$ByteType() {
        return this.org$json4s$reflect$ScalaType$$ByteType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$ShortType() {
        return this.org$json4s$reflect$ScalaType$$ShortType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$BooleanType() {
        return this.org$json4s$reflect$ScalaType$$BooleanType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$FloatType() {
        return this.org$json4s$reflect$ScalaType$$FloatType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$DoubleType() {
        return this.org$json4s$reflect$ScalaType$$DoubleType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$StringType() {
        return this.org$json4s$reflect$ScalaType$$StringType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$SymbolType() {
        return this.org$json4s$reflect$ScalaType$$SymbolType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$BigDecimalType() {
        return this.org$json4s$reflect$ScalaType$$BigDecimalType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$BigIntType() {
        return this.org$json4s$reflect$ScalaType$$BigIntType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$JValueType() {
        return this.org$json4s$reflect$ScalaType$$JValueType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$JObjectType() {
        return this.org$json4s$reflect$ScalaType$$JObjectType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$JArrayType() {
        return this.org$json4s$reflect$ScalaType$$JArrayType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$DateType() {
        return this.org$json4s$reflect$ScalaType$$DateType;
    }
    
    public ScalaType org$json4s$reflect$ScalaType$$TimestampType() {
        return this.org$json4s$reflect$ScalaType$$TimestampType;
    }
    
    private ScalaType$() {
        MODULE$ = this;
        this.org$json4s$reflect$ScalaType$$types = new package.Memo<Manifest<?>, ScalaType>();
        this.org$json4s$reflect$ScalaType$$singletonFieldName = "MODULE$";
        this.org$json4s$reflect$ScalaType$$IntType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.Manifest().Int());
        this.org$json4s$reflect$ScalaType$$NumberType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.manifest(scala.reflect.ManifestFactory$.MODULE$.classType((Class)Number.class)));
        this.org$json4s$reflect$ScalaType$$LongType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.Manifest().Long());
        this.org$json4s$reflect$ScalaType$$ByteType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.Manifest().Byte());
        this.org$json4s$reflect$ScalaType$$ShortType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.Manifest().Short());
        this.org$json4s$reflect$ScalaType$$BooleanType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.Manifest().Boolean());
        this.org$json4s$reflect$ScalaType$$FloatType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.Manifest().Float());
        this.org$json4s$reflect$ScalaType$$DoubleType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.Manifest().Double());
        this.org$json4s$reflect$ScalaType$$StringType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.manifest(scala.reflect.ManifestFactory$.MODULE$.classType((Class)String.class)));
        this.org$json4s$reflect$ScalaType$$SymbolType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.manifest(scala.reflect.ManifestFactory$.MODULE$.classType((Class)Symbol.class)));
        this.org$json4s$reflect$ScalaType$$BigDecimalType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.manifest(scala.reflect.ManifestFactory$.MODULE$.classType((Class)BigDecimal.class)));
        this.org$json4s$reflect$ScalaType$$BigIntType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.manifest(scala.reflect.ManifestFactory$.MODULE$.classType((Class)BigInt.class)));
        this.org$json4s$reflect$ScalaType$$JValueType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.manifest(scala.reflect.ManifestFactory$.MODULE$.classType((Class)JsonAST.JValue.class)));
        this.org$json4s$reflect$ScalaType$$JObjectType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.manifest(scala.reflect.ManifestFactory$.MODULE$.classType((Class)JsonAST.JObject.class)));
        this.org$json4s$reflect$ScalaType$$JArrayType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.manifest(scala.reflect.ManifestFactory$.MODULE$.classType((Class)JsonAST.JArray.class)));
        this.org$json4s$reflect$ScalaType$$DateType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.manifest(scala.reflect.ManifestFactory$.MODULE$.classType((Class)Date.class)));
        this.org$json4s$reflect$ScalaType$$TimestampType = new ScalaType.PrimitiveScalaType((Manifest<?>)Predef$.MODULE$.manifest(scala.reflect.ManifestFactory$.MODULE$.classType((Class)Timestamp.class)));
    }
}
