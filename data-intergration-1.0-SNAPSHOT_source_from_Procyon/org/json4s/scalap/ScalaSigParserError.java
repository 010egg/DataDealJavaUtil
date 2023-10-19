// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001db\u0001B\u0001\u0003\u0001&\u00111cU2bY\u0006\u001c\u0016n\u001a)beN,'/\u0012:s_JT!a\u0001\u0003\u0002\rM\u001c\u0017\r\\1q\u0015\t)a!\u0001\u0004kg>tGg\u001d\u0006\u0002\u000f\u0005\u0019qN]4\u0004\u0001M!\u0001A\u0003\r\u001d!\tYQC\u0004\u0002\r%9\u0011Q\u0002E\u0007\u0002\u001d)\u0011q\u0002C\u0001\u0007yI|w\u000e\u001e \n\u0003E\tQa]2bY\u0006L!a\u0005\u000b\u0002\u000fA\f7m[1hK*\t\u0011#\u0003\u0002\u0017/\t\u0001\"+\u001e8uS6,W\t_2faRLwN\u001c\u0006\u0003'Q\u0001\"!\u0007\u000e\u000e\u0003QI!a\u0007\u000b\u0003\u000fA\u0013x\u000eZ;diB\u0011\u0011$H\u0005\u0003=Q\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001\u0002\t\u0001\u0003\u0016\u0004%\t!I\u0001\u0004[N<W#\u0001\u0012\u0011\u0005\r2cBA\r%\u0013\t)C#\u0001\u0004Qe\u0016$WMZ\u0005\u0003O!\u0012aa\u0015;sS:<'BA\u0013\u0015\u0011!Q\u0003A!E!\u0002\u0013\u0011\u0013\u0001B7tO\u0002BQ\u0001\f\u0001\u0005\u00025\na\u0001P5oSRtDC\u0001\u00181!\ty\u0003!D\u0001\u0003\u0011\u0015\u00013\u00061\u0001#\u0011\u001d\u0011\u0004!!A\u0005\u0002M\nAaY8qsR\u0011a\u0006\u000e\u0005\bAE\u0002\n\u00111\u0001#\u0011\u001d1\u0004!%A\u0005\u0002]\nabY8qs\u0012\"WMZ1vYR$\u0013'F\u00019U\t\u0011\u0013hK\u0001;!\tY\u0004)D\u0001=\u0015\tid(A\u0005v]\u000eDWmY6fI*\u0011q\bF\u0001\u000bC:tw\u000e^1uS>t\u0017BA!=\u0005E)hn\u00195fG.,GMV1sS\u0006t7-\u001a\u0005\b\u0007\u0002\t\t\u0011\"\u0011E\u00035\u0001(o\u001c3vGR\u0004&/\u001a4jqV\tQ\t\u0005\u0002G\u00176\tqI\u0003\u0002I\u0013\u0006!A.\u00198h\u0015\u0005Q\u0015\u0001\u00026bm\u0006L!aJ$\t\u000f5\u0003\u0011\u0011!C\u0001\u001d\u0006a\u0001O]8ek\u000e$\u0018I]5usV\tq\n\u0005\u0002\u001a!&\u0011\u0011\u000b\u0006\u0002\u0004\u0013:$\bbB*\u0001\u0003\u0003%\t\u0001V\u0001\u000faJ|G-^2u\u000b2,W.\u001a8u)\t)\u0006\f\u0005\u0002\u001a-&\u0011q\u000b\u0006\u0002\u0004\u0003:L\bbB-S\u0003\u0003\u0005\raT\u0001\u0004q\u0012\n\u0004bB.\u0001\u0003\u0003%\t\u0005X\u0001\u0010aJ|G-^2u\u0013R,'/\u0019;peV\tQ\fE\u0002_CVk\u0011a\u0018\u0006\u0003AR\t!bY8mY\u0016\u001cG/[8o\u0013\t\u0011wL\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0011\u001d!\u0007!!A\u0005\u0002\u0015\f\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0003M&\u0004\"!G4\n\u0005!$\"a\u0002\"p_2,\u0017M\u001c\u0005\b3\u000e\f\t\u00111\u0001V\u0011\u001dY\u0007!!A\u0005B1\f\u0001\u0002[1tQ\u000e{G-\u001a\u000b\u0002\u001f\"9a\u000eAA\u0001\n\u0003z\u0017AB3rk\u0006d7\u000f\u0006\u0002ga\"9\u0011,\\A\u0001\u0002\u0004)va\u0002:\u0003\u0003\u0003E\ta]\u0001\u0014'\u000e\fG.Y*jOB\u000b'o]3s\u000bJ\u0014xN\u001d\t\u0003_Q4q!\u0001\u0002\u0002\u0002#\u0005QoE\u0002umr\u0001Ba\u001e>#]5\t\u0001P\u0003\u0002z)\u00059!/\u001e8uS6,\u0017BA>y\u0005E\t%m\u001d;sC\u000e$h)\u001e8di&|g.\r\u0005\u0006YQ$\t! \u000b\u0002g\"Aq\u0010^A\u0001\n\u000b\n\t!\u0001\u0005u_N#(/\u001b8h)\u0005)\u0005\"CA\u0003i\u0006\u0005I\u0011QA\u0004\u0003\u0015\t\u0007\u000f\u001d7z)\rq\u0013\u0011\u0002\u0005\u0007A\u0005\r\u0001\u0019\u0001\u0012\t\u0013\u00055A/!A\u0005\u0002\u0006=\u0011aB;oCB\u0004H.\u001f\u000b\u0005\u0003#\t9\u0002\u0005\u0003\u001a\u0003'\u0011\u0013bAA\u000b)\t1q\n\u001d;j_:D\u0011\"!\u0007\u0002\f\u0005\u0005\t\u0019\u0001\u0018\u0002\u0007a$\u0003\u0007C\u0005\u0002\u001eQ\f\t\u0011\"\u0003\u0002 \u0005Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\t\t\u0003E\u0002G\u0003GI1!!\nH\u0005\u0019y%M[3di\u0002")
public class ScalaSigParserError extends RuntimeException implements Product, scala.Serializable
{
    private final String msg;
    
    public static Option<String> unapply(final ScalaSigParserError x$0) {
        return ScalaSigParserError$.MODULE$.unapply(x$0);
    }
    
    public static ScalaSigParserError apply(final String msg) {
        return ScalaSigParserError$.MODULE$.apply(msg);
    }
    
    public static <A> Function1<String, A> andThen(final Function1<ScalaSigParserError, A> function1) {
        return (Function1<String, A>)ScalaSigParserError$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, ScalaSigParserError> compose(final Function1<A, String> function1) {
        return (Function1<A, ScalaSigParserError>)ScalaSigParserError$.MODULE$.compose((Function1)function1);
    }
    
    public String msg() {
        return this.msg;
    }
    
    public ScalaSigParserError copy(final String msg) {
        return new ScalaSigParserError(msg);
    }
    
    public String copy$default$1() {
        return this.msg();
    }
    
    public String productPrefix() {
        return "ScalaSigParserError";
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
                return this.msg();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ScalaSigParserError;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ScalaSigParserError) {
                final ScalaSigParserError scalaSigParserError = (ScalaSigParserError)x$1;
                final String msg = this.msg();
                final String msg2 = scalaSigParserError.msg();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (msg == null) {
                            if (msg2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!msg.equals(msg2)) {
                            break Label_0076;
                        }
                        if (scalaSigParserError.canEqual(this)) {
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
    
    public ScalaSigParserError(final String msg) {
        super(this.msg = msg);
        Product$class.$init$((Product)this);
    }
}
