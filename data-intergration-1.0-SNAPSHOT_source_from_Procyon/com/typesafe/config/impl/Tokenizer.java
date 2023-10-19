// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import com.typesafe.config.ConfigException;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;
import com.typesafe.config.ConfigSyntax;
import java.io.Reader;
import com.typesafe.config.ConfigOrigin;

final class Tokenizer
{
    private static String asString(final int codepoint) {
        if (codepoint == 10) {
            return "newline";
        }
        if (codepoint == 9) {
            return "tab";
        }
        if (codepoint == -1) {
            return "end of file";
        }
        if (ConfigImplUtil.isC0Control(codepoint)) {
            return String.format("control character 0x%x", codepoint);
        }
        return String.format("%c", codepoint);
    }
    
    static Iterator<Token> tokenize(final ConfigOrigin origin, final Reader input, final ConfigSyntax flavor) {
        return new TokenIterator(origin, input, flavor != ConfigSyntax.JSON);
    }
    
    static String render(final Iterator<Token> tokens) {
        final StringBuilder renderedText = new StringBuilder();
        while (tokens.hasNext()) {
            renderedText.append(tokens.next().tokenText());
        }
        return renderedText.toString();
    }
    
    private static class ProblemException extends Exception
    {
        private static final long serialVersionUID = 1L;
        private final Token problem;
        
        ProblemException(final Token problem) {
            this.problem = problem;
        }
        
        Token problem() {
            return this.problem;
        }
    }
    
    private static class TokenIterator implements Iterator<Token>
    {
        private final SimpleConfigOrigin origin;
        private final Reader input;
        private final LinkedList<Integer> buffer;
        private int lineNumber;
        private ConfigOrigin lineOrigin;
        private final Queue<Token> tokens;
        private final WhitespaceSaver whitespaceSaver;
        private final boolean allowComments;
        static final String firstNumberChars = "0123456789-";
        static final String numberChars = "0123456789eE+-.";
        static final String notInUnquotedText = "$\"{}[]:=,+#`^?!@*&\\";
        
        TokenIterator(final ConfigOrigin origin, final Reader input, final boolean allowComments) {
            this.origin = (SimpleConfigOrigin)origin;
            this.input = input;
            this.allowComments = allowComments;
            this.buffer = new LinkedList<Integer>();
            this.lineNumber = 1;
            this.lineOrigin = this.origin.withLineNumber(this.lineNumber);
            (this.tokens = new LinkedList<Token>()).add(Tokens.START);
            this.whitespaceSaver = new WhitespaceSaver();
        }
        
        private int nextCharRaw() {
            if (this.buffer.isEmpty()) {
                try {
                    return this.input.read();
                }
                catch (IOException e) {
                    throw new ConfigException.IO(this.origin, "read error: " + e.getMessage(), e);
                }
            }
            final int c = this.buffer.pop();
            return c;
        }
        
        private void putBack(final int c) {
            if (this.buffer.size() > 2) {
                throw new ConfigException.BugOrBroken("bug: putBack() three times, undesirable look-ahead");
            }
            this.buffer.push(c);
        }
        
        static boolean isWhitespace(final int c) {
            return ConfigImplUtil.isWhitespace(c);
        }
        
        static boolean isWhitespaceNotNewline(final int c) {
            return c != 10 && ConfigImplUtil.isWhitespace(c);
        }
        
        private boolean startOfComment(final int c) {
            if (c == -1) {
                return false;
            }
            if (!this.allowComments) {
                return false;
            }
            if (c == 35) {
                return true;
            }
            if (c == 47) {
                final int maybeSecondSlash = this.nextCharRaw();
                this.putBack(maybeSecondSlash);
                return maybeSecondSlash == 47;
            }
            return false;
        }
        
        private int nextCharAfterWhitespace(final WhitespaceSaver saver) {
            while (true) {
                final int c = this.nextCharRaw();
                if (c == -1) {
                    return -1;
                }
                if (!isWhitespaceNotNewline(c)) {
                    return c;
                }
                saver.add(c);
            }
        }
        
        private ProblemException problem(final String message) {
            return this.problem("", message, null);
        }
        
        private ProblemException problem(final String what, final String message) {
            return this.problem(what, message, null);
        }
        
        private ProblemException problem(final String what, final String message, final boolean suggestQuotes) {
            return this.problem(what, message, suggestQuotes, null);
        }
        
        private ProblemException problem(final String what, final String message, final Throwable cause) {
            return problem(this.lineOrigin, what, message, cause);
        }
        
        private ProblemException problem(final String what, final String message, final boolean suggestQuotes, final Throwable cause) {
            return problem(this.lineOrigin, what, message, suggestQuotes, cause);
        }
        
        private static ProblemException problem(final ConfigOrigin origin, final String what, final String message, final Throwable cause) {
            return problem(origin, what, message, false, cause);
        }
        
        private static ProblemException problem(final ConfigOrigin origin, final String what, final String message, final boolean suggestQuotes, final Throwable cause) {
            if (what == null || message == null) {
                throw new ConfigException.BugOrBroken("internal error, creating bad ProblemException");
            }
            return new ProblemException(Tokens.newProblem(origin, what, message, suggestQuotes, cause));
        }
        
        private static ProblemException problem(final ConfigOrigin origin, final String message) {
            return problem(origin, "", message, null);
        }
        
        private static ConfigOrigin lineOrigin(final ConfigOrigin baseOrigin, final int lineNumber) {
            return ((SimpleConfigOrigin)baseOrigin).withLineNumber(lineNumber);
        }
        
        private Token pullComment(final int firstChar) {
            boolean doubleSlash = false;
            if (firstChar == 47) {
                final int discard = this.nextCharRaw();
                if (discard != 47) {
                    throw new ConfigException.BugOrBroken("called pullComment but // not seen");
                }
                doubleSlash = true;
            }
            final StringBuilder sb = new StringBuilder();
            int c;
            while (true) {
                c = this.nextCharRaw();
                if (c == -1 || c == 10) {
                    break;
                }
                sb.appendCodePoint(c);
            }
            this.putBack(c);
            if (doubleSlash) {
                return Tokens.newCommentDoubleSlash(this.lineOrigin, sb.toString());
            }
            return Tokens.newCommentHash(this.lineOrigin, sb.toString());
        }
        
        private Token pullUnquotedText() {
            final ConfigOrigin origin = this.lineOrigin;
            final StringBuilder sb = new StringBuilder();
            int c = this.nextCharRaw();
            while (true) {
                while (c != -1) {
                    if ("$\"{}[]:=,+#`^?!@*&\\".indexOf(c) < 0) {
                        if (!isWhitespace(c)) {
                            if (!this.startOfComment(c)) {
                                sb.appendCodePoint(c);
                                if (sb.length() == 4) {
                                    final String s = sb.toString();
                                    if (s.equals("true")) {
                                        return Tokens.newBoolean(origin, true);
                                    }
                                    if (s.equals("null")) {
                                        return Tokens.newNull(origin);
                                    }
                                }
                                else if (sb.length() == 5) {
                                    final String s = sb.toString();
                                    if (s.equals("false")) {
                                        return Tokens.newBoolean(origin, false);
                                    }
                                }
                                c = this.nextCharRaw();
                                continue;
                            }
                        }
                    }
                    this.putBack(c);
                    final String s = sb.toString();
                    return Tokens.newUnquotedText(origin, s);
                }
                continue;
            }
        }
        
        private Token pullNumber(final int firstChar) throws ProblemException {
            final StringBuilder sb = new StringBuilder();
            sb.appendCodePoint(firstChar);
            boolean containedDecimalOrE = false;
            int c;
            for (c = this.nextCharRaw(); c != -1 && "0123456789eE+-.".indexOf(c) >= 0; c = this.nextCharRaw()) {
                if (c == 46 || c == 101 || c == 69) {
                    containedDecimalOrE = true;
                }
                sb.appendCodePoint(c);
            }
            this.putBack(c);
            final String s = sb.toString();
            try {
                if (containedDecimalOrE) {
                    return Tokens.newDouble(this.lineOrigin, Double.parseDouble(s), s);
                }
                return Tokens.newLong(this.lineOrigin, Long.parseLong(s), s);
            }
            catch (NumberFormatException e) {
                for (final char u : s.toCharArray()) {
                    if ("$\"{}[]:=,+#`^?!@*&\\".indexOf(u) >= 0) {
                        throw this.problem(asString(u), "Reserved character '" + asString(u) + "' is not allowed outside quotes", true);
                    }
                }
                return Tokens.newUnquotedText(this.lineOrigin, s);
            }
        }
        
        private void pullEscapeSequence(final StringBuilder sb, final StringBuilder sbOrig) throws ProblemException {
            final int escaped = this.nextCharRaw();
            if (escaped == -1) {
                throw this.problem("End of input but backslash in string had nothing after it");
            }
            sbOrig.appendCodePoint(92);
            sbOrig.appendCodePoint(escaped);
            switch (escaped) {
                case 34: {
                    sb.append('\"');
                    break;
                }
                case 92: {
                    sb.append('\\');
                    break;
                }
                case 47: {
                    sb.append('/');
                    break;
                }
                case 98: {
                    sb.append('\b');
                    break;
                }
                case 102: {
                    sb.append('\f');
                    break;
                }
                case 110: {
                    sb.append('\n');
                    break;
                }
                case 114: {
                    sb.append('\r');
                    break;
                }
                case 116: {
                    sb.append('\t');
                    break;
                }
                case 117: {
                    final char[] a = new char[4];
                    for (int i = 0; i < 4; ++i) {
                        final int c = this.nextCharRaw();
                        if (c == -1) {
                            throw this.problem("End of input but expecting 4 hex digits for \\uXXXX escape");
                        }
                        a[i] = (char)c;
                    }
                    final String digits = new String(a);
                    sbOrig.append(a);
                    try {
                        sb.appendCodePoint(Integer.parseInt(digits, 16));
                    }
                    catch (NumberFormatException e) {
                        throw this.problem(digits, String.format("Malformed hex digits after \\u escape in string: '%s'", digits), e);
                    }
                    break;
                }
                default: {
                    throw this.problem(asString(escaped), String.format("backslash followed by '%s', this is not a valid escape sequence (quoted strings use JSON escaping, so use double-backslash \\\\ for literal backslash)", asString(escaped)));
                }
            }
        }
        
        private void appendTripleQuotedString(final StringBuilder sb, final StringBuilder sbOrig) throws ProblemException {
            int consecutiveQuotes = 0;
            while (true) {
                final int c = this.nextCharRaw();
                if (c == 34) {
                    ++consecutiveQuotes;
                }
                else {
                    if (consecutiveQuotes >= 3) {
                        sb.setLength(sb.length() - 3);
                        this.putBack(c);
                        return;
                    }
                    consecutiveQuotes = 0;
                    if (c == -1) {
                        throw this.problem("End of input but triple-quoted string was still open");
                    }
                    if (c == 10) {
                        ++this.lineNumber;
                        this.lineOrigin = this.origin.withLineNumber(this.lineNumber);
                    }
                }
                sb.appendCodePoint(c);
                sbOrig.appendCodePoint(c);
            }
        }
        
        private Token pullQuotedString() throws ProblemException {
            final StringBuilder sb = new StringBuilder();
            final StringBuilder sbOrig = new StringBuilder();
            sbOrig.appendCodePoint(34);
            while (true) {
                final int c = this.nextCharRaw();
                if (c == -1) {
                    throw this.problem("End of input but string quote was still open");
                }
                if (c == 92) {
                    this.pullEscapeSequence(sb, sbOrig);
                }
                else {
                    if (c == 34) {
                        sbOrig.appendCodePoint(c);
                        if (sb.length() == 0) {
                            final int third = this.nextCharRaw();
                            if (third == 34) {
                                sbOrig.appendCodePoint(third);
                                this.appendTripleQuotedString(sb, sbOrig);
                            }
                            else {
                                this.putBack(third);
                            }
                        }
                        return Tokens.newString(this.lineOrigin, sb.toString(), sbOrig.toString());
                    }
                    if (ConfigImplUtil.isC0Control(c)) {
                        throw this.problem(asString(c), "JSON does not allow unescaped " + asString(c) + " in quoted strings, use a backslash escape");
                    }
                    sb.appendCodePoint(c);
                    sbOrig.appendCodePoint(c);
                }
            }
        }
        
        private Token pullPlusEquals() throws ProblemException {
            final int c = this.nextCharRaw();
            if (c != 61) {
                throw this.problem(asString(c), "'+' not followed by =, '" + asString(c) + "' not allowed after '+'", true);
            }
            return Tokens.PLUS_EQUALS;
        }
        
        private Token pullSubstitution() throws ProblemException {
            final ConfigOrigin origin = this.lineOrigin;
            int c = this.nextCharRaw();
            if (c != 123) {
                throw this.problem(asString(c), "'$' not followed by {, '" + asString(c) + "' not allowed after '$'", true);
            }
            boolean optional = false;
            c = this.nextCharRaw();
            if (c == 63) {
                optional = true;
            }
            else {
                this.putBack(c);
            }
            final WhitespaceSaver saver = new WhitespaceSaver();
            final List<Token> expression = new ArrayList<Token>();
            while (true) {
                final Token t = this.pullNextToken(saver);
                if (t == Tokens.CLOSE_CURLY) {
                    return Tokens.newSubstitution(origin, optional, expression);
                }
                if (t == Tokens.END) {
                    throw problem(origin, "Substitution ${ was not closed with a }");
                }
                final Token whitespace = saver.check(t, origin, this.lineNumber);
                if (whitespace != null) {
                    expression.add(whitespace);
                }
                expression.add(t);
            }
        }
        
        private Token pullNextToken(final WhitespaceSaver saver) throws ProblemException {
            final int c = this.nextCharAfterWhitespace(saver);
            if (c == -1) {
                return Tokens.END;
            }
            if (c == 10) {
                final Token line = Tokens.newLine(this.lineOrigin);
                ++this.lineNumber;
                this.lineOrigin = this.origin.withLineNumber(this.lineNumber);
                return line;
            }
            Token t = null;
            if (this.startOfComment(c)) {
                t = this.pullComment(c);
            }
            else {
                switch (c) {
                    case 34: {
                        t = this.pullQuotedString();
                        break;
                    }
                    case 36: {
                        t = this.pullSubstitution();
                        break;
                    }
                    case 58: {
                        t = Tokens.COLON;
                        break;
                    }
                    case 44: {
                        t = Tokens.COMMA;
                        break;
                    }
                    case 61: {
                        t = Tokens.EQUALS;
                        break;
                    }
                    case 123: {
                        t = Tokens.OPEN_CURLY;
                        break;
                    }
                    case 125: {
                        t = Tokens.CLOSE_CURLY;
                        break;
                    }
                    case 91: {
                        t = Tokens.OPEN_SQUARE;
                        break;
                    }
                    case 93: {
                        t = Tokens.CLOSE_SQUARE;
                        break;
                    }
                    case 43: {
                        t = this.pullPlusEquals();
                        break;
                    }
                    default: {
                        t = null;
                        break;
                    }
                }
                if (t == null) {
                    if ("0123456789-".indexOf(c) >= 0) {
                        t = this.pullNumber(c);
                    }
                    else {
                        if ("$\"{}[]:=,+#`^?!@*&\\".indexOf(c) >= 0) {
                            throw this.problem(asString(c), "Reserved character '" + asString(c) + "' is not allowed outside quotes", true);
                        }
                        this.putBack(c);
                        t = this.pullUnquotedText();
                    }
                }
            }
            if (t == null) {
                throw new ConfigException.BugOrBroken("bug: failed to generate next token");
            }
            return t;
        }
        
        private static boolean isSimpleValue(final Token t) {
            return Tokens.isSubstitution(t) || Tokens.isUnquotedText(t) || Tokens.isValue(t);
        }
        
        private void queueNextToken() throws ProblemException {
            final Token t = this.pullNextToken(this.whitespaceSaver);
            final Token whitespace = this.whitespaceSaver.check(t, this.origin, this.lineNumber);
            if (whitespace != null) {
                this.tokens.add(whitespace);
            }
            this.tokens.add(t);
        }
        
        @Override
        public boolean hasNext() {
            return !this.tokens.isEmpty();
        }
        
        @Override
        public Token next() {
            final Token t = this.tokens.remove();
            if (this.tokens.isEmpty() && t != Tokens.END) {
                try {
                    this.queueNextToken();
                }
                catch (ProblemException e) {
                    this.tokens.add(e.problem());
                }
                if (this.tokens.isEmpty()) {
                    throw new ConfigException.BugOrBroken("bug: tokens queue should not be empty here");
                }
            }
            return t;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Does not make sense to remove items from token stream");
        }
        
        private static class WhitespaceSaver
        {
            private StringBuilder whitespace;
            private boolean lastTokenWasSimpleValue;
            
            WhitespaceSaver() {
                this.whitespace = new StringBuilder();
                this.lastTokenWasSimpleValue = false;
            }
            
            void add(final int c) {
                this.whitespace.appendCodePoint(c);
            }
            
            Token check(final Token t, final ConfigOrigin baseOrigin, final int lineNumber) {
                if (isSimpleValue(t)) {
                    return this.nextIsASimpleValue(baseOrigin, lineNumber);
                }
                return this.nextIsNotASimpleValue(baseOrigin, lineNumber);
            }
            
            private Token nextIsNotASimpleValue(final ConfigOrigin baseOrigin, final int lineNumber) {
                this.lastTokenWasSimpleValue = false;
                return this.createWhitespaceTokenFromSaver(baseOrigin, lineNumber);
            }
            
            private Token nextIsASimpleValue(final ConfigOrigin baseOrigin, final int lineNumber) {
                final Token t = this.createWhitespaceTokenFromSaver(baseOrigin, lineNumber);
                if (!this.lastTokenWasSimpleValue) {
                    this.lastTokenWasSimpleValue = true;
                }
                return t;
            }
            
            private Token createWhitespaceTokenFromSaver(final ConfigOrigin baseOrigin, final int lineNumber) {
                if (this.whitespace.length() > 0) {
                    Token t;
                    if (this.lastTokenWasSimpleValue) {
                        t = Tokens.newUnquotedText(lineOrigin(baseOrigin, lineNumber), this.whitespace.toString());
                    }
                    else {
                        t = Tokens.newIgnoredWhitespace(lineOrigin(baseOrigin, lineNumber), this.whitespace.toString());
                    }
                    this.whitespace.setLength(0);
                    return t;
                }
                return null;
            }
        }
    }
}
