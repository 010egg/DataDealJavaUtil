// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.visitor;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import java.util.List;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTParameterizedVisitor;

public class MySqlParameterizedVisitor extends SQLASTParameterizedVisitor implements MySqlASTVisitor
{
    public MySqlParameterizedVisitor() {
        super(DbType.mysql);
    }
    
    public MySqlParameterizedVisitor(final List<Object> outParameters) {
        super(DbType.mysql, outParameters);
    }
    
    @Override
    public boolean visit(final MySqlCharExpr x) {
        this.parameterizeAndExportPara(x);
        return false;
    }
}
