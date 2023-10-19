// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.runtime.BoxedUnit;
import scala.Function1;
import org.json4s.scalap.Rule;
import scala.collection.TraversableLike;
import scala.collection.Seq;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001%4Q!\u0001\u0002\u0002\u0002-\u0011abU2bY\u0006\u001c\u0016nZ*z[\n|GN\u0003\u0002\u0004\t\u0005A1oY1mCNLwM\u0003\u0002\u0006\r\u000511oY1mCBT!a\u0002\u0005\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005I\u0011aA8sO\u000e\u00011c\u0001\u0001\r%A\u0011Q\u0002E\u0007\u0002\u001d)\tq\"A\u0003tG\u0006d\u0017-\u0003\u0002\u0012\u001d\t1\u0011I\\=SK\u001a\u0004\"a\u0005\u000b\u000e\u0003\tI!!\u0006\u0002\u0003\rMKXNY8m\u0011\u00159\u0002\u0001\"\u0001\u0019\u0003\u0019a\u0014N\\5u}Q\t\u0011\u0004\u0005\u0002\u0014\u0001!)1\u0004\u0001C\u00019\u0005I\u0011\r\u001d9msJ+H.Z\u000b\u0003;\u0001\"\"AH\u0015\u0011\u0005}\u0001C\u0002\u0001\u0003\u0006Ci\u0011\rA\t\u0002\u0002\u0003F\u00111E\n\t\u0003\u001b\u0011J!!\n\b\u0003\u000f9{G\u000f[5oOB\u0011QbJ\u0005\u0003Q9\u00111!\u00118z\u0011\u0015Q#\u00041\u0001,\u0003\u0011\u0011X\u000f\\3\u0011\u00071zcD\u0004\u0002\u0014[%\u0011aFA\u0001\u0015'\u000e\fG.Y*jO\u0016sGO]=QCJ\u001cXM]:\n\u0005A\n$aC#oiJL\b+\u0019:tKJT!A\f\u0002\t\u000bM\u0002A\u0011\u0001\u001b\u0002#\u0005\u0004\b\u000f\\=TG\u0006d\u0017mU5h%VdW-\u0006\u00026oQ\u0011a\u0007\u000f\t\u0003?]\"Q!\t\u001aC\u0002\tBQA\u000b\u001aA\u0002e\u00022AO\u001f7\u001d\t\u00192(\u0003\u0002=\u0005\u0005y1kY1mCNKw\rU1sg\u0016\u00148/\u0003\u0002?\u007f\t1\u0001+\u0019:tKJT!\u0001\u0010\u0002\t\u000b\u0005\u0003a\u0011\u0001\"\u0002\u000b\u0015tGO]=\u0016\u0003\r\u0003\"\u0001R$\u0011\u0005M)\u0015B\u0001$\u0003\u0005!\u00196-\u00197b'&<\u0017B\u0001%F\u0005\u0015)e\u000e\u001e:z\u0011\u0015Q\u0005\u0001\"\u0001L\u0003\u0015Ig\u000eZ3y+\u0005a\u0005CA\u0007N\u0013\tqeBA\u0002J]RD\u0001\u0002\u0015\u0001\t\u0006\u0004%\t!U\u0001\tG\"LG\u000e\u001a:f]V\t!\u000bE\u0002T7Jq!\u0001V-\u000f\u0005UCV\"\u0001,\u000b\u0005]S\u0011A\u0002\u001fs_>$h(C\u0001\u0010\u0013\tQf\"A\u0004qC\u000e\\\u0017mZ3\n\u0005qk&aA*fc*\u0011!L\u0004\u0005\t?\u0002A\t\u0011)Q\u0005%\u0006I1\r[5mIJ,g\u000e\t\u0005\tC\u0002A)\u0019!C\u0001E\u0006Q\u0011\r\u001e;sS\n,H/Z:\u0016\u0003\r\u00042aU.e!\t\u0019R-\u0003\u0002g\u0005\ti\u0011\t\u001e;sS\n,H/Z%oM>D\u0001\u0002\u001b\u0001\t\u0002\u0003\u0006KaY\u0001\fCR$(/\u001b2vi\u0016\u001c\b\u0005")
public abstract class ScalaSigSymbol implements Symbol
{
    private Seq<Symbol> children;
    private Seq<AttributeInfo> attributes;
    private volatile byte bitmap$0;
    
    private Seq children$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x1) == 0) {
                this.children = (Seq<Symbol>)this.applyScalaSigRule((Rule<ScalaSig, ScalaSig, TraversableLike, String>)ScalaSigParsers$.MODULE$.symbols()).filter((Function1)new ScalaSigSymbol$$anonfun$children.ScalaSigSymbol$$anonfun$children$1(this));
                this.bitmap$0 |= 0x1;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.children;
        }
    }
    
    private Seq attributes$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x2) == 0) {
                this.attributes = (Seq<AttributeInfo>)this.applyScalaSigRule((Rule<ScalaSig, ScalaSig, TraversableLike, String>)ScalaSigParsers$.MODULE$.attributes()).filter((Function1)new ScalaSigSymbol$$anonfun$attributes.ScalaSigSymbol$$anonfun$attributes$1(this));
                this.bitmap$0 |= 0x2;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.attributes;
        }
    }
    
    @Override
    public String path() {
        return Symbol$class.path(this);
    }
    
    @Override
    public boolean isImplicit() {
        return Flags$class.isImplicit(this);
    }
    
    @Override
    public boolean isFinal() {
        return Flags$class.isFinal(this);
    }
    
    @Override
    public boolean isPrivate() {
        return Flags$class.isPrivate(this);
    }
    
    @Override
    public boolean isProtected() {
        return Flags$class.isProtected(this);
    }
    
    @Override
    public boolean isSealed() {
        return Flags$class.isSealed(this);
    }
    
    @Override
    public boolean isOverride() {
        return Flags$class.isOverride(this);
    }
    
    @Override
    public boolean isCase() {
        return Flags$class.isCase(this);
    }
    
    @Override
    public boolean isAbstract() {
        return Flags$class.isAbstract(this);
    }
    
    @Override
    public boolean isDeferred() {
        return Flags$class.isDeferred(this);
    }
    
    @Override
    public boolean isMethod() {
        return Flags$class.isMethod(this);
    }
    
    @Override
    public boolean isModule() {
        return Flags$class.isModule(this);
    }
    
    @Override
    public boolean isInterface() {
        return Flags$class.isInterface(this);
    }
    
    @Override
    public boolean isMutable() {
        return Flags$class.isMutable(this);
    }
    
    @Override
    public boolean isParam() {
        return Flags$class.isParam(this);
    }
    
    @Override
    public boolean isPackage() {
        return Flags$class.isPackage(this);
    }
    
    @Override
    public boolean isDeprecated() {
        return Flags$class.isDeprecated(this);
    }
    
    @Override
    public boolean isCovariant() {
        return Flags$class.isCovariant(this);
    }
    
    @Override
    public boolean isCaptured() {
        return Flags$class.isCaptured(this);
    }
    
    @Override
    public boolean isByNameParam() {
        return Flags$class.isByNameParam(this);
    }
    
    @Override
    public boolean isContravariant() {
        return Flags$class.isContravariant(this);
    }
    
    @Override
    public boolean isLabel() {
        return Flags$class.isLabel(this);
    }
    
    @Override
    public boolean isInConstructor() {
        return Flags$class.isInConstructor(this);
    }
    
    @Override
    public boolean isAbstractOverride() {
        return Flags$class.isAbstractOverride(this);
    }
    
    @Override
    public boolean isLocal() {
        return Flags$class.isLocal(this);
    }
    
    @Override
    public boolean isJava() {
        return Flags$class.isJava(this);
    }
    
    @Override
    public boolean isSynthetic() {
        return Flags$class.isSynthetic(this);
    }
    
    @Override
    public boolean isStable() {
        return Flags$class.isStable(this);
    }
    
    @Override
    public boolean isStatic() {
        return Flags$class.isStatic(this);
    }
    
    @Override
    public boolean isCaseAccessor() {
        return Flags$class.isCaseAccessor(this);
    }
    
    @Override
    public boolean isTrait() {
        return Flags$class.isTrait(this);
    }
    
    @Override
    public boolean isBridge() {
        return Flags$class.isBridge(this);
    }
    
    @Override
    public boolean isAccessor() {
        return Flags$class.isAccessor(this);
    }
    
    @Override
    public boolean isSuperAccessor() {
        return Flags$class.isSuperAccessor(this);
    }
    
    @Override
    public boolean isParamAccessor() {
        return Flags$class.isParamAccessor(this);
    }
    
    @Override
    public boolean isModuleVar() {
        return Flags$class.isModuleVar(this);
    }
    
    @Override
    public boolean isMonomorphic() {
        return Flags$class.isMonomorphic(this);
    }
    
    @Override
    public boolean isLazy() {
        return Flags$class.isLazy(this);
    }
    
    @Override
    public boolean isError() {
        return Flags$class.isError(this);
    }
    
    @Override
    public boolean isOverloaded() {
        return Flags$class.isOverloaded(this);
    }
    
    @Override
    public boolean isLifted() {
        return Flags$class.isLifted(this);
    }
    
    @Override
    public boolean isMixedIn() {
        return Flags$class.isMixedIn(this);
    }
    
    @Override
    public boolean isExistential() {
        return Flags$class.isExistential(this);
    }
    
    @Override
    public boolean isExpandedName() {
        return Flags$class.isExpandedName(this);
    }
    
    @Override
    public boolean isImplementationClass() {
        return Flags$class.isImplementationClass(this);
    }
    
    @Override
    public boolean isPreSuper() {
        return Flags$class.isPreSuper(this);
    }
    
    public <A> A applyRule(final Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> rule) {
        return (A)ScalaSigEntryParsers$.MODULE$.expect((Rule<Object, Object, Object, Object>)rule).apply((Object)this.entry());
    }
    
    public <A> A applyScalaSigRule(final Rule<ScalaSig, ScalaSig, A, String> rule) {
        return (A)ScalaSigParsers$.MODULE$.expect((Rule<Object, Object, Object, Object>)rule).apply((Object)this.entry().scalaSig());
    }
    
    public abstract ScalaSig.Entry entry();
    
    public int index() {
        return this.entry().index();
    }
    
    @Override
    public Seq<Symbol> children() {
        return (Seq<Symbol>)(((byte)(this.bitmap$0 & 0x1) == 0) ? this.children$lzycompute() : this.children);
    }
    
    public Seq<AttributeInfo> attributes() {
        return (Seq<AttributeInfo>)(((byte)(this.bitmap$0 & 0x2) == 0) ? this.attributes$lzycompute() : this.attributes);
    }
    
    public ScalaSigSymbol() {
        Flags$class.$init$(this);
        Symbol$class.$init$(this);
    }
}
