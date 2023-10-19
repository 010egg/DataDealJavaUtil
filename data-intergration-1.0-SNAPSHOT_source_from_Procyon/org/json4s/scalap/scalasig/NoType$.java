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

public final class NoType$ extends Type implements Product, Serializable
{
    public static final NoType$ MODULE$;
    
    static {
        new NoType$();
    }
    
    public String productPrefix() {
        return "NoType";
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
        return x$1 instanceof NoType$;
    }
    
    public int hashCode() {
        return -1956760389;
    }
    
    public String toString() {
        return "NoType";
    }
    
    private Object readResolve() {
        return NoType$.MODULE$;
    }
    
    private NoType$() {
        Product$class.$init$((Product)(MODULE$ = this));
    }
}
