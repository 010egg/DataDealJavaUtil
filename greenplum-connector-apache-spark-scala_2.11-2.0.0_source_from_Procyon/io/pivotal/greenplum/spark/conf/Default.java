// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.conf;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u0015b\u0001B\u0001\u0003\u00016\u0011q\u0001R3gCVdGO\u0003\u0002\u0004\t\u0005!1m\u001c8g\u0015\t)a!A\u0003ta\u0006\u00148N\u0003\u0002\b\u0011\u0005IqM]3f]BdW/\u001c\u0006\u0003\u0013)\tq\u0001]5w_R\fGNC\u0001\f\u0003\tIwn\u0001\u0001\u0014\u000b\u0001qA\u0003G\u000e\u0011\u0005=\u0011R\"\u0001\t\u000b\u0003E\tQa]2bY\u0006L!a\u0005\t\u0003\r\u0005s\u0017PU3g!\t)b#D\u0001\u0003\u0013\t9\"AA\u0007XQ\u0006$\u0018JZ'jgNLgn\u001a\t\u0003\u001feI!A\u0007\t\u0003\u000fA\u0013x\u000eZ;diB\u0011q\u0002H\u0005\u0003;A\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001b\b\u0001\u0003\u0016\u0004%\t\u0001I\u0001\u0006m\u0006dW/Z\u000b\u0002CA\u0011!%\n\b\u0003\u001f\rJ!\u0001\n\t\u0002\rA\u0013X\rZ3g\u0013\t1sE\u0001\u0004TiJLgn\u001a\u0006\u0003IAA\u0001\"\u000b\u0001\u0003\u0012\u0003\u0006I!I\u0001\u0007m\u0006dW/\u001a\u0011\t\u000b-\u0002A\u0011\u0001\u0017\u0002\rqJg.\u001b;?)\tic\u0006\u0005\u0002\u0016\u0001!)qD\u000ba\u0001C!9\u0001\u0007AA\u0001\n\u0003\t\u0014\u0001B2paf$\"!\f\u001a\t\u000f}y\u0003\u0013!a\u0001C!9A\u0007AI\u0001\n\u0003)\u0014AD2paf$C-\u001a4bk2$H%M\u000b\u0002m)\u0012\u0011eN\u0016\u0002qA\u0011\u0011HP\u0007\u0002u)\u00111\bP\u0001\nk:\u001c\u0007.Z2lK\u0012T!!\u0010\t\u0002\u0015\u0005tgn\u001c;bi&|g.\u0003\u0002@u\t\tRO\\2iK\u000e\\W\r\u001a,be&\fgnY3\t\u000f\u0005\u0003\u0011\u0011!C!\u0005\u0006i\u0001O]8ek\u000e$\bK]3gSb,\u0012a\u0011\t\u0003\t&k\u0011!\u0012\u0006\u0003\r\u001e\u000bA\u0001\\1oO*\t\u0001*\u0001\u0003kCZ\f\u0017B\u0001\u0014F\u0011\u001dY\u0005!!A\u0005\u00021\u000bA\u0002\u001d:pIV\u001cG/\u0011:jif,\u0012!\u0014\t\u0003\u001f9K!a\u0014\t\u0003\u0007%sG\u000fC\u0004R\u0001\u0005\u0005I\u0011\u0001*\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u00111K\u0016\t\u0003\u001fQK!!\u0016\t\u0003\u0007\u0005s\u0017\u0010C\u0004X!\u0006\u0005\t\u0019A'\u0002\u0007a$\u0013\u0007C\u0004Z\u0001\u0005\u0005I\u0011\t.\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\u0012a\u0017\t\u00049~\u001bV\"A/\u000b\u0005y\u0003\u0012AC2pY2,7\r^5p]&\u0011\u0001-\u0018\u0002\t\u0013R,'/\u0019;pe\"9!\rAA\u0001\n\u0003\u0019\u0017\u0001C2b]\u0016\u000bX/\u00197\u0015\u0005\u0011<\u0007CA\bf\u0013\t1\u0007CA\u0004C_>dW-\u00198\t\u000f]\u000b\u0017\u0011!a\u0001'\"9\u0011\u000eAA\u0001\n\u0003R\u0017\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u00035Cq\u0001\u001c\u0001\u0002\u0002\u0013\u0005S.\u0001\u0005u_N#(/\u001b8h)\u0005\u0019\u0005bB8\u0001\u0003\u0003%\t\u0005]\u0001\u0007KF,\u0018\r\\:\u0015\u0005\u0011\f\bbB,o\u0003\u0003\u0005\raU\u0004\bg\n\t\t\u0011#\u0001u\u0003\u001d!UMZ1vYR\u0004\"!F;\u0007\u000f\u0005\u0011\u0011\u0011!E\u0001mN\u0019Qo^\u000e\u0011\ta\\\u0018%L\u0007\u0002s*\u0011!\u0010E\u0001\beVtG/[7f\u0013\ta\u0018PA\tBEN$(/Y2u\rVt7\r^5p]FBQaK;\u0005\u0002y$\u0012\u0001\u001e\u0005\bYV\f\t\u0011\"\u0012n\u0011%\t\u0019!^A\u0001\n\u0003\u000b)!A\u0003baBd\u0017\u0010F\u0002.\u0003\u000fAaaHA\u0001\u0001\u0004\t\u0003\"CA\u0006k\u0006\u0005I\u0011QA\u0007\u0003\u001d)h.\u00199qYf$B!a\u0004\u0002\u0016A!q\"!\u0005\"\u0013\r\t\u0019\u0002\u0005\u0002\u0007\u001fB$\u0018n\u001c8\t\u0013\u0005]\u0011\u0011BA\u0001\u0002\u0004i\u0013a\u0001=%a!I\u00111D;\u0002\u0002\u0013%\u0011QD\u0001\fe\u0016\fGMU3t_24X\r\u0006\u0002\u0002 A\u0019A)!\t\n\u0007\u0005\rRI\u0001\u0004PE*,7\r\u001e")
public class Default implements WhatIfMissing, Product, Serializable
{
    private final String value;
    
    public static Option<String> unapply(final Default x$0) {
        return Default$.MODULE$.unapply(x$0);
    }
    
    public static Default apply(final String value) {
        return Default$.MODULE$.apply(value);
    }
    
    public static <A> Function1<String, A> andThen(final Function1<Default, A> function1) {
        return (Function1<String, A>)Default$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, Default> compose(final Function1<A, String> function1) {
        return (Function1<A, Default>)Default$.MODULE$.compose((Function1)function1);
    }
    
    public String value() {
        return this.value;
    }
    
    public Default copy(final String value) {
        return new Default(value);
    }
    
    public String copy$default$1() {
        return this.value();
    }
    
    public String productPrefix() {
        return "Default";
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
                return this.value();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof Default;
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
            if (x$1 instanceof Default) {
                final Default default1 = (Default)x$1;
                final String value = this.value();
                final String value2 = default1.value();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (value == null) {
                            if (value2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!value.equals(value2)) {
                            break Label_0076;
                        }
                        if (default1.canEqual(this)) {
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
    
    public Default(final String value) {
        this.value = value;
        Product$class.$init$((Product)this);
    }
}
