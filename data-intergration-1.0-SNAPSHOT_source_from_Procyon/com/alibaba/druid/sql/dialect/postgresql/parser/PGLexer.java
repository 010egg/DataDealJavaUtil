// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.parser;

import java.util.Map;
import java.util.HashMap;
import com.alibaba.druid.sql.parser.CharTypes;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.Keywords;
import com.alibaba.druid.sql.parser.Lexer;

public class PGLexer extends Lexer
{
    public static final Keywords DEFAULT_PG_KEYWORDS;
    
    public PGLexer(final String input, final SQLParserFeature... features) {
        super(input, true);
        this.keepComments = true;
        super.keywords = PGLexer.DEFAULT_PG_KEYWORDS;
        super.dbType = DbType.postgresql;
        for (final SQLParserFeature feature : features) {
            this.config(feature, true);
        }
    }
    
    @Override
    protected void scanString() {
        this.mark = this.pos;
        boolean hasSpecial = false;
        while (!this.isEOF()) {
            this.ch = this.charAt(++this.pos);
            if (this.ch == '\\') {
                this.scanChar();
                if (!hasSpecial) {
                    this.initBuff(this.bufPos);
                    this.arraycopy(this.mark + 1, this.buf, 0, this.bufPos);
                    hasSpecial = true;
                }
                this.putChar('\\');
                switch (this.ch) {
                    case '\0': {
                        this.putChar('\0');
                        break;
                    }
                    case '\'': {
                        this.putChar('\'');
                        break;
                    }
                    case '\"': {
                        this.putChar('\"');
                        break;
                    }
                    case 'b': {
                        this.putChar('b');
                        break;
                    }
                    case 'n': {
                        this.putChar('n');
                        break;
                    }
                    case 'r': {
                        this.putChar('r');
                        break;
                    }
                    case 't': {
                        this.putChar('t');
                        break;
                    }
                    case '\\': {
                        this.putChar('\\');
                        break;
                    }
                    case 'Z': {
                        this.putChar('\u001a');
                        break;
                    }
                    default: {
                        this.putChar(this.ch);
                        break;
                    }
                }
                this.scanChar();
            }
            if (this.ch == '\'') {
                this.scanChar();
                if (this.ch != '\'') {
                    this.token = Token.LITERAL_CHARS;
                    if (!hasSpecial) {
                        this.stringVal = this.subString(this.mark + 1, this.bufPos);
                    }
                    else {
                        this.stringVal = new String(this.buf, 0, this.bufPos);
                    }
                    return;
                }
                if (!hasSpecial) {
                    this.initBuff(this.bufPos);
                    this.arraycopy(this.mark + 1, this.buf, 0, this.bufPos);
                    hasSpecial = true;
                }
                this.putChar('\'');
            }
            else if (!hasSpecial) {
                ++this.bufPos;
            }
            else if (this.bufPos == this.buf.length) {
                this.putChar(this.ch);
            }
            else {
                this.buf[this.bufPos++] = this.ch;
            }
        }
        this.lexError("unclosed.str.lit", new Object[0]);
    }
    
    @Override
    public void scanSharp() {
        this.scanChar();
        if (this.ch == '>') {
            this.scanChar();
            if (this.ch == '>') {
                this.scanChar();
                this.token = Token.POUNDGTGT;
            }
            else {
                this.token = Token.POUNDGT;
            }
        }
        else {
            this.token = Token.POUND;
        }
    }
    
    @Override
    protected void scanVariable_at() {
        if (this.ch != '@') {
            throw new ParserException("illegal variable. " + this.info());
        }
        this.mark = this.pos;
        this.bufPos = 1;
        final char c1 = this.charAt(this.pos + 1);
        if (c1 == '@') {
            this.pos += 2;
            this.token = Token.MONKEYS_AT_AT;
            this.ch = this.charAt(++this.pos);
            return;
        }
        if (c1 == '>') {
            this.pos += 2;
            this.token = Token.MONKEYS_AT_GT;
            this.ch = this.charAt(++this.pos);
            return;
        }
        while (true) {
            final char ch = this.charAt(++this.pos);
            if (!CharTypes.isIdentifierChar(ch)) {
                break;
            }
            ++this.bufPos;
        }
        this.ch = this.charAt(this.pos);
        this.stringVal = this.addSymbol();
        this.token = Token.VARIANT;
    }
    
    static {
        final Map<String, Token> map = new HashMap<String, Token>();
        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());
        map.put("BEGIN", Token.BEGIN);
        map.put("CASCADE", Token.CASCADE);
        map.put("CONTINUE", Token.CONTINUE);
        map.put("CURRENT", Token.CURRENT);
        map.put("FETCH", Token.FETCH);
        map.put("FIRST", Token.FIRST);
        map.put("IDENTITY", Token.IDENTITY);
        map.put("LIMIT", Token.LIMIT);
        map.put("NEXT", Token.NEXT);
        map.put("NOWAIT", Token.NOWAIT);
        map.put("OF", Token.OF);
        map.put("OFFSET", Token.OFFSET);
        map.put("ONLY", Token.ONLY);
        map.put("RECURSIVE", Token.RECURSIVE);
        map.put("RESTART", Token.RESTART);
        map.put("RESTRICT", Token.RESTRICT);
        map.put("RETURNING", Token.RETURNING);
        map.put("ROW", Token.ROW);
        map.put("ROWS", Token.ROWS);
        map.put("SHARE", Token.SHARE);
        map.put("SHOW", Token.SHOW);
        map.put("START", Token.START);
        map.put("USING", Token.USING);
        map.put("WINDOW", Token.WINDOW);
        map.put("TRUE", Token.TRUE);
        map.put("FALSE", Token.FALSE);
        map.put("ARRAY", Token.ARRAY);
        map.put("IF", Token.IF);
        map.put("TYPE", Token.TYPE);
        map.put("ILIKE", Token.ILIKE);
        map.put("MERGE", Token.MERGE);
        map.put("MATCHED", Token.MATCHED);
        map.put("PARTITION", Token.PARTITION);
        DEFAULT_PG_KEYWORDS = new Keywords(map);
    }
}
