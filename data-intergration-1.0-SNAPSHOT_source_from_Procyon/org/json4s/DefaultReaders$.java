// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.generic.CanBuildFrom;
import scala.reflect.Manifest;
import scala.collection.immutable.Map;
import scala.runtime.BoxedUnit;

public final class DefaultReaders$ implements DefaultReaders
{
    public static final DefaultReaders$ MODULE$;
    private volatile IntReader$ IntReader$module;
    private volatile BigIntReader$ BigIntReader$module;
    private volatile LongReader$ LongReader$module;
    private volatile ShortReader$ ShortReader$module;
    private volatile ByteReader$ ByteReader$module;
    private volatile FloatReader$ FloatReader$module;
    private volatile DoubleReader$ DoubleReader$module;
    private volatile BigDecimalReader$ BigDecimalReader$module;
    private volatile BooleanReader$ BooleanReader$module;
    private volatile StringReader$ StringReader$module;
    private volatile JValueReader$ JValueReader$module;
    private volatile JObjectReader$ JObjectReader$module;
    private volatile JArrayReader$ JArrayReader$module;
    
    static {
        new DefaultReaders$();
    }
    
    private IntReader$ IntReader$lzycompute() {
        synchronized (this) {
            if (this.IntReader$module == null) {
                this.IntReader$module = new IntReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.IntReader$module;
        }
    }
    
    @Override
    public IntReader$ IntReader() {
        return (this.IntReader$module == null) ? this.IntReader$lzycompute() : this.IntReader$module;
    }
    
    private BigIntReader$ BigIntReader$lzycompute() {
        synchronized (this) {
            if (this.BigIntReader$module == null) {
                this.BigIntReader$module = new BigIntReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.BigIntReader$module;
        }
    }
    
    @Override
    public BigIntReader$ BigIntReader() {
        return (this.BigIntReader$module == null) ? this.BigIntReader$lzycompute() : this.BigIntReader$module;
    }
    
    private LongReader$ LongReader$lzycompute() {
        synchronized (this) {
            if (this.LongReader$module == null) {
                this.LongReader$module = new LongReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.LongReader$module;
        }
    }
    
    @Override
    public LongReader$ LongReader() {
        return (this.LongReader$module == null) ? this.LongReader$lzycompute() : this.LongReader$module;
    }
    
    private ShortReader$ ShortReader$lzycompute() {
        synchronized (this) {
            if (this.ShortReader$module == null) {
                this.ShortReader$module = new ShortReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.ShortReader$module;
        }
    }
    
    @Override
    public ShortReader$ ShortReader() {
        return (this.ShortReader$module == null) ? this.ShortReader$lzycompute() : this.ShortReader$module;
    }
    
    private ByteReader$ ByteReader$lzycompute() {
        synchronized (this) {
            if (this.ByteReader$module == null) {
                this.ByteReader$module = new ByteReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.ByteReader$module;
        }
    }
    
    @Override
    public ByteReader$ ByteReader() {
        return (this.ByteReader$module == null) ? this.ByteReader$lzycompute() : this.ByteReader$module;
    }
    
    private FloatReader$ FloatReader$lzycompute() {
        synchronized (this) {
            if (this.FloatReader$module == null) {
                this.FloatReader$module = new FloatReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.FloatReader$module;
        }
    }
    
    @Override
    public FloatReader$ FloatReader() {
        return (this.FloatReader$module == null) ? this.FloatReader$lzycompute() : this.FloatReader$module;
    }
    
    private DoubleReader$ DoubleReader$lzycompute() {
        synchronized (this) {
            if (this.DoubleReader$module == null) {
                this.DoubleReader$module = new DoubleReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.DoubleReader$module;
        }
    }
    
    @Override
    public DoubleReader$ DoubleReader() {
        return (this.DoubleReader$module == null) ? this.DoubleReader$lzycompute() : this.DoubleReader$module;
    }
    
    private BigDecimalReader$ BigDecimalReader$lzycompute() {
        synchronized (this) {
            if (this.BigDecimalReader$module == null) {
                this.BigDecimalReader$module = new BigDecimalReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.BigDecimalReader$module;
        }
    }
    
    @Override
    public BigDecimalReader$ BigDecimalReader() {
        return (this.BigDecimalReader$module == null) ? this.BigDecimalReader$lzycompute() : this.BigDecimalReader$module;
    }
    
    private BooleanReader$ BooleanReader$lzycompute() {
        synchronized (this) {
            if (this.BooleanReader$module == null) {
                this.BooleanReader$module = new BooleanReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.BooleanReader$module;
        }
    }
    
    @Override
    public BooleanReader$ BooleanReader() {
        return (this.BooleanReader$module == null) ? this.BooleanReader$lzycompute() : this.BooleanReader$module;
    }
    
    private StringReader$ StringReader$lzycompute() {
        synchronized (this) {
            if (this.StringReader$module == null) {
                this.StringReader$module = new StringReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.StringReader$module;
        }
    }
    
    @Override
    public StringReader$ StringReader() {
        return (this.StringReader$module == null) ? this.StringReader$lzycompute() : this.StringReader$module;
    }
    
    private JValueReader$ JValueReader$lzycompute() {
        synchronized (this) {
            if (this.JValueReader$module == null) {
                this.JValueReader$module = new JValueReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.JValueReader$module;
        }
    }
    
    @Override
    public JValueReader$ JValueReader() {
        return (this.JValueReader$module == null) ? this.JValueReader$lzycompute() : this.JValueReader$module;
    }
    
    private JObjectReader$ JObjectReader$lzycompute() {
        synchronized (this) {
            if (this.JObjectReader$module == null) {
                this.JObjectReader$module = new JObjectReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.JObjectReader$module;
        }
    }
    
    @Override
    public JObjectReader$ JObjectReader() {
        return (this.JObjectReader$module == null) ? this.JObjectReader$lzycompute() : this.JObjectReader$module;
    }
    
    private JArrayReader$ JArrayReader$lzycompute() {
        synchronized (this) {
            if (this.JArrayReader$module == null) {
                this.JArrayReader$module = new JArrayReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.JArrayReader$module;
        }
    }
    
    @Override
    public JArrayReader$ JArrayReader() {
        return (this.JArrayReader$module == null) ? this.JArrayReader$lzycompute() : this.JArrayReader$module;
    }
    
    @Override
    public <V> Reader<Map<String, V>> mapReader(final Reader<V> valueReader) {
        return (Reader<Map<String, V>>)DefaultReaders$class.mapReader(this, valueReader);
    }
    
    @Override
    public <T> Reader<Object> arrayReader(final Manifest<T> evidence$1, final Reader<T> evidence$2) {
        return (Reader<Object>)DefaultReaders$class.arrayReader(this, evidence$1, evidence$2);
    }
    
    @Override
    public <T> Object OptionReader(final Reader<T> valueReader) {
        return DefaultReaders$class.OptionReader(this, valueReader);
    }
    
    @Override
    public <F, V> Reader<F> iterableReader(final CanBuildFrom<F, V, F> cbf, final Reader<V> valueReader) {
        return (Reader<F>)DefaultReaders0$class.iterableReader(this, cbf, valueReader);
    }
    
    private DefaultReaders$() {
        DefaultReaders0$class.$init$(MODULE$ = this);
        DefaultReaders$class.$init$(this);
    }
}
