// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.record;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import java.math.BigDecimal;
import java.math.BigInteger;
import shadeio.univocity.parsers.conversions.Conversion;
import shadeio.univocity.parsers.common.Context;

class RecordImpl<C extends Context> implements Record
{
    private final String[] data;
    private final RecordMetaDataImpl<C> metaData;
    
    RecordImpl(final String[] data, final RecordMetaDataImpl metaData) {
        this.data = data;
        this.metaData = (RecordMetaDataImpl<C>)metaData;
    }
    
    @Override
    public RecordMetaData getMetaData() {
        return this.metaData;
    }
    
    @Override
    public String[] getValues() {
        return this.data;
    }
    
    @Override
    public <T> T getValue(final String headerName, final Class<T> expectedType) {
        return this.metaData.getObjectValue(this.data, headerName, expectedType, (T)null);
    }
    
    @Override
    public <T> T getValue(final Enum<?> column, final Class<T> expectedType) {
        return this.metaData.getObjectValue(this.data, column, expectedType, (T)null);
    }
    
    @Override
    public <T> T getValue(final int columnIndex, final Class<T> expectedType) {
        return this.metaData.getObjectValue(this.data, columnIndex, expectedType, (T)null);
    }
    
    @Override
    public <T> T getValue(final String headerName, final Class<T> expectedType, final Conversion... conversions) {
        return this.metaData.getValue(this.data, headerName, expectedType, conversions);
    }
    
    @Override
    public <T> T getValue(final Enum<?> column, final Class<T> expectedType, final Conversion... conversions) {
        return this.metaData.getValue(this.data, column, expectedType, conversions);
    }
    
    @Override
    public <T> T getValue(final int columnIndex, final Class<T> expectedType, final Conversion... conversions) {
        return this.metaData.getValue(this.data, columnIndex, expectedType, conversions);
    }
    
    @Override
    public <T> T getValue(final String headerName, final T defaultValue) {
        return this.metaData.getObjectValue(this.data, headerName, defaultValue.getClass(), defaultValue);
    }
    
    @Override
    public <T> T getValue(final Enum<?> column, final T defaultValue) {
        return this.metaData.getObjectValue(this.data, column, defaultValue.getClass(), defaultValue);
    }
    
    @Override
    public <T> T getValue(final int columnIndex, final T defaultValue) {
        return this.metaData.getObjectValue(this.data, columnIndex, defaultValue.getClass(), defaultValue);
    }
    
    @Override
    public <T> T getValue(final String headerName, final T defaultValue, final Conversion... conversions) {
        return this.metaData.getValue(this.data, headerName, defaultValue, conversions);
    }
    
    @Override
    public <T> T getValue(final Enum<?> column, final T defaultValue, final Conversion... conversions) {
        return this.metaData.getValue(this.data, column, defaultValue, conversions);
    }
    
    @Override
    public <T> T getValue(final int columnIndex, final T defaultValue, final Conversion... conversions) {
        return this.metaData.getValue(this.data, columnIndex, defaultValue, conversions);
    }
    
    @Override
    public String getString(final String headerName) {
        return this.metaData.getValue(this.data, headerName);
    }
    
    @Override
    public String getString(final Enum<?> column) {
        return this.metaData.getValue(this.data, column);
    }
    
    @Override
    public String getString(final int columnIndex) {
        return this.metaData.getValue(this.data, columnIndex);
    }
    
    @Override
    public String getString(final String headerName, final int maxLength) {
        return this.truncate(this.metaData.getValue(this.data, headerName), maxLength);
    }
    
    @Override
    public String getString(final Enum<?> column, final int maxLength) {
        return this.truncate(this.metaData.getValue(this.data, column), maxLength);
    }
    
    @Override
    public String getString(final int columnIndex, final int maxLength) {
        return this.truncate(this.metaData.getValue(this.data, columnIndex), maxLength);
    }
    
    private String truncate(final String string, final int maxLength) {
        if (string == null) {
            return null;
        }
        if (maxLength < 0) {
            throw new IllegalArgumentException("Maximum length can't be negative");
        }
        if (string.length() > maxLength) {
            return string.substring(0, maxLength);
        }
        return string;
    }
    
    @Override
    public Byte getByte(final String headerName, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, headerName, Byte.class, null, format, formatOptions);
    }
    
    @Override
    public Byte getByte(final Enum<?> column, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, column, Byte.class, null, format, formatOptions);
    }
    
    @Override
    public Byte getByte(final int columnIndex, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, columnIndex, Byte.class, null, format, formatOptions);
    }
    
    @Override
    public Short getShort(final String headerName, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, headerName, Short.class, null, format, formatOptions);
    }
    
    @Override
    public Short getShort(final Enum<?> column, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, column, Short.class, null, format, formatOptions);
    }
    
    @Override
    public Short getShort(final int columnIndex, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, columnIndex, Short.class, null, format, formatOptions);
    }
    
    @Override
    public Integer getInt(final String headerName, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, headerName, Integer.class, null, format, formatOptions);
    }
    
    @Override
    public Integer getInt(final Enum<?> column, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, column, Integer.class, null, format, formatOptions);
    }
    
    @Override
    public Integer getInt(final int columnIndex, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, columnIndex, Integer.class, null, format, formatOptions);
    }
    
    @Override
    public Long getLong(final String headerName, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, headerName, Long.class, null, format, formatOptions);
    }
    
    @Override
    public Long getLong(final Enum<?> column, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, column, Long.class, null, format, formatOptions);
    }
    
    @Override
    public Long getLong(final int columnIndex, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, columnIndex, Long.class, null, format, formatOptions);
    }
    
    @Override
    public Float getFloat(final String headerName, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, headerName, Float.class, null, format, formatOptions);
    }
    
    @Override
    public Float getFloat(final Enum<?> column, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, column, Float.class, null, format, formatOptions);
    }
    
    @Override
    public Float getFloat(final int columnIndex, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, columnIndex, Float.class, null, format, formatOptions);
    }
    
    @Override
    public Double getDouble(final String headerName, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, headerName, Double.class, null, format, formatOptions);
    }
    
    @Override
    public Double getDouble(final Enum<?> column, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, column, Double.class, null, format, formatOptions);
    }
    
    @Override
    public Double getDouble(final int columnIndex, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, columnIndex, Double.class, null, format, formatOptions);
    }
    
    @Override
    public Character getChar(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, Character.class, null);
    }
    
    @Override
    public Character getChar(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, Character.class, null);
    }
    
    @Override
    public Character getChar(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, Character.class, null);
    }
    
    @Override
    public Boolean getBoolean(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, Boolean.class, null);
    }
    
    @Override
    public Boolean getBoolean(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, Boolean.class, null);
    }
    
    @Override
    public Boolean getBoolean(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, Boolean.class, null);
    }
    
    @Override
    public Boolean getBoolean(final String headerName, final String trueString, final String falseString) {
        return this.metaData.getObjectValue(this.data, headerName, Boolean.class, false, trueString, falseString);
    }
    
    @Override
    public Boolean getBoolean(final Enum<?> column, final String trueString, final String falseString) {
        return this.metaData.getObjectValue(this.data, column, Boolean.class, false, trueString, falseString);
    }
    
    @Override
    public Boolean getBoolean(final int columnIndex, final String trueString, final String falseString) {
        return this.metaData.getObjectValue(this.data, columnIndex, Boolean.class, false, trueString, falseString);
    }
    
    @Override
    public BigInteger getBigInteger(final String headerName, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, headerName, BigInteger.class, null, format, formatOptions);
    }
    
    @Override
    public BigInteger getBigInteger(final Enum<?> column, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, column, BigInteger.class, null, format, formatOptions);
    }
    
    @Override
    public BigInteger getBigInteger(final int columnIndex, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, columnIndex, BigInteger.class, null, format, formatOptions);
    }
    
    @Override
    public BigDecimal getBigDecimal(final String headerName, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, headerName, BigDecimal.class, null, format, formatOptions);
    }
    
    @Override
    public BigDecimal getBigDecimal(final Enum<?> column, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, column, BigDecimal.class, null, format, formatOptions);
    }
    
    @Override
    public BigDecimal getBigDecimal(final int columnIndex, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, columnIndex, BigDecimal.class, null, format, formatOptions);
    }
    
    @Override
    public Date getDate(final String headerName, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, headerName, Date.class, null, format, formatOptions);
    }
    
    @Override
    public Date getDate(final Enum<?> column, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, column, Date.class, null, format, formatOptions);
    }
    
    @Override
    public Date getDate(final int columnIndex, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, columnIndex, Date.class, null, format, formatOptions);
    }
    
    @Override
    public Calendar getCalendar(final String headerName, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, headerName, Calendar.class, null, format, formatOptions);
    }
    
    @Override
    public Calendar getCalendar(final Enum<?> column, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, column, Calendar.class, null, format, formatOptions);
    }
    
    @Override
    public Calendar getCalendar(final int columnIndex, final String format, final String... formatOptions) {
        return this.metaData.getObjectValue(this.data, columnIndex, Calendar.class, null, format, formatOptions);
    }
    
    private String[] buildSelection(String[] selectedFields) {
        if (selectedFields.length == 0) {
            selectedFields = this.metaData.headers();
        }
        return selectedFields;
    }
    
    private int[] buildSelection(int[] selectedIndexes) {
        if (selectedIndexes.length == 0) {
            selectedIndexes = new int[this.data.length];
            for (int i = 0; i < this.data.length; ++i) {
                selectedIndexes[i] = i;
            }
        }
        return selectedIndexes;
    }
    
    public <T extends Enum<T>> T[] buildSelection(final Class<T> enumType, T... selectedColumns) {
        if (selectedColumns.length == 0) {
            selectedColumns = enumType.getEnumConstants();
        }
        return selectedColumns;
    }
    
    @Override
    public Map<Integer, String> toIndexMap(final int... selectedIndexes) {
        return this.fillIndexMap(new HashMap<Integer, String>(selectedIndexes.length), selectedIndexes);
    }
    
    @Override
    public Map<String, String> toFieldMap(final String... selectedFields) {
        return this.fillFieldMap(new HashMap<String, String>(selectedFields.length), selectedFields);
    }
    
    @Override
    public <T extends Enum<T>> Map<T, String> toEnumMap(final Class<T> enumType, final T... selectedColumns) {
        return this.fillEnumMap(new EnumMap<T, String>(enumType), selectedColumns);
    }
    
    @Override
    public Map<String, String> fillFieldMap(final Map<String, String> map, String... selectedFields) {
        selectedFields = this.buildSelection(selectedFields);
        for (int i = 0; i < selectedFields.length; ++i) {
            map.put(selectedFields[i], this.getString(selectedFields[i]));
        }
        return map;
    }
    
    @Override
    public Map<Integer, String> fillIndexMap(final Map<Integer, String> map, int... selectedIndexes) {
        selectedIndexes = this.buildSelection(selectedIndexes);
        for (int i = 0; i < selectedIndexes.length; ++i) {
            map.put(selectedIndexes[i], this.getString(selectedIndexes[i]));
        }
        return map;
    }
    
    @Override
    public <T extends Enum<T>> Map<T, String> fillEnumMap(final Map<T, String> map, final T... selectedColumns) {
        for (int i = 0; i < selectedColumns.length; ++i) {
            map.put(selectedColumns[i], this.getString(selectedColumns[i]));
        }
        return map;
    }
    
    @Override
    public Map<String, Object> toFieldObjectMap(final String... selectedFields) {
        return this.fillFieldObjectMap(new HashMap<String, Object>(selectedFields.length), selectedFields);
    }
    
    @Override
    public Map<Integer, Object> toIndexObjectMap(final int... selectedIndex) {
        return this.fillIndexObjectMap(new HashMap<Integer, Object>(selectedIndex.length), selectedIndex);
    }
    
    @Override
    public <T extends Enum<T>> Map<T, Object> toEnumObjectMap(final Class<T> enumType, final T... selectedColumns) {
        return this.fillEnumObjectMap(new EnumMap<T, Object>(enumType), selectedColumns);
    }
    
    @Override
    public Map<String, Object> fillFieldObjectMap(final Map<String, Object> map, String... selectedFields) {
        selectedFields = this.buildSelection(selectedFields);
        for (int i = 0; i < selectedFields.length; ++i) {
            map.put(selectedFields[i], this.metaData.getObjectValue(this.data, selectedFields[i], null, (Object)null));
        }
        return map;
    }
    
    @Override
    public Map<Integer, Object> fillIndexObjectMap(final Map<Integer, Object> map, int... selectedIndexes) {
        selectedIndexes = this.buildSelection(selectedIndexes);
        for (int i = 0; i < selectedIndexes.length; ++i) {
            map.put(selectedIndexes[i], this.metaData.getObjectValue(this.data, selectedIndexes[i], null, (Object)null));
        }
        return map;
    }
    
    @Override
    public <T extends Enum<T>> Map<T, Object> fillEnumObjectMap(final Map<T, Object> map, T... selectedColumns) {
        selectedColumns = this.buildSelection(selectedColumns.getClass().getComponentType(), selectedColumns);
        for (int i = 0; i < selectedColumns.length; ++i) {
            map.put(selectedColumns[i], this.metaData.getObjectValue(this.data, selectedColumns[i], null, (Object)null));
        }
        return map;
    }
    
    @Override
    public BigInteger getBigInteger(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, BigInteger.class, null);
    }
    
    @Override
    public BigInteger getBigInteger(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, BigInteger.class, null);
    }
    
    @Override
    public BigInteger getBigInteger(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, BigInteger.class, null);
    }
    
    @Override
    public BigDecimal getBigDecimal(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, BigDecimal.class, null);
    }
    
    @Override
    public BigDecimal getBigDecimal(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, BigDecimal.class, null);
    }
    
    @Override
    public BigDecimal getBigDecimal(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, BigDecimal.class, null);
    }
    
    @Override
    public Byte getByte(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, Byte.class, null);
    }
    
    @Override
    public Byte getByte(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, Byte.class, null);
    }
    
    @Override
    public Byte getByte(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, Byte.class, null);
    }
    
    @Override
    public Short getShort(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, Short.class, null);
    }
    
    @Override
    public Short getShort(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, Short.class, null);
    }
    
    @Override
    public Short getShort(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, Short.class, null);
    }
    
    @Override
    public Integer getInt(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, Integer.class, null);
    }
    
    @Override
    public Integer getInt(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, Integer.class, null);
    }
    
    @Override
    public Integer getInt(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, Integer.class, null);
    }
    
    @Override
    public Long getLong(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, Long.class, null);
    }
    
    @Override
    public Long getLong(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, Long.class, null);
    }
    
    @Override
    public Long getLong(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, Long.class, null);
    }
    
    @Override
    public Float getFloat(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, Float.class, null);
    }
    
    @Override
    public Float getFloat(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, Float.class, null);
    }
    
    @Override
    public Float getFloat(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, Float.class, null);
    }
    
    @Override
    public Double getDouble(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, Double.class, null);
    }
    
    @Override
    public Double getDouble(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, Double.class, null);
    }
    
    @Override
    public Double getDouble(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, Double.class, null);
    }
    
    @Override
    public Date getDate(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, Date.class, null);
    }
    
    @Override
    public Date getDate(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, Date.class, null);
    }
    
    @Override
    public Date getDate(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, Date.class, null);
    }
    
    @Override
    public Calendar getCalendar(final String headerName) {
        return this.metaData.getObjectValue(this.data, headerName, Calendar.class, null);
    }
    
    @Override
    public Calendar getCalendar(final Enum<?> column) {
        return this.metaData.getObjectValue(this.data, column, Calendar.class, null);
    }
    
    @Override
    public Calendar getCalendar(final int columnIndex) {
        return this.metaData.getObjectValue(this.data, columnIndex, Calendar.class, null);
    }
    
    @Override
    public String toString() {
        if (this.data == null) {
            return "null";
        }
        if (this.data.length == 0) {
            return "[]";
        }
        final StringBuilder out = new StringBuilder();
        for (int i = 0; i < this.data.length; ++i) {
            if (out.length() != 0) {
                out.append(',').append(' ');
            }
            out.append(this.data[i]);
        }
        return out.toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }
    
    @Override
    public String[] getValues(final String... fieldNames) {
        final String[] out = new String[fieldNames.length];
        for (int i = 0; i < out.length; ++i) {
            out[i] = this.getString(fieldNames[i]);
        }
        return out;
    }
    
    @Override
    public String[] getValues(final int... fieldIndexes) {
        final String[] out = new String[fieldIndexes.length];
        for (int i = 0; i < out.length; ++i) {
            out[i] = this.getString(fieldIndexes[i]);
        }
        return out;
    }
    
    @Override
    public String[] getValues(final Enum<?>... fields) {
        final String[] out = new String[fields.length];
        for (int i = 0; i < out.length; ++i) {
            out[i] = this.getString(fields[i]);
        }
        return out;
    }
}
