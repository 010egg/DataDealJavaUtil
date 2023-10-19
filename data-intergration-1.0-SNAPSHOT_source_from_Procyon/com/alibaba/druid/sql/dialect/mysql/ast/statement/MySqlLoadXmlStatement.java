// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;

public class MySqlLoadXmlStatement extends MySqlStatementImpl
{
    private boolean lowPriority;
    private boolean concurrent;
    private boolean local;
    private SQLLiteralExpr fileName;
    private boolean replicate;
    private boolean ignore;
    private SQLName tableName;
    private String charset;
    private SQLExpr rowsIdentifiedBy;
    private SQLExpr ignoreLinesNumber;
    private final List<SQLExpr> setList;
    
    public MySqlLoadXmlStatement() {
        this.lowPriority = false;
        this.concurrent = false;
        this.local = false;
        this.replicate = false;
        this.ignore = false;
        this.setList = new ArrayList<SQLExpr>();
    }
    
    public SQLExpr getRowsIdentifiedBy() {
        return this.rowsIdentifiedBy;
    }
    
    public void setRowsIdentifiedBy(final SQLExpr rowsIdentifiedBy) {
        this.rowsIdentifiedBy = rowsIdentifiedBy;
    }
    
    public boolean isLowPriority() {
        return this.lowPriority;
    }
    
    public void setLowPriority(final boolean lowPriority) {
        this.lowPriority = lowPriority;
    }
    
    public boolean isConcurrent() {
        return this.concurrent;
    }
    
    public void setConcurrent(final boolean concurrent) {
        this.concurrent = concurrent;
    }
    
    public boolean isLocal() {
        return this.local;
    }
    
    public void setLocal(final boolean local) {
        this.local = local;
    }
    
    public SQLLiteralExpr getFileName() {
        return this.fileName;
    }
    
    public void setFileName(final SQLLiteralExpr fileName) {
        this.fileName = fileName;
    }
    
    public boolean isReplicate() {
        return this.replicate;
    }
    
    public void setReplicate(final boolean replicate) {
        this.replicate = replicate;
    }
    
    public boolean isIgnore() {
        return this.ignore;
    }
    
    public void setIgnore(final boolean ignore) {
        this.ignore = ignore;
    }
    
    public SQLName getTableName() {
        return this.tableName;
    }
    
    public void setTableName(final SQLName tableName) {
        this.tableName = tableName;
    }
    
    public String getCharset() {
        return this.charset;
    }
    
    public void setCharset(final String charset) {
        this.charset = charset;
    }
    
    public SQLExpr getIgnoreLinesNumber() {
        return this.ignoreLinesNumber;
    }
    
    public void setIgnoreLinesNumber(final SQLExpr ignoreLinesNumber) {
        this.ignoreLinesNumber = ignoreLinesNumber;
    }
    
    public List<SQLExpr> getSetList() {
        return this.setList;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.fileName);
            this.acceptChild(visitor, this.tableName);
            this.acceptChild(visitor, this.rowsIdentifiedBy);
            this.acceptChild(visitor, this.ignoreLinesNumber);
            this.acceptChild(visitor, this.setList);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.fileName != null) {
            children.add(this.fileName);
        }
        if (this.tableName != null) {
            children.add(this.tableName);
        }
        if (this.rowsIdentifiedBy != null) {
            children.add(this.rowsIdentifiedBy);
        }
        if (this.ignoreLinesNumber != null) {
            children.add(this.ignoreLinesNumber);
        }
        children.addAll(this.setList);
        return children;
    }
}
