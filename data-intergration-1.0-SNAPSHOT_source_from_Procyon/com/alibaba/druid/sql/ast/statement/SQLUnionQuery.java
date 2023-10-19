// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLDbTypedObject;

public class SQLUnionQuery extends SQLSelectQueryBase implements SQLDbTypedObject
{
    private List<SQLSelectQuery> relations;
    private SQLUnionOperator operator;
    private SQLOrderBy orderBy;
    private SQLLimit limit;
    private DbType dbType;
    
    public SQLUnionOperator getOperator() {
        return this.operator;
    }
    
    public void setOperator(final SQLUnionOperator operator) {
        this.operator = operator;
    }
    
    public SQLUnionQuery() {
        this.relations = new ArrayList<SQLSelectQuery>();
        this.operator = SQLUnionOperator.UNION;
    }
    
    public SQLUnionQuery(final DbType dbType) {
        this.relations = new ArrayList<SQLSelectQuery>();
        this.operator = SQLUnionOperator.UNION;
        this.dbType = dbType;
    }
    
    public SQLUnionQuery(final SQLSelectQuery left, final SQLUnionOperator operator, final SQLSelectQuery right) {
        this.relations = new ArrayList<SQLSelectQuery>();
        this.operator = SQLUnionOperator.UNION;
        this.setLeft(left);
        this.operator = operator;
        this.setRight(right);
    }
    
    public List<SQLSelectQuery> getRelations() {
        return this.relations;
    }
    
    public boolean isEmpty() {
        return this.relations.isEmpty();
    }
    
    public void addRelation(final SQLSelectQuery relation) {
        if (relation != null) {
            relation.setParent(this);
        }
        this.relations.add(relation);
    }
    
    public SQLSelectQuery getLeft() {
        if (this.relations.size() == 0) {
            return null;
        }
        return this.relations.get(0);
    }
    
    public void setLeft(final SQLSelectQuery left) {
        if (left != null) {
            left.setParent(this);
        }
        if (this.relations.size() == 0) {
            this.relations.add(left);
        }
        else {
            if (this.relations.size() > 2) {
                throw new UnsupportedOperationException("multi-union");
            }
            this.relations.set(0, left);
        }
    }
    
    public SQLSelectQuery getRight() {
        if (this.relations.size() < 2) {
            return null;
        }
        if (this.relations.size() == 2) {
            return this.relations.get(1);
        }
        throw new UnsupportedOperationException("multi-union");
    }
    
    public void setRight(final SQLSelectQuery right) {
        if (right != null) {
            right.setParent(this);
        }
        if (this.relations.size() == 0) {
            this.relations.add(null);
        }
        if (this.relations.size() == 1) {
            this.relations.add(right);
        }
        else {
            if (this.relations.size() != 2) {
                throw new UnsupportedOperationException("multi-union");
            }
            this.relations.set(1, right);
        }
    }
    
    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final SQLOrderBy orderBy) {
        if (orderBy != null) {
            orderBy.setParent(this);
        }
        this.orderBy = orderBy;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (final SQLSelectQuery relation : this.relations) {
                relation.accept(visitor);
            }
            if (this.orderBy != null) {
                this.orderBy.accept(visitor);
            }
            if (this.limit != null) {
                this.limit.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    public SQLLimit getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLLimit limit) {
        if (limit != null) {
            limit.setParent(this);
        }
        this.limit = limit;
    }
    
    @Override
    public SQLUnionQuery clone() {
        final SQLUnionQuery x = new SQLUnionQuery();
        x.parenthesized = this.parenthesized;
        for (final SQLSelectQuery relation : this.relations) {
            final SQLSelectQuery r = relation.clone();
            r.setParent(x);
            x.relations.add(r);
        }
        x.operator = this.operator;
        if (this.orderBy != null) {
            x.setOrderBy(this.orderBy.clone());
        }
        if (this.limit != null) {
            x.setLimit(this.limit.clone());
        }
        x.dbType = this.dbType;
        return x;
    }
    
    public SQLSelectQueryBlock getFirstQueryBlock() {
        final SQLSelectQuery left = this.getLeft();
        if (left instanceof SQLSelectQueryBlock) {
            return (SQLSelectQueryBlock)left;
        }
        if (left instanceof SQLUnionQuery) {
            return ((SQLUnionQuery)left).getFirstQueryBlock();
        }
        return null;
    }
    
    @Override
    public DbType getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    public boolean replace(final SQLSelectQuery cmp, final SQLSelectQuery target) {
        for (int i = 0; i < this.relations.size(); ++i) {
            final SQLSelectQuery r = this.relations.get(i);
            if (r == cmp) {
                if (cmp != null) {
                    cmp.setParent(this);
                }
                this.relations.set(i, cmp);
                return true;
            }
        }
        return false;
    }
    
    public List<SQLSelectQuery> getChildren() {
        final boolean bracket = this.parenthesized && !(this.parent instanceof SQLUnionQueryTableSource);
        if (this.relations.size() > 2) {
            return this.relations;
        }
        final SQLSelectQuery left = this.getLeft();
        final SQLSelectQuery right = this.getRight();
        if (!bracket && left instanceof SQLUnionQuery && ((SQLUnionQuery)left).getOperator() == this.operator && !right.isParenthesized() && this.orderBy == null) {
            SQLUnionQuery leftUnion = (SQLUnionQuery)left;
            final ArrayList<SQLSelectQuery> rights = new ArrayList<SQLSelectQuery>();
            rights.add(right);
            SQLSelectQuery leftLeft;
            SQLSelectQuery leftRight;
            while (true) {
                leftLeft = leftUnion.getLeft();
                leftRight = leftUnion.getRight();
                if (leftUnion.isParenthesized() || leftUnion.getOrderBy() != null || leftLeft.isParenthesized() || leftRight.isParenthesized() || !(leftLeft instanceof SQLUnionQuery) || ((SQLUnionQuery)leftLeft).getOperator() != this.operator) {
                    break;
                }
                rights.add(leftRight);
                leftUnion = (SQLUnionQuery)leftLeft;
            }
            rights.add(leftRight);
            rights.add(leftLeft);
            Collections.reverse(rights);
            return rights;
        }
        return Arrays.asList(left, right);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLUnionQuery that = (SQLUnionQuery)o;
        if (this.parenthesized != that.parenthesized) {
            return false;
        }
        Label_0077: {
            if (this.relations != null) {
                if (this.relations.equals(that.relations)) {
                    break Label_0077;
                }
            }
            else if (that.relations == null) {
                break Label_0077;
            }
            return false;
        }
        if (this.operator != that.operator) {
            return false;
        }
        Label_0123: {
            if (this.orderBy != null) {
                if (this.orderBy.equals(that.orderBy)) {
                    break Label_0123;
                }
            }
            else if (that.orderBy == null) {
                break Label_0123;
            }
            return false;
        }
        if (this.limit != null) {
            if (this.limit.equals(that.limit)) {
                return this.dbType == that.dbType;
            }
        }
        else if (that.limit == null) {
            return this.dbType == that.dbType;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.parenthesized ? 1 : 0;
        result = 31 * result + ((this.relations != null) ? this.relations.hashCode() : 0);
        result = 31 * result + ((this.operator != null) ? this.operator.hashCode() : 0);
        result = 31 * result + ((this.orderBy != null) ? this.orderBy.hashCode() : 0);
        result = 31 * result + ((this.limit != null) ? this.limit.hashCode() : 0);
        result = 31 * result + ((this.dbType != null) ? this.dbType.hashCode() : 0);
        return result;
    }
}
