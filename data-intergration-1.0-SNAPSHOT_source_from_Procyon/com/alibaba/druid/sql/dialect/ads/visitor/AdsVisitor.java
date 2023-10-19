// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.ads.visitor;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface AdsVisitor extends SQLASTVisitor
{
    boolean visit(final MySqlPrimaryKey p0);
    
    void endVisit(final MySqlPrimaryKey p0);
    
    boolean visit(final MySqlCreateTableStatement p0);
    
    void endVisit(final MySqlCreateTableStatement p0);
}
