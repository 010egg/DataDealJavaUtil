// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;

public class MysqlCreateFullTextAnalyzerStatement extends MySqlStatementImpl
{
    private SQLName name;
    private String tokenizer;
    private List<String> charfilters;
    private List<String> tokenizers;
    
    public MysqlCreateFullTextAnalyzerStatement() {
        this.charfilters = new ArrayList<String>();
        this.tokenizers = new ArrayList<String>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public String getTokenizer() {
        return this.tokenizer;
    }
    
    public void setTokenizer(final String tokenizer) {
        this.tokenizer = tokenizer;
    }
    
    public List<String> getCharfilters() {
        return this.charfilters;
    }
    
    public List<String> getTokenizers() {
        return this.tokenizers;
    }
}
