// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.sql.dialect.postgresql.visitor.PGExportParameterVisitor;
import com.alibaba.druid.sql.visitor.ExportParameterVisitor;
import com.alibaba.druid.wall.WallVisitor;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallProvider;

public class PGWallProvider extends WallProvider
{
    public static final String DEFAULT_CONFIG_DIR = "META-INF/druid/wall/postgres";
    
    public PGWallProvider() {
        this(new WallConfig("META-INF/druid/wall/postgres"));
    }
    
    public PGWallProvider(final WallConfig config) {
        super(config, DbType.postgresql);
    }
    
    @Override
    public SQLStatementParser createParser(final String sql) {
        return new PGSQLStatementParser(sql, new SQLParserFeature[] { SQLParserFeature.EnableSQLBinaryOpExprGroup });
    }
    
    @Override
    public WallVisitor createWallVisitor() {
        return new PGWallVisitor(this);
    }
    
    @Override
    public ExportParameterVisitor createExportParameterVisitor() {
        return new PGExportParameterVisitor();
    }
}
