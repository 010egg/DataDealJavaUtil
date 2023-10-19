// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.sql.dialect.clickhouse.visitor.ClickhouseExportParameterVisitor;
import com.alibaba.druid.sql.visitor.ExportParameterVisitor;
import com.alibaba.druid.wall.WallVisitor;
import com.alibaba.druid.sql.dialect.clickhouse.parser.ClickhouseStatementParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallProvider;

public class ClickhouseWallProvider extends WallProvider
{
    public static final String DEFAULT_CONFIG_DIR = "META-INF/druid/wall/clickhouse";
    
    public ClickhouseWallProvider() {
        this(new WallConfig("META-INF/druid/wall/clickhouse"));
    }
    
    public ClickhouseWallProvider(final WallConfig config) {
        super(config, DbType.postgresql);
    }
    
    @Override
    public SQLStatementParser createParser(final String sql) {
        return new ClickhouseStatementParser(sql, new SQLParserFeature[] { SQLParserFeature.EnableSQLBinaryOpExprGroup });
    }
    
    @Override
    public WallVisitor createWallVisitor() {
        return new ClickhouseWallVisitor(this);
    }
    
    @Override
    public ExportParameterVisitor createExportParameterVisitor() {
        return new ClickhouseExportParameterVisitor();
    }
}
