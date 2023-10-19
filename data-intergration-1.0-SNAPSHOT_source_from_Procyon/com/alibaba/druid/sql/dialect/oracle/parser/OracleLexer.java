// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.parser;

import java.util.Map;
import java.util.HashMap;
import com.alibaba.druid.sql.parser.NotAllowCommentException;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.parser.CharTypes;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.Keywords;
import com.alibaba.druid.sql.parser.Lexer;

public class OracleLexer extends Lexer
{
    public static final Keywords DEFAULT_ORACLE_KEYWORDS;
    
    public OracleLexer(final char[] input, final int inputLength, final boolean skipComment) {
        super(input, inputLength, skipComment);
        super.keywords = OracleLexer.DEFAULT_ORACLE_KEYWORDS;
    }
    
    public OracleLexer(final String input) {
        super(input);
        this.skipComment = true;
        this.keepComments = true;
        super.keywords = OracleLexer.DEFAULT_ORACLE_KEYWORDS;
    }
    
    public OracleLexer(final String input, final SQLParserFeature... features) {
        super(input);
        this.skipComment = true;
        this.keepComments = true;
        super.keywords = OracleLexer.DEFAULT_ORACLE_KEYWORDS;
        for (final SQLParserFeature feature : features) {
            this.config(feature, true);
        }
    }
    
    @Override
    public void scanVariable() {
        final char c0 = this.ch;
        if (c0 != ':' && c0 != '#' && c0 != '$') {
            throw new ParserException("illegal variable. " + this.info());
        }
        this.mark = this.pos;
        this.bufPos = 1;
        boolean quoteFlag = false;
        boolean mybatisFlag = false;
        char c2 = this.charAt(this.pos + 1);
        if (c0 == ':' && c2 == ' ') {
            ++this.pos;
            this.bufPos = 2;
            c2 = this.charAt(this.pos + 1);
        }
        if (c2 == '\"') {
            ++this.pos;
            ++this.bufPos;
            quoteFlag = true;
        }
        else if (c2 == '{') {
            ++this.pos;
            ++this.bufPos;
            mybatisFlag = true;
        }
        char ch;
        if (c0 == ':' && c2 >= '0' && c2 <= '9') {
            while (true) {
                ch = this.charAt(++this.pos);
                if (ch < '0') {
                    break;
                }
                if (ch > '9') {
                    break;
                }
                ++this.bufPos;
            }
        }
        else {
            while (true) {
                ch = this.charAt(++this.pos);
                if (!CharTypes.isIdentifierChar(ch) && ch != ':') {
                    break;
                }
                ++this.bufPos;
            }
        }
        if (quoteFlag) {
            if (ch != '\"') {
                throw new ParserException("syntax error. " + this.info());
            }
            ++this.pos;
            ++this.bufPos;
        }
        else if (mybatisFlag) {
            if (ch != '}') {
                throw new ParserException("syntax error" + this.info());
            }
            ++this.pos;
            ++this.bufPos;
        }
        this.ch = this.charAt(this.pos);
        this.stringVal = this.addSymbol();
        final Token tok = this.keywords.getKeyword(this.stringVal);
        if (tok != null) {
            this.token = tok;
        }
        else {
            this.token = Token.VARIANT;
        }
    }
    
    @Override
    protected void scanVariable_at() {
        this.scanChar();
        if (this.ch == '@') {
            this.scanChar();
            this.token = Token.MONKEYS_AT_AT;
        }
        else {
            this.token = Token.MONKEYS_AT;
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
            if (this.ch == '+') {
                isHint = true;
                this.scanChar();
                ++this.bufPos;
            }
            while (!this.isEOF()) {
                if (this.ch == '*' && this.charAt(this.pos + 1) == '/') {
                    this.bufPos += 2;
                    this.scanChar();
                    this.scanChar();
                    break;
                }
                this.scanChar();
                ++this.bufPos;
            }
            if (isHint) {
                this.stringVal = this.subString(this.mark + startHintSp, this.bufPos - startHintSp - 1);
                this.token = Token.HINT;
            }
            else {
                this.stringVal = this.subString(this.mark, this.bufPos + 1);
                this.token = Token.MULTI_LINE_COMMENT;
                ++this.commentCount;
                if (this.keepComments) {
                    this.addComment(this.stringVal);
                }
            }
            if (this.token != Token.HINT && !this.isAllowComment()) {
                throw new NotAllowCommentException();
            }
        }
        else {
            if (!this.isAllowComment()) {
                throw new NotAllowCommentException();
            }
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
                    this.stringVal = this.subString(this.mark, (this.ch != '\u001a') ? this.bufPos : (this.bufPos + 1));
                    this.token = Token.LINE_COMMENT;
                    ++this.commentCount;
                    if (this.keepComments) {
                        this.addComment(this.stringVal);
                    }
                    this.endOfComment = this.isEOF();
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
    public void scanNumber() {
        this.mark = this.pos;
        if (this.ch == '-') {
            ++this.bufPos;
            this.ch = this.charAt(++this.pos);
        }
        while (this.ch >= '0' && this.ch <= '9') {
            ++this.bufPos;
            this.ch = this.charAt(++this.pos);
        }
        boolean isDouble = false;
        if (this.ch == '.') {
            if (this.charAt(this.pos + 1) == '.') {
                this.token = Token.LITERAL_INT;
                return;
            }
            ++this.bufPos;
            this.ch = this.charAt(++this.pos);
            isDouble = true;
            while (this.ch >= '0' && this.ch <= '9') {
                ++this.bufPos;
                this.ch = this.charAt(++this.pos);
            }
        }
        if ((this.ch == 'e' || this.ch == 'E') && Lexer.isDigit2(this.charAt(this.pos + 1))) {
            ++this.bufPos;
            this.ch = this.charAt(++this.pos);
            if (this.ch == '+' || this.ch == '-') {
                ++this.bufPos;
                this.ch = this.charAt(++this.pos);
            }
            while (this.ch >= '0' && this.ch <= '9') {
                ++this.bufPos;
                this.ch = this.charAt(++this.pos);
            }
            isDouble = true;
        }
        if (this.ch == 'f' || this.ch == 'F') {
            this.token = Token.BINARY_FLOAT;
            this.scanChar();
            return;
        }
        if (this.ch == 'd' || this.ch == 'D') {
            this.token = Token.BINARY_DOUBLE;
            this.scanChar();
            return;
        }
        if (isDouble) {
            this.token = Token.LITERAL_FLOAT;
        }
        else {
            this.token = Token.LITERAL_INT;
        }
    }
    
    static {
        final Map<String, Token> map = new HashMap<String, Token>();
        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());
        map.put("BEGIN", Token.BEGIN);
        map.put("COMMENT", Token.COMMENT);
        map.put("COMMIT", Token.COMMIT);
        map.put("CONNECT", Token.CONNECT);
        map.put("CONTINUE", Token.CONTINUE);
        map.put("CROSS", Token.CROSS);
        map.put("CURSOR", Token.CURSOR);
        map.put("DECLARE", Token.DECLARE);
        map.put("ERRORS", Token.ERRORS);
        map.put("EXCEPTION", Token.EXCEPTION);
        map.put("EXCLUSIVE", Token.EXCLUSIVE);
        map.put("EXTRACT", Token.EXTRACT);
        map.put("GOTO", Token.GOTO);
        map.put("IF", Token.IF);
        map.put("ELSIF", Token.ELSIF);
        map.put("LIMIT", Token.LIMIT);
        map.put("LOOP", Token.LOOP);
        map.put("MATCHED", Token.MATCHED);
        map.put("MERGE", Token.MERGE);
        map.put("MODE", Token.MODE);
        map.put("NOWAIT", Token.NOWAIT);
        map.put("OF", Token.OF);
        map.put("PRIOR", Token.PRIOR);
        map.put("REJECT", Token.REJECT);
        map.put("RETURN", Token.RETURN);
        map.put("RETURNING", Token.RETURNING);
        map.put("SAVEPOINT", Token.SAVEPOINT);
        map.put("SESSION", Token.SESSION);
        map.put("SHARE", Token.SHARE);
        map.put("START", Token.START);
        map.put("SYSDATE", Token.SYSDATE);
        map.put("UNLIMITED", Token.UNLIMITED);
        map.put("USING", Token.USING);
        map.put("WAIT", Token.WAIT);
        map.put("WITH", Token.WITH);
        map.put("PCTFREE", Token.PCTFREE);
        map.put("INITRANS", Token.INITRANS);
        map.put("MAXTRANS", Token.MAXTRANS);
        map.put("SEGMENT", Token.SEGMENT);
        map.put("CREATION", Token.CREATION);
        map.put("IMMEDIATE", Token.IMMEDIATE);
        map.put("DEFERRED", Token.DEFERRED);
        map.put("STORAGE", Token.STORAGE);
        map.put("NEXT", Token.NEXT);
        map.put("MINEXTENTS", Token.MINEXTENTS);
        map.put("MAXEXTENTS", Token.MAXEXTENTS);
        map.put("MAXSIZE", Token.MAXSIZE);
        map.put("PCTINCREASE", Token.PCTINCREASE);
        map.put("FLASH_CACHE", Token.FLASH_CACHE);
        map.put("CELL_FLASH_CACHE", Token.CELL_FLASH_CACHE);
        map.put("NONE", Token.NONE);
        map.put("LOB", Token.LOB);
        map.put("STORE", Token.STORE);
        map.put("ROW", Token.ROW);
        map.put("CHUNK", Token.CHUNK);
        map.put("CACHE", Token.CACHE);
        map.put("NOCACHE", Token.NOCACHE);
        map.put("LOGGING", Token.LOGGING);
        map.put("NOCOMPRESS", Token.NOCOMPRESS);
        map.put("KEEP_DUPLICATES", Token.KEEP_DUPLICATES);
        map.put("EXCEPTIONS", Token.EXCEPTIONS);
        map.put("PURGE", Token.PURGE);
        map.put("INITIALLY", Token.INITIALLY);
        map.put("FETCH", Token.FETCH);
        map.put("TABLESPACE", Token.TABLESPACE);
        map.put("PARTITION", Token.PARTITION);
        map.put("TRUE", Token.TRUE);
        map.put("FALSE", Token.FALSE);
        map.put("\uff0c", Token.COMMA);
        map.put("\uff08", Token.LPAREN);
        map.put("\uff09", Token.RPAREN);
        DEFAULT_ORACLE_KEYWORDS = new Keywords(map);
    }
}
