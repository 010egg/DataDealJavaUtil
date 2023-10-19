// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.Map;

public final class StringBuilders
{
    private StringBuilders() {
    }
    
    public static StringBuilder appendDqValue(final StringBuilder sb, final Object value) {
        return sb.append('\"').append(value).append('\"');
    }
    
    public static StringBuilder appendKeyDqValue(final StringBuilder sb, final Map.Entry<String, String> entry) {
        return appendKeyDqValue(sb, entry.getKey(), entry.getValue());
    }
    
    public static StringBuilder appendKeyDqValue(final StringBuilder sb, final String key, final Object value) {
        return sb.append(key).append('=').append('\"').append(value).append('\"');
    }
    
    public static void appendValue(final StringBuilder stringBuilder, final Object obj) {
        if (obj == null || obj instanceof String) {
            stringBuilder.append((String)obj);
        }
        else if (obj instanceof StringBuilderFormattable) {
            ((StringBuilderFormattable)obj).formatTo(stringBuilder);
        }
        else if (obj instanceof CharSequence) {
            stringBuilder.append((CharSequence)obj);
        }
        else if (obj instanceof Integer) {
            stringBuilder.append((int)obj);
        }
        else if (obj instanceof Long) {
            stringBuilder.append((long)obj);
        }
        else if (obj instanceof Double) {
            stringBuilder.append((double)obj);
        }
        else if (obj instanceof Boolean) {
            stringBuilder.append((boolean)obj);
        }
        else if (obj instanceof Character) {
            stringBuilder.append((char)obj);
        }
        else if (obj instanceof Short) {
            stringBuilder.append((short)obj);
        }
        else if (obj instanceof Float) {
            stringBuilder.append((float)obj);
        }
        else {
            stringBuilder.append(obj);
        }
    }
}
