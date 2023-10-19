// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Serializable;
import scala.Product;
import scala.runtime.Nothing$;

public final class Failure$ extends NoSuccess<Nothing$> implements Product, Serializable
{
    public static final Failure$ MODULE$;
    
    static {
        new Failure$();
    }
    
    public Nothing$ error() {
        throw new ScalaSigParserError("No error");
    }
    
    public String productPrefix() {
        return "Failure";
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
        return x$1 instanceof Failure$;
    }
    
    public int hashCode() {
        return 578079082;
    }
    
    public String toString() {
        return "Failure";
    }
    
    private Object readResolve() {
        return Failure$.MODULE$;
    }
    
    private Failure$() {
        Product$class.$init$((Product)(MODULE$ = this));
    }
}
