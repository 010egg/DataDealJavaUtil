// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributesImpl;

public class SQLSubPartition extends OracleSegmentAttributesImpl
{
    protected SQLName name;
    protected SQLPartitionValue values;
    protected SQLName tableSpace;
    protected SQLExpr dataDirectory;
    protected SQLExpr indexDirectory;
    protected SQLExpr maxRows;
    protected SQLExpr minRows;
    protected SQLExpr engine;
    protected SQLExpr comment;
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public SQLPartitionValue getValues() {
        return this.values;
    }
    
    public void setValues(final SQLPartitionValue values) {
        if (values != null) {
            values.setParent(this);
        }
        this.values = values;
    }
    
    public SQLName getTableSpace() {
        return this.tableSpace;
    }
    
    public void setTableSpace(final SQLName tableSpace) {
        if (tableSpace != null) {
            tableSpace.setParent(this);
        }
        this.tableSpace = tableSpace;
    }
    
    public SQLExpr getDataDirectory() {
        return this.dataDirectory;
    }
    
    public void setDataDirectory(final SQLExpr dataDirectory) {
        this.dataDirectory = dataDirectory;
    }
    
    public SQLExpr getIndexDirectory() {
        return this.indexDirectory;
    }
    
    public void setIndexDirectory(final SQLExpr indexDirectory) {
        this.indexDirectory = indexDirectory;
    }
    
    public SQLExpr getMaxRows() {
        return this.maxRows;
    }
    
    public void setMaxRows(final SQLExpr maxRows) {
        this.maxRows = maxRows;
    }
    
    public SQLExpr getMinRows() {
        return this.minRows;
    }
    
    public void setMinRows(final SQLExpr minRows) {
        this.minRows = minRows;
    }
    
    public SQLExpr getEngine() {
        return this.engine;
    }
    
    public void setEngine(final SQLExpr engine) {
        this.engine = engine;
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr comment) {
        this.comment = comment;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.tableSpace);
            this.acceptChild(visitor, this.values);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLSubPartition clone() {
        final SQLSubPartition x = new SQLSubPartition();
        if (this.name != null) {
            x.setName(this.name.clone());
        }
        if (this.values != null) {
            x.setValues(this.values.clone());
        }
        if (this.tableSpace != null) {
            x.setTableSpace(this.tableSpace.clone());
        }
        return x;
    }
}
