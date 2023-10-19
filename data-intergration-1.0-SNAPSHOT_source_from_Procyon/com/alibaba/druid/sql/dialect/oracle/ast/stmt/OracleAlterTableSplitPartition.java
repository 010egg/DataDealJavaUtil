// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;

public class OracleAlterTableSplitPartition extends OracleAlterTableItem
{
    private SQLName name;
    private List<SQLExpr> at;
    private List<SQLExpr> values;
    private List<NestedTablePartitionSpec> into;
    private UpdateIndexesClause updateIndexes;
    
    public OracleAlterTableSplitPartition() {
        this.at = new ArrayList<SQLExpr>();
        this.values = new ArrayList<SQLExpr>();
        this.into = new ArrayList<NestedTablePartitionSpec>();
        this.updateIndexes = null;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.at);
            this.acceptChild(visitor, this.values);
            this.acceptChild(visitor, this.updateIndexes);
        }
        visitor.endVisit(this);
    }
    
    public UpdateIndexesClause getUpdateIndexes() {
        return this.updateIndexes;
    }
    
    public void setUpdateIndexes(final UpdateIndexesClause updateIndexes) {
        this.updateIndexes = updateIndexes;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public List<SQLExpr> getAt() {
        return this.at;
    }
    
    public void setAt(final List<SQLExpr> at) {
        this.at = at;
    }
    
    public List<NestedTablePartitionSpec> getInto() {
        return this.into;
    }
    
    public void setInto(final List<NestedTablePartitionSpec> into) {
        this.into = into;
    }
    
    public List<SQLExpr> getValues() {
        return this.values;
    }
    
    public void setValues(final List<SQLExpr> values) {
        this.values = values;
    }
    
    public static class NestedTablePartitionSpec extends OracleSQLObjectImpl
    {
        private SQLName partition;
        private List<SQLObject> segmentAttributeItems;
        
        public NestedTablePartitionSpec() {
            this.segmentAttributeItems = new ArrayList<SQLObject>();
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.partition);
                this.acceptChild(visitor, this.segmentAttributeItems);
            }
            visitor.endVisit(this);
        }
        
        public SQLName getPartition() {
            return this.partition;
        }
        
        public void setPartition(final SQLName partition) {
            this.partition = partition;
        }
        
        public List<SQLObject> getSegmentAttributeItems() {
            return this.segmentAttributeItems;
        }
        
        public void setSegmentAttributeItems(final List<SQLObject> segmentAttributeItems) {
            this.segmentAttributeItems = segmentAttributeItems;
        }
    }
    
    public static class TableSpaceItem extends OracleSQLObjectImpl
    {
        private SQLName tablespace;
        
        public TableSpaceItem() {
        }
        
        public TableSpaceItem(final SQLName tablespace) {
            this.tablespace = tablespace;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.tablespace);
            }
            visitor.endVisit(this);
        }
        
        public SQLName getTablespace() {
            return this.tablespace;
        }
        
        public void setTablespace(final SQLName tablespace) {
            this.tablespace = tablespace;
        }
    }
    
    public static class UpdateIndexesClause extends OracleSQLObjectImpl
    {
        private List<SQLObject> items;
        
        public UpdateIndexesClause() {
            this.items = new ArrayList<SQLObject>();
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.items);
            }
            visitor.endVisit(this);
        }
        
        public List<SQLObject> getItems() {
            return this.items;
        }
        
        public void setItems(final List<SQLObject> items) {
            this.items = items;
        }
    }
}
