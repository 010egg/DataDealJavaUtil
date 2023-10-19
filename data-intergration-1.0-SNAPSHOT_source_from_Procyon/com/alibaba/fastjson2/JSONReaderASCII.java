// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.Map;
import java.util.Arrays;
import com.alibaba.fastjson2.util.TypeUtils;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.util.JDKUtils;

class JSONReaderASCII extends JSONReaderUTF8
{
    final String str;
    
    JSONReaderASCII(final Context ctx, final String str, final byte[] bytes, final int offset, final int length) {
        super(ctx, str, bytes, offset, length);
        this.str = str;
        this.nameAscii = true;
    }
    
    @Override
    public final void next() {
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return;
        }
        byte[] bytes;
        byte ch;
        for (bytes = this.bytes, ch = bytes[offset]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset]) {
            if (++offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return;
            }
        }
        this.offset = offset + 1;
        this.ch = (char)(ch & 0xFF);
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
    }
    
    @Override
    public final boolean nextIfObjectStart() {
        int ch = this.ch;
        if (ch != 123) {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        byte[] bytes;
        for (bytes = this.bytes, ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        this.ch = (char)(ch & 0xFF);
        this.offset = offset;
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfObjectEnd() {
        int ch = this.ch;
        if (ch == 93 || ch == 26) {
            throw new JSONException(this.info("Illegal syntax: `" + (char)ch + '`'));
        }
        if (ch != 125) {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        byte[] bytes;
        for (bytes = this.bytes, ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        if (ch == 44) {
            this.comma = true;
            for (ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
                if (offset >= this.end) {
                    this.ch = '\u001a';
                    this.offset = offset;
                    return true;
                }
            }
        }
        this.ch = (char)(ch & 0xFF);
        this.offset = offset;
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfComma() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        int ch = this.ch;
        if (ch != 44) {
            return false;
        }
        this.comma = true;
        if (offset >= this.end) {
            this.offset = offset;
            this.ch = '\u001a';
            return true;
        }
        for (ch = bytes[offset]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset]) {
            if (++offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return true;
            }
        }
        this.offset = offset + 1;
        this.ch = (char)(ch & 0xFF);
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfMatch(final char m) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        int ch = this.ch;
        while (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L) {
            if (offset >= this.end) {
                ch = 26;
            }
            else {
                ch = bytes[offset++];
            }
        }
        if (ch != m) {
            return false;
        }
        this.comma = (m == ',');
        if (offset >= this.end) {
            this.offset = offset;
            this.ch = '\u001a';
            return true;
        }
        for (ch = bytes[offset]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset]) {
            if (++offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return true;
            }
        }
        this.offset = offset + 1;
        this.ch = (char)(ch & 0xFF);
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfArrayStart() {
        int ch = this.ch;
        if (ch != 91) {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        byte[] bytes;
        for (bytes = this.bytes, ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        this.ch = (char)(ch & 0xFF);
        this.offset = offset;
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfArrayEnd() {
        int ch = this.ch;
        if (ch == 125 || ch == 26) {
            throw new JSONException(this.info("Illegal syntax: `" + (char)ch + '`'));
        }
        if (ch != 93) {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        byte[] bytes;
        for (bytes = this.bytes, ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        if (ch == 44) {
            this.comma = true;
            for (ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
                if (offset >= this.end) {
                    this.ch = '\u001a';
                    this.offset = offset;
                    return true;
                }
            }
        }
        this.ch = (char)(ch & 0xFF);
        this.offset = offset;
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfNullOrEmptyString() {
        final char first = this.ch;
        final int end = this.end;
        int offset = this.offset;
        final byte[] bytes = this.bytes;
        if (first == 'n' && offset + 2 < end && bytes[offset] == 117 && bytes[offset + 1] == 108 && bytes[offset + 2] == 108) {
            offset += 3;
        }
        else {
            if ((first != '\"' && first != '\'') || offset >= end || bytes[offset] != first) {
                return false;
            }
            ++offset;
        }
        char ch;
        for (ch = ((offset == end) ? '\u001a' : ((char)bytes[offset])); ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = (char)bytes[offset]) {
            if (++offset >= end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        if (this.comma = (ch == ',')) {
            if (++offset >= end) {
                ch = '\u001a';
            }
            else {
                ch = (char)bytes[offset];
            }
        }
        if (offset >= end) {
            this.ch = '\u001a';
            this.offset = offset;
            return true;
        }
        while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
            if (++offset >= end) {
                this.ch = '\u001a';
                return true;
            }
            ch = (char)bytes[offset];
        }
        this.offset = offset + 1;
        this.ch = ch;
        return true;
    }
    
    @Override
    public final long readFieldNameHashCode() {
        final byte[] bytes = this.bytes;
        if (this.ch != '\"' && this.ch != '\'') {
            if ((this.context.features & Feature.AllowUnQuotedFieldNames.mask) != 0x0L && JSONReader.isFirstIdentifier(this.ch)) {
                return this.readFieldNameHashCodeUnquote();
            }
            if (this.ch == '}' || this.isNull()) {
                return -1L;
            }
            final String preFieldName;
            String errorMsg;
            if (this.ch == '[' && this.nameBegin > 0 && (preFieldName = this.getFieldName()) != null) {
                errorMsg = "illegal fieldName input " + this.ch + ", previous fieldName " + preFieldName;
            }
            else {
                errorMsg = "illegal fieldName input" + this.ch;
            }
            throw new JSONException(this.info(errorMsg));
        }
        else {
            final char quote = this.ch;
            this.stringValue = null;
            this.nameEscape = false;
            final int offset2 = this.offset;
            this.nameBegin = offset2;
            int offset = offset2;
            long nameValue = 0L;
            if (offset + 9 < this.end) {
                final byte c0;
                if ((c0 = bytes[offset]) == quote) {
                    nameValue = 0L;
                }
                else {
                    final byte c2;
                    if ((c2 = bytes[offset + 1]) == quote && c0 != 0 && c0 != 92) {
                        nameValue = c0;
                        this.nameLength = 1;
                        this.nameEnd = offset + 1;
                        offset += 2;
                    }
                    else {
                        final byte c3;
                        if ((c3 = bytes[offset + 2]) == quote && c2 != 0 && c0 != 92 && c2 != 92) {
                            nameValue = (c2 << 8) + (c0 & 0xFF);
                            this.nameLength = 2;
                            this.nameEnd = offset + 2;
                            offset += 3;
                        }
                        else {
                            final byte c4;
                            if ((c4 = bytes[offset + 3]) == quote && c3 != 0 && c0 != 92 && c2 != 92 && c3 != 92) {
                                nameValue = (c3 << 16) + ((c2 & 0xFF) << 8) + (c0 & 0xFF);
                                this.nameLength = 3;
                                this.nameEnd = offset + 3;
                                offset += 4;
                            }
                            else {
                                final byte c5;
                                if ((c5 = bytes[offset + 4]) == quote && c4 != 0 && c0 != 92 && c2 != 92 && c3 != 92 && c4 != 92) {
                                    nameValue = (c4 << 24) + ((c3 & 0xFF) << 16) + ((c2 & 0xFF) << 8) + (c0 & 0xFF);
                                    this.nameLength = 4;
                                    this.nameEnd = offset + 4;
                                    offset += 5;
                                }
                                else {
                                    final byte c6;
                                    if ((c6 = bytes[offset + 5]) == quote && c5 != 0 && c0 != 92 && c2 != 92 && c3 != 92 && c4 != 92 && c5 != 92) {
                                        nameValue = ((long)c5 << 32) + (((long)c4 & 0xFFL) << 24) + (((long)c3 & 0xFFL) << 16) + (((long)c2 & 0xFFL) << 8) + ((long)c0 & 0xFFL);
                                        this.nameLength = 5;
                                        this.nameEnd = offset + 5;
                                        offset += 6;
                                    }
                                    else {
                                        final byte c7;
                                        if ((c7 = bytes[offset + 6]) == quote && c6 != 0 && c0 != 92 && c2 != 92 && c3 != 92 && c4 != 92 && c5 != 92 && c6 != 92) {
                                            nameValue = ((long)c6 << 40) + (((long)c5 & 0xFFL) << 32) + (((long)c4 & 0xFFL) << 24) + (((long)c3 & 0xFFL) << 16) + (((long)c2 & 0xFFL) << 8) + ((long)c0 & 0xFFL);
                                            this.nameLength = 6;
                                            this.nameEnd = offset + 6;
                                            offset += 7;
                                        }
                                        else {
                                            final byte c8;
                                            if ((c8 = bytes[offset + 7]) == quote && c7 != 0 && c0 != 92 && c2 != 92 && c3 != 92 && c4 != 92 && c5 != 92 && c6 != 92 && c7 != 92) {
                                                nameValue = ((long)c7 << 48) + (((long)c6 & 0xFFL) << 40) + (((long)c5 & 0xFFL) << 32) + (((long)c4 & 0xFFL) << 24) + (((long)c3 & 0xFFL) << 16) + (((long)c2 & 0xFFL) << 8) + ((long)c0 & 0xFFL);
                                                this.nameLength = 7;
                                                this.nameEnd = offset + 7;
                                                offset += 8;
                                            }
                                            else if (bytes[offset + 8] == quote && c8 != 0 && c0 != 92 && c2 != 92 && c3 != 92 && c4 != 92 && c5 != 92 && c6 != 92 && c7 != 92 && c8 != 92) {
                                                nameValue = getLong(bytes, offset);
                                                this.nameLength = 8;
                                                this.nameEnd = offset + 8;
                                                offset += 9;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (nameValue == 0L) {
                int i = 0;
                while (offset < this.end) {
                    int c9 = bytes[offset];
                    if (c9 == quote) {
                        if (i == 0) {
                            offset = this.nameBegin;
                            break;
                        }
                        this.nameLength = i;
                        this.nameEnd = offset;
                        ++offset;
                        break;
                    }
                    else {
                        if (c9 == 92) {
                            this.nameEscape = true;
                            c9 = bytes[offset + 1];
                            switch (c9) {
                                case 117: {
                                    final byte c10 = bytes[offset + 2];
                                    final byte c11 = bytes[offset + 3];
                                    final byte c12 = bytes[offset + 4];
                                    final byte c13 = bytes[offset + 5];
                                    c9 = JSONReader.char4(c10, c11, c12, c13);
                                    offset += 5;
                                    break;
                                }
                                case 120: {
                                    final byte c10 = bytes[offset + 2];
                                    final byte c11 = bytes[offset + 3];
                                    c9 = JSONReader.char2(c10, c11);
                                    offset += 3;
                                    break;
                                }
                                default: {
                                    c9 = this.char1(c9);
                                    ++offset;
                                    break;
                                }
                            }
                        }
                        if (c9 > 255 || i >= 8 || (i == 0 && c9 == 0)) {
                            nameValue = 0L;
                            offset = this.nameBegin;
                            break;
                        }
                        switch (i) {
                            case 0: {
                                nameValue = (byte)c9;
                                break;
                            }
                            case 1: {
                                nameValue = ((byte)c9 << 8) + (nameValue & 0xFFL);
                                break;
                            }
                            case 2: {
                                nameValue = ((byte)c9 << 16) + (nameValue & 0xFFFFL);
                                break;
                            }
                            case 3: {
                                nameValue = ((byte)c9 << 24) + (nameValue & 0xFFFFFFL);
                                break;
                            }
                            case 4: {
                                nameValue = ((long)(byte)c9 << 32) + (nameValue & 0xFFFFFFFFL);
                                break;
                            }
                            case 5: {
                                nameValue = ((long)(byte)c9 << 40) + (nameValue & 0xFFFFFFFFFFL);
                                break;
                            }
                            case 6: {
                                nameValue = ((long)(byte)c9 << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                                break;
                            }
                            case 7: {
                                nameValue = ((long)(byte)c9 << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                                break;
                            }
                        }
                        ++offset;
                        ++i;
                    }
                }
            }
            long hashCode;
            if (nameValue != 0L) {
                hashCode = nameValue;
            }
            else {
                hashCode = -3750763034362895579L;
                int j = 0;
                while (true) {
                    int c14 = bytes[offset];
                    if (c14 == 92) {
                        this.nameEscape = true;
                        c14 = bytes[++offset];
                        switch (c14) {
                            case 117: {
                                final byte c15 = bytes[++offset];
                                final byte c16 = bytes[++offset];
                                final byte c17 = bytes[++offset];
                                final byte c18 = bytes[++offset];
                                c14 = JSONReader.char4(c15, c16, c17, c18);
                                break;
                            }
                            case 120: {
                                final byte c15 = bytes[++offset];
                                final byte c16 = bytes[++offset];
                                c14 = JSONReader.char2(c15, c16);
                                break;
                            }
                            default: {
                                c14 = this.char1(c14);
                                break;
                            }
                        }
                        ++offset;
                        hashCode ^= c14;
                        hashCode *= 1099511628211L;
                    }
                    else {
                        if (c14 == quote) {
                            break;
                        }
                        ++offset;
                        hashCode ^= (c14 & 0xFF);
                        hashCode *= 1099511628211L;
                    }
                    ++j;
                }
                this.nameLength = j;
                this.nameEnd = offset;
                ++offset;
            }
            byte c19;
            if (offset < this.end) {
                for (c19 = bytes[offset]; c19 <= 32 && (1L << c19 & 0x100003700L) != 0x0L; c19 = bytes[offset]) {
                    ++offset;
                }
            }
            else {
                c19 = 26;
            }
            if (c19 != 58) {
                throw new JSONException(this.info("expect ':', but " + c19));
            }
            if (++offset == this.end) {
                c19 = 26;
            }
            else {
                c19 = bytes[offset];
            }
            while (c19 <= 32 && (1L << c19 & 0x100003700L) != 0x0L) {
                ++offset;
                c19 = bytes[offset];
            }
            this.offset = offset + 1;
            this.ch = (char)c19;
            return hashCode;
        }
    }
    
    public static int getInt(final byte[] bytes, final int off) {
        return JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off);
    }
    
    public static long getLong(final byte[] bytes, final int off) {
        return JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off);
    }
    
    @Override
    public final long readValueHashCode() {
        if (this.ch != '\"' && this.ch != '\'') {
            return -1L;
        }
        final char quote = this.ch;
        this.nameEscape = false;
        final int offset2 = this.offset;
        this.nameBegin = offset2;
        int offset = offset2;
        long nameValue = 0L;
        int i = 0;
        while (offset < this.end) {
            int c = this.bytes[offset];
            if (c == quote) {
                if (i == 0) {
                    nameValue = 0L;
                    offset = this.nameBegin;
                    break;
                }
                this.nameLength = i;
                this.nameEnd = offset;
                ++offset;
                break;
            }
            else {
                if (c == 92) {
                    this.nameEscape = true;
                    c = this.bytes[++offset];
                    switch (c) {
                        case 117: {
                            final byte c2 = this.bytes[++offset];
                            final byte c3 = this.bytes[++offset];
                            final byte c4 = this.bytes[++offset];
                            final byte c5 = this.bytes[++offset];
                            c = JSONReader.char4(c2, c3, c4, c5);
                            break;
                        }
                        case 120: {
                            final byte c2 = this.bytes[++offset];
                            final byte c3 = this.bytes[++offset];
                            c = JSONReader.char2(c2, c3);
                            break;
                        }
                        default: {
                            c = this.char1(c);
                            break;
                        }
                    }
                }
                if (c > 255 || i >= 8 || (i == 0 && c == 0)) {
                    nameValue = 0L;
                    offset = this.nameBegin;
                    break;
                }
                switch (i) {
                    case 0: {
                        nameValue = (byte)c;
                        break;
                    }
                    case 1: {
                        nameValue = ((byte)c << 8) + (nameValue & 0xFFL);
                        break;
                    }
                    case 2: {
                        nameValue = ((byte)c << 16) + (nameValue & 0xFFFFL);
                        break;
                    }
                    case 3: {
                        nameValue = ((byte)c << 24) + (nameValue & 0xFFFFFFL);
                        break;
                    }
                    case 4: {
                        nameValue = ((long)(byte)c << 32) + (nameValue & 0xFFFFFFFFL);
                        break;
                    }
                    case 5: {
                        nameValue = ((long)(byte)c << 40) + (nameValue & 0xFFFFFFFFFFL);
                        break;
                    }
                    case 6: {
                        nameValue = ((long)(byte)c << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                        break;
                    }
                    case 7: {
                        nameValue = ((long)(byte)c << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                        break;
                    }
                }
                ++offset;
                ++i;
            }
        }
        long hashCode;
        if (nameValue != 0L) {
            hashCode = nameValue;
        }
        else {
            hashCode = -3750763034362895579L;
            int j = 0;
            while (true) {
                int c6 = this.bytes[offset];
                if (c6 == 92) {
                    this.nameEscape = true;
                    c6 = this.bytes[++offset];
                    switch (c6) {
                        case 117: {
                            final byte c7 = this.bytes[++offset];
                            final byte c8 = this.bytes[++offset];
                            final byte c9 = this.bytes[++offset];
                            final byte c10 = this.bytes[++offset];
                            c6 = JSONReader.char4(c7, c8, c9, c10);
                            break;
                        }
                        case 120: {
                            final byte c7 = this.bytes[++offset];
                            final byte c8 = this.bytes[++offset];
                            c6 = JSONReader.char2(c7, c8);
                            break;
                        }
                        default: {
                            c6 = this.char1(c6);
                            break;
                        }
                    }
                    ++offset;
                    hashCode ^= c6;
                    hashCode *= 1099511628211L;
                }
                else {
                    if (c6 == 34) {
                        break;
                    }
                    ++offset;
                    hashCode ^= ((c6 < 0) ? (c6 & 0xFF) : ((long)c6));
                    hashCode *= 1099511628211L;
                }
                ++j;
            }
            this.nameLength = j;
            this.nameEnd = offset;
            this.stringValue = null;
            ++offset;
        }
        byte c11;
        if (offset == this.end) {
            c11 = 26;
        }
        else {
            c11 = this.bytes[offset];
        }
        while (c11 <= 32 && (1L << c11 & 0x100003700L) != 0x0L) {
            ++offset;
            c11 = this.bytes[offset];
        }
        if (this.comma = (c11 == 44)) {
            if (++offset == this.end) {
                c11 = 26;
            }
            else {
                c11 = this.bytes[offset];
            }
            while (c11 <= 32 && (1L << c11 & 0x100003700L) != 0x0L) {
                ++offset;
                c11 = this.bytes[offset];
            }
        }
        this.offset = offset + 1;
        this.ch = (char)c11;
        return hashCode;
    }
    
    @Override
    public final long getNameHashCodeLCase() {
        int offset = this.nameBegin;
        long nameValue = 0L;
        int i = 0;
        while (offset < this.end) {
            int c = this.bytes[offset];
            if (c == 92) {
                c = this.bytes[++offset];
                switch (c) {
                    case 117: {
                        final int c2 = this.bytes[++offset];
                        final int c3 = this.bytes[++offset];
                        final int c4 = this.bytes[++offset];
                        final int c5 = this.bytes[++offset];
                        c = JSONReader.char4(c2, c3, c4, c5);
                        break;
                    }
                    case 120: {
                        final int c2 = this.bytes[++offset];
                        final int c3 = this.bytes[++offset];
                        c = JSONReader.char2(c2, c3);
                        break;
                    }
                    default: {
                        c = this.char1(c);
                        break;
                    }
                }
            }
            else if (c == 34) {
                break;
            }
            if (c > 255 || c < 0 || i >= 8 || (i == 0 && c == 0)) {
                nameValue = 0L;
                offset = this.nameBegin;
                break;
            }
            Label_0498: {
                if (c == 95 || c == 45 || c == 32) {
                    final byte c6 = this.bytes[offset + 1];
                    if (c6 != 34 && c6 != 39 && c6 != c) {
                        break Label_0498;
                    }
                }
                if (c >= 65 && c <= 90) {
                    c = (char)(c + 32);
                }
                switch (i) {
                    case 0: {
                        nameValue = (byte)c;
                        break;
                    }
                    case 1: {
                        nameValue = ((byte)c << 8) + (nameValue & 0xFFL);
                        break;
                    }
                    case 2: {
                        nameValue = ((byte)c << 16) + (nameValue & 0xFFFFL);
                        break;
                    }
                    case 3: {
                        nameValue = ((byte)c << 24) + (nameValue & 0xFFFFFFL);
                        break;
                    }
                    case 4: {
                        nameValue = ((long)(byte)c << 32) + (nameValue & 0xFFFFFFFFL);
                        break;
                    }
                    case 5: {
                        nameValue = ((long)(byte)c << 40) + (nameValue & 0xFFFFFFFFFFL);
                        break;
                    }
                    case 6: {
                        nameValue = ((long)(byte)c << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                        break;
                    }
                    case 7: {
                        nameValue = ((long)(byte)c << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                        break;
                    }
                }
                ++i;
            }
            ++offset;
        }
        if (nameValue != 0L) {
            return nameValue;
        }
        long hashCode = -3750763034362895579L;
        while (offset < this.end) {
            int c7 = this.bytes[offset];
            if (c7 == 92) {
                c7 = this.bytes[++offset];
                switch (c7) {
                    case 117: {
                        final int c8 = this.bytes[++offset];
                        final int c9 = this.bytes[++offset];
                        final int c10 = this.bytes[++offset];
                        final int c11 = this.bytes[++offset];
                        c7 = JSONReader.char4(c8, c9, c10, c11);
                        break;
                    }
                    case 120: {
                        final int c8 = this.bytes[++offset];
                        final int c9 = this.bytes[++offset];
                        c7 = JSONReader.char2(c8, c9);
                        break;
                    }
                    default: {
                        c7 = this.char1(c7);
                        break;
                    }
                }
            }
            else if (c7 == 34) {
                break;
            }
            ++offset;
            if (c7 == 95 || c7 == 45 || c7 == 32) {
                final byte c12 = this.bytes[offset];
                if (c12 != 34 && c12 != 39 && c12 != c7) {
                    continue;
                }
            }
            if (c7 >= 65 && c7 <= 90) {
                c7 = (char)(c7 + 32);
            }
            hashCode ^= ((c7 < 0) ? (c7 & 0xFF) : ((long)c7));
            hashCode *= 1099511628211L;
        }
        return hashCode;
    }
    
    @Override
    public final String getFieldName() {
        final int length = this.nameEnd - this.nameBegin;
        if (this.nameEscape) {
            if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                byte[] chars = new byte[this.nameLength];
            Label_0410:
                for (int offset = this.nameBegin, i = 0; offset < this.nameEnd; ++offset, ++i) {
                    byte b = this.bytes[offset];
                    if (b == 92) {
                        b = this.bytes[++offset];
                        switch (b) {
                            case 117: {
                                final int c1 = this.bytes[++offset];
                                final int c2 = this.bytes[++offset];
                                final int c3 = this.bytes[++offset];
                                final int c4 = this.bytes[++offset];
                                final char c5 = JSONReader.char4(c1, c2, c3, c4);
                                if (c5 > '\u00ff') {
                                    chars = null;
                                    break Label_0410;
                                }
                                b = (byte)c5;
                                break;
                            }
                            case 120: {
                                final int c1 = this.bytes[++offset];
                                final int c2 = this.bytes[++offset];
                                final char c6 = JSONReader.char2(c1, c2);
                                if (c6 > '\u00ff') {
                                    chars = null;
                                    break Label_0410;
                                }
                                b = (byte)c6;
                                break;
                            }
                            case 34:
                            case 42:
                            case 43:
                            case 45:
                            case 46:
                            case 47:
                            case 58:
                            case 60:
                            case 61:
                            case 62:
                            case 64:
                            case 92: {
                                break;
                            }
                            default: {
                                b = (byte)this.char1(b);
                                break;
                            }
                        }
                    }
                    else if (b == 34) {
                        break;
                    }
                    chars[i] = b;
                }
                if (chars != null) {
                    return JDKUtils.STRING_CREATOR_JDK11.apply(chars, JDKUtils.LATIN1);
                }
            }
            final char[] chars2 = new char[this.nameLength];
            for (int offset = this.nameBegin, i = 0; offset < this.nameEnd; ++offset, ++i) {
                char c7 = (char)(this.bytes[offset] & 0xFF);
                if (c7 == '\\') {
                    c7 = (char)this.bytes[++offset];
                    switch (c7) {
                        case 'u': {
                            final int c1 = this.bytes[++offset];
                            final int c2 = this.bytes[++offset];
                            final int c3 = this.bytes[++offset];
                            final int c4 = this.bytes[++offset];
                            c7 = JSONReader.char4(c1, c2, c3, c4);
                            break;
                        }
                        case 'x': {
                            final int c1 = this.bytes[++offset];
                            final int c2 = this.bytes[++offset];
                            c7 = JSONReader.char2(c1, c2);
                            break;
                        }
                        case '*':
                        case '+':
                        case '-':
                        case '.':
                        case '/':
                        case '<':
                        case '=':
                        case '>':
                        case '@': {
                            break;
                        }
                        default: {
                            c7 = this.char1(c7);
                            break;
                        }
                    }
                }
                else if (c7 == '\"') {
                    break;
                }
                chars2[i] = c7;
            }
            return new String(chars2);
        }
        if (this.str != null) {
            return this.str.substring(this.nameBegin, this.nameEnd);
        }
        return new String(this.bytes, this.nameBegin, length, StandardCharsets.ISO_8859_1);
    }
    
    @Override
    public final String readFieldName() {
        if (this.ch != '\"' && this.ch != '\'') {
            if ((this.context.features & Feature.AllowUnQuotedFieldNames.mask) != 0x0L && JSONReader.isFirstIdentifier(this.ch)) {
                return this.readFieldNameUnquote();
            }
            return null;
        }
        else {
            final char quote = this.ch;
            this.nameEscape = false;
            final int offset2 = this.offset;
            this.nameBegin = offset2;
            int offset = offset2;
            int i = 0;
            while (offset < this.end) {
                int c = this.bytes[offset];
                if (c == 92) {
                    this.nameEscape = true;
                    c = this.bytes[++offset];
                    switch (c) {
                        case 117: {
                            offset += 4;
                            break;
                        }
                        case 120: {
                            offset += 2;
                            break;
                        }
                    }
                    ++offset;
                }
                else if (c == quote) {
                    this.nameLength = i;
                    this.nameEnd = offset;
                    ++offset;
                    for (c = this.bytes[offset]; c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = this.bytes[offset]) {
                        ++offset;
                    }
                    if (c != 58) {
                        throw new JSONException("syntax error : " + offset);
                    }
                    if (++offset >= this.end) {
                        this.ch = '\u001a';
                        throw new JSONException("syntax error : " + offset);
                    }
                    for (c = this.bytes[offset]; c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = this.bytes[offset]) {
                        ++offset;
                    }
                    this.offset = offset + 1;
                    this.ch = (char)c;
                    break;
                }
                else {
                    ++offset;
                }
                ++i;
            }
            if (this.nameEnd < this.nameBegin) {
                throw new JSONException("syntax error : " + offset);
            }
            if (!this.nameEscape) {
                long nameValue0 = -1L;
                long nameValue2 = -1L;
                final int length = this.nameEnd - this.nameBegin;
                switch (length) {
                    case 1: {
                        return TypeUtils.toString(this.bytes[this.nameBegin]);
                    }
                    case 2: {
                        return TypeUtils.toString(this.bytes[this.nameBegin], this.bytes[this.nameBegin + 1]);
                    }
                    case 3: {
                        nameValue0 = (this.bytes[this.nameBegin + 2] << 16) + ((this.bytes[this.nameBegin + 1] & 0xFF) << 8) + (this.bytes[this.nameBegin] & 0xFF);
                        break;
                    }
                    case 4: {
                        nameValue0 = (this.bytes[this.nameBegin + 3] << 24) + ((this.bytes[this.nameBegin + 2] & 0xFF) << 16) + ((this.bytes[this.nameBegin + 1] & 0xFF) << 8) + (this.bytes[this.nameBegin] & 0xFF);
                        break;
                    }
                    case 5: {
                        nameValue0 = ((long)this.bytes[this.nameBegin + 4] << 32) + (((long)this.bytes[this.nameBegin + 3] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 2] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 1] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin] & 0xFFL);
                        break;
                    }
                    case 6: {
                        nameValue0 = ((long)this.bytes[this.nameBegin + 5] << 40) + (((long)this.bytes[this.nameBegin + 4] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 3] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 2] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 1] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin] & 0xFFL);
                        break;
                    }
                    case 7: {
                        nameValue0 = ((long)this.bytes[this.nameBegin + 6] << 48) + (((long)this.bytes[this.nameBegin + 5] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 4] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 3] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 2] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 1] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin] & 0xFFL);
                        break;
                    }
                    case 8: {
                        nameValue0 = ((long)this.bytes[this.nameBegin + 7] << 56) + (((long)this.bytes[this.nameBegin + 6] & 0xFFL) << 48) + (((long)this.bytes[this.nameBegin + 5] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 4] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 3] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 2] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 1] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin] & 0xFFL);
                        break;
                    }
                    case 9: {
                        nameValue0 = this.bytes[this.nameBegin];
                        nameValue2 = ((long)this.bytes[this.nameBegin + 8] << 56) + (((long)this.bytes[this.nameBegin + 7] & 0xFFL) << 48) + (((long)this.bytes[this.nameBegin + 6] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 5] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 4] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 3] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 2] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin + 1] & 0xFFL);
                        break;
                    }
                    case 10: {
                        nameValue0 = (this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                        nameValue2 = ((long)this.bytes[this.nameBegin + 9] << 56) + (((long)this.bytes[this.nameBegin + 8] & 0xFFL) << 48) + (((long)this.bytes[this.nameBegin + 7] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 6] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 5] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 4] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 3] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin + 2] & 0xFFL);
                        break;
                    }
                    case 11: {
                        nameValue0 = (this.bytes[this.nameBegin + 2] << 16) + (this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                        nameValue2 = ((long)this.bytes[this.nameBegin + 10] << 56) + (((long)this.bytes[this.nameBegin + 9] & 0xFFL) << 48) + (((long)this.bytes[this.nameBegin + 8] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 7] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 6] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 5] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 4] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin + 3] & 0xFFL);
                        break;
                    }
                    case 12: {
                        nameValue0 = (this.bytes[this.nameBegin + 3] << 24) + (this.bytes[this.nameBegin + 2] << 16) + (this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                        nameValue2 = ((long)this.bytes[this.nameBegin + 11] << 56) + (((long)this.bytes[this.nameBegin + 10] & 0xFFL) << 48) + (((long)this.bytes[this.nameBegin + 9] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 8] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 7] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 6] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 5] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin + 4] & 0xFFL);
                        break;
                    }
                    case 13: {
                        nameValue0 = ((long)this.bytes[this.nameBegin + 4] << 32) + ((long)this.bytes[this.nameBegin + 3] << 24) + ((long)this.bytes[this.nameBegin + 2] << 16) + ((long)this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                        nameValue2 = ((long)this.bytes[this.nameBegin + 12] << 56) + (((long)this.bytes[this.nameBegin + 11] & 0xFFL) << 48) + (((long)this.bytes[this.nameBegin + 10] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 9] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 8] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 7] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 6] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin + 5] & 0xFFL);
                        break;
                    }
                    case 14: {
                        nameValue0 = ((long)this.bytes[this.nameBegin + 5] << 40) + (((long)this.bytes[this.nameBegin + 4] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 3] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 2] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 1] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin] & 0xFFL);
                        nameValue2 = ((long)this.bytes[this.nameBegin + 13] << 56) + (((long)this.bytes[this.nameBegin + 12] & 0xFFL) << 48) + (((long)this.bytes[this.nameBegin + 11] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 10] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 9] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 8] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 7] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin + 6] & 0xFFL);
                        break;
                    }
                    case 15: {
                        nameValue0 = ((long)this.bytes[this.nameBegin + 6] << 48) + (((long)this.bytes[this.nameBegin + 5] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 4] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 3] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 2] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 1] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin] & 0xFFL);
                        nameValue2 = ((long)this.bytes[this.nameBegin + 14] << 56) + (((long)this.bytes[this.nameBegin + 13] & 0xFFL) << 48) + (((long)this.bytes[this.nameBegin + 12] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 11] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 10] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 9] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 8] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin + 7] & 0xFFL);
                        break;
                    }
                    case 16: {
                        nameValue0 = ((long)this.bytes[this.nameBegin + 7] << 56) + (((long)this.bytes[this.nameBegin + 6] & 0xFFL) << 48) + (((long)this.bytes[this.nameBegin + 5] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 4] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 3] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 2] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 1] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin] & 0xFFL);
                        nameValue2 = ((long)this.bytes[this.nameBegin + 15] << 56) + (((long)this.bytes[this.nameBegin + 14] & 0xFFL) << 48) + (((long)this.bytes[this.nameBegin + 13] & 0xFFL) << 40) + (((long)this.bytes[this.nameBegin + 12] & 0xFFL) << 32) + (((long)this.bytes[this.nameBegin + 11] & 0xFFL) << 24) + (((long)this.bytes[this.nameBegin + 10] & 0xFFL) << 16) + (((long)this.bytes[this.nameBegin + 9] & 0xFFL) << 8) + ((long)this.bytes[this.nameBegin + 8] & 0xFFL);
                        break;
                    }
                }
                if (nameValue0 != -1L) {
                    if (nameValue2 != -1L) {
                        final int indexMask = (int)nameValue2 & JSONFactory.NAME_CACHE2.length - 1;
                        final JSONFactory.NameCacheEntry2 entry = JSONFactory.NAME_CACHE2[indexMask];
                        if (entry == null) {
                            final char[] chars = new char[length];
                            for (int j = 0; j < length; ++j) {
                                chars[j] = (char)(this.bytes[this.nameBegin + j] & 0xFF);
                            }
                            String name;
                            if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                                name = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
                            }
                            else {
                                name = new String(chars);
                            }
                            JSONFactory.NAME_CACHE2[indexMask] = new JSONFactory.NameCacheEntry2(name, nameValue0, nameValue2);
                            return name;
                        }
                        if (entry.value0 == nameValue0 && entry.value1 == nameValue2) {
                            return entry.name;
                        }
                    }
                    else {
                        final int indexMask = (int)nameValue0 & JSONFactory.NAME_CACHE.length - 1;
                        final JSONFactory.NameCacheEntry entry2 = JSONFactory.NAME_CACHE[indexMask];
                        if (entry2 == null) {
                            final char[] chars = new char[length];
                            for (int j = 0; j < length; ++j) {
                                chars[j] = (char)(this.bytes[this.nameBegin + j] & 0xFF);
                            }
                            String name;
                            if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                                name = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
                            }
                            else {
                                name = new String(chars);
                            }
                            JSONFactory.NAME_CACHE[indexMask] = new JSONFactory.NameCacheEntry(name, nameValue0);
                            return name;
                        }
                        if (entry2.value == nameValue0) {
                            return entry2.name;
                        }
                    }
                }
            }
            return this.getFieldName();
        }
    }
    
    @Override
    protected final void readString0() {
        final char quote = this.ch;
        final int start = this.offset;
        this.valueEscape = false;
        int i = 0;
        while (true) {
            int c = this.bytes[this.offset];
            if (c == 92) {
                this.valueEscape = true;
                c = this.bytes[++this.offset];
                switch (c) {
                    case 117: {
                        this.offset += 4;
                        break;
                    }
                    case 120: {
                        this.offset += 2;
                        break;
                    }
                }
                ++this.offset;
            }
            else {
                if (c == quote) {
                    break;
                }
                ++this.offset;
            }
            ++i;
        }
        final int valueLength = i;
        String str;
        if (this.valueEscape) {
            final char[] chars = new char[valueLength];
            this.offset = start;
            int j = 0;
            while (true) {
                char c2 = (char)(this.bytes[this.offset] & 0xFF);
                if (c2 == '\\') {
                    c2 = (char)this.bytes[++this.offset];
                    switch (c2) {
                        case 'u': {
                            final byte c3 = this.bytes[++this.offset];
                            final byte c4 = this.bytes[++this.offset];
                            final byte c5 = this.bytes[++this.offset];
                            final byte c6 = this.bytes[++this.offset];
                            c2 = JSONReader.char4(c3, c4, c5, c6);
                            break;
                        }
                        case 'x': {
                            final byte c3 = this.bytes[++this.offset];
                            final byte c4 = this.bytes[++this.offset];
                            c2 = JSONReader.char2(c3, c4);
                            break;
                        }
                        case '\"':
                        case '\\': {
                            break;
                        }
                        default: {
                            c2 = this.char1(c2);
                            break;
                        }
                    }
                }
                else if (c2 == '\"') {
                    break;
                }
                chars[j] = c2;
                ++this.offset;
                ++j;
            }
            str = new String(chars);
        }
        else if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            final byte[] bytes = Arrays.copyOfRange(this.bytes, start, this.offset);
            str = JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
        }
        else {
            str = new String(this.bytes, start, this.offset - start, StandardCharsets.ISO_8859_1);
        }
        int b;
        for (b = this.bytes[++this.offset]; b <= 32 && (1L << b & 0x100003700L) != 0x0L; b = this.bytes[++this.offset]) {}
        final boolean comma = b == 44;
        this.comma = comma;
        if (comma) {
            ++this.offset;
            this.next();
        }
        else {
            ++this.offset;
            this.ch = (char)b;
        }
        this.stringValue = str;
    }
    
    @Override
    public String readString() {
        if (this.ch == '\"' || this.ch == '\'') {
            final byte quote = (byte)this.ch;
            final byte slash = 92;
            final int start;
            int offset = start = this.offset;
            boolean valueEscape = false;
            int i = 0;
            byte c0 = 0;
            byte c2 = 0;
            byte c3 = 0;
            byte c4 = 0;
            boolean quoted = false;
            for (int upperBound = offset + (this.end - offset & 0xFFFFFFFC); offset < upperBound; offset += 4, i += 4) {
                c0 = this.bytes[offset];
                c2 = this.bytes[offset + 1];
                c3 = this.bytes[offset + 2];
                c4 = this.bytes[offset + 3];
                if (c0 == 92 || c2 == 92 || c3 == 92) {
                    break;
                }
                if (c4 == 92) {
                    break;
                }
                if (c0 == quote || c2 == quote || c3 == quote || c4 == quote) {
                    quoted = true;
                    break;
                }
            }
            int valueLength = 0;
            Label_0356: {
                if (!quoted) {
                    while (offset < this.end) {
                        byte c5 = this.bytes[offset];
                        if (c5 == 92) {
                            valueEscape = true;
                            c5 = this.bytes[++offset];
                            switch (c5) {
                                case 117: {
                                    offset += 4;
                                    break;
                                }
                                case 120: {
                                    offset += 2;
                                    break;
                                }
                            }
                            ++offset;
                        }
                        else {
                            if (c5 == quote) {
                                valueLength = i;
                                break Label_0356;
                            }
                            ++offset;
                        }
                        ++i;
                    }
                    throw new JSONException("invalid escape character EOI");
                }
                if (c0 != quote) {
                    if (c2 == quote) {
                        ++offset;
                        ++i;
                    }
                    else if (c3 == quote) {
                        offset += 2;
                        i += 2;
                    }
                    else {
                        offset += 3;
                        i += 3;
                    }
                }
                valueLength = i;
            }
            String str;
            if (valueEscape) {
                final char[] chars = new char[valueLength];
                offset = start;
                int j = 0;
                while (true) {
                    char c6 = (char)(this.bytes[offset] & 0xFF);
                    if (c6 == '\\') {
                        c6 = (char)this.bytes[++offset];
                        switch (c6) {
                            case 'u': {
                                final char c7 = (char)this.bytes[++offset];
                                final char c8 = (char)this.bytes[++offset];
                                final char c9 = (char)this.bytes[++offset];
                                final char c10 = (char)this.bytes[++offset];
                                c6 = JSONReader.char4(c7, c8, c9, c10);
                                break;
                            }
                            case 'x': {
                                final char c7 = (char)this.bytes[++offset];
                                final char c8 = (char)this.bytes[++offset];
                                c6 = JSONReader.char2(c7, c8);
                                break;
                            }
                            case '\"':
                            case '\\': {
                                break;
                            }
                            case 'b': {
                                c6 = '\b';
                                break;
                            }
                            case 't': {
                                c6 = '\t';
                                break;
                            }
                            case 'n': {
                                c6 = '\n';
                                break;
                            }
                            case 'f': {
                                c6 = '\f';
                                break;
                            }
                            case 'r': {
                                c6 = '\r';
                                break;
                            }
                            default: {
                                c6 = this.char1(c6);
                                break;
                            }
                        }
                    }
                    else if (c6 == quote) {
                        break;
                    }
                    chars[j] = c6;
                    ++offset;
                    ++j;
                }
                str = new String(chars);
            }
            else if (this.str != null) {
                str = this.str.substring(this.offset, offset);
            }
            else if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                final byte[] bytes = Arrays.copyOfRange(this.bytes, this.offset, offset);
                str = JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
            }
            else {
                str = new String(this.bytes, this.offset, offset - this.offset, StandardCharsets.ISO_8859_1);
            }
            if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                str = str.trim();
            }
            Label_0970: {
                if (++offset != this.end) {
                    byte e;
                    for (e = this.bytes[offset++]; e <= 32 && (1L << e & 0x100003700L) != 0x0L; e = this.bytes[offset++]) {
                        if (offset == this.end) {
                            break Label_0970;
                        }
                    }
                    final boolean comma = e == 44;
                    this.comma = comma;
                    if (comma) {
                        if (offset == this.end) {
                            e = 26;
                        }
                        else {
                            for (e = this.bytes[offset++]; e <= 32 && (1L << e & 0x100003700L) != 0x0L; e = this.bytes[offset++]) {
                                if (offset == this.end) {
                                    e = 26;
                                    break;
                                }
                            }
                        }
                    }
                    this.ch = (char)e;
                    this.offset = offset;
                    return str;
                }
            }
            this.ch = '\u001a';
            this.comma = false;
            this.offset = offset;
            return str;
        }
        switch (this.ch) {
            case '[': {
                return this.toString(this.readArray());
            }
            case '{': {
                return this.toString(this.readObject());
            }
            case '+':
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                this.readNumber0();
                final Number number = this.getNumber();
                return number.toString();
            }
            case 'f':
            case 't': {
                this.boolValue = this.readBoolValue();
                return this.boolValue ? "true" : "false";
            }
            case 'n': {
                this.readNull();
                return null;
            }
            default: {
                throw new JSONException(this.info("illegal input : " + this.ch));
            }
        }
    }
}
