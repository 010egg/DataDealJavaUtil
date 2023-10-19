// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigException;
import java.io.ObjectInput;
import java.io.NotSerializableException;
import java.io.ObjectOutput;
import java.util.HashMap;
import com.typesafe.config.ConfigList;
import java.util.ArrayList;
import java.util.EnumMap;
import java.io.DataInput;
import java.util.Map;
import java.util.Collections;
import java.util.Iterator;
import java.io.IOException;
import java.util.List;
import java.io.DataOutput;
import java.io.ObjectStreamException;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigOrigin;
import com.typesafe.config.ConfigValue;
import java.io.Externalizable;

class SerializedConfigValue extends AbstractConfigValue implements Externalizable
{
    private static final long serialVersionUID = 1L;
    private ConfigValue value;
    private boolean wasConfig;
    
    public SerializedConfigValue() {
        super(null);
    }
    
    SerializedConfigValue(final ConfigValue value) {
        this();
        this.value = value;
        this.wasConfig = false;
    }
    
    SerializedConfigValue(final Config conf) {
        this(conf.root());
        this.wasConfig = true;
    }
    
    private Object readResolve() throws ObjectStreamException {
        if (this.wasConfig) {
            return ((ConfigObject)this.value).toConfig();
        }
        return this.value;
    }
    
    private static void writeOriginField(final DataOutput out, final SerializedField code, final Object v) throws IOException {
        switch (code) {
            case ORIGIN_DESCRIPTION: {
                out.writeUTF((String)v);
                break;
            }
            case ORIGIN_LINE_NUMBER: {
                out.writeInt((int)v);
                break;
            }
            case ORIGIN_END_LINE_NUMBER: {
                out.writeInt((int)v);
                break;
            }
            case ORIGIN_TYPE: {
                out.writeByte((int)v);
                break;
            }
            case ORIGIN_URL: {
                out.writeUTF((String)v);
                break;
            }
            case ORIGIN_RESOURCE: {
                out.writeUTF((String)v);
                break;
            }
            case ORIGIN_COMMENTS: {
                final List<String> list = (List<String>)v;
                final int size = list.size();
                out.writeInt(size);
                for (final String s : list) {
                    out.writeUTF(s);
                }
                break;
            }
            case ORIGIN_NULL_URL:
            case ORIGIN_NULL_RESOURCE:
            case ORIGIN_NULL_COMMENTS: {
                break;
            }
            default: {
                throw new IOException("Unhandled field from origin: " + code);
            }
        }
    }
    
    static void writeOrigin(final DataOutput out, final SimpleConfigOrigin origin, final SimpleConfigOrigin baseOrigin) throws IOException {
        Map<SerializedField, Object> m;
        if (origin != null) {
            m = origin.toFieldsDelta(baseOrigin);
        }
        else {
            m = Collections.emptyMap();
        }
        for (final Map.Entry<SerializedField, Object> e : m.entrySet()) {
            final FieldOut field = new FieldOut(e.getKey());
            final Object v = e.getValue();
            writeOriginField(field.data, field.code, v);
            writeField(out, field);
        }
        writeEndMarker(out);
    }
    
    static SimpleConfigOrigin readOrigin(final DataInput in, final SimpleConfigOrigin baseOrigin) throws IOException {
        final Map<SerializedField, Object> m = new EnumMap<SerializedField, Object>(SerializedField.class);
        while (true) {
            Object v = null;
            final SerializedField field = readCode(in);
            switch (field) {
                case END_MARKER: {
                    return SimpleConfigOrigin.fromBase(baseOrigin, m);
                }
                case ORIGIN_DESCRIPTION: {
                    in.readInt();
                    v = in.readUTF();
                    break;
                }
                case ORIGIN_LINE_NUMBER: {
                    in.readInt();
                    v = in.readInt();
                    break;
                }
                case ORIGIN_END_LINE_NUMBER: {
                    in.readInt();
                    v = in.readInt();
                    break;
                }
                case ORIGIN_TYPE: {
                    in.readInt();
                    v = in.readUnsignedByte();
                    break;
                }
                case ORIGIN_URL: {
                    in.readInt();
                    v = in.readUTF();
                    break;
                }
                case ORIGIN_RESOURCE: {
                    in.readInt();
                    v = in.readUTF();
                    break;
                }
                case ORIGIN_COMMENTS: {
                    in.readInt();
                    final int size = in.readInt();
                    final List<String> list = new ArrayList<String>(size);
                    for (int i = 0; i < size; ++i) {
                        list.add(in.readUTF());
                    }
                    v = list;
                    break;
                }
                case ORIGIN_NULL_URL:
                case ORIGIN_NULL_RESOURCE:
                case ORIGIN_NULL_COMMENTS: {
                    in.readInt();
                    v = "";
                    break;
                }
                case ROOT_VALUE:
                case ROOT_WAS_CONFIG:
                case VALUE_DATA:
                case VALUE_ORIGIN: {
                    throw new IOException("Not expecting this field here: " + field);
                }
                case UNKNOWN: {
                    skipField(in);
                    break;
                }
            }
            if (v != null) {
                m.put(field, v);
            }
        }
    }
    
    private static void writeValueData(final DataOutput out, final ConfigValue value) throws IOException {
        final SerializedValueType st = SerializedValueType.forValue(value);
        out.writeByte(st.ordinal());
        switch (st) {
            case BOOLEAN: {
                out.writeBoolean(((ConfigBoolean)value).unwrapped());
            }
            case INT: {
                out.writeInt(((ConfigInt)value).unwrapped());
                out.writeUTF(((ConfigNumber)value).transformToString());
                break;
            }
            case LONG: {
                out.writeLong(((ConfigLong)value).unwrapped());
                out.writeUTF(((ConfigNumber)value).transformToString());
                break;
            }
            case DOUBLE: {
                out.writeDouble(((ConfigDouble)value).unwrapped());
                out.writeUTF(((ConfigNumber)value).transformToString());
                break;
            }
            case STRING: {
                out.writeUTF(((ConfigString)value).unwrapped());
                break;
            }
            case LIST: {
                final ConfigList list = (ConfigList)value;
                out.writeInt(list.size());
                for (final ConfigValue v : list) {
                    writeValue(out, v, (SimpleConfigOrigin)list.origin());
                }
                break;
            }
            case OBJECT: {
                final ConfigObject obj = (ConfigObject)value;
                out.writeInt(obj.size());
                for (final Map.Entry<String, ConfigValue> e : obj.entrySet()) {
                    out.writeUTF(e.getKey());
                    writeValue(out, e.getValue(), (SimpleConfigOrigin)obj.origin());
                }
                break;
            }
        }
    }
    
    private static AbstractConfigValue readValueData(final DataInput in, final SimpleConfigOrigin origin) throws IOException {
        final int stb = in.readUnsignedByte();
        final SerializedValueType st = SerializedValueType.forInt(stb);
        if (st == null) {
            throw new IOException("Unknown serialized value type: " + stb);
        }
        switch (st) {
            case BOOLEAN: {
                return new ConfigBoolean(origin, in.readBoolean());
            }
            case NULL: {
                return new ConfigNull(origin);
            }
            case INT: {
                final int vi = in.readInt();
                final String si = in.readUTF();
                return new ConfigInt(origin, vi, si);
            }
            case LONG: {
                final long vl = in.readLong();
                final String sl = in.readUTF();
                return new ConfigLong(origin, vl, sl);
            }
            case DOUBLE: {
                final double vd = in.readDouble();
                final String sd = in.readUTF();
                return new ConfigDouble(origin, vd, sd);
            }
            case STRING: {
                return new ConfigString.Quoted(origin, in.readUTF());
            }
            case LIST: {
                final int listSize = in.readInt();
                final List<AbstractConfigValue> list = new ArrayList<AbstractConfigValue>(listSize);
                for (int i = 0; i < listSize; ++i) {
                    final AbstractConfigValue v = readValue(in, origin);
                    list.add(v);
                }
                return new SimpleConfigList(origin, list);
            }
            case OBJECT: {
                final int mapSize = in.readInt();
                final Map<String, AbstractConfigValue> map = new HashMap<String, AbstractConfigValue>(mapSize);
                for (int j = 0; j < mapSize; ++j) {
                    final String key = in.readUTF();
                    final AbstractConfigValue v2 = readValue(in, origin);
                    map.put(key, v2);
                }
                return new SimpleConfigObject(origin, map);
            }
            default: {
                throw new IOException("Unhandled serialized value type: " + st);
            }
        }
    }
    
    private static void writeValue(final DataOutput out, final ConfigValue value, final SimpleConfigOrigin baseOrigin) throws IOException {
        final FieldOut origin = new FieldOut(SerializedField.VALUE_ORIGIN);
        writeOrigin(origin.data, (SimpleConfigOrigin)value.origin(), baseOrigin);
        writeField(out, origin);
        final FieldOut data = new FieldOut(SerializedField.VALUE_DATA);
        writeValueData(data.data, value);
        writeField(out, data);
        writeEndMarker(out);
    }
    
    private static AbstractConfigValue readValue(final DataInput in, final SimpleConfigOrigin baseOrigin) throws IOException {
        AbstractConfigValue value = null;
        SimpleConfigOrigin origin = null;
        while (true) {
            final SerializedField code = readCode(in);
            if (code == SerializedField.END_MARKER) {
                if (value == null) {
                    throw new IOException("No value data found in serialization of value");
                }
                return value;
            }
            else if (code == SerializedField.VALUE_DATA) {
                if (origin == null) {
                    throw new IOException("Origin must be stored before value data");
                }
                in.readInt();
                value = readValueData(in, origin);
            }
            else if (code == SerializedField.VALUE_ORIGIN) {
                in.readInt();
                origin = readOrigin(in, baseOrigin);
            }
            else {
                skipField(in);
            }
        }
    }
    
    private static void writeField(final DataOutput out, final FieldOut field) throws IOException {
        final byte[] bytes = field.bytes.toByteArray();
        out.writeByte(field.code.ordinal());
        out.writeInt(bytes.length);
        out.write(bytes);
    }
    
    private static void writeEndMarker(final DataOutput out) throws IOException {
        out.writeByte(SerializedField.END_MARKER.ordinal());
    }
    
    private static SerializedField readCode(final DataInput in) throws IOException {
        final int c = in.readUnsignedByte();
        if (c == SerializedField.UNKNOWN.ordinal()) {
            throw new IOException("field code " + c + " is not supposed to be on the wire");
        }
        return SerializedField.forInt(c);
    }
    
    private static void skipField(final DataInput in) throws IOException {
        final int len = in.readInt();
        final int skipped = in.skipBytes(len);
        if (skipped < len) {
            final byte[] bytes = new byte[len - skipped];
            in.readFully(bytes);
        }
    }
    
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (((AbstractConfigValue)this.value).resolveStatus() != ResolveStatus.RESOLVED) {
            throw new NotSerializableException("tried to serialize a value with unresolved substitutions, need to Config#resolve() first, see API docs");
        }
        FieldOut field = new FieldOut(SerializedField.ROOT_VALUE);
        writeValue(field.data, this.value, null);
        writeField(out, field);
        field = new FieldOut(SerializedField.ROOT_WAS_CONFIG);
        field.data.writeBoolean(this.wasConfig);
        writeField(out, field);
        writeEndMarker(out);
    }
    
    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        while (true) {
            final SerializedField code = readCode(in);
            if (code == SerializedField.END_MARKER) {
                break;
            }
            if (code == SerializedField.ROOT_VALUE) {
                in.readInt();
                this.value = readValue(in, null);
            }
            else if (code == SerializedField.ROOT_WAS_CONFIG) {
                in.readInt();
                this.wasConfig = in.readBoolean();
            }
            else {
                skipField(in);
            }
        }
    }
    
    private static ConfigException shouldNotBeUsed() {
        return new ConfigException.BugOrBroken(SerializedConfigValue.class.getName() + " should not exist outside of serialization");
    }
    
    @Override
    public ConfigValueType valueType() {
        throw shouldNotBeUsed();
    }
    
    @Override
    public Object unwrapped() {
        throw shouldNotBeUsed();
    }
    
    @Override
    protected SerializedConfigValue newCopy(final ConfigOrigin origin) {
        throw shouldNotBeUsed();
    }
    
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + "(value=" + this.value + ",wasConfig=" + this.wasConfig + ")";
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof SerializedConfigValue && this.canEqual(other) && this.wasConfig == ((SerializedConfigValue)other).wasConfig && this.value.equals(((SerializedConfigValue)other).value);
    }
    
    @Override
    public int hashCode() {
        int h = 41 * (41 + this.value.hashCode());
        h = 41 * (h + (this.wasConfig ? 1 : 0));
        return h;
    }
    
    enum SerializedField
    {
        UNKNOWN, 
        END_MARKER, 
        ROOT_VALUE, 
        ROOT_WAS_CONFIG, 
        VALUE_DATA, 
        VALUE_ORIGIN, 
        ORIGIN_DESCRIPTION, 
        ORIGIN_LINE_NUMBER, 
        ORIGIN_END_LINE_NUMBER, 
        ORIGIN_TYPE, 
        ORIGIN_URL, 
        ORIGIN_COMMENTS, 
        ORIGIN_NULL_URL, 
        ORIGIN_NULL_COMMENTS, 
        ORIGIN_RESOURCE, 
        ORIGIN_NULL_RESOURCE;
        
        static SerializedField forInt(final int b) {
            if (b < values().length) {
                return values()[b];
            }
            return SerializedField.UNKNOWN;
        }
    }
    
    private enum SerializedValueType
    {
        NULL(ConfigValueType.NULL), 
        BOOLEAN(ConfigValueType.BOOLEAN), 
        INT(ConfigValueType.NUMBER), 
        LONG(ConfigValueType.NUMBER), 
        DOUBLE(ConfigValueType.NUMBER), 
        STRING(ConfigValueType.STRING), 
        LIST(ConfigValueType.LIST), 
        OBJECT(ConfigValueType.OBJECT);
        
        ConfigValueType configType;
        
        private SerializedValueType(final ConfigValueType configType) {
            this.configType = configType;
        }
        
        static SerializedValueType forInt(final int b) {
            if (b < values().length) {
                return values()[b];
            }
            return null;
        }
        
        static SerializedValueType forValue(final ConfigValue value) {
            final ConfigValueType t = value.valueType();
            if (t == ConfigValueType.NUMBER) {
                if (value instanceof ConfigInt) {
                    return SerializedValueType.INT;
                }
                if (value instanceof ConfigLong) {
                    return SerializedValueType.LONG;
                }
                if (value instanceof ConfigDouble) {
                    return SerializedValueType.DOUBLE;
                }
            }
            else {
                for (final SerializedValueType st : values()) {
                    if (st.configType == t) {
                        return st;
                    }
                }
            }
            throw new ConfigException.BugOrBroken("don't know how to serialize " + value);
        }
    }
    
    private static class FieldOut
    {
        final SerializedField code;
        final ByteArrayOutputStream bytes;
        final DataOutput data;
        
        FieldOut(final SerializedField code) {
            this.code = code;
            this.bytes = new ByteArrayOutputStream();
            this.data = new DataOutputStream(this.bytes);
        }
    }
}
