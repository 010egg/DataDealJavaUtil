// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributesImpl;

public class SQLPartition extends OracleSegmentAttributesImpl implements OracleSegmentAttributes
{
    protected SQLName name;
    protected SQLExpr subPartitionsCount;
    protected List<SQLSubPartition> subPartitions;
    protected SQLPartitionValue values;
    protected SQLExpr dataDirectory;
    protected SQLExpr indexDirectory;
    protected SQLExpr maxRows;
    protected SQLExpr minRows;
    protected SQLExpr engine;
    protected SQLExpr comment;
    protected boolean segmentCreationImmediate;
    protected boolean segmentCreationDeferred;
    private SQLObject lobStorage;
    
    public SQLPartition() {
        this.subPartitions = new ArrayList<SQLSubPartition>();
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
    
    public SQLExpr getSubPartitionsCount() {
        return this.subPartitionsCount;
    }
    
    public void setSubPartitionsCount(final SQLExpr subPartitionsCount) {
        if (subPartitionsCount != null) {
            subPartitionsCount.setParent(this);
        }
        this.subPartitionsCount = subPartitionsCount;
    }
    
    public SQLPartitionValue getValues() {
        return this.values;
    }
    
    public void setValues(final SQLPartitionValue values) {
        if (values != null) {
            values.setParent(this);
        }
        this.values = values;
    }
    
    public List<SQLSubPartition> getSubPartitions() {
        return this.subPartitions;
    }
    
    public void addSubPartition(final SQLSubPartition partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        this.subPartitions.add(partition);
    }
    
    public SQLExpr getIndexDirectory() {
        return this.indexDirectory;
    }
    
    public void setIndexDirectory(final SQLExpr indexDirectory) {
        if (indexDirectory != null) {
            indexDirectory.setParent(this);
        }
        this.indexDirectory = indexDirectory;
    }
    
    public SQLExpr getDataDirectory() {
        return this.dataDirectory;
    }
    
    public void setDataDirectory(final SQLExpr dataDirectory) {
        if (dataDirectory != null) {
            dataDirectory.setParent(this);
        }
        this.dataDirectory = dataDirectory;
    }
    
    public SQLExpr getMaxRows() {
        return this.maxRows;
    }
    
    public void setMaxRows(final SQLExpr maxRows) {
        if (maxRows != null) {
            maxRows.setParent(this);
        }
        this.maxRows = maxRows;
    }
    
    public SQLExpr getMinRows() {
        return this.minRows;
    }
    
    public void setMinRows(final SQLExpr minRows) {
        if (minRows != null) {
            minRows.setParent(this);
        }
        this.minRows = minRows;
    }
    
    public SQLExpr getEngine() {
        return this.engine;
    }
    
    public void setEngine(final SQLExpr engine) {
        if (engine != null) {
            engine.setParent(this);
        }
        this.engine = engine;
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.values);
            this.acceptChild(visitor, this.dataDirectory);
            this.acceptChild(visitor, this.indexDirectory);
            this.acceptChild(visitor, this.tablespace);
            this.acceptChild(visitor, this.maxRows);
            this.acceptChild(visitor, this.minRows);
            this.acceptChild(visitor, this.engine);
            this.acceptChild(visitor, this.comment);
            this.acceptChild(visitor, this.subPartitionsCount);
            this.acceptChild(visitor, this.storage);
            this.acceptChild(visitor, this.subPartitions);
        }
        visitor.endVisit(this);
    }
    
    public SQLObject getLobStorage() {
        return this.lobStorage;
    }
    
    public void setLobStorage(final SQLObject lobStorage) {
        if (lobStorage != null) {
            lobStorage.setParent(this);
        }
        this.lobStorage = lobStorage;
    }
    
    public boolean isSegmentCreationImmediate() {
        return this.segmentCreationImmediate;
    }
    
    public void setSegmentCreationImmediate(final boolean segmentCreationImmediate) {
        this.segmentCreationImmediate = segmentCreationImmediate;
    }
    
    public boolean isSegmentCreationDeferred() {
        return this.segmentCreationDeferred;
    }
    
    public void setSegmentCreationDeferred(final boolean segmentCreationDeferred) {
        this.segmentCreationDeferred = segmentCreationDeferred;
    }
    
    @Override
    public SQLPartition clone() {
        final SQLPartition x = new SQLPartition();
        if (this.name != null) {
            x.setName(this.name.clone());
        }
        if (this.subPartitionsCount != null) {
            x.setSubPartitionsCount(this.subPartitionsCount.clone());
        }
        for (final SQLSubPartition p : this.subPartitions) {
            final SQLSubPartition p2 = p.clone();
            p2.setParent(x);
            x.subPartitions.add(p2);
        }
        if (this.values != null) {
            x.setValues(this.values.clone());
        }
        if (this.dataDirectory != null) {
            x.setDataDirectory(this.dataDirectory.clone());
        }
        if (this.indexDirectory != null) {
            x.setDataDirectory(this.indexDirectory.clone());
        }
        if (this.maxRows != null) {
            x.setDataDirectory(this.maxRows.clone());
        }
        if (this.minRows != null) {
            x.setDataDirectory(this.minRows.clone());
        }
        if (this.engine != null) {
            x.setDataDirectory(this.engine.clone());
        }
        if (this.comment != null) {
            x.setDataDirectory(this.comment.clone());
        }
        x.segmentCreationImmediate = this.segmentCreationImmediate;
        x.segmentCreationDeferred = this.segmentCreationDeferred;
        if (this.lobStorage != null) {
            x.setLobStorage(this.lobStorage.clone());
        }
        return x;
    }
}
