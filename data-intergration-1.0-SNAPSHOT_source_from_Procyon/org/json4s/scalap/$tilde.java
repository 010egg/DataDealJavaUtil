// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.collection.mutable.StringBuilder;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u0015d\u0001B\u0001\u0003\u0001&\u0011a\u0001\n;jY\u0012,'BA\u0002\u0005\u0003\u0019\u00198-\u00197ba*\u0011QAB\u0001\u0007UN|g\u000eN:\u000b\u0003\u001d\t1a\u001c:h\u0007\u0001)2A\u0003\u000f,'\u0011\u00011\"\u0005\u000b\u0011\u00051yQ\"A\u0007\u000b\u00039\tQa]2bY\u0006L!\u0001E\u0007\u0003\r\u0005s\u0017PU3g!\ta!#\u0003\u0002\u0014\u001b\t9\u0001K]8ek\u000e$\bC\u0001\u0007\u0016\u0013\t1RB\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005\u0019\u0001\tU\r\u0011\"\u0001\u001a\u0003\ty\u0016'F\u0001\u001b!\tYB\u0004\u0004\u0001\u0005\ru\u0001AQ1\u0001\u001f\u0005\u0005\t\u0015CA\u0010#!\ta\u0001%\u0003\u0002\"\u001b\t9aj\u001c;iS:<\u0007C\u0001\u0007$\u0013\t!SBA\u0002B]fD\u0001B\n\u0001\u0003\u0012\u0003\u0006IAG\u0001\u0004?F\u0002\u0003\u0002\u0003\u0015\u0001\u0005+\u0007I\u0011A\u0015\u0002\u0005}\u0013T#\u0001\u0016\u0011\u0005mYCA\u0002\u0017\u0001\t\u000b\u0007aDA\u0001C\u0011!q\u0003A!E!\u0002\u0013Q\u0013aA03A!)\u0001\u0007\u0001C\u0001c\u00051A(\u001b8jiz\"2A\r\u001b6!\u0011\u0019\u0004A\u0007\u0016\u000e\u0003\tAQ\u0001G\u0018A\u0002iAQ\u0001K\u0018A\u0002)BQa\u000e\u0001\u0005Ba\n\u0001\u0002^8TiJLgn\u001a\u000b\u0002sA\u0011!hP\u0007\u0002w)\u0011A(P\u0001\u0005Y\u0006twMC\u0001?\u0003\u0011Q\u0017M^1\n\u0005\u0001[$AB*ue&tw\rC\u0004C\u0001\u0005\u0005I\u0011A\"\u0002\t\r|\u0007/_\u000b\u0004\t\u001eKEcA#K\u0017B!1\u0007\u0001$I!\tYr\tB\u0003\u001e\u0003\n\u0007a\u0004\u0005\u0002\u001c\u0013\u0012)A&\u0011b\u0001=!9\u0001$\u0011I\u0001\u0002\u00041\u0005b\u0002\u0015B!\u0003\u0005\r\u0001\u0013\u0005\b\u001b\u0002\t\n\u0011\"\u0001O\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*2a\u0014.\\+\u0005\u0001&F\u0001\u000eRW\u0005\u0011\u0006CA*Y\u001b\u0005!&BA+W\u0003%)hn\u00195fG.,GM\u0003\u0002X\u001b\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\u0005e#&!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\u0012)Q\u0004\u0014b\u0001=\u0011)A\u0006\u0014b\u0001=!9Q\fAI\u0001\n\u0003q\u0016AD2paf$C-\u001a4bk2$HEM\u000b\u0004?\u0006\u0014W#\u00011+\u0005)\nF!B\u000f]\u0005\u0004qB!\u0002\u0017]\u0005\u0004q\u0002b\u00023\u0001\u0003\u0003%\t%Z\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003eBqa\u001a\u0001\u0002\u0002\u0013\u0005\u0001.\u0001\u0007qe>$Wo\u0019;Be&$\u00180F\u0001j!\ta!.\u0003\u0002l\u001b\t\u0019\u0011J\u001c;\t\u000f5\u0004\u0011\u0011!C\u0001]\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HC\u0001\u0012p\u0011\u001d\u0001H.!AA\u0002%\f1\u0001\u001f\u00132\u0011\u001d\u0011\b!!A\u0005BM\fq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0002iB\u0019Q\u000f\u001f\u0012\u000e\u0003YT!a^\u0007\u0002\u0015\r|G\u000e\\3di&|g.\u0003\u0002zm\nA\u0011\n^3sCR|'\u000fC\u0004|\u0001\u0005\u0005I\u0011\u0001?\u0002\u0011\r\fg.R9vC2$2!`A\u0001!\taa0\u0003\u0002\u0000\u001b\t9!i\\8mK\u0006t\u0007b\u00029{\u0003\u0003\u0005\rA\t\u0005\n\u0003\u000b\u0001\u0011\u0011!C!\u0003\u000f\t\u0001\u0002[1tQ\u000e{G-\u001a\u000b\u0002S\"I\u00111\u0002\u0001\u0002\u0002\u0013\u0005\u0013QB\u0001\u0007KF,\u0018\r\\:\u0015\u0007u\fy\u0001\u0003\u0005q\u0003\u0013\t\t\u00111\u0001#\u000f%\t\u0019BAA\u0001\u0012\u0003\t)\"\u0001\u0004%i&dG-\u001a\t\u0004g\u0005]a\u0001C\u0001\u0003\u0003\u0003E\t!!\u0007\u0014\t\u0005]1\u0002\u0006\u0005\ba\u0005]A\u0011AA\u000f)\t\t)\u0002\u0003\u00058\u0003/\t\t\u0011\"\u00129\u0011)\t\u0019#a\u0006\u0002\u0002\u0013\u0005\u0015QE\u0001\u0006CB\u0004H._\u000b\u0007\u0003O\ti#!\r\u0015\r\u0005%\u00121GA\u001b!\u0019\u0019\u0004!a\u000b\u00020A\u00191$!\f\u0005\ru\t\tC1\u0001\u001f!\rY\u0012\u0011\u0007\u0003\u0007Y\u0005\u0005\"\u0019\u0001\u0010\t\u000fa\t\t\u00031\u0001\u0002,!9\u0001&!\tA\u0002\u0005=\u0002BCA\u001d\u0003/\t\t\u0011\"!\u0002<\u00059QO\\1qa2LXCBA\u001f\u0003\u001b\n\t\u0006\u0006\u0003\u0002@\u0005M\u0003#\u0002\u0007\u0002B\u0005\u0015\u0013bAA\"\u001b\t1q\n\u001d;j_:\u0004r\u0001DA$\u0003\u0017\ny%C\u0002\u0002J5\u0011a\u0001V;qY\u0016\u0014\u0004cA\u000e\u0002N\u00111Q$a\u000eC\u0002y\u00012aGA)\t\u0019a\u0013q\u0007b\u0001=!Q\u0011QKA\u001c\u0003\u0003\u0005\r!a\u0016\u0002\u0007a$\u0003\u0007\u0005\u00044\u0001\u0005-\u0013q\n\u0005\u000b\u00037\n9\"!A\u0005\n\u0005u\u0013a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"!a\u0018\u0011\u0007i\n\t'C\u0002\u0002dm\u0012aa\u00142kK\u000e$\b")
public class $tilde<A, B> implements Product, Serializable
{
    private final A _1;
    private final B _2;
    
    public A _1() {
        return this._1;
    }
    
    public B _2() {
        return this._2;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append((Object)"(").append(this._1()).append((Object)" ~ ").append(this._2()).append((Object)")").toString();
    }
    
    public <A, B> $tilde<A, B> copy(final A _1, final B _2) {
        return new $tilde<A, B>(_1, _2);
    }
    
    public <A, B> A copy$default$1() {
        return this._1();
    }
    
    public <A, B> B copy$default$2() {
        return this._2();
    }
    
    public String productPrefix() {
        return "~";
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
                o = this._2();
                break;
            }
            case 0: {
                o = this._1();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof $tilde;
    }
    
    @Override
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof $tilde) {
                final $tilde $tilde = ($tilde)x$1;
                if (BoxesRunTime.equals(this._1(), $tilde._1()) && BoxesRunTime.equals(this._2(), (Object)$tilde._2()) && $tilde.canEqual(this)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public $tilde(final A _1, final B _2) {
        this._1 = _1;
        this._2 = _2;
        Product$class.$init$((Product)this);
    }
}
