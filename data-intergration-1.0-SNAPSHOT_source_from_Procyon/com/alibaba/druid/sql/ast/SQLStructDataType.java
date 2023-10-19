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

public class SQLStructDataType extends SQLObjectImpl implements SQLDataType
{
    private DbType dbType;
    private List<Field> fields;
    
    public SQLStructDataType() {
        this.fields = new ArrayList<Field>();
    }
    
    public SQLStructDataType(final DbType dbType) {
        this.fields = new ArrayList<Field>();
        this.dbType = dbType;
    }
    
    @Override
    public String getName() {
        return "STRUCT";
    }
    
    @Override
    public long nameHashCode64() {
        return FnvHash.Constants.STRUCT;
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
    public SQLStructDataType clone() {
        final SQLStructDataType x = new SQLStructDataType(this.dbType);
        for (final Field field : this.fields) {
            x.addField(field.name, field.dataType.clone());
        }
        return x;
    }
    
    public List<Field> getFields() {
        return this.fields;
    }
    
    public Field addField(final SQLName name, final SQLDataType dataType) {
        final Field field = new Field(name, dataType);
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
    
    public static class Field extends SQLObjectImpl
    {
        private SQLName name;
        private SQLDataType dataType;
        private String comment;
        
        public Field(final SQLName name, final SQLDataType dataType) {
            this.setName(name);
            this.setDataType(dataType);
        }
        
        @Override
        protected void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.name);
                this.acceptChild(visitor, this.dataType);
            }
            visitor.endVisit(this);
        }
        
        public SQLName getName() {
            return this.name;
        }
        
        public void setName(final SQLName x) {
            if (x != null) {
                x.setParent(this);
            }
            this.name = x;
        }
        
        public SQLDataType getDataType() {
            return this.dataType;
        }
        
        public void setDataType(final SQLDataType x) {
            if (x != null) {
                x.setParent(this);
            }
            this.dataType = x;
        }
        
        public String getComment() {
            return this.comment;
        }
        
        public void setComment(final String comment) {
            this.comment = comment;
        }
    }
}
