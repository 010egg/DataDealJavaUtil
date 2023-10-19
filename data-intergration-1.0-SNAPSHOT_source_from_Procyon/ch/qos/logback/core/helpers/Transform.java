// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.helpers;

import java.util.regex.Pattern;

public class Transform
{
    private static final String CDATA_START = "<![CDATA[";
    private static final String CDATA_END = "]]>";
    private static final String CDATA_PSEUDO_END = "]]&gt;";
    private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
    private static final int CDATA_END_LEN;
    private static final Pattern UNSAFE_XML_CHARS;
    
    public static String escapeTags(final String input) {
        if (input == null || input.length() == 0 || !Transform.UNSAFE_XML_CHARS.matcher(input).find()) {
            return input;
        }
        final StringBuffer buf = new StringBuffer(input);
        return escapeTags(buf);
    }
    
    public static String escapeTags(final StringBuffer buf) {
        for (int i = 0; i < buf.length(); ++i) {
            final char ch = buf.charAt(i);
            switch (ch) {
                case '\t':
                case '\n':
                case '\r': {
                    break;
                }
                case '&': {
                    buf.replace(i, i + 1, "&amp;");
                    break;
                }
                case '<': {
                    buf.replace(i, i + 1, "&lt;");
                    break;
                }
                case '>': {
                    buf.replace(i, i + 1, "&gt;");
                    break;
                }
                case '\"': {
                    buf.replace(i, i + 1, "&quot;");
                    break;
                }
                case '\'': {
                    buf.replace(i, i + 1, "&#39;");
                    break;
                }
                default: {
                    if (ch < ' ') {
                        buf.replace(i, i + 1, "\ufffd");
                        break;
                    }
                    break;
                }
            }
        }
        return buf.toString();
    }
    
    public static void appendEscapingCDATA(final StringBuilder output, final String str) {
        if (str == null) {
            return;
        }
        int end = str.indexOf("]]>");
        if (end < 0) {
            output.append(str);
            return;
        }
        int start;
        for (start = 0; end > -1; end = str.indexOf("]]>", start)) {
            output.append(str.substring(start, end));
            output.append("]]>]]&gt;<![CDATA[");
            start = end + Transform.CDATA_END_LEN;
            if (start >= str.length()) {
                return;
            }
        }
        output.append(str.substring(start));
    }
    
    static {
        CDATA_END_LEN = "]]>".length();
        UNSAFE_XML_CHARS = Pattern.compile("[\u0000-\b\u000b\f\u000e-\u001f<>&'\"]");
    }
}
