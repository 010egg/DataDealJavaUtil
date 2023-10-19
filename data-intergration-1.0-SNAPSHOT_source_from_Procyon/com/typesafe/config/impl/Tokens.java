// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Iterator;
import com.typesafe.config.ConfigOrigin;
import java.util.List;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigException;

final class Tokens
{
    static final Token START;
    static final Token END;
    static final Token COMMA;
    static final Token EQUALS;
    static final Token COLON;
    static final Token OPEN_CURLY;
    static final Token CLOSE_CURLY;
    static final Token OPEN_SQUARE;
    static final Token CLOSE_SQUARE;
    static final Token PLUS_EQUALS;
    
    static boolean isValue(final Token token) {
        return token instanceof Value;
    }
    
    static AbstractConfigValue getValue(final Token token) {
        if (token instanceof Value) {
            return ((Value)token).value();
        }
        throw new ConfigException.BugOrBroken("tried to get value of non-value token " + token);
    }
    
    static boolean isValueWithType(final Token t, final ConfigValueType valueType) {
        return isValue(t) && getValue(t).valueType() == valueType;
    }
    
    static boolean isNewline(final Token token) {
        return token instanceof Line;
    }
    
    static boolean isProblem(final Token token) {
        return token instanceof Problem;
    }
    
    static String getProblemWhat(final Token token) {
        if (token instanceof Problem) {
            return ((Problem)token).what();
        }
        throw new ConfigException.BugOrBroken("tried to get problem what from " + token);
    }
    
    static String getProblemMessage(final Token token) {
        if (token instanceof Problem) {
            return ((Problem)token).message();
        }
        throw new ConfigException.BugOrBroken("tried to get problem message from " + token);
    }
    
    static boolean getProblemSuggestQuotes(final Token token) {
        if (token instanceof Problem) {
            return ((Problem)token).suggestQuotes();
        }
        throw new ConfigException.BugOrBroken("tried to get problem suggestQuotes from " + token);
    }
    
    static Throwable getProblemCause(final Token token) {
        if (token instanceof Problem) {
            return ((Problem)token).cause();
        }
        throw new ConfigException.BugOrBroken("tried to get problem cause from " + token);
    }
    
    static boolean isComment(final Token token) {
        return token instanceof Comment;
    }
    
    static String getCommentText(final Token token) {
        if (token instanceof Comment) {
            return ((Comment)token).text();
        }
        throw new ConfigException.BugOrBroken("tried to get comment text from " + token);
    }
    
    static boolean isUnquotedText(final Token token) {
        return token instanceof UnquotedText;
    }
    
    static String getUnquotedText(final Token token) {
        if (token instanceof UnquotedText) {
            return ((UnquotedText)token).value();
        }
        throw new ConfigException.BugOrBroken("tried to get unquoted text from " + token);
    }
    
    static boolean isIgnoredWhitespace(final Token token) {
        return token instanceof IgnoredWhitespace;
    }
    
    static boolean isSubstitution(final Token token) {
        return token instanceof Substitution;
    }
    
    static List<Token> getSubstitutionPathExpression(final Token token) {
        if (token instanceof Substitution) {
            return ((Substitution)token).value();
        }
        throw new ConfigException.BugOrBroken("tried to get substitution from " + token);
    }
    
    static boolean getSubstitutionOptional(final Token token) {
        if (token instanceof Substitution) {
            return ((Substitution)token).optional();
        }
        throw new ConfigException.BugOrBroken("tried to get substitution optionality from " + token);
    }
    
    static Token newLine(final ConfigOrigin origin) {
        return new Line(origin);
    }
    
    static Token newProblem(final ConfigOrigin origin, final String what, final String message, final boolean suggestQuotes, final Throwable cause) {
        return new Problem(origin, what, message, suggestQuotes, cause);
    }
    
    static Token newCommentDoubleSlash(final ConfigOrigin origin, final String text) {
        return new Comment.DoubleSlashComment(origin, text);
    }
    
    static Token newCommentHash(final ConfigOrigin origin, final String text) {
        return new Comment.HashComment(origin, text);
    }
    
    static Token newUnquotedText(final ConfigOrigin origin, final String s) {
        return new UnquotedText(origin, s);
    }
    
    static Token newIgnoredWhitespace(final ConfigOrigin origin, final String s) {
        return new IgnoredWhitespace(origin, s);
    }
    
    static Token newSubstitution(final ConfigOrigin origin, final boolean optional, final List<Token> expression) {
        return new Substitution(origin, optional, expression);
    }
    
    static Token newValue(final AbstractConfigValue value) {
        return new Value(value);
    }
    
    static Token newValue(final AbstractConfigValue value, final String origText) {
        return new Value(value, origText);
    }
    
    static Token newString(final ConfigOrigin origin, final String value, final String origText) {
        return newValue(new ConfigString.Quoted(origin, value), origText);
    }
    
    static Token newInt(final ConfigOrigin origin, final int value, final String origText) {
        return newValue(ConfigNumber.newNumber(origin, value, origText), origText);
    }
    
    static Token newDouble(final ConfigOrigin origin, final double value, final String origText) {
        return newValue(ConfigNumber.newNumber(origin, value, origText), origText);
    }
    
    static Token newLong(final ConfigOrigin origin, final long value, final String origText) {
        return newValue(ConfigNumber.newNumber(origin, value, origText), origText);
    }
    
    static Token newNull(final ConfigOrigin origin) {
        return newValue(new ConfigNull(origin), "null");
    }
    
    static Token newBoolean(final ConfigOrigin origin, final boolean value) {
        return newValue(new ConfigBoolean(origin, value), "" + value);
    }
    
    static {
        START = Token.newWithoutOrigin(TokenType.START, "start of file", "");
        END = Token.newWithoutOrigin(TokenType.END, "end of file", "");
        COMMA = Token.newWithoutOrigin(TokenType.COMMA, "','", ",");
        EQUALS = Token.newWithoutOrigin(TokenType.EQUALS, "'='", "=");
        COLON = Token.newWithoutOrigin(TokenType.COLON, "':'", ":");
        OPEN_CURLY = Token.newWithoutOrigin(TokenType.OPEN_CURLY, "'{'", "{");
        CLOSE_CURLY = Token.newWithoutOrigin(TokenType.CLOSE_CURLY, "'}'", "}");
        OPEN_SQUARE = Token.newWithoutOrigin(TokenType.OPEN_SQUARE, "'['", "[");
        CLOSE_SQUARE = Token.newWithoutOrigin(TokenType.CLOSE_SQUARE, "']'", "]");
        PLUS_EQUALS = Token.newWithoutOrigin(TokenType.PLUS_EQUALS, "'+='", "+=");
    }
    
    private static class Value extends Token
    {
        private final AbstractConfigValue value;
        
        Value(final AbstractConfigValue value) {
            this(value, null);
        }
        
        Value(final AbstractConfigValue value, final String origText) {
            super(TokenType.VALUE, value.origin(), origText);
            this.value = value;
        }
        
        AbstractConfigValue value() {
            return this.value;
        }
        
        @Override
        public String toString() {
            if (this.value().resolveStatus() == ResolveStatus.RESOLVED) {
                return "'" + this.value().unwrapped() + "' (" + this.value.valueType().name() + ")";
            }
            return "'<unresolved value>' (" + this.value.valueType().name() + ")";
        }
        
        @Override
        protected boolean canEqual(final Object other) {
            return other instanceof Value;
        }
        
        @Override
        public boolean equals(final Object other) {
            return super.equals(other) && ((Value)other).value.equals(this.value);
        }
        
        @Override
        public int hashCode() {
            return 41 * (41 + super.hashCode()) + this.value.hashCode();
        }
    }
    
    private static class Line extends Token
    {
        Line(final ConfigOrigin origin) {
            super(TokenType.NEWLINE, origin);
        }
        
        @Override
        public String toString() {
            return "'\\n'@" + this.lineNumber();
        }
        
        @Override
        protected boolean canEqual(final Object other) {
            return other instanceof Line;
        }
        
        @Override
        public boolean equals(final Object other) {
            return super.equals(other) && ((Line)other).lineNumber() == this.lineNumber();
        }
        
        @Override
        public int hashCode() {
            return 41 * (41 + super.hashCode()) + this.lineNumber();
        }
        
        @Override
        public String tokenText() {
            return "\n";
        }
    }
    
    private static class UnquotedText extends Token
    {
        private final String value;
        
        UnquotedText(final ConfigOrigin origin, final String s) {
            super(TokenType.UNQUOTED_TEXT, origin);
            this.value = s;
        }
        
        String value() {
            return this.value;
        }
        
        @Override
        public String toString() {
            return "'" + this.value + "'";
        }
        
        @Override
        protected boolean canEqual(final Object other) {
            return other instanceof UnquotedText;
        }
        
        @Override
        public boolean equals(final Object other) {
            return super.equals(other) && ((UnquotedText)other).value.equals(this.value);
        }
        
        @Override
        public int hashCode() {
            return 41 * (41 + super.hashCode()) + this.value.hashCode();
        }
        
        @Override
        public String tokenText() {
            return this.value;
        }
    }
    
    private static class IgnoredWhitespace extends Token
    {
        private final String value;
        
        IgnoredWhitespace(final ConfigOrigin origin, final String s) {
            super(TokenType.IGNORED_WHITESPACE, origin);
            this.value = s;
        }
        
        @Override
        public String toString() {
            return "'" + this.value + "' (WHITESPACE)";
        }
        
        @Override
        protected boolean canEqual(final Object other) {
            return other instanceof IgnoredWhitespace;
        }
        
        @Override
        public boolean equals(final Object other) {
            return super.equals(other) && ((IgnoredWhitespace)other).value.equals(this.value);
        }
        
        @Override
        public int hashCode() {
            return 41 * (41 + super.hashCode()) + this.value.hashCode();
        }
        
        @Override
        public String tokenText() {
            return this.value;
        }
    }
    
    private static class Problem extends Token
    {
        private final String what;
        private final String message;
        private final boolean suggestQuotes;
        private final Throwable cause;
        
        Problem(final ConfigOrigin origin, final String what, final String message, final boolean suggestQuotes, final Throwable cause) {
            super(TokenType.PROBLEM, origin);
            this.what = what;
            this.message = message;
            this.suggestQuotes = suggestQuotes;
            this.cause = cause;
        }
        
        String what() {
            return this.what;
        }
        
        String message() {
            return this.message;
        }
        
        boolean suggestQuotes() {
            return this.suggestQuotes;
        }
        
        Throwable cause() {
            return this.cause;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('\'');
            sb.append(this.what);
            sb.append('\'');
            sb.append(" (");
            sb.append(this.message);
            sb.append(")");
            return sb.toString();
        }
        
        @Override
        protected boolean canEqual(final Object other) {
            return other instanceof Problem;
        }
        
        @Override
        public boolean equals(final Object other) {
            return super.equals(other) && ((Problem)other).what.equals(this.what) && ((Problem)other).message.equals(this.message) && ((Problem)other).suggestQuotes == this.suggestQuotes && ConfigImplUtil.equalsHandlingNull(((Problem)other).cause, this.cause);
        }
        
        @Override
        public int hashCode() {
            int h = 41 * (41 + super.hashCode());
            h = 41 * (h + this.what.hashCode());
            h = 41 * (h + this.message.hashCode());
            h = 41 * (h + Boolean.valueOf(this.suggestQuotes).hashCode());
            if (this.cause != null) {
                h = 41 * (h + this.cause.hashCode());
            }
            return h;
        }
    }
    
    private abstract static class Comment extends Token
    {
        private final String text;
        
        Comment(final ConfigOrigin origin, final String text) {
            super(TokenType.COMMENT, origin);
            this.text = text;
        }
        
        String text() {
            return this.text;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("'#");
            sb.append(this.text);
            sb.append("' (COMMENT)");
            return sb.toString();
        }
        
        @Override
        protected boolean canEqual(final Object other) {
            return other instanceof Comment;
        }
        
        @Override
        public boolean equals(final Object other) {
            return super.equals(other) && ((Comment)other).text.equals(this.text);
        }
        
        @Override
        public int hashCode() {
            int h = 41 * (41 + super.hashCode());
            h = 41 * (h + this.text.hashCode());
            return h;
        }
        
        static final class DoubleSlashComment extends Comment
        {
            DoubleSlashComment(final ConfigOrigin origin, final String text) {
                super(origin, text);
            }
            
            @Override
            public String tokenText() {
                return "//" + this.text;
            }
        }
        
        static final class HashComment extends Comment
        {
            HashComment(final ConfigOrigin origin, final String text) {
                super(origin, text);
            }
            
            @Override
            public String tokenText() {
                return "#" + this.text;
            }
        }
    }
    
    private static class Substitution extends Token
    {
        private final boolean optional;
        private final List<Token> value;
        
        Substitution(final ConfigOrigin origin, final boolean optional, final List<Token> expression) {
            super(TokenType.SUBSTITUTION, origin);
            this.optional = optional;
            this.value = expression;
        }
        
        boolean optional() {
            return this.optional;
        }
        
        List<Token> value() {
            return this.value;
        }
        
        @Override
        public String tokenText() {
            return "${" + (this.optional ? "?" : "") + Tokenizer.render(this.value.iterator()) + "}";
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            for (final Token t : this.value) {
                sb.append(t.toString());
            }
            return "'${" + sb.toString() + "}'";
        }
        
        @Override
        protected boolean canEqual(final Object other) {
            return other instanceof Substitution;
        }
        
        @Override
        public boolean equals(final Object other) {
            return super.equals(other) && ((Substitution)other).value.equals(this.value);
        }
        
        @Override
        public int hashCode() {
            return 41 * (41 + super.hashCode()) + this.value.hashCode();
        }
    }
}
