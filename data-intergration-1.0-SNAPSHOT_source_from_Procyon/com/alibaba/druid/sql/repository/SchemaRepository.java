// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.repository;

import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.alibaba.druid.sql.ast.statement.SQLUseStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.statement.SQLDropSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewStatement;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnConstraint;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.Collection;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.SQLUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedHashMap;
import com.alibaba.druid.sql.repository.function.Function;
import java.util.Map;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.support.logging.Log;

public class SchemaRepository
{
    private static Log LOG;
    private Schema defaultSchema;
    protected DbType dbType;
    protected DbType schemaDbType;
    protected SQLASTVisitor consoleVisitor;
    protected Map<String, Schema> schemas;
    protected final Map<Long, Function> internalFunctions;
    protected SchemaLoader schemaLoader;
    
    public SchemaRepository() {
        this.schemas = new LinkedHashMap<String, Schema>();
        this.internalFunctions = new ConcurrentHashMap<Long, Function>(16, 0.75f, 1);
    }
    
    public SchemaRepository(final DbType dbType) {
        this(dbType, dbType);
    }
    
    public SchemaRepository(DbType dbType, DbType schemaDbType) {
        this.schemas = new LinkedHashMap<String, Schema>();
        this.internalFunctions = new ConcurrentHashMap<Long, Function>(16, 0.75f, 1);
        if (dbType == null) {
            dbType = DbType.other;
        }
        if (schemaDbType == null) {
            schemaDbType = dbType;
        }
        this.dbType = dbType;
        this.schemaDbType = schemaDbType;
        switch (dbType) {
            case mysql: {
                this.consoleVisitor = new MySqlConsoleSchemaVisitor();
                break;
            }
            case oracle: {
                this.consoleVisitor = new OracleConsoleSchemaVisitor();
                break;
            }
            default: {
                this.consoleVisitor = new DefaultConsoleSchemaVisitor();
                break;
            }
        }
    }
    
    public DbType getDbType() {
        return this.dbType;
    }
    
    public String getDefaultSchemaName() {
        return this.getDefaultSchema().getName();
    }
    
    public void setDefaultSchema(final String name) {
        if (name == null) {
            this.defaultSchema = null;
            return;
        }
        final String normalizedName = SQLUtils.normalize(name).toLowerCase();
        Schema defaultSchema = this.schemas.get(normalizedName);
        if (defaultSchema != null) {
            this.defaultSchema = defaultSchema;
            return;
        }
        if (this.defaultSchema != null && this.defaultSchema.getName() == null) {
            this.defaultSchema.setName(name);
            this.schemas.put(normalizedName, this.defaultSchema);
            return;
        }
        defaultSchema = new Schema(this);
        defaultSchema.setName(name);
        this.schemas.put(normalizedName, defaultSchema);
        this.defaultSchema = defaultSchema;
    }
    
    public Schema findSchema(final String schema) {
        return this.findSchema(schema, false);
    }
    
    protected Schema findSchema(String name, final boolean create) {
        if (name == null || name.length() == 0) {
            return this.getDefaultSchema();
        }
        name = SQLUtils.normalize(name);
        final String normalizedName = name.toLowerCase();
        if (this.getDefaultSchema() != null && this.defaultSchema.getName() == null && create) {
            this.defaultSchema.setName(name);
            this.schemas.put(normalizedName, this.defaultSchema);
            return this.defaultSchema;
        }
        Schema schema = this.schemas.get(normalizedName);
        if (schema == null && create) {
            final int p = name.indexOf(46);
            String catalog = null;
            if (p != -1) {
                catalog = name.substring(0, p);
            }
            schema = new Schema(this, catalog, name);
            this.schemas.put(normalizedName, schema);
        }
        return schema;
    }
    
    public Schema getDefaultSchema() {
        if (this.defaultSchema == null) {
            this.defaultSchema = new Schema(this);
        }
        return this.defaultSchema;
    }
    
    public void setDefaultSchema(final Schema schema) {
        this.defaultSchema = schema;
    }
    
    public SchemaObject findTable(final String tableName) {
        if (tableName.indexOf(46) != -1) {
            final SQLExpr expr = SQLUtils.toSQLExpr(tableName, this.dbType);
            if (!(expr instanceof SQLIdentifierExpr)) {
                return this.findTable((SQLName)expr);
            }
        }
        final SchemaObject object = this.getDefaultSchema().findTable(tableName);
        if (object != null) {
            return object;
        }
        final String ddl = this.loadDDL(tableName);
        if (ddl == null) {
            return null;
        }
        DbType schemaDbType = this.schemaDbType;
        if (schemaDbType == null) {
            schemaDbType = this.dbType;
        }
        final SchemaObject schemaObject = this.acceptDDL(ddl, schemaDbType);
        if (schemaObject != null) {
            return schemaObject;
        }
        return this.getDefaultSchema().findTable(tableName);
    }
    
    public SchemaObject findView(final String viewName) {
        final SchemaObject object = this.getDefaultSchema().findView(viewName);
        if (object != null) {
            return object;
        }
        final String ddl = this.loadDDL(viewName);
        if (ddl == null) {
            return null;
        }
        this.acceptDDL(ddl);
        return this.getDefaultSchema().findView(viewName);
    }
    
    public SchemaObject findTable(final long tableNameHash) {
        return this.getDefaultSchema().findTable(tableNameHash);
    }
    
    public SchemaObject findTableOrView(final String tableName) {
        return this.findTableOrView(tableName, true);
    }
    
    public SchemaObject findTableOrView(final String tableName, final boolean onlyCurrent) {
        final Schema schema = this.getDefaultSchema();
        SchemaObject object = schema.findTableOrView(tableName);
        if (object != null) {
            return object;
        }
        for (final Schema s : this.schemas.values()) {
            if (s == schema) {
                continue;
            }
            object = schema.findTableOrView(tableName);
            if (object != null) {
                return object;
            }
        }
        final String ddl = this.loadDDL(tableName);
        if (ddl == null) {
            return null;
        }
        this.acceptDDL(ddl);
        object = schema.findTableOrView(tableName);
        if (object != null) {
            return object;
        }
        for (final Schema s2 : this.schemas.values()) {
            if (s2 == schema) {
                continue;
            }
            object = schema.findTableOrView(tableName);
            if (object != null) {
                return object;
            }
        }
        return null;
    }
    
    public Collection<Schema> getSchemas() {
        return this.schemas.values();
    }
    
    public SchemaObject findFunction(final String functionName) {
        return this.getDefaultSchema().findFunction(functionName);
    }
    
    public void acceptDDL(final String ddl) {
        this.acceptDDL(ddl, this.schemaDbType);
    }
    
    public SchemaObject acceptDDL(final String ddl, final DbType dbType) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(ddl, dbType);
        for (final SQLStatement stmt : stmtList) {
            if (stmt instanceof SQLCreateTableStatement) {
                final SchemaObject schemaObject = this.acceptCreateTable((SQLCreateTableStatement)stmt);
                if (stmtList.size() == 1) {
                    return schemaObject;
                }
                continue;
            }
            else if (stmt instanceof SQLCreateViewStatement) {
                final SchemaObject schemaObject = this.acceptView((SQLCreateViewStatement)stmt);
                if (stmtList.size() == 1) {
                    return schemaObject;
                }
                continue;
            }
            else {
                this.accept(stmt);
            }
        }
        return null;
    }
    
    public void accept(final SQLStatement stmt) {
        stmt.accept(this.consoleVisitor);
    }
    
    public boolean isSequence(final String name) {
        return this.getDefaultSchema().isSequence(name);
    }
    
    public SchemaObject findTable(final SQLTableSource tableSource, final String alias) {
        return this.getDefaultSchema().findTable(tableSource, alias);
    }
    
    public SQLColumnDefinition findColumn(final SQLTableSource tableSource, final SQLSelectItem selectItem) {
        return this.getDefaultSchema().findColumn(tableSource, selectItem);
    }
    
    public SQLColumnDefinition findColumn(final SQLTableSource tableSource, final SQLExpr expr) {
        return this.getDefaultSchema().findColumn(tableSource, expr);
    }
    
    public SchemaObject findTable(final SQLTableSource tableSource, final SQLSelectItem selectItem) {
        return this.getDefaultSchema().findTable(tableSource, selectItem);
    }
    
    public SchemaObject findTable(final SQLTableSource tableSource, final SQLExpr expr) {
        return this.getDefaultSchema().findTable(tableSource, expr);
    }
    
    public Map<String, SchemaObject> getTables(final SQLTableSource x) {
        return this.getDefaultSchema().getTables(x);
    }
    
    public boolean removeTable(final SQLName name) {
        return this.getDefaultSchema().removeObject(name.nameHashCode64());
    }
    
    public int getTableCount() {
        return this.getDefaultSchema().getTableCount();
    }
    
    public Collection<SchemaObject> getObjects() {
        return this.getDefaultSchema().getObjects();
    }
    
    public int getViewCount() {
        return this.getDefaultSchema().getViewCount();
    }
    
    public void resolve(final SQLSelectStatement stmt, final SchemaResolveVisitor.Option... options) {
        if (stmt == null) {
            return;
        }
        final SchemaResolveVisitor resolveVisitor = this.createResolveVisitor(options);
        resolveVisitor.visit(stmt);
    }
    
    public void resolve(final SQLSelect select, final SchemaResolveVisitor.Option... options) {
        if (select == null) {
            return;
        }
        final SchemaResolveVisitor resolveVisitor = this.createResolveVisitor(options);
        resolveVisitor.visit(select);
    }
    
    public void resolve(final SQLSelectQueryBlock queryBlock, final SchemaResolveVisitor.Option... options) {
        if (queryBlock == null) {
            return;
        }
        final SchemaResolveVisitor resolveVisitor = this.createResolveVisitor(options);
        resolveVisitor.visit(queryBlock);
    }
    
    public void resolve(final SQLStatement stmt, final SchemaResolveVisitor.Option... options) {
        if (stmt == null) {
            return;
        }
        final SchemaResolveVisitor resolveVisitor = this.createResolveVisitor(options);
        if (stmt instanceof SQLSelectStatement) {
            resolveVisitor.visit((SQLSelectStatement)stmt);
        }
        else {
            stmt.accept(resolveVisitor);
        }
    }
    
    private SchemaResolveVisitor createResolveVisitor(final SchemaResolveVisitor.Option... options) {
        final int optionsValue = SchemaResolveVisitor.Option.of(options);
        SchemaResolveVisitor resolveVisitor = null;
        switch (this.dbType) {
            case mysql:
            case mariadb:
            case sqlite: {
                resolveVisitor = new SchemaResolveVisitorFactory.MySqlResolveVisitor(this, optionsValue);
                break;
            }
            case oracle: {
                resolveVisitor = new SchemaResolveVisitorFactory.OracleResolveVisitor(this, optionsValue);
                break;
            }
            case db2: {
                resolveVisitor = new SchemaResolveVisitorFactory.DB2ResolveVisitor(this, optionsValue);
                break;
            }
            case odps: {
                resolveVisitor = new SchemaResolveVisitorFactory.OdpsResolveVisitor(this, optionsValue);
                break;
            }
            case hive: {
                resolveVisitor = new SchemaResolveVisitorFactory.HiveResolveVisitor(this, optionsValue);
                break;
            }
            case postgresql:
            case edb: {
                resolveVisitor = new SchemaResolveVisitorFactory.PGResolveVisitor(this, optionsValue);
                break;
            }
            case sqlserver: {
                resolveVisitor = new SchemaResolveVisitorFactory.SQLServerResolveVisitor(this, optionsValue);
                break;
            }
            default: {
                resolveVisitor = new SchemaResolveVisitorFactory.SQLResolveVisitor(this, optionsValue);
                break;
            }
        }
        return resolveVisitor;
    }
    
    public String resolve(final String input) {
        final SchemaResolveVisitor visitor = this.createResolveVisitor(SchemaResolveVisitor.Option.ResolveAllColumn, SchemaResolveVisitor.Option.ResolveIdentifierAlias);
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(input, this.dbType);
        for (final SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }
        return SQLUtils.toSQLString(stmtList, this.dbType);
    }
    
    public String console(final String input) {
        try {
            final StringBuffer buf = new StringBuffer();
            final List<SQLStatement> stmtList = SQLUtils.parseStatements(input, this.dbType);
            for (final SQLStatement stmt : stmtList) {
                if (stmt instanceof SQLShowColumnsStatement) {
                    final SQLShowColumnsStatement showColumns = (SQLShowColumnsStatement)stmt;
                    final SQLName db = showColumns.getDatabase();
                    Schema schema;
                    if (db == null) {
                        schema = this.getDefaultSchema();
                    }
                    else {
                        schema = this.findSchema(db.getSimpleName());
                    }
                    SQLName table = null;
                    SchemaObject schemaObject = null;
                    if (schema != null) {
                        table = showColumns.getTable();
                        schemaObject = schema.findTable(table.nameHashCode64());
                    }
                    if (schemaObject == null) {
                        buf.append("ERROR 1146 (42S02): Table '" + table + "' doesn't exist\n");
                    }
                    else {
                        final MySqlCreateTableStatement createTableStmt = (MySqlCreateTableStatement)schemaObject.getStatement();
                        createTableStmt.showCoumns(buf);
                    }
                }
                else if (stmt instanceof SQLShowCreateTableStatement) {
                    final SQLShowCreateTableStatement showCreateTableStmt = (SQLShowCreateTableStatement)stmt;
                    final SQLName table2 = showCreateTableStmt.getName();
                    final SchemaObject schemaObject2 = this.findTable(table2);
                    if (schemaObject2 == null) {
                        buf.append("ERROR 1146 (42S02): Table '" + table2 + "' doesn't exist\n");
                    }
                    else {
                        final MySqlCreateTableStatement createTableStmt2 = (MySqlCreateTableStatement)schemaObject2.getStatement();
                        createTableStmt2.output(buf);
                    }
                }
                else if (stmt instanceof MySqlRenameTableStatement) {
                    final MySqlRenameTableStatement renameStmt = (MySqlRenameTableStatement)stmt;
                    for (final MySqlRenameTableStatement.Item item : renameStmt.getItems()) {
                        this.renameTable(item.getName(), item.getTo());
                    }
                }
                else if (stmt instanceof SQLShowTablesStatement) {
                    final SQLShowTablesStatement showTables = (SQLShowTablesStatement)stmt;
                    final SQLName database = showTables.getDatabase();
                    Schema schema;
                    if (database == null) {
                        schema = this.getDefaultSchema();
                    }
                    else {
                        schema = this.findSchema(database.getSimpleName());
                    }
                    if (schema == null) {
                        continue;
                    }
                    for (final String table3 : schema.showTables()) {
                        buf.append(table3);
                        buf.append('\n');
                    }
                }
                else {
                    stmt.accept(this.consoleVisitor);
                }
            }
            if (buf.length() == 0) {
                return "\n";
            }
            return buf.toString();
        }
        catch (IOException ex) {
            throw new FastsqlException("exeucte command error.", ex);
        }
    }
    
    public SchemaObject findTable(final SQLName name) {
        if (name instanceof SQLIdentifierExpr) {
            return this.findTable(((SQLIdentifierExpr)name).getName());
        }
        if (!(name instanceof SQLPropertyExpr)) {
            return null;
        }
        final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)name;
        final SQLExpr owner = propertyExpr.getOwner();
        String catalog = null;
        String schema;
        if (owner instanceof SQLIdentifierExpr) {
            schema = ((SQLIdentifierExpr)owner).getName();
        }
        else {
            if (!(owner instanceof SQLPropertyExpr)) {
                return null;
            }
            schema = ((SQLPropertyExpr)owner).getName();
            catalog = ((SQLPropertyExpr)owner).getOwnernName();
        }
        final long tableHashCode64 = propertyExpr.nameHashCode64();
        Schema schemaObj = this.findSchema(schema, false);
        if (schemaObj != null) {
            final SchemaObject table = schemaObj.findTable(tableHashCode64);
            if (table != null) {
                return table;
            }
        }
        final String ddl = this.loadDDL(catalog, schema, propertyExpr.getName());
        if (ddl == null) {
            schemaObj = this.findSchema(schema, true);
        }
        else {
            final List<SQLStatement> stmtList = SQLUtils.parseStatements(ddl, this.schemaDbType);
            for (final SQLStatement stmt : stmtList) {
                this.accept(stmt);
            }
            if (stmtList.size() == 1) {
                final SQLStatement stmt2 = stmtList.get(0);
                if (stmt2 instanceof SQLCreateTableStatement) {
                    final SQLCreateTableStatement createStmt = (SQLCreateTableStatement)stmt2;
                    final String schemaName = createStmt.getSchema();
                    schemaObj = this.findSchema(schemaName, true);
                }
            }
        }
        if (schemaObj == null) {
            return null;
        }
        return schemaObj.findTable(tableHashCode64);
    }
    
    public SchemaObject findView(final SQLName name) {
        if (name instanceof SQLIdentifierExpr) {
            return this.findView(((SQLIdentifierExpr)name).getName());
        }
        if (!(name instanceof SQLPropertyExpr)) {
            return null;
        }
        final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)name;
        final SQLExpr owner = propertyExpr.getOwner();
        String catalog = null;
        String schema;
        if (owner instanceof SQLIdentifierExpr) {
            schema = ((SQLIdentifierExpr)owner).getName();
        }
        else {
            if (!(owner instanceof SQLPropertyExpr)) {
                return null;
            }
            schema = ((SQLPropertyExpr)owner).getName();
            catalog = ((SQLPropertyExpr)owner).getOwnernName();
        }
        final long tableHashCode64 = propertyExpr.nameHashCode64();
        Schema schemaObj = this.findSchema(schema, false);
        if (schemaObj != null) {
            final SchemaObject table = schemaObj.findView(tableHashCode64);
            if (table != null) {
                return table;
            }
        }
        final String ddl = this.loadDDL(catalog, schema, propertyExpr.getName());
        if (ddl == null) {
            schemaObj = this.findSchema(schema, true);
        }
        else {
            final List<SQLStatement> stmtList = SQLUtils.parseStatements(ddl, this.schemaDbType);
            for (final SQLStatement stmt : stmtList) {
                this.accept(stmt);
            }
            if (stmtList.size() == 1) {
                final SQLStatement stmt2 = stmtList.get(0);
                if (stmt2 instanceof SQLCreateTableStatement) {
                    final SQLCreateTableStatement createStmt = (SQLCreateTableStatement)stmt2;
                    final String schemaName = createStmt.getSchema();
                    schemaObj = this.findSchema(schemaName, true);
                }
            }
        }
        if (schemaObj == null) {
            return null;
        }
        return schemaObj.findView(tableHashCode64);
    }
    
    private boolean renameTable(final SQLName name, final SQLName to) {
        Schema schema;
        if (name instanceof SQLPropertyExpr) {
            final String schemaName = ((SQLPropertyExpr)name).getOwnernName();
            schema = this.findSchema(schemaName);
        }
        else {
            schema = this.getDefaultSchema();
        }
        if (schema == null) {
            return false;
        }
        final long nameHashCode64 = name.nameHashCode64();
        final SchemaObject schemaObject = schema.findTable(nameHashCode64);
        if (schemaObject != null) {
            final MySqlCreateTableStatement createTableStmt = (MySqlCreateTableStatement)schemaObject.getStatement();
            if (createTableStmt != null) {
                createTableStmt.setName(to.clone());
                this.acceptCreateTable(createTableStmt);
            }
            schema.objects.remove(nameHashCode64);
        }
        return true;
    }
    
    public SchemaObject findTable(final SQLExprTableSource x) {
        if (x == null) {
            return null;
        }
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLName) {
            return this.findTable((SQLName)expr);
        }
        return null;
    }
    
    SchemaObject acceptCreateTable(final MySqlCreateTableStatement x) {
        final SQLExprTableSource like = x.getLike();
        if (like != null) {
            final SchemaObject table = this.findTable((SQLName)like.getExpr());
            if (table != null) {
                final MySqlCreateTableStatement stmt = (MySqlCreateTableStatement)table.getStatement();
                final MySqlCreateTableStatement stmtCloned = stmt.clone();
                stmtCloned.setName(x.getName().clone());
                this.acceptCreateTable((SQLCreateTableStatement)stmtCloned);
                return table;
            }
        }
        return this.acceptCreateTable((SQLCreateTableStatement)x);
    }
    
    SchemaObject acceptCreateTable(final SQLCreateTableStatement x) {
        final SQLCreateTableStatement x2 = x.clone();
        final String schemaName = x2.getSchema();
        final Schema schema = this.findSchema(schemaName, true);
        final SQLSelect select = x2.getSelect();
        if (select != null) {
            select.accept(this.createResolveVisitor(SchemaResolveVisitor.Option.ResolveAllColumn));
            final SQLSelectQueryBlock queryBlock = select.getFirstQueryBlock();
            this.resolve(queryBlock, new SchemaResolveVisitor.Option[0]);
            if (queryBlock != null) {
                final List<SQLSelectItem> selectList = queryBlock.getSelectList();
                for (final SQLSelectItem selectItem : selectList) {
                    final SQLExpr selectItemExpr = selectItem.getExpr();
                    if (!(selectItemExpr instanceof SQLAllColumnExpr)) {
                        if (selectItemExpr instanceof SQLPropertyExpr && ((SQLPropertyExpr)selectItemExpr).getName().equals("*")) {
                            continue;
                        }
                        SQLColumnDefinition column = null;
                        if (selectItemExpr instanceof SQLName) {
                            final SQLColumnDefinition resolvedColumn = ((SQLName)selectItemExpr).getResolvedColumn();
                            if (resolvedColumn != null) {
                                column = new SQLColumnDefinition();
                                column.setDataType(selectItem.computeDataType());
                                if (DbType.mysql == this.dbType) {
                                    if (resolvedColumn.getDefaultExpr() != null) {
                                        column.setDefaultExpr(resolvedColumn.getDefaultExpr().clone());
                                    }
                                    if (resolvedColumn.getConstraints().size() > 0) {
                                        for (final SQLColumnConstraint constraint : resolvedColumn.getConstraints()) {
                                            column.addConstraint(constraint.clone());
                                        }
                                    }
                                    if (resolvedColumn.getComment() != null) {
                                        column.setComment(resolvedColumn.getComment());
                                    }
                                }
                            }
                        }
                        if (column == null) {
                            column = new SQLColumnDefinition();
                            column.setDataType(selectItem.computeDataType());
                        }
                        final String name = selectItem.computeAlias();
                        column.setName(name);
                        column.setDbType(this.dbType);
                        x2.addColumn(column);
                    }
                }
                if (x2.getTableElementList().size() > 0) {
                    x2.setSelect(null);
                }
            }
        }
        final SQLExprTableSource like = x2.getLike();
        if (like != null) {
            SchemaObject tableObject = null;
            final SQLName name2 = like.getName();
            if (name2 != null) {
                tableObject = this.findTable(name2);
            }
            SQLCreateTableStatement tableStmt = null;
            if (tableObject != null) {
                final SQLStatement stmt = tableObject.getStatement();
                if (stmt instanceof SQLCreateTableStatement) {
                    tableStmt = (SQLCreateTableStatement)stmt;
                }
            }
            if (tableStmt != null) {
                final SQLName tableName = x2.getName();
                tableStmt.cloneTo(x2);
                x2.setName(tableName);
                x2.setLike((SQLExprTableSource)null);
            }
        }
        x2.setSchema(null);
        final String name3 = x2.computeName();
        SchemaObject table = schema.findTableOrView(name3);
        if (table != null) {
            if (x2.isIfNotExists()) {
                return table;
            }
            SchemaRepository.LOG.info("replaced table '" + name3 + "'");
        }
        table = new SchemaObject(schema, name3, SchemaObjectType.Table, x2);
        schema.objects.put(table.nameHashCode64(), table);
        return table;
    }
    
    boolean acceptDropTable(final SQLDropTableStatement x) {
        for (final SQLExprTableSource table : x.getTableSources()) {
            final String schemaName = table.getSchema();
            final Schema schema = this.findSchema(schemaName, false);
            if (schema == null) {
                continue;
            }
            final long nameHashCode64 = table.getName().nameHashCode64();
            schema.objects.remove(nameHashCode64);
        }
        return true;
    }
    
    SchemaObject acceptView(final SQLCreateViewStatement x) {
        final String schemaName = x.getSchema();
        final Schema schema = this.findSchema(schemaName, true);
        final String name = x.computeName();
        final SchemaObject view = schema.findTableOrView(name);
        if (view != null) {
            return view;
        }
        final SchemaObject object = new SchemaObject(schema, name, SchemaObjectType.View, x.clone());
        final long nameHashCode64 = FnvHash.hashCode64(name);
        schema.objects.put(nameHashCode64, object);
        return object;
    }
    
    boolean acceptView(final SQLAlterViewStatement x) {
        final String schemaName = x.getSchema();
        final Schema schema = this.findSchema(schemaName, true);
        final String name = x.computeName();
        final SchemaObject view = schema.findTableOrView(name);
        if (view != null) {
            return false;
        }
        final SchemaObject object = new SchemaObject(schema, name, SchemaObjectType.View, x.clone());
        schema.objects.put(object.nameHashCode64(), object);
        return true;
    }
    
    boolean acceptDropIndex(final SQLDropIndexStatement x) {
        if (x.getTableName() == null) {
            return false;
        }
        final SQLName table = x.getTableName().getName();
        final SchemaObject object = this.findTable(table);
        if (object != null) {
            final SQLCreateTableStatement stmt = (SQLCreateTableStatement)object.getStatement();
            if (stmt != null) {
                stmt.apply(x);
                return true;
            }
        }
        return false;
    }
    
    boolean acceptCreateIndex(final SQLCreateIndexStatement x) {
        final String schemaName = x.getSchema();
        final Schema schema = this.findSchema(schemaName, true);
        final String name = x.getName().getSimpleName();
        final SchemaObject object = new SchemaObject(schema, name, SchemaObjectType.Index, x.clone());
        schema.objects.put(object.nameHashCode64(), object);
        return true;
    }
    
    boolean acceptCreateFunction(final SQLCreateFunctionStatement x) {
        final String schemaName = x.getSchema();
        final Schema schema = this.findSchema(schemaName, true);
        final String name = x.getName().getSimpleName();
        final SchemaObject object = new SchemaObject(schema, name, SchemaObjectType.Function, x.clone());
        schema.functions.put(object.nameHashCode64(), object);
        return true;
    }
    
    boolean acceptAlterTable(final SQLAlterTableStatement x) {
        final String schemaName = x.getSchema();
        final Schema schema = this.findSchema(schemaName, true);
        final SchemaObject object = schema.findTable(x.nameHashCode64());
        if (object != null) {
            final SQLCreateTableStatement stmt = (SQLCreateTableStatement)object.getStatement();
            if (stmt != null) {
                stmt.apply(x);
                return true;
            }
        }
        return false;
    }
    
    public boolean acceptCreateSequence(final SQLCreateSequenceStatement x) {
        final String schemaName = x.getSchema();
        final Schema schema = this.findSchema(schemaName, true);
        final String name = x.getName().getSimpleName();
        final SchemaObject object = new SchemaObject(schema, name, SchemaObjectType.Sequence);
        schema.objects.put(object.nameHashCode64(), object);
        return false;
    }
    
    public boolean acceptDropSequence(final SQLDropSequenceStatement x) {
        final String schemaName = x.getSchema();
        final Schema schema = this.findSchema(schemaName, true);
        final long nameHashCode64 = x.getName().nameHashCode64();
        schema.objects.remove(nameHashCode64);
        return false;
    }
    
    public SQLDataType findFuntionReturnType(final long functionNameHashCode) {
        if (functionNameHashCode == FnvHash.Constants.LEN || functionNameHashCode == FnvHash.Constants.LENGTH) {
            return SQLIntegerExpr.DATA_TYPE;
        }
        return null;
    }
    
    protected String loadDDL(String table) {
        if (table == null) {
            return null;
        }
        table = SQLUtils.normalize(table, this.schemaDbType);
        if (this.schemaLoader != null) {
            return this.schemaLoader.loadDDL(null, null, table);
        }
        return null;
    }
    
    protected String loadDDL(String schema, String table) {
        if (table == null) {
            return null;
        }
        table = SQLUtils.normalize(table, this.dbType);
        if (schema != null) {
            schema = SQLUtils.normalize(schema, this.dbType);
        }
        if (this.schemaLoader != null) {
            return this.schemaLoader.loadDDL(null, schema, table);
        }
        return null;
    }
    
    protected String loadDDL(String catalog, String schema, String table) {
        if (table == null) {
            return null;
        }
        table = SQLUtils.normalize(table, this.dbType);
        if (schema != null) {
            schema = SQLUtils.normalize(schema, this.dbType);
        }
        if (catalog != null) {
            catalog = SQLUtils.normalize(catalog, this.dbType);
        }
        if (this.schemaLoader != null) {
            return this.schemaLoader.loadDDL(catalog, schema, table);
        }
        return null;
    }
    
    public SchemaLoader getSchemaLoader() {
        return this.schemaLoader;
    }
    
    public void setSchemaLoader(final SchemaLoader schemaLoader) {
        this.schemaLoader = schemaLoader;
    }
    
    static {
        SchemaRepository.LOG = LogFactory.getLog(SchemaRepository.class);
    }
    
    public class MySqlConsoleSchemaVisitor extends MySqlASTVisitorAdapter
    {
        @Override
        public boolean visit(final SQLDropSequenceStatement x) {
            SchemaRepository.this.acceptDropSequence(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateSequenceStatement x) {
            SchemaRepository.this.acceptCreateSequence(x);
            return false;
        }
        
        @Override
        public boolean visit(final HiveCreateTableStatement x) {
            SchemaRepository.this.acceptCreateTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final MySqlCreateTableStatement x) {
            SchemaRepository.this.acceptCreateTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateTableStatement x) {
            SchemaRepository.this.acceptCreateTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLDropTableStatement x) {
            SchemaRepository.this.acceptDropTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateViewStatement x) {
            SchemaRepository.this.acceptView(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLAlterViewStatement x) {
            SchemaRepository.this.acceptView(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateIndexStatement x) {
            SchemaRepository.this.acceptCreateIndex(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateFunctionStatement x) {
            SchemaRepository.this.acceptCreateFunction(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLAlterTableStatement x) {
            SchemaRepository.this.acceptAlterTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLUseStatement x) {
            final String schema = x.getDatabase().getSimpleName();
            SchemaRepository.this.setDefaultSchema(schema);
            return false;
        }
        
        @Override
        public boolean visit(final SQLDropIndexStatement x) {
            SchemaRepository.this.acceptDropIndex(x);
            return false;
        }
    }
    
    public class OracleConsoleSchemaVisitor extends OracleASTVisitorAdapter
    {
        @Override
        public boolean visit(final SQLDropSequenceStatement x) {
            SchemaRepository.this.acceptDropSequence(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateSequenceStatement x) {
            SchemaRepository.this.acceptCreateSequence(x);
            return false;
        }
        
        @Override
        public boolean visit(final OracleCreateTableStatement x) {
            this.visit((SQLCreateTableStatement)x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateTableStatement x) {
            SchemaRepository.this.acceptCreateTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLDropTableStatement x) {
            SchemaRepository.this.acceptDropTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateViewStatement x) {
            SchemaRepository.this.acceptView(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLAlterViewStatement x) {
            SchemaRepository.this.acceptView(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateIndexStatement x) {
            SchemaRepository.this.acceptCreateIndex(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateFunctionStatement x) {
            SchemaRepository.this.acceptCreateFunction(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLAlterTableStatement x) {
            SchemaRepository.this.acceptAlterTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLUseStatement x) {
            final String schema = x.getDatabase().getSimpleName();
            SchemaRepository.this.setDefaultSchema(schema);
            return false;
        }
        
        @Override
        public boolean visit(final SQLDropIndexStatement x) {
            SchemaRepository.this.acceptDropIndex(x);
            return false;
        }
    }
    
    public class DefaultConsoleSchemaVisitor extends SQLASTVisitorAdapter
    {
        @Override
        public boolean visit(final SQLDropSequenceStatement x) {
            SchemaRepository.this.acceptDropSequence(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateSequenceStatement x) {
            SchemaRepository.this.acceptCreateSequence(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateTableStatement x) {
            SchemaRepository.this.acceptCreateTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final HiveCreateTableStatement x) {
            SchemaRepository.this.acceptCreateTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLDropTableStatement x) {
            SchemaRepository.this.acceptDropTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateViewStatement x) {
            SchemaRepository.this.acceptView(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLAlterViewStatement x) {
            SchemaRepository.this.acceptView(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateIndexStatement x) {
            SchemaRepository.this.acceptCreateIndex(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLCreateFunctionStatement x) {
            SchemaRepository.this.acceptCreateFunction(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLAlterTableStatement x) {
            SchemaRepository.this.acceptAlterTable(x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLDropIndexStatement x) {
            SchemaRepository.this.acceptDropIndex(x);
            return false;
        }
    }
    
    public interface SchemaLoader
    {
        String loadDDL(final String p0, final String p1, final String p2);
    }
}
