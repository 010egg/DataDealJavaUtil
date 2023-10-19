// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLAnnIndex;
import com.alibaba.druid.sql.ast.AutoIncrementType;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLDbTypedObject;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLObjectWithDataType;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLColumnDefinition extends SQLObjectImpl implements SQLTableElement, SQLObjectWithDataType, SQLReplaceable, SQLDbTypedObject
{
    protected DbType dbType;
    protected SQLName name;
    protected SQLDataType dataType;
    protected SQLExpr defaultExpr;
    protected final List<SQLColumnConstraint> constraints;
    protected SQLExpr comment;
    protected Boolean enable;
    protected Boolean validate;
    protected Boolean rely;
    protected boolean autoIncrement;
    protected SQLExpr onUpdate;
    protected SQLExpr format;
    protected SQLExpr storage;
    protected SQLExpr charsetExpr;
    protected SQLExpr collateExpr;
    protected SQLExpr asExpr;
    protected boolean stored;
    protected boolean virtual;
    protected boolean visible;
    protected AutoIncrementType sequenceType;
    protected boolean preSort;
    protected int preSortOrder;
    protected Identity identity;
    protected SQLExpr generatedAlawsAs;
    protected SQLExpr delimiter;
    protected SQLExpr delimiterTokenizer;
    protected SQLExpr nlpTokenizer;
    protected SQLExpr valueType;
    protected boolean disableIndex;
    protected SQLExpr jsonIndexAttrsExpr;
    protected SQLAnnIndex annIndex;
    private SQLExpr unitCount;
    private SQLExpr unitIndex;
    private SQLExpr step;
    private SQLCharExpr encode;
    private SQLCharExpr compression;
    private List<SQLAssignItem> mappedBy;
    private List<SQLAssignItem> colProperties;
    
    public SQLColumnDefinition() {
        this.constraints = new ArrayList<SQLColumnConstraint>(0);
        this.autoIncrement = false;
        this.stored = false;
        this.virtual = false;
        this.visible = false;
        this.preSort = false;
        this.preSortOrder = 0;
        this.disableIndex = false;
    }
    
    public Identity getIdentity() {
        return this.identity;
    }
    
    public void setIdentity(final Identity identity) {
        if (identity != null) {
            identity.setParent(this);
        }
        this.identity = identity;
    }
    
    public Boolean getEnable() {
        return this.enable;
    }
    
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    public Boolean getValidate() {
        return this.validate;
    }
    
    public void setValidate(final Boolean validate) {
        this.validate = validate;
    }
    
    public Boolean getRely() {
        return this.rely;
    }
    
    public void setRely(final Boolean rely) {
        this.rely = rely;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public String getColumnName() {
        if (this.name == null) {
            return null;
        }
        return this.name.getSimpleName();
    }
    
    public long nameHashCode64() {
        if (this.name == null) {
            return 0L;
        }
        return this.name.hashCode64();
    }
    
    public String getNameAsString() {
        if (this.name == null) {
            return null;
        }
        return this.name.toString();
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public void setName(final String name) {
        this.setName(new SQLIdentifierExpr(name));
    }
    
    @Override
    public SQLDataType getDataType() {
        return this.dataType;
    }
    
    public int jdbcType() {
        if (this.dataType == null) {
            return 1111;
        }
        return this.dataType.jdbcType();
    }
    
    @Override
    public void setDataType(final SQLDataType dataType) {
        if (dataType != null) {
            dataType.setParent(this);
        }
        this.dataType = dataType;
    }
    
    public SQLExpr getDefaultExpr() {
        return this.defaultExpr;
    }
    
    public void setDefaultExpr(final SQLExpr defaultExpr) {
        if (defaultExpr != null) {
            defaultExpr.setParent(this);
        }
        this.defaultExpr = defaultExpr;
    }
    
    public List<SQLColumnConstraint> getConstraints() {
        return this.constraints;
    }
    
    public boolean isPrimaryKey() {
        for (final SQLColumnConstraint constraint : this.constraints) {
            if (constraint instanceof SQLColumnPrimaryKey) {
                return true;
            }
        }
        return this.parent instanceof SQLCreateTableStatement && ((SQLCreateTableStatement)this.parent).isPrimaryColumn(this.nameHashCode64());
    }
    
    public boolean isOnlyPrimaryKey() {
        for (final SQLColumnConstraint constraint : this.constraints) {
            if (constraint instanceof SQLColumnPrimaryKey) {
                return true;
            }
        }
        return this.parent instanceof SQLCreateTableStatement && ((SQLCreateTableStatement)this.parent).isPrimaryColumn(this.nameHashCode64());
    }
    
    public boolean isPartitionBy() {
        if (!(this.parent instanceof SQLCreateTableStatement)) {
            return false;
        }
        final SQLCreateTableStatement stmt = (SQLCreateTableStatement)this.parent;
        final SQLPartitionBy partitioning = stmt.getPartitioning();
        return partitioning != null && this.name != null && partitioning.isPartitionByColumn(this.nameHashCode64());
    }
    
    public void addConstraint(final SQLColumnConstraint constraint) {
        if (constraint != null) {
            constraint.setParent(this);
        }
        this.constraints.add(constraint);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.dataType);
            this.acceptChild(visitor, this.defaultExpr);
            this.acceptChild(visitor, this.constraints);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final String comment) {
        SQLCharExpr expr;
        if (comment == null) {
            expr = null;
        }
        else {
            expr = new SQLCharExpr(comment);
        }
        this.setComment(expr);
    }
    
    public void setComment(final SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
    
    public boolean isVirtual() {
        return this.virtual;
    }
    
    public void setVirtual(final boolean virtual) {
        this.virtual = virtual;
    }
    
    public boolean isStored() {
        return this.stored;
    }
    
    public void setStored(final boolean stored) {
        this.stored = stored;
    }
    
    public SQLExpr getCharsetExpr() {
        return this.charsetExpr;
    }
    
    public void setCharsetExpr(final SQLExpr charsetExpr) {
        if (charsetExpr != null) {
            charsetExpr.setParent(this);
        }
        this.charsetExpr = charsetExpr;
    }
    
    public SQLExpr getCollateExpr() {
        return this.collateExpr;
    }
    
    public void setCollateExpr(final SQLExpr x) {
        if (this.charsetExpr != null) {
            this.charsetExpr.setParent(this);
        }
        this.collateExpr = x;
    }
    
    public SQLExpr getAsExpr() {
        return this.asExpr;
    }
    
    public void setAsExpr(final SQLExpr asExpr) {
        if (this.charsetExpr != null) {
            this.charsetExpr.setParent(this);
        }
        this.asExpr = asExpr;
    }
    
    public boolean isAutoIncrement() {
        return this.autoIncrement;
    }
    
    public void setAutoIncrement(final boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
    
    public SQLExpr getOnUpdate() {
        return this.onUpdate;
    }
    
    public void setOnUpdate(final SQLExpr onUpdate) {
        this.onUpdate = onUpdate;
    }
    
    public SQLExpr getFormat() {
        return this.format;
    }
    
    public void setFormat(final SQLExpr format) {
        this.format = format;
    }
    
    public SQLExpr getStorage() {
        return this.storage;
    }
    
    public void setStorage(final SQLExpr storage) {
        this.storage = storage;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.defaultExpr == expr) {
            this.setDefaultExpr(target);
            return true;
        }
        if (this.name == expr) {
            this.setName((SQLName)target);
            return true;
        }
        if (this.comment == expr) {
            this.setComment(target);
            return true;
        }
        return false;
    }
    
    public void setUnitCount(final SQLExpr unitCount) {
        if (unitCount != null) {
            unitCount.setParent(this);
        }
        this.unitCount = unitCount;
    }
    
    public String computeAlias() {
        String alias = null;
        if (this.name instanceof SQLIdentifierExpr) {
            alias = ((SQLIdentifierExpr)this.name).getName();
        }
        else if (this.name instanceof SQLPropertyExpr) {
            alias = ((SQLPropertyExpr)this.name).getName();
        }
        return SQLUtils.normalize(alias);
    }
    
    @Override
    public SQLColumnDefinition clone() {
        final SQLColumnDefinition x = new SQLColumnDefinition();
        x.setDbType(this.dbType);
        if (this.name != null) {
            x.setName(this.name.clone());
        }
        if (this.dataType != null) {
            x.setDataType(this.dataType.clone());
        }
        if (this.defaultExpr != null) {
            x.setDefaultExpr(this.defaultExpr.clone());
        }
        for (final SQLColumnConstraint item : this.constraints) {
            final SQLColumnConstraint itemCloned = item.clone();
            itemCloned.setParent(x);
            x.constraints.add(itemCloned);
        }
        if (this.comment != null) {
            x.setComment(this.comment.clone());
        }
        x.enable = this.enable;
        x.validate = this.validate;
        x.rely = this.rely;
        x.autoIncrement = this.autoIncrement;
        if (this.onUpdate != null) {
            x.setOnUpdate(this.onUpdate.clone());
        }
        if (this.format != null) {
            x.setFormat(this.format.clone());
        }
        if (this.storage != null) {
            x.setStorage(this.storage.clone());
        }
        if (this.charsetExpr != null) {
            x.setCharsetExpr(this.charsetExpr.clone());
        }
        if (this.collateExpr != null) {
            x.setCollateExpr(this.collateExpr.clone());
        }
        if (this.asExpr != null) {
            x.setAsExpr(this.asExpr.clone());
        }
        x.stored = this.stored;
        x.virtual = this.virtual;
        if (this.identity != null) {
            x.setIdentity(this.identity.clone());
        }
        if (this.delimiter != null) {
            x.setDelimiter(this.delimiter.clone());
        }
        if (this.valueType != null) {
            x.setValueType(this.valueType.clone());
        }
        if (this.nlpTokenizer != null) {
            x.setNplTokenizer(this.nlpTokenizer.clone());
        }
        x.preSort = this.preSort;
        x.preSortOrder = this.preSortOrder;
        if (this.jsonIndexAttrsExpr != null) {
            x.setJsonIndexAttrsExpr(this.jsonIndexAttrsExpr.clone());
        }
        if (this.annIndex != null) {
            x.setAnnIndex(this.annIndex.clone());
        }
        if (this.mappedBy != null) {
            for (final SQLAssignItem item2 : this.mappedBy) {
                final SQLAssignItem item3 = item2.clone();
                item3.setParent(this);
                if (x.mappedBy == null) {
                    x.mappedBy = new ArrayList<SQLAssignItem>();
                }
                x.mappedBy.add(item3);
            }
        }
        if (this.colProperties != null) {
            for (final SQLAssignItem item2 : this.colProperties) {
                final SQLAssignItem item3 = item2.clone();
                item3.setParent(this);
                if (x.colProperties == null) {
                    x.colProperties = new ArrayList<SQLAssignItem>();
                }
                x.colProperties.add(item3);
            }
        }
        return x;
    }
    
    @Override
    public DbType getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    public void simplify() {
        this.enable = null;
        this.validate = null;
        this.rely = null;
        if (this.name instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)this.name;
            final String columnName = identExpr.getName();
            final String normalized = SQLUtils.normalize(columnName, this.dbType);
            if (normalized != columnName) {
                this.setName(normalized);
            }
        }
    }
    
    public boolean containsNotNullConstaint() {
        for (final SQLColumnConstraint constraint : this.constraints) {
            if (constraint instanceof SQLNotNullConstraint) {
                return true;
            }
        }
        return false;
    }
    
    public SQLExpr getGeneratedAlawsAs() {
        return this.generatedAlawsAs;
    }
    
    public void setGeneratedAlawsAs(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.generatedAlawsAs = x;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public SQLExpr getDelimiter() {
        return this.delimiter;
    }
    
    public boolean isDisableIndex() {
        return this.disableIndex;
    }
    
    public void setDisableIndex(final boolean disableIndex) {
        this.disableIndex = disableIndex;
    }
    
    public void setDelimiter(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.delimiter = x;
    }
    
    public SQLExpr getDelimiterTokenizer() {
        return this.delimiterTokenizer;
    }
    
    public void setDelimiterTokenizer(final SQLExpr delimiterTokenizer) {
        this.delimiterTokenizer = delimiterTokenizer;
    }
    
    public SQLExpr getNlpTokenizer() {
        return this.nlpTokenizer;
    }
    
    public void setNlpTokenizer(final SQLExpr nlpTokenizer) {
        this.nlpTokenizer = nlpTokenizer;
    }
    
    public SQLExpr getValueType() {
        return this.valueType;
    }
    
    public void setValueType(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.valueType = x;
    }
    
    public boolean isPreSort() {
        return this.preSort;
    }
    
    public void setPreSort(final boolean preSort) {
        this.preSort = preSort;
    }
    
    public int getPreSortOrder() {
        return this.preSortOrder;
    }
    
    public void setPreSortOrder(final int preSortOrder) {
        this.preSortOrder = preSortOrder;
    }
    
    public SQLExpr getJsonIndexAttrsExpr() {
        return this.jsonIndexAttrsExpr;
    }
    
    public void setJsonIndexAttrsExpr(final SQLExpr jsonIndexAttrsExpr) {
        this.jsonIndexAttrsExpr = jsonIndexAttrsExpr;
    }
    
    public SQLAnnIndex getAnnIndex() {
        return this.annIndex;
    }
    
    public void setAnnIndex(final SQLAnnIndex x) {
        if (x != null) {
            x.setParent(this);
        }
        this.annIndex = x;
    }
    
    public AutoIncrementType getSequenceType() {
        return this.sequenceType;
    }
    
    public void setSequenceType(final AutoIncrementType sequenceType) {
        this.sequenceType = sequenceType;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this, this.dbType);
    }
    
    public SQLExpr getUnitCount() {
        return this.unitCount;
    }
    
    public SQLExpr getUnitIndex() {
        return this.unitIndex;
    }
    
    public void setUnitIndex(final SQLExpr unitIndex) {
        if (unitIndex != null) {
            unitIndex.setParent(this);
        }
        this.unitIndex = unitIndex;
    }
    
    public SQLExpr getNplTokenizer() {
        return this.nlpTokenizer;
    }
    
    public void setNplTokenizer(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.nlpTokenizer = x;
    }
    
    public SQLExpr getStep() {
        return this.step;
    }
    
    public void setStep(final SQLExpr step) {
        if (step != null) {
            step.setParent(this);
        }
        this.step = step;
    }
    
    public List<SQLAssignItem> getMappedBy() {
        if (this.mappedBy == null) {
            this.mappedBy = new ArrayList<SQLAssignItem>();
        }
        return this.mappedBy;
    }
    
    public List<SQLAssignItem> getMappedByDirect() {
        return this.mappedBy;
    }
    
    public List<SQLAssignItem> getColProperties() {
        if (this.colProperties == null) {
            this.colProperties = new ArrayList<SQLAssignItem>();
        }
        return this.colProperties;
    }
    
    public SQLCharExpr getEncode() {
        return this.encode;
    }
    
    public void setEncode(final SQLCharExpr encode) {
        this.encode = encode;
    }
    
    public SQLCharExpr getCompression() {
        return this.compression;
    }
    
    public void setCompression(final SQLCharExpr compression) {
        this.compression = compression;
    }
    
    public List<SQLAssignItem> getColPropertiesDirect() {
        return this.colProperties;
    }
    
    public static class Identity extends SQLObjectImpl
    {
        private Integer seed;
        private Integer increment;
        private boolean notForReplication;
        private boolean cycle;
        private Integer minValue;
        private Integer maxValue;
        
        public Integer getSeed() {
            return this.seed;
        }
        
        public void setSeed(final Integer seed) {
            this.seed = seed;
        }
        
        public Integer getIncrement() {
            return this.increment;
        }
        
        public void setIncrement(final Integer increment) {
            this.increment = increment;
        }
        
        public boolean isCycle() {
            return this.cycle;
        }
        
        public void setCycle(final boolean cycle) {
            this.cycle = cycle;
        }
        
        public Integer getMinValue() {
            return this.minValue;
        }
        
        public void setMinValue(final Integer minValue) {
            this.minValue = minValue;
        }
        
        public Integer getMaxValue() {
            return this.maxValue;
        }
        
        public void setMaxValue(final Integer maxValue) {
            this.maxValue = maxValue;
        }
        
        public boolean isNotForReplication() {
            return this.notForReplication;
        }
        
        public void setNotForReplication(final boolean notForReplication) {
            this.notForReplication = notForReplication;
        }
        
        public void accept0(final SQLASTVisitor visitor) {
            visitor.visit(this);
            visitor.endVisit(this);
        }
        
        @Override
        public Identity clone() {
            final Identity x = new Identity();
            x.seed = this.seed;
            x.increment = this.increment;
            x.cycle = this.cycle;
            x.minValue = this.minValue;
            x.maxValue = this.maxValue;
            x.notForReplication = this.notForReplication;
            return x;
        }
    }
}
