// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLStartTransactionStatement extends SQLStatementImpl
{
    private boolean consistentSnapshot;
    private boolean begin;
    private boolean work;
    private SQLExpr name;
    private List<SQLCommentHint> hints;
    private IsolationLevel IsolationLevel;
    private boolean readOnly;
    
    public SQLStartTransactionStatement() {
        this.consistentSnapshot = false;
        this.begin = false;
        this.work = false;
    }
    
    public SQLStartTransactionStatement(final DbType dbType) {
        this.consistentSnapshot = false;
        this.begin = false;
        this.work = false;
        this.dbType = dbType;
    }
    
    public boolean isConsistentSnapshot() {
        return this.consistentSnapshot;
    }
    
    public void setConsistentSnapshot(final boolean consistentSnapshot) {
        this.consistentSnapshot = consistentSnapshot;
    }
    
    public boolean isBegin() {
        return this.begin;
    }
    
    public void setBegin(final boolean begin) {
        this.begin = begin;
    }
    
    public boolean isWork() {
        return this.work;
    }
    
    public void setWork(final boolean work) {
        this.work = work;
    }
    
    public IsolationLevel getIsolationLevel() {
        return this.IsolationLevel;
    }
    
    public void setIsolationLevel(final IsolationLevel isolationLevel) {
        this.IsolationLevel = isolationLevel;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    public SQLExpr getName() {
        return this.name;
    }
    
    public void setName(final SQLExpr name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        if (this.name != null) {
            return (List<SQLObject>)Collections.singletonList(this.name);
        }
        return Collections.emptyList();
    }
    
    public boolean isReadOnly() {
        return this.readOnly;
    }
    
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }
    
    public enum IsolationLevel
    {
        SERIALIZABLE("SERIALIZABLE"), 
        REPEATABLE_READ("REPEATABLE READ"), 
        READ_COMMITTED("READ COMMITTED"), 
        READ_UNCOMMITTED("READ UNCOMMITTED");
        
        private final String text;
        
        private IsolationLevel(final String text) {
            this.text = text;
        }
        
        public String getText() {
            return this.text;
        }
    }
}
