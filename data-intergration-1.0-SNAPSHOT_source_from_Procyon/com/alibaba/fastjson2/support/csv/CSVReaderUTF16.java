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
import com.alibaba.fastjson2.stream.StreamReader;
import java.io.Reader;
import com.alibaba.fastjson2.reader.CharArrayValueConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Map;

final class CSVReaderUTF16<T> extends CSVReader<T>
{
    static final Map<Long, Function<Consumer, CharArrayValueConsumer>> valueConsumerCreators;
    CharArrayValueConsumer valueConsumer;
    char[] buf;
    Reader input;
    
    CSVReaderUTF16(final Feature... features) {
        for (final Feature feature : features) {
            this.features |= feature.mask;
        }
    }
    
    CSVReaderUTF16(final Reader input, final Class<T> objectClass) {
        super(objectClass);
        this.input = input;
    }
    
    CSVReaderUTF16(final Reader input, final CharArrayValueConsumer valueConsumer) {
        this.valueConsumer = valueConsumer;
        this.input = input;
    }
    
    CSVReaderUTF16(final Reader input, final Type[] types) {
        super(types);
        this.input = input;
    }
    
    CSVReaderUTF16(final char[] bytes, final int off, final int len, final Class<T> objectClass) {
        super(objectClass);
        this.buf = bytes;
        this.off = off;
        this.end = off + len;
    }
    
    CSVReaderUTF16(final char[] bytes, final int off, final int len, final CharArrayValueConsumer valueConsumer) {
        this.valueConsumer = valueConsumer;
        this.buf = bytes;
        this.off = off;
        this.end = off + len;
    }
    
    CSVReaderUTF16(final char[] bytes, final int off, final int len, final Type[] types) {
        super(types);
        this.buf = bytes;
        this.off = off;
        this.end = off + len;
    }
    
    @Override
    protected boolean seekLine() throws IOException {
        char[] buf = this.buf;
        int off = this.off;
        if (buf == null && this.input != null) {
            final char[] buf2 = new char[524288];
            this.buf = buf2;
            buf = buf2;
            final int cnt = this.input.read(buf);
            if (cnt == -1) {
                this.inputEnd = true;
                return false;
            }
            this.end = cnt;
        }
        int k = 0;
        while (k < 3) {
            this.lineTerminated = false;
            for (int i = off; i < this.end; ++i) {
                if (i + 4 < this.end) {
                    final char b0 = buf[i];
                    final char b2 = buf[i + 1];
                    final char b3 = buf[i + 2];
                    final char b4 = buf[i + 3];
                    if (b0 > '\"' && b2 > '\"' && b3 > '\"' && b4 > '\"') {
                        this.lineSize += 4;
                        i += 3;
                        continue;
                    }
                }
                final char ch = buf[i];
                if (ch == '\"') {
                    ++this.lineSize;
                    if (!this.quote) {
                        this.quote = true;
                    }
                    else {
                        final int n = i + 1;
                        if (n >= this.end) {
                            break;
                        }
                        if (buf[n] == '\"') {
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
                    if (ch == '\n') {
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
                    if (ch == '\r') {
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
                        if (buf[n] == '\n') {
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
    
    Object readValue(final char[] chars, final int off, final int len, final Type type) {
        if (len == 0) {
            return null;
        }
        if (type == Integer.class) {
            return TypeUtils.parseInt(chars, off, len);
        }
        if (type == Long.class) {
            return TypeUtils.parseLong(chars, off, len);
        }
        if (type == BigDecimal.class) {
            return TypeUtils.parseBigDecimal(chars, off, len);
        }
        if (type == Float.class) {
            return TypeUtils.parseFloat(chars, off, len);
        }
        if (type == Double.class) {
            return TypeUtils.parseDouble(chars, off, len);
        }
        if (type == Date.class) {
            final long millis = DateUtils.parseMillis(chars, off, len, DateUtils.DEFAULT_ZONE_ID);
            return new Date(millis);
        }
        if (type == Boolean.class) {
            return TypeUtils.parseBoolean(chars, off, len);
        }
        final String str = new String(chars, off, len);
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
            char ch = this.buf[i];
            if (quote) {
                if (ch != '\"') {
                    ++valueSize;
                    continue;
                }
                final int n = i + 1;
                if (n < this.lineEnd) {
                    final char c1 = this.buf[n];
                    if (c1 == '\"') {
                        valueSize += 2;
                        ++escapeCount;
                        ++i;
                        continue;
                    }
                    if (c1 == ',') {
                        ++i;
                        ch = c1;
                    }
                }
                else if (n == this.lineEnd) {
                    break;
                }
            }
            else if (ch == '\"') {
                quote = true;
                continue;
            }
            if (ch == ',') {
                final Type type = (this.types != null && columnIndex < this.types.length) ? this.types[columnIndex] : null;
                Object value;
                if (quote) {
                    if (escapeCount == 0) {
                        if (type == null || type == String.class || type == Object.class || strings) {
                            value = new String(this.buf, valueStart + 1, valueSize);
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
                        final char[] chars = new char[valueSize - escapeCount];
                        final int valueEnd = valueStart + valueSize;
                        int j = valueStart + 1;
                        int k = 0;
                        while (j < valueEnd) {
                            final char c2 = this.buf[j];
                            chars[k++] = c2;
                            if (c2 == '\"' && this.buf[j + 1] == '\"') {
                                ++j;
                            }
                            ++j;
                        }
                        if (type == null || type == String.class || type == Object.class || strings) {
                            value = new String(chars);
                        }
                        else {
                            try {
                                value = this.readValue(chars, 0, chars.length, type);
                            }
                            catch (Exception e3) {
                                value = this.error(columnIndex, e3);
                            }
                        }
                    }
                }
                else if (type == null || type == String.class || type == Object.class || strings) {
                    if (valueSize == 1) {
                        value = TypeUtils.toString(this.buf[valueStart]);
                    }
                    else if (valueSize == 2) {
                        value = TypeUtils.toString(this.buf[valueStart], this.buf[valueStart + 1]);
                    }
                    else {
                        value = new String(this.buf, valueStart, valueSize);
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
                    if (type2 == null || type2 == String.class || type2 == Object.class || strings) {
                        value2 = new String(this.buf, valueStart + 1, valueSize);
                    }
                    else {
                        value2 = this.readValue(this.buf, valueStart + 1, valueSize, type2);
                    }
                }
                else {
                    final char[] chars2 = new char[valueSize - escapeCount];
                    final int valueEnd2 = this.lineEnd;
                    int l = valueStart + 1;
                    int m = 0;
                    while (l < valueEnd2) {
                        final char c3 = this.buf[l];
                        chars2[m++] = c3;
                        if (c3 == '\"' && this.buf[l + 1] == '\"') {
                            ++l;
                        }
                        ++l;
                    }
                    if (type2 == null || type2 == String.class || type2 == Object.class || strings) {
                        value2 = new String(chars2);
                    }
                    else {
                        try {
                            value2 = this.readValue(chars2, 0, chars2.length, type2);
                        }
                        catch (Exception e2) {
                            value2 = this.error(columnIndex, e2);
                        }
                    }
                }
            }
            else if (type2 == null || type2 == String.class || type2 == Object.class || strings) {
                if (valueSize == 1) {
                    value2 = TypeUtils.toString(this.buf[valueStart]);
                }
                else if (valueSize == 2) {
                    value2 = TypeUtils.toString(this.buf[valueStart], this.buf[valueStart + 1]);
                }
                else {
                    value2 = new String(this.buf, valueStart, valueSize);
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
        final CharArrayValueConsumer consumer = (row, column, bytes, off, len) -> {
            stat = this.getColumnStat(column);
            stat.stat(bytes, off, len);
            return;
        };
        this.readAll(consumer, Integer.MAX_VALUE);
    }
    
    @Override
    public void statAll(final int maxRows) {
        final ColumnStat stat;
        final CharArrayValueConsumer consumer = (row, column, bytes, off, len) -> {
            stat = this.getColumnStat(column);
            stat.stat(bytes, off, len);
            return;
        };
        this.readAll(consumer, maxRows);
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
        Function<Consumer, CharArrayValueConsumer> valueConsumerCreator = CSVReaderUTF16.valueConsumerCreators.get(fullNameHash);
        if (valueConsumerCreator == null) {
            valueConsumerCreator = provider.createCharArrayValueConsumerCreator(this.objectClass, this.fieldReaders);
            if (valueConsumerCreator != null) {
                CSVReaderUTF16.valueConsumerCreators.putIfAbsent(fullNameHash, valueConsumerCreator);
            }
        }
        CharArrayValueConsumer bytesConsumer = null;
        if (valueConsumerCreator != null) {
            bytesConsumer = valueConsumerCreator.apply(consumer);
        }
        if (bytesConsumer == null) {
            bytesConsumer = new CharArrayConsumerImpl(consumer);
        }
        this.readAll(bytesConsumer, Integer.MAX_VALUE);
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
    
    public void readAll(final CharArrayValueConsumer<T> consumer, final int maxRows) {
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
                char ch = this.buf[i];
                if (quote) {
                    if (ch != '\"') {
                        ++valueSize;
                        continue;
                    }
                    final int n = i + 1;
                    if (n < this.lineEnd) {
                        final char c1 = this.buf[n];
                        if (c1 == '\"') {
                            valueSize += 2;
                            ++escapeCount;
                            ++i;
                            continue;
                        }
                        if (c1 == ',') {
                            ++i;
                            ch = c1;
                        }
                    }
                    else if (n == this.lineEnd) {
                        break;
                    }
                }
                else if (ch == '\"') {
                    quote = true;
                    continue;
                }
                if (ch == ',') {
                    if (quote) {
                        if (escapeCount == 0) {
                            consumer.accept(this.rowCount, columnIndex, this.buf, valueStart + 1, valueSize);
                        }
                        else {
                            final char[] bytes = new char[valueSize - escapeCount];
                            final int valueEnd = valueStart + valueSize;
                            int j = valueStart + 1;
                            int k = 0;
                            while (j < valueEnd) {
                                final char c2 = this.buf[j];
                                bytes[k++] = c2;
                                if (c2 == '\"' && this.buf[j + 1] == '\"') {
                                    ++j;
                                }
                                ++j;
                            }
                            consumer.accept(this.rowCount, columnIndex, bytes, 0, bytes.length);
                        }
                    }
                    else {
                        consumer.accept(this.rowCount, columnIndex, this.buf, valueStart, valueSize);
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
                if (quote) {
                    if (escapeCount == 0) {
                        consumer.accept(this.rowCount, columnIndex, this.buf, valueStart + 1, valueSize);
                    }
                    else {
                        final char[] bytes2 = new char[valueSize - escapeCount];
                        final int valueEnd2 = this.lineEnd;
                        int l = valueStart + 1;
                        int m = 0;
                        while (l < valueEnd2) {
                            final char c3 = this.buf[l];
                            bytes2[m++] = c3;
                            if (c3 == '\"' && this.buf[l + 1] == '\"') {
                                ++l;
                            }
                            ++l;
                        }
                        consumer.accept(this.rowCount, columnIndex, bytes2, 0, bytes2.length);
                    }
                }
                else {
                    consumer.accept(this.rowCount, columnIndex, this.buf, valueStart, valueSize);
                }
            }
            consumer.afterRow(this.rowCount);
            ++r;
        }
        consumer.end();
    }
    
    static {
        valueConsumerCreators = new ConcurrentHashMap<Long, Function<Consumer, CharArrayValueConsumer>>();
    }
    
    class CharArrayConsumerImpl<T> implements CharArrayValueConsumer
    {
        protected T object;
        final Consumer<T> consumer;
        
        public CharArrayConsumerImpl(final Consumer<T> consumer) {
            this.consumer = consumer;
        }
        
        @Override
        public final void beforeRow(final int row) {
            if (CSVReaderUTF16.this.objectCreator != null) {
                this.object = CSVReaderUTF16.this.objectCreator.get();
            }
        }
        
        @Override
        public void accept(final int row, final int column, final char[] bytes, final int off, final int len) {
            if (column >= CSVReaderUTF16.this.fieldReaders.length || len == 0) {
                return;
            }
            final FieldReader fieldReader = CSVReaderUTF16.this.fieldReaders[column];
            final Object fieldValue = CSVReaderUTF16.this.readValue(bytes, off, len, fieldReader.fieldType);
            fieldReader.accept(this.object, fieldValue);
        }
        
        @Override
        public final void afterRow(final int row) {
            this.consumer.accept(this.object);
            this.object = null;
        }
    }
}
