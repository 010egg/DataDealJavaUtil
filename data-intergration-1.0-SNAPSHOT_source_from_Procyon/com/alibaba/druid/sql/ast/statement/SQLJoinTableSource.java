// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObjectImpl;
import com.alibaba.druid.FastsqlColumnAmbiguousException;
import com.alibaba.druid.sql.repository.SchemaResolveVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.SQLUtils;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;

public class SQLJoinTableSource extends SQLTableSourceImpl implements SQLReplaceable
{
    protected SQLTableSource left;
    protected JoinType joinType;
    protected SQLTableSource right;
    protected SQLExpr condition;
    protected final List<SQLExpr> using;
    protected boolean natural;
    protected UDJ udj;
    protected boolean asof;
    protected boolean global;
    
    public SQLJoinTableSource(final String alias) {
        super(alias);
        this.using = new ArrayList<SQLExpr>();
        this.natural = false;
    }
    
    public SQLJoinTableSource() {
        this.using = new ArrayList<SQLExpr>();
        this.natural = false;
    }
    
    public SQLJoinTableSource(final SQLTableSource left, final JoinType joinType, final SQLTableSource right, final SQLExpr condition) {
        this.using = new ArrayList<SQLExpr>();
        this.natural = false;
        this.setLeft(left);
        this.setJoinType(joinType);
        this.setRight(right);
        this.setCondition(condition);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.left != null) {
                this.left.accept(visitor);
            }
            if (this.right != null) {
                this.right.accept(visitor);
            }
            if (this.condition != null) {
                this.condition.accept(visitor);
            }
            for (int i = 0; i < this.using.size(); ++i) {
                final SQLExpr item = this.using.get(i);
                if (item != null) {
                    item.accept(visitor);
                }
            }
            if (this.udj != null) {
                this.udj.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    public UDJ getUdj() {
        return this.udj;
    }
    
    public void setUdj(final UDJ x) {
        if (x != null) {
            x.setParent(this);
        }
        this.udj = x;
    }
    
    public boolean isAsof() {
        return this.asof;
    }
    
    public void setAsof(final boolean asof) {
        this.asof = asof;
    }
    
    public JoinType getJoinType() {
        return this.joinType;
    }
    
    public void setJoinType(final JoinType joinType) {
        this.joinType = joinType;
    }
    
    public void setImplicitJoinToCross() {
        if (this.joinType == JoinType.COMMA) {
            this.joinType = JoinType.CROSS_JOIN;
        }
        if (this.left instanceof SQLJoinTableSource) {
            ((SQLJoinTableSource)this.left).setImplicitJoinToCross();
        }
        if (this.right instanceof SQLJoinTableSource) {
            ((SQLJoinTableSource)this.right).setImplicitJoinToCross();
        }
    }
    
    public SQLTableSource getLeft() {
        return this.left;
    }
    
    public void setLeft(final SQLTableSource left) {
        if (left != null) {
            left.setParent(this);
        }
        this.left = left;
    }
    
    public void setLeft(final String tableName, final String alias) {
        SQLExprTableSource tableSource;
        if (tableName == null || tableName.length() == 0) {
            tableSource = null;
        }
        else {
            tableSource = new SQLExprTableSource(new SQLIdentifierExpr(tableName), alias);
        }
        this.setLeft(tableSource);
    }
    
    public void setRight(final String tableName, final String alias) {
        SQLExprTableSource tableSource;
        if (tableName == null || tableName.length() == 0) {
            tableSource = null;
        }
        else {
            tableSource = new SQLExprTableSource(new SQLIdentifierExpr(tableName), alias);
        }
        this.setRight(tableSource);
    }
    
    public SQLTableSource getRight() {
        return this.right;
    }
    
    public void setRight(final SQLTableSource right) {
        if (right != null) {
            right.setParent(this);
        }
        this.right = right;
    }
    
    public SQLExpr getCondition() {
        return this.condition;
    }
    
    public void setCondition(final SQLExpr condition) {
        if (condition != null) {
            condition.setParent(this);
        }
        this.condition = condition;
    }
    
    public void addCondition(final SQLExpr condition) {
        if (this.condition == null) {
            this.condition = condition;
            this.setImplicitJoinToCross();
            return;
        }
        this.condition = SQLBinaryOpExpr.and(this.condition, condition);
    }
    
    public void addConditionnIfAbsent(final SQLExpr condition) {
        if (this.containsCondition(condition)) {
            return;
        }
        this.condition = SQLBinaryOpExpr.and(this.condition, condition);
    }
    
    public boolean containsCondition(final SQLExpr condition) {
        return this.condition != null && !this.condition.equals(condition) && this.condition instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)this.condition).contains(condition);
    }
    
    public List<SQLExpr> getUsing() {
        return this.using;
    }
    
    public boolean isNatural() {
        return this.natural;
    }
    
    public void setNatural(final boolean natural) {
        this.natural = natural;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            this.left.output(buf);
            buf.append(' ');
            buf.append(JoinType.toString(this.joinType));
            buf.append(' ');
            this.right.output(buf);
            if (this.condition != null) {
                buf.append(" ON ");
                this.condition.output(buf);
            }
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.condition == expr) {
            this.setCondition(target);
            return true;
        }
        for (int i = 0; i < this.using.size(); ++i) {
            if (this.using.get(i) == expr) {
                target.setParent(this);
                this.using.set(i, target);
                return true;
            }
        }
        return false;
    }
    
    public boolean replace(final SQLTableSource cmp, final SQLTableSource target) {
        if (this.left == cmp) {
            if (target == null) {
                SQLUtils.replaceInParent(this, this.right);
            }
            else {
                this.setLeft(target);
            }
            return true;
        }
        if (this.right == cmp) {
            if (target == null) {
                SQLUtils.replaceInParent(this, this.left);
            }
            else {
                this.setRight(target);
            }
            return true;
        }
        return false;
    }
    
    public void cloneTo(final SQLJoinTableSource x) {
        x.alias = this.alias;
        if (this.left != null) {
            x.setLeft(this.left.clone());
        }
        x.joinType = this.joinType;
        if (this.right != null) {
            x.setRight(this.right.clone());
        }
        if (this.condition != null) {
            x.setCondition(this.condition.clone());
        }
        for (final SQLExpr item : this.using) {
            final SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.using.add(item2);
        }
        x.natural = this.natural;
        x.asof = this.asof;
        if (this.udj != null) {
            x.udj = this.udj.clone();
        }
    }
    
    @Override
    public SQLJoinTableSource clone() {
        final SQLJoinTableSource x = new SQLJoinTableSource();
        this.cloneTo(x);
        return x;
    }
    
    public void reverse() {
        final SQLTableSource temp = this.left;
        this.left = this.right;
        this.right = temp;
        if (this.left instanceof SQLJoinTableSource) {
            ((SQLJoinTableSource)this.left).reverse();
        }
        if (this.right instanceof SQLJoinTableSource) {
            ((SQLJoinTableSource)this.right).reverse();
        }
    }
    
    public void rearrangement() {
        if (this.joinType != JoinType.COMMA && this.joinType != JoinType.INNER_JOIN) {
            return;
        }
        if (this.right instanceof SQLJoinTableSource) {
            final SQLJoinTableSource rightJoin = (SQLJoinTableSource)this.right;
            if (rightJoin.joinType != JoinType.COMMA && rightJoin.joinType != JoinType.INNER_JOIN) {
                return;
            }
            final SQLTableSource a = this.left;
            final SQLTableSource b = rightJoin.getLeft();
            final SQLTableSource c = rightJoin.getRight();
            final SQLExpr on_ab = this.condition;
            SQLExpr on_bc = rightJoin.condition;
            this.setLeft(rightJoin);
            rightJoin.setLeft(a);
            rightJoin.setRight(b);
            boolean on_ab_match = false;
            if (on_ab instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr on_ab_binaryOpExpr = (SQLBinaryOpExpr)on_ab;
                if (on_ab_binaryOpExpr.getLeft() instanceof SQLPropertyExpr && on_ab_binaryOpExpr.getRight() instanceof SQLPropertyExpr) {
                    final String leftOwnerName = ((SQLPropertyExpr)on_ab_binaryOpExpr.getLeft()).getOwnernName();
                    final String rightOwnerName = ((SQLPropertyExpr)on_ab_binaryOpExpr.getRight()).getOwnernName();
                    if (rightJoin.containsAlias(leftOwnerName) && rightJoin.containsAlias(rightOwnerName)) {
                        on_ab_match = true;
                    }
                }
            }
            if (on_ab_match) {
                rightJoin.setCondition(on_ab);
            }
            else {
                rightJoin.setCondition(null);
                on_bc = SQLBinaryOpExpr.and(on_bc, on_ab);
            }
            this.setRight(c);
            this.setCondition(on_bc);
        }
    }
    
    public boolean contains(final SQLTableSource tableSource, final SQLExpr condition) {
        if (this.right.equals(tableSource)) {
            return this.condition == condition || (this.condition != null && this.condition.equals(condition));
        }
        if (this.left instanceof SQLJoinTableSource) {
            final SQLJoinTableSource joinLeft = (SQLJoinTableSource)this.left;
            if (tableSource instanceof SQLJoinTableSource) {
                final SQLJoinTableSource join = (SQLJoinTableSource)tableSource;
                if (join.right.equals(this.right) && this.condition.equals(condition) && joinLeft.right.equals(join.left)) {
                    return true;
                }
            }
            return joinLeft.contains(tableSource, condition);
        }
        return false;
    }
    
    public boolean contains(final SQLTableSource tableSource, final SQLExpr condition, final JoinType joinType) {
        if (this.right.equals(tableSource)) {
            return this.condition == condition || (this.condition != null && this.condition.equals(condition) && this.joinType == joinType);
        }
        if (this.left instanceof SQLJoinTableSource) {
            final SQLJoinTableSource joinLeft = (SQLJoinTableSource)this.left;
            if (tableSource instanceof SQLJoinTableSource) {
                final SQLJoinTableSource join = (SQLJoinTableSource)tableSource;
                if (join.right.equals(this.right) && this.condition != null && this.condition.equals(join.condition) && joinLeft.right.equals(join.left) && this.joinType == join.joinType && joinLeft.condition != null && joinLeft.condition.equals(condition) && joinLeft.joinType == joinType) {
                    return true;
                }
            }
            return joinLeft.contains(tableSource, condition, joinType);
        }
        return false;
    }
    
    public SQLJoinTableSource findJoin(final SQLTableSource tableSource, final JoinType joinType) {
        if (this.right.equals(tableSource)) {
            if (this.joinType == joinType) {
                return this;
            }
            return null;
        }
        else {
            if (this.left instanceof SQLJoinTableSource) {
                return ((SQLJoinTableSource)this.left).findJoin(tableSource, joinType);
            }
            return null;
        }
    }
    
    @Override
    public boolean containsAlias(final String alias) {
        return SQLUtils.nameEquals(this.alias, alias) || (this.left != null && this.left.containsAlias(alias)) || (this.right != null && this.right.containsAlias(alias));
    }
    
    @Override
    public SQLColumnDefinition findColumn(final String columnName) {
        final long hash = FnvHash.hashCode64(columnName);
        return this.findColumn(hash);
    }
    
    @Override
    public SQLColumnDefinition findColumn(final long columnNameHash) {
        final SQLObject column = this.resolveColum(columnNameHash);
        if (column instanceof SQLColumnDefinition) {
            return (SQLColumnDefinition)column;
        }
        return null;
    }
    
    @Override
    public SQLObject resolveColum(final long columnNameHash) {
        if (this.left != null) {
            final SQLObject column = this.left.resolveColum(columnNameHash);
            if (column != null) {
                return column;
            }
        }
        if (this.right != null) {
            return this.right.resolveColum(columnNameHash);
        }
        return null;
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final String columnName) {
        final long hash = FnvHash.hashCode64(columnName);
        return this.findTableSourceWithColumn(hash, columnName, 0);
    }
    
    public SQLJoinTableSource findTableSourceWithColumn(final SQLName a, final SQLName b) {
        if (this.left.findTableSourceWithColumn(a) != null && this.right.findTableSourceWithColumn(b) != null) {
            return this;
        }
        if (this.right.findTableSourceWithColumn(a) != null && this.left.findTableSourceWithColumn(b) != null) {
            return this;
        }
        if (this.left instanceof SQLJoinTableSource) {
            return ((SQLJoinTableSource)this.left).findTableSourceWithColumn(a, b);
        }
        if (this.right instanceof SQLJoinTableSource) {
            return ((SQLJoinTableSource)this.right).findTableSourceWithColumn(a, b);
        }
        return null;
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final long columnNameHash, final String name, final int option) {
        SQLTableSource leftMatch = null;
        if (this.left != null) {
            leftMatch = this.left.findTableSourceWithColumn(columnNameHash, name, option);
        }
        final boolean checkColumnAmbiguous = (option & SchemaResolveVisitor.Option.CheckColumnAmbiguous.mask) != 0x0;
        if (leftMatch != null && !checkColumnAmbiguous) {
            return leftMatch;
        }
        SQLTableSource rightMatch = null;
        if (this.right != null) {
            rightMatch = this.right.findTableSourceWithColumn(columnNameHash, name, option);
        }
        if (leftMatch == null) {
            return rightMatch;
        }
        if (rightMatch == null) {
            return leftMatch;
        }
        if (name != null) {
            final String msg = "Column '" + name + "' is ambiguous";
            throw new FastsqlColumnAmbiguousException(msg);
        }
        throw new FastsqlColumnAmbiguousException();
    }
    
    public boolean match(final String alias_a, final String alias_b) {
        return this.left != null && this.right != null && ((this.left.containsAlias(alias_a) && this.right.containsAlias(alias_b)) || (this.right.containsAlias(alias_a) && this.left.containsAlias(alias_b)));
    }
    
    public boolean conditionContainsTable(final String alias) {
        return this.condition != null && this.condition instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)this.condition).conditionContainsTable(alias);
    }
    
    public SQLJoinTableSource join(final SQLTableSource right, final JoinType joinType, final SQLExpr condition) {
        final SQLJoinTableSource joined = new SQLJoinTableSource(this, joinType, right, condition);
        return joined;
    }
    
    @Override
    public SQLTableSource findTableSource(final long alias_hash) {
        if (alias_hash == 0L) {
            return null;
        }
        if (this.aliasHashCode64() == alias_hash) {
            return this;
        }
        final SQLTableSource result = this.left.findTableSource(alias_hash);
        if (result != null) {
            return result;
        }
        return this.right.findTableSource(alias_hash);
    }
    
    public SQLTableSource other(final SQLTableSource x) {
        if (this.left == x) {
            return this.right;
        }
        if (this.right == x) {
            return this.left;
        }
        return null;
    }
    
    public boolean isGlobal() {
        return this.global;
    }
    
    public void setGlobal(final boolean global) {
        this.global = global;
    }
    
    @Override
    public int hashCode() {
        int result = (this.left != null) ? this.left.hashCode() : 0;
        result = 31 * result + ((this.joinType != null) ? this.joinType.hashCode() : 0);
        result = 31 * result + ((this.right != null) ? this.right.hashCode() : 0);
        result = 31 * result + ((this.condition != null) ? this.condition.hashCode() : 0);
        result = 31 * result + this.using.hashCode();
        result = 31 * result + (this.natural ? 1 : 0);
        result = 31 * result + (this.global ? 1 : 0);
        return result;
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
        final SQLJoinTableSource that = (SQLJoinTableSource)o;
        if (this.natural != that.natural) {
            return false;
        }
        if (this.asof != that.asof) {
            return false;
        }
        if (this.global != that.global) {
            return false;
        }
        Label_0111: {
            if (this.left != null) {
                if (this.left.equals(that.left)) {
                    break Label_0111;
                }
            }
            else if (that.left == null) {
                break Label_0111;
            }
            return false;
        }
        if (this.joinType != that.joinType) {
            return false;
        }
        Label_0157: {
            if (this.right != null) {
                if (this.right.equals(that.right)) {
                    break Label_0157;
                }
            }
            else if (that.right == null) {
                break Label_0157;
            }
            return false;
        }
        Label_0190: {
            if (this.condition != null) {
                if (this.condition.equals(that.condition)) {
                    break Label_0190;
                }
            }
            else if (that.condition == null) {
                break Label_0190;
            }
            return false;
        }
        if (this.using != null) {
            if (this.using.equals(that.using)) {
                return (this.udj != null) ? this.udj.equals(that.udj) : (that.udj == null);
            }
        }
        else if (that.using == null) {
            return (this.udj != null) ? this.udj.equals(that.udj) : (that.udj == null);
        }
        return false;
    }
    
    public void splitTo(final List<SQLTableSource> outTableSources, final JoinType joinType) {
        if (joinType == this.joinType) {
            if (this.left instanceof SQLJoinTableSource) {
                ((SQLJoinTableSource)this.left).splitTo(outTableSources, joinType);
            }
            else {
                outTableSources.add(this.left);
            }
            if (this.right instanceof SQLJoinTableSource) {
                ((SQLJoinTableSource)this.right).splitTo(outTableSources, joinType);
            }
            else {
                outTableSources.add(this.right);
            }
        }
        else {
            outTableSources.add(this);
        }
    }
    
    public enum JoinType
    {
        COMMA(","), 
        JOIN("JOIN"), 
        INNER_JOIN("INNER JOIN"), 
        CROSS_JOIN("CROSS JOIN"), 
        NATURAL_JOIN("NATURAL JOIN"), 
        NATURAL_CROSS_JOIN("NATURAL CROSS JOIN"), 
        NATURAL_LEFT_JOIN("NATURAL LEFT JOIN"), 
        NATURAL_RIGHT_JOIN("NATURAL RIGHT JOIN"), 
        NATURAL_INNER_JOIN("NATURAL INNER JOIN"), 
        LEFT_OUTER_JOIN("LEFT JOIN"), 
        LEFT_SEMI_JOIN("LEFT SEMI JOIN"), 
        LEFT_ANTI_JOIN("LEFT ANTI JOIN"), 
        RIGHT_OUTER_JOIN("RIGHT JOIN"), 
        FULL_OUTER_JOIN("FULL JOIN"), 
        STRAIGHT_JOIN("STRAIGHT_JOIN"), 
        OUTER_APPLY("OUTER APPLY"), 
        CROSS_APPLY("CROSS APPLY");
        
        public final String name;
        public final String name_lcase;
        
        private JoinType(final String name) {
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
        
        public static String toString(final JoinType joinType) {
            return joinType.name;
        }
    }
    
    public static class UDJ extends SQLObjectImpl
    {
        protected SQLExpr function;
        protected final List<SQLExpr> arguments;
        protected String alias;
        protected final List<SQLName> columns;
        protected List<SQLSelectOrderByItem> sortBy;
        protected List<SQLAssignItem> properties;
        
        public UDJ() {
            this.arguments = new ArrayList<SQLExpr>();
            this.columns = new ArrayList<SQLName>();
            this.sortBy = new ArrayList<SQLSelectOrderByItem>();
            this.properties = new ArrayList<SQLAssignItem>();
        }
        
        @Override
        protected void accept0(final SQLASTVisitor v) {
            if (v.visit(this)) {
                this.acceptChild(v, this.arguments);
                this.acceptChild(v, this.columns);
            }
            v.endVisit(this);
        }
        
        public List<SQLSelectOrderByItem> getSortBy() {
            return this.sortBy;
        }
        
        public UDJ(final SQLExpr function) {
            this.arguments = new ArrayList<SQLExpr>();
            this.columns = new ArrayList<SQLName>();
            this.sortBy = new ArrayList<SQLSelectOrderByItem>();
            this.properties = new ArrayList<SQLAssignItem>();
            this.function = function;
        }
        
        @Override
        public UDJ clone() {
            final UDJ x = new UDJ();
            x.function = this.function.clone();
            for (final SQLExpr arg : this.arguments) {
                final SQLExpr t = arg.clone();
                t.setParent(x);
                x.arguments.add(t);
            }
            x.alias = this.alias;
            for (final SQLName column : this.columns) {
                final SQLName t2 = column.clone();
                t2.setParent(x);
                x.columns.add(t2);
            }
            for (final SQLAssignItem property : this.properties) {
                final SQLAssignItem c = property.clone();
                c.setParent(x);
                x.properties.add(c);
            }
            return x;
        }
        
        public SQLExpr getFunction() {
            return this.function;
        }
        
        public void setFunction(final SQLExpr function) {
            this.function = function;
        }
        
        public List<SQLExpr> getArguments() {
            return this.arguments;
        }
        
        public List<SQLName> getColumns() {
            return this.columns;
        }
        
        public String getAlias() {
            return this.alias;
        }
        
        public void setAlias(final String alias) {
            this.alias = alias;
        }
        
        public List<SQLAssignItem> getProperties() {
            return this.properties;
        }
    }
}
