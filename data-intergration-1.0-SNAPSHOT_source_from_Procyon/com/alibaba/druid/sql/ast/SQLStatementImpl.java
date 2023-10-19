// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.alibaba.druid.sql.SQLUtils;
import java.util.List;
import com.alibaba.druid.DbType;

public abstract class SQLStatementImpl extends SQLObjectImpl implements SQLStatement
{
    protected DbType dbType;
    protected boolean afterSemi;
    protected List<SQLCommentHint> headHints;
    
    public SQLStatementImpl() {
    }
    
    public SQLStatementImpl(final DbType dbType) {
        this.dbType = dbType;
    }
    
    @Override
    public DbType getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this, this.dbType);
    }
    
    @Override
    public String toString(final VisitorFeature... features) {
        return SQLUtils.toSQLString(this, this.dbType, null, features);
    }
    
    @Override
    public String toLowerCaseString() {
        return SQLUtils.toSQLString(this, this.dbType, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION);
    }
    
    public String toUnformattedString() {
        return SQLUtils.toSQLString(this, this.dbType, new SQLUtils.FormatOption(true, false));
    }
    
    @Override
    public String toParameterizedString() {
        return ParameterizedOutputVisitorUtils.parameterize(this, this.dbType);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    
    @Override
    public List<SQLObject> getChildren() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    
    @Override
    public boolean isAfterSemi() {
        return this.afterSemi;
    }
    
    @Override
    public void setAfterSemi(final boolean afterSemi) {
        this.afterSemi = afterSemi;
    }
    
    @Override
    public SQLStatement clone() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    
    @Override
    public List<SQLCommentHint> getHeadHintsDirect() {
        return this.headHints;
    }
    
    @Override
    public void setHeadHints(final List<SQLCommentHint> headHints) {
        this.headHints = headHints;
    }
}
