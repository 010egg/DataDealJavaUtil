// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.parser;

import java.util.Map;
import java.util.HashMap;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.parser.CharTypes;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.Keywords;
import com.alibaba.druid.sql.parser.Lexer;

public class OdpsLexer extends Lexer
{
    public static final Keywords DEFAULT_ODPS_KEYWORDS;
    
    public OdpsLexer(final String input, final SQLParserFeature... features) {
        super(input);
        this.init();
        this.dbType = DbType.odps;
        super.keywords = OdpsLexer.DEFAULT_ODPS_KEYWORDS;
        this.skipComment = true;
        this.keepComments = false;
        for (final SQLParserFeature feature : features) {
            this.config(feature, true);
        }
    }
    
    public OdpsLexer(final String input, final boolean skipComment, final boolean keepComments) {
        super(input, skipComment);
        this.init();
        this.dbType = DbType.odps;
        this.skipComment = skipComment;
        this.keepComments = keepComments;
        super.keywords = OdpsLexer.DEFAULT_ODPS_KEYWORDS;
    }
    
    public OdpsLexer(final String input, final CommentHandler commentHandler) {
        super(input, commentHandler);
        this.init();
        this.dbType = DbType.odps;
        super.keywords = OdpsLexer.DEFAULT_ODPS_KEYWORDS;
    }
    
    private void init() {
        if (this.ch == '\u3011' || this.ch == '\u2008' || this.ch == '\uff0c' || this.ch == '\uff1a' || this.ch == '\u3001' || this.ch == '\u200c' || this.ch == '\uff1b') {
            this.ch = this.charAt(++this.pos);
        }
        if (this.ch == '\u4e0a' && this.charAt(this.pos + 1) == '\u4f20') {
            this.pos += 2;
            this.ch = this.charAt(this.pos);
            while (CharTypes.isWhitespace(this.ch)) {
                this.ch = this.charAt(++this.pos);
            }
        }
    }
    
    @Override
    public void scanComment() {
        this.scanHiveComment();
    }
    
    @Override
    public void scanIdentifier() {
        this.hash_lower = 0L;
        this.hash = 0L;
        final char first = this.ch;
        if (first == '`') {
            this.mark = this.pos;
            this.bufPos = 1;
            while (true) {
                char ch = this.charAt(++this.pos);
                if (ch == '`') {
                    ++this.bufPos;
                    ch = this.charAt(++this.pos);
                    if (ch != '`') {
                        this.ch = this.charAt(this.pos);
                        this.stringVal = this.subString(this.mark, this.bufPos);
                        this.token = Token.IDENTIFIER;
                        return;
                    }
                    ch = this.charAt(++this.pos);
                }
                else {
                    if (ch == '\u001a') {
                        throw new ParserException("illegal identifier. " + this.info());
                    }
                    ++this.bufPos;
                }
            }
        }
        else {
            final boolean firstFlag = CharTypes.isFirstIdentifierChar(first) || this.ch == '\u00e5' || this.ch == '\u00df' || this.ch == '\u00e7';
            if (!firstFlag) {
                throw new ParserException("illegal identifier. " + this.info());
            }
            this.mark = this.pos;
            this.bufPos = 1;
            char ch2;
            while (true) {
                ch2 = this.charAt(++this.pos);
                if (ch2 != '\u00f3' && ch2 != '\u00e5' && ch2 != '\u00e9' && ch2 != '\u00ed' && ch2 != '\u00df' && ch2 != '\u00fc' && !CharTypes.isIdentifierChar(ch2)) {
                    if (ch2 == '{' && this.charAt(this.pos - 1) == '$') {
                        final int endIndex = this.text.indexOf(125, this.pos);
                        if (endIndex != -1) {
                            this.bufPos += endIndex - this.pos + 1;
                            this.pos = endIndex;
                            continue;
                        }
                    }
                    if (ch2 == '-' && this.bufPos > 7 && this.text.regionMatches(false, this.mark, "ALIYUN$", 0, 7)) {
                        continue;
                    }
                    break;
                }
                else {
                    if (ch2 == '\uff1b') {
                        break;
                    }
                    ++this.bufPos;
                }
            }
            this.ch = this.charAt(this.pos);
            if (ch2 == '@') {
                ++this.bufPos;
                while (true) {
                    ch2 = this.charAt(++this.pos);
                    if (ch2 != '-' && ch2 != '.' && !CharTypes.isIdentifierChar(ch2)) {
                        break;
                    }
                    ++this.bufPos;
                }
            }
            this.ch = this.charAt(this.pos);
            final int LEN = "USING#CODE".length();
            if (this.bufPos == LEN && this.text.regionMatches(this.mark, "USING#CODE", 0, LEN)) {
                this.bufPos = "USING".length();
                this.pos -= 5;
                this.ch = this.charAt(this.pos);
            }
            this.stringVal = this.addSymbol();
            final Token tok = this.keywords.getKeyword(this.stringVal);
            if (tok != null) {
                this.token = tok;
            }
            else {
                this.token = Token.IDENTIFIER;
            }
        }
    }
    
    @Override
    public void scanVariable() {
        if (this.ch == ':') {
            this.token = Token.COLON;
            this.ch = this.charAt(++this.pos);
            return;
        }
        if (this.ch == '#' && (this.charAt(this.pos + 1) == 'C' || this.charAt(this.pos + 1) == 'c') && (this.charAt(this.pos + 2) == 'O' || this.charAt(this.pos + 2) == 'o') && (this.charAt(this.pos + 3) == 'D' || this.charAt(this.pos + 3) == 'd') && (this.charAt(this.pos + 4) == 'E' || this.charAt(this.pos + 4) == 'e')) {
            int p1 = this.text.indexOf("#END CODE", this.pos + 1);
            final int p2 = this.text.indexOf("#end code", this.pos + 1);
            if (p1 == -1) {
                p1 = p2;
            }
            else if (p1 > p2 && p2 != -1) {
                p1 = p2;
            }
            if (p1 != -1) {
                final int end = p1 + "#END CODE".length();
                this.stringVal = this.text.substring(this.pos, end);
                this.token = Token.CODE;
                this.pos = end;
                this.ch = this.charAt(this.pos);
                return;
            }
        }
        super.scanVariable();
    }
    
    @Override
    protected void scanVariable_at() {
        this.scanVariable();
    }
    
    @Override
    protected final void scanString() {
        this.scanString2();
    }
    
    static {
        final Map<String, Token> map = new HashMap<String, Token>();
        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());
        map.put("SHOW", Token.SHOW);
        map.put("PARTITION", Token.PARTITION);
        map.put("PARTITIONED", Token.PARTITIONED);
        map.put("OVERWRITE", Token.OVERWRITE);
        map.put("OVER", Token.OVER);
        map.put("LIMIT", Token.LIMIT);
        map.put("IF", Token.IF);
        map.put("DISTRIBUTE", Token.DISTRIBUTE);
        map.put("TRUE", Token.TRUE);
        map.put("FALSE", Token.FALSE);
        map.put("RLIKE", Token.RLIKE);
        map.put("DIV", Token.DIV);
        map.put("LATERAL", Token.LATERAL);
        map.put("\uff1b", Token.SEMI);
        DEFAULT_ODPS_KEYWORDS = new Keywords(map);
    }
}
