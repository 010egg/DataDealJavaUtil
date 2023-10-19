// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.util.FnvHash;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.DbType;

public class SQLArrayDataType extends SQLObjectImpl implements SQLDataType
{
    public static final SQLArrayDataType ARRYA_CHAR;
    private DbType dbType;
    private SQLDataType componentType;
    private List<SQLExpr> arguments;
    
    public SQLArrayDataType(final SQLDataType componentType) {
        this.arguments = new ArrayList<SQLExpr>();
        this.setComponentType(componentType);
    }
    
    public SQLArrayDataType(final SQLDataType componentType, final DbType dbType) {
        this.arguments = new ArrayList<SQLExpr>();
        this.dbType = dbType;
        this.setComponentType(componentType);
    }
    
    @Override
    public String getName() {
        return "ARRAY";
    }
    
    @Override
    public long nameHashCode64() {
        return FnvHash.Constants.ARRAY;
    }
    
    @Override
    public void setName(final String name) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<SQLExpr> getArguments() {
        return this.arguments;
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
            this.acceptChild(visitor, this.componentType);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLArrayDataType clone() {
        final SQLArrayDataType x = new SQLArrayDataType(this.componentType.clone());
        x.dbType = this.dbType;
        for (final SQLExpr arg : this.arguments) {
            final SQLExpr item = arg.clone();
            item.setParent(x);
            x.arguments.add(item);
        }
        return x;
    }
    
    public SQLDataType getComponentType() {
        return this.componentType;
    }
    
    public void setComponentType(final SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.componentType = x;
    }
    
    @Override
    public int jdbcType() {
        return 2003;
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
        ARRYA_CHAR = new SQLArrayDataType(SQLCharExpr.DATA_TYPE);
    }
}
