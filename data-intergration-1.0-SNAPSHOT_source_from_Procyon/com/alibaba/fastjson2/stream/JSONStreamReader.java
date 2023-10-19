// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.stream;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONFactory;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.io.File;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;

public abstract class JSONStreamReader<T> extends StreamReader<T>
{
    protected ObjectReaderAdapter objectReader;
    
    public JSONStreamReader(final Type[] types) {
        super(types);
    }
    
    public JSONStreamReader(final ObjectReaderAdapter objectReader) {
        this.objectReader = objectReader;
    }
    
    public static JSONStreamReader of(final File file) throws IOException {
        return of(Files.newInputStream(file.toPath(), new OpenOption[0]), StandardCharsets.UTF_8, new Type[0]);
    }
    
    public static JSONStreamReader of(final InputStream in) throws IOException {
        return of(in, StandardCharsets.UTF_8, new Type[0]);
    }
    
    public static JSONStreamReader of(final InputStream in, final Type... types) throws IOException {
        return of(in, StandardCharsets.UTF_8, types);
    }
    
    public static JSONStreamReader of(final InputStream in, final Charset charset, final Type... types) {
        if (charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return new JSONStreamReaderUTF16(new InputStreamReader(in, charset), types);
        }
        return new JSONStreamReaderUTF8(in, charset, types);
    }
    
    public static JSONStreamReader of(final InputStream in, final Class objectClass) {
        return of(in, StandardCharsets.UTF_8, objectClass);
    }
    
    public static JSONStreamReader of(final InputStream in, final Charset charset, final Class objectClass) {
        final JSONReader.Context context = JSONFactory.createReadContext();
        final ObjectReaderAdapter objectReader = (ObjectReaderAdapter)context.getObjectReader(objectClass);
        if (charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return new JSONStreamReaderUTF16(new InputStreamReader(in, charset), objectReader);
        }
        return new JSONStreamReaderUTF8(in, charset, objectReader);
    }
    
    public ColumnStat getColumnStat(final String name) {
        if (this.columnStatsMap == null) {
            this.columnStatsMap = new LinkedHashMap<String, ColumnStat>();
        }
        if (this.columns == null) {
            this.columns = new ArrayList<String>();
        }
        if (this.columnStats == null) {
            this.columnStats = new ArrayList<ColumnStat>();
        }
        ColumnStat stat = this.columnStatsMap.get(name);
        if (stat == null && this.columnStatsMap.size() <= 100) {
            stat = new ColumnStat(name);
            this.columnStatsMap.put(name, stat);
            this.columns.add(name);
            this.columnStats.add(stat);
        }
        return stat;
    }
    
    protected static void stat(final ColumnStat stat, final Object value) {
        if (stat == null) {
            return;
        }
        if (value == null) {
            ++stat.nulls;
            return;
        }
        ++stat.values;
        if (value instanceof Number) {
            ++stat.numbers;
            if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
                ++stat.integers;
            }
            else if (value instanceof Float || value instanceof Double) {
                ++stat.doubles;
            }
            return;
        }
        if (value instanceof String) {
            stat.stat((String)value);
            return;
        }
        if (value instanceof Boolean) {
            ++stat.booleans;
            return;
        }
        if (value instanceof Map) {
            ++stat.maps;
            return;
        }
        if (value instanceof Collection) {
            ++stat.arrays;
        }
    }
    
    public void statAll() {
        this.columnStatsMap = new LinkedHashMap<String, ColumnStat>();
        this.columns = new ArrayList<String>();
        this.columnStats = new ArrayList<ColumnStat>();
        while (true) {
            final Object object = this.readLineObject();
            if (object == null) {
                break;
            }
            this.statLine(object);
        }
    }
    
    public void statLine(final Object object) {
        if (object instanceof Map) {
            this.statMap(null, (Map)object, 0);
        }
        else if (object instanceof List) {
            this.statArray(null, (List)object, 0);
        }
        ++this.rowCount;
    }
    
    private void statArray(final String parentKey, final List list, final int level) {
        if (level > 10) {
            return;
        }
        if (list.size() > 10) {
            return;
        }
        for (int i = 0; i < list.size(); ++i) {
            final Object item = list.get(i);
            final String strKey = (parentKey == null) ? ("[" + i + "]") : (parentKey + "[" + i + "]");
            final ColumnStat stat = this.getColumnStat(parentKey);
            stat(stat, item);
            if (item instanceof Map) {
                this.statMap(strKey, (Map)item, level + 1);
            }
            else if (item instanceof List) {
                this.statArray(strKey, (List)item, level + 1);
            }
        }
    }
    
    private void statMap(final String parentKey, final Map map, final int level) {
        if (level > 10) {
            return;
        }
        for (final Object o : map.entrySet()) {
            final Map.Entry entry = (Map.Entry)o;
            final Object key = entry.getKey();
            if (key instanceof String) {
                final String strKey = (String)((parentKey == null) ? key : (parentKey + "." + key));
                final ColumnStat stat = this.getColumnStat(strKey);
                final Object entryValue = entry.getValue();
                stat(stat, entryValue);
                if (entryValue instanceof Map) {
                    this.statMap(strKey, (Map)entryValue, level + 1);
                }
                else {
                    if (!(entryValue instanceof List)) {
                        continue;
                    }
                    this.statArray(strKey, (List)entryValue, level + 1);
                }
            }
        }
    }
}
