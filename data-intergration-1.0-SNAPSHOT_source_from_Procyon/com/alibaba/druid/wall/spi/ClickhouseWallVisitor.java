// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.wall.WallVisitor;

public class ClickhouseWallVisitor extends WallVisitorBase implements WallVisitor, PGASTVisitor
{
    public ClickhouseWallVisitor(final WallProvider provider) {
        super(provider);
    }
    
    @Override
    public DbType getDbType() {
        return DbType.clickhouse;
    }
}
