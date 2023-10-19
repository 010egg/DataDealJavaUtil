// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005Mb\u0001B\u0001\u0003\u0001&\u0011Q!\u0012:s_JT!a\u0001\u0003\u0002\rM\u001c\u0017\r\\1q\u0015\t)a!\u0001\u0004kg>tGg\u001d\u0006\u0002\u000f\u0005\u0019qN]4\u0004\u0001U\u0011!\"E\n\u0005\u0001-i\u0002\u0005E\u0002\r\u001b=i\u0011AA\u0005\u0003\u001d\t\u0011\u0011BT8Tk\u000e\u001cWm]:\u0011\u0005A\tB\u0002\u0001\u0003\u0007%\u0001!)\u0019A\n\u0003\u0003a\u000b\"\u0001\u0006\u000e\u0011\u0005UAR\"\u0001\f\u000b\u0003]\tQa]2bY\u0006L!!\u0007\f\u0003\u000f9{G\u000f[5oOB\u0011QcG\u0005\u00039Y\u00111!\u00118z!\t)b$\u0003\u0002 -\t9\u0001K]8ek\u000e$\bCA\u000b\"\u0013\t\u0011cC\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005%\u0001\tU\r\u0011\"\u0001&\u0003\u0015)'O]8s+\u0005y\u0001\u0002C\u0014\u0001\u0005#\u0005\u000b\u0011B\b\u0002\r\u0015\u0014(o\u001c:!\u0011\u0015I\u0003\u0001\"\u0001+\u0003\u0019a\u0014N\\5u}Q\u00111\u0006\f\t\u0004\u0019\u0001y\u0001\"\u0002\u0013)\u0001\u0004y\u0001b\u0002\u0018\u0001\u0003\u0003%\taL\u0001\u0005G>\u0004\u00180\u0006\u00021gQ\u0011\u0011\u0007\u000e\t\u0004\u0019\u0001\u0011\u0004C\u0001\t4\t\u0015\u0011RF1\u0001\u0014\u0011\u001d!S\u0006%AA\u0002IBqA\u000e\u0001\u0012\u0002\u0013\u0005q'\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u0005a\u001aU#A\u001d+\u0005=Q4&A\u001e\u0011\u0005q\nU\"A\u001f\u000b\u0005yz\u0014!C;oG\",7m[3e\u0015\t\u0001e#\u0001\u0006b]:|G/\u0019;j_:L!AQ\u001f\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rB\u0003\u0013k\t\u00071\u0003C\u0004F\u0001\u0005\u0005I\u0011\t$\u0002\u001bA\u0014x\u000eZ;diB\u0013XMZ5y+\u00059\u0005C\u0001%N\u001b\u0005I%B\u0001&L\u0003\u0011a\u0017M\\4\u000b\u00031\u000bAA[1wC&\u0011a*\u0013\u0002\u0007'R\u0014\u0018N\\4\t\u000fA\u0003\u0011\u0011!C\u0001#\u0006a\u0001O]8ek\u000e$\u0018I]5usV\t!\u000b\u0005\u0002\u0016'&\u0011AK\u0006\u0002\u0004\u0013:$\bb\u0002,\u0001\u0003\u0003%\taV\u0001\u000faJ|G-^2u\u000b2,W.\u001a8u)\tQ\u0002\fC\u0004Z+\u0006\u0005\t\u0019\u0001*\u0002\u0007a$\u0013\u0007C\u0004\\\u0001\u0005\u0005I\u0011\t/\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\u0012!\u0018\t\u0004=\u0006TR\"A0\u000b\u0005\u00014\u0012AC2pY2,7\r^5p]&\u0011!m\u0018\u0002\t\u0013R,'/\u0019;pe\"9A\rAA\u0001\n\u0003)\u0017\u0001C2b]\u0016\u000bX/\u00197\u0015\u0005\u0019L\u0007CA\u000bh\u0013\tAgCA\u0004C_>dW-\u00198\t\u000fe\u001b\u0017\u0011!a\u00015!91\u000eAA\u0001\n\u0003b\u0017\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003ICqA\u001c\u0001\u0002\u0002\u0013\u0005s.\u0001\u0005u_N#(/\u001b8h)\u00059\u0005bB9\u0001\u0003\u0003%\tE]\u0001\u0007KF,\u0018\r\\:\u0015\u0005\u0019\u001c\bbB-q\u0003\u0003\u0005\rAG\u0004\bk\n\t\t\u0011#\u0001w\u0003\u0015)%O]8s!\taqOB\u0004\u0002\u0005\u0005\u0005\t\u0012\u0001=\u0014\u0007]L\b\u0005\u0005\u0002\u0016u&\u00111P\u0006\u0002\u0007\u0003:L(+\u001a4\t\u000b%:H\u0011A?\u0015\u0003YDqA\\<\u0002\u0002\u0013\u0015s\u000eC\u0005\u0002\u0002]\f\t\u0011\"!\u0002\u0004\u0005)\u0011\r\u001d9msV!\u0011QAA\u0006)\u0011\t9!!\u0004\u0011\t1\u0001\u0011\u0011\u0002\t\u0004!\u0005-A!\u0002\n\u0000\u0005\u0004\u0019\u0002B\u0002\u0013\u0000\u0001\u0004\tI\u0001C\u0005\u0002\u0012]\f\t\u0011\"!\u0002\u0014\u00059QO\\1qa2LX\u0003BA\u000b\u0003?!B!a\u0006\u0002\"A)Q#!\u0007\u0002\u001e%\u0019\u00111\u0004\f\u0003\r=\u0003H/[8o!\r\u0001\u0012q\u0004\u0003\u0007%\u0005=!\u0019A\n\t\u0015\u0005\r\u0012qBA\u0001\u0002\u0004\t)#A\u0002yIA\u0002B\u0001\u0004\u0001\u0002\u001e!I\u0011\u0011F<\u0002\u0002\u0013%\u00111F\u0001\fe\u0016\fGMU3t_24X\r\u0006\u0002\u0002.A\u0019\u0001*a\f\n\u0007\u0005E\u0012J\u0001\u0004PE*,7\r\u001e")
public class Error<X> extends NoSuccess<X> implements Product, Serializable
{
    private final X error;
    
    public static <X> Option<X> unapply(final Error<X> x$0) {
        return Error$.MODULE$.unapply(x$0);
    }
    
    public static <X> Error<X> apply(final X error) {
        return Error$.MODULE$.apply(error);
    }
    
    public X error() {
        return this.error;
    }
    
    public <X> Error<X> copy(final X error) {
        return new Error<X>(error);
    }
    
    public <X> X copy$default$1() {
        return this.error();
    }
    
    public String productPrefix() {
        return "Error";
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
                return this.error();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof Error;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof Error) {
                final Error error = (Error)x$1;
                if (BoxesRunTime.equals(this.error(), error.error()) && error.canEqual(this)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public Error(final X error) {
        this.error = error;
        Product$class.$init$((Product)this);
    }
}
