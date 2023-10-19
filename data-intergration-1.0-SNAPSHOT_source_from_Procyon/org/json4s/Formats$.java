// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import org.json4s.reflect.package;
import scala.Tuple2;
import scala.Function0;
import scala.PartialFunction;
import scala.Serializable;

public final class Formats$ implements Serializable
{
    public static final Formats$ MODULE$;
    
    static {
        new Formats$();
    }
    
    public <T> T read(final JsonAST.JValue json, final Reader<T> reader) {
        return reader.read(json);
    }
    
    public <T> JsonAST.JValue write(final T obj, final Writer<T> writer) {
        return writer.write(obj);
    }
    
    public PartialFunction<Object, JsonAST.JValue> customSerializer(final Object a, final Formats format) {
        return (PartialFunction<Object, JsonAST.JValue>)format.customSerializers().collectFirst((PartialFunction)new Formats$$anonfun$customSerializer.Formats$$anonfun$customSerializer$1(a, format)).getOrElse((Function0)new Formats$$anonfun$customSerializer.Formats$$anonfun$customSerializer$2());
    }
    
    public PartialFunction<Tuple2<package.TypeInfo, JsonAST.JValue>, Object> customDeserializer(final Tuple2<package.TypeInfo, JsonAST.JValue> a, final Formats format) {
        return (PartialFunction<Tuple2<package.TypeInfo, JsonAST.JValue>, Object>)format.customSerializers().collectFirst((PartialFunction)new Formats$$anonfun$customDeserializer.Formats$$anonfun$customDeserializer$1((Tuple2)a, format)).getOrElse((Function0)new Formats$$anonfun$customDeserializer.Formats$$anonfun$customDeserializer$2());
    }
    
    public PartialFunction<Object, String> customKeySerializer(final Object a, final Formats format) {
        return (PartialFunction<Object, String>)format.customKeySerializers().collectFirst((PartialFunction)new Formats$$anonfun$customKeySerializer.Formats$$anonfun$customKeySerializer$1(a, format)).getOrElse((Function0)new Formats$$anonfun$customKeySerializer.Formats$$anonfun$customKeySerializer$2());
    }
    
    public PartialFunction<Tuple2<package.TypeInfo, String>, Object> customKeyDeserializer(final Tuple2<package.TypeInfo, String> a, final Formats format) {
        return (PartialFunction<Tuple2<package.TypeInfo, String>, Object>)format.customKeySerializers().collectFirst((PartialFunction)new Formats$$anonfun$customKeyDeserializer.Formats$$anonfun$customKeyDeserializer$1((Tuple2)a, format)).getOrElse((Function0)new Formats$$anonfun$customKeyDeserializer.Formats$$anonfun$customKeyDeserializer$2());
    }
    
    private Object readResolve() {
        return Formats$.MODULE$;
    }
    
    private Formats$() {
        MODULE$ = this;
    }
}
