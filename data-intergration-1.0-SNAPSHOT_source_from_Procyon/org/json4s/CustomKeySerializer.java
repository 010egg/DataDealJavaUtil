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

@ScalaSignature(bytes = "\u0006\u0001\r4A!\u0001\u0002\u0001\u000f\t\u00192)^:u_6\\U-_*fe&\fG.\u001b>fe*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001)\"\u0001C\u000b\u0014\u0007\u0001Iq\u0002\u0005\u0002\u000b\u001b5\t1BC\u0001\r\u0003\u0015\u00198-\u00197b\u0013\tq1B\u0001\u0004B]f\u0014VM\u001a\t\u0004!E\u0019R\"\u0001\u0002\n\u0005I\u0011!!D&fsN+'/[1mSj,'\u000f\u0005\u0002\u0015+1\u0001A!\u0002\f\u0001\u0005\u00049\"!A!\u0012\u0005aY\u0002C\u0001\u0006\u001a\u0013\tQ2BA\u0004O_RD\u0017N\\4\u0011\u0005)a\u0012BA\u000f\f\u0005\r\te.\u001f\u0005\t?\u0001\u0011\t\u0011)A\u0005A\u0005\u00191/\u001a:\u0011\t)\t3EJ\u0005\u0003E-\u0011\u0011BR;oGRLwN\\\u0019\u0011\u0005A!\u0013BA\u0013\u0003\u0005\u001d1uN]7biN\u0004BAC\u0014*g%\u0011\u0001f\u0003\u0002\u0007)V\u0004H.\u001a\u001a\u0011\t)QCfE\u0005\u0003W-\u0011q\u0002U1si&\fGNR;oGRLwN\u001c\t\u0003[Ar!A\u0003\u0018\n\u0005=Z\u0011A\u0002)sK\u0012,g-\u0003\u00022e\t11\u000b\u001e:j]\u001eT!aL\u0006\u0011\t)Q3\u0004\f\u0005\tk\u0001\u0011\u0019\u0011)A\u0006m\u0005QQM^5eK:\u001cW\r\n\u001a\u0011\u00075:4#\u0003\u00029e\tAQ*\u00198jM\u0016\u001cH\u000fC\u0003;\u0001\u0011\u00051(\u0001\u0004=S:LGO\u0010\u000b\u0003y}\"\"!\u0010 \u0011\u0007A\u00011\u0003C\u00036s\u0001\u000fa\u0007C\u0003 s\u0001\u0007\u0001\u0005C\u0004B\u0001\t\u0007I\u0011\u0001\"\u0002\u000b\rc\u0017m]:\u0016\u0003\r\u0003$\u0001R'\u0011\u0007\u0015SE*D\u0001G\u0015\t9\u0005*\u0001\u0003mC:<'\"A%\u0002\t)\fg/Y\u0005\u0003\u0017\u001a\u0013Qa\u00117bgN\u0004\"\u0001F'\u0005\u00139{\u0015\u0011!A\u0001\u0006\u00039\"aA0%c!1\u0001\u000b\u0001Q\u0001\n\r\u000baa\u00117bgN\u0004\u0003\"\u0002*\u0001\t\u0003\u0019\u0016a\u00033fg\u0016\u0014\u0018.\u00197ju\u0016$\"\u0001V/\u0011\t)QSk\u0005\t\u0005\u0015\u001d2F\u0006\u0005\u0002X5:\u0011\u0001\u0003W\u0005\u00033\n\tq\u0001]1dW\u0006<W-\u0003\u0002\\9\nAA+\u001f9f\u0013:4wN\u0003\u0002Z\u0005!)a,\u0015a\u0002G\u00051am\u001c:nCRDQ\u0001\u0019\u0001\u0005\u0002\u0005\f\u0011b]3sS\u0006d\u0017N_3\u0015\u0005M\u0012\u0007\"\u00020`\u0001\b\u0019\u0003")
public class CustomKeySerializer<A> implements KeySerializer<A>
{
    public final Function1<Formats, Tuple2<PartialFunction<String, A>, PartialFunction<Object, String>>> org$json4s$CustomKeySerializer$$ser;
    private final Class<?> Class;
    
    public Class<?> Class() {
        return this.Class;
    }
    
    @Override
    public PartialFunction<Tuple2<package.TypeInfo, String>, A> deserialize(final Formats format) {
        return (PartialFunction<Tuple2<package.TypeInfo, String>, A>)new CustomKeySerializer$$anonfun$deserialize.CustomKeySerializer$$anonfun$deserialize$2(this, format);
    }
    
    @Override
    public PartialFunction<Object, String> serialize(final Formats format) {
        return (PartialFunction<Object, String>)((Tuple2)this.org$json4s$CustomKeySerializer$$ser.apply((Object)format))._2();
    }
    
    public CustomKeySerializer(final Function1<Formats, Tuple2<PartialFunction<String, A>, PartialFunction<Object, String>>> ser, final Manifest<A> evidence$2) {
        this.org$json4s$CustomKeySerializer$$ser = ser;
        this.Class = (Class<?>)((ClassTag)Predef$.MODULE$.implicitly((Object)evidence$2)).runtimeClass();
    }
}
