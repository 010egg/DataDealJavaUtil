// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.sql.Array;
import java.util.List;
import org.postgresql.core.BaseStatement;
import java.util.StringTokenizer;
import java.util.ArrayList;
import org.postgresql.core.Field;
import org.postgresql.Driver;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.util.Map;

public abstract class AbstractJdbc2DatabaseMetaData
{
    private static final String keywords = "abort,acl,add,aggregate,append,archive,arch_store,backward,binary,boolean,change,cluster,copy,database,delimiter,delimiters,do,extend,explain,forward,heavy,index,inherits,isnull,light,listen,load,merge,nothing,notify,notnull,oids,purge,rename,replace,retrieve,returns,rule,recipe,setof,stdin,stdout,store,vacuum,verbose,version";
    protected final AbstractJdbc2Connection connection;
    private int NAMEDATALEN;
    private int INDEX_MAX_KEYS;
    private static final Map tableTypeClauses;
    
    public AbstractJdbc2DatabaseMetaData(final AbstractJdbc2Connection conn) {
        this.NAMEDATALEN = 0;
        this.INDEX_MAX_KEYS = 0;
        this.connection = conn;
    }
    
    protected int getMaxIndexKeys() throws SQLException {
        if (this.INDEX_MAX_KEYS == 0) {
            String sql;
            if (this.connection.haveMinimumServerVersion("8.0")) {
                sql = "SELECT setting FROM pg_catalog.pg_settings WHERE name='max_index_keys'";
            }
            else {
                String from;
                if (this.connection.haveMinimumServerVersion("7.3")) {
                    from = "pg_catalog.pg_namespace n, pg_catalog.pg_type t1, pg_catalog.pg_type t2 WHERE t1.typnamespace=n.oid AND n.nspname='pg_catalog' AND ";
                }
                else {
                    from = "pg_type t1, pg_type t2 WHERE ";
                }
                sql = "SELECT t1.typlen/t2.typlen FROM " + from + " t1.typelem=t2.oid AND t1.typname='oidvector'";
            }
            final Statement stmt = this.connection.createStatement();
            final ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                stmt.close();
                throw new PSQLException(GT.tr("Unable to determine a value for MaxIndexKeys due to missing system catalog data."), PSQLState.UNEXPECTED_ERROR);
            }
            this.INDEX_MAX_KEYS = rs.getInt(1);
            rs.close();
            stmt.close();
        }
        return this.INDEX_MAX_KEYS;
    }
    
    protected int getMaxNameLength() throws SQLException {
        if (this.NAMEDATALEN == 0) {
            String sql;
            if (this.connection.haveMinimumServerVersion("7.3")) {
                sql = "SELECT t.typlen FROM pg_catalog.pg_type t, pg_catalog.pg_namespace n WHERE t.typnamespace=n.oid AND t.typname='name' AND n.nspname='pg_catalog'";
            }
            else {
                sql = "SELECT typlen FROM pg_type WHERE typname='name'";
            }
            final Statement stmt = this.connection.createStatement();
            final ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                throw new PSQLException(GT.tr("Unable to find name datatype in the system catalogs."), PSQLState.UNEXPECTED_ERROR);
            }
            this.NAMEDATALEN = rs.getInt("typlen");
            rs.close();
            stmt.close();
        }
        return this.NAMEDATALEN - 1;
    }
    
    public boolean allProceduresAreCallable() throws SQLException {
        return true;
    }
    
    public boolean allTablesAreSelectable() throws SQLException {
        return true;
    }
    
    public String getURL() throws SQLException {
        return this.connection.getURL();
    }
    
    public String getUserName() throws SQLException {
        return this.connection.getUserName();
    }
    
    public boolean isReadOnly() throws SQLException {
        return this.connection.isReadOnly();
    }
    
    public boolean nullsAreSortedHigh() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.2");
    }
    
    public boolean nullsAreSortedLow() throws SQLException {
        return false;
    }
    
    public boolean nullsAreSortedAtStart() throws SQLException {
        return false;
    }
    
    public boolean nullsAreSortedAtEnd() throws SQLException {
        return !this.connection.haveMinimumServerVersion("7.2");
    }
    
    public String getDatabaseProductName() throws SQLException {
        return "PostgreSQL";
    }
    
    public String getDatabaseProductVersion() throws SQLException {
        return this.connection.getDBVersionNumber();
    }
    
    public String getDriverName() throws SQLException {
        return "PostgreSQL Native Driver";
    }
    
    public String getDriverVersion() throws SQLException {
        return Driver.getVersion();
    }
    
    public int getDriverMajorVersion() {
        return 9;
    }
    
    public int getDriverMinorVersion() {
        return 4;
    }
    
    public boolean usesLocalFiles() throws SQLException {
        return false;
    }
    
    public boolean usesLocalFilePerTable() throws SQLException {
        return false;
    }
    
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        return false;
    }
    
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        return false;
    }
    
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        return true;
    }
    
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        return false;
    }
    
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        return true;
    }
    
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        return false;
    }
    
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        return false;
    }
    
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        return false;
    }
    
    public String getIdentifierQuoteString() throws SQLException {
        return "\"";
    }
    
    public String getSQLKeywords() throws SQLException {
        return "abort,acl,add,aggregate,append,archive,arch_store,backward,binary,boolean,change,cluster,copy,database,delimiter,delimiters,do,extend,explain,forward,heavy,index,inherits,isnull,light,listen,load,merge,nothing,notify,notnull,oids,purge,rename,replace,retrieve,returns,rule,recipe,setof,stdin,stdout,store,vacuum,verbose,version";
    }
    
    public String getNumericFunctions() throws SQLException {
        return "abs,acos,asin,atan,atan2,ceiling,cos,cot,degrees,exp,floor,log,log10,mod,pi,power,radians,round,sign,sin,sqrt,tan,truncate";
    }
    
    public String getStringFunctions() throws SQLException {
        String funcs = "ascii,char,concat,lcase,left,length,ltrim,repeat,rtrim,space,substring,ucase";
        if (this.connection.haveMinimumServerVersion("7.3")) {
            funcs += ",replace";
        }
        return funcs;
    }
    
    public String getSystemFunctions() throws SQLException {
        if (this.connection.haveMinimumServerVersion("7.3")) {
            return "database,ifnull,user";
        }
        return "ifnull,user";
    }
    
    public String getTimeDateFunctions() throws SQLException {
        String timeDateFuncs = "curdate,curtime,dayname,dayofmonth,dayofweek,dayofyear,hour,minute,month,monthname,now,quarter,second,week,year";
        if (this.connection.haveMinimumServerVersion("8.0")) {
            timeDateFuncs += ",timestampadd";
        }
        return timeDateFuncs;
    }
    
    public String getSearchStringEscape() throws SQLException {
        return "\\";
    }
    
    public String getExtraNameCharacters() throws SQLException {
        return "";
    }
    
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        return true;
    }
    
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.3");
    }
    
    public boolean supportsColumnAliasing() throws SQLException {
        return true;
    }
    
    public boolean nullPlusNonNullIsNull() throws SQLException {
        return true;
    }
    
    public boolean supportsConvert() throws SQLException {
        return false;
    }
    
    public boolean supportsConvert(final int fromType, final int toType) throws SQLException {
        return false;
    }
    
    public boolean supportsTableCorrelationNames() throws SQLException {
        return true;
    }
    
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        return false;
    }
    
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        return true;
    }
    
    public boolean supportsOrderByUnrelated() throws SQLException {
        return this.connection.haveMinimumServerVersion("6.4");
    }
    
    public boolean supportsGroupBy() throws SQLException {
        return true;
    }
    
    public boolean supportsGroupByUnrelated() throws SQLException {
        return this.connection.haveMinimumServerVersion("6.4");
    }
    
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        return this.connection.haveMinimumServerVersion("6.4");
    }
    
    public boolean supportsLikeEscapeClause() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.1");
    }
    
    public boolean supportsMultipleResultSets() throws SQLException {
        return true;
    }
    
    public boolean supportsMultipleTransactions() throws SQLException {
        return true;
    }
    
    public boolean supportsNonNullableColumns() throws SQLException {
        return true;
    }
    
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        return true;
    }
    
    public boolean supportsCoreSQLGrammar() throws SQLException {
        return false;
    }
    
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        return false;
    }
    
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.3");
    }
    
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        return false;
    }
    
    public boolean supportsANSI92FullSQL() throws SQLException {
        return false;
    }
    
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        return true;
    }
    
    public boolean supportsOuterJoins() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.1");
    }
    
    public boolean supportsFullOuterJoins() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.1");
    }
    
    public boolean supportsLimitedOuterJoins() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.1");
    }
    
    public String getSchemaTerm() throws SQLException {
        return "schema";
    }
    
    public String getProcedureTerm() throws SQLException {
        return "function";
    }
    
    public String getCatalogTerm() throws SQLException {
        return "database";
    }
    
    public boolean isCatalogAtStart() throws SQLException {
        return true;
    }
    
    public String getCatalogSeparator() throws SQLException {
        return ".";
    }
    
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.3");
    }
    
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.3");
    }
    
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.3");
    }
    
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.3");
    }
    
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.3");
    }
    
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        return false;
    }
    
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        return false;
    }
    
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        return false;
    }
    
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        return false;
    }
    
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        return false;
    }
    
    public boolean supportsPositionedDelete() throws SQLException {
        return false;
    }
    
    public boolean supportsPositionedUpdate() throws SQLException {
        return false;
    }
    
    public boolean supportsSelectForUpdate() throws SQLException {
        return this.connection.haveMinimumServerVersion("6.5");
    }
    
    public boolean supportsStoredProcedures() throws SQLException {
        return true;
    }
    
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        return true;
    }
    
    public boolean supportsSubqueriesInExists() throws SQLException {
        return true;
    }
    
    public boolean supportsSubqueriesInIns() throws SQLException {
        return true;
    }
    
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        return true;
    }
    
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.1");
    }
    
    public boolean supportsUnion() throws SQLException {
        return true;
    }
    
    public boolean supportsUnionAll() throws SQLException {
        return this.connection.haveMinimumServerVersion("7.1");
    }
    
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        return false;
    }
    
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        return false;
    }
    
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        return true;
    }
    
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        return true;
    }
    
    public int getMaxBinaryLiteralLength() throws SQLException {
        return 0;
    }
    
    public int getMaxCharLiteralLength() throws SQLException {
        return 0;
    }
    
    public int getMaxColumnNameLength() throws SQLException {
        return this.getMaxNameLength();
    }
    
    public int getMaxColumnsInGroupBy() throws SQLException {
        return 0;
    }
    
    public int getMaxColumnsInIndex() throws SQLException {
        return this.getMaxIndexKeys();
    }
    
    public int getMaxColumnsInOrderBy() throws SQLException {
        return 0;
    }
    
    public int getMaxColumnsInSelect() throws SQLException {
        return 0;
    }
    
    public int getMaxColumnsInTable() throws SQLException {
        return 1600;
    }
    
    public int getMaxConnections() throws SQLException {
        return 8192;
    }
    
    public int getMaxCursorNameLength() throws SQLException {
        return this.getMaxNameLength();
    }
    
    public int getMaxIndexLength() throws SQLException {
        return 0;
    }
    
    public int getMaxSchemaNameLength() throws SQLException {
        return this.getMaxNameLength();
    }
    
    public int getMaxProcedureNameLength() throws SQLException {
        return this.getMaxNameLength();
    }
    
    public int getMaxCatalogNameLength() throws SQLException {
        return this.getMaxNameLength();
    }
    
    public int getMaxRowSize() throws SQLException {
        if (this.connection.haveMinimumServerVersion("7.1")) {
            return 1073741824;
        }
        return 8192;
    }
    
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        return false;
    }
    
    public int getMaxStatementLength() throws SQLException {
        if (this.connection.haveMinimumServerVersion("7.0")) {
            return 0;
        }
        return 16384;
    }
    
    public int getMaxStatements() throws SQLException {
        return 0;
    }
    
    public int getMaxTableNameLength() throws SQLException {
        return this.getMaxNameLength();
    }
    
    public int getMaxTablesInSelect() throws SQLException {
        return 0;
    }
    
    public int getMaxUserNameLength() throws SQLException {
        return this.getMaxNameLength();
    }
    
    public int getDefaultTransactionIsolation() throws SQLException {
        return 2;
    }
    
    public boolean supportsTransactions() throws SQLException {
        return true;
    }
    
    public boolean supportsTransactionIsolationLevel(final int level) throws SQLException {
        return level == 8 || level == 2 || (this.connection.haveMinimumServerVersion("8.0") && (level == 1 || level == 4));
    }
    
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        return true;
    }
    
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        return false;
    }
    
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        return false;
    }
    
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        return false;
    }
    
    protected String escapeQuotes(final String s) throws SQLException {
        final StringBuilder sb = new StringBuilder();
        if (!this.connection.getStandardConformingStrings() && this.connection.haveMinimumServerVersion("8.1")) {
            sb.append("E");
        }
        sb.append("'");
        sb.append(this.connection.escapeString(s));
        sb.append("'");
        return sb.toString();
    }
    
    public ResultSet getProcedures(final String catalog, final String schemaPattern, final String procedureNamePattern) throws SQLException {
        return this.getProcedures(2, catalog, schemaPattern, procedureNamePattern);
    }
    
    protected ResultSet getProcedures(final int jdbcVersion, final String catalog, final String schemaPattern, final String procedureNamePattern) throws SQLException {
        String sql;
        if (this.connection.haveMinimumServerVersion("7.3")) {
            sql = "SELECT NULL AS PROCEDURE_CAT, n.nspname AS PROCEDURE_SCHEM, p.proname AS PROCEDURE_NAME, NULL, NULL, NULL, d.description AS REMARKS, 2 AS PROCEDURE_TYPE ";
            if (jdbcVersion >= 4) {
                sql += ", p.proname || '_' || p.oid AS SPECIFIC_NAME ";
            }
            sql += " FROM pg_catalog.pg_namespace n, pg_catalog.pg_proc p  LEFT JOIN pg_catalog.pg_description d ON (p.oid=d.objoid)  LEFT JOIN pg_catalog.pg_class c ON (d.classoid=c.oid AND c.relname='pg_proc')  LEFT JOIN pg_catalog.pg_namespace pn ON (c.relnamespace=pn.oid AND pn.nspname='pg_catalog')  WHERE p.pronamespace=n.oid ";
            if (schemaPattern != null && !"".equals(schemaPattern)) {
                sql = sql + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
            }
            if (procedureNamePattern != null) {
                sql = sql + " AND p.proname LIKE " + this.escapeQuotes(procedureNamePattern);
            }
            sql += " ORDER BY PROCEDURE_SCHEM, PROCEDURE_NAME, p.oid::text ";
        }
        else if (this.connection.haveMinimumServerVersion("7.1")) {
            sql = "SELECT NULL AS PROCEDURE_CAT, NULL AS PROCEDURE_SCHEM, p.proname AS PROCEDURE_NAME, NULL, NULL, NULL, d.description AS REMARKS, 2 AS PROCEDURE_TYPE ";
            if (jdbcVersion >= 4) {
                sql += ", p.proname || '_' || p.oid AS SPECIFIC_NAME ";
            }
            sql += " FROM pg_proc p  LEFT JOIN pg_description d ON (p.oid=d.objoid) ";
            if (this.connection.haveMinimumServerVersion("7.2")) {
                sql += " LEFT JOIN pg_class c ON (d.classoid=c.oid AND c.relname='pg_proc') ";
            }
            if (procedureNamePattern != null) {
                sql = sql + " WHERE p.proname LIKE " + this.escapeQuotes(procedureNamePattern);
            }
            sql += " ORDER BY PROCEDURE_NAME, p.oid::text ";
        }
        else {
            sql = "SELECT NULL AS PROCEDURE_CAT, NULL AS PROCEDURE_SCHEM, p.proname AS PROCEDURE_NAME, NULL, NULL, NULL, NULL AS REMARKS, 2 AS PROCEDURE_TYPE ";
            if (jdbcVersion >= 4) {
                sql += ", p.proname || '_' || p.oid AS SPECIFIC_NAME ";
            }
            sql += " FROM pg_proc p ";
            if (procedureNamePattern != null) {
                sql = sql + " WHERE p.proname LIKE " + this.escapeQuotes(procedureNamePattern);
            }
            sql += " ORDER BY PROCEDURE_NAME, p.oid::text ";
        }
        return this.createMetaDataStatement().executeQuery(sql);
    }
    
    public ResultSet getProcedureColumns(final String catalog, final String schemaPattern, final String procedureNamePattern, final String columnNamePattern) throws SQLException {
        return this.getProcedureColumns(2, catalog, schemaPattern, procedureNamePattern, columnNamePattern);
    }
    
    protected ResultSet getProcedureColumns(final int jdbcVersion, final String catalog, final String schemaPattern, final String procedureNamePattern, final String columnNamePattern) throws SQLException {
        int columns = 13;
        if (jdbcVersion >= 4) {
            columns += 7;
        }
        final Field[] f = new Field[columns];
        final List v = new ArrayList();
        f[0] = new Field("PROCEDURE_CAT", 1043);
        f[1] = new Field("PROCEDURE_SCHEM", 1043);
        f[2] = new Field("PROCEDURE_NAME", 1043);
        f[3] = new Field("COLUMN_NAME", 1043);
        f[4] = new Field("COLUMN_TYPE", 21);
        f[5] = new Field("DATA_TYPE", 21);
        f[6] = new Field("TYPE_NAME", 1043);
        f[7] = new Field("PRECISION", 23);
        f[8] = new Field("LENGTH", 23);
        f[9] = new Field("SCALE", 21);
        f[10] = new Field("RADIX", 21);
        f[11] = new Field("NULLABLE", 21);
        f[12] = new Field("REMARKS", 1043);
        if (jdbcVersion >= 4) {
            f[13] = new Field("COLUMN_DEF", 1043);
            f[14] = new Field("SQL_DATA_TYPE", 23);
            f[15] = new Field("SQL_DATETIME_SUB", 23);
            f[16] = new Field("CHAR_OCTECT_LENGTH", 23);
            f[17] = new Field("ORDINAL_POSITION", 23);
            f[18] = new Field("IS_NULLABLE", 1043);
            f[19] = new Field("SPECIFIC_NAME", 1043);
        }
        String sql;
        if (this.connection.haveMinimumServerVersion("7.3")) {
            sql = "SELECT n.nspname,p.proname,p.prorettype,p.proargtypes, t.typtype,t.typrelid ";
            if (this.connection.haveMinimumServerVersion("8.1")) {
                sql += ", p.proargnames, p.proargmodes, p.proallargtypes  ";
            }
            else if (this.connection.haveMinimumServerVersion("8.0")) {
                sql += ", p.proargnames, NULL AS proargmodes, NULL AS proallargtypes ";
            }
            else {
                sql += ", NULL AS proargnames, NULL AS proargmodes, NULL AS proallargtypes ";
            }
            sql += ", p.oid  FROM pg_catalog.pg_proc p, pg_catalog.pg_namespace n, pg_catalog.pg_type t  WHERE p.pronamespace=n.oid AND p.prorettype=t.oid ";
            if (schemaPattern != null && !"".equals(schemaPattern)) {
                sql = sql + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
            }
            if (procedureNamePattern != null) {
                sql = sql + " AND p.proname LIKE " + this.escapeQuotes(procedureNamePattern);
            }
            sql += " ORDER BY n.nspname, p.proname, p.oid::text ";
        }
        else {
            sql = "SELECT NULL AS nspname,p.proname,p.prorettype,p.proargtypes,t.typtype,t.typrelid, NULL AS proargnames, NULL AS proargmodes, NULL AS proallargtypes, p.oid  FROM pg_proc p,pg_type t  WHERE p.prorettype=t.oid ";
            if (procedureNamePattern != null) {
                sql = sql + " AND p.proname LIKE " + this.escapeQuotes(procedureNamePattern);
            }
            sql += " ORDER BY p.proname, p.oid::text ";
        }
        final byte[] isnullableUnknown = new byte[0];
        final Statement stmt = this.connection.createStatement();
        final ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            final byte[] schema = rs.getBytes("nspname");
            final byte[] procedureName = rs.getBytes("proname");
            final byte[] specificName = this.connection.encodeString(rs.getString("proname") + "_" + rs.getString("oid"));
            final int returnType = (int)rs.getLong("prorettype");
            final String returnTypeType = rs.getString("typtype");
            final int returnTypeRelid = (int)rs.getLong("typrelid");
            final String strArgTypes = rs.getString("proargtypes");
            final StringTokenizer st = new StringTokenizer(strArgTypes);
            final List argTypes = new ArrayList();
            while (st.hasMoreTokens()) {
                argTypes.add(new Long(st.nextToken()));
            }
            String[] argNames = null;
            final Array argNamesArray = rs.getArray("proargnames");
            if (argNamesArray != null) {
                argNames = (String[])argNamesArray.getArray();
            }
            String[] argModes = null;
            final Array argModesArray = rs.getArray("proargmodes");
            if (argModesArray != null) {
                argModes = (String[])argModesArray.getArray();
            }
            int numArgs = argTypes.size();
            Long[] allArgTypes = null;
            final Array allArgTypesArray = rs.getArray("proallargtypes");
            if (allArgTypesArray != null) {
                if (this.connection.haveMinimumCompatibleVersion("8.3")) {
                    allArgTypes = (Long[])allArgTypesArray.getArray();
                }
                else {
                    final long[] tempAllArgTypes = (long[])allArgTypesArray.getArray();
                    allArgTypes = new Long[tempAllArgTypes.length];
                    for (int i = 0; i < tempAllArgTypes.length; ++i) {
                        allArgTypes[i] = new Long(tempAllArgTypes[i]);
                    }
                }
                numArgs = allArgTypes.length;
            }
            if (returnTypeType.equals("b") || returnTypeType.equals("d") || returnTypeType.equals("e") || (returnTypeType.equals("p") && argModesArray == null)) {
                final byte[][] tuple = new byte[columns][];
                tuple[0] = null;
                tuple[1] = schema;
                tuple[2] = procedureName;
                tuple[3] = this.connection.encodeString("returnValue");
                tuple[4] = this.connection.encodeString(Integer.toString(5));
                tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(returnType)));
                tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(returnType));
                tuple[8] = (tuple[7] = null);
                tuple[10] = (tuple[9] = null);
                tuple[11] = this.connection.encodeString(Integer.toString(2));
                tuple[12] = null;
                if (jdbcVersion >= 4) {
                    tuple[17] = this.connection.encodeString(Integer.toString(0));
                    tuple[18] = isnullableUnknown;
                    tuple[19] = specificName;
                }
                v.add(tuple);
            }
            for (int j = 0; j < numArgs; ++j) {
                final byte[][] tuple2 = new byte[columns][];
                tuple2[0] = null;
                tuple2[1] = schema;
                tuple2[2] = procedureName;
                if (argNames != null) {
                    tuple2[3] = this.connection.encodeString(argNames[j]);
                }
                else {
                    tuple2[3] = this.connection.encodeString("$" + (j + 1));
                }
                int columnMode = 1;
                if (argModes != null && argModes[j].equals("o")) {
                    columnMode = 4;
                }
                else if (argModes != null && argModes[j].equals("b")) {
                    columnMode = 2;
                }
                else if (argModes != null && argModes[j].equals("t")) {
                    columnMode = 5;
                }
                tuple2[4] = this.connection.encodeString(Integer.toString(columnMode));
                int argOid;
                if (allArgTypes != null) {
                    argOid = allArgTypes[j].intValue();
                }
                else {
                    argOid = argTypes.get(j).intValue();
                }
                tuple2[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(argOid)));
                tuple2[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(argOid));
                tuple2[8] = (tuple2[7] = null);
                tuple2[10] = (tuple2[9] = null);
                tuple2[11] = this.connection.encodeString(Integer.toString(2));
                tuple2[12] = null;
                if (jdbcVersion >= 4) {
                    tuple2[17] = this.connection.encodeString(Integer.toString(j + 1));
                    tuple2[18] = isnullableUnknown;
                    tuple2[19] = specificName;
                }
                v.add(tuple2);
            }
            if (returnTypeType.equals("c") || (returnTypeType.equals("p") && argModesArray != null)) {
                String columnsql = "SELECT a.attname,a.atttypid FROM ";
                if (this.connection.haveMinimumServerVersion("7.3")) {
                    columnsql += "pg_catalog.";
                }
                columnsql = columnsql + "pg_attribute a WHERE a.attrelid = " + returnTypeRelid + " AND a.attnum > 0 ORDER BY a.attnum ";
                final Statement columnstmt = this.connection.createStatement();
                final ResultSet columnrs = columnstmt.executeQuery(columnsql);
                while (columnrs.next()) {
                    final int columnTypeOid = (int)columnrs.getLong("atttypid");
                    final byte[][] tuple3 = new byte[columns][];
                    tuple3[0] = null;
                    tuple3[1] = schema;
                    tuple3[2] = procedureName;
                    tuple3[3] = columnrs.getBytes("attname");
                    tuple3[4] = this.connection.encodeString(Integer.toString(3));
                    tuple3[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(columnTypeOid)));
                    tuple3[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(columnTypeOid));
                    tuple3[8] = (tuple3[7] = null);
                    tuple3[10] = (tuple3[9] = null);
                    tuple3[11] = this.connection.encodeString(Integer.toString(2));
                    tuple3[12] = null;
                    if (jdbcVersion >= 4) {
                        tuple3[17] = this.connection.encodeString(Integer.toString(0));
                        tuple3[18] = isnullableUnknown;
                        tuple3[19] = specificName;
                    }
                    v.add(tuple3);
                }
                columnrs.close();
                columnstmt.close();
            }
        }
        rs.close();
        stmt.close();
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
    }
    
    public ResultSet getTables(final String catalog, final String schemaPattern, final String tableNamePattern, final String[] types) throws SQLException {
        String useSchemas;
        String select;
        String orderby;
        if (this.connection.haveMinimumServerVersion("7.3")) {
            useSchemas = "SCHEMAS";
            select = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM, c.relname AS TABLE_NAME,  CASE n.nspname ~ '^pg_' OR n.nspname = 'information_schema'  WHEN true THEN CASE  WHEN n.nspname = 'pg_catalog' OR n.nspname = 'information_schema' THEN CASE c.relkind   WHEN 'r' THEN 'SYSTEM TABLE'   WHEN 'v' THEN 'SYSTEM VIEW'   WHEN 'i' THEN 'SYSTEM INDEX'   ELSE NULL   END  WHEN n.nspname = 'pg_toast' THEN CASE c.relkind   WHEN 'r' THEN 'SYSTEM TOAST TABLE'   WHEN 'i' THEN 'SYSTEM TOAST INDEX'   ELSE NULL   END  ELSE CASE c.relkind   WHEN 'r' THEN 'TEMPORARY TABLE'   WHEN 'i' THEN 'TEMPORARY INDEX'   WHEN 'S' THEN 'TEMPORARY SEQUENCE'   WHEN 'v' THEN 'TEMPORARY VIEW'   ELSE NULL   END  END  WHEN false THEN CASE c.relkind  WHEN 'r' THEN 'TABLE'  WHEN 'i' THEN 'INDEX'  WHEN 'S' THEN 'SEQUENCE'  WHEN 'v' THEN 'VIEW'  WHEN 'c' THEN 'TYPE'  WHEN 'f' THEN 'FOREIGN TABLE'  WHEN 'm' THEN 'MATERIALIZED VIEW'  ELSE NULL  END  ELSE NULL  END  AS TABLE_TYPE, d.description AS REMARKS  FROM pg_catalog.pg_namespace n, pg_catalog.pg_class c  LEFT JOIN pg_catalog.pg_description d ON (c.oid = d.objoid AND d.objsubid = 0)  LEFT JOIN pg_catalog.pg_class dc ON (d.classoid=dc.oid AND dc.relname='pg_class')  LEFT JOIN pg_catalog.pg_namespace dn ON (dn.oid=dc.relnamespace AND dn.nspname='pg_catalog')  WHERE c.relnamespace = n.oid ";
            if (schemaPattern != null && !"".equals(schemaPattern)) {
                select = select + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
            }
            orderby = " ORDER BY TABLE_TYPE,TABLE_SCHEM,TABLE_NAME ";
        }
        else {
            useSchemas = "NOSCHEMAS";
            final String tableType = " CASE c.relname ~ '^pg_'  WHEN true THEN CASE c.relname ~ '^pg_toast_'  WHEN true THEN CASE c.relkind   WHEN 'r' THEN 'SYSTEM TOAST TABLE'   WHEN 'i' THEN 'SYSTEM TOAST INDEX'   ELSE NULL   END  WHEN false THEN CASE c.relname ~ '^pg_temp_'   WHEN true THEN CASE c.relkind    WHEN 'r' THEN 'TEMPORARY TABLE'    WHEN 'i' THEN 'TEMPORARY INDEX'    WHEN 'S' THEN 'TEMPORARY SEQUENCE'    WHEN 'v' THEN 'TEMPORARY VIEW'    ELSE NULL    END   WHEN false THEN CASE c.relkind    WHEN 'r' THEN 'SYSTEM TABLE'    WHEN 'v' THEN 'SYSTEM VIEW'    WHEN 'i' THEN 'SYSTEM INDEX'    ELSE NULL    END   ELSE NULL   END  ELSE NULL  END  WHEN false THEN CASE c.relkind  WHEN 'r' THEN 'TABLE'  WHEN 'i' THEN 'INDEX'  WHEN 'S' THEN 'SEQUENCE'  WHEN 'v' THEN 'VIEW'  WHEN 'c' THEN 'TYPE'  ELSE NULL  END  ELSE NULL  END ";
            orderby = " ORDER BY TABLE_TYPE,TABLE_NAME ";
            if (this.connection.haveMinimumServerVersion("7.2")) {
                select = "SELECT NULL AS TABLE_CAT, NULL AS TABLE_SCHEM, c.relname AS TABLE_NAME, " + tableType + " AS TABLE_TYPE, d.description AS REMARKS " + " FROM pg_class c " + " LEFT JOIN pg_description d ON (c.oid=d.objoid AND d.objsubid = 0) " + " LEFT JOIN pg_class dc ON (d.classoid = dc.oid AND dc.relname='pg_class') " + " WHERE true ";
            }
            else if (this.connection.haveMinimumServerVersion("7.1")) {
                select = "SELECT NULL AS TABLE_CAT, NULL AS TABLE_SCHEM, c.relname AS TABLE_NAME, " + tableType + " AS TABLE_TYPE, d.description AS REMARKS " + " FROM pg_class c " + " LEFT JOIN pg_description d ON (c.oid=d.objoid) " + " WHERE true ";
            }
            else {
                select = "SELECT NULL AS TABLE_CAT, NULL AS TABLE_SCHEM, c.relname AS TABLE_NAME, " + tableType + " AS TABLE_TYPE, NULL AS REMARKS " + " FROM pg_class c " + " WHERE true ";
            }
        }
        if (tableNamePattern != null && !"".equals(tableNamePattern)) {
            select = select + " AND c.relname LIKE " + this.escapeQuotes(tableNamePattern);
        }
        if (types != null) {
            select += " AND (false ";
            for (int i = 0; i < types.length; ++i) {
                final Map clauses = AbstractJdbc2DatabaseMetaData.tableTypeClauses.get(types[i]);
                if (clauses != null) {
                    final String clause = clauses.get(useSchemas);
                    select = select + " OR ( " + clause + " ) ";
                }
            }
            select += ") ";
        }
        final String sql = select + orderby;
        return this.createMetaDataStatement().executeQuery(sql);
    }
    
    public ResultSet getSchemas() throws SQLException {
        return this.getSchemas(2, null, null);
    }
    
    protected ResultSet getSchemas(final int jdbcVersion, final String catalog, final String schemaPattern) throws SQLException {
        String sql;
        if (this.connection.haveMinimumServerVersion("7.3")) {
            String tempSchema = "substring(textin(array_out(pg_catalog.current_schemas(true))) from '{(pg_temp_[0-9]+),')";
            if (this.connection.haveMinimumServerVersion("7.4")) {
                tempSchema = "(pg_catalog.current_schemas(true))[1]";
            }
            sql = "SELECT nspname AS TABLE_SCHEM ";
            if (jdbcVersion >= 3) {
                sql += ", NULL AS TABLE_CATALOG ";
            }
            sql = sql + " FROM pg_catalog.pg_namespace WHERE nspname <> 'pg_toast' AND (nspname !~ '^pg_temp_' OR nspname = " + tempSchema + ") AND (nspname !~ '^pg_toast_temp_' OR nspname = replace(" + tempSchema + ", 'pg_temp_', 'pg_toast_temp_')) ";
            if (schemaPattern != null && !"".equals(schemaPattern)) {
                sql = sql + " AND nspname LIKE " + this.escapeQuotes(schemaPattern);
            }
            sql += " ORDER BY TABLE_SCHEM";
        }
        else {
            sql = "SELECT ''::text AS TABLE_SCHEM ";
            if (jdbcVersion >= 3) {
                sql += ", NULL AS TABLE_CATALOG ";
            }
            if (schemaPattern != null) {
                sql = sql + " WHERE ''::text LIKE " + this.escapeQuotes(schemaPattern);
            }
        }
        return this.createMetaDataStatement().executeQuery(sql);
    }
    
    public ResultSet getCatalogs() throws SQLException {
        final Field[] f = { null };
        final List v = new ArrayList();
        f[0] = new Field("TABLE_CAT", 1043);
        final byte[][] tuple = { this.connection.encodeString(this.connection.getCatalog()) };
        v.add(tuple);
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
    }
    
    public ResultSet getTableTypes() throws SQLException {
        final String[] types = new String[AbstractJdbc2DatabaseMetaData.tableTypeClauses.size()];
        final Iterator e = AbstractJdbc2DatabaseMetaData.tableTypeClauses.keySet().iterator();
        int i = 0;
        while (e.hasNext()) {
            types[i++] = e.next();
        }
        sortStringArray(types);
        final Field[] f = { null };
        final List v = new ArrayList();
        f[0] = new Field("TABLE_TYPE", 1043);
        for (i = 0; i < types.length; ++i) {
            final byte[][] tuple = { this.connection.encodeString(types[i]) };
            v.add(tuple);
        }
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
    }
    
    protected ResultSet getColumns(final int jdbcVersion, final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        int numberOfFields;
        if (jdbcVersion >= 4) {
            numberOfFields = 23;
        }
        else if (jdbcVersion >= 3) {
            numberOfFields = 22;
        }
        else {
            numberOfFields = 18;
        }
        final List v = new ArrayList();
        final Field[] f = new Field[numberOfFields];
        f[0] = new Field("TABLE_CAT", 1043);
        f[1] = new Field("TABLE_SCHEM", 1043);
        f[2] = new Field("TABLE_NAME", 1043);
        f[3] = new Field("COLUMN_NAME", 1043);
        f[4] = new Field("DATA_TYPE", 21);
        f[5] = new Field("TYPE_NAME", 1043);
        f[6] = new Field("COLUMN_SIZE", 23);
        f[7] = new Field("BUFFER_LENGTH", 1043);
        f[8] = new Field("DECIMAL_DIGITS", 23);
        f[9] = new Field("NUM_PREC_RADIX", 23);
        f[10] = new Field("NULLABLE", 23);
        f[11] = new Field("REMARKS", 1043);
        f[12] = new Field("COLUMN_DEF", 1043);
        f[13] = new Field("SQL_DATA_TYPE", 23);
        f[14] = new Field("SQL_DATETIME_SUB", 23);
        f[15] = new Field("CHAR_OCTET_LENGTH", 1043);
        f[16] = new Field("ORDINAL_POSITION", 23);
        f[17] = new Field("IS_NULLABLE", 1043);
        if (jdbcVersion >= 3) {
            f[18] = new Field("SCOPE_CATLOG", 1043);
            f[19] = new Field("SCOPE_SCHEMA", 1043);
            f[20] = new Field("SCOPE_TABLE", 1043);
            f[21] = new Field("SOURCE_DATA_TYPE", 21);
        }
        if (jdbcVersion >= 4) {
            f[22] = new Field("IS_AUTOINCREMENT", 1043);
        }
        String sql;
        if (this.connection.haveMinimumServerVersion("7.3")) {
            if (this.connection.haveMinimumServerVersion("8.4")) {
                sql = "SELECT * FROM (";
            }
            else {
                sql = "";
            }
            sql += "SELECT n.nspname,c.relname,a.attname,a.atttypid,a.attnotnull OR (t.typtype = 'd' AND t.typnotnull) AS attnotnull,a.atttypmod,a.attlen,";
            if (this.connection.haveMinimumServerVersion("8.4")) {
                sql += "row_number() OVER (PARTITION BY a.attrelid ORDER BY a.attnum) AS attnum, ";
            }
            else {
                sql += "a.attnum,";
            }
            sql += "pg_catalog.pg_get_expr(def.adbin, def.adrelid) AS adsrc,dsc.description,t.typbasetype,t.typtype  FROM pg_catalog.pg_namespace n  JOIN pg_catalog.pg_class c ON (c.relnamespace = n.oid)  JOIN pg_catalog.pg_attribute a ON (a.attrelid=c.oid)  JOIN pg_catalog.pg_type t ON (a.atttypid = t.oid)  LEFT JOIN pg_catalog.pg_attrdef def ON (a.attrelid=def.adrelid AND a.attnum = def.adnum)  LEFT JOIN pg_catalog.pg_description dsc ON (c.oid=dsc.objoid AND a.attnum = dsc.objsubid)  LEFT JOIN pg_catalog.pg_class dc ON (dc.oid=dsc.classoid AND dc.relname='pg_class')  LEFT JOIN pg_catalog.pg_namespace dn ON (dc.relnamespace=dn.oid AND dn.nspname='pg_catalog')  WHERE a.attnum > 0 AND NOT a.attisdropped ";
            if (schemaPattern != null && !"".equals(schemaPattern)) {
                sql = sql + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
            }
            if (tableNamePattern != null && !"".equals(tableNamePattern)) {
                sql = sql + " AND c.relname LIKE " + this.escapeQuotes(tableNamePattern);
            }
            if (this.connection.haveMinimumServerVersion("8.4")) {
                sql += ") c WHERE true ";
            }
        }
        else if (this.connection.haveMinimumServerVersion("7.2")) {
            sql = "SELECT NULL::text AS nspname,c.relname,a.attname,a.atttypid,a.attnotnull,a.atttypmod,a.attlen,a.attnum,pg_get_expr(def.adbin,def.adrelid) AS adsrc,dsc.description,NULL::oid AS typbasetype,t.typtype  FROM pg_class c  JOIN pg_attribute a ON (a.attrelid=c.oid)  JOIN pg_type t ON (a.atttypid = t.oid)  LEFT JOIN pg_attrdef def ON (a.attrelid=def.adrelid AND a.attnum = def.adnum)  LEFT JOIN pg_description dsc ON (c.oid=dsc.objoid AND a.attnum = dsc.objsubid)  LEFT JOIN pg_class dc ON (dc.oid=dsc.classoid AND dc.relname='pg_class')  WHERE a.attnum > 0 ";
        }
        else if (this.connection.haveMinimumServerVersion("7.1")) {
            sql = "SELECT NULL::text AS nspname,c.relname,a.attname,a.atttypid,a.attnotnull,a.atttypmod,a.attlen,a.attnum,def.adsrc,dsc.description,NULL::oid AS typbasetype, 'b' AS typtype   FROM pg_class c  JOIN pg_attribute a ON (a.attrelid=c.oid)  LEFT JOIN pg_attrdef def ON (a.attrelid=def.adrelid AND a.attnum = def.adnum)  LEFT JOIN pg_description dsc ON (a.oid=dsc.objoid)  WHERE a.attnum > 0 ";
        }
        else {
            sql = "SELECT NULL::text AS nspname,c.relname,a.attname,a.atttypid,a.attnotnull,a.atttypmod,a.attlen,a.attnum,NULL AS adsrc,NULL AS description,NULL AS typbasetype, 'b' AS typtype  FROM pg_class c, pg_attribute a  WHERE a.attrelid=c.oid AND a.attnum > 0 ";
        }
        if (!this.connection.haveMinimumServerVersion("7.3") && tableNamePattern != null && !"".equals(tableNamePattern)) {
            sql = sql + " AND c.relname LIKE " + this.escapeQuotes(tableNamePattern);
        }
        if (columnNamePattern != null && !"".equals(columnNamePattern)) {
            sql = sql + " AND attname LIKE " + this.escapeQuotes(columnNamePattern);
        }
        sql += " ORDER BY nspname,c.relname,attnum ";
        final Statement stmt = this.connection.createStatement();
        final ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            final byte[][] tuple = new byte[numberOfFields][];
            final int typeOid = (int)rs.getLong("atttypid");
            final int typeMod = rs.getInt("atttypmod");
            tuple[0] = null;
            tuple[1] = rs.getBytes("nspname");
            tuple[2] = rs.getBytes("relname");
            tuple[3] = rs.getBytes("attname");
            final String typtype = rs.getString("typtype");
            int sqlType;
            if ("c".equals(typtype)) {
                sqlType = 2002;
            }
            else if ("d".equals(typtype)) {
                sqlType = 2001;
            }
            else if ("e".equals(typtype)) {
                sqlType = 12;
            }
            else {
                sqlType = this.connection.getTypeInfo().getSQLType(typeOid);
            }
            tuple[4] = this.connection.encodeString(Integer.toString(sqlType));
            final String pgType = this.connection.getTypeInfo().getPGType(typeOid);
            tuple[5] = this.connection.encodeString(pgType);
            tuple[7] = null;
            final String defval = rs.getString("adsrc");
            if (defval != null) {
                if (pgType.equals("int4")) {
                    if (defval.indexOf("nextval(") != -1) {
                        tuple[5] = this.connection.encodeString("serial");
                    }
                }
                else if (pgType.equals("int8") && defval.indexOf("nextval(") != -1) {
                    tuple[5] = this.connection.encodeString("bigserial");
                }
            }
            final int decimalDigits = this.connection.getTypeInfo().getScale(typeOid, typeMod);
            int columnSize = this.connection.getTypeInfo().getPrecision(typeOid, typeMod);
            if (columnSize == 0) {
                columnSize = this.connection.getTypeInfo().getDisplaySize(typeOid, typeMod);
            }
            tuple[6] = this.connection.encodeString(Integer.toString(columnSize));
            tuple[8] = this.connection.encodeString(Integer.toString(decimalDigits));
            tuple[9] = this.connection.encodeString("10");
            if (pgType.equals("bit") || pgType.equals("varbit")) {
                tuple[9] = this.connection.encodeString("2");
            }
            tuple[10] = this.connection.encodeString(Integer.toString((int)(rs.getBoolean("attnotnull") ? 0 : 1)));
            tuple[11] = rs.getBytes("description");
            tuple[12] = rs.getBytes("adsrc");
            tuple[14] = (tuple[13] = null);
            tuple[15] = tuple[6];
            tuple[16] = this.connection.encodeString(String.valueOf(rs.getInt("attnum")));
            tuple[17] = this.connection.encodeString(rs.getBoolean("attnotnull") ? "NO" : "YES");
            if (jdbcVersion >= 3) {
                final int baseTypeOid = (int)rs.getLong("typbasetype");
                tuple[18] = null;
                tuple[20] = (tuple[19] = null);
                tuple[21] = (byte[])((baseTypeOid == 0) ? null : this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(baseTypeOid))));
            }
            if (jdbcVersion >= 4) {
                String autoinc = "NO";
                if (defval != null && defval.indexOf("nextval(") != -1) {
                    autoinc = "YES";
                }
                tuple[22] = this.connection.encodeString(autoinc);
            }
            v.add(tuple);
        }
        rs.close();
        stmt.close();
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
    }
    
    public ResultSet getColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        return this.getColumns(2, catalog, schemaPattern, tableNamePattern, columnNamePattern);
    }
    
    public ResultSet getColumnPrivileges(final String catalog, final String schema, String table, String columnNamePattern) throws SQLException {
        final Field[] f = new Field[8];
        final List v = new ArrayList();
        if (table == null) {
            table = "%";
        }
        if (columnNamePattern == null) {
            columnNamePattern = "%";
        }
        f[0] = new Field("TABLE_CAT", 1043);
        f[1] = new Field("TABLE_SCHEM", 1043);
        f[2] = new Field("TABLE_NAME", 1043);
        f[3] = new Field("COLUMN_NAME", 1043);
        f[4] = new Field("GRANTOR", 1043);
        f[5] = new Field("GRANTEE", 1043);
        f[6] = new Field("PRIVILEGE", 1043);
        f[7] = new Field("IS_GRANTABLE", 1043);
        String sql;
        if (this.connection.haveMinimumServerVersion("8.4")) {
            sql = "SELECT n.nspname,c.relname,r.rolname,c.relacl,a.attacl,a.attname  FROM pg_catalog.pg_namespace n, pg_catalog.pg_class c, pg_catalog.pg_roles r, pg_catalog.pg_attribute a  WHERE c.relnamespace = n.oid  AND c.relowner = r.oid  AND c.oid = a.attrelid  AND c.relkind = 'r'  AND a.attnum > 0 AND NOT a.attisdropped ";
            if (schema != null && !"".equals(schema)) {
                sql = sql + " AND n.nspname = " + this.escapeQuotes(schema);
            }
        }
        else if (this.connection.haveMinimumServerVersion("7.3")) {
            sql = "SELECT n.nspname,c.relname,r.rolname,c.relacl,a.attname  FROM pg_catalog.pg_namespace n, pg_catalog.pg_class c, pg_catalog.pg_roles r, pg_catalog.pg_attribute a  WHERE c.relnamespace = n.oid  AND c.relowner = r.oid  AND c.oid = a.attrelid  AND c.relkind = 'r'  AND a.attnum > 0 AND NOT a.attisdropped ";
            if (schema != null && !"".equals(schema)) {
                sql = sql + " AND n.nspname = " + this.escapeQuotes(schema);
            }
        }
        else {
            sql = "SELECT NULL::text AS nspname,c.relname,u.usename,c.relacl,a.attname FROM pg_class c, pg_user u,pg_attribute a  WHERE u.usesysid = c.relowner  AND c.oid = a.attrelid  AND a.attnum > 0  AND c.relkind = 'r' ";
        }
        sql = sql + " AND c.relname = " + this.escapeQuotes(table);
        if (columnNamePattern != null && !"".equals(columnNamePattern)) {
            sql = sql + " AND a.attname LIKE " + this.escapeQuotes(columnNamePattern);
        }
        sql += " ORDER BY attname ";
        final Statement stmt = this.connection.createStatement();
        final ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            final byte[] schemaName = rs.getBytes("nspname");
            final byte[] tableName = rs.getBytes("relname");
            final byte[] column = rs.getBytes("attname");
            final String owner = rs.getString("rolname");
            final String relAcl = rs.getString("relacl");
            final Map permissions = this.parseACL(relAcl, owner);
            if (this.connection.haveMinimumServerVersion("8.4")) {
                final String acl = rs.getString("attacl");
                final Map relPermissions = this.parseACL(acl, owner);
                permissions.putAll(relPermissions);
            }
            final String[] permNames = new String[permissions.size()];
            final Iterator e = permissions.keySet().iterator();
            int i = 0;
            while (e.hasNext()) {
                permNames[i++] = e.next();
            }
            sortStringArray(permNames);
            for (i = 0; i < permNames.length; ++i) {
                final byte[] privilege = this.connection.encodeString(permNames[i]);
                final Map grantees = permissions.get(permNames[i]);
                final String[] granteeUsers = new String[grantees.size()];
                final Iterator g = grantees.keySet().iterator();
                int k = 0;
                while (g.hasNext()) {
                    granteeUsers[k++] = g.next();
                }
                for (int j = 0; j < grantees.size(); ++j) {
                    final List grantor = grantees.get(granteeUsers[j]);
                    final String grantee = granteeUsers[j];
                    for (int l = 0; l < grantor.size(); ++l) {
                        final String[] grants = grantor.get(l);
                        final String grantable = owner.equals(grantee) ? "YES" : grants[1];
                        final byte[][] tuple = { null, schemaName, tableName, column, this.connection.encodeString(grants[0]), this.connection.encodeString(grantee), privilege, this.connection.encodeString(grantable) };
                        v.add(tuple);
                    }
                }
            }
        }
        rs.close();
        stmt.close();
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
    }
    
    public ResultSet getTablePrivileges(final String catalog, final String schemaPattern, final String tableNamePattern) throws SQLException {
        final Field[] f = new Field[7];
        final List v = new ArrayList();
        f[0] = new Field("TABLE_CAT", 1043);
        f[1] = new Field("TABLE_SCHEM", 1043);
        f[2] = new Field("TABLE_NAME", 1043);
        f[3] = new Field("GRANTOR", 1043);
        f[4] = new Field("GRANTEE", 1043);
        f[5] = new Field("PRIVILEGE", 1043);
        f[6] = new Field("IS_GRANTABLE", 1043);
        String sql;
        if (this.connection.haveMinimumServerVersion("7.3")) {
            sql = "SELECT n.nspname,c.relname,r.rolname,c.relacl  FROM pg_catalog.pg_namespace n, pg_catalog.pg_class c, pg_catalog.pg_roles r  WHERE c.relnamespace = n.oid  AND c.relowner = r.oid  AND c.relkind = 'r' ";
            if (schemaPattern != null && !"".equals(schemaPattern)) {
                sql = sql + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
            }
        }
        else {
            sql = "SELECT NULL::text AS nspname,c.relname,u.usename,c.relacl FROM pg_class c, pg_user u  WHERE u.usesysid = c.relowner  AND c.relkind = 'r' ";
        }
        if (tableNamePattern != null && !"".equals(tableNamePattern)) {
            sql = sql + " AND c.relname LIKE " + this.escapeQuotes(tableNamePattern);
        }
        sql += " ORDER BY nspname, relname ";
        final Statement stmt = this.connection.createStatement();
        final ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            final byte[] schema = rs.getBytes("nspname");
            final byte[] table = rs.getBytes("relname");
            final String owner = rs.getString("rolname");
            final String acl = rs.getString("relacl");
            final Map permissions = this.parseACL(acl, owner);
            final String[] permNames = new String[permissions.size()];
            final Iterator e = permissions.keySet().iterator();
            int i = 0;
            while (e.hasNext()) {
                permNames[i++] = e.next();
            }
            sortStringArray(permNames);
            for (i = 0; i < permNames.length; ++i) {
                final byte[] privilege = this.connection.encodeString(permNames[i]);
                final Map grantees = permissions.get(permNames[i]);
                final String[] granteeUsers = new String[grantees.size()];
                final Iterator g = grantees.keySet().iterator();
                int k = 0;
                while (g.hasNext()) {
                    granteeUsers[k++] = g.next();
                }
                for (int j = 0; j < granteeUsers.length; ++j) {
                    final List grants = grantees.get(granteeUsers[j]);
                    final String grantee = granteeUsers[j];
                    for (int l = 0; l < grants.size(); ++l) {
                        final String[] grantTuple = grants.get(l);
                        final String grantor = grantTuple[0].equals(null) ? owner : grantTuple[0];
                        final String grantable = owner.equals(grantee) ? "YES" : grantTuple[1];
                        final byte[][] tuple = { null, schema, table, this.connection.encodeString(grantor), this.connection.encodeString(grantee), privilege, this.connection.encodeString(grantable) };
                        v.add(tuple);
                    }
                }
            }
        }
        rs.close();
        stmt.close();
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
    }
    
    private static void sortStringArray(final String[] s) {
        for (int i = 0; i < s.length - 1; ++i) {
            for (int j = i + 1; j < s.length; ++j) {
                if (s[i].compareTo(s[j]) > 0) {
                    final String tmp = s[i];
                    s[i] = s[j];
                    s[j] = tmp;
                }
            }
        }
    }
    
    private static List parseACLArray(final String aclString) {
        final List acls = new ArrayList();
        if (aclString == null || aclString.length() == 0) {
            return acls;
        }
        boolean inQuotes = false;
        int beginIndex = 1;
        char prevChar = ' ';
        for (int i = beginIndex; i < aclString.length(); ++i) {
            final char c = aclString.charAt(i);
            if (c == '\"' && prevChar != '\\') {
                inQuotes = !inQuotes;
            }
            else if (c == ',' && !inQuotes) {
                acls.add(aclString.substring(beginIndex, i));
                beginIndex = i + 1;
            }
            prevChar = c;
        }
        acls.add(aclString.substring(beginIndex, aclString.length() - 1));
        for (int i = 0; i < acls.size(); ++i) {
            String acl = acls.get(i);
            if (acl.startsWith("\"") && acl.endsWith("\"")) {
                acl = acl.substring(1, acl.length() - 1);
                acls.set(i, acl);
            }
        }
        return acls;
    }
    
    private void addACLPrivileges(final String acl, final Map privileges) {
        final int equalIndex = acl.lastIndexOf("=");
        final int slashIndex = acl.lastIndexOf("/");
        if (equalIndex == -1) {
            return;
        }
        String user = acl.substring(0, equalIndex);
        String grantor = null;
        if (user.length() == 0) {
            user = "PUBLIC";
        }
        String privs;
        if (slashIndex != -1) {
            privs = acl.substring(equalIndex + 1, slashIndex);
            grantor = acl.substring(slashIndex + 1, acl.length());
        }
        else {
            privs = acl.substring(equalIndex + 1, acl.length());
        }
        for (int i = 0; i < privs.length(); ++i) {
            final char c = privs.charAt(i);
            if (c != '*') {
                String grantable;
                if (i < privs.length() - 1 && privs.charAt(i + 1) == '*') {
                    grantable = "YES";
                }
                else {
                    grantable = "NO";
                }
                String sqlpriv = null;
                switch (c) {
                    case 'a': {
                        sqlpriv = "INSERT";
                        break;
                    }
                    case 'r': {
                        sqlpriv = "SELECT";
                        break;
                    }
                    case 'w': {
                        sqlpriv = "UPDATE";
                        break;
                    }
                    case 'd': {
                        sqlpriv = "DELETE";
                        break;
                    }
                    case 'D': {
                        sqlpriv = "TRUNCATE";
                        break;
                    }
                    case 'R': {
                        sqlpriv = "RULE";
                        break;
                    }
                    case 'x': {
                        sqlpriv = "REFERENCES";
                        break;
                    }
                    case 't': {
                        sqlpriv = "TRIGGER";
                        break;
                    }
                    case 'X': {
                        sqlpriv = "EXECUTE";
                        break;
                    }
                    case 'U': {
                        sqlpriv = "USAGE";
                        break;
                    }
                    case 'C': {
                        sqlpriv = "CREATE";
                        break;
                    }
                    case 'T': {
                        sqlpriv = "CREATE TEMP";
                        break;
                    }
                    default: {
                        sqlpriv = "UNKNOWN";
                        break;
                    }
                }
                Map usersWithPermission = privileges.get(sqlpriv);
                final String[] grant = { grantor, grantable };
                if (usersWithPermission == null) {
                    usersWithPermission = new HashMap();
                    final List permissionByGrantor = new ArrayList();
                    permissionByGrantor.add(grant);
                    usersWithPermission.put(user, permissionByGrantor);
                    privileges.put(sqlpriv, usersWithPermission);
                }
                else {
                    List permissionByGrantor = usersWithPermission.get(user);
                    if (permissionByGrantor == null) {
                        permissionByGrantor = new ArrayList();
                        permissionByGrantor.add(grant);
                        usersWithPermission.put(user, permissionByGrantor);
                    }
                    else {
                        permissionByGrantor.add(grant);
                    }
                }
            }
        }
    }
    
    public Map parseACL(String aclArray, final String owner) {
        if (aclArray == null) {
            String perms = "arwdRxt";
            if (this.connection.haveMinimumServerVersion("8.2")) {
                perms = "arwdxt";
            }
            else if (this.connection.haveMinimumServerVersion("8.4")) {
                perms = "arwdDxt";
            }
            aclArray = "{" + owner + "=" + perms + "/" + owner + "}";
        }
        final List acls = parseACLArray(aclArray);
        final Map privileges = new HashMap();
        for (int i = 0; i < acls.size(); ++i) {
            final String acl = acls.get(i);
            this.addACLPrivileges(acl, privileges);
        }
        return privileges;
    }
    
    public ResultSet getBestRowIdentifier(final String catalog, final String schema, final String table, final int scope, final boolean nullable) throws SQLException {
        final Field[] f = new Field[8];
        final List v = new ArrayList();
        f[0] = new Field("SCOPE", 21);
        f[1] = new Field("COLUMN_NAME", 1043);
        f[2] = new Field("DATA_TYPE", 21);
        f[3] = new Field("TYPE_NAME", 1043);
        f[4] = new Field("COLUMN_SIZE", 23);
        f[5] = new Field("BUFFER_LENGTH", 23);
        f[6] = new Field("DECIMAL_DIGITS", 21);
        f[7] = new Field("PSEUDO_COLUMN", 21);
        String sql;
        if (this.connection.haveMinimumServerVersion("8.1")) {
            sql = "SELECT a.attname, a.atttypid, atttypmod FROM pg_catalog.pg_class ct   JOIN pg_catalog.pg_attribute a ON (ct.oid = a.attrelid)   JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid)   JOIN (SELECT i.indexrelid, i.indrelid, i.indisprimary,              information_schema._pg_expandarray(i.indkey) AS keys         FROM pg_catalog.pg_index i) i     ON (a.attnum = (i.keys).x AND a.attrelid = i.indrelid) WHERE true ";
            if (schema != null && !"".equals(schema)) {
                sql = sql + " AND n.nspname = " + this.escapeQuotes(schema);
            }
        }
        else {
            String where = "";
            String from;
            if (this.connection.haveMinimumServerVersion("7.3")) {
                from = " FROM pg_catalog.pg_namespace n, pg_catalog.pg_class ct, pg_catalog.pg_class ci, pg_catalog.pg_attribute a, pg_catalog.pg_index i ";
                where = " AND ct.relnamespace = n.oid ";
                if (schema != null && !"".equals(schema)) {
                    where = where + " AND n.nspname = " + this.escapeQuotes(schema);
                }
            }
            else {
                from = " FROM pg_class ct, pg_class ci, pg_attribute a, pg_index i ";
            }
            sql = "SELECT a.attname, a.atttypid, a.atttypmod " + from + " WHERE ct.oid=i.indrelid AND ci.oid=i.indexrelid " + " AND a.attrelid=ci.oid " + where;
        }
        sql = sql + " AND ct.relname = " + this.escapeQuotes(table) + " AND i.indisprimary " + " ORDER BY a.attnum ";
        final Statement stmt = this.connection.createStatement();
        final ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            final byte[][] tuple = new byte[8][];
            final int typeOid = (int)rs.getLong("atttypid");
            final int typeMod = rs.getInt("atttypmod");
            final int decimalDigits = this.connection.getTypeInfo().getScale(typeOid, typeMod);
            int columnSize = this.connection.getTypeInfo().getPrecision(typeOid, typeMod);
            if (columnSize == 0) {
                columnSize = this.connection.getTypeInfo().getDisplaySize(typeOid, typeMod);
            }
            tuple[0] = this.connection.encodeString(Integer.toString(scope));
            tuple[1] = rs.getBytes("attname");
            tuple[2] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(typeOid)));
            tuple[3] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(typeOid));
            tuple[4] = this.connection.encodeString(Integer.toString(columnSize));
            tuple[5] = null;
            tuple[6] = this.connection.encodeString(Integer.toString(decimalDigits));
            tuple[7] = this.connection.encodeString(Integer.toString(1));
            v.add(tuple);
        }
        rs.close();
        stmt.close();
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
    }
    
    public ResultSet getVersionColumns(final String catalog, final String schema, final String table) throws SQLException {
        final Field[] f = new Field[8];
        final List v = new ArrayList();
        f[0] = new Field("SCOPE", 21);
        f[1] = new Field("COLUMN_NAME", 1043);
        f[2] = new Field("DATA_TYPE", 21);
        f[3] = new Field("TYPE_NAME", 1043);
        f[4] = new Field("COLUMN_SIZE", 23);
        f[5] = new Field("BUFFER_LENGTH", 23);
        f[6] = new Field("DECIMAL_DIGITS", 21);
        f[7] = new Field("PSEUDO_COLUMN", 21);
        final byte[][] tuple = { null, this.connection.encodeString("ctid"), this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType("tid"))), this.connection.encodeString("tid"), null, null, null, this.connection.encodeString(Integer.toString(2)) };
        v.add(tuple);
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
    }
    
    public ResultSet getPrimaryKeys(final String catalog, final String schema, final String table) throws SQLException {
        String sql;
        if (this.connection.haveMinimumServerVersion("8.1")) {
            sql = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM,   ct.relname AS TABLE_NAME, a.attname AS COLUMN_NAME,   (i.keys).n AS KEY_SEQ, ci.relname AS PK_NAME FROM pg_catalog.pg_class ct   JOIN pg_catalog.pg_attribute a ON (ct.oid = a.attrelid)   JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid)   JOIN (SELECT i.indexrelid, i.indrelid, i.indisprimary,              information_schema._pg_expandarray(i.indkey) AS keys         FROM pg_catalog.pg_index i) i     ON (a.attnum = (i.keys).x AND a.attrelid = i.indrelid)   JOIN pg_catalog.pg_class ci ON (ci.oid = i.indexrelid) WHERE true ";
            if (schema != null && !"".equals(schema)) {
                sql = sql + " AND n.nspname = " + this.escapeQuotes(schema);
            }
        }
        else {
            String where = "";
            String select;
            String from;
            if (this.connection.haveMinimumServerVersion("7.3")) {
                select = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM, ";
                from = " FROM pg_catalog.pg_namespace n, pg_catalog.pg_class ct, pg_catalog.pg_class ci, pg_catalog.pg_attribute a, pg_catalog.pg_index i ";
                where = " AND ct.relnamespace = n.oid ";
                if (schema != null && !"".equals(schema)) {
                    where = where + " AND n.nspname = " + this.escapeQuotes(schema);
                }
            }
            else {
                select = "SELECT NULL AS TABLE_CAT, NULL AS TABLE_SCHEM, ";
                from = " FROM pg_class ct, pg_class ci, pg_attribute a, pg_index i ";
            }
            sql = select + " ct.relname AS TABLE_NAME, " + " a.attname AS COLUMN_NAME, " + " a.attnum AS KEY_SEQ, " + " ci.relname AS PK_NAME " + from + " WHERE ct.oid=i.indrelid AND ci.oid=i.indexrelid " + " AND a.attrelid=ci.oid " + where;
        }
        if (table != null && !"".equals(table)) {
            sql = sql + " AND ct.relname = " + this.escapeQuotes(table);
        }
        sql += " AND i.indisprimary  ORDER BY table_name, pk_name, key_seq";
        return this.createMetaDataStatement().executeQuery(sql);
    }
    
    protected ResultSet getImportedExportedKeys(final String primaryCatalog, final String primarySchema, final String primaryTable, final String foreignCatalog, final String foreignSchema, final String foreignTable) throws SQLException {
        final Field[] f = { new Field("PKTABLE_CAT", 1043), new Field("PKTABLE_SCHEM", 1043), new Field("PKTABLE_NAME", 1043), new Field("PKCOLUMN_NAME", 1043), new Field("FKTABLE_CAT", 1043), new Field("FKTABLE_SCHEM", 1043), new Field("FKTABLE_NAME", 1043), new Field("FKCOLUMN_NAME", 1043), new Field("KEY_SEQ", 21), new Field("UPDATE_RULE", 21), new Field("DELETE_RULE", 21), new Field("FK_NAME", 1043), new Field("PK_NAME", 1043), new Field("DEFERRABILITY", 21) };
        String where = "";
        if (this.connection.haveMinimumServerVersion("7.4")) {
            String sql = "SELECT NULL::text AS PKTABLE_CAT, pkn.nspname AS PKTABLE_SCHEM, pkc.relname AS PKTABLE_NAME, pka.attname AS PKCOLUMN_NAME, NULL::text AS FKTABLE_CAT, fkn.nspname AS FKTABLE_SCHEM, fkc.relname AS FKTABLE_NAME, fka.attname AS FKCOLUMN_NAME, pos.n AS KEY_SEQ, CASE con.confupdtype  WHEN 'c' THEN 0 WHEN 'n' THEN 2 WHEN 'd' THEN 4 WHEN 'r' THEN 1 WHEN 'a' THEN 3 ELSE NULL END AS UPDATE_RULE, CASE con.confdeltype  WHEN 'c' THEN 0 WHEN 'n' THEN 2 WHEN 'd' THEN 4 WHEN 'r' THEN 1 WHEN 'a' THEN 3 ELSE NULL END AS DELETE_RULE, con.conname AS FK_NAME, pkic.relname AS PK_NAME, CASE  WHEN con.condeferrable AND con.condeferred THEN 5 WHEN con.condeferrable THEN 6 ELSE 7 END AS DEFERRABILITY  FROM  pg_catalog.pg_namespace pkn, pg_catalog.pg_class pkc, pg_catalog.pg_attribute pka,  pg_catalog.pg_namespace fkn, pg_catalog.pg_class fkc, pg_catalog.pg_attribute fka,  pg_catalog.pg_constraint con, ";
            if (this.connection.haveMinimumServerVersion("8.0")) {
                sql = sql + " pg_catalog.generate_series(1, " + this.getMaxIndexKeys() + ") pos(n), ";
            }
            else {
                sql += " information_schema._pg_keypositions() pos(n), ";
            }
            sql += " pg_catalog.pg_depend dep, pg_catalog.pg_class pkic  WHERE pkn.oid = pkc.relnamespace AND pkc.oid = pka.attrelid AND pka.attnum = con.confkey[pos.n] AND con.confrelid = pkc.oid  AND fkn.oid = fkc.relnamespace AND fkc.oid = fka.attrelid AND fka.attnum = con.conkey[pos.n] AND con.conrelid = fkc.oid  AND con.contype = 'f' AND con.oid = dep.objid AND pkic.oid = dep.refobjid AND pkic.relkind = 'i' AND dep.classid = 'pg_constraint'::regclass::oid AND dep.refclassid = 'pg_class'::regclass::oid ";
            if (primarySchema != null && !"".equals(primarySchema)) {
                sql = sql + " AND pkn.nspname = " + this.escapeQuotes(primarySchema);
            }
            if (foreignSchema != null && !"".equals(foreignSchema)) {
                sql = sql + " AND fkn.nspname = " + this.escapeQuotes(foreignSchema);
            }
            if (primaryTable != null && !"".equals(primaryTable)) {
                sql = sql + " AND pkc.relname = " + this.escapeQuotes(primaryTable);
            }
            if (foreignTable != null && !"".equals(foreignTable)) {
                sql = sql + " AND fkc.relname = " + this.escapeQuotes(foreignTable);
            }
            if (primaryTable != null) {
                sql += " ORDER BY fkn.nspname,fkc.relname,con.conname,pos.n";
            }
            else {
                sql += " ORDER BY pkn.nspname,pkc.relname, con.conname,pos.n";
            }
            return this.createMetaDataStatement().executeQuery(sql);
        }
        String select;
        String from;
        if (this.connection.haveMinimumServerVersion("7.3")) {
            select = "SELECT DISTINCT n1.nspname as pnspname,n2.nspname as fnspname, ";
            from = " FROM pg_catalog.pg_namespace n1  JOIN pg_catalog.pg_class c1 ON (c1.relnamespace = n1.oid)  JOIN pg_catalog.pg_index i ON (c1.oid=i.indrelid)  JOIN pg_catalog.pg_class ic ON (i.indexrelid=ic.oid)  JOIN pg_catalog.pg_attribute a ON (ic.oid=a.attrelid),  pg_catalog.pg_namespace n2  JOIN pg_catalog.pg_class c2 ON (c2.relnamespace=n2.oid),  pg_catalog.pg_trigger t1  JOIN pg_catalog.pg_proc p1 ON (t1.tgfoid=p1.oid),  pg_catalog.pg_trigger t2  JOIN pg_catalog.pg_proc p2 ON (t2.tgfoid=p2.oid) ";
            if (primarySchema != null && !"".equals(primarySchema)) {
                where = where + " AND n1.nspname = " + this.escapeQuotes(primarySchema);
            }
            if (foreignSchema != null && !"".equals(foreignSchema)) {
                where = where + " AND n2.nspname = " + this.escapeQuotes(foreignSchema);
            }
        }
        else {
            select = "SELECT DISTINCT NULL::text as pnspname, NULL::text as fnspname, ";
            from = " FROM pg_class c1  JOIN pg_index i ON (c1.oid=i.indrelid)  JOIN pg_class ic ON (i.indexrelid=ic.oid)  JOIN pg_attribute a ON (ic.oid=a.attrelid),  pg_class c2,  pg_trigger t1  JOIN pg_proc p1 ON (t1.tgfoid=p1.oid),  pg_trigger t2  JOIN pg_proc p2 ON (t2.tgfoid=p2.oid) ";
        }
        String sql = select + "c1.relname as prelname, " + "c2.relname as frelname, " + "t1.tgconstrname, " + "a.attnum as keyseq, " + "ic.relname as fkeyname, " + "t1.tgdeferrable, " + "t1.tginitdeferred, " + "t1.tgnargs,t1.tgargs, " + "p1.proname as updaterule, " + "p2.proname as deleterule " + from + "WHERE " + "(t1.tgrelid=c1.oid " + "AND t1.tgisconstraint " + "AND t1.tgconstrrelid=c2.oid " + "AND p1.proname ~ '^RI_FKey_.*_upd$') " + "AND " + "(t2.tgrelid=c1.oid " + "AND t2.tgisconstraint " + "AND t2.tgconstrrelid=c2.oid " + "AND p2.proname ~ '^RI_FKey_.*_del$') " + "AND i.indisprimary " + where;
        if (primaryTable != null) {
            sql = sql + "AND c1.relname=" + this.escapeQuotes(primaryTable);
        }
        if (foreignTable != null) {
            sql = sql + "AND c2.relname=" + this.escapeQuotes(foreignTable);
        }
        sql += "ORDER BY ";
        if (primaryTable != null) {
            if (this.connection.haveMinimumServerVersion("7.3")) {
                sql += "fnspname,";
            }
            sql += "frelname";
        }
        else {
            if (this.connection.haveMinimumServerVersion("7.3")) {
                sql += "pnspname,";
            }
            sql += "prelname";
        }
        sql += ",keyseq";
        final Statement stmt = this.connection.createStatement();
        final ResultSet rs = stmt.executeQuery(sql);
        final List tuples = new ArrayList();
        while (rs.next()) {
            final byte[][] tuple = new byte[14][];
            tuple[1] = rs.getBytes(1);
            tuple[5] = rs.getBytes(2);
            tuple[2] = rs.getBytes(3);
            tuple[6] = rs.getBytes(4);
            final String fKeyName = rs.getString(5);
            final String updateRule = rs.getString(12);
            if (updateRule != null) {
                final String rule = updateRule.substring(8, updateRule.length() - 4);
                int action = 3;
                if (rule == null || "noaction".equals(rule)) {
                    action = 3;
                }
                if ("cascade".equals(rule)) {
                    action = 0;
                }
                else if ("setnull".equals(rule)) {
                    action = 2;
                }
                else if ("setdefault".equals(rule)) {
                    action = 4;
                }
                else if ("restrict".equals(rule)) {
                    action = 1;
                }
                tuple[9] = this.connection.encodeString(Integer.toString(action));
            }
            final String deleteRule = rs.getString(13);
            if (deleteRule != null) {
                final String rule2 = deleteRule.substring(8, deleteRule.length() - 4);
                int action2 = 3;
                if ("cascade".equals(rule2)) {
                    action2 = 0;
                }
                else if ("setnull".equals(rule2)) {
                    action2 = 2;
                }
                else if ("setdefault".equals(rule2)) {
                    action2 = 4;
                }
                else if ("restrict".equals(rule2)) {
                    action2 = 1;
                }
                tuple[10] = this.connection.encodeString(Integer.toString(action2));
            }
            final int keySequence = rs.getInt(6);
            String fkeyColumn = "";
            String pkeyColumn = "";
            String fkName = "";
            final String targs = rs.getString(11);
            final List tokens = tokenize(targs, "\\000");
            if (tokens.size() > 0) {
                fkName = tokens.get(0);
            }
            if (fkName.startsWith("<unnamed>")) {
                fkName = targs;
            }
            int element = 4 + (keySequence - 1) * 2;
            if (tokens.size() > element) {
                fkeyColumn = tokens.get(element);
            }
            ++element;
            if (tokens.size() > element) {
                pkeyColumn = tokens.get(element);
            }
            tuple[3] = this.connection.encodeString(pkeyColumn);
            tuple[7] = this.connection.encodeString(fkeyColumn);
            tuple[8] = rs.getBytes(6);
            tuple[11] = this.connection.encodeString(fkName);
            tuple[12] = rs.getBytes(7);
            int deferrability = 7;
            final boolean deferrable = rs.getBoolean(8);
            final boolean initiallyDeferred = rs.getBoolean(9);
            if (deferrable) {
                if (initiallyDeferred) {
                    deferrability = 5;
                }
                else {
                    deferrability = 6;
                }
            }
            tuple[13] = this.connection.encodeString(Integer.toString(deferrability));
            tuples.add(tuple);
        }
        rs.close();
        stmt.close();
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, tuples);
    }
    
    public ResultSet getImportedKeys(final String catalog, final String schema, final String table) throws SQLException {
        return this.getImportedExportedKeys(null, null, null, catalog, schema, table);
    }
    
    public ResultSet getExportedKeys(final String catalog, final String schema, final String table) throws SQLException {
        return this.getImportedExportedKeys(catalog, schema, table, null, null, null);
    }
    
    public ResultSet getCrossReference(final String primaryCatalog, final String primarySchema, final String primaryTable, final String foreignCatalog, final String foreignSchema, final String foreignTable) throws SQLException {
        return this.getImportedExportedKeys(primaryCatalog, primarySchema, primaryTable, foreignCatalog, foreignSchema, foreignTable);
    }
    
    public ResultSet getTypeInfo() throws SQLException {
        final Field[] f = new Field[18];
        final List v = new ArrayList();
        f[0] = new Field("TYPE_NAME", 1043);
        f[1] = new Field("DATA_TYPE", 21);
        f[2] = new Field("PRECISION", 23);
        f[3] = new Field("LITERAL_PREFIX", 1043);
        f[4] = new Field("LITERAL_SUFFIX", 1043);
        f[5] = new Field("CREATE_PARAMS", 1043);
        f[6] = new Field("NULLABLE", 21);
        f[7] = new Field("CASE_SENSITIVE", 16);
        f[8] = new Field("SEARCHABLE", 21);
        f[9] = new Field("UNSIGNED_ATTRIBUTE", 16);
        f[10] = new Field("FIXED_PREC_SCALE", 16);
        f[11] = new Field("AUTO_INCREMENT", 16);
        f[12] = new Field("LOCAL_TYPE_NAME", 1043);
        f[13] = new Field("MINIMUM_SCALE", 21);
        f[14] = new Field("MAXIMUM_SCALE", 21);
        f[15] = new Field("SQL_DATA_TYPE", 23);
        f[16] = new Field("SQL_DATETIME_SUB", 23);
        f[17] = new Field("NUM_PREC_RADIX", 23);
        String sql;
        if (this.connection.haveMinimumServerVersion("7.3")) {
            sql = "SELECT t.typname,t.oid FROM pg_catalog.pg_type t JOIN pg_catalog.pg_namespace n ON (t.typnamespace = n.oid)  WHERE n.nspname != 'pg_toast'";
        }
        else {
            sql = "SELECT typname,oid FROM pg_type WHERE NOT (typname ~ '^pg_toast_') ";
        }
        final Statement stmt = this.connection.createStatement();
        final ResultSet rs = stmt.executeQuery(sql);
        final byte[] bZero = this.connection.encodeString("0");
        final byte[] b10 = this.connection.encodeString("10");
        final byte[] bf = this.connection.encodeString("f");
        final byte[] bt = this.connection.encodeString("t");
        final byte[] bliteral = this.connection.encodeString("'");
        final byte[] bNullable = this.connection.encodeString(Integer.toString(1));
        final byte[] bSearchable = this.connection.encodeString(Integer.toString(3));
        while (rs.next()) {
            final byte[][] tuple = new byte[18][];
            final String typname = rs.getString(1);
            final int typeOid = (int)rs.getLong(2);
            tuple[0] = this.connection.encodeString(typname);
            tuple[1] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(typname)));
            tuple[2] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getMaximumPrecision(typeOid)));
            if (this.connection.getTypeInfo().requiresQuoting(typeOid)) {
                tuple[4] = (tuple[3] = bliteral);
            }
            tuple[6] = bNullable;
            tuple[7] = (this.connection.getTypeInfo().isCaseSensitive(typeOid) ? bt : bf);
            tuple[8] = bSearchable;
            tuple[9] = (this.connection.getTypeInfo().isSigned(typeOid) ? bf : bt);
            tuple[11] = (tuple[10] = bf);
            tuple[13] = bZero;
            tuple[14] = ((typeOid == 1700) ? this.connection.encodeString("1000") : bZero);
            tuple[17] = b10;
            v.add(tuple);
            if (typname.equals("int4")) {
                final byte[][] tuple2 = tuple.clone();
                tuple2[0] = this.connection.encodeString("serial");
                tuple2[11] = bt;
                v.add(tuple2);
            }
            else {
                if (!typname.equals("int8")) {
                    continue;
                }
                final byte[][] tuple2 = tuple.clone();
                tuple2[0] = this.connection.encodeString("bigserial");
                tuple2[11] = bt;
                v.add(tuple2);
            }
        }
        rs.close();
        stmt.close();
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
    }
    
    public ResultSet getIndexInfo(final String catalog, final String schema, final String tableName, final boolean unique, final boolean approximate) throws SQLException {
        String sql;
        if (this.connection.haveMinimumServerVersion("8.3")) {
            sql = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM,   ct.relname AS TABLE_NAME, NOT i.indisunique AS NON_UNIQUE,   NULL AS INDEX_QUALIFIER, ci.relname AS INDEX_NAME,   CASE i.indisclustered     WHEN true THEN 1    ELSE CASE am.amname       WHEN 'hash' THEN 2      ELSE 3    END   END AS TYPE,   (i.keys).n AS ORDINAL_POSITION,   trim(both '\"' from pg_catalog.pg_get_indexdef(ci.oid, (i.keys).n, false)) AS COLUMN_NAME,   CASE am.amcanorder     WHEN true THEN CASE i.indoption[(i.keys).n - 1] & 1       WHEN 1 THEN 'D'       ELSE 'A'     END     ELSE NULL   END AS ASC_OR_DESC,   ci.reltuples AS CARDINALITY,   ci.relpages AS PAGES,   pg_catalog.pg_get_expr(i.indpred, i.indrelid) AS FILTER_CONDITION FROM pg_catalog.pg_class ct   JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid)   JOIN (SELECT i.indexrelid, i.indrelid, i.indoption,           i.indisunique, i.indisclustered, i.indpred,           i.indexprs,           information_schema._pg_expandarray(i.indkey) AS keys         FROM pg_catalog.pg_index i) i     ON (ct.oid = i.indrelid)   JOIN pg_catalog.pg_class ci ON (ci.oid = i.indexrelid)   JOIN pg_catalog.pg_am am ON (ci.relam = am.oid) WHERE true ";
            if (schema != null && !"".equals(schema)) {
                sql = sql + " AND n.nspname = " + this.escapeQuotes(schema);
            }
        }
        else {
            String where = "";
            String select;
            String from;
            if (this.connection.haveMinimumServerVersion("7.3")) {
                select = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM, ";
                from = " FROM pg_catalog.pg_namespace n, pg_catalog.pg_class ct, pg_catalog.pg_class ci, pg_catalog.pg_attribute a, pg_catalog.pg_am am ";
                where = " AND n.oid = ct.relnamespace ";
                if (!this.connection.haveMinimumServerVersion("7.4")) {
                    from += ", pg_catalog.pg_attribute ai, pg_catalog.pg_index i LEFT JOIN pg_catalog.pg_proc ip ON (i.indproc = ip.oid) ";
                    where += " AND ai.attnum = i.indkey[0] AND ai.attrelid = ct.oid ";
                }
                else {
                    from += ", pg_catalog.pg_index i ";
                }
                if (schema != null && !"".equals(schema)) {
                    where = where + " AND n.nspname = " + this.escapeQuotes(schema);
                }
            }
            else {
                select = "SELECT NULL AS TABLE_CAT, NULL AS TABLE_SCHEM, ";
                from = " FROM pg_class ct, pg_class ci, pg_attribute a, pg_am am, pg_attribute ai, pg_index i LEFT JOIN pg_proc ip ON (i.indproc = ip.oid) ";
                where = " AND ai.attnum = i.indkey[0] AND ai.attrelid = ct.oid ";
            }
            sql = select + " ct.relname AS TABLE_NAME, NOT i.indisunique AS NON_UNIQUE, NULL AS INDEX_QUALIFIER, ci.relname AS INDEX_NAME, " + " CASE i.indisclustered " + " WHEN true THEN " + 1 + " ELSE CASE am.amname " + " WHEN 'hash' THEN " + 2 + " ELSE " + 3 + " END " + " END AS TYPE, " + " a.attnum AS ORDINAL_POSITION, ";
            if (this.connection.haveMinimumServerVersion("7.4")) {
                sql += " CASE WHEN i.indexprs IS NULL THEN a.attname ELSE pg_catalog.pg_get_indexdef(ci.oid,a.attnum,false) END AS COLUMN_NAME, ";
            }
            else {
                sql += " CASE i.indproc WHEN 0 THEN a.attname ELSE ip.proname || '(' || ai.attname || ')' END AS COLUMN_NAME, ";
            }
            sql += " NULL AS ASC_OR_DESC,  ci.reltuples AS CARDINALITY,  ci.relpages AS PAGES, ";
            if (this.connection.haveMinimumServerVersion("7.3")) {
                sql += " pg_catalog.pg_get_expr(i.indpred, i.indrelid) AS FILTER_CONDITION ";
            }
            else if (this.connection.haveMinimumServerVersion("7.2")) {
                sql += " pg_get_expr(i.indpred, i.indrelid) AS FILTER_CONDITION ";
            }
            else {
                sql += " NULL AS FILTER_CONDITION ";
            }
            sql = sql + from + " WHERE ct.oid=i.indrelid AND ci.oid=i.indexrelid AND a.attrelid=ci.oid AND ci.relam=am.oid " + where;
        }
        sql = sql + " AND ct.relname = " + this.escapeQuotes(tableName);
        if (unique) {
            sql += " AND i.indisunique ";
        }
        sql += " ORDER BY NON_UNIQUE, TYPE, INDEX_NAME, ORDINAL_POSITION ";
        return this.createMetaDataStatement().executeQuery(sql);
    }
    
    private static List tokenize(final String input, final String delimiter) {
        final List result = new ArrayList();
        int delimiterIndex;
        for (int start = 0, end = input.length(), delimiterSize = delimiter.length(); start < end; start = delimiterIndex + delimiterSize) {
            delimiterIndex = input.indexOf(delimiter, start);
            if (delimiterIndex < 0) {
                result.add(input.substring(start));
                break;
            }
            final String token = input.substring(start, delimiterIndex);
            result.add(token);
        }
        return result;
    }
    
    public boolean supportsResultSetType(final int type) throws SQLException {
        return type != 1005;
    }
    
    public boolean supportsResultSetConcurrency(final int type, final int concurrency) throws SQLException {
        return type != 1005 && (concurrency != 1008 || true);
    }
    
    public boolean ownUpdatesAreVisible(final int type) throws SQLException {
        return true;
    }
    
    public boolean ownDeletesAreVisible(final int type) throws SQLException {
        return true;
    }
    
    public boolean ownInsertsAreVisible(final int type) throws SQLException {
        return true;
    }
    
    public boolean othersUpdatesAreVisible(final int type) throws SQLException {
        return false;
    }
    
    public boolean othersDeletesAreVisible(final int i) throws SQLException {
        return false;
    }
    
    public boolean othersInsertsAreVisible(final int type) throws SQLException {
        return false;
    }
    
    public boolean updatesAreDetected(final int type) throws SQLException {
        return false;
    }
    
    public boolean deletesAreDetected(final int i) throws SQLException {
        return false;
    }
    
    public boolean insertsAreDetected(final int type) throws SQLException {
        return false;
    }
    
    public boolean supportsBatchUpdates() throws SQLException {
        return true;
    }
    
    public ResultSet getUDTs(final String catalog, String schemaPattern, String typeNamePattern, final int[] types) throws SQLException {
        String sql = "select null as type_cat, n.nspname as type_schem, t.typname as type_name,  null as class_name, CASE WHEN t.typtype='c' then 2002 else 2001 end as data_type, pg_catalog.obj_description(t.oid, 'pg_type')  as remarks, CASE WHEN t.typtype = 'd' then  (select CASE";
        final Iterator i = this.connection.getTypeInfo().getPGTypeNamesWithSQLTypes();
        while (i.hasNext()) {
            final String pgType = i.next();
            final int sqlType = this.connection.getTypeInfo().getSQLType(pgType);
            sql = sql + " when typname = " + this.escapeQuotes(pgType) + " then " + sqlType;
        }
        sql += " else 1111 end from pg_type where oid=t.typbasetype) else null end as base_type from pg_catalog.pg_type t, pg_catalog.pg_namespace n where t.typnamespace = n.oid and n.nspname != 'pg_catalog' and n.nspname != 'pg_toast'";
        String toAdd = "";
        if (types != null) {
            toAdd += " and (false ";
            for (int j = 0; j < types.length; ++j) {
                switch (types[j]) {
                    case 2002: {
                        toAdd += " or t.typtype = 'c'";
                        break;
                    }
                    case 2001: {
                        toAdd += " or t.typtype = 'd'";
                        break;
                    }
                }
            }
            toAdd += " ) ";
        }
        else {
            toAdd += " and t.typtype IN ('c','d') ";
        }
        if (typeNamePattern != null) {
            final int firstQualifier = typeNamePattern.indexOf(46);
            final int secondQualifier = typeNamePattern.lastIndexOf(46);
            if (firstQualifier != -1) {
                if (firstQualifier != secondQualifier) {
                    schemaPattern = typeNamePattern.substring(firstQualifier + 1, secondQualifier);
                }
                else {
                    schemaPattern = typeNamePattern.substring(0, firstQualifier);
                }
                typeNamePattern = typeNamePattern.substring(secondQualifier + 1);
            }
            toAdd = toAdd + " and t.typname like " + this.escapeQuotes(typeNamePattern);
        }
        if (schemaPattern != null) {
            toAdd = toAdd + " and n.nspname like " + this.escapeQuotes(schemaPattern);
        }
        sql += toAdd;
        sql += " order by data_type, type_schem, type_name";
        final ResultSet rs = this.createMetaDataStatement().executeQuery(sql);
        return rs;
    }
    
    public Connection getConnection() throws SQLException {
        return this.connection;
    }
    
    public boolean rowChangesAreDetected(final int type) throws SQLException {
        return false;
    }
    
    public boolean rowChangesAreVisible(final int type) throws SQLException {
        return false;
    }
    
    protected Statement createMetaDataStatement() throws SQLException {
        return this.connection.createStatement(1004, 1007);
    }
    
    static {
        tableTypeClauses = new HashMap();
        Map ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("TABLE", ht);
        ht.put("SCHEMAS", "c.relkind = 'r' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
        ht.put("NOSCHEMAS", "c.relkind = 'r' AND c.relname !~ '^pg_'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("VIEW", ht);
        ht.put("SCHEMAS", "c.relkind = 'v' AND n.nspname <> 'pg_catalog' AND n.nspname <> 'information_schema'");
        ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname !~ '^pg_'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("INDEX", ht);
        ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
        ht.put("NOSCHEMAS", "c.relkind = 'i' AND c.relname !~ '^pg_'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("SEQUENCE", ht);
        ht.put("SCHEMAS", "c.relkind = 'S'");
        ht.put("NOSCHEMAS", "c.relkind = 'S'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("TYPE", ht);
        ht.put("SCHEMAS", "c.relkind = 'c' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
        ht.put("NOSCHEMAS", "c.relkind = 'c' AND c.relname !~ '^pg_'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("SYSTEM TABLE", ht);
        ht.put("SCHEMAS", "c.relkind = 'r' AND (n.nspname = 'pg_catalog' OR n.nspname = 'information_schema')");
        ht.put("NOSCHEMAS", "c.relkind = 'r' AND c.relname ~ '^pg_' AND c.relname !~ '^pg_toast_' AND c.relname !~ '^pg_temp_'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("SYSTEM TOAST TABLE", ht);
        ht.put("SCHEMAS", "c.relkind = 'r' AND n.nspname = 'pg_toast'");
        ht.put("NOSCHEMAS", "c.relkind = 'r' AND c.relname ~ '^pg_toast_'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("SYSTEM TOAST INDEX", ht);
        ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname = 'pg_toast'");
        ht.put("NOSCHEMAS", "c.relkind = 'i' AND c.relname ~ '^pg_toast_'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("SYSTEM VIEW", ht);
        ht.put("SCHEMAS", "c.relkind = 'v' AND (n.nspname = 'pg_catalog' OR n.nspname = 'information_schema') ");
        ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname ~ '^pg_'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("SYSTEM INDEX", ht);
        ht.put("SCHEMAS", "c.relkind = 'i' AND (n.nspname = 'pg_catalog' OR n.nspname = 'information_schema') ");
        ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname ~ '^pg_' AND c.relname !~ '^pg_toast_' AND c.relname !~ '^pg_temp_'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("TEMPORARY TABLE", ht);
        ht.put("SCHEMAS", "c.relkind = 'r' AND n.nspname ~ '^pg_temp_' ");
        ht.put("NOSCHEMAS", "c.relkind = 'r' AND c.relname ~ '^pg_temp_' ");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("TEMPORARY INDEX", ht);
        ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname ~ '^pg_temp_' ");
        ht.put("NOSCHEMAS", "c.relkind = 'i' AND c.relname ~ '^pg_temp_' ");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("TEMPORARY VIEW", ht);
        ht.put("SCHEMAS", "c.relkind = 'v' AND n.nspname ~ '^pg_temp_' ");
        ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname ~ '^pg_temp_' ");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("TEMPORARY SEQUENCE", ht);
        ht.put("SCHEMAS", "c.relkind = 'S' AND n.nspname ~ '^pg_temp_' ");
        ht.put("NOSCHEMAS", "c.relkind = 'S' AND c.relname ~ '^pg_temp_' ");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("FOREIGN TABLE", ht);
        ht.put("SCHEMAS", "c.relkind = 'f'");
        ht.put("NOSCHEMAS", "c.relkind = 'f'");
        ht = new HashMap();
        AbstractJdbc2DatabaseMetaData.tableTypeClauses.put("MATERIALIZED VIEW", ht);
        ht.put("SCHEMAS", "c.relkind = 'm'");
        ht.put("NOSCHEMAS", "c.relkind = 'm'");
    }
}
