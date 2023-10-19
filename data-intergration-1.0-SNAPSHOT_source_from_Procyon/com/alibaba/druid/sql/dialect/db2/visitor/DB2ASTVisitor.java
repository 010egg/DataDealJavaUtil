// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.visitor;

import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateTableStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2ValuesStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface DB2ASTVisitor extends SQLASTVisitor
{
    default boolean visit(final DB2SelectQueryBlock x) {
        return this.visit(x);
    }
    
    default void endVisit(final DB2SelectQueryBlock x) {
        this.endVisit(x);
    }
    
    default boolean visit(final DB2ValuesStatement x) {
        return true;
    }
    
    default void endVisit(final DB2ValuesStatement x) {
    }
    
    default boolean visit(final DB2CreateTableStatement x) {
        return this.visit(x);
    }
    
    default void endVisit(final DB2CreateTableStatement x) {
        this.endVisit(x);
    }
}
