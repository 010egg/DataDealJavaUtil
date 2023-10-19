// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Option;
import scala.collection.immutable.Map;
import scala.runtime.BoxedUnit;

public final class DoubleWriters$ implements DoubleWriters
{
    public static final DoubleWriters$ MODULE$;
    private volatile FloatWriter$ FloatWriter$module;
    private volatile DoubleWriter$ DoubleWriter$module;
    private volatile BigDecimalWriter$ BigDecimalWriter$module;
    private volatile DefaultWriters.IntWriter$ IntWriter$module;
    private volatile DefaultWriters.ByteWriter$ ByteWriter$module;
    private volatile DefaultWriters.ShortWriter$ ShortWriter$module;
    private volatile DefaultWriters.LongWriter$ LongWriter$module;
    private volatile DefaultWriters.BigIntWriter$ BigIntWriter$module;
    private volatile DefaultWriters.BooleanWriter$ BooleanWriter$module;
    private volatile DefaultWriters.StringWriter$ StringWriter$module;
    private volatile DefaultWriters.JValueWriter$ JValueWriter$module;
    
    static {
        new DoubleWriters$();
    }
    
    private FloatWriter$ FloatWriter$lzycompute() {
        synchronized (this) {
            if (this.FloatWriter$module == null) {
                this.FloatWriter$module = new FloatWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.FloatWriter$module;
        }
    }
    
    @Override
    public FloatWriter$ FloatWriter() {
        return (this.FloatWriter$module == null) ? this.FloatWriter$lzycompute() : this.FloatWriter$module;
    }
    
    private DoubleWriter$ DoubleWriter$lzycompute() {
        synchronized (this) {
            if (this.DoubleWriter$module == null) {
                this.DoubleWriter$module = new DoubleWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.DoubleWriter$module;
        }
    }
    
    @Override
    public DoubleWriter$ DoubleWriter() {
        return (this.DoubleWriter$module == null) ? this.DoubleWriter$lzycompute() : this.DoubleWriter$module;
    }
    
    private BigDecimalWriter$ BigDecimalWriter$lzycompute() {
        synchronized (this) {
            if (this.BigDecimalWriter$module == null) {
                this.BigDecimalWriter$module = new BigDecimalWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.BigDecimalWriter$module;
        }
    }
    
    @Override
    public BigDecimalWriter$ BigDecimalWriter() {
        return (this.BigDecimalWriter$module == null) ? this.BigDecimalWriter$lzycompute() : this.BigDecimalWriter$module;
    }
    
    private DefaultWriters.IntWriter$ IntWriter$lzycompute() {
        synchronized (this) {
            if (this.IntWriter$module == null) {
                this.IntWriter$module = new DefaultWriters.IntWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.IntWriter$module;
        }
    }
    
    @Override
    public DefaultWriters.IntWriter$ IntWriter() {
        return (this.IntWriter$module == null) ? this.IntWriter$lzycompute() : this.IntWriter$module;
    }
    
    private DefaultWriters.ByteWriter$ ByteWriter$lzycompute() {
        synchronized (this) {
            if (this.ByteWriter$module == null) {
                this.ByteWriter$module = new DefaultWriters.ByteWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.ByteWriter$module;
        }
    }
    
    @Override
    public DefaultWriters.ByteWriter$ ByteWriter() {
        return (this.ByteWriter$module == null) ? this.ByteWriter$lzycompute() : this.ByteWriter$module;
    }
    
    private DefaultWriters.ShortWriter$ ShortWriter$lzycompute() {
        synchronized (this) {
            if (this.ShortWriter$module == null) {
                this.ShortWriter$module = new DefaultWriters.ShortWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.ShortWriter$module;
        }
    }
    
    @Override
    public DefaultWriters.ShortWriter$ ShortWriter() {
        return (this.ShortWriter$module == null) ? this.ShortWriter$lzycompute() : this.ShortWriter$module;
    }
    
    private DefaultWriters.LongWriter$ LongWriter$lzycompute() {
        synchronized (this) {
            if (this.LongWriter$module == null) {
                this.LongWriter$module = new DefaultWriters.LongWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.LongWriter$module;
        }
    }
    
    @Override
    public DefaultWriters.LongWriter$ LongWriter() {
        return (this.LongWriter$module == null) ? this.LongWriter$lzycompute() : this.LongWriter$module;
    }
    
    private DefaultWriters.BigIntWriter$ BigIntWriter$lzycompute() {
        synchronized (this) {
            if (this.BigIntWriter$module == null) {
                this.BigIntWriter$module = new DefaultWriters.BigIntWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.BigIntWriter$module;
        }
    }
    
    @Override
    public DefaultWriters.BigIntWriter$ BigIntWriter() {
        return (this.BigIntWriter$module == null) ? this.BigIntWriter$lzycompute() : this.BigIntWriter$module;
    }
    
    private DefaultWriters.BooleanWriter$ BooleanWriter$lzycompute() {
        synchronized (this) {
            if (this.BooleanWriter$module == null) {
                this.BooleanWriter$module = new DefaultWriters.BooleanWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.BooleanWriter$module;
        }
    }
    
    @Override
    public DefaultWriters.BooleanWriter$ BooleanWriter() {
        return (this.BooleanWriter$module == null) ? this.BooleanWriter$lzycompute() : this.BooleanWriter$module;
    }
    
    private DefaultWriters.StringWriter$ StringWriter$lzycompute() {
        synchronized (this) {
            if (this.StringWriter$module == null) {
                this.StringWriter$module = new DefaultWriters.StringWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.StringWriter$module;
        }
    }
    
    @Override
    public DefaultWriters.StringWriter$ StringWriter() {
        return (this.StringWriter$module == null) ? this.StringWriter$lzycompute() : this.StringWriter$module;
    }
    
    private DefaultWriters.JValueWriter$ JValueWriter$lzycompute() {
        synchronized (this) {
            if (this.JValueWriter$module == null) {
                this.JValueWriter$module = new DefaultWriters.JValueWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.JValueWriter$module;
        }
    }
    
    @Override
    public DefaultWriters.JValueWriter$ JValueWriter() {
        return (this.JValueWriter$module == null) ? this.JValueWriter$lzycompute() : this.JValueWriter$module;
    }
    
    @Override
    public <T> Writer<Object> arrayWriter(final Writer<T> valueWriter) {
        return (Writer<Object>)DefaultWriters$class.arrayWriter(this, valueWriter);
    }
    
    @Override
    public <V> Writer<Map<String, V>> mapWriter(final Writer<V> valueWriter) {
        return (Writer<Map<String, V>>)DefaultWriters$class.mapWriter(this, valueWriter);
    }
    
    @Override
    public <T> Writer<Option<T>> OptionWriter(final Writer<T> valueWriter) {
        return (Writer<Option<T>>)DefaultWriters$class.OptionWriter(this, valueWriter);
    }
    
    private DoubleWriters$() {
        DefaultWriters$class.$init$(MODULE$ = this);
        DoubleWriters$class.$init$(this);
    }
}
