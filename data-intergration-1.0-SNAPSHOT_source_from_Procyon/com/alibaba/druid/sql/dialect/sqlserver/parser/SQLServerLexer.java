// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.parser;

import java.util.Map;
import java.util.HashMap;
import com.alibaba.druid.sql.parser.NotAllowCommentException;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.Keywords;
import com.alibaba.druid.sql.parser.Lexer;

public class SQLServerLexer extends Lexer
{
    public static final Keywords DEFAULT_SQL_SERVER_KEYWORDS;
    
    public SQLServerLexer(final char[] input, final int inputLength, final boolean skipComment) {
        super(input, inputLength, skipComment);
        super.keywords = SQLServerLexer.DEFAULT_SQL_SERVER_KEYWORDS;
    }
    
    public SQLServerLexer(final String input) {
        super(input);
        super.keywords = SQLServerLexer.DEFAULT_SQL_SERVER_KEYWORDS;
    }
    
    public SQLServerLexer(final String input, final SQLParserFeature... features) {
        super(input);
        super.keywords = SQLServerLexer.DEFAULT_SQL_SERVER_KEYWORDS;
        for (final SQLParserFeature feature : features) {
            this.config(feature, true);
        }
    }
    
    @Override
    public void scanComment() {
        if (this.ch != '/' && this.ch != '-') {
            throw new IllegalStateException();
        }
        this.mark = this.pos;
        this.bufPos = 0;
        this.scanChar();
        if (this.ch == '*') {
            this.scanChar();
            ++this.bufPos;
            while (this.ch == ' ') {
                this.scanChar();
                ++this.bufPos;
            }
            boolean isHint = false;
            final int startHintSp = this.bufPos + 1;
            if (this.ch == '!') {
                isHint = true;
                this.scanChar();
                ++this.bufPos;
            }
            while (this.ch != '*' || this.charAt(this.pos + 1) != '/') {
                this.scanChar();
                ++this.bufPos;
            }
            this.bufPos += 2;
            this.scanChar();
            this.scanChar();
            if (isHint) {
                this.stringVal = this.subString(this.mark + startHintSp, this.bufPos - startHintSp - 1);
                this.token = Token.HINT;
            }
            else {
                this.stringVal = this.subString(this.mark, this.bufPos);
                this.token = Token.MULTI_LINE_COMMENT;
                ++this.commentCount;
                if (this.keepComments) {
                    this.addComment(this.stringVal);
                }
            }
            if (this.token != Token.HINT && !this.isAllowComment() && !this.isSafeComment(this.stringVal)) {
                throw new NotAllowCommentException();
            }
        }
        else {
            if (this.ch != '/' && this.ch != '-') {
                return;
            }
            this.scanChar();
            ++this.bufPos;
            while (true) {
                while (this.ch != '\r') {
                    if (this.ch != '\u001a') {
                        if (this.ch != '\n') {
                            this.scanChar();
                            ++this.bufPos;
                            continue;
                        }
                        this.scanChar();
                        ++this.bufPos;
                    }
                    this.stringVal = this.subString(this.mark + 1, this.bufPos);
                    this.token = Token.LINE_COMMENT;
                    ++this.commentCount;
                    if (this.keepComments) {
                        this.addComment(this.stringVal);
                    }
                    this.endOfComment = this.isEOF();
                    if (!this.isAllowComment() && (this.isEOF() || !this.isSafeComment(this.stringVal))) {
                        throw new NotAllowCommentException();
                    }
                    return;
                }
                if (this.charAt(this.pos + 1) == '\n') {
                    this.bufPos += 2;
                    this.scanChar();
                    continue;
                }
                ++this.bufPos;
                continue;
            }
        }
    }
    
    @Override
    protected void scanLBracket() {
        this.mark = this.pos;
        if (this.buf == null) {
            this.buf = new char[32];
        }
        while (!this.isEOF()) {
            this.ch = this.charAt(++this.pos);
            if (this.ch == ']') {
                this.scanChar();
                this.token = Token.IDENTIFIER;
                this.stringVal = this.subString(this.mark, this.bufPos + 2);
                return;
            }
            if (this.bufPos == this.buf.length) {
                this.putChar(this.ch);
            }
            else {
                this.buf[this.bufPos++] = this.ch;
            }
        }
        this.lexError("unclosed.str.lit", new Object[0]);
    }
    
    static {
        final Map<String, Token> map = new HashMap<String, Token>();
        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());
        map.put("CURSOR", Token.CURSOR);
        map.put("TOP", Token.TOP);
        map.put("USE", Token.USE);
        map.put("WITH", Token.WITH);
        map.put("PERCENT", Token.PERCENT);
        map.put("IDENTITY", Token.IDENTITY);
        map.put("DECLARE", Token.DECLARE);
        map.put("IF", Token.IF);
        map.put("ELSE", Token.ELSE);
        map.put("BEGIN", Token.BEGIN);
        map.put("END", Token.END);
        map.put("MERGE", Token.MERGE);
        map.put("USING", Token.USING);
        map.put("MATCHED", Token.MATCHED);
        DEFAULT_SQL_SERVER_KEYWORDS = new Keywords(map);
    }
}
