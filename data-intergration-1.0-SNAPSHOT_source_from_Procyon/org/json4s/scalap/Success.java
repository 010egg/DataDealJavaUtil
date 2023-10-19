// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function0;
import scala.MatchError;
import scala.Function2;
import scala.Function1;
import scala.Some;
import scala.Tuple2;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;
import scala.runtime.Nothing$;

@ScalaSignature(bytes = "\u0006\u0001\u0005mh\u0001B\u0001\u0003\u0001&\u0011qaU;dG\u0016\u001c8O\u0003\u0002\u0004\t\u000511oY1mCBT!!\u0002\u0004\u0002\r)\u001cxN\u001c\u001bt\u0015\u00059\u0011aA8sO\u000e\u0001Qc\u0001\u0006\u0012=M!\u0001a\u0003\u0011$!\u0015aQbD\u000f\u0015\u001b\u0005\u0011\u0011B\u0001\b\u0003\u0005\u0019\u0011Vm];miB\u0011\u0001#\u0005\u0007\u0001\t\u0019\u0011\u0002\u0001\"b\u0001'\t\u0019q*\u001e;\u0012\u0005QQ\u0002CA\u000b\u0019\u001b\u00051\"\"A\f\u0002\u000bM\u001c\u0017\r\\1\n\u0005e1\"a\u0002(pi\"Lgn\u001a\t\u0003+mI!\u0001\b\f\u0003\u0007\u0005s\u0017\u0010\u0005\u0002\u0011=\u00111q\u0004\u0001CC\u0002M\u0011\u0011!\u0011\t\u0003+\u0005J!A\t\f\u0003\u000fA\u0013x\u000eZ;diB\u0011Q\u0003J\u0005\u0003KY\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001b\n\u0001\u0003\u0016\u0004%\t\u0001K\u0001\u0004_V$X#A\b\t\u0011)\u0002!\u0011#Q\u0001\n=\tAa\\;uA!AA\u0006\u0001BK\u0002\u0013\u0005Q&A\u0003wC2,X-F\u0001\u001e\u0011!y\u0003A!E!\u0002\u0013i\u0012A\u0002<bYV,\u0007\u0005C\u00032\u0001\u0011\u0005!'\u0001\u0004=S:LGO\u0010\u000b\u0004gQ*\u0004\u0003\u0002\u0007\u0001\u001fuAQa\n\u0019A\u0002=AQ\u0001\f\u0019A\u0002uAQa\u000e\u0001\u0005\u0002a\nQ!\u001a:s_J,\u0012\u0001\u0006\u0005\u0006u\u0001!\taO\u0001\ti>|\u0005\u000f^5p]V\tA\bE\u0002\u0016{uI!A\u0010\f\u0003\tM{W.\u001a\u0005\u0006\u0001\u0002!\t!Q\u0001\u0004[\u0006\u0004XC\u0001\"F)\t\u0019u\tE\u0003\r\u001b=!E\u0003\u0005\u0002\u0011\u000b\u0012)ai\u0010b\u0001'\t\t!\tC\u0003I\u007f\u0001\u0007\u0011*A\u0001g!\u0011)\"*\b#\n\u0005-3\"!\u0003$v]\u000e$\u0018n\u001c82\u0011\u0015i\u0005\u0001\"\u0001O\u0003\u0019i\u0017\r](viV\u0011qJ\u0015\u000b\u0003!R\u0003R\u0001D\u0007R;Q\u0001\"\u0001\u0005*\u0005\u000bMc%\u0019A\n\u0003\t=+HO\r\u0005\u0006\u00112\u0003\r!\u0016\t\u0005+){\u0011\u000bC\u0003A\u0001\u0011\u0005q+F\u0002Y7v#\"!\u00170\u0011\t1\u0001!\f\u0018\t\u0003!m#Qa\u0015,C\u0002M\u0001\"\u0001E/\u0005\u000b\u00193&\u0019A\n\t\u000b!3\u0006\u0019A0\u0011\u000bU\u0001w\"\b2\n\u0005\u00054\"!\u0003$v]\u000e$\u0018n\u001c83!\u0011)2M\u0017/\n\u0005\u00114\"A\u0002+va2,'\u0007C\u0003g\u0001\u0011\u0005q-A\u0004gY\u0006$X*\u00199\u0016\u0007!\\W\u000e\u0006\u0002j]B)A\"\u00046m)A\u0011\u0001c\u001b\u0003\u0006'\u0016\u0014\ra\u0005\t\u0003!5$QAR3C\u0002MAQ\u0001S3A\u0002=\u0004R!\u00061\u0010;%DQ!\u001d\u0001\u0005\u0002I\faa\u001c:FYN,WcA:wsR\u0011Ao\u001f\t\u0006\u00195)\b\u0010\u0006\t\u0003!Y$Qa\u00159C\u0002]\f\"a\u0004\u000e\u0011\u0005AIH!\u0002$q\u0005\u0004Q\u0018CA\u000f\u001b\u0011\u0019a\b\u000f\"a\u0001{\u0006)q\u000e\u001e5feB\u0019QC ;\n\u0005}4\"\u0001\u0003\u001fcs:\fW.\u001a \t\u0013\u0005\r\u0001!!A\u0005\u0002\u0005\u0015\u0011\u0001B2paf,b!a\u0002\u0002\u000e\u0005EACBA\u0005\u0003'\t)\u0002\u0005\u0004\r\u0001\u0005-\u0011q\u0002\t\u0004!\u00055AA\u0002\n\u0002\u0002\t\u00071\u0003E\u0002\u0011\u0003#!aaHA\u0001\u0005\u0004\u0019\u0002\"C\u0014\u0002\u0002A\u0005\t\u0019AA\u0006\u0011%a\u0013\u0011\u0001I\u0001\u0002\u0004\ty\u0001C\u0005\u0002\u001a\u0001\t\n\u0011\"\u0001\u0002\u001c\u0005q1m\u001c9zI\u0011,g-Y;mi\u0012\nTCBA\u000f\u0003g\t)$\u0006\u0002\u0002 )\u001aq\"!\t,\u0005\u0005\r\u0002\u0003BA\u0013\u0003_i!!a\n\u000b\t\u0005%\u00121F\u0001\nk:\u001c\u0007.Z2lK\u0012T1!!\f\u0017\u0003)\tgN\\8uCRLwN\\\u0005\u0005\u0003c\t9CA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016$aAEA\f\u0005\u0004\u0019BAB\u0010\u0002\u0018\t\u00071\u0003C\u0005\u0002:\u0001\t\n\u0011\"\u0001\u0002<\u0005q1m\u001c9zI\u0011,g-Y;mi\u0012\u0012TCBA\u001f\u0003\u0003\n\u0019%\u0006\u0002\u0002@)\u001aQ$!\t\u0005\rI\t9D1\u0001\u0014\t\u0019y\u0012q\u0007b\u0001'!I\u0011q\t\u0001\u0002\u0002\u0013\u0005\u0013\u0011J\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0005\u0005-\u0003\u0003BA'\u0003/j!!a\u0014\u000b\t\u0005E\u00131K\u0001\u0005Y\u0006twM\u0003\u0002\u0002V\u0005!!.\u0019<b\u0013\u0011\tI&a\u0014\u0003\rM#(/\u001b8h\u0011%\ti\u0006AA\u0001\n\u0003\ty&\u0001\u0007qe>$Wo\u0019;Be&$\u00180\u0006\u0002\u0002bA\u0019Q#a\u0019\n\u0007\u0005\u0015dCA\u0002J]RD\u0011\"!\u001b\u0001\u0003\u0003%\t!a\u001b\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0019!$!\u001c\t\u0015\u0005=\u0014qMA\u0001\u0002\u0004\t\t'A\u0002yIEB\u0011\"a\u001d\u0001\u0003\u0003%\t%!\u001e\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\"!a\u001e\u0011\u000b\u0005e\u0014q\u0010\u000e\u000e\u0005\u0005m$bAA?-\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\t\u0005\u0005\u00151\u0010\u0002\t\u0013R,'/\u0019;pe\"I\u0011Q\u0011\u0001\u0002\u0002\u0013\u0005\u0011qQ\u0001\tG\u0006tW)];bYR!\u0011\u0011RAH!\r)\u00121R\u0005\u0004\u0003\u001b3\"a\u0002\"p_2,\u0017M\u001c\u0005\n\u0003_\n\u0019)!AA\u0002iA\u0011\"a%\u0001\u0003\u0003%\t%!&\u0002\u0011!\f7\u000f[\"pI\u0016$\"!!\u0019\t\u0013\u0005e\u0005!!A\u0005B\u0005m\u0015\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0005\u0005-\u0003\"CAP\u0001\u0005\u0005I\u0011IAQ\u0003\u0019)\u0017/^1mgR!\u0011\u0011RAR\u0011%\ty'!(\u0002\u0002\u0003\u0007!dB\u0005\u0002(\n\t\t\u0011#\u0001\u0002*\u000691+^2dKN\u001c\bc\u0001\u0007\u0002,\u001aA\u0011AAA\u0001\u0012\u0003\tikE\u0003\u0002,\u0006=6\u0005E\u0002\u0016\u0003cK1!a-\u0017\u0005\u0019\te.\u001f*fM\"9\u0011'a+\u0005\u0002\u0005]FCAAU\u0011)\tI*a+\u0002\u0002\u0013\u0015\u00131\u0014\u0005\u000b\u0003{\u000bY+!A\u0005\u0002\u0006}\u0016!B1qa2LXCBAa\u0003\u000f\fY\r\u0006\u0004\u0002D\u00065\u0017q\u001a\t\u0007\u0019\u0001\t)-!3\u0011\u0007A\t9\r\u0002\u0004\u0013\u0003w\u0013\ra\u0005\t\u0004!\u0005-GAB\u0010\u0002<\n\u00071\u0003C\u0004(\u0003w\u0003\r!!2\t\u000f1\nY\f1\u0001\u0002J\"Q\u00111[AV\u0003\u0003%\t)!6\u0002\u000fUt\u0017\r\u001d9msV1\u0011q[Ar\u0003O$B!!7\u0002jB)Q#a7\u0002`&\u0019\u0011Q\u001c\f\u0003\r=\u0003H/[8o!\u0019)2-!9\u0002fB\u0019\u0001#a9\u0005\rI\t\tN1\u0001\u0014!\r\u0001\u0012q\u001d\u0003\u0007?\u0005E'\u0019A\n\t\u0015\u0005-\u0018\u0011[A\u0001\u0002\u0004\ti/A\u0002yIA\u0002b\u0001\u0004\u0001\u0002b\u0006\u0015\bBCAy\u0003W\u000b\t\u0011\"\u0003\u0002t\u0006Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\t)\u0010\u0005\u0003\u0002N\u0005]\u0018\u0002BA}\u0003\u001f\u0012aa\u00142kK\u000e$\b")
public class Success<Out, A> extends Result<Out, A, Nothing$> implements Product, Serializable
{
    private final Out out;
    private final A value;
    
    public static <Out, A> Option<Tuple2<Out, A>> unapply(final Success<Out, A> x$0) {
        return Success$.MODULE$.unapply(x$0);
    }
    
    public static <Out, A> Success<Out, A> apply(final Out out, final A value) {
        return Success$.MODULE$.apply(out, value);
    }
    
    @Override
    public Out out() {
        return this.out;
    }
    
    @Override
    public A value() {
        return this.value;
    }
    
    @Override
    public Nothing$ error() {
        throw new ScalaSigParserError("No error");
    }
    
    public Some<A> toOption() {
        return (Some<A>)new Some(this.value());
    }
    
    @Override
    public <B> Result<Out, B, Nothing$> map(final Function1<A, B> f) {
        return (Result<Out, B, Nothing$>)new Success(this.out(), f.apply(this.value()));
    }
    
    @Override
    public <Out2> Result<Out2, A, Nothing$> mapOut(final Function1<Out, Out2> f) {
        return (Result<Out2, A, Nothing$>)new Success(f.apply(this.out()), this.value());
    }
    
    @Override
    public <Out2, B> Success<Out2, B> map(final Function2<Out, A, Tuple2<Out2, B>> f) {
        final Tuple2 tuple2 = (Tuple2)f.apply(this.out(), this.value());
        if (tuple2 != null) {
            final Object out2 = tuple2._1();
            final Object b = tuple2._2();
            return new Success<Out2, B>((Out2)out2, (B)b);
        }
        throw new MatchError((Object)tuple2);
    }
    
    @Override
    public <Out2, B> Result<Out2, B, Nothing$> flatMap(final Function2<Out, A, Result<Out2, B, Nothing$>> f) {
        return (Result<Out2, B, Nothing$>)f.apply(this.out(), this.value());
    }
    
    @Override
    public <Out2, B> Result<Out2, B, Nothing$> orElse(final Function0<Result<Out2, B, Nothing$>> other) {
        return (Result<Out2, B, Nothing$>)this;
    }
    
    public <Out, A> Success<Out, A> copy(final Out out, final A value) {
        return new Success<Out, A>(out, value);
    }
    
    public <Out, A> Out copy$default$1() {
        return this.out();
    }
    
    public <Out, A> A copy$default$2() {
        return this.value();
    }
    
    public String productPrefix() {
        return "Success";
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
                o = this.value();
                break;
            }
            case 0: {
                o = this.out();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof Success;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof Success) {
                final Success success = (Success)x$1;
                if (BoxesRunTime.equals(this.out(), success.out()) && BoxesRunTime.equals(this.value(), (Object)success.value()) && success.canEqual(this)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public Success(final Out out, final A value) {
        this.out = out;
        this.value = value;
        Product$class.$init$((Product)this);
    }
}
