// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.util.StringUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlLexer;
import com.alibaba.druid.util.FnvHash;
import java.util.Arrays;
import com.alibaba.druid.DbType;
import java.util.List;
import java.util.TimeZone;

public class Lexer
{
    protected static SymbolTable symbols_l2;
    protected int features;
    protected TimeZone timeZone;
    public final String text;
    protected int pos;
    protected int mark;
    protected int numberSale;
    protected boolean numberExp;
    protected char ch;
    protected char[] buf;
    protected int bufPos;
    protected Token token;
    protected Keywords keywords;
    protected String stringVal;
    protected long hash_lower;
    protected long hash;
    protected int commentCount;
    protected List<String> comments;
    protected boolean skipComment;
    private SavePoint savePoint;
    private boolean allowComment;
    private int varIndex;
    protected CommentHandler commentHandler;
    protected boolean endOfComment;
    protected boolean keepComments;
    protected int line;
    protected int lines;
    protected DbType dbType;
    protected boolean optimizedForParameterized;
    protected boolean keepSourceLocation;
    protected int startPos;
    protected int posLine;
    protected int posColumn;
    private static final long MULTMIN_RADIX_TEN = -922337203685477580L;
    private static final long N_MULTMAX_RADIX_TEN = -922337203685477580L;
    private static final int[] digits;
    
    public Lexer(final String input) {
        this(input, null);
    }
    
    public Lexer(final String input, final CommentHandler commentHandler) {
        this(input, true);
        this.commentHandler = commentHandler;
    }
    
    public Lexer(final String input, final CommentHandler commentHandler, final DbType dbType) {
        this(input, true);
        this.commentHandler = commentHandler;
        this.dbType = dbType;
        if (DbType.sqlite == dbType) {
            this.keywords = Keywords.SQLITE_KEYWORDS;
        }
        else if (DbType.dm == dbType) {
            this.keywords = Keywords.DM_KEYWORDS;
        }
    }
    
    public boolean isKeepComments() {
        return this.keepComments;
    }
    
    public void setKeepComments(final boolean keepComments) {
        this.keepComments = keepComments;
    }
    
    public CommentHandler getCommentHandler() {
        return this.commentHandler;
    }
    
    public void setCommentHandler(final CommentHandler commentHandler) {
        this.commentHandler = commentHandler;
    }
    
    public final char charAt(final int index) {
        if (index >= this.text.length()) {
            return '\u001a';
        }
        return this.text.charAt(index);
    }
    
    public final String addSymbol() {
        return this.subString(this.mark, this.bufPos);
    }
    
    public final String subString(final int offset, final int count) {
        return this.text.substring(offset, offset + count);
    }
    
    public final char[] sub_chars(final int offset, final int count) {
        final char[] chars = new char[count];
        this.text.getChars(offset, offset + count, chars, 0);
        return chars;
    }
    
    protected void initBuff(final int size) {
        if (this.buf == null) {
            if (size < 32) {
                this.buf = new char[32];
            }
            else {
                this.buf = new char[size + 32];
            }
        }
        else if (this.buf.length < size) {
            this.buf = Arrays.copyOf(this.buf, size);
        }
    }
    
    public void arraycopy(final int srcPos, final char[] dest, final int destPos, final int length) {
        this.text.getChars(srcPos, srcPos + length, dest, destPos);
    }
    
    public boolean isAllowComment() {
        return this.allowComment;
    }
    
    public void setAllowComment(final boolean allowComment) {
        this.allowComment = allowComment;
    }
    
    public int nextVarIndex() {
        return ++this.varIndex;
    }
    
    public Keywords getKeywords() {
        return this.keywords;
    }
    
    public SavePoint mark() {
        final SavePoint savePoint = new SavePoint();
        savePoint.bp = this.pos;
        savePoint.sp = this.bufPos;
        savePoint.np = this.mark;
        savePoint.ch = this.ch;
        savePoint.token = this.token;
        savePoint.stringVal = this.stringVal;
        savePoint.hash = this.hash;
        savePoint.hash_lower = this.hash_lower;
        return this.savePoint = savePoint;
    }
    
    public void reset(final SavePoint savePoint) {
        this.pos = savePoint.bp;
        this.bufPos = savePoint.sp;
        this.mark = savePoint.np;
        this.ch = savePoint.ch;
        this.token = savePoint.token;
        this.stringVal = savePoint.stringVal;
        this.hash = savePoint.hash;
        this.hash_lower = savePoint.hash_lower;
    }
    
    public void reset() {
        this.reset(this.savePoint);
    }
    
    public void reset(final int pos) {
        this.pos = pos;
        this.ch = this.charAt(pos);
    }
    
    public Lexer(final String input, final boolean skipComment) {
        this.features = 0;
        this.keywords = Keywords.DEFAULT_KEYWORDS;
        this.commentCount = 0;
        this.comments = null;
        this.skipComment = true;
        this.savePoint = null;
        this.allowComment = true;
        this.varIndex = -1;
        this.endOfComment = false;
        this.keepComments = false;
        this.line = 0;
        this.lines = 0;
        this.optimizedForParameterized = false;
        this.keepSourceLocation = false;
        this.skipComment = skipComment;
        this.text = input;
        this.pos = 0;
        this.ch = this.charAt(this.pos);
        while (this.ch == '\u200b' || this.ch == '\n') {
            if (this.ch == '\n') {
                ++this.line;
            }
            this.ch = this.charAt(++this.pos);
        }
    }
    
    public Lexer(final char[] input, final int inputLength, final boolean skipComment) {
        this(new String(input, 0, inputLength), skipComment);
    }
    
    protected final void scanChar() {
        this.ch = this.charAt(++this.pos);
    }
    
    protected void unscan() {
        final int n = this.pos - 1;
        this.pos = n;
        this.ch = this.charAt(n);
    }
    
    public boolean isEOF() {
        return this.pos >= this.text.length();
    }
    
    protected void lexError(final String key, final Object... args) {
        this.token = Token.ERROR;
    }
    
    public final Token token() {
        return this.token;
    }
    
    public void setToken(final Token token) {
        this.token = token;
    }
    
    public final DbType getDbType() {
        return this.dbType;
    }
    
    public String info() {
        int line = 1;
        int column = 1;
        for (int i = 0; i < this.startPos; ++i, ++column) {
            final char ch = this.text.charAt(i);
            if (ch == '\n') {
                column = 1;
                ++line;
            }
        }
        this.posLine = line;
        this.posColumn = column;
        final StringBuilder buf = new StringBuilder();
        buf.append("pos ").append(this.pos).append(", line ").append(line).append(", column ").append(column);
        if (this.token != null) {
            if (this.token.name != null) {
                buf.append(", token ").append(this.token.name);
            }
            else {
                buf.append(", token ").append(this.token.name());
            }
        }
        if (this.token == Token.IDENTIFIER || this.token == Token.LITERAL_ALIAS || this.token == Token.LITERAL_CHARS) {
            buf.append(" ").append(this.stringVal());
        }
        if (this.isEnabled(SQLParserFeature.PrintSQLWhileParsingFailed)) {
            buf.append(", SQL : ");
            buf.append(this.text);
        }
        return buf.toString();
    }
    
    public final void nextTokenComma() {
        if (this.ch == ' ') {
            this.scanChar();
        }
        if (this.ch == ',' || this.ch == '\uff0c') {
            this.scanChar();
            this.token = Token.COMMA;
            return;
        }
        if (this.ch == ')' || this.ch == '\uff09') {
            this.scanChar();
            this.token = Token.RPAREN;
            return;
        }
        if (this.ch == '.') {
            this.scanChar();
            this.token = Token.DOT;
            return;
        }
        if (this.ch == 'a' || this.ch == 'A') {
            final char ch_next = this.charAt(this.pos + 1);
            if (ch_next == 's' || ch_next == 'S') {
                final char ch_next_2 = this.charAt(this.pos + 2);
                if (ch_next_2 == ' ') {
                    this.pos += 2;
                    this.ch = ' ';
                    this.token = Token.AS;
                    this.stringVal = "AS";
                    return;
                }
            }
        }
        this.nextToken();
    }
    
    public final void nextTokenCommaValue() {
        if (this.ch == ' ') {
            this.scanChar();
        }
        if (this.ch == ',' || this.ch == '\uff0c') {
            this.scanChar();
            this.token = Token.COMMA;
            return;
        }
        if (this.ch == ')' || this.ch == '\uff09') {
            this.scanChar();
            this.token = Token.RPAREN;
            return;
        }
        if (this.ch == '.') {
            this.scanChar();
            this.token = Token.DOT;
            return;
        }
        if (this.ch == 'a' || this.ch == 'A') {
            final char ch_next = this.charAt(this.pos + 1);
            if (ch_next == 's' || ch_next == 'S') {
                final char ch_next_2 = this.charAt(this.pos + 2);
                if (ch_next_2 == ' ') {
                    this.pos += 2;
                    this.ch = ' ';
                    this.token = Token.AS;
                    this.stringVal = "AS";
                    return;
                }
            }
        }
        this.nextTokenValue();
    }
    
    public final void nextTokenEq() {
        if (this.ch == ' ') {
            this.scanChar();
        }
        if (this.ch == '=') {
            this.scanChar();
            if (this.ch == '=') {
                this.scanChar();
                this.token = Token.EQEQ;
            }
            else if (this.ch == '>') {
                this.scanChar();
                this.token = Token.EQGT;
            }
            this.token = Token.EQ;
            return;
        }
        if (this.ch == '.') {
            this.scanChar();
            this.token = Token.DOT;
            return;
        }
        if (this.ch == 'a' || this.ch == 'A') {
            final char ch_next = this.charAt(this.pos + 1);
            if (ch_next == 's' || ch_next == 'S') {
                final char ch_next_2 = this.charAt(this.pos + 2);
                if (ch_next_2 == ' ') {
                    this.pos += 2;
                    this.ch = ' ';
                    this.token = Token.AS;
                    this.stringVal = "AS";
                    return;
                }
            }
        }
        this.nextToken();
    }
    
    public final void nextTokenLParen() {
        if (this.ch == ' ') {
            this.scanChar();
        }
        if (this.ch == '(' || this.ch == '\uff08') {
            this.scanChar();
            this.token = Token.LPAREN;
            return;
        }
        this.nextToken();
    }
    
    public final void nextTokenValue() {
        this.startPos = this.pos;
        while (this.ch == ' ') {
            this.scanChar();
        }
        if (this.ch == '\'') {
            this.bufPos = 0;
            if (this.dbType == DbType.mysql) {
                this.scanString2();
            }
            else {
                this.scanString();
            }
            return;
        }
        if (this.ch == '\"' && !this.isEnabled(SQLParserFeature.KeepNameQuotes)) {
            this.bufPos = 0;
            this.scanString2_d();
            return;
        }
        if (this.ch == '0') {
            this.bufPos = 0;
            if (this.charAt(this.pos + 1) == 'x') {
                this.scanChar();
                this.scanChar();
                this.scanHexaDecimal();
            }
            else {
                this.scanNumber();
            }
            return;
        }
        if (this.ch > '0' && this.ch <= '9') {
            this.bufPos = 0;
            this.scanNumber();
            return;
        }
        if (this.ch == '-') {
            final char next = this.charAt(this.pos + 1);
            if (next >= '0' && next <= '9') {
                this.bufPos = 0;
                this.scanNumber();
                return;
            }
        }
        if (this.ch == '?') {
            this.scanChar();
            this.token = Token.QUES;
            return;
        }
        if (this.ch == 'n' || this.ch == 'N') {
            char c1 = '\0';
            final char c2;
            final char c3;
            final char c4;
            if (this.pos + 4 < this.text.length() && ((c1 = this.text.charAt(this.pos + 1)) == 'u' || c1 == 'U') && ((c2 = this.text.charAt(this.pos + 2)) == 'l' || c2 == 'L') && ((c3 = this.text.charAt(this.pos + 3)) == 'l' || c3 == 'L') && (CharTypes.isWhitespace(c4 = this.text.charAt(this.pos + 4)) || c4 == ',' || c4 == ')' || c4 == '\uff09')) {
                this.pos += 4;
                this.ch = c4;
                this.token = Token.NULL;
                this.stringVal = "NULL";
                return;
            }
            if (c1 == '\'') {
                this.bufPos = 0;
                ++this.pos;
                this.ch = '\'';
                this.scanString();
                this.token = Token.LITERAL_NCHARS;
                return;
            }
        }
        if (this.ch == ')' || this.ch == '\uff09') {
            this.scanChar();
            this.token = Token.RPAREN;
            return;
        }
        if (this.ch == '\uff08') {
            this.scanChar();
            this.token = Token.LPAREN;
            return;
        }
        if (this.ch == '$' && isVaraintChar(this.charAt(this.pos + 1))) {
            this.scanVariable();
            return;
        }
        if (CharTypes.isFirstIdentifierChar(this.ch)) {
            this.scanIdentifier();
            return;
        }
        this.nextToken();
    }
    
    static boolean isVaraintChar(final char ch) {
        return ch == '{' || (ch >= '0' && ch <= '9');
    }
    
    public final void nextTokenBy() {
        while (this.ch == ' ') {
            this.scanChar();
        }
        if (this.ch == 'b' || this.ch == 'B') {
            final char ch_next = this.charAt(this.pos + 1);
            if (ch_next == 'y' || ch_next == 'Y') {
                final char ch_next_2 = this.charAt(this.pos + 2);
                if (ch_next_2 == ' ') {
                    this.pos += 2;
                    this.ch = ' ';
                    this.token = Token.BY;
                    this.stringVal = "BY";
                    return;
                }
            }
        }
        this.nextToken();
    }
    
    public final void nextTokenNotOrNull() {
        while (this.ch == ' ') {
            this.scanChar();
        }
        if ((this.ch == 'n' || this.ch == 'N') && this.pos + 3 < this.text.length()) {
            final char c1 = this.text.charAt(this.pos + 1);
            final char c2 = this.text.charAt(this.pos + 2);
            final char c3 = this.text.charAt(this.pos + 3);
            if ((c1 == 'o' || c1 == 'O') && (c2 == 't' || c2 == 'T') && CharTypes.isWhitespace(c3)) {
                this.pos += 3;
                this.ch = c3;
                this.token = Token.NOT;
                this.stringVal = "NOT";
                return;
            }
            final char c4;
            if (this.pos + 4 < this.text.length() && (c1 == 'u' || c1 == 'U') && (c2 == 'l' || c2 == 'L') && (c3 == 'l' || c3 == 'L') && CharTypes.isWhitespace(c4 = this.text.charAt(this.pos + 4))) {
                this.pos += 4;
                this.ch = c4;
                this.token = Token.NULL;
                this.stringVal = "NULL";
                return;
            }
        }
        this.nextToken();
    }
    
    public final void nextTokenIdent() {
        while (this.ch == ' ') {
            this.scanChar();
        }
        if (this.ch == '$' && isVaraintChar(this.charAt(this.pos + 1))) {
            this.scanVariable();
            return;
        }
        if (CharTypes.isFirstIdentifierChar(this.ch) && this.ch != '\uff08') {
            this.scanIdentifier();
            return;
        }
        if (this.ch == ')') {
            this.scanChar();
            this.token = Token.RPAREN;
            return;
        }
        this.nextToken();
    }
    
    public final SQLType scanSQLType() {
        while (true) {
            if (CharTypes.isWhitespace(this.ch)) {
                this.ch = this.charAt(++this.pos);
            }
            else {
                if (this.ch == '/') {
                    if (this.pos + 1 < this.text.length() && this.text.charAt(this.pos + 1) == '*') {
                        final int index = this.text.indexOf("*/", this.pos + 2);
                        if (index == -1) {
                            return SQLType.UNKNOWN;
                        }
                        this.pos = index + 2;
                        this.ch = this.charAt(this.pos);
                        continue;
                    }
                    else if (this.pos + 2 < this.text.length() && this.text.charAt(this.pos + 1) == ' ' && this.text.charAt(this.pos + 2) == '*' && this.dbType == DbType.odps) {
                        final int index = this.text.indexOf("* /", this.pos + 3);
                        if (index == -1) {
                            return SQLType.UNKNOWN;
                        }
                        this.pos = index + 3;
                        this.ch = this.charAt(this.pos);
                        continue;
                    }
                }
                if (this.dbType == DbType.odps && this.ch == ';') {
                    this.ch = this.charAt(++this.pos);
                    if (this.isEOF()) {
                        return SQLType.EMPTY;
                    }
                    while (CharTypes.isWhitespace(this.ch)) {
                        this.ch = this.charAt(++this.pos);
                    }
                }
                else {
                    if (this.pos + 1 >= this.text.length() || ((this.ch != '-' || this.text.charAt(this.pos + 1) != '-') && (this.ch != '\u2014' || this.text.charAt(this.pos + 1) != '\u2014'))) {
                        break;
                    }
                    final int index = this.text.indexOf(10, this.pos + 2);
                    if (index == -1) {
                        this.reset(0);
                        this.nextToken();
                        if (this.token == Token.EOF) {
                            return SQLType.EMPTY;
                        }
                        return SQLType.UNKNOWN;
                    }
                    else {
                        this.pos = index + 1;
                        this.ch = this.charAt(this.pos);
                    }
                }
            }
        }
        while (this.ch == '(') {
            this.ch = this.charAt(++this.pos);
            while (CharTypes.isWhitespace(this.ch)) {
                this.ch = this.charAt(++this.pos);
            }
        }
        long hashCode = -3750763034362895579L;
        while (true) {
            char c;
            if (this.ch >= 'a' && this.ch <= 'z') {
                c = this.ch;
            }
            else {
                if (this.ch < 'A' || this.ch > 'Z') {
                    break;
                }
                c = (char)(this.ch + ' ');
            }
            hashCode ^= c;
            hashCode *= 1099511628211L;
            this.ch = this.charAt(++this.pos);
        }
        if (this.ch == '_' || (this.ch >= '0' && this.ch <= '9')) {
            return SQLType.UNKNOWN;
        }
        if (hashCode == FnvHash.Constants.SELECT) {
            return SQLType.SELECT;
        }
        if (hashCode == FnvHash.Constants.INSERT) {
            return SQLType.INSERT;
        }
        if (hashCode == FnvHash.Constants.DELETE) {
            return SQLType.DELETE;
        }
        if (hashCode == FnvHash.Constants.UPDATE) {
            return SQLType.UPDATE;
        }
        if (hashCode == FnvHash.Constants.REPLACE) {
            return SQLType.REPLACE;
        }
        if (hashCode == FnvHash.Constants.TRUNCATE) {
            return SQLType.TRUNCATE;
        }
        if (hashCode == FnvHash.Constants.MERGE) {
            return SQLType.MERGE;
        }
        if (hashCode == FnvHash.Constants.CREATE) {
            return SQLType.CREATE;
        }
        if (hashCode == FnvHash.Constants.ALTER) {
            return SQLType.ALTER;
        }
        if (hashCode == FnvHash.Constants.SHOW) {
            this.nextToken();
            if (this.identifierEquals(FnvHash.Constants.STATISTIC)) {
                return SQLType.SHOW_STATISTIC;
            }
            if (this.identifierEquals(FnvHash.Constants.STATISTIC_LIST)) {
                return SQLType.SHOW_STATISTIC_LIST;
            }
            if (this.identifierEquals(FnvHash.Constants.TABLES) || this.identifierEquals("TABLES\uff1b")) {
                return SQLType.SHOW_TABLES;
            }
            if (this.identifierEquals(FnvHash.Constants.PARTITIONS)) {
                return SQLType.SHOW_PARTITIONS;
            }
            if (this.identifierEquals(FnvHash.Constants.CATALOGS)) {
                return SQLType.SHOW_CATALOGS;
            }
            if (this.identifierEquals(FnvHash.Constants.FUNCTIONS)) {
                return SQLType.SHOW_FUNCTIONS;
            }
            if (this.identifierEquals(FnvHash.Constants.ROLES)) {
                return SQLType.SHOW_ROLES;
            }
            if (this.identifierEquals(FnvHash.Constants.ROLE)) {
                return SQLType.SHOW_ROLE;
            }
            if (this.identifierEquals(FnvHash.Constants.LABEL)) {
                return SQLType.SHOW_LABEL;
            }
            if (this.identifierEquals(FnvHash.Constants.GRANTS)) {
                return SQLType.SHOW_GRANTS;
            }
            if (this.identifierEquals(FnvHash.Constants.GRANT) || this.token == Token.GRANT) {
                return SQLType.SHOW_GRANT;
            }
            if (this.identifierEquals(FnvHash.Constants.RECYCLEBIN)) {
                return SQLType.SHOW_RECYCLEBIN;
            }
            if (this.identifierEquals("VARIABLES")) {
                return SQLType.SHOW_VARIABLES;
            }
            if (this.identifierEquals("HISTORY")) {
                return SQLType.SHOW_HISTORY;
            }
            if (this.identifierEquals("PACKAGES")) {
                return SQLType.SHOW_PACKAGES;
            }
            if (this.identifierEquals("PACKAGE")) {
                return SQLType.SHOW_PACKAGE;
            }
            if (this.identifierEquals("CHANGELOGS")) {
                return SQLType.SHOW_CHANGELOGS;
            }
            if (this.identifierEquals("ACL")) {
                return SQLType.SHOW_ACL;
            }
            if (this.token == Token.CREATE) {
                this.nextToken();
                if (this.token == Token.TABLE) {
                    return SQLType.SHOW_CREATE_TABLE;
                }
            }
            return SQLType.SHOW;
        }
        else {
            if (hashCode == FnvHash.Constants.DESC) {
                return SQLType.DESC;
            }
            if (hashCode == FnvHash.Constants.DESCRIBE) {
                return SQLType.DESC;
            }
            if (hashCode == FnvHash.Constants.SET) {
                this.nextToken();
                if (this.identifierEquals(FnvHash.Constants.LABEL)) {
                    return SQLType.SET_LABEL;
                }
                if (this.identifierEquals("PROJECT")) {
                    return SQLType.SET_PROJECT;
                }
                return SQLType.SET;
            }
            else {
                if (hashCode == FnvHash.Constants.KILL) {
                    return SQLType.KILL;
                }
                if (hashCode == FnvHash.Constants.MSCK) {
                    return SQLType.MSCK;
                }
                if (hashCode == FnvHash.Constants.USE) {
                    return SQLType.USE;
                }
                if (hashCode == FnvHash.Constants.DROP) {
                    return SQLType.DROP;
                }
                if (hashCode == FnvHash.Constants.LIST) {
                    this.nextToken();
                    if (this.identifierEquals(FnvHash.Constants.USERS)) {
                        return SQLType.LIST_USERS;
                    }
                    if (this.identifierEquals(FnvHash.Constants.TABLES)) {
                        return SQLType.LIST_TABLES;
                    }
                    if (this.identifierEquals(FnvHash.Constants.ROLES)) {
                        return SQLType.LIST_ROLES;
                    }
                    if (this.identifierEquals(FnvHash.Constants.TEMPORARY)) {
                        return SQLType.LIST_TEMPORARY_OUTPUT;
                    }
                    if (this.identifierEquals("TENANT")) {
                        this.nextToken();
                        if (this.identifierEquals(FnvHash.Constants.ROLES)) {
                            return SQLType.LIST_TENANT_ROLES;
                        }
                    }
                    else {
                        if (this.identifierEquals("TRUSTEDPROJECTS")) {
                            return SQLType.LIST_TRUSTEDPROJECTS;
                        }
                        if (this.identifierEquals("ACCOUNTPROVIDERS")) {
                            return SQLType.LIST_ACCOUNTPROVIDERS;
                        }
                    }
                    return SQLType.LIST;
                }
                else {
                    if (hashCode == FnvHash.Constants.ROLLBACK) {
                        return SQLType.ROLLBACK;
                    }
                    if (hashCode == FnvHash.Constants.COMMIT) {
                        return SQLType.COMMIT;
                    }
                    if (hashCode == FnvHash.Constants.WHO) {
                        return SQLType.WHO;
                    }
                    if (hashCode == FnvHash.Constants.GRANT) {
                        return SQLType.GRANT;
                    }
                    if (hashCode == FnvHash.Constants.REVOKE) {
                        return SQLType.REVOKE;
                    }
                    if (hashCode == FnvHash.Constants.ANALYZE) {
                        return SQLType.ANALYZE;
                    }
                    if (hashCode == FnvHash.Constants.EXPLAIN) {
                        return SQLType.EXPLAIN;
                    }
                    if (hashCode == FnvHash.Constants.READ) {
                        return SQLType.READ;
                    }
                    if (hashCode == FnvHash.Constants.WITH) {
                        return SQLType.WITH;
                    }
                    if (hashCode == FnvHash.Constants.DUMP) {
                        this.nextToken();
                        if (this.identifierEquals(FnvHash.Constants.DATA)) {
                            return SQLType.DUMP_DATA;
                        }
                    }
                    else if (hashCode == FnvHash.Constants.ADD) {
                        this.nextToken();
                        if (this.token == Token.USER || this.identifierEquals(FnvHash.Constants.USER)) {
                            return SQLType.ADD_USER;
                        }
                        if (this.token == Token.TABLE) {
                            return SQLType.ADD_TABLE;
                        }
                        if (this.token == Token.FUNCTION) {
                            return SQLType.ADD_FUNCTION;
                        }
                        if (this.identifierEquals(FnvHash.Constants.STATISTIC)) {
                            return SQLType.ADD_STATISTIC;
                        }
                        if (this.identifierEquals(FnvHash.Constants.RESOURCE)) {
                            return SQLType.ADD_RESOURCE;
                        }
                        if (this.identifierEquals("VOLUME")) {
                            return SQLType.ADD_VOLUME;
                        }
                        if (this.identifierEquals("ACCOUNTPROVIDER")) {
                            return SQLType.ADD_ACCOUNTPROVIDER;
                        }
                        if (this.identifierEquals("TRUSTEDPROJECT")) {
                            return SQLType.ADD_TRUSTEDPROJECT;
                        }
                        return SQLType.ADD;
                    }
                    else if (hashCode == FnvHash.Constants.REMOVE) {
                        this.nextToken();
                        if (this.token == Token.USER || this.identifierEquals(FnvHash.Constants.USER)) {
                            return SQLType.REMOVE_USER;
                        }
                        if (this.identifierEquals(FnvHash.Constants.RESOURCE)) {
                            return SQLType.REMOVE_RESOURCE;
                        }
                        return SQLType.REMOVE;
                    }
                    else if (hashCode == FnvHash.Constants.TUNNEL) {
                        this.nextToken();
                        if (this.identifierEquals(FnvHash.Constants.DOWNLOAD)) {
                            return SQLType.TUNNEL_DOWNLOAD;
                        }
                    }
                    else {
                        if (hashCode == FnvHash.Constants.UPLOAD) {
                            return SQLType.UPLOAD;
                        }
                        if (hashCode == FnvHash.Constants.WHOAMI) {
                            return SQLType.WHOAMI;
                        }
                        if (hashCode == FnvHash.Constants.COUNT) {
                            return SQLType.COUNT;
                        }
                        if (hashCode == FnvHash.Constants.CLONE) {
                            return SQLType.CLONE;
                        }
                        if (hashCode == FnvHash.Constants.LOAD) {
                            return SQLType.LOAD;
                        }
                        if (hashCode == FnvHash.Constants.INSTALL) {
                            return SQLType.INSTALL;
                        }
                        if (hashCode == FnvHash.Constants.UNLOAD) {
                            return SQLType.UNLOAD;
                        }
                        if (hashCode == FnvHash.Constants.ALLOW) {
                            return SQLType.ALLOW;
                        }
                        if (hashCode == FnvHash.Constants.PURGE) {
                            return SQLType.PURGE;
                        }
                        if (hashCode == FnvHash.Constants.RESTORE) {
                            return SQLType.RESTORE;
                        }
                        if (hashCode == FnvHash.Constants.EXSTORE) {
                            return SQLType.EXSTORE;
                        }
                        if (hashCode == FnvHash.Constants.UNDO) {
                            return SQLType.UNDO;
                        }
                        if (hashCode == FnvHash.Constants.REMOVE) {
                            return SQLType.REMOVE;
                        }
                        if (hashCode == FnvHash.Constants.FROM) {
                            if (this.dbType == DbType.odps || this.dbType == DbType.hive) {
                                return SQLType.INSERT_MULTI;
                            }
                        }
                        else {
                            if (hashCode == FnvHash.Constants.ADD) {
                                return SQLType.ADD;
                            }
                            if (hashCode == FnvHash.Constants.IF) {
                                return SQLType.SCRIPT;
                            }
                            if (hashCode == FnvHash.Constants.FUNCTION) {
                                if (this.dbType == DbType.odps) {
                                    return SQLType.SCRIPT;
                                }
                            }
                            else if (hashCode == FnvHash.Constants.BEGIN) {
                                if (this.dbType == DbType.odps || this.dbType == DbType.oracle) {
                                    return SQLType.SCRIPT;
                                }
                            }
                            else if (this.ch == '@') {
                                this.nextToken();
                                if (this.token == Token.VARIANT && this.dbType == DbType.odps) {
                                    this.nextToken();
                                    if (this.token == Token.TABLE) {
                                        return SQLType.SCRIPT;
                                    }
                                    if (this.token == Token.IDENTIFIER) {
                                        this.nextToken();
                                    }
                                    if (this.token == Token.COLONEQ || this.token == Token.SEMI) {
                                        return SQLType.SCRIPT;
                                    }
                                }
                            }
                        }
                    }
                    if (this.ch == '\u001a') {
                        return SQLType.EMPTY;
                    }
                    return SQLType.UNKNOWN;
                }
            }
        }
    }
    
    public final SQLType scanSQLTypeV2() {
        SQLType sqlType = this.scanSQLType();
        Label_0536: {
            if (sqlType == SQLType.CREATE) {
                this.nextToken();
                if (this.token == Token.USER || this.identifierEquals(FnvHash.Constants.USER)) {
                    return SQLType.CREATE_USER;
                }
                for (int i = 0; i < 1000; ++i) {
                    switch (this.token) {
                        case EOF:
                        case ERROR: {
                            break Label_0536;
                        }
                        case TABLE: {
                            sqlType = SQLType.CREATE_TABLE;
                            break;
                        }
                        case VIEW: {
                            if (sqlType == SQLType.CREATE) {
                                sqlType = SQLType.CREATE_VIEW;
                                break Label_0536;
                            }
                            break;
                        }
                        case FUNCTION: {
                            if (sqlType == SQLType.CREATE) {
                                sqlType = SQLType.CREATE_FUNCTION;
                                break Label_0536;
                            }
                            break;
                        }
                        case SELECT: {
                            if (sqlType == SQLType.CREATE_TABLE) {
                                sqlType = SQLType.CREATE_TABLE_AS_SELECT;
                                break Label_0536;
                            }
                            break;
                        }
                        default: {
                            if (sqlType == SQLType.CREATE && this.identifierEquals(FnvHash.Constants.ROLE)) {
                                sqlType = SQLType.CREATE_ROLE;
                                break Label_0536;
                            }
                            if (sqlType == SQLType.CREATE && this.identifierEquals(FnvHash.Constants.PACKAGE)) {
                                sqlType = SQLType.CREATE_PACKAGE;
                                break Label_0536;
                            }
                            break;
                        }
                    }
                    this.nextToken();
                }
            }
            else if (sqlType == SQLType.DROP) {
                this.nextToken();
                if (this.token == Token.USER || this.identifierEquals(FnvHash.Constants.USER)) {
                    return SQLType.DROP_USER;
                }
                if (this.token == Token.TABLE) {
                    return SQLType.DROP_TABLE;
                }
                if (this.token == Token.VIEW) {
                    return SQLType.DROP_VIEW;
                }
                if (this.token == Token.FUNCTION) {
                    return SQLType.DROP_FUNCTION;
                }
                if (this.identifierEquals(FnvHash.Constants.ROLE)) {
                    return SQLType.DROP_ROLE;
                }
                if (this.identifierEquals(FnvHash.Constants.RESOURCE)) {
                    return SQLType.DROP_RESOURCE;
                }
                if (this.identifierEquals(FnvHash.Constants.MATERIALIZED)) {
                    this.nextToken();
                    if (this.token == Token.VIEW) {
                        return SQLType.DROP_MATERIALIZED_VIEW;
                    }
                }
            }
            else if (sqlType == SQLType.ALTER) {
                this.nextToken();
                if (this.token == Token.USER || this.identifierEquals(FnvHash.Constants.USER)) {
                    return SQLType.ALTER_USER;
                }
                if (this.token == Token.TABLE) {
                    return SQLType.ALTER_TABLE;
                }
                if (this.token == Token.VIEW) {
                    return SQLType.ALTER_VIEW;
                }
            }
            else if (sqlType == SQLType.INSERT) {
                this.nextToken();
                final boolean overwrite = this.token == Token.OVERWRITE;
                int j = 0;
                while (j < 1000) {
                    this.nextToken();
                    if (this.token == Token.SELECT) {
                        return overwrite ? SQLType.INSERT_OVERWRITE_SELECT : SQLType.INSERT_INTO_SELECT;
                    }
                    if (this.token == Token.VALUES) {
                        return overwrite ? SQLType.INSERT_OVERWRITE_VALUES : SQLType.INSERT_INTO_VALUES;
                    }
                    if (this.token == Token.ERROR || this.token == Token.EOF) {
                        if (overwrite) {
                            sqlType = SQLType.INSERT_OVERWRITE;
                            break;
                        }
                        break;
                    }
                    else {
                        ++j;
                    }
                }
                return sqlType;
            }
        }
        return sqlType;
    }
    
    public final void nextTokenAlias() {
        this.startPos = this.pos;
        this.bufPos = 0;
        while (CharTypes.isWhitespace(this.ch)) {
            if (this.ch == '\n') {
                ++this.line;
            }
            this.ch = this.charAt(++this.pos);
            this.startPos = this.pos;
        }
        if (this.ch == '\"') {
            this.scanAlias();
        }
        else if (this.ch == '\'') {
            this.scanAlias();
            final int p;
            if (this.stringVal.length() > 1 && this.stringVal.indexOf(34) == -1 && ((p = this.stringVal.indexOf(39, 1)) == -1 || p == this.stringVal.length() - 1)) {
                final char[] chars = this.stringVal.toCharArray();
                chars[0] = '\"';
                chars[chars.length - 1] = '\"';
                this.stringVal = new String(chars);
            }
            this.token = Token.LITERAL_ALIAS;
        }
        else {
            this.nextToken();
        }
    }
    
    public final void nextPath() {
        while (CharTypes.isWhitespace(this.ch)) {
            this.ch = this.charAt(++this.pos);
        }
        this.stringVal = null;
        this.mark = this.pos;
        while (!CharTypes.isWhitespace(this.ch)) {
            this.ch = this.charAt(++this.pos);
        }
        this.bufPos = this.pos - this.mark;
        this.ch = this.charAt(++this.pos);
        this.token = Token.LITERAL_PATH;
    }
    
    public final void nextTokenForSet() {
        while (CharTypes.isWhitespace(this.ch)) {
            this.ch = this.charAt(++this.pos);
        }
        if (CharTypes.isFirstIdentifierChar(this.ch) || this.ch == '{') {
            this.stringVal = null;
            this.mark = this.pos;
            while (this.ch != ';' && this.ch != '\u001a') {
                this.ch = this.charAt(++this.pos);
            }
            this.bufPos = this.pos - this.mark;
            this.stringVal = this.subString(this.mark, this.bufPos);
            this.token = Token.IDENTIFIER;
            return;
        }
        this.nextToken();
    }
    
    public final boolean skipToNextLine(final int startPosition) {
        int i = 0;
        while (true) {
            final int pos = startPosition + i;
            final char ch = this.charAt(pos);
            if (ch == '\n') {
                this.pos = pos;
                this.ch = this.charAt(this.pos);
                return true;
            }
            if (ch == '\u001a') {
                this.pos = pos;
                return false;
            }
            ++i;
        }
    }
    
    public final boolean skipToNextLineOrParameter(final int startPosition) {
        int i = 0;
        while (true) {
            final int pos = startPosition + i;
            final char ch = this.charAt(pos);
            if (ch == '\n') {
                this.pos = pos;
                this.ch = this.charAt(this.pos);
                return true;
            }
            if (ch == '$' && this.charAt(pos + 1) == '{') {
                this.pos = pos;
                this.ch = this.charAt(this.pos);
                return true;
            }
            if (ch == '\u001a') {
                this.pos = pos;
                return false;
            }
            ++i;
        }
    }
    
    public final void nextToken() {
        this.startPos = this.pos;
        this.bufPos = 0;
        if (this.comments != null && this.comments.size() > 0) {
            this.comments = null;
        }
        this.lines = 0;
        final int startLine = this.line;
    Label_1318:
        while (true) {
            if (CharTypes.isWhitespace(this.ch)) {
                if (this.ch == '\n') {
                    ++this.line;
                    this.lines = this.line - startLine;
                }
                this.ch = this.charAt(++this.pos);
                this.startPos = this.pos;
            }
            else {
                if (this.ch == '$' && isVaraintChar(this.charAt(this.pos + 1))) {
                    this.scanVariable();
                    return;
                }
                if (CharTypes.isFirstIdentifierChar(this.ch)) {
                    if (this.ch == '\uff08') {
                        this.scanChar();
                        this.token = Token.LPAREN;
                        return;
                    }
                    if (this.ch == '\uff09') {
                        this.scanChar();
                        this.token = Token.RPAREN;
                        return;
                    }
                    if ((this.ch == 'N' || this.ch == 'n') && this.charAt(this.pos + 1) == '\'') {
                        ++this.pos;
                        this.ch = '\'';
                        this.scanString();
                        this.token = Token.LITERAL_NCHARS;
                        return;
                    }
                    if (this.ch != '\u2014' || this.charAt(this.pos + 1) != '\u2014' || this.charAt(this.pos + 2) != '\n') {
                        this.scanIdentifier();
                        return;
                    }
                    this.pos += 3;
                    this.ch = this.charAt(this.pos);
                }
                else {
                    switch (this.ch) {
                        case '0': {
                            if (this.charAt(this.pos + 1) == 'x') {
                                this.scanChar();
                                this.scanChar();
                                this.scanHexaDecimal();
                            }
                            else {
                                this.scanNumber();
                            }
                            return;
                        }
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9': {
                            this.scanNumber();
                            return;
                        }
                        case ',':
                        case '\uff0c': {
                            this.scanChar();
                            this.token = Token.COMMA;
                            return;
                        }
                        case '(':
                        case '\uff08': {
                            this.scanChar();
                            this.token = Token.LPAREN;
                            return;
                        }
                        case ')':
                        case '\uff09': {
                            this.scanChar();
                            this.token = Token.RPAREN;
                            return;
                        }
                        case '[': {
                            this.scanLBracket();
                            return;
                        }
                        case ']': {
                            this.scanChar();
                            this.token = Token.RBRACKET;
                            return;
                        }
                        case '{': {
                            this.scanChar();
                            this.token = Token.LBRACE;
                            return;
                        }
                        case '}': {
                            this.scanChar();
                            this.token = Token.RBRACE;
                            return;
                        }
                        case ':': {
                            this.scanChar();
                            if (this.ch == '=') {
                                this.scanChar();
                                this.token = Token.COLONEQ;
                            }
                            else if (this.ch == ':') {
                                this.scanChar();
                                this.token = Token.COLONCOLON;
                            }
                            else {
                                if (this.isEnabled(SQLParserFeature.TDDLHint) || this.dbType == DbType.hive || this.dbType == DbType.odps) {
                                    this.token = Token.COLON;
                                    return;
                                }
                                this.unscan();
                                this.scanVariable();
                            }
                            return;
                        }
                        case '#': {
                            this.scanSharp();
                            if ((this.token == Token.LINE_COMMENT || this.token == Token.MULTI_LINE_COMMENT) && this.skipComment) {
                                this.bufPos = 0;
                                continue;
                            }
                            return;
                        }
                        case '.': {
                            this.scanChar();
                            if (this.isDigit(this.ch) && (this.pos == 1 || this.token != Token.IDENTIFIER)) {
                                this.unscan();
                                this.scanNumber();
                                return;
                            }
                            if (this.ch == '.') {
                                this.scanChar();
                                if (this.ch == '.') {
                                    this.scanChar();
                                    this.token = Token.DOTDOTDOT;
                                }
                                else {
                                    this.token = Token.DOTDOT;
                                }
                            }
                            else {
                                this.token = Token.DOT;
                            }
                            return;
                        }
                        case '\'': {
                            this.scanString();
                            return;
                        }
                        case '\"': {
                            this.scanAlias();
                            return;
                        }
                        case '*': {
                            this.scanChar();
                            this.token = Token.STAR;
                            return;
                        }
                        case '?': {
                            this.scanChar();
                            if (this.ch == '?' && DbType.postgresql == this.dbType) {
                                this.scanChar();
                                if (this.ch == '|') {
                                    this.scanChar();
                                    this.token = Token.QUESBAR;
                                }
                                else {
                                    this.token = Token.QUESQUES;
                                }
                            }
                            else if (this.ch == '|' && DbType.postgresql == this.dbType) {
                                this.scanChar();
                                if (this.ch == '|') {
                                    this.unscan();
                                    this.token = Token.QUES;
                                }
                                else {
                                    this.token = Token.QUESBAR;
                                }
                            }
                            else if (this.ch == '&' && DbType.postgresql == this.dbType) {
                                this.scanChar();
                                this.token = Token.QUESAMP;
                            }
                            else {
                                this.token = Token.QUES;
                            }
                            return;
                        }
                        case ';': {
                            this.scanChar();
                            this.token = Token.SEMI;
                            return;
                        }
                        case '`': {
                            throw new ParserException("TODO. " + this.info());
                        }
                        case '@': {
                            this.scanVariable_at();
                            return;
                        }
                        case '-': {
                            final char next = this.charAt(this.pos + 1);
                            if (next == '-') {
                                this.scanComment();
                                if ((this.token == Token.LINE_COMMENT || this.token == Token.MULTI_LINE_COMMENT) && this.skipComment) {
                                    this.bufPos = 0;
                                    continue;
                                }
                                break Label_1318;
                            }
                            else {
                                if (next < '0' || next > '9') {
                                    this.scanOperator();
                                    break Label_1318;
                                }
                                if (this.token == null) {
                                    this.scanNumber();
                                    return;
                                }
                                switch (this.token) {
                                    case COMMA:
                                    case LPAREN:
                                    case WITH:
                                    case BY: {
                                        this.scanNumber();
                                        break Label_1318;
                                    }
                                    default: {
                                        this.scanOperator();
                                        break Label_1318;
                                    }
                                }
                            }
                            break;
                        }
                        case '/': {
                            final char nextChar = this.charAt(this.pos + 1);
                            if (nextChar == '/' || nextChar == '*' || (nextChar == '!' && this.isEnabled(SQLParserFeature.TDDLHint))) {
                                this.scanComment();
                                if ((this.token == Token.LINE_COMMENT || this.token == Token.MULTI_LINE_COMMENT) && this.skipComment) {
                                    this.bufPos = 0;
                                    continue;
                                }
                                return;
                            }
                            else if (nextChar == ' ' && this.charAt(this.pos + 2) == '*') {
                                this.scanComment();
                                if ((this.token == Token.LINE_COMMENT || this.token == Token.MULTI_LINE_COMMENT) && this.skipComment) {
                                    this.bufPos = 0;
                                    continue;
                                }
                                return;
                            }
                            else {
                                if (nextChar != ' ' || this.charAt(this.pos + 2) != ' ' || this.charAt(this.pos + 3) != '*') {
                                    this.token = Token.SLASH;
                                    this.scanChar();
                                    return;
                                }
                                this.scanComment();
                                if ((this.token == Token.LINE_COMMENT || this.token == Token.MULTI_LINE_COMMENT) && this.skipComment) {
                                    this.bufPos = 0;
                                    continue;
                                }
                                return;
                            }
                            break;
                        }
                        default: {
                            if (Character.isLetter(this.ch)) {
                                this.scanIdentifier();
                                return;
                            }
                            if (this.isOperator(this.ch)) {
                                this.scanOperator();
                                return;
                            }
                            if (this.ch == '\\' && this.charAt(this.pos + 1) == 'N' && DbType.mysql == this.dbType) {
                                this.scanChar();
                                this.scanChar();
                                this.token = Token.NULL;
                                return;
                            }
                            if (this.isEOF()) {
                                this.token = Token.EOF;
                            }
                            else {
                                this.lexError("illegal.char", String.valueOf((int)this.ch));
                                this.scanChar();
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
    
    protected void scanLBracket() {
        this.scanChar();
        this.token = Token.LBRACKET;
    }
    
    private final void scanOperator() {
        switch (this.ch) {
            case '+': {
                this.scanChar();
                this.token = Token.PLUS;
                break;
            }
            case '-': {
                this.scanChar();
                if (this.ch != '>') {
                    this.token = Token.SUB;
                    break;
                }
                this.scanChar();
                if (this.ch == '>') {
                    this.scanChar();
                    this.token = Token.SUBGTGT;
                    break;
                }
                this.token = Token.SUBGT;
                break;
            }
            case '*': {
                this.scanChar();
                this.token = Token.STAR;
                break;
            }
            case '/': {
                this.scanChar();
                this.token = Token.SLASH;
                break;
            }
            case '&': {
                this.scanChar();
                if (this.ch == '&') {
                    this.scanChar();
                    this.token = Token.AMPAMP;
                    break;
                }
                this.token = Token.AMP;
                break;
            }
            case '|': {
                this.scanChar();
                if (this.ch == '|') {
                    this.scanChar();
                    if (this.ch == '/') {
                        this.scanChar();
                        this.token = Token.BARBARSLASH;
                        break;
                    }
                    this.token = Token.BARBAR;
                    break;
                }
                else {
                    if (this.ch == '/') {
                        this.scanChar();
                        this.token = Token.BARSLASH;
                        break;
                    }
                    this.token = Token.BAR;
                    break;
                }
                break;
            }
            case '^': {
                this.scanChar();
                if (this.ch == '=') {
                    this.scanChar();
                    this.token = Token.CARETEQ;
                    break;
                }
                this.token = Token.CARET;
                break;
            }
            case '%': {
                this.scanChar();
                this.token = Token.PERCENT;
                break;
            }
            case '=': {
                this.scanChar();
                if (this.ch == ' ') {
                    this.scanChar();
                }
                if (this.ch == '=') {
                    this.scanChar();
                    this.token = Token.EQEQ;
                    break;
                }
                if (this.ch == '>') {
                    this.scanChar();
                    this.token = Token.EQGT;
                    break;
                }
                this.token = Token.EQ;
                break;
            }
            case '>': {
                this.scanChar();
                if (this.ch == '=') {
                    this.scanChar();
                    this.token = Token.GTEQ;
                    break;
                }
                if (this.ch == '>') {
                    this.scanChar();
                    this.token = Token.GTGT;
                    break;
                }
                this.token = Token.GT;
                break;
            }
            case '<': {
                this.scanChar();
                if (this.ch == '=') {
                    this.scanChar();
                    if (this.ch == '>') {
                        this.token = Token.LTEQGT;
                        this.scanChar();
                        break;
                    }
                    this.token = Token.LTEQ;
                    break;
                }
                else {
                    if (this.ch == '>') {
                        this.scanChar();
                        this.token = Token.LTGT;
                        break;
                    }
                    if (this.ch == '<') {
                        this.scanChar();
                        this.token = Token.LTLT;
                        break;
                    }
                    if (this.ch == '@') {
                        this.scanChar();
                        this.token = Token.LT_MONKEYS_AT;
                        break;
                    }
                    if (this.ch == '-' && this.charAt(this.pos + 1) == '>') {
                        this.scanChar();
                        this.scanChar();
                        this.token = Token.LT_SUB_GT;
                        break;
                    }
                    if (this.ch == ' ') {
                        final char c1 = this.charAt(this.pos + 1);
                        if (c1 == '=') {
                            this.scanChar();
                            this.scanChar();
                            if (this.ch == '>') {
                                this.token = Token.LTEQGT;
                                this.scanChar();
                            }
                            else {
                                this.token = Token.LTEQ;
                            }
                        }
                        else if (c1 == '>') {
                            this.scanChar();
                            this.scanChar();
                            this.token = Token.LTGT;
                        }
                        else if (c1 == '<') {
                            this.scanChar();
                            this.scanChar();
                            this.token = Token.LTLT;
                        }
                        else if (c1 == '@') {
                            this.scanChar();
                            this.scanChar();
                            this.token = Token.LT_MONKEYS_AT;
                        }
                        else if (c1 == '-' && this.charAt(this.pos + 2) == '>') {
                            this.scanChar();
                            this.scanChar();
                            this.scanChar();
                            this.token = Token.LT_SUB_GT;
                        }
                        else {
                            this.token = Token.LT;
                        }
                        break;
                    }
                    this.token = Token.LT;
                    break;
                }
                break;
            }
            case '!': {
                this.scanChar();
                while (CharTypes.isWhitespace(this.ch)) {
                    this.scanChar();
                }
                if (this.ch == '=') {
                    this.scanChar();
                    this.token = Token.BANGEQ;
                    break;
                }
                if (this.ch == '>') {
                    this.scanChar();
                    this.token = Token.BANGGT;
                    break;
                }
                if (this.ch == '<') {
                    this.scanChar();
                    this.token = Token.BANGLT;
                    break;
                }
                if (this.ch == '!') {
                    this.scanChar();
                    this.token = Token.BANGBANG;
                    break;
                }
                if (this.ch != '~') {
                    this.token = Token.BANG;
                    break;
                }
                this.scanChar();
                if (this.ch == '*') {
                    this.scanChar();
                    this.token = Token.BANG_TILDE_STAR;
                    break;
                }
                this.token = Token.BANG_TILDE;
                break;
            }
            case '?': {
                this.scanChar();
                this.token = Token.QUES;
                break;
            }
            case '~': {
                this.scanChar();
                if (this.ch == '*') {
                    this.scanChar();
                    this.token = Token.TILDE_STAR;
                    break;
                }
                if (this.ch == '=') {
                    this.scanChar();
                    this.token = Token.TILDE_EQ;
                    break;
                }
                this.token = Token.TILDE;
                break;
            }
            default: {
                throw new ParserException("TODO. " + this.info());
            }
        }
    }
    
    protected void scanString() {
        this.mark = this.pos;
        boolean hasSpecial = false;
        final Token preToken = this.token;
        while (!this.isEOF()) {
            this.ch = this.charAt(++this.pos);
            if (this.ch == '\'') {
                this.scanChar();
                if (this.ch != '\'') {
                    this.token = Token.LITERAL_CHARS;
                    if (!hasSpecial) {
                        if (preToken == Token.AS) {
                            this.stringVal = this.subString(this.mark, this.bufPos + 2);
                        }
                        else {
                            this.stringVal = this.subString(this.mark + 1, this.bufPos);
                        }
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
    
    protected final void scanString2() {
        boolean hasSpecial = false;
        final int startIndex = this.pos + 1;
        int endIndex = -1;
        for (int i = startIndex; i < this.text.length(); ++i) {
            final char ch = this.text.charAt(i);
            if (ch == '\\') {
                hasSpecial = true;
            }
            else if (ch == '\'') {
                endIndex = i;
                break;
            }
        }
        if (endIndex == -1) {
            throw new ParserException("unclosed str. " + this.info());
        }
        String stringVal;
        if (this.token == Token.AS) {
            stringVal = this.text.substring(this.pos, endIndex + 1);
        }
        else if (startIndex == endIndex) {
            stringVal = "";
        }
        else {
            stringVal = this.text.substring(startIndex, endIndex);
        }
        if (!hasSpecial) {
            this.stringVal = stringVal;
            final int pos = endIndex + 1;
            final char ch2 = this.charAt(pos);
            if (ch2 != '\'') {
                this.pos = pos;
                this.ch = ch2;
                this.token = Token.LITERAL_CHARS;
                return;
            }
        }
        this.mark = this.pos;
        hasSpecial = false;
        while (!this.isEOF()) {
            this.ch = this.charAt(++this.pos);
            if (this.ch == '\\') {
                this.scanChar();
                if (!hasSpecial) {
                    this.initBuff(this.bufPos);
                    this.arraycopy(this.mark + 1, this.buf, 0, this.bufPos);
                    hasSpecial = true;
                }
                switch (this.ch) {
                    case '0': {
                        this.putChar('\0');
                        continue;
                    }
                    case '\'': {
                        this.putChar('\'');
                        continue;
                    }
                    case '\"': {
                        this.putChar('\"');
                        continue;
                    }
                    case 'b': {
                        this.putChar('\b');
                        continue;
                    }
                    case 'n': {
                        this.putChar('\n');
                        continue;
                    }
                    case 'r': {
                        this.putChar('\r');
                        continue;
                    }
                    case 't': {
                        this.putChar('\t');
                        continue;
                    }
                    case '\\': {
                        this.putChar('\\');
                        continue;
                    }
                    case 'Z': {
                        this.putChar('\u001a');
                        continue;
                    }
                    case '%': {
                        if (this.dbType == DbType.mysql) {
                            this.putChar('\\');
                        }
                        this.putChar('%');
                        continue;
                    }
                    case '_': {
                        if (this.dbType == DbType.mysql) {
                            this.putChar('\\');
                        }
                        this.putChar('_');
                        continue;
                    }
                    case 'u': {
                        if ((this.features & SQLParserFeature.SupportUnicodeCodePoint.mask) != 0x0) {
                            final char c1 = this.charAt(++this.pos);
                            final char c2 = this.charAt(++this.pos);
                            final char c3 = this.charAt(++this.pos);
                            final char c4 = this.charAt(++this.pos);
                            final int intVal = Integer.parseInt(new String(new char[] { c1, c2, c3, c4 }), 16);
                            this.putChar((char)intVal);
                            continue;
                        }
                        this.putChar(this.ch);
                        continue;
                    }
                    default: {
                        this.putChar(this.ch);
                        continue;
                    }
                }
            }
            else if (this.ch == '\'') {
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
    
    protected final void scanString2_d() {
        boolean hasSpecial = false;
        final int startIndex = this.pos + 1;
        int endIndex = -1;
        for (int i = startIndex; i < this.text.length(); ++i) {
            final char ch = this.text.charAt(i);
            if (ch == '\\') {
                final int i2 = i + 1;
                if (i2 < this.text.length()) {
                    hasSpecial = true;
                    ++i;
                    continue;
                }
            }
            if (ch == '\"') {
                final int i2 = i + 1;
                if (i2 >= this.text.length() || this.text.charAt(i2) != '\"') {
                    endIndex = i;
                    break;
                }
                hasSpecial = true;
                ++i;
            }
        }
        if (endIndex == -1) {
            throw new ParserException("unclosed str. " + this.info());
        }
        String stringVal;
        if (this.token == Token.AS) {
            stringVal = this.subString(this.pos, endIndex + 1 - this.pos);
        }
        else if (this.charAt(endIndex + 1) == '.') {
            stringVal = this.subString(startIndex - 1, endIndex - startIndex + 2);
        }
        else {
            stringVal = this.subString(startIndex, endIndex - startIndex);
        }
        if (!hasSpecial) {
            this.stringVal = stringVal;
            final int pos = endIndex + 1;
            final char ch2 = this.charAt(pos);
            if (ch2 == '.') {
                this.pos = pos;
                this.ch = ch2;
                this.token = Token.IDENTIFIER;
                return;
            }
            if (ch2 != '\'') {
                this.pos = pos;
                this.ch = ch2;
                this.token = Token.LITERAL_CHARS;
                return;
            }
        }
        this.mark = this.pos;
        hasSpecial = false;
        while (!this.isEOF()) {
            this.ch = this.charAt(++this.pos);
            if (this.ch == '\\') {
                this.scanChar();
                if (!hasSpecial) {
                    this.initBuff(this.bufPos);
                    this.arraycopy(this.mark + 1, this.buf, 0, this.bufPos);
                    hasSpecial = true;
                }
                switch (this.ch) {
                    case '0': {
                        this.putChar('\0');
                        continue;
                    }
                    case '\'': {
                        this.putChar('\'');
                        continue;
                    }
                    case '\"': {
                        this.putChar('\"');
                        continue;
                    }
                    case 'b': {
                        this.putChar('\b');
                        continue;
                    }
                    case 'n': {
                        this.putChar('\n');
                        continue;
                    }
                    case 'r': {
                        this.putChar('\r');
                        continue;
                    }
                    case 't': {
                        this.putChar('\t');
                        continue;
                    }
                    case '\\': {
                        this.putChar('\\');
                        continue;
                    }
                    case 'Z': {
                        this.putChar('\u001a');
                        continue;
                    }
                    case '%': {
                        if (this.dbType == DbType.mysql) {
                            this.putChar('\\');
                        }
                        this.putChar('%');
                        continue;
                    }
                    case '_': {
                        if (this.dbType == DbType.mysql) {
                            this.putChar('\\');
                        }
                        this.putChar('_');
                        continue;
                    }
                    default: {
                        this.putChar(this.ch);
                        continue;
                    }
                }
            }
            else if (this.ch == '\"') {
                this.scanChar();
                if (this.ch != '\"') {
                    if (this.buf != null && this.bufPos > 0) {
                        this.stringVal = new String(this.buf, 0, this.bufPos);
                    }
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
                this.putChar('\"');
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
    
    protected final void scanAlias() {
        final char quote = this.ch;
        boolean hasSpecial = false;
        final int startIndex = this.pos + 1;
        int endIndex = -1;
        for (int i = startIndex; i < this.text.length(); ++i) {
            final char ch = this.text.charAt(i);
            if (ch == '\\') {
                hasSpecial = true;
                ++i;
            }
            else if (ch == quote) {
                if (i + 1 < this.text.length()) {
                    final char ch_next = this.charAt(i + 1);
                    if (ch_next == quote) {
                        hasSpecial = true;
                        ++i;
                        continue;
                    }
                }
                endIndex = i;
                break;
            }
        }
        if (endIndex == -1) {
            throw new ParserException("unclosed str. " + this.info());
        }
        final String stringVal = this.subString(this.pos, endIndex + 1 - this.pos);
        if (!hasSpecial) {
            this.stringVal = stringVal;
            final int pos = endIndex + 1;
            final char ch2 = this.charAt(pos);
            if (ch2 != '\'') {
                this.pos = pos;
                this.ch = ch2;
                this.token = Token.LITERAL_ALIAS;
                return;
            }
        }
        this.mark = this.pos;
        this.initBuff(this.bufPos);
        this.putChar(this.ch);
        while (!this.isEOF()) {
            this.ch = this.charAt(++this.pos);
            if (this.ch == '\\') {
                this.scanChar();
                switch (this.ch) {
                    case '0': {
                        this.putChar('\0');
                        continue;
                    }
                    case '\'': {
                        if (this.ch == quote) {
                            this.putChar('\\');
                        }
                        this.putChar('\'');
                        continue;
                    }
                    case '\"': {
                        if (this.ch == quote) {
                            this.putChar('\\');
                        }
                        this.putChar('\"');
                        continue;
                    }
                    case 'b': {
                        this.putChar('\b');
                        continue;
                    }
                    case 'n': {
                        this.putChar('\n');
                        continue;
                    }
                    case 'r': {
                        this.putChar('\r');
                        continue;
                    }
                    case 't': {
                        this.putChar('\t');
                        continue;
                    }
                    case '\\': {
                        this.putChar('\\');
                        this.putChar('\\');
                        continue;
                    }
                    case 'Z': {
                        this.putChar('\u001a');
                        continue;
                    }
                    case 'u': {
                        if (this.dbType == DbType.hive) {
                            final char c1 = this.charAt(++this.pos);
                            final char c2 = this.charAt(++this.pos);
                            final char c3 = this.charAt(++this.pos);
                            final char c4 = this.charAt(++this.pos);
                            final int intVal = Integer.parseInt(new String(new char[] { c1, c2, c3, c4 }), 16);
                            this.putChar((char)intVal);
                            continue;
                        }
                        this.putChar(this.ch);
                        continue;
                    }
                    default: {
                        this.putChar(this.ch);
                        continue;
                    }
                }
            }
            else if (this.ch == quote) {
                final char ch_next2 = this.charAt(this.pos + 1);
                if (ch_next2 != quote) {
                    this.putChar(this.ch);
                    this.scanChar();
                    this.token = Token.LITERAL_ALIAS;
                    this.stringVal = new String(this.buf, 0, this.bufPos);
                    return;
                }
                this.putChar('\\');
                this.putChar(this.ch);
                this.scanChar();
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
    
    public void scanSharp() {
        this.scanVariable();
    }
    
    public void scanVariable() {
        if (this.ch != ':' && this.ch != '#' && this.ch != '$' && (this.ch != '@' || this.dbType != DbType.odps)) {
            throw new ParserException("illegal variable. " + this.info());
        }
        this.mark = this.pos;
        this.bufPos = 1;
        final char c1 = this.charAt(this.pos + 1);
        if (c1 == '>' && DbType.postgresql == this.dbType) {
            this.pos += 2;
            this.token = Token.MONKEYS_AT_GT;
            this.ch = this.charAt(++this.pos);
            return;
        }
        if (c1 == '{') {
            ++this.pos;
            ++this.bufPos;
            boolean ident = false;
            char ch;
            while (true) {
                ch = this.charAt(++this.pos);
                if (this.isEOF()) {
                    --this.pos;
                    --this.bufPos;
                    break;
                }
                if (ch == '}' && !ident) {
                    if (!CharTypes.isIdentifierChar(this.charAt(this.pos + 1))) {
                        break;
                    }
                    ++this.bufPos;
                    ident = true;
                }
                else {
                    if (ident && CharTypes.isWhitespace(ch)) {
                        break;
                    }
                    ++this.bufPos;
                }
            }
            if (ch != '}' && !ident) {
                throw new ParserException("syntax error. " + this.info());
            }
            ++this.pos;
            ++this.bufPos;
            this.ch = this.charAt(this.pos);
            if (this.dbType == DbType.odps) {
                while (CharTypes.isIdentifierChar(this.ch)) {
                    ++this.pos;
                    ++this.bufPos;
                    this.ch = this.charAt(this.pos);
                }
            }
            this.stringVal = this.addSymbol();
            this.token = (ident ? Token.IDENTIFIER : Token.VARIANT);
        }
        else {
            if (c1 != '$' || this.charAt(this.pos + 2) != '{') {
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
                return;
            }
            this.pos += 2;
            this.bufPos += 2;
            char ch;
            while (true) {
                ch = this.charAt(++this.pos);
                if (ch == '}') {
                    break;
                }
                ++this.bufPos;
            }
            if (ch != '}') {
                throw new ParserException("syntax error. " + this.info());
            }
            ++this.pos;
            ++this.bufPos;
            this.ch = this.charAt(this.pos);
            if (this.dbType == DbType.odps) {
                while (CharTypes.isIdentifierChar(this.ch)) {
                    ++this.pos;
                    ++this.bufPos;
                    this.ch = this.charAt(this.pos);
                }
            }
            this.stringVal = this.addSymbol();
            this.token = Token.VARIANT;
        }
    }
    
    protected void scanVariable_at() {
        if (this.ch != '@') {
            throw new ParserException("illegal variable. " + this.info());
        }
        this.mark = this.pos;
        this.bufPos = 1;
        final char c1 = this.charAt(this.pos + 1);
        if (c1 == '@') {
            ++this.pos;
            ++this.bufPos;
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
    
    public void scanComment() {
        if (!this.allowComment) {
            throw new NotAllowCommentException();
        }
        if ((this.ch == '/' && this.charAt(this.pos + 1) == '/') || (this.ch == '-' && this.charAt(this.pos + 1) == '-')) {
            this.scanSingleLineComment();
        }
        else {
            if (this.ch != '/' || this.charAt(this.pos + 1) != '*') {
                throw new IllegalStateException();
            }
            this.scanMultiLineComment();
        }
    }
    
    protected final void scanHiveComment() {
        if (this.ch != '/' && this.ch != '-') {
            throw new IllegalStateException();
        }
        final Token lastToken = this.token;
        this.mark = this.pos;
        this.bufPos = 0;
        this.scanChar();
        if (this.ch == ' ') {
            this.mark = this.pos;
            this.bufPos = 0;
            this.scanChar();
            if (this.dbType == DbType.odps && this.ch == ' ') {
                this.mark = this.pos;
                this.bufPos = 0;
                this.scanChar();
            }
        }
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
            while (true) {
                if (this.ch == '*') {
                    if (this.charAt(this.pos + 1) == '/') {
                        this.bufPos += 2;
                        this.scanChar();
                        this.scanChar();
                        break;
                    }
                    if (CharTypes.isWhitespace(this.charAt(this.pos + 1))) {
                        int i;
                        for (i = 2; i < 1048576 && CharTypes.isWhitespace(this.charAt(this.pos + i)); ++i) {}
                        if (this.charAt(this.pos + i) == '/') {
                            this.bufPos += 2;
                            this.pos += i + 1;
                            this.ch = this.charAt(this.pos);
                            break;
                        }
                    }
                }
                this.scanChar();
                if (this.ch == '\u001a') {
                    break;
                }
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
            if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
                return;
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
                        ++this.line;
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
                    if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
                        return;
                    }
                    return;
                }
                if (this.charAt(this.pos + 1) == '\n') {
                    ++this.line;
                    this.bufPos += 2;
                    this.scanChar();
                    continue;
                }
                ++this.bufPos;
                continue;
            }
        }
    }
    
    private void scanMultiLineComment() {
        final Token lastToken = this.token;
        int depth = 1;
        this.scanChar();
        this.scanChar();
        this.mark = this.pos;
        this.bufPos = 0;
        while (true) {
            if (this.ch == '/' && this.charAt(this.pos + 1) == '*') {
                this.scanChar();
                this.scanChar();
                if (this.ch == '!' || this.ch == '+') {
                    this.scanChar();
                    ++depth;
                }
            }
            if (this.ch == '*' && this.charAt(this.pos + 1) == '/') {
                this.scanChar();
                this.scanChar();
                if (0 == --depth) {
                    this.stringVal = this.subString(this.mark, this.bufPos);
                    this.token = Token.MULTI_LINE_COMMENT;
                    ++this.commentCount;
                    if (this.keepComments) {
                        this.addComment(this.stringVal);
                    }
                    if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
                        return;
                    }
                    if (!this.isAllowComment() && !this.isSafeComment(this.stringVal)) {
                        throw new NotAllowCommentException();
                    }
                    return;
                }
            }
            if (this.ch == '\u001a') {
                throw new ParserException("unterminated /* comment. " + this.info());
            }
            this.scanChar();
            ++this.bufPos;
        }
    }
    
    private void scanSingleLineComment() {
        final Token lastToken = this.token;
        this.mark = this.pos;
        this.bufPos = 2;
        this.scanChar();
        this.scanChar();
        while (true) {
            while (this.ch != '\r') {
                if (this.ch == '\n') {
                    ++this.line;
                    this.scanChar();
                }
                else if (this.ch != '\u001a') {
                    this.scanChar();
                    ++this.bufPos;
                    continue;
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
                if (!this.isAllowComment() && !this.isSafeComment(this.stringVal)) {
                    throw new NotAllowCommentException();
                }
                return;
            }
            if (this.charAt(this.pos + 1) == '\n') {
                ++this.line;
                this.scanChar();
                continue;
            }
            ++this.bufPos;
            continue;
        }
    }
    
    public void scanIdentifier() {
        this.hash_lower = 0L;
        this.hash = 0L;
        final char first = this.ch;
        if (this.ch == '`') {
            this.mark = this.pos;
            this.bufPos = 1;
            final int startPos = this.pos + 1;
            final int quoteIndex = this.text.indexOf(96, startPos);
            if (quoteIndex == -1) {
                throw new ParserException("illegal identifier. " + this.info());
            }
            this.hash_lower = -3750763034362895579L;
            this.hash = -3750763034362895579L;
            for (int i = startPos; i < quoteIndex; ++i) {
                final char ch = this.text.charAt(i);
                this.hash_lower ^= ((ch >= 'A' && ch <= 'Z') ? (ch + ' ') : ch);
                this.hash_lower *= 1099511628211L;
                this.hash ^= ch;
                this.hash *= 1099511628211L;
            }
            this.stringVal = MySqlLexer.quoteTable.addSymbol(this.text, this.pos, quoteIndex + 1 - this.pos, this.hash);
            this.pos = quoteIndex + 1;
            this.ch = this.charAt(this.pos);
            this.token = Token.IDENTIFIER;
        }
        else {
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
            char ch2 = '\0';
            while (true) {
                final char c0 = ch2;
                ch2 = this.charAt(++this.pos);
                if (!CharTypes.isIdentifierChar(ch2)) {
                    if ((ch2 != '\uff08' && ch2 != '\uff09') || c0 <= '\u0100') {
                        break;
                    }
                    ++this.bufPos;
                }
                else {
                    this.hash_lower ^= ((ch2 >= 'A' && ch2 <= 'Z') ? (ch2 + ' ') : ch2);
                    this.hash_lower *= 1099511628211L;
                    this.hash ^= ch2;
                    this.hash *= 1099511628211L;
                    ++this.bufPos;
                }
            }
            this.ch = this.charAt(this.pos);
            if (this.bufPos != 1) {
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
            switch (first) {
                case '\uff08': {
                    this.token = Token.LPAREN;
                }
                case '\uff09': {
                    this.token = Token.RPAREN;
                }
                default: {
                    this.token = Token.IDENTIFIER;
                    this.stringVal = CharTypes.valueOf(first);
                    if (this.stringVal == null) {
                        this.stringVal = Character.toString(first);
                    }
                }
            }
        }
    }
    
    public void scanNumber() {
        this.mark = this.pos;
        this.numberSale = 0;
        this.numberExp = false;
        this.bufPos = 0;
        if (this.ch == '0' && this.charAt(this.pos + 1) == 'b' && this.dbType != DbType.odps) {
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
            if (ch < '2' || ch > '9') {
                this.bufPos += i;
                this.pos += i;
                this.stringVal = this.subString(mark, i - 2);
                this.ch = this.charAt(this.pos);
                this.token = Token.BITS;
                return;
            }
        }
        if (this.ch == '-') {
            ++this.bufPos;
            this.ch = this.charAt(++this.pos);
        }
        while (this.ch >= '0' && this.ch <= '9') {
            ++this.bufPos;
            this.ch = this.charAt(++this.pos);
        }
        if (this.ch == '.') {
            if (this.charAt(this.pos + 1) == '.') {
                this.token = Token.LITERAL_INT;
                return;
            }
            ++this.bufPos;
            this.ch = this.charAt(++this.pos);
            this.numberSale = 0;
            while (this.ch >= '0' && this.ch <= '9') {
                ++this.bufPos;
                this.ch = this.charAt(++this.pos);
                ++this.numberSale;
            }
            this.numberExp = true;
        }
        if ((this.ch == 'e' || this.ch == 'E') && (this.isDigit(this.charAt(this.pos + 1)) || (isDigit2(this.charAt(this.pos + 1)) && isDigit2(this.charAt(this.pos + 2))))) {
            this.numberExp = true;
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
            if ((this.ch >= 'a' && this.ch <= 'z') || (this.ch >= 'A' && this.ch <= 'Z')) {
                this.numberExp = false;
            }
        }
        if (this.numberSale > 0 || this.numberExp) {
            if (this.text.charAt(this.mark) == '.' && CharTypes.isIdentifierChar(this.ch)) {
                this.pos = this.mark + 1;
                this.ch = this.charAt(this.pos);
                this.token = Token.DOT;
                return;
            }
            this.token = Token.LITERAL_FLOAT;
        }
        else {
            if (this.ch != '`' && CharTypes.isFirstIdentifierChar(this.ch) && this.ch != '\uff09' && (this.ch != 'b' || this.bufPos != 1 || this.charAt(this.pos - 1) != '0' || this.dbType == DbType.odps)) {
                ++this.bufPos;
                boolean brace = false;
                while (true) {
                    final char c0 = this.ch;
                    this.ch = this.charAt(++this.pos);
                    if (this.isEOF()) {
                        break;
                    }
                    if (!CharTypes.isIdentifierChar(this.ch)) {
                        if (this.ch == '{' && this.charAt(this.pos - 1) == '$' && !brace) {
                            ++this.bufPos;
                            brace = true;
                        }
                        else if (this.ch == '}' && brace) {
                            ++this.bufPos;
                            brace = false;
                        }
                        else {
                            if ((this.ch != '\uff08' && this.ch != '\uff09') || c0 <= '\u0100') {
                                break;
                            }
                            ++this.bufPos;
                        }
                    }
                    else {
                        ++this.bufPos;
                    }
                }
                this.stringVal = this.addSymbol();
                this.hash_lower = FnvHash.hashCode64(this.stringVal);
                this.token = Token.IDENTIFIER;
                return;
            }
            this.token = Token.LITERAL_INT;
        }
    }
    
    public void scanHexaDecimal() {
        this.mark = this.pos;
        this.bufPos = 0;
        if (this.ch == '-') {
            ++this.bufPos;
            this.ch = this.charAt(++this.pos);
        }
        while (CharTypes.isHex(this.ch)) {
            ++this.bufPos;
            this.ch = this.charAt(++this.pos);
        }
        if (CharTypes.isIdentifierChar(this.ch)) {
            do {
                ++this.bufPos;
                this.ch = this.charAt(++this.pos);
            } while (CharTypes.isIdentifierChar(this.ch));
            this.mark -= 2;
            this.bufPos += 2;
            this.stringVal = this.addSymbol();
            this.hash_lower = FnvHash.hashCode64(this.stringVal);
            this.token = Token.IDENTIFIER;
            return;
        }
        this.token = Token.LITERAL_HEX;
    }
    
    public String hexString() {
        return this.subString(this.mark, this.bufPos);
    }
    
    public final boolean isDigit(final char ch) {
        return ch >= '0' && ch <= '9';
    }
    
    protected static final boolean isDigit2(final char ch) {
        return ch == '+' || ch == '-' || (ch >= '0' && ch <= '9');
    }
    
    protected final void putChar(final char ch) {
        if (this.bufPos == this.buf.length) {
            final char[] newsbuf = new char[this.buf.length * 2];
            System.arraycopy(this.buf, 0, newsbuf, 0, this.buf.length);
            this.buf = newsbuf;
        }
        this.buf[this.bufPos++] = ch;
    }
    
    public final int pos() {
        return this.pos;
    }
    
    public final String stringVal() {
        if (this.stringVal == null) {
            this.stringVal = this.subString(this.mark, this.bufPos);
        }
        return this.stringVal;
    }
    
    private final void stringVal(final StringBuffer out) {
        if (this.stringVal != null) {
            out.append(this.stringVal);
            return;
        }
        out.append(this.text, this.mark, this.mark + this.bufPos);
    }
    
    public final boolean identifierEquals(final String text) {
        if (this.token != Token.IDENTIFIER) {
            return false;
        }
        if (this.stringVal == null) {
            this.stringVal = this.subString(this.mark, this.bufPos);
        }
        return text.equalsIgnoreCase(this.stringVal);
    }
    
    public final boolean identifierEquals(final long hash_lower) {
        if (this.token != Token.IDENTIFIER) {
            return false;
        }
        if (this.hash_lower == 0L) {
            if (this.stringVal == null) {
                this.stringVal = this.subString(this.mark, this.bufPos);
            }
            this.hash_lower = FnvHash.fnv1a_64_lower(this.stringVal);
        }
        return this.hash_lower == hash_lower;
    }
    
    public final long hash_lower() {
        if (this.hash_lower == 0L) {
            if (this.stringVal == null) {
                this.stringVal = this.subString(this.mark, this.bufPos);
            }
            this.hash_lower = FnvHash.fnv1a_64_lower(this.stringVal);
        }
        return this.hash_lower;
    }
    
    public final List<String> readAndResetComments() {
        final List<String> comments = this.comments;
        this.comments = null;
        return comments;
    }
    
    private boolean isOperator(final char ch) {
        switch (ch) {
            case '!':
            case '%':
            case '&':
            case '*':
            case '+':
            case '-':
            case ';':
            case '<':
            case '=':
            case '>':
            case '^':
            case '|':
            case '~': {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public final boolean isNegativeIntegerValue() {
        return this.charAt(this.mark) == '-';
    }
    
    public final Number integerValue() {
        long result = 0L;
        boolean negative = false;
        int i = this.mark;
        final int max = this.mark + this.bufPos;
        long limit;
        if (this.charAt(this.mark) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            ++i;
        }
        else {
            limit = -9223372036854775807L;
        }
        final long multmin = negative ? -922337203685477580L : -922337203685477580L;
        if (i < max) {
            final int digit = this.charAt(i++) - '0';
            result = -digit;
        }
        while (i < max) {
            final int digit = this.charAt(i++) - '0';
            if (result < multmin) {
                return new BigInteger(this.numberString());
            }
            result *= 10L;
            if (result < limit + digit) {
                return new BigInteger(this.numberString());
            }
            result -= digit;
        }
        if (negative) {
            if (i <= this.mark + 1) {
                throw new NumberFormatException(this.numberString());
            }
            if (result >= -2147483648L) {
                return (int)result;
            }
            return result;
        }
        else {
            result = -result;
            if (result <= 2147483647L) {
                return (int)result;
            }
            return result;
        }
    }
    
    public int bp() {
        return this.pos;
    }
    
    public char current() {
        return this.ch;
    }
    
    public void reset(final int mark, final char markChar, final Token token) {
        this.pos = mark;
        this.ch = markChar;
        this.token = token;
    }
    
    public final String numberString() {
        return this.text.substring(this.mark, this.mark + this.bufPos);
    }
    
    public BigDecimal decimalValue() {
        if (this.numberSale > 0 && !this.numberExp) {
            final int len = this.bufPos;
            if (len < 20) {
                long unscaleVal = 0L;
                boolean negative = false;
                int i = 0;
                final char first = this.text.charAt(this.mark);
                if (first == '+') {
                    ++i;
                }
                else if (first == '-') {
                    ++i;
                    negative = true;
                }
                while (i < len) {
                    final char ch = this.text.charAt(this.mark + i);
                    if (ch != '.') {
                        final int digit = ch - '0';
                        unscaleVal = unscaleVal * 10L + digit;
                    }
                    ++i;
                }
                return BigDecimal.valueOf(negative ? (-unscaleVal) : unscaleVal, this.numberSale);
            }
        }
        final char[] value = this.sub_chars(this.mark, this.bufPos);
        if (!StringUtils.isNumber(value)) {
            throw new ParserException((Object)value + " is not a number! " + this.info());
        }
        return new BigDecimal(value);
    }
    
    public SQLNumberExpr numberExpr() {
        final char[] value = this.sub_chars(this.mark, this.bufPos);
        if (!StringUtils.isNumber(value)) {
            throw new ParserException((Object)value + " is not a number! " + this.info());
        }
        return new SQLNumberExpr(value);
    }
    
    public SQLNumberExpr numberExpr(final SQLObject parent) {
        final char[] value = this.sub_chars(this.mark, this.bufPos);
        if (!StringUtils.isNumber(value)) {
            throw new ParserException((Object)value + " is not a number! " + this.info());
        }
        return new SQLNumberExpr(value, parent);
    }
    
    public SQLNumberExpr numberExpr(final boolean negate) {
        final char[] value = this.sub_chars(this.mark, this.bufPos);
        if (!StringUtils.isNumber(value)) {
            throw new ParserException((Object)value + " is not a number! " + this.info());
        }
        if (negate) {
            final char[] chars = new char[value.length + 1];
            chars[0] = '-';
            System.arraycopy(value, 0, chars, 1, value.length);
            return new SQLNumberExpr(chars);
        }
        return new SQLNumberExpr(value);
    }
    
    public boolean hasComment() {
        return this.comments != null;
    }
    
    public int getCommentCount() {
        return this.commentCount;
    }
    
    public void skipToEOF() {
        this.pos = this.text.length();
        this.token = Token.EOF;
    }
    
    public boolean isEndOfComment() {
        return this.endOfComment;
    }
    
    protected boolean isSafeComment(String comment) {
        if (comment == null) {
            return true;
        }
        comment = comment.toLowerCase();
        return comment.indexOf("select") == -1 && comment.indexOf("delete") == -1 && comment.indexOf("insert") == -1 && comment.indexOf("update") == -1 && comment.indexOf("into") == -1 && comment.indexOf("where") == -1 && comment.indexOf("or") == -1 && comment.indexOf("and") == -1 && comment.indexOf("union") == -1 && comment.indexOf(39) == -1 && comment.indexOf(61) == -1 && comment.indexOf(62) == -1 && comment.indexOf(60) == -1 && comment.indexOf(38) == -1 && comment.indexOf(124) == -1 && comment.indexOf(94) == -1;
    }
    
    protected void addComment(final String comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<String>(2);
        }
        this.comments.add(comment);
    }
    
    public List<String> getComments() {
        return this.comments;
    }
    
    public int getLine() {
        return this.line;
    }
    
    public void computeRowAndColumn() {
        int line = 1;
        int column = 1;
        for (int i = 0; i < this.startPos; ++i) {
            final char ch = this.text.charAt(i);
            if (ch == '\n') {
                column = 0;
                ++line;
            }
            else {
                ++column;
            }
        }
        this.posLine = line;
        this.posColumn = column;
    }
    
    public int getPosLine() {
        return this.posLine;
    }
    
    public int getPosColumn() {
        return this.posColumn;
    }
    
    public void config(final SQLParserFeature feature, final boolean state) {
        this.features = SQLParserFeature.config(this.features, feature, state);
        if (feature == SQLParserFeature.OptimizedForParameterized) {
            this.optimizedForParameterized = state;
        }
        else if (feature == SQLParserFeature.KeepComments) {
            this.keepComments = state;
        }
        else if (feature == SQLParserFeature.KeepSourceLocation) {
            this.keepSourceLocation = state;
        }
        else if (feature == SQLParserFeature.SkipComments) {
            this.skipComment = state;
        }
    }
    
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    
    public void setTimeZone(final TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    
    public final boolean isEnabled(final SQLParserFeature feature) {
        return SQLParserFeature.isEnabled(this.features, feature);
    }
    
    public static String parameterize(final String sql, final DbType dbType) {
        final Lexer lexer = SQLParserUtils.createLexer(sql, dbType);
        lexer.optimizedForParameterized = true;
        lexer.nextToken();
        final StringBuffer buf = new StringBuffer();
        while (true) {
            final Token token = lexer.token;
            switch (token) {
                case LITERAL_ALIAS:
                case LITERAL_FLOAT:
                case LITERAL_CHARS:
                case LITERAL_INT:
                case LITERAL_NCHARS:
                case LITERAL_HEX:
                case VARIANT: {
                    if (buf.length() != 0) {
                        buf.append(' ');
                    }
                    buf.append('?');
                    break;
                }
                case COMMA: {
                    buf.append(',');
                    break;
                }
                case EQ: {
                    buf.append('=');
                    break;
                }
                case EOF: {
                    return buf.toString();
                }
                case ERROR: {
                    return sql;
                }
                case SELECT: {
                    buf.append("SELECT");
                    break;
                }
                case UPDATE: {
                    buf.append("UPDATE");
                    break;
                }
                default: {
                    if (buf.length() != 0) {
                        buf.append(' ');
                    }
                    lexer.stringVal(buf);
                    break;
                }
            }
            lexer.nextToken();
        }
    }
    
    public String getSource() {
        return this.text;
    }
    
    static {
        Lexer.symbols_l2 = new SymbolTable(512);
        digits = new int[58];
        for (int i = 48; i <= 57; ++i) {
            Lexer.digits[i] = i - 48;
        }
    }
    
    public static class SavePoint
    {
        int bp;
        int sp;
        int np;
        char ch;
        long hash;
        long hash_lower;
        public Token token;
        String stringVal;
    }
    
    public interface CommentHandler
    {
        boolean handle(final Token p0, final String p1);
    }
}
