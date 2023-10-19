// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.ast.AnalyzerIndexType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlAlterTableAlterFullTextIndex extends MySqlObjectImpl implements SQLAlterTableItem
{
    private SQLName indexName;
    private AnalyzerIndexType analyzerType;
    private SQLName analyzerName;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.indexName);
            this.acceptChild(visitor, this.analyzerName);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getIndexName() {
        return this.indexName;
    }
    
    public void setIndexName(final SQLName indexName) {
        this.indexName = indexName;
    }
    
    public SQLName getAnalyzerName() {
        return this.analyzerName;
    }
    
    public void setAnalyzerName(final SQLName analyzerName) {
        this.analyzerName = analyzerName;
    }
    
    public AnalyzerIndexType getAnalyzerType() {
        return this.analyzerType;
    }
    
    public void setAnalyzerType(final AnalyzerIndexType analyzerType) {
        this.analyzerType = analyzerType;
    }
}
