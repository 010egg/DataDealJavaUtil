// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.json;

import com.alibaba.druid.sql.parser.CharTypes;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class JSONParser
{
    private String text;
    private int index;
    private char ch;
    private Token token;
    private String stringValue;
    private long longValue;
    private double doubleValue;
    
    public JSONParser(final String text) {
        this.index = 0;
        this.text = text;
        this.ch = text.charAt(0);
        this.nextToken();
    }
    
    public Object parse() {
        if (this.token == Token.LBRACE) {
            return this.parseMap();
        }
        if (this.token == Token.INT) {
            Object value;
            if (this.longValue >= -2147483648L && this.longValue <= 2147483647L) {
                value = (int)this.longValue;
            }
            else {
                value = this.longValue;
            }
            this.nextToken();
            return value;
        }
        if (this.token == Token.DOUBLE) {
            final Object value = this.doubleValue;
            this.nextToken();
            return value;
        }
        if (this.token == Token.STRING) {
            final Object value = this.stringValue;
            this.nextToken();
            return value;
        }
        if (this.token == Token.LBRACKET) {
            return this.parseArray();
        }
        if (this.token == Token.TRUE) {
            this.nextToken();
            return true;
        }
        if (this.token == Token.FALSE) {
            this.nextToken();
            return false;
        }
        if (this.token == Token.NULL) {
            this.nextToken();
            return null;
        }
        throw new IllegalArgumentException("illegal token : " + this.token);
    }
    
    public List<Object> parseArray() {
        this.accept(Token.LBRACKET);
        final ArrayList<Object> list = new ArrayList<Object>();
        while (this.token != Token.RBRACKET) {
            if (this.token == Token.COMMA) {
                this.nextToken();
            }
            else {
                final Object item = this.parse();
                list.add(item);
            }
        }
        this.accept(Token.RBRACKET);
        return list;
    }
    
    public Map<String, Object> parseMap() {
        this.accept(Token.LBRACE);
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        while (this.token != Token.RBRACE) {
            if (this.token == Token.COMMA) {
                this.nextToken();
            }
            else {
                if (this.token != Token.STRING) {
                    throw new IllegalArgumentException("illegal json, " + this.token + " : " + this.text);
                }
                final String key = this.stringValue;
                this.nextToken();
                this.accept(Token.COLON);
                final Object value = this.parse();
                map.put(key, value);
            }
        }
        this.accept(Token.RBRACE);
        return map;
    }
    
    void accept(final Token token) {
        if (this.token == token) {
            this.nextToken();
            return;
        }
        throw new IllegalArgumentException("illegal token : " + this.token + ", expect " + token);
    }
    
    final void nextToken() {
        if (this.index == Integer.MIN_VALUE) {
            this.token = Token.EOF;
            return;
        }
        while (CharTypes.isWhitespace(this.ch)) {
            this.nextChar();
        }
        if (this.index >= this.text.length()) {
            this.token = Token.EOF;
            return;
        }
        switch (this.ch) {
            case '{': {
                this.token = Token.LBRACE;
                this.nextChar();
                break;
            }
            case '}': {
                this.token = Token.RBRACE;
                this.nextChar();
                break;
            }
            case '[': {
                this.token = Token.LBRACKET;
                this.nextChar();
                break;
            }
            case ']': {
                this.token = Token.RBRACKET;
                this.nextChar();
                break;
            }
            case ',': {
                this.token = Token.COMMA;
                this.nextChar();
                break;
            }
            case ':': {
                this.token = Token.COLON;
                this.nextChar();
                break;
            }
            case '\"': {
                this.scanString();
                break;
            }
            default: {
                if (isDigit(this.ch) || this.ch == '-') {
                    this.scanDigit();
                    return;
                }
                if (this.text.startsWith("null", this.index)) {
                    this.token = Token.NULL;
                    this.index += 3;
                    this.nextChar();
                    return;
                }
                if (this.text.startsWith("true", this.index)) {
                    this.token = Token.TRUE;
                    this.index += 3;
                    this.nextChar();
                    return;
                }
                if (this.text.startsWith("false", this.index)) {
                    this.token = Token.FALSE;
                    this.index += 4;
                    this.nextChar();
                    return;
                }
                throw new IllegalArgumentException("illegal json char : " + this.ch);
            }
        }
    }
    
    private void scanDigit() {
        boolean isNegate = false;
        if (this.ch == '-') {
            isNegate = true;
            this.nextChar();
        }
        int dotCount = 0;
        final StringBuilder digitBuf = new StringBuilder();
        while (true) {
            digitBuf.append(this.ch);
            this.nextChar();
            if (this.ch == '.') {
                ++dotCount;
                digitBuf.append('.');
                this.nextChar();
            }
            else {
                if (!isDigit(this.ch)) {
                    break;
                }
                continue;
            }
        }
        if (dotCount == 0) {
            long longValue = Long.parseLong(digitBuf.toString());
            if (isNegate) {
                longValue = -longValue;
            }
            this.longValue = longValue;
            this.token = Token.INT;
        }
        else {
            double doubleValue = Double.parseDouble(digitBuf.toString());
            if (isNegate) {
                doubleValue = -doubleValue;
            }
            this.doubleValue = doubleValue;
            this.token = Token.DOUBLE;
        }
    }
    
    private void scanString() {
        this.nextChar();
        final StringBuilder strBuf = new StringBuilder();
        while (this.index < this.text.length()) {
            if (this.ch == '\"') {
                this.nextChar();
                this.stringValue = strBuf.toString();
                this.token = Token.STRING;
                return;
            }
            if (this.ch == '\\') {
                this.nextChar();
                if (this.ch == '\"' || this.ch == '\\' || this.ch == '/') {
                    strBuf.append(this.ch);
                }
                else if (this.ch == 'n') {
                    strBuf.append('\n');
                }
                else if (this.ch == 'r') {
                    strBuf.append('\r');
                }
                else if (this.ch == 'b') {
                    strBuf.append('\b');
                }
                else if (this.ch == 'f') {
                    strBuf.append('\f');
                }
                else if (this.ch == 't') {
                    strBuf.append('\t');
                }
                else {
                    if (this.ch != 'u') {
                        throw new IllegalArgumentException("illegal string : " + (Object)strBuf);
                    }
                    this.nextChar();
                    final char c1 = this.ch;
                    this.nextChar();
                    final char c2 = this.ch;
                    this.nextChar();
                    final char c3 = this.ch;
                    this.nextChar();
                    final char c4 = this.ch;
                    final int val = Integer.parseInt(new String(new char[] { c1, c2, c3, c4 }), 16);
                    strBuf.append((char)val);
                }
            }
            else {
                strBuf.append(this.ch);
            }
            this.nextChar();
        }
        throw new IllegalArgumentException("illegal string : " + (Object)strBuf);
    }
    
    static boolean isDigit(final char ch) {
        return ch >= '0' && ch <= '9';
    }
    
    void nextChar() {
        ++this.index;
        if (this.index >= this.text.length()) {
            this.index = Integer.MIN_VALUE;
            return;
        }
        this.ch = this.text.charAt(this.index);
    }
    
    enum Token
    {
        INT, 
        DOUBLE, 
        STRING, 
        BOOLEAN, 
        TRUE, 
        FALSE, 
        NULL, 
        EOF, 
        LBRACE("{"), 
        RBRACE("}"), 
        LBRACKET("["), 
        RBRACKET("]"), 
        COMMA(","), 
        COLON(":");
        
        public final String name;
        
        private Token() {
            this(null);
        }
        
        private Token(final String name) {
            this.name = name;
        }
    }
}
