// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLDbTypedObject;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public abstract class SQLConstraintImpl extends SQLObjectImpl implements SQLConstraint, SQLDbTypedObject
{
    protected DbType dbType;
    private SQLName name;
    protected Boolean enable;
    protected Boolean validate;
    protected Boolean rely;
    private SQLExpr comment;
    public List<SQLCommentHint> hints;
    
    public void cloneTo(final SQLConstraintImpl x) {
        if (this.getName() != null) {
            x.setName(this.getName().clone());
        }
        x.enable = this.enable;
        x.validate = this.validate;
        x.rely = this.rely;
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    @Override
    public SQLName getName() {
        return this.name;
    }
    
    @Override
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public void setName(final String name) {
        this.setName(new SQLIdentifierExpr(name));
    }
    
    public Boolean getEnable() {
        return this.enable;
    }
    
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    public void cloneTo(final SQLConstraint x) {
        if (this.getName() != null) {
            x.setName(this.getName().clone());
        }
    }
    
    public Boolean getValidate() {
        return this.validate;
    }
    
    public void setValidate(final Boolean validate) {
        this.validate = validate;
    }
    
    public Boolean getRely() {
        return this.rely;
    }
    
    public void setRely(final Boolean rely) {
        this.rely = rely;
    }
    
    @Override
    public DbType getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    @Override
    public SQLExpr getComment() {
        return this.comment;
    }
    
    @Override
    public void setComment(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.comment = x;
    }
    
    @Override
    public void simplify() {
        if (this.getName() instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)this.getName();
            final String columnName = identExpr.getName();
            final String normalized = SQLUtils.normalize(columnName, this.dbType);
            if (columnName != normalized) {
                this.setName(normalized);
            }
        }
    }
    
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.getName() == expr) {
            this.setName((SQLName)target);
            return true;
        }
        if (this.getComment() == expr) {
            this.setComment(target);
            return true;
        }
        return false;
    }
}
