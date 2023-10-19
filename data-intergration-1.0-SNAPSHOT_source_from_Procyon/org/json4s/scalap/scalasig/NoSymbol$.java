// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.Option;
import scala.collection.Seq;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.collection.immutable.Nil$;
import scala.None$;
import scala.Serializable;
import scala.Product;

public final class NoSymbol$ implements Symbol, Product, Serializable
{
    public static final NoSymbol$ MODULE$;
    
    static {
        new NoSymbol$();
    }
    
    @Override
    public String path() {
        return Symbol$class.path(this);
    }
    
    public boolean isImplicit() {
        return Flags$class.isImplicit(this);
    }
    
    public boolean isFinal() {
        return Flags$class.isFinal(this);
    }
    
    public boolean isPrivate() {
        return Flags$class.isPrivate(this);
    }
    
    public boolean isProtected() {
        return Flags$class.isProtected(this);
    }
    
    public boolean isSealed() {
        return Flags$class.isSealed(this);
    }
    
    public boolean isOverride() {
        return Flags$class.isOverride(this);
    }
    
    public boolean isCase() {
        return Flags$class.isCase(this);
    }
    
    public boolean isAbstract() {
        return Flags$class.isAbstract(this);
    }
    
    public boolean isDeferred() {
        return Flags$class.isDeferred(this);
    }
    
    public boolean isMethod() {
        return Flags$class.isMethod(this);
    }
    
    public boolean isModule() {
        return Flags$class.isModule(this);
    }
    
    public boolean isInterface() {
        return Flags$class.isInterface(this);
    }
    
    public boolean isMutable() {
        return Flags$class.isMutable(this);
    }
    
    public boolean isParam() {
        return Flags$class.isParam(this);
    }
    
    public boolean isPackage() {
        return Flags$class.isPackage(this);
    }
    
    public boolean isDeprecated() {
        return Flags$class.isDeprecated(this);
    }
    
    public boolean isCovariant() {
        return Flags$class.isCovariant(this);
    }
    
    public boolean isCaptured() {
        return Flags$class.isCaptured(this);
    }
    
    public boolean isByNameParam() {
        return Flags$class.isByNameParam(this);
    }
    
    public boolean isContravariant() {
        return Flags$class.isContravariant(this);
    }
    
    public boolean isLabel() {
        return Flags$class.isLabel(this);
    }
    
    public boolean isInConstructor() {
        return Flags$class.isInConstructor(this);
    }
    
    public boolean isAbstractOverride() {
        return Flags$class.isAbstractOverride(this);
    }
    
    public boolean isLocal() {
        return Flags$class.isLocal(this);
    }
    
    public boolean isJava() {
        return Flags$class.isJava(this);
    }
    
    public boolean isSynthetic() {
        return Flags$class.isSynthetic(this);
    }
    
    public boolean isStable() {
        return Flags$class.isStable(this);
    }
    
    public boolean isStatic() {
        return Flags$class.isStatic(this);
    }
    
    public boolean isCaseAccessor() {
        return Flags$class.isCaseAccessor(this);
    }
    
    public boolean isTrait() {
        return Flags$class.isTrait(this);
    }
    
    public boolean isBridge() {
        return Flags$class.isBridge(this);
    }
    
    public boolean isAccessor() {
        return Flags$class.isAccessor(this);
    }
    
    public boolean isSuperAccessor() {
        return Flags$class.isSuperAccessor(this);
    }
    
    public boolean isParamAccessor() {
        return Flags$class.isParamAccessor(this);
    }
    
    public boolean isModuleVar() {
        return Flags$class.isModuleVar(this);
    }
    
    public boolean isMonomorphic() {
        return Flags$class.isMonomorphic(this);
    }
    
    public boolean isLazy() {
        return Flags$class.isLazy(this);
    }
    
    public boolean isError() {
        return Flags$class.isError(this);
    }
    
    public boolean isOverloaded() {
        return Flags$class.isOverloaded(this);
    }
    
    public boolean isLifted() {
        return Flags$class.isLifted(this);
    }
    
    public boolean isMixedIn() {
        return Flags$class.isMixedIn(this);
    }
    
    public boolean isExistential() {
        return Flags$class.isExistential(this);
    }
    
    public boolean isExpandedName() {
        return Flags$class.isExpandedName(this);
    }
    
    public boolean isImplementationClass() {
        return Flags$class.isImplementationClass(this);
    }
    
    public boolean isPreSuper() {
        return Flags$class.isPreSuper(this);
    }
    
    @Override
    public String name() {
        return "<no symbol>";
    }
    
    public None$ parent() {
        return None$.MODULE$;
    }
    
    public boolean hasFlag(final long flag) {
        return false;
    }
    
    public Nil$ children() {
        return Nil$.MODULE$;
    }
    
    public String productPrefix() {
        return "NoSymbol";
    }
    
    public int productArity() {
        return 0;
    }
    
    public Object productElement(final int x$1) {
        throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof NoSymbol$;
    }
    
    @Override
    public int hashCode() {
        return 720223961;
    }
    
    @Override
    public String toString() {
        return "NoSymbol";
    }
    
    private Object readResolve() {
        return NoSymbol$.MODULE$;
    }
    
    private NoSymbol$() {
        Flags$class.$init$(MODULE$ = this);
        Symbol$class.$init$(this);
        Product$class.$init$((Product)this);
    }
}
