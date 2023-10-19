// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.record;

import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import java.math.BigDecimal;
import java.math.BigInteger;
import shadeio.univocity.parsers.conversions.Conversion;

public interface Record
{
    RecordMetaData getMetaData();
    
    String[] getValues();
    
    String[] getValues(final String... p0);
    
    String[] getValues(final int... p0);
    
    String[] getValues(final Enum<?>... p0);
    
     <T> T getValue(final String p0, final Class<T> p1);
    
     <T> T getValue(final Enum<?> p0, final Class<T> p1);
    
     <T> T getValue(final int p0, final Class<T> p1);
    
     <T> T getValue(final String p0, final Class<T> p1, final Conversion... p2);
    
     <T> T getValue(final Enum<?> p0, final Class<T> p1, final Conversion... p2);
    
     <T> T getValue(final int p0, final Class<T> p1, final Conversion... p2);
    
     <T> T getValue(final String p0, final T p1);
    
     <T> T getValue(final Enum<?> p0, final T p1);
    
     <T> T getValue(final int p0, final T p1);
    
     <T> T getValue(final String p0, final T p1, final Conversion... p2);
    
     <T> T getValue(final Enum<?> p0, final T p1, final Conversion... p2);
    
     <T> T getValue(final int p0, final T p1, final Conversion... p2);
    
    String getString(final String p0);
    
    String getString(final Enum<?> p0);
    
    String getString(final int p0, final int p1);
    
    String getString(final String p0, final int p1);
    
    String getString(final Enum<?> p0, final int p1);
    
    String getString(final int p0);
    
    Byte getByte(final String p0, final String p1, final String... p2);
    
    Byte getByte(final Enum<?> p0, final String p1, final String... p2);
    
    Byte getByte(final int p0, final String p1, final String... p2);
    
    Short getShort(final String p0, final String p1, final String... p2);
    
    Short getShort(final Enum<?> p0, final String p1, final String... p2);
    
    Short getShort(final int p0, final String p1, final String... p2);
    
    Integer getInt(final String p0, final String p1, final String... p2);
    
    Integer getInt(final Enum<?> p0, final String p1, final String... p2);
    
    Integer getInt(final int p0, final String p1, final String... p2);
    
    Long getLong(final String p0, final String p1, final String... p2);
    
    Long getLong(final Enum<?> p0, final String p1, final String... p2);
    
    Long getLong(final int p0, final String p1, final String... p2);
    
    Float getFloat(final String p0, final String p1, final String... p2);
    
    Float getFloat(final Enum<?> p0, final String p1, final String... p2);
    
    Float getFloat(final int p0, final String p1, final String... p2);
    
    Double getDouble(final String p0, final String p1, final String... p2);
    
    Double getDouble(final Enum<?> p0, final String p1, final String... p2);
    
    Double getDouble(final int p0, final String p1, final String... p2);
    
    Byte getByte(final String p0);
    
    Byte getByte(final Enum<?> p0);
    
    Byte getByte(final int p0);
    
    Short getShort(final String p0);
    
    Short getShort(final Enum<?> p0);
    
    Short getShort(final int p0);
    
    Integer getInt(final String p0);
    
    Integer getInt(final Enum<?> p0);
    
    Integer getInt(final int p0);
    
    Long getLong(final String p0);
    
    Long getLong(final Enum<?> p0);
    
    Long getLong(final int p0);
    
    Float getFloat(final String p0);
    
    Float getFloat(final Enum<?> p0);
    
    Float getFloat(final int p0);
    
    Double getDouble(final String p0);
    
    Double getDouble(final Enum<?> p0);
    
    Double getDouble(final int p0);
    
    Character getChar(final String p0);
    
    Character getChar(final Enum<?> p0);
    
    Character getChar(final int p0);
    
    Boolean getBoolean(final String p0);
    
    Boolean getBoolean(final Enum<?> p0);
    
    Boolean getBoolean(final int p0);
    
    Boolean getBoolean(final String p0, final String p1, final String p2);
    
    Boolean getBoolean(final Enum<?> p0, final String p1, final String p2);
    
    Boolean getBoolean(final int p0, final String p1, final String p2);
    
    BigInteger getBigInteger(final String p0, final String p1, final String... p2);
    
    BigInteger getBigInteger(final Enum<?> p0, final String p1, final String... p2);
    
    BigInteger getBigInteger(final int p0, final String p1, final String... p2);
    
    BigDecimal getBigDecimal(final String p0, final String p1, final String... p2);
    
    BigDecimal getBigDecimal(final Enum<?> p0, final String p1, final String... p2);
    
    BigDecimal getBigDecimal(final int p0, final String p1, final String... p2);
    
    BigInteger getBigInteger(final String p0);
    
    BigInteger getBigInteger(final Enum<?> p0);
    
    BigInteger getBigInteger(final int p0);
    
    BigDecimal getBigDecimal(final String p0);
    
    BigDecimal getBigDecimal(final Enum<?> p0);
    
    BigDecimal getBigDecimal(final int p0);
    
    Date getDate(final String p0, final String p1, final String... p2);
    
    Date getDate(final Enum<?> p0, final String p1, final String... p2);
    
    Date getDate(final int p0, final String p1, final String... p2);
    
    Calendar getCalendar(final String p0, final String p1, final String... p2);
    
    Calendar getCalendar(final Enum<?> p0, final String p1, final String... p2);
    
    Calendar getCalendar(final int p0, final String p1, final String... p2);
    
    Date getDate(final String p0);
    
    Date getDate(final Enum<?> p0);
    
    Date getDate(final int p0);
    
    Calendar getCalendar(final String p0);
    
    Calendar getCalendar(final Enum<?> p0);
    
    Calendar getCalendar(final int p0);
    
    Map<String, String> toFieldMap(final String... p0);
    
    Map<Integer, String> toIndexMap(final int... p0);
    
     <T extends Enum<T>> Map<T, String> toEnumMap(final Class<T> p0, final T... p1);
    
    Map<String, String> fillFieldMap(final Map<String, String> p0, final String... p1);
    
    Map<Integer, String> fillIndexMap(final Map<Integer, String> p0, final int... p1);
    
     <T extends Enum<T>> Map<T, String> fillEnumMap(final Map<T, String> p0, final T... p1);
    
    Map<String, Object> toFieldObjectMap(final String... p0);
    
    Map<Integer, Object> toIndexObjectMap(final int... p0);
    
     <T extends Enum<T>> Map<T, Object> toEnumObjectMap(final Class<T> p0, final T... p1);
    
    Map<String, Object> fillFieldObjectMap(final Map<String, Object> p0, final String... p1);
    
    Map<Integer, Object> fillIndexObjectMap(final Map<Integer, Object> p0, final int... p1);
    
     <T extends Enum<T>> Map<T, Object> fillEnumObjectMap(final Map<T, Object> p0, final T... p1);
}
