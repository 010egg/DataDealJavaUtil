// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;

public class OracleCreatePackageStatement extends OracleStatementImpl implements SQLCreateStatement
{
    private boolean orReplace;
    private SQLName name;
    private boolean body;
    private final List<SQLStatement> statements;
    
    public OracleCreatePackageStatement() {
        this.statements = new ArrayList<SQLStatement>();
        super.setDbType(DbType.oracle);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.statements);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public OracleCreatePackageStatement clone() {
        final OracleCreatePackageStatement x = new OracleCreatePackageStatement();
        x.orReplace = this.orReplace;
        if (this.name != null) {
            x.setName(this.name.clone());
        }
        x.body = this.body;
        for (final SQLStatement stmt : this.statements) {
            final SQLStatement s2 = stmt.clone();
            s2.setParent(x);
            x.statements.add(s2);
        }
        return x;
    }
    
    public boolean isOrReplace() {
        return this.orReplace;
    }
    
    public void setOrReplace(final boolean orReplace) {
        this.orReplace = orReplace;
    }
    
    public boolean isBody() {
        return this.body;
    }
    
    public void setBody(final boolean body) {
        this.body = body;
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
    
    public List<SQLStatement> getStatements() {
        return this.statements;
    }
}
