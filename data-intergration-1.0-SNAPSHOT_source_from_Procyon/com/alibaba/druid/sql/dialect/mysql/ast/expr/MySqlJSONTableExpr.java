// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;

public class MySqlJSONTableExpr extends MySqlExprImpl
{
    private final List<Column> columns;
    private SQLExpr expr;
    private SQLExpr path;
    
    public MySqlJSONTableExpr() {
        this.columns = new ArrayList<Column>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.expr);
            this.acceptChild(v, this.path);
            this.acceptChild(v, this.columns);
        }
        v.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return null;
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.expr = x;
    }
    
    public SQLExpr getPath() {
        return this.path;
    }
    
    public void setPath(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.path = x;
    }
    
    public List<Column> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final Column column) {
        column.setParent(this);
        this.columns.add(column);
    }
    
    public static class Column extends MySqlObjectImpl
    {
        private final List<Column> nestedColumns;
        private SQLName name;
        private SQLDataType dataType;
        private SQLExpr path;
        private boolean ordinality;
        private boolean exists;
        private SQLExpr onError;
        private SQLExpr onEmpty;
        
        public Column() {
            this.nestedColumns = new ArrayList<Column>();
        }
        
        @Override
        public void accept0(final MySqlASTVisitor v) {
            if (v.visit(this)) {
                this.acceptChild(v, this.name);
                this.acceptChild(v, this.dataType);
                this.acceptChild(v, this.path);
                this.acceptChild(v, this.onEmpty);
                this.acceptChild(v, this.onError);
            }
            v.endVisit(this);
        }
        
        public SQLName getName() {
            return this.name;
        }
        
        public void setName(final SQLName x) {
            if (x != null) {
                x.setParent(this);
            }
            this.name = x;
        }
        
        public SQLDataType getDataType() {
            return this.dataType;
        }
        
        public void setDataType(final SQLDataType x) {
            if (x != null) {
                x.setParent(this);
            }
            this.dataType = x;
        }
        
        public SQLExpr getPath() {
            return this.path;
        }
        
        public void setPath(final SQLExpr x) {
            if (x != null) {
                x.setParent(this);
            }
            this.path = x;
        }
        
        public boolean isOrdinality() {
            return this.ordinality;
        }
        
        public void setOrdinality(final boolean ordinality) {
            this.ordinality = ordinality;
        }
        
        public boolean isExists() {
            return this.exists;
        }
        
        public void setExists(final boolean exists) {
            this.exists = exists;
        }
        
        public SQLExpr getOnError() {
            return this.onError;
        }
        
        public void setOnError(final SQLExpr x) {
            if (x != null) {
                x.setParent(this);
            }
            this.onError = x;
        }
        
        public SQLExpr getOnEmpty() {
            return this.onEmpty;
        }
        
        public void setOnEmpty(final SQLExpr x) {
            if (x != null) {
                x.setParent(this);
            }
            this.onEmpty = x;
        }
        
        public List<Column> getNestedColumns() {
            return this.nestedColumns;
        }
        
        public void addNestedColumn(final Column column) {
            column.setParent(this);
            this.nestedColumns.add(column);
        }
    }
}
