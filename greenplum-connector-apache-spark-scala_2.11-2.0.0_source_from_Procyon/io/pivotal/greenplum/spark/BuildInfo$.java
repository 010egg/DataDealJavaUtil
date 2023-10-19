// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.collection.Seq;
import scala.collection.immutable.StringOps;
import scala.Predef$;
import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Serializable;
import scala.Product;

public final class BuildInfo$ implements Product, Serializable
{
    public static final BuildInfo$ MODULE$;
    private final String name;
    private final String version;
    private final String scalaVersion;
    private final String sbtVersion;
    private final String toString;
    
    static {
        new BuildInfo$();
    }
    
    public String name() {
        return this.name;
    }
    
    public String version() {
        return this.version;
    }
    
    public String scalaVersion() {
        return this.scalaVersion;
    }
    
    public String sbtVersion() {
        return this.sbtVersion;
    }
    
    @Override
    public String toString() {
        return this.toString;
    }
    
    public String productPrefix() {
        return "BuildInfo";
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
        return x$1 instanceof BuildInfo$;
    }
    
    @Override
    public int hashCode() {
        return 602658844;
    }
    
    private Object readResolve() {
        return BuildInfo$.MODULE$;
    }
    
    private BuildInfo$() {
        Product$class.$init$((Product)(MODULE$ = this));
        this.name = "greenplum-spark";
        this.version = "2.0.0";
        this.scalaVersion = "2.11.12";
        this.sbtVersion = "1.2.6";
        this.toString = new StringOps(Predef$.MODULE$.augmentString("name: %s, version: %s, scalaVersion: %s, sbtVersion: %s")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.name(), this.version(), this.scalaVersion(), this.sbtVersion() }));
    }
}
