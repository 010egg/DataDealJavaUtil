// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Collections;
import java.util.List;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Reader;
import java.io.StringReader;
import com.typesafe.config.ConfigSyntax;
import com.typesafe.config.ConfigOrigin;

final class PathParser
{
    static ConfigOrigin apiOrigin;
    
    static ConfigNodePath parsePathNode(final String path) {
        return parsePathNode(path, ConfigSyntax.CONF);
    }
    
    static ConfigNodePath parsePathNode(final String path, final ConfigSyntax flavor) {
        final StringReader reader = new StringReader(path);
        try {
            final Iterator<Token> tokens = Tokenizer.tokenize(PathParser.apiOrigin, reader, flavor);
            tokens.next();
            return parsePathNodeExpression(tokens, PathParser.apiOrigin, path, flavor);
        }
        finally {
            reader.close();
        }
    }
    
    static Path parsePath(final String path) {
        final Path speculated = speculativeFastParsePath(path);
        if (speculated != null) {
            return speculated;
        }
        final StringReader reader = new StringReader(path);
        try {
            final Iterator<Token> tokens = Tokenizer.tokenize(PathParser.apiOrigin, reader, ConfigSyntax.CONF);
            tokens.next();
            return parsePathExpression(tokens, PathParser.apiOrigin, path);
        }
        finally {
            reader.close();
        }
    }
    
    protected static Path parsePathExpression(final Iterator<Token> expression, final ConfigOrigin origin) {
        return parsePathExpression(expression, origin, null, null, ConfigSyntax.CONF);
    }
    
    protected static Path parsePathExpression(final Iterator<Token> expression, final ConfigOrigin origin, final String originalText) {
        return parsePathExpression(expression, origin, originalText, null, ConfigSyntax.CONF);
    }
    
    protected static ConfigNodePath parsePathNodeExpression(final Iterator<Token> expression, final ConfigOrigin origin) {
        return parsePathNodeExpression(expression, origin, null, ConfigSyntax.CONF);
    }
    
    protected static ConfigNodePath parsePathNodeExpression(final Iterator<Token> expression, final ConfigOrigin origin, final String originalText, final ConfigSyntax flavor) {
        final ArrayList<Token> pathTokens = new ArrayList<Token>();
        final Path path = parsePathExpression(expression, origin, originalText, pathTokens, flavor);
        return new ConfigNodePath(path, pathTokens);
    }
    
    protected static Path parsePathExpression(final Iterator<Token> expression, final ConfigOrigin origin, final String originalText, final ArrayList<Token> pathTokens, final ConfigSyntax flavor) {
        final List<Element> buf = new ArrayList<Element>();
        buf.add(new Element("", false));
        if (!expression.hasNext()) {
            throw new ConfigException.BadPath(origin, originalText, "Expecting a field name or path here, but got nothing");
        }
        while (expression.hasNext()) {
            final Token t = expression.next();
            if (pathTokens != null) {
                pathTokens.add(t);
            }
            if (Tokens.isIgnoredWhitespace(t)) {
                continue;
            }
            if (Tokens.isValueWithType(t, ConfigValueType.STRING)) {
                final AbstractConfigValue v = Tokens.getValue(t);
                final String s = v.transformToString();
                addPathText(buf, true, s);
            }
            else {
                if (t == Tokens.END) {
                    continue;
                }
                String text;
                if (Tokens.isValue(t)) {
                    final AbstractConfigValue v2 = Tokens.getValue(t);
                    if (pathTokens != null) {
                        pathTokens.remove(pathTokens.size() - 1);
                        pathTokens.addAll(splitTokenOnPeriod(t, flavor));
                    }
                    text = v2.transformToString();
                }
                else {
                    if (!Tokens.isUnquotedText(t)) {
                        throw new ConfigException.BadPath(origin, originalText, "Token not allowed in path expression: " + t + " (you can double-quote this token if you really want it here)");
                    }
                    if (pathTokens != null) {
                        pathTokens.remove(pathTokens.size() - 1);
                        pathTokens.addAll(splitTokenOnPeriod(t, flavor));
                    }
                    text = Tokens.getUnquotedText(t);
                }
                addPathText(buf, false, text);
            }
        }
        final PathBuilder pb = new PathBuilder();
        for (final Element e : buf) {
            if (e.sb.length() == 0 && !e.canBeEmpty) {
                throw new ConfigException.BadPath(origin, originalText, "path has a leading, trailing, or two adjacent period '.' (use quoted \"\" empty string if you want an empty element)");
            }
            pb.appendKey(e.sb.toString());
        }
        return pb.result();
    }
    
    private static Collection<Token> splitTokenOnPeriod(final Token t, final ConfigSyntax flavor) {
        final String tokenText = t.tokenText();
        if (tokenText.equals(".")) {
            return Collections.singletonList(t);
        }
        final String[] splitToken = tokenText.split("\\.");
        final ArrayList<Token> splitTokens = new ArrayList<Token>();
        for (final String s : splitToken) {
            if (flavor == ConfigSyntax.CONF) {
                splitTokens.add(Tokens.newUnquotedText(t.origin(), s));
            }
            else {
                splitTokens.add(Tokens.newString(t.origin(), s, "\"" + s + "\""));
            }
            splitTokens.add(Tokens.newUnquotedText(t.origin(), "."));
        }
        if (tokenText.charAt(tokenText.length() - 1) != '.') {
            splitTokens.remove(splitTokens.size() - 1);
        }
        return splitTokens;
    }
    
    private static void addPathText(final List<Element> buf, final boolean wasQuoted, final String newText) {
        final int i = wasQuoted ? -1 : newText.indexOf(46);
        final Element current = buf.get(buf.size() - 1);
        if (i < 0) {
            current.sb.append(newText);
            if (wasQuoted && current.sb.length() == 0) {
                current.canBeEmpty = true;
            }
        }
        else {
            current.sb.append(newText.substring(0, i));
            buf.add(new Element("", false));
            addPathText(buf, false, newText.substring(i + 1));
        }
    }
    
    private static boolean looksUnsafeForFastParser(final String s) {
        boolean lastWasDot = true;
        final int len = s.length();
        if (s.isEmpty()) {
            return true;
        }
        if (s.charAt(0) == '.') {
            return true;
        }
        if (s.charAt(len - 1) == '.') {
            return true;
        }
        for (int i = 0; i < len; ++i) {
            final char c = s.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {
                lastWasDot = false;
            }
            else if (c == '.') {
                if (lastWasDot) {
                    return true;
                }
                lastWasDot = true;
            }
            else {
                if (c != '-') {
                    return true;
                }
                if (lastWasDot) {
                    return true;
                }
            }
        }
        return lastWasDot;
    }
    
    private static Path fastPathBuild(final Path tail, final String s, final int end) {
        final int splitAt = s.lastIndexOf(46, end - 1);
        final ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(Tokens.newUnquotedText(null, s));
        final Path withOneMoreElement = new Path(s.substring(splitAt + 1, end), tail);
        if (splitAt < 0) {
            return withOneMoreElement;
        }
        return fastPathBuild(withOneMoreElement, s, splitAt);
    }
    
    private static Path speculativeFastParsePath(final String path) {
        final String s = ConfigImplUtil.unicodeTrim(path);
        if (looksUnsafeForFastParser(s)) {
            return null;
        }
        return fastPathBuild(null, s, s.length());
    }
    
    static {
        PathParser.apiOrigin = SimpleConfigOrigin.newSimple("path parameter");
    }
    
    static class Element
    {
        StringBuilder sb;
        boolean canBeEmpty;
        
        Element(final String initial, final boolean canBeEmpty) {
            this.canBeEmpty = canBeEmpty;
            this.sb = new StringBuilder(initial);
        }
        
        @Override
        public String toString() {
            return "Element(" + this.sb.toString() + "," + this.canBeEmpty + ")";
        }
    }
}
