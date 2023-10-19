// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleFileSpecification extends OracleSQLObjectImpl
{
    private List<SQLExpr> fileNames;
    private SQLExpr size;
    private boolean autoExtendOff;
    private SQLExpr autoExtendOn;
    
    public OracleFileSpecification() {
        this.fileNames = new ArrayList<SQLExpr>();
        this.autoExtendOff = false;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.fileNames);
            this.acceptChild(visitor, this.size);
            this.acceptChild(visitor, this.autoExtendOn);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getAutoExtendOn() {
        return this.autoExtendOn;
    }
    
    public void setAutoExtendOn(final SQLExpr autoExtendOn) {
        this.autoExtendOn = autoExtendOn;
    }
    
    public SQLExpr getSize() {
        return this.size;
    }
    
    public void setSize(final SQLExpr size) {
        this.size = size;
    }
    
    public boolean isAutoExtendOff() {
        return this.autoExtendOff;
    }
    
    public void setAutoExtendOff(final boolean autoExtendOff) {
        this.autoExtendOff = autoExtendOff;
    }
    
    public List<SQLExpr> getFileNames() {
        return this.fileNames;
    }
    
    public void setFileNames(final List<SQLExpr> fileNames) {
        this.fileNames = fileNames;
    }
}
