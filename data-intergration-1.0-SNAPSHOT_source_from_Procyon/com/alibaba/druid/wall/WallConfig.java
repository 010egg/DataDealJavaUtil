// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.LinkedHashSet;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.Utils;
import java.util.Properties;
import com.alibaba.druid.wall.spi.WallVisitorUtils;
import java.util.HashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.Map;
import java.util.Set;

public class WallConfig implements WallConfigMBean
{
    private boolean noneBaseStatementAllow;
    private boolean callAllow;
    private boolean selelctAllow;
    private boolean selectIntoAllow;
    private boolean selectIntoOutfileAllow;
    private boolean selectWhereAlwayTrueCheck;
    private boolean selectHavingAlwayTrueCheck;
    private boolean selectUnionCheck;
    private boolean selectMinusCheck;
    private boolean selectExceptCheck;
    private boolean selectIntersectCheck;
    private boolean createTableAllow;
    private boolean dropTableAllow;
    private boolean alterTableAllow;
    private boolean renameTableAllow;
    private boolean hintAllow;
    private boolean lockTableAllow;
    private boolean startTransactionAllow;
    private boolean blockAllow;
    private boolean conditionAndAlwayTrueAllow;
    private boolean conditionAndAlwayFalseAllow;
    private boolean conditionDoubleConstAllow;
    private boolean conditionLikeTrueAllow;
    private boolean selectAllColumnAllow;
    private boolean deleteAllow;
    private boolean deleteWhereAlwayTrueCheck;
    private boolean deleteWhereNoneCheck;
    private boolean updateAllow;
    private boolean updateWhereAlayTrueCheck;
    private boolean updateWhereNoneCheck;
    private boolean insertAllow;
    private boolean mergeAllow;
    private boolean minusAllow;
    private boolean intersectAllow;
    private boolean replaceAllow;
    private boolean setAllow;
    private boolean commitAllow;
    private boolean rollbackAllow;
    private boolean useAllow;
    private boolean multiStatementAllow;
    private boolean truncateAllow;
    private boolean commentAllow;
    private boolean strictSyntaxCheck;
    private boolean constArithmeticAllow;
    private boolean limitZeroAllow;
    private boolean describeAllow;
    private boolean showAllow;
    private boolean schemaCheck;
    private boolean tableCheck;
    private boolean functionCheck;
    private boolean objectCheck;
    private boolean variantCheck;
    private boolean mustParameterized;
    private boolean doPrivilegedAllow;
    protected final Set<String> denyFunctions;
    protected final Set<String> denyTables;
    protected final Set<String> denySchemas;
    protected final Set<String> denyVariants;
    protected final Set<String> denyObjects;
    protected final Set<String> permitFunctions;
    protected final Set<String> permitTables;
    protected final Set<String> permitSchemas;
    protected final Set<String> permitVariants;
    protected final Set<String> readOnlyTables;
    private String dir;
    private boolean inited;
    private String tenantTablePattern;
    private String tenantColumn;
    private TenantCallBack tenantCallBack;
    private boolean wrapAllow;
    private boolean metadataAllow;
    private boolean conditionOpXorAllow;
    private boolean conditionOpBitwseAllow;
    private boolean caseConditionConstAllow;
    private boolean completeInsertValuesCheck;
    private int insertValuesCheckSize;
    private int selectLimit;
    protected Map<String, Set<String>> updateCheckColumns;
    protected WallUpdateCheckHandler updateCheckHandler;
    
    public WallConfig() {
        this.noneBaseStatementAllow = false;
        this.callAllow = true;
        this.selelctAllow = true;
        this.selectIntoAllow = true;
        this.selectIntoOutfileAllow = false;
        this.selectWhereAlwayTrueCheck = true;
        this.selectHavingAlwayTrueCheck = true;
        this.selectUnionCheck = true;
        this.selectMinusCheck = true;
        this.selectExceptCheck = true;
        this.selectIntersectCheck = true;
        this.createTableAllow = true;
        this.dropTableAllow = true;
        this.alterTableAllow = true;
        this.renameTableAllow = true;
        this.hintAllow = true;
        this.lockTableAllow = true;
        this.startTransactionAllow = true;
        this.blockAllow = true;
        this.conditionAndAlwayTrueAllow = false;
        this.conditionAndAlwayFalseAllow = false;
        this.conditionDoubleConstAllow = false;
        this.conditionLikeTrueAllow = true;
        this.selectAllColumnAllow = true;
        this.deleteAllow = true;
        this.deleteWhereAlwayTrueCheck = true;
        this.deleteWhereNoneCheck = false;
        this.updateAllow = true;
        this.updateWhereAlayTrueCheck = true;
        this.updateWhereNoneCheck = false;
        this.insertAllow = true;
        this.mergeAllow = true;
        this.minusAllow = true;
        this.intersectAllow = true;
        this.replaceAllow = true;
        this.setAllow = true;
        this.commitAllow = true;
        this.rollbackAllow = true;
        this.useAllow = true;
        this.multiStatementAllow = false;
        this.truncateAllow = true;
        this.commentAllow = false;
        this.strictSyntaxCheck = true;
        this.constArithmeticAllow = true;
        this.limitZeroAllow = false;
        this.describeAllow = true;
        this.showAllow = true;
        this.schemaCheck = true;
        this.tableCheck = true;
        this.functionCheck = true;
        this.objectCheck = true;
        this.variantCheck = true;
        this.mustParameterized = false;
        this.doPrivilegedAllow = false;
        this.denyFunctions = new ConcurrentSkipListSet<String>();
        this.denyTables = new ConcurrentSkipListSet<String>();
        this.denySchemas = new ConcurrentSkipListSet<String>();
        this.denyVariants = new ConcurrentSkipListSet<String>();
        this.denyObjects = new ConcurrentSkipListSet<String>();
        this.permitFunctions = new ConcurrentSkipListSet<String>();
        this.permitTables = new ConcurrentSkipListSet<String>();
        this.permitSchemas = new ConcurrentSkipListSet<String>();
        this.permitVariants = new ConcurrentSkipListSet<String>();
        this.readOnlyTables = new ConcurrentSkipListSet<String>();
        this.wrapAllow = true;
        this.metadataAllow = true;
        this.conditionOpXorAllow = false;
        this.conditionOpBitwseAllow = true;
        this.caseConditionConstAllow = false;
        this.completeInsertValuesCheck = false;
        this.insertValuesCheckSize = 3;
        this.selectLimit = -1;
        this.updateCheckColumns = new HashMap<String, Set<String>>();
        this.configFromProperties(System.getProperties());
    }
    
    public boolean isCaseConditionConstAllow() {
        return this.caseConditionConstAllow;
    }
    
    public void setCaseConditionConstAllow(final boolean caseConditionConstAllow) {
        this.caseConditionConstAllow = caseConditionConstAllow;
    }
    
    public boolean isConditionDoubleConstAllow() {
        return this.conditionDoubleConstAllow;
    }
    
    public void setConditionDoubleConstAllow(final boolean conditionDoubleConstAllow) {
        this.conditionDoubleConstAllow = conditionDoubleConstAllow;
    }
    
    public boolean isConditionLikeTrueAllow() {
        return this.conditionLikeTrueAllow;
    }
    
    public void setConditionLikeTrueAllow(final boolean conditionLikeTrueAllow) {
        this.conditionLikeTrueAllow = conditionLikeTrueAllow;
    }
    
    public boolean isLimitZeroAllow() {
        return this.limitZeroAllow;
    }
    
    public void setLimitZeroAllow(final boolean limitZero) {
        this.limitZeroAllow = limitZero;
    }
    
    public boolean isUseAllow() {
        return this.useAllow;
    }
    
    public void setUseAllow(final boolean useAllow) {
        this.useAllow = useAllow;
    }
    
    public boolean isCommitAllow() {
        return this.commitAllow;
    }
    
    public void setCommitAllow(final boolean commitAllow) {
        this.commitAllow = commitAllow;
    }
    
    public boolean isRollbackAllow() {
        return this.rollbackAllow;
    }
    
    public void setRollbackAllow(final boolean rollbackAllow) {
        this.rollbackAllow = rollbackAllow;
    }
    
    public boolean isIntersectAllow() {
        return this.intersectAllow;
    }
    
    public void setIntersectAllow(final boolean intersectAllow) {
        this.intersectAllow = intersectAllow;
    }
    
    public boolean isMinusAllow() {
        return this.minusAllow;
    }
    
    public void setMinusAllow(final boolean minusAllow) {
        this.minusAllow = minusAllow;
    }
    
    public boolean isConditionOpXorAllow() {
        return this.conditionOpXorAllow;
    }
    
    public void setConditionOpXorAllow(final boolean conditionOpXorAllow) {
        this.conditionOpXorAllow = conditionOpXorAllow;
    }
    
    public String getTenantTablePattern() {
        return this.tenantTablePattern;
    }
    
    public void setTenantTablePattern(final String tenantTablePattern) {
        this.tenantTablePattern = tenantTablePattern;
    }
    
    public String getTenantColumn() {
        return this.tenantColumn;
    }
    
    public void setTenantColumn(final String tenantColumn) {
        this.tenantColumn = tenantColumn;
    }
    
    public TenantCallBack getTenantCallBack() {
        return this.tenantCallBack;
    }
    
    public void setTenantCallBack(final TenantCallBack tenantCallBack) {
        this.tenantCallBack = tenantCallBack;
    }
    
    public boolean isMetadataAllow() {
        return this.metadataAllow;
    }
    
    public void setMetadataAllow(final boolean metadataAllow) {
        this.metadataAllow = metadataAllow;
    }
    
    public boolean isWrapAllow() {
        return this.wrapAllow;
    }
    
    public void setWrapAllow(final boolean wrapAllow) {
        this.wrapAllow = wrapAllow;
    }
    
    public boolean isDoPrivilegedAllow() {
        return this.doPrivilegedAllow;
    }
    
    public void setDoPrivilegedAllow(final boolean doPrivilegedAllow) {
        this.doPrivilegedAllow = doPrivilegedAllow;
    }
    
    public boolean isSelectAllColumnAllow() {
        return this.selectAllColumnAllow;
    }
    
    public void setSelectAllColumnAllow(final boolean selectAllColumnAllow) {
        this.selectAllColumnAllow = selectAllColumnAllow;
    }
    
    @Override
    public boolean isInited() {
        return this.inited;
    }
    
    public WallConfig(final String dir) {
        this();
        this.dir = dir;
        this.init();
    }
    
    @Override
    public String getDir() {
        return this.dir;
    }
    
    @Override
    public void setDir(final String dir) {
        this.dir = dir;
    }
    
    @Override
    public final void init() {
        this.loadConfig(this.dir);
    }
    
    @Override
    public void loadConfig(String dir) {
        if (dir.endsWith("/")) {
            dir = dir.substring(0, dir.length() - 1);
        }
        WallVisitorUtils.loadResource(this.denyVariants, dir + "/deny-variant.txt");
        WallVisitorUtils.loadResource(this.denySchemas, dir + "/deny-schema.txt");
        WallVisitorUtils.loadResource(this.denyFunctions, dir + "/deny-function.txt");
        WallVisitorUtils.loadResource(this.denyTables, dir + "/deny-table.txt");
        WallVisitorUtils.loadResource(this.denyObjects, dir + "/deny-object.txt");
        WallVisitorUtils.loadResource(this.readOnlyTables, dir + "/readonly-table.txt");
        WallVisitorUtils.loadResource(this.permitFunctions, dir + "/permit-function.txt");
        WallVisitorUtils.loadResource(this.permitTables, dir + "/permit-table.txt");
        WallVisitorUtils.loadResource(this.permitSchemas, dir + "/permit-schema.txt");
        WallVisitorUtils.loadResource(this.permitVariants, dir + "/permit-variant.txt");
    }
    
    @Override
    public boolean isNoneBaseStatementAllow() {
        return this.noneBaseStatementAllow;
    }
    
    @Override
    public void setNoneBaseStatementAllow(final boolean noneBaseStatementAllow) {
        this.noneBaseStatementAllow = noneBaseStatementAllow;
    }
    
    public boolean isDescribeAllow() {
        return this.describeAllow;
    }
    
    public void setDescribeAllow(final boolean describeAllow) {
        this.describeAllow = describeAllow;
    }
    
    public boolean isShowAllow() {
        return this.showAllow;
    }
    
    public void setShowAllow(final boolean showAllow) {
        this.showAllow = showAllow;
    }
    
    @Override
    public boolean isTruncateAllow() {
        return this.truncateAllow;
    }
    
    @Override
    public void setTruncateAllow(final boolean truncateAllow) {
        this.truncateAllow = truncateAllow;
    }
    
    @Override
    public boolean isSelectIntoAllow() {
        return this.selectIntoAllow;
    }
    
    @Override
    public void setSelectIntoAllow(final boolean selectIntoAllow) {
        this.selectIntoAllow = selectIntoAllow;
    }
    
    @Override
    public boolean isSelectIntoOutfileAllow() {
        return this.selectIntoOutfileAllow;
    }
    
    @Override
    public void setSelectIntoOutfileAllow(final boolean selectIntoOutfileAllow) {
        this.selectIntoOutfileAllow = selectIntoOutfileAllow;
    }
    
    public boolean isCreateTableAllow() {
        return this.createTableAllow;
    }
    
    public void setCreateTableAllow(final boolean createTableAllow) {
        this.createTableAllow = createTableAllow;
    }
    
    public boolean isDropTableAllow() {
        return this.dropTableAllow;
    }
    
    public void setDropTableAllow(final boolean dropTableAllow) {
        this.dropTableAllow = dropTableAllow;
    }
    
    public boolean isAlterTableAllow() {
        return this.alterTableAllow;
    }
    
    public void setAlterTableAllow(final boolean alterTableAllow) {
        this.alterTableAllow = alterTableAllow;
    }
    
    public boolean isRenameTableAllow() {
        return this.renameTableAllow;
    }
    
    public void setRenameTableAllow(final boolean renameTableAllow) {
        this.renameTableAllow = renameTableAllow;
    }
    
    @Override
    public boolean isSelectUnionCheck() {
        return this.selectUnionCheck;
    }
    
    @Override
    public void setSelectUnionCheck(final boolean selectUnionCheck) {
        this.selectUnionCheck = selectUnionCheck;
    }
    
    public boolean isSelectMinusCheck() {
        return this.selectMinusCheck;
    }
    
    public void setSelectMinusCheck(final boolean selectMinusCheck) {
        this.selectMinusCheck = selectMinusCheck;
    }
    
    public boolean isSelectExceptCheck() {
        return this.selectExceptCheck;
    }
    
    public void setSelectExceptCheck(final boolean selectExceptCheck) {
        this.selectExceptCheck = selectExceptCheck;
    }
    
    public boolean isSelectIntersectCheck() {
        return this.selectIntersectCheck;
    }
    
    public void setSelectIntersectCheck(final boolean selectIntersectCheck) {
        this.selectIntersectCheck = selectIntersectCheck;
    }
    
    @Override
    public boolean isDeleteAllow() {
        return this.deleteAllow;
    }
    
    @Override
    public void setDeleteAllow(final boolean deleteAllow) {
        this.deleteAllow = deleteAllow;
    }
    
    public boolean isDeleteWhereNoneCheck() {
        return this.deleteWhereNoneCheck;
    }
    
    public void setDeleteWhereNoneCheck(final boolean deleteWhereNoneCheck) {
        this.deleteWhereNoneCheck = deleteWhereNoneCheck;
    }
    
    @Override
    public boolean isUpdateAllow() {
        return this.updateAllow;
    }
    
    @Override
    public void setUpdateAllow(final boolean updateAllow) {
        this.updateAllow = updateAllow;
    }
    
    public boolean isUpdateWhereNoneCheck() {
        return this.updateWhereNoneCheck;
    }
    
    public void setUpdateWhereNoneCheck(final boolean updateWhereNoneCheck) {
        this.updateWhereNoneCheck = updateWhereNoneCheck;
    }
    
    @Override
    public boolean isInsertAllow() {
        return this.insertAllow;
    }
    
    @Override
    public void setInsertAllow(final boolean insertAllow) {
        this.insertAllow = insertAllow;
    }
    
    public boolean isReplaceAllow() {
        return this.replaceAllow;
    }
    
    public void setReplaceAllow(final boolean replaceAllow) {
        this.replaceAllow = replaceAllow;
    }
    
    public boolean isSetAllow() {
        return this.setAllow;
    }
    
    public void setSetAllow(final boolean value) {
        this.setAllow = value;
    }
    
    @Override
    public boolean isMergeAllow() {
        return this.mergeAllow;
    }
    
    @Override
    public void setMergeAllow(final boolean mergeAllow) {
        this.mergeAllow = mergeAllow;
    }
    
    @Override
    public boolean isMultiStatementAllow() {
        return this.multiStatementAllow;
    }
    
    @Override
    public void setMultiStatementAllow(final boolean multiStatementAllow) {
        this.multiStatementAllow = multiStatementAllow;
    }
    
    @Override
    public boolean isSchemaCheck() {
        return this.schemaCheck;
    }
    
    @Override
    public void setSchemaCheck(final boolean schemaCheck) {
        this.schemaCheck = schemaCheck;
    }
    
    @Override
    public boolean isTableCheck() {
        return this.tableCheck;
    }
    
    @Override
    public void setTableCheck(final boolean tableCheck) {
        this.tableCheck = tableCheck;
    }
    
    @Override
    public boolean isFunctionCheck() {
        return this.functionCheck;
    }
    
    @Override
    public void setFunctionCheck(final boolean functionCheck) {
        this.functionCheck = functionCheck;
    }
    
    @Override
    public boolean isVariantCheck() {
        return this.variantCheck;
    }
    
    @Override
    public void setVariantCheck(final boolean variantCheck) {
        this.variantCheck = variantCheck;
    }
    
    @Override
    public boolean isObjectCheck() {
        return this.objectCheck;
    }
    
    @Override
    public void setObjectCheck(final boolean objectCheck) {
        this.objectCheck = objectCheck;
    }
    
    @Override
    public boolean isCommentAllow() {
        return this.commentAllow;
    }
    
    @Override
    public void setCommentAllow(final boolean commentAllow) {
        this.commentAllow = commentAllow;
    }
    
    public boolean isStrictSyntaxCheck() {
        return this.strictSyntaxCheck;
    }
    
    public void setStrictSyntaxCheck(final boolean strictSyntaxCheck) {
        this.strictSyntaxCheck = strictSyntaxCheck;
    }
    
    public boolean isConstArithmeticAllow() {
        return this.constArithmeticAllow;
    }
    
    public void setConstArithmeticAllow(final boolean constArithmeticAllow) {
        this.constArithmeticAllow = constArithmeticAllow;
    }
    
    @Override
    public Set<String> getDenyFunctions() {
        return this.denyFunctions;
    }
    
    @Override
    public Set<String> getDenyTables() {
        return this.denyTables;
    }
    
    @Override
    public Set<String> getDenySchemas() {
        return this.denySchemas;
    }
    
    @Override
    public Set<String> getDenyVariants() {
        return this.denyVariants;
    }
    
    @Override
    public Set<String> getDenyObjects() {
        return this.denyObjects;
    }
    
    @Override
    public Set<String> getReadOnlyTables() {
        return this.readOnlyTables;
    }
    
    public void addReadOnlyTable(final String tableName) {
        this.readOnlyTables.add(tableName);
    }
    
    public boolean isReadOnly(final String tableName) {
        return this.readOnlyTables.contains(tableName);
    }
    
    public Set<String> getPermitFunctions() {
        return this.permitFunctions;
    }
    
    public Set<String> getPermitTables() {
        return this.permitTables;
    }
    
    public Set<String> getPermitSchemas() {
        return this.permitSchemas;
    }
    
    public Set<String> getPermitVariants() {
        return this.permitVariants;
    }
    
    public boolean isMustParameterized() {
        return this.mustParameterized;
    }
    
    public void setMustParameterized(final boolean mustParameterized) {
        this.mustParameterized = mustParameterized;
    }
    
    @Override
    public boolean isDenyObjects(String name) {
        if (!this.objectCheck) {
            return false;
        }
        name = WallVisitorUtils.form(name);
        return this.denyObjects.contains(name);
    }
    
    @Override
    public boolean isDenySchema(String name) {
        if (!this.schemaCheck) {
            return false;
        }
        name = WallVisitorUtils.form(name);
        return this.denySchemas.contains(name);
    }
    
    @Override
    public boolean isDenyFunction(String name) {
        if (!this.functionCheck) {
            return false;
        }
        name = WallVisitorUtils.form(name);
        return this.denyFunctions.contains(name);
    }
    
    public boolean isCallAllow() {
        return this.callAllow;
    }
    
    public void setCallAllow(final boolean callAllow) {
        this.callAllow = callAllow;
    }
    
    public boolean isHintAllow() {
        return this.hintAllow;
    }
    
    public void setHintAllow(final boolean hintAllow) {
        this.hintAllow = hintAllow;
    }
    
    public boolean isSelectAllow() {
        return this.selelctAllow;
    }
    
    public void setSelectAllow(final boolean selelctAllow) {
        this.selelctAllow = selelctAllow;
    }
    
    @Override
    @Deprecated
    public boolean isSelelctAllow() {
        return this.isSelectAllow();
    }
    
    @Override
    @Deprecated
    public void setSelelctAllow(final boolean selelctAllow) {
        this.setSelectAllow(selelctAllow);
    }
    
    @Override
    public boolean isSelectWhereAlwayTrueCheck() {
        return this.selectWhereAlwayTrueCheck;
    }
    
    @Override
    public void setSelectWhereAlwayTrueCheck(final boolean selectWhereAlwayTrueCheck) {
        this.selectWhereAlwayTrueCheck = selectWhereAlwayTrueCheck;
    }
    
    @Override
    public boolean isSelectHavingAlwayTrueCheck() {
        return this.selectHavingAlwayTrueCheck;
    }
    
    @Override
    public void setSelectHavingAlwayTrueCheck(final boolean selectHavingAlwayTrueCheck) {
        this.selectHavingAlwayTrueCheck = selectHavingAlwayTrueCheck;
    }
    
    public boolean isConditionAndAlwayTrueAllow() {
        return this.conditionAndAlwayTrueAllow;
    }
    
    public void setConditionAndAlwayTrueAllow(final boolean conditionAndAlwayTrueAllow) {
        this.conditionAndAlwayTrueAllow = conditionAndAlwayTrueAllow;
    }
    
    public boolean isConditionAndAlwayFalseAllow() {
        return this.conditionAndAlwayFalseAllow;
    }
    
    public void setConditionAndAlwayFalseAllow(final boolean conditionAndAlwayFalseAllow) {
        this.conditionAndAlwayFalseAllow = conditionAndAlwayFalseAllow;
    }
    
    @Override
    public boolean isDeleteWhereAlwayTrueCheck() {
        return this.deleteWhereAlwayTrueCheck;
    }
    
    @Override
    public void setDeleteWhereAlwayTrueCheck(final boolean deleteWhereAlwayTrueCheck) {
        this.deleteWhereAlwayTrueCheck = deleteWhereAlwayTrueCheck;
    }
    
    @Override
    public boolean isUpdateWhereAlayTrueCheck() {
        return this.updateWhereAlayTrueCheck;
    }
    
    @Override
    public void setUpdateWhereAlayTrueCheck(final boolean updateWhereAlayTrueCheck) {
        this.updateWhereAlayTrueCheck = updateWhereAlayTrueCheck;
    }
    
    public boolean isConditionOpBitwseAllow() {
        return this.conditionOpBitwseAllow;
    }
    
    public void setConditionOpBitwseAllow(final boolean conditionOpBitwseAllow) {
        this.conditionOpBitwseAllow = conditionOpBitwseAllow;
    }
    
    public void setInited(final boolean inited) {
        this.inited = inited;
    }
    
    public boolean isLockTableAllow() {
        return this.lockTableAllow;
    }
    
    public void setLockTableAllow(final boolean lockTableAllow) {
        this.lockTableAllow = lockTableAllow;
    }
    
    public boolean isStartTransactionAllow() {
        return this.startTransactionAllow;
    }
    
    public void setStartTransactionAllow(final boolean startTransactionAllow) {
        this.startTransactionAllow = startTransactionAllow;
    }
    
    public boolean isCompleteInsertValuesCheck() {
        return this.completeInsertValuesCheck;
    }
    
    public void setCompleteInsertValuesCheck(final boolean completeInsertValuesCheck) {
        this.completeInsertValuesCheck = completeInsertValuesCheck;
    }
    
    public int getInsertValuesCheckSize() {
        return this.insertValuesCheckSize;
    }
    
    public void setInsertValuesCheckSize(final int insertValuesCheckSize) {
        this.insertValuesCheckSize = insertValuesCheckSize;
    }
    
    public boolean isBlockAllow() {
        return this.blockAllow;
    }
    
    public void setBlockAllow(final boolean blockAllow) {
        this.blockAllow = blockAllow;
    }
    
    public int getSelectLimit() {
        return this.selectLimit;
    }
    
    public void setSelectLimit(final int selectLimit) {
        this.selectLimit = selectLimit;
    }
    
    public void configFromProperties(final Properties properties) {
        String propertyValue = properties.getProperty("druid.wall.tenantColumn");
        if (propertyValue != null) {
            this.setTenantColumn(propertyValue);
        }
        Boolean propertyValue2 = Utils.getBoolean(properties, "druid.wall.selelctAllow");
        if (propertyValue2 != null) {
            this.setSelectAllow(propertyValue2);
        }
        propertyValue2 = Utils.getBoolean(properties, "druid.wall.updateAllow");
        if (propertyValue2 != null) {
            this.setUpdateAllow(propertyValue2);
        }
        propertyValue2 = Utils.getBoolean(properties, "druid.wall.deleteAllow");
        if (propertyValue2 != null) {
            this.setDeleteAllow(propertyValue2);
        }
        propertyValue2 = Utils.getBoolean(properties, "druid.wall.insertAllow");
        if (propertyValue2 != null) {
            this.setInsertAllow(propertyValue2);
        }
        propertyValue2 = Utils.getBoolean(properties, "druid.wall.multiStatementAllow");
        if (propertyValue2 != null) {
            this.setMultiStatementAllow(propertyValue2);
        }
        final Integer propertyValue3 = Utils.getInteger(properties, "druid.wall.selectLimit");
        if (propertyValue3 != null) {
            this.setSelectLimit(propertyValue3);
        }
        propertyValue = properties.getProperty("druid.wall.updateCheckColumns");
        if (propertyValue != null) {
            final String[] split;
            final String[] items = split = propertyValue.split(",");
            for (final String item : split) {
                this.addUpdateCheckCoumns(item);
            }
        }
        propertyValue2 = Utils.getBoolean(properties, "druid.wall.updateWhereNoneCheck");
        if (propertyValue2 != null) {
            this.setUpdateWhereNoneCheck(propertyValue2);
        }
        propertyValue2 = Utils.getBoolean(properties, "druid.wall.deleteWhereNoneCheck");
        if (propertyValue2 != null) {
            this.setDeleteWhereNoneCheck(propertyValue2);
        }
    }
    
    public void addUpdateCheckCoumns(final String columnInfo) {
        final String[] items = columnInfo.split("\\.");
        if (items.length != 2) {
            return;
        }
        final String table = SQLUtils.normalize(items[0]).toLowerCase();
        final String column = SQLUtils.normalize(items[1]).toLowerCase();
        Set<String> columns = this.updateCheckColumns.get(table);
        if (columns == null) {
            columns = new LinkedHashSet<String>();
            this.updateCheckColumns.put(table, columns);
        }
        columns.add(column);
    }
    
    public boolean isUpdateCheckTable(final String tableName) {
        if (this.updateCheckColumns.isEmpty()) {
            return false;
        }
        final String tableNameLower = SQLUtils.normalize(tableName).toLowerCase();
        return this.updateCheckColumns.containsKey(tableNameLower);
    }
    
    public Set<String> getUpdateCheckTable(final String tableName) {
        if (this.updateCheckColumns.isEmpty()) {
            return null;
        }
        final String tableNameLower = SQLUtils.normalize(tableName).toLowerCase();
        return this.updateCheckColumns.get(tableNameLower);
    }
    
    public WallUpdateCheckHandler getUpdateCheckHandler() {
        return this.updateCheckHandler;
    }
    
    public void setUpdateCheckHandler(final WallUpdateCheckHandler updateCheckHandler) {
        this.updateCheckHandler = updateCheckHandler;
    }
    
    public interface TenantCallBack
    {
        Object getTenantValue(final StatementType p0, final String p1);
        
        String getTenantColumn(final StatementType p0, final String p1);
        
        String getHiddenColumn(final String p0);
        
        void filterResultsetTenantColumn(final Object p0);
        
        public enum StatementType
        {
            SELECT, 
            UPDATE, 
            INSERT, 
            DELETE;
        }
    }
}
