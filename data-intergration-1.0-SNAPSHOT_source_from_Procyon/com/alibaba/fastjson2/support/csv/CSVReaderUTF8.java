// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.support.csv;

import java.util.concurrent.ConcurrentHashMap;
import com.alibaba.fastjson2.reader.FieldReader;
import java.util.function.Supplier;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import com.alibaba.fastjson2.JSONFactory;
import java.io.Closeable;
import com.alibaba.fastjson2.util.IOUtils;
import java.util.List;
import java.util.ArrayList;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.DateUtils;
import java.util.Date;
import java.math.BigDecimal;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.stream.StreamReader;
import java.nio.charset.Charset;
import java.io.InputStream;
import com.alibaba.fastjson2.reader.ByteArrayValueConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Map;

final class CSVReaderUTF8<T> extends CSVReader<T>
{
    static final Map<Long, Function<Consumer, ByteArrayValueConsumer>> valueConsumerCreators;
    byte[] buf;
    InputStream input;
    Charset charset;
    ByteArrayValueConsumer valueConsumer;
    
    CSVReaderUTF8(final Feature... features) {
        this.charset = StandardCharsets.UTF_8;
        for (final Feature feature : features) {
            this.features |= feature.mask;
        }
    }
    
    CSVReaderUTF8(final byte[] bytes, final int off, final int len, final Charset charset, final Class<T> objectClass) {
        super(objectClass);
        this.charset = StandardCharsets.UTF_8;
        this.buf = bytes;
        this.off = off;
        this.end = off + len;
        this.charset = charset;
    }
    
    CSVReaderUTF8(final byte[] bytes, final int off, final int len, final Charset charset, final ByteArrayValueConsumer valueConsumer) {
        this.charset = StandardCharsets.UTF_8;
        this.valueConsumer = valueConsumer;
        this.buf = bytes;
        this.off = off;
        this.end = off + len;
        this.charset = charset;
    }
    
    CSVReaderUTF8(final byte[] bytes, final int off, final int len, final Type[] types) {
        super(types);
        this.charset = StandardCharsets.UTF_8;
        this.buf = bytes;
        this.off = off;
        this.end = off + len;
        this.types = types;
    }
    
    CSVReaderUTF8(final byte[] bytes, final int off, final int len, final Class<T> objectClass) {
        super(objectClass);
        this.charset = StandardCharsets.UTF_8;
        this.buf = bytes;
        this.off = off;
        this.end = off + len;
    }
    
    CSVReaderUTF8(final InputStream input, final Charset charset, final Type[] types) {
        super(types);
        this.charset = StandardCharsets.UTF_8;
        this.charset = charset;
        this.input = input;
    }
    
    CSVReaderUTF8(final InputStream input, final Charset charset, final Class<T> objectClass) {
        super(objectClass);
        this.charset = StandardCharsets.UTF_8;
        this.charset = charset;
        this.input = input;
    }
    
    CSVReaderUTF8(final InputStream input, final Charset charset, final ByteArrayValueConsumer valueConsumer) {
        this.charset = StandardCharsets.UTF_8;
        this.charset = charset;
        this.input = input;
        this.valueConsumer = valueConsumer;
    }
    
    @Override
    protected boolean seekLine() throws IOException {
        byte[] buf = this.buf;
        int off = this.off;
        if (buf == null && this.input != null) {
            final byte[] buf2 = new byte[524288];
            this.buf = buf2;
            buf = buf2;
            final int cnt = this.input.read(buf);
            if (cnt == -1) {
                this.inputEnd = true;
                return false;
            }
            this.end = cnt;
            if (this.end > 3 && buf[0] == -17 && buf[1] == -69 && buf[2] == -65) {
                off = 3;
                this.lineNextStart = off;
            }
        }
        int k = 0;
        while (k < 3) {
            this.lineTerminated = false;
            for (int i = off; i < this.end; ++i) {
                final byte ch = buf[i];
                if (ch == 34) {
                    ++this.lineSize;
                    if (!this.quote) {
                        this.quote = true;
                    }
                    else {
                        final int n = i + 1;
                        if (n >= this.end) {
                            break;
                        }
                        if (buf[n] == 34) {
                            ++this.lineSize;
                            ++i;
                        }
                        else {
                            this.quote = false;
                        }
                    }
                }
                else if (this.quote) {
                    ++this.lineSize;
                }
                else {
                    if (ch == 10) {
                        if (this.lineSize > 0 || (this.features & Feature.IgnoreEmptyLine.mask) == 0x0L) {
                            ++this.rowCount;
                        }
                        this.lineTerminated = true;
                        this.lineSize = 0;
                        this.lineEnd = i;
                        this.lineStart = this.lineNextStart;
                        off = (this.lineNextStart = i + 1);
                        break;
                    }
                    if (ch == 13) {
                        if (this.lineSize > 0 || (this.features & Feature.IgnoreEmptyLine.mask) == 0x0L) {
                            ++this.rowCount;
                        }
                        this.lineTerminated = true;
                        this.lineSize = 0;
                        this.lineEnd = i;
                        final int n = i + 1;
                        if (n >= this.end) {
                            break;
                        }
                        if (buf[n] == 10) {
                            ++i;
                        }
                        this.lineStart = this.lineNextStart;
                        off = (this.lineNextStart = i + 1);
                        break;
                    }
                    else {
                        ++this.lineSize;
                    }
                }
            }
            if (!this.lineTerminated) {
                if (this.input != null && !this.inputEnd) {
                    final int len = this.end - off;
                    if (off > 0) {
                        if (len > 0) {
                            System.arraycopy(buf, off, buf, 0, len);
                        }
                        final int n2 = 0;
                        this.lineNextStart = n2;
                        this.lineStart = n2;
                        off = 0;
                        this.end = len;
                        this.quote = false;
                    }
                    final int cnt2 = this.input.read(buf, this.end, buf.length - this.end);
                    if (cnt2 != -1) {
                        this.end += cnt2;
                        ++k;
                        continue;
                    }
                    this.inputEnd = true;
                    if (off == this.end) {
                        this.off = off;
                        return false;
                    }
                }
                this.lineStart = this.lineNextStart;
                this.lineEnd = this.end;
                ++this.rowCount;
                this.lineSize = 0;
                off = this.end;
            }
            this.lineTerminated = (off == this.end);
            break;
        }
        this.off = off;
        return true;
    }
    
    Object readValue(final byte[] bytes, final int off, final int len, final Type type) {
        if (len == 0) {
            return null;
        }
        if (type == Integer.class) {
            return TypeUtils.parseInt(bytes, off, len);
        }
        if (type == Long.class) {
            return TypeUtils.parseLong(bytes, off, len);
        }
        if (type == BigDecimal.class) {
            return TypeUtils.parseBigDecimal(bytes, off, len);
        }
        if (type == Float.class) {
            return TypeUtils.parseFloat(bytes, off, len);
        }
        if (type == Double.class) {
            return TypeUtils.parseDouble(bytes, off, len);
        }
        if (type == Date.class) {
            final long millis = DateUtils.parseMillis(bytes, off, len, this.charset, DateUtils.DEFAULT_ZONE_ID);
            return new Date(millis);
        }
        if (type == Boolean.class) {
            return TypeUtils.parseBoolean(bytes, off, len);
        }
        final String str = new String(bytes, off, len, this.charset);
        return TypeUtils.cast(str, type);
    }
    
    @Override
    public boolean isEnd() {
        return this.inputEnd;
    }
    
    public Object[] readLineValues(final boolean strings) {
        try {
            if (this.inputEnd) {
                return null;
            }
            if (this.input == null && this.off >= this.end) {
                return null;
            }
            final boolean result = this.seekLine();
            if (!result) {
                return null;
            }
        }
        catch (IOException e) {
            throw new JSONException("seekLine error", e);
        }
        Object[] values = null;
        List<Object> valueList = null;
        if (this.columns != null) {
            if (strings) {
                values = new String[this.columns.size()];
            }
            else {
                values = new Object[this.columns.size()];
            }
        }
        boolean quote = false;
        int valueStart = this.lineStart;
        int valueSize = 0;
        int escapeCount = 0;
        int columnIndex = 0;
        for (int i = this.lineStart; i < this.lineEnd; ++i) {
            byte ch = this.buf[i];
            if (quote) {
                if (ch != 34) {
                    ++valueSize;
                    continue;
                }
                final int n = i + 1;
                if (n < this.lineEnd) {
                    final byte c1 = this.buf[n];
                    if (c1 == 34) {
                        valueSize += 2;
                        ++escapeCount;
                        ++i;
                        continue;
                    }
                    if (c1 == 44) {
                        ++i;
                        ch = c1;
                    }
                }
                else if (n == this.lineEnd) {
                    break;
                }
            }
            else if (ch == 34) {
                quote = true;
                continue;
            }
            if (ch == 44) {
                final Type type = (this.types != null && columnIndex < this.types.length) ? this.types[columnIndex] : null;
                Object value;
                if (quote) {
                    if (escapeCount == 0) {
                        if (type == null || type == String.class || type == Object.class) {
                            value = new String(this.buf, valueStart + 1, valueSize, this.charset);
                        }
                        else {
                            try {
                                value = this.readValue(this.buf, valueStart + 1, valueSize, type);
                            }
                            catch (Exception e2) {
                                value = this.error(columnIndex, e2);
                            }
                        }
                    }
                    else {
                        final byte[] bytes = new byte[valueSize - escapeCount];
                        final int valueEnd = valueStart + valueSize;
                        int j = valueStart + 1;
                        int k = 0;
                        while (j < valueEnd) {
                            final byte c2 = this.buf[j];
                            bytes[k++] = c2;
                            if (c2 == 34 && this.buf[j + 1] == 34) {
                                ++j;
                            }
                            ++j;
                        }
                        if (type == null || type == String.class || type == Object.class) {
                            value = new String(bytes, 0, bytes.length, this.charset);
                        }
                        else {
                            try {
                                value = this.readValue(bytes, 0, bytes.length, type);
                            }
                            catch (Exception e3) {
                                value = this.error(columnIndex, e3);
                            }
                        }
                    }
                }
                else if (type == null || type == String.class || type == Object.class || strings) {
                    byte c3;
                    if (valueSize == 1 && (c3 = this.buf[valueStart]) >= 0) {
                        value = TypeUtils.toString((char)c3);
                    }
                    else {
                        final byte c4;
                        if (valueSize == 2 && (c3 = this.buf[valueStart]) >= 0 && (c4 = this.buf[valueStart + 1]) >= 0) {
                            value = TypeUtils.toString((char)c3, (char)c4);
                        }
                        else {
                            value = new String(this.buf, valueStart, valueSize, this.charset);
                        }
                    }
                }
                else {
                    try {
                        value = this.readValue(this.buf, valueStart, valueSize, type);
                    }
                    catch (Exception e2) {
                        value = this.error(columnIndex, e2);
                    }
                }
                if (values != null) {
                    if (columnIndex < values.length) {
                        values[columnIndex] = value;
                    }
                }
                else {
                    if (valueList == null) {
                        valueList = new ArrayList<Object>();
                    }
                    valueList.add(value);
                }
                quote = false;
                valueStart = i + 1;
                valueSize = 0;
                escapeCount = 0;
                ++columnIndex;
            }
            else {
                ++valueSize;
            }
        }
        if (valueSize > 0) {
            final Type type2 = (this.types != null && columnIndex < this.types.length) ? this.types[columnIndex] : null;
            Object value2;
            if (quote) {
                if (escapeCount == 0) {
                    if (type2 == null || type2 == String.class || type2 == Object.class) {
                        value2 = new String(this.buf, valueStart + 1, valueSize, this.charset);
                    }
                    else {
                        try {
                            value2 = this.readValue(this.buf, valueStart + 1, valueSize, type2);
                        }
                        catch (Exception e4) {
                            value2 = this.error(columnIndex, e4);
                        }
                    }
                }
                else {
                    final byte[] bytes2 = new byte[valueSize - escapeCount];
                    final int valueEnd2 = this.lineEnd;
                    int l = valueStart + 1;
                    int m = 0;
                    while (l < valueEnd2) {
                        final byte c5 = this.buf[l];
                        bytes2[m++] = c5;
                        if (c5 == 34 && this.buf[l + 1] == 34) {
                            ++l;
                        }
                        ++l;
                    }
                    if (type2 == null || type2 == String.class || type2 == Object.class) {
                        value2 = new String(bytes2, 0, bytes2.length, this.charset);
                    }
                    else {
                        try {
                            value2 = this.readValue(bytes2, 0, bytes2.length, type2);
                        }
                        catch (Exception e2) {
                            value2 = this.error(columnIndex, e2);
                        }
                    }
                }
            }
            else if (type2 == null || type2 == String.class || type2 == Object.class || strings) {
                byte c6;
                if (valueSize == 1 && (c6 = this.buf[valueStart]) >= 0) {
                    value2 = TypeUtils.toString((char)c6);
                }
                else {
                    final byte c1;
                    if (valueSize == 2 && (c6 = this.buf[valueStart]) >= 0 && (c1 = this.buf[valueStart + 1]) >= 0) {
                        value2 = TypeUtils.toString((char)c6, (char)c1);
                    }
                    else {
                        value2 = new String(this.buf, valueStart, valueSize, this.charset);
                    }
                }
            }
            else {
                try {
                    value2 = this.readValue(this.buf, valueStart, valueSize, type2);
                }
                catch (Exception e4) {
                    value2 = this.error(columnIndex, e4);
                }
            }
            if (values != null) {
                if (columnIndex < values.length) {
                    values[columnIndex] = value2;
                }
            }
            else {
                if (valueList == null) {
                    valueList = new ArrayList<Object>();
                }
                valueList.add(value2);
            }
        }
        if (values == null && valueList != null) {
            if (strings) {
                values = new String[valueList.size()];
            }
            else {
                values = new Object[valueList.size()];
            }
            valueList.toArray(values);
        }
        if (this.input == null && this.off == this.end) {
            this.inputEnd = true;
        }
        return values;
    }
    
    @Override
    public void close() {
        if (this.input != null) {
            IOUtils.close(this.input);
        }
    }
    
    @Override
    public void statAll() {
        final ColumnStat stat;
        final ByteArrayValueConsumer consumer = (row, column, bytes, off, len, charset) -> {
            stat = this.getColumnStat(column);
            stat.stat(bytes, off, len, charset);
            return;
        };
        this.readAll(consumer, Integer.MAX_VALUE);
    }
    
    @Override
    public void statAll(final int maxRows) {
        final ColumnStat stat;
        final ByteArrayValueConsumer consumer = (row, column, bytes, off, len, charset) -> {
            stat = this.getColumnStat(column);
            stat.stat(bytes, off, len, charset);
            return;
        };
        this.readAll(consumer, maxRows);
    }
    
    @Override
    public void readAll() {
        if (this.valueConsumer == null) {
            throw new JSONException("unsupported operation, consumer is null");
        }
        this.readAll(this.valueConsumer, Integer.MAX_VALUE);
    }
    
    @Override
    public void readAll(final int maxRows) {
        if (this.valueConsumer == null) {
            throw new JSONException("unsupported operation, consumer is null");
        }
        this.readAll(this.valueConsumer, maxRows);
    }
    
    @Override
    public void readLineObjectAll(final boolean readHeader, final Consumer<T> consumer) {
        if (readHeader) {
            this.readHeader();
        }
        if (this.fieldReaders == null) {
            while (true) {
                final Object[] line = this.readLineValues(false);
                if (line == null) {
                    break;
                }
                consumer.accept((T)line);
            }
            return;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        if (this.fieldReaders == null && this.objectClass != null) {
            final ObjectReaderAdapter objectReader = (ObjectReaderAdapter)provider.getObjectReader(this.objectClass);
            this.fieldReaders = objectReader.getFieldReaders();
            this.objectCreator = provider.createObjectCreator(this.objectClass, this.features);
        }
        final String[] strings = new String[this.fieldReaders.length + 1];
        strings[0] = this.objectClass.getName();
        for (int i = 0; i < this.fieldReaders.length; ++i) {
            strings[i + 1] = this.fieldReaders[i].fieldName;
        }
        final long fullNameHash = Fnv.hashCode64(strings);
        Function<Consumer, ByteArrayValueConsumer> valueConsumerCreator = CSVReaderUTF8.valueConsumerCreators.get(fullNameHash);
        if (valueConsumerCreator == null) {
            valueConsumerCreator = provider.createValueConsumerCreator(this.objectClass, this.fieldReaders);
            if (valueConsumerCreator != null) {
                CSVReaderUTF8.valueConsumerCreators.putIfAbsent(fullNameHash, valueConsumerCreator);
            }
        }
        ByteArrayValueConsumer bytesConsumer = null;
        if (valueConsumerCreator != null) {
            bytesConsumer = valueConsumerCreator.apply(consumer);
        }
        if (bytesConsumer == null) {
            bytesConsumer = new ByteArrayConsumerImpl(consumer);
        }
        this.readAll(bytesConsumer, Integer.MAX_VALUE);
    }
    
    private void readAll(final ByteArrayValueConsumer consumer, final int maxRows) {
        consumer.start();
        int r = 0;
        while (true) {
            if (r >= maxRows) {
                if (maxRows >= 0) {
                    break;
                }
            }
            try {
                if (this.inputEnd) {
                    break;
                }
                if (this.input == null && this.off >= this.end) {
                    break;
                }
                final boolean result = this.seekLine();
                if (!result) {
                    break;
                }
            }
            catch (IOException e) {
                throw new JSONException("seekLine error", e);
            }
            consumer.beforeRow(this.rowCount);
            boolean quote = false;
            int valueStart = this.lineStart;
            int valueSize = 0;
            int escapeCount = 0;
            int columnIndex = 0;
            for (int i = this.lineStart; i < this.lineEnd; ++i) {
                byte ch = this.buf[i];
                if (quote) {
                    if (ch != 34) {
                        ++valueSize;
                        continue;
                    }
                    final int n = i + 1;
                    if (n < this.lineEnd) {
                        final byte c1 = this.buf[n];
                        if (c1 == 34) {
                            valueSize += 2;
                            ++escapeCount;
                            ++i;
                            continue;
                        }
                        if (c1 == 44) {
                            ++i;
                            ch = c1;
                        }
                    }
                    else if (n == this.lineEnd) {
                        break;
                    }
                }
                else if (ch == 34) {
                    quote = true;
                    continue;
                }
                if (ch == 44) {
                    byte[] columnBuf = this.buf;
                    int columnStart = 0;
                    int columnSize = valueSize;
                    if (quote) {
                        if (escapeCount == 0) {
                            columnStart = valueStart + 1;
                        }
                        else {
                            final byte[] bytes = new byte[valueSize - escapeCount];
                            final int valueEnd = valueStart + valueSize;
                            int j = valueStart + 1;
                            int k = 0;
                            while (j < valueEnd) {
                                final byte c2 = this.buf[j];
                                bytes[k++] = c2;
                                if (c2 == 34 && this.buf[j + 1] == 34) {
                                    ++j;
                                }
                                ++j;
                            }
                            columnBuf = bytes;
                            columnSize = bytes.length;
                        }
                    }
                    else {
                        columnStart = valueStart;
                    }
                    consumer.accept(this.rowCount, columnIndex, columnBuf, columnStart, columnSize, this.charset);
                    quote = false;
                    valueStart = i + 1;
                    valueSize = 0;
                    escapeCount = 0;
                    ++columnIndex;
                }
                else {
                    ++valueSize;
                }
            }
            if (valueSize > 0) {
                byte[] columnBuf2 = this.buf;
                int columnStart2 = 0;
                int columnSize2 = valueSize;
                if (quote) {
                    if (escapeCount == 0) {
                        columnStart2 = valueStart + 1;
                    }
                    else {
                        final byte[] bytes2 = new byte[valueSize - escapeCount];
                        final int valueEnd2 = this.lineEnd;
                        int l = valueStart + 1;
                        int m = 0;
                        while (l < valueEnd2) {
                            final byte c3 = this.buf[l];
                            bytes2[m++] = c3;
                            if (c3 == 34 && this.buf[l + 1] == 34) {
                                ++l;
                            }
                            ++l;
                        }
                        columnBuf2 = bytes2;
                        columnSize2 = bytes2.length;
                    }
                }
                else {
                    columnStart2 = valueStart;
                }
                consumer.accept(this.rowCount, columnIndex, columnBuf2, columnStart2, columnSize2, this.charset);
            }
            consumer.afterRow(this.rowCount);
            ++r;
        }
        consumer.end();
    }
    
    static {
        valueConsumerCreators = new ConcurrentHashMap<Long, Function<Consumer, ByteArrayValueConsumer>>();
    }
    
    class ByteArrayConsumerImpl implements ByteArrayValueConsumer
    {
        protected Object object;
        final Consumer consumer;
        
        public ByteArrayConsumerImpl(final Consumer consumer) {
            this.consumer = consumer;
        }
        
        @Override
        public final void beforeRow(final int row) {
            if (CSVReaderUTF8.this.objectCreator != null) {
                this.object = CSVReaderUTF8.this.objectCreator.get();
            }
        }
        
        @Override
        public void accept(final int row, final int column, final byte[] bytes, final int off, final int len, final Charset charset) {
            if (column >= CSVReaderUTF8.this.fieldReaders.length || len == 0) {
                return;
            }
            final FieldReader fieldReader = CSVReaderUTF8.this.fieldReaders[column];
            final Object fieldValue = CSVReaderUTF8.this.readValue(bytes, off, len, fieldReader.fieldType);
            fieldReader.accept(this.object, fieldValue);
        }
        
        @Override
        public final void afterRow(final int row) {
            this.consumer.accept(this.object);
            this.object = null;
        }
    }
}
