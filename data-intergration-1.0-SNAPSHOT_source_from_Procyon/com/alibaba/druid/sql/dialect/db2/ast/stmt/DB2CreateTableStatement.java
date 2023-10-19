// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.ast.stmt;

import java.util.List;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2ASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.db2.ast.DB2Statement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;

public class DB2CreateTableStatement extends SQLCreateTableStatement implements DB2Statement
{
    private boolean dataCaptureNone;
    private boolean dataCaptureChanges;
    protected SQLName database;
    protected SQLName validproc;
    protected SQLName indexIn;
    
    public boolean isDataCaptureNone() {
        return this.dataCaptureNone;
    }
    
    public void setDataCaptureNone(final boolean dataCaptureNone) {
        this.dataCaptureNone = dataCaptureNone;
    }
    
    public boolean isDataCaptureChanges() {
        return this.dataCaptureChanges;
    }
    
    public void setDataCaptureChanges(final boolean dataCaptureChanges) {
        this.dataCaptureChanges = dataCaptureChanges;
    }
    
    public SQLName getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final SQLName database) {
        if (database != null) {
            database.setParent(this);
        }
        this.database = database;
    }
    
    public SQLName getValidproc() {
        return this.validproc;
    }
    
    public void setValidproc(final SQLName x) {
        if (this.validproc != null) {
            x.setParent(this);
        }
        this.validproc = x;
    }
    
    public SQLName getIndexIn() {
        return this.indexIn;
    }
    
    public void setIndexIn(final SQLName x) {
        if (this.validproc != null) {
            x.setParent(this);
        }
        this.indexIn = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof DB2ASTVisitor) {
            this.accept0((DB2ASTVisitor)visitor);
            return;
        }
        super.accept0(visitor);
    }
    
    @Override
    public void accept0(final DB2ASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.tableElementList);
            this.acceptChild(visitor, this.inherits);
            this.acceptChild(visitor, this.select);
            this.acceptChild(visitor, this.database);
            this.acceptChild(visitor, this.validproc);
            this.acceptChild(visitor, this.indexIn);
        }
        visitor.endVisit(this);
    }
}
