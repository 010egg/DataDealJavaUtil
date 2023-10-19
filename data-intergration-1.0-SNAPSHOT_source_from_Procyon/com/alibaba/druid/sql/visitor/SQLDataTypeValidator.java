// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.MySqlUtils;
import com.alibaba.druid.util.HiveUtils;
import com.alibaba.druid.util.OdpsUtils;
import java.util.HashSet;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.SQLStructDataType;
import com.alibaba.druid.sql.ast.SQLMapDataType;
import com.alibaba.druid.sql.ast.SQLArrayDataType;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Set;
import com.alibaba.druid.util.FnvHash;

public class SQLDataTypeValidator extends SQLASTVisitorAdapter
{
    private long[] supportTypeHashCodes;
    private static String[] odpsTypes;
    private static String[] hiveTypes;
    private static String[] mysqlTypes;
    
    public SQLDataTypeValidator(final String[] supportTypes) {
        this.supportTypeHashCodes = FnvHash.fnv1a_64_lower(supportTypes, true);
    }
    
    public SQLDataTypeValidator(final Set<String> typeSet) {
        this.supportTypeHashCodes = new long[typeSet.size()];
        int i = 0;
        for (final String type : typeSet) {
            this.supportTypeHashCodes[i++] = FnvHash.fnv1a_64_lower(type);
        }
        Arrays.sort(this.supportTypeHashCodes);
    }
    
    @Override
    public boolean visit(final SQLDataType x) {
        this.validate(x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLCharacterDataType x) {
        this.validate(x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLArrayDataType x) {
        this.validate(x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLMapDataType x) {
        this.validate(x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLStructDataType x) {
        this.validate(x);
        return true;
    }
    
    public void validate(final SQLDataType x) {
        final long hash = x.nameHashCode64();
        if (Arrays.binarySearch(this.supportTypeHashCodes, hash) < 0) {
            String msg = "illegal dataType : " + x.getName();
            final SQLObject parent = x.getParent();
            if (parent instanceof SQLColumnDefinition) {
                final SQLColumnDefinition column = (SQLColumnDefinition)parent;
                if (column.getName() != null) {
                    msg = msg + ", column " + column.getName();
                }
            }
            throw new FastsqlException(msg);
        }
    }
    
    public static SQLDataTypeValidator of(final DbType dbType) {
        Set<String> typeSet = null;
        String[] types = null;
        switch (dbType) {
            case odps: {
                types = SQLDataTypeValidator.odpsTypes;
                if (types == null) {
                    typeSet = new HashSet<String>();
                    OdpsUtils.loadDataTypes(typeSet);
                    break;
                }
                break;
            }
            case hive: {
                types = SQLDataTypeValidator.hiveTypes;
                if (types == null) {
                    typeSet = new HashSet<String>();
                    HiveUtils.loadDataTypes(typeSet);
                    break;
                }
                break;
            }
            case mysql: {
                types = SQLDataTypeValidator.mysqlTypes;
                if (types == null) {
                    typeSet = new HashSet<String>();
                    MySqlUtils.loadDataTypes(typeSet);
                    break;
                }
                break;
            }
        }
        if (types == null && typeSet != null) {
            types = typeSet.toArray(new String[typeSet.size()]);
        }
        if (types == null) {
            throw new FastsqlException("dataType " + dbType + " not support.");
        }
        return new SQLDataTypeValidator(types);
    }
    
    public static void check(final SQLStatement stmt) {
        final SQLDataTypeValidator v = of(stmt.getDbType());
        stmt.accept(v);
    }
    
    public static void check(final List<SQLStatement> stmtList) {
        if (stmtList.size() == 0) {
            return;
        }
        final DbType dbType = stmtList.get(0).getDbType();
        final SQLDataTypeValidator v = of(dbType);
        check(stmtList, dbType);
    }
    
    public static void check(final List<SQLStatement> stmtList, final DbType dbType) {
        if (stmtList.size() == 0) {
            return;
        }
        final SQLDataTypeValidator v = of(dbType);
        for (final SQLStatement stmt : stmtList) {
            stmt.accept(v);
        }
    }
    
    static {
        SQLDataTypeValidator.odpsTypes = null;
        SQLDataTypeValidator.hiveTypes = null;
        SQLDataTypeValidator.mysqlTypes = null;
    }
}
