// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigInt;
import scala.Function1;
import scala.Option;
import scala.collection.immutable.Map;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u0015gaB\u0001\u0003!\u0003\r\ta\u0002\u0002\u000f\t\u00164\u0017-\u001e7u/JLG/\u001a:t\u0015\t\u0019A!\u0001\u0004kg>tGg\u001d\u0006\u0002\u000b\u0005\u0019qN]4\u0004\u0001M\u0011\u0001\u0001\u0003\t\u0003\u00131i\u0011A\u0003\u0006\u0002\u0017\u0005)1oY1mC&\u0011QB\u0003\u0002\u0007\u0003:L(+\u001a4\t\u000b=\u0001A\u0011\u0001\t\u0002\r\u0011Jg.\u001b;%)\u0005\t\u0002CA\u0005\u0013\u0013\t\u0019\"B\u0001\u0003V]&$h!B\u000b\u0001\u0003#1\"!A,\u0016\u0005]q2c\u0001\u000b\t1A\u0019\u0011D\u0007\u000f\u000e\u0003\tI!a\u0007\u0002\u0003\r]\u0013\u0018\u000e^3s!\tib\u0004\u0004\u0001\u0005\r}!\u0002R1\u0001!\u0005\u0005!\u0016CA\u0011%!\tI!%\u0003\u0002$\u0015\t9aj\u001c;iS:<\u0007CA\u0005&\u0013\t1#BA\u0002B]fD\u0001\u0002\u000b\u000b\u0003\u0002\u0003\u0006I!K\u0001\u0003M:\u0004B!\u0003\u0016\u001dY%\u00111F\u0003\u0002\n\rVt7\r^5p]F\u0002\"!\f\u0019\u000f\u0005eq\u0013BA\u0018\u0003\u0003\u001d\u0001\u0018mY6bO\u0016L!!\r\u001a\u0003\r)3\u0016\r\\;f\u0015\ty#\u0001C\u00035)\u0011\u0005Q'\u0001\u0004=S:LGO\u0010\u000b\u0003ma\u00022a\u000e\u000b\u001d\u001b\u0005\u0001\u0001\"\u0002\u00154\u0001\u0004I\u0003\"\u0002\u001e\u0015\t\u0003Y\u0014!B<sSR,GC\u0001\u001fF!\ti\u0004G\u0004\u0002?]9\u0011q\b\u0012\b\u0003\u0001\u000ek\u0011!\u0011\u0006\u0003\u0005\u001a\ta\u0001\u0010:p_Rt\u0014\"A\u0003\n\u0005\r!\u0001\"\u0002$:\u0001\u0004a\u0012aA8cU\u001e)\u0001\n\u0001E\u0002\u0013\u0006I\u0011J\u001c;Xe&$XM\u001d\t\u0003o)3Qa\u0013\u0001\t\u00021\u0013\u0011\"\u00138u/JLG/\u001a:\u0014\u0005)k\u0005cA\u001c\u0015\u001dB\u0011\u0011bT\u0005\u0003!*\u00111!\u00138u\u0011\u0015!$\n\"\u0001S)\u0005Iu!\u0002+\u0001\u0011\u0007)\u0016A\u0003\"zi\u0016<&/\u001b;feB\u0011qG\u0016\u0004\u0006/\u0002A\t\u0001\u0017\u0002\u000b\u0005f$Xm\u0016:ji\u0016\u00148C\u0001,Z!\r9DC\u0017\t\u0003\u0013mK!\u0001\u0018\u0006\u0003\t\tKH/\u001a\u0005\u0006iY#\tA\u0018\u000b\u0002+\u001e)\u0001\r\u0001E\u0002C\u0006Y1\u000b[8si^\u0013\u0018\u000e^3s!\t9$MB\u0003d\u0001!\u0005AMA\u0006TQ>\u0014Ho\u0016:ji\u0016\u00148C\u00012f!\r9DC\u001a\t\u0003\u0013\u001dL!\u0001\u001b\u0006\u0003\u000bMCwN\u001d;\t\u000bQ\u0012G\u0011\u00016\u0015\u0003\u0005<Q\u0001\u001c\u0001\t\u00045\f!\u0002T8oO^\u0013\u0018\u000e^3s!\t9dNB\u0003p\u0001!\u0005\u0001O\u0001\u0006M_:<wK]5uKJ\u001c\"A\\9\u0011\u0007]\"\"\u000f\u0005\u0002\ng&\u0011AO\u0003\u0002\u0005\u0019>tw\rC\u00035]\u0012\u0005a\u000fF\u0001n\u000f\u0015A\b\u0001c\u0001z\u00031\u0011\u0015nZ%oi^\u0013\u0018\u000e^3s!\t9$PB\u0003|\u0001!\u0005AP\u0001\u0007CS\u001eLe\u000e^,sSR,'o\u0005\u0002{{B\u0019q\u0007\u0006@\u0011\u0007}\f9A\u0004\u0003\u0002\u0002\u0005\u0015ab\u0001!\u0002\u0004%\t1\"\u0003\u00020\u0015%!\u0011\u0011BA\u0006\u0005\u0019\u0011\u0015nZ%oi*\u0011qF\u0003\u0005\u0007ii$\t!a\u0004\u0015\u0003e<q!a\u0005\u0001\u0011\u0007\t)\"A\u0007C_>dW-\u00198Xe&$XM\u001d\t\u0004o\u0005]aaBA\r\u0001!\u0005\u00111\u0004\u0002\u000e\u0005>|G.Z1o/JLG/\u001a:\u0014\t\u0005]\u0011Q\u0004\t\u0005oQ\ty\u0002E\u0002\n\u0003CI1!a\t\u000b\u0005\u001d\u0011un\u001c7fC:Dq\u0001NA\f\t\u0003\t9\u0003\u0006\u0002\u0002\u0016\u001d9\u00111\u0006\u0001\t\u0004\u00055\u0012\u0001D*ue&twm\u0016:ji\u0016\u0014\bcA\u001c\u00020\u00199\u0011\u0011\u0007\u0001\t\u0002\u0005M\"\u0001D*ue&twm\u0016:ji\u0016\u00148\u0003BA\u0018\u0003k\u0001Ba\u000e\u000b\u00028A!\u0011\u0011HA \u001d\rI\u00111H\u0005\u0004\u0003{Q\u0011A\u0002)sK\u0012,g-\u0003\u0003\u0002B\u0005\r#AB*ue&twMC\u0002\u0002>)Aq\u0001NA\u0018\t\u0003\t9\u0005\u0006\u0002\u0002.!9\u00111\n\u0001\u0005\u0004\u00055\u0013aC1se\u0006LxK]5uKJ,B!a\u0014\u0002\\Q!\u0011\u0011KA/!\u0011I\"$a\u0015\u0011\u000b%\t)&!\u0017\n\u0007\u0005]#BA\u0003BeJ\f\u0017\u0010E\u0002\u001e\u00037\"aaHA%\u0005\u0004\u0001\u0003\u0002CA0\u0003\u0013\u0002\u001d!!\u0019\u0002\u0017Y\fG.^3Xe&$XM\u001d\t\u00053i\tI\u0006C\u0004\u0002f\u0001!\u0019!a\u001a\u0002\u00135\f\u0007o\u0016:ji\u0016\u0014X\u0003BA5\u0003\u007f\"B!a\u001b\u0002\u0004B!\u0011DGA7!!\ty'!\u001f\u00028\u0005uTBAA9\u0015\u0011\t\u0019(!\u001e\u0002\u0013%lW.\u001e;bE2,'bAA<\u0015\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\t\u0005m\u0014\u0011\u000f\u0002\u0004\u001b\u0006\u0004\bcA\u000f\u0002\u0000\u00119\u0011\u0011QA2\u0005\u0004\u0001#!\u0001,\t\u0011\u0005}\u00131\ra\u0002\u0003\u000b\u0003B!\u0007\u000e\u0002~\u001d9\u0011\u0011\u0012\u0001\t\u0004\u0005-\u0015\u0001\u0004&WC2,Xm\u0016:ji\u0016\u0014\bcA\u001c\u0002\u000e\u001a9\u0011q\u0012\u0001\t\u0002\u0005E%\u0001\u0004&WC2,Xm\u0016:ji\u0016\u00148\u0003BAG\u0003'\u00032a\u000e\u000b-\u0011\u001d!\u0014Q\u0012C\u0001\u0003/#\"!a#\t\u000f\u0005m\u0005\u0001b\u0001\u0002\u001e\u0006aq\n\u001d;j_:<&/\u001b;feV!\u0011qTAV)\u0011\t\t+!,\u0011\teQ\u00121\u0015\t\u0006\u0013\u0005\u0015\u0016\u0011V\u0005\u0004\u0003OS!AB(qi&|g\u000eE\u0002\u001e\u0003W#aaHAM\u0005\u0004\u0001\u0003\u0002CA0\u00033\u0003\u001d!a,\u0011\teQ\u0012\u0011V\u0004\b\u0003g\u0013\u0001\u0012AA[\u00039!UMZ1vYR<&/\u001b;feN\u00042!GA\\\r\u0019\t!\u0001#\u0001\u0002:N)\u0011q\u0017\u0005\u0002<B\u0019\u0011$!0\n\u0007\u0005}&AA\u0007E_V\u0014G.Z,sSR,'o\u001d\u0005\bi\u0005]F\u0011AAb)\t\t)\f")
public interface DefaultWriters
{
    IntWriter$ IntWriter();
    
    ByteWriter$ ByteWriter();
    
    ShortWriter$ ShortWriter();
    
    LongWriter$ LongWriter();
    
    BigIntWriter$ BigIntWriter();
    
    BooleanWriter$ BooleanWriter();
    
    StringWriter$ StringWriter();
    
     <T> Writer<Object> arrayWriter(final Writer<T> p0);
    
     <V> Writer<Map<String, V>> mapWriter(final Writer<V> p0);
    
    JValueWriter$ JValueWriter();
    
     <T> Writer<Option<T>> OptionWriter(final Writer<T> p0);
    
    public abstract class W<T> implements Writer<T>
    {
        private final Function1<T, JsonAST.JValue> fn;
        
        @Override
        public JsonAST.JValue write(final T obj) {
            return (JsonAST.JValue)this.fn.apply((Object)obj);
        }
        
        public W(final Function1<T, JsonAST.JValue> fn) {
            this.fn = fn;
            if (DefaultWriters.this == null) {
                throw null;
            }
        }
    }
    
    public class IntWriter$ extends W<Object>
    {
        public IntWriter$(final DefaultWriters $outer) {
            $outer.super((Function1)new DefaultWriters$IntWriter$$anonfun$$lessinit$greater.DefaultWriters$IntWriter$$anonfun$$lessinit$greater$1($outer));
        }
    }
    
    public class ByteWriter$ extends W<Object>
    {
        public ByteWriter$(final DefaultWriters $outer) {
            $outer.super((Function1)new DefaultWriters$ByteWriter$$anonfun$$lessinit$greater.DefaultWriters$ByteWriter$$anonfun$$lessinit$greater$2($outer));
        }
    }
    
    public class ShortWriter$ extends W<Object>
    {
        public ShortWriter$(final DefaultWriters $outer) {
            $outer.super((Function1)new DefaultWriters$ShortWriter$$anonfun$$lessinit$greater.DefaultWriters$ShortWriter$$anonfun$$lessinit$greater$3($outer));
        }
    }
    
    public class LongWriter$ extends W<Object>
    {
        public LongWriter$(final DefaultWriters $outer) {
            $outer.super((Function1)new DefaultWriters$LongWriter$$anonfun$$lessinit$greater.DefaultWriters$LongWriter$$anonfun$$lessinit$greater$4($outer));
        }
    }
    
    public class BigIntWriter$ extends W<BigInt>
    {
        public BigIntWriter$(final DefaultWriters $outer) {
            $outer.super((Function1)new DefaultWriters$BigIntWriter$$anonfun$$lessinit$greater.DefaultWriters$BigIntWriter$$anonfun$$lessinit$greater$5($outer));
        }
    }
    
    public class BooleanWriter$ extends W<Object>
    {
        public BooleanWriter$(final DefaultWriters $outer) {
            $outer.super((Function1)new DefaultWriters$BooleanWriter$$anonfun$$lessinit$greater.DefaultWriters$BooleanWriter$$anonfun$$lessinit$greater$6($outer));
        }
    }
    
    public class StringWriter$ extends W<String>
    {
        public StringWriter$(final DefaultWriters $outer) {
            $outer.super((Function1)new DefaultWriters$StringWriter$$anonfun$$lessinit$greater.DefaultWriters$StringWriter$$anonfun$$lessinit$greater$7($outer));
        }
    }
    
    public class JValueWriter$ extends W<JsonAST.JValue>
    {
        public JValueWriter$(final DefaultWriters $outer) {
            $outer.super((Function1)new DefaultWriters$JValueWriter$$anonfun$$lessinit$greater.DefaultWriters$JValueWriter$$anonfun$$lessinit$greater$8($outer));
        }
    }
}
