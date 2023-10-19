// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Product;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.collection.immutable.Nil$;
import scala.Function0;
import scala.StringContext;
import scala.Some;
import scala.Function2;
import scala.runtime.BoxesRunTime;
import scala.collection.TraversableOnce;
import scala.Tuple2;
import scala.None$;
import scala.Predef$;
import scala.collection.immutable.Set;
import scala.collection.immutable.List;
import scala.Function1;
import scala.Tuple6;
import scala.collection.Seq;
import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001dh\u0001B\u0001\u0003\u0001&\u0011qb\u00117bgN$Um]2sSB$xN\u001d\u0006\u0003\u0007\u0011\tqA]3gY\u0016\u001cGO\u0003\u0002\u0006\r\u00051!n]8oiMT\u0011aB\u0001\u0004_J<7\u0001A\n\u0005\u0001)qA\u0003\u0005\u0002\f\u00195\t!!\u0003\u0002\u000e\u0005\t\u0001rJ\u00196fGR$Um]2sSB$xN\u001d\t\u0003\u001fIi\u0011\u0001\u0005\u0006\u0002#\u0005)1oY1mC&\u00111\u0003\u0005\u0002\b!J|G-^2u!\tyQ#\u0003\u0002\u0017!\ta1+\u001a:jC2L'0\u00192mK\"A\u0001\u0004\u0001BK\u0002\u0013\u0005\u0011$\u0001\u0006tS6\u0004H.\u001a(b[\u0016,\u0012A\u0007\t\u00037yq!a\u0004\u000f\n\u0005u\u0001\u0012A\u0002)sK\u0012,g-\u0003\u0002 A\t11\u000b\u001e:j]\u001eT!!\b\t\t\u0011\t\u0002!\u0011#Q\u0001\ni\t1b]5na2,g*Y7fA!AA\u0005\u0001BK\u0002\u0013\u0005\u0011$\u0001\u0005gk2dg*Y7f\u0011!1\u0003A!E!\u0002\u0013Q\u0012!\u00034vY2t\u0015-\\3!\u0011!A\u0003A!f\u0001\n\u0003I\u0013aB3sCN,(/Z\u000b\u0002UA\u00111bK\u0005\u0003Y\t\u0011\u0011bU2bY\u0006$\u0016\u0010]3\t\u00119\u0002!\u0011#Q\u0001\n)\n\u0001\"\u001a:bgV\u0014X\r\t\u0005\ta\u0001\u0011)\u001a!C\u0001c\u0005I1m\\7qC:LwN\\\u000b\u0002eA\u0019qbM\u001b\n\u0005Q\u0002\"AB(qi&|g\u000e\u0005\u0002\fm%\u0011qG\u0001\u0002\u0014'&tw\r\\3u_:$Um]2sSB$xN\u001d\u0005\ts\u0001\u0011\t\u0012)A\u0005e\u0005Q1m\\7qC:LwN\u001c\u0011\t\u0011m\u0002!Q3A\u0005\u0002q\nAbY8ogR\u0014Xo\u0019;peN,\u0012!\u0010\t\u0004}\u0019KeBA E\u001d\t\u00015)D\u0001B\u0015\t\u0011\u0005\"\u0001\u0004=e>|GOP\u0005\u0002#%\u0011Q\tE\u0001\ba\u0006\u001c7.Y4f\u0013\t9\u0005JA\u0002TKFT!!\u0012\t\u0011\u0005-Q\u0015BA&\u0003\u0005U\u0019uN\\:ueV\u001cGo\u001c:EKN\u001c'/\u001b9u_JD\u0001\"\u0014\u0001\u0003\u0012\u0003\u0006I!P\u0001\u000eG>t7\u000f\u001e:vGR|'o\u001d\u0011\t\u0011=\u0003!Q3A\u0005\u0002A\u000b!\u0002\u001d:pa\u0016\u0014H/[3t+\u0005\t\u0006c\u0001 G%B\u00111bU\u0005\u0003)\n\u0011!\u0003\u0015:pa\u0016\u0014H/\u001f#fg\u000e\u0014\u0018\u000e\u001d;pe\"Aa\u000b\u0001B\tB\u0003%\u0011+A\u0006qe>\u0004XM\u001d;jKN\u0004\u0003\"\u0002-\u0001\t\u0003I\u0016A\u0002\u001fj]&$h\bF\u0004[7rkfl\u00181\u0011\u0005-\u0001\u0001\"\u0002\rX\u0001\u0004Q\u0002\"\u0002\u0013X\u0001\u0004Q\u0002\"\u0002\u0015X\u0001\u0004Q\u0003\"\u0002\u0019X\u0001\u0004\u0011\u0004\"B\u001eX\u0001\u0004i\u0004\"B(X\u0001\u0004\t\u0006\"\u00022\u0001\t\u0003\u0019\u0017\u0001\u00042fgRl\u0015\r^2iS:<GC\u00013f!\ry1'\u0013\u0005\u0006M\u0006\u0004\raZ\u0001\tCJ<g*Y7fgB\u0019a\b\u001b\u000e\n\u0005%D%\u0001\u0002'jgRDaa\u001b\u0001!B\u0013a\u0017AE0n_N$8i\\7qe\u0016DWM\\:jm\u0016\u00042A\u0010$n!\tYa.\u0003\u0002p\u0005\tQ2i\u001c8tiJ,8\r^8s!\u0006\u0014\u0018-\u001c#fg\u000e\u0014\u0018\u000e\u001d;pe\")\u0011\u000f\u0001C\u0001e\u0006\tRn\\:u\u0007>l\u0007O]3iK:\u001c\u0018N^3\u0016\u00031Dq\u0001\u001e\u0001\u0002\u0002\u0013\u0005Q/\u0001\u0003d_BLHc\u0002.wobL(p\u001f\u0005\b1M\u0004\n\u00111\u0001\u001b\u0011\u001d!3\u000f%AA\u0002iAq\u0001K:\u0011\u0002\u0003\u0007!\u0006C\u00041gB\u0005\t\u0019\u0001\u001a\t\u000fm\u001a\b\u0013!a\u0001{!9qj\u001dI\u0001\u0002\u0004\t\u0006bB?\u0001#\u0003%\tA`\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00132+\u0005y(f\u0001\u000e\u0002\u0002-\u0012\u00111\u0001\t\u0005\u0003\u000b\ty!\u0004\u0002\u0002\b)!\u0011\u0011BA\u0006\u0003%)hn\u00195fG.,GMC\u0002\u0002\u000eA\t!\"\u00198o_R\fG/[8o\u0013\u0011\t\t\"a\u0002\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\r\u0003\u0005\u0002\u0016\u0001\t\n\u0011\"\u0001\u007f\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIIB\u0011\"!\u0007\u0001#\u0003%\t!a\u0007\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%gU\u0011\u0011Q\u0004\u0016\u0004U\u0005\u0005\u0001\"CA\u0011\u0001E\u0005I\u0011AA\u0012\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIQ*\"!!\n+\u0007I\n\t\u0001C\u0005\u0002*\u0001\t\n\u0011\"\u0001\u0002,\u0005q1m\u001c9zI\u0011,g-Y;mi\u0012*TCAA\u0017U\ri\u0014\u0011\u0001\u0005\n\u0003c\u0001\u0011\u0013!C\u0001\u0003g\tabY8qs\u0012\"WMZ1vYR$c'\u0006\u0002\u00026)\u001a\u0011+!\u0001\t\u0013\u0005e\u0002!!A\u0005B\u0005m\u0012!\u00049s_\u0012,8\r\u001e)sK\u001aL\u00070\u0006\u0002\u0002>A!\u0011qHA%\u001b\t\t\tE\u0003\u0003\u0002D\u0005\u0015\u0013\u0001\u00027b]\u001eT!!a\u0012\u0002\t)\fg/Y\u0005\u0004?\u0005\u0005\u0003\"CA'\u0001\u0005\u0005I\u0011AA(\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\t\t\t\u0006E\u0002\u0010\u0003'J1!!\u0016\u0011\u0005\rIe\u000e\u001e\u0005\n\u00033\u0002\u0011\u0011!C\u0001\u00037\na\u0002\u001d:pIV\u001cG/\u00127f[\u0016tG\u000f\u0006\u0003\u0002^\u0005\r\u0004cA\b\u0002`%\u0019\u0011\u0011\r\t\u0003\u0007\u0005s\u0017\u0010\u0003\u0006\u0002f\u0005]\u0013\u0011!a\u0001\u0003#\n1\u0001\u001f\u00132\u0011%\tI\u0007AA\u0001\n\u0003\nY'A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\t\ti\u0007\u0005\u0004\u0002p\u0005U\u0014QL\u0007\u0003\u0003cR1!a\u001d\u0011\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0005\u0003o\n\tH\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0011%\tY\bAA\u0001\n\u0003\ti(\u0001\u0005dC:,\u0015/^1m)\u0011\ty(!\"\u0011\u0007=\t\t)C\u0002\u0002\u0004B\u0011qAQ8pY\u0016\fg\u000e\u0003\u0006\u0002f\u0005e\u0014\u0011!a\u0001\u0003;B\u0011\"!#\u0001\u0003\u0003%\t%a#\u0002\u0011!\f7\u000f[\"pI\u0016$\"!!\u0015\t\u0013\u0005=\u0005!!A\u0005B\u0005E\u0015\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0005\u0005u\u0002\"CAK\u0001\u0005\u0005I\u0011IAL\u0003\u0019)\u0017/^1mgR!\u0011qPAM\u0011)\t)'a%\u0002\u0002\u0003\u0007\u0011QL\u0004\n\u0003;\u0013\u0011\u0011!E\u0001\u0003?\u000bqb\u00117bgN$Um]2sSB$xN\u001d\t\u0004\u0017\u0005\u0005f\u0001C\u0001\u0003\u0003\u0003E\t!a)\u0014\u000b\u0005\u0005\u0016Q\u0015\u000b\u0011\u0017\u0005\u001d\u0016Q\u0016\u000e\u001bUIj\u0014KW\u0007\u0003\u0003SS1!a+\u0011\u0003\u001d\u0011XO\u001c;j[\u0016LA!a,\u0002*\n\t\u0012IY:ue\u0006\u001cGOR;oGRLwN\u001c\u001c\t\u000fa\u000b\t\u000b\"\u0001\u00024R\u0011\u0011q\u0014\u0005\u000b\u0003\u001f\u000b\t+!A\u0005F\u0005E\u0005BCA]\u0003C\u000b\t\u0011\"!\u0002<\u0006)\u0011\r\u001d9msRi!,!0\u0002@\u0006\u0005\u00171YAc\u0003\u000fDa\u0001GA\\\u0001\u0004Q\u0002B\u0002\u0013\u00028\u0002\u0007!\u0004\u0003\u0004)\u0003o\u0003\rA\u000b\u0005\u0007a\u0005]\u0006\u0019\u0001\u001a\t\rm\n9\f1\u0001>\u0011\u0019y\u0015q\u0017a\u0001#\"Q\u00111ZAQ\u0003\u0003%\t)!4\u0002\u000fUt\u0017\r\u001d9msR!\u0011qZAl!\u0011y1'!5\u0011\u0013=\t\u0019N\u0007\u000e+eu\n\u0016bAAk!\t1A+\u001e9mKZB\u0011\"!7\u0002J\u0006\u0005\t\u0019\u0001.\u0002\u0007a$\u0003\u0007\u0003\u0006\u0002^\u0006\u0005\u0016\u0011!C\u0005\u0003?\f1B]3bIJ+7o\u001c7wKR\u0011\u0011\u0011\u001d\t\u0005\u0003\u007f\t\u0019/\u0003\u0003\u0002f\u0006\u0005#AB(cU\u0016\u001cG\u000f")
public class ClassDescriptor extends ObjectDescriptor
{
    private final String simpleName;
    private final String fullName;
    private final ScalaType erasure;
    private final Option<SingletonDescriptor> companion;
    private final Seq<ConstructorDescriptor> constructors;
    private final Seq<PropertyDescriptor> properties;
    private Seq<ConstructorParamDescriptor> _mostComprehensive;
    
    public static Option<Tuple6<String, String, ScalaType, Option<SingletonDescriptor>, Seq<ConstructorDescriptor>, Seq<PropertyDescriptor>>> unapply(final ClassDescriptor x$0) {
        return ClassDescriptor$.MODULE$.unapply(x$0);
    }
    
    public static ClassDescriptor apply(final String simpleName, final String fullName, final ScalaType erasure, final Option<SingletonDescriptor> companion, final Seq<ConstructorDescriptor> constructors, final Seq<PropertyDescriptor> properties) {
        return ClassDescriptor$.MODULE$.apply(simpleName, fullName, erasure, companion, constructors, properties);
    }
    
    public static Function1<Tuple6<String, String, ScalaType, Option<SingletonDescriptor>, Seq<ConstructorDescriptor>, Seq<PropertyDescriptor>>, ClassDescriptor> tupled() {
        return (Function1<Tuple6<String, String, ScalaType, Option<SingletonDescriptor>, Seq<ConstructorDescriptor>, Seq<PropertyDescriptor>>, ClassDescriptor>)ClassDescriptor$.MODULE$.tupled();
    }
    
    public static Function1 curried() {
        return ClassDescriptor$.MODULE$.curried();
    }
    
    public String simpleName() {
        return this.simpleName;
    }
    
    public String fullName() {
        return this.fullName;
    }
    
    public ScalaType erasure() {
        return this.erasure;
    }
    
    public Option<SingletonDescriptor> companion() {
        return this.companion;
    }
    
    public Seq<ConstructorDescriptor> constructors() {
        return this.constructors;
    }
    
    public Seq<PropertyDescriptor> properties() {
        return this.properties;
    }
    
    public Option<ConstructorDescriptor> bestMatching(final List<String> argNames) {
        final Set names = (Set)Predef$.MODULE$.Set().apply((Seq)argNames);
        Object module$;
        if (this.constructors().isEmpty()) {
            module$ = None$.MODULE$;
        }
        else {
            final Tuple2 best = (Tuple2)((TraversableOnce)this.constructors().tail()).foldLeft((Object)new Tuple2(this.constructors().head(), (Object)BoxesRunTime.boxToInteger(this.org$json4s$reflect$ClassDescriptor$$score$1(((ConstructorDescriptor)this.constructors().head()).params().toList(), names))), (Function2)new ClassDescriptor$$anonfun.ClassDescriptor$$anonfun$1(this, names));
            module$ = new Some(best._1());
        }
        return (Option<ConstructorDescriptor>)module$;
    }
    
    public Seq<ConstructorParamDescriptor> mostComprehensive() {
        if (this._mostComprehensive == null) {
            Object module$;
            if (this.constructors().nonEmpty()) {
                final Seq primaryCtors = (Seq)this.constructors().filter((Function1)new ClassDescriptor$$anonfun.ClassDescriptor$$anonfun$2(this));
                if (primaryCtors.length() > 1) {
                    throw new IllegalArgumentException(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Two constructors annotated with PrimaryConstructor in `", "`" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.fullName() })));
                }
                module$ = primaryCtors.headOption().orElse((Function0)new ClassDescriptor$$anonfun$mostComprehensive.ClassDescriptor$$anonfun$mostComprehensive$1(this)).map((Function1)new ClassDescriptor$$anonfun$mostComprehensive.ClassDescriptor$$anonfun$mostComprehensive$2(this)).getOrElse((Function0)new ClassDescriptor$$anonfun$mostComprehensive.ClassDescriptor$$anonfun$mostComprehensive$3(this));
            }
            else {
                module$ = Nil$.MODULE$;
            }
            this._mostComprehensive = (Seq<ConstructorParamDescriptor>)module$;
        }
        return this._mostComprehensive;
    }
    
    public ClassDescriptor copy(final String simpleName, final String fullName, final ScalaType erasure, final Option<SingletonDescriptor> companion, final Seq<ConstructorDescriptor> constructors, final Seq<PropertyDescriptor> properties) {
        return new ClassDescriptor(simpleName, fullName, erasure, companion, constructors, properties);
    }
    
    public String copy$default$1() {
        return this.simpleName();
    }
    
    public String copy$default$2() {
        return this.fullName();
    }
    
    public ScalaType copy$default$3() {
        return this.erasure();
    }
    
    public Option<SingletonDescriptor> copy$default$4() {
        return this.companion();
    }
    
    public Seq<ConstructorDescriptor> copy$default$5() {
        return this.constructors();
    }
    
    public Seq<PropertyDescriptor> copy$default$6() {
        return this.properties();
    }
    
    @Override
    public String productPrefix() {
        return "ClassDescriptor";
    }
    
    public int productArity() {
        return 6;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 5: {
                o = this.properties();
                break;
            }
            case 4: {
                o = this.constructors();
                break;
            }
            case 3: {
                o = this.companion();
                break;
            }
            case 2: {
                o = this.erasure();
                break;
            }
            case 1: {
                o = this.fullName();
                break;
            }
            case 0: {
                o = this.simpleName();
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
        return x$1 instanceof ClassDescriptor;
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
            if (x$1 instanceof ClassDescriptor) {
                final ClassDescriptor classDescriptor = (ClassDescriptor)x$1;
                final String simpleName = this.simpleName();
                final String simpleName2 = classDescriptor.simpleName();
                boolean b = false;
                Label_0237: {
                    Label_0236: {
                        if (simpleName == null) {
                            if (simpleName2 != null) {
                                break Label_0236;
                            }
                        }
                        else if (!simpleName.equals(simpleName2)) {
                            break Label_0236;
                        }
                        final String fullName = this.fullName();
                        final String fullName2 = classDescriptor.fullName();
                        if (fullName == null) {
                            if (fullName2 != null) {
                                break Label_0236;
                            }
                        }
                        else if (!fullName.equals(fullName2)) {
                            break Label_0236;
                        }
                        final ScalaType erasure = this.erasure();
                        final ScalaType erasure2 = classDescriptor.erasure();
                        if (erasure == null) {
                            if (erasure2 != null) {
                                break Label_0236;
                            }
                        }
                        else if (!erasure.equals(erasure2)) {
                            break Label_0236;
                        }
                        final Option<SingletonDescriptor> companion = this.companion();
                        final Option<SingletonDescriptor> companion2 = classDescriptor.companion();
                        if (companion == null) {
                            if (companion2 != null) {
                                break Label_0236;
                            }
                        }
                        else if (!companion.equals(companion2)) {
                            break Label_0236;
                        }
                        final Seq<ConstructorDescriptor> constructors = this.constructors();
                        final Seq<ConstructorDescriptor> constructors2 = classDescriptor.constructors();
                        if (constructors == null) {
                            if (constructors2 != null) {
                                break Label_0236;
                            }
                        }
                        else if (!constructors.equals(constructors2)) {
                            break Label_0236;
                        }
                        final Seq<PropertyDescriptor> properties = this.properties();
                        final Seq<PropertyDescriptor> properties2 = classDescriptor.properties();
                        if (properties == null) {
                            if (properties2 != null) {
                                break Label_0236;
                            }
                        }
                        else if (!properties.equals(properties2)) {
                            break Label_0236;
                        }
                        if (classDescriptor.canEqual(this)) {
                            b = true;
                            break Label_0237;
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
    
    public final int org$json4s$reflect$ClassDescriptor$$countOptionals$1(final List args) {
        return args.count((Function1)new ClassDescriptor$$anonfun$org$json4s$reflect$ClassDescriptor$$countOptionals$1.ClassDescriptor$$anonfun$org$json4s$reflect$ClassDescriptor$$countOptionals$1$1(this));
    }
    
    public final int org$json4s$reflect$ClassDescriptor$$countDefaults$1(final List args) {
        return args.count((Function1)new ClassDescriptor$$anonfun$org$json4s$reflect$ClassDescriptor$$countDefaults$1.ClassDescriptor$$anonfun$org$json4s$reflect$ClassDescriptor$$countDefaults$1$1(this));
    }
    
    public final int org$json4s$reflect$ClassDescriptor$$score$1(final List args, final Set names$1) {
        return BoxesRunTime.unboxToInt(args.foldLeft((Object)BoxesRunTime.boxToInteger(0), (Function2)new ClassDescriptor$$anonfun$org$json4s$reflect$ClassDescriptor$$score$1.ClassDescriptor$$anonfun$org$json4s$reflect$ClassDescriptor$$score$1$1(this, names$1)));
    }
    
    public ClassDescriptor(final String simpleName, final String fullName, final ScalaType erasure, final Option<SingletonDescriptor> companion, final Seq<ConstructorDescriptor> constructors, final Seq<PropertyDescriptor> properties) {
        this.simpleName = simpleName;
        this.fullName = fullName;
        this.erasure = erasure;
        this.companion = companion;
        this.constructors = constructors;
        this.properties = properties;
        this._mostComprehensive = null;
    }
}
