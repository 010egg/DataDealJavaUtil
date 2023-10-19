// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.immutable.Nil$;
import scala.Product$class;
import scala.Option;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.sys.package$;
import scala.runtime.Nothing$;
import scala.Tuple2;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;
import scala.collection.immutable.List;
import scala.Serializable;
import scala.Product;

public final class NoTypeHints$ implements TypeHints, Product, Serializable
{
    public static final NoTypeHints$ MODULE$;
    private final List<Class<?>> hints;
    private volatile CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints$module;
    
    static {
        new NoTypeHints$();
    }
    
    private CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints$lzycompute() {
        synchronized (this) {
            if (this.org$json4s$TypeHints$$CompositeTypeHints$module == null) {
                this.org$json4s$TypeHints$$CompositeTypeHints$module = new CompositeTypeHints$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.org$json4s$TypeHints$$CompositeTypeHints$module;
        }
    }
    
    @Override
    public CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints() {
        return (this.org$json4s$TypeHints$$CompositeTypeHints$module == null) ? this.org$json4s$TypeHints$$CompositeTypeHints$lzycompute() : this.org$json4s$TypeHints$$CompositeTypeHints$module;
    }
    
    @Override
    public boolean containsHint(final Class<?> clazz) {
        return TypeHints$class.containsHint(this, clazz);
    }
    
    @Override
    public PartialFunction<Tuple2<String, JsonAST.JObject>, Object> deserialize() {
        return (PartialFunction<Tuple2<String, JsonAST.JObject>, Object>)TypeHints$class.deserialize(this);
    }
    
    @Override
    public PartialFunction<Object, JsonAST.JObject> serialize() {
        return (PartialFunction<Object, JsonAST.JObject>)TypeHints$class.serialize(this);
    }
    
    @Override
    public List<TypeHints> components() {
        return (List<TypeHints>)TypeHints$class.components(this);
    }
    
    @Override
    public TypeHints $plus(final TypeHints hints) {
        return TypeHints$class.$plus(this, hints);
    }
    
    @Override
    public List<Class<?>> hints() {
        return this.hints;
    }
    
    public Nothing$ hintFor(final Class<?> clazz) {
        return package$.MODULE$.error("NoTypeHints does not provide any type hints.");
    }
    
    public None$ classFor(final String hint) {
        return None$.MODULE$;
    }
    
    @Override
    public boolean shouldExtractHints(final Class<?> clazz) {
        return false;
    }
    
    public String productPrefix() {
        return "NoTypeHints";
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
        return x$1 instanceof NoTypeHints$;
    }
    
    @Override
    public int hashCode() {
        return -559641903;
    }
    
    @Override
    public String toString() {
        return "NoTypeHints";
    }
    
    private Object readResolve() {
        return NoTypeHints$.MODULE$;
    }
    
    private NoTypeHints$() {
        TypeHints$class.$init$(MODULE$ = this);
        Product$class.$init$((Product)this);
        this.hints = (List<Class<?>>)Nil$.MODULE$;
    }
}
