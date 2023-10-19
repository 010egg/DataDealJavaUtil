// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSourceImpl;

public class SQLAdhocTableSource extends SQLTableSourceImpl
{
    private SQLCreateTableStatement definition;
    
    public SQLAdhocTableSource(final SQLCreateTableStatement definition) {
        this.setDefinition(definition);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this) && this.definition != null) {
            this.definition.accept(v);
        }
        v.endVisit(this);
    }
    
    public SQLCreateTableStatement getDefinition() {
        return this.definition;
    }
    
    public void setDefinition(final SQLCreateTableStatement x) {
        if (x != null) {
            x.setParent(this);
        }
        this.definition = x;
    }
}
