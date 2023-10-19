// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.json;

import java.util.Iterator;
import com.alibaba.druid.util.Utils;
import java.text.SimpleDateFormat;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import java.util.Collection;
import java.util.Date;

public class JSONWriter
{
    private StringBuilder out;
    
    public JSONWriter() {
        this.out = new StringBuilder();
    }
    
    public void writeArrayStart() {
        this.write('[');
    }
    
    public void writeComma() {
        this.write(',');
    }
    
    public void writeArrayEnd() {
        this.write(']');
    }
    
    public void writeNull() {
        this.write("null");
    }
    
    public void writeObject(final Object o) {
        if (o == null) {
            this.writeNull();
            return;
        }
        if (o instanceof String) {
            this.writeString((String)o);
            return;
        }
        if (o instanceof Number) {
            this.write(o.toString());
            return;
        }
        if (o instanceof Boolean) {
            this.write(o.toString());
            return;
        }
        if (o instanceof Date) {
            this.writeDate((Date)o);
            return;
        }
        if (o instanceof Collection) {
            this.writeArray((Collection<Object>)o);
            return;
        }
        if (o instanceof Throwable) {
            this.writeError((Throwable)o);
            return;
        }
        if (o instanceof int[]) {
            final int[] array = (int[])o;
            this.write('[');
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    this.write(',');
                }
                this.write(array[i]);
            }
            this.write(']');
            return;
        }
        if (o instanceof long[]) {
            final long[] array2 = (long[])o;
            this.write('[');
            for (int i = 0; i < array2.length; ++i) {
                if (i != 0) {
                    this.write(',');
                }
                this.write(array2[i]);
            }
            this.write(']');
            return;
        }
        if (o instanceof TabularData) {
            this.writeTabularData((TabularData)o);
            return;
        }
        if (o instanceof CompositeData) {
            this.writeCompositeData((CompositeData)o);
            return;
        }
        if (o instanceof Map) {
            this.writeMap((Map<String, Object>)o);
            return;
        }
        if (o == SQLEvalVisitor.EVAL_VALUE_NULL) {
            this.write("null");
            return;
        }
        throw new IllegalArgumentException("not support type : " + o.getClass());
    }
    
    public void writeDate(final Date date) {
        if (date == null) {
            this.writeNull();
            return;
        }
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.writeString(dateFormat.format(date));
    }
    
    public void writeError(final Throwable error) {
        if (error == null) {
            this.writeNull();
            return;
        }
        this.write("{\"Class\":");
        this.writeString(error.getClass().getName());
        this.write(",\"Message\":");
        this.writeString(error.getMessage());
        this.write(",\"StackTrace\":");
        this.writeString(Utils.getStackTrace(error));
        this.write('}');
    }
    
    public void writeArray(final Object[] array) {
        if (array == null) {
            this.writeNull();
            return;
        }
        this.write('[');
        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                this.write(',');
            }
            this.writeObject(array[i]);
        }
        this.write(']');
    }
    
    public void writeArray(final Collection<Object> list) {
        if (list == null) {
            this.writeNull();
            return;
        }
        int entryIndex = 0;
        this.write('[');
        for (final Object entry : list) {
            if (entryIndex != 0) {
                this.write(',');
            }
            this.writeObject(entry);
            ++entryIndex;
        }
        this.write(']');
    }
    
    public void writeString(final String text) {
        if (text == null) {
            this.writeNull();
            return;
        }
        this.write('\"');
        for (int i = 0; i < text.length(); ++i) {
            final char c = text.charAt(i);
            if (c == '\"') {
                this.write("\\\"");
            }
            else if (c == '\n') {
                this.write("\\n");
            }
            else if (c == '\r') {
                this.write("\\r");
            }
            else if (c == '\\') {
                this.write("\\\\");
            }
            else if (c == '\t') {
                this.write("\\t");
            }
            else if (c < '\u0010') {
                this.write("\\u000");
                this.write(Integer.toHexString(c));
            }
            else if (c < ' ') {
                this.write("\\u00");
                this.write(Integer.toHexString(c));
            }
            else if (c >= '\u007f' && c <= ' ') {
                this.write("\\u00");
                this.write(Integer.toHexString(c));
            }
            else {
                this.write(c);
            }
        }
        this.write('\"');
    }
    
    public void writeTabularData(final TabularData tabularData) {
        if (tabularData == null) {
            this.writeNull();
            return;
        }
        int entryIndex = 0;
        this.write('[');
        for (final Object item : tabularData.values()) {
            if (entryIndex != 0) {
                this.write(',');
            }
            final CompositeData row = (CompositeData)item;
            this.writeCompositeData(row);
            ++entryIndex;
        }
        this.write(']');
    }
    
    public void writeCompositeData(final CompositeData compositeData) {
        if (compositeData == null) {
            this.writeNull();
            return;
        }
        int entryIndex = 0;
        this.write('{');
        for (final Object key : compositeData.getCompositeType().keySet()) {
            if (entryIndex != 0) {
                this.write(',');
            }
            this.writeString((String)key);
            this.write(':');
            final Object value = compositeData.get((String)key);
            this.writeObject(value);
            ++entryIndex;
        }
        this.write('}');
    }
    
    public void writeMap(final Map<String, Object> map) {
        if (map == null) {
            this.writeNull();
            return;
        }
        int entryIndex = 0;
        this.write('{');
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            if (entryIndex != 0) {
                this.write(',');
            }
            this.writeString(entry.getKey());
            this.write(':');
            this.writeObject(entry.getValue());
            ++entryIndex;
        }
        this.write('}');
    }
    
    protected void write(final String text) {
        this.out.append(text);
    }
    
    protected void write(final char c) {
        this.out.append(c);
    }
    
    protected void write(final int c) {
        this.out.append(c);
    }
    
    protected void write(final long c) {
        this.out.append(c);
    }
    
    @Override
    public String toString() {
        return this.out.toString();
    }
}
