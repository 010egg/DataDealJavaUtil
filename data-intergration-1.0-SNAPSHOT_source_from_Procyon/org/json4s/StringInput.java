// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Product;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ma\u0001B\u0001\u0003\u0001\u001e\u00111b\u0015;sS:<\u0017J\u001c9vi*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0005\r%A\u0011\u0011BC\u0007\u0002\u0005%\u00111B\u0001\u0002\n\u0015N|g.\u00138qkR\u0004\"!\u0004\t\u000e\u00039Q\u0011aD\u0001\u0006g\u000e\fG.Y\u0005\u0003#9\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u000e'%\u0011AC\u0004\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\t-\u0001\u0011)\u001a!C\u0001/\u000511\u000f\u001e:j]\u001e,\u0012\u0001\u0007\t\u00033qq!!\u0004\u000e\n\u0005mq\u0011A\u0002)sK\u0012,g-\u0003\u0002\u001e=\t11\u000b\u001e:j]\u001eT!a\u0007\b\t\u0011\u0001\u0002!\u0011#Q\u0001\na\tqa\u001d;sS:<\u0007\u0005C\u0003#\u0001\u0011\u00051%\u0001\u0004=S:LGO\u0010\u000b\u0003I\u0015\u0002\"!\u0003\u0001\t\u000bY\t\u0003\u0019\u0001\r\t\u000f\u001d\u0002\u0011\u0011!C\u0001Q\u0005!1m\u001c9z)\t!\u0013\u0006C\u0004\u0017MA\u0005\t\u0019\u0001\r\t\u000f-\u0002\u0011\u0013!C\u0001Y\u0005q1m\u001c9zI\u0011,g-Y;mi\u0012\nT#A\u0017+\u0005aq3&A\u0018\u0011\u0005A*T\"A\u0019\u000b\u0005I\u001a\u0014!C;oG\",7m[3e\u0015\t!d\"\u0001\u0006b]:|G/\u0019;j_:L!AN\u0019\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rC\u00049\u0001\u0005\u0005I\u0011I\u001d\u0002\u001bA\u0014x\u000eZ;diB\u0013XMZ5y+\u0005Q\u0004CA\u001eA\u001b\u0005a$BA\u001f?\u0003\u0011a\u0017M\\4\u000b\u0003}\nAA[1wC&\u0011Q\u0004\u0010\u0005\b\u0005\u0002\t\t\u0011\"\u0001D\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\u0005!\u0005CA\u0007F\u0013\t1eBA\u0002J]RDq\u0001\u0013\u0001\u0002\u0002\u0013\u0005\u0011*\u0001\bqe>$Wo\u0019;FY\u0016lWM\u001c;\u0015\u0005)k\u0005CA\u0007L\u0013\taeBA\u0002B]fDqAT$\u0002\u0002\u0003\u0007A)A\u0002yIEBq\u0001\u0015\u0001\u0002\u0002\u0013\u0005\u0013+A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\u0005\u0011\u0006cA*W\u00156\tAK\u0003\u0002V\u001d\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\u0005]#&\u0001C%uKJ\fGo\u001c:\t\u000fe\u0003\u0011\u0011!C\u00015\u0006A1-\u00198FcV\fG\u000e\u0006\u0002\\=B\u0011Q\u0002X\u0005\u0003;:\u0011qAQ8pY\u0016\fg\u000eC\u0004O1\u0006\u0005\t\u0019\u0001&\t\u000f\u0001\u0004\u0011\u0011!C!C\u0006A\u0001.Y:i\u0007>$W\rF\u0001E\u0011\u001d\u0019\u0007!!A\u0005B\u0011\f\u0001\u0002^8TiJLgn\u001a\u000b\u0002u!9a\rAA\u0001\n\u0003:\u0017AB3rk\u0006d7\u000f\u0006\u0002\\Q\"9a*ZA\u0001\u0002\u0004Qua\u00026\u0003\u0003\u0003E\ta[\u0001\f'R\u0014\u0018N\\4J]B,H\u000f\u0005\u0002\nY\u001a9\u0011AAA\u0001\u0012\u0003i7c\u00017o%A!qN\u001d\r%\u001b\u0005\u0001(BA9\u000f\u0003\u001d\u0011XO\u001c;j[\u0016L!a\u001d9\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>t\u0017\u0007C\u0003#Y\u0012\u0005Q\u000fF\u0001l\u0011\u001d\u0019G.!A\u0005F\u0011Dq\u0001\u001f7\u0002\u0002\u0013\u0005\u00150A\u0003baBd\u0017\u0010\u0006\u0002%u\")ac\u001ea\u00011!9A\u0010\\A\u0001\n\u0003k\u0018aB;oCB\u0004H.\u001f\u000b\u0004}\u0006\r\u0001cA\u0007\u00001%\u0019\u0011\u0011\u0001\b\u0003\r=\u0003H/[8o\u0011!\t)a_A\u0001\u0002\u0004!\u0013a\u0001=%a!I\u0011\u0011\u00027\u0002\u0002\u0013%\u00111B\u0001\fe\u0016\fGMU3t_24X\r\u0006\u0002\u0002\u000eA\u00191(a\u0004\n\u0007\u0005EAH\u0001\u0004PE*,7\r\u001e")
public class StringInput extends JsonInput
{
    private final String string;
    
    public static Option<String> unapply(final StringInput x$0) {
        return StringInput$.MODULE$.unapply(x$0);
    }
    
    public static StringInput apply(final String string) {
        return StringInput$.MODULE$.apply(string);
    }
    
    public static <A> Function1<String, A> andThen(final Function1<StringInput, A> function1) {
        return (Function1<String, A>)StringInput$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, StringInput> compose(final Function1<A, String> function1) {
        return (Function1<A, StringInput>)StringInput$.MODULE$.compose((Function1)function1);
    }
    
    public String string() {
        return this.string;
    }
    
    public StringInput copy(final String string) {
        return new StringInput(string);
    }
    
    public String copy$default$1() {
        return this.string();
    }
    
    @Override
    public String productPrefix() {
        return "StringInput";
    }
    
    public int productArity() {
        return 1;
    }
    
    public Object productElement(final int x$1) {
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 0: {
                return this.string();
            }
        }
    }
    
    @Override
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof StringInput;
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
            if (x$1 instanceof StringInput) {
                final StringInput stringInput = (StringInput)x$1;
                final String string = this.string();
                final String string2 = stringInput.string();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (string == null) {
                            if (string2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!string.equals(string2)) {
                            break Label_0076;
                        }
                        if (stringInput.canEqual(this)) {
                            b = true;
                            break Label_0077;
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
    
    public StringInput(final String string) {
        this.string = string;
    }
}
