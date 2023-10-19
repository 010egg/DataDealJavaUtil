// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast;

import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlUnique extends MySqlKey
{
    @Override
    protected void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getColumns());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public MySqlUnique clone() {
        final MySqlUnique x = new MySqlUnique();
        this.cloneTo(x);
        return x;
    }
    
    public SQLExpr getDbPartitionBy() {
        return this.indexDefinition.getDbPartitionBy();
    }
    
    public void setDbPartitionBy(final SQLExpr x) {
        this.indexDefinition.setDbPartitionBy(x);
    }
    
    public boolean isGlobal() {
        return this.indexDefinition.isGlobal();
    }
    
    public void setGlobal(final boolean global) {
        this.indexDefinition.setGlobal(global);
    }
    
    public boolean isLocal() {
        return this.indexDefinition.isLocal();
    }
    
    public void setLocal(final boolean local) {
        this.indexDefinition.setLocal(local);
    }
    
    public SQLExpr getTablePartitions() {
        return this.indexDefinition.getTbPartitions();
    }
    
    public void setTablePartitions(final SQLExpr x) {
        this.indexDefinition.setTbPartitions(x);
    }
    
    public SQLExpr getTablePartitionBy() {
        return this.indexDefinition.getTbPartitionBy();
    }
    
    public void setTablePartitionBy(final SQLExpr x) {
        this.indexDefinition.setTbPartitionBy(x);
    }
}
