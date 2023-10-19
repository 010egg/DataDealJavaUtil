// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlExportParameterVisitor;
import com.alibaba.druid.sql.visitor.ExportParameterVisitor;
import com.alibaba.druid.wall.WallVisitor;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallProvider;

public class SQLiteWallProvider extends WallProvider
{
    public static final String DEFAULT_CONFIG_DIR = "META-INF/druid/wall/sqlite";
    
    public SQLiteWallProvider() {
        this(new WallConfig("META-INF/druid/wall/sqlite"));
    }
    
    public SQLiteWallProvider(final WallConfig config) {
        super(config, DbType.sqlite);
    }
    
    @Override
    public SQLStatementParser createParser(final String sql) {
        return new MySqlStatementParser(sql, new SQLParserFeature[] { SQLParserFeature.EnableSQLBinaryOpExprGroup });
    }
    
    @Override
    public WallVisitor createWallVisitor() {
        return new SQLiteWallVisitor(this);
    }
    
    @Override
    public ExportParameterVisitor createExportParameterVisitor() {
        return new MySqlExportParameterVisitor();
    }
}
