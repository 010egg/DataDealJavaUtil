// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.support.csv;

import java.util.Iterator;
import java.io.FileInputStream;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import java.util.function.Consumer;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Arrays;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONFactory;
import java.util.List;
import com.alibaba.fastjson2.util.IOUtils;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.alibaba.fastjson2.reader.CharArrayValueConsumer;
import java.nio.charset.Charset;
import com.alibaba.fastjson2.reader.ByteArrayValueConsumer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.io.File;
import com.alibaba.fastjson2.JSONException;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.util.JDKUtils;
import java.io.Reader;
import java.lang.reflect.Type;
import java.io.Closeable;
import com.alibaba.fastjson2.stream.StreamReader;

public abstract class CSVReader<T> extends StreamReader<T> implements Closeable
{
    boolean quote;
    protected Class<T> objectClass;
    private boolean objectSupport;
    
    CSVReader() {
        this.objectSupport = true;
    }
    
    CSVReader(final Class<T> objectClass) {
        this.objectSupport = true;
        this.objectClass = objectClass;
    }
    
    public CSVReader(final Type[] types) {
        super(types);
        this.objectSupport = true;
        this.objectSupport = false;
    }
    
    public void config(final Feature... features) {
        for (final Feature feature : features) {
            this.features |= feature.mask;
        }
    }
    
    public void config(final Feature feature, final boolean state) {
        if (state) {
            this.features |= feature.mask;
        }
        else {
            this.features &= ~feature.mask;
        }
    }
    
    public static <T> CSVReader<T> of(final Reader reader, final Class<T> objectClass) {
        return new CSVReaderUTF16<T>(reader, objectClass);
    }
    
    public static <T> CSVReader of(final String str, final Class<T> objectClass) {
        if (JDKUtils.JVM_VERSION > 8 && JDKUtils.STRING_VALUE != null) {
            try {
                final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
                if (coder == 0) {
                    final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
                    return new CSVReaderUTF8(bytes, 0, bytes.length, StandardCharsets.ISO_8859_1, objectClass);
                }
            }
            catch (Exception e) {
                throw new JSONException("unsafe get String.coder error");
            }
        }
        final char[] chars = JDKUtils.getCharArray(str);
        return new CSVReaderUTF16(chars, 0, chars.length, objectClass);
    }
    
    public static <T> CSVReader<T> of(final char[] chars, final Class<T> objectClass) {
        return new CSVReaderUTF16<T>(chars, 0, chars.length, objectClass);
    }
    
    public static <T> CSVReader<T> of(final byte[] utf8Bytes, final Class<T> objectClass) {
        return of(utf8Bytes, 0, utf8Bytes.length, StandardCharsets.UTF_8, objectClass);
    }
    
    public static CSVReader of(final File file, final Type... types) throws IOException {
        return new CSVReaderUTF8(Files.newInputStream(file.toPath(), new OpenOption[0]), StandardCharsets.UTF_8, types);
    }
    
    public static CSVReader of(final File file, final ByteArrayValueConsumer consumer) throws IOException {
        return of(file, StandardCharsets.UTF_8, consumer);
    }
    
    public static CSVReader of(final File file, final Charset charset, final ByteArrayValueConsumer consumer) throws IOException {
        if (charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            throw new JSONException("not support charset : " + charset);
        }
        return new CSVReaderUTF8(Files.newInputStream(file.toPath(), new OpenOption[0]), charset, consumer);
    }
    
    public static CSVReader of(final File file, final CharArrayValueConsumer consumer) throws IOException {
        return of(file, StandardCharsets.UTF_8, consumer);
    }
    
    public static CSVReader of(final File file, final Charset charset, final CharArrayValueConsumer consumer) throws IOException {
        return new CSVReaderUTF16(new InputStreamReader(Files.newInputStream(file.toPath(), new OpenOption[0]), charset), consumer);
    }
    
    public static CSVReader of(final File file, final Charset charset, final Type... types) throws IOException {
        if (JDKUtils.JVM_VERSION == 8 || charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return new CSVReaderUTF16(new InputStreamReader(Files.newInputStream(file.toPath(), new OpenOption[0]), charset), types);
        }
        return new CSVReaderUTF8(Files.newInputStream(file.toPath(), new OpenOption[0]), charset, types);
    }
    
    public static <T> CSVReader<T> of(final File file, final Class<T> objectClass) throws IOException {
        return of(file, StandardCharsets.UTF_8, objectClass);
    }
    
    public static <T> CSVReader<T> of(final File file, final Charset charset, final Class<T> objectClass) throws IOException {
        if (JDKUtils.JVM_VERSION == 8 || charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return new CSVReaderUTF16<T>(new InputStreamReader(Files.newInputStream(file.toPath(), new OpenOption[0]), charset), objectClass);
        }
        return new CSVReaderUTF8<T>(Files.newInputStream(file.toPath(), new OpenOption[0]), charset, objectClass);
    }
    
    public static CSVReader of(final InputStream in, final Type... types) throws IOException {
        return of(in, StandardCharsets.UTF_8, types);
    }
    
    public static <T> CSVReader<T> of(final InputStream in, final Class<T> objectClass) {
        return of(in, StandardCharsets.UTF_8, objectClass);
    }
    
    public static <T> CSVReader<T> of(final InputStream in, final Charset charset, final Class<T> objectClass) {
        if (JDKUtils.JVM_VERSION == 8 || charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return new CSVReaderUTF16<T>(new InputStreamReader(in, charset), objectClass);
        }
        return new CSVReaderUTF8<T>(in, charset, objectClass);
    }
    
    public static CSVReader of(final InputStream in, final Charset charset, final Type... types) {
        if (JDKUtils.JVM_VERSION == 8 || charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return new CSVReaderUTF16(new InputStreamReader(in, charset), types);
        }
        return new CSVReaderUTF8(in, charset, types);
    }
    
    public static CSVReader of(final Reader in, final Type... types) {
        return new CSVReaderUTF16(in, types);
    }
    
    public static CSVReader of(final String str, final Type... types) {
        if (JDKUtils.JVM_VERSION > 8 && JDKUtils.STRING_VALUE != null) {
            try {
                final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
                if (coder == 0) {
                    final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
                    return new CSVReaderUTF8(bytes, 0, bytes.length, types);
                }
            }
            catch (Exception e) {
                throw new JSONException("unsafe get String.coder error");
            }
        }
        final char[] chars = JDKUtils.getCharArray(str);
        return new CSVReaderUTF16(chars, 0, chars.length, types);
    }
    
    public static CSVReader of(final char[] chars, final Type... types) {
        return new CSVReaderUTF16(chars, 0, chars.length, types);
    }
    
    public static <T> CSVReader<T> of(final char[] chars, final int off, final int len, final CharArrayValueConsumer consumer) {
        return new CSVReaderUTF16<T>(chars, off, len, consumer);
    }
    
    public static CSVReader of(final byte[] utf8Bytes, final Type... types) {
        return new CSVReaderUTF8(utf8Bytes, 0, utf8Bytes.length, types);
    }
    
    public static CSVReader of(final byte[] utf8Bytes, final ByteArrayValueConsumer consumer) {
        return of(utf8Bytes, 0, utf8Bytes.length, StandardCharsets.UTF_8, consumer);
    }
    
    public static <T> CSVReader<T> of(final byte[] utf8Bytes, final int off, final int len, final Charset charset, final ByteArrayValueConsumer consumer) {
        return new CSVReaderUTF8<T>(utf8Bytes, off, len, charset, consumer);
    }
    
    public static <T> CSVReader<T> of(final byte[] utf8Bytes, final Charset charset, final Class<T> objectClass) {
        return of(utf8Bytes, 0, utf8Bytes.length, charset, objectClass);
    }
    
    public static <T> CSVReader<T> of(final byte[] utf8Bytes, final int off, final int len, final Class<T> objectClass) {
        return new CSVReaderUTF8<T>(utf8Bytes, off, len, StandardCharsets.UTF_8, objectClass);
    }
    
    public static <T> CSVReader<T> of(final byte[] utf8Bytes, final int off, final int len, final Charset charset, final Class<T> objectClass) {
        if (charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            final char[] chars = new char[len];
            final int size = IOUtils.decodeUTF8(utf8Bytes, off, len, chars);
            return new CSVReaderUTF16<T>(chars, 0, size, objectClass);
        }
        return new CSVReaderUTF8<T>(utf8Bytes, off, len, charset, objectClass);
    }
    
    public static <T> CSVReader<T> of(final char[] utf8Bytes, final int off, final int len, final Class<T> objectClass) {
        return new CSVReaderUTF16<T>(utf8Bytes, off, len, objectClass);
    }
    
    public void skipLines(final int lines) throws IOException {
        if (lines < 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < lines; ++i) {
            this.seekLine();
        }
    }
    
    public List<String> readHeader() {
        this.objectSupport = true;
        final String[] columns = (String[])this.readLineValues(true);
        if (this.objectClass != null) {
            final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
            final boolean fieldBased = (this.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
            final Type[] types = new Type[columns.length];
            final ObjectReader[] typeReaders = new ObjectReader[columns.length];
            final FieldReader[] fieldReaders = new FieldReader[columns.length];
            for (int j = 0; j < columns.length; ++j) {
                final String column = columns[j].trim();
                final FieldReader fieldReader = provider.createFieldReader(this.objectClass, column, this.features);
                if (fieldReader != null) {
                    fieldReaders[j] = fieldReader;
                    Type fieldType = fieldReader.fieldType;
                    if (fieldType instanceof Class) {
                        final Class fieldClass = (Class)fieldType;
                        if (fieldClass.isPrimitive()) {
                            fieldType = TypeUtils.nonePrimitive((Class)fieldType);
                        }
                    }
                    types[j] = fieldType;
                    typeReaders[j] = provider.getObjectReader(fieldType, fieldBased);
                }
                else {
                    types[j] = String.class;
                }
            }
            this.types = types;
            this.typeReaders = typeReaders;
            this.fieldReaders = fieldReaders;
            this.objectCreator = provider.createObjectCreator(this.objectClass, this.features);
        }
        this.columns = Arrays.asList(columns);
        this.columnStats = new ArrayList<ColumnStat>();
        IntStream.range(0, columns.length).forEach(i -> this.columnStats.add(new ColumnStat(columns[i])));
        if (this.rowCount == 1) {
            this.rowCount = (this.lineTerminated ? 0 : -1);
        }
        return this.columns;
    }
    
    public List<String> getColumns() {
        return this.columns;
    }
    
    public String getColumn(final int columnIndex) {
        if (this.columns != null && columnIndex < this.columns.size()) {
            return this.columns.get(columnIndex);
        }
        return null;
    }
    
    public Type getColumnType(final int columnIndex) {
        if (this.types != null && columnIndex < this.types.length) {
            return this.types[columnIndex];
        }
        return null;
    }
    
    public List<ColumnStat> getColumnStats() {
        return this.columnStats;
    }
    
    public void readLineObjectAll(final Consumer<T> consumer) {
        this.readLineObjectAll(true, consumer);
    }
    
    public abstract void readLineObjectAll(final boolean p0, final Consumer<T> p1);
    
    @Override
    public T readLineObject() {
        if (!this.objectSupport) {
            throw new UnsupportedOperationException("this method should not be called, try specify objectClass or method readLineValues instead ?");
        }
        if (this.inputEnd) {
            return null;
        }
        if (this.fieldReaders == null) {
            final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
            if (this.objectClass == null) {
                throw new JSONException("not support operation, objectClass is null");
            }
            final boolean fieldBased = (this.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
            final ObjectReader objectReader = provider.getObjectReader(this.objectClass, fieldBased);
            if (!(objectReader instanceof ObjectReaderAdapter)) {
                throw new JSONException("not support operation : " + this.objectClass);
            }
            this.fieldReaders = ((ObjectReaderAdapter)objectReader).getFieldReaders();
            this.types = new Type[this.fieldReaders.length];
            for (int i = 0; i < this.types.length; ++i) {
                this.types[i] = this.fieldReaders[i].fieldType;
            }
            this.objectCreator = provider.createObjectCreator(this.objectClass, this.features);
        }
        if (this.objectCreator == null) {
            throw new JSONException("not support operation, objectClass is null");
        }
        final Object[] values = this.readLineValues(false);
        if (values == null) {
            return null;
        }
        if (this.fieldReaders != null) {
            final Object object = this.objectCreator.get();
            for (int j = 0; j < this.fieldReaders.length; ++j) {
                final FieldReader fieldReader = this.fieldReaders[j];
                if (fieldReader != null) {
                    fieldReader.accept(object, values[j]);
                }
            }
            return (T)object;
        }
        throw new JSONException("not support operation, objectClass is null");
    }
    
    public abstract boolean isEnd();
    
    public final Object[] readLineValues() {
        return this.readLineValues(false);
    }
    
    protected abstract Object[] readLineValues(final boolean p0);
    
    public final String[] readLine() {
        return (String[])this.readLineValues(true);
    }
    
    public static int rowCount(final String str, final Feature... features) {
        final CSVReader state = new CSVReaderUTF8(features);
        state.rowCount(str, str.length());
        return state.rowCount();
    }
    
    public static int rowCount(final byte[] bytes, final Feature... features) {
        final CSVReaderUTF8 state = new CSVReaderUTF8(features);
        state.rowCount(bytes, bytes.length);
        return state.rowCount();
    }
    
    public static int rowCount(final char[] chars, final Feature... features) {
        final CSVReaderUTF16 state = new CSVReaderUTF16(features);
        state.rowCount(chars, chars.length);
        return state.rowCount();
    }
    
    public static int rowCount(final File file) throws IOException {
        if (!file.exists()) {
            return -1;
        }
        final FileInputStream in = new FileInputStream(file);
        try {
            final int rowCount = rowCount(in);
            in.close();
            return rowCount;
        }
        catch (Throwable t) {
            try {
                in.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    public static int rowCount(final InputStream in) throws IOException {
        final byte[] bytes = new byte[524288];
        final CSVReaderUTF8 state = new CSVReaderUTF8(new Feature[0]);
        while (true) {
            final int cnt = in.read(bytes);
            if (cnt == -1) {
                break;
            }
            state.rowCount(bytes, cnt);
        }
        return state.rowCount();
    }
    
    public int errorCount() {
        return this.errorCount;
    }
    
    public int rowCount() {
        return this.lineTerminated ? this.rowCount : (this.rowCount + 1);
    }
    
    void rowCount(final String bytes, final int length) {
        this.lineTerminated = false;
        for (int i = 0; i < length; ++i) {
            final char ch = bytes.charAt(i);
            if (ch == '\"') {
                ++this.lineSize;
                if (!this.quote) {
                    this.quote = true;
                }
                else {
                    final int n = i + 1;
                    if (n >= length) {
                        break;
                    }
                    final char next = bytes.charAt(n);
                    if (next == '\"') {
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
            else if (ch == '\n') {
                if (this.lineSize > 0 || (this.features & Feature.IgnoreEmptyLine.mask) == 0x0L) {
                    ++this.rowCount;
                    this.lineSize = 0;
                }
                this.lineTerminated = (i + 1 == length);
            }
            else if (ch == '\r') {
                this.lineTerminated = true;
                if (this.lineSize > 0 || (this.features & Feature.IgnoreEmptyLine.mask) == 0x0L) {
                    ++this.rowCount;
                }
                this.lineSize = 0;
                final int n = i + 1;
                if (n >= length) {
                    break;
                }
                final char next = bytes.charAt(n);
                if (next == '\n') {
                    ++i;
                }
                this.lineTerminated = (i + 1 == length);
            }
            else {
                ++this.lineSize;
            }
        }
    }
    
    void rowCount(final byte[] bytes, final int length) {
        this.lineTerminated = false;
        for (int i = 0; i < length; ++i) {
            if (i + 4 < length) {
                final byte b0 = bytes[i];
                final byte b2 = bytes[i + 1];
                final byte b3 = bytes[i + 2];
                final byte b4 = bytes[i + 3];
                if (b0 > 34 && b2 > 34 && b3 > 34 && b4 > 34) {
                    this.lineSize += 4;
                    i += 3;
                    continue;
                }
            }
            final byte ch = bytes[i];
            if (ch == 34) {
                ++this.lineSize;
                if (!this.quote) {
                    this.quote = true;
                }
                else {
                    final int n = i + 1;
                    if (n >= length) {
                        break;
                    }
                    final byte next = bytes[n];
                    if (next == 34) {
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
            else if (ch == 10) {
                if (this.lineSize > 0 || (this.features & Feature.IgnoreEmptyLine.mask) == 0x0L) {
                    ++this.rowCount;
                }
                this.lineSize = 0;
                this.lineTerminated = (i + 1 == length);
            }
            else if (ch == 13) {
                if (this.lineSize > 0 || (this.features & Feature.IgnoreEmptyLine.mask) == 0x0L) {
                    ++this.rowCount;
                }
                this.lineTerminated = true;
                this.lineSize = 0;
                final int n = i + 1;
                if (n >= length) {
                    break;
                }
                final byte next = bytes[n];
                if (next == 10) {
                    ++i;
                }
                this.lineTerminated = (i + 1 == length);
            }
            else {
                ++this.lineSize;
            }
        }
    }
    
    void rowCount(final char[] bytes, final int length) {
        this.lineTerminated = false;
        for (int i = 0; i < length; ++i) {
            if (i + 4 < length) {
                final char b0 = bytes[i];
                final char b2 = bytes[i + 1];
                final char b3 = bytes[i + 2];
                final char b4 = bytes[i + 3];
                if (b0 > '\"' && b2 > '\"' && b3 > '\"' && b4 > '\"') {
                    i += 3;
                    this.lineSize += 4;
                    continue;
                }
            }
            final char ch = bytes[i];
            if (ch == '\"') {
                ++this.lineSize;
                if (!this.quote) {
                    this.quote = true;
                }
                else {
                    final int n = i + 1;
                    if (n >= length) {
                        break;
                    }
                    final char next = bytes[n];
                    if (next == '\"') {
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
            else if (ch == '\n') {
                if (this.lineSize > 0 || (this.features & Feature.IgnoreEmptyLine.mask) == 0x0L) {
                    ++this.rowCount;
                }
                this.lineSize = 0;
                this.lineTerminated = (i + 1 == length);
            }
            else if (ch == '\r' || (this.features & Feature.IgnoreEmptyLine.mask) == 0x0L) {
                if (this.lineSize > 0) {
                    ++this.rowCount;
                }
                this.lineTerminated = true;
                this.lineSize = 0;
                final int n = i + 1;
                if (n >= length) {
                    break;
                }
                final char next = bytes[n];
                if (next == '\n') {
                    ++i;
                }
                this.lineTerminated = (i + 1 == length);
            }
            else {
                ++this.lineSize;
            }
        }
    }
    
    protected Object error(final int columnIndex, final Exception e) {
        ++this.errorCount;
        final ColumnStat columnStat = this.getColumnStat(columnIndex);
        ++columnStat.errors;
        if ((this.features & Feature.ErrorAsNull.mask) != 0x0L) {
            return null;
        }
        String message = "read csv error, line " + this.rowCount + ", column ";
        String column = null;
        if (this.columns != null && columnIndex < this.columns.size()) {
            column = this.columns.get(columnIndex);
        }
        if (column != null && !column.isEmpty()) {
            message += column;
        }
        else {
            message += columnIndex;
        }
        throw new JSONException(message, e);
    }
    
    public ColumnStat getColumnStat(final String name) {
        if (this.columnStats != null) {
            for (final ColumnStat stat : this.columnStats) {
                if (name.equals(stat.name)) {
                    return stat;
                }
            }
        }
        return null;
    }
    
    public ColumnStat getColumnStat(final int i) {
        if (this.columnStats == null) {
            this.columnStats = new ArrayList<ColumnStat>();
        }
        ColumnStat stat = null;
        if (i >= this.columnStats.size()) {
            for (int j = this.columnStats.size(); j <= i; ++j) {
                String column = null;
                if (this.columns != null && i < this.columns.size()) {
                    column = this.columns.get(i);
                }
                stat = new ColumnStat(column);
                this.columnStats.add(stat);
            }
        }
        else {
            stat = this.columnStats.get(i);
        }
        return stat;
    }
    
    public List<String[]> readLineAll() {
        final List<String[]> lines = new ArrayList<String[]>();
        while (true) {
            final String[] line = this.readLine();
            if (line == null) {
                break;
            }
            lines.add(line);
        }
        return lines;
    }
    
    public List<T> readLineObjectAll() {
        final List<T> objects = new ArrayList<T>();
        while (true) {
            final T object = this.readLineObject();
            if (object == null) {
                break;
            }
            objects.add(object);
        }
        return objects;
    }
    
    public boolean isObjectSupport() {
        return this.objectSupport;
    }
    
    public abstract void statAll();
    
    public abstract void statAll(final int p0);
    
    public abstract void readAll();
    
    public abstract void readAll(final int p0);
}
