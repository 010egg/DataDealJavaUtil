// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLLockTableStatement;

public class MySqlLockTableStatement extends MySqlStatementImpl implements SQLLockTableStatement
{
    private List<Item> items;
    
    public MySqlLockTableStatement() {
        this.items = new ArrayList<Item>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
        }
        visitor.endVisit(this);
    }
    
    public List<Item> getItems() {
        return this.items;
    }
    
    public void setItems(final List<Item> items) {
        this.items = items;
    }
    
    public enum LockType
    {
        READ("READ"), 
        READ_LOCAL("READ LOCAL"), 
        WRITE("WRITE"), 
        LOW_PRIORITY_WRITE("LOW_PRIORITY WRITE");
        
        public final String name;
        
        private LockType(final String name) {
            this.name = name;
        }
    }
    
    public static class Item extends MySqlObjectImpl
    {
        private SQLExprTableSource tableSource;
        private LockType lockType;
        private List<SQLCommentHint> hints;
        
        public Item() {
            this.tableSource = new SQLExprTableSource();
        }
        
        @Override
        public void accept0(final MySqlASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.tableSource);
            }
            visitor.endVisit(this);
        }
        
        public SQLExprTableSource getTableSource() {
            return this.tableSource;
        }
        
        public void setTableSource(final SQLExprTableSource tableSource) {
            if (tableSource != null) {
                tableSource.setParent(this);
            }
            this.tableSource = tableSource;
        }
        
        public LockType getLockType() {
            return this.lockType;
        }
        
        public void setLockType(final LockType lockType) {
            this.lockType = lockType;
        }
        
        public List<SQLCommentHint> getHints() {
            return this.hints;
        }
        
        public void setHints(final List<SQLCommentHint> hints) {
            this.hints = hints;
        }
    }
}
