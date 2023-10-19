// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Function7;
import scala.Function4;
import scala.Function2;
import scala.Predef$;
import org.json4s.scalap.RulesWithState$class;
import org.json4s.scalap.$tilde;
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
import org.json4s.scalap.RulesWithState;
import scala.collection.Seq;
import scala.Function1;
import scala.runtime.Nothing$;
import scala.Tuple2;
import org.json4s.scalap.Rule;

public final class ClassFileParser$ implements ByteCodeReader
{
    public static final ClassFileParser$ MODULE$;
    private final Rule<ByteCode, ByteCode, Object, String> magicNumber;
    private final Rule<ByteCode, ByteCode, Tuple2<Object, Object>, Nothing$> version;
    private final Rule<ByteCode, ByteCode, ConstantPool, Nothing$> constantPool;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> utf8String;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> intConstant;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> floatConstant;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> longConstant;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> doubleConstant;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> classRef;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> stringRef;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> fieldRef;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> methodRef;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> interfaceMethodRef;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> nameAndType;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> methodHandle;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> methodType;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> invokeDynamic;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> constantModule;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> constantPackage;
    private final Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> constantPoolEntry;
    private final Rule<ByteCode, ByteCode, Seq<Object>, Nothing$> interfaces;
    private final Rule<ByteCode, ByteCode, Attribute, Nothing$> attribute;
    private final Rule<ByteCode, ByteCode, Seq<Attribute>, Nothing$> attributes;
    private final Rule<ByteCode, ByteCode, ClassFileParser.AnnotationElement, String> element_value_pair;
    private final Rule<ByteCode, ByteCode, ClassFileParser.Annotation, String> annotation;
    private final Rule<ByteCode, ByteCode, Seq<ClassFileParser.Annotation>, String> annotations;
    private final Rule<ByteCode, ByteCode, Field, Nothing$> field;
    private final Rule<ByteCode, ByteCode, Seq<Field>, Nothing$> fields;
    private final Rule<ByteCode, ByteCode, Method, Nothing$> method;
    private final Rule<ByteCode, ByteCode, Seq<Method>, Nothing$> methods;
    private final Rule<ByteCode, ByteCode, ClassFileHeader, String> header;
    private final Rule<ByteCode, ByteCode, ClassFile, String> classFile;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> byte;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> u1;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> u2;
    private final Rule<ByteCode, ByteCode, Object, Nothing$> u4;
    private final RulesWithState factory;
    
    static {
        new ClassFileParser$();
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
    
    public ClassFile parse(final ByteCode byteCode) {
        return (ClassFile)this.expect(this.classFile()).apply((Object)byteCode);
    }
    
    public Seq<ClassFileParser.Annotation> parseAnnotations(final ByteCode byteCode) {
        return (Seq<ClassFileParser.Annotation>)this.expect(this.annotations()).apply((Object)byteCode);
    }
    
    public Rule<ByteCode, ByteCode, Object, String> magicNumber() {
        return this.magicNumber;
    }
    
    public Rule<ByteCode, ByteCode, Tuple2<Object, Object>, Nothing$> version() {
        return this.version;
    }
    
    public Rule<ByteCode, ByteCode, ConstantPool, Nothing$> constantPool() {
        return this.constantPool;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> utf8String() {
        return this.utf8String;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> intConstant() {
        return this.intConstant;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> floatConstant() {
        return this.floatConstant;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> longConstant() {
        return this.longConstant;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> doubleConstant() {
        return this.doubleConstant;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> classRef() {
        return this.classRef;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> stringRef() {
        return this.stringRef;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> fieldRef() {
        return this.fieldRef;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> methodRef() {
        return this.methodRef;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> interfaceMethodRef() {
        return this.interfaceMethodRef;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> nameAndType() {
        return this.nameAndType;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> methodHandle() {
        return this.methodHandle;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> methodType() {
        return this.methodType;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> invokeDynamic() {
        return this.invokeDynamic;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> constantModule() {
        return this.constantModule;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> constantPackage() {
        return this.constantPackage;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> constantPoolEntry() {
        return this.constantPoolEntry;
    }
    
    public Rule<ByteCode, ByteCode, Seq<Object>, Nothing$> interfaces() {
        return this.interfaces;
    }
    
    public Rule<ByteCode, ByteCode, Attribute, Nothing$> attribute() {
        return this.attribute;
    }
    
    public Rule<ByteCode, ByteCode, Seq<Attribute>, Nothing$> attributes() {
        return this.attributes;
    }
    
    public Rule<ByteCode, ByteCode, ClassFileParser.ElementValue, String> element_value() {
        return this.u1().$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, ClassFileParser.ElementValue, String>>>)new ClassFileParser$$anonfun$element_value.ClassFileParser$$anonfun$element_value$1());
    }
    
    public Rule<ByteCode, ByteCode, ClassFileParser.AnnotationElement, String> element_value_pair() {
        return this.element_value_pair;
    }
    
    public Rule<ByteCode, ByteCode, ClassFileParser.Annotation, String> annotation() {
        return this.annotation;
    }
    
    public Rule<ByteCode, ByteCode, Seq<ClassFileParser.Annotation>, String> annotations() {
        return this.annotations;
    }
    
    public Rule<ByteCode, ByteCode, Field, Nothing$> field() {
        return this.field;
    }
    
    public Rule<ByteCode, ByteCode, Seq<Field>, Nothing$> fields() {
        return this.fields;
    }
    
    public Rule<ByteCode, ByteCode, Method, Nothing$> method() {
        return this.method;
    }
    
    public Rule<ByteCode, ByteCode, Seq<Method>, Nothing$> methods() {
        return this.methods;
    }
    
    public Rule<ByteCode, ByteCode, ClassFileHeader, String> header() {
        return this.header;
    }
    
    public Rule<ByteCode, ByteCode, ClassFile, String> classFile() {
        return this.classFile;
    }
    
    public Rule<ByteCode, ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$> memberRef(final String description) {
        return this.u2().$tilde((scala.Function0<Rule<ByteCode, ByteCode, Object, Nothing$>>)new ClassFileParser$$anonfun$memberRef.ClassFileParser$$anonfun$memberRef$1()).$up$up((scala.Function1<$tilde<Object, Object>, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun$memberRef.ClassFileParser$$anonfun$memberRef$3((Function1)new ClassFileParser$$anonfun$memberRef.ClassFileParser$$anonfun$memberRef$2(description)));
    }
    
    public <T> ConstantPool add1(final Function1<T, Function1<ConstantPool, Object>> f, final T raw, final ConstantPool pool) {
        return pool.add((Function1<ConstantPool, Object>)f.apply((Object)raw));
    }
    
    public <T> ConstantPool add2(final Function1<T, Function1<ConstantPool, Object>> f, final T raw, final ConstantPool pool) {
        return pool.add((Function1<ConstantPool, Object>)f.apply((Object)raw)).add((Function1<ConstantPool, Object>)new ClassFileParser$$anonfun$add2.ClassFileParser$$anonfun$add2$1());
    }
    
    private ClassFileParser$() {
        Rules$class.$init$(MODULE$ = this);
        StateRules$class.$init$(this);
        RulesWithState$class.$init$(this);
        ByteCodeReader$class.$init$(this);
        this.magicNumber = this.u4().filter((scala.Function1<Object, Object>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$1()).$bar((scala.Function0<Rule<ByteCode, ByteCode, Object, String>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$6());
        this.version = this.u2().$tilde((scala.Function0<Rule<ByteCode, ByteCode, Object, Nothing$>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$7()).$up$up((scala.Function1<$tilde<Object, Object>, Tuple2<Object, Object>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$8());
        this.constantPool = this.u2().$up$up((scala.Function1<Object, Object>)ConstantPool$.MODULE$).$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, ConstantPool, Nothing$>>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$10((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$9()));
        this.utf8String = this.u2().$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, Object, Nothing$>>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$11()).$up$up((scala.Function1<Object, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$13((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$12()));
        this.intConstant = this.u4().$up$up((scala.Function1<Object, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$15((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$14()));
        this.floatConstant = this.bytes(4).$up$up((scala.Function1<ByteCode, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$17((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$16()));
        this.longConstant = this.bytes(8).$up$up((scala.Function1<ByteCode, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$19((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$18()));
        this.doubleConstant = this.bytes(8).$up$up((scala.Function1<ByteCode, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$21((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$20()));
        this.classRef = this.u2().$up$up((scala.Function1<Object, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$23((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$22()));
        this.stringRef = this.u2().$up$up((scala.Function1<Object, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$25((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$24()));
        this.fieldRef = this.memberRef("Field");
        this.methodRef = this.memberRef("Method");
        this.interfaceMethodRef = this.memberRef("InterfaceMethod");
        this.nameAndType = this.u2().$tilde((scala.Function0<Rule<ByteCode, ByteCode, Object, Nothing$>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$26()).$up$up((scala.Function1<$tilde<Object, Object>, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$28((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$27()));
        this.methodHandle = this.u1().$tilde((scala.Function0<Rule<ByteCode, ByteCode, Object, Nothing$>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$29()).$up$up((scala.Function1<$tilde<Object, Object>, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$31((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$30()));
        this.methodType = this.u2().$up$up((scala.Function1<Object, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$33((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$32()));
        this.invokeDynamic = this.u2().$tilde((scala.Function0<Rule<ByteCode, ByteCode, Object, Nothing$>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$34()).$up$up((scala.Function1<$tilde<Object, Object>, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$36((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$35()));
        this.constantModule = this.u2().$up$up((scala.Function1<Object, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$38((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$37()));
        this.constantPackage = this.u2().$up$up((scala.Function1<Object, Function1<ConstantPool, ConstantPool>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$40((Function1)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$39()));
        this.constantPoolEntry = this.u1().$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, Function1<ConstantPool, ConstantPool>, Nothing$>>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$41());
        this.interfaces = this.u2().$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, Seq<Object>, Nothing$>>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$42((SeqRule)this.seqRule(this.u2())));
        this.attribute = this.u2().$tilde((scala.Function0<Rule<ByteCode, ByteCode, Object, Nothing$>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$43()).$up$tilde$up((scala.Function2<Object, Object, Attribute>)Attribute$.MODULE$, (scala.Function1<$tilde<Object, Object>, $tilde<Object, Object>>)Predef$.MODULE$.$conforms());
        this.attributes = this.u2().$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, Seq<Attribute>, Nothing$>>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$44((SeqRule)this.seqRule(this.attribute())));
        this.element_value_pair = this.u2().$tilde((scala.Function0<Rule<ByteCode, ByteCode, Object, String>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$45()).$up$tilde$up((scala.Function2<Object, Object, ClassFileParser.AnnotationElement>)ClassFileParser.AnnotationElement$.MODULE$, (scala.Function1<$tilde<Object, Object>, $tilde<Object, Object>>)Predef$.MODULE$.$conforms());
        this.annotation = this.u2().$tilde((scala.Function0<Rule<ByteCode, ByteCode, Object, String>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$46()).$up$tilde$up((scala.Function2<Object, Object, ClassFileParser.Annotation>)ClassFileParser.Annotation$.MODULE$, (scala.Function1<$tilde<Object, Object>, $tilde<Object, Object>>)Predef$.MODULE$.$conforms());
        this.annotations = this.u2().$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, Seq<ClassFileParser.Annotation>, String>>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$47((SeqRule)this.seqRule(this.annotation())));
        this.field = this.u2().$tilde((scala.Function0<Rule<ByteCode, Object, Object, Object>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$48()).$tilde((scala.Function0<Rule<Object, Object, Object, Object>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$49()).$tilde((scala.Function0<Rule<Object, ByteCode, Object, Nothing$>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$50()).$up$tilde$tilde$tilde$up((scala.Function4<Object, Object, Object, Object, Field>)Field$.MODULE$, (scala.Function1<$tilde<$tilde<$tilde<Object, Object>, Object>, Object>, $tilde<$tilde<$tilde<Object, Object>, Object>, Object>>)Predef$.MODULE$.$conforms());
        this.fields = this.u2().$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, Seq<Field>, Nothing$>>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$51((SeqRule)this.seqRule(this.field())));
        this.method = this.u2().$tilde((scala.Function0<Rule<ByteCode, Object, Object, Object>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$52()).$tilde((scala.Function0<Rule<Object, Object, Object, Object>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$53()).$tilde((scala.Function0<Rule<Object, ByteCode, Object, Nothing$>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$54()).$up$tilde$tilde$tilde$up((scala.Function4<Object, Object, Object, Object, Method>)Method$.MODULE$, (scala.Function1<$tilde<$tilde<$tilde<Object, Object>, Object>, Object>, $tilde<$tilde<$tilde<Object, Object>, Object>, Object>>)Predef$.MODULE$.$conforms());
        this.methods = this.u2().$greater$greater((scala.Function1<Object, scala.Function1<ByteCode, Result<ByteCode, Seq<Method>, Nothing$>>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$55((SeqRule)this.seqRule(this.method())));
        this.header = (Rule<ByteCode, ByteCode, ClassFileHeader, String>)this.magicNumber().$minus$tilde((scala.Function0<Rule<ByteCode, Object, Object, Object>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$56()).$up$tilde$tilde$tilde$tilde$tilde$tilde$up((Function7)ClassFileHeader$.MODULE$, (Function1)Predef$.MODULE$.$conforms());
        this.classFile = this.header().$tilde((scala.Function0<Rule<ByteCode, Object, Object, Object>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$57()).$tilde((scala.Function0<Rule<Object, Object, Object, Object>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$58()).$tilde((scala.Function0<Rule<Object, Object, Object, Object>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$59()).$tilde$minus((scala.Function0<Rule<Object, ByteCode, Object, String>>)new ClassFileParser$$anonfun.ClassFileParser$$anonfun$60()).$up$tilde$tilde$tilde$up((scala.Function4<Object, Object, Object, Object, ClassFile>)ClassFile$.MODULE$, (scala.Function1<$tilde<$tilde<$tilde<ClassFileHeader, Object>, Object>, Object>, $tilde<$tilde<$tilde<Object, Object>, Object>, Object>>)Predef$.MODULE$.$conforms());
    }
}
