// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.visitor;

import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLScriptCommitStatement;
import com.alibaba.druid.sql.ast.statement.SQLStartTransactionStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerWaitForStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerRollbackStatement;
import com.alibaba.druid.sql.ast.statement.SQLCommitStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLGrantStatement;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerSetTransactionIsolationLevelStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerExecStatement;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.statement.SQLColumnConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import java.util.List;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerOutput;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.sqlserver.ast.expr.SQLServerObjectReferenceExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerInsertStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerUpdateStatement;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerTop;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerSelectQueryBlock;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class SQLServerOutputVisitor extends SQLASTOutputVisitor implements SQLServerASTVisitor
{
    public SQLServerOutputVisitor(final Appendable appender) {
        super(appender, DbType.sqlserver);
    }
    
    public SQLServerOutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
        this.dbType = DbType.sqlserver;
    }
    
    @Override
    public boolean visit(final SQLServerSelectQueryBlock x) {
        this.print0(this.ucase ? "SELECT " : "select ");
        if (1 == x.getDistionOption()) {
            this.print0(this.ucase ? "ALL " : "all ");
        }
        else if (2 == x.getDistionOption()) {
            this.print0(this.ucase ? "DISTINCT " : "distinct ");
        }
        else if (3 == x.getDistionOption()) {
            this.print0(this.ucase ? "UNIQUE " : "unique ");
        }
        final SQLServerTop top = x.getTop();
        if (top != null) {
            this.visit(top);
            this.print(' ');
        }
        this.printSelectList(x.getSelectList());
        final SQLExprTableSource into = x.getInto();
        if (into != null) {
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            this.printTableSource(into);
        }
        final SQLTableSource from = x.getFrom();
        if (from != null) {
            this.println();
            this.print0(this.ucase ? "FROM " : "from ");
            this.printTableSource(from);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            this.printExpr(where);
        }
        final SQLSelectGroupByClause groupBy = x.getGroupBy();
        if (groupBy != null) {
            this.println();
            this.visit(groupBy);
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            this.println();
            this.visit(orderBy);
        }
        this.printFetchFirst(x);
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerTop x) {
        final boolean parameterized = this.parameterized;
        this.parameterized = false;
        this.print0(this.ucase ? "TOP " : "top ");
        boolean paren = false;
        if (x.getParent() instanceof SQLServerUpdateStatement || x.getParent() instanceof SQLServerInsertStatement) {
            paren = true;
            this.print('(');
        }
        x.getExpr().accept(this);
        if (paren) {
            this.print(')');
        }
        if (x.isPercent()) {
            this.print0(this.ucase ? " PERCENT" : " percent");
        }
        this.parameterized = parameterized;
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerObjectReferenceExpr x) {
        this.print0(x.toString());
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerInsertStatement x) {
        this.print0(this.ucase ? "INSERT " : "insert ");
        if (x.getTop() != null) {
            x.getTop().setParent(x);
            x.getTop().accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "INTO " : "into ");
        x.getTableSource().accept(this);
        this.printInsertColumns(x.getColumns());
        if (x.getOutput() != null) {
            this.println();
            x.getOutput().setParent(x);
            x.getOutput().accept(this);
        }
        if (x.getValuesList().size() != 0) {
            this.println();
            this.print0(this.ucase ? "VALUES " : "values ");
            for (int i = 0, size = x.getValuesList().size(); i < size; ++i) {
                if (i != 0) {
                    this.print(',');
                    this.println();
                }
                x.getValuesList().get(i).accept(this);
            }
        }
        if (x.getQuery() != null) {
            this.println();
            x.getQuery().accept(this);
        }
        if (x.isDefaultValues()) {
            this.print0(this.ucase ? " DEFAULT VALUES" : " default values");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerUpdateStatement x) {
        this.print0(this.ucase ? "UPDATE " : "update ");
        final SQLServerTop top = x.getTop();
        if (top != null) {
            top.accept(this);
            this.print(' ');
        }
        this.printTableSource(x.getTableSource());
        this.println();
        this.print0(this.ucase ? "SET " : "set ");
        for (int i = 0, size = x.getItems().size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            final SQLUpdateSetItem item = x.getItems().get(i);
            this.visit(item);
        }
        final SQLServerOutput output = x.getOutput();
        if (output != null) {
            this.println();
            this.visit(output);
        }
        final SQLTableSource from = x.getFrom();
        if (from != null) {
            this.println();
            this.print0(this.ucase ? "FROM " : "from ");
            this.printTableSource(from);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            ++this.indentCount;
            this.print0(this.ucase ? "WHERE " : "where ");
            this.printExpr(where);
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExprTableSource x) {
        this.printTableSourceExpr(x.getExpr());
        final String alias = x.getAlias();
        if (alias != null) {
            this.print(' ');
            this.print0(alias);
        }
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.print0(this.ucase ? " WITH (" : " with (");
            this.printAndAccept(x.getHints(), ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLColumnDefinition x) {
        final boolean parameterized = this.parameterized;
        this.parameterized = false;
        x.getName().accept(this);
        if (x.getDataType() != null) {
            this.print(' ');
            x.getDataType().accept(this);
        }
        if (x.getDefaultExpr() != null) {
            this.visitColumnDefault(x);
        }
        for (final SQLColumnConstraint item : x.getConstraints()) {
            this.print(' ');
            item.accept(this);
        }
        final SQLColumnDefinition.Identity identity = x.getIdentity();
        if (identity != null) {
            this.print(' ');
            identity.accept(this);
        }
        if (x.getEnable() != null && x.getEnable()) {
            this.print0(this.ucase ? " ENABLE" : " enable");
        }
        this.parameterized = parameterized;
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerExecStatement x) {
        this.print0(this.ucase ? "EXEC " : "exec ");
        final SQLName returnStatus = x.getReturnStatus();
        if (returnStatus != null) {
            returnStatus.accept(this);
            this.print0(" = ");
        }
        final SQLName moduleName = x.getModuleName();
        if (moduleName != null) {
            moduleName.accept(this);
            this.print(' ');
        }
        else {
            this.print0(" (");
        }
        this.printAndAccept(x.getParameters(), ", ");
        if (moduleName == null) {
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerSetTransactionIsolationLevelStatement x) {
        this.print0(this.ucase ? "SET TRANSACTION ISOLATION LEVEL " : "set transaction isolation level ");
        this.print0(x.getLevel());
        return false;
    }
    
    @Override
    public boolean visit(final SQLSetStatement x) {
        this.print0(this.ucase ? "SET " : "set ");
        final SQLSetStatement.Option option = x.getOption();
        if (option != null) {
            this.print(option.name());
            this.print(' ');
        }
        final List<SQLAssignItem> items = x.getItems();
        for (int i = 0; i < items.size(); ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            final SQLAssignItem item = x.getItems().get(i);
            item.getTarget().accept(this);
            final SQLExpr value = item.getValue();
            if (value instanceof SQLIdentifierExpr && (((SQLIdentifierExpr)value).nameHashCode64() == FnvHash.Constants.ON || ((SQLIdentifierExpr)value).nameHashCode64() == FnvHash.Constants.OFF)) {
                this.print(' ');
            }
            else {
                this.print0(" = ");
            }
            value.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerOutput x) {
        this.print0(this.ucase ? "OUTPUT " : "output ");
        this.printSelectList(x.getSelectList());
        if (x.getInto() != null) {
            ++this.indentCount;
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            x.getInto().accept(this);
            if (x.getColumns().size() > 0) {
                ++this.indentCount;
                this.println();
                this.print('(');
                for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
                    if (i != 0) {
                        if (i % 5 == 0) {
                            this.println();
                        }
                        this.print0(", ");
                    }
                    x.getColumns().get(i).accept(this);
                }
                this.print(')');
                --this.indentCount;
            }
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLBlockStatement x) {
        this.print0(this.ucase ? "BEGIN" : "begin");
        ++this.indentCount;
        this.println();
        for (int i = 0, size = x.getStatementList().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }
            final SQLStatement stmt = x.getStatementList().get(i);
            stmt.setParent(x);
            stmt.accept(this);
            this.print(';');
        }
        --this.indentCount;
        this.println();
        this.print0(this.ucase ? "END" : "end");
        return false;
    }
    
    @Override
    protected void printGrantOn(final SQLGrantStatement x) {
        if (x.getResource() != null) {
            this.print0(this.ucase ? " ON " : " on ");
            if (x.getResourceType() != null) {
                this.print0(x.getResourceType().name());
                this.print0("::");
            }
            x.getResource().accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLSelect x) {
        super.visit(x);
        if (x.isForBrowse()) {
            this.println();
            this.print0(this.ucase ? "FOR BROWSE" : "for browse");
        }
        if (x.getForXmlOptionsSize() > 0) {
            this.println();
            this.print0(this.ucase ? "FOR XML " : "for xml ");
            for (int i = 0; i < x.getForXmlOptions().size(); ++i) {
                if (i != 0) {
                    this.print0(", ");
                    this.print0(x.getForXmlOptions().get(i));
                }
            }
        }
        if (x.getXmlPath() != null) {
            this.println();
            this.print0(this.ucase ? "FOR XML " : "for xml ");
            x.getXmlPath().accept(this);
        }
        if (x.getOffset() != null) {
            this.println();
            this.print0(this.ucase ? "OFFSET " : "offset ");
            x.getOffset().accept(this);
            this.print0(this.ucase ? " ROWS" : " rows");
            if (x.getRowCount() != null) {
                this.print0(this.ucase ? " FETCH NEXT " : " fetch next ");
                x.getRowCount().accept(this);
                this.print0(this.ucase ? " ROWS ONLY" : " rows only");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCommitStatement x) {
        this.print0(this.ucase ? "COMMIT" : "commit");
        if (x.isWork()) {
            this.print0(this.ucase ? " WORK" : " work");
        }
        else {
            this.print0(this.ucase ? " TRANSACTION" : " transaction");
            if (x.getTransactionName() != null) {
                this.print(' ');
                x.getTransactionName().accept(this);
            }
            if (x.getDelayedDurability() != null) {
                this.print0(this.ucase ? " WITH ( DELAYED_DURABILITY = " : " with ( delayed_durability = ");
                x.getDelayedDurability().accept(this);
                this.print0(" )");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerRollbackStatement x) {
        this.print0(this.ucase ? "ROLLBACK" : "rollback");
        if (x.isWork()) {
            this.print0(this.ucase ? " WORK" : " work");
        }
        else {
            this.print0(this.ucase ? " TRANSACTION" : " transaction");
            if (x.getName() != null) {
                this.print(' ');
                x.getName().accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerWaitForStatement x) {
        this.print0(this.ucase ? "WAITFOR" : "waitfor");
        if (x.getDelay() != null) {
            this.print0(this.ucase ? " DELAY " : " delay ");
            x.getDelay().accept(this);
        }
        else if (x.getTime() != null) {
            this.print0(this.ucase ? " TIME " : " time ");
            x.getTime().accept(this);
        }
        if (x.getStatement() != null) {
            this.print0(this.ucase ? " DELAY " : " delay ");
            x.getStatement().accept(this);
        }
        if (x.getTimeout() != null) {
            this.print0(this.ucase ? " ,TIMEOUT " : " ,timeout ");
            x.getTimeout().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerExecStatement.SQLServerParameter x) {
        x.getExpr().accept(this);
        if (x.getType()) {
            this.print0(this.ucase ? " OUT" : " out");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLStartTransactionStatement x) {
        this.print0(this.ucase ? "BEGIN TRANSACTION" : "begin transaction");
        if (x.getName() != null) {
            this.print(' ');
            x.getName().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLScriptCommitStatement x) {
        this.print0(this.ucase ? "GO" : "go");
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateUserStatement x) {
        this.print0(this.ucase ? "CREATE USER " : "create user ");
        x.getUser().accept(this);
        this.print0(this.ucase ? " WITH PASSWORD = " : " with password = ");
        final SQLExpr passoword = x.getPassword();
        if (passoword instanceof SQLIdentifierExpr) {
            this.print('\'');
            passoword.accept(this);
            this.print('\'');
        }
        else {
            passoword.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSequenceExpr x) {
        final SQLSequenceExpr.Function function = x.getFunction();
        switch (function) {
            case NextVal: {
                this.print0(this.ucase ? "NEXT VALUE FOR " : "next value for ");
                this.printExpr(x.getSequence());
                return false;
            }
            default: {
                throw new ParserException("not support function : " + function);
            }
        }
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddColumn x) {
        final boolean odps = this.isOdps();
        this.print0(this.ucase ? "ADD " : "add ");
        this.printAndAccept(x.getColumns(), ", ");
        return false;
    }
}
