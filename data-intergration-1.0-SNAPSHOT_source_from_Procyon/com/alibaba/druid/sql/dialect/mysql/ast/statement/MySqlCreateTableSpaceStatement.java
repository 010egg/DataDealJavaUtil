// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;

public class MySqlCreateTableSpaceStatement extends MySqlStatementImpl implements SQLCreateStatement
{
    private SQLName name;
    private SQLExpr addDataFile;
    private SQLExpr initialSize;
    private SQLExpr extentSize;
    private SQLExpr autoExtentSize;
    private SQLExpr fileBlockSize;
    private SQLExpr logFileGroup;
    private SQLExpr maxSize;
    private SQLExpr nodeGroup;
    private boolean wait;
    private SQLExpr comment;
    private SQLExpr engine;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.addDataFile);
            this.acceptChild(visitor, this.initialSize);
            this.acceptChild(visitor, this.extentSize);
            this.acceptChild(visitor, this.autoExtentSize);
            this.acceptChild(visitor, this.fileBlockSize);
            this.acceptChild(visitor, this.logFileGroup);
            this.acceptChild(visitor, this.maxSize);
            this.acceptChild(visitor, this.nodeGroup);
            this.acceptChild(visitor, this.comment);
            this.acceptChild(visitor, this.engine);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
    }
    
    public SQLExpr getAddDataFile() {
        return this.addDataFile;
    }
    
    public void setAddDataFile(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.addDataFile = this.addDataFile;
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
    
    public SQLExpr getFileBlockSize() {
        return this.fileBlockSize;
    }
    
    public void setFileBlockSize(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.fileBlockSize = x;
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
    
    public void setEngine(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.engine = this.engine;
    }
    
    public SQLExpr getLogFileGroup() {
        return this.logFileGroup;
    }
    
    public void setLogFileGroup(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.logFileGroup = x;
    }
    
    public SQLExpr getExtentSize() {
        return this.extentSize;
    }
    
    public void setExtentSize(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.extentSize = x;
    }
    
    public SQLExpr getAutoExtentSize() {
        return this.autoExtentSize;
    }
    
    public void setAutoExtentSize(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.autoExtentSize = x;
    }
    
    public SQLExpr getMaxSize() {
        return this.maxSize;
    }
    
    public void setMaxSize(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.maxSize = x;
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
}
