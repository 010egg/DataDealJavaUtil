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
import java.io.Reader;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ma\u0001B\u0001\u0003\u0001\u001e\u00111BU3bI\u0016\u0014\u0018J\u001c9vi*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0005\r%A\u0011\u0011BC\u0007\u0002\u0005%\u00111B\u0001\u0002\n\u0015N|g.\u00138qkR\u0004\"!\u0004\t\u000e\u00039Q\u0011aD\u0001\u0006g\u000e\fG.Y\u0005\u0003#9\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u000e'%\u0011AC\u0004\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\t-\u0001\u0011)\u001a!C\u0001/\u00051!/Z1eKJ,\u0012\u0001\u0007\t\u00033yi\u0011A\u0007\u0006\u00037q\t!![8\u000b\u0003u\tAA[1wC&\u0011qD\u0007\u0002\u0007%\u0016\fG-\u001a:\t\u0011\u0005\u0002!\u0011#Q\u0001\na\tqA]3bI\u0016\u0014\b\u0005C\u0003$\u0001\u0011\u0005A%\u0001\u0004=S:LGO\u0010\u000b\u0003K\u0019\u0002\"!\u0003\u0001\t\u000bY\u0011\u0003\u0019\u0001\r\t\u000f!\u0002\u0011\u0011!C\u0001S\u0005!1m\u001c9z)\t)#\u0006C\u0004\u0017OA\u0005\t\u0019\u0001\r\t\u000f1\u0002\u0011\u0013!C\u0001[\u0005q1m\u001c9zI\u0011,g-Y;mi\u0012\nT#\u0001\u0018+\u0005ay3&\u0001\u0019\u0011\u0005E2T\"\u0001\u001a\u000b\u0005M\"\u0014!C;oG\",7m[3e\u0015\t)d\"\u0001\u0006b]:|G/\u0019;j_:L!a\u000e\u001a\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rC\u0004:\u0001\u0005\u0005I\u0011\t\u001e\u0002\u001bA\u0014x\u000eZ;diB\u0013XMZ5y+\u0005Y\u0004C\u0001\u001f@\u001b\u0005i$B\u0001 \u001d\u0003\u0011a\u0017M\\4\n\u0005\u0001k$AB*ue&tw\rC\u0004C\u0001\u0005\u0005I\u0011A\"\u0002\u0019A\u0014x\u000eZ;di\u0006\u0013\u0018\u000e^=\u0016\u0003\u0011\u0003\"!D#\n\u0005\u0019s!aA%oi\"9\u0001\nAA\u0001\n\u0003I\u0015A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0003\u00156\u0003\"!D&\n\u00051s!aA!os\"9ajRA\u0001\u0002\u0004!\u0015a\u0001=%c!9\u0001\u000bAA\u0001\n\u0003\n\u0016a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0003I\u00032a\u0015,K\u001b\u0005!&BA+\u000f\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0003/R\u0013\u0001\"\u0013;fe\u0006$xN\u001d\u0005\b3\u0002\t\t\u0011\"\u0001[\u0003!\u0019\u0017M\\#rk\u0006dGCA._!\tiA,\u0003\u0002^\u001d\t9!i\\8mK\u0006t\u0007b\u0002(Y\u0003\u0003\u0005\rA\u0013\u0005\bA\u0002\t\t\u0011\"\u0011b\u0003!A\u0017m\u001d5D_\u0012,G#\u0001#\t\u000f\r\u0004\u0011\u0011!C!I\u0006AAo\\*ue&tw\rF\u0001<\u0011\u001d1\u0007!!A\u0005B\u001d\fa!Z9vC2\u001cHCA.i\u0011\u001dqU-!AA\u0002);qA\u001b\u0002\u0002\u0002#\u00051.A\u0006SK\u0006$WM]%oaV$\bCA\u0005m\r\u001d\t!!!A\t\u00025\u001c2\u0001\u001c8\u0013!\u0011y'\u000fG\u0013\u000e\u0003AT!!\u001d\b\u0002\u000fI,h\u000e^5nK&\u00111\u000f\u001d\u0002\u0012\u0003\n\u001cHO]1di\u001a+hn\u0019;j_:\f\u0004\"B\u0012m\t\u0003)H#A6\t\u000f\rd\u0017\u0011!C#I\"9\u0001\u0010\\A\u0001\n\u0003K\u0018!B1qa2LHCA\u0013{\u0011\u00151r\u000f1\u0001\u0019\u0011\u001daH.!A\u0005\u0002v\fq!\u001e8baBd\u0017\u0010F\u0002\u007f\u0003\u0007\u00012!D@\u0019\u0013\r\t\tA\u0004\u0002\u0007\u001fB$\u0018n\u001c8\t\u0011\u0005\u001510!AA\u0002\u0015\n1\u0001\u001f\u00131\u0011%\tI\u0001\\A\u0001\n\u0013\tY!A\u0006sK\u0006$'+Z:pYZ,GCAA\u0007!\ra\u0014qB\u0005\u0004\u0003#i$AB(cU\u0016\u001cG\u000f")
public class ReaderInput extends JsonInput
{
    private final Reader reader;
    
    public static Option<Reader> unapply(final ReaderInput x$0) {
        return ReaderInput$.MODULE$.unapply(x$0);
    }
    
    public static ReaderInput apply(final Reader reader) {
        return ReaderInput$.MODULE$.apply(reader);
    }
    
    public static <A> Function1<Reader, A> andThen(final Function1<ReaderInput, A> function1) {
        return (Function1<Reader, A>)ReaderInput$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, ReaderInput> compose(final Function1<A, Reader> function1) {
        return (Function1<A, ReaderInput>)ReaderInput$.MODULE$.compose((Function1)function1);
    }
    
    public Reader reader() {
        return this.reader;
    }
    
    public ReaderInput copy(final Reader reader) {
        return new ReaderInput(reader);
    }
    
    public Reader copy$default$1() {
        return this.reader();
    }
    
    @Override
    public String productPrefix() {
        return "ReaderInput";
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
                return this.reader();
            }
        }
    }
    
    @Override
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ReaderInput;
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
            if (x$1 instanceof ReaderInput) {
                final ReaderInput readerInput = (ReaderInput)x$1;
                final Reader reader = this.reader();
                final Reader reader2 = readerInput.reader();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (reader == null) {
                            if (reader2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!reader.equals(reader2)) {
                            break Label_0076;
                        }
                        if (readerInput.canEqual(this)) {
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
    
    public ReaderInput(final Reader reader) {
        this.reader = reader;
    }
}
