// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.SQLUtils;
import java.util.Iterator;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import java.util.List;

public class SQLDataTypeImpl extends SQLObjectImpl implements SQLDataType, SQLDbTypedObject
{
    private String name;
    private long nameHashCode64;
    protected final List<SQLExpr> arguments;
    private Boolean withTimeZone;
    private boolean withLocalTimeZone;
    private DbType dbType;
    private boolean unsigned;
    private boolean zerofill;
    private SQLExpr indexBy;
    
    public SQLDataTypeImpl() {
        this.arguments = new ArrayList<SQLExpr>();
        this.withLocalTimeZone = false;
    }
    
    public SQLDataTypeImpl(final String name) {
        this.arguments = new ArrayList<SQLExpr>();
        this.withLocalTimeZone = false;
        this.name = name;
    }
    
    public SQLDataTypeImpl(final String name, final int precision) {
        this(name);
        this.addArgument(new SQLIntegerExpr(precision));
    }
    
    public SQLDataTypeImpl(final String name, final SQLExpr arg) {
        this(name);
        this.addArgument(arg);
    }
    
    public SQLDataTypeImpl(final String name, final int precision, final int scale) {
        this(name);
        this.addArgument(new SQLIntegerExpr(precision));
        this.addArgument(new SQLIntegerExpr(scale));
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (int i = 0; i < this.arguments.size(); ++i) {
                final SQLExpr arg = this.arguments.get(i);
                if (arg != null) {
                    arg.accept(visitor);
                }
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public long nameHashCode64() {
        if (this.nameHashCode64 == 0L) {
            this.nameHashCode64 = FnvHash.hashCode64(this.name);
        }
        return this.nameHashCode64;
    }
    
    @Override
    public void setName(final String name) {
        this.name = name;
        this.nameHashCode64 = 0L;
    }
    
    @Override
    public List<SQLExpr> getArguments() {
        return this.arguments;
    }
    
    public void addArgument(final SQLExpr argument) {
        if (argument != null) {
            argument.setParent(this);
        }
        this.arguments.add(argument);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLDataTypeImpl dataType = (SQLDataTypeImpl)o;
        if (this.name != null) {
            if (this.name.equals(dataType.name)) {
                return this.arguments.equals(dataType.arguments) && ((this.withTimeZone != null) ? this.withTimeZone.equals(dataType.withTimeZone) : (dataType.withTimeZone == null));
            }
        }
        else if (dataType.name == null) {
            return this.arguments.equals(dataType.arguments) && ((this.withTimeZone != null) ? this.withTimeZone.equals(dataType.withTimeZone) : (dataType.withTimeZone == null));
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long value = this.nameHashCode64();
        return (int)(value ^ value >>> 32);
    }
    
    @Override
    public Boolean getWithTimeZone() {
        return this.withTimeZone;
    }
    
    @Override
    public void setWithTimeZone(final Boolean withTimeZone) {
        this.withTimeZone = withTimeZone;
    }
    
    @Override
    public boolean isWithLocalTimeZone() {
        return this.withLocalTimeZone;
    }
    
    @Override
    public void setWithLocalTimeZone(final boolean withLocalTimeZone) {
        this.withLocalTimeZone = withLocalTimeZone;
    }
    
    @Override
    public DbType getDbType() {
        return this.dbType;
    }
    
    @Override
    public int jdbcType() {
        final long nameNash = this.nameHashCode64();
        if (nameNash == FnvHash.Constants.TINYINT) {
            return -6;
        }
        if (nameNash == FnvHash.Constants.SMALLINT) {
            return 5;
        }
        if (nameNash == FnvHash.Constants.INT || nameNash == FnvHash.Constants.INTEGER) {
            return 4;
        }
        if (nameNash == FnvHash.Constants.BIGINT) {
            return -5;
        }
        if (nameNash == FnvHash.Constants.DECIMAL) {
            return 3;
        }
        if (nameNash == FnvHash.Constants.FLOAT) {
            return 6;
        }
        if (nameNash == FnvHash.Constants.REAL) {
            return 7;
        }
        if (nameNash == FnvHash.Constants.DOUBLE) {
            return 8;
        }
        if (nameNash == FnvHash.Constants.NUMBER || nameNash == FnvHash.Constants.NUMERIC) {
            return 2;
        }
        if (nameNash == FnvHash.Constants.BOOLEAN) {
            return 16;
        }
        if (nameNash == FnvHash.Constants.DATE || nameNash == FnvHash.Constants.NEWDATE) {
            return 91;
        }
        if (nameNash == FnvHash.Constants.DATETIME || nameNash == FnvHash.Constants.TIMESTAMP) {
            return 93;
        }
        if (nameNash == FnvHash.Constants.TIME) {
            return 92;
        }
        if (nameNash == FnvHash.Constants.BLOB) {
            return 2004;
        }
        if (nameNash == FnvHash.Constants.ROWID) {
            return -8;
        }
        if (nameNash == FnvHash.Constants.REF) {
            return 2006;
        }
        if (nameNash == FnvHash.Constants.TINYINT || nameNash == FnvHash.Constants.TINY) {
            return -6;
        }
        if (nameNash == FnvHash.Constants.SMALLINT || nameNash == FnvHash.Constants.SHORT) {
            return 5;
        }
        if (nameNash == FnvHash.Constants.INT || nameNash == FnvHash.Constants.INT24 || nameNash == FnvHash.Constants.INTEGER) {
            return 4;
        }
        if (nameNash == FnvHash.Constants.NUMBER || nameNash == FnvHash.Constants.NUMERIC) {
            return 2;
        }
        if (nameNash == FnvHash.Constants.BOOLEAN) {
            return 16;
        }
        if (nameNash == FnvHash.Constants.DATE || nameNash == FnvHash.Constants.YEAR || nameNash == FnvHash.Constants.NEWDATE) {
            return 91;
        }
        if (nameNash == FnvHash.Constants.DATETIME || nameNash == FnvHash.Constants.TIMESTAMP) {
            return 93;
        }
        if (nameNash == FnvHash.Constants.TIME) {
            return 92;
        }
        if (nameNash == FnvHash.Constants.TINYBLOB) {
            return -3;
        }
        if (nameNash == FnvHash.Constants.BLOB) {
            return 2004;
        }
        if (nameNash == FnvHash.Constants.LONGBLOB) {
            return -4;
        }
        if (nameNash == FnvHash.Constants.ROWID) {
            return -8;
        }
        if (nameNash == FnvHash.Constants.REF) {
            return 2006;
        }
        if (nameNash == FnvHash.Constants.BINARY || nameNash == FnvHash.Constants.GEOMETRY) {
            return -2;
        }
        if (nameNash == FnvHash.Constants.SQLXML) {
            return 2009;
        }
        if (nameNash == FnvHash.Constants.BIT) {
            return -7;
        }
        if (nameNash == FnvHash.Constants.NCHAR) {
            return -15;
        }
        if (nameNash == FnvHash.Constants.CHAR || nameNash == FnvHash.Constants.ENUM || nameNash == FnvHash.Constants.SET || nameNash == FnvHash.Constants.JSON) {
            return 1;
        }
        if (nameNash == FnvHash.Constants.VARCHAR || nameNash == FnvHash.Constants.VARCHAR2 || nameNash == FnvHash.Constants.STRING) {
            return 12;
        }
        if (nameNash == FnvHash.Constants.NVARCHAR || nameNash == FnvHash.Constants.NVARCHAR2) {
            return -9;
        }
        if (nameNash == FnvHash.Constants.CLOB || nameNash == FnvHash.Constants.TEXT || nameNash == FnvHash.Constants.TINYTEXT || nameNash == FnvHash.Constants.MEDIUMTEXT || nameNash == FnvHash.Constants.LONGTEXT) {
            return 2005;
        }
        if (nameNash == FnvHash.Constants.NCLOB) {
            return 2011;
        }
        if (nameNash == FnvHash.Constants.TINYBLOB) {
            return -3;
        }
        if (nameNash == FnvHash.Constants.LONGBLOB) {
            return -4;
        }
        if (nameNash == FnvHash.Constants.BINARY || nameNash == FnvHash.Constants.GEOMETRY) {
            return -2;
        }
        if (nameNash == FnvHash.Constants.SQLXML) {
            return 2009;
        }
        if (nameNash == FnvHash.Constants.NCHAR) {
            return -15;
        }
        if (nameNash == FnvHash.Constants.CHAR || nameNash == FnvHash.Constants.JSON) {
            return 1;
        }
        if (nameNash == FnvHash.Constants.VARCHAR || nameNash == FnvHash.Constants.VARCHAR2 || nameNash == FnvHash.Constants.STRING) {
            return 12;
        }
        if (nameNash == FnvHash.Constants.NVARCHAR || nameNash == FnvHash.Constants.NVARCHAR2) {
            return -9;
        }
        if (nameNash == FnvHash.Constants.CLOB || nameNash == FnvHash.Constants.TEXT || nameNash == FnvHash.Constants.TINYTEXT || nameNash == FnvHash.Constants.MEDIUMTEXT || nameNash == FnvHash.Constants.LONGTEXT) {
            return 2005;
        }
        if (nameNash == FnvHash.Constants.NCLOB) {
            return 2011;
        }
        return 0;
    }
    
    @Override
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    @Override
    public SQLDataTypeImpl clone() {
        final SQLDataTypeImpl x = new SQLDataTypeImpl();
        this.cloneTo(x);
        return x;
    }
    
    public void cloneTo(final SQLDataTypeImpl x) {
        x.dbType = this.dbType;
        x.name = this.name;
        x.nameHashCode64 = this.nameHashCode64;
        for (final SQLExpr arg : this.arguments) {
            x.addArgument(arg.clone());
        }
        x.withTimeZone = this.withTimeZone;
        x.withLocalTimeZone = this.withLocalTimeZone;
        x.zerofill = this.zerofill;
        x.unsigned = this.unsigned;
        if (this.indexBy != null) {
            x.setIndexBy(this.indexBy.clone());
        }
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this, this.dbType);
    }
    
    public boolean isUnsigned() {
        return this.unsigned;
    }
    
    public void setUnsigned(final boolean unsigned) {
        this.unsigned = unsigned;
    }
    
    public boolean isZerofill() {
        return this.zerofill;
    }
    
    public void setZerofill(final boolean zerofill) {
        this.zerofill = zerofill;
    }
    
    public SQLExpr getIndexBy() {
        return this.indexBy;
    }
    
    public void setIndexBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.indexBy = x;
    }
    
    @Override
    public boolean isInt() {
        final long hashCode64 = this.nameHashCode64();
        return hashCode64 == FnvHash.Constants.BIGINT || hashCode64 == FnvHash.Constants.INT || hashCode64 == FnvHash.Constants.INT4 || hashCode64 == FnvHash.Constants.INT24 || hashCode64 == FnvHash.Constants.SMALLINT || hashCode64 == FnvHash.Constants.TINYINT || hashCode64 == FnvHash.Constants.INTEGER;
    }
    
    @Override
    public boolean isNumberic() {
        final long hashCode64 = this.nameHashCode64();
        return hashCode64 == FnvHash.Constants.REAL || hashCode64 == FnvHash.Constants.FLOAT || hashCode64 == FnvHash.Constants.DOUBLE || hashCode64 == FnvHash.Constants.DOUBLE_PRECISION || hashCode64 == FnvHash.Constants.NUMBER || hashCode64 == FnvHash.Constants.DECIMAL;
    }
    
    @Override
    public boolean isString() {
        final long hashCode64 = this.nameHashCode64();
        return hashCode64 == FnvHash.Constants.VARCHAR || hashCode64 == FnvHash.Constants.VARCHAR2 || hashCode64 == FnvHash.Constants.CHAR || hashCode64 == FnvHash.Constants.NCHAR || hashCode64 == FnvHash.Constants.NVARCHAR || hashCode64 == FnvHash.Constants.NVARCHAR2 || hashCode64 == FnvHash.Constants.TEXT || hashCode64 == FnvHash.Constants.TINYTEXT || hashCode64 == FnvHash.Constants.MEDIUMTEXT || hashCode64 == FnvHash.Constants.LONGTEXT || hashCode64 == FnvHash.Constants.CLOB || hashCode64 == FnvHash.Constants.NCLOB || hashCode64 == FnvHash.Constants.MULTIVALUE || hashCode64 == FnvHash.Constants.STRING;
    }
    
    @Override
    public boolean hasKeyLength() {
        final long hashCode64 = this.nameHashCode64();
        return hashCode64 == FnvHash.Constants.VARCHAR || hashCode64 == FnvHash.Constants.VARCHAR2 || hashCode64 == FnvHash.Constants.CHAR || hashCode64 == FnvHash.Constants.NCHAR || hashCode64 == FnvHash.Constants.NVARCHAR || hashCode64 == FnvHash.Constants.NVARCHAR2 || hashCode64 == FnvHash.Constants.TEXT || hashCode64 == FnvHash.Constants.TINYTEXT || hashCode64 == FnvHash.Constants.MEDIUMTEXT || hashCode64 == FnvHash.Constants.LONGTEXT || hashCode64 == FnvHash.Constants.CLOB || hashCode64 == FnvHash.Constants.NCLOB || hashCode64 == FnvHash.Constants.MULTIVALUE || hashCode64 == FnvHash.Constants.STRING || hashCode64 == FnvHash.Constants.BLOB || hashCode64 == FnvHash.Constants.TINYBLOB || hashCode64 == FnvHash.Constants.LONGBLOB || hashCode64 == FnvHash.Constants.BINARY || hashCode64 == FnvHash.Constants.VARBINARY;
    }
}
