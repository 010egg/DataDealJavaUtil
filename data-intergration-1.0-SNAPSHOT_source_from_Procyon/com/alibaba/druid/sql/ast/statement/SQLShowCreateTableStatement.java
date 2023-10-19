// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowCreateTableStatement extends SQLStatementImpl implements SQLShowStatement, SQLReplaceable
{
    private SQLName name;
    private boolean all;
    private SQLName likeMapping;
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.likeMapping);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public SQLName getLikeMapping() {
        return this.likeMapping;
    }
    
    public void setLikeMapping(final SQLName likeMapping) {
        this.likeMapping = likeMapping;
    }
    
    public boolean isAll() {
        return this.all;
    }
    
    public void setAll(final boolean all) {
        this.all = all;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.name == expr) {
            this.setName((SQLName)target);
            return true;
        }
        if (this.likeMapping == expr) {
            this.setLikeMapping((SQLName)target);
            return true;
        }
        return false;
    }
}
