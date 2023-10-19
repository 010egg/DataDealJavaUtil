// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterTypeStatement extends SQLStatementImpl
{
    private SQLName name;
    private boolean compile;
    private boolean debug;
    private boolean body;
    private boolean reuseSettings;
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public boolean isCompile() {
        return this.compile;
    }
    
    public void setCompile(final boolean compile) {
        this.compile = compile;
    }
    
    public boolean isDebug() {
        return this.debug;
    }
    
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }
    
    public boolean isBody() {
        return this.body;
    }
    
    public void setBody(final boolean body) {
        this.body = body;
    }
    
    public boolean isReuseSettings() {
        return this.reuseSettings;
    }
    
    public void setReuseSettings(final boolean reuseSettings) {
        this.reuseSettings = reuseSettings;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.name);
    }
}
