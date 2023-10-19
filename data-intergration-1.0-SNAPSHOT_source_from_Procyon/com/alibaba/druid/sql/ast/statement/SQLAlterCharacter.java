// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterCharacter extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLExpr characterSet;
    private SQLExpr collate;
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.characterSet);
            this.acceptChild(visitor, this.collate);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getCharacterSet() {
        return this.characterSet;
    }
    
    public void setCharacterSet(final SQLExpr characterSet) {
        this.characterSet = characterSet;
    }
    
    public SQLExpr getCollate() {
        return this.collate;
    }
    
    public void setCollate(final SQLExpr collate) {
        this.collate = collate;
    }
}
