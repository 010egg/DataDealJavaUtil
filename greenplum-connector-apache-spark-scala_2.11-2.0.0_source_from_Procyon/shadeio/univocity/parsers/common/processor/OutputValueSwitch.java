// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import java.util.Collection;
import shadeio.univocity.parsers.common.ArgumentUtils;
import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import shadeio.univocity.parsers.annotations.helpers.MethodFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Arrays;
import java.util.Comparator;

public class OutputValueSwitch extends RowWriterProcessorSwitch
{
    private Switch defaultSwitch;
    private Switch[] switches;
    private Switch selectedSwitch;
    private Class[] types;
    private final int columnIndex;
    private final String headerName;
    private Comparator comparator;
    
    public OutputValueSwitch() {
        this(0);
    }
    
    public OutputValueSwitch(final int columnIndex) {
        this.switches = new Switch[0];
        this.types = new Class[0];
        this.comparator = new Comparator<Object>() {
            @Override
            public int compare(final Object o1, final Object o2) {
                return (o1 != o2 && (o1 == null || !o1.equals(o2))) ? 1 : 0;
            }
        };
        this.columnIndex = this.getValidatedIndex(columnIndex);
        this.headerName = null;
    }
    
    public OutputValueSwitch(final String headerName) {
        this.switches = new Switch[0];
        this.types = new Class[0];
        this.comparator = new Comparator<Object>() {
            @Override
            public int compare(final Object o1, final Object o2) {
                return (o1 != o2 && (o1 == null || !o1.equals(o2))) ? 1 : 0;
            }
        };
        this.headerName = this.getValidatedHeaderName(headerName);
        this.columnIndex = 0;
    }
    
    public OutputValueSwitch(final String headerName, final int columnIndex) {
        this.switches = new Switch[0];
        this.types = new Class[0];
        this.comparator = new Comparator<Object>() {
            @Override
            public int compare(final Object o1, final Object o2) {
                return (o1 != o2 && (o1 == null || !o1.equals(o2))) ? 1 : 0;
            }
        };
        this.columnIndex = this.getValidatedIndex(columnIndex);
        this.headerName = this.getValidatedHeaderName(headerName);
    }
    
    private int getValidatedIndex(final int columnIndex) {
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Column index must be positive");
        }
        return columnIndex;
    }
    
    private String getValidatedHeaderName(final String headerName) {
        if (headerName == null || headerName.trim().length() == 0) {
            throw new IllegalArgumentException("Header name cannot be blank");
        }
        return headerName;
    }
    
    public void setComparator(final Comparator<?> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator must not be null");
        }
        this.comparator = comparator;
    }
    
    public void setDefaultSwitch(final RowWriterProcessor<Object[]> rowProcessor, final String... headersToUse) {
        this.defaultSwitch = new Switch(rowProcessor, headersToUse, null, null);
    }
    
    public void setDefaultSwitch(final RowWriterProcessor<Object[]> rowProcessor, final int... indexesToUse) {
        this.defaultSwitch = new Switch(rowProcessor, null, indexesToUse, null);
    }
    
    @Override
    protected String[] getHeaders() {
        return (String[])((this.selectedSwitch != null) ? this.selectedSwitch.headers : null);
    }
    
    @Override
    protected int[] getIndexes() {
        return (int[])((this.selectedSwitch != null) ? this.selectedSwitch.indexes : null);
    }
    
    private Switch getSwitch(Object value) {
        if (value instanceof Object[]) {
            final Object[] row = (Object[])value;
            if (row.length < this.columnIndex) {
                return this.defaultSwitch;
            }
            value = row[this.columnIndex];
        }
        for (int i = 0; i < this.switches.length; ++i) {
            final Switch s = this.switches[i];
            final Class type = this.types[i];
            if (type != null) {
                if (type.isAssignableFrom(value.getClass())) {
                    return s;
                }
            }
            else if (this.comparator.compare(value, s.value) == 0) {
                return s;
            }
        }
        return this.defaultSwitch;
    }
    
    @Override
    protected RowWriterProcessor<?> switchRowProcessor(final Object row) {
        this.selectedSwitch = this.getSwitch(row);
        return (this.selectedSwitch != null) ? this.selectedSwitch.processor : null;
    }
    
    public void addSwitchForValue(final Object value, final RowWriterProcessor<Object[]> rowProcessor, final String... headersToUse) {
        this.addSwitch(new Switch(rowProcessor, headersToUse, null, value));
    }
    
    public void addSwitchForValue(final Object value, final RowWriterProcessor<Object[]> rowProcessor) {
        this.addSwitch(new Switch(rowProcessor, null, null, value));
    }
    
    public void addSwitchForValue(final Object value, final RowWriterProcessor<Object[]> rowProcessor, final int... indexesToUse) {
        this.addSwitch(new Switch(rowProcessor, null, indexesToUse, value));
    }
    
    public <T> void addSwitchForType(final Class<T> beanType, final String... headersToUse) {
        this.addSwitch(new Switch(headersToUse, null, beanType));
    }
    
    public <T> void addSwitchForType(final Class<T> beanType, final int... indexesToUse) {
        this.addSwitch(new Switch(null, indexesToUse, beanType));
    }
    
    public <T> void addSwitchForType(final Class<T> beanType) {
        this.addSwitch(new Switch(null, null, beanType));
    }
    
    private void addSwitch(final Switch newSwitch) {
        (this.switches = Arrays.copyOf(this.switches, this.switches.length + 1))[this.switches.length - 1] = newSwitch;
        this.types = Arrays.copyOf(this.types, this.types.length + 1);
        if (newSwitch.value != null && newSwitch.value.getClass() == Class.class) {
            this.types[this.types.length - 1] = (Class)newSwitch.value;
        }
    }
    
    private <V> V getValue(final Map<?, V> map, final int index) {
        final int i = 0;
        for (final Map.Entry<?, V> e : map.entrySet()) {
            if (i == index) {
                return e.getValue();
            }
        }
        return null;
    }
    
    private String[] getHeadersFromSwitch(final Object value) {
        for (int i = 0; i < this.switches.length; ++i) {
            final Switch s = this.getSwitch(value);
            if (s != null) {
                return s.headers;
            }
        }
        return null;
    }
    
    @Override
    public String[] getHeaders(final Object input) {
        if (!(input instanceof Object[])) {
            return this.getHeadersFromSwitch(input);
        }
        final Object[] row = (Object[])input;
        if (this.columnIndex < row.length) {
            return this.getHeadersFromSwitch(row[this.columnIndex]);
        }
        return null;
    }
    
    @Override
    public String[] getHeaders(final Map headerMapping, final Map mapInput) {
        Object mapValue = null;
        if (mapInput != null && !mapInput.isEmpty()) {
            String headerToUse = this.headerName;
            if (headerMapping != null) {
                if (this.headerName != null) {
                    final Object value = headerMapping.get(this.headerName);
                    headerToUse = ((value == null) ? null : value.toString());
                }
                else if (this.columnIndex != -1) {
                    final Object value = this.getValue((Map<?, Object>)headerMapping, this.columnIndex);
                    headerToUse = ((value == null) ? null : value.toString());
                }
            }
            if (headerToUse != null) {
                mapValue = mapInput.get(headerToUse);
            }
            else {
                mapValue = this.getValue((Map<?, Object>)mapInput, this.columnIndex);
            }
        }
        return this.getHeadersFromSwitch(mapValue);
    }
    
    public int getColumnIndex() {
        return this.columnIndex;
    }
    
    private List<Object> getSwitchValues() {
        final List<Object> values = new ArrayList<Object>(this.switches.length);
        for (final Switch s : this.switches) {
            values.add(s.value);
        }
        return values;
    }
    
    @Override
    protected String describeSwitch() {
        return "Expecting one of values: " + this.getSwitchValues() + " at column index " + this.getColumnIndex();
    }
    
    private static class Switch
    {
        final RowWriterProcessor<Object[]> processor;
        final String[] headers;
        final int[] indexes;
        final Object value;
        
        Switch(final RowWriterProcessor<Object[]> processor, final String[] headers, final int[] indexes, final Object value) {
            this.processor = processor;
            this.headers = (String[])((headers == null || headers.length == 0) ? null : headers);
            this.indexes = (int[])((indexes == null || indexes.length == 0) ? null : indexes);
            this.value = value;
        }
        
        Switch(String[] headers, int[] indexes, final Class<?> type) {
            this.processor = new BeanWriterProcessor<Object[]>((Class<Object[]>)type);
            if (headers == null && indexes == null) {
                headers = AnnotationHelper.deriveHeaderNamesFromFields(type, MethodFilter.ONLY_GETTERS);
                indexes = ArgumentUtils.toIntArray(Arrays.asList(AnnotationHelper.getSelectedIndexes(type, MethodFilter.ONLY_GETTERS)));
            }
            this.headers = (String[])((headers == null || headers.length == 0) ? null : headers);
            this.indexes = (int[])((indexes == null || indexes.length == 0) ? null : indexes);
            this.value = type;
        }
    }
}
