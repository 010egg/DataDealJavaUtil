// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import com.typesafe.config.ConfigException;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.util.Properties;
import com.typesafe.config.ConfigOrigin;
import java.io.Reader;

final class PropertiesParser
{
    static AbstractConfigObject parse(final Reader reader, final ConfigOrigin origin) throws IOException {
        final Properties props = new Properties();
        props.load(reader);
        return fromProperties(origin, props);
    }
    
    static String lastElement(final String path) {
        final int i = path.lastIndexOf(46);
        if (i < 0) {
            return path;
        }
        return path.substring(i + 1);
    }
    
    static String exceptLastElement(final String path) {
        final int i = path.lastIndexOf(46);
        if (i < 0) {
            return null;
        }
        return path.substring(0, i);
    }
    
    static Path pathFromPropertyKey(final String key) {
        String last;
        String exceptLast;
        Path path;
        for (last = lastElement(key), exceptLast = exceptLastElement(key), path = new Path(last, null); exceptLast != null; exceptLast = exceptLastElement(exceptLast), path = new Path(last, path)) {
            last = lastElement(exceptLast);
        }
        return path;
    }
    
    static AbstractConfigObject fromProperties(final ConfigOrigin origin, final Properties props) {
        final Map<Path, Object> pathMap = new HashMap<Path, Object>();
        for (final Map.Entry<Object, Object> entry : props.entrySet()) {
            final Object key = entry.getKey();
            if (key instanceof String) {
                final Path path = pathFromPropertyKey((String)key);
                pathMap.put(path, entry.getValue());
            }
        }
        return fromPathMap(origin, pathMap, true);
    }
    
    static AbstractConfigObject fromPathMap(final ConfigOrigin origin, final Map<?, ?> pathExpressionMap) {
        final Map<Path, Object> pathMap = new HashMap<Path, Object>();
        for (final Map.Entry<?, ?> entry : pathExpressionMap.entrySet()) {
            final Object keyObj = entry.getKey();
            if (!(keyObj instanceof String)) {
                throw new ConfigException.BugOrBroken("Map has a non-string as a key, expecting a path expression as a String");
            }
            final Path path = Path.newPath((String)keyObj);
            pathMap.put(path, entry.getValue());
        }
        return fromPathMap(origin, pathMap, false);
    }
    
    private static AbstractConfigObject fromPathMap(final ConfigOrigin origin, final Map<Path, Object> pathMap, final boolean convertedFromProperties) {
        final Set<Path> scopePaths = new HashSet<Path>();
        final Set<Path> valuePaths = new HashSet<Path>();
        for (final Path path : pathMap.keySet()) {
            valuePaths.add(path);
            for (Path next = path.parent(); next != null; next = next.parent()) {
                scopePaths.add(next);
            }
        }
        if (convertedFromProperties) {
            valuePaths.removeAll(scopePaths);
        }
        else {
            for (final Path path : valuePaths) {
                if (scopePaths.contains(path)) {
                    throw new ConfigException.BugOrBroken("In the map, path '" + path.render() + "' occurs as both the parent object of a value and as a value. Because Map has no defined ordering, this is a broken situation.");
                }
            }
        }
        final Map<String, AbstractConfigValue> root = new HashMap<String, AbstractConfigValue>();
        final Map<Path, Map<String, AbstractConfigValue>> scopes = new HashMap<Path, Map<String, AbstractConfigValue>>();
        for (final Path path2 : scopePaths) {
            final Map<String, AbstractConfigValue> scope = new HashMap<String, AbstractConfigValue>();
            scopes.put(path2, scope);
        }
        for (final Path path2 : valuePaths) {
            final Path parentPath = path2.parent();
            final Map<String, AbstractConfigValue> parent = (parentPath != null) ? scopes.get(parentPath) : root;
            final String last = path2.last();
            final Object rawValue = pathMap.get(path2);
            AbstractConfigValue value;
            if (convertedFromProperties) {
                if (rawValue instanceof String) {
                    value = new ConfigString.Quoted(origin, (String)rawValue);
                }
                else {
                    value = null;
                }
            }
            else {
                value = ConfigImpl.fromAnyRef(pathMap.get(path2), origin, FromMapMode.KEYS_ARE_PATHS);
            }
            if (value != null) {
                parent.put(last, value);
            }
        }
        final List<Path> sortedScopePaths = new ArrayList<Path>();
        sortedScopePaths.addAll(scopePaths);
        Collections.sort(sortedScopePaths, new Comparator<Path>() {
            @Override
            public int compare(final Path a, final Path b) {
                return b.length() - a.length();
            }
        });
        for (final Path scopePath : sortedScopePaths) {
            final Map<String, AbstractConfigValue> scope2 = scopes.get(scopePath);
            final Path parentPath2 = scopePath.parent();
            final Map<String, AbstractConfigValue> parent2 = (parentPath2 != null) ? scopes.get(parentPath2) : root;
            final AbstractConfigObject o = new SimpleConfigObject(origin, scope2, ResolveStatus.RESOLVED, false);
            parent2.put(scopePath.last(), o);
        }
        return new SimpleConfigObject(origin, root, ResolveStatus.RESOLVED, false);
    }
}
