// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import com.alibaba.druid.wall.spi.SQLServerWallProvider;
import com.alibaba.druid.wall.spi.OracleWallProvider;
import com.alibaba.druid.wall.spi.MySqlWallProvider;
import com.alibaba.druid.wall.spi.PGWallProvider;
import com.alibaba.druid.wall.spi.DB2WallProvider;

public class WallUtils
{
    public static boolean isValidateDB2(final String sql) {
        final DB2WallProvider provider = new DB2WallProvider();
        return provider.checkValid(sql);
    }
    
    public static boolean isValidateDB2(final String sql, final WallConfig config) {
        final DB2WallProvider provider = new DB2WallProvider(config);
        return provider.checkValid(sql);
    }
    
    public static boolean isValidatePostgres(final String sql) {
        final PGWallProvider provider = new PGWallProvider();
        return provider.checkValid(sql);
    }
    
    public static boolean isValidatePostgres(final String sql, final WallConfig config) {
        final PGWallProvider provider = new PGWallProvider(config);
        return provider.checkValid(sql);
    }
    
    public static boolean isValidateMySql(final String sql) {
        final MySqlWallProvider provider = new MySqlWallProvider();
        return provider.checkValid(sql);
    }
    
    public static boolean isValidateMySql(final String sql, final WallConfig config) {
        final MySqlWallProvider provider = new MySqlWallProvider(config);
        return provider.checkValid(sql);
    }
    
    public static boolean isValidateOracle(final String sql) {
        final OracleWallProvider provider = new OracleWallProvider();
        return provider.checkValid(sql);
    }
    
    public static boolean isValidateOracle(final String sql, final WallConfig config) {
        final OracleWallProvider provider = new OracleWallProvider(config);
        return provider.checkValid(sql);
    }
    
    public static boolean isValidateSqlServer(final String sql) {
        final SQLServerWallProvider provider = new SQLServerWallProvider();
        return provider.checkValid(sql);
    }
    
    public static boolean isValidateSqlServer(final String sql, final WallConfig config) {
        final SQLServerWallProvider provider = new SQLServerWallProvider(config);
        return provider.checkValid(sql);
    }
}
