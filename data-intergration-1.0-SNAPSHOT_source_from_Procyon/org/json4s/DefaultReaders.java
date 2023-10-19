// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigDecimal$;
import scala.package$;
import scala.runtime.BoxesRunTime;
import scala.math.BigDecimal;
import scala.math.BigInt;
import scala.collection.Seq;
import scala.collection.immutable.StringOps;
import scala.Predef$;
import scala.reflect.Manifest;
import scala.collection.immutable.Map;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\tut!B\u0001\u0003\u0011\u00039\u0011A\u0004#fM\u0006,H\u000e\u001e*fC\u0012,'o\u001d\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0011\u0005!IQ\"\u0001\u0002\u0007\u000b)\u0011\u0001\u0012A\u0006\u0003\u001d\u0011+g-Y;miJ+\u0017\rZ3sgN\u0019\u0011\u0002\u0004\n\u0011\u00055\u0001R\"\u0001\b\u000b\u0003=\tQa]2bY\u0006L!!\u0005\b\u0003\r\u0005s\u0017PU3g!\tA1CB\u0004\u000b\u0005A\u0005\u0019\u0011\u0001\u000b\u0014\u0007MaQ\u0003\u0005\u0002\t-%\u0011qC\u0001\u0002\u0010\t\u00164\u0017-\u001e7u%\u0016\fG-\u001a:ta!)\u0011d\u0005C\u00015\u00051A%\u001b8ji\u0012\"\u0012a\u0007\t\u0003\u001bqI!!\b\b\u0003\tUs\u0017\u000e^\u0004\u0006?MA\u0019\u0001I\u0001\n\u0013:$(+Z1eKJ\u0004\"!\t\u0012\u000e\u0003M1QaI\n\t\u0002\u0011\u0012\u0011\"\u00138u%\u0016\fG-\u001a:\u0014\u0007\tbQ\u0005E\u0002\tM!J!a\n\u0002\u0003\rI+\u0017\rZ3s!\ti\u0011&\u0003\u0002+\u001d\t\u0019\u0011J\u001c;\t\u000b1\u0012C\u0011A\u0017\u0002\rqJg.\u001b;?)\u0005\u0001\u0003\"B\u0018#\t\u0003\u0001\u0014\u0001\u0002:fC\u0012$\"\u0001K\u0019\t\u000bIr\u0003\u0019A\u001a\u0002\u000bY\fG.^3\u0011\u0005Q:dB\u0001\u00056\u0013\t1$!A\u0004qC\u000e\\\u0017mZ3\n\u0005aJ$A\u0002&WC2,XM\u0003\u00027\u0005\u001d)1h\u0005E\u0002y\u0005a!)[4J]R\u0014V-\u00193feB\u0011\u0011%\u0010\u0004\u0006}MA\ta\u0010\u0002\r\u0005&<\u0017J\u001c;SK\u0006$WM]\n\u0004{1\u0001\u0005c\u0001\u0005'\u0003B\u0011!)\u0013\b\u0003\u0007\"s!\u0001R$\u000e\u0003\u0015S!A\u0012\u0004\u0002\rq\u0012xn\u001c;?\u0013\u0005y\u0011B\u0001\u001c\u000f\u0013\tQ5J\u0001\u0004CS\u001eLe\u000e\u001e\u0006\u0003m9AQ\u0001L\u001f\u0005\u00025#\u0012\u0001\u0010\u0005\u0006_u\"\ta\u0014\u000b\u0003\u0003BCQA\r(A\u0002M:QAU\n\t\u0004M\u000b!\u0002T8oOJ+\u0017\rZ3s!\t\tCKB\u0003V'!\u0005aK\u0001\u0006M_:<'+Z1eKJ\u001c2\u0001\u0016\u0007X!\rAa\u0005\u0017\t\u0003\u001beK!A\u0017\b\u0003\t1{gn\u001a\u0005\u0006YQ#\t\u0001\u0018\u000b\u0002'\")q\u0006\u0016C\u0001=R\u0011\u0001l\u0018\u0005\u0006eu\u0003\raM\u0004\u0006CNA\u0019AY\u0001\f'\"|'\u000f\u001e*fC\u0012,'\u000f\u0005\u0002\"G\u001a)Am\u0005E\u0001K\nY1\u000b[8siJ+\u0017\rZ3s'\r\u0019GB\u001a\t\u0004\u0011\u0019:\u0007CA\u0007i\u0013\tIgBA\u0003TQ>\u0014H\u000fC\u0003-G\u0012\u00051\u000eF\u0001c\u0011\u0015y3\r\"\u0001n)\t9g\u000eC\u00033Y\u0002\u00071gB\u0003q'!\r\u0011/\u0001\u0006CsR,'+Z1eKJ\u0004\"!\t:\u0007\u000bM\u001c\u0002\u0012\u0001;\u0003\u0015\tKH/\u001a*fC\u0012,'oE\u0002s\u0019U\u00042\u0001\u0003\u0014w!\tiq/\u0003\u0002y\u001d\t!!)\u001f;f\u0011\u0015a#\u000f\"\u0001{)\u0005\t\b\"B\u0018s\t\u0003aHC\u0001<~\u0011\u0015\u00114\u00101\u0001\u007f!\tyxGD\u0002\u0002\u0002UrA!a\u0001\u0002\b9\u0019A)!\u0002\n\u0003\u0015I!a\u0001\u0003\b\u000f\u0005-1\u0003c\u0001\u0002\u000e\u0005Ya\t\\8biJ+\u0017\rZ3s!\r\t\u0013q\u0002\u0004\b\u0003#\u0019\u0002\u0012AA\n\u0005-1En\\1u%\u0016\fG-\u001a:\u0014\u000b\u0005=A\"!\u0006\u0011\t!1\u0013q\u0003\t\u0004\u001b\u0005e\u0011bAA\u000e\u001d\t)a\t\\8bi\"9A&a\u0004\u0005\u0002\u0005}ACAA\u0007\u0011\u001dy\u0013q\u0002C\u0001\u0003G!B!a\u0006\u0002&!1!'!\tA\u0002y<q!!\u000b\u0014\u0011\u0007\tY#\u0001\u0007E_V\u0014G.\u001a*fC\u0012,'\u000fE\u0002\"\u0003[1q!a\f\u0014\u0011\u0003\t\tD\u0001\u0007E_V\u0014G.\u001a*fC\u0012,'oE\u0003\u0002.1\t\u0019\u0004\u0005\u0003\tM\u0005U\u0002cA\u0007\u00028%\u0019\u0011\u0011\b\b\u0003\r\u0011{WO\u00197f\u0011\u001da\u0013Q\u0006C\u0001\u0003{!\"!a\u000b\t\u000f=\ni\u0003\"\u0001\u0002BQ!\u0011QGA\"\u0011\u0019\u0011\u0014q\ba\u0001}\u001e9\u0011qI\n\t\u0004\u0005%\u0013\u0001\u0005\"jO\u0012+7-[7bYJ+\u0017\rZ3s!\r\t\u00131\n\u0004\b\u0003\u001b\u001a\u0002\u0012AA(\u0005A\u0011\u0015n\u001a#fG&l\u0017\r\u001c*fC\u0012,'oE\u0003\u0002L1\t\t\u0006\u0005\u0003\tM\u0005M\u0003c\u0001\"\u0002V%\u0019\u0011qK&\u0003\u0015\tKw\rR3dS6\fG\u000eC\u0004-\u0003\u0017\"\t!a\u0017\u0015\u0005\u0005%\u0003bB\u0018\u0002L\u0011\u0005\u0011q\f\u000b\u0005\u0003'\n\t\u0007\u0003\u00043\u0003;\u0002\rA`\u0004\b\u0003K\u001a\u00022AA4\u00035\u0011un\u001c7fC:\u0014V-\u00193feB\u0019\u0011%!\u001b\u0007\u000f\u0005-4\u0003#\u0001\u0002n\ti!i\\8mK\u0006t'+Z1eKJ\u001cR!!\u001b\r\u0003_\u0002B\u0001\u0003\u0014\u0002rA\u0019Q\"a\u001d\n\u0007\u0005UdBA\u0004C_>dW-\u00198\t\u000f1\nI\u0007\"\u0001\u0002zQ\u0011\u0011q\r\u0005\b_\u0005%D\u0011AA?)\u0011\t\t(a \t\rI\nY\b1\u0001\u007f\u000f\u001d\t\u0019i\u0005E\u0002\u0003\u000b\u000bAb\u0015;sS:<'+Z1eKJ\u00042!IAD\r\u001d\tIi\u0005E\u0001\u0003\u0017\u0013Ab\u0015;sS:<'+Z1eKJ\u001cR!a\"\r\u0003\u001b\u0003B\u0001\u0003\u0014\u0002\u0010B!\u0011\u0011SAL\u001d\ri\u00111S\u0005\u0004\u0003+s\u0011A\u0002)sK\u0012,g-\u0003\u0003\u0002\u001a\u0006m%AB*ue&twMC\u0002\u0002\u0016:Aq\u0001LAD\t\u0003\ty\n\u0006\u0002\u0002\u0006\"9q&a\"\u0005\u0002\u0005\rF\u0003BAH\u0003KCaAMAQ\u0001\u0004q\bbBAU'\u0011\r\u00111V\u0001\n[\u0006\u0004(+Z1eKJ,B!!,\u0002FR!\u0011qVAl!\u0011Aa%!-\u0011\u0011\u0005M\u0016QXAH\u0003\u0003l!!!.\u000b\t\u0005]\u0016\u0011X\u0001\nS6lW\u000f^1cY\u0016T1!a/\u000f\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0005\u0003\u007f\u000b)LA\u0002NCB\u0004B!a1\u0002F2\u0001A\u0001CAd\u0003O\u0013\r!!3\u0003\u0003Y\u000bB!a3\u0002RB\u0019Q\"!4\n\u0007\u0005=gBA\u0004O_RD\u0017N\\4\u0011\u00075\t\u0019.C\u0002\u0002V:\u00111!\u00118z\u0011!\tI.a*A\u0004\u0005m\u0017a\u0003<bYV,'+Z1eKJ\u0004B\u0001\u0003\u0014\u0002B\"9\u0011q\\\n\u0005\u0004\u0005\u0005\u0018aC1se\u0006L(+Z1eKJ,B!a9\u0002pR1\u0011Q]Az\u0003{\u0004B\u0001\u0003\u0014\u0002hB)Q\"!;\u0002n&\u0019\u00111\u001e\b\u0003\u000b\u0005\u0013(/Y=\u0011\t\u0005\r\u0017q\u001e\u0003\t\u0003c\fiN1\u0001\u0002J\n\tA\u000b\u0003\u0006\u0002v\u0006u\u0017\u0011!a\u0002\u0003o\f!\"\u001a<jI\u0016t7-\u001a\u00132!\u0019\t\t*!?\u0002n&!\u00111`AN\u0005!i\u0015M\\5gKN$\bBCA\u0000\u0003;\f\t\u0011q\u0001\u0003\u0002\u0005QQM^5eK:\u001cW\r\n\u001a\u0011\t!1\u0013Q^\u0004\b\u0005\u000b\u0019\u00022\u0001B\u0004\u00031Qe+\u00197vKJ+\u0017\rZ3s!\r\t#\u0011\u0002\u0004\b\u0005\u0017\u0019\u0002\u0012\u0001B\u0007\u00051Qe+\u00197vKJ+\u0017\rZ3s'\u0015\u0011I\u0001\u0004B\b!\rAae\r\u0005\bY\t%A\u0011\u0001B\n)\t\u00119\u0001C\u00040\u0005\u0013!\tAa\u0006\u0015\u0007y\u0014I\u0002\u0003\u00043\u0005+\u0001\rA`\u0004\b\u0005;\u0019\u00022\u0001B\u0010\u00035QuJ\u00196fGR\u0014V-\u00193feB\u0019\u0011E!\t\u0007\u000f\t\r2\u0003#\u0001\u0003&\ti!j\u00142kK\u000e$(+Z1eKJ\u001cRA!\t\r\u0005O\u0001B\u0001\u0003\u0014\u0003*A\u0019AGa\u000b\n\u0007\t5\u0012HA\u0004K\u001f\nTWm\u0019;\t\u000f1\u0012\t\u0003\"\u0001\u00032Q\u0011!q\u0004\u0005\b_\t\u0005B\u0011\u0001B\u001b)\u0011\u00119D!\u000f\u0011\u0007}\u0014Y\u0003\u0003\u00043\u0005g\u0001\rA`\u0004\b\u0005{\u0019\u00022\u0001B \u00031Q\u0015I\u001d:bsJ+\u0017\rZ3s!\r\t#\u0011\t\u0004\b\u0005\u0007\u001a\u0002\u0012\u0001B#\u00051Q\u0015I\u001d:bsJ+\u0017\rZ3s'\u0015\u0011\t\u0005\u0004B$!\u0011AaE!\u0013\u0011\u0007Q\u0012Y%C\u0002\u0003Ne\u0012aAS!se\u0006L\bb\u0002\u0017\u0003B\u0011\u0005!\u0011\u000b\u000b\u0003\u0005\u007fAqa\fB!\t\u0003\u0011)\u0006\u0006\u0003\u0003X\te\u0003cA@\u0003L!1!Ga\u0015A\u0002yDqA!\u0018\u0014\t\u0007\u0011y&\u0001\u0007PaRLwN\u001c*fC\u0012,'/\u0006\u0003\u0003b\tMD\u0003\u0002B2\u0005k\u0012RA!\u001a\r\u0005S2qAa\u001a\u0003\\\u0001\u0011\u0019G\u0001\u0007=e\u00164\u0017N\\3nK:$h\b\u0005\u0003\tM\t-\u0004#B\u0007\u0003n\tE\u0014b\u0001B8\u001d\t1q\n\u001d;j_:\u0004B!a1\u0003t\u0011A\u0011\u0011\u001fB.\u0005\u0004\tI\r\u0003\u0005\u0002Z\nm\u00039\u0001B<!\u0011AaE!\u001d\t\r1JA\u0011\u0001B>)\u00059\u0001")
public interface DefaultReaders extends DefaultReaders0
{
    IntReader$ IntReader();
    
    BigIntReader$ BigIntReader();
    
    LongReader$ LongReader();
    
    ShortReader$ ShortReader();
    
    ByteReader$ ByteReader();
    
    FloatReader$ FloatReader();
    
    DoubleReader$ DoubleReader();
    
    BigDecimalReader$ BigDecimalReader();
    
    BooleanReader$ BooleanReader();
    
    StringReader$ StringReader();
    
     <V> Reader<Map<String, V>> mapReader(final Reader<V> p0);
    
     <T> Reader<Object> arrayReader(final Manifest<T> p0, final Reader<T> p1);
    
    JValueReader$ JValueReader();
    
    JObjectReader$ JObjectReader();
    
    JArrayReader$ JArrayReader();
    
     <T> Object OptionReader(final Reader<T> p0);
    
    public class IntReader$ implements Reader<Object>
    {
        @Override
        public int read(final JsonAST.JValue value) {
            int n;
            if (value instanceof JsonAST.JInt) {
                final BigInt x = ((JsonAST.JInt)value).num();
                n = x.intValue();
            }
            else if (value instanceof JsonAST.JLong) {
                final long x2 = ((JsonAST.JLong)value).num();
                n = Predef$.MODULE$.long2Long(x2).intValue();
            }
            else if (value instanceof JsonAST.JDouble) {
                final double x3 = ((JsonAST.JDouble)value).num();
                n = Predef$.MODULE$.double2Double(x3).intValue();
            }
            else {
                if (!(value instanceof JsonAST.JDecimal)) {
                    throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Int.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                }
                final BigDecimal x4 = ((JsonAST.JDecimal)value).num();
                n = x4.intValue();
            }
            return n;
        }
        
        public IntReader$(final DefaultReaders $outer) {
        }
    }
    
    public class BigIntReader$ implements Reader<BigInt>
    {
        @Override
        public BigInt read(final JsonAST.JValue value) {
            BigInt bigInt;
            if (value instanceof JsonAST.JInt) {
                final BigInt x = bigInt = ((JsonAST.JInt)value).num();
            }
            else if (value instanceof JsonAST.JLong) {
                final long x2 = ((JsonAST.JLong)value).num();
                bigInt = package$.MODULE$.BigInt().apply(x2);
            }
            else if (value instanceof JsonAST.JDouble) {
                final double x3 = ((JsonAST.JDouble)value).num();
                bigInt = package$.MODULE$.BigInt().apply(Predef$.MODULE$.double2Double(x3).longValue());
            }
            else {
                if (!(value instanceof JsonAST.JDecimal)) {
                    throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to BigInt.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                }
                final BigDecimal x4 = ((JsonAST.JDecimal)value).num();
                bigInt = x4.toBigInt();
            }
            return bigInt;
        }
        
        public BigIntReader$(final DefaultReaders $outer) {
        }
    }
    
    public class LongReader$ implements Reader<Object>
    {
        @Override
        public long read(final JsonAST.JValue value) {
            long n;
            if (value instanceof JsonAST.JInt) {
                final BigInt x = ((JsonAST.JInt)value).num();
                n = x.longValue();
            }
            else if (value instanceof JsonAST.JLong) {
                final long x2 = n = ((JsonAST.JLong)value).num();
            }
            else if (value instanceof JsonAST.JDouble) {
                final double x3 = ((JsonAST.JDouble)value).num();
                n = Predef$.MODULE$.double2Double(x3).longValue();
            }
            else {
                if (!(value instanceof JsonAST.JDecimal)) {
                    throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Long.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                }
                final BigDecimal x4 = ((JsonAST.JDecimal)value).num();
                n = x4.longValue();
            }
            return n;
        }
        
        public LongReader$(final DefaultReaders $outer) {
        }
    }
    
    public class ShortReader$ implements Reader<Object>
    {
        @Override
        public short read(final JsonAST.JValue value) {
            short n;
            if (value instanceof JsonAST.JInt) {
                final BigInt x = ((JsonAST.JInt)value).num();
                n = x.shortValue();
            }
            else if (value instanceof JsonAST.JLong) {
                final long x2 = ((JsonAST.JLong)value).num();
                n = Predef$.MODULE$.long2Long(x2).shortValue();
            }
            else if (value instanceof JsonAST.JDouble) {
                final double x3 = ((JsonAST.JDouble)value).num();
                n = Predef$.MODULE$.double2Double(x3).shortValue();
            }
            else {
                if (!(value instanceof JsonAST.JDecimal)) {
                    final JsonAST.JNull$ jNull = org.json4s.package$.MODULE$.JNull();
                    if (jNull == null) {
                        if (value != null) {
                            throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Short.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                        }
                    }
                    else if (!jNull.equals(value)) {
                        throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Short.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                    }
                    n = 0;
                    return n;
                }
                final BigDecimal x4 = ((JsonAST.JDecimal)value).num();
                n = x4.shortValue();
            }
            return n;
        }
        
        public ShortReader$(final DefaultReaders $outer) {
        }
    }
    
    public class ByteReader$ implements Reader<Object>
    {
        @Override
        public byte read(final JsonAST.JValue value) {
            byte b;
            if (value instanceof JsonAST.JInt) {
                final BigInt x = ((JsonAST.JInt)value).num();
                b = x.byteValue();
            }
            else if (value instanceof JsonAST.JLong) {
                final long x2 = ((JsonAST.JLong)value).num();
                b = Predef$.MODULE$.long2Long(x2).byteValue();
            }
            else if (value instanceof JsonAST.JDouble) {
                final double x3 = ((JsonAST.JDouble)value).num();
                b = Predef$.MODULE$.double2Double(x3).byteValue();
            }
            else {
                if (!(value instanceof JsonAST.JDecimal)) {
                    final JsonAST.JNull$ jNull = org.json4s.package$.MODULE$.JNull();
                    if (jNull == null) {
                        if (value != null) {
                            throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Byte.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                        }
                    }
                    else if (!jNull.equals(value)) {
                        throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Byte.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                    }
                    b = 0;
                    return b;
                }
                final BigDecimal x4 = ((JsonAST.JDecimal)value).num();
                b = x4.byteValue();
            }
            return b;
        }
        
        public ByteReader$(final DefaultReaders $outer) {
        }
    }
    
    public class FloatReader$ implements Reader<Object>
    {
        @Override
        public float read(final JsonAST.JValue value) {
            float n;
            if (value instanceof JsonAST.JInt) {
                final BigInt x = ((JsonAST.JInt)value).num();
                n = x.floatValue();
            }
            else if (value instanceof JsonAST.JLong) {
                final long x2 = ((JsonAST.JLong)value).num();
                n = Predef$.MODULE$.long2Long(x2);
            }
            else if (value instanceof JsonAST.JDouble) {
                final double x3 = ((JsonAST.JDouble)value).num();
                n = Predef$.MODULE$.double2Double(x3).floatValue();
            }
            else {
                if (!(value instanceof JsonAST.JDecimal)) {
                    final JsonAST.JNull$ jNull = org.json4s.package$.MODULE$.JNull();
                    if (jNull == null) {
                        if (value != null) {
                            throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Float.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                        }
                    }
                    else if (!jNull.equals(value)) {
                        throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Float.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                    }
                    n = 0.0f;
                    return n;
                }
                final BigDecimal x4 = ((JsonAST.JDecimal)value).num();
                n = x4.floatValue();
            }
            return n;
        }
        
        public FloatReader$(final DefaultReaders $outer) {
        }
    }
    
    public class DoubleReader$ implements Reader<Object>
    {
        @Override
        public double read(final JsonAST.JValue value) {
            double n;
            if (value instanceof JsonAST.JInt) {
                final BigInt x = ((JsonAST.JInt)value).num();
                n = x.doubleValue();
            }
            else if (value instanceof JsonAST.JLong) {
                final long x2 = ((JsonAST.JLong)value).num();
                n = Predef$.MODULE$.long2Long(x2);
            }
            else if (value instanceof JsonAST.JDouble) {
                final double x3 = n = ((JsonAST.JDouble)value).num();
            }
            else {
                if (!(value instanceof JsonAST.JDecimal)) {
                    final JsonAST.JNull$ jNull = org.json4s.package$.MODULE$.JNull();
                    if (jNull == null) {
                        if (value != null) {
                            throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Double.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                        }
                    }
                    else if (!jNull.equals(value)) {
                        throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Double.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                    }
                    n = 0.0;
                    return n;
                }
                final BigDecimal x4 = ((JsonAST.JDecimal)value).num();
                n = x4.doubleValue();
            }
            return n;
        }
        
        public DoubleReader$(final DefaultReaders $outer) {
        }
    }
    
    public class BigDecimalReader$ implements Reader<BigDecimal>
    {
        @Override
        public BigDecimal read(final JsonAST.JValue value) {
            BigDecimal bigDecimal;
            if (value instanceof JsonAST.JInt) {
                final BigInt x = ((JsonAST.JInt)value).num();
                bigDecimal = package$.MODULE$.BigDecimal().apply(x);
            }
            else if (value instanceof JsonAST.JLong) {
                final long x2 = ((JsonAST.JLong)value).num();
                bigDecimal = package$.MODULE$.BigDecimal().apply(x2);
            }
            else if (value instanceof JsonAST.JDouble) {
                final double x3 = ((JsonAST.JDouble)value).num();
                bigDecimal = package$.MODULE$.BigDecimal().apply(x3);
            }
            else {
                if (!(value instanceof JsonAST.JDecimal)) {
                    final JsonAST.JNull$ jNull = org.json4s.package$.MODULE$.JNull();
                    if (jNull == null) {
                        if (value != null) {
                            throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to BigDecimal.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                        }
                    }
                    else if (!jNull.equals(value)) {
                        throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to BigDecimal.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                    }
                    bigDecimal = BigDecimal$.MODULE$.int2bigDecimal(0);
                    return bigDecimal;
                }
                final BigDecimal x4 = bigDecimal = ((JsonAST.JDecimal)value).num();
            }
            return bigDecimal;
        }
        
        public BigDecimalReader$(final DefaultReaders $outer) {
        }
    }
    
    public class BooleanReader$ implements Reader<Object>
    {
        @Override
        public boolean read(final JsonAST.JValue value) {
            if (!(value instanceof JsonAST.JBool)) {
                final JsonAST.JNull$ jNull = org.json4s.package$.MODULE$.JNull();
                if (jNull == null) {
                    if (value != null) {
                        throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Boolean.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                    }
                }
                else if (!jNull.equals(value)) {
                    throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to Boolean.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                }
                return false;
            }
            boolean value2;
            final boolean v = value2 = ((JsonAST.JBool)value).value();
            return value2;
        }
        
        public BooleanReader$(final DefaultReaders $outer) {
        }
    }
    
    public class StringReader$ implements Reader<String>
    {
        @Override
        public String read(final JsonAST.JValue value) {
            String s2;
            if (value instanceof JsonAST.JInt) {
                final BigInt x = ((JsonAST.JInt)value).num();
                s2 = x.toString();
            }
            else if (value instanceof JsonAST.JLong) {
                final long x2 = ((JsonAST.JLong)value).num();
                s2 = BoxesRunTime.boxToLong(x2).toString();
            }
            else if (value instanceof JsonAST.JDecimal) {
                final BigDecimal x3 = ((JsonAST.JDecimal)value).num();
                s2 = x3.toString();
            }
            else if (value instanceof JsonAST.JDouble) {
                final double x4 = ((JsonAST.JDouble)value).num();
                s2 = BoxesRunTime.boxToDouble(x4).toString();
            }
            else if (value instanceof JsonAST.JBool) {
                final boolean x5 = ((JsonAST.JBool)value).value();
                s2 = BoxesRunTime.boxToBoolean(x5).toString();
            }
            else {
                if (!(value instanceof JsonAST.JString)) {
                    final JsonAST.JNull$ jNull = org.json4s.package$.MODULE$.JNull();
                    if (jNull == null) {
                        if (value != null) {
                            throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to String.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                        }
                    }
                    else if (!jNull.equals(value)) {
                        throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("Can't convert %s to String.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
                    }
                    s2 = null;
                    return s2;
                }
                final String s = s2 = ((JsonAST.JString)value).s();
            }
            return s2;
        }
        
        public StringReader$(final DefaultReaders $outer) {
        }
    }
    
    public class JValueReader$ implements Reader<JsonAST.JValue>
    {
        @Override
        public JsonAST.JValue read(final JsonAST.JValue value) {
            return value;
        }
        
        public JValueReader$(final DefaultReaders $outer) {
        }
    }
    
    public class JObjectReader$ implements Reader<JsonAST.JObject>
    {
        @Override
        public JsonAST.JObject read(final JsonAST.JValue value) {
            if (value instanceof JsonAST.JObject) {
                return (JsonAST.JObject)value;
            }
            throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("JObject expected, but got %s.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
        }
        
        public JObjectReader$(final DefaultReaders $outer) {
        }
    }
    
    public class JArrayReader$ implements Reader<JsonAST.JArray>
    {
        @Override
        public JsonAST.JArray read(final JsonAST.JValue value) {
            if (value instanceof JsonAST.JArray) {
                return (JsonAST.JArray)value;
            }
            throw new package.MappingException(new StringOps(Predef$.MODULE$.augmentString("JArray expected, but got %s.")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { value })));
        }
        
        public JArrayReader$(final DefaultReaders $outer) {
        }
    }
}
