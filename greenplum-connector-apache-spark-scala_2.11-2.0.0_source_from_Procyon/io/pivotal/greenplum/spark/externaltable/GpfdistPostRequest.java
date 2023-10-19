// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Tuple3;
import scala.runtime.AbstractFunction3;
import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.runtime.AbstractFunction2;
import scala.Product$class;
import scala.runtime.Statics;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import java.lang.constant.Constable;
import scala.runtime.BoxesRunTime;
import scala.Serializable;
import scala.Product;
import javax.servlet.http.HttpServletRequest;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\t-r!B\u0001\u0003\u0011\u0003i\u0011AE$qM\u0012L7\u000f\u001e)pgR\u0014V-];fgRT!a\u0001\u0003\u0002\u001b\u0015DH/\u001a:oC2$\u0018M\u00197f\u0015\t)a!A\u0003ta\u0006\u00148N\u0003\u0002\b\u0011\u0005IqM]3f]BdW/\u001c\u0006\u0003\u0013)\tq\u0001]5w_R\fGNC\u0001\f\u0003\tIwn\u0001\u0001\u0011\u00059yQ\"\u0001\u0002\u0007\u000bA\u0011\u0001\u0012A\t\u0003%\u001d\u0003h\rZ5tiB{7\u000f\u001e*fcV,7\u000f^\n\u0003\u001fI\u0001\"a\u0005\f\u000e\u0003QQ\u0011!F\u0001\u0006g\u000e\fG.Y\u0005\u0003/Q\u0011a!\u00118z%\u00164\u0007\"B\r\u0010\t\u0003Q\u0012A\u0002\u001fj]&$h\bF\u0001\u000e\u0011\u0015ar\u0002\"\u0001\u001e\u0003!1\u0018\r\\5eCR,Gc\u0001\u0010\u0002nA\u0011q\u0004I\u0007\u0002\u001f\u00199\u0011e\u0004I\u0001$C\u0011#!\u0005*fcV,7\u000f\u001e,bY&$\u0017\r^5p]N\u0011\u0001EE\u0015\u0005A\u0011\n)B\u0002\u0003&\u001f\u00013#AD%om\u0006d\u0017\u000e\u001a*fcV,7\u000f^\n\u0006IIqrE\u000b\t\u0003'!J!!\u000b\u000b\u0003\u000fA\u0013x\u000eZ;diB\u00111cK\u0005\u0003YQ\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001B\f\u0013\u0003\u0016\u0004%\taL\u0001\b[\u0016\u001c8/Y4f+\u0005\u0001\u0004CA\u00195\u001d\t\u0019\"'\u0003\u00024)\u00051\u0001K]3eK\u001aL!!\u000e\u001c\u0003\rM#(/\u001b8h\u0015\t\u0019D\u0003\u0003\u00059I\tE\t\u0015!\u00031\u0003!iWm]:bO\u0016\u0004\u0003\u0002\u0003\u001e%\u0005+\u0007I\u0011A\u001e\u0002\u0015M$\u0018\r^;t\u0007>$W-F\u0001=!\t\u0019R(\u0003\u0002?)\t\u0019\u0011J\u001c;\t\u0011\u0001##\u0011#Q\u0001\nq\n1b\u001d;biV\u001c8i\u001c3fA!)\u0011\u0004\nC\u0001\u0005R\u00191\tR#\u0011\u0005}!\u0003\"\u0002\u0018B\u0001\u0004\u0001\u0004\"\u0002\u001eB\u0001\u0004a\u0004bB$%\u0003\u0003%\t\u0001S\u0001\u0005G>\u0004\u0018\u0010F\u0002D\u0013*CqA\f$\u0011\u0002\u0003\u0007\u0001\u0007C\u0004;\rB\u0005\t\u0019\u0001\u001f\t\u000f1#\u0013\u0013!C\u0001\u001b\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\nT#\u0001(+\u0005Az5&\u0001)\u0011\u0005E3V\"\u0001*\u000b\u0005M#\u0016!C;oG\",7m[3e\u0015\t)F#\u0001\u0006b]:|G/\u0019;j_:L!a\u0016*\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rC\u0004ZIE\u0005I\u0011\u0001.\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%eU\t1L\u000b\u0002=\u001f\"9Q\fJA\u0001\n\u0003r\u0016!\u00049s_\u0012,8\r\u001e)sK\u001aL\u00070F\u0001`!\t\u0001W-D\u0001b\u0015\t\u00117-\u0001\u0003mC:<'\"\u00013\u0002\t)\fg/Y\u0005\u0003k\u0005Dqa\u001a\u0013\u0002\u0002\u0013\u00051(\u0001\u0007qe>$Wo\u0019;Be&$\u0018\u0010C\u0004jI\u0005\u0005I\u0011\u00016\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u00111N\u001c\t\u0003'1L!!\u001c\u000b\u0003\u0007\u0005s\u0017\u0010C\u0004pQ\u0006\u0005\t\u0019\u0001\u001f\u0002\u0007a$\u0013\u0007C\u0004rI\u0005\u0005I\u0011\t:\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\u0012a\u001d\t\u0004i^\\W\"A;\u000b\u0005Y$\u0012AC2pY2,7\r^5p]&\u0011\u00010\u001e\u0002\t\u0013R,'/\u0019;pe\"9!\u0010JA\u0001\n\u0003Y\u0018\u0001C2b]\u0016\u000bX/\u00197\u0015\u0005q|\bCA\n~\u0013\tqHCA\u0004C_>dW-\u00198\t\u000f=L\u0018\u0011!a\u0001W\"I\u00111\u0001\u0013\u0002\u0002\u0013\u0005\u0013QA\u0001\tQ\u0006\u001c\bnQ8eKR\tA\bC\u0005\u0002\n\u0011\n\t\u0011\"\u0011\u0002\f\u0005AAo\\*ue&tw\rF\u0001`\u0011%\ty\u0001JA\u0001\n\u0003\n\t\"\u0001\u0004fcV\fGn\u001d\u000b\u0004y\u0006M\u0001\u0002C8\u0002\u000e\u0005\u0005\t\u0019A6\u0007\r\u0005]q\u0002QA\r\u000511\u0016\r\\5e%\u0016\fX/Z:u'\u0019\t)B\u0005\u0010(U!Q\u0011QDA\u000b\u0005+\u0007I\u0011A\u001e\u0002\u001b\r|g\u000e^3oi2+gn\u001a;i\u0011)\t\t#!\u0006\u0003\u0012\u0003\u0006I\u0001P\u0001\u000fG>tG/\u001a8u\u0019\u0016tw\r\u001e5!\u0011)\t)#!\u0006\u0003\u0016\u0004%\taL\u0001\u000eiJ\fgn]1di&|g.\u00133\t\u0015\u0005%\u0012Q\u0003B\tB\u0003%\u0001'\u0001\bue\u0006t7/Y2uS>t\u0017\n\u001a\u0011\t\u0015\u00055\u0012Q\u0003BK\u0002\u0013\u00051(A\u0005tK\u001elWM\u001c;JI\"Q\u0011\u0011GA\u000b\u0005#\u0005\u000b\u0011\u0002\u001f\u0002\u0015M,w-\\3oi&#\u0007\u0005C\u0004\u001a\u0003+!\t!!\u000e\u0015\u0011\u0005]\u0012\u0011HA\u001e\u0003{\u00012aHA\u000b\u0011\u001d\ti\"a\rA\u0002qBq!!\n\u00024\u0001\u0007\u0001\u0007C\u0004\u0002.\u0005M\u0002\u0019\u0001\u001f\t\u0013\u001d\u000b)\"!A\u0005\u0002\u0005\u0005C\u0003CA\u001c\u0003\u0007\n)%a\u0012\t\u0013\u0005u\u0011q\bI\u0001\u0002\u0004a\u0004\"CA\u0013\u0003\u007f\u0001\n\u00111\u00011\u0011%\ti#a\u0010\u0011\u0002\u0003\u0007A\b\u0003\u0005M\u0003+\t\n\u0011\"\u0001[\u0011!I\u0016QCI\u0001\n\u0003i\u0005\"CA(\u0003+\t\n\u0011\"\u0001[\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIMB\u0001\"XA\u000b\u0003\u0003%\tE\u0018\u0005\tO\u0006U\u0011\u0011!C\u0001w!I\u0011.!\u0006\u0002\u0002\u0013\u0005\u0011q\u000b\u000b\u0004W\u0006e\u0003\u0002C8\u0002V\u0005\u0005\t\u0019\u0001\u001f\t\u0011E\f)\"!A\u0005BID\u0011B_A\u000b\u0003\u0003%\t!a\u0018\u0015\u0007q\f\t\u0007\u0003\u0005p\u0003;\n\t\u00111\u0001l\u0011)\t\u0019!!\u0006\u0002\u0002\u0013\u0005\u0013Q\u0001\u0005\u000b\u0003\u0013\t)\"!A\u0005B\u0005-\u0001BCA\b\u0003+\t\t\u0011\"\u0011\u0002jQ\u0019A0a\u001b\t\u0011=\f9'!AA\u0002-Dq!a\u001c\u001c\u0001\u0004\t\t(A\u0004sKF,Xm\u001d;\u0011\t\u0005M\u0014\u0011Q\u0007\u0003\u0003kRA!a\u001e\u0002z\u0005!\u0001\u000e\u001e;q\u0015\u0011\tY(! \u0002\u000fM,'O\u001e7fi*\u0011\u0011qP\u0001\u0006U\u00064\u0018\r_\u0005\u0005\u0003\u0007\u000b)H\u0001\nIiR\u00048+\u001a:wY\u0016$(+Z9vKN$x!CAD\u001f\u0005\u0005\t\u0012AAE\u00039IeN^1mS\u0012\u0014V-];fgR\u00042aHAF\r!)s\"!A\t\u0002\u000555#BAF\u0003\u001fS\u0003cBAI\u0003/\u0003DhQ\u0007\u0003\u0003'S1!!&\u0015\u0003\u001d\u0011XO\u001c;j[\u0016LA!!'\u0002\u0014\n\t\u0012IY:ue\u0006\u001cGOR;oGRLwN\u001c\u001a\t\u000fe\tY\t\"\u0001\u0002\u001eR\u0011\u0011\u0011\u0012\u0005\u000b\u0003\u0013\tY)!A\u0005F\u0005-\u0001BCAR\u0003\u0017\u000b\t\u0011\"!\u0002&\u0006)\u0011\r\u001d9msR)1)a*\u0002*\"1a&!)A\u0002ABaAOAQ\u0001\u0004a\u0004BCAW\u0003\u0017\u000b\t\u0011\"!\u00020\u00069QO\\1qa2LH\u0003BAY\u0003{\u0003RaEAZ\u0003oK1!!.\u0015\u0005\u0019y\u0005\u000f^5p]B)1#!/1y%\u0019\u00111\u0018\u000b\u0003\rQ+\b\u000f\\33\u0011%\ty,a+\u0002\u0002\u0003\u00071)A\u0002yIAB!\"a1\u0002\f\u0006\u0005I\u0011BAc\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005\u001d\u0007c\u00011\u0002J&\u0019\u00111Z1\u0003\r=\u0013'.Z2u\u000f%\tymDA\u0001\u0012\u0003\t\t.\u0001\u0007WC2LGMU3rk\u0016\u001cH\u000fE\u0002 \u0003'4\u0011\"a\u0006\u0010\u0003\u0003E\t!!6\u0014\u000b\u0005M\u0017q\u001b\u0016\u0011\u0013\u0005E\u0015\u0011\u001c\u001f1y\u0005]\u0012\u0002BAn\u0003'\u0013\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c84\u0011\u001dI\u00121\u001bC\u0001\u0003?$\"!!5\t\u0015\u0005%\u00111[A\u0001\n\u000b\nY\u0001\u0003\u0006\u0002$\u0006M\u0017\u0011!CA\u0003K$\u0002\"a\u000e\u0002h\u0006%\u00181\u001e\u0005\b\u0003;\t\u0019\u000f1\u0001=\u0011\u001d\t)#a9A\u0002ABq!!\f\u0002d\u0002\u0007A\b\u0003\u0006\u0002.\u0006M\u0017\u0011!CA\u0003_$B!!=\u0002zB)1#a-\u0002tB11#!>=aqJ1!a>\u0015\u0005\u0019!V\u000f\u001d7fg!Q\u0011qXAw\u0003\u0003\u0005\r!a\u000e\t\u0015\u0005\r\u00171[A\u0001\n\u0013\t)\r\u0003\u0005\u0002\u0000>\u0011\r\u0011\"\u00010\u0003\u0005\"\u0015j\u0015+S\u0013\n+F+\u0012#`)J\u000bejU!D)&{ejX%E?\"+\u0015\tR#S\u0011\u001d\u0011\u0019a\u0004Q\u0001\nA\n!\u0005R%T)JK%)\u0016+F\t~#&+\u0011(T\u0003\u000e#\u0016j\u0014(`\u0013\u0012{\u0006*R!E\u000bJ\u0003\u0003\u0002\u0003B\u0004\u001f\t\u0007I\u0011A\u0018\u0002#M+u)T#O)~KEi\u0018%F\u0003\u0012+%\u000bC\u0004\u0003\f=\u0001\u000b\u0011\u0002\u0019\u0002%M+u)T#O)~KEi\u0018%F\u0003\u0012+%\u000b\t\u0005\t\u0005\u001fy!\u0019!C\u0001_\u0005!2+R$N\u000b:#vlQ(V\u001dR{\u0006*R!E\u000bJCqAa\u0005\u0010A\u0003%\u0001'A\u000bT\u000b\u001ekUI\u0014+`\u0007>+f\nV0I\u000b\u0006#UI\u0015\u0011\t\u0013\t]qB1A\u0005\n\te\u0011a\u0004:fcVL'/\u001a3IK\u0006$WM]:\u0016\u0005\tm\u0001#\u0002B\u000f\u0005G\u0001TB\u0001B\u0010\u0015\r\u0011\t#^\u0001\nS6lW\u000f^1cY\u0016LAA!\n\u0003 \t!A*[:u\u0011!\u0011Ic\u0004Q\u0001\n\tm\u0011\u0001\u0005:fcVL'/\u001a3IK\u0006$WM]:!\u0001")
public final class GpfdistPostRequest
{
    public static String SEGMENT_COUNT_HEADER() {
        return GpfdistPostRequest$.MODULE$.SEGMENT_COUNT_HEADER();
    }
    
    public static String SEGMENT_ID_HEADER() {
        return GpfdistPostRequest$.MODULE$.SEGMENT_ID_HEADER();
    }
    
    public static String DISTRIBUTED_TRANSACTION_ID_HEADER() {
        return GpfdistPostRequest$.MODULE$.DISTRIBUTED_TRANSACTION_ID_HEADER();
    }
    
    public static RequestValidation validate(final HttpServletRequest request) {
        return GpfdistPostRequest$.MODULE$.validate(request);
    }
    
    public static class InvalidRequest implements RequestValidation, Product, Serializable
    {
        private final String message;
        private final int statusCode;
        
        public String message() {
            return this.message;
        }
        
        public int statusCode() {
            return this.statusCode;
        }
        
        public InvalidRequest copy(final String message, final int statusCode) {
            return new InvalidRequest(message, statusCode);
        }
        
        public String copy$default$1() {
            return this.message();
        }
        
        public int copy$default$2() {
            return this.statusCode();
        }
        
        public String productPrefix() {
            return "InvalidRequest";
        }
        
        public int productArity() {
            return 2;
        }
        
        public Object productElement(final int x$1) {
            Constable constable = null;
            switch (x$1) {
                default: {
                    throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
                }
                case 1: {
                    constable = BoxesRunTime.boxToInteger(this.statusCode());
                    break;
                }
                case 0: {
                    constable = this.message();
                    break;
                }
            }
            return constable;
        }
        
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof InvalidRequest;
        }
        
        @Override
        public int hashCode() {
            return Statics.finalizeHash(Statics.mix(Statics.mix(-889275714, Statics.anyHash((Object)this.message())), this.statusCode()), 2);
        }
        
        @Override
        public String toString() {
            return ScalaRunTime$.MODULE$._toString((Product)this);
        }
        
        @Override
        public boolean equals(final Object x$1) {
            if (this != x$1) {
                if (x$1 instanceof InvalidRequest) {
                    final InvalidRequest invalidRequest = (InvalidRequest)x$1;
                    final String message = this.message();
                    final String message2 = invalidRequest.message();
                    boolean b = false;
                    Label_0089: {
                        Label_0088: {
                            if (message == null) {
                                if (message2 != null) {
                                    break Label_0088;
                                }
                            }
                            else if (!message.equals(message2)) {
                                break Label_0088;
                            }
                            if (this.statusCode() == invalidRequest.statusCode() && invalidRequest.canEqual(this)) {
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
        
        public InvalidRequest(final String message, final int statusCode) {
            this.message = message;
            this.statusCode = statusCode;
            Product$class.$init$((Product)this);
        }
    }
    
    public static class InvalidRequest$ extends AbstractFunction2<String, Object, InvalidRequest> implements Serializable
    {
        public static final InvalidRequest$ MODULE$;
        
        static {
            new InvalidRequest$();
        }
        
        public final String toString() {
            return "InvalidRequest";
        }
        
        public InvalidRequest apply(final String message, final int statusCode) {
            return new InvalidRequest(message, statusCode);
        }
        
        public Option<Tuple2<String, Object>> unapply(final InvalidRequest x$0) {
            return (Option<Tuple2<String, Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.message(), (Object)BoxesRunTime.boxToInteger(x$0.statusCode()))));
        }
        
        private Object readResolve() {
            return InvalidRequest$.MODULE$;
        }
        
        public InvalidRequest$() {
            MODULE$ = this;
        }
    }
    
    public static class ValidRequest implements RequestValidation, Product, Serializable
    {
        private final int contentLength;
        private final String transactionId;
        private final int segmentId;
        
        public int contentLength() {
            return this.contentLength;
        }
        
        public String transactionId() {
            return this.transactionId;
        }
        
        public int segmentId() {
            return this.segmentId;
        }
        
        public ValidRequest copy(final int contentLength, final String transactionId, final int segmentId) {
            return new ValidRequest(contentLength, transactionId, segmentId);
        }
        
        public int copy$default$1() {
            return this.contentLength();
        }
        
        public String copy$default$2() {
            return this.transactionId();
        }
        
        public int copy$default$3() {
            return this.segmentId();
        }
        
        public String productPrefix() {
            return "ValidRequest";
        }
        
        public int productArity() {
            return 3;
        }
        
        public Object productElement(final int x$1) {
            Constable constable = null;
            switch (x$1) {
                default: {
                    throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
                }
                case 2: {
                    constable = BoxesRunTime.boxToInteger(this.segmentId());
                    break;
                }
                case 1: {
                    constable = this.transactionId();
                    break;
                }
                case 0: {
                    constable = BoxesRunTime.boxToInteger(this.contentLength());
                    break;
                }
            }
            return constable;
        }
        
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof ValidRequest;
        }
        
        @Override
        public int hashCode() {
            return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(-889275714, this.contentLength()), Statics.anyHash((Object)this.transactionId())), this.segmentId()), 3);
        }
        
        @Override
        public String toString() {
            return ScalaRunTime$.MODULE$._toString((Product)this);
        }
        
        @Override
        public boolean equals(final Object x$1) {
            if (this != x$1) {
                if (x$1 instanceof ValidRequest) {
                    final ValidRequest validRequest = (ValidRequest)x$1;
                    boolean b = false;
                    Label_0101: {
                        Label_0100: {
                            if (this.contentLength() == validRequest.contentLength()) {
                                final String transactionId = this.transactionId();
                                final String transactionId2 = validRequest.transactionId();
                                if (transactionId == null) {
                                    if (transactionId2 != null) {
                                        break Label_0100;
                                    }
                                }
                                else if (!transactionId.equals(transactionId2)) {
                                    break Label_0100;
                                }
                                if (this.segmentId() == validRequest.segmentId() && validRequest.canEqual(this)) {
                                    b = true;
                                    break Label_0101;
                                }
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
        
        public ValidRequest(final int contentLength, final String transactionId, final int segmentId) {
            this.contentLength = contentLength;
            this.transactionId = transactionId;
            this.segmentId = segmentId;
            Product$class.$init$((Product)this);
        }
    }
    
    public static class ValidRequest$ extends AbstractFunction3<Object, String, Object, ValidRequest> implements Serializable
    {
        public static final ValidRequest$ MODULE$;
        
        static {
            new ValidRequest$();
        }
        
        public final String toString() {
            return "ValidRequest";
        }
        
        public ValidRequest apply(final int contentLength, final String transactionId, final int segmentId) {
            return new ValidRequest(contentLength, transactionId, segmentId);
        }
        
        public Option<Tuple3<Object, String, Object>> unapply(final ValidRequest x$0) {
            return (Option<Tuple3<Object, String, Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)BoxesRunTime.boxToInteger(x$0.contentLength()), (Object)x$0.transactionId(), (Object)BoxesRunTime.boxToInteger(x$0.segmentId()))));
        }
        
        private Object readResolve() {
            return ValidRequest$.MODULE$;
        }
        
        public ValidRequest$() {
            MODULE$ = this;
        }
    }
    
    public interface RequestValidation
    {
    }
}
