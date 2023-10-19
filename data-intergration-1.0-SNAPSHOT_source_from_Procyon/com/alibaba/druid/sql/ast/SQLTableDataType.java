// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import com.alibaba.druid.util.FnvHash;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import java.util.List;
import com.alibaba.druid.DbType;

public class SQLTableDataType extends SQLObjectImpl implements SQLDataType
{
    private DbType dbType;
    private List<SQLColumnDefinition> columns;
    
    public SQLTableDataType() {
        this.columns = new ArrayList<SQLColumnDefinition>();
    }
    
    public SQLTableDataType(final DbType dbType) {
        this.columns = new ArrayList<SQLColumnDefinition>();
        this.dbType = dbType;
    }
    
    @Override
    public String getName() {
        return "TABLE";
    }
    
    @Override
    public void setName(final String name) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long nameHashCode64() {
        return FnvHash.Constants.TABLE;
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
    public DbType getDbType() {
        return this.dbType;
    }
    
    @Override
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.columns);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLTableDataType clone() {
        final SQLTableDataType x = new SQLTableDataType(this.dbType);
        for (final SQLColumnDefinition item : this.columns) {
            final SQLColumnDefinition item2 = item.clone();
            item2.setParent(x);
            x.columns.add(item2);
        }
        return x;
    }
    
    public List<SQLColumnDefinition> getColumns() {
        return this.columns;
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
