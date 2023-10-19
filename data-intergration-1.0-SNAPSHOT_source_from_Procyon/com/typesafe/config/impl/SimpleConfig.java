// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.HashMap;
import java.io.ObjectStreamException;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.typesafe.config.ConfigMergeable;
import com.typesafe.config.ConfigObject;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import com.typesafe.config.ConfigMemorySize;
import com.typesafe.config.ConfigList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigResolveOptions;
import com.typesafe.config.ConfigOrigin;
import java.io.Serializable;
import com.typesafe.config.Config;

final class SimpleConfig implements Config, MergeableValue, Serializable
{
    private static final long serialVersionUID = 1L;
    private final AbstractConfigObject object;
    
    SimpleConfig(final AbstractConfigObject object) {
        this.object = object;
    }
    
    @Override
    public AbstractConfigObject root() {
        return this.object;
    }
    
    @Override
    public ConfigOrigin origin() {
        return this.object.origin();
    }
    
    @Override
    public SimpleConfig resolve() {
        return this.resolve(ConfigResolveOptions.defaults());
    }
    
    @Override
    public SimpleConfig resolve(final ConfigResolveOptions options) {
        return this.resolveWith((Config)this, options);
    }
    
    @Override
    public SimpleConfig resolveWith(final Config source) {
        return this.resolveWith(source, ConfigResolveOptions.defaults());
    }
    
    @Override
    public SimpleConfig resolveWith(final Config source, final ConfigResolveOptions options) {
        final AbstractConfigValue resolved = ResolveContext.resolve(this.object, ((SimpleConfig)source).object, options);
        if (resolved == this.object) {
            return this;
        }
        return new SimpleConfig((AbstractConfigObject)resolved);
    }
    
    private ConfigValue hasPathPeek(final String pathExpression) {
        final Path path = Path.newPath(pathExpression);
        ConfigValue peeked;
        try {
            peeked = this.object.peekPath(path);
        }
        catch (ConfigException.NotResolved e) {
            throw ConfigImpl.improveNotResolved(path, e);
        }
        return peeked;
    }
    
    @Override
    public boolean hasPath(final String pathExpression) {
        final ConfigValue peeked = this.hasPathPeek(pathExpression);
        return peeked != null && peeked.valueType() != ConfigValueType.NULL;
    }
    
    @Override
    public boolean hasPathOrNull(final String path) {
        final ConfigValue peeked = this.hasPathPeek(path);
        return peeked != null;
    }
    
    @Override
    public boolean isEmpty() {
        return this.object.isEmpty();
    }
    
    private static void findPaths(final Set<Map.Entry<String, ConfigValue>> entries, final Path parent, final AbstractConfigObject obj) {
        for (final Map.Entry<String, ConfigValue> entry : obj.entrySet()) {
            final String elem = entry.getKey();
            final ConfigValue v = entry.getValue();
            Path path = Path.newKey(elem);
            if (parent != null) {
                path = path.prepend(parent);
            }
            if (v instanceof AbstractConfigObject) {
                findPaths(entries, path, (AbstractConfigObject)v);
            }
            else {
                if (v instanceof ConfigNull) {
                    continue;
                }
                entries.add(new AbstractMap.SimpleImmutableEntry<String, ConfigValue>(path.render(), v));
            }
        }
    }
    
    @Override
    public Set<Map.Entry<String, ConfigValue>> entrySet() {
        final Set<Map.Entry<String, ConfigValue>> entries = new HashSet<Map.Entry<String, ConfigValue>>();
        findPaths(entries, null, this.object);
        return entries;
    }
    
    private static AbstractConfigValue throwIfNull(final AbstractConfigValue v, final ConfigValueType expected, final Path originalPath) {
        if (v.valueType() == ConfigValueType.NULL) {
            throw new ConfigException.Null(v.origin(), originalPath.render(), (expected != null) ? expected.name() : null);
        }
        return v;
    }
    
    private static AbstractConfigValue findKey(final AbstractConfigObject self, final String key, final ConfigValueType expected, final Path originalPath) {
        return throwIfNull(findKeyOrNull(self, key, expected, originalPath), expected, originalPath);
    }
    
    private static AbstractConfigValue findKeyOrNull(final AbstractConfigObject self, final String key, final ConfigValueType expected, final Path originalPath) {
        AbstractConfigValue v = self.peekAssumingResolved(key, originalPath);
        if (v == null) {
            throw new ConfigException.Missing(originalPath.render());
        }
        if (expected != null) {
            v = DefaultTransformer.transform(v, expected);
        }
        if (expected != null && v.valueType() != expected && v.valueType() != ConfigValueType.NULL) {
            throw new ConfigException.WrongType(v.origin(), originalPath.render(), expected.name(), v.valueType().name());
        }
        return v;
    }
    
    private static AbstractConfigValue findOrNull(final AbstractConfigObject self, final Path path, final ConfigValueType expected, final Path originalPath) {
        try {
            final String key = path.first();
            final Path next = path.remainder();
            if (next == null) {
                return findKeyOrNull(self, key, expected, originalPath);
            }
            final AbstractConfigObject o = (AbstractConfigObject)findKey(self, key, ConfigValueType.OBJECT, originalPath.subPath(0, originalPath.length() - next.length()));
            assert o != null;
            return findOrNull(o, next, expected, originalPath);
        }
        catch (ConfigException.NotResolved e) {
            throw ConfigImpl.improveNotResolved(path, e);
        }
    }
    
    AbstractConfigValue find(final Path pathExpression, final ConfigValueType expected, final Path originalPath) {
        return throwIfNull(findOrNull(this.object, pathExpression, expected, originalPath), expected, originalPath);
    }
    
    AbstractConfigValue find(final String pathExpression, final ConfigValueType expected) {
        final Path path = Path.newPath(pathExpression);
        return this.find(path, expected, path);
    }
    
    private AbstractConfigValue findOrNull(final Path pathExpression, final ConfigValueType expected, final Path originalPath) {
        return findOrNull(this.object, pathExpression, expected, originalPath);
    }
    
    private AbstractConfigValue findOrNull(final String pathExpression, final ConfigValueType expected) {
        final Path path = Path.newPath(pathExpression);
        return this.findOrNull(path, expected, path);
    }
    
    @Override
    public AbstractConfigValue getValue(final String path) {
        return this.find(path, null);
    }
    
    @Override
    public boolean getIsNull(final String path) {
        final AbstractConfigValue v = this.findOrNull(path, null);
        return v.valueType() == ConfigValueType.NULL;
    }
    
    @Override
    public boolean getBoolean(final String path) {
        final ConfigValue v = this.find(path, ConfigValueType.BOOLEAN);
        return (boolean)v.unwrapped();
    }
    
    private ConfigNumber getConfigNumber(final String path) {
        final ConfigValue v = this.find(path, ConfigValueType.NUMBER);
        return (ConfigNumber)v;
    }
    
    @Override
    public Number getNumber(final String path) {
        return this.getConfigNumber(path).unwrapped();
    }
    
    @Override
    public int getInt(final String path) {
        final ConfigNumber n = this.getConfigNumber(path);
        return n.intValueRangeChecked(path);
    }
    
    @Override
    public long getLong(final String path) {
        return this.getNumber(path).longValue();
    }
    
    @Override
    public double getDouble(final String path) {
        return this.getNumber(path).doubleValue();
    }
    
    @Override
    public String getString(final String path) {
        final ConfigValue v = this.find(path, ConfigValueType.STRING);
        return (String)v.unwrapped();
    }
    
    @Override
    public <T extends Enum<T>> T getEnum(final Class<T> enumClass, final String path) {
        final ConfigValue v = this.find(path, ConfigValueType.STRING);
        return this.getEnumValue(path, enumClass, v);
    }
    
    @Override
    public ConfigList getList(final String path) {
        final AbstractConfigValue v = this.find(path, ConfigValueType.LIST);
        return (ConfigList)v;
    }
    
    @Override
    public AbstractConfigObject getObject(final String path) {
        final AbstractConfigObject obj = (AbstractConfigObject)this.find(path, ConfigValueType.OBJECT);
        return obj;
    }
    
    @Override
    public SimpleConfig getConfig(final String path) {
        return this.getObject(path).toConfig();
    }
    
    @Override
    public Object getAnyRef(final String path) {
        final ConfigValue v = this.find(path, null);
        return v.unwrapped();
    }
    
    @Override
    public Long getBytes(final String path) {
        Long size = null;
        try {
            size = this.getLong(path);
        }
        catch (ConfigException.WrongType e) {
            final ConfigValue v = this.find(path, ConfigValueType.STRING);
            size = parseBytes((String)v.unwrapped(), v.origin(), path);
        }
        return size;
    }
    
    @Override
    public ConfigMemorySize getMemorySize(final String path) {
        return ConfigMemorySize.ofBytes(this.getBytes(path));
    }
    
    @Deprecated
    @Override
    public Long getMilliseconds(final String path) {
        return this.getDuration(path, TimeUnit.MILLISECONDS);
    }
    
    @Deprecated
    @Override
    public Long getNanoseconds(final String path) {
        return this.getDuration(path, TimeUnit.NANOSECONDS);
    }
    
    @Override
    public long getDuration(final String path, final TimeUnit unit) {
        final ConfigValue v = this.find(path, ConfigValueType.STRING);
        final long result = unit.convert(parseDuration((String)v.unwrapped(), v.origin(), path), TimeUnit.NANOSECONDS);
        return result;
    }
    
    @Override
    public Duration getDuration(final String path) {
        final ConfigValue v = this.find(path, ConfigValueType.STRING);
        final long nanos = parseDuration((String)v.unwrapped(), v.origin(), path);
        return Duration.ofNanos(nanos);
    }
    
    private <T> List<T> getHomogeneousUnwrappedList(final String path, final ConfigValueType expected) {
        final List<T> l = new ArrayList<T>();
        final List<? extends ConfigValue> list = this.getList(path);
        for (final ConfigValue cv : list) {
            AbstractConfigValue v = (AbstractConfigValue)cv;
            if (expected != null) {
                v = DefaultTransformer.transform(v, expected);
            }
            if (v.valueType() != expected) {
                throw new ConfigException.WrongType(v.origin(), path, "list of " + expected.name(), "list of " + v.valueType().name());
            }
            l.add((T)v.unwrapped());
        }
        return l;
    }
    
    @Override
    public List<Boolean> getBooleanList(final String path) {
        return this.getHomogeneousUnwrappedList(path, ConfigValueType.BOOLEAN);
    }
    
    @Override
    public List<Number> getNumberList(final String path) {
        return this.getHomogeneousUnwrappedList(path, ConfigValueType.NUMBER);
    }
    
    @Override
    public List<Integer> getIntList(final String path) {
        final List<Integer> l = new ArrayList<Integer>();
        final List<AbstractConfigValue> numbers = this.getHomogeneousWrappedList(path, ConfigValueType.NUMBER);
        for (final AbstractConfigValue v : numbers) {
            l.add(((ConfigNumber)v).intValueRangeChecked(path));
        }
        return l;
    }
    
    @Override
    public List<Long> getLongList(final String path) {
        final List<Long> l = new ArrayList<Long>();
        final List<Number> numbers = this.getNumberList(path);
        for (final Number n : numbers) {
            l.add(n.longValue());
        }
        return l;
    }
    
    @Override
    public List<Double> getDoubleList(final String path) {
        final List<Double> l = new ArrayList<Double>();
        final List<Number> numbers = this.getNumberList(path);
        for (final Number n : numbers) {
            l.add(n.doubleValue());
        }
        return l;
    }
    
    @Override
    public List<String> getStringList(final String path) {
        return this.getHomogeneousUnwrappedList(path, ConfigValueType.STRING);
    }
    
    @Override
    public <T extends Enum<T>> List<T> getEnumList(final Class<T> enumClass, final String path) {
        final List<ConfigString> enumNames = this.getHomogeneousWrappedList(path, ConfigValueType.STRING);
        final List<T> enumList = new ArrayList<T>();
        for (final ConfigString enumName : enumNames) {
            enumList.add(this.getEnumValue(path, enumClass, enumName));
        }
        return enumList;
    }
    
    private <T extends Enum<T>> T getEnumValue(final String path, final Class<T> enumClass, final ConfigValue enumConfigValue) {
        final String enumName = (String)enumConfigValue.unwrapped();
        try {
            return Enum.valueOf(enumClass, enumName);
        }
        catch (IllegalArgumentException e) {
            final List<String> enumNames = new ArrayList<String>();
            final Enum[] enumConstants = enumClass.getEnumConstants();
            if (enumConstants != null) {
                for (final Enum enumConstant : enumConstants) {
                    enumNames.add(enumConstant.name());
                }
            }
            throw new ConfigException.BadValue(enumConfigValue.origin(), path, String.format("The enum class %s has no constant of the name '%s' (should be one of %s.)", enumClass.getSimpleName(), enumName, enumNames));
        }
    }
    
    private <T extends ConfigValue> List<T> getHomogeneousWrappedList(final String path, final ConfigValueType expected) {
        final List<T> l = new ArrayList<T>();
        final List<? extends ConfigValue> list = this.getList(path);
        for (final ConfigValue cv : list) {
            AbstractConfigValue v = (AbstractConfigValue)cv;
            if (expected != null) {
                v = DefaultTransformer.transform(v, expected);
            }
            if (v.valueType() != expected) {
                throw new ConfigException.WrongType(v.origin(), path, "list of " + expected.name(), "list of " + v.valueType().name());
            }
            l.add((T)v);
        }
        return l;
    }
    
    @Override
    public List<ConfigObject> getObjectList(final String path) {
        return this.getHomogeneousWrappedList(path, ConfigValueType.OBJECT);
    }
    
    @Override
    public List<? extends Config> getConfigList(final String path) {
        final List<ConfigObject> objects = this.getObjectList(path);
        final List<Config> l = new ArrayList<Config>();
        for (final ConfigObject o : objects) {
            l.add(o.toConfig());
        }
        return l;
    }
    
    @Override
    public List<?> getAnyRefList(final String path) {
        final List<Object> l = new ArrayList<Object>();
        final List<? extends ConfigValue> list = this.getList(path);
        for (final ConfigValue v : list) {
            l.add(v.unwrapped());
        }
        return l;
    }
    
    @Override
    public List<Long> getBytesList(final String path) {
        final List<Long> l = new ArrayList<Long>();
        final List<? extends ConfigValue> list = this.getList(path);
        for (final ConfigValue v : list) {
            if (v.valueType() == ConfigValueType.NUMBER) {
                l.add(((Number)v.unwrapped()).longValue());
            }
            else {
                if (v.valueType() != ConfigValueType.STRING) {
                    throw new ConfigException.WrongType(v.origin(), path, "memory size string or number of bytes", v.valueType().name());
                }
                final String s = (String)v.unwrapped();
                final Long n = parseBytes(s, v.origin(), path);
                l.add(n);
            }
        }
        return l;
    }
    
    @Override
    public List<ConfigMemorySize> getMemorySizeList(final String path) {
        final List<Long> list = this.getBytesList(path);
        final List<ConfigMemorySize> builder = new ArrayList<ConfigMemorySize>();
        for (final Long v : list) {
            builder.add(ConfigMemorySize.ofBytes(v));
        }
        return builder;
    }
    
    @Override
    public List<Long> getDurationList(final String path, final TimeUnit unit) {
        final List<Long> l = new ArrayList<Long>();
        final List<? extends ConfigValue> list = this.getList(path);
        for (final ConfigValue v : list) {
            if (v.valueType() == ConfigValueType.NUMBER) {
                final Long n = unit.convert(((Number)v.unwrapped()).longValue(), TimeUnit.MILLISECONDS);
                l.add(n);
            }
            else {
                if (v.valueType() != ConfigValueType.STRING) {
                    throw new ConfigException.WrongType(v.origin(), path, "duration string or number of milliseconds", v.valueType().name());
                }
                final String s = (String)v.unwrapped();
                final Long n2 = unit.convert(parseDuration(s, v.origin(), path), TimeUnit.NANOSECONDS);
                l.add(n2);
            }
        }
        return l;
    }
    
    @Override
    public List<Duration> getDurationList(final String path) {
        final List<Long> l = this.getDurationList(path, TimeUnit.NANOSECONDS);
        final List<Duration> builder = new ArrayList<Duration>(l.size());
        for (final Long value : l) {
            builder.add(Duration.ofNanos(value));
        }
        return builder;
    }
    
    @Deprecated
    @Override
    public List<Long> getMillisecondsList(final String path) {
        return this.getDurationList(path, TimeUnit.MILLISECONDS);
    }
    
    @Deprecated
    @Override
    public List<Long> getNanosecondsList(final String path) {
        return this.getDurationList(path, TimeUnit.NANOSECONDS);
    }
    
    @Override
    public AbstractConfigObject toFallbackValue() {
        return this.object;
    }
    
    @Override
    public SimpleConfig withFallback(final ConfigMergeable other) {
        return this.object.withFallback(other).toConfig();
    }
    
    @Override
    public final boolean equals(final Object other) {
        return other instanceof SimpleConfig && this.object.equals(((SimpleConfig)other).object);
    }
    
    @Override
    public final int hashCode() {
        return 41 * this.object.hashCode();
    }
    
    @Override
    public String toString() {
        return "Config(" + this.object.toString() + ")";
    }
    
    private static String getUnits(final String s) {
        int i;
        for (i = s.length() - 1; i >= 0; --i) {
            final char c = s.charAt(i);
            if (!Character.isLetter(c)) {
                break;
            }
        }
        return s.substring(i + 1);
    }
    
    public static long parseDuration(final String input, final ConfigOrigin originForException, final String pathForException) {
        final String s = ConfigImplUtil.unicodeTrim(input);
        String unitString;
        final String originalUnitString = unitString = getUnits(s);
        final String numberString = ConfigImplUtil.unicodeTrim(s.substring(0, s.length() - unitString.length()));
        TimeUnit units = null;
        if (numberString.length() == 0) {
            throw new ConfigException.BadValue(originForException, pathForException, "No number in duration value '" + input + "'");
        }
        if (unitString.length() > 2 && !unitString.endsWith("s")) {
            unitString += "s";
        }
        if (unitString.equals("") || unitString.equals("ms") || unitString.equals("millis") || unitString.equals("milliseconds")) {
            units = TimeUnit.MILLISECONDS;
        }
        else if (unitString.equals("us") || unitString.equals("micros") || unitString.equals("microseconds")) {
            units = TimeUnit.MICROSECONDS;
        }
        else if (unitString.equals("ns") || unitString.equals("nanos") || unitString.equals("nanoseconds")) {
            units = TimeUnit.NANOSECONDS;
        }
        else if (unitString.equals("d") || unitString.equals("days")) {
            units = TimeUnit.DAYS;
        }
        else if (unitString.equals("h") || unitString.equals("hours")) {
            units = TimeUnit.HOURS;
        }
        else if (unitString.equals("s") || unitString.equals("seconds")) {
            units = TimeUnit.SECONDS;
        }
        else {
            if (!unitString.equals("m") && !unitString.equals("minutes")) {
                throw new ConfigException.BadValue(originForException, pathForException, "Could not parse time unit '" + originalUnitString + "' (try ns, us, ms, s, m, h, d)");
            }
            units = TimeUnit.MINUTES;
        }
        try {
            if (numberString.matches("[+-]?[0-9]+")) {
                return units.toNanos(Long.parseLong(numberString));
            }
            final long nanosInUnit = units.toNanos(1L);
            return (long)(Double.parseDouble(numberString) * nanosInUnit);
        }
        catch (NumberFormatException e) {
            throw new ConfigException.BadValue(originForException, pathForException, "Could not parse duration number '" + numberString + "'");
        }
    }
    
    public static long parseBytes(final String input, final ConfigOrigin originForException, final String pathForException) {
        final String s = ConfigImplUtil.unicodeTrim(input);
        final String unitString = getUnits(s);
        final String numberString = ConfigImplUtil.unicodeTrim(s.substring(0, s.length() - unitString.length()));
        if (numberString.length() == 0) {
            throw new ConfigException.BadValue(originForException, pathForException, "No number in size-in-bytes value '" + input + "'");
        }
        final MemoryUnit units = MemoryUnit.parseUnit(unitString);
        if (units == null) {
            throw new ConfigException.BadValue(originForException, pathForException, "Could not parse size-in-bytes unit '" + unitString + "' (try k, K, kB, KiB, kilobytes, kibibytes)");
        }
        try {
            BigInteger result;
            if (numberString.matches("[0-9]+")) {
                result = units.bytes.multiply(new BigInteger(numberString));
            }
            else {
                final BigDecimal resultDecimal = new BigDecimal(units.bytes).multiply(new BigDecimal(numberString));
                result = resultDecimal.toBigInteger();
            }
            if (result.bitLength() < 64) {
                return result.longValue();
            }
            throw new ConfigException.BadValue(originForException, pathForException, "size-in-bytes value is out of range for a 64-bit long: '" + input + "'");
        }
        catch (NumberFormatException e) {
            throw new ConfigException.BadValue(originForException, pathForException, "Could not parse size-in-bytes number '" + numberString + "'");
        }
    }
    
    private AbstractConfigValue peekPath(final Path path) {
        return this.root().peekPath(path);
    }
    
    private static void addProblem(final List<ConfigException.ValidationProblem> accumulator, final Path path, final ConfigOrigin origin, final String problem) {
        accumulator.add(new ConfigException.ValidationProblem(path.render(), origin, problem));
    }
    
    private static String getDesc(final ConfigValueType type) {
        return type.name().toLowerCase();
    }
    
    private static String getDesc(final ConfigValue refValue) {
        if (!(refValue instanceof AbstractConfigObject)) {
            return getDesc(refValue.valueType());
        }
        final AbstractConfigObject obj = (AbstractConfigObject)refValue;
        if (!obj.isEmpty()) {
            return "object with keys " + obj.keySet();
        }
        return getDesc(refValue.valueType());
    }
    
    private static void addMissing(final List<ConfigException.ValidationProblem> accumulator, final String refDesc, final Path path, final ConfigOrigin origin) {
        addProblem(accumulator, path, origin, "No setting at '" + path.render() + "', expecting: " + refDesc);
    }
    
    private static void addMissing(final List<ConfigException.ValidationProblem> accumulator, final ConfigValue refValue, final Path path, final ConfigOrigin origin) {
        addMissing(accumulator, getDesc(refValue), path, origin);
    }
    
    static void addMissing(final List<ConfigException.ValidationProblem> accumulator, final ConfigValueType refType, final Path path, final ConfigOrigin origin) {
        addMissing(accumulator, getDesc(refType), path, origin);
    }
    
    private static void addWrongType(final List<ConfigException.ValidationProblem> accumulator, final String refDesc, final AbstractConfigValue actual, final Path path) {
        addProblem(accumulator, path, actual.origin(), "Wrong value type at '" + path.render() + "', expecting: " + refDesc + " but got: " + getDesc(actual));
    }
    
    private static void addWrongType(final List<ConfigException.ValidationProblem> accumulator, final ConfigValue refValue, final AbstractConfigValue actual, final Path path) {
        addWrongType(accumulator, getDesc(refValue), actual, path);
    }
    
    private static void addWrongType(final List<ConfigException.ValidationProblem> accumulator, final ConfigValueType refType, final AbstractConfigValue actual, final Path path) {
        addWrongType(accumulator, getDesc(refType), actual, path);
    }
    
    private static boolean couldBeNull(final AbstractConfigValue v) {
        return DefaultTransformer.transform(v, ConfigValueType.NULL).valueType() == ConfigValueType.NULL;
    }
    
    private static boolean haveCompatibleTypes(final ConfigValue reference, final AbstractConfigValue value) {
        return couldBeNull((AbstractConfigValue)reference) || haveCompatibleTypes(reference.valueType(), value);
    }
    
    private static boolean haveCompatibleTypes(final ConfigValueType referenceType, final AbstractConfigValue value) {
        if (referenceType == ConfigValueType.NULL || couldBeNull(value)) {
            return true;
        }
        if (referenceType == ConfigValueType.OBJECT) {
            return value instanceof AbstractConfigObject;
        }
        if (referenceType == ConfigValueType.LIST) {
            return value instanceof SimpleConfigList || value instanceof SimpleConfigObject;
        }
        return referenceType == ConfigValueType.STRING || value instanceof ConfigString || referenceType == value.valueType();
    }
    
    private static void checkValidObject(final Path path, final AbstractConfigObject reference, final AbstractConfigObject value, final List<ConfigException.ValidationProblem> accumulator) {
        for (final Map.Entry<String, ConfigValue> entry : reference.entrySet()) {
            final String key = entry.getKey();
            Path childPath;
            if (path != null) {
                childPath = Path.newKey(key).prepend(path);
            }
            else {
                childPath = Path.newKey(key);
            }
            final AbstractConfigValue v = value.get(key);
            if (v == null) {
                addMissing(accumulator, entry.getValue(), childPath, value.origin());
            }
            else {
                checkValid(childPath, entry.getValue(), v, accumulator);
            }
        }
    }
    
    private static void checkListCompatibility(final Path path, final SimpleConfigList listRef, final SimpleConfigList listValue, final List<ConfigException.ValidationProblem> accumulator) {
        if (!listRef.isEmpty()) {
            if (!listValue.isEmpty()) {
                final AbstractConfigValue refElement = listRef.get(0);
                for (final ConfigValue elem : listValue) {
                    final AbstractConfigValue e = (AbstractConfigValue)elem;
                    if (!haveCompatibleTypes(refElement, e)) {
                        addProblem(accumulator, path, e.origin(), "List at '" + path.render() + "' contains wrong value type, expecting list of " + getDesc(refElement) + " but got element of type " + getDesc(e));
                        break;
                    }
                }
            }
        }
    }
    
    static void checkValid(final Path path, final ConfigValueType referenceType, final AbstractConfigValue value, final List<ConfigException.ValidationProblem> accumulator) {
        if (haveCompatibleTypes(referenceType, value)) {
            if (referenceType == ConfigValueType.LIST && value instanceof SimpleConfigObject) {
                final AbstractConfigValue listValue = DefaultTransformer.transform(value, ConfigValueType.LIST);
                if (!(listValue instanceof SimpleConfigList)) {
                    addWrongType(accumulator, referenceType, value, path);
                }
            }
        }
        else {
            addWrongType(accumulator, referenceType, value, path);
        }
    }
    
    private static void checkValid(final Path path, final ConfigValue reference, final AbstractConfigValue value, final List<ConfigException.ValidationProblem> accumulator) {
        if (haveCompatibleTypes(reference, value)) {
            if (reference instanceof AbstractConfigObject && value instanceof AbstractConfigObject) {
                checkValidObject(path, (AbstractConfigObject)reference, (AbstractConfigObject)value, accumulator);
            }
            else if (reference instanceof SimpleConfigList && value instanceof SimpleConfigList) {
                final SimpleConfigList listRef = (SimpleConfigList)reference;
                final SimpleConfigList listValue = (SimpleConfigList)value;
                checkListCompatibility(path, listRef, listValue, accumulator);
            }
            else if (reference instanceof SimpleConfigList && value instanceof SimpleConfigObject) {
                final SimpleConfigList listRef = (SimpleConfigList)reference;
                final AbstractConfigValue listValue2 = DefaultTransformer.transform(value, ConfigValueType.LIST);
                if (listValue2 instanceof SimpleConfigList) {
                    checkListCompatibility(path, listRef, (SimpleConfigList)listValue2, accumulator);
                }
                else {
                    addWrongType(accumulator, reference, value, path);
                }
            }
        }
        else {
            addWrongType(accumulator, reference, value, path);
        }
    }
    
    @Override
    public boolean isResolved() {
        return this.root().resolveStatus() == ResolveStatus.RESOLVED;
    }
    
    @Override
    public void checkValid(final Config reference, final String... restrictToPaths) {
        final SimpleConfig ref = (SimpleConfig)reference;
        if (ref.root().resolveStatus() != ResolveStatus.RESOLVED) {
            throw new ConfigException.BugOrBroken("do not call checkValid() with an unresolved reference config, call Config#resolve(), see Config#resolve() API docs");
        }
        if (this.root().resolveStatus() != ResolveStatus.RESOLVED) {
            throw new ConfigException.NotResolved("need to Config#resolve() each config before using it, see the API docs for Config#resolve()");
        }
        final List<ConfigException.ValidationProblem> problems = new ArrayList<ConfigException.ValidationProblem>();
        if (restrictToPaths.length == 0) {
            checkValidObject(null, ref.root(), this.root(), problems);
        }
        else {
            for (final String p : restrictToPaths) {
                final Path path = Path.newPath(p);
                final AbstractConfigValue refValue = ref.peekPath(path);
                if (refValue != null) {
                    final AbstractConfigValue child = this.peekPath(path);
                    if (child != null) {
                        checkValid(path, refValue, child, problems);
                    }
                    else {
                        addMissing(problems, refValue, path, this.origin());
                    }
                }
            }
        }
        if (!problems.isEmpty()) {
            throw new ConfigException.ValidationFailed(problems);
        }
    }
    
    @Override
    public SimpleConfig withOnlyPath(final String pathExpression) {
        final Path path = Path.newPath(pathExpression);
        return new SimpleConfig(this.root().withOnlyPath(path));
    }
    
    @Override
    public SimpleConfig withoutPath(final String pathExpression) {
        final Path path = Path.newPath(pathExpression);
        return new SimpleConfig(this.root().withoutPath(path));
    }
    
    @Override
    public SimpleConfig withValue(final String pathExpression, final ConfigValue v) {
        final Path path = Path.newPath(pathExpression);
        return new SimpleConfig(this.root().withValue(path, v));
    }
    
    SimpleConfig atKey(final ConfigOrigin origin, final String key) {
        return this.root().atKey(origin, key);
    }
    
    @Override
    public SimpleConfig atKey(final String key) {
        return this.root().atKey(key);
    }
    
    @Override
    public Config atPath(final String path) {
        return this.root().atPath(path);
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new SerializedConfigValue(this);
    }
    
    private enum MemoryUnit
    {
        BYTES("", 1024, 0), 
        KILOBYTES("kilo", 1000, 1), 
        MEGABYTES("mega", 1000, 2), 
        GIGABYTES("giga", 1000, 3), 
        TERABYTES("tera", 1000, 4), 
        PETABYTES("peta", 1000, 5), 
        EXABYTES("exa", 1000, 6), 
        ZETTABYTES("zetta", 1000, 7), 
        YOTTABYTES("yotta", 1000, 8), 
        KIBIBYTES("kibi", 1024, 1), 
        MEBIBYTES("mebi", 1024, 2), 
        GIBIBYTES("gibi", 1024, 3), 
        TEBIBYTES("tebi", 1024, 4), 
        PEBIBYTES("pebi", 1024, 5), 
        EXBIBYTES("exbi", 1024, 6), 
        ZEBIBYTES("zebi", 1024, 7), 
        YOBIBYTES("yobi", 1024, 8);
        
        final String prefix;
        final int powerOf;
        final int power;
        final BigInteger bytes;
        private static Map<String, MemoryUnit> unitsMap;
        
        private MemoryUnit(final String prefix, final int powerOf, final int power) {
            this.prefix = prefix;
            this.powerOf = powerOf;
            this.power = power;
            this.bytes = BigInteger.valueOf(powerOf).pow(power);
        }
        
        private static Map<String, MemoryUnit> makeUnitsMap() {
            final Map<String, MemoryUnit> map = new HashMap<String, MemoryUnit>();
            for (final MemoryUnit unit : values()) {
                map.put(unit.prefix + "byte", unit);
                map.put(unit.prefix + "bytes", unit);
                if (unit.prefix.length() == 0) {
                    map.put("b", unit);
                    map.put("B", unit);
                    map.put("", unit);
                }
                else {
                    final String first = unit.prefix.substring(0, 1);
                    final String firstUpper = first.toUpperCase();
                    if (unit.powerOf == 1024) {
                        map.put(first, unit);
                        map.put(firstUpper, unit);
                        map.put(firstUpper + "i", unit);
                        map.put(firstUpper + "iB", unit);
                    }
                    else {
                        if (unit.powerOf != 1000) {
                            throw new RuntimeException("broken MemoryUnit enum");
                        }
                        if (unit.power == 1) {
                            map.put(first + "B", unit);
                        }
                        else {
                            map.put(firstUpper + "B", unit);
                        }
                    }
                }
            }
            return map;
        }
        
        static MemoryUnit parseUnit(final String unit) {
            return MemoryUnit.unitsMap.get(unit);
        }
        
        static {
            MemoryUnit.unitsMap = makeUnitsMap();
        }
    }
}
