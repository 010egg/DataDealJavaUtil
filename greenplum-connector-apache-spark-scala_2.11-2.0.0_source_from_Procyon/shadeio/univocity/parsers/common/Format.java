// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map;

public abstract class Format implements Cloneable
{
    private static final String systemLineSeparatorString;
    private static final char[] systemLineSeparator;
    private String lineSeparatorString;
    private char[] lineSeparator;
    private char normalizedNewline;
    private char comment;
    
    protected Format() {
        this.normalizedNewline = '\n';
        this.comment = '#';
        this.lineSeparator = Format.systemLineSeparator.clone();
        this.lineSeparatorString = Format.systemLineSeparatorString;
    }
    
    public char[] getLineSeparator() {
        return this.lineSeparator.clone();
    }
    
    public static char[] getSystemLineSeparator() {
        return Format.systemLineSeparator.clone();
    }
    
    public String getLineSeparatorString() {
        return this.lineSeparatorString;
    }
    
    public void setLineSeparator(final String lineSeparator) {
        if (lineSeparator == null || lineSeparator.isEmpty()) {
            throw new IllegalArgumentException("Line separator cannot be empty");
        }
        this.setLineSeparator(lineSeparator.toCharArray());
    }
    
    public void setLineSeparator(final char[] lineSeparator) {
        if (lineSeparator == null || lineSeparator.length == 0) {
            throw new IllegalArgumentException("Invalid line separator. Expected 1 to 2 characters");
        }
        if (lineSeparator.length > 2) {
            throw new IllegalArgumentException("Invalid line separator. Up to 2 characters are expected. Got " + lineSeparator.length + " characters.");
        }
        this.lineSeparator = lineSeparator;
        this.lineSeparatorString = new String(lineSeparator);
        if (lineSeparator.length == 1) {
            this.setNormalizedNewline(lineSeparator[0]);
        }
    }
    
    public char getNormalizedNewline() {
        return this.normalizedNewline;
    }
    
    public void setNormalizedNewline(final char normalizedNewline) {
        this.normalizedNewline = normalizedNewline;
    }
    
    public boolean isNewLine(final char ch) {
        return this.normalizedNewline == ch;
    }
    
    public char getComment() {
        return this.comment;
    }
    
    public void setComment(final char comment) {
        this.comment = comment;
    }
    
    public boolean isComment(final char ch) {
        return this.comment == ch;
    }
    
    private static String getFormattedValue(Object value) {
        if (value instanceof Character) {
            final char ch = (char)value;
            switch (ch) {
                case '\n': {
                    return "\\n";
                }
                case '\r': {
                    return "\\r";
                }
                case '\t': {
                    return "\\t";
                }
                case '\0': {
                    return "\\0";
                }
                default: {
                    return value.toString();
                }
            }
        }
        else {
            if (value instanceof String) {
                final String s = (String)value;
                final StringBuilder tmp = new StringBuilder();
                for (int i = 0; i < s.length(); ++i) {
                    tmp.append(getFormattedValue(s.charAt(i)));
                }
                value = tmp.toString();
            }
            if (String.valueOf(value).trim().isEmpty()) {
                return "'" + value + '\'';
            }
            return String.valueOf(value);
        }
    }
    
    @Override
    public final String toString() {
        final StringBuilder out = new StringBuilder();
        out.append(this.getClass().getSimpleName()).append(':');
        final TreeMap<String, Object> config = this.getConfiguration();
        config.put("Comment character", this.comment);
        config.put("Line separator sequence", this.lineSeparatorString);
        config.put("Line separator (normalized)", this.normalizedNewline);
        for (final Map.Entry<String, Object> e : config.entrySet()) {
            out.append("\n\t\t");
            out.append(e.getKey()).append('=').append(getFormattedValue(e.getValue()));
        }
        return out.toString();
    }
    
    protected abstract TreeMap<String, Object> getConfiguration();
    
    @Override
    protected Format clone() {
        try {
            return (Format)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Error cloning format object", e);
        }
    }
    
    static {
        final String lineSeparator = System.getProperty("line.separator");
        if (lineSeparator == null) {
            systemLineSeparatorString = "\n";
        }
        else {
            systemLineSeparatorString = lineSeparator;
        }
        systemLineSeparator = Format.systemLineSeparatorString.toCharArray();
    }
}
