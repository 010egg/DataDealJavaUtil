// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Product$class;
import scala.Function0;
import scala.util.control.Exception$;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.collection.Seq;
import scala.collection.immutable.Nil$;
import scala.Predef$;
import scala.Function2;
import scala.collection.immutable.List$;
import scala.Function1;
import scala.collection.SeqLike;
import scala.collection.IterableLike;
import scala.runtime.BoxedUnit;
import scala.Product;
import scala.Some;
import scala.None$;
import scala.Serializable;
import scala.runtime.AbstractFunction1;
import scala.Tuple2;
import scala.PartialFunction;
import scala.Option;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\tmgaB\u0001\u0003!\u0003\r\ta\u0002\u0002\n)f\u0004X\rS5oiNT!a\u0001\u0003\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005)\u0011aA8sO\u000e\u00011C\u0001\u0001\t!\tIA\"D\u0001\u000b\u0015\u0005Y\u0011!B:dC2\f\u0017BA\u0007\u000b\u0005\u0019\te.\u001f*fM\")q\u0002\u0001C\u0001!\u00051A%\u001b8ji\u0012\"\u0012!\u0005\t\u0003\u0013II!a\u0005\u0006\u0003\tUs\u0017\u000e\u001e\u0005\b+\u0001\u0011\rQ\"\u0001\u0017\u0003\u0015A\u0017N\u001c;t+\u00059\u0002c\u0001\r!G9\u0011\u0011D\b\b\u00035ui\u0011a\u0007\u0006\u00039\u0019\ta\u0001\u0010:p_Rt\u0014\"A\u0006\n\u0005}Q\u0011a\u00029bG.\fw-Z\u0005\u0003C\t\u0012A\u0001T5ti*\u0011qD\u0003\u0019\u0003I5\u00022!\n\u0015,\u001d\tIa%\u0003\u0002(\u0015\u00051\u0001K]3eK\u001aL!!\u000b\u0016\u0003\u000b\rc\u0017m]:\u000b\u0005\u001dR\u0001C\u0001\u0017.\u0019\u0001!\u0011B\f\u000b\u0002\u0002\u0003\u0005)\u0011A\u0018\u0003\t}##GN\t\u0003aM\u0002\"!C\u0019\n\u0005IR!a\u0002(pi\"Lgn\u001a\t\u0003\u0013QJ!!\u000e\u0006\u0003\u0007\u0005s\u0017\u0010C\u00038\u0001\u0019\u0005\u0001(A\u0004iS:$hi\u001c:\u0015\u0005eb\u0004CA\u0013;\u0013\tY$F\u0001\u0004TiJLgn\u001a\u0005\u0006{Y\u0002\rAP\u0001\u0006G2\f'P\u001f\u0019\u0003\u007f\u0005\u00032!\n\u0015A!\ta\u0013\tB\u0005Cy\u0005\u0005\t\u0011!B\u0001_\t!q\f\n\u001a8\u0011\u0015!\u0005A\"\u0001F\u0003!\u0019G.Y:t\r>\u0014HC\u0001$O!\rIq)S\u0005\u0003\u0011*\u0011aa\u00149uS>t\u0007G\u0001&M!\r)\u0003f\u0013\t\u0003Y1#\u0011\"T\"\u0002\u0002\u0003\u0005)\u0011A\u0018\u0003\t}##\u0007\u000f\u0005\u0006\u001f\u000e\u0003\r!O\u0001\u0005Q&tG\u000fC\u0003R\u0001\u0011\u0005!+\u0001\u0007d_:$\u0018-\u001b8t\u0011&tG\u000f\u0006\u0002T-B\u0011\u0011\u0002V\u0005\u0003+*\u0011qAQ8pY\u0016\fg\u000eC\u0003>!\u0002\u0007q\u000b\r\u0002Y5B\u0019Q\u0005K-\u0011\u00051RF!C.W\u0003\u0003\u0005\tQ!\u00010\u0005\u0011yFEM\u001d\t\u000bu\u0003A\u0011\u00010\u0002%MDw.\u001e7e\u000bb$(/Y2u\u0011&tGo\u001d\u000b\u0003'~CQ!\u0010/A\u0002\u0001\u0004$!Y2\u0011\u0007\u0015B#\r\u0005\u0002-G\u0012IAmXA\u0001\u0002\u0003\u0015\ta\f\u0002\u0005?\u0012\u001a\u0004\u0007C\u0003g\u0001\u0011\u0005q-A\u0006eKN,'/[1mSj,W#\u00015\u0011\t%I7nM\u0005\u0003U*\u0011q\u0002U1si&\fGNR;oGRLwN\u001c\t\u0005\u00131Ld.\u0003\u0002n\u0015\t1A+\u001e9mKJ\u0002\"a\u001c:\u000f\u0005A\fX\"\u0001\u0002\n\u0005}\u0011\u0011BA:u\u0005\u001dQuJ\u00196fGRT!a\b\u0002\t\u000bY\u0004A\u0011A<\u0002\u0013M,'/[1mSj,W#\u0001=\u0011\t%I7G\u001c\u0005\u0006u\u0002!\ta_\u0001\u000bG>l\u0007o\u001c8f]R\u001cX#\u0001?\u0011\u0007a\u0001S\u0010\u0005\u0002q\u0001!1q\u0010\u0001C\u0001\u0003\u0003\tQ\u0001\n9mkN$2!`A\u0002\u0011\u0015)b\u00101\u0001~\r\u001d\t9\u0001\u0001!\u0001\u0003\u0013\u0011!cQ8na>\u001c\u0018\u000e^3UsB,\u0007*\u001b8ugNA\u0011Q\u0001\u0005~\u0003\u0017\t\t\u0002E\u0002\n\u0003\u001bI1!a\u0004\u000b\u0005\u001d\u0001&o\u001c3vGR\u00042!CA\n\u0013\r\t)B\u0003\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\nu\u0006\u0015!Q3A\u0005BmD!\"a\u0007\u0002\u0006\tE\t\u0015!\u0003}\u0003-\u0019w.\u001c9p]\u0016tGo\u001d\u0011\t\u0011\u0005}\u0011Q\u0001C\u0001\u0003C\ta\u0001P5oSRtD\u0003BA\u0012\u0003O\u0001B!!\n\u0002\u00065\t\u0001\u0001\u0003\u0004{\u0003;\u0001\r\u0001 \u0005\n+\u0005\u0015!\u0019!C\u0001\u0003W)\"!!\f\u0011\ta\u0001\u0013q\u0006\u0019\u0005\u0003c\t)\u0004\u0005\u0003&Q\u0005M\u0002c\u0001\u0017\u00026\u0011Y\u0011qGA\u001d\u0003\u0003\u0005\tQ!\u00010\u0005\u0011yFeM\u0019\t\u0013\u0005m\u0012Q\u0001Q\u0001\n\u0005u\u0012A\u00025j]R\u001c\b\u0005\u0005\u0003\u0019A\u0005}\u0002\u0007BA!\u0003\u000b\u0002B!\n\u0015\u0002DA\u0019A&!\u0012\u0005\u0017\u0005]\u0012\u0011HA\u0001\u0002\u0003\u0015\ta\f\u0005\bo\u0005\u0015A\u0011AA%)\rI\u00141\n\u0005\b{\u0005\u001d\u0003\u0019AA'a\u0011\ty%a\u0015\u0011\t\u0015B\u0013\u0011\u000b\t\u0004Y\u0005MCaCA+\u0003\u0017\n\t\u0011!A\u0003\u0002=\u0012Aa\u0018\u00134e!9A)!\u0002\u0005\u0002\u0005eC\u0003BA.\u0003O\u0002B!C$\u0002^A\"\u0011qLA2!\u0011)\u0003&!\u0019\u0011\u00071\n\u0019\u0007B\u0006\u0002f\u0005]\u0013\u0011!A\u0001\u0006\u0003y#\u0001B0%gMBaaTA,\u0001\u0004I\u0004B\u00024\u0002\u0006\u0011\u0005s\r\u0003\u0004w\u0003\u000b!\te\u001e\u0005\u000b\u0003_\n)!!A\u0005\u0002\u0005E\u0014\u0001B2paf$B!a\t\u0002t!A!0!\u001c\u0011\u0002\u0003\u0007A\u0010\u0003\u0006\u0002x\u0005\u0015\u0011\u0013!C\u0001\u0003s\nabY8qs\u0012\"WMZ1vYR$\u0013'\u0006\u0002\u0002|)\u001aA0! ,\u0005\u0005}\u0004\u0003BAA\u0003\u0017k!!a!\u000b\t\u0005\u0015\u0015qQ\u0001\nk:\u001c\u0007.Z2lK\u0012T1!!#\u000b\u0003)\tgN\\8uCRLwN\\\u0005\u0005\u0003\u001b\u000b\u0019IA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016D!\"!%\u0002\u0006\u0005\u0005I\u0011IAJ\u00035\u0001(o\u001c3vGR\u0004&/\u001a4jqV\u0011\u0011Q\u0013\t\u0005\u0003/\u000b\t+\u0004\u0002\u0002\u001a*!\u00111TAO\u0003\u0011a\u0017M\\4\u000b\u0005\u0005}\u0015\u0001\u00026bm\u0006L1aOAM\u0011)\t)+!\u0002\u0002\u0002\u0013\u0005\u0011qU\u0001\raJ|G-^2u\u0003JLG/_\u000b\u0003\u0003S\u00032!CAV\u0013\r\tiK\u0003\u0002\u0004\u0013:$\bBCAY\u0003\u000b\t\t\u0011\"\u0001\u00024\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HcA\u001a\u00026\"Q\u0011qWAX\u0003\u0003\u0005\r!!+\u0002\u0007a$\u0013\u0007\u0003\u0006\u0002<\u0006\u0015\u0011\u0011!C!\u0003{\u000bq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0003\u0003\u007f\u0003R!!1\u0002HNj!!a1\u000b\u0007\u0005\u0015'\"\u0001\u0006d_2dWm\u0019;j_:LA!!3\u0002D\nA\u0011\n^3sCR|'\u000f\u0003\u0006\u0002N\u0006\u0015\u0011\u0011!C\u0001\u0003\u001f\f\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0004'\u0006E\u0007\"CA\\\u0003\u0017\f\t\u00111\u00014\u0011)\t).!\u0002\u0002\u0002\u0013\u0005\u0013q[\u0001\tQ\u0006\u001c\bnQ8eKR\u0011\u0011\u0011\u0016\u0005\u000b\u00037\f)!!A\u0005B\u0005u\u0017\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0005\u0005U\u0005BCAq\u0003\u000b\t\t\u0011\"\u0011\u0002d\u00061Q-];bYN$2aUAs\u0011%\t9,a8\u0002\u0002\u0003\u00071\u0007\u000b\u0005\u0002\u0006\u0005%\u0018q^Az!\rI\u00111^\u0005\u0004\u0003[T!A\u00033faJ,7-\u0019;fI\u0006\u0012\u0011\u0011_\u0001Co&dG\u000e\t2fAI,Wn\u001c<fI\u0002\"W/\u001a\u0011u_\u0002BG\u000f\u001e9tu=zs-\u001b;ik\nt3m\\70UN|g\u000eN:0UN|g\u000eN:0SN\u001cX/Z:0kU\u0002\u0014EAA{\u0003\u0015\u0019dF\u000e\u00188\u000f1\tI\u0010\u0001B\u0001\u0004\u0003E\t\u0001AA~\u0003!z'o\u001a\u0013kg>tGg\u001d\u0013UsB,\u0007*\u001b8ug\u0012\"3i\\7q_NLG/\u001a+za\u0016D\u0015N\u001c;t!\u0011\t)#!@\u0007\u0015\u0005\u001d\u0001!!A\t\u0002\u0001\typ\u0005\u0004\u0002~\n\u0005\u0011\u0011\u0003\t\b\u0005\u0007\u0011I\u0001`A\u0012\u001b\t\u0011)AC\u0002\u0003\b)\tqA];oi&lW-\u0003\u0003\u0003\f\t\u0015!!E!cgR\u0014\u0018m\u0019;Gk:\u001cG/[8oc!A\u0011qDA\u007f\t\u0003\u0011y\u0001\u0006\u0002\u0002|\"Q\u00111\\A\u007f\u0003\u0003%)%!8\t\u0015\tU\u0011Q`A\u0001\n\u0003\u00139\"A\u0003baBd\u0017\u0010\u0006\u0003\u0002$\te\u0001B\u0002>\u0003\u0014\u0001\u0007A\u0010\u0003\u0006\u0003\u001e\u0005u\u0018\u0011!CA\u0005?\tq!\u001e8baBd\u0017\u0010\u0006\u0003\u0003\"\t\r\u0002cA\u0005Hy\"Q!Q\u0005B\u000e\u0003\u0003\u0005\r!a\t\u0002\u0007a$\u0003\u0007\u000b\u0005\u0002~\u0006%\u0018q^Az\u000f!\u0011YC\u0001E\u0001\u0005\t5\u0012!\u0003+za\u0016D\u0015N\u001c;t!\r\u0001(q\u0006\u0004\b\u0003\tA\tA\u0001B\u0019'\r\u0011y\u0003\u0003\u0005\t\u0003?\u0011y\u0003\"\u0001\u00036Q\u0011!Q\u0006\u0004\b\u0005s\u0011y\u0003\u0012B\u001e\u0005M\u0019u.\u001c9pg&$X\rV=qK\"Kg\u000e^:3'!\u00119\u0004C?\u0002\f\u0005E\u0001\"\u0003>\u00038\tU\r\u0011\"\u0011|\u0011)\tYBa\u000e\u0003\u0012\u0003\u0006I\u0001 \u0005\t\u0003?\u00119\u0004\"\u0001\u0003DQ!!Q\tB%!\u0011\u00119Ea\u000e\u000e\u0005\t=\u0002B\u0002>\u0003B\u0001\u0007A\u0010C\u0005\u0016\u0005o\u0011\r\u0011\"\u0001\u0003NU\u0011!q\n\t\u00051\u0001\u0012\t\u0006\r\u0003\u0003T\t]\u0003\u0003B\u0013)\u0005+\u00022\u0001\fB,\t-\u0011IFa\u0017\u0002\u0002\u0003\u0005)\u0011A\u0018\u0003\t}#3\u0007\u000e\u0005\n\u0003w\u00119\u0004)A\u0005\u0005;\u0002B\u0001\u0007\u0011\u0003`A\"!\u0011\rB3!\u0011)\u0003Fa\u0019\u0011\u00071\u0012)\u0007B\u0006\u0003Z\tm\u0013\u0011!A\u0001\u0006\u0003y\u0003bB\u001c\u00038\u0011\u0005!\u0011\u000e\u000b\u0004s\t-\u0004bB\u001f\u0003h\u0001\u0007!Q\u000e\u0019\u0005\u0005_\u0012\u0019\b\u0005\u0003&Q\tE\u0004c\u0001\u0017\u0003t\u0011Y!Q\u000fB6\u0003\u0003\u0005\tQ!\u00010\u0005\u0011yFeM\u001b\t\u000f\u0011\u00139\u0004\"\u0001\u0003zQ!!1\u0010BD!\u0011IqI! 1\t\t}$1\u0011\t\u0005K!\u0012\t\tE\u0002-\u0005\u0007#1B!\"\u0003x\u0005\u0005\t\u0011!B\u0001_\t!q\fJ\u001a7\u0011\u0019y%q\u000fa\u0001s!1aMa\u000e\u0005B\u001dDaA\u001eB\u001c\t\u0003:\bBCA8\u0005o\t\t\u0011\"\u0001\u0003\u0010R!!Q\tBI\u0011!Q(Q\u0012I\u0001\u0002\u0004a\bBCA<\u0005o\t\n\u0011\"\u0001\u0002z!Q\u0011\u0011\u0013B\u001c\u0003\u0003%\t%a%\t\u0015\u0005\u0015&qGA\u0001\n\u0003\t9\u000b\u0003\u0006\u00022\n]\u0012\u0011!C\u0001\u00057#2a\rBO\u0011)\t9L!'\u0002\u0002\u0003\u0007\u0011\u0011\u0016\u0005\u000b\u0003w\u00139$!A\u0005B\u0005u\u0006BCAg\u0005o\t\t\u0011\"\u0001\u0003$R\u00191K!*\t\u0013\u0005]&\u0011UA\u0001\u0002\u0004\u0019\u0004BCAk\u0005o\t\t\u0011\"\u0011\u0002X\"Q\u00111\u001cB\u001c\u0003\u0003%\t%!8\t\u0015\u0005\u0005(qGA\u0001\n\u0003\u0012i\u000bF\u0002T\u0005_C\u0011\"a.\u0003,\u0006\u0005\t\u0019A\u001a\b\u0015\tM&qFA\u0001\u0012\u0013\u0011),A\nD_6\u0004xn]5uKRK\b/\u001a%j]R\u001c(\u0007\u0005\u0003\u0003H\t]fA\u0003B\u001d\u0005_\t\t\u0011#\u0003\u0003:N1!q\u0017B^\u0003#\u0001rAa\u0001\u0003\nq\u0014)\u0005\u0003\u0005\u0002 \t]F\u0011\u0001B`)\t\u0011)\f\u0003\u0006\u0002\\\n]\u0016\u0011!C#\u0003;D!B!\u0006\u00038\u0006\u0005I\u0011\u0011Bc)\u0011\u0011)Ea2\t\ri\u0014\u0019\r1\u0001}\u0011)\u0011iBa.\u0002\u0002\u0013\u0005%1\u001a\u000b\u0005\u0005C\u0011i\r\u0003\u0006\u0003&\t%\u0017\u0011!a\u0001\u0005\u000bB!B!5\u00038\u0006\u0005I\u0011\u0002Bj\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\tU\u0007\u0003BAL\u0005/LAA!7\u0002\u001a\n1qJ\u00196fGR\u0004")
public interface TypeHints
{
    List<Class<?>> hints();
    
    String hintFor(final Class<?> p0);
    
    Option<Class<?>> classFor(final String p0);
    
    boolean containsHint(final Class<?> p0);
    
    boolean shouldExtractHints(final Class<?> p0);
    
    PartialFunction<Tuple2<String, JsonAST.JObject>, Object> deserialize();
    
    PartialFunction<Object, JsonAST.JObject> serialize();
    
    List<TypeHints> components();
    
    TypeHints $plus(final TypeHints p0);
    
    CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints();
    
    public class CompositeTypeHints$ extends AbstractFunction1<List<TypeHints>, CompositeTypeHints> implements Serializable
    {
        public final String toString() {
            return "CompositeTypeHints";
        }
        
        public CompositeTypeHints apply(final List<TypeHints> components) {
            return new CompositeTypeHints(components);
        }
        
        public Option<List<TypeHints>> unapply(final CompositeTypeHints x$0) {
            return (Option<List<TypeHints>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.components()));
        }
        
        public CompositeTypeHints$() {
            if (TypeHints.this == null) {
                throw null;
            }
        }
    }
    
    public class CompositeTypeHints implements TypeHints, Product, Serializable
    {
        private final List<TypeHints> components;
        private final List<Class<?>> hints;
        private volatile CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints$module;
        
        private CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints$lzycompute() {
            synchronized (this) {
                if (this.org$json4s$TypeHints$$CompositeTypeHints$module == null) {
                    this.org$json4s$TypeHints$$CompositeTypeHints$module = new CompositeTypeHints$(this);
                }
                final BoxedUnit unit = BoxedUnit.UNIT;
                return this.org$json4s$TypeHints$$CompositeTypeHints$module;
            }
        }
        
        @Override
        public CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints() {
            return (this.org$json4s$TypeHints$$CompositeTypeHints$module == null) ? this.org$json4s$TypeHints$$CompositeTypeHints$lzycompute() : this.org$json4s$TypeHints$$CompositeTypeHints$module;
        }
        
        @Override
        public boolean containsHint(final Class<?> clazz) {
            return TypeHints$class.containsHint(this, clazz);
        }
        
        @Override
        public boolean shouldExtractHints(final Class<?> clazz) {
            return TypeHints$class.shouldExtractHints(this, clazz);
        }
        
        @Override
        public TypeHints $plus(final TypeHints hints) {
            return TypeHints$class.$plus(this, hints);
        }
        
        @Override
        public List<TypeHints> components() {
            return this.components;
        }
        
        @Override
        public List<Class<?>> hints() {
            return this.hints;
        }
        
        @Override
        public String hintFor(final Class<?> clazz) {
            return (String)((Tuple2)((IterableLike)((SeqLike)((List)this.components().reverse().filter((Function1)new TypeHints$CompositeTypeHints$$anonfun$hintFor.TypeHints$CompositeTypeHints$$anonfun$hintFor$1(this, (Class)clazz))).map((Function1)new TypeHints$CompositeTypeHints$$anonfun$hintFor.TypeHints$CompositeTypeHints$$anonfun$hintFor$2(this, (Class)clazz), List$.MODULE$.canBuildFrom())).sortWith((Function2)new TypeHints$CompositeTypeHints$$anonfun$hintFor.TypeHints$CompositeTypeHints$$anonfun$hintFor$3(this, (Class)clazz))).head())._1();
        }
        
        @Override
        public Option<Class<?>> classFor(final String hint) {
            return (Option<Class<?>>)this.components().find((Function1)new TypeHints$CompositeTypeHints$$anonfun$classFor.TypeHints$CompositeTypeHints$$anonfun$classFor$1(this, hint)).flatMap((Function1)new TypeHints$CompositeTypeHints$$anonfun$classFor.TypeHints$CompositeTypeHints$$anonfun$classFor$2(this, hint));
        }
        
        @Override
        public PartialFunction<Tuple2<String, JsonAST.JObject>, Object> deserialize() {
            return (PartialFunction<Tuple2<String, JsonAST.JObject>, Object>)this.components().foldLeft((Object)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$), (Function2)new TypeHints$CompositeTypeHints$$anonfun$deserialize.TypeHints$CompositeTypeHints$$anonfun$deserialize$3(this));
        }
        
        @Override
        public PartialFunction<Object, JsonAST.JObject> serialize() {
            return (PartialFunction<Object, JsonAST.JObject>)this.components().foldLeft((Object)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$), (Function2)new TypeHints$CompositeTypeHints$$anonfun$serialize.TypeHints$CompositeTypeHints$$anonfun$serialize$1(this));
        }
        
        public CompositeTypeHints copy(final List<TypeHints> components) {
            return this.org$json4s$TypeHints$CompositeTypeHints$$$outer().new CompositeTypeHints(components);
        }
        
        public List<TypeHints> copy$default$1() {
            return this.components();
        }
        
        public String productPrefix() {
            return "CompositeTypeHints";
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
                    return this.components();
                }
            }
        }
        
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof CompositeTypeHints;
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
                if (x$1 instanceof CompositeTypeHints) {
                    final CompositeTypeHints compositeTypeHints = (CompositeTypeHints)x$1;
                    final List<TypeHints> components = this.components();
                    final List<TypeHints> components2 = compositeTypeHints.components();
                    boolean b = false;
                    Label_0077: {
                        Label_0076: {
                            if (components == null) {
                                if (components2 != null) {
                                    break Label_0076;
                                }
                            }
                            else if (!components.equals(components2)) {
                                break Label_0076;
                            }
                            if (compositeTypeHints.canEqual(this)) {
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
        
        public /* synthetic */ TypeHints org$json4s$TypeHints$CompositeTypeHints$$$outer() {
            return TypeHints.this;
        }
        
        public final boolean org$json4s$TypeHints$CompositeTypeHints$$hasClass$1(final TypeHints h, final String hint$1) {
            return BoxesRunTime.unboxToBoolean(Exception$.MODULE$.allCatch().opt((Function0)new TypeHints$CompositeTypeHints$$anonfun$org$json4s$TypeHints$CompositeTypeHints$$hasClass$1.TypeHints$CompositeTypeHints$$anonfun$org$json4s$TypeHints$CompositeTypeHints$$hasClass$1$2(this, hint$1, h)).map((Function1)new TypeHints$CompositeTypeHints$$anonfun$org$json4s$TypeHints$CompositeTypeHints$$hasClass$1.TypeHints$CompositeTypeHints$$anonfun$org$json4s$TypeHints$CompositeTypeHints$$hasClass$1$3(this)).getOrElse((Function0)new TypeHints$CompositeTypeHints$$anonfun$org$json4s$TypeHints$CompositeTypeHints$$hasClass$1.TypeHints$CompositeTypeHints$$anonfun$org$json4s$TypeHints$CompositeTypeHints$$hasClass$1$1(this)));
        }
        
        public CompositeTypeHints(final List<TypeHints> components) {
            this.components = components;
            if (TypeHints.this == null) {
                throw null;
            }
            TypeHints$class.$init$(this);
            Product$class.$init$((Product)this);
            this.hints = (List<Class<?>>)components.flatMap((Function1)new TypeHints$CompositeTypeHints$$anonfun.TypeHints$CompositeTypeHints$$anonfun$5(this), List$.MODULE$.canBuildFrom());
        }
    }
    
    public static class CompositeTypeHints2 implements TypeHints, Product, Serializable
    {
        private final List<TypeHints> components;
        private final List<Class<?>> hints;
        private volatile CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints$module;
        
        private CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints$lzycompute() {
            synchronized (this) {
                if (this.org$json4s$TypeHints$$CompositeTypeHints$module == null) {
                    this.org$json4s$TypeHints$$CompositeTypeHints$module = new CompositeTypeHints$(this);
                }
                final BoxedUnit unit = BoxedUnit.UNIT;
                return this.org$json4s$TypeHints$$CompositeTypeHints$module;
            }
        }
        
        @Override
        public CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints() {
            return (this.org$json4s$TypeHints$$CompositeTypeHints$module == null) ? this.org$json4s$TypeHints$$CompositeTypeHints$lzycompute() : this.org$json4s$TypeHints$$CompositeTypeHints$module;
        }
        
        @Override
        public boolean containsHint(final Class<?> clazz) {
            return TypeHints$class.containsHint(this, clazz);
        }
        
        @Override
        public boolean shouldExtractHints(final Class<?> clazz) {
            return TypeHints$class.shouldExtractHints(this, clazz);
        }
        
        @Override
        public TypeHints $plus(final TypeHints hints) {
            return TypeHints$class.$plus(this, hints);
        }
        
        @Override
        public List<TypeHints> components() {
            return this.components;
        }
        
        @Override
        public List<Class<?>> hints() {
            return this.hints;
        }
        
        @Override
        public String hintFor(final Class<?> clazz) {
            return (String)((Tuple2)((IterableLike)((SeqLike)((List)this.components().reverse().filter((Function1)new TypeHints$CompositeTypeHints2$$anonfun$hintFor.TypeHints$CompositeTypeHints2$$anonfun$hintFor$4(this, (Class)clazz))).map((Function1)new TypeHints$CompositeTypeHints2$$anonfun$hintFor.TypeHints$CompositeTypeHints2$$anonfun$hintFor$5(this, (Class)clazz), List$.MODULE$.canBuildFrom())).sortWith((Function2)new TypeHints$CompositeTypeHints2$$anonfun$hintFor.TypeHints$CompositeTypeHints2$$anonfun$hintFor$6(this, (Class)clazz))).head())._1();
        }
        
        @Override
        public Option<Class<?>> classFor(final String hint) {
            return (Option<Class<?>>)this.components().find((Function1)new TypeHints$CompositeTypeHints2$$anonfun$classFor.TypeHints$CompositeTypeHints2$$anonfun$classFor$3(this, hint)).flatMap((Function1)new TypeHints$CompositeTypeHints2$$anonfun$classFor.TypeHints$CompositeTypeHints2$$anonfun$classFor$4(this, hint));
        }
        
        @Override
        public PartialFunction<Tuple2<String, JsonAST.JObject>, Object> deserialize() {
            return (PartialFunction<Tuple2<String, JsonAST.JObject>, Object>)this.components().foldLeft((Object)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$), (Function2)new TypeHints$CompositeTypeHints2$$anonfun$deserialize.TypeHints$CompositeTypeHints2$$anonfun$deserialize$4(this));
        }
        
        @Override
        public PartialFunction<Object, JsonAST.JObject> serialize() {
            return (PartialFunction<Object, JsonAST.JObject>)this.components().foldLeft((Object)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$), (Function2)new TypeHints$CompositeTypeHints2$$anonfun$serialize.TypeHints$CompositeTypeHints2$$anonfun$serialize$2(this));
        }
        
        public CompositeTypeHints2 copy(final List<TypeHints> components) {
            return new CompositeTypeHints2(components);
        }
        
        public List<TypeHints> copy$default$1() {
            return this.components();
        }
        
        public String productPrefix() {
            return "CompositeTypeHints2";
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
                    return this.components();
                }
            }
        }
        
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof CompositeTypeHints2;
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
                if (x$1 instanceof CompositeTypeHints2) {
                    final CompositeTypeHints2 compositeTypeHints2 = (CompositeTypeHints2)x$1;
                    final List<TypeHints> components = this.components();
                    final List<TypeHints> components2 = compositeTypeHints2.components();
                    boolean b = false;
                    Label_0077: {
                        Label_0076: {
                            if (components == null) {
                                if (components2 != null) {
                                    break Label_0076;
                                }
                            }
                            else if (!components.equals(components2)) {
                                break Label_0076;
                            }
                            if (compositeTypeHints2.canEqual(this)) {
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
        
        public final boolean org$json4s$TypeHints$CompositeTypeHints2$$hasClass$2(final TypeHints h, final String hint$2) {
            return BoxesRunTime.unboxToBoolean(Exception$.MODULE$.allCatch().opt((Function0)new TypeHints$CompositeTypeHints2$$anonfun$org$json4s$TypeHints$CompositeTypeHints2$$hasClass$2.TypeHints$CompositeTypeHints2$$anonfun$org$json4s$TypeHints$CompositeTypeHints2$$hasClass$2$2(this, hint$2, h)).map((Function1)new TypeHints$CompositeTypeHints2$$anonfun$org$json4s$TypeHints$CompositeTypeHints2$$hasClass$2.TypeHints$CompositeTypeHints2$$anonfun$org$json4s$TypeHints$CompositeTypeHints2$$hasClass$2$3(this)).getOrElse((Function0)new TypeHints$CompositeTypeHints2$$anonfun$org$json4s$TypeHints$CompositeTypeHints2$$hasClass$2.TypeHints$CompositeTypeHints2$$anonfun$org$json4s$TypeHints$CompositeTypeHints2$$hasClass$2$1(this)));
        }
        
        public CompositeTypeHints2(final List<TypeHints> components) {
            this.components = components;
            TypeHints$class.$init$(this);
            Product$class.$init$((Product)this);
            this.hints = (List<Class<?>>)components.flatMap((Function1)new TypeHints$CompositeTypeHints2$$anonfun.TypeHints$CompositeTypeHints2$$anonfun$6(this), List$.MODULE$.canBuildFrom());
        }
    }
    
    public static class CompositeTypeHints2$ extends AbstractFunction1<List<TypeHints>, CompositeTypeHints2> implements Serializable
    {
        public static final CompositeTypeHints2$ MODULE$;
        
        static {
            new CompositeTypeHints2$();
        }
        
        public final String toString() {
            return "CompositeTypeHints2";
        }
        
        public CompositeTypeHints2 apply(final List<TypeHints> components) {
            return new CompositeTypeHints2(components);
        }
        
        public Option<List<TypeHints>> unapply(final CompositeTypeHints2 x$0) {
            return (Option<List<TypeHints>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.components()));
        }
        
        private Object readResolve() {
            return CompositeTypeHints2$.MODULE$;
        }
        
        public CompositeTypeHints2$() {
            MODULE$ = this;
        }
    }
}
