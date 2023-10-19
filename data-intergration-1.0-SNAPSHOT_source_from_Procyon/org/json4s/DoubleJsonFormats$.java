// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.generic.CanBuildFrom;
import scala.reflect.Manifest;
import scala.Option;
import scala.collection.immutable.Map;
import scala.runtime.BoxedUnit;

public final class DoubleJsonFormats$ implements DoubleJsonFormats
{
    public static final DoubleJsonFormats$ MODULE$;
    private volatile DoubleWriters.FloatWriter$ FloatWriter$module;
    private volatile DoubleWriters.DoubleWriter$ DoubleWriter$module;
    private volatile DoubleWriters.BigDecimalWriter$ BigDecimalWriter$module;
    private volatile DefaultWriters.IntWriter$ IntWriter$module;
    private volatile DefaultWriters.ByteWriter$ ByteWriter$module;
    private volatile DefaultWriters.ShortWriter$ ShortWriter$module;
    private volatile DefaultWriters.LongWriter$ LongWriter$module;
    private volatile DefaultWriters.BigIntWriter$ BigIntWriter$module;
    private volatile DefaultWriters.BooleanWriter$ BooleanWriter$module;
    private volatile DefaultWriters.StringWriter$ StringWriter$module;
    private volatile DefaultWriters.JValueWriter$ JValueWriter$module;
    private volatile DefaultReaders.IntReader$ IntReader$module;
    private volatile DefaultReaders.BigIntReader$ BigIntReader$module;
    private volatile DefaultReaders.LongReader$ LongReader$module;
    private volatile DefaultReaders.ShortReader$ ShortReader$module;
    private volatile DefaultReaders.ByteReader$ ByteReader$module;
    private volatile DefaultReaders.FloatReader$ FloatReader$module;
    private volatile DefaultReaders.DoubleReader$ DoubleReader$module;
    private volatile DefaultReaders.BigDecimalReader$ BigDecimalReader$module;
    private volatile DefaultReaders.BooleanReader$ BooleanReader$module;
    private volatile DefaultReaders.StringReader$ StringReader$module;
    private volatile DefaultReaders.JValueReader$ JValueReader$module;
    private volatile DefaultReaders.JObjectReader$ JObjectReader$module;
    private volatile DefaultReaders.JArrayReader$ JArrayReader$module;
    
    static {
        new DoubleJsonFormats$();
    }
    
    private DoubleWriters.FloatWriter$ FloatWriter$lzycompute() {
        synchronized (this) {
            if (this.FloatWriter$module == null) {
                this.FloatWriter$module = new DoubleWriters.FloatWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.FloatWriter$module;
        }
    }
    
    @Override
    public DoubleWriters.FloatWriter$ FloatWriter() {
        return (this.FloatWriter$module == null) ? this.FloatWriter$lzycompute() : this.FloatWriter$module;
    }
    
    private DoubleWriters.DoubleWriter$ DoubleWriter$lzycompute() {
        synchronized (this) {
            if (this.DoubleWriter$module == null) {
                this.DoubleWriter$module = new DoubleWriters.DoubleWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.DoubleWriter$module;
        }
    }
    
    @Override
    public DoubleWriters.DoubleWriter$ DoubleWriter() {
        return (this.DoubleWriter$module == null) ? this.DoubleWriter$lzycompute() : this.DoubleWriter$module;
    }
    
    private DoubleWriters.BigDecimalWriter$ BigDecimalWriter$lzycompute() {
        synchronized (this) {
            if (this.BigDecimalWriter$module == null) {
                this.BigDecimalWriter$module = new DoubleWriters.BigDecimalWriter$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.BigDecimalWriter$module;
        }
    }
    
    @Override
    public DoubleWriters.BigDecimalWriter$ BigDecimalWriter() {
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
    
    private DefaultReaders.IntReader$ IntReader$lzycompute() {
        synchronized (this) {
            if (this.IntReader$module == null) {
                this.IntReader$module = new DefaultReaders.IntReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.IntReader$module;
        }
    }
    
    @Override
    public DefaultReaders.IntReader$ IntReader() {
        return (this.IntReader$module == null) ? this.IntReader$lzycompute() : this.IntReader$module;
    }
    
    private DefaultReaders.BigIntReader$ BigIntReader$lzycompute() {
        synchronized (this) {
            if (this.BigIntReader$module == null) {
                this.BigIntReader$module = new DefaultReaders.BigIntReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.BigIntReader$module;
        }
    }
    
    @Override
    public DefaultReaders.BigIntReader$ BigIntReader() {
        return (this.BigIntReader$module == null) ? this.BigIntReader$lzycompute() : this.BigIntReader$module;
    }
    
    private DefaultReaders.LongReader$ LongReader$lzycompute() {
        synchronized (this) {
            if (this.LongReader$module == null) {
                this.LongReader$module = new DefaultReaders.LongReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.LongReader$module;
        }
    }
    
    @Override
    public DefaultReaders.LongReader$ LongReader() {
        return (this.LongReader$module == null) ? this.LongReader$lzycompute() : this.LongReader$module;
    }
    
    private DefaultReaders.ShortReader$ ShortReader$lzycompute() {
        synchronized (this) {
            if (this.ShortReader$module == null) {
                this.ShortReader$module = new DefaultReaders.ShortReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.ShortReader$module;
        }
    }
    
    @Override
    public DefaultReaders.ShortReader$ ShortReader() {
        return (this.ShortReader$module == null) ? this.ShortReader$lzycompute() : this.ShortReader$module;
    }
    
    private DefaultReaders.ByteReader$ ByteReader$lzycompute() {
        synchronized (this) {
            if (this.ByteReader$module == null) {
                this.ByteReader$module = new DefaultReaders.ByteReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.ByteReader$module;
        }
    }
    
    @Override
    public DefaultReaders.ByteReader$ ByteReader() {
        return (this.ByteReader$module == null) ? this.ByteReader$lzycompute() : this.ByteReader$module;
    }
    
    private DefaultReaders.FloatReader$ FloatReader$lzycompute() {
        synchronized (this) {
            if (this.FloatReader$module == null) {
                this.FloatReader$module = new DefaultReaders.FloatReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.FloatReader$module;
        }
    }
    
    @Override
    public DefaultReaders.FloatReader$ FloatReader() {
        return (this.FloatReader$module == null) ? this.FloatReader$lzycompute() : this.FloatReader$module;
    }
    
    private DefaultReaders.DoubleReader$ DoubleReader$lzycompute() {
        synchronized (this) {
            if (this.DoubleReader$module == null) {
                this.DoubleReader$module = new DefaultReaders.DoubleReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.DoubleReader$module;
        }
    }
    
    @Override
    public DefaultReaders.DoubleReader$ DoubleReader() {
        return (this.DoubleReader$module == null) ? this.DoubleReader$lzycompute() : this.DoubleReader$module;
    }
    
    private DefaultReaders.BigDecimalReader$ BigDecimalReader$lzycompute() {
        synchronized (this) {
            if (this.BigDecimalReader$module == null) {
                this.BigDecimalReader$module = new DefaultReaders.BigDecimalReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.BigDecimalReader$module;
        }
    }
    
    @Override
    public DefaultReaders.BigDecimalReader$ BigDecimalReader() {
        return (this.BigDecimalReader$module == null) ? this.BigDecimalReader$lzycompute() : this.BigDecimalReader$module;
    }
    
    private DefaultReaders.BooleanReader$ BooleanReader$lzycompute() {
        synchronized (this) {
            if (this.BooleanReader$module == null) {
                this.BooleanReader$module = new DefaultReaders.BooleanReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.BooleanReader$module;
        }
    }
    
    @Override
    public DefaultReaders.BooleanReader$ BooleanReader() {
        return (this.BooleanReader$module == null) ? this.BooleanReader$lzycompute() : this.BooleanReader$module;
    }
    
    private DefaultReaders.StringReader$ StringReader$lzycompute() {
        synchronized (this) {
            if (this.StringReader$module == null) {
                this.StringReader$module = new DefaultReaders.StringReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.StringReader$module;
        }
    }
    
    @Override
    public DefaultReaders.StringReader$ StringReader() {
        return (this.StringReader$module == null) ? this.StringReader$lzycompute() : this.StringReader$module;
    }
    
    private DefaultReaders.JValueReader$ JValueReader$lzycompute() {
        synchronized (this) {
            if (this.JValueReader$module == null) {
                this.JValueReader$module = new DefaultReaders.JValueReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.JValueReader$module;
        }
    }
    
    @Override
    public DefaultReaders.JValueReader$ JValueReader() {
        return (this.JValueReader$module == null) ? this.JValueReader$lzycompute() : this.JValueReader$module;
    }
    
    private DefaultReaders.JObjectReader$ JObjectReader$lzycompute() {
        synchronized (this) {
            if (this.JObjectReader$module == null) {
                this.JObjectReader$module = new DefaultReaders.JObjectReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.JObjectReader$module;
        }
    }
    
    @Override
    public DefaultReaders.JObjectReader$ JObjectReader() {
        return (this.JObjectReader$module == null) ? this.JObjectReader$lzycompute() : this.JObjectReader$module;
    }
    
    private DefaultReaders.JArrayReader$ JArrayReader$lzycompute() {
        synchronized (this) {
            if (this.JArrayReader$module == null) {
                this.JArrayReader$module = new DefaultReaders.JArrayReader$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.JArrayReader$module;
        }
    }
    
    @Override
    public DefaultReaders.JArrayReader$ JArrayReader() {
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
    
    @Override
    public <T> JsonFormat<T> GenericFormat(final Reader<T> reader, final Writer<T> writer) {
        return (JsonFormat<T>)DefaultJsonFormats$class.GenericFormat(this, reader, writer);
    }
    
    private DoubleJsonFormats$() {
        DefaultJsonFormats$class.$init$(MODULE$ = this);
        DefaultReaders0$class.$init$(this);
        DefaultReaders$class.$init$(this);
        DefaultWriters$class.$init$(this);
        DoubleWriters$class.$init$(this);
    }
}
