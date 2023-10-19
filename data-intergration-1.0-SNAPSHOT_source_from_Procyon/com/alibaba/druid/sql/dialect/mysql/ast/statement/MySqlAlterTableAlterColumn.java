// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlAlterTableAlterColumn extends MySqlObjectImpl implements SQLAlterTableItem
{
    private SQLName column;
    private boolean dropDefault;
    private SQLExpr defaultExpr;
    
    public MySqlAlterTableAlterColumn() {
        this.dropDefault = false;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.column);
            this.acceptChild(visitor, this.defaultExpr);
        }
        visitor.endVisit(this);
    }
    
    public boolean isDropDefault() {
        return this.dropDefault;
    }
    
    public void setDropDefault(final boolean dropDefault) {
        this.dropDefault = dropDefault;
    }
    
    public SQLExpr getDefaultExpr() {
        return this.defaultExpr;
    }
    
    public void setDefaultExpr(final SQLExpr defaultExpr) {
        this.defaultExpr = defaultExpr;
    }
    
    public SQLName getColumn() {
        return this.column;
    }
    
    public void setColumn(final SQLName column) {
        this.column = column;
    }
}
