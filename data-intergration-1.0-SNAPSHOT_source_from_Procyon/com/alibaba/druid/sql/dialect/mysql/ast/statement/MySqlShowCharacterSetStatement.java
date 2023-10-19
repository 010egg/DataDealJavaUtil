// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.Arrays;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;

public class MySqlShowCharacterSetStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLExpr where;
    private SQLExpr pattern;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.pattern);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr where) {
        this.where = where;
    }
    
    public SQLExpr getPattern() {
        return this.pattern;
    }
    
    public void setPattern(final SQLExpr pattern) {
        this.pattern = pattern;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Arrays.asList(this.where, this.pattern);
    }
}
