// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlAlterTableOption extends MySqlObjectImpl implements SQLAlterTableItem
{
    private String name;
    private SQLObject value;
    
    public MySqlAlterTableOption(final String name, final String value) {
        this(name, new SQLIdentifierExpr(value));
    }
    
    public MySqlAlterTableOption(final String name, final SQLObject value) {
        this.name = name;
        this.setValue(value);
    }
    
    public MySqlAlterTableOption() {
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public SQLObject getValue() {
        return this.value;
    }
    
    public void setValue(final SQLObject value) {
        this.value = value;
    }
}
