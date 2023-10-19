// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.HashMap;
import java.util.List;
import com.alibaba.druid.DbType;
import java.util.Map;

public class WallContext
{
    private static final ThreadLocal<WallContext> contextLocal;
    private WallSqlStat sqlStat;
    private Map<String, WallSqlTableStat> tableStats;
    private Map<String, WallSqlFunctionStat> functionStats;
    private final DbType dbType;
    private int commentCount;
    private int warnings;
    private int unionWarnings;
    private int updateNoneConditionWarnings;
    private int deleteNoneConditionWarnings;
    private int likeNumberWarnings;
    private List<WallUpdateCheckItem> wallUpdateCheckItems;
    
    public WallContext(final String dbType) {
        this(DbType.of(dbType));
    }
    
    public WallContext(final DbType dbType) {
        this.warnings = 0;
        this.unionWarnings = 0;
        this.updateNoneConditionWarnings = 0;
        this.deleteNoneConditionWarnings = 0;
        this.likeNumberWarnings = 0;
        this.dbType = dbType;
    }
    
    public void incrementFunctionInvoke(final String tableName) {
        if (this.functionStats == null) {
            this.functionStats = new HashMap<String, WallSqlFunctionStat>();
        }
        final String lowerCaseName = tableName.toLowerCase();
        WallSqlFunctionStat stat = this.functionStats.get(lowerCaseName);
        if (stat == null) {
            if (this.functionStats.size() > 100) {
                return;
            }
            stat = new WallSqlFunctionStat();
            this.functionStats.put(tableName, stat);
        }
        stat.incrementInvokeCount();
    }
    
    public WallSqlTableStat getTableStat(final String tableName) {
        if (this.tableStats == null) {
            this.tableStats = new HashMap<String, WallSqlTableStat>(2);
        }
        final String lowerCaseName = tableName.toLowerCase();
        WallSqlTableStat stat = this.tableStats.get(lowerCaseName);
        if (stat == null) {
            if (this.tableStats.size() > 100) {
                return null;
            }
            stat = new WallSqlTableStat();
            this.tableStats.put(tableName, stat);
        }
        return stat;
    }
    
    public static WallContext createIfNotExists(final DbType dbType) {
        WallContext context = WallContext.contextLocal.get();
        if (context == null) {
            context = new WallContext(dbType);
            WallContext.contextLocal.set(context);
        }
        return context;
    }
    
    public static WallContext create(final String dbType) {
        return create(DbType.of(dbType));
    }
    
    public static WallContext create(final DbType dbType) {
        final WallContext context = new WallContext(dbType);
        WallContext.contextLocal.set(context);
        return context;
    }
    
    public static WallContext current() {
        return WallContext.contextLocal.get();
    }
    
    public static void clearContext() {
        WallContext.contextLocal.remove();
    }
    
    public static void setContext(final WallContext context) {
        WallContext.contextLocal.set(context);
    }
    
    public WallSqlStat getSqlStat() {
        return this.sqlStat;
    }
    
    public void setSqlStat(final WallSqlStat sqlStat) {
        this.sqlStat = sqlStat;
    }
    
    public Map<String, WallSqlTableStat> getTableStats() {
        return this.tableStats;
    }
    
    public Map<String, WallSqlFunctionStat> getFunctionStats() {
        return this.functionStats;
    }
    
    public DbType getDbType() {
        return this.dbType;
    }
    
    public int getCommentCount() {
        return this.commentCount;
    }
    
    public void incrementCommentCount() {
        if (this.commentCount == 0) {
            ++this.warnings;
        }
        ++this.commentCount;
    }
    
    public int getWarnings() {
        return this.warnings;
    }
    
    public void incrementWarnings() {
        ++this.warnings;
    }
    
    public int getLikeNumberWarnings() {
        return this.likeNumberWarnings;
    }
    
    public void incrementLikeNumberWarnings() {
        if (this.likeNumberWarnings == 0) {
            ++this.warnings;
        }
        ++this.likeNumberWarnings;
    }
    
    public int getUnionWarnings() {
        return this.unionWarnings;
    }
    
    public void incrementUnionWarnings() {
        if (this.unionWarnings == 0) {
            this.incrementWarnings();
        }
        ++this.unionWarnings;
    }
    
    public int getUpdateNoneConditionWarnings() {
        return this.updateNoneConditionWarnings;
    }
    
    public void incrementUpdateNoneConditionWarnings() {
        ++this.updateNoneConditionWarnings;
    }
    
    public int getDeleteNoneConditionWarnings() {
        return this.deleteNoneConditionWarnings;
    }
    
    public void incrementDeleteNoneConditionWarnings() {
        ++this.deleteNoneConditionWarnings;
    }
    
    public List<WallUpdateCheckItem> getWallUpdateCheckItems() {
        return this.wallUpdateCheckItems;
    }
    
    public void setWallUpdateCheckItems(final List<WallUpdateCheckItem> wallUpdateCheckItems) {
        this.wallUpdateCheckItems = wallUpdateCheckItems;
    }
    
    static {
        contextLocal = new ThreadLocal<WallContext>();
    }
}
