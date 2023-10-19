// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Function6;
import org.json4s.scalap.RulesWithState$class;
import scala.Option;
import scala.Some;
import org.json4s.scalap.SeqRule;
import org.json4s.scalap.InRule;
import scala.collection.immutable.List;
import scala.None$;
import scala.collection.immutable.Nil$;
import org.json4s.scalap.StateRules;
import org.json4s.scalap.StateRules$class;
import org.json4s.scalap.MemoisableRules$class;
import org.json4s.scalap.Memoisable;
import org.json4s.scalap.Result;
import org.json4s.scalap.Rules;
import org.json4s.scalap.Rules$class;
import scala.Function4;
import scala.Function3;
import org.json4s.scalap.$tilde;
import scala.Function2;
import scala.Function1;
import scala.collection.Seq;
import scala.Predef$;
import scala.runtime.BoxedUnit;
import scala.Function0;
import scala.runtime.Nothing$;
import org.json4s.scalap.Rule;
import org.json4s.scalap.MemoisableRules;
import org.json4s.scalap.RulesWithState;

public final class ScalaSigEntryParsers$ implements RulesWithState, MemoisableRules
{
    public static final ScalaSigEntryParsers$ MODULE$;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$> index;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$> key;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> entry;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> ref;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> termName;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> typeName;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> name;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> nameRef;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, Symbol, String> symbolRef;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, Type, String> typeRef;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> constantRef;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, SymbolInfo, String> symbolInfo;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, NoSymbol$, Nothing$> noSymbol;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, TypeSymbol, String> typeSymbol;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, AliasSymbol, String> aliasSymbol;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, ClassSymbol, String> classSymbol;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, ObjectSymbol, String> objectSymbol;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, MethodSymbol, String> methodSymbol;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, ExternalSymbol, String> extRef;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, ExternalSymbol, String> extModClassRef;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, Symbol, String> symbol;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, ClassSymbol, String> classSymRef;
    private final Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> attribTreeRef;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> typeLevel;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> typeIndex;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, Type, String> typeEntry;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> literal;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, AttributeInfo, String> attributeInfo;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, Children, String> children;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, AnnotInfo, String> annotInfo;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, ClassSymbol, String> topLevelClass;
    private Rule<ScalaSig.Entry, ScalaSig.Entry, ObjectSymbol, String> topLevelObject;
    private final RulesWithState factory;
    private volatile int bitmap$0;
    
    static {
        new ScalaSigEntryParsers$();
    }
    
    private Rule entry$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x1) == 0x0) {
                this.entry = this.symbol().$bar((scala.Function0<Rule<Object, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$entry.ScalaSigEntryParsers$$anonfun$entry$2()).$bar((scala.Function0<Rule<Object, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$entry.ScalaSigEntryParsers$$anonfun$entry$3()).$bar((scala.Function0<Rule<Object, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$entry.ScalaSigEntryParsers$$anonfun$entry$4()).$bar((scala.Function0<Rule<Object, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$entry.ScalaSigEntryParsers$$anonfun$entry$5()).$bar((scala.Function0<Rule<Object, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$entry.ScalaSigEntryParsers$$anonfun$entry$6()).$bar((scala.Function0<Rule<Object, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$entry.ScalaSigEntryParsers$$anonfun$entry$7()).$bar((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String>>)new ScalaSigEntryParsers$$anonfun$entry.ScalaSigEntryParsers$$anonfun$entry$8());
                this.bitmap$0 |= 0x1;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.entry;
        }
    }
    
    private Rule nameRef$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x2) == 0x0) {
                this.nameRef = this.refTo(this.name());
                this.bitmap$0 |= 0x2;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.nameRef;
        }
    }
    
    private Rule symbolRef$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x4) == 0x0) {
                this.symbolRef = this.refTo(this.symbol());
                this.bitmap$0 |= 0x4;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.symbolRef;
        }
    }
    
    private Rule typeRef$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x8) == 0x0) {
                this.typeRef = this.refTo(this.typeEntry());
                this.bitmap$0 |= 0x8;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.typeRef;
        }
    }
    
    private Rule constantRef$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x10) == 0x0) {
                this.constantRef = this.refTo(this.literal());
                this.bitmap$0 |= 0x10;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.constantRef;
        }
    }
    
    private Rule symbol$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x20) == 0x0) {
                this.symbol = this.oneOf((scala.collection.Seq<Rule<ScalaSig.Entry, ScalaSig.Entry, Symbol, String>>)Predef$.MODULE$.wrapRefArray((Object[])new Rule[] { this.noSymbol(), this.typeSymbol(), this.aliasSymbol(), this.classSymbol(), this.objectSymbol(), this.methodSymbol(), this.extRef(), this.extModClassRef() })).as("symbol");
                this.bitmap$0 |= 0x20;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.symbol;
        }
    }
    
    private Rule typeEntry$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x40) == 0x0) {
                this.typeEntry = this.oneOf((scala.collection.Seq<Rule<ScalaSig.Entry, ScalaSig.Entry, Type, String>>)Predef$.MODULE$.wrapRefArray((Object[])new Rule[] { this.entryType(11).$minus$up(NoType$.MODULE$), this.entryType(12).$minus$up(NoPrefixType$.MODULE$), this.entryType(13).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$1()).$up$up((scala.Function1<Object, Object>)ThisType$.MODULE$), this.entryType(14).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$2()).$up$tilde$up((scala.Function2<Object, Object, Object>)SingleType$.MODULE$, (scala.Function1<Object, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()), this.entryType(15).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$3()).$up$up((scala.Function1<Object, Object>)ConstantType$.MODULE$), this.entryType(16).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$4()).$up$tilde$tilde$up((scala.Function3<Object, Object, Object, Object>)TypeRefType$.MODULE$, (scala.Function1<Object, $tilde<$tilde<Object, Object>, Object>>)Predef$.MODULE$.$conforms()), this.entryType(17).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$5()).$up$tilde$up((scala.Function2<Object, Object, Object>)TypeBoundsType$.MODULE$, (scala.Function1<Object, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()), this.entryType(18).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$6()).$up$tilde$up((scala.Function2<Object, Object, Object>)RefinedType$.MODULE$, (scala.Function1<Object, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()), this.entryType(19).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$7()).$up$tilde$up((scala.Function2<Object, Object, Object>)ClassInfoType$.MODULE$, (scala.Function1<Object, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()), this.entryType(20).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$8()).$up$tilde$up((scala.Function2<Object, Object, Object>)MethodType$.MODULE$, (scala.Function1<Object, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()), this.entryType(21).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$9()).$up$tilde$up((scala.Function2<Object, Object, Object>)PolyType$.MODULE$, (scala.Function1<Object, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()), this.entryType(21).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$10()).$up$up((scala.Function1<Object, Object>)NullaryMethodType$.MODULE$), this.entryType(22).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$11()).$up$tilde$up((scala.Function2<Object, Object, Object>)MethodType$.MODULE$, (scala.Function1<Object, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()), this.entryType(42).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$12()).$up$tilde$up((scala.Function2<Object, Object, Object>)AnnotatedType$.MODULE$, (scala.Function1<Object, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()), this.entryType(51).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$13()).$up$tilde$tilde$up((scala.Function3<Object, Object, Object, Object>)AnnotatedWithSelfType$.MODULE$, (scala.Function1<Object, $tilde<$tilde<Object, Object>, Object>>)Predef$.MODULE$.$conforms()), this.entryType(48).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$typeEntry.ScalaSigEntryParsers$$anonfun$typeEntry$14()).$up$tilde$up((scala.Function2<Object, Object, Object>)ExistentialType$.MODULE$, (scala.Function1<Object, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()) })).as("type");
                this.bitmap$0 |= 0x40;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.typeEntry;
        }
    }
    
    private Rule literal$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x80) == 0x0) {
                this.literal = this.oneOf((scala.collection.Seq<Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String>>)Predef$.MODULE$.wrapRefArray((Object[])new Rule[] { this.entryType(24).$minus$up(BoxedUnit.UNIT), this.entryType(25).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$6()).$up$up((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$1()), this.entryType(26).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$7()).$up$up((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$8()), this.entryType(27).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$9()).$up$up((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$10()), this.entryType(28).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$11()).$up$up((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$12()), this.entryType(29).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$13()).$up$up((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$2()), this.entryType(30).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$14()).$up$up((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$3()), this.entryType(31).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$15()).$up$up((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$4()), this.entryType(32).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$16()).$up$up((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$5()), this.entryType(33).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$17()), this.entryType(34).$minus$up((Object)null), this.entryType(35).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$literal.ScalaSigEntryParsers$$anonfun$literal$18()) }));
                this.bitmap$0 |= 0x80;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.literal;
        }
    }
    
    private Rule attributeInfo$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x100) == 0x0) {
                this.attributeInfo = this.entryType(40).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String>>)new ScalaSigEntryParsers$$anonfun$attributeInfo.ScalaSigEntryParsers$$anonfun$attributeInfo$1()).$up$tilde$tilde$tilde$up((scala.Function4<Object, Object, Object, Object, AttributeInfo>)AttributeInfo$.MODULE$, (scala.Function1<Object, $tilde<$tilde<$tilde<Object, Object>, Object>, Object>>)Predef$.MODULE$.$conforms());
                this.bitmap$0 |= 0x100;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.attributeInfo;
        }
    }
    
    private Rule children$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x200) == 0x0) {
                this.children = this.entryType(41).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String>>)new ScalaSigEntryParsers$$anonfun$children.ScalaSigEntryParsers$$anonfun$children$1()).$up$up((scala.Function1<Object, Children>)Children$.MODULE$);
                this.bitmap$0 |= 0x200;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.children;
        }
    }
    
    private Rule annotInfo$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x400) == 0x0) {
                this.annotInfo = this.entryType(43).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String>>)new ScalaSigEntryParsers$$anonfun$annotInfo.ScalaSigEntryParsers$$anonfun$annotInfo$1()).$up$up((scala.Function1<Object, AnnotInfo>)AnnotInfo$.MODULE$);
                this.bitmap$0 |= 0x400;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.annotInfo;
        }
    }
    
    private Rule topLevelClass$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x800) == 0x0) {
                this.topLevelClass = this.classSymbol().filter((scala.Function1<ClassSymbol, Object>)new ScalaSigEntryParsers$$anonfun$topLevelClass.ScalaSigEntryParsers$$anonfun$topLevelClass$1());
                this.bitmap$0 |= 0x800;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.topLevelClass;
        }
    }
    
    private Rule topLevelObject$lzycompute() {
        synchronized (this) {
            if ((this.bitmap$0 & 0x1000) == 0x0) {
                this.topLevelObject = this.objectSymbol().filter((scala.Function1<ObjectSymbol, Object>)new ScalaSigEntryParsers$$anonfun$topLevelObject.ScalaSigEntryParsers$$anonfun$topLevelObject$1());
                this.bitmap$0 |= 0x1000;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.topLevelObject;
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
    
    public <A> Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> byteCodeEntryParser(final Rule<ByteCode, ByteCode, A, String> rule) {
        return (Rule<ScalaSig.Entry, ScalaSig.Entry, A, String>)this.apply((scala.Function1<Object, Result<Object, A, String>>)new ScalaSigEntryParsers$$anonfun$byteCodeEntryParser.ScalaSigEntryParsers$$anonfun$byteCodeEntryParser$1((Rule)rule));
    }
    
    public <A> Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$> toEntry(final int index) {
        return (Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$>)this.apply((scala.Function1<Object, Result<Object, Object, Nothing$>>)new ScalaSigEntryParsers$$anonfun$toEntry.ScalaSigEntryParsers$$anonfun$toEntry$1(index));
    }
    
    public <A> Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> parseEntry(final Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> parser, final int index) {
        return this.toEntry(index).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, A, String>>)new ScalaSigEntryParsers$$anonfun$parseEntry.ScalaSigEntryParsers$$anonfun$parseEntry$3((Rule)parser));
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$> entryType(final int code) {
        return this.key().filter((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun$entryType.ScalaSigEntryParsers$$anonfun$entryType$1(code));
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$> index() {
        return this.index;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$> key() {
        return this.key;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> entry() {
        return ((this.bitmap$0 & 0x1) == 0x0) ? this.entry$lzycompute() : this.entry;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> ref() {
        return this.ref;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> termName() {
        return this.termName;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> typeName() {
        return this.typeName;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> name() {
        return this.name;
    }
    
    public <A> Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> refTo(final Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> rule) {
        return this.ref().$greater$greater$amp((scala.Function1<Object, scala.Function1<ScalaSig.Entry, Result<Object, A, String>>>)new ScalaSigEntryParsers$$anonfun$refTo.ScalaSigEntryParsers$$anonfun$refTo$1((Rule)rule));
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> nameRef() {
        return ((this.bitmap$0 & 0x2) == 0x0) ? this.nameRef$lzycompute() : this.nameRef;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Symbol, String> symbolRef() {
        return ((this.bitmap$0 & 0x4) == 0x0) ? this.symbolRef$lzycompute() : this.symbolRef;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Type, String> typeRef() {
        return ((this.bitmap$0 & 0x8) == 0x0) ? this.typeRef$lzycompute() : this.typeRef;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> constantRef() {
        return ((this.bitmap$0 & 0x10) == 0x0) ? this.constantRef$lzycompute() : this.constantRef;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, SymbolInfo, String> symbolInfo() {
        return this.symbolInfo;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> symHeader(final int key) {
        return this.entryType(key).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun$symHeader.ScalaSigEntryParsers$$anonfun$symHeader$1()).$bar((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String>>)new ScalaSigEntryParsers$$anonfun$symHeader.ScalaSigEntryParsers$$anonfun$symHeader$2(key));
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, SymbolInfo, String> symbolEntry(final int key) {
        return this.symHeader(key).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, SymbolInfo, String>>)new ScalaSigEntryParsers$$anonfun$symbolEntry.ScalaSigEntryParsers$$anonfun$symbolEntry$1());
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, NoSymbol$, Nothing$> noSymbol() {
        return this.noSymbol;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, TypeSymbol, String> typeSymbol() {
        return this.typeSymbol;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, AliasSymbol, String> aliasSymbol() {
        return this.aliasSymbol;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, ClassSymbol, String> classSymbol() {
        return this.classSymbol;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, ObjectSymbol, String> objectSymbol() {
        return this.objectSymbol;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, MethodSymbol, String> methodSymbol() {
        return this.methodSymbol;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, ExternalSymbol, String> extRef() {
        return this.extRef;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, ExternalSymbol, String> extModClassRef() {
        return this.extModClassRef;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Symbol, String> symbol() {
        return ((this.bitmap$0 & 0x20) == 0x0) ? this.symbol$lzycompute() : this.symbol;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, ClassSymbol, String> classSymRef() {
        return this.classSymRef;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> attribTreeRef() {
        return this.attribTreeRef;
    }
    
    public Rule<ByteCode, ByteCode, Object, Nothing$> typeLevel() {
        return this.typeLevel;
    }
    
    public Rule<ByteCode, ByteCode, Object, Nothing$> typeIndex() {
        return this.typeIndex;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Type, String> typeEntry() {
        return ((this.bitmap$0 & 0x40) == 0x0) ? this.typeEntry$lzycompute() : this.typeEntry;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> literal() {
        return ((this.bitmap$0 & 0x80) == 0x0) ? this.literal$lzycompute() : this.literal;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, AttributeInfo, String> attributeInfo() {
        return ((this.bitmap$0 & 0x100) == 0x0) ? this.attributeInfo$lzycompute() : this.attributeInfo;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, Children, String> children() {
        return ((this.bitmap$0 & 0x200) == 0x0) ? this.children$lzycompute() : this.children;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, AnnotInfo, String> annotInfo() {
        return ((this.bitmap$0 & 0x400) == 0x0) ? this.annotInfo$lzycompute() : this.annotInfo;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, ClassSymbol, String> topLevelClass() {
        return ((this.bitmap$0 & 0x800) == 0x0) ? this.topLevelClass$lzycompute() : this.topLevelClass;
    }
    
    public Rule<ScalaSig.Entry, ScalaSig.Entry, ObjectSymbol, String> topLevelObject() {
        return ((this.bitmap$0 & 0x1000) == 0x0) ? this.topLevelObject$lzycompute() : this.topLevelObject;
    }
    
    public boolean isTopLevel(final Symbol symbol) {
        final Option<Symbol> parent = symbol.parent();
        return parent instanceof Some && ((Some)parent).x() instanceof ExternalSymbol;
    }
    
    public boolean isTopLevelClass(final Symbol symbol) {
        return !symbol.isModule() && this.isTopLevel(symbol);
    }
    
    private ScalaSigEntryParsers$() {
        Rules$class.$init$(MODULE$ = this);
        StateRules$class.$init$(this);
        RulesWithState$class.$init$(this);
        MemoisableRules$class.$init$(this);
        this.index = (Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$>)this.read((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$12());
        this.key = (Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$>)this.read((scala.Function1<Object, Object>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$13());
        this.ref = this.byteCodeEntryParser((Rule<ByteCode, ByteCode, Object, String>)ScalaSigAttributeParsers$.MODULE$.nat());
        this.termName = this.entryType(1).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, String, String>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$14());
        this.typeName = this.entryType(2).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, String, String>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$15());
        this.name = this.termName().$bar((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, String, String>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$16()).as("name");
        this.symbolInfo = this.nameRef().$tilde((scala.Function0<Rule<ScalaSig.Entry, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$17()).$tilde((scala.Function0<Rule<Object, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$18()).$tilde((scala.Function0<Rule<Object, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$19()).$tilde((scala.Function0<Rule<Object, Object, Object, Object>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$20()).$tilde((scala.Function0<Rule<Object, ScalaSig.Entry, Object, String>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$21()).$up$tilde$tilde$tilde$tilde$tilde$up((scala.Function6<Object, Object, Object, Object, Object, Object, SymbolInfo>)SymbolInfo$.MODULE$, (scala.Function1<$tilde<$tilde<$tilde<$tilde<$tilde<String, Object>, Object>, Object>, Object>, Object>, $tilde<$tilde<$tilde<$tilde<$tilde<Object, Object>, Object>, Object>, Object>, Object>>)Predef$.MODULE$.$conforms());
        this.noSymbol = this.entryType(3).$minus$up(NoSymbol$.MODULE$);
        this.typeSymbol = this.symbolEntry(4).$up$up((scala.Function1<SymbolInfo, TypeSymbol>)TypeSymbol$.MODULE$).as("typeSymbol");
        this.aliasSymbol = this.symbolEntry(5).$up$up((scala.Function1<SymbolInfo, AliasSymbol>)AliasSymbol$.MODULE$).as("alias");
        this.classSymbol = this.symbolEntry(6).$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$22()).$up$tilde$up((scala.Function2<Object, Object, ClassSymbol>)ClassSymbol$.MODULE$, (scala.Function1<$tilde<SymbolInfo, Object>, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()).as("class");
        this.objectSymbol = this.symbolEntry(7).$up$up((scala.Function1<SymbolInfo, ObjectSymbol>)ObjectSymbol$.MODULE$).as("object");
        this.methodSymbol = this.symHeader(8).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$23()).$up$tilde$up((scala.Function2<Object, Object, MethodSymbol>)MethodSymbol$.MODULE$, (scala.Function1<Object, $tilde<Object, Object>>)Predef$.MODULE$.$conforms()).as("method");
        this.extRef = this.entryType(9).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$24()).$up$tilde$tilde$up((scala.Function3<Object, Object, Object, ExternalSymbol>)ExternalSymbol$.MODULE$, (scala.Function1<Object, $tilde<$tilde<Object, Object>, Object>>)Predef$.MODULE$.$conforms()).as("extRef");
        this.extModClassRef = this.entryType(10).$minus$tilde((scala.Function0<Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String>>)new ScalaSigEntryParsers$$anonfun.ScalaSigEntryParsers$$anonfun$25()).$up$tilde$tilde$up((scala.Function3<Object, Object, Object, ExternalSymbol>)ExternalSymbol$.MODULE$, (scala.Function1<Object, $tilde<$tilde<Object, Object>, Object>>)Predef$.MODULE$.$conforms()).as("extModClassRef");
        this.classSymRef = this.refTo(this.classSymbol());
        this.attribTreeRef = this.ref();
        this.typeLevel = ScalaSigAttributeParsers$.MODULE$.nat();
        this.typeIndex = ScalaSigAttributeParsers$.MODULE$.nat();
    }
}
