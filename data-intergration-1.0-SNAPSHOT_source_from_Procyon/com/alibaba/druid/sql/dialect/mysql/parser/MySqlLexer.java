// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.parser;

import java.util.Map;
import java.util.HashMap;
import com.alibaba.druid.sql.parser.CharTypes;
import com.alibaba.druid.sql.parser.NotAllowCommentException;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Keywords;
import com.alibaba.druid.sql.parser.SymbolTable;
import com.alibaba.druid.sql.parser.Lexer;

public class MySqlLexer extends Lexer
{
    public static SymbolTable quoteTable;
    public static final Keywords DEFAULT_MYSQL_KEYWORDS;
    private static final boolean[] identifierFlags;
    
    public MySqlLexer(final char[] input, final int inputLength, final boolean skipComment) {
        super(input, inputLength, skipComment);
        this.dbType = DbType.mysql;
        super.keywords = MySqlLexer.DEFAULT_MYSQL_KEYWORDS;
    }
    
    public MySqlLexer(final String input) {
        this(input, true, true);
    }
    
    public MySqlLexer(final String input, final SQLParserFeature... features) {
        super(input, true);
        this.dbType = DbType.mysql;
        this.keepComments = true;
        super.keywords = MySqlLexer.DEFAULT_MYSQL_KEYWORDS;
        for (final SQLParserFeature feature : features) {
            this.config(feature, true);
        }
    }
    
    public MySqlLexer(final String input, final boolean skipComment, final boolean keepComments) {
        super(input, skipComment);
        this.dbType = DbType.mysql;
        this.skipComment = skipComment;
        this.keepComments = keepComments;
        super.keywords = MySqlLexer.DEFAULT_MYSQL_KEYWORDS;
    }
    
    @Override
    public void scanSharp() {
        if (this.ch != '#') {
            throw new ParserException("illegal stat. " + this.info());
        }
        if (this.charAt(this.pos + 1) == '{') {
            this.scanVariable();
            return;
        }
        final Token lastToken = this.token;
        this.scanChar();
        this.mark = this.pos;
        this.bufPos = 0;
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
                this.stringVal = this.subString(this.mark - 1, this.bufPos + 1);
                this.token = Token.LINE_COMMENT;
                ++this.commentCount;
                if (this.keepComments) {
                    this.addComment(this.stringVal);
                }
                if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
                    return;
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
    
    @Override
    public void scanVariable() {
        if (this.ch != ':' && this.ch != '#' && this.ch != '$') {
            throw new ParserException("illegal variable. " + this.info());
        }
        this.mark = this.pos;
        this.bufPos = 1;
        if (this.charAt(this.pos + 1) == '`') {
            ++this.pos;
            ++this.bufPos;
            while (true) {
                char ch = this.charAt(++this.pos);
                if (ch == '`') {
                    ++this.bufPos;
                    ch = this.charAt(++this.pos);
                    this.ch = this.charAt(this.pos);
                    this.stringVal = this.subString(this.mark, this.bufPos);
                    this.token = Token.VARIANT;
                    break;
                }
                if (ch == '\u001a') {
                    throw new ParserException("illegal identifier. " + this.info());
                }
                ++this.bufPos;
            }
        }
        else if (this.charAt(this.pos + 1) == '{') {
            ++this.pos;
            ++this.bufPos;
            while (true) {
                char ch = this.charAt(++this.pos);
                if (ch == '}') {
                    ++this.bufPos;
                    ch = this.charAt(++this.pos);
                    this.ch = this.charAt(this.pos);
                    this.stringVal = this.subString(this.mark, this.bufPos);
                    this.token = Token.VARIANT;
                    break;
                }
                if (ch == '\u001a') {
                    throw new ParserException("illegal identifier. " + this.info());
                }
                ++this.bufPos;
            }
        }
        else {
            while (true) {
                this.ch = this.charAt(++this.pos);
                if (!isIdentifierChar(this.ch)) {
                    break;
                }
                ++this.bufPos;
            }
        }
        this.ch = this.charAt(this.pos);
        this.stringVal = this.subString(this.mark, this.bufPos);
        this.token = Token.VARIANT;
    }
    
    @Override
    protected void scanVariable_at() {
        if (this.ch != '@') {
            throw new ParserException("illegal variable. " + this.info());
        }
        this.mark = this.pos;
        this.bufPos = 1;
        if (this.charAt(this.pos + 1) == '@') {
            this.ch = this.charAt(++this.pos);
            ++this.bufPos;
        }
        if (this.charAt(this.pos + 1) == '`') {
            ++this.pos;
            ++this.bufPos;
            while (true) {
                final char ch = this.charAt(++this.pos);
                if (ch == '`') {
                    ++this.bufPos;
                    ++this.pos;
                    this.ch = this.charAt(this.pos);
                    this.stringVal = this.subString(this.mark, this.bufPos);
                    this.token = Token.VARIANT;
                    break;
                }
                if (ch == '\u001a') {
                    throw new ParserException("illegal identifier. " + this.info());
                }
                ++this.bufPos;
            }
        }
        else {
            while (true) {
                this.ch = this.charAt(++this.pos);
                if (!isIdentifierChar(this.ch)) {
                    break;
                }
                ++this.bufPos;
            }
        }
        this.ch = this.charAt(this.pos);
        this.stringVal = this.subString(this.mark, this.bufPos);
        this.token = Token.VARIANT;
    }
    
    @Override
    public void scanIdentifier() {
        this.hash_lower = 0L;
        this.hash = 0L;
        final char first = this.ch;
        if (first != 'U' || !this.isEnabled(SQLParserFeature.Presto) || this.charAt(this.pos + 1) != '&' || this.charAt(this.pos + 2) != '\'') {
            if ((this.ch == 'b' || this.ch == 'B') && this.charAt(this.pos + 1) == '\'') {
                int i = 2;
                final int mark = this.pos + 2;
                char ch;
                while (true) {
                    ch = this.charAt(this.pos + i);
                    if (ch != '0' && ch != '1') {
                        break;
                    }
                    ++i;
                }
                if (ch == '\'') {
                    this.bufPos += i;
                    this.pos += i + 1;
                    this.stringVal = this.subString(mark, i - 2);
                    this.ch = this.charAt(this.pos);
                    this.token = Token.BITS;
                    return;
                }
                if (ch == '\u001a') {
                    throw new ParserException("illegal identifier. " + this.info());
                }
            }
            if (this.ch == '`') {
                this.mark = this.pos;
                this.bufPos = 1;
                final int startPos = this.pos + 1;
                this.hash_lower = -3750763034362895579L;
                this.hash = -3750763034362895579L;
                for (int j = startPos; j < this.text.length(); ++j) {
                    final char ch2 = this.text.charAt(j);
                    if ('`' == ch2) {
                        if (j + 1 >= this.text.length() || '`' != this.text.charAt(j + 1)) {
                            final int quoteIndex = j;
                            this.stringVal = MySqlLexer.quoteTable.addSymbol(this.text, this.pos, quoteIndex + 1 - this.pos, this.hash);
                            this.pos = quoteIndex + 1;
                            this.ch = this.charAt(this.pos);
                            this.token = Token.IDENTIFIER;
                            return;
                        }
                        ++j;
                    }
                    this.hash_lower ^= ((ch2 >= 'A' && ch2 <= 'Z') ? (ch2 + ' ') : ch2);
                    this.hash_lower *= 1099511628211L;
                    this.hash ^= ch2;
                    this.hash *= 1099511628211L;
                }
                throw new ParserException("illegal identifier. " + this.info());
            }
            final boolean firstFlag = CharTypes.isFirstIdentifierChar(first);
            if (!firstFlag) {
                throw new ParserException("illegal identifier. " + this.info());
            }
            this.hash_lower = -3750763034362895579L;
            this.hash = -3750763034362895579L;
            this.hash_lower ^= ((this.ch >= 'A' && this.ch <= 'Z') ? (this.ch + ' ') : this.ch);
            this.hash_lower *= 1099511628211L;
            this.hash ^= this.ch;
            this.hash *= 1099511628211L;
            this.mark = this.pos;
            this.bufPos = 1;
            char ch3 = '\0';
            while (true) {
                ch3 = this.charAt(++this.pos);
                if (!isIdentifierChar(ch3)) {
                    break;
                }
                ++this.bufPos;
                this.hash_lower ^= ((ch3 >= 'A' && ch3 <= 'Z') ? (ch3 + ' ') : ch3);
                this.hash_lower *= 1099511628211L;
                this.hash ^= ch3;
                this.hash *= 1099511628211L;
            }
            this.ch = this.charAt(this.pos);
            if (this.bufPos == 1) {
                this.token = Token.IDENTIFIER;
                this.stringVal = CharTypes.valueOf(first);
                if (this.stringVal == null) {
                    this.stringVal = Character.toString(first);
                }
                return;
            }
            final Token tok = this.keywords.getKeyword(this.hash_lower);
            if (tok != null) {
                this.token = tok;
                if (this.token == Token.IDENTIFIER) {
                    this.stringVal = SymbolTable.global.addSymbol(this.text, this.mark, this.bufPos, this.hash);
                }
                else {
                    this.stringVal = null;
                }
            }
            else {
                this.token = Token.IDENTIFIER;
                this.stringVal = SymbolTable.global.addSymbol(this.text, this.mark, this.bufPos, this.hash);
            }
            return;
        }
        this.initBuff(32);
        this.pos += 3;
        while (true) {
            this.ch = this.charAt(this.pos);
            if (this.isEOF()) {
                this.lexError("unclosed.str.lit", new Object[0]);
                return;
            }
            if (this.ch == '\'') {
                this.ch = this.charAt(++this.pos);
                this.stringVal = new String(this.buf, 0, this.bufPos);
                this.token = Token.LITERAL_CHARS;
                return;
            }
            if (this.ch == '\\') {
                final char c1 = this.charAt(++this.pos);
                final char c2 = this.charAt(++this.pos);
                final char c3 = this.charAt(++this.pos);
                final char c4 = this.charAt(++this.pos);
                String tmp;
                if (this.ch == '+') {
                    final char c5 = this.charAt(++this.pos);
                    final char c6 = this.charAt(++this.pos);
                    tmp = new String(new char[] { c1, c2, c3, c4, c5, c6 });
                }
                else {
                    tmp = new String(new char[] { c1, c2, c3, c4 });
                }
                final int intVal = Integer.parseInt(tmp, 16);
                this.putChar((char)intVal);
            }
            else {
                this.putChar(this.ch);
            }
            ++this.pos;
        }
    }
    
    @Override
    protected final void scanString() {
        this.scanString2();
    }
    
    public void skipFirstHintsOrMultiCommentAndNextToken() {
        int starIndex = this.pos + 2;
        while (true) {
            starIndex = this.text.indexOf(42, starIndex);
            if (starIndex == -1 || starIndex == this.text.length() - 1) {
                this.token = Token.ERROR;
                return;
            }
            final int slashIndex = starIndex + 1;
            if (this.charAt(slashIndex) == '/') {
                this.pos = slashIndex + 1;
                this.ch = this.text.charAt(this.pos);
                if (this.pos >= this.text.length() - 6) {
                    this.nextToken();
                    return;
                }
                final int pos_6 = this.pos + 6;
                final char c0 = this.ch;
                final char c2 = this.text.charAt(this.pos + 1);
                final char c3 = this.text.charAt(this.pos + 2);
                final char c4 = this.text.charAt(this.pos + 3);
                final char c5 = this.text.charAt(this.pos + 4);
                final char c6 = this.text.charAt(this.pos + 5);
                final char c7 = this.text.charAt(pos_6);
                if (c0 == 's' && c2 == 'e' && c3 == 'l' && c4 == 'e' && c5 == 'c' && c6 == 't' && c7 == ' ') {
                    this.comments = null;
                    this.reset(pos_6, ' ', Token.SELECT);
                    return;
                }
                if (c0 == 'i' && c2 == 'n' && c3 == 's' && c4 == 'e' && c5 == 'r' && c6 == 't' && c7 == ' ') {
                    this.comments = null;
                    this.reset(pos_6, ' ', Token.INSERT);
                    return;
                }
                if (c0 == 'u' && c2 == 'p' && c3 == 'd' && c4 == 'a' && c5 == 't' && c6 == 'e' && c7 == ' ') {
                    this.comments = null;
                    this.reset(pos_6, ' ', Token.UPDATE);
                    return;
                }
                if (c0 == 'd' && c2 == 'e' && c3 == 'l' && c4 == 'e' && c5 == 't' && c6 == 'e' && c7 == ' ') {
                    this.comments = null;
                    this.reset(pos_6, ' ', Token.DELETE);
                    return;
                }
                if (c0 == 'S' && c2 == 'E' && c3 == 'L' && c4 == 'E' && c5 == 'C' && c6 == 'T' && c7 == ' ') {
                    this.comments = null;
                    this.reset(pos_6, ' ', Token.SELECT);
                    return;
                }
                if (c0 == 'I' && c2 == 'N' && c3 == 'S' && c4 == 'E' && c5 == 'R' && c6 == 'T' && c7 == ' ') {
                    this.comments = null;
                    this.reset(pos_6, ' ', Token.INSERT);
                    return;
                }
                if (c0 == 'U' && c2 == 'P' && c3 == 'D' && c4 == 'A' && c5 == 'T' && c6 == 'E' && c7 == ' ') {
                    this.comments = null;
                    this.reset(pos_6, ' ', Token.UPDATE);
                    return;
                }
                if (c0 == 'D' && c2 == 'E' && c3 == 'L' && c4 == 'E' && c5 == 'T' && c6 == 'E' && c7 == ' ') {
                    this.comments = null;
                    this.reset(pos_6, ' ', Token.DELETE);
                    return;
                }
                this.nextToken();
            }
            else {
                ++starIndex;
            }
        }
    }
    
    @Override
    public void scanComment() {
        final Token lastToken = this.token;
        if (this.ch == '-') {
            final char before_1 = (this.pos == 0) ? ' ' : this.charAt(this.pos - 1);
            final char next_2 = this.charAt(this.pos + 2);
            if (this.isDigit(next_2)) {
                this.scanChar();
                this.token = Token.SUB;
                return;
            }
            Label_0197: {
                if (before_1 == ' ' || (before_1 != '-' && before_1 != '+')) {
                    if (next_2 == ' ' || next_2 == '\u001a') {
                        break Label_0197;
                    }
                    if (next_2 == '\n') {
                        break Label_0197;
                    }
                }
                if ((before_1 == '-' || before_1 == '+') && next_2 == ' ') {
                    throw new ParserException("illegal state. " + this.info());
                }
                if (this.ch == '-') {
                    this.scanChar();
                    this.token = Token.SUB;
                    return;
                }
                if (this.ch == '+') {
                    this.scanChar();
                    this.token = Token.PLUS;
                    return;
                }
            }
        }
        else if (this.ch != '/') {
            throw new ParserException("illegal state. " + this.info());
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
            if (this.ch == '!' || this.ch == '+' || (this.ch == 'T' && this.charAt(this.pos + 1) == 'D' && this.charAt(this.pos + 2) == 'D' && this.charAt(this.pos + 3) == 'L' && this.isEnabled(SQLParserFeature.TDDLHint))) {
                isHint = true;
                this.scanChar();
                ++this.bufPos;
            }
            int starIndex = this.pos;
            int depth = 1;
            while (true) {
                char ch = this.charAt(starIndex);
                if (ch == '/' && this.charAt(starIndex + 1) == '*') {
                    starIndex += 2;
                    ch = this.charAt(starIndex);
                    if (ch == '!' || ch == '+') {
                        ++depth;
                        ++starIndex;
                        continue;
                    }
                }
                else if (ch == '*' && this.charAt(starIndex + 1) == '/') {
                    if (0 != --depth) {
                        starIndex += 2;
                        continue;
                    }
                    if (isHint) {
                        this.stringVal = this.subString(this.mark + startHintSp, starIndex - startHintSp - this.mark);
                        this.token = Token.HINT;
                    }
                    else {
                        if (!this.optimizedForParameterized) {
                            this.stringVal = this.subString(this.mark, starIndex + 2 - this.mark);
                        }
                        this.token = Token.MULTI_LINE_COMMENT;
                        ++this.commentCount;
                        if (this.keepComments) {
                            this.addComment(this.stringVal = this.subString(this.mark, starIndex + 2 - this.mark));
                        }
                    }
                    this.pos = starIndex + 2;
                    this.ch = this.charAt(this.pos);
                    this.endOfComment = this.isEOF();
                    if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
                        return;
                    }
                    if (!isHint && !this.isAllowComment() && !this.isSafeComment(this.stringVal)) {
                        throw new NotAllowCommentException();
                    }
                    return;
                }
                if (ch == '\u001a') {
                    this.token = Token.ERROR;
                    return;
                }
                ++starIndex;
            }
        }
        else if (this.ch == '!' && this.isEnabled(SQLParserFeature.TDDLHint)) {
            this.scanChar();
            ++this.bufPos;
            while (this.ch == ' ') {
                this.scanChar();
                ++this.bufPos;
            }
            final int startHintSp2 = this.bufPos + 1;
            int starIndex2 = this.pos;
            while (true) {
                starIndex2 = this.text.indexOf(42, starIndex2);
                if (starIndex2 == -1 || starIndex2 == this.text.length() - 1) {
                    this.token = Token.ERROR;
                    return;
                }
                if (this.charAt(starIndex2 + 1) == '/') {
                    this.stringVal = this.subString(this.mark + startHintSp2, starIndex2 - startHintSp2 - this.mark);
                    this.token = Token.HINT;
                    this.pos = starIndex2 + 2;
                    this.ch = this.charAt(this.pos);
                    this.endOfComment = this.isEOF();
                    if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
                        return;
                    }
                    if (!this.isAllowComment() && !this.isSafeComment(this.stringVal)) {
                        throw new NotAllowCommentException();
                    }
                }
                else {
                    ++starIndex2;
                }
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
                    this.stringVal = this.subString(this.mark, this.bufPos);
                    this.token = Token.LINE_COMMENT;
                    ++this.commentCount;
                    if (this.keepComments) {
                        this.addComment(this.stringVal);
                    }
                    if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
                        return;
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
    
    public static boolean isIdentifierChar(final char c) {
        if (c <= MySqlLexer.identifierFlags.length) {
            return MySqlLexer.identifierFlags[c];
        }
        return c != '\u3000' && c != '\uff0c';
    }
    
    static {
        MySqlLexer.quoteTable = new SymbolTable(8192);
        final Map<String, Token> map = new HashMap<String, Token>();
        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());
        map.put("DUAL", Token.DUAL);
        map.put("FALSE", Token.FALSE);
        map.put("IF", Token.IF);
        map.put("KILL", Token.KILL);
        map.put("LIMIT", Token.LIMIT);
        map.put("TRUE", Token.TRUE);
        map.put("BINARY", Token.BINARY);
        map.put("SHOW", Token.SHOW);
        map.put("CACHE", Token.CACHE);
        map.put("ANALYZE", Token.ANALYZE);
        map.put("OPTIMIZE", Token.OPTIMIZE);
        map.put("ROW", Token.ROW);
        map.put("BEGIN", Token.BEGIN);
        map.put("END", Token.END);
        map.put("DIV", Token.DIV);
        map.put("MERGE", Token.MERGE);
        map.put("PARTITION", Token.PARTITION);
        map.put("CONTINUE", Token.CONTINUE);
        map.put("UNDO", Token.UNDO);
        map.put("SQLSTATE", Token.SQLSTATE);
        map.put("CONDITION", Token.CONDITION);
        map.put("MOD", Token.MOD);
        map.put("CONTAINS", Token.CONTAINS);
        map.put("RLIKE", Token.RLIKE);
        map.put("FULLTEXT", Token.FULLTEXT);
        DEFAULT_MYSQL_KEYWORDS = new Keywords(map);
        identifierFlags = new boolean[256];
        for (char c = '\0'; c < MySqlLexer.identifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                MySqlLexer.identifierFlags[c] = true;
            }
            else if (c >= 'a' && c <= 'z') {
                MySqlLexer.identifierFlags[c] = true;
            }
            else if (c >= '0' && c <= '9') {
                MySqlLexer.identifierFlags[c] = true;
            }
        }
        MySqlLexer.identifierFlags[95] = true;
        MySqlLexer.identifierFlags[36] = true;
    }
}
