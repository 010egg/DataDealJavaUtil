// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;

public class SQLCharacterDataType extends SQLDataTypeImpl
{
    private String charSetName;
    private String collate;
    private String charType;
    private boolean hasBinary;
    public List<SQLCommentHint> hints;
    public static final String CHAR_TYPE_BYTE = "BYTE";
    public static final String CHAR_TYPE_CHAR = "CHAR";
    
    public SQLCharacterDataType(final String name) {
        super(name);
    }
    
    public SQLCharacterDataType(final String name, final int precision) {
        super(name, precision);
    }
    
    public String getCharSetName() {
        return this.charSetName;
    }
    
    public void setCharSetName(final String charSetName) {
        this.charSetName = charSetName;
    }
    
    public boolean isHasBinary() {
        return this.hasBinary;
    }
    
    public void setHasBinary(final boolean hasBinary) {
        this.hasBinary = hasBinary;
    }
    
    public String getCollate() {
        return this.collate;
    }
    
    public void setCollate(final String collate) {
        this.collate = collate;
    }
    
    public String getCharType() {
        return this.charType;
    }
    
    public void setCharType(final String charType) {
        this.charType = charType;
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    public int getLength() {
        if (this.arguments.size() == 1) {
            final SQLExpr arg = this.arguments.get(0);
            if (arg instanceof SQLIntegerExpr) {
                return ((SQLIntegerExpr)arg).getNumber().intValue();
            }
        }
        return -1;
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
    public SQLCharacterDataType clone() {
        final SQLCharacterDataType x = new SQLCharacterDataType(this.getName());
        super.cloneTo(x);
        x.charSetName = this.charSetName;
        x.collate = this.collate;
        x.charType = this.charType;
        x.hasBinary = this.hasBinary;
        return x;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this);
    }
    
    @Override
    public int jdbcType() {
        final long nameNash = this.nameHashCode64();
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
        return 1111;
    }
}
