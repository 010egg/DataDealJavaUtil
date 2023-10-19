// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import shadeio.univocity.parsers.common.ArgumentUtils;
import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Arrays;
import shadeio.univocity.parsers.common.processor.CustomMatcher;
import java.util.Comparator;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractInputValueSwitch<T extends Context> extends AbstractProcessorSwitch<T>
{
    private int columnIndex;
    private String columnName;
    private Switch[] switches;
    private Switch defaultSwitch;
    private String[] headers;
    private int[] indexes;
    private static final Comparator<String> caseSensitiveComparator;
    private static final Comparator<String> caseInsensitiveComparator;
    private Comparator<String> comparator;
    
    public AbstractInputValueSwitch() {
        this(0);
    }
    
    public AbstractInputValueSwitch(final int columnIndex) {
        this.columnIndex = -1;
        this.columnName = null;
        this.switches = new Switch[0];
        this.defaultSwitch = null;
        this.comparator = AbstractInputValueSwitch.caseInsensitiveComparator;
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Column index must be positive");
        }
        this.columnIndex = columnIndex;
    }
    
    public AbstractInputValueSwitch(final String columnName) {
        this.columnIndex = -1;
        this.columnName = null;
        this.switches = new Switch[0];
        this.defaultSwitch = null;
        this.comparator = AbstractInputValueSwitch.caseInsensitiveComparator;
        if (columnName == null || columnName.trim().isEmpty()) {
            throw new IllegalArgumentException("Column name cannot be blank");
        }
        this.columnName = columnName;
    }
    
    public void setCaseSensitive(final boolean caseSensitive) {
        this.comparator = (caseSensitive ? AbstractInputValueSwitch.caseSensitiveComparator : AbstractInputValueSwitch.caseInsensitiveComparator);
    }
    
    public void setComparator(final Comparator<String> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator must not be null");
        }
        this.comparator = comparator;
    }
    
    public void setDefaultSwitch(final Processor<T> processor, final String... headersToUse) {
        this.defaultSwitch = new Switch((Processor<T>)processor, headersToUse, null, null, null);
    }
    
    public void setDefaultSwitch(final Processor<T> processor) {
        this.defaultSwitch = new Switch((Processor<T>)processor, null, null, null, null);
    }
    
    public void setDefaultSwitch(final Processor<T> processor, final int... indexesToUse) {
        this.defaultSwitch = new Switch((Processor<T>)processor, null, indexesToUse, null, null);
    }
    
    public boolean hasDefaultSwitch() {
        return this.defaultSwitch != null;
    }
    
    public void addSwitchForValue(final String value, final Processor<T> processor) {
        (this.switches = Arrays.copyOf(this.switches, this.switches.length + 1))[this.switches.length - 1] = new Switch((Processor<T>)processor, null, null, value, null);
    }
    
    public void addSwitchForValue(final String value, final Processor<T> processor, final String... headersToUse) {
        (this.switches = Arrays.copyOf(this.switches, this.switches.length + 1))[this.switches.length - 1] = new Switch((Processor<T>)processor, headersToUse, null, value, null);
    }
    
    public void addSwitchForValue(final CustomMatcher matcher, final Processor<T> processor) {
        (this.switches = Arrays.copyOf(this.switches, this.switches.length + 1))[this.switches.length - 1] = new Switch((Processor<T>)processor, null, null, null, matcher);
    }
    
    public void addSwitchForValue(final CustomMatcher matcher, final Processor<T> processor, final String... headersToUse) {
        (this.switches = Arrays.copyOf(this.switches, this.switches.length + 1))[this.switches.length - 1] = new Switch((Processor<T>)processor, headersToUse, null, null, matcher);
    }
    
    public void addSwitchForValue(final String value, final Processor<T> processor, final int... indexesToUse) {
        (this.switches = Arrays.copyOf(this.switches, this.switches.length + 1))[this.switches.length - 1] = new Switch((Processor<T>)processor, null, indexesToUse, value, null);
    }
    
    public void addSwitchForValue(final CustomMatcher matcher, final Processor<T> processor, final int... indexesToUse) {
        (this.switches = Arrays.copyOf(this.switches, this.switches.length + 1))[this.switches.length - 1] = new Switch((Processor<T>)processor, null, indexesToUse, null, matcher);
    }
    
    @Override
    public String[] getHeaders() {
        return this.headers;
    }
    
    @Override
    public int[] getIndexes() {
        return this.indexes;
    }
    
    @Override
    protected Processor<T> switchRowProcessor(final String[] row, final T context) {
        if (this.columnIndex == -1) {
            final String[] headers = context.headers();
            if (headers == null) {
                throw new DataProcessingException("Unable to determine position of column named '" + this.columnName + "' as no headers have been defined nor extracted from the input");
            }
            this.columnIndex = ArgumentUtils.indexOf(headers, this.columnName);
            if (this.columnIndex == -1) {
                throw new DataProcessingException("Unable to determine position of column named '" + this.columnName + "' as it does not exist in the headers. Available headers are " + Arrays.toString(headers));
            }
        }
        if (this.columnIndex < row.length) {
            final String valueToMatch = row[this.columnIndex];
            for (int i = 0; i < this.switches.length; ++i) {
                final Switch s = this.switches[i];
                if (s.matcher != null && s.matcher.matches(valueToMatch)) {
                    return (Processor<T>)s.processor;
                }
                if (this.comparator.compare(valueToMatch, s.value) == 0) {
                    this.headers = s.headers;
                    this.indexes = s.indexes;
                    return (Processor<T>)s.processor;
                }
            }
        }
        if (this.defaultSwitch != null) {
            this.headers = this.defaultSwitch.headers;
            this.indexes = this.defaultSwitch.indexes;
            return (Processor<T>)this.defaultSwitch.processor;
        }
        this.headers = null;
        this.indexes = null;
        throw new DataProcessingException("Unable to process input row. No switches activated and no default switch defined.", this.columnIndex, row, null);
    }
    
    static {
        caseSensitiveComparator = new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return (o1 != o2 && (o1 == null || !o1.equals(o2))) ? 1 : 0;
            }
        };
        caseInsensitiveComparator = new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return (o1 != o2 && (o1 == null || !o1.equalsIgnoreCase(o2))) ? 1 : 0;
            }
        };
    }
    
    private static class Switch<T extends Context>
    {
        final Processor<T> processor;
        final String[] headers;
        final int[] indexes;
        final String value;
        final CustomMatcher matcher;
        
        Switch(final Processor<T> processor, final String[] headers, final int[] indexes, final String value, final CustomMatcher matcher) {
            this.processor = processor;
            this.headers = (String[])((headers == null || headers.length == 0) ? null : headers);
            this.indexes = (int[])((indexes == null || indexes.length == 0) ? null : indexes);
            this.value = ((value == null) ? null : value.intern());
            this.matcher = matcher;
        }
        
        @Override
        public String toString() {
            return "Switch{processor=" + this.processor + ", headers=" + Arrays.toString(this.headers) + ", indexes=" + Arrays.toString(this.indexes) + ", value='" + this.value + '\'' + ", matcher=" + this.matcher + '}';
        }
    }
}
