// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.wall.WallVisitor;

public class PGWallVisitor extends WallVisitorBase implements WallVisitor, PGASTVisitor
{
    public PGWallVisitor(final WallProvider provider) {
        super(provider);
    }
    
    @Override
    public DbType getDbType() {
        return DbType.postgresql;
    }
    
    @Override
    public boolean isDenyTable(String name) {
        if (!this.config.isTableCheck()) {
            return false;
        }
        name = WallVisitorUtils.form(name);
        return name.startsWith("v$") || name.startsWith("v_$") || !this.provider.checkDenyTable(name);
    }
}
