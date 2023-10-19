// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Function3;
import scala.Predef$;
import org.json4s.scalap.RulesWithState$class;
import scala.runtime.BoxesRunTime;
import org.json4s.scalap.Failure$;
import org.json4s.scalap.Success;
import org.json4s.scalap.SeqRule;
import org.json4s.scalap.InRule;
import org.json4s.scalap.Rules;
import org.json4s.scalap.Rules$class;
import scala.collection.immutable.List;
import scala.None$;
import scala.collection.immutable.Nil$;
import scala.Function0;
import org.json4s.scalap.StateRules;
import org.json4s.scalap.StateRules$class;
import org.json4s.scalap.Result;
import scala.Function1;
import org.json4s.scalap.RulesWithState;
import scala.collection.Seq;
import org.json4s.scalap.$tilde;
import scala.runtime.Nothing$;
import org.json4s.scalap.Rule;

public final class ScalaSigAttributeParsers$ implements ByteCodeReader
{
    public static final ScalaSigAttributeParsers$ MODULE$;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> nat;
    private final Rule<ByteCode, ByteCode, ByteCode, Nothing$> rawBytes;
    private final Rule<ByteCode, ByteCode, $tilde<Object, ByteCode>, Nothing$> entry;
    private final Rule<ByteCode, ByteCode, Seq<$tilde<Object, ByteCode>>, Nothing$> symtab;
    private final Rule<ByteCode, ByteCode, ScalaSig, Nothing$> scalaSig;
    private final Rule<ByteCode, ByteCode, String, Nothing$> utf8;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> longValue;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> byte;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> u1;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> u2;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> u4;
    private final RulesWithState factory;
    
    static {
        new ScalaSigAttributeParsers$();
    }
    
    @Override
    public Rule<ByteCode, ByteCode, Object, Nothing$> byte() {
        return this.byte;
    }
    
    @Override
    public Rule<ByteCode, ByteCode, Object, Nothing$> u1() {
        return this.u1;
    }
    
    @Override
    public Rule<ByteCode, ByteCode, Object, Nothing$> u2() {
        return this.u2;
    }
    
    @Override
    public Rule<ByteCode, ByteCode, Object, Nothing$> u4() {
        return this.u4;
    }
    
    @Override
    public void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$byte_$eq(final Rule x$1) {
        this.byte = (Rule<ByteCode, ByteCode, Object, Nothing$>)x$1;
    }
    
    @Override
    public void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u1_$eq(final Rule x$1) {
        this.u1 = (Rule<ByteCode, ByteCode, Object, Nothing$>)x$1;
    }
    
    @Override
    public void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u2_$eq(final Rule x$1) {
        this.u2 = (Rule<ByteCode, ByteCode, Object, Nothing$>)x$1;
    }
    
    @Override
    public void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u4_$eq(final Rule x$1) {
        this.u4 = (Rule<ByteCode, ByteCode, Object, Nothing$>)x$1;
    }
    
    @Override
    public Rule<ByteCode, ByteCode, ByteCode, Nothing$> bytes(final int n) {
        return (Rule<ByteCode, ByteCode, ByteCode, Nothing$>)ByteCodeReader$class.bytes(this, n);
    }
    
    @Override
    public RulesWithState factory() {
        return this.factory;
    }
    
    @Override
    public void org$json4s$scalap$RulesWithState$_setter_$factory_$eq(final RulesWithState x$1) {
        this.factory = x$1;
    }
    
    @Override
    public <A, X> Rule<Object, Object, A, X> apply(final Function1<Object, Result<Object, A, X>> f) {
        return (Rule<Object, Object, A, X>)StateRules$class.apply(this, f);
    }
    
    @Override
    public <A> Rule<Object, Object, A, Nothing$> unit(final Function0<A> a) {
        return (Rule<Object, Object, A, Nothing$>)StateRules$class.unit(this, a);
    }
    
    @Override
    public <A> Rule<Object, Object, A, Nothing$> read(final Function1<Object, A> f) {
        return (Rule<Object, Object, A, Nothing$>)StateRules$class.read(this, f);
    }
    
    @Override
    public Rule<Object, Object, Object, Nothing$> get() {
        return (Rule<Object, Object, Object, Nothing$>)StateRules$class.get(this);
    }
    
    @Override
    public Rule<Object, Object, Object, Nothing$> set(final Function0<Object> s) {
        return (Rule<Object, Object, Object, Nothing$>)StateRules$class.set(this, s);
    }
    
    @Override
    public Rule<Object, Object, Object, Nothing$> update(final Function1<Object, Object> f) {
        return (Rule<Object, Object, Object, Nothing$>)StateRules$class.update(this, f);
    }
    
    @Override
    public Rule<Object, Object, Nil$, Nothing$> nil() {
        return (Rule<Object, Object, Nil$, Nothing$>)StateRules$class.nil(this);
    }
    
    @Override
    public Rule<Object, Object, None$, Nothing$> none() {
        return (Rule<Object, Object, None$, Nothing$>)StateRules$class.none(this);
    }
    
    @Override
    public Rule<Object, Object, Object, Nothing$> cond(final Function1<Object, Object> f) {
        return (Rule<Object, Object, Object, Nothing$>)StateRules$class.cond(this, f);
    }
    
    @Override
    public <A, X> Function1<Object, Result<Object, List<A>, X>> allOf(final Seq<Rule<Object, Object, A, X>> rules) {
        return (Function1<Object, Result<Object, List<A>, X>>)StateRules$class.allOf(this, rules);
    }
    
    @Override
    public <A, X> Rule<Object, Object, List<A>, X> anyOf(final Seq<Rule<Object, Object, A, X>> rules) {
        return (Rule<Object, Object, List<A>, X>)StateRules$class.anyOf(this, rules);
    }
    
    @Override
    public <T, X> Rule<Object, Object, T, X> repeatUntil(final Rule<Object, Object, Function1<T, T>, X> rule, final Function1<T, Object> finished, final T initial) {
        return (Rule<Object, Object, T, X>)StateRules$class.repeatUntil(this, rule, finished, initial);
    }
    
    @Override
    public <In, Out, A, X> Rule<In, Out, A, X> rule(final Function1<In, Result<Out, A, X>> f) {
        return (Rule<In, Out, A, X>)Rules$class.rule(this, f);
    }
    
    @Override
    public <In, Out, A, X> InRule<In, Out, A, X> inRule(final Rule<In, Out, A, X> rule) {
        return (InRule<In, Out, A, X>)Rules$class.inRule(this, rule);
    }
    
    @Override
    public <In, A, X> SeqRule<In, A, X> seqRule(final Rule<In, In, A, X> rule) {
        return (SeqRule<In, A, X>)Rules$class.seqRule(this, rule);
    }
    
    @Override
    public <In> Object from() {
        return Rules$class.from(this);
    }
    
    @Override
    public <s> Object state() {
        return Rules$class.state(this);
    }
    
    @Override
    public <Out, A> Rule<Object, Out, A, Nothing$> success(final Out out, final A a) {
        return (Rule<Object, Out, A, Nothing$>)Rules$class.success(this, out, a);
    }
    
    @Override
    public Rule<Object, Nothing$, Nothing$, Nothing$> failure() {
        return (Rule<Object, Nothing$, Nothing$, Nothing$>)Rules$class.failure(this);
    }
    
    @Override
    public <In> Rule<In, Nothing$, Nothing$, In> error() {
        return (Rule<In, Nothing$, Nothing$, In>)Rules$class.error(this);
    }
    
    @Override
    public <X> Rule<Object, Nothing$, Nothing$, X> error(final X err) {
        return (Rule<Object, Nothing$, Nothing$, X>)Rules$class.error(this, err);
    }
    
    @Override
    public <In, Out, A, X> Rule<In, Out, A, X> oneOf(final Seq<Rule<In, Out, A, X>> rules) {
        return (Rule<In, Out, A, X>)Rules$class.oneOf(this, rules);
    }
    
    @Override
    public <In, Out, A, X> Rule<In, Out, A, X> ruleWithName(final String _name, final Function1<In, Result<Out, A, X>> f) {
        return (Rule<In, Out, A, X>)Rules$class.ruleWithName(this, _name, f);
    }
    
    @Override
    public <In, Out, A, Any> Function1<In, A> expect(final Rule<In, Out, A, Any> rule) {
        return (Function1<In, A>)Rules$class.expect(this, rule);
    }
    
    public ScalaSig parse(final ByteCode byteCode) {
        return (ScalaSig)this.expect(this.scalaSig()).apply((Object)byteCode);
    }
    
    public Rule<ByteCode, ByteCode, Object, Nothing$> nat() {
        return this.nat;
    }
    
    public Rule<ByteCode, ByteCode, ByteCode, Nothing$> rawBytes() {
        return this.rawBytes;
    }
    
    public Rule<ByteCode, ByteCode, $tilde<Object, ByteCode>, Nothing$> entry() {
        return this.entry;
    }
    
    public Rule<ByteCode, ByteCode, Seq<$tilde<Object, ByteCode>>, Nothing$> symtab() {
        return this.symtab;
    }
    
    public Rule<ByteCode, ByteCode, ScalaSig, Nothing$> scalaSig() {
        return this.scalaSig;
    }
    
    public Rule<ByteCode, ByteCode, String, Nothing$> utf8() {
        return this.utf8;
    }
    
    public Rule<ByteCode, ByteCode, Object, Nothing$> longValue() {
        return this.longValue;
    }
    
    public final Result org$json4s$scalap$scalasig$ScalaSigAttributeParsers$$natN$1(ByteCode in, int x) {
        Object module$;
        while (true) {
            final Result nextByte = in.nextByte();
            if (!(nextByte instanceof Success)) {
                module$ = Failure$.MODULE$;
                break;
            }
            final Success<ByteCode, A> success = (Success<ByteCode, A>)nextByte;
            final ByteCode out = success.out();
            final byte b = BoxesRunTime.unboxToByte((Object)success.value());
            final int y = (x << 7) + (b & 0x7F);
            if ((b & 0x80) == 0x0) {
                module$ = new Success(out, BoxesRunTime.boxToInteger(y));
                break;
            }
            final ByteCode byteCode = out;
            x = y;
            in = byteCode;
        }
        return (Result)module$;
    }
    
    private ScalaSigAttributeParsers$() {
        Rules$class.$init$(MODULE$ = this);
        StateRules$class.$init$(this);
        RulesWithState$class.$init$(this);
        ByteCodeReader$class.$init$(this);
        this.nat = (Rule<ByteCode, ByteCode, Object, Nothing$>)this.apply((scala.Function1<Object, Result<Object, Object, Nothing$>>)new ScalaSigAttributeParsers$$anonfun.ScalaSigAttributeParsers$$anonfun$2());
        this.rawBytes = this.nat().$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, ByteCode, Nothing$>>>)new ScalaSigAttributeParsers$$anonfun.ScalaSigAttributeParsers$$anonfun$3());
        this.entry = this.nat().$tilde((scala.Function0<Rule<ByteCode, ByteCode, ByteCode, Nothing$>>)new ScalaSigAttributeParsers$$anonfun.ScalaSigAttributeParsers$$anonfun$4());
        this.symtab = this.nat().$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, Seq<$tilde<Object, ByteCode>>, Nothing$>>>)new ScalaSigAttributeParsers$$anonfun.ScalaSigAttributeParsers$$anonfun$5((SeqRule)this.seqRule(this.entry())));
        this.scalaSig = this.nat().$tilde((scala.Function0<Rule<ByteCode, Object, Object, Object>>)new ScalaSigAttributeParsers$$anonfun.ScalaSigAttributeParsers$$anonfun$6()).$tilde((scala.Function0<Rule<Object, ByteCode, Object, Nothing$>>)new ScalaSigAttributeParsers$$anonfun.ScalaSigAttributeParsers$$anonfun$7()).$up$tilde$tilde$up((scala.Function3<Object, Object, Object, ScalaSig>)ScalaSig$.MODULE$, (scala.Function1<$tilde<$tilde<Object, Object>, Object>, $tilde<$tilde<Object, Object>, Object>>)Predef$.MODULE$.$conforms());
        this.utf8 = (Rule<ByteCode, ByteCode, String, Nothing$>)this.read((scala.Function1<Object, String>)new ScalaSigAttributeParsers$$anonfun.ScalaSigAttributeParsers$$anonfun$8());
        this.longValue = (Rule<ByteCode, ByteCode, Object, Nothing$>)this.read((scala.Function1<Object, Object>)new ScalaSigAttributeParsers$$anonfun.ScalaSigAttributeParsers$$anonfun$9());
    }
}
