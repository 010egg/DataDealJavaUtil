// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.Iterator;
import java.util.Collection;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterViewStatement extends SQLStatementImpl implements SQLCreateStatement
{
    private boolean force;
    protected SQLSelect subQuery;
    protected boolean ifNotExists;
    protected String algorithm;
    protected SQLName definer;
    protected String sqlSecurity;
    protected SQLExprTableSource tableSource;
    protected final List<SQLTableElement> columns;
    private boolean withCheckOption;
    private boolean withCascaded;
    private boolean withLocal;
    private boolean withReadOnly;
    private SQLLiteralExpr comment;
    
    public SQLAlterViewStatement() {
        this.force = false;
        this.ifNotExists = false;
        this.columns = new ArrayList<SQLTableElement>();
    }
    
    public SQLAlterViewStatement(final DbType dbType) {
        super(dbType);
        this.force = false;
        this.ifNotExists = false;
        this.columns = new ArrayList<SQLTableElement>();
    }
    
    public String computeName() {
        if (this.tableSource == null) {
            return null;
        }
        final SQLExpr expr = this.tableSource.getExpr();
        if (expr instanceof SQLName) {
            final String name = ((SQLName)expr).getSimpleName();
            return SQLUtils.normalize(name);
        }
        return null;
    }
    
    public String getSchema() {
        final SQLName name = this.getName();
        if (name == null) {
            return null;
        }
        if (name instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)name).getOwnernName();
        }
        return null;
    }
    
    public SQLName getName() {
        if (this.tableSource == null) {
            return null;
        }
        return (SQLName)this.tableSource.getExpr();
    }
    
    public void setName(final SQLName name) {
        this.setTableSource(new SQLExprTableSource(name));
    }
    
    public void setName(final String name) {
        this.setName(new SQLIdentifierExpr(name));
    }
    
    public SQLExprTableSource getTableSource() {
        return this.tableSource;
    }
    
    public void setTableSource(final SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSource = tableSource;
    }
    
    public boolean isWithCheckOption() {
        return this.withCheckOption;
    }
    
    public void setWithCheckOption(final boolean withCheckOption) {
        this.withCheckOption = withCheckOption;
    }
    
    public boolean isWithCascaded() {
        return this.withCascaded;
    }
    
    public void setWithCascaded(final boolean withCascaded) {
        this.withCascaded = withCascaded;
    }
    
    public boolean isWithLocal() {
        return this.withLocal;
    }
    
    public void setWithLocal(final boolean withLocal) {
        this.withLocal = withLocal;
    }
    
    public boolean isWithReadOnly() {
        return this.withReadOnly;
    }
    
    public void setWithReadOnly(final boolean withReadOnly) {
        this.withReadOnly = withReadOnly;
    }
    
    public SQLSelect getSubQuery() {
        return this.subQuery;
    }
    
    public void setSubQuery(final SQLSelect subQuery) {
        if (subQuery != null) {
            subQuery.setParent(this);
        }
        this.subQuery = subQuery;
    }
    
    public List<SQLTableElement> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLTableElement column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
    
    public SQLLiteralExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLLiteralExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
    
    public String getAlgorithm() {
        return this.algorithm;
    }
    
    public void setAlgorithm(final String algorithm) {
        this.algorithm = algorithm;
    }
    
    public SQLName getDefiner() {
        return this.definer;
    }
    
    public void setDefiner(final SQLName definer) {
        if (definer != null) {
            definer.setParent(this);
        }
        this.definer = definer;
    }
    
    public String getSqlSecurity() {
        return this.sqlSecurity;
    }
    
    public void setSqlSecurity(final String sqlSecurity) {
        this.sqlSecurity = sqlSecurity;
    }
    
    public boolean isForce() {
        return this.force;
    }
    
    public void setForce(final boolean force) {
        this.force = force;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.comment);
            this.acceptChild(visitor, this.subQuery);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.tableSource != null) {
            children.add(this.tableSource);
        }
        children.addAll(this.columns);
        if (this.comment != null) {
            children.add(this.comment);
        }
        if (this.subQuery != null) {
            children.add(this.subQuery);
        }
        return children;
    }
    
    @Override
    public SQLAlterViewStatement clone() {
        final SQLAlterViewStatement x = new SQLAlterViewStatement();
        x.force = this.force;
        if (this.subQuery != null) {
            x.setSubQuery(this.subQuery.clone());
        }
        x.ifNotExists = this.ifNotExists;
        x.algorithm = this.algorithm;
        if (this.definer != null) {
            x.setDefiner(this.definer.clone());
        }
        x.sqlSecurity = this.sqlSecurity;
        if (this.tableSource != null) {
            x.setTableSource(this.tableSource.clone());
        }
        for (final SQLTableElement column : this.columns) {
            final SQLTableElement column2 = column.clone();
            column2.setParent(x);
            x.columns.add(column2);
        }
        x.withCheckOption = this.withCheckOption;
        x.withCascaded = this.withCascaded;
        x.withLocal = this.withLocal;
        x.withReadOnly = this.withReadOnly;
        if (this.comment != null) {
            x.setComment(this.comment.clone());
        }
        return x;
    }
}
