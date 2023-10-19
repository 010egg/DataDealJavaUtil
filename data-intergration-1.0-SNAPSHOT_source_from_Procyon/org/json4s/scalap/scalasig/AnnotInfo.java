// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Option;
import scala.collection.Seq;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001db\u0001B\u0001\u0003\u0001.\u0011\u0011\"\u00118o_RLeNZ8\u000b\u0005\r!\u0011\u0001C:dC2\f7/[4\u000b\u0005\u00151\u0011AB:dC2\f\u0007O\u0003\u0002\b\u0011\u00051!n]8oiMT\u0011!C\u0001\u0004_J<7\u0001A\n\u0005\u00011\u0011R\u0003\u0005\u0002\u000e!5\taBC\u0001\u0010\u0003\u0015\u00198-\u00197b\u0013\t\tbB\u0001\u0004B]f\u0014VM\u001a\t\u0003\u001bMI!\u0001\u0006\b\u0003\u000fA\u0013x\u000eZ;diB\u0011QBF\u0005\u0003/9\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001\"\u0007\u0001\u0003\u0016\u0004%\tAG\u0001\u0005e\u001647/F\u0001\u001c!\raBe\n\b\u0003;\tr!AH\u0011\u000e\u0003}Q!\u0001\t\u0006\u0002\rq\u0012xn\u001c;?\u0013\u0005y\u0011BA\u0012\u000f\u0003\u001d\u0001\u0018mY6bO\u0016L!!\n\u0014\u0003\u0007M+\u0017O\u0003\u0002$\u001dA\u0011Q\u0002K\u0005\u0003S9\u00111!\u00138u\u0011!Y\u0003A!E!\u0002\u0013Y\u0012!\u0002:fMN\u0004\u0003\"B\u0017\u0001\t\u0003q\u0013A\u0002\u001fj]&$h\b\u0006\u00020cA\u0011\u0001\u0007A\u0007\u0002\u0005!)\u0011\u0004\fa\u00017!91\u0007AA\u0001\n\u0003!\u0014\u0001B2paf$\"aL\u001b\t\u000fe\u0011\u0004\u0013!a\u00017!9q\u0007AI\u0001\n\u0003A\u0014AD2paf$C-\u001a4bk2$H%M\u000b\u0002s)\u00121DO\u0016\u0002wA\u0011A(Q\u0007\u0002{)\u0011ahP\u0001\nk:\u001c\u0007.Z2lK\u0012T!\u0001\u0011\b\u0002\u0015\u0005tgn\u001c;bi&|g.\u0003\u0002C{\t\tRO\\2iK\u000e\\W\r\u001a,be&\fgnY3\t\u000f\u0011\u0003\u0011\u0011!C!\u000b\u0006i\u0001O]8ek\u000e$\bK]3gSb,\u0012A\u0012\t\u0003\u000f2k\u0011\u0001\u0013\u0006\u0003\u0013*\u000bA\u0001\\1oO*\t1*\u0001\u0003kCZ\f\u0017BA'I\u0005\u0019\u0019FO]5oO\"9q\nAA\u0001\n\u0003\u0001\u0016\u0001\u00049s_\u0012,8\r^!sSRLX#A\u0014\t\u000fI\u0003\u0011\u0011!C\u0001'\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HC\u0001+X!\tiQ+\u0003\u0002W\u001d\t\u0019\u0011I\\=\t\u000fa\u000b\u0016\u0011!a\u0001O\u0005\u0019\u0001\u0010J\u0019\t\u000fi\u0003\u0011\u0011!C!7\u0006y\u0001O]8ek\u000e$\u0018\n^3sCR|'/F\u0001]!\ri\u0006\rV\u0007\u0002=*\u0011qLD\u0001\u000bG>dG.Z2uS>t\u0017BA1_\u0005!IE/\u001a:bi>\u0014\bbB2\u0001\u0003\u0003%\t\u0001Z\u0001\tG\u0006tW)];bYR\u0011Q\r\u001b\t\u0003\u001b\u0019L!a\u001a\b\u0003\u000f\t{w\u000e\\3b]\"9\u0001LYA\u0001\u0002\u0004!\u0006b\u00026\u0001\u0003\u0003%\te[\u0001\tQ\u0006\u001c\bnQ8eKR\tq\u0005C\u0004n\u0001\u0005\u0005I\u0011\t8\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012A\u0012\u0005\ba\u0002\t\t\u0011\"\u0011r\u0003\u0019)\u0017/^1mgR\u0011QM\u001d\u0005\b1>\f\t\u00111\u0001U\u000f\u001d!(!!A\t\u0002U\f\u0011\"\u00118o_RLeNZ8\u0011\u0005A2haB\u0001\u0003\u0003\u0003E\ta^\n\u0004mb,\u0002\u0003B=}7=j\u0011A\u001f\u0006\u0003w:\tqA];oi&lW-\u0003\u0002~u\n\t\u0012IY:ue\u0006\u001cGOR;oGRLwN\\\u0019\t\u000b52H\u0011A@\u0015\u0003UDq!\u001c<\u0002\u0002\u0013\u0015c\u000eC\u0005\u0002\u0006Y\f\t\u0011\"!\u0002\b\u0005)\u0011\r\u001d9msR\u0019q&!\u0003\t\re\t\u0019\u00011\u0001\u001c\u0011%\tiA^A\u0001\n\u0003\u000by!A\u0004v]\u0006\u0004\b\u000f\\=\u0015\t\u0005E\u0011q\u0003\t\u0005\u001b\u0005M1$C\u0002\u0002\u00169\u0011aa\u00149uS>t\u0007\"CA\r\u0003\u0017\t\t\u00111\u00010\u0003\rAH\u0005\r\u0005\n\u0003;1\u0018\u0011!C\u0005\u0003?\t1B]3bIJ+7o\u001c7wKR\u0011\u0011\u0011\u0005\t\u0004\u000f\u0006\r\u0012bAA\u0013\u0011\n1qJ\u00196fGR\u0004")
public class AnnotInfo implements Product, Serializable
{
    private final Seq<Object> refs;
    
    public static Option<Seq<Object>> unapply(final AnnotInfo x$0) {
        return AnnotInfo$.MODULE$.unapply(x$0);
    }
    
    public static AnnotInfo apply(final Seq<Object> refs) {
        return AnnotInfo$.MODULE$.apply(refs);
    }
    
    public static <A> Function1<Seq<Object>, A> andThen(final Function1<AnnotInfo, A> function1) {
        return (Function1<Seq<Object>, A>)AnnotInfo$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, AnnotInfo> compose(final Function1<A, Seq<Object>> function1) {
        return (Function1<A, AnnotInfo>)AnnotInfo$.MODULE$.compose((Function1)function1);
    }
    
    public Seq<Object> refs() {
        return this.refs;
    }
    
    public AnnotInfo copy(final Seq<Object> refs) {
        return new AnnotInfo(refs);
    }
    
    public Seq<Object> copy$default$1() {
        return this.refs();
    }
    
    public String productPrefix() {
        return "AnnotInfo";
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
                return this.refs();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof AnnotInfo;
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
            if (x$1 instanceof AnnotInfo) {
                final AnnotInfo annotInfo = (AnnotInfo)x$1;
                final Seq<Object> refs = this.refs();
                final Seq<Object> refs2 = annotInfo.refs();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (refs == null) {
                            if (refs2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!refs.equals(refs2)) {
                            break Label_0076;
                        }
                        if (annotInfo.canEqual(this)) {
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
    
    public AnnotInfo(final Seq<Object> refs) {
        this.refs = refs;
        Product$class.$init$((Product)this);
    }
}
