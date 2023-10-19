// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import java.nio.charset.Charset;
import scala.package$;
import java.io.Reader;
import java.io.StringReader;
import scala.collection.Seq;
import scala.runtime.BoxesRunTime;
import scala.collection.immutable.StringOps;
import scala.Predef$;
import scala.collection.immutable.StringOps$;
import java.io.Writer;
import scala.collection.mutable.StringBuilder;
import scala.math.BigDecimal;
import java.nio.charset.CharsetEncoder;

public final class ParserUtil$
{
    public static final ParserUtil$ MODULE$;
    public final char org$json4s$ParserUtil$$EOF;
    private final CharsetEncoder AsciiEncoder;
    private final BigDecimal BrokenDouble;
    
    static {
        new ParserUtil$();
    }
    
    public String quote(final String s, final Formats formats) {
        return this.quote(s, (ParserUtil.StringAppender<StringBuilder>)new ParserUtil.StringBuilderAppender(new StringBuilder()), formats).toString();
    }
    
    public Writer quote(final String s, final Writer writer, final Formats formats) {
        return this.quote(s, (ParserUtil.StringAppender<Writer>)new ParserUtil.StringWriterAppender(writer), formats);
    }
    
    private <T> T quote(final String s, final ParserUtil.StringAppender<T> appender, final Formats formats) {
        for (int i = 0, l = s.length(); i < l; ++i) {
            final char apply$extension = StringOps$.MODULE$.apply$extension(Predef$.MODULE$.augmentString(s), i);
            switch (apply$extension) {
                default: {
                    final boolean shouldEscape = formats.alwaysEscapeUnicode() ? (!this.AsciiEncoder.canEncode(apply$extension)) : ((apply$extension >= '\0' && apply$extension <= '\u001f') || (apply$extension >= '\u0080' && apply$extension < ' ') || (apply$extension >= '\u2000' && apply$extension < '\u2100'));
                    if (shouldEscape) {
                        appender.append(new StringOps(Predef$.MODULE$.augmentString("\\u%04x")).format((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToInteger((int)apply$extension) })));
                        break;
                    }
                    appender.append(BoxesRunTime.boxToCharacter(apply$extension).toString());
                    break;
                }
                case 9: {
                    appender.append("\\t");
                    break;
                }
                case 13: {
                    appender.append("\\r");
                    break;
                }
                case 10: {
                    appender.append("\\n");
                    break;
                }
                case 12: {
                    appender.append("\\f");
                    break;
                }
                case 8: {
                    appender.append("\\b");
                    break;
                }
                case 92: {
                    appender.append("\\\\");
                    break;
                }
                case 34: {
                    appender.append("\\\"");
                    break;
                }
            }
        }
        return appender.subj();
    }
    
    public Formats quote$default$2(final String s) {
        return DefaultFormats$.MODULE$;
    }
    
    public String unquote(final String string) {
        return this.unquote(new ParserUtil.Buffer(new StringReader(string), false));
    }
    
    public String unquote(final ParserUtil.Buffer buf) {
        buf.eofIsFailure_$eq(true);
        buf.mark();
        for (char c = buf.next(); c != '\"'; c = buf.next()) {
            if (c == '\\') {
                final String s = this.unquote0$1(buf, buf.substring());
                buf.eofIsFailure_$eq(false);
                return s;
            }
        }
        buf.eofIsFailure_$eq(false);
        return buf.substring();
    }
    
    public double parseDouble(final String s) {
        final BigDecimal apply;
        final BigDecimal d = apply = package$.MODULE$.BigDecimal().apply(s);
        final BigDecimal brokenDouble = this.BrokenDouble;
        if (apply == null) {
            if (brokenDouble != null) {
                return d.doubleValue();
            }
        }
        else if (!apply.equals(brokenDouble)) {
            return d.doubleValue();
        }
        throw scala.sys.package$.MODULE$.error("Error parsing 2.2250738585072012e-308");
    }
    
    private final String unquote0$1(final ParserUtil.Buffer buf, final String base) {
        final java.lang.StringBuilder s = new java.lang.StringBuilder(base);
        for (char c = '\\'; c != '\"'; c = buf.next()) {
            if (c == '\\') {
                switch (buf.next()) {
                    default: {
                        s.append('\\');
                        break;
                    }
                    case 'u': {
                        final char[] chars = { buf.next(), buf.next(), buf.next(), buf.next() };
                        final int codePoint = Integer.parseInt(new String(chars), 16);
                        s.appendCodePoint(codePoint);
                        break;
                    }
                    case 't': {
                        s.append('\t');
                        break;
                    }
                    case 'r': {
                        s.append('\r');
                        break;
                    }
                    case 'n': {
                        s.append('\n');
                        break;
                    }
                    case 'f': {
                        s.append('\f');
                        break;
                    }
                    case 'b': {
                        s.append('\b');
                        break;
                    }
                    case '/': {
                        s.append('/');
                        break;
                    }
                    case '\\': {
                        s.append('\\');
                        break;
                    }
                    case '\"': {
                        s.append('\"');
                        break;
                    }
                }
            }
            else {
                s.append(c);
            }
        }
        return s.toString();
    }
    
    private ParserUtil$() {
        MODULE$ = this;
        this.org$json4s$ParserUtil$$EOF = (char)(-1);
        this.AsciiEncoder = Charset.forName("US-ASCII").newEncoder();
        this.BrokenDouble = package$.MODULE$.BigDecimal().apply("2.2250738585072012e-308");
    }
}
