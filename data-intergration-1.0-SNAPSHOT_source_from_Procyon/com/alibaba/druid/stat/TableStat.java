// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import java.util.HashMap;
import com.alibaba.druid.DbType;
import java.util.Map;
import com.alibaba.druid.support.json.JSONUtils;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.FnvHash;

public class TableStat
{
    int selectCount;
    int updateCount;
    int deleteCount;
    int insertCount;
    int dropCount;
    int mergeCount;
    int createCount;
    int alterCount;
    int createIndexCount;
    int dropIndexCount;
    int referencedCount;
    int addCount;
    int addPartitionCount;
    int analyzeCount;
    
    public TableStat() {
        this.selectCount = 0;
        this.updateCount = 0;
        this.deleteCount = 0;
        this.insertCount = 0;
        this.dropCount = 0;
        this.mergeCount = 0;
        this.createCount = 0;
        this.alterCount = 0;
        this.createIndexCount = 0;
        this.dropIndexCount = 0;
        this.referencedCount = 0;
        this.addCount = 0;
        this.addPartitionCount = 0;
        this.analyzeCount = 0;
    }
    
    public int getReferencedCount() {
        return this.referencedCount;
    }
    
    public void incrementReferencedCount() {
        ++this.referencedCount;
    }
    
    public int getDropIndexCount() {
        return this.dropIndexCount;
    }
    
    public void incrementDropIndexCount() {
        ++this.dropIndexCount;
    }
    
    public void incrementAddCount() {
        ++this.addCount;
    }
    
    public int getAddCount() {
        return this.addCount;
    }
    
    public void incrementAddPartitionCount() {
        ++this.addPartitionCount;
    }
    
    public int getAddPartitionCount() {
        return this.addPartitionCount;
    }
    
    public int getCreateIndexCount() {
        return this.createIndexCount;
    }
    
    public void incrementCreateIndexCount() {
        ++this.createIndexCount;
    }
    
    public int getAlterCount() {
        return this.alterCount;
    }
    
    public void incrementAlterCount() {
        ++this.alterCount;
    }
    
    public int getCreateCount() {
        return this.createCount;
    }
    
    public void incrementCreateCount() {
        ++this.createCount;
    }
    
    public int getMergeCount() {
        return this.mergeCount;
    }
    
    public void incrementMergeCount() {
        ++this.mergeCount;
    }
    
    public int getDropCount() {
        return this.dropCount;
    }
    
    public void incrementDropCount() {
        ++this.dropCount;
    }
    
    public void setDropCount(final int dropCount) {
        this.dropCount = dropCount;
    }
    
    public int getSelectCount() {
        return this.selectCount;
    }
    
    public void incrementSelectCount() {
        ++this.selectCount;
    }
    
    public void setSelectCount(final int selectCount) {
        this.selectCount = selectCount;
    }
    
    public int getUpdateCount() {
        return this.updateCount;
    }
    
    public void incrementUpdateCount() {
        ++this.updateCount;
    }
    
    public void setUpdateCount(final int updateCount) {
        this.updateCount = updateCount;
    }
    
    public int getDeleteCount() {
        return this.deleteCount;
    }
    
    public void incrementDeleteCount() {
        ++this.deleteCount;
    }
    
    public void setDeleteCount(final int deleteCount) {
        this.deleteCount = deleteCount;
    }
    
    public void incrementInsertCount() {
        ++this.insertCount;
    }
    
    public int getInsertCount() {
        return this.insertCount;
    }
    
    public void setInsertCount(final int insertCount) {
        this.insertCount = insertCount;
    }
    
    public int getAnalyzeCount() {
        return this.analyzeCount;
    }
    
    public void incrementAnalyzeCount() {
        ++this.analyzeCount;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(4);
        if (this.mergeCount > 0) {
            buf.append("Merge");
        }
        if (this.insertCount > 0) {
            buf.append("Insert");
        }
        if (this.updateCount > 0) {
            buf.append("Update");
        }
        if (this.selectCount > 0) {
            buf.append("Select");
        }
        if (this.deleteCount > 0) {
            buf.append("Delete");
        }
        if (this.dropCount > 0) {
            buf.append("Drop");
        }
        if (this.createCount > 0) {
            buf.append("Create");
        }
        if (this.alterCount > 0) {
            buf.append("Alter");
        }
        if (this.createIndexCount > 0) {
            buf.append("CreateIndex");
        }
        if (this.dropIndexCount > 0) {
            buf.append("DropIndex");
        }
        if (this.addCount > 0) {
            buf.append("Add");
        }
        if (this.addPartitionCount > 0) {
            buf.append("AddPartition");
        }
        if (this.analyzeCount > 0) {
            buf.append("Analyze");
        }
        return buf.toString();
    }
    
    public static class Name
    {
        private final String name;
        private final long hashCode64;
        
        public Name(final String name) {
            this(name, FnvHash.hashCode64(name));
        }
        
        public Name(final String name, final long hashCode64) {
            this.name = name;
            this.hashCode64 = hashCode64;
        }
        
        public String getName() {
            return this.name;
        }
        
        @Override
        public int hashCode() {
            final long value = this.hashCode64();
            return (int)(value ^ value >>> 32);
        }
        
        public long hashCode64() {
            return this.hashCode64;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Name)) {
                return false;
            }
            final Name other = (Name)o;
            return this.hashCode64 == other.hashCode64;
        }
        
        @Override
        public String toString() {
            return SQLUtils.normalize(this.name);
        }
    }
    
    public static class Relationship
    {
        private Column left;
        private Column right;
        private String operator;
        
        public Relationship(final Column left, final Column right, final String operator) {
            this.left = left;
            this.right = right;
            this.operator = operator;
        }
        
        public Column getLeft() {
            return this.left;
        }
        
        public Column getRight() {
            return this.right;
        }
        
        public String getOperator() {
            return this.operator;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.left == null) ? 0 : this.left.hashCode());
            result = 31 * result + ((this.operator == null) ? 0 : this.operator.hashCode());
            result = 31 * result + ((this.right == null) ? 0 : this.right.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final Relationship other = (Relationship)obj;
            if (this.left == null) {
                if (other.left != null) {
                    return false;
                }
            }
            else if (!this.left.equals(other.left)) {
                return false;
            }
            if (this.operator == null) {
                if (other.operator != null) {
                    return false;
                }
            }
            else if (!this.operator.equals(other.operator)) {
                return false;
            }
            if (this.right == null) {
                if (other.right != null) {
                    return false;
                }
            }
            else if (!this.right.equals(other.right)) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            return this.left + " " + this.operator + " " + this.right;
        }
    }
    
    public static class Condition
    {
        private final Column column;
        private final String operator;
        private final List<Object> values;
        
        public Condition(final Column column, final String operator) {
            this.values = new ArrayList<Object>();
            this.column = column;
            this.operator = operator;
        }
        
        public Column getColumn() {
            return this.column;
        }
        
        public String getOperator() {
            return this.operator;
        }
        
        public List<Object> getValues() {
            return this.values;
        }
        
        public void addValue(final Object value) {
            this.values.add(value);
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.column == null) ? 0 : this.column.hashCode());
            result = 31 * result + ((this.operator == null) ? 0 : this.operator.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final Condition other = (Condition)obj;
            if (this.column == null) {
                if (other.column != null) {
                    return false;
                }
            }
            else if (!this.column.equals(other.column)) {
                return false;
            }
            if (this.operator == null) {
                if (other.operator != null) {
                    return false;
                }
            }
            else if (!this.operator.equals(other.operator)) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            final StringBuilder buf = new StringBuilder();
            buf.append(this.column.toString());
            buf.append(' ');
            buf.append(this.operator);
            if (this.values.size() == 1) {
                buf.append(' ');
                buf.append(String.valueOf(this.values.get(0)));
            }
            else if (this.values.size() > 0) {
                buf.append(" (");
                for (int i = 0; i < this.values.size(); ++i) {
                    if (i != 0) {
                        buf.append(", ");
                    }
                    final Object val = this.values.get(i);
                    if (val instanceof String) {
                        final String jsonStr = JSONUtils.toJSONString(val);
                        buf.append(jsonStr);
                    }
                    else {
                        buf.append(String.valueOf(val));
                    }
                }
                buf.append(")");
            }
            return buf.toString();
        }
    }
    
    public static class Column
    {
        private final String table;
        private final String name;
        private final long hashCode64;
        private boolean where;
        private boolean select;
        private boolean groupBy;
        private boolean having;
        private boolean join;
        private boolean primaryKey;
        private boolean unique;
        private boolean update;
        private Map<String, Object> attributes;
        private transient String fullName;
        private String dataType;
        
        public Column(final String table, final String name) {
            this(table, name, null);
        }
        
        public Column(final String table, final String name, DbType dbType) {
            this.attributes = new HashMap<String, Object>();
            this.table = table;
            this.name = name;
            final int p = table.indexOf(46);
            if (p != -1) {
                if (dbType == null) {
                    if (table.indexOf(96) != -1) {
                        dbType = DbType.mysql;
                    }
                    else if (table.indexOf(91) != -1) {
                        dbType = DbType.sqlserver;
                    }
                    else if (table.indexOf(64) != -1) {
                        dbType = DbType.oracle;
                    }
                }
                final SQLExpr owner = SQLUtils.toSQLExpr(table, dbType);
                this.hashCode64 = new SQLPropertyExpr(owner, name).hashCode64();
            }
            else {
                this.hashCode64 = FnvHash.hashCode64(table, name);
            }
        }
        
        public Column(final String table, final String name, final long hashCode64) {
            this.attributes = new HashMap<String, Object>();
            this.table = table;
            this.name = name;
            this.hashCode64 = hashCode64;
        }
        
        public String getTable() {
            return this.table;
        }
        
        public String getFullName() {
            if (this.fullName == null) {
                if (this.table == null) {
                    this.fullName = this.name;
                }
                else {
                    this.fullName = this.table + '.' + this.name;
                }
            }
            return this.fullName;
        }
        
        public long hashCode64() {
            return this.hashCode64;
        }
        
        public boolean isWhere() {
            return this.where;
        }
        
        public void setWhere(final boolean where) {
            this.where = where;
        }
        
        public boolean isSelect() {
            return this.select;
        }
        
        public void setSelec(final boolean select) {
            this.select = select;
        }
        
        public boolean isGroupBy() {
            return this.groupBy;
        }
        
        public void setGroupBy(final boolean groupBy) {
            this.groupBy = groupBy;
        }
        
        public boolean isHaving() {
            return this.having;
        }
        
        public boolean isJoin() {
            return this.join;
        }
        
        public void setJoin(final boolean join) {
            this.join = join;
        }
        
        public void setHaving(final boolean having) {
            this.having = having;
        }
        
        public boolean isPrimaryKey() {
            return this.primaryKey;
        }
        
        public void setPrimaryKey(final boolean primaryKey) {
            this.primaryKey = primaryKey;
        }
        
        public boolean isUnique() {
            return this.unique;
        }
        
        public void setUnique(final boolean unique) {
            this.unique = unique;
        }
        
        public boolean isUpdate() {
            return this.update;
        }
        
        public void setUpdate(final boolean update) {
            this.update = update;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getDataType() {
            return this.dataType;
        }
        
        public void setDataType(final String dataType) {
            this.dataType = dataType;
        }
        
        public Map<String, Object> getAttributes() {
            return this.attributes;
        }
        
        public void setAttributes(final Map<String, Object> attributes) {
            this.attributes = attributes;
        }
        
        @Override
        public int hashCode() {
            final long hash = this.hashCode64();
            return (int)(hash ^ hash >>> 32);
        }
        
        @Override
        public String toString() {
            if (this.table != null) {
                return SQLUtils.normalize(this.table) + "." + SQLUtils.normalize(this.name);
            }
            return SQLUtils.normalize(this.name);
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof Column)) {
                return false;
            }
            final Column column = (Column)obj;
            return this.hashCode64 == column.hashCode64;
        }
    }
    
    public enum Mode
    {
        Insert(1), 
        Update(2), 
        Delete(4), 
        Select(8), 
        Merge(16), 
        Truncate(32), 
        Alter(64), 
        Drop(128), 
        DropIndex(256), 
        CreateIndex(512), 
        Replace(1024), 
        DESC(2048);
        
        public final int mark;
        
        private Mode(final int mark) {
            this.mark = mark;
        }
    }
}
