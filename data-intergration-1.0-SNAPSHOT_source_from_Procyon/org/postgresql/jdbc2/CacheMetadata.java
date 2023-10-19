// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import org.postgresql.core.Field;
import java.util.List;
import java.util.Map;

public class CacheMetadata
{
    private static Map<String, List<CacheMetadataField>> _cache;
    
    protected boolean isCached(final String idFields) {
        return CacheMetadata._cache.containsKey(idFields);
    }
    
    protected void getCache(final String idFields, final Field[] fields) {
        final List<CacheMetadataField> liste = CacheMetadata._cache.get(idFields);
        if (liste != null) {
            int no = 0;
            for (final CacheMetadataField c : liste) {
                c.get(fields[no++]);
            }
        }
    }
    
    protected void setCache(final String idFields, final Field[] fields) {
        final List<CacheMetadataField> liste = new LinkedList<CacheMetadataField>();
        for (int i = 0; i < fields.length; ++i) {
            final CacheMetadataField c = new CacheMetadataField(fields[i]);
            liste.add(c);
        }
        CacheMetadata._cache.put(idFields, liste);
    }
    
    protected String getIdFields(final Field[] fields) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; ++i) {
            sb.append(this.getIdField(fields[i])).append('/');
        }
        return sb.toString();
    }
    
    private String getIdField(final Field f) {
        return f.getTableOid() + "." + f.getPositionInTable();
    }
    
    static {
        CacheMetadata._cache = new HashMap<String, List<CacheMetadataField>>();
    }
}
