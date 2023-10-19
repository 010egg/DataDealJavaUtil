// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.fixed;

import shadeio.univocity.parsers.common.record.AbstractRecordFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import shadeio.univocity.parsers.common.record.RecordMetaData;
import shadeio.univocity.parsers.common.record.Record;
import shadeio.univocity.parsers.common.ParserOutput;
import shadeio.univocity.parsers.common.ColumnMap;
import shadeio.univocity.parsers.common.record.RecordFactory;
import shadeio.univocity.parsers.common.ParsingContextWrapper;
import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.Context;

class Lookup
{
    final char[] value;
    final int[] lengths;
    final FieldAlignment[] alignments;
    final boolean[] ignore;
    final char[] paddings;
    final String[] fieldNames;
    final char wildcard;
    Context context;
    
    Lookup(final String value, final FixedWidthFields config, final FixedWidthFormat format) {
        this.value = value.toCharArray();
        this.lengths = config.getFieldLengths();
        this.alignments = config.getFieldAlignments();
        this.fieldNames = config.getFieldNames();
        this.paddings = config.getFieldPaddings(format);
        this.wildcard = format.getLookupWildcard();
        this.ignore = config.getFieldsToIgnore();
    }
    
    void initializeLookupContext(final ParsingContext context, final String[] headersToUse) {
        this.context = new ParsingContextWrapper(context) {
            RecordFactory recordFactory;
            final ColumnMap columnMap = new ColumnMap(this, null);
            
            @Override
            public String[] headers() {
                return headersToUse;
            }
            
            @Override
            public int indexOf(final String header) {
                return this.columnMap.indexOf(header);
            }
            
            @Override
            public int indexOf(final Enum<?> header) {
                return this.columnMap.indexOf(header);
            }
            
            @Override
            public Record toRecord(final String[] row) {
                if (this.recordFactory == null) {
                    this.recordFactory = new RecordFactory(this);
                }
                return this.recordFactory.newRecord(row);
            }
            
            @Override
            public RecordMetaData recordMetaData() {
                if (this.recordFactory == null) {
                    this.recordFactory = new RecordFactory(this);
                }
                return ((AbstractRecordFactory<R, RecordMetaData>)this.recordFactory).getRecordMetaData();
            }
        };
    }
    
    boolean matches(final char[] lookup) {
        if (this.value.length > lookup.length) {
            return false;
        }
        for (int i = 0; i < this.value.length; ++i) {
            final char ch = this.value[i];
            if (ch != this.wildcard && ch != lookup[i]) {
                return false;
            }
        }
        return true;
    }
    
    static void registerLookahead(final String lookup, final FixedWidthFields lengths, final Map<String, FixedWidthFields> map) {
        registerLookup("ahead", lookup, lengths, map);
    }
    
    static void registerLookbehind(final String lookup, final FixedWidthFields lengths, final Map<String, FixedWidthFields> map) {
        registerLookup("behind", lookup, lengths, map);
    }
    
    private static void registerLookup(final String direction, final String lookup, final FixedWidthFields lengths, final Map<String, FixedWidthFields> map) {
        if (lookup == null) {
            throw new IllegalArgumentException("Look" + direction + " value cannot be null");
        }
        if (lengths == null) {
            throw new IllegalArgumentException("Lengths of fields associated to look" + direction + " value '" + lookup + "' cannot be null");
        }
        map.put(lookup, lengths);
    }
    
    static Lookup[] getLookupFormats(final Map<String, FixedWidthFields> map, final FixedWidthFormat format) {
        if (map.isEmpty()) {
            return null;
        }
        final Lookup[] out = new Lookup[map.size()];
        int i = 0;
        for (final Map.Entry<String, FixedWidthFields> e : map.entrySet()) {
            out[i++] = new Lookup(e.getKey(), e.getValue(), format);
        }
        Arrays.sort(out, new Comparator<Lookup>() {
            @Override
            public int compare(final Lookup o1, final Lookup o2) {
                return (o1.value.length < o2.value.length) ? 1 : ((o1.value.length == o2.value.length) ? 0 : -1);
            }
        });
        return out;
    }
    
    static int calculateMaxLookupLength(final Lookup[]... lookupArrays) {
        int max = 0;
        for (final Lookup[] lookups : lookupArrays) {
            if (lookups != null) {
                for (final Lookup lookup : lookups) {
                    if (max < lookup.value.length) {
                        max = lookup.value.length;
                    }
                }
            }
        }
        return max;
    }
    
    static int[] calculateMaxFieldLengths(final FixedWidthFields fieldLengths, final Map<String, FixedWidthFields> lookaheadFormats, final Map<String, FixedWidthFields> lookbehindFormats) {
        final List<int[]> allLengths = new ArrayList<int[]>();
        if (fieldLengths != null) {
            allLengths.add(fieldLengths.getFieldLengths());
        }
        for (final FixedWidthFields lengths : lookaheadFormats.values()) {
            allLengths.add(lengths.getFieldLengths());
        }
        for (final FixedWidthFields lengths : lookbehindFormats.values()) {
            allLengths.add(lengths.getFieldLengths());
        }
        if (allLengths.isEmpty()) {
            throw new IllegalStateException("Cannot determine field lengths to use.");
        }
        int lastColumn = -1;
        for (final int[] lengths2 : allLengths) {
            if (lastColumn < lengths2.length) {
                lastColumn = lengths2.length;
            }
        }
        final int[] out = new int[lastColumn];
        Arrays.fill(out, 0);
        for (final int[] lengths3 : allLengths) {
            for (int i = 0; i < lastColumn; ++i) {
                if (i < lengths3.length) {
                    final int length = lengths3[i];
                    if (out[i] < length) {
                        out[i] = length;
                    }
                }
            }
        }
        return out;
    }
}
