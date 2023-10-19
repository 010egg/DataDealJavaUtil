// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import java.util.List;

public class MySqlFlushStatement extends MySqlStatementImpl
{
    private boolean noWriteToBinlog;
    private boolean local;
    private final List<SQLExprTableSource> tables;
    private boolean withReadLock;
    private boolean forExport;
    private boolean binaryLogs;
    private boolean desKeyFile;
    private boolean engineLogs;
    private boolean errorLogs;
    private boolean generalLogs;
    private boolean hots;
    private boolean logs;
    private boolean master;
    private boolean privileges;
    private boolean optimizerCosts;
    private boolean queryCache;
    private boolean relayLogs;
    private SQLExpr relayLogsForChannel;
    private boolean slowLogs;
    private boolean status;
    private boolean userResources;
    private boolean tableOption;
    private SQLIntegerExpr version;
    
    public MySqlFlushStatement() {
        this.noWriteToBinlog = false;
        this.local = false;
        this.tables = new ArrayList<SQLExprTableSource>();
        this.withReadLock = false;
    }
    
    public boolean isNoWriteToBinlog() {
        return this.noWriteToBinlog;
    }
    
    public void setNoWriteToBinlog(final boolean noWriteToBinlog) {
        this.noWriteToBinlog = noWriteToBinlog;
    }
    
    public boolean isLocal() {
        return this.local;
    }
    
    public void setLocal(final boolean local) {
        this.local = local;
    }
    
    public List<SQLExprTableSource> getTables() {
        return this.tables;
    }
    
    public boolean isWithReadLock() {
        return this.withReadLock;
    }
    
    public void setWithReadLock(final boolean withReadLock) {
        this.withReadLock = withReadLock;
    }
    
    public boolean isForExport() {
        return this.forExport;
    }
    
    public void setForExport(final boolean forExport) {
        this.forExport = forExport;
    }
    
    public boolean isBinaryLogs() {
        return this.binaryLogs;
    }
    
    public void setBinaryLogs(final boolean binaryLogs) {
        this.binaryLogs = binaryLogs;
    }
    
    public boolean isDesKeyFile() {
        return this.desKeyFile;
    }
    
    public void setDesKeyFile(final boolean desKeyFile) {
        this.desKeyFile = desKeyFile;
    }
    
    public boolean isEngineLogs() {
        return this.engineLogs;
    }
    
    public void setEngineLogs(final boolean engineLogs) {
        this.engineLogs = engineLogs;
    }
    
    public boolean isGeneralLogs() {
        return this.generalLogs;
    }
    
    public void setGeneralLogs(final boolean generalLogs) {
        this.generalLogs = generalLogs;
    }
    
    public boolean isHots() {
        return this.hots;
    }
    
    public void setHots(final boolean hots) {
        this.hots = hots;
    }
    
    public boolean isLogs() {
        return this.logs;
    }
    
    public void setLogs(final boolean logs) {
        this.logs = logs;
    }
    
    public boolean isPrivileges() {
        return this.privileges;
    }
    
    public void setPrivileges(final boolean privileges) {
        this.privileges = privileges;
    }
    
    public boolean isOptimizerCosts() {
        return this.optimizerCosts;
    }
    
    public void setOptimizerCosts(final boolean optimizerCosts) {
        this.optimizerCosts = optimizerCosts;
    }
    
    public boolean isQueryCache() {
        return this.queryCache;
    }
    
    public void setQueryCache(final boolean queryCache) {
        this.queryCache = queryCache;
    }
    
    public boolean isRelayLogs() {
        return this.relayLogs;
    }
    
    public void setRelayLogs(final boolean relayLogs) {
        this.relayLogs = relayLogs;
    }
    
    public SQLExpr getRelayLogsForChannel() {
        return this.relayLogsForChannel;
    }
    
    public void setRelayLogsForChannel(final SQLExpr relayLogsForChannel) {
        this.relayLogsForChannel = relayLogsForChannel;
    }
    
    public boolean isSlowLogs() {
        return this.slowLogs;
    }
    
    public void setSlowLogs(final boolean showLogs) {
        this.slowLogs = showLogs;
    }
    
    public boolean isStatus() {
        return this.status;
    }
    
    public void setStatus(final boolean status) {
        this.status = status;
    }
    
    public boolean isUserResources() {
        return this.userResources;
    }
    
    public void setUserResources(final boolean userResources) {
        this.userResources = userResources;
    }
    
    public boolean isErrorLogs() {
        return this.errorLogs;
    }
    
    public void setErrorLogs(final boolean errorLogs) {
        this.errorLogs = errorLogs;
    }
    
    public boolean isTableOption() {
        return this.tableOption;
    }
    
    public void setTableOption(final boolean tableOption) {
        this.tableOption = tableOption;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tables);
            this.acceptChild(visitor, this.relayLogsForChannel);
        }
        visitor.endVisit(this);
    }
    
    public void addTable(final SQLName name) {
        if (name == null) {
            return;
        }
        this.addTable(new SQLExprTableSource(name));
    }
    
    public void addTable(final SQLExprTableSource table) {
        if (table == null) {
            return;
        }
        table.setParent(this);
        this.tables.add(table);
    }
    
    public SQLIntegerExpr getVersion() {
        return this.version;
    }
    
    public void setVersion(final SQLIntegerExpr version) {
        this.version = version;
    }
    
    public boolean isMaster() {
        return this.master;
    }
    
    public void setMaster(final boolean master) {
        this.master = master;
    }
}
