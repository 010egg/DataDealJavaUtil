// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleSupplementalLogGrp extends OracleSQLObjectImpl implements SQLTableElement
{
    private SQLName group;
    private List<SQLName> columns;
    private boolean always;
    
    public OracleSupplementalLogGrp() {
        this.columns = new ArrayList<SQLName>();
        this.always = false;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.group);
            this.acceptChild(visitor, this.columns);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getGroup() {
        return this.group;
    }
    
    public void setGroup(final SQLName group) {
        if (group != null) {
            group.setParent(this);
        }
        this.group = group;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLName column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }
    
    public boolean isAlways() {
        return this.always;
    }
    
    public void setAlways(final boolean always) {
        this.always = always;
    }
    
    @Override
    public OracleSupplementalLogGrp clone() {
        final OracleSupplementalLogGrp x = new OracleSupplementalLogGrp();
        if (this.group != null) {
            x.setGroup(this.group.clone());
        }
        for (final SQLName column : this.columns) {
            final SQLName c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        x.always = this.always;
        return x;
    }
}
