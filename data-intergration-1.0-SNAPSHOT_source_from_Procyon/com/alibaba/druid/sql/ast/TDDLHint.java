// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlExprParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import java.util.ArrayList;
import java.util.List;

public class TDDLHint extends SQLCommentHint
{
    private List<Function> functions;
    private String json;
    private Type type;
    
    public List<Function> getFunctions() {
        return this.functions;
    }
    
    public TDDLHint(final String text) {
        super(text);
        this.functions = new ArrayList<Function>();
        this.type = Type.Unknown;
        final MySqlExprParser hintParser = new MySqlExprParser(text, new SQLParserFeature[] { SQLParserFeature.TDDLHint });
        final Lexer lexer = hintParser.getLexer();
        if (lexer.token() == Token.PLUS || lexer.token() == Token.BANG) {
            lexer.nextToken();
        }
        if (!lexer.identifierEquals(FnvHash.Constants.TDDL)) {
            return;
        }
        lexer.nextToken();
        switch (lexer.token()) {
            case COLON: {
                lexer.nextToken();
                while (true) {
                    if (lexer.token() == Token.AND) {
                        lexer.nextToken();
                    }
                    String name = lexer.stringVal();
                    final long hash = lexer.hash_lower();
                    if (lexer.identifierEquals(FnvHash.Constants.NODE)) {
                        lexer.nextToken();
                        if (lexer.token() == Token.IN) {
                            lexer.nextToken();
                            name = "NODE_IN";
                        }
                        else if (lexer.token() == Token.EQ) {
                            lexer.nextToken();
                            name = "NODE_IN";
                            final SQLExpr value = hintParser.primary();
                            final Function function = new Function(name);
                            final Argument argument = new Argument(null, value);
                            function.getArguments().add(argument);
                            this.functions.add(function);
                            if (lexer.token() == Token.EOF) {
                                break;
                            }
                            continue;
                        }
                    }
                    else if (hash == FnvHash.Constants.SCAN || hash == FnvHash.Constants.DEFER) {
                        lexer.nextToken();
                        if (lexer.token() == Token.EQ) {
                            lexer.nextToken();
                            final SQLExpr value = hintParser.primary();
                            final Function function = new Function(name);
                            final Argument argument = new Argument(null, value);
                            function.getArguments().add(argument);
                            this.functions.add(function);
                            if (lexer.token() == Token.EOF) {
                                break;
                            }
                            continue;
                        }
                        else if (lexer.token() == Token.EOF) {
                            final Function function2 = new Function(name);
                            this.functions.add(function2);
                            break;
                        }
                    }
                    else if (hash == FnvHash.Constants.SQL_DELAY_CUTOFF || hash == FnvHash.Constants.SOCKET_TIMEOUT || hash == FnvHash.Constants.UNDO_LOG_LIMIT || hash == FnvHash.Constants.FORBID_EXECUTE_DML_ALL) {
                        lexer.nextToken();
                        if (lexer.token() == Token.EQ) {
                            lexer.nextToken();
                            final SQLExpr value = hintParser.primary();
                            final Function function = new Function(name);
                            final Argument argument = new Argument(null, value);
                            function.getArguments().add(argument);
                            this.functions.add(function);
                            if (lexer.token() == Token.EOF) {
                                break;
                            }
                            continue;
                        }
                    }
                    else {
                        lexer.nextToken();
                    }
                    if (lexer.token() == Token.DOT) {
                        lexer.nextToken();
                        if (!lexer.identifierEquals("partition_key")) {
                            return;
                        }
                        final String table = name;
                        lexer.nextToken();
                        hintParser.accept(Token.EQ);
                        SQLExpr value2 = hintParser.primary();
                        final Function function3 = new Function("PARTITIONS");
                        this.functions.add(function3);
                        function3.getArguments().add(new Argument(new SQLPropertyExpr(name, "partition_key"), value2));
                        while (lexer.token() == Token.AND) {
                            lexer.nextToken();
                            final SQLExpr key = hintParser.primary();
                            hintParser.accept(Token.EQ);
                            value2 = hintParser.primary();
                            function3.getArguments().add(new Argument(key, value2));
                        }
                        if (lexer.token() == Token.EOF) {
                            break;
                        }
                    }
                    else if (lexer.token() == Token.EQ) {
                        lexer.nextToken();
                        final SQLExpr value = hintParser.primary();
                        final Function function = new Function(name);
                        final Argument argument = new Argument(null, value);
                        function.getArguments().add(argument);
                        this.functions.add(function);
                        if (lexer.token() == Token.EOF) {
                            break;
                        }
                        continue;
                    }
                    final Function function2 = new Function(name);
                    this.functions.add(function2);
                    if (hash == FnvHash.Constants.MASTER) {
                        if (lexer.token() == Token.EOF) {
                            break;
                        }
                        if (lexer.token() == Token.BAR) {
                            lexer.nextToken();
                            continue;
                        }
                    }
                    if (hash == FnvHash.Constants.SLAVE) {
                        if (lexer.token() == Token.EOF) {
                            break;
                        }
                        if (lexer.token() == Token.AND) {
                            lexer.nextToken();
                            continue;
                        }
                    }
                    if (lexer.token() == Token.AND) {
                        continue;
                    }
                    hintParser.accept(Token.LPAREN);
                    if (lexer.token() != Token.RPAREN) {
                        while (true) {
                            final Lexer.SavePoint mark = lexer.mark();
                            SQLExpr value3 = null;
                            final String keyVal = lexer.stringVal();
                            final long keyHash = lexer.hash_lower();
                            lexer.nextToken();
                            SQLIdentifierExpr key2 = new SQLIdentifierExpr(keyVal, keyHash);
                            if (lexer.token() == Token.EQ) {
                                hintParser.accept(Token.EQ);
                                if (lexer.token() == Token.LITERAL_ALIAS) {
                                    String stringVal = lexer.stringVal();
                                    stringVal = stringVal.substring(1, stringVal.length() - 1);
                                    value3 = new SQLCharExpr(stringVal);
                                    value3 = hintParser.exprRest(value3);
                                    lexer.nextToken();
                                }
                                else {
                                    value3 = hintParser.expr();
                                }
                            }
                            if (value3 == null) {
                                lexer.reset(mark);
                                key2 = null;
                                value3 = hintParser.expr();
                            }
                            final Argument argument2 = new Argument(key2, value3);
                            function2.getArguments().add(argument2);
                            if (lexer.token() == Token.COMMA) {
                                lexer.nextToken();
                            }
                            else {
                                if (lexer.token() == Token.RPAREN) {
                                    break;
                                }
                                continue;
                            }
                        }
                        lexer.nextToken();
                    }
                    else {
                        lexer.nextToken();
                    }
                    if (lexer.token() == Token.AND) {
                        continue;
                    }
                    if (hash == FnvHash.Constants.MASTER && lexer.token() == Token.BAR) {
                        lexer.nextToken();
                    }
                    if (lexer.token() == Token.EOF) {
                        break;
                    }
                }
                if (this.functions.size() > 0) {
                    this.type = Type.Function;
                }
            }
            case LPAREN: {
                final int rp = text.lastIndexOf(41);
                if (rp != -1) {
                    this.json = text.substring(lexer.pos(), rp);
                    this.type = Type.JSON;
                }
            }
            default: {}
        }
    }
    
    public String getJson() {
        return this.json;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public static class Function
    {
        private final String name;
        private final List<Argument> arguments;
        
        public Function(final String name) {
            this.arguments = new ArrayList<Argument>();
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public List<Argument> getArguments() {
            return this.arguments;
        }
    }
    
    public static class Argument
    {
        private final SQLExpr name;
        private final SQLExpr value;
        
        public Argument(final SQLExpr name, final SQLExpr value) {
            this.name = name;
            this.value = value;
        }
        
        public SQLExpr getName() {
            return this.name;
        }
        
        public SQLExpr getValue() {
            return this.value;
        }
    }
    
    public enum Type
    {
        Function, 
        JSON, 
        Unknown;
    }
}
