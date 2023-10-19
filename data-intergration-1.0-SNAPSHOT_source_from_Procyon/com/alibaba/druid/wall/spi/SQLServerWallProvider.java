// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.sql.dialect.sqlserver.visitor.MSSQLServerExportParameterVisitor;
import com.alibaba.druid.sql.visitor.ExportParameterVisitor;
import com.alibaba.druid.wall.WallVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallProvider;

public class SQLServerWallProvider extends WallProvider
{
    public static final String DEFAULT_CONFIG_DIR = "META-INF/druid/wall/sqlserver";
    
    public SQLServerWallProvider() {
        this(new WallConfig("META-INF/druid/wall/sqlserver"));
    }
    
    public SQLServerWallProvider(final WallConfig config) {
        super(config, DbType.sqlserver);
    }
    
    @Override
    public SQLStatementParser createParser(final String sql) {
        return new SQLServerStatementParser(sql, new SQLParserFeature[] { SQLParserFeature.EnableSQLBinaryOpExprGroup });
    }
    
    @Override
    public WallVisitor createWallVisitor() {
        return new SQLServerWallVisitor(this);
    }
    
    @Override
    public ExportParameterVisitor createExportParameterVisitor() {
        return new MSSQLServerExportParameterVisitor();
    }
}
