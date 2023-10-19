// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.fixed;

import shadeio.univocity.parsers.common.CommonSettings;
import java.util.Collection;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.Set;
import java.lang.annotation.Annotation;
import shadeio.univocity.parsers.annotations.helpers.AnnotationRegistry;
import shadeio.univocity.parsers.annotations.FixedWidth;
import shadeio.univocity.parsers.annotations.helpers.TransformedHeader;
import java.util.LinkedHashSet;
import shadeio.univocity.parsers.annotations.HeaderTransformer;
import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import shadeio.univocity.parsers.annotations.helpers.MethodFilter;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FixedWidthFields implements Cloneable
{
    private List<Integer> fieldLengths;
    private List<Boolean> fieldsToIgnore;
    private List<String> fieldNames;
    private List<FieldAlignment> fieldAlignment;
    private List<Character> fieldPadding;
    private boolean noNames;
    private int totalLength;
    
    public FixedWidthFields(final LinkedHashMap<String, Integer> fields) {
        this.fieldLengths = new ArrayList<Integer>();
        this.fieldsToIgnore = new ArrayList<Boolean>();
        this.fieldNames = new ArrayList<String>();
        this.fieldAlignment = new ArrayList<FieldAlignment>();
        this.fieldPadding = new ArrayList<Character>();
        this.noNames = true;
        this.totalLength = 0;
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Map of fields and their lengths cannot be null/empty");
        }
        for (final Map.Entry<String, Integer> entry : fields.entrySet()) {
            final String fieldName = entry.getKey();
            final Integer fieldLength = entry.getValue();
            this.addField(fieldName, fieldLength);
        }
    }
    
    public FixedWidthFields(final String[] headers, final int[] lengths) {
        this.fieldLengths = new ArrayList<Integer>();
        this.fieldsToIgnore = new ArrayList<Boolean>();
        this.fieldNames = new ArrayList<String>();
        this.fieldAlignment = new ArrayList<FieldAlignment>();
        this.fieldPadding = new ArrayList<Character>();
        this.noNames = true;
        this.totalLength = 0;
        if (headers == null || headers.length == 0) {
            throw new IllegalArgumentException("Headers cannot be null/empty");
        }
        if (lengths == null || lengths.length == 0) {
            throw new IllegalArgumentException("Field lengths cannot be null/empty");
        }
        if (headers.length != lengths.length) {
            throw new IllegalArgumentException("Sequence of headers and their respective lengths must match. Got " + headers.length + " headers but " + lengths.length + " lengths");
        }
        for (int i = 0; i < headers.length; ++i) {
            this.addField(headers[i], lengths[i]);
        }
    }
    
    public FixedWidthFields(final int... fieldLengths) {
        this.fieldLengths = new ArrayList<Integer>();
        this.fieldsToIgnore = new ArrayList<Boolean>();
        this.fieldNames = new ArrayList<String>();
        this.fieldAlignment = new ArrayList<FieldAlignment>();
        this.fieldPadding = new ArrayList<Character>();
        this.noNames = true;
        this.totalLength = 0;
        for (int i = 0; i < fieldLengths.length; ++i) {
            this.addField(fieldLengths[i]);
        }
    }
    
    @Deprecated
    public FixedWidthFields(final Class beanClass) {
        this(beanClass, MethodFilter.ONLY_SETTERS);
    }
    
    public static FixedWidthFields forParsing(final Class beanClass) {
        return new FixedWidthFields(beanClass, MethodFilter.ONLY_SETTERS);
    }
    
    public static FixedWidthFields forWriting(final Class beanClass) {
        return new FixedWidthFields(beanClass, MethodFilter.ONLY_GETTERS);
    }
    
    private FixedWidthFields(final Class beanClass, final MethodFilter methodFilter) {
        this.fieldLengths = new ArrayList<Integer>();
        this.fieldsToIgnore = new ArrayList<Boolean>();
        this.fieldNames = new ArrayList<String>();
        this.fieldAlignment = new ArrayList<FieldAlignment>();
        this.fieldPadding = new ArrayList<Character>();
        this.noNames = true;
        this.totalLength = 0;
        if (beanClass == null) {
            throw new IllegalArgumentException("Class must not be null.");
        }
        final List<TransformedHeader> fieldSequence = AnnotationHelper.getFieldSequence(beanClass, true, null, methodFilter);
        if (fieldSequence.isEmpty()) {
            throw new IllegalArgumentException("Can't derive fixed-width fields from class '" + beanClass.getName() + "'. No @Parsed annotations found.");
        }
        final Set<String> fieldNamesWithoutConfig = new LinkedHashSet<String>();
        for (final TransformedHeader field : fieldSequence) {
            if (field == null) {
                continue;
            }
            final String fieldName = field.getHeaderName();
            final FixedWidth fw = AnnotationHelper.findAnnotation(field.getTarget(), FixedWidth.class);
            if (fw == null) {
                fieldNamesWithoutConfig.add(field.getTargetName());
            }
            else {
                final int length = AnnotationRegistry.getValue(field.getTarget(), fw, "value", fw.value());
                final int from = AnnotationRegistry.getValue(field.getTarget(), fw, "from", fw.from());
                final int to = AnnotationRegistry.getValue(field.getTarget(), fw, "to", fw.to());
                final FieldAlignment alignment = AnnotationRegistry.getValue(field.getTarget(), fw, "alignment", fw.alignment());
                final char padding = AnnotationRegistry.getValue(field.getTarget(), fw, "padding", fw.padding());
                if (length != -1) {
                    if (from != -1 || to != -1) {
                        throw new IllegalArgumentException("Can't initialize fixed-width field from " + field.describe() + ". " + "Can't have field length (" + length + ") defined along with position from (" + from + ") and to (" + to + ")");
                    }
                    this.addField(fieldName, length, alignment, padding);
                }
                else {
                    if (from == -1 || to == -1) {
                        throw new IllegalArgumentException("Can't initialize fixed-width field from " + field.describe() + "'. " + "Field length/position undefined defined");
                    }
                    this.addField(fieldName, from, to, alignment, padding);
                }
            }
        }
        if (fieldNamesWithoutConfig.size() > 0) {
            throw new IllegalArgumentException("Can't derive fixed-width fields from class '" + beanClass.getName() + "'. " + "The following fields don't have a @FixedWidth annotation: " + fieldNamesWithoutConfig);
        }
    }
    
    public FixedWidthFields addField(final int startPosition, final int endPosition) {
        return this.addField(null, startPosition, endPosition, FieldAlignment.LEFT, '\0');
    }
    
    public FixedWidthFields addField(final String name, final int startPosition, final int endPosition) {
        return this.addField(name, startPosition, endPosition, FieldAlignment.LEFT, '\0');
    }
    
    public FixedWidthFields addField(final String name, final int startPosition, final int endPosition, final char padding) {
        return this.addField(name, startPosition, endPosition, FieldAlignment.LEFT, padding);
    }
    
    public FixedWidthFields addField(final String name, final int startPosition, final int endPosition, final FieldAlignment alignment) {
        return this.addField(name, startPosition, endPosition, alignment, '\0');
    }
    
    public FixedWidthFields addField(final int startPosition, final int endPosition, final FieldAlignment alignment) {
        return this.addField(null, startPosition, endPosition, alignment, '\0');
    }
    
    public FixedWidthFields addField(final int startPosition, final int endPosition, final FieldAlignment alignment, final char padding) {
        return this.addField(null, startPosition, endPosition, alignment, padding);
    }
    
    public FixedWidthFields addField(final int startPosition, final int endPosition, final char padding) {
        return this.addField(null, startPosition, endPosition, FieldAlignment.LEFT, padding);
    }
    
    public FixedWidthFields addField(final String name, final int startPosition, final int endPosition, final FieldAlignment alignment, final char padding) {
        final int length = endPosition - startPosition;
        if (startPosition < this.totalLength) {
            throw new IllegalArgumentException("Start position '" + startPosition + "' overlaps with one or more fields");
        }
        if (startPosition > this.totalLength) {
            this.addField(null, startPosition - this.totalLength, FieldAlignment.LEFT, '\0');
            this.fieldsToIgnore.set(this.fieldsToIgnore.size() - 1, Boolean.TRUE);
        }
        return this.addField(name, length, alignment, padding);
    }
    
    boolean[] getFieldsToIgnore() {
        final boolean[] out = new boolean[this.fieldsToIgnore.size()];
        for (int i = 0; i < this.fieldsToIgnore.size(); ++i) {
            out[i] = this.fieldsToIgnore.get(i);
        }
        return out;
    }
    
    public FixedWidthFields addField(final int length) {
        return this.addField(null, length, FieldAlignment.LEFT, '\0');
    }
    
    public FixedWidthFields addField(final int length, final FieldAlignment alignment) {
        return this.addField(null, length, alignment, '\0');
    }
    
    public FixedWidthFields addField(final String name, final int length) {
        return this.addField(name, length, FieldAlignment.LEFT, '\0');
    }
    
    public FixedWidthFields addField(final String name, final int length, final FieldAlignment alignment) {
        return this.addField(name, length, alignment, '\0');
    }
    
    public FixedWidthFields addField(final int length, final char padding) {
        return this.addField(null, length, FieldAlignment.LEFT, padding);
    }
    
    public FixedWidthFields addField(final int length, final FieldAlignment alignment, final char padding) {
        return this.addField(null, length, alignment, padding);
    }
    
    public FixedWidthFields addField(final String name, final int length, final char padding) {
        return this.addField(name, length, FieldAlignment.LEFT, padding);
    }
    
    public FixedWidthFields addField(final String name, final int length, final FieldAlignment alignment, final char padding) {
        this.validateLength(name, length);
        this.fieldLengths.add(length);
        this.fieldsToIgnore.add(Boolean.FALSE);
        this.fieldNames.add(name);
        this.fieldPadding.add(padding);
        if (name != null) {
            this.noNames = false;
        }
        this.fieldAlignment.add(alignment);
        this.totalLength += length;
        return this;
    }
    
    private void validateLength(final String name, final int length) {
        if (length >= 1) {
            return;
        }
        if (name == null) {
            throw new IllegalArgumentException("Invalid field length: " + length + " for field at index " + this.fieldLengths.size());
        }
        throw new IllegalArgumentException("Invalid field length: " + length + " for field " + name);
    }
    
    public int getFieldsPerRecord() {
        return this.fieldLengths.size();
    }
    
    public String[] getFieldNames() {
        if (this.noNames) {
            return null;
        }
        return this.getSelectedElements(this.fieldNames).toArray(ArgumentUtils.EMPTY_STRING_ARRAY);
    }
    
    private <T> List<T> getSelectedElements(final List<T> elements) {
        final List<T> out = new ArrayList<T>();
        for (int i = 0; i < elements.size(); ++i) {
            if (!this.fieldsToIgnore.get(i)) {
                out.add(elements.get(i));
            }
        }
        return out;
    }
    
    public int[] getFieldLengths() {
        return ArgumentUtils.toIntArray(this.getSelectedElements(this.fieldLengths));
    }
    
    int[] getAllLengths() {
        return ArgumentUtils.toIntArray(this.fieldLengths);
    }
    
    public void setFieldLength(final String name, final int newLength) {
        if (name == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        final int index = this.fieldNames.indexOf(name);
        if (index == -1) {
            throw new IllegalArgumentException("Cannot find field with name '" + name + '\'');
        }
        this.validateLength(name, newLength);
        this.fieldLengths.set(index, newLength);
    }
    
    public void setFieldLength(final int position, final int newLength) {
        this.validateIndex(position);
        this.validateLength("at index " + position, newLength);
        this.fieldLengths.set(position, newLength);
    }
    
    public void setAlignment(final FieldAlignment alignment, final int... positions) {
        for (final int position : positions) {
            this.setAlignment(position, alignment);
        }
    }
    
    public void setAlignment(final FieldAlignment alignment, final String... names) {
        for (final String name : names) {
            final int position = this.indexOf(name);
            this.setAlignment(position, alignment);
        }
    }
    
    private void validateIndex(final int position) {
        if (position < 0 && position >= this.fieldLengths.size()) {
            throw new IllegalArgumentException("No field defined at index " + position);
        }
    }
    
    public int indexOf(String fieldName) {
        if (this.noNames) {
            throw new IllegalArgumentException("No field names defined");
        }
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new IllegalArgumentException("Field name cannot be null/empty");
        }
        fieldName = ArgumentUtils.normalize(fieldName);
        int i = 0;
        for (String name : this.fieldNames) {
            name = ArgumentUtils.normalize(name);
            if (name.equals(fieldName)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    private void setAlignment(final int position, final FieldAlignment alignment) {
        if (alignment == null) {
            throw new IllegalArgumentException("Alignment cannot be null");
        }
        this.validateIndex(position);
        this.fieldAlignment.set(position, alignment);
    }
    
    public FieldAlignment getAlignment(final int position) {
        this.validateIndex(position);
        return this.fieldAlignment.get(position);
    }
    
    public FieldAlignment getAlignment(final String fieldName) {
        final int index = this.indexOf(fieldName);
        if (index == -1) {
            throw new IllegalArgumentException("Field '" + fieldName + "' does not exist. Available field names are: " + this.fieldNames);
        }
        return this.getAlignment(index);
    }
    
    public FieldAlignment[] getFieldAlignments() {
        return this.fieldAlignment.toArray(new FieldAlignment[this.fieldAlignment.size()]);
    }
    
    public char[] getFieldPaddings() {
        return ArgumentUtils.toCharArray(this.fieldPadding);
    }
    
    char[] getFieldPaddings(final FixedWidthFormat format) {
        final char[] out = this.getFieldPaddings();
        for (int i = 0; i < out.length; ++i) {
            if (out[i] == '\0') {
                out[i] = format.getPadding();
            }
        }
        return out;
    }
    
    public void setPadding(final char padding, final int... positions) {
        for (final int position : positions) {
            this.setPadding(position, padding);
        }
    }
    
    public void setPadding(final char padding, final String... names) {
        for (final String name : names) {
            final int position = this.indexOf(name);
            this.setPadding(position, padding);
        }
    }
    
    private void setPadding(final int position, final char padding) {
        if (padding == '\0') {
            throw new IllegalArgumentException("Cannot use the null character as padding");
        }
        this.validateIndex(position);
        this.fieldPadding.set(position, padding);
    }
    
    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();
        int i = 0;
        for (final Integer length : this.fieldLengths) {
            out.append("\n\t\t").append(i + 1).append('\t');
            if (i < this.fieldNames.size()) {
                out.append(this.fieldNames.get(i));
            }
            out.append(", length: ").append(length);
            out.append(", align: ").append(this.fieldAlignment.get(i));
            out.append(", padding: ").append(this.fieldPadding.get(i));
            ++i;
        }
        return out.toString();
    }
    
    static void setHeadersIfPossible(final FixedWidthFields fieldLengths, final CommonSettings settings) {
        if (fieldLengths != null && settings.getHeaders() == null) {
            final String[] headers = fieldLengths.getFieldNames();
            if (headers != null) {
                final int[] lengths = fieldLengths.getFieldLengths();
                if (lengths.length == headers.length) {
                    settings.setHeaders(headers);
                }
            }
        }
    }
    
    @Override
    protected FixedWidthFields clone() {
        try {
            final FixedWidthFields out = (FixedWidthFields)super.clone();
            out.fieldLengths = new ArrayList<Integer>(this.fieldLengths);
            out.fieldNames = new ArrayList<String>(this.fieldNames);
            out.fieldAlignment = new ArrayList<FieldAlignment>(this.fieldAlignment);
            out.fieldPadding = new ArrayList<Character>(this.fieldPadding);
            return out;
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
