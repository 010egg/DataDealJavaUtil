// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class MySqlAlterLogFileGroupStatement extends MySqlStatementImpl implements SQLAlterStatement
{
    private SQLName name;
    private SQLExpr addUndoFile;
    private SQLExpr initialSize;
    private boolean wait;
    private SQLExpr engine;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.addUndoFile);
            this.acceptChild(visitor, this.initialSize);
            this.acceptChild(visitor, this.engine);
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
    
    public SQLExpr getAddUndoFile() {
        return this.addUndoFile;
    }
    
    public void setAddUndoFile(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.addUndoFile = x;
    }
    
    public SQLExpr getInitialSize() {
        return this.initialSize;
    }
    
    public void setInitialSize(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.initialSize = x;
    }
    
    public boolean isWait() {
        return this.wait;
    }
    
    public void setWait(final boolean wait) {
        this.wait = wait;
    }
    
    public SQLExpr getEngine() {
        return this.engine;
    }
    
    public void setEngine(final SQLExpr engine) {
        if (engine != null) {
            engine.setParent(this);
        }
        this.engine = engine;
    }
}
