// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast.stmt;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerObjectImpl;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerStatementImpl;

public class SQLServerExecStatement extends SQLServerStatementImpl implements SQLServerStatement
{
    private SQLName returnStatus;
    private SQLName moduleName;
    private List<SQLServerParameter> parameters;
    
    public SQLServerExecStatement() {
        this.parameters = new ArrayList<SQLServerParameter>();
    }
    
    public SQLName getModuleName() {
        return this.moduleName;
    }
    
    public void setModuleName(final SQLName moduleName) {
        this.moduleName = moduleName;
    }
    
    public List<SQLServerParameter> getParameters() {
        return this.parameters;
    }
    
    @Override
    public void accept0(final SQLServerASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.returnStatus);
            this.acceptChild(visitor, this.moduleName);
            this.acceptChild(visitor, this.parameters);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getReturnStatus() {
        return this.returnStatus;
    }
    
    public void setReturnStatus(final SQLName returnStatus) {
        this.returnStatus = returnStatus;
    }
    
    public static class SQLServerParameter extends SQLServerObjectImpl
    {
        private SQLExpr expr;
        private boolean type;
        
        public SQLExpr getExpr() {
            return this.expr;
        }
        
        public void setExpr(final SQLExpr expr) {
            this.expr = expr;
        }
        
        public boolean getType() {
            return this.type;
        }
        
        public void setType(final boolean type) {
            this.type = type;
        }
        
        @Override
        public void accept0(final SQLServerASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.expr);
            }
            visitor.endVisit(this);
        }
    }
}
