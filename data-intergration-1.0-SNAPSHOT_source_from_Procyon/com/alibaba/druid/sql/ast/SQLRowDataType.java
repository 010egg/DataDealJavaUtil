// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import com.alibaba.druid.util.FnvHash;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.DbType;

public class SQLRowDataType extends SQLObjectImpl implements SQLDataType
{
    private DbType dbType;
    private List<SQLStructDataType.Field> fields;
    
    public SQLRowDataType() {
        this.fields = new ArrayList<SQLStructDataType.Field>();
    }
    
    public SQLRowDataType(final DbType dbType) {
        this.fields = new ArrayList<SQLStructDataType.Field>();
        this.dbType = dbType;
    }
    
    @Override
    public String getName() {
        return "ROW";
    }
    
    @Override
    public long nameHashCode64() {
        return FnvHash.Constants.ROW;
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
            this.acceptChild(visitor, this.fields);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLRowDataType clone() {
        final SQLRowDataType x = new SQLRowDataType(this.dbType);
        for (final SQLStructDataType.Field field : this.fields) {
            x.addField(field.getName(), field.getDataType().clone());
        }
        return x;
    }
    
    public List<SQLStructDataType.Field> getFields() {
        return this.fields;
    }
    
    public SQLStructDataType.Field addField(final SQLName name, final SQLDataType dataType) {
        final SQLStructDataType.Field field = new SQLStructDataType.Field(name, dataType);
        field.setParent(this);
        this.fields.add(field);
        return field;
    }
    
    @Override
    public int jdbcType() {
        return 2002;
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
}
