// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import org.json4s.scalap.$tilde;
import org.json4s.scalap.RulesWithState;
import scala.Function0;
import scala.collection.immutable.Nil$;
import scala.None$;
import scala.collection.immutable.List;
import org.json4s.scalap.InRule;
import org.json4s.scalap.SeqRule;
import scala.runtime.Nothing$;
import scala.collection.Seq;
import org.json4s.scalap.Result;
import scala.Function1;
import org.json4s.scalap.Rule;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001]<Q!\u0001\u0002\t\u0002-\t\u0001dU2bY\u0006\u001c\u0016nZ!uiJL'-\u001e;f!\u0006\u00148/\u001a:t\u0015\t\u0019A!\u0001\u0005tG\u0006d\u0017m]5h\u0015\t)a!\u0001\u0004tG\u0006d\u0017\r\u001d\u0006\u0003\u000f!\taA[:p]R\u001a(\"A\u0005\u0002\u0007=\u0014xm\u0001\u0001\u0011\u00051iQ\"\u0001\u0002\u0007\u000b9\u0011\u0001\u0012A\b\u00031M\u001b\u0017\r\\1TS\u001e\fE\u000f\u001e:jEV$X\rU1sg\u0016\u00148oE\u0002\u000e!Y\u0001\"!\u0005\u000b\u000e\u0003IQ\u0011aE\u0001\u0006g\u000e\fG.Y\u0005\u0003+I\u0011a!\u00118z%\u00164\u0007C\u0001\u0007\u0018\u0013\tA\"A\u0001\bCsR,7i\u001c3f%\u0016\fG-\u001a:\t\u000biiA\u0011A\u000e\u0002\rqJg.\u001b;?)\u0005Y\u0001\"B\u000f\u000e\t\u0003q\u0012!\u00029beN,GCA\u0010#!\ta\u0001%\u0003\u0002\"\u0005\tA1kY1mCNKw\rC\u0003$9\u0001\u0007A%\u0001\u0005csR,7i\u001c3f!\taQ%\u0003\u0002'\u0005\tA!)\u001f;f\u0007>$W\rC\u0004)\u001b\t\u0007I\u0011A\u0015\u0002\u00079\fG/F\u0001+!\u0019YCF\f\u00183k5\tA!\u0003\u0002.\t\t!!+\u001e7f!\ty\u0003'D\u0001\u000e\u0013\t\ttCA\u0001T!\t\t2'\u0003\u00025%\t\u0019\u0011J\u001c;\u0011\u0005E1\u0014BA\u001c\u0013\u0005\u001dqu\u000e\u001e5j]\u001eDa!O\u0007!\u0002\u0013Q\u0013\u0001\u00028bi\u0002BqaO\u0007C\u0002\u0013\u0005A(\u0001\u0005sC^\u0014\u0015\u0010^3t+\u0005i\u0004CB\u0016-]9\"S\u0007\u0003\u0004@\u001b\u0001\u0006I!P\u0001\ne\u0006<()\u001f;fg\u0002Bq!Q\u0007C\u0002\u0013\u0005!)A\u0003f]R\u0014\u00180F\u0001D!\u0019YCF\f\u0018EkA!1&\u0012\u001a%\u0013\t1EA\u0001\u0004%i&dG-\u001a\u0005\u0007\u00116\u0001\u000b\u0011B\"\u0002\r\u0015tGO]=!\u0011\u001dQUB1A\u0005\u0002-\u000baa]=ni\u0006\u0014W#\u0001'\u0011\r-bcFL'6!\rqe\u000b\u0012\b\u0003\u001fRs!\u0001U*\u000e\u0003ES!A\u0015\u0006\u0002\rq\u0012xn\u001c;?\u0013\u0005\u0019\u0012BA+\u0013\u0003\u001d\u0001\u0018mY6bO\u0016L!a\u0016-\u0003\u0007M+\u0017O\u0003\u0002V%!1!,\u0004Q\u0001\n1\u000bqa]=ni\u0006\u0014\u0007\u0005C\u0004]\u001b\t\u0007I\u0011A/\u0002\u0011M\u001c\u0017\r\\1TS\u001e,\u0012A\u0018\t\u0007W1rcfH\u001b\t\r\u0001l\u0001\u0015!\u0003_\u0003%\u00198-\u00197b'&<\u0007\u0005C\u0004c\u001b\t\u0007I\u0011A2\u0002\tU$h\rO\u000b\u0002IB11\u0006\f\u0018/KV\u0002\"AZ5\u000f\u0005E9\u0017B\u00015\u0013\u0003\u0019\u0001&/\u001a3fM&\u0011!n\u001b\u0002\u0007'R\u0014\u0018N\\4\u000b\u0005!\u0014\u0002BB7\u000eA\u0003%A-A\u0003vi\u001aD\u0004\u0005C\u0004p\u001b\t\u0007I\u0011\u00019\u0002\u00131|gn\u001a,bYV,W#A9\u0011\r-bcF\f:6!\t\t2/\u0003\u0002u%\t!Aj\u001c8h\u0011\u00191X\u0002)A\u0005c\u0006QAn\u001c8h-\u0006dW/\u001a\u0011")
public final class ScalaSigAttributeParsers
{
    public static <In, Out, A, Any> Function1<In, A> expect(final Rule<In, Out, A, Any> rule) {
        return ScalaSigAttributeParsers$.MODULE$.expect(rule);
    }
    
    public static <In, Out, A, X> Rule<In, Out, A, X> ruleWithName(final String name, final Function1<In, Result<Out, A, X>> f) {
        return ScalaSigAttributeParsers$.MODULE$.ruleWithName(name, f);
    }
    
    public static <In, Out, A, X> Rule<In, Out, A, X> oneOf(final Seq<Rule<In, Out, A, X>> rules) {
        return ScalaSigAttributeParsers$.MODULE$.oneOf(rules);
    }
    
    public static <X> Rule<Object, Nothing$, Nothing$, X> error(final X err) {
        return ScalaSigAttributeParsers$.MODULE$.error(err);
    }
    
    public static <In> Rule<In, Nothing$, Nothing$, In> error() {
        return ScalaSigAttributeParsers$.MODULE$.error();
    }
    
    public static Rule<Object, Nothing$, Nothing$, Nothing$> failure() {
        return ScalaSigAttributeParsers$.MODULE$.failure();
    }
    
    public static <Out, A> Rule<Object, Out, A, Nothing$> success(final Out out, final A a) {
        return ScalaSigAttributeParsers$.MODULE$.success(out, a);
    }
    
    public static <s> Object state() {
        return ScalaSigAttributeParsers$.MODULE$.state();
    }
    
    public static <In> Object from() {
        return ScalaSigAttributeParsers$.MODULE$.from();
    }
    
    public static <In, A, X> SeqRule<In, A, X> seqRule(final Rule<In, In, A, X> rule) {
        return ScalaSigAttributeParsers$.MODULE$.seqRule(rule);
    }
    
    public static <In, Out, A, X> InRule<In, Out, A, X> inRule(final Rule<In, Out, A, X> rule) {
        return ScalaSigAttributeParsers$.MODULE$.inRule(rule);
    }
    
    public static <In, Out, A, X> Rule<In, Out, A, X> rule(final Function1<In, Result<Out, A, X>> f) {
        return ScalaSigAttributeParsers$.MODULE$.rule(f);
    }
    
    public static <T, X> Rule<Object, Object, T, X> repeatUntil(final Rule<Object, Object, Function1<T, T>, X> rule, final Function1<T, Object> finished, final T initial) {
        return ScalaSigAttributeParsers$.MODULE$.repeatUntil(rule, finished, initial);
    }
    
    public static <A, X> Rule<Object, Object, List<A>, X> anyOf(final Seq<Rule<Object, Object, A, X>> rules) {
        return ScalaSigAttributeParsers$.MODULE$.anyOf(rules);
    }
    
    public static <A, X> Function1<Object, Result<Object, List<A>, X>> allOf(final Seq<Rule<Object, Object, A, X>> rules) {
        return ScalaSigAttributeParsers$.MODULE$.allOf(rules);
    }
    
    public static Rule<Object, Object, Object, Nothing$> cond(final Function1<Object, Object> f) {
        return ScalaSigAttributeParsers$.MODULE$.cond(f);
    }
    
    public static Rule<Object, Object, None$, Nothing$> none() {
        return ScalaSigAttributeParsers$.MODULE$.none();
    }
    
    public static Rule<Object, Object, Nil$, Nothing$> nil() {
        return ScalaSigAttributeParsers$.MODULE$.nil();
    }
    
    public static Rule<Object, Object, Object, Nothing$> update(final Function1<Object, Object> f) {
        return ScalaSigAttributeParsers$.MODULE$.update(f);
    }
    
    public static Rule<Object, Object, Object, Nothing$> set(final Function0<Object> s) {
        return ScalaSigAttributeParsers$.MODULE$.set(s);
    }
    
    public static Rule<Object, Object, Object, Nothing$> get() {
        return ScalaSigAttributeParsers$.MODULE$.get();
    }
    
    public static <A> Rule<Object, Object, A, Nothing$> read(final Function1<Object, A> f) {
        return ScalaSigAttributeParsers$.MODULE$.read(f);
    }
    
    public static <A> Rule<Object, Object, A, Nothing$> unit(final Function0<A> a) {
        return ScalaSigAttributeParsers$.MODULE$.unit(a);
    }
    
    public static <A, X> Rule<Object, Object, A, X> apply(final Function1<Object, Result<Object, A, X>> f) {
        return ScalaSigAttributeParsers$.MODULE$.apply(f);
    }
    
    public static void org$json4s$scalap$RulesWithState$_setter_$factory_$eq(final RulesWithState x$1) {
        ScalaSigAttributeParsers$.MODULE$.org$json4s$scalap$RulesWithState$_setter_$factory_$eq(x$1);
    }
    
    public static RulesWithState factory() {
        return ScalaSigAttributeParsers$.MODULE$.factory();
    }
    
    public static Rule<ByteCode, ByteCode, ByteCode, Nothing$> bytes(final int n) {
        return ScalaSigAttributeParsers$.MODULE$.bytes(n);
    }
    
    public static void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u4_$eq(final Rule x$1) {
        ScalaSigAttributeParsers$.MODULE$.org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u4_$eq(x$1);
    }
    
    public static void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u2_$eq(final Rule x$1) {
        ScalaSigAttributeParsers$.MODULE$.org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u2_$eq(x$1);
    }
    
    public static void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u1_$eq(final Rule x$1) {
        ScalaSigAttributeParsers$.MODULE$.org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u1_$eq(x$1);
    }
    
    public static void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$byte_$eq(final Rule x$1) {
        ScalaSigAttributeParsers$.MODULE$.org$json4s$scalap$scalasig$ByteCodeReader$_setter_$byte_$eq(x$1);
    }
    
    public static Rule<ByteCode, ByteCode, Object, Nothing$> u4() {
        return ScalaSigAttributeParsers$.MODULE$.u4();
    }
    
    public static Rule<ByteCode, ByteCode, Object, Nothing$> u2() {
        return ScalaSigAttributeParsers$.MODULE$.u2();
    }
    
    public static Rule<ByteCode, ByteCode, Object, Nothing$> u1() {
        return ScalaSigAttributeParsers$.MODULE$.u1();
    }
    
    public static Rule<ByteCode, ByteCode, Object, Nothing$> byte() {
        return ScalaSigAttributeParsers$.MODULE$.byte();
    }
    
    public static Rule<ByteCode, ByteCode, Object, Nothing$> longValue() {
        return ScalaSigAttributeParsers$.MODULE$.longValue();
    }
    
    public static Rule<ByteCode, ByteCode, String, Nothing$> utf8() {
        return ScalaSigAttributeParsers$.MODULE$.utf8();
    }
    
    public static Rule<ByteCode, ByteCode, ScalaSig, Nothing$> scalaSig() {
        return ScalaSigAttributeParsers$.MODULE$.scalaSig();
    }
    
    public static Rule<ByteCode, ByteCode, Seq<$tilde<Object, ByteCode>>, Nothing$> symtab() {
        return ScalaSigAttributeParsers$.MODULE$.symtab();
    }
    
    public static Rule<ByteCode, ByteCode, $tilde<Object, ByteCode>, Nothing$> entry() {
        return ScalaSigAttributeParsers$.MODULE$.entry();
    }
    
    public static Rule<ByteCode, ByteCode, ByteCode, Nothing$> rawBytes() {
        return ScalaSigAttributeParsers$.MODULE$.rawBytes();
    }
    
    public static Rule<ByteCode, ByteCode, Object, Nothing$> nat() {
        return ScalaSigAttributeParsers$.MODULE$.nat();
    }
    
    public static ScalaSig parse(final ByteCode byteCode) {
        return ScalaSigAttributeParsers$.MODULE$.parse(byteCode);
    }
}
