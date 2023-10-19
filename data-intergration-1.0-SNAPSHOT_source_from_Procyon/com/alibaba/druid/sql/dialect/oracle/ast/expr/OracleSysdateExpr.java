// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.SQLUtils;
import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleSysdateExpr extends OracleSQLObjectImpl implements SQLExpr
{
    private String option;
    
    public String getOption() {
        return this.option;
    }
    
    public void setOption(final String option) {
        this.option = option;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public OracleSysdateExpr clone() {
        final OracleSysdateExpr x = new OracleSysdateExpr();
        x.option = this.option;
        return x;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public String toString() {
        return SQLUtils.toOracleString(this);
    }
}
