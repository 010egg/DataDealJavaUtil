// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import java.sql.ResultSet;
import org.postgresql.core.BaseStatement;
import java.util.Iterator;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.util.PGobject;
import java.util.Collections;
import java.util.HashMap;
import java.sql.PreparedStatement;
import org.postgresql.core.BaseConnection;
import java.util.Map;
import org.postgresql.core.TypeInfo;

public class TypeInfoCache implements TypeInfo
{
    private Map _pgNameToSQLType;
    private Map _pgNameToJavaClass;
    private Map _oidToPgName;
    private Map _pgNameToOid;
    private Map _pgNameToPgObject;
    private Map _pgArrayToPgType;
    private Map _arrayOidToDelimiter;
    private BaseConnection _conn;
    private final int _unknownLength;
    private PreparedStatement _getOidStatement;
    private PreparedStatement _getNameStatement;
    private PreparedStatement _getArrayElementOidStatement;
    private PreparedStatement _getArrayDelimiterStatement;
    private PreparedStatement _getTypeInfoStatement;
    private static final Object[][] types;
    private static final HashMap typeAliases;
    
    public TypeInfoCache(final BaseConnection conn, final int unknownLength) {
        this._conn = conn;
        this._unknownLength = unknownLength;
        this._oidToPgName = new HashMap();
        this._pgNameToOid = new HashMap();
        this._pgNameToJavaClass = new HashMap();
        this._pgNameToPgObject = new HashMap();
        this._pgArrayToPgType = new HashMap();
        this._arrayOidToDelimiter = new HashMap();
        this._pgNameToSQLType = Collections.synchronizedMap(new HashMap<Object, Object>());
        for (int i = 0; i < TypeInfoCache.types.length; ++i) {
            final String pgTypeName = (String)TypeInfoCache.types[i][0];
            final Integer oid = (Integer)TypeInfoCache.types[i][1];
            final Integer sqlType = (Integer)TypeInfoCache.types[i][2];
            final String javaClass = (String)TypeInfoCache.types[i][3];
            final Integer arrayOid = (Integer)TypeInfoCache.types[i][4];
            this.addCoreType(pgTypeName, oid, sqlType, javaClass, arrayOid);
        }
        this._pgNameToJavaClass.put("hstore", Map.class.getName());
    }
    
    @Override
    public synchronized void addCoreType(final String pgTypeName, final Integer oid, final Integer sqlType, final String javaClass, final Integer arrayOid) {
        this._pgNameToJavaClass.put(pgTypeName, javaClass);
        this._pgNameToOid.put(pgTypeName, oid);
        this._oidToPgName.put(oid, pgTypeName);
        this._pgArrayToPgType.put(arrayOid, oid);
        this._pgNameToSQLType.put(pgTypeName, sqlType);
        final Character delim = new Character(',');
        this._arrayOidToDelimiter.put(oid, delim);
        final String pgArrayTypeName = "_" + pgTypeName;
        this._pgNameToJavaClass.put(pgArrayTypeName, "java.sql.Array");
        this._pgNameToSQLType.put(pgArrayTypeName, new Integer(2003));
    }
    
    @Override
    public synchronized void addDataType(final String type, final Class klass) throws SQLException {
        if (!PGobject.class.isAssignableFrom(klass)) {
            throw new PSQLException(GT.tr("The class {0} does not implement org.postgresql.util.PGobject.", klass.toString()), PSQLState.INVALID_PARAMETER_TYPE);
        }
        this._pgNameToPgObject.put(type, klass);
        this._pgNameToJavaClass.put(type, klass.getName());
    }
    
    @Override
    public Iterator getPGTypeNamesWithSQLTypes() {
        return this._pgNameToSQLType.keySet().iterator();
    }
    
    @Override
    public int getSQLType(final int oid) throws SQLException {
        return this.getSQLType(this.getPGType(oid));
    }
    
    @Override
    public synchronized int getSQLType(final String pgTypeName) throws SQLException {
        final Integer i = this._pgNameToSQLType.get(pgTypeName);
        if (i != null) {
            return i;
        }
        if (this._getTypeInfoStatement == null) {
            String sql;
            if (this._conn.haveMinimumServerVersion("8.0")) {
                sql = "SELECT typinput='array_in'::regproc, typtype   FROM pg_catalog.pg_type   LEFT   JOIN (select ns.oid as nspoid, ns.nspname, r.r           from pg_namespace as ns           join ( select s.r, (current_schemas(false))[s.r] as nspname                    from generate_series(1, array_upper(current_schemas(false), 1)) as s(r) ) as r          using ( nspname )        ) as sp     ON sp.nspoid = typnamespace  WHERE typname = ?  ORDER BY sp.r, pg_type.oid DESC LIMIT 1;";
            }
            else if (this._conn.haveMinimumServerVersion("7.3")) {
                sql = "SELECT typinput='array_in'::regproc, typtype FROM pg_catalog.pg_type WHERE typname = ? ORDER BY oid DESC LIMIT 1";
            }
            else {
                sql = "SELECT typinput='array_in'::regproc, typtype FROM pg_type WHERE typname = ? LIMIT 1";
            }
            this._getTypeInfoStatement = this._conn.prepareStatement(sql);
        }
        this._getTypeInfoStatement.setString(1, pgTypeName);
        if (!((BaseStatement)this._getTypeInfoStatement).executeWithFlags(16)) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
        }
        final ResultSet rs = this._getTypeInfoStatement.getResultSet();
        Integer type = null;
        if (rs.next()) {
            final boolean isArray = rs.getBoolean(1);
            final String typtype = rs.getString(2);
            if (isArray) {
                type = new Integer(2003);
            }
            else if ("c".equals(typtype)) {
                type = new Integer(2002);
            }
            else if ("d".equals(typtype)) {
                type = new Integer(2001);
            }
            else if ("e".equals(typtype)) {
                type = new Integer(12);
            }
        }
        if (type == null) {
            type = new Integer(1111);
        }
        rs.close();
        if (pgTypeName != null) {
            this._pgNameToSQLType.put(pgTypeName, type);
        }
        return type;
    }
    
    @Override
    public synchronized int getPGType(final String pgTypeName) throws SQLException {
        Integer oid = this._pgNameToOid.get(pgTypeName);
        if (oid != null) {
            return oid;
        }
        if (this._getOidStatement == null) {
            String sql;
            if (this._conn.haveMinimumServerVersion("8.0")) {
                sql = "SELECT pg_type.oid   FROM pg_catalog.pg_type   LEFT   JOIN (select ns.oid as nspoid, ns.nspname, r.r           from pg_namespace as ns           join ( select s.r, (current_schemas(false))[s.r] as nspname                    from generate_series(1, array_upper(current_schemas(false), 1)) as s(r) ) as r          using ( nspname )        ) as sp     ON sp.nspoid = typnamespace  WHERE typname = ?  ORDER BY sp.r, pg_type.oid DESC LIMIT 1;";
            }
            else if (this._conn.haveMinimumServerVersion("7.3")) {
                sql = "SELECT oid FROM pg_catalog.pg_type WHERE typname = ? ORDER BY oid DESC LIMIT 1";
            }
            else {
                sql = "SELECT oid FROM pg_type WHERE typname = ? ORDER BY oid DESC LIMIT 1";
            }
            this._getOidStatement = this._conn.prepareStatement(sql);
        }
        this._getOidStatement.setString(1, pgTypeName);
        if (!((BaseStatement)this._getOidStatement).executeWithFlags(16)) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
        }
        oid = new Integer(0);
        final ResultSet rs = this._getOidStatement.getResultSet();
        if (rs.next()) {
            oid = new Integer((int)rs.getLong(1));
            this._oidToPgName.put(oid, pgTypeName);
        }
        this._pgNameToOid.put(pgTypeName, oid);
        rs.close();
        return oid;
    }
    
    @Override
    public synchronized String getPGType(final int oid) throws SQLException {
        if (oid == 0) {
            return null;
        }
        String pgTypeName = this._oidToPgName.get(new Integer(oid));
        if (pgTypeName != null) {
            return pgTypeName;
        }
        if (this._getNameStatement == null) {
            String sql;
            if (this._conn.haveMinimumServerVersion("7.3")) {
                sql = "SELECT typname FROM pg_catalog.pg_type WHERE oid = ?";
            }
            else {
                sql = "SELECT typname FROM pg_type WHERE oid = ?";
            }
            this._getNameStatement = this._conn.prepareStatement(sql);
        }
        this._getNameStatement.setInt(1, oid);
        if (!((BaseStatement)this._getNameStatement).executeWithFlags(16)) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
        }
        final ResultSet rs = this._getNameStatement.getResultSet();
        if (rs.next()) {
            pgTypeName = rs.getString(1);
            this._pgNameToOid.put(pgTypeName, new Integer(oid));
            this._oidToPgName.put(new Integer(oid), pgTypeName);
        }
        rs.close();
        return pgTypeName;
    }
    
    @Override
    public int getPGArrayType(String elementTypeName) throws SQLException {
        elementTypeName = this.getTypeForAlias(elementTypeName);
        return this.getPGType("_" + elementTypeName);
    }
    
    protected synchronized int convertArrayToBaseOid(final int oid) {
        final Integer i = this._pgArrayToPgType.get(new Integer(oid));
        if (i == null) {
            return oid;
        }
        return i;
    }
    
    @Override
    public synchronized char getArrayDelimiter(final int oid) throws SQLException {
        if (oid == 0) {
            return ',';
        }
        Character delim = this._arrayOidToDelimiter.get(new Integer(oid));
        if (delim != null) {
            return delim;
        }
        if (this._getArrayDelimiterStatement == null) {
            String sql;
            if (this._conn.haveMinimumServerVersion("7.3")) {
                sql = "SELECT e.typdelim FROM pg_catalog.pg_type t, pg_catalog.pg_type e WHERE t.oid = ? and t.typelem = e.oid";
            }
            else {
                sql = "SELECT e.typdelim FROM pg_type t, pg_type e WHERE t.oid = ? and t.typelem = e.oid";
            }
            this._getArrayDelimiterStatement = this._conn.prepareStatement(sql);
        }
        this._getArrayDelimiterStatement.setInt(1, oid);
        if (!((BaseStatement)this._getArrayDelimiterStatement).executeWithFlags(16)) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
        }
        final ResultSet rs = this._getArrayDelimiterStatement.getResultSet();
        if (!rs.next()) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
        }
        final String s = rs.getString(1);
        delim = new Character(s.charAt(0));
        this._arrayOidToDelimiter.put(new Integer(oid), delim);
        rs.close();
        return delim;
    }
    
    @Override
    public synchronized int getPGArrayElement(final int oid) throws SQLException {
        if (oid == 0) {
            return 0;
        }
        Integer pgType = this._pgArrayToPgType.get(new Integer(oid));
        if (pgType != null) {
            return pgType;
        }
        if (this._getArrayElementOidStatement == null) {
            String sql;
            if (this._conn.haveMinimumServerVersion("7.3")) {
                sql = "SELECT e.oid, e.typname FROM pg_catalog.pg_type t, pg_catalog.pg_type e WHERE t.oid = ? and t.typelem = e.oid";
            }
            else {
                sql = "SELECT e.oid, e.typname FROM pg_type t, pg_type e WHERE t.oid = ? and t.typelem = e.oid";
            }
            this._getArrayElementOidStatement = this._conn.prepareStatement(sql);
        }
        this._getArrayElementOidStatement.setInt(1, oid);
        if (!((BaseStatement)this._getArrayElementOidStatement).executeWithFlags(16)) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
        }
        final ResultSet rs = this._getArrayElementOidStatement.getResultSet();
        if (!rs.next()) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
        }
        pgType = new Integer((int)rs.getLong(1));
        this._pgArrayToPgType.put(new Integer(oid), pgType);
        this._pgNameToOid.put(rs.getString(2), pgType);
        this._oidToPgName.put(pgType, rs.getString(2));
        rs.close();
        return pgType;
    }
    
    @Override
    public synchronized Class getPGobject(final String type) {
        return this._pgNameToPgObject.get(type);
    }
    
    @Override
    public synchronized String getJavaClass(final int oid) throws SQLException {
        final String pgTypeName = this.getPGType(oid);
        String result = this._pgNameToJavaClass.get(pgTypeName);
        if (result != null) {
            return result;
        }
        if (this.getSQLType(pgTypeName) == 2003) {
            result = "java.sql.Array";
            this._pgNameToJavaClass.put(pgTypeName, result);
        }
        return result;
    }
    
    @Override
    public String getTypeForAlias(final String alias) {
        final String type = TypeInfoCache.typeAliases.get(alias);
        if (type != null) {
            return type;
        }
        return alias;
    }
    
    @Override
    public int getPrecision(int oid, final int typmod) {
        oid = this.convertArrayToBaseOid(oid);
        switch (oid) {
            case 21: {
                return 5;
            }
            case 23:
            case 26: {
                return 10;
            }
            case 20: {
                return 19;
            }
            case 700: {
                return 8;
            }
            case 701: {
                return 17;
            }
            case 1700: {
                if (typmod == -1) {
                    return 0;
                }
                return (typmod - 4 & 0xFFFF0000) >> 16;
            }
            case 16:
            case 18: {
                return 1;
            }
            case 1042:
            case 1043: {
                if (typmod == -1) {
                    return this._unknownLength;
                }
                return typmod - 4;
            }
            case 1082:
            case 1083:
            case 1114:
            case 1184:
            case 1186:
            case 1266: {
                return this.getDisplaySize(oid, typmod);
            }
            case 1560: {
                return typmod;
            }
            case 1562: {
                if (typmod == -1) {
                    return this._unknownLength;
                }
                return typmod;
            }
            default: {
                return this._unknownLength;
            }
        }
    }
    
    @Override
    public int getScale(int oid, final int typmod) {
        oid = this.convertArrayToBaseOid(oid);
        switch (oid) {
            case 700: {
                return 8;
            }
            case 701: {
                return 17;
            }
            case 1700: {
                if (typmod == -1) {
                    return 0;
                }
                return typmod - 4 & 0xFFFF;
            }
            case 1083:
            case 1114:
            case 1184:
            case 1266: {
                if (typmod == -1) {
                    return 6;
                }
                return typmod;
            }
            case 1186: {
                if (typmod == -1) {
                    return 6;
                }
                return typmod & 0xFFFF;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public boolean isCaseSensitive(int oid) {
        oid = this.convertArrayToBaseOid(oid);
        switch (oid) {
            case 16:
            case 20:
            case 21:
            case 23:
            case 26:
            case 700:
            case 701:
            case 1082:
            case 1083:
            case 1114:
            case 1184:
            case 1186:
            case 1266:
            case 1560:
            case 1562:
            case 1700: {
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public boolean isSigned(int oid) {
        oid = this.convertArrayToBaseOid(oid);
        switch (oid) {
            case 20:
            case 21:
            case 23:
            case 700:
            case 701:
            case 1700: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public int getDisplaySize(int oid, final int typmod) {
        oid = this.convertArrayToBaseOid(oid);
        switch (oid) {
            case 21: {
                return 6;
            }
            case 23: {
                return 11;
            }
            case 26: {
                return 10;
            }
            case 20: {
                return 20;
            }
            case 700: {
                return 15;
            }
            case 701: {
                return 25;
            }
            case 18: {
                return 1;
            }
            case 16: {
                return 1;
            }
            case 1082: {
                return 13;
            }
            case 1083:
            case 1114:
            case 1184:
            case 1266: {
                int secondSize = 0;
                switch (typmod) {
                    case -1: {
                        secondSize = 7;
                        break;
                    }
                    case 0: {
                        secondSize = 0;
                        break;
                    }
                    case 1: {
                        secondSize = 3;
                        break;
                    }
                    default: {
                        secondSize = typmod + 1;
                        break;
                    }
                }
                switch (oid) {
                    case 1083: {
                        return 8 + secondSize;
                    }
                    case 1266: {
                        return 8 + secondSize + 6;
                    }
                    case 1114: {
                        return 22 + secondSize;
                    }
                    case 1184: {
                        return 22 + secondSize + 6;
                    }
                    default: {
                        return 49;
                    }
                }
                break;
            }
            case 1186: {
                return 49;
            }
            case 1042:
            case 1043: {
                if (typmod == -1) {
                    return this._unknownLength;
                }
                return typmod - 4;
            }
            case 1700: {
                if (typmod == -1) {
                    return 131089;
                }
                final int precision = typmod - 4 >> 16 & 0xFFFF;
                final int scale = typmod - 4 & 0xFFFF;
                return 1 + precision + ((scale != 0) ? 1 : 0);
            }
            case 1560: {
                return typmod;
            }
            case 1562: {
                if (typmod == -1) {
                    return this._unknownLength;
                }
                return typmod;
            }
            case 17:
            case 25: {
                return this._unknownLength;
            }
            default: {
                return this._unknownLength;
            }
        }
    }
    
    @Override
    public int getMaximumPrecision(int oid) {
        oid = this.convertArrayToBaseOid(oid);
        switch (oid) {
            case 1700: {
                return 1000;
            }
            case 1083:
            case 1266: {
                return 6;
            }
            case 1114:
            case 1184:
            case 1186: {
                return 6;
            }
            case 1042:
            case 1043: {
                return 10485760;
            }
            case 1560:
            case 1562: {
                return 83886080;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public boolean requiresQuoting(final int oid) throws SQLException {
        final int sqlType = this.getSQLType(oid);
        switch (sqlType) {
            case -6:
            case -5:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    static {
        types = new Object[][] { { "int2", new Integer(21), new Integer(5), "java.lang.Integer", new Integer(1005) }, { "int4", new Integer(23), new Integer(4), "java.lang.Integer", new Integer(1007) }, { "oid", new Integer(26), new Integer(-5), "java.lang.Long", new Integer(1028) }, { "int8", new Integer(20), new Integer(-5), "java.lang.Long", new Integer(1016) }, { "money", new Integer(790), new Integer(8), "java.lang.Double", new Integer(791) }, { "numeric", new Integer(1700), new Integer(2), "java.math.BigDecimal", new Integer(1231) }, { "float4", new Integer(700), new Integer(7), "java.lang.Float", new Integer(1021) }, { "float8", new Integer(701), new Integer(8), "java.lang.Double", new Integer(1022) }, { "char", new Integer(18), new Integer(1), "java.lang.String", new Integer(1002) }, { "bpchar", new Integer(1042), new Integer(1), "java.lang.String", new Integer(1014) }, { "varchar", new Integer(1043), new Integer(12), "java.lang.String", new Integer(1015) }, { "text", new Integer(25), new Integer(12), "java.lang.String", new Integer(1009) }, { "name", new Integer(19), new Integer(12), "java.lang.String", new Integer(1003) }, { "bytea", new Integer(17), new Integer(-2), "[B", new Integer(1001) }, { "bool", new Integer(16), new Integer(-7), "java.lang.Boolean", new Integer(1000) }, { "bit", new Integer(1560), new Integer(-7), "java.lang.Boolean", new Integer(1561) }, { "date", new Integer(1082), new Integer(91), "java.sql.Date", new Integer(1182) }, { "time", new Integer(1083), new Integer(92), "java.sql.Time", new Integer(1183) }, { "timetz", new Integer(1266), new Integer(92), "java.sql.Time", new Integer(1270) }, { "timestamp", new Integer(1114), new Integer(93), "java.sql.Timestamp", new Integer(1115) }, { "timestamptz", new Integer(1184), new Integer(93), "java.sql.Timestamp", new Integer(1185) } };
        (typeAliases = new HashMap()).put("smallint", "int2");
        TypeInfoCache.typeAliases.put("integer", "int4");
        TypeInfoCache.typeAliases.put("int", "int4");
        TypeInfoCache.typeAliases.put("bigint", "int8");
        TypeInfoCache.typeAliases.put("float", "float8");
        TypeInfoCache.typeAliases.put("boolean", "bool");
        TypeInfoCache.typeAliases.put("decimal", "numeric");
    }
}
