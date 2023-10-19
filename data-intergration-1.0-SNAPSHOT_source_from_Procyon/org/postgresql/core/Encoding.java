// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.io.UnsupportedEncodingException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;

public class Encoding
{
    private static final Encoding DEFAULT_ENCODING;
    private static final HashMap encodings;
    private final String encoding;
    private final boolean fastASCIINumbers;
    
    protected Encoding(final String encoding) {
        this.encoding = encoding;
        this.fastASCIINumbers = this.testAsciiNumbers();
    }
    
    public boolean hasAsciiNumbers() {
        return this.fastASCIINumbers;
    }
    
    public static Encoding getJVMEncoding(final String jvmEncoding) {
        if (!isAvailable(jvmEncoding)) {
            return defaultEncoding();
        }
        if (jvmEncoding.equals("UTF-8") || jvmEncoding.equals("UTF8")) {
            return new UTF8Encoding(jvmEncoding);
        }
        return new Encoding(jvmEncoding);
    }
    
    public static Encoding getDatabaseEncoding(final String databaseEncoding) {
        final String[] candidates = Encoding.encodings.get(databaseEncoding);
        if (candidates != null) {
            for (int i = 0; i < candidates.length; ++i) {
                if (isAvailable(candidates[i])) {
                    return new Encoding(candidates[i]);
                }
            }
        }
        if (isAvailable(databaseEncoding)) {
            return new Encoding(databaseEncoding);
        }
        return defaultEncoding();
    }
    
    public String name() {
        return this.encoding;
    }
    
    public byte[] encode(final String s) throws IOException {
        if (s == null) {
            return null;
        }
        if (this.encoding == null) {
            return s.getBytes();
        }
        return s.getBytes(this.encoding);
    }
    
    public String decode(final byte[] encodedString, final int offset, final int length) throws IOException {
        if (this.encoding == null) {
            return new String(encodedString, offset, length);
        }
        return new String(encodedString, offset, length, this.encoding);
    }
    
    public String decode(final byte[] encodedString) throws IOException {
        return this.decode(encodedString, 0, encodedString.length);
    }
    
    public Reader getDecodingReader(final InputStream in) throws IOException {
        if (this.encoding == null) {
            return new InputStreamReader(in);
        }
        return new InputStreamReader(in, this.encoding);
    }
    
    public Writer getEncodingWriter(final OutputStream out) throws IOException {
        if (this.encoding == null) {
            return new OutputStreamWriter(out);
        }
        return new OutputStreamWriter(out, this.encoding);
    }
    
    public static Encoding defaultEncoding() {
        return Encoding.DEFAULT_ENCODING;
    }
    
    private static boolean isAvailable(final String encodingName) {
        try {
            "DUMMY".getBytes(encodingName);
            return true;
        }
        catch (UnsupportedEncodingException e) {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return (this.encoding == null) ? "<default JVM encoding>" : this.encoding;
    }
    
    private boolean testAsciiNumbers() {
        try {
            final String test = "-0123456789";
            final byte[] bytes = this.encode(test);
            final String res = new String(bytes, "US-ASCII");
            return test.equals(res);
        }
        catch (UnsupportedEncodingException e) {
            return false;
        }
        catch (IOException e2) {
            return false;
        }
    }
    
    static {
        DEFAULT_ENCODING = new Encoding(null);
        (encodings = new HashMap()).put("SQL_ASCII", new String[] { "ASCII", "us-ascii" });
        Encoding.encodings.put("UNICODE", new String[] { "UTF-8", "UTF8" });
        Encoding.encodings.put("UTF8", new String[] { "UTF-8", "UTF8" });
        Encoding.encodings.put("LATIN1", new String[] { "ISO8859_1" });
        Encoding.encodings.put("LATIN2", new String[] { "ISO8859_2" });
        Encoding.encodings.put("LATIN3", new String[] { "ISO8859_3" });
        Encoding.encodings.put("LATIN4", new String[] { "ISO8859_4" });
        Encoding.encodings.put("ISO_8859_5", new String[] { "ISO8859_5" });
        Encoding.encodings.put("ISO_8859_6", new String[] { "ISO8859_6" });
        Encoding.encodings.put("ISO_8859_7", new String[] { "ISO8859_7" });
        Encoding.encodings.put("ISO_8859_8", new String[] { "ISO8859_8" });
        Encoding.encodings.put("LATIN5", new String[] { "ISO8859_9" });
        Encoding.encodings.put("LATIN7", new String[] { "ISO8859_13" });
        Encoding.encodings.put("LATIN9", new String[] { "ISO8859_15_FDIS" });
        Encoding.encodings.put("EUC_JP", new String[] { "EUC_JP" });
        Encoding.encodings.put("EUC_CN", new String[] { "EUC_CN" });
        Encoding.encodings.put("EUC_KR", new String[] { "EUC_KR" });
        Encoding.encodings.put("JOHAB", new String[] { "Johab" });
        Encoding.encodings.put("EUC_TW", new String[] { "EUC_TW" });
        Encoding.encodings.put("SJIS", new String[] { "MS932", "SJIS" });
        Encoding.encodings.put("BIG5", new String[] { "Big5", "MS950", "Cp950" });
        Encoding.encodings.put("GBK", new String[] { "GBK", "MS936" });
        Encoding.encodings.put("UHC", new String[] { "MS949", "Cp949", "Cp949C" });
        Encoding.encodings.put("TCVN", new String[] { "Cp1258" });
        Encoding.encodings.put("WIN1256", new String[] { "Cp1256" });
        Encoding.encodings.put("WIN1250", new String[] { "Cp1250" });
        Encoding.encodings.put("WIN874", new String[] { "MS874", "Cp874" });
        Encoding.encodings.put("WIN", new String[] { "Cp1251" });
        Encoding.encodings.put("ALT", new String[] { "Cp866" });
        Encoding.encodings.put("KOI8", new String[] { "KOI8_U", "KOI8_R" });
        Encoding.encodings.put("UNKNOWN", new String[0]);
        Encoding.encodings.put("MULE_INTERNAL", new String[0]);
        Encoding.encodings.put("LATIN6", new String[0]);
        Encoding.encodings.put("LATIN8", new String[0]);
        Encoding.encodings.put("LATIN10", new String[0]);
    }
}
