// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.IOUtils;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import com.alibaba.fastjson2.SymbolTable;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

class FieldWriterEnum extends FieldWriter
{
    final byte[][] valueNameCacheUTF8;
    final char[][] valueNameCacheUTF16;
    final byte[][] utf8ValueCache;
    final char[][] utf16ValueCache;
    final Class enumType;
    final Enum[] enumConstants;
    final long[] hashCodes;
    final long[] hashCodesSymbolCache;
    
    protected FieldWriterEnum(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class<? extends Enum> enumClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldType, enumClass, field, method);
        this.enumType = enumClass;
        this.enumConstants = (Enum[])enumClass.getEnumConstants();
        this.hashCodes = new long[this.enumConstants.length];
        this.hashCodesSymbolCache = new long[this.enumConstants.length];
        for (int i = 0; i < this.enumConstants.length; ++i) {
            this.hashCodes[i] = Fnv.hashCode64(this.enumConstants[i].name());
        }
        this.valueNameCacheUTF8 = new byte[this.enumConstants.length][];
        this.valueNameCacheUTF16 = new char[this.enumConstants.length][];
        this.utf8ValueCache = new byte[this.enumConstants.length][];
        this.utf16ValueCache = new char[this.enumConstants.length][];
    }
    
    @Override
    public final void writeEnumJSONB(final JSONWriter jsonWriter, final Enum e) {
        if (e == null) {
            return;
        }
        final long features = jsonWriter.getFeatures(this.features);
        final boolean usingOrdinal = (features & (JSONWriter.Feature.WriteEnumUsingToString.mask | JSONWriter.Feature.WriteEnumsUsingName.mask)) == 0x0L;
        final boolean usingToString = (features & JSONWriter.Feature.WriteEnumUsingToString.mask) != 0x0L;
        final int ordinal = e.ordinal();
        final SymbolTable symbolTable = jsonWriter.symbolTable;
        if (symbolTable != null && usingOrdinal && !usingToString && this.writeSymbolNameOrdinal(jsonWriter, ordinal, symbolTable)) {
            return;
        }
        if (usingToString) {
            this.writeJSONBToString(jsonWriter, e, symbolTable);
            return;
        }
        if (usingOrdinal) {
            int symbol;
            if (symbolTable != null) {
                final int symbolTableIdentity = System.identityHashCode(symbolTable);
                if (this.nameSymbolCache == 0L) {
                    symbol = symbolTable.getOrdinalByHashCode(this.hashCode);
                    this.nameSymbolCache = ((long)symbol << 32 | (long)symbolTableIdentity);
                }
                else {
                    final int identity = (int)this.nameSymbolCache;
                    if (identity == symbolTableIdentity) {
                        symbol = (int)(this.nameSymbolCache >> 32);
                    }
                    else {
                        symbol = symbolTable.getOrdinalByHashCode(this.hashCode);
                        this.nameSymbolCache = ((long)symbol << 32 | (long)symbolTableIdentity);
                    }
                }
            }
            else {
                symbol = -1;
            }
            if (symbol != -1) {
                jsonWriter.writeSymbol(-symbol);
            }
            else {
                jsonWriter.writeNameRaw(this.nameJSONB, this.hashCode);
            }
            jsonWriter.writeInt32(ordinal);
            return;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeString(e.name());
    }
    
    private boolean writeSymbolNameOrdinal(final JSONWriter jsonWriter, final int ordinal, final SymbolTable symbolTable) {
        final int symbolTableIdentity = System.identityHashCode(symbolTable);
        final long enumNameCache = this.hashCodesSymbolCache[ordinal];
        int enumSymbol;
        if (enumNameCache == 0L) {
            enumSymbol = symbolTable.getOrdinalByHashCode(this.hashCodes[ordinal]);
            this.hashCodesSymbolCache[ordinal] = ((long)enumSymbol << 32 | (long)symbolTableIdentity);
        }
        else {
            final int identity = (int)enumNameCache;
            if (identity == symbolTableIdentity) {
                enumSymbol = (int)(enumNameCache >> 32);
            }
            else {
                enumSymbol = symbolTable.getOrdinalByHashCode(this.hashCodes[ordinal]);
                this.hashCodesSymbolCache[ordinal] = ((long)enumSymbol << 32 | (long)symbolTableIdentity);
            }
        }
        final int namingOrdinal = enumSymbol;
        if (namingOrdinal >= 0) {
            int symbol;
            if (this.nameSymbolCache == 0L) {
                symbol = symbolTable.getOrdinalByHashCode(this.hashCode);
                if (symbol != -1) {
                    this.nameSymbolCache = ((long)symbol << 32 | (long)symbolTableIdentity);
                }
            }
            else {
                final int identity2 = (int)this.nameSymbolCache;
                if (identity2 == symbolTableIdentity) {
                    symbol = (int)(this.nameSymbolCache >> 32);
                }
                else {
                    symbol = symbolTable.getOrdinalByHashCode(this.hashCode);
                    this.nameSymbolCache = ((long)symbol << 32 | (long)symbolTableIdentity);
                }
            }
            if (symbol != -1) {
                jsonWriter.writeSymbol(-symbol);
            }
            else {
                jsonWriter.writeNameRaw(this.nameJSONB, this.hashCode);
            }
            jsonWriter.writeRaw((byte)121);
            jsonWriter.writeInt32(-namingOrdinal);
            return true;
        }
        return false;
    }
    
    private void writeJSONBToString(final JSONWriter jsonWriter, final Enum e, final SymbolTable symbolTable) {
        int symbol;
        if (symbolTable != null) {
            final int symbolTableIdentity = System.identityHashCode(symbolTable);
            if (this.nameSymbolCache == 0L) {
                symbol = symbolTable.getOrdinalByHashCode(this.hashCode);
                this.nameSymbolCache = ((long)symbol << 32 | (long)symbolTableIdentity);
            }
            else {
                final int identity = (int)this.nameSymbolCache;
                if (identity == symbolTableIdentity) {
                    symbol = (int)(this.nameSymbolCache >> 32);
                }
                else {
                    symbol = symbolTable.getOrdinalByHashCode(this.hashCode);
                    this.nameSymbolCache = ((long)symbol << 32 | (long)symbolTableIdentity);
                }
            }
        }
        else {
            symbol = -1;
        }
        if (symbol != -1) {
            jsonWriter.writeSymbol(-symbol);
        }
        else {
            jsonWriter.writeNameRaw(this.nameJSONB, this.hashCode);
        }
        jsonWriter.writeString(e.toString());
    }
    
    @Override
    public final void writeEnum(final JSONWriter jsonWriter, final Enum e) {
        final long features = jsonWriter.getFeatures(this.features);
        if ((features & JSONWriter.Feature.WriteEnumUsingToString.mask) == 0x0L) {
            if (jsonWriter.jsonb) {
                this.writeEnumJSONB(jsonWriter, e);
                return;
            }
            final boolean unquoteName = (features & JSONWriter.Feature.UnquoteFieldName.mask) != 0x0L;
            final boolean utf8 = jsonWriter.utf8;
            final boolean utf9 = !jsonWriter.utf8 && jsonWriter.utf16;
            final int ordinal = e.ordinal();
            if ((features & JSONWriter.Feature.WriteEnumUsingOrdinal.mask) != 0x0L) {
                if (!unquoteName) {
                    if (utf8) {
                        byte[] bytes = this.utf8ValueCache[ordinal];
                        if (bytes == null) {
                            bytes = (this.utf8ValueCache[ordinal] = this.getBytes(ordinal));
                        }
                        jsonWriter.writeNameRaw(bytes);
                        return;
                    }
                    if (utf9) {
                        char[] chars = this.utf16ValueCache[ordinal];
                        if (chars == null) {
                            chars = (this.utf16ValueCache[ordinal] = this.getChars(ordinal));
                        }
                        jsonWriter.writeNameRaw(chars);
                        return;
                    }
                }
                this.writeFieldName(jsonWriter);
                jsonWriter.writeInt32(ordinal);
                return;
            }
            if (!unquoteName) {
                if (utf8) {
                    byte[] bytes = this.valueNameCacheUTF8[ordinal];
                    if (bytes == null) {
                        bytes = (this.valueNameCacheUTF8[ordinal] = this.getNameBytes(ordinal));
                    }
                    jsonWriter.writeNameRaw(bytes);
                    return;
                }
                if (utf9) {
                    char[] chars = this.valueNameCacheUTF16[ordinal];
                    if (chars == null) {
                        chars = (this.valueNameCacheUTF16[ordinal] = this.getNameChars(ordinal));
                    }
                    jsonWriter.writeNameRaw(chars);
                    return;
                }
            }
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeString(e.toString());
    }
    
    private char[] getNameChars(final int ordinal) {
        final String name = this.enumConstants[ordinal].name();
        final char[] chars = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + name.length() + 2);
        chars[this.nameWithColonUTF16.length] = '\"';
        name.getChars(0, name.length(), chars, this.nameWithColonUTF16.length + 1);
        chars[chars.length - 1] = '\"';
        return chars;
    }
    
    private byte[] getNameBytes(final int ordinal) {
        final byte[] nameUft8Bytes = this.enumConstants[ordinal].name().getBytes(StandardCharsets.UTF_8);
        final byte[] bytes = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + nameUft8Bytes.length + 2);
        bytes[this.nameWithColonUTF8.length] = 34;
        int index = this.nameWithColonUTF8.length + 1;
        for (final byte b : nameUft8Bytes) {
            bytes[index++] = b;
        }
        bytes[bytes.length - 1] = 34;
        return bytes;
    }
    
    private char[] getChars(final int ordinal) {
        final int size = IOUtils.stringSize(ordinal);
        final char[] original = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + size);
        final char[] chars = Arrays.copyOf(original, original.length);
        IOUtils.getChars(ordinal, chars.length, chars);
        return chars;
    }
    
    private byte[] getBytes(final int ordinal) {
        final int size = IOUtils.stringSize(ordinal);
        final byte[] original = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + size);
        final byte[] bytes = Arrays.copyOf(original, original.length);
        IOUtils.getChars(ordinal, bytes.length, bytes);
        return bytes;
    }
    
    @Override
    public final void writeValue(final JSONWriter jsonWriter, final Object object) {
        final Enum value = (Enum)this.getFieldValue(object);
        jsonWriter.writeEnum(value);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final Object object) {
        final Enum value = (Enum)this.getFieldValue(object);
        if (value != null) {
            if (jsonWriter.jsonb) {
                this.writeEnumJSONB(jsonWriter, value);
            }
            else {
                this.writeEnum(jsonWriter, value);
            }
            return true;
        }
        final long features = this.features | jsonWriter.getFeatures();
        if ((features & JSONWriter.Feature.WriteNulls.mask) != 0x0L) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeNull();
            return true;
        }
        return false;
    }
}
