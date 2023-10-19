// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.runtime.Statics;
import scala.Product;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple3;
import scala.Option;
import scala.collection.Seq;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u0015d\u0001B\u0001\u0003\u0001&\u0011QcQ8ogR\u0014Xo\u0019;pe\u0012+7o\u0019:jaR|'O\u0003\u0002\u0004\t\u00059!/\u001a4mK\u000e$(BA\u0003\u0007\u0003\u0019Q7o\u001c85g*\tq!A\u0002pe\u001e\u001c\u0001a\u0005\u0003\u0001\u00159!\u0002CA\u0006\r\u001b\u0005\u0011\u0011BA\u0007\u0003\u0005)!Um]2sSB$xN\u001d\t\u0003\u001fIi\u0011\u0001\u0005\u0006\u0002#\u0005)1oY1mC&\u00111\u0003\u0005\u0002\b!J|G-^2u!\tyQ#\u0003\u0002\u0017!\ta1+\u001a:jC2L'0\u00192mK\"A\u0001\u0004\u0001BK\u0002\u0013\u0005\u0011$\u0001\u0004qCJ\fWn]\u000b\u00025A\u00191d\t\u0014\u000f\u0005q\tcBA\u000f!\u001b\u0005q\"BA\u0010\t\u0003\u0019a$o\\8u}%\t\u0011#\u0003\u0002#!\u00059\u0001/Y2lC\u001e,\u0017B\u0001\u0013&\u0005\r\u0019V-\u001d\u0006\u0003EA\u0001\"aC\u0014\n\u0005!\u0012!AG\"p]N$(/^2u_J\u0004\u0016M]1n\t\u0016\u001c8M]5qi>\u0014\b\u0002\u0003\u0016\u0001\u0005#\u0005\u000b\u0011\u0002\u000e\u0002\u000fA\f'/Y7tA!AA\u0006\u0001BK\u0002\u0013\u0005Q&A\u0006d_:\u001cHO];di>\u0014X#\u0001\u0018\u0011\u0005-y\u0013B\u0001\u0019\u0003\u0005))\u00050Z2vi\u0006\u0014G.\u001a\u0005\te\u0001\u0011\t\u0012)A\u0005]\u0005a1m\u001c8tiJ,8\r^8sA!AA\u0007\u0001BK\u0002\u0013\u0005Q'A\u0005jgB\u0013\u0018.\\1ssV\ta\u0007\u0005\u0002\u0010o%\u0011\u0001\b\u0005\u0002\b\u0005>|G.Z1o\u0011!Q\u0004A!E!\u0002\u00131\u0014AC5t!JLW.\u0019:zA!)A\b\u0001C\u0001{\u00051A(\u001b8jiz\"BAP A\u0003B\u00111\u0002\u0001\u0005\u00061m\u0002\rA\u0007\u0005\u0006Ym\u0002\rA\f\u0005\u0006im\u0002\rA\u000e\u0005\b\u0007\u0002\t\t\u0011\"\u0001E\u0003\u0011\u0019w\u000e]=\u0015\ty*ei\u0012\u0005\b1\t\u0003\n\u00111\u0001\u001b\u0011\u001da#\t%AA\u00029Bq\u0001\u000e\"\u0011\u0002\u0003\u0007a\u0007C\u0004J\u0001E\u0005I\u0011\u0001&\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\t1J\u000b\u0002\u001b\u0019.\nQ\n\u0005\u0002O'6\tqJ\u0003\u0002Q#\u0006IQO\\2iK\u000e\\W\r\u001a\u0006\u0003%B\t!\"\u00198o_R\fG/[8o\u0013\t!vJA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016DqA\u0016\u0001\u0012\u0002\u0013\u0005q+\u0001\bd_BLH\u0005Z3gCVdG\u000f\n\u001a\u0016\u0003aS#A\f'\t\u000fi\u0003\u0011\u0013!C\u00017\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\u001aT#\u0001/+\u0005Yb\u0005b\u00020\u0001\u0003\u0003%\teX\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003\u0001\u0004\"!\u00194\u000e\u0003\tT!a\u00193\u0002\t1\fgn\u001a\u0006\u0002K\u0006!!.\u0019<b\u0013\t9'M\u0001\u0004TiJLgn\u001a\u0005\bS\u0002\t\t\u0011\"\u0001k\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\u0005Y\u0007CA\bm\u0013\ti\u0007CA\u0002J]RDqa\u001c\u0001\u0002\u0002\u0013\u0005\u0001/\u0001\bqe>$Wo\u0019;FY\u0016lWM\u001c;\u0015\u0005E$\bCA\bs\u0013\t\u0019\bCA\u0002B]fDq!\u001e8\u0002\u0002\u0003\u00071.A\u0002yIEBqa\u001e\u0001\u0002\u0002\u0013\u0005\u00030A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\u0005I\bc\u0001>~c6\t1P\u0003\u0002}!\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\u0005y\\(\u0001C%uKJ\fGo\u001c:\t\u0013\u0005\u0005\u0001!!A\u0005\u0002\u0005\r\u0011\u0001C2b]\u0016\u000bX/\u00197\u0015\u0007Y\n)\u0001C\u0004v\u007f\u0006\u0005\t\u0019A9\t\u0013\u0005%\u0001!!A\u0005B\u0005-\u0011\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003-D\u0011\"a\u0004\u0001\u0003\u0003%\t%!\u0005\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012\u0001\u0019\u0005\n\u0003+\u0001\u0011\u0011!C!\u0003/\ta!Z9vC2\u001cHc\u0001\u001c\u0002\u001a!AQ/a\u0005\u0002\u0002\u0003\u0007\u0011oB\u0005\u0002\u001e\t\t\t\u0011#\u0001\u0002 \u0005)2i\u001c8tiJ,8\r^8s\t\u0016\u001c8M]5qi>\u0014\bcA\u0006\u0002\"\u0019A\u0011AAA\u0001\u0012\u0003\t\u0019cE\u0003\u0002\"\u0005\u0015B\u0003\u0005\u0005\u0002(\u00055\"D\f\u001c?\u001b\t\tICC\u0002\u0002,A\tqA];oi&lW-\u0003\u0003\u00020\u0005%\"!E!cgR\u0014\u0018m\u0019;Gk:\u001cG/[8og!9A(!\t\u0005\u0002\u0005MBCAA\u0010\u0011)\ty!!\t\u0002\u0002\u0013\u0015\u0013\u0011\u0003\u0005\u000b\u0003s\t\t#!A\u0005\u0002\u0006m\u0012!B1qa2LHc\u0002 \u0002>\u0005}\u0012\u0011\t\u0005\u00071\u0005]\u0002\u0019\u0001\u000e\t\r1\n9\u00041\u0001/\u0011\u0019!\u0014q\u0007a\u0001m!Q\u0011QIA\u0011\u0003\u0003%\t)a\u0012\u0002\u000fUt\u0017\r\u001d9msR!\u0011\u0011JA+!\u0015y\u00111JA(\u0013\r\ti\u0005\u0005\u0002\u0007\u001fB$\u0018n\u001c8\u0011\r=\t\tF\u0007\u00187\u0013\r\t\u0019\u0006\u0005\u0002\u0007)V\u0004H.Z\u001a\t\u0013\u0005]\u00131IA\u0001\u0002\u0004q\u0014a\u0001=%a!Q\u00111LA\u0011\u0003\u0003%I!!\u0018\u0002\u0017I,\u0017\r\u001a*fg>dg/\u001a\u000b\u0003\u0003?\u00022!YA1\u0013\r\t\u0019G\u0019\u0002\u0007\u001f\nTWm\u0019;")
public class ConstructorDescriptor extends Descriptor
{
    private final Seq<ConstructorParamDescriptor> params;
    private final Executable constructor;
    private final boolean isPrimary;
    
    public static Option<Tuple3<Seq<ConstructorParamDescriptor>, Executable, Object>> unapply(final ConstructorDescriptor x$0) {
        return ConstructorDescriptor$.MODULE$.unapply(x$0);
    }
    
    public static ConstructorDescriptor apply(final Seq<ConstructorParamDescriptor> params, final Executable constructor, final boolean isPrimary) {
        return ConstructorDescriptor$.MODULE$.apply(params, constructor, isPrimary);
    }
    
    public static Function1<Tuple3<Seq<ConstructorParamDescriptor>, Executable, Object>, ConstructorDescriptor> tupled() {
        return (Function1<Tuple3<Seq<ConstructorParamDescriptor>, Executable, Object>, ConstructorDescriptor>)ConstructorDescriptor$.MODULE$.tupled();
    }
    
    public static Function1<Seq<ConstructorParamDescriptor>, Function1<Executable, Function1<Object, ConstructorDescriptor>>> curried() {
        return (Function1<Seq<ConstructorParamDescriptor>, Function1<Executable, Function1<Object, ConstructorDescriptor>>>)ConstructorDescriptor$.MODULE$.curried();
    }
    
    public Seq<ConstructorParamDescriptor> params() {
        return this.params;
    }
    
    public Executable constructor() {
        return this.constructor;
    }
    
    public boolean isPrimary() {
        return this.isPrimary;
    }
    
    public ConstructorDescriptor copy(final Seq<ConstructorParamDescriptor> params, final Executable constructor, final boolean isPrimary) {
        return new ConstructorDescriptor(params, constructor, isPrimary);
    }
    
    public Seq<ConstructorParamDescriptor> copy$default$1() {
        return this.params();
    }
    
    public Executable copy$default$2() {
        return this.constructor();
    }
    
    public boolean copy$default$3() {
        return this.isPrimary();
    }
    
    @Override
    public String productPrefix() {
        return "ConstructorDescriptor";
    }
    
    public int productArity() {
        return 3;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 2: {
                o = BoxesRunTime.boxToBoolean(this.isPrimary());
                break;
            }
            case 1: {
                o = this.constructor();
                break;
            }
            case 0: {
                o = this.params();
                break;
            }
        }
        return o;
    }
    
    @Override
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ConstructorDescriptor;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(-889275714, Statics.anyHash((Object)this.params())), Statics.anyHash((Object)this.constructor())), this.isPrimary() ? 1231 : 1237), 3);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ConstructorDescriptor) {
                final ConstructorDescriptor constructorDescriptor = (ConstructorDescriptor)x$1;
                final Seq<ConstructorParamDescriptor> params = this.params();
                final Seq<ConstructorParamDescriptor> params2 = constructorDescriptor.params();
                boolean b = false;
                Label_0121: {
                    Label_0120: {
                        if (params == null) {
                            if (params2 != null) {
                                break Label_0120;
                            }
                        }
                        else if (!params.equals(params2)) {
                            break Label_0120;
                        }
                        final Executable constructor = this.constructor();
                        final Executable constructor2 = constructorDescriptor.constructor();
                        if (constructor == null) {
                            if (constructor2 != null) {
                                break Label_0120;
                            }
                        }
                        else if (!constructor.equals(constructor2)) {
                            break Label_0120;
                        }
                        if (this.isPrimary() == constructorDescriptor.isPrimary() && constructorDescriptor.canEqual(this)) {
                            b = true;
                            break Label_0121;
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
    
    public ConstructorDescriptor(final Seq<ConstructorParamDescriptor> params, final Executable constructor, final boolean isPrimary) {
        this.params = params;
        this.constructor = constructor;
        this.isPrimary = isPrimary;
    }
}
