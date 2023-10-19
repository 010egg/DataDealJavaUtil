// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple2;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u0015c\u0001B\u0001\u0003\u0001.\u0011qb\u0015;sS:<')\u001f;fgB\u000b\u0017N\u001d\u0006\u0003\u0007\u0011\t\u0001b]2bY\u0006\u001c\u0018n\u001a\u0006\u0003\u000b\u0019\taa]2bY\u0006\u0004(BA\u0004\t\u0003\u0019Q7o\u001c85g*\t\u0011\"A\u0002pe\u001e\u001c\u0001a\u0005\u0003\u0001\u0019I)\u0002CA\u0007\u0011\u001b\u0005q!\"A\b\u0002\u000bM\u001c\u0017\r\\1\n\u0005Eq!AB!osJ+g\r\u0005\u0002\u000e'%\u0011AC\u0004\u0002\b!J|G-^2u!\tia#\u0003\u0002\u0018\u001d\ta1+\u001a:jC2L'0\u00192mK\"A\u0011\u0004\u0001BK\u0002\u0013\u0005!$\u0001\u0004tiJLgnZ\u000b\u00027A\u0011Ad\b\b\u0003\u001buI!A\b\b\u0002\rA\u0013X\rZ3g\u0013\t\u0001\u0013E\u0001\u0004TiJLgn\u001a\u0006\u0003=9A\u0001b\t\u0001\u0003\u0012\u0003\u0006IaG\u0001\bgR\u0014\u0018N\\4!\u0011!)\u0003A!f\u0001\n\u00031\u0013!\u00022zi\u0016\u001cX#A\u0014\u0011\u00075A#&\u0003\u0002*\u001d\t)\u0011I\u001d:bsB\u0011QbK\u0005\u0003Y9\u0011AAQ=uK\"Aa\u0006\u0001B\tB\u0003%q%\u0001\u0004csR,7\u000f\t\u0005\u0006a\u0001!\t!M\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0007I\"T\u0007\u0005\u00024\u00015\t!\u0001C\u0003\u001a_\u0001\u00071\u0004C\u0003&_\u0001\u0007q\u0005C\u00048\u0001\u0005\u0005I\u0011\u0001\u001d\u0002\t\r|\u0007/\u001f\u000b\u0004eeR\u0004bB\r7!\u0003\u0005\ra\u0007\u0005\bKY\u0002\n\u00111\u0001(\u0011\u001da\u0004!%A\u0005\u0002u\nabY8qs\u0012\"WMZ1vYR$\u0013'F\u0001?U\tYrhK\u0001A!\t\te)D\u0001C\u0015\t\u0019E)A\u0005v]\u000eDWmY6fI*\u0011QID\u0001\u000bC:tw\u000e^1uS>t\u0017BA$C\u0005E)hn\u00195fG.,GMV1sS\u0006t7-\u001a\u0005\b\u0013\u0002\t\n\u0011\"\u0001K\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uII*\u0012a\u0013\u0016\u0003O}Bq!\u0014\u0001\u0002\u0002\u0013\u0005c*A\u0007qe>$Wo\u0019;Qe\u00164\u0017\u000e_\u000b\u0002\u001fB\u0011\u0001+V\u0007\u0002#*\u0011!kU\u0001\u0005Y\u0006twMC\u0001U\u0003\u0011Q\u0017M^1\n\u0005\u0001\n\u0006bB,\u0001\u0003\u0003%\t\u0001W\u0001\raJ|G-^2u\u0003JLG/_\u000b\u00023B\u0011QBW\u0005\u00037:\u00111!\u00138u\u0011\u001di\u0006!!A\u0005\u0002y\u000ba\u0002\u001d:pIV\u001cG/\u00127f[\u0016tG\u000f\u0006\u0002`EB\u0011Q\u0002Y\u0005\u0003C:\u00111!\u00118z\u0011\u001d\u0019G,!AA\u0002e\u000b1\u0001\u001f\u00132\u0011\u001d)\u0007!!A\u0005B\u0019\fq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0002OB\u0019\u0001n[0\u000e\u0003%T!A\u001b\b\u0002\u0015\r|G\u000e\\3di&|g.\u0003\u0002mS\nA\u0011\n^3sCR|'\u000fC\u0004o\u0001\u0005\u0005I\u0011A8\u0002\u0011\r\fg.R9vC2$\"\u0001]:\u0011\u00055\t\u0018B\u0001:\u000f\u0005\u001d\u0011un\u001c7fC:DqaY7\u0002\u0002\u0003\u0007q\fC\u0004v\u0001\u0005\u0005I\u0011\t<\u0002\u0011!\f7\u000f[\"pI\u0016$\u0012!\u0017\u0005\bq\u0002\t\t\u0011\"\u0011z\u0003!!xn\u0015;sS:<G#A(\t\u000fm\u0004\u0011\u0011!C!y\u00061Q-];bYN$\"\u0001]?\t\u000f\rT\u0018\u0011!a\u0001?\u001eAqPAA\u0001\u0012\u0003\t\t!A\bTiJLgn\u001a\"zi\u0016\u001c\b+Y5s!\r\u0019\u00141\u0001\u0004\t\u0003\t\t\t\u0011#\u0001\u0002\u0006M)\u00111AA\u0004+A9\u0011\u0011BA\b7\u001d\u0012TBAA\u0006\u0015\r\tiAD\u0001\beVtG/[7f\u0013\u0011\t\t\"a\u0003\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>t'\u0007C\u00041\u0003\u0007!\t!!\u0006\u0015\u0005\u0005\u0005\u0001\u0002\u0003=\u0002\u0004\u0005\u0005IQI=\t\u0015\u0005m\u00111AA\u0001\n\u0003\u000bi\"A\u0003baBd\u0017\u0010F\u00033\u0003?\t\t\u0003\u0003\u0004\u001a\u00033\u0001\ra\u0007\u0005\u0007K\u0005e\u0001\u0019A\u0014\t\u0015\u0005\u0015\u00121AA\u0001\n\u0003\u000b9#A\u0004v]\u0006\u0004\b\u000f\\=\u0015\t\u0005%\u0012Q\u0007\t\u0006\u001b\u0005-\u0012qF\u0005\u0004\u0003[q!AB(qi&|g\u000eE\u0003\u000e\u0003cYr%C\u0002\u000249\u0011a\u0001V;qY\u0016\u0014\u0004\"CA\u001c\u0003G\t\t\u00111\u00013\u0003\rAH\u0005\r\u0005\u000b\u0003w\t\u0019!!A\u0005\n\u0005u\u0012a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"!a\u0010\u0011\u0007A\u000b\t%C\u0002\u0002DE\u0013aa\u00142kK\u000e$\b")
public class StringBytesPair implements Product, Serializable
{
    private final String string;
    private final byte[] bytes;
    
    public static Option<Tuple2<String, byte[]>> unapply(final StringBytesPair x$0) {
        return StringBytesPair$.MODULE$.unapply(x$0);
    }
    
    public static StringBytesPair apply(final String string, final byte[] bytes) {
        return StringBytesPair$.MODULE$.apply(string, bytes);
    }
    
    public static Function1<Tuple2<String, byte[]>, StringBytesPair> tupled() {
        return (Function1<Tuple2<String, byte[]>, StringBytesPair>)StringBytesPair$.MODULE$.tupled();
    }
    
    public static Function1<String, Function1<byte[], StringBytesPair>> curried() {
        return (Function1<String, Function1<byte[], StringBytesPair>>)StringBytesPair$.MODULE$.curried();
    }
    
    public String string() {
        return this.string;
    }
    
    public byte[] bytes() {
        return this.bytes;
    }
    
    public StringBytesPair copy(final String string, final byte[] bytes) {
        return new StringBytesPair(string, bytes);
    }
    
    public String copy$default$1() {
        return this.string();
    }
    
    public byte[] copy$default$2() {
        return this.bytes();
    }
    
    public String productPrefix() {
        return "StringBytesPair";
    }
    
    public int productArity() {
        return 2;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 1: {
                o = this.bytes();
                break;
            }
            case 0: {
                o = this.string();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof StringBytesPair;
    }
    
    @Override
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof StringBytesPair) {
                final StringBytesPair stringBytesPair = (StringBytesPair)x$1;
                final String string = this.string();
                final String string2 = stringBytesPair.string();
                boolean b = false;
                Label_0089: {
                    Label_0088: {
                        if (string == null) {
                            if (string2 != null) {
                                break Label_0088;
                            }
                        }
                        else if (!string.equals(string2)) {
                            break Label_0088;
                        }
                        if (this.bytes() == stringBytesPair.bytes() && stringBytesPair.canEqual(this)) {
                            b = true;
                            break Label_0089;
                        }
                    }
                    b = false;
                }
                if (b) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public StringBytesPair(final String string, final byte[] bytes) {
        this.string = string;
        this.bytes = bytes;
        Product$class.$init$((Product)this);
    }
}
