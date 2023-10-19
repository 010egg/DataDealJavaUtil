// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.stream;

import com.alibaba.fastjson2.JSONException;
import java.io.IOException;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import com.alibaba.fastjson2.JSONFactory;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.nio.charset.Charset;
import java.io.InputStream;

class JSONStreamReaderUTF8<T> extends JSONStreamReader<T>
{
    byte[] buf;
    final InputStream input;
    final Charset charset;
    final JSONReader.Context context;
    
    JSONStreamReaderUTF8(final InputStream input, final Charset charset, final Type[] types) {
        super(types);
        this.charset = charset;
        this.input = input;
        this.context = JSONFactory.createReadContext();
    }
    
    JSONStreamReaderUTF8(final InputStream input, final Charset charset, final ObjectReaderAdapter objectReader) {
        super(objectReader);
        this.charset = charset;
        this.input = input;
        this.context = JSONFactory.createReadContext();
    }
    
    @Override
    protected boolean seekLine() throws IOException {
        if (this.buf == null && this.input != null) {
            this.buf = new byte[524288];
            final int cnt = this.input.read(this.buf);
            if (cnt == -1) {
                this.inputEnd = true;
                return false;
            }
            this.end = cnt;
            if (this.end > 3 && this.buf[0] == -17 && this.buf[1] == -69 && this.buf[2] == -65) {
                this.off = 3;
                this.lineNextStart = this.off;
            }
        }
        int k = 0;
        while (k < 3) {
            this.lineTerminated = false;
            for (int i = this.off; i < this.end; ++i) {
                if (i + 4 < this.end) {
                    final byte b0 = this.buf[i];
                    final byte b2 = this.buf[i + 1];
                    final byte b3 = this.buf[i + 2];
                    final byte b4 = this.buf[i + 3];
                    if (b0 > 34 && b2 > 34 && b3 > 34 && b4 > 34) {
                        this.lineSize += 4;
                        i += 3;
                        continue;
                    }
                }
                final byte ch = this.buf[i];
                if (ch == 10) {
                    if (this.lineSize > 0 || (this.features & Feature.IgnoreEmptyLine.mask) == 0x0L) {
                        ++this.rowCount;
                    }
                    this.lineTerminated = true;
                    this.lineSize = 0;
                    this.lineEnd = i;
                    this.lineStart = this.lineNextStart;
                    final int n2 = i + 1;
                    this.off = n2;
                    this.lineNextStart = n2;
                    break;
                }
                if (ch == 13) {
                    if (this.lineSize > 0 || (this.features & Feature.IgnoreEmptyLine.mask) == 0x0L) {
                        ++this.rowCount;
                    }
                    this.lineTerminated = true;
                    this.lineSize = 0;
                    this.lineEnd = i;
                    final int n = i + 1;
                    if (n >= this.end) {
                        break;
                    }
                    if (this.buf[n] == 10) {
                        ++i;
                    }
                    this.lineStart = this.lineNextStart;
                    final int n3 = i + 1;
                    this.off = n3;
                    this.lineNextStart = n3;
                    break;
                }
                else {
                    ++this.lineSize;
                }
            }
            if (!this.lineTerminated) {
                if (this.input != null && !this.inputEnd) {
                    final int len = this.end - this.off;
                    if (this.off > 0) {
                        if (len > 0) {
                            System.arraycopy(this.buf, this.off, this.buf, 0, len);
                        }
                        final int n4 = 0;
                        this.lineNextStart = n4;
                        this.lineStart = n4;
                        this.off = 0;
                        this.end = len;
                    }
                    final int cnt2 = this.input.read(this.buf, this.end, this.buf.length - this.end);
                    if (cnt2 != -1) {
                        this.end += cnt2;
                        ++k;
                        continue;
                    }
                    this.inputEnd = true;
                    if (this.off == this.end) {
                        return false;
                    }
                }
                this.lineStart = this.lineNextStart;
                this.lineEnd = this.end;
                ++this.rowCount;
                this.lineSize = 0;
                this.off = this.end;
            }
            this.lineTerminated = (this.off == this.end);
            break;
        }
        return true;
    }
    
    @Override
    public <T> T readLineObject() {
        try {
            if (this.inputEnd) {
                return null;
            }
            if (this.input == null && this.off >= this.end) {
                return null;
            }
            final boolean result = this.seekLine();
            if (!result) {
                return null;
            }
        }
        catch (IOException e) {
            throw new JSONException("seekLine error", e);
        }
        final JSONReader reader = JSONReader.of(this.buf, this.lineStart, this.lineEnd - this.lineStart, this.charset, this.context);
        Object object;
        if (this.objectReader != null) {
            object = this.objectReader.readObject(reader, null, null, this.features);
        }
        else if (reader.isArray() && this.types != null && this.types.length != 0) {
            object = reader.readList(this.types);
        }
        else {
            object = reader.readAny();
        }
        return (T)object;
    }
}
