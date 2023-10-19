// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2ASTVisitor;
import com.alibaba.druid.wall.WallVisitor;

public class DB2WallVisitor extends WallVisitorBase implements WallVisitor, DB2ASTVisitor
{
    public DB2WallVisitor(final WallProvider provider) {
        super(provider);
    }
    
    @Override
    public boolean isDenyTable(String name) {
        if (!this.config.isTableCheck()) {
            return false;
        }
        name = WallVisitorUtils.form(name);
        return name.startsWith("v$") || name.startsWith("v_$") || !this.provider.checkDenyTable(name);
    }
    
    @Override
    public DbType getDbType() {
        return DbType.db2;
    }
}
