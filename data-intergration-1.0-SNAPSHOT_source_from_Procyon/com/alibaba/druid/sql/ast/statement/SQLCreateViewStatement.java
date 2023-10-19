// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;
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
import com.alibaba.druid.sql.ast.SQLTableDataType;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateViewStatement extends SQLStatementImpl implements SQLCreateStatement
{
    private boolean orReplace;
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
    private SQLVariantRefExpr returns;
    private SQLTableDataType returnsDataType;
    protected boolean onCluster;
    private SQLName to;
    private SQLBlockStatement script;
    
    public SQLCreateViewStatement() {
        this.orReplace = false;
        this.force = false;
        this.ifNotExists = false;
        this.columns = new ArrayList<SQLTableElement>();
    }
    
    public SQLCreateViewStatement(final DbType dbType) {
        super(dbType);
        this.orReplace = false;
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
    
    public boolean isOrReplace() {
        return this.orReplace;
    }
    
    public void setOrReplace(final boolean orReplace) {
        this.orReplace = orReplace;
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
            if (this.tableSource != null) {
                this.tableSource.accept(visitor);
            }
            for (int i = 0; i < this.columns.size(); ++i) {
                final SQLTableElement column = this.columns.get(i);
                if (column != null) {
                    column.accept(visitor);
                }
            }
            if (this.comment != null) {
                this.comment.accept(visitor);
            }
            if (this.subQuery != null) {
                this.subQuery.accept(visitor);
            }
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
    
    public boolean isOnCluster() {
        return this.onCluster;
    }
    
    public void setOnCluster(final boolean onCluster) {
        this.onCluster = onCluster;
    }
    
    public SQLName getTo() {
        return this.to;
    }
    
    public void setTo(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.to = x;
    }
    
    public SQLVariantRefExpr getReturns() {
        return this.returns;
    }
    
    public void setReturns(final SQLVariantRefExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.returns = x;
    }
    
    public SQLTableDataType getReturnsDataType() {
        return this.returnsDataType;
    }
    
    public void setReturnsDataType(final SQLTableDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.returnsDataType = x;
    }
    
    public SQLBlockStatement getScript() {
        return this.script;
    }
    
    public void setScript(final SQLBlockStatement x) {
        if (x != null) {
            x.setParent(this);
        }
        this.script = x;
    }
    
    @Override
    public SQLCreateViewStatement clone() {
        final SQLCreateViewStatement x = new SQLCreateViewStatement();
        x.orReplace = this.orReplace;
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
        x.onCluster = this.onCluster;
        if (x.to != null) {
            this.to = x.to.clone();
        }
        if (x.returns != null) {
            this.returns = x.returns.clone();
        }
        if (x.returnsDataType != null) {
            this.returnsDataType = x.returnsDataType.clone();
        }
        return x;
    }
    
    public enum Level
    {
        CASCADED, 
        LOCAL;
    }
    
    public static class Column extends SQLObjectImpl
    {
        private SQLExpr expr;
        private SQLCharExpr comment;
        
        public SQLExpr getExpr() {
            return this.expr;
        }
        
        public void setExpr(final SQLExpr expr) {
            if (expr != null) {
                expr.setParent(this);
            }
            this.expr = expr;
        }
        
        public SQLCharExpr getComment() {
            return this.comment;
        }
        
        public void setComment(final SQLCharExpr comment) {
            if (comment != null) {
                comment.setParent(this);
            }
            this.comment = comment;
        }
        
        @Override
        protected void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.expr);
                this.acceptChild(visitor, this.comment);
            }
        }
    }
}
