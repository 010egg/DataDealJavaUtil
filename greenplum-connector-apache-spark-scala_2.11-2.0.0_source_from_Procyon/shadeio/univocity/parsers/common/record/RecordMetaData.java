// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.record;

import shadeio.univocity.parsers.common.fields.FieldSet;
import shadeio.univocity.parsers.conversions.Conversion;

public interface RecordMetaData
{
    int indexOf(final Enum<?> p0);
    
    int indexOf(final String p0);
    
    Class<?> typeOf(final Enum<?> p0);
    
    Class<?> typeOf(final String p0);
    
    Class<?> typeOf(final int p0);
    
    void setTypeOfColumns(final Class<?> p0, final Enum... p1);
    
    void setTypeOfColumns(final Class<?> p0, final String... p1);
    
    void setTypeOfColumns(final Class<?> p0, final int... p1);
    
     <T> void setDefaultValueOfColumns(final T p0, final Enum<?>... p1);
    
     <T> void setDefaultValueOfColumns(final T p0, final String... p1);
    
     <T> void setDefaultValueOfColumns(final T p0, final int... p1);
    
    Object defaultValueOf(final Enum<?> p0);
    
    Object defaultValueOf(final String p0);
    
    Object defaultValueOf(final int p0);
    
     <T extends Enum<T>> FieldSet<T> convertFields(final Class<T> p0, final Conversion... p1);
    
    FieldSet<String> convertFields(final Conversion... p0);
    
    FieldSet<Integer> convertIndexes(final Conversion... p0);
    
    String[] headers();
    
    String[] selectedHeaders();
    
    boolean containsColumn(final String p0);
}
