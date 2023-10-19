// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import com.typesafe.config.ConfigSyntax;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigOrigin;
import java.util.Iterator;

final class ConfigDocumentParser
{
    static ConfigNodeRoot parse(final Iterator<Token> tokens, final ConfigOrigin origin, final ConfigParseOptions options) {
        final ConfigSyntax syntax = (options.getSyntax() == null) ? ConfigSyntax.CONF : options.getSyntax();
        final ParseContext context = new ParseContext(syntax, origin, tokens);
        return context.parse();
    }
    
    static AbstractConfigNodeValue parseValue(final Iterator<Token> tokens, final ConfigOrigin origin, final ConfigParseOptions options) {
        final ConfigSyntax syntax = (options.getSyntax() == null) ? ConfigSyntax.CONF : options.getSyntax();
        final ParseContext context = new ParseContext(syntax, origin, tokens);
        return context.parseSingleValue();
    }
    
    private static final class ParseContext
    {
        private int lineNumber;
        private final Stack<Token> buffer;
        private final Iterator<Token> tokens;
        private final ConfigSyntax flavor;
        private final ConfigOrigin baseOrigin;
        int equalsCount;
        private final String ExpectingClosingParenthesisError = "expecting a close parentheses ')' here, not: ";
        
        ParseContext(final ConfigSyntax flavor, final ConfigOrigin origin, final Iterator<Token> tokens) {
            this.lineNumber = 1;
            this.buffer = new Stack<Token>();
            this.tokens = tokens;
            this.flavor = flavor;
            this.equalsCount = 0;
            this.baseOrigin = origin;
        }
        
        private Token popToken() {
            if (this.buffer.isEmpty()) {
                return this.tokens.next();
            }
            return this.buffer.pop();
        }
        
        private Token nextToken() {
            final Token t = this.popToken();
            if (this.flavor == ConfigSyntax.JSON) {
                if (Tokens.isUnquotedText(t) && !isUnquotedWhitespace(t)) {
                    throw this.parseError("Token not allowed in valid JSON: '" + Tokens.getUnquotedText(t) + "'");
                }
                if (Tokens.isSubstitution(t)) {
                    throw this.parseError("Substitutions (${} syntax) not allowed in JSON");
                }
            }
            return t;
        }
        
        private Token nextTokenCollectingWhitespace(final Collection<AbstractConfigNode> nodes) {
            Token t;
            while (true) {
                t = this.nextToken();
                if (Tokens.isIgnoredWhitespace(t) || Tokens.isNewline(t) || isUnquotedWhitespace(t)) {
                    nodes.add(new ConfigNodeSingleToken(t));
                    if (!Tokens.isNewline(t)) {
                        continue;
                    }
                    this.lineNumber = t.lineNumber() + 1;
                }
                else {
                    if (!Tokens.isComment(t)) {
                        break;
                    }
                    nodes.add(new ConfigNodeComment(t));
                }
            }
            final int newNumber = t.lineNumber();
            if (newNumber >= 0) {
                this.lineNumber = newNumber;
            }
            return t;
        }
        
        private void putBack(final Token token) {
            this.buffer.push(token);
        }
        
        private boolean checkElementSeparator(final Collection<AbstractConfigNode> nodes) {
            if (this.flavor == ConfigSyntax.JSON) {
                final Token t = this.nextTokenCollectingWhitespace(nodes);
                if (t == Tokens.COMMA) {
                    nodes.add(new ConfigNodeSingleToken(t));
                    return true;
                }
                this.putBack(t);
                return false;
            }
            else {
                boolean sawSeparatorOrNewline = false;
                Token t2 = this.nextToken();
                while (true) {
                    if (Tokens.isIgnoredWhitespace(t2) || isUnquotedWhitespace(t2)) {
                        nodes.add(new ConfigNodeSingleToken(t2));
                    }
                    else if (Tokens.isComment(t2)) {
                        nodes.add(new ConfigNodeComment(t2));
                    }
                    else {
                        if (!Tokens.isNewline(t2)) {
                            break;
                        }
                        sawSeparatorOrNewline = true;
                        ++this.lineNumber;
                        nodes.add(new ConfigNodeSingleToken(t2));
                    }
                    t2 = this.nextToken();
                }
                if (t2 == Tokens.COMMA) {
                    nodes.add(new ConfigNodeSingleToken(t2));
                    return true;
                }
                this.putBack(t2);
                return sawSeparatorOrNewline;
            }
        }
        
        private AbstractConfigNodeValue consolidateValues(final Collection<AbstractConfigNode> nodes) {
            if (this.flavor == ConfigSyntax.JSON) {
                return null;
            }
            final ArrayList<AbstractConfigNode> values = new ArrayList<AbstractConfigNode>();
            int valueCount = 0;
            Token t = this.nextTokenCollectingWhitespace(nodes);
            while (true) {
                AbstractConfigNodeValue v = null;
                if (Tokens.isIgnoredWhitespace(t)) {
                    values.add(new ConfigNodeSingleToken(t));
                    t = this.nextToken();
                }
                else if (Tokens.isValue(t) || Tokens.isUnquotedText(t) || Tokens.isSubstitution(t) || t == Tokens.OPEN_CURLY || t == Tokens.OPEN_SQUARE) {
                    v = this.parseValue(t);
                    ++valueCount;
                    if (v == null) {
                        throw new ConfigException.BugOrBroken("no value");
                    }
                    values.add(v);
                    t = this.nextToken();
                }
                else {
                    this.putBack(t);
                    if (valueCount < 2) {
                        AbstractConfigNodeValue value = null;
                        for (final AbstractConfigNode node : values) {
                            if (node instanceof AbstractConfigNodeValue) {
                                value = (AbstractConfigNodeValue)node;
                            }
                            else if (value == null) {
                                nodes.add(node);
                            }
                            else {
                                this.putBack(new ArrayList<Token>(node.tokens()).get(0));
                            }
                        }
                        return value;
                    }
                    for (int i = values.size() - 1; i >= 0 && values.get(i) instanceof ConfigNodeSingleToken; --i) {
                        this.putBack(values.get(i).token());
                        values.remove(i);
                    }
                    return new ConfigNodeConcatenation(values);
                }
            }
        }
        
        private ConfigException parseError(final String message) {
            return this.parseError(message, null);
        }
        
        private ConfigException parseError(final String message, final Throwable cause) {
            return new ConfigException.Parse(this.baseOrigin.withLineNumber(this.lineNumber), message, cause);
        }
        
        private String addQuoteSuggestion(final String badToken, final String message) {
            return this.addQuoteSuggestion(null, this.equalsCount > 0, badToken, message);
        }
        
        private String addQuoteSuggestion(final Path lastPath, final boolean insideEquals, final String badToken, final String message) {
            final String previousFieldName = (lastPath != null) ? lastPath.render() : null;
            String part;
            if (badToken.equals(Tokens.END.toString())) {
                if (previousFieldName == null) {
                    return message;
                }
                part = message + " (if you intended '" + previousFieldName + "' to be part of a value, instead of a key, try adding double quotes around the whole value";
            }
            else if (previousFieldName != null) {
                part = message + " (if you intended " + badToken + " to be part of the value for '" + previousFieldName + "', try enclosing the value in double quotes";
            }
            else {
                part = message + " (if you intended " + badToken + " to be part of a key or string value, try enclosing the key or value in double quotes";
            }
            if (insideEquals) {
                return part + ", or you may be able to rename the file .properties rather than .conf)";
            }
            return part + ")";
        }
        
        private AbstractConfigNodeValue parseValue(final Token t) {
            AbstractConfigNodeValue v = null;
            final int startingEqualsCount = this.equalsCount;
            if (Tokens.isValue(t) || Tokens.isUnquotedText(t) || Tokens.isSubstitution(t)) {
                v = new ConfigNodeSimpleValue(t);
            }
            else if (t == Tokens.OPEN_CURLY) {
                v = this.parseObject(true);
            }
            else {
                if (t != Tokens.OPEN_SQUARE) {
                    throw this.parseError(this.addQuoteSuggestion(t.toString(), "Expecting a value but got wrong token: " + t));
                }
                v = this.parseArray();
            }
            if (this.equalsCount != startingEqualsCount) {
                throw new ConfigException.BugOrBroken("Bug in config parser: unbalanced equals count");
            }
            return v;
        }
        
        private ConfigNodePath parseKey(final Token token) {
            if (this.flavor == ConfigSyntax.JSON) {
                if (Tokens.isValueWithType(token, ConfigValueType.STRING)) {
                    return PathParser.parsePathNodeExpression(Collections.singletonList(token).iterator(), null);
                }
                throw this.parseError("Expecting close brace } or a field name here, got " + token);
            }
            else {
                final List<Token> expression = new ArrayList<Token>();
                Token t;
                for (t = token; Tokens.isValue(t) || Tokens.isUnquotedText(t); t = this.nextToken()) {
                    expression.add(t);
                }
                if (expression.isEmpty()) {
                    throw this.parseError("expecting a close parentheses ')' here, not: " + t);
                }
                this.putBack(t);
                return PathParser.parsePathNodeExpression(expression.iterator(), null);
            }
        }
        
        private static boolean isIncludeKeyword(final Token t) {
            return Tokens.isUnquotedText(t) && Tokens.getUnquotedText(t).equals("include");
        }
        
        private static boolean isUnquotedWhitespace(final Token t) {
            if (!Tokens.isUnquotedText(t)) {
                return false;
            }
            final String s = Tokens.getUnquotedText(t);
            for (int i = 0; i < s.length(); ++i) {
                final char c = s.charAt(i);
                if (!ConfigImplUtil.isWhitespace(c)) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean isKeyValueSeparatorToken(final Token t) {
            if (this.flavor == ConfigSyntax.JSON) {
                return t == Tokens.COLON;
            }
            return t == Tokens.COLON || t == Tokens.EQUALS || t == Tokens.PLUS_EQUALS;
        }
        
        private ConfigNodeInclude parseInclude(final ArrayList<AbstractConfigNode> children) {
            Token t = this.nextTokenCollectingWhitespace(children);
            if (!Tokens.isUnquotedText(t)) {
                this.putBack(t);
                return this.parseIncludeResource(children, false);
            }
            final String kindText = Tokens.getUnquotedText(t);
            if (!kindText.startsWith("required(")) {
                this.putBack(t);
                return this.parseIncludeResource(children, false);
            }
            final String r = kindText.replaceFirst("required\\(", "");
            if (r.length() > 0) {
                this.putBack(Tokens.newUnquotedText(t.origin(), r));
            }
            children.add(new ConfigNodeSingleToken(t));
            final ConfigNodeInclude res = this.parseIncludeResource(children, true);
            t = this.nextTokenCollectingWhitespace(children);
            if (Tokens.isUnquotedText(t) && Tokens.getUnquotedText(t).equals(")")) {
                return res;
            }
            throw this.parseError("expecting a close parentheses ')' here, not: " + t);
        }
        
        private ConfigNodeInclude parseIncludeResource(final ArrayList<AbstractConfigNode> children, final boolean isRequired) {
            Token t = this.nextTokenCollectingWhitespace(children);
            if (Tokens.isUnquotedText(t)) {
                final String kindText = Tokens.getUnquotedText(t);
                ConfigIncludeKind kind;
                String prefix;
                if (kindText.startsWith("url(")) {
                    kind = ConfigIncludeKind.URL;
                    prefix = "url(";
                }
                else if (kindText.startsWith("file(")) {
                    kind = ConfigIncludeKind.FILE;
                    prefix = "file(";
                }
                else {
                    if (!kindText.startsWith("classpath(")) {
                        throw this.parseError("expecting include parameter to be quoted filename, file(), classpath(), or url(). No spaces are allowed before the open paren. Not expecting: " + t);
                    }
                    kind = ConfigIncludeKind.CLASSPATH;
                    prefix = "classpath(";
                }
                final String r = kindText.replaceFirst("[^(]*\\(", "");
                if (r.length() > 0) {
                    this.putBack(Tokens.newUnquotedText(t.origin(), r));
                }
                children.add(new ConfigNodeSingleToken(t));
                t = this.nextTokenCollectingWhitespace(children);
                if (!Tokens.isValueWithType(t, ConfigValueType.STRING)) {
                    throw this.parseError("expecting include " + prefix + ") parameter to be a quoted string, rather than: " + t);
                }
                children.add(new ConfigNodeSimpleValue(t));
                t = this.nextTokenCollectingWhitespace(children);
                if (Tokens.isUnquotedText(t) && Tokens.getUnquotedText(t).startsWith(")")) {
                    final String rest = Tokens.getUnquotedText(t).substring(1);
                    if (rest.length() > 0) {
                        this.putBack(Tokens.newUnquotedText(t.origin(), rest));
                    }
                    return new ConfigNodeInclude(children, kind, isRequired);
                }
                throw this.parseError("expecting a close parentheses ')' here, not: " + t);
            }
            else {
                if (Tokens.isValueWithType(t, ConfigValueType.STRING)) {
                    children.add(new ConfigNodeSimpleValue(t));
                    return new ConfigNodeInclude(children, ConfigIncludeKind.HEURISTIC, isRequired);
                }
                throw this.parseError("include keyword is not followed by a quoted string, but by: " + t);
            }
        }
        
        private ConfigNodeComplexValue parseObject(final boolean hadOpenCurly) {
            boolean afterComma = false;
            final Path lastPath = null;
            boolean lastInsideEquals = false;
            final ArrayList<AbstractConfigNode> objectNodes = new ArrayList<AbstractConfigNode>();
            final HashMap<String, Boolean> keys = new HashMap<String, Boolean>();
            if (hadOpenCurly) {
                objectNodes.add(new ConfigNodeSingleToken(Tokens.OPEN_CURLY));
            }
            while (true) {
                Token t = this.nextTokenCollectingWhitespace(objectNodes);
                if (t == Tokens.CLOSE_CURLY) {
                    if (this.flavor == ConfigSyntax.JSON && afterComma) {
                        throw this.parseError(this.addQuoteSuggestion(t.toString(), "expecting a field name after a comma, got a close brace } instead"));
                    }
                    if (!hadOpenCurly) {
                        throw this.parseError(this.addQuoteSuggestion(t.toString(), "unbalanced close brace '}' with no open brace"));
                    }
                    objectNodes.add(new ConfigNodeSingleToken(Tokens.CLOSE_CURLY));
                    break;
                }
                else {
                    if (t == Tokens.END && !hadOpenCurly) {
                        this.putBack(t);
                        break;
                    }
                    if (this.flavor != ConfigSyntax.JSON && isIncludeKeyword(t)) {
                        final ArrayList<AbstractConfigNode> includeNodes = new ArrayList<AbstractConfigNode>();
                        includeNodes.add(new ConfigNodeSingleToken(t));
                        objectNodes.add(this.parseInclude(includeNodes));
                        afterComma = false;
                    }
                    else {
                        final ArrayList<AbstractConfigNode> keyValueNodes = new ArrayList<AbstractConfigNode>();
                        final Token keyToken = t;
                        final ConfigNodePath path = this.parseKey(keyToken);
                        keyValueNodes.add(path);
                        final Token afterKey = this.nextTokenCollectingWhitespace(keyValueNodes);
                        boolean insideEquals = false;
                        AbstractConfigNodeValue nextValue;
                        if (this.flavor == ConfigSyntax.CONF && afterKey == Tokens.OPEN_CURLY) {
                            nextValue = this.parseValue(afterKey);
                        }
                        else {
                            if (!this.isKeyValueSeparatorToken(afterKey)) {
                                throw this.parseError(this.addQuoteSuggestion(afterKey.toString(), "Key '" + path.render() + "' may not be followed by token: " + afterKey));
                            }
                            keyValueNodes.add(new ConfigNodeSingleToken(afterKey));
                            if (afterKey == Tokens.EQUALS) {
                                insideEquals = true;
                                ++this.equalsCount;
                            }
                            nextValue = this.consolidateValues(keyValueNodes);
                            if (nextValue == null) {
                                nextValue = this.parseValue(this.nextTokenCollectingWhitespace(keyValueNodes));
                            }
                        }
                        keyValueNodes.add(nextValue);
                        if (insideEquals) {
                            --this.equalsCount;
                        }
                        lastInsideEquals = insideEquals;
                        final String key = path.value().first();
                        final Path remaining = path.value().remainder();
                        if (remaining == null) {
                            final Boolean existing = keys.get(key);
                            if (existing != null && this.flavor == ConfigSyntax.JSON) {
                                throw this.parseError("JSON does not allow duplicate fields: '" + key + "' was already seen");
                            }
                            keys.put(key, true);
                        }
                        else {
                            if (this.flavor == ConfigSyntax.JSON) {
                                throw new ConfigException.BugOrBroken("somehow got multi-element path in JSON mode");
                            }
                            keys.put(key, true);
                        }
                        afterComma = false;
                        objectNodes.add(new ConfigNodeField(keyValueNodes));
                    }
                    if (this.checkElementSeparator(objectNodes)) {
                        afterComma = true;
                    }
                    else {
                        t = this.nextTokenCollectingWhitespace(objectNodes);
                        if (t == Tokens.CLOSE_CURLY) {
                            if (!hadOpenCurly) {
                                throw this.parseError(this.addQuoteSuggestion(lastPath, lastInsideEquals, t.toString(), "unbalanced close brace '}' with no open brace"));
                            }
                            objectNodes.add(new ConfigNodeSingleToken(t));
                            break;
                        }
                        else {
                            if (hadOpenCurly) {
                                throw this.parseError(this.addQuoteSuggestion(lastPath, lastInsideEquals, t.toString(), "Expecting close brace } or a comma, got " + t));
                            }
                            if (t == Tokens.END) {
                                this.putBack(t);
                                break;
                            }
                            throw this.parseError(this.addQuoteSuggestion(lastPath, lastInsideEquals, t.toString(), "Expecting end of input or a comma, got " + t));
                        }
                    }
                }
            }
            return new ConfigNodeObject(objectNodes);
        }
        
        private ConfigNodeComplexValue parseArray() {
            final ArrayList<AbstractConfigNode> children = new ArrayList<AbstractConfigNode>();
            children.add(new ConfigNodeSingleToken(Tokens.OPEN_SQUARE));
            AbstractConfigNodeValue nextValue = this.consolidateValues(children);
            if (nextValue != null) {
                children.add(nextValue);
            }
            else {
                final Token t = this.nextTokenCollectingWhitespace(children);
                if (t == Tokens.CLOSE_SQUARE) {
                    children.add(new ConfigNodeSingleToken(t));
                    return new ConfigNodeArray(children);
                }
                if (!Tokens.isValue(t) && t != Tokens.OPEN_CURLY && t != Tokens.OPEN_SQUARE && !Tokens.isUnquotedText(t) && !Tokens.isSubstitution(t)) {
                    throw this.parseError("List should have ] or a first element after the open [, instead had token: " + t + " (if you want " + t + " to be part of a string value, then double-quote it)");
                }
                nextValue = this.parseValue(t);
                children.add(nextValue);
            }
            while (this.checkElementSeparator(children)) {
                nextValue = this.consolidateValues(children);
                if (nextValue != null) {
                    children.add(nextValue);
                }
                else {
                    final Token t = this.nextTokenCollectingWhitespace(children);
                    if (Tokens.isValue(t) || t == Tokens.OPEN_CURLY || t == Tokens.OPEN_SQUARE || Tokens.isUnquotedText(t) || Tokens.isSubstitution(t)) {
                        nextValue = this.parseValue(t);
                        children.add(nextValue);
                    }
                    else {
                        if (this.flavor == ConfigSyntax.JSON || t != Tokens.CLOSE_SQUARE) {
                            throw this.parseError("List should have had new element after a comma, instead had token: " + t + " (if you want the comma or " + t + " to be part of a string value, then double-quote it)");
                        }
                        this.putBack(t);
                    }
                }
            }
            final Token t = this.nextTokenCollectingWhitespace(children);
            if (t == Tokens.CLOSE_SQUARE) {
                children.add(new ConfigNodeSingleToken(t));
                return new ConfigNodeArray(children);
            }
            throw this.parseError("List should have ended with ] or had a comma, instead had token: " + t + " (if you want " + t + " to be part of a string value, then double-quote it)");
        }
        
        ConfigNodeRoot parse() {
            final ArrayList<AbstractConfigNode> children = new ArrayList<AbstractConfigNode>();
            Token t = this.nextToken();
            if (t != Tokens.START) {
                throw new ConfigException.BugOrBroken("token stream did not begin with START, had " + t);
            }
            t = this.nextTokenCollectingWhitespace(children);
            AbstractConfigNode result = null;
            boolean missingCurly = false;
            if (t == Tokens.OPEN_CURLY || t == Tokens.OPEN_SQUARE) {
                result = this.parseValue(t);
            }
            else if (this.flavor == ConfigSyntax.JSON) {
                if (t == Tokens.END) {
                    throw this.parseError("Empty document");
                }
                throw this.parseError("Document must have an object or array at root, unexpected token: " + t);
            }
            else {
                this.putBack(t);
                missingCurly = true;
                result = this.parseObject(false);
            }
            if (result instanceof ConfigNodeObject && missingCurly) {
                children.addAll(((ConfigNodeComplexValue)result).children());
            }
            else {
                children.add(result);
            }
            t = this.nextTokenCollectingWhitespace(children);
            if (t != Tokens.END) {
                throw this.parseError("Document has trailing tokens after first object or array: " + t);
            }
            if (missingCurly) {
                return new ConfigNodeRoot((Collection<AbstractConfigNode>)Collections.singletonList(new ConfigNodeObject(children)), this.baseOrigin);
            }
            return new ConfigNodeRoot(children, this.baseOrigin);
        }
        
        AbstractConfigNodeValue parseSingleValue() {
            Token t = this.nextToken();
            if (t != Tokens.START) {
                throw new ConfigException.BugOrBroken("token stream did not begin with START, had " + t);
            }
            t = this.nextToken();
            if (Tokens.isIgnoredWhitespace(t) || Tokens.isNewline(t) || isUnquotedWhitespace(t) || Tokens.isComment(t)) {
                throw this.parseError("The value from withValueText cannot have leading or trailing newlines, whitespace, or comments");
            }
            if (t == Tokens.END) {
                throw this.parseError("Empty value");
            }
            if (this.flavor == ConfigSyntax.JSON) {
                final AbstractConfigNodeValue node = this.parseValue(t);
                t = this.nextToken();
                if (t == Tokens.END) {
                    return node;
                }
                throw this.parseError("Parsing JSON and the value set in withValueText was either a concatenation or had trailing whitespace, newlines, or comments");
            }
            else {
                this.putBack(t);
                final ArrayList<AbstractConfigNode> nodes = new ArrayList<AbstractConfigNode>();
                final AbstractConfigNodeValue node2 = this.consolidateValues(nodes);
                t = this.nextToken();
                if (t == Tokens.END) {
                    return node2;
                }
                throw this.parseError("The value from withValueText cannot have leading or trailing newlines, whitespace, or comments");
            }
        }
    }
}
