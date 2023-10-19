// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.clause;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class PartitionExtensionClause extends OracleSQLObjectImpl
{
    private boolean subPartition;
    private SQLName partition;
    private final List<SQLName> target;
    
    public PartitionExtensionClause() {
        this.target = new ArrayList<SQLName>();
    }
    
    public boolean isSubPartition() {
        return this.subPartition;
    }
    
    public void setSubPartition(final boolean subPartition) {
        this.subPartition = subPartition;
    }
    
    public SQLName getPartition() {
        return this.partition;
    }
    
    public void setPartition(final SQLName partition) {
        this.partition = partition;
    }
    
    public List<SQLName> getFor() {
        return this.target;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partition);
            this.acceptChild(visitor, this.target);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public PartitionExtensionClause clone() {
        final PartitionExtensionClause x = new PartitionExtensionClause();
        x.subPartition = this.subPartition;
        if (this.partition != null) {
            x.setPartition(this.partition.clone());
        }
        for (final SQLName item : this.target) {
            final SQLName item2 = item.clone();
            item2.setParent(x);
            x.target.add(item2);
        }
        return x;
    }
}
