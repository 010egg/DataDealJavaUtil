// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;

public class OracleCreateSynonymStatement extends OracleStatementImpl implements SQLCreateStatement
{
    private boolean orReplace;
    private SQLName name;
    private boolean isPublic;
    private SQLName object;
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.object);
        }
    }
    
    public boolean isPublic() {
        return this.isPublic;
    }
    
    public void setPublic(final boolean value) {
        this.isPublic = value;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public SQLName getObject() {
        return this.object;
    }
    
    public void setObject(final SQLName object) {
        if (object != null) {
            object.setParent(this);
        }
        this.object = object;
    }
    
    public boolean isOrReplace() {
        return this.orReplace;
    }
    
    public void setOrReplace(final boolean orReplace) {
        this.orReplace = orReplace;
    }
}
