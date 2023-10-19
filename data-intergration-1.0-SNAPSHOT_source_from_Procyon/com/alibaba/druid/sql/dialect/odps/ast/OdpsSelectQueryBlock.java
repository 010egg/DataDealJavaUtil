// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBase;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLZOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;

public class OdpsSelectQueryBlock extends SQLSelectQueryBlock
{
    private SQLZOrderBy zOrderBy;
    
    public OdpsSelectQueryBlock() {
        this.dbType = DbType.odps;
        this.clusterBy = new ArrayList<SQLSelectOrderByItem>();
        this.distributeBy = new ArrayList<SQLSelectOrderByItem>();
        this.sortBy = new ArrayList<SQLSelectOrderByItem>(2);
    }
    
    @Override
    public OdpsSelectQueryBlock clone() {
        final OdpsSelectQueryBlock x = new OdpsSelectQueryBlock();
        this.cloneTo(x);
        return x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof OdpsASTVisitor) {
            this.accept0((OdpsASTVisitor)visitor);
            return;
        }
        super.accept0(visitor);
    }
    
    public void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.hints);
            this.acceptChild(visitor, this.selectList);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.groupBy);
            this.acceptChild(visitor, this.orderBy);
            this.acceptChild(visitor, this.zOrderBy);
            this.acceptChild(visitor, this.clusterBy);
            this.acceptChild(visitor, this.distributeBy);
            this.acceptChild(visitor, this.sortBy);
            this.acceptChild(visitor, this.limit);
            this.acceptChild(visitor, this.into);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public String toString() {
        return SQLUtils.toOdpsString(this);
    }
    
    @Override
    public void limit(final int rowCount, final int offset) {
        if (offset > 0) {
            throw new UnsupportedOperationException("not support offset");
        }
        this.setLimit(new SQLLimit(new SQLIntegerExpr(rowCount)));
    }
    
    public SQLZOrderBy getZOrderBy() {
        return this.zOrderBy;
    }
    
    public void setZOrderBy(final SQLZOrderBy x) {
        if (x != null) {
            x.setParent(this);
        }
        this.zOrderBy = x;
    }
}
