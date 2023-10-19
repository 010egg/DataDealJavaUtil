// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.clause;

import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLLoopStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.ast.statement.SQLWhileStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.SQLStatement;

public enum MySqlStatementType
{
    SELECT(SQLSelectStatement.class.getName()), 
    UPDATE(MySqlUpdateStatement.class.getName()), 
    INSERT(MySqlInsertStatement.class.getName()), 
    DELETE(MySqlDeleteStatement.class.getName()), 
    WHILE(SQLWhileStatement.class.getName()), 
    IF(SQLIfStatement.class.getName()), 
    LOOP(SQLLoopStatement.class.getName()), 
    BLOCK(SQLBlockStatement.class.getName()), 
    DECLARE(MySqlDeclareStatement.class.getName()), 
    SELECTINTO(MySqlSelectIntoStatement.class.getName()), 
    CASE(MySqlCaseStatement.class.getName()), 
    UNDEFINED;
    
    public final String name;
    
    private MySqlStatementType() {
        this(null);
    }
    
    private MySqlStatementType(final String name) {
        this.name = name;
    }
    
    public static MySqlStatementType getType(final SQLStatement stmt) {
        for (final MySqlStatementType type : values()) {
            if (type.name == stmt.getClass().getName()) {
                return type;
            }
        }
        return MySqlStatementType.UNDEFINED;
    }
}
