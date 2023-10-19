// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.sql.dialect.db2.visitor.DB2ExportParameterVisitor;
import com.alibaba.druid.sql.visitor.ExportParameterVisitor;
import com.alibaba.druid.wall.WallVisitor;
import com.alibaba.druid.sql.dialect.db2.parser.DB2StatementParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallProvider;

public class DB2WallProvider extends WallProvider
{
    public static final String DEFAULT_CONFIG_DIR = "META-INF/druid/wall/db2";
    
    public DB2WallProvider() {
        this(new WallConfig("META-INF/druid/wall/db2"));
    }
    
    public DB2WallProvider(final WallConfig config) {
        super(config, DbType.db2);
    }
    
    @Override
    public SQLStatementParser createParser(final String sql) {
        return new DB2StatementParser(sql, new SQLParserFeature[] { SQLParserFeature.EnableSQLBinaryOpExprGroup });
    }
    
    @Override
    public WallVisitor createWallVisitor() {
        return new DB2WallVisitor(this);
    }
    
    @Override
    public ExportParameterVisitor createExportParameterVisitor() {
        return new DB2ExportParameterVisitor();
    }
}
