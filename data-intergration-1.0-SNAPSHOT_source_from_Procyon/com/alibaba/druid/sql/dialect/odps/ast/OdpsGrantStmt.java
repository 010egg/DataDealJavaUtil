// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLObjectType;
import com.alibaba.druid.sql.ast.statement.SQLGrantStatement;

public class OdpsGrantStmt extends SQLGrantStatement
{
    private SQLObjectType subjectType;
    private boolean isSuper;
    private boolean isLabel;
    private SQLExpr label;
    private List<SQLName> columns;
    private SQLExpr expire;
    
    public OdpsGrantStmt() {
        super(DbType.odps);
        this.isSuper = false;
        this.isLabel = false;
        this.columns = new ArrayList<SQLName>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OdpsASTVisitor)visitor);
    }
    
    protected void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.resource);
            this.acceptChild(visitor, this.users);
        }
        visitor.endVisit(this);
    }
    
    public SQLObjectType getSubjectType() {
        return this.subjectType;
    }
    
    public void setSubjectType(final SQLObjectType subjectType) {
        this.subjectType = subjectType;
    }
    
    public boolean isSuper() {
        return this.isSuper;
    }
    
    public void setSuper(final boolean isSuper) {
        this.isSuper = isSuper;
    }
    
    public boolean isLabel() {
        return this.isLabel;
    }
    
    public void setLabel(final boolean isLabel) {
        this.isLabel = isLabel;
    }
    
    public SQLExpr getLabel() {
        return this.label;
    }
    
    public void setLabel(final SQLExpr label) {
        this.label = label;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public void setColumnList(final List<SQLName> columns) {
        this.columns = columns;
    }
    
    public SQLExpr getExpire() {
        return this.expire;
    }
    
    public void setExpire(final SQLExpr expire) {
        this.expire = expire;
    }
}
