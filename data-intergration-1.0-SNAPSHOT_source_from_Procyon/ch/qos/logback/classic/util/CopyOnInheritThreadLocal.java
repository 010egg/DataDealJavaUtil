// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.util;

import java.util.Map;
import java.util.HashMap;

public class CopyOnInheritThreadLocal extends InheritableThreadLocal<HashMap<String, String>>
{
    @Override
    protected HashMap<String, String> childValue(final HashMap<String, String> parentValue) {
        if (parentValue == null) {
            return null;
        }
        return new HashMap<String, String>(parentValue);
    }
}
