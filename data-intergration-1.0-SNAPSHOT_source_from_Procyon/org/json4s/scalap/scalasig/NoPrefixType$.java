// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Serializable;
import scala.Product;

public final class NoPrefixType$ extends Type implements Product, Serializable
{
    public static final NoPrefixType$ MODULE$;
    
    static {
        new NoPrefixType$();
    }
    
    public String productPrefix() {
        return "NoPrefixType";
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
        return x$1 instanceof NoPrefixType$;
    }
    
    public int hashCode() {
        return 238607789;
    }
    
    public String toString() {
        return "NoPrefixType";
    }
    
    private Object readResolve() {
        return NoPrefixType$.MODULE$;
    }
    
    private NoPrefixType$() {
        Product$class.$init$((Product)(MODULE$ = this));
    }
}
