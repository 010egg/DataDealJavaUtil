// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.util.Arrays;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

abstract class FieldWriterBoolean extends FieldWriter
{
    final byte[] utf8ValueTrue;
    final byte[] utf8ValueFalse;
    final byte[] utf8Value1;
    final byte[] utf8Value0;
    final char[] utf16ValueTrue;
    final char[] utf16ValueFalse;
    final char[] utf16Value1;
    final char[] utf16Value0;
    
    FieldWriterBoolean(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
        byte[] bytes = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + 4);
        bytes[this.nameWithColonUTF8.length] = 116;
        bytes[this.nameWithColonUTF8.length + 1] = 114;
        bytes[this.nameWithColonUTF8.length + 2] = 117;
        bytes[this.nameWithColonUTF8.length + 3] = 101;
        this.utf8ValueTrue = bytes;
        bytes = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + 5);
        bytes[this.nameWithColonUTF8.length] = 102;
        bytes[this.nameWithColonUTF8.length + 1] = 97;
        bytes[this.nameWithColonUTF8.length + 2] = 108;
        bytes[this.nameWithColonUTF8.length + 3] = 115;
        bytes[this.nameWithColonUTF8.length + 4] = 101;
        this.utf8ValueFalse = bytes;
        bytes = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + 1);
        bytes[this.nameWithColonUTF8.length] = 49;
        this.utf8Value1 = bytes;
        bytes = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + 1);
        bytes[this.nameWithColonUTF8.length] = 48;
        this.utf8Value0 = bytes;
        char[] chars = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + 4);
        chars[this.nameWithColonUTF16.length] = 't';
        chars[this.nameWithColonUTF16.length + 1] = 'r';
        chars[this.nameWithColonUTF16.length + 2] = 'u';
        chars[this.nameWithColonUTF16.length + 3] = 'e';
        this.utf16ValueTrue = chars;
        chars = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + 5);
        chars[this.nameWithColonUTF16.length] = 'f';
        chars[this.nameWithColonUTF16.length + 1] = 'a';
        chars[this.nameWithColonUTF16.length + 2] = 'l';
        chars[this.nameWithColonUTF16.length + 3] = 's';
        chars[this.nameWithColonUTF16.length + 4] = 'e';
        this.utf16ValueFalse = chars;
        chars = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + 1);
        chars[this.nameWithColonUTF16.length] = '1';
        this.utf16Value1 = chars;
        chars = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + 1);
        chars[this.nameWithColonUTF16.length] = '0';
        this.utf16Value0 = chars;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final Object object) {
        final Boolean value = (Boolean)this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeBool(value);
    }
    
    @Override
    public final void writeBool(final JSONWriter jsonWriter, final boolean value) {
        final long features = jsonWriter.getFeatures(this.features);
        if ((features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0x0L) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeString(value ? "true" : "false");
            return;
        }
        if (jsonWriter.utf8) {
            jsonWriter.writeNameRaw(((features & JSONWriter.Feature.WriteBooleanAsNumber.mask) != 0x0L) ? (value ? this.utf8Value1 : this.utf8Value0) : (value ? this.utf8ValueTrue : this.utf8ValueFalse));
            return;
        }
        if (jsonWriter.utf16) {
            jsonWriter.writeNameRaw(((features & JSONWriter.Feature.WriteBooleanAsNumber.mask) != 0x0L) ? (value ? this.utf16Value1 : this.utf16Value0) : (value ? this.utf16ValueTrue : this.utf16ValueFalse));
            return;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeBool(value);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final Object object) {
        Boolean value;
        try {
            value = (Boolean)this.getFieldValue(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        if (value == null) {
            final long features = this.features | jsonWriter.getFeatures();
            if ((features & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullBooleanAsFalse.mask)) == 0x0L) {
                return false;
            }
            this.writeFieldName(jsonWriter);
            jsonWriter.writeBooleanNull();
            return true;
        }
        else {
            if (this.fieldClass == Boolean.TYPE && !value && (jsonWriter.getFeatures(this.features) & JSONWriter.Feature.NotWriteDefaultValue.mask) != 0x0L) {
                return false;
            }
            this.writeBool(jsonWriter, value);
            return true;
        }
    }
    
    @Override
    public ObjectWriter getObjectWriter(final JSONWriter jsonWriter, final Class valueClass) {
        return ObjectWriterImplBoolean.INSTANCE;
    }
}
