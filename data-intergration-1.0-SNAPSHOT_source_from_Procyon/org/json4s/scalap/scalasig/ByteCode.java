// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.io.Codec$;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.Function2;
import org.json4s.scalap.Success;
import scala.runtime.BoxesRunTime;
import org.json4s.scalap.Failure$;
import scala.Product;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005ur!B\u0001\u0003\u0011\u0003Y\u0011\u0001\u0003\"zi\u0016\u001cu\u000eZ3\u000b\u0005\r!\u0011\u0001C:dC2\f7/[4\u000b\u0005\u00151\u0011AB:dC2\f\u0007O\u0003\u0002\b\u0011\u00051!n]8oiMT\u0011!C\u0001\u0004_J<7\u0001\u0001\t\u0003\u00195i\u0011A\u0001\u0004\u0006\u001d\tA\ta\u0004\u0002\t\u0005f$XmQ8eKN\u0011Q\u0002\u0005\t\u0003#Qi\u0011A\u0005\u0006\u0002'\u0005)1oY1mC&\u0011QC\u0005\u0002\u0007\u0003:L(+\u001a4\t\u000b]iA\u0011\u0001\r\u0002\rqJg.\u001b;?)\u0005Y\u0001\"\u0002\u000e\u000e\t\u0003Y\u0012!B1qa2LHc\u0001\u000f\u0002$A\u0011A\"\b\u0004\u0005\u001d\t\u0001ad\u0005\u0002\u001e!!A\u0001%\bBC\u0002\u0013\u0005\u0011%A\u0003csR,7/F\u0001#!\r\t2%J\u0005\u0003II\u0011Q!\u0011:sCf\u0004\"!\u0005\u0014\n\u0005\u001d\u0012\"\u0001\u0002\"zi\u0016D\u0001\"K\u000f\u0003\u0002\u0003\u0006IAI\u0001\u0007Ef$Xm\u001d\u0011\t\u0011-j\"Q1A\u0005\u00021\n1\u0001]8t+\u0005i\u0003CA\t/\u0013\ty#CA\u0002J]RD\u0001\"M\u000f\u0003\u0002\u0003\u0006I!L\u0001\u0005a>\u001c\b\u0005\u0003\u00054;\t\u0015\r\u0011\"\u0001-\u0003\u0019aWM\\4uQ\"AQ'\bB\u0001B\u0003%Q&A\u0004mK:<G\u000f\u001b\u0011\t\u000b]iB\u0011A\u001c\u0015\tqA\u0014H\u000f\u0005\u0006AY\u0002\rA\t\u0005\u0006WY\u0002\r!\f\u0005\u0006gY\u0002\r!\f\u0005\u0006yu!\t!P\u0001\t]\u0016DHOQ=uKV\taH\u0005\u0003@\u0007\u001aKe\u0001\u0002!\u0001\u0001y\u0012A\u0002\u0010:fM&tW-\\3oizR!A\u0011\u0006\u0002\rq\u0012xn\u001c;?!\t\tB)\u0003\u0002F%\t9\u0001K]8ek\u000e$\bCA\tH\u0013\tA%C\u0001\u0007TKJL\u0017\r\\5{C\ndW\rE\u0003K\u0017r)S*D\u0001\u0005\u0013\taEA\u0001\u0004SKN,H\u000e\u001e\t\u0003#9K!a\u0014\n\u0003\u000f9{G\u000f[5oO\")\u0011+\bC\u0001%\u0006!a.\u001a=u)\t\u0019fK\u0005\u0003U\u0007\u001a+f\u0001\u0002!\u0001\u0001M\u0003RAS&\u001d95CQa\u0016)A\u00025\n\u0011A\u001c\u0005\u00063v!\tAW\u0001\u0005i\u0006\\W\r\u0006\u0002\u001d7\")q\u000b\u0017a\u0001[!)Q,\bC\u0001=\u0006!AM]8q)\tar\fC\u0003X9\u0002\u0007Q\u0006C\u0003b;\u0011\u0005!-\u0001\u0003g_2$WCA2h)\t!'\u000f\u0006\u0002f[B\u0011am\u001a\u0007\u0001\t\u0015A\u0007M1\u0001j\u0005\u0005A\u0016CA'k!\t\t2.\u0003\u0002m%\t\u0019\u0011I\\=\t\u000b9\u0004\u0007\u0019A8\u0002\u0003\u0019\u0004R!\u00059fK\u0015L!!\u001d\n\u0003\u0013\u0019+hn\u0019;j_:\u0014\u0004\"B:a\u0001\u0004)\u0017!\u0001=\t\u000bUlB\u0011\t<\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012a\u001e\t\u0003qnt!!E=\n\u0005i\u0014\u0012A\u0002)sK\u0012,g-\u0003\u0002}{\n11\u000b\u001e:j]\u001eT!A\u001f\n\t\u000b}lB\u0011\u0001\u0017\u0002\u000bQ|\u0017J\u001c;\t\u000f\u0005\rQ\u0004\"\u0001\u0002\u0006\u00051Ao\u001c'p]\u001e,\"!a\u0002\u0011\u0007E\tI!C\u0002\u0002\fI\u0011A\u0001T8oO\"9\u0011qB\u000f\u0005\u0002\u0005E\u0011A\u00064s_6,FK\u0012\u001dTiJLgnZ!oI\nKH/Z:\u0016\u0005\u0005M\u0001c\u0001\u0007\u0002\u0016%\u0019\u0011q\u0003\u0002\u0003\u001fM#(/\u001b8h\u0005f$Xm\u001d)bSJDq!a\u0007\u001e\t\u0003\ti\"\u0001\u0003csR,GcA\u0017\u0002 !9\u0011\u0011EA\r\u0001\u0004i\u0013!A5\t\u000b\u0001J\u0002\u0019\u0001\u0012\t\u000f\u0005\u001dR\u0002\"\u0001\u0002*\u0005Aam\u001c:DY\u0006\u001c8\u000fF\u0002\u001d\u0003WA\u0001\"!\f\u0002&\u0001\u0007\u0011qF\u0001\u0006G2\f'P\u001f\u0019\u0005\u0003c\tI\u0004E\u0003y\u0003g\t9$C\u0002\u00026u\u0014Qa\u00117bgN\u00042AZA\u001d\t-\tY$a\u000b\u0002\u0002\u0003\u0005)\u0011A5\u0003\u0007}#\u0013\u0007")
public class ByteCode
{
    private final byte[] bytes;
    private final int pos;
    private final int length;
    
    public static ByteCode forClass(final Class<?> clazz) {
        return ByteCode$.MODULE$.forClass(clazz);
    }
    
    public static ByteCode apply(final byte[] bytes) {
        return ByteCode$.MODULE$.apply(bytes);
    }
    
    public byte[] bytes() {
        return this.bytes;
    }
    
    public int pos() {
        return this.pos;
    }
    
    public int length() {
        return this.length;
    }
    
    public Product nextByte() {
        return (Product)((this.length() == 0) ? Failure$.MODULE$ : new Success(this.drop(1), BoxesRunTime.boxToByte(this.bytes()[this.pos()])));
    }
    
    public Product next(final int n) {
        return (Product)((this.length() >= n) ? new Success(this.drop(n), this.take(n)) : Failure$.MODULE$);
    }
    
    public ByteCode take(final int n) {
        return new ByteCode(this.bytes(), this.pos(), n);
    }
    
    public ByteCode drop(final int n) {
        return new ByteCode(this.bytes(), this.pos() + n, this.length() - n);
    }
    
    public <X> X fold(final X x, final Function2<X, Object, X> f) {
        Object result = x;
        for (int i = this.pos(); i < this.pos() + this.length(); ++i) {
            result = f.apply(result, (Object)BoxesRunTime.boxToByte(this.bytes()[i]));
        }
        return (X)result;
    }
    
    @Override
    public String toString() {
        return new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "", "bytes" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToInteger(this.length()) }));
    }
    
    public int toInt() {
        return BoxesRunTime.unboxToInt((Object)this.fold(BoxesRunTime.boxToInteger(0), (scala.Function2<Integer, Object, Integer>)new ByteCode$$anonfun$toInt.ByteCode$$anonfun$toInt$1(this)));
    }
    
    public long toLong() {
        return BoxesRunTime.unboxToLong((Object)this.fold(BoxesRunTime.boxToLong(0L), (scala.Function2<Long, Object, Long>)new ByteCode$$anonfun$toLong.ByteCode$$anonfun$toLong$1(this)));
    }
    
    public StringBytesPair fromUTF8StringAndBytes() {
        final byte[] chunk = new byte[this.length()];
        System.arraycopy(this.bytes(), this.pos(), chunk, 0, this.length());
        final String str = new String(Codec$.MODULE$.fromUTF8(this.bytes(), this.pos(), this.length()));
        return new StringBytesPair(str, chunk);
    }
    
    public int byte(final int i) {
        return this.bytes()[this.pos()] & 0xFF;
    }
    
    public ByteCode(final byte[] bytes, final int pos, final int length) {
        this.bytes = bytes;
        this.pos = pos;
        this.length = length;
        Predef$.MODULE$.assert(pos >= 0 && length >= 0 && pos + length <= bytes.length);
    }
}
