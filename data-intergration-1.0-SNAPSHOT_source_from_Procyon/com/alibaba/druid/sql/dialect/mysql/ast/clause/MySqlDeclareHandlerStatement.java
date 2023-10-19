// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.clause;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;

public class MySqlDeclareHandlerStatement extends MySqlStatementImpl
{
    private MySqlHandlerType handleType;
    private SQLStatement spStatement;
    private List<ConditionValue> conditionValues;
    
    public MySqlDeclareHandlerStatement() {
        this.conditionValues = new ArrayList<ConditionValue>();
    }
    
    public List<ConditionValue> getConditionValues() {
        return this.conditionValues;
    }
    
    public void setConditionValues(final List<ConditionValue> conditionValues) {
        this.conditionValues = conditionValues;
    }
    
    public MySqlHandlerType getHandleType() {
        return this.handleType;
    }
    
    public void setHandleType(final MySqlHandlerType handleType) {
        this.handleType = handleType;
    }
    
    public SQLStatement getSpStatement() {
        return this.spStatement;
    }
    
    public void setSpStatement(final SQLStatement spStatement) {
        this.spStatement = spStatement;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.spStatement);
        }
        visitor.endVisit(this);
    }
}
