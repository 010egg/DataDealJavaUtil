// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;

public class MySqlShowProfileStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private List<Type> types;
    private SQLExpr forQuery;
    private SQLLimit limit;
    
    public MySqlShowProfileStatement() {
        this.types = new ArrayList<Type>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public List<Type> getTypes() {
        return this.types;
    }
    
    public SQLExpr getForQuery() {
        return this.forQuery;
    }
    
    public void setForQuery(final SQLExpr forQuery) {
        this.forQuery = forQuery;
    }
    
    public SQLLimit getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLLimit limit) {
        this.limit = limit;
    }
    
    public enum Type
    {
        ALL("ALL"), 
        BLOCK_IO("BLOCK IO"), 
        CONTEXT_SWITCHES("CONTEXT SWITCHES"), 
        CPU("CPU"), 
        IPC("IPC"), 
        MEMORY("MEMORY"), 
        PAGE_FAULTS("PAGE FAULTS"), 
        SOURCE("SOURCE"), 
        SWAPS("SWAPS");
        
        public final String name;
        
        private Type(final String name) {
            this.name = name;
        }
    }
}
