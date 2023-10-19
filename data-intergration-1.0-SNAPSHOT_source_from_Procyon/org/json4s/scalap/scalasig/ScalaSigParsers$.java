// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import org.json4s.scalap.RulesWithState$class;
import scala.Tuple2;
import scala.runtime.BoxesRunTime;
import org.json4s.scalap.SeqRule;
import org.json4s.scalap.InRule;
import scala.None$;
import scala.collection.immutable.Nil$;
import org.json4s.scalap.StateRules;
import org.json4s.scalap.StateRules$class;
import org.json4s.scalap.MemoisableRules$class;
import org.json4s.scalap.Memoisable;
import org.json4s.scalap.Result;
import scala.Function0;
import org.json4s.scalap.Rules;
import org.json4s.scalap.Rules$class;
import scala.Function1;
import scala.runtime.BoxedUnit;
import scala.collection.immutable.List;
import scala.runtime.Nothing$;
import org.json4s.scalap.$tilde;
import scala.collection.Seq;
import org.json4s.scalap.Rule;
import org.json4s.scalap.MemoisableRules;
import org.json4s.scalap.RulesWithState;

public final class ScalaSigParsers$ implements RulesWithState, MemoisableRules
{
    public static final ScalaSigParsers$ MODULE$;
    private final Rule<ScalaSig, ScalaSig, Seq<$tilde<Object, ByteCode>>, Nothing$> symTab;
    private final Rule<ScalaSig, ScalaSig, Object, Nothing$> size;
    private Rule<ScalaSig, ScalaSig, List<Object>, String> entries;
    private Rule<ScalaSig, ScalaSig, List<Symbol>, String> symbols;
    private Rule<ScalaSig, ScalaSig, List<MethodSymbol>, String> methods;
    private Rule<ScalaSig, ScalaSig, List<AttributeInfo>, String> attributes;
    private Rule<ScalaSig, ScalaSig, List<ClassSymbol>, String> topLevelClasses;
    private Rule<ScalaSig, ScalaSig, List<ObjectSymbol>, String> topLevelObjects;
    private final RulesWithState factory;
    private volatile byte bitmap$0;
    
    static {
        new ScalaSigParsers$();
    }
    
    private Rule entries$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x1) == 0) {
                this.entries = this.allEntries(ScalaSigEntryParsers$.MODULE$.entry()).as("entries");
                this.bitmap$0 |= 0x1;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.entries;
        }
    }
    
    private Rule symbols$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x2) == 0) {
                this.symbols = this.allEntries(ScalaSigEntryParsers$.MODULE$.symbol()).as("symbols");
                this.bitmap$0 |= 0x2;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.symbols;
        }
    }
    
    private Rule methods$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x4) == 0) {
                this.methods = this.allEntries(ScalaSigEntryParsers$.MODULE$.methodSymbol()).as("methods");
                this.bitmap$0 |= 0x4;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.methods;
        }
    }
    
    private Rule attributes$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x8) == 0) {
                this.attributes = this.allEntries(ScalaSigEntryParsers$.MODULE$.attributeInfo()).as("attributes");
                this.bitmap$0 |= 0x8;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.attributes;
        }
    }
    
    private Rule topLevelClasses$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x10) == 0) {
                this.topLevelClasses = this.allEntries(ScalaSigEntryParsers$.MODULE$.topLevelClass());
                this.bitmap$0 |= 0x10;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.topLevelClasses;
        }
    }
    
    private Rule topLevelObjects$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x20) == 0) {
                this.topLevelObjects = this.allEntries(ScalaSigEntryParsers$.MODULE$.topLevelObject());
                this.bitmap$0 |= 0x20;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.topLevelObjects;
        }
    }
    
    @Override
    public <In extends Memoisable, Out, A, X> Rule<In, Out, A, X> memo(final Object key, final Function0<Function1<In, Result<Out, A, X>>> toRule) {
        return (Rule<In, Out, A, X>)MemoisableRules$class.memo(this, key, toRule);
    }
    
    @Override
    public <In, Out, A, X> Rule<In, Out, A, X> ruleWithName(final String name, final Function1<In, Result<Out, A, X>> f) {
        return (Rule<In, Out, A, X>)MemoisableRules$class.ruleWithName(this, name, f);
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
    public <In, Out, A, Any> Function1<In, A> expect(final Rule<In, Out, A, Any> rule) {
        return (Function1<In, A>)Rules$class.expect(this, rule);
    }
    
    public Rule<ScalaSig, ScalaSig, Seq<$tilde<Object, ByteCode>>, Nothing$> symTab() {
        return this.symTab;
    }
    
    public Rule<ScalaSig, ScalaSig, Object, Nothing$> size() {
        return this.size;
    }
    
    public Rule<ScalaSig, ScalaSig.Entry, Object, Nothing$> entry(final int index) {
        return this.memo(new Tuple2((Object)"entry", (Object)BoxesRunTime.boxToInteger(index)), (scala.Function0<scala.Function1<ScalaSig, Result<ScalaSig.Entry, Object, Nothing$>>>)new ScalaSigParsers$$anonfun$entry.ScalaSigParsers$$anonfun$entry$1(index));
    }
    
    public <A> Rule<ScalaSig, ScalaSig, A, String> parseEntry(final Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> parser, final int index) {
        return this.entry(index).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigParsers$$anonfun$parseEntry.ScalaSigParsers$$anonfun$parseEntry$1((Rule)parser)).$greater$greater((scala.Function1<Object, scala.Function1<Object, Result<ScalaSig, A, String>>>)new ScalaSigParsers$$anonfun$parseEntry.ScalaSigParsers$$anonfun$parseEntry$2());
    }
    
    public <A> Rule<ScalaSig, ScalaSig, List<A>, String> allEntries(final Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> f) {
        return this.size().$greater$greater((scala.Function1<Object, scala.Function1<ScalaSig, Result<ScalaSig, List<A>, String>>>)new ScalaSigParsers$$anonfun$allEntries.ScalaSigParsers$$anonfun$allEntries$1((Rule)f));
    }
    
    public Rule<ScalaSig, ScalaSig, List<Object>, String> entries() {
        return ((byte)(this.bitmap$0 & 0x1) == 0) ? this.entries$lzycompute() : this.entries;
    }
    
    public Rule<ScalaSig, ScalaSig, List<Symbol>, String> symbols() {
        return ((byte)(this.bitmap$0 & 0x2) == 0) ? this.symbols$lzycompute() : this.symbols;
    }
    
    public Rule<ScalaSig, ScalaSig, List<MethodSymbol>, String> methods() {
        return ((byte)(this.bitmap$0 & 0x4) == 0) ? this.methods$lzycompute() : this.methods;
    }
    
    public Rule<ScalaSig, ScalaSig, List<AttributeInfo>, String> attributes() {
        return ((byte)(this.bitmap$0 & 0x8) == 0) ? this.attributes$lzycompute() : this.attributes;
    }
    
    public Rule<ScalaSig, ScalaSig, List<ClassSymbol>, String> topLevelClasses() {
        return ((byte)(this.bitmap$0 & 0x10) == 0) ? this.topLevelClasses$lzycompute() : this.topLevelClasses;
    }
    
    public Rule<ScalaSig, ScalaSig, List<ObjectSymbol>, String> topLevelObjects() {
        return ((byte)(this.bitmap$0 & 0x20) == 0) ? this.topLevelObjects$lzycompute() : this.topLevelObjects;
    }
    
    private ScalaSigParsers$() {
        Rules$class.$init$(MODULE$ = this);
        StateRules$class.$init$(this);
        RulesWithState$class.$init$(this);
        MemoisableRules$class.$init$(this);
        this.symTab = (Rule<ScalaSig, ScalaSig, Seq<$tilde<Object, ByteCode>>, Nothing$>)this.read((scala.Function1<Object, Seq<$tilde<Object, ByteCode>>>)new ScalaSigParsers$$anonfun.ScalaSigParsers$$anonfun$10());
        this.size = this.symTab().$up$up((scala.Function1<Seq<$tilde<Object, ByteCode>>, Object>)new ScalaSigParsers$$anonfun.ScalaSigParsers$$anonfun$11());
    }
}
