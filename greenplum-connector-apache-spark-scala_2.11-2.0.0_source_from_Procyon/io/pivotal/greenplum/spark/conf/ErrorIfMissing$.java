// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.conf;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Serializable;
import scala.Product;

public final class ErrorIfMissing$ implements WhatIfMissing, Product, Serializable
{
    public static final ErrorIfMissing$ MODULE$;
    
    static {
        new ErrorIfMissing$();
    }
    
    public String productPrefix() {
        return "ErrorIfMissing";
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
        return x$1 instanceof ErrorIfMissing$;
    }
    
    @Override
    public int hashCode() {
        return 709649537;
    }
    
    @Override
    public String toString() {
        return "ErrorIfMissing";
    }
    
    private Object readResolve() {
        return ErrorIfMissing$.MODULE$;
    }
    
    private ErrorIfMissing$() {
        Product$class.$init$((Product)(MODULE$ = this));
    }
}
