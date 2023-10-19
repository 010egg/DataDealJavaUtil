// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class OdpsAddFileStatement extends OdpsStatementImpl implements SQLAlterStatement
{
    private FileType type;
    private String file;
    private String alias;
    protected SQLExpr comment;
    protected boolean force;
    
    public OdpsAddFileStatement() {
        this.type = FileType.FILE;
        super.dbType = DbType.odps;
    }
    
    @Override
    protected void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {}
        visitor.endVisit(this);
    }
    
    public FileType getType() {
        return this.type;
    }
    
    public void setType(final FileType type) {
        this.type = type;
    }
    
    public String getFile() {
        return this.file;
    }
    
    public void setFile(final String file) {
        this.file = file;
    }
    
    public String getAlias() {
        return this.alias;
    }
    
    public void setAlias(final String alias) {
        this.alias = alias;
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.comment = x;
    }
    
    public boolean isForce() {
        return this.force;
    }
    
    public void setForce(final boolean force) {
        this.force = force;
    }
    
    public enum FileType
    {
        FILE, 
        ARCHIVE, 
        JAR, 
        PY;
    }
}
