// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.Set;

public interface WallConfigMBean
{
    boolean isInited();
    
    String getDir();
    
    void setDir(final String p0);
    
    void init();
    
    void loadConfig(final String p0);
    
    boolean isNoneBaseStatementAllow();
    
    void setNoneBaseStatementAllow(final boolean p0);
    
    boolean isTruncateAllow();
    
    void setTruncateAllow(final boolean p0);
    
    boolean isSelelctAllow();
    
    void setSelelctAllow(final boolean p0);
    
    boolean isSelectIntoAllow();
    
    void setSelectIntoAllow(final boolean p0);
    
    boolean isSelectIntoOutfileAllow();
    
    void setSelectIntoOutfileAllow(final boolean p0);
    
    boolean isSelectUnionCheck();
    
    void setSelectUnionCheck(final boolean p0);
    
    boolean isSelectWhereAlwayTrueCheck();
    
    void setSelectWhereAlwayTrueCheck(final boolean p0);
    
    boolean isSelectHavingAlwayTrueCheck();
    
    void setSelectHavingAlwayTrueCheck(final boolean p0);
    
    boolean isDeleteAllow();
    
    void setDeleteAllow(final boolean p0);
    
    boolean isDeleteWhereAlwayTrueCheck();
    
    void setDeleteWhereAlwayTrueCheck(final boolean p0);
    
    boolean isUpdateAllow();
    
    void setUpdateAllow(final boolean p0);
    
    boolean isUpdateWhereAlayTrueCheck();
    
    void setUpdateWhereAlayTrueCheck(final boolean p0);
    
    boolean isInsertAllow();
    
    void setInsertAllow(final boolean p0);
    
    boolean isMergeAllow();
    
    void setMergeAllow(final boolean p0);
    
    boolean isMultiStatementAllow();
    
    void setMultiStatementAllow(final boolean p0);
    
    boolean isSchemaCheck();
    
    void setSchemaCheck(final boolean p0);
    
    boolean isTableCheck();
    
    void setTableCheck(final boolean p0);
    
    boolean isFunctionCheck();
    
    void setFunctionCheck(final boolean p0);
    
    boolean isVariantCheck();
    
    void setVariantCheck(final boolean p0);
    
    boolean isObjectCheck();
    
    void setObjectCheck(final boolean p0);
    
    boolean isCommentAllow();
    
    void setCommentAllow(final boolean p0);
    
    Set<String> getDenyFunctions();
    
    Set<String> getDenyTables();
    
    Set<String> getDenySchemas();
    
    Set<String> getDenyVariants();
    
    Set<String> getDenyObjects();
    
    Set<String> getReadOnlyTables();
    
    boolean isDenyObjects(final String p0);
    
    boolean isDenySchema(final String p0);
    
    boolean isDenyFunction(final String p0);
}
