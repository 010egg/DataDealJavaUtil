// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import java.util.TimeZone;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.DbType;

public class SQLParser
{
    protected final Lexer lexer;
    protected DbType dbType;
    private int errorEndPos;
    
    public SQLParser(final String sql, final DbType dbType, final SQLParserFeature... features) {
        this(new Lexer(sql, null, dbType), dbType);
        for (final SQLParserFeature feature : features) {
            this.config(feature, true);
        }
        this.lexer.nextToken();
    }
    
    public SQLParser(final String sql) {
        this(sql, null, new SQLParserFeature[0]);
    }
    
    public SQLParser(final Lexer lexer) {
        this(lexer, null);
        if (this.dbType == null) {
            this.dbType = lexer.dbType;
        }
    }
    
    public SQLParser(final Lexer lexer, final DbType dbType) {
        this.errorEndPos = -1;
        this.lexer = lexer;
        this.dbType = dbType;
    }
    
    public final Lexer getLexer() {
        return this.lexer;
    }
    
    public DbType getDbType() {
        return this.dbType;
    }
    
    protected boolean identifierEquals(final String text) {
        return this.lexer.identifierEquals(text);
    }
    
    protected void acceptIdentifier(final String text) {
        if (this.lexer.identifierEquals(text)) {
            this.lexer.nextToken();
            return;
        }
        this.setErrorEndPos(this.lexer.pos());
        throw new ParserException("syntax error, expect " + text + ", actual " + this.lexer.token + ", " + this.lexer.info());
    }
    
    protected String tableAlias() {
        return this.tableAlias(false);
    }
    
    protected String tableAlias(final boolean must) {
        final Token token = this.lexer.token;
        if (token == Token.CONNECT || token == Token.START || token == Token.SELECT || token == Token.FROM || token == Token.WHERE) {
            if (token == Token.WHERE && this.dbType == DbType.odps) {
                return null;
            }
            if (must) {
                throw new ParserException("illegal alias. " + this.lexer.info());
            }
            return null;
        }
        else {
            if (token == Token.IDENTIFIER) {
                String ident = this.lexer.stringVal;
                final long hash = this.lexer.hash_lower;
                if (this.isEnabled(SQLParserFeature.IgnoreNameQuotes) && ident.length() > 1) {
                    ident = StringUtils.removeNameQuotes(ident);
                }
                if (hash == FnvHash.Constants.START || hash == FnvHash.Constants.CONNECT || hash == FnvHash.Constants.NATURAL || hash == FnvHash.Constants.CROSS || hash == FnvHash.Constants.OFFSET || hash == FnvHash.Constants.LIMIT) {
                    if (must) {
                        throw new ParserException("illegal alias. " + this.lexer.info());
                    }
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextToken();
                    switch (this.lexer.token) {
                        case EOF:
                        case COMMA:
                        case WHERE:
                        case INNER:
                        case LEFT:
                        case RIGHT:
                        case FULL:
                        case ON:
                        case GROUP:
                        case ORDER: {
                            return ident;
                        }
                        case JOIN: {
                            if (hash != FnvHash.Constants.NATURAL && hash != FnvHash.Constants.CROSS) {
                                return ident;
                            }
                            this.lexer.reset(mark);
                            break;
                        }
                        default: {
                            this.lexer.reset(mark);
                            break;
                        }
                    }
                    return null;
                }
                else if (!must) {
                    if (hash == FnvHash.Constants.MODEL) {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.token == Token.PARTITION || this.lexer.identifierEquals(FnvHash.Constants.DIMENSION) || this.lexer.identifierEquals(FnvHash.Constants.IGNORE) || this.lexer.identifierEquals(FnvHash.Constants.KEEP)) {
                            this.lexer.reset(mark);
                            return null;
                        }
                        return ident;
                    }
                    else if (hash == FnvHash.Constants.WINDOW) {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.token == Token.IDENTIFIER) {
                            this.lexer.reset(mark);
                            return null;
                        }
                        return ident;
                    }
                    else if (hash == FnvHash.Constants.DISTRIBUTE || hash == FnvHash.Constants.SORT || hash == FnvHash.Constants.CLUSTER || hash == FnvHash.Constants.ZORDER) {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.token == Token.BY) {
                            this.lexer.reset(mark);
                            return null;
                        }
                        return ident;
                    }
                    else if (hash == FnvHash.Constants.ASOF && this.dbType == DbType.clickhouse) {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.token == Token.LEFT || this.lexer.token == Token.JOIN) {
                            this.lexer.reset(mark);
                            return null;
                        }
                        return ident;
                    }
                }
            }
            Label_1585: {
                if (!must) {
                    switch (token) {
                        case INNER:
                        case LEFT:
                        case RIGHT:
                        case FULL: {
                            final Lexer.SavePoint mark2 = this.lexer.mark();
                            final String strVal = this.lexer.stringVal();
                            this.lexer.nextToken();
                            if (this.lexer.token == Token.OUTER || this.lexer.token == Token.JOIN || this.lexer.identifierEquals(FnvHash.Constants.ANTI) || this.lexer.identifierEquals(FnvHash.Constants.SEMI)) {
                                this.lexer.reset(mark2);
                                return null;
                            }
                            return strVal;
                        }
                        case OUTER:
                        case IN:
                        case SET:
                        case BY: {
                            final Lexer.SavePoint mark2 = this.lexer.mark();
                            final String strVal = this.lexer.stringVal();
                            this.lexer.nextToken();
                            switch (this.lexer.token) {
                                case WHERE:
                                case LEFT:
                                case RIGHT:
                                case FULL:
                                case ON:
                                case GROUP:
                                case ORDER:
                                case JOIN:
                                case RPAREN:
                                case SEMI: {
                                    return strVal;
                                }
                                default: {
                                    this.lexer.reset(mark2);
                                    break Label_1585;
                                }
                            }
                            break;
                        }
                        case FOR:
                        case GRANT:
                        case CHECK:
                        case LEAVE:
                        case TRIGGER:
                        case CREATE:
                        case ASC:
                        case INOUT:
                        case DESC:
                        case SCHEMA:
                        case IS:
                        case DECLARE:
                        case DROP:
                        case FETCH:
                        case LOCK: {
                            if (this.dbType == DbType.odps || this.dbType == DbType.hive) {
                                final String strVal2 = this.lexer.stringVal();
                                this.lexer.nextToken();
                                return strVal2;
                            }
                            break;
                        }
                        case PARTITION: {
                            if (this.dbType != DbType.odps && this.dbType != DbType.hive) {
                                break;
                            }
                            final Lexer.SavePoint mark2 = this.lexer.mark();
                            final String strVal = this.lexer.stringVal();
                            this.lexer.nextToken();
                            if (this.lexer.token == Token.LPAREN) {
                                this.lexer.reset(mark2);
                                return null;
                            }
                            return strVal;
                        }
                        case TABLE: {
                            if (this.dbType != DbType.odps) {
                                break;
                            }
                            final Lexer.SavePoint mark2 = this.lexer.mark();
                            final String strVal = this.lexer.stringVal();
                            this.lexer.nextToken();
                            switch (this.lexer.token) {
                                case ON:
                                case GROUP:
                                case ORDER:
                                case FROM: {
                                    return strVal;
                                }
                                default: {
                                    this.lexer.reset(mark2);
                                    break Label_1585;
                                }
                            }
                            break;
                        }
                        case SHOW:
                        case REFERENCES:
                        case REPEAT:
                        case USE:
                        case MOD:
                        case OUT: {
                            final String strVal2 = this.lexer.stringVal();
                            this.lexer.nextToken();
                            return strVal2;
                        }
                        case DISTRIBUTE: {
                            final String strVal2 = this.lexer.stringVal();
                            final Lexer.SavePoint mark3 = this.lexer.mark();
                            this.lexer.nextToken();
                            if (this.lexer.token == Token.BY) {
                                this.lexer.reset(mark3);
                                return null;
                            }
                            return strVal2;
                        }
                        case MINUS:
                        case EXCEPT:
                        case LIMIT:
                        case BETWEEN: {
                            if (this.dbType != DbType.odps) {
                                break;
                            }
                            final Lexer.SavePoint mark2 = this.lexer.mark();
                            final String strVal = this.lexer.stringVal();
                            this.lexer.nextToken();
                            switch (this.lexer.token) {
                                case EOF:
                                case COMMA:
                                case WHERE:
                                case LEFT:
                                case RIGHT:
                                case FULL:
                                case ON:
                                case GROUP:
                                case ORDER:
                                case JOIN:
                                case RPAREN:
                                case SEMI: {
                                    return strVal;
                                }
                                default: {
                                    this.lexer.reset(mark2);
                                    break Label_1585;
                                }
                            }
                            break;
                        }
                        case UNION: {
                            final Lexer.SavePoint mark2 = this.lexer.mark();
                            final String strVal = this.lexer.stringVal();
                            this.lexer.nextToken();
                            switch (this.lexer.token) {
                                case INNER:
                                case LEFT:
                                case RIGHT:
                                case GROUP:
                                case ORDER:
                                case JOIN:
                                case RPAREN:
                                case SEMI: {
                                    return strVal;
                                }
                                default: {
                                    this.lexer.reset(mark2);
                                    return null;
                                }
                            }
                            break;
                        }
                    }
                }
            }
            if (must) {
                if (this.dbType == DbType.odps) {
                    switch (this.lexer.token) {
                        case GROUP:
                        case ORDER: {
                            final Lexer.SavePoint mark2 = this.lexer.mark();
                            final String strVal = this.lexer.stringVal();
                            this.lexer.nextToken();
                            if (this.lexer.token == Token.BY) {
                                this.lexer.reset(mark2);
                                return null;
                            }
                            return strVal;
                        }
                        case UNION: {
                            final Lexer.SavePoint mark2 = this.lexer.mark();
                            final String strVal = this.lexer.stringVal();
                            this.lexer.nextToken();
                            if (this.lexer.token == Token.ALL) {
                                this.lexer.reset(mark2);
                                return null;
                            }
                            return strVal;
                        }
                        case LIMIT: {
                            final Lexer.SavePoint mark2 = this.lexer.mark();
                            final String strVal = this.lexer.stringVal();
                            this.lexer.nextToken();
                            if (this.lexer.token == Token.LITERAL_INT) {
                                this.lexer.reset(mark2);
                                return null;
                            }
                            return strVal;
                        }
                        case BETWEEN: {
                            final Lexer.SavePoint mark2 = this.lexer.mark();
                            final String strVal = this.lexer.stringVal();
                            this.lexer.nextToken();
                            switch (this.lexer.token) {
                                case INNER:
                                case LEFT:
                                case RIGHT:
                                case GROUP:
                                case ORDER:
                                case JOIN:
                                case SEMI: {
                                    return strVal;
                                }
                                default: {
                                    this.lexer.reset(mark2);
                                    return null;
                                }
                            }
                            break;
                        }
                    }
                }
                return this.alias();
            }
            return this.as();
        }
    }
    
    protected String as() {
        String alias = null;
        final Token token = this.lexer.token;
        if (token == Token.COMMA) {
            return null;
        }
        if (token == Token.AS) {
            this.lexer.nextTokenAlias();
            if (this.lexer.token == Token.LPAREN) {
                return null;
            }
            if (this.dbType == DbType.oracle && (this.lexer.token == Token.COMMA || this.lexer.token == Token.FROM)) {
                return null;
            }
            alias = this.lexer.stringVal();
            this.lexer.nextToken();
            if (alias != null) {
                while (this.lexer.token == Token.DOT) {
                    this.lexer.nextToken();
                    alias = alias + '.' + this.lexer.token.name();
                    this.lexer.nextToken();
                }
                return alias;
            }
            if (this.lexer.token == Token.LPAREN) {
                return null;
            }
            throw new ParserException("Error : " + this.lexer.info());
        }
        else {
            if (this.lexer.token == Token.LITERAL_ALIAS) {
                alias = this.lexer.stringVal();
                this.lexer.nextToken();
            }
            else if (this.lexer.token == Token.IDENTIFIER) {
                alias = this.lexer.stringVal();
                this.lexer.nextToken();
            }
            else if (this.lexer.token == Token.LITERAL_CHARS) {
                alias = "'" + this.lexer.stringVal() + "'";
                this.lexer.nextToken();
            }
            else {
                switch (this.lexer.token) {
                    case FULL:
                    case OUTER:
                    case LEAVE:
                    case REPEAT:
                    case MOD:
                    case CASE:
                    case USER:
                    case LOB:
                    case END:
                    case DEFERRED:
                    case DO:
                    case LOOP:
                    case STORE:
                    case ANY:
                    case BEGIN:
                    case CAST:
                    case COMPUTE:
                    case ESCAPE:
                    case MERGE:
                    case OPEN:
                    case SOME:
                    case TRUNCATE:
                    case UNTIL:
                    case VIEW:
                    case KILL:
                    case COMMENT:
                    case TABLESPACE:
                    case PRIMARY:
                    case FOREIGN:
                    case UNIQUE:
                    case ENABLE:
                    case DISABLE:
                    case REPLACE: {
                        alias = this.lexer.stringVal();
                        this.lexer.nextToken();
                        break;
                    }
                    case IN:
                    case BY:
                    case CREATE:
                    case ASC:
                    case INOUT:
                    case DESC:
                    case TABLE:
                    case USE:
                    case MINUS:
                    case EXCEPT:
                    case LIMIT:
                    case UNION:
                    case INTERSECT:
                    case UPDATE:
                    case DELETE:
                    case EXPLAIN:
                    case ALTER:
                    case INTO: {
                        alias = this.lexer.stringVal();
                        final Lexer.SavePoint mark = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.token() != Token.COMMA && this.lexer.token() != Token.RPAREN && this.lexer.token() != Token.FROM && this.lexer.token() != Token.SEMI && this.lexer.token() != Token.SEMI) {
                            alias = null;
                            this.lexer.reset(mark);
                            break;
                        }
                        break;
                    }
                    case INNER:
                    case LEFT:
                    case RIGHT:
                    case CHECK:
                    case SHOW:
                    case REFERENCES:
                    case INDEX:
                    case ALL:
                    case CLOSE:
                    case VALUES:
                    case SEQUENCE:
                    case TO:
                    case LIKE:
                    case RLIKE:
                    case NULL:
                    case DATABASE: {
                        if (this.dbType == DbType.odps || this.dbType == DbType.hive) {
                            alias = this.lexer.stringVal();
                            this.lexer.nextToken();
                            break;
                        }
                        break;
                    }
                    case GROUP:
                    case ORDER:
                    case DISTRIBUTE:
                    case DEFAULT: {
                        if (this.dbType != DbType.odps && this.dbType != DbType.hive) {
                            break;
                        }
                        final Lexer.SavePoint mark = this.lexer.mark();
                        alias = this.lexer.stringVal();
                        this.lexer.nextToken();
                        if (this.lexer.token == Token.BY) {
                            this.lexer.reset(mark);
                            alias = null;
                            break;
                        }
                        break;
                    }
                }
            }
            switch (this.lexer.token) {
                case KEY:
                case INTERVAL:
                case CONSTRAINT: {
                    alias = this.lexer.token.name();
                    this.lexer.nextToken();
                    return alias;
                }
                default: {
                    if (this.isEnabled(SQLParserFeature.IgnoreNameQuotes) && alias != null && alias.length() > 1) {
                        alias = StringUtils.removeNameQuotes(alias);
                    }
                    return alias;
                }
            }
        }
    }
    
    protected String alias() {
        String alias = null;
        if (this.lexer.token == Token.LITERAL_ALIAS) {
            alias = this.lexer.stringVal();
            this.lexer.nextToken();
        }
        else if (this.lexer.token == Token.IDENTIFIER) {
            alias = this.lexer.stringVal();
            this.lexer.nextToken();
        }
        else if (this.lexer.token == Token.LITERAL_CHARS) {
            alias = "'" + this.lexer.stringVal() + "'";
            this.lexer.nextToken();
        }
        else {
            if (this.lexer.token == Token.LITERAL_FLOAT && this.dbType == DbType.odps) {
                String numStr = this.lexer.numberString();
                this.lexer.nextToken();
                if (this.lexer.token == Token.IDENTIFIER) {
                    numStr += this.lexer.stringVal();
                    this.lexer.nextToken();
                }
                return numStr;
            }
            switch (this.lexer.token) {
                case INNER:
                case LEFT:
                case RIGHT:
                case FULL:
                case OUTER:
                case IN:
                case BY:
                case FOR:
                case GRANT:
                case CHECK:
                case LEAVE:
                case TRIGGER:
                case CREATE:
                case ASC:
                case INOUT:
                case DESC:
                case SCHEMA:
                case IS:
                case DECLARE:
                case DROP:
                case LOCK:
                case PARTITION:
                case TABLE:
                case SHOW:
                case REFERENCES:
                case REPEAT:
                case USE:
                case OUT:
                case DISTRIBUTE:
                case MINUS:
                case EXCEPT:
                case LIMIT:
                case CASE:
                case USER:
                case LOB:
                case END:
                case DEFERRED:
                case DO:
                case LOOP:
                case STORE:
                case ANY:
                case BEGIN:
                case CAST:
                case COMPUTE:
                case ESCAPE:
                case MERGE:
                case OPEN:
                case SOME:
                case TRUNCATE:
                case UNTIL:
                case VIEW:
                case KILL:
                case COMMENT:
                case TABLESPACE:
                case PRIMARY:
                case FOREIGN:
                case UNIQUE:
                case ENABLE:
                case DISABLE:
                case REPLACE:
                case INTERSECT:
                case UPDATE:
                case DELETE:
                case EXPLAIN:
                case ALTER:
                case INTO:
                case INDEX:
                case ALL:
                case CLOSE:
                case VALUES:
                case SEQUENCE:
                case TO:
                case LIKE:
                case NULL:
                case DATABASE:
                case DEFAULT:
                case KEY:
                case INTERVAL:
                case CONSTRAINT:
                case PCTFREE:
                case INITRANS:
                case MAXTRANS:
                case SEGMENT:
                case CREATION:
                case IMMEDIATE:
                case STORAGE:
                case NEXT:
                case MINEXTENTS:
                case MAXEXTENTS:
                case MAXSIZE:
                case PCTINCREASE:
                case FLASH_CACHE:
                case CELL_FLASH_CACHE:
                case NONE:
                case ROW:
                case CHUNK:
                case CACHE:
                case NOCACHE:
                case LOGGING:
                case NOCOMPRESS:
                case KEEP_DUPLICATES:
                case EXCEPTIONS:
                case PURGE:
                case INITIALLY:
                case ANALYZE:
                case OPTIMIZE:
                case REVOKE:
                case NEW:
                case IDENTIFIED:
                case PASSWORD:
                case BINARY:
                case WINDOW:
                case OFFSET:
                case SHARE:
                case START:
                case CONNECT:
                case MATCHED:
                case ERRORS:
                case REJECT:
                case UNLIMITED:
                case EXCLUSIVE:
                case MODE:
                case ADVISE:
                case TYPE:
                case FUNCTION:
                case AS:
                case PARTITIONED:
                case TRUE:
                case FALSE: {
                    alias = this.lexer.stringVal();
                    this.lexer.nextToken();
                    return alias;
                }
                case GROUP:
                case ORDER: {
                    if (this.dbType != DbType.odps && this.dbType != DbType.hive) {
                        break;
                    }
                    final Lexer.SavePoint mark = this.lexer.mark();
                    alias = this.lexer.stringVal();
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.BY) {
                        this.lexer.reset(mark);
                        alias = null;
                        break;
                    }
                    break;
                }
                case QUES: {
                    alias = "?";
                    this.lexer.nextToken();
                    return alias;
                }
                case UNION: {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    final String strVal = this.lexer.stringVal();
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.ALL) {
                        this.lexer.reset(mark);
                        return null;
                    }
                    return strVal;
                }
            }
        }
        return alias;
    }
    
    protected void printError(final Token token) {
        String arround;
        if (this.lexer.mark >= 0 && this.lexer.text.length() > this.lexer.mark + 30) {
            int begin;
            int end;
            if (this.lexer.mark - 5 > 0) {
                begin = this.lexer.mark - 5;
                end = this.lexer.mark + 30;
            }
            else {
                begin = this.lexer.mark;
                end = this.lexer.mark + 30;
            }
            if (begin < 10) {
                begin = 0;
            }
            else {
                for (int i = 1; i < 10 && i < begin; ++i) {
                    final char ch = this.lexer.text.charAt(begin - i);
                    if (ch == ' ' || ch == '\n') {
                        begin = begin - i + 1;
                    }
                }
            }
            arround = this.lexer.text.substring(begin, end);
        }
        else if (this.lexer.mark >= 0) {
            if (this.lexer.mark - 5 > 0) {
                arround = this.lexer.text.substring(this.lexer.mark - 5);
            }
            else {
                arround = this.lexer.text.substring(this.lexer.mark);
            }
        }
        else {
            arround = this.lexer.text;
        }
        final StringBuilder buf = new StringBuilder().append("syntax error, error in :'").append(arround);
        if (token != this.lexer.token) {
            buf.append("', expect ").append(token.name).append(", actual ").append(this.lexer.token.name);
        }
        buf.append(", ").append(this.lexer.info());
        throw new ParserException(buf.toString());
    }
    
    public void accept(final Token token) {
        if (this.lexer.token == token) {
            this.lexer.nextToken();
        }
        else {
            this.setErrorEndPos(this.lexer.pos());
            this.printError(token);
        }
    }
    
    public int acceptInteger() {
        if (this.lexer.token == Token.LITERAL_INT) {
            final int intVal = (int)this.lexer.integerValue();
            this.lexer.nextToken();
            return intVal;
        }
        throw new ParserException("syntax error, expect int, actual " + this.lexer.token + " " + this.lexer.info());
    }
    
    public void match(final Token token) {
        if (this.lexer.token != token) {
            throw new ParserException("syntax error, expect " + token + ", actual " + this.lexer.token + " " + this.lexer.info());
        }
    }
    
    protected void setErrorEndPos(final int errPos) {
        if (errPos > this.errorEndPos) {
            this.errorEndPos = errPos;
        }
    }
    
    public void config(final SQLParserFeature feature, final boolean state) {
        this.lexer.config(feature, state);
    }
    
    public TimeZone getTimeZone() {
        return this.lexer.getTimeZone();
    }
    
    public void setTimeZone(final TimeZone timeZone) {
        this.lexer.setTimeZone(timeZone);
    }
    
    public final boolean isEnabled(final SQLParserFeature feature) {
        return this.lexer.isEnabled(feature);
    }
    
    protected SQLCreateTableStatement newCreateStatement() {
        return new SQLCreateTableStatement(this.dbType);
    }
}
