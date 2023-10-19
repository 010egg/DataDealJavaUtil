// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBase;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.SQLWindow;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObject;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;

public class MySqlSelectQueryBlock extends SQLSelectQueryBlock implements MySqlObject
{
    private boolean hignPriority;
    private boolean straightJoin;
    private boolean smallResult;
    private boolean bigResult;
    private boolean bufferResult;
    private Boolean cache;
    private boolean calcFoundRows;
    private SQLName procedureName;
    private List<SQLExpr> procedureArgumentList;
    private boolean lockInShareMode;
    private SQLName forcePartition;
    
    public MySqlSelectQueryBlock() {
        this.dbType = DbType.mysql;
    }
    
    @Override
    public MySqlSelectQueryBlock clone() {
        final MySqlSelectQueryBlock x = new MySqlSelectQueryBlock();
        this.cloneTo(x);
        x.hignPriority = this.hignPriority;
        x.straightJoin = this.straightJoin;
        x.smallResult = this.smallResult;
        x.bigResult = this.bigResult;
        x.bufferResult = this.bufferResult;
        x.cache = this.cache;
        x.calcFoundRows = this.calcFoundRows;
        if (this.procedureName != null) {
            x.setProcedureName(this.procedureName.clone());
        }
        if (this.procedureArgumentList != null) {
            for (final SQLExpr arg : this.procedureArgumentList) {
                final SQLExpr arg_cloned = arg.clone();
                arg_cloned.setParent(this);
                x.procedureArgumentList.add(arg_cloned);
            }
        }
        x.lockInShareMode = this.lockInShareMode;
        return x;
    }
    
    @Override
    public int getHintsSize() {
        if (this.hints == null) {
            return 0;
        }
        return this.hints.size();
    }
    
    public boolean isLockInShareMode() {
        return this.lockInShareMode;
    }
    
    public void setLockInShareMode(final boolean lockInShareMode) {
        this.lockInShareMode = lockInShareMode;
    }
    
    public SQLName getProcedureName() {
        return this.procedureName;
    }
    
    public void setProcedureName(final SQLName procedureName) {
        this.procedureName = procedureName;
    }
    
    public List<SQLExpr> getProcedureArgumentList() {
        if (this.procedureArgumentList == null) {
            this.procedureArgumentList = new ArrayList<SQLExpr>(2);
        }
        return this.procedureArgumentList;
    }
    
    public boolean isHignPriority() {
        return this.hignPriority;
    }
    
    public void setHignPriority(final boolean hignPriority) {
        this.hignPriority = hignPriority;
    }
    
    public boolean isStraightJoin() {
        return this.straightJoin;
    }
    
    public void setStraightJoin(final boolean straightJoin) {
        this.straightJoin = straightJoin;
    }
    
    public boolean isSmallResult() {
        return this.smallResult;
    }
    
    public void setSmallResult(final boolean smallResult) {
        this.smallResult = smallResult;
    }
    
    public boolean isBigResult() {
        return this.bigResult;
    }
    
    public void setBigResult(final boolean bigResult) {
        this.bigResult = bigResult;
    }
    
    public boolean isBufferResult() {
        return this.bufferResult;
    }
    
    public void setBufferResult(final boolean bufferResult) {
        this.bufferResult = bufferResult;
    }
    
    public Boolean getCache() {
        return this.cache;
    }
    
    public void setCache(final Boolean cache) {
        this.cache = cache;
    }
    
    public boolean isCalcFoundRows() {
        return this.calcFoundRows;
    }
    
    public void setCalcFoundRows(final boolean calcFoundRows) {
        this.calcFoundRows = calcFoundRows;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final MySqlSelectQueryBlock that = (MySqlSelectQueryBlock)o;
        if (this.hignPriority != that.hignPriority) {
            return false;
        }
        if (this.straightJoin != that.straightJoin) {
            return false;
        }
        if (this.smallResult != that.smallResult) {
            return false;
        }
        if (this.bigResult != that.bigResult) {
            return false;
        }
        if (this.bufferResult != that.bufferResult) {
            return false;
        }
        if (this.calcFoundRows != that.calcFoundRows) {
            return false;
        }
        if (this.lockInShareMode != that.lockInShareMode) {
            return false;
        }
        Label_0163: {
            if (this.cache != null) {
                if (this.cache.equals(that.cache)) {
                    break Label_0163;
                }
            }
            else if (that.cache == null) {
                break Label_0163;
            }
            return false;
        }
        Label_0196: {
            if (this.procedureName != null) {
                if (this.procedureName.equals(that.procedureName)) {
                    break Label_0196;
                }
            }
            else if (that.procedureName == null) {
                break Label_0196;
            }
            return false;
        }
        Label_0231: {
            if (this.procedureArgumentList != null) {
                if (this.procedureArgumentList.equals(that.procedureArgumentList)) {
                    break Label_0231;
                }
            }
            else if (that.procedureArgumentList == null) {
                break Label_0231;
            }
            return false;
        }
        Label_0266: {
            if (this.hints != null) {
                if (this.hints.equals(that.hints)) {
                    break Label_0266;
                }
            }
            else if (that.hints == null) {
                break Label_0266;
            }
            return false;
        }
        if (this.forcePartition != null) {
            if (this.forcePartition.equals(that.forcePartition)) {
                return true;
            }
        }
        else if (that.forcePartition == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.hignPriority ? 1 : 0);
        result = 31 * result + (this.straightJoin ? 1 : 0);
        result = 31 * result + (this.smallResult ? 1 : 0);
        result = 31 * result + (this.bigResult ? 1 : 0);
        result = 31 * result + (this.bufferResult ? 1 : 0);
        result = 31 * result + ((this.cache != null) ? this.cache.hashCode() : 0);
        result = 31 * result + (this.calcFoundRows ? 1 : 0);
        result = 31 * result + ((this.procedureName != null) ? this.procedureName.hashCode() : 0);
        result = 31 * result + ((this.procedureArgumentList != null) ? this.procedureArgumentList.hashCode() : 0);
        result = 31 * result + (this.lockInShareMode ? 1 : 0);
        result = 31 * result + ((this.hints != null) ? this.hints.hashCode() : 0);
        result = 31 * result + ((this.forcePartition != null) ? this.forcePartition.hashCode() : 0);
        return result;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
            return;
        }
        super.accept0(visitor);
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (int i = 0; i < this.selectList.size(); ++i) {
                final SQLSelectItem item = this.selectList.get(i);
                if (item != null) {
                    item.accept(visitor);
                }
            }
            if (this.from != null) {
                this.from.accept(visitor);
            }
            if (this.into != null) {
                this.into.accept(visitor);
            }
            if (this.where != null) {
                this.where.accept(visitor);
            }
            if (this.startWith != null) {
                this.startWith.accept(visitor);
            }
            if (this.connectBy != null) {
                this.connectBy.accept(visitor);
            }
            if (this.groupBy != null) {
                this.groupBy.accept(visitor);
            }
            if (this.windows != null) {
                for (final SQLWindow item2 : this.windows) {
                    item2.accept(visitor);
                }
            }
            if (this.orderBy != null) {
                this.orderBy.accept(visitor);
            }
            if (this.distributeBy != null) {
                for (int i = 0; i < this.distributeBy.size(); ++i) {
                    final SQLSelectOrderByItem item3 = this.distributeBy.get(i);
                    item3.accept(visitor);
                }
            }
            if (this.sortBy != null) {
                for (int i = 0; i < this.sortBy.size(); ++i) {
                    final SQLSelectOrderByItem item3 = this.sortBy.get(i);
                    item3.accept(visitor);
                }
            }
            if (this.waitTime != null) {
                this.waitTime.accept(visitor);
            }
            if (this.limit != null) {
                this.limit.accept(visitor);
            }
            if (this.procedureName != null) {
                this.procedureName.accept(visitor);
            }
            if (this.procedureArgumentList != null) {
                for (final SQLExpr item4 : this.procedureArgumentList) {
                    item4.accept(visitor);
                }
            }
        }
        visitor.endVisit(this);
    }
    
    public SQLName getForcePartition() {
        return this.forcePartition;
    }
    
    public void setForcePartition(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.forcePartition = x;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toMySqlString(this);
    }
}
