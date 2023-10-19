// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.wall.WallVisitor;

public class SQLiteWallVisitor extends WallVisitorBase implements WallVisitor, MySqlASTVisitor
{
    public SQLiteWallVisitor(final WallProvider provider) {
        super(provider);
    }
    
    @Override
    public DbType getDbType() {
        return DbType.postgresql;
    }
    
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        return true;
    }
}
