// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import java.util.HashMap;
import java.util.Map;

public class ArrayAssistantRegistry
{
    private static Map<Integer, ArrayAssistant> arrayAssistantMap;
    
    public static ArrayAssistant getAssistant(final int oid) {
        return ArrayAssistantRegistry.arrayAssistantMap.get(oid);
    }
    
    public static void register(final int oid, final ArrayAssistant assistant) {
        ArrayAssistantRegistry.arrayAssistantMap.put(oid, assistant);
    }
    
    static {
        ArrayAssistantRegistry.arrayAssistantMap = new HashMap<Integer, ArrayAssistant>();
    }
}
