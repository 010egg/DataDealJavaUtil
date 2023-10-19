// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLExternalRecordFormat extends SQLObjectImpl
{
    private SQLExpr delimitedBy;
    private SQLExpr terminatedBy;
    private SQLExpr escapedBy;
    private SQLExpr collectionItemsTerminatedBy;
    private SQLExpr mapKeysTerminatedBy;
    private SQLExpr linesTerminatedBy;
    private SQLExpr nullDefinedAs;
    private SQLExpr serde;
    private Boolean logfile;
    private Boolean badfile;
    private boolean ltrim;
    private boolean missingFieldValuesAreNull;
    private boolean rejectRowsWithAllNullFields;
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.delimitedBy);
            this.acceptChild(visitor, this.terminatedBy);
            this.acceptChild(visitor, this.escapedBy);
            this.acceptChild(visitor, this.collectionItemsTerminatedBy);
            this.acceptChild(visitor, this.mapKeysTerminatedBy);
            this.acceptChild(visitor, this.linesTerminatedBy);
            this.acceptChild(visitor, this.nullDefinedAs);
            this.acceptChild(visitor, this.serde);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getDelimitedBy() {
        return this.delimitedBy;
    }
    
    public void setDelimitedBy(final SQLExpr delimitedBy) {
        if (delimitedBy != null) {
            delimitedBy.setParent(this);
        }
        this.delimitedBy = delimitedBy;
    }
    
    public SQLExpr getTerminatedBy() {
        return this.terminatedBy;
    }
    
    public void setTerminatedBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.terminatedBy = x;
    }
    
    public SQLExpr getSerde() {
        return this.serde;
    }
    
    public void setSerde(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.serde = x;
    }
    
    public SQLExpr getMapKeysTerminatedBy() {
        return this.mapKeysTerminatedBy;
    }
    
    public void setMapKeysTerminatedBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.mapKeysTerminatedBy = x;
    }
    
    public SQLExpr getCollectionItemsTerminatedBy() {
        return this.collectionItemsTerminatedBy;
    }
    
    public void setCollectionItemsTerminatedBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.collectionItemsTerminatedBy = x;
    }
    
    public SQLExpr getEscapedBy() {
        return this.escapedBy;
    }
    
    public void setEscapedBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.escapedBy = x;
    }
    
    public SQLExpr getLinesTerminatedBy() {
        return this.linesTerminatedBy;
    }
    
    public void setLinesTerminatedBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.linesTerminatedBy = x;
    }
    
    public SQLExpr getNullDefinedAs() {
        return this.nullDefinedAs;
    }
    
    public void setNullDefinedAs(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.nullDefinedAs = x;
    }
    
    public Boolean getLogfile() {
        return this.logfile;
    }
    
    public void setLogfile(final Boolean logfile) {
        this.logfile = logfile;
    }
    
    public Boolean getBadfile() {
        return this.badfile;
    }
    
    public void setBadfile(final Boolean badfile) {
        this.badfile = badfile;
    }
    
    public boolean isLtrim() {
        return this.ltrim;
    }
    
    public void setLtrim(final boolean ltrim) {
        this.ltrim = ltrim;
    }
    
    public boolean isRejectRowsWithAllNullFields() {
        return this.rejectRowsWithAllNullFields;
    }
    
    public void setRejectRowsWithAllNullFields(final boolean rejectRowsWithAllNullFields) {
        this.rejectRowsWithAllNullFields = rejectRowsWithAllNullFields;
    }
    
    public boolean isMissingFieldValuesAreNull() {
        return this.missingFieldValuesAreNull;
    }
    
    public void setMissingFieldValuesAreNull(final boolean missingFieldValuesAreNull) {
        this.missingFieldValuesAreNull = missingFieldValuesAreNull;
    }
    
    @Override
    public SQLExternalRecordFormat clone() {
        final SQLExternalRecordFormat x = new SQLExternalRecordFormat();
        if (this.delimitedBy != null) {
            x.setDelimitedBy(this.delimitedBy.clone());
        }
        if (this.escapedBy != null) {
            x.setEscapedBy(this.escapedBy.clone());
        }
        if (this.collectionItemsTerminatedBy != null) {
            x.setCollectionItemsTerminatedBy(this.collectionItemsTerminatedBy.clone());
        }
        if (this.mapKeysTerminatedBy != null) {
            x.setMapKeysTerminatedBy(this.mapKeysTerminatedBy.clone());
        }
        if (this.linesTerminatedBy != null) {
            x.setLinesTerminatedBy(this.linesTerminatedBy.clone());
        }
        if (this.nullDefinedAs != null) {
            x.setNullDefinedAs(this.nullDefinedAs.clone());
        }
        if (this.serde != null) {
            x.setSerde(this.serde.clone());
        }
        x.logfile = this.logfile;
        x.badfile = this.badfile;
        x.ltrim = this.ltrim;
        x.missingFieldValuesAreNull = this.missingFieldValuesAreNull;
        x.rejectRowsWithAllNullFields = this.rejectRowsWithAllNullFields;
        return x;
    }
}
