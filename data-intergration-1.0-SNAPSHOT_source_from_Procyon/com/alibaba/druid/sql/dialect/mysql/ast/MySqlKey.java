// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast;

import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.ads.visitor.AdsVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLIndex;
import com.alibaba.druid.sql.ast.statement.SQLTableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLUniqueConstraint;
import com.alibaba.druid.sql.ast.statement.SQLUnique;

public class MySqlKey extends SQLUnique implements SQLUniqueConstraint, SQLTableConstraint, SQLIndex
{
    public MySqlKey() {
        this.dbType = DbType.mysql;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
        }
        else if (visitor instanceof AdsVisitor) {
            this.accept0((AdsVisitor)visitor);
        }
    }
    
    protected void accept0(final AdsVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getColumns());
            this.acceptChild(visitor, this.getName());
        }
        visitor.endVisit(this);
    }
    
    protected void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getColumns());
            this.acceptChild(visitor, this.getName());
        }
        visitor.endVisit(this);
    }
    
    public String getIndexType() {
        return this.indexDefinition.getOptions().getIndexType();
    }
    
    public void setIndexType(final String indexType) {
        this.indexDefinition.getOptions().setIndexType(indexType);
    }
    
    public boolean isHasConstraint() {
        return this.indexDefinition.hasConstraint();
    }
    
    public void setHasConstraint(final boolean hasConstraint) {
        this.indexDefinition.setHasConstraint(hasConstraint);
    }
    
    public void cloneTo(final MySqlKey x) {
        super.cloneTo(x);
    }
    
    @Override
    public MySqlKey clone() {
        final MySqlKey x = new MySqlKey();
        this.cloneTo(x);
        return x;
    }
    
    public SQLExpr getKeyBlockSize() {
        return this.indexDefinition.getOptions().getKeyBlockSize();
    }
    
    public void setKeyBlockSize(final SQLExpr x) {
        this.indexDefinition.getOptions().setKeyBlockSize(x);
    }
}
