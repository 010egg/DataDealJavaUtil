// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class MySqlCreateAddLogFileGroupStatement extends MySqlStatementImpl implements SQLAlterStatement
{
    private SQLName name;
    private SQLExpr addUndoFile;
    private SQLExpr initialSize;
    private SQLExpr undoBufferSize;
    private SQLExpr redoBufferSize;
    private SQLExpr nodeGroup;
    private boolean wait;
    private SQLExpr comment;
    private SQLExpr engine;
    
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
    
    public SQLExpr getUndoBufferSize() {
        return this.undoBufferSize;
    }
    
    public void setUndoBufferSize(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.undoBufferSize = x;
    }
    
    public SQLExpr getRedoBufferSize() {
        return this.redoBufferSize;
    }
    
    public void setRedoBufferSize(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.redoBufferSize = x;
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
    
    public SQLExpr getNodeGroup() {
        return this.nodeGroup;
    }
    
    public void setNodeGroup(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.nodeGroup = x;
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.comment = x;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.addUndoFile);
            this.acceptChild(visitor, this.initialSize);
            this.acceptChild(visitor, this.undoBufferSize);
            this.acceptChild(visitor, this.redoBufferSize);
            this.acceptChild(visitor, this.nodeGroup);
            this.acceptChild(visitor, this.comment);
            this.acceptChild(visitor, this.engine);
        }
        visitor.endVisit(this);
    }
}
