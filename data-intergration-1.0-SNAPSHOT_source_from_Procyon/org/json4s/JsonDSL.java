// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.Seq;
import scala.Predef$;
import scala.collection.immutable.List$;
import scala.collection.immutable.Nil$;
import scala.math.BigInt;
import scala.math.BigDecimal;
import scala.collection.immutable.List;
import scala.Tuple2;
import scala.Symbol;
import scala.Option;
import scala.collection.immutable.Map;
import scala.Function1;
import scala.collection.Iterable;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\t5q!B\u0001\u0003\u0011\u00039\u0011a\u0002&t_:$5\u000b\u0014\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0011\u0005!IQ\"\u0001\u0002\u0007\u000b)\u0011\u0001\u0012A\u0006\u0003\u000f)\u001bxN\u001c#T\u0019N)\u0011\u0002\u0004\n\u0002\\B\u0011Q\u0002E\u0007\u0002\u001d)\tq\"A\u0003tG\u0006d\u0017-\u0003\u0002\u0012\u001d\t1\u0011I\\=SK\u001a\u0004\"\u0001C\n\u0007\u000f)\u0011\u0001\u0013aA\u0001)M\u00191\u0003D\u000b\u0011\u0005!1\u0012BA\f\u0003\u0005%IU\u000e\u001d7jG&$8\u000fC\u0003\u001a'\u0011\u0005!$\u0001\u0004%S:LG\u000f\n\u000b\u00027A\u0011Q\u0002H\u0005\u0003;9\u0011A!\u00168ji\")qd\u0005C\u0002A\u0005Q1/Z93UZ\fG.^3\u0016\u0005\u0005\nDC\u0001\u0012>)\t\u0019#\u0006\u0005\u0002%O9\u0011\u0001\"J\u0005\u0003M\t\tqAS:p]\u0006\u001bF+\u0003\u0002)S\t1!*\u0011:sCfT!A\n\u0002\t\u000b-r\u00029\u0001\u0017\u0002\u0005\u00154\b\u0003B\u0007._iJ!A\f\b\u0003\u0013\u0019+hn\u0019;j_:\f\u0004C\u0001\u00192\u0019\u0001!QA\r\u0010C\u0002M\u0012\u0011!Q\t\u0003i]\u0002\"!D\u001b\n\u0005Yr!a\u0002(pi\"Lgn\u001a\t\u0003\u001baJ!!\u000f\b\u0003\u0007\u0005s\u0017\u0010\u0005\u0002%w%\u0011A(\u000b\u0002\u0007\u0015Z\u000bG.^3\t\u000byr\u0002\u0019A \u0002\u0003M\u00042\u0001\u0011%0\u001d\t\teI\u0004\u0002C\u000b6\t1I\u0003\u0002E\r\u00051AH]8pizJ\u0011aD\u0005\u0003\u000f:\tq\u0001]1dW\u0006<W-\u0003\u0002J\u0015\nA\u0011\n^3sC\ndWM\u0003\u0002H\u001d!)Aj\u0005C\u0002\u001b\u0006QQ.\u001993UZ\fG.^3\u0016\u000593FCA(X)\t\u00016\u000b\u0005\u0002%#&\u0011!+\u000b\u0002\b\u0015>\u0013'.Z2u\u0011\u0015Y3\nq\u0001U!\u0011iQ&\u0016\u001e\u0011\u0005A2F!\u0002\u001aL\u0005\u0004\u0019\u0004\"\u0002-L\u0001\u0004I\u0016!A7\u0011\tik\u0006-\u0016\b\u0003\u001bmK!\u0001\u0018\b\u0002\rA\u0013X\rZ3g\u0013\tqvLA\u0002NCBT!\u0001\u0018\b\u0011\u0005i\u000b\u0017B\u00012`\u0005\u0019\u0019FO]5oO\")Am\u0005C\u0002K\u0006iq\u000e\u001d;j_:\u0014$N^1mk\u0016,\"AZ6\u0015\u0005\u001ddGC\u0001\u001ei\u0011\u0015Y3\rq\u0001j!\u0011iQF\u001b\u001e\u0011\u0005AZG!\u0002\u001ad\u0005\u0004\u0019\u0004\"B7d\u0001\u0004q\u0017aA8qiB\u0019Qb\u001c6\n\u0005At!AB(qi&|g\u000eC\u0003s'\u0011\r1/A\u0007ts6\u0014w\u000e\u001c\u001akm\u0006dW/\u001a\u000b\u0003i^\u0004\"\u0001J;\n\u0005YL#a\u0002&TiJLgn\u001a\u0005\u0006qF\u0004\r!_\u0001\u0002qB\u0011QB_\u0005\u0003w:\u0011aaU=nE>d\u0007\"B?\u0014\t\u0007q\u0018a\u00039bSJ\u0014$N^1mk\u0016,2a`A\u0005)\u0011\t\t!a\u0003\u0015\u0007A\u000b\u0019\u0001\u0003\u0004,y\u0002\u000f\u0011Q\u0001\t\u0006\u001b5\n9A\u000f\t\u0004a\u0005%A!\u0002\u001a}\u0005\u0004\u0019\u0004bBA\u0007y\u0002\u0007\u0011qB\u0001\u0002iB1Q\"!\u0005a\u0003\u000fI1!a\u0005\u000f\u0005\u0019!V\u000f\u001d7fe!9\u0011qC\n\u0005\u0004\u0005e\u0011a\u00037jgR\u0014$N^1mk\u0016$2\u0001UA\u000e\u0011!\ti\"!\u0006A\u0002\u0005}\u0011!\u00017\u0011\u000b\u0001\u000b\t#!\n\n\u0007\u0005\r\"J\u0001\u0003MSN$\bc\u0001\u0013\u0002(%\u0019\u0011\u0011F\u0015\u0003\r)3\u0015.\u001a7e\u0011\u001d\tic\u0005C\u0002\u0003_\tQB[8cU\u0016\u001cGOM1tg>\u001cG\u0003BA\u0019\u0003O\u0002B!a\r\u000265\t1C\u0002\u0004\u00028M\u0001\u0011\u0011\b\u0002\u000e\u0015N|g\u000eT5ti\u0006\u001b8o\\2\u0014\u0007\u0005UB\u0002C\u0006\u0002>\u0005U\"\u0011!Q\u0001\n\u0005}\u0011\u0001\u00027fMRD\u0001\"!\u0011\u00026\u0011\u0005\u00111I\u0001\u0007y%t\u0017\u000e\u001e \u0015\t\u0005E\u0012Q\t\u0005\t\u0003{\ty\u00041\u0001\u0002 !A\u0011\u0011JA\u001b\t\u0003\tY%\u0001\u0004%i&dG-\u001a\u000b\u0004!\u00065\u0003\u0002CA(\u0003\u000f\u0002\r!!\u0015\u0002\u000bILw\r\u001b;\u0011\u000b5\t\t\u0002\u0019\u001e\t\u0011\u0005%\u0013Q\u0007C\u0001\u0003+\"2\u0001UA,\u0011\u001d\ty%a\u0015A\u0002AC\u0001\"a\u0017\u00026\u0011\u0005\u0011QL\u0001\rIQLG\u000eZ3%i&dG-\u001a\u000b\u0004!\u0006}\u0003\u0002CA(\u00033\u0002\r!!\u0015\t\u0011\u0005m\u0013Q\u0007C\u0001\u0003G\"2\u0001UA3\u0011\u001d\ty%!\u0019A\u0002ACq!!\u001b\u0002,\u0001\u0007\u0001+A\u0001p\u0011\u001d\tig\u0005C\u0002\u0003_\n!\u0002]1jeJ\n5o]8d+\u0011\t\t(!5\u0015\t\u0005M\u0014q\u001b\u000b\u0005\u0003k\n\u0019\u000e\u0005\u0004\u00024\u0005]\u0014q\u001a\u0004\u0007\u0003s\u001a\u0002!a\u001f\u0003\u0013)\u001bxN\\!tg>\u001cW\u0003BA?\u0003\u000b\u001b2!a\u001e\r\u0011-\ti$a\u001e\u0003\u0002\u0003\u0006I!!!\u0011\r5\t\t\u0002YAB!\r\u0001\u0014Q\u0011\u0003\u0007e\u0005]$\u0019A\u001a\t\u0015-\n9H!A!\u0002\u0017\tI\tE\u0003\u000e[\u0005\r%\b\u0003\u0005\u0002B\u0005]D\u0011AAG)\u0011\ty)!&\u0015\t\u0005E\u00151\u0013\t\u0007\u0003g\t9(a!\t\u000f-\nY\tq\u0001\u0002\n\"A\u0011QHAF\u0001\u0004\t\t\t\u0003\u0005\u0002J\u0005]D\u0011AAM+\u0011\tY*a*\u0015\t\u0005u\u00151\u0016\u000b\u0004!\u0006}\u0005\u0002CAQ\u0003/\u0003\u001d!a)\u0002\u0007\u00154\u0018\u0007E\u0003\u000e[\u0005\u0015&\bE\u00021\u0003O#q!!+\u0002\u0018\n\u00071GA\u0001C\u0011!\ty%a&A\u0002\u00055\u0006CB\u0007\u0002\u0012\u0001\f)\u000b\u0003\u0005\u0002J\u0005]D\u0011AAY)\r\u0001\u00161\u0017\u0005\b\u0003\u001f\ny\u000b1\u0001Q\u0011!\tY&a\u001e\u0005\u0002\u0005]V\u0003BA]\u0003\u0007$B!a/\u0002FR\u0019\u0001+!0\t\u000f-\n)\fq\u0001\u0002@B)Q\"LAauA\u0019\u0001'a1\u0005\u000f\u0005%\u0016Q\u0017b\u0001g!A\u0011qJA[\u0001\u0004\t9\r\u0005\u0004\u000e\u0003#\u0001\u0017\u0011\u0019\u0005\t\u00037\n9\b\"\u0001\u0002LR\u0019\u0001+!4\t\u000f\u0005=\u0013\u0011\u001aa\u0001!B\u0019\u0001'!5\u0005\rI\nYG1\u00014\u0011\u001dY\u00131\u000ea\u0002\u0003+\u0004R!D\u0017\u0002PjB\u0001\"!\u0004\u0002l\u0001\u0007\u0011\u0011\u001c\t\u0007\u001b\u0005E\u0001-a4\u0011\u0007!\ti.C\u0002\u0002`\n\u0011!\u0002R8vE2,Wj\u001c3f\u0011\u001d\t\t%\u0003C\u0001\u0003G$\u0012aB\u0004\b\u0003OL\u0001\u0012AAu\u0003)9\u0016\u000e\u001e5E_V\u0014G.\u001a\t\u0005\u0003W\fi/D\u0001\n\r\u001d\ty/\u0003E\u0001\u0003c\u0014!bV5uQ\u0012{WO\u00197f'\u0019\ti\u000f\u0004\n\u0002\\\"A\u0011\u0011IAw\t\u0003\t)\u0010\u0006\u0002\u0002j\u001e9\u0011\u0011`\u0005\t\u0002\u0005m\u0018AD,ji\"\u0014\u0015n\u001a#fG&l\u0017\r\u001c\t\u0005\u0003W\fiPB\u0004\u0002\u0000&A\tA!\u0001\u0003\u001d]KG\u000f\u001b\"jO\u0012+7-[7bYN1\u0011Q \u0007\u0013\u0005\u0007\u00012\u0001\u0003B\u0003\u0013\r\u00119A\u0001\u0002\u000f\u0005&<G)Z2j[\u0006dWj\u001c3f\u0011!\t\t%!@\u0005\u0002\t-ACAA~\u0001")
public interface JsonDSL extends Implicits
{
     <A> JsonAST.JArray seq2jvalue(final Iterable<A> p0, final Function1<A, JsonAST.JValue> p1);
    
     <A> JsonAST.JObject map2jvalue(final Map<String, A> p0, final Function1<A, JsonAST.JValue> p1);
    
     <A> JsonAST.JValue option2jvalue(final Option<A> p0, final Function1<A, JsonAST.JValue> p1);
    
    JsonAST.JString symbol2jvalue(final Symbol p0);
    
     <A> JsonAST.JObject pair2jvalue(final Tuple2<String, A> p0, final Function1<A, JsonAST.JValue> p1);
    
    JsonAST.JObject list2jvalue(final List<Tuple2<String, JsonAST.JValue>> p0);
    
    JsonListAssoc jobject2assoc(final JsonAST.JObject p0);
    
     <A> JsonAssoc<A> pair2Assoc(final Tuple2<String, A> p0, final Function1<A, JsonAST.JValue> p1);
    
    public static class WithDouble$ implements JsonDSL, DoubleMode
    {
        public static final WithDouble$ MODULE$;
        
        static {
            new WithDouble$();
        }
        
        @Override
        public JsonAST.JValue double2jvalue(final double x) {
            return DoubleMode$class.double2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue float2jvalue(final float x) {
            return DoubleMode$class.float2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue bigdecimal2jvalue(final BigDecimal x) {
            return DoubleMode$class.bigdecimal2jvalue(this, x);
        }
        
        @Override
        public <A> JsonAST.JArray seq2jvalue(final Iterable<A> s, final Function1<A, JsonAST.JValue> ev) {
            return JsonDSL$class.seq2jvalue(this, s, ev);
        }
        
        @Override
        public <A> JsonAST.JObject map2jvalue(final Map<String, A> m, final Function1<A, JsonAST.JValue> ev) {
            return JsonDSL$class.map2jvalue(this, m, ev);
        }
        
        @Override
        public <A> JsonAST.JValue option2jvalue(final Option<A> opt, final Function1<A, JsonAST.JValue> ev) {
            return JsonDSL$class.option2jvalue(this, opt, ev);
        }
        
        @Override
        public JsonAST.JString symbol2jvalue(final Symbol x) {
            return JsonDSL$class.symbol2jvalue(this, x);
        }
        
        @Override
        public <A> JsonAST.JObject pair2jvalue(final Tuple2<String, A> t, final Function1<A, JsonAST.JValue> ev) {
            return JsonDSL$class.pair2jvalue(this, t, ev);
        }
        
        @Override
        public JsonAST.JObject list2jvalue(final List<Tuple2<String, JsonAST.JValue>> l) {
            return JsonDSL$class.list2jvalue(this, l);
        }
        
        @Override
        public JsonListAssoc jobject2assoc(final JsonAST.JObject o) {
            return JsonDSL$class.jobject2assoc(this, o);
        }
        
        @Override
        public <A> JsonAssoc<A> pair2Assoc(final Tuple2<String, A> t, final Function1<A, JsonAST.JValue> ev) {
            return (JsonAssoc<A>)JsonDSL$class.pair2Assoc(this, t, ev);
        }
        
        @Override
        public JsonAST.JValue short2jvalue(final short x) {
            return Implicits$class.short2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue byte2jvalue(final byte x) {
            return Implicits$class.byte2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue char2jvalue(final char x) {
            return Implicits$class.char2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue int2jvalue(final int x) {
            return Implicits$class.int2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue long2jvalue(final long x) {
            return Implicits$class.long2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue bigint2jvalue(final BigInt x) {
            return Implicits$class.bigint2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue boolean2jvalue(final boolean x) {
            return Implicits$class.boolean2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue string2jvalue(final String x) {
            return Implicits$class.string2jvalue(this, x);
        }
        
        public WithDouble$() {
            Implicits$class.$init$(MODULE$ = this);
            JsonDSL$class.$init$(this);
            DoubleMode$class.$init$(this);
        }
    }
    
    public static class WithBigDecimal$ implements JsonDSL, BigDecimalMode
    {
        public static final WithBigDecimal$ MODULE$;
        
        static {
            new WithBigDecimal$();
        }
        
        @Override
        public JsonAST.JValue double2jvalue(final double x) {
            return BigDecimalMode$class.double2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue float2jvalue(final float x) {
            return BigDecimalMode$class.float2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue bigdecimal2jvalue(final BigDecimal x) {
            return BigDecimalMode$class.bigdecimal2jvalue(this, x);
        }
        
        @Override
        public <A> JsonAST.JArray seq2jvalue(final Iterable<A> s, final Function1<A, JsonAST.JValue> ev) {
            return JsonDSL$class.seq2jvalue(this, s, ev);
        }
        
        @Override
        public <A> JsonAST.JObject map2jvalue(final Map<String, A> m, final Function1<A, JsonAST.JValue> ev) {
            return JsonDSL$class.map2jvalue(this, m, ev);
        }
        
        @Override
        public <A> JsonAST.JValue option2jvalue(final Option<A> opt, final Function1<A, JsonAST.JValue> ev) {
            return JsonDSL$class.option2jvalue(this, opt, ev);
        }
        
        @Override
        public JsonAST.JString symbol2jvalue(final Symbol x) {
            return JsonDSL$class.symbol2jvalue(this, x);
        }
        
        @Override
        public <A> JsonAST.JObject pair2jvalue(final Tuple2<String, A> t, final Function1<A, JsonAST.JValue> ev) {
            return JsonDSL$class.pair2jvalue(this, t, ev);
        }
        
        @Override
        public JsonAST.JObject list2jvalue(final List<Tuple2<String, JsonAST.JValue>> l) {
            return JsonDSL$class.list2jvalue(this, l);
        }
        
        @Override
        public JsonListAssoc jobject2assoc(final JsonAST.JObject o) {
            return JsonDSL$class.jobject2assoc(this, o);
        }
        
        @Override
        public <A> JsonAssoc<A> pair2Assoc(final Tuple2<String, A> t, final Function1<A, JsonAST.JValue> ev) {
            return (JsonAssoc<A>)JsonDSL$class.pair2Assoc(this, t, ev);
        }
        
        @Override
        public JsonAST.JValue short2jvalue(final short x) {
            return Implicits$class.short2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue byte2jvalue(final byte x) {
            return Implicits$class.byte2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue char2jvalue(final char x) {
            return Implicits$class.char2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue int2jvalue(final int x) {
            return Implicits$class.int2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue long2jvalue(final long x) {
            return Implicits$class.long2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue bigint2jvalue(final BigInt x) {
            return Implicits$class.bigint2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue boolean2jvalue(final boolean x) {
            return Implicits$class.boolean2jvalue(this, x);
        }
        
        @Override
        public JsonAST.JValue string2jvalue(final String x) {
            return Implicits$class.string2jvalue(this, x);
        }
        
        public WithBigDecimal$() {
            Implicits$class.$init$(MODULE$ = this);
            JsonDSL$class.$init$(this);
            BigDecimalMode$class.$init$(this);
        }
    }
    
    public class JsonAssoc<A>
    {
        private final Tuple2<String, A> left;
        private final Function1<A, JsonAST.JValue> ev;
        
        public <B> JsonAST.JObject $tilde(final Tuple2<String, B> right, final Function1<B, JsonAST.JValue> ev1) {
            final JsonAST.JValue l = (JsonAST.JValue)this.ev.apply(this.left._2());
            final JsonAST.JValue r = (JsonAST.JValue)ev1.apply(right._2());
            return new JsonAST.JObject((List<Tuple2<String, JsonAST.JValue>>)Nil$.MODULE$.$colon$colon((Object)JsonAST.JField$.MODULE$.apply((String)right._1(), r)).$colon$colon((Object)JsonAST.JField$.MODULE$.apply((String)this.left._1(), l)));
        }
        
        public JsonAST.JObject $tilde(final JsonAST.JObject right) {
            final JsonAST.JValue l = (JsonAST.JValue)this.ev.apply(this.left._2());
            return new JsonAST.JObject((List<Tuple2<String, JsonAST.JValue>>)right.obj().$colon$colon((Object)JsonAST.JField$.MODULE$.apply((String)this.left._1(), l)));
        }
        
        public <B> JsonAST.JObject $tilde$tilde(final Tuple2<String, B> right, final Function1<B, JsonAST.JValue> ev) {
            return this.$tilde((scala.Tuple2<String, Object>)right, (scala.Function1<Object, JsonAST.JValue>)ev);
        }
        
        public JsonAST.JObject $tilde$tilde(final JsonAST.JObject right) {
            return this.$tilde(right);
        }
        
        public JsonAssoc(final Tuple2<String, A> left, final Function1<A, JsonAST.JValue> ev) {
            this.left = left;
            this.ev = ev;
            if (JsonDSL.this == null) {
                throw null;
            }
        }
    }
    
    public class JsonListAssoc
    {
        private final List<Tuple2<String, JsonAST.JValue>> left;
        
        public JsonAST.JObject $tilde(final Tuple2<String, JsonAST.JValue> right) {
            return new JsonAST.JObject((List<Tuple2<String, JsonAST.JValue>>)List$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { JsonAST.JField$.MODULE$.apply((String)right._1(), (JsonAST.JValue)right._2()) })).$colon$colon$colon((List)this.left));
        }
        
        public JsonAST.JObject $tilde(final JsonAST.JObject right) {
            return new JsonAST.JObject((List<Tuple2<String, JsonAST.JValue>>)right.obj().$colon$colon$colon((List)this.left));
        }
        
        public JsonAST.JObject $tilde$tilde(final Tuple2<String, JsonAST.JValue> right) {
            return this.$tilde(right);
        }
        
        public JsonAST.JObject $tilde$tilde(final JsonAST.JObject right) {
            return this.$tilde(right);
        }
        
        public JsonListAssoc(final List<Tuple2<String, JsonAST.JValue>> left) {
            this.left = left;
            if (JsonDSL.this == null) {
                throw null;
            }
        }
    }
}
