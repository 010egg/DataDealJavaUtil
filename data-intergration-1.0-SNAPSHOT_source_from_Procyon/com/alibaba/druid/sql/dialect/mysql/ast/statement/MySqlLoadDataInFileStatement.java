// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;

public class MySqlLoadDataInFileStatement extends MySqlStatementImpl
{
    private boolean lowPriority;
    private boolean concurrent;
    private boolean local;
    private SQLLiteralExpr fileName;
    private boolean replicate;
    private boolean ignore;
    private SQLName tableName;
    private String charset;
    private SQLLiteralExpr columnsTerminatedBy;
    private boolean columnsEnclosedOptionally;
    private SQLLiteralExpr columnsEnclosedBy;
    private SQLLiteralExpr columnsEscaped;
    private SQLLiteralExpr linesStartingBy;
    private SQLLiteralExpr linesTerminatedBy;
    private SQLExpr ignoreLinesNumber;
    private List<SQLExpr> setList;
    private List<SQLExpr> columns;
    
    public MySqlLoadDataInFileStatement() {
        this.lowPriority = false;
        this.concurrent = false;
        this.local = false;
        this.replicate = false;
        this.ignore = false;
        this.columnsEnclosedOptionally = false;
        this.setList = new ArrayList<SQLExpr>();
        this.columns = new ArrayList<SQLExpr>();
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
    
    public SQLLiteralExpr getColumnsTerminatedBy() {
        return this.columnsTerminatedBy;
    }
    
    public void setColumnsTerminatedBy(final SQLLiteralExpr columnsTerminatedBy) {
        this.columnsTerminatedBy = columnsTerminatedBy;
    }
    
    public boolean isColumnsEnclosedOptionally() {
        return this.columnsEnclosedOptionally;
    }
    
    public void setColumnsEnclosedOptionally(final boolean columnsEnclosedOptionally) {
        this.columnsEnclosedOptionally = columnsEnclosedOptionally;
    }
    
    public SQLLiteralExpr getColumnsEnclosedBy() {
        return this.columnsEnclosedBy;
    }
    
    public void setColumnsEnclosedBy(final SQLLiteralExpr columnsEnclosedBy) {
        this.columnsEnclosedBy = columnsEnclosedBy;
    }
    
    public SQLLiteralExpr getColumnsEscaped() {
        return this.columnsEscaped;
    }
    
    public void setColumnsEscaped(final SQLLiteralExpr columnsEscaped) {
        this.columnsEscaped = columnsEscaped;
    }
    
    public SQLLiteralExpr getLinesStartingBy() {
        return this.linesStartingBy;
    }
    
    public void setLinesStartingBy(final SQLLiteralExpr linesStartingBy) {
        this.linesStartingBy = linesStartingBy;
    }
    
    public SQLLiteralExpr getLinesTerminatedBy() {
        return this.linesTerminatedBy;
    }
    
    public void setLinesTerminatedBy(final SQLLiteralExpr linesTerminatedBy) {
        this.linesTerminatedBy = linesTerminatedBy;
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
            this.acceptChild(visitor, this.columnsTerminatedBy);
            this.acceptChild(visitor, this.columnsEnclosedBy);
            this.acceptChild(visitor, this.columnsEscaped);
            this.acceptChild(visitor, this.linesStartingBy);
            this.acceptChild(visitor, this.linesTerminatedBy);
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
        if (this.columnsTerminatedBy != null) {
            children.add(this.columnsTerminatedBy);
        }
        if (this.columnsEnclosedBy != null) {
            children.add(this.columnsEnclosedBy);
        }
        if (this.columnsEscaped != null) {
            children.add(this.columnsEscaped);
        }
        if (this.linesStartingBy != null) {
            children.add(this.linesStartingBy);
        }
        if (this.linesTerminatedBy != null) {
            children.add(this.linesTerminatedBy);
        }
        if (this.ignoreLinesNumber != null) {
            children.add(this.ignoreLinesNumber);
        }
        return children;
    }
    
    public List<SQLExpr> getColumns() {
        return this.columns;
    }
    
    public void setColumns(final List<SQLExpr> columns) {
        this.columns = columns;
    }
    
    public void setSetList(final List<SQLExpr> setList) {
        this.setList = setList;
    }
}
