// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Predef$;
import scala.reflect.ClassTag;
import scala.reflect.Manifest;
import org.json4s.reflect.package;
import scala.PartialFunction;
import scala.Tuple2;
import scala.Function1;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\r4A!\u0001\u0002\u0001\u000f\t\u00012)^:u_6\u001cVM]5bY&TXM\u001d\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0016\u0005!)2c\u0001\u0001\n\u001fA\u0011!\"D\u0007\u0002\u0017)\tA\"A\u0003tG\u0006d\u0017-\u0003\u0002\u000f\u0017\t1\u0011I\\=SK\u001a\u00042\u0001E\t\u0014\u001b\u0005\u0011\u0011B\u0001\n\u0003\u0005)\u0019VM]5bY&TXM\u001d\t\u0003)Ua\u0001\u0001B\u0003\u0017\u0001\t\u0007qCA\u0001B#\tA2\u0004\u0005\u0002\u000b3%\u0011!d\u0003\u0002\b\u001d>$\b.\u001b8h!\tQA$\u0003\u0002\u001e\u0017\t\u0019\u0011I\\=\t\u0011}\u0001!\u0011!Q\u0001\n\u0001\n1a]3s!\u0011Q\u0011e\t\u0014\n\u0005\tZ!!\u0003$v]\u000e$\u0018n\u001c82!\t\u0001B%\u0003\u0002&\u0005\t9ai\u001c:nCR\u001c\b\u0003\u0002\u0006(SMJ!\u0001K\u0006\u0003\rQ+\b\u000f\\33!\u0011Q!\u0006L\n\n\u0005-Z!a\u0004)beRL\u0017\r\u001c$v]\u000e$\u0018n\u001c8\u0011\u00055\u0002dB\u0001\t/\u0013\ty#!A\u0004qC\u000e\\\u0017mZ3\n\u0005E\u0012$A\u0002&WC2,XM\u0003\u00020\u0005A!!BK\u000e-\u0011!)\u0004AaA!\u0002\u00171\u0014AC3wS\u0012,gnY3%cA\u0019qGO\n\u000f\u0005)A\u0014BA\u001d\f\u0003\u0019\u0001&/\u001a3fM&\u00111\b\u0010\u0002\t\u001b\u0006t\u0017NZ3ti*\u0011\u0011h\u0003\u0005\u0006}\u0001!\taP\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0005\u0001\u001bECA!C!\r\u0001\u0002a\u0005\u0005\u0006ku\u0002\u001dA\u000e\u0005\u0006?u\u0002\r\u0001\t\u0005\b\u000b\u0002\u0011\r\u0011\"\u0001G\u0003\u0015\u0019E.Y:t+\u00059\u0005G\u0001%R!\rIe\nU\u0007\u0002\u0015*\u00111\nT\u0001\u0005Y\u0006twMC\u0001N\u0003\u0011Q\u0017M^1\n\u0005=S%!B\"mCN\u001c\bC\u0001\u000bR\t%\u00116+!A\u0001\u0002\u000b\u0005qCA\u0002`IEBa\u0001\u0016\u0001!\u0002\u00139\u0015AB\"mCN\u001c\b\u0005C\u0003W\u0001\u0011\u0005q+A\u0006eKN,'/[1mSj,GC\u0001-^!\u0011Q!&W\n\u0011\t)9#\f\f\t\u0003[mK!\u0001\u0018\u001a\u0003\u0011QK\b/Z%oM>DQAX+A\u0004\r\naAZ8s[\u0006$\b\"\u00021\u0001\t\u0003\t\u0017!C:fe&\fG.\u001b>f)\t\u0019$\rC\u0003_?\u0002\u000f1\u0005")
public class CustomSerializer<A> implements Serializer<A>
{
    public final Function1<Formats, Tuple2<PartialFunction<JsonAST.JValue, A>, PartialFunction<Object, JsonAST.JValue>>> org$json4s$CustomSerializer$$ser;
    private final Class<?> Class;
    
    public Class<?> Class() {
        return this.Class;
    }
    
    @Override
    public PartialFunction<Tuple2<package.TypeInfo, JsonAST.JValue>, A> deserialize(final Formats format) {
        return (PartialFunction<Tuple2<package.TypeInfo, JsonAST.JValue>, A>)new CustomSerializer$$anonfun$deserialize.CustomSerializer$$anonfun$deserialize$1(this, format);
    }
    
    @Override
    public PartialFunction<Object, JsonAST.JValue> serialize(final Formats format) {
        return (PartialFunction<Object, JsonAST.JValue>)((Tuple2)this.org$json4s$CustomSerializer$$ser.apply((Object)format))._2();
    }
    
    public CustomSerializer(final Function1<Formats, Tuple2<PartialFunction<JsonAST.JValue, A>, PartialFunction<Object, JsonAST.JValue>>> ser, final Manifest<A> evidence$1) {
        this.org$json4s$CustomSerializer$$ser = ser;
        this.Class = (Class<?>)((ClassTag)Predef$.MODULE$.implicitly((Object)evidence$1)).runtimeClass();
    }
}
