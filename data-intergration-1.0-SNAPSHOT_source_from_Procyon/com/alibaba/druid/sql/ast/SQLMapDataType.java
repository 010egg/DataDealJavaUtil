// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.DbType;

public class SQLMapDataType extends SQLObjectImpl implements SQLDataType
{
    public static final SQLMapDataType MAP_CHAR_CHAR;
    private DbType dbType;
    private SQLDataType keyType;
    private SQLDataType valueType;
    
    public SQLMapDataType() {
    }
    
    public SQLMapDataType(final SQLDataType keyType, final SQLDataType valueType) {
        this.setKeyType(keyType);
        this.setValueType(valueType);
    }
    
    public SQLMapDataType(final SQLDataType keyType, final SQLDataType valueType, final DbType dbType) {
        this.setKeyType(keyType);
        this.setValueType(valueType);
        this.dbType = dbType;
    }
    
    @Override
    public String getName() {
        return "MAP";
    }
    
    @Override
    public long nameHashCode64() {
        return FnvHash.Constants.MAP;
    }
    
    @Override
    public void setName(final String name) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<SQLExpr> getArguments() {
        return Collections.emptyList();
    }
    
    @Override
    public Boolean getWithTimeZone() {
        return null;
    }
    
    @Override
    public void setWithTimeZone(final Boolean value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isWithLocalTimeZone() {
        return false;
    }
    
    @Override
    public void setWithLocalTimeZone(final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    @Override
    public DbType getDbType() {
        return this.dbType;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.keyType);
            this.acceptChild(visitor, this.valueType);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLMapDataType clone() {
        final SQLMapDataType x = new SQLMapDataType();
        x.dbType = this.dbType;
        if (this.keyType != null) {
            x.setKeyType(this.keyType.clone());
        }
        if (this.valueType != null) {
            x.setValueType(this.valueType.clone());
        }
        return x;
    }
    
    public SQLDataType getKeyType() {
        return this.keyType;
    }
    
    public void setKeyType(final SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.keyType = x;
    }
    
    public SQLDataType getValueType() {
        return this.valueType;
    }
    
    public void setValueType(final SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.valueType = x;
    }
    
    @Override
    public int jdbcType() {
        return 1111;
    }
    
    @Override
    public boolean isInt() {
        return false;
    }
    
    @Override
    public boolean isNumberic() {
        return false;
    }
    
    @Override
    public boolean isString() {
        return false;
    }
    
    @Override
    public boolean hasKeyLength() {
        return false;
    }
    
    static {
        MAP_CHAR_CHAR = new SQLMapDataType(SQLCharExpr.DATA_TYPE, SQLCharExpr.DATA_TYPE);
    }
}
