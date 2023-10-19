// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import com.typesafe.config.ConfigOrigin;
import com.typesafe.config.ConfigValueType;

final class DefaultTransformer
{
    static AbstractConfigValue transform(final AbstractConfigValue value, final ConfigValueType requested) {
        if (value.valueType() == ConfigValueType.STRING) {
            final String s = (String)value.unwrapped();
            switch (requested) {
                case NUMBER: {
                    try {
                        final Long v = Long.parseLong(s);
                        return new ConfigLong(value.origin(), v, s);
                    }
                    catch (NumberFormatException ex) {
                        try {
                            final Double v2 = Double.parseDouble(s);
                            return new ConfigDouble(value.origin(), v2, s);
                        }
                        catch (NumberFormatException ex2) {}
                    }
                }
                case NULL: {
                    if (s.equals("null")) {
                        return new ConfigNull(value.origin());
                    }
                    break;
                }
                case BOOLEAN: {
                    if (s.equals("true") || s.equals("yes") || s.equals("on")) {
                        return new ConfigBoolean(value.origin(), true);
                    }
                    if (s.equals("false") || s.equals("no") || s.equals("off")) {
                        return new ConfigBoolean(value.origin(), false);
                    }
                    break;
                }
                case LIST: {}
            }
        }
        else if (requested == ConfigValueType.STRING) {
            switch (value.valueType()) {
                case NUMBER:
                case BOOLEAN: {
                    return new ConfigString.Quoted(value.origin(), value.transformToString());
                }
                case NULL: {}
                case OBJECT: {}
            }
        }
        else if (requested == ConfigValueType.LIST && value.valueType() == ConfigValueType.OBJECT) {
            final AbstractConfigObject o = (AbstractConfigObject)value;
            final Map<Integer, AbstractConfigValue> values = new HashMap<Integer, AbstractConfigValue>();
            for (final String key : ((Map<String, V>)o).keySet()) {
                try {
                    final int i = Integer.parseInt(key, 10);
                    if (i < 0) {
                        continue;
                    }
                    values.put(i, o.get(key));
                }
                catch (NumberFormatException e) {}
            }
            if (!values.isEmpty()) {
                final ArrayList<Map.Entry<Integer, AbstractConfigValue>> entryList = new ArrayList<Map.Entry<Integer, AbstractConfigValue>>(values.entrySet());
                Collections.sort(entryList, new Comparator<Map.Entry<Integer, AbstractConfigValue>>() {
                    @Override
                    public int compare(final Map.Entry<Integer, AbstractConfigValue> a, final Map.Entry<Integer, AbstractConfigValue> b) {
                        return Integer.compare(a.getKey(), b.getKey());
                    }
                });
                final ArrayList<AbstractConfigValue> list = new ArrayList<AbstractConfigValue>();
                for (final Map.Entry<Integer, AbstractConfigValue> entry : entryList) {
                    list.add(entry.getValue());
                }
                return new SimpleConfigList(value.origin(), list);
            }
        }
        return value;
    }
}
