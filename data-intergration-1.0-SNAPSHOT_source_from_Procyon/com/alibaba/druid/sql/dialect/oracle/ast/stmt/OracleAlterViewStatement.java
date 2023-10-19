// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;

public class OracleAlterViewStatement extends OracleStatementImpl implements OracleAlterStatement
{
    private SQLName name;
    private Boolean enable;
    private boolean compile;
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    public Boolean getEnable() {
        return this.enable;
    }
    
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public boolean isCompile() {
        return this.compile;
    }
    
    public void setCompile(final boolean compile) {
        this.compile = compile;
    }
}
