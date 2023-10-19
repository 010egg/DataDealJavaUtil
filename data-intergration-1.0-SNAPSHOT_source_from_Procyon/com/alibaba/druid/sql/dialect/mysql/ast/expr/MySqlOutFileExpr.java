// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.expr;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlOutFileExpr extends MySqlObjectImpl implements SQLExpr
{
    private SQLExpr file;
    private String charset;
    private SQLExpr columnsTerminatedBy;
    private boolean columnsEnclosedOptionally;
    private SQLLiteralExpr columnsEnclosedBy;
    private SQLLiteralExpr columnsEscaped;
    private SQLLiteralExpr linesStartingBy;
    private SQLLiteralExpr linesTerminatedBy;
    private SQLExpr ignoreLinesNumber;
    
    public MySqlOutFileExpr() {
        this.columnsEnclosedOptionally = false;
    }
    
    public MySqlOutFileExpr(final SQLExpr file) {
        this.columnsEnclosedOptionally = false;
        this.file = file;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.file);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        return Collections.singletonList(this.file);
    }
    
    public SQLExpr getFile() {
        return this.file;
    }
    
    public void setFile(final SQLExpr file) {
        this.file = file;
    }
    
    public String getCharset() {
        return this.charset;
    }
    
    public void setCharset(final String charset) {
        this.charset = charset;
    }
    
    public SQLExpr getColumnsTerminatedBy() {
        return this.columnsTerminatedBy;
    }
    
    public void setColumnsTerminatedBy(final SQLExpr columnsTerminatedBy) {
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
    
    @Override
    public SQLExpr clone() {
        final MySqlOutFileExpr x = new MySqlOutFileExpr();
        if (this.file != null) {
            x.setFile(this.file.clone());
        }
        x.charset = this.charset;
        if (this.columnsTerminatedBy != null) {
            x.setColumnsTerminatedBy(this.columnsTerminatedBy.clone());
        }
        x.columnsEnclosedOptionally = this.columnsEnclosedOptionally;
        if (this.columnsEnclosedBy != null) {
            x.setColumnsEnclosedBy(this.columnsEnclosedBy.clone());
        }
        if (this.columnsEscaped != null) {
            x.setColumnsEscaped(this.columnsEscaped.clone());
        }
        if (this.linesStartingBy != null) {
            x.setLinesStartingBy(this.linesStartingBy.clone());
        }
        if (this.linesTerminatedBy != null) {
            x.setLinesTerminatedBy(this.linesTerminatedBy.clone());
        }
        if (this.ignoreLinesNumber != null) {
            x.setIgnoreLinesNumber(this.ignoreLinesNumber.clone());
        }
        return x;
    }
}
