// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.stream;

import java.util.function.Consumer;
import com.alibaba.fastjson2.support.csv.CSVReader;
import java.util.Collection;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.time.Instant;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.util.JDKUtils;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.annotation.JSONField;
import java.util.Spliterator;
import java.util.stream.StreamSupport;
import java.util.stream.Stream;
import java.io.IOException;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.JSONFactory;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson2.reader.FieldReader;
import java.util.function.Supplier;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;

public abstract class StreamReader<T>
{
    protected static final int SIZE_512K = 524288;
    protected long features;
    protected Type[] types;
    protected ObjectReader[] typeReaders;
    protected Supplier objectCreator;
    protected FieldReader[] fieldReaders;
    protected int lineSize;
    protected int rowCount;
    protected int errorCount;
    protected int lineStart;
    protected int lineEnd;
    protected int lineNextStart;
    protected int end;
    protected int off;
    protected boolean inputEnd;
    protected boolean lineTerminated;
    protected Map<String, ColumnStat> columnStatsMap;
    protected List<String> columns;
    protected List<ColumnStat> columnStats;
    protected int[] mapping;
    
    public StreamReader() {
        this.lineTerminated = true;
    }
    
    public StreamReader(final Type[] types) {
        this.lineTerminated = true;
        this.types = types;
        if (types.length == 0) {
            this.typeReaders = new ObjectReader[0];
            return;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final ObjectReader[] readers = new ObjectReader[types.length];
        for (int i = 0; i < types.length; ++i) {
            final Type type = types[i];
            if (type == String.class || type == Object.class) {
                readers[i] = null;
            }
            else {
                readers[i] = provider.getObjectReader(type);
            }
        }
        this.typeReaders = readers;
    }
    
    protected abstract boolean seekLine() throws IOException;
    
    public abstract <T> T readLineObject();
    
    public <T> Stream<T> stream() {
        return StreamSupport.stream(new StreamReaderSpliterator<T>((StreamReader<T>)this), false);
    }
    
    public <T> Stream<T> stream(final Class<T> clazz) {
        return StreamSupport.stream(new StreamReaderSpliterator<T>((StreamReader<T>)this, clazz), false);
    }
    
    public enum Feature
    {
        IgnoreEmptyLine(1L), 
        ErrorAsNull(2L);
        
        public final long mask;
        
        private Feature(final long mask) {
            this.mask = mask;
        }
        
        private static /* synthetic */ Feature[] $values() {
            return new Feature[] { Feature.IgnoreEmptyLine, Feature.ErrorAsNull };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public static class ColumnStat
    {
        @JSONField(ordinal = -1)
        public final String name;
        public int values;
        public int nulls;
        public int integers;
        public int doubles;
        public int numbers;
        public int dates;
        public int booleans;
        public int precision;
        public int scale;
        public int nonAsciiStrings;
        public int errors;
        public int maps;
        public int arrays;
        
        public ColumnStat(final String name) {
            this.name = name;
        }
        
        public void stat(final char[] bytes, final int off, final int len) {
            ++this.values;
            if (len == 0) {
                ++this.nulls;
                return;
            }
            final int end = off + len;
            boolean nonAscii = false;
            for (int i = off; i < end; ++i) {
                final char b = bytes[i];
                if (b > '\u007f') {
                    nonAscii = true;
                    break;
                }
            }
            if (nonAscii) {
                if (this.precision < len) {
                    this.precision = len;
                }
                ++this.nonAsciiStrings;
                return;
            }
            int precision = len;
            if (TypeUtils.isNumber(bytes, off, len)) {
                final char ch = bytes[off];
                if (ch == '+' || ch == '-') {
                    --precision;
                }
                ++this.numbers;
                if (TypeUtils.isInteger(bytes, off, len)) {
                    ++this.integers;
                }
                else {
                    boolean e = false;
                    int dotIndex = -1;
                    for (int j = off; j < end; ++j) {
                        final char b2 = bytes[j];
                        if (b2 == '.') {
                            dotIndex = j;
                        }
                        else if (b2 == 'e' || b2 == 'E') {
                            e = true;
                        }
                    }
                    if (e) {
                        ++this.doubles;
                    }
                    else if (dotIndex != -1) {
                        final int scale = end - dotIndex - 1;
                        if (this.scale < scale) {
                            this.scale = scale;
                        }
                        --precision;
                    }
                }
            }
            else {
                boolean checkDate = false;
                int sub = 0;
                int slash = 0;
                int colon = 0;
                int dot = 0;
                int nums = 0;
                for (int k = off; k < end; ++k) {
                    final char ch2 = bytes[k];
                    switch (ch2) {
                        case '-': {
                            ++sub;
                            break;
                        }
                        case '/': {
                            ++slash;
                            break;
                        }
                        case ':': {
                            ++colon;
                            break;
                        }
                        case '.': {
                            ++dot;
                            break;
                        }
                        default: {
                            if (ch2 >= '0' && ch2 <= '9') {
                                ++nums;
                                break;
                            }
                            break;
                        }
                    }
                }
                if (sub == 2 || slash == 2 || colon == 2) {
                    checkDate = true;
                }
                if (checkDate && (nums < 2 || len > 36)) {
                    checkDate = false;
                }
                if (checkDate) {
                    try {
                        LocalDateTime ldt = null;
                        switch (len) {
                            case 8: {
                                final LocalDate localDate = DateUtils.parseLocalDate8(bytes, off);
                                ldt = localDate.atStartOfDay();
                                break;
                            }
                            case 9: {
                                final LocalDate localDate = DateUtils.parseLocalDate9(bytes, off);
                                ldt = localDate.atStartOfDay();
                                break;
                            }
                            case 10: {
                                final LocalDate localDate = DateUtils.parseLocalDate10(bytes, off);
                                ldt = localDate.atStartOfDay();
                                break;
                            }
                        }
                        if (ldt == null) {
                            final String str = new String(bytes, off, len);
                            final ZonedDateTime zdt = DateUtils.parseZonedDateTime(str);
                            if (zdt != null) {
                                ldt = zdt.toLocalDateTime();
                            }
                        }
                        if (ldt != null) {
                            precision = 0;
                            ++this.dates;
                        }
                        final int nanoOfSeconds = ldt.getNano();
                        if (nanoOfSeconds != 0) {
                            if (nanoOfSeconds % 100000000 == 0) {
                                precision = 1;
                            }
                            else if (nanoOfSeconds % 10000000 == 0) {
                                precision = 2;
                            }
                            else if (nanoOfSeconds % 1000000 == 0) {
                                precision = 3;
                            }
                            else if (nanoOfSeconds % 100000 == 0) {
                                precision = 4;
                            }
                            else if (nanoOfSeconds % 10000 == 0) {
                                precision = 5;
                            }
                            else if (nanoOfSeconds % 1000 == 0) {
                                precision = 6;
                            }
                            else if (nanoOfSeconds % 100 == 0) {
                                precision = 7;
                            }
                            else if (nanoOfSeconds % 10 == 0) {
                                precision = 8;
                            }
                            else {
                                precision = 9;
                            }
                        }
                    }
                    catch (Exception ignored) {
                        ++this.errors;
                    }
                }
            }
            if (this.precision < precision) {
                this.precision = precision;
            }
        }
        
        public void stat(final byte[] bytes, final int off, final int len, final Charset charset) {
            ++this.values;
            if (len == 0) {
                ++this.nulls;
                return;
            }
            final int end = off + len;
            boolean nonAscii = false;
            for (int i = off; i < end; ++i) {
                final byte b = bytes[i];
                if (b < 0) {
                    nonAscii = true;
                    break;
                }
            }
            if (nonAscii) {
                if (this.precision < len) {
                    this.precision = len;
                }
                ++this.nonAsciiStrings;
                return;
            }
            int precision = len;
            if (TypeUtils.isNumber(bytes, off, len)) {
                final char ch = (char)bytes[off];
                if (ch == '+' || ch == '-') {
                    --precision;
                }
                ++this.numbers;
                if (TypeUtils.isInteger(bytes, off, len)) {
                    ++this.integers;
                }
                else {
                    boolean e = false;
                    int dotIndex = -1;
                    for (int j = off; j < end; ++j) {
                        final byte b2 = bytes[j];
                        if (b2 == 46) {
                            dotIndex = j;
                        }
                        else if (b2 == 101 || b2 == 69) {
                            e = true;
                        }
                    }
                    if (e) {
                        ++this.doubles;
                    }
                    else if (dotIndex != -1) {
                        final int scale = end - dotIndex - 1;
                        if (this.scale < scale) {
                            this.scale = scale;
                        }
                        --precision;
                    }
                }
            }
            else {
                boolean checkDate = false;
                int sub = 0;
                int slash = 0;
                int colon = 0;
                int dot = 0;
                int nums = 0;
                for (int k = off; k < end; ++k) {
                    final char ch2 = (char)bytes[k];
                    switch (ch2) {
                        case '-': {
                            ++sub;
                            break;
                        }
                        case '/': {
                            ++slash;
                            break;
                        }
                        case ':': {
                            ++colon;
                            break;
                        }
                        case '.': {
                            ++dot;
                            break;
                        }
                        default: {
                            if (ch2 >= '0' && ch2 <= '9') {
                                ++nums;
                                break;
                            }
                            break;
                        }
                    }
                }
                if (sub == 2 || slash == 2 || colon == 2) {
                    checkDate = true;
                }
                if (checkDate && (nums < 2 || len > 36)) {
                    checkDate = false;
                }
                if (checkDate) {
                    try {
                        LocalDateTime ldt = null;
                        switch (len) {
                            case 8: {
                                final LocalDate localDate = DateUtils.parseLocalDate8(bytes, off);
                                ldt = localDate.atStartOfDay();
                                break;
                            }
                            case 9: {
                                final LocalDate localDate = DateUtils.parseLocalDate9(bytes, off);
                                ldt = localDate.atStartOfDay();
                                break;
                            }
                            case 10: {
                                final LocalDate localDate = DateUtils.parseLocalDate10(bytes, off);
                                ldt = localDate.atStartOfDay();
                                break;
                            }
                        }
                        if (ldt == null) {
                            final String str = new String(bytes, off, len, charset);
                            final ZonedDateTime zdt = DateUtils.parseZonedDateTime(str);
                            if (zdt != null) {
                                ldt = zdt.toLocalDateTime();
                            }
                        }
                        if (ldt != null) {
                            precision = 0;
                            ++this.dates;
                        }
                        final int nanoOfSeconds = ldt.getNano();
                        if (nanoOfSeconds != 0) {
                            if (nanoOfSeconds % 100000000 == 0) {
                                precision = 1;
                            }
                            else if (nanoOfSeconds % 10000000 == 0) {
                                precision = 2;
                            }
                            else if (nanoOfSeconds % 1000000 == 0) {
                                precision = 3;
                            }
                            else if (nanoOfSeconds % 100000 == 0) {
                                precision = 4;
                            }
                            else if (nanoOfSeconds % 10000 == 0) {
                                precision = 5;
                            }
                            else if (nanoOfSeconds % 1000 == 0) {
                                precision = 6;
                            }
                            else if (nanoOfSeconds % 100 == 0) {
                                precision = 7;
                            }
                            else if (nanoOfSeconds % 10 == 0) {
                                precision = 8;
                            }
                            else {
                                precision = 9;
                            }
                        }
                    }
                    catch (Exception ignored) {
                        ++this.errors;
                    }
                }
            }
            if (this.precision < precision) {
                this.precision = precision;
            }
        }
        
        public void stat(final String str) {
            if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_CODER.applyAsInt(str) == JDKUtils.LATIN1 && JDKUtils.STRING_VALUE != null) {
                final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
                this.stat(bytes, 0, bytes.length, StandardCharsets.ISO_8859_1);
                return;
            }
            final char[] chars = JDKUtils.getCharArray(str);
            this.stat(chars, 0, chars.length);
        }
        
        public String getInferSQLType() {
            if (this.nonAsciiStrings > 0 || this.nulls == this.values) {
                return "STRING";
            }
            if (this.values == this.dates + this.nulls) {
                if (this.precision != 0) {
                    return "TIMESTAMP";
                }
                return "DATETIME";
            }
            else if (this.values == this.integers + this.nulls) {
                if (this.precision < 10) {
                    return "INT";
                }
                if (this.precision < 20) {
                    return "BIGINT";
                }
                return "DECIMAL(" + this.precision + ", 0)";
            }
            else {
                if (this.values != this.numbers + this.nulls) {
                    return "STRING";
                }
                if (this.doubles > 0 || this.scale > 5) {
                    return "DOUBLE";
                }
                int precision = this.precision;
                if (precision < 19) {
                    precision = 19;
                }
                return "DECIMAL(" + precision + ", " + this.scale + ")";
            }
        }
        
        public Type getInferType() {
            if (this.nonAsciiStrings > 0 || this.nulls == this.values) {
                return String.class;
            }
            if (this.values == this.booleans + this.nulls) {
                return Boolean.class;
            }
            if (this.values == this.dates + this.nulls) {
                if (this.precision != 0) {
                    return Instant.class;
                }
                return Date.class;
            }
            else {
                if (this.doubles > 0) {
                    return Double.class;
                }
                if (this.values == this.integers + this.nulls) {
                    if (this.precision < 10) {
                        return Integer.class;
                    }
                    if (this.precision < 20) {
                        return Long.class;
                    }
                    return BigInteger.class;
                }
                else {
                    if (this.values == this.numbers + this.nulls) {
                        return BigDecimal.class;
                    }
                    if (this.arrays > 0) {
                        return Collection.class;
                    }
                    if (this.maps > 0) {
                        return Map.class;
                    }
                    return String.class;
                }
            }
        }
    }
    
    protected static class StreamReaderSpliterator<T> implements Spliterator<T>
    {
        private final StreamReader<T> streamReader;
        private Class<T> clazz;
        private CSVReader csvReader;
        
        public StreamReaderSpliterator(final StreamReader<T> streamReader) {
            this.streamReader = streamReader;
            if (streamReader instanceof CSVReader) {
                final CSVReader reader = (CSVReader)streamReader;
                if (!reader.isObjectSupport()) {
                    this.csvReader = reader;
                }
            }
        }
        
        public StreamReaderSpliterator(final StreamReader<T> streamReader, final Class<T> clazz) {
            this.streamReader = streamReader;
            this.clazz = clazz;
            if (streamReader instanceof CSVReader) {
                final CSVReader reader = (CSVReader)streamReader;
                if (!reader.isObjectSupport()) {
                    this.csvReader = reader;
                }
            }
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super T> action) {
            if (action == null) {
                throw new IllegalArgumentException("action must not be null");
            }
            final T object = this.next();
            if (this.streamReader.inputEnd || object == null) {
                return false;
            }
            action.accept((Object)object);
            return true;
        }
        
        private T next() {
            if (this.csvReader == null) {
                return this.streamReader.readLineObject();
            }
            final Object[] objects = this.csvReader.readLineValues();
            if (this.clazz != null & !this.clazz.isAssignableFrom(objects.getClass())) {
                throw new ClassCastException(String.format("%s can not cast to %s", objects.getClass(), this.clazz));
            }
            return (T)objects;
        }
        
        @Override
        public Spliterator<T> trySplit() {
            throw new UnsupportedOperationException("parallel stream not supported");
        }
        
        @Override
        public long estimateSize() {
            return this.streamReader.inputEnd ? 0L : Long.MAX_VALUE;
        }
        
        @Override
        public int characteristics() {
            return 1296;
        }
    }
}
