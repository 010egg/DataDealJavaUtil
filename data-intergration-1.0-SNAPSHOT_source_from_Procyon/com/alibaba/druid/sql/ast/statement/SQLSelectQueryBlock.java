// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import java.util.Collections;
import java.util.Collection;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsUDTFSQLSelectItem;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import java.util.Iterator;
import java.util.TreeSet;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLWindow;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLDbTypedObject;
import com.alibaba.druid.sql.ast.SQLReplaceable;

public class SQLSelectQueryBlock extends SQLSelectQueryBase implements SQLReplaceable, SQLDbTypedObject
{
    protected int distionOption;
    protected final List<SQLSelectItem> selectList;
    protected SQLTableSource from;
    protected SQLExprTableSource into;
    protected SQLExpr where;
    protected SQLExpr startWith;
    protected SQLExpr connectBy;
    protected boolean prior;
    protected boolean noCycle;
    protected SQLOrderBy orderBySiblings;
    protected SQLSelectGroupByClause groupBy;
    protected List<SQLWindow> windows;
    protected SQLOrderBy orderBy;
    protected boolean forUpdate;
    protected boolean noWait;
    protected boolean skipLocked;
    protected boolean forShare;
    protected SQLExpr waitTime;
    protected SQLLimit limit;
    protected List<SQLExpr> forUpdateOf;
    protected List<SQLSelectOrderByItem> distributeBy;
    protected List<SQLSelectOrderByItem> sortBy;
    protected List<SQLSelectOrderByItem> clusterBy;
    protected String cachedSelectList;
    protected long cachedSelectListHash;
    protected DbType dbType;
    protected List<SQLCommentHint> hints;
    
    public SQLSelectQueryBlock() {
        this.selectList = new ArrayList<SQLSelectItem>();
        this.prior = false;
        this.noCycle = false;
        this.forUpdate = false;
        this.noWait = false;
        this.skipLocked = false;
        this.forShare = false;
    }
    
    public SQLSelectQueryBlock(final DbType dbType) {
        this.selectList = new ArrayList<SQLSelectItem>();
        this.prior = false;
        this.noCycle = false;
        this.forUpdate = false;
        this.noWait = false;
        this.skipLocked = false;
        this.forShare = false;
        this.dbType = dbType;
    }
    
    public SQLExprTableSource getInto() {
        return this.into;
    }
    
    public void setInto(final SQLExpr into) {
        this.setInto(new SQLExprTableSource(into));
    }
    
    public void setInto(final SQLExprTableSource into) {
        if (into != null) {
            into.setParent(this);
        }
        this.into = into;
    }
    
    public SQLSelectGroupByClause getGroupBy() {
        return this.groupBy;
    }
    
    public void setGroupBy(final SQLSelectGroupByClause x) {
        if (x != null) {
            x.setParent(this);
        }
        this.groupBy = x;
    }
    
    public List<SQLWindow> getWindows() {
        return this.windows;
    }
    
    public void addWindow(final SQLWindow x) {
        if (x != null) {
            x.setParent(this);
        }
        if (this.windows == null) {
            this.windows = new ArrayList<SQLWindow>(4);
        }
        this.windows.add(x);
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.where = x;
    }
    
    public void addWhere(final SQLExpr condition) {
        if (condition == null) {
            return;
        }
        if (this.where == null) {
            condition.setParent(this);
            this.where = condition;
            return;
        }
        final List<SQLExpr> items = SQLBinaryOpExpr.split(this.where, SQLBinaryOperator.BooleanAnd);
        for (final SQLExpr item : items) {
            if (condition.equals(item)) {
                return;
            }
            if (!(condition instanceof SQLInListExpr)) {
                continue;
            }
            final SQLInListExpr inListExpr = (SQLInListExpr)condition;
            if (item instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr binaryOpItem = (SQLBinaryOpExpr)item;
                final SQLExpr left = binaryOpItem.getLeft();
                final SQLExpr right = binaryOpItem.getRight();
                if (!inListExpr.getExpr().equals(left) || binaryOpItem.getOperator() != SQLBinaryOperator.Equality || right instanceof SQLNullExpr) {
                    continue;
                }
                if (inListExpr.getTargetList().contains(right)) {
                    return;
                }
                SQLUtils.replaceInParent(item, new SQLBooleanExpr(false));
                return;
            }
            else {
                if (!(item instanceof SQLInListExpr)) {
                    continue;
                }
                final SQLInListExpr inListItem = (SQLInListExpr)item;
                if (!inListExpr.getExpr().equals(inListItem.getExpr())) {
                    continue;
                }
                final TreeSet<SQLExpr> set = new TreeSet<SQLExpr>();
                for (final SQLExpr itemItem : inListItem.getTargetList()) {
                    set.add(itemItem);
                }
                final List<SQLExpr> andList = new ArrayList<SQLExpr>();
                for (final SQLExpr exprItem : inListExpr.getTargetList()) {
                    if (set.contains(exprItem)) {
                        andList.add(exprItem.clone());
                    }
                }
                if (andList.size() == 0) {
                    SQLUtils.replaceInParent(item, new SQLBooleanExpr(false));
                    return;
                }
                inListItem.getTargetList().clear();
                for (final SQLExpr val : andList) {
                    inListItem.addTarget(val);
                }
                return;
            }
        }
        (this.where = SQLBinaryOpExpr.and(this.where, condition)).setParent(this);
    }
    
    public void addWhereForDynamicFilter(final SQLExpr condition) {
        if (condition == null) {
            return;
        }
        if (this.where == null) {
            condition.setParent(this);
            this.where = condition;
            return;
        }
        if (this.where instanceof SQLBinaryOpExpr || this.where instanceof SQLBinaryOpExprGroup) {
            final List<SQLExpr> items = SQLBinaryOpExpr.split(this.where, SQLBinaryOperator.BooleanAnd);
            for (final SQLExpr item : items) {
                if (condition.equals(item)) {
                    return;
                }
                if (!(condition instanceof SQLInListExpr)) {
                    continue;
                }
                final SQLInListExpr inListExpr = (SQLInListExpr)condition;
                if (item instanceof SQLBinaryOpExpr) {
                    final SQLBinaryOpExpr binaryOpItem = (SQLBinaryOpExpr)item;
                    final SQLExpr left = binaryOpItem.getLeft();
                    final SQLExpr right = binaryOpItem.getRight();
                    if (inListExpr.getExpr().equals(left)) {
                        if (inListExpr.getTargetList().contains(right)) {
                            this.replace(item, inListExpr);
                        }
                        else {
                            final SQLInListExpr inListExpr2 = inListExpr.clone();
                            inListExpr2.addTarget(right.clone());
                            this.replace(item, inListExpr2);
                        }
                        return;
                    }
                    continue;
                }
                else {
                    if (!(item instanceof SQLInListExpr)) {
                        continue;
                    }
                    final SQLInListExpr inListItem = (SQLInListExpr)item;
                    if (inListExpr.getExpr().equals(inListItem.getExpr())) {
                        final TreeSet<SQLExpr> set = new TreeSet<SQLExpr>();
                        for (final SQLExpr itemItem : inListItem.getTargetList()) {
                            set.add(itemItem);
                        }
                        for (final SQLExpr exprItem : inListExpr.getTargetList()) {
                            if (!set.contains(exprItem)) {
                                inListItem.addTarget(exprItem.clone());
                            }
                        }
                        return;
                    }
                    continue;
                }
            }
        }
        (this.where = SQLBinaryOpExpr.and(this.where, condition)).setParent(this);
    }
    
    public void whereOr(final SQLExpr condition) {
        if (condition == null) {
            return;
        }
        if (this.where == null) {
            condition.setParent(this);
            this.where = condition;
        }
        else if (SQLBinaryOpExpr.isOr(this.where) || SQLBinaryOpExpr.isOr(condition)) {
            final SQLBinaryOpExprGroup group = new SQLBinaryOpExprGroup(SQLBinaryOperator.BooleanOr, this.dbType);
            group.add(this.where);
            group.add(condition);
            group.setParent(this);
            this.where = group;
        }
        else {
            (this.where = SQLBinaryOpExpr.or(this.where, condition)).setParent(this);
        }
    }
    
    public void addHaving(final SQLExpr condition) {
        if (condition == null) {
            return;
        }
        if (this.groupBy == null) {
            this.groupBy = new SQLSelectGroupByClause();
        }
        this.groupBy.addHaving(condition);
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
    
    public void addOrderBy(final SQLOrderBy orderBy) {
        if (orderBy == null) {
            return;
        }
        if (this.orderBy == null) {
            this.setOrderBy(orderBy);
            return;
        }
        for (final SQLSelectOrderByItem item : orderBy.getItems()) {
            this.orderBy.addItem(item.clone());
        }
    }
    
    public void addOrderBy(final SQLSelectOrderByItem orderByItem) {
        if (orderByItem == null) {
            return;
        }
        if (this.orderBy == null) {
            (this.orderBy = new SQLOrderBy()).setParent(this);
        }
        this.orderBy.addItem(orderByItem);
    }
    
    public boolean containsOrderBy(final SQLSelectOrderByItem orderByItem) {
        if (orderByItem == null || this.orderBy == null) {
            return false;
        }
        if (this.orderBy.getItems().contains(orderByItem)) {
            return true;
        }
        final SQLExpr expr = orderByItem.getExpr();
        if (expr == null && expr instanceof SQLIntegerExpr) {
            return false;
        }
        int index = 0;
        for (int i = 0; i < this.selectList.size(); ++i) {
            final SQLSelectItem selectItem = this.selectList.get(i);
            if (selectItem.getExpr().equals(expr)) {
                index = i + 1;
                break;
            }
        }
        if (index > 0) {
            for (final SQLSelectOrderByItem selectOrderByItem : this.orderBy.getItems()) {
                final SQLExpr orderByItemExpr = selectOrderByItem.getExpr();
                if (orderByItemExpr instanceof SQLIntegerExpr && ((SQLIntegerExpr)orderByItemExpr).getNumber().intValue() == index) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void addOrderBy(final SQLExpr orderBy, final SQLOrderingSpecification type) {
        if (orderBy == null) {
            return;
        }
        if (this.orderBy == null) {
            this.setOrderBy(new SQLOrderBy(orderBy, type));
            return;
        }
        this.orderBy.addItem(orderBy, type);
    }
    
    public void addOrderBy(final SQLExpr orderBy) {
        if (orderBy == null) {
            return;
        }
        if (this.orderBy == null) {
            this.setOrderBy(new SQLOrderBy(orderBy));
            return;
        }
        this.orderBy.addItem(orderBy);
    }
    
    public SQLOrderBy getOrderBySiblings() {
        return this.orderBySiblings;
    }
    
    public void setOrderBySiblings(final SQLOrderBy orderBySiblings) {
        if (orderBySiblings != null) {
            orderBySiblings.setParent(this);
        }
        this.orderBySiblings = orderBySiblings;
    }
    
    public int getDistionOption() {
        return this.distionOption;
    }
    
    public void setDistionOption(final int distionOption) {
        this.distionOption = distionOption;
    }
    
    public void setDistinct() {
        this.distionOption = 2;
    }
    
    public boolean isDistinct() {
        return this.distionOption == 2;
    }
    
    public List<SQLSelectItem> getSelectList() {
        return this.selectList;
    }
    
    public SQLSelectItem getSelectItem(final int i) {
        return this.selectList.get(i);
    }
    
    public void addSelectItem(final SQLSelectItem item) {
        this.selectList.add(item);
        item.setParent(this);
    }
    
    public SQLSelectItem addSelectItem(final SQLExpr expr) {
        final SQLSelectItem item = new SQLSelectItem(expr);
        this.addSelectItem(item);
        return item;
    }
    
    public void addSelectItem(final String selectItemExpr, final String alias) {
        final SQLExpr expr = SQLUtils.toSQLExpr(selectItemExpr, this.dbType);
        this.addSelectItem(new SQLSelectItem(expr, alias));
    }
    
    public void addSelectItem(final SQLExpr expr, final String alias) {
        this.addSelectItem(new SQLSelectItem(expr, alias));
    }
    
    public boolean hasSelectAggregation() {
        final AggregationStatVisitor v = new AggregationStatVisitor();
        for (final SQLSelectItem item : this.selectList) {
            final SQLExpr expr = item.getExpr();
            expr.accept(v);
        }
        return v.aggregation;
    }
    
    public SQLTableSource getFrom() {
        return this.from;
    }
    
    public void setFrom(final SQLExpr from) {
        this.setFrom(new SQLExprTableSource(from));
    }
    
    public void setFrom(final SQLTableSource from) {
        if (from != null) {
            from.setParent(this);
        }
        this.from = from;
    }
    
    public void setFrom(final SQLSelectQueryBlock queryBlock, final String alias) {
        if (queryBlock == null) {
            this.from = null;
            return;
        }
        this.setFrom(new SQLSelect(queryBlock), alias);
    }
    
    public void setFrom(final SQLSelect select, final String alias) {
        if (select == null) {
            this.from = null;
            return;
        }
        final SQLSubqueryTableSource from = new SQLSubqueryTableSource(select);
        from.setAlias(alias);
        this.setFrom(from);
    }
    
    public void setFrom(final String tableName, final String alias) {
        SQLExprTableSource from;
        if (tableName == null || tableName.length() == 0) {
            from = null;
        }
        else {
            final SQLExpr expr = SQLUtils.toSQLExpr(tableName);
            from = new SQLExprTableSource(expr, alias);
        }
        this.setFrom(from);
    }
    
    public boolean isForUpdate() {
        return this.forUpdate;
    }
    
    public void setForUpdate(final boolean forUpdate) {
        this.forUpdate = forUpdate;
    }
    
    public boolean isNoWait() {
        return this.noWait;
    }
    
    public void setNoWait(final boolean noWait) {
        this.noWait = noWait;
    }
    
    public boolean isSkipLocked() {
        return this.skipLocked;
    }
    
    public void setSkipLocked(final boolean skipLocked) {
        this.skipLocked = skipLocked;
    }
    
    public boolean isForShare() {
        return this.forShare;
    }
    
    public void setForShare(final boolean forShare) {
        this.forShare = forShare;
    }
    
    public SQLExpr getWaitTime() {
        return this.waitTime;
    }
    
    public void setWaitTime(final SQLExpr waitTime) {
        if (waitTime != null) {
            waitTime.setParent(this);
        }
        this.waitTime = waitTime;
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
    
    public void mergeLimit(final SQLLimit limit) {
        if (this.limit == null) {
            this.limit = limit.clone();
            return;
        }
        this.limit.merge(limit);
    }
    
    public SQLExpr getFirst() {
        if (this.limit == null) {
            return null;
        }
        return this.limit.getRowCount();
    }
    
    public void setFirst(final SQLExpr first) {
        if (this.limit == null) {
            this.limit = new SQLLimit();
        }
        this.limit.setRowCount(first);
    }
    
    public SQLExpr getOffset() {
        if (this.limit == null) {
            return null;
        }
        return this.limit.getOffset();
    }
    
    public void setOffset(final SQLExpr offset) {
        if (this.limit == null) {
            this.limit = new SQLLimit();
        }
        this.limit.setOffset(offset);
    }
    
    public boolean isPrior() {
        return this.prior;
    }
    
    public void setPrior(final boolean prior) {
        this.prior = prior;
    }
    
    public SQLExpr getStartWith() {
        return this.startWith;
    }
    
    public void setStartWith(final SQLExpr startWith) {
        if (startWith != null) {
            startWith.setParent(this);
        }
        this.startWith = startWith;
    }
    
    public SQLExpr getConnectBy() {
        return this.connectBy;
    }
    
    public void setConnectBy(final SQLExpr connectBy) {
        if (connectBy != null) {
            connectBy.setParent(this);
        }
        this.connectBy = connectBy;
    }
    
    public boolean isNoCycle() {
        return this.noCycle;
    }
    
    public void setNoCycle(final boolean noCycle) {
        this.noCycle = noCycle;
    }
    
    public List<SQLSelectOrderByItem> getDistributeBy() {
        if (this.distributeBy == null) {
            this.distributeBy = new ArrayList<SQLSelectOrderByItem>();
        }
        return this.distributeBy;
    }
    
    public List<SQLSelectOrderByItem> getDistributeByDirect() {
        return this.distributeBy;
    }
    
    public void addDistributeBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
            if (this.distributeBy == null) {
                this.distributeBy = new ArrayList<SQLSelectOrderByItem>();
            }
            this.distributeBy.add(new SQLSelectOrderByItem(x));
        }
    }
    
    public void addDistributeBy(final SQLSelectOrderByItem item) {
        if (this.distributeBy == null) {
            this.distributeBy = new ArrayList<SQLSelectOrderByItem>();
        }
        if (item != null) {
            item.setParent(this);
        }
        this.distributeBy.add(item);
    }
    
    public List<SQLSelectOrderByItem> getSortBy() {
        if (this.sortBy == null) {
            this.sortBy = new ArrayList<SQLSelectOrderByItem>();
        }
        return this.sortBy;
    }
    
    public List<SQLSelectOrderByItem> getSortByDirect() {
        return this.sortBy;
    }
    
    public void addSortBy(final SQLSelectOrderByItem item) {
        if (this.sortBy == null) {
            this.sortBy = new ArrayList<SQLSelectOrderByItem>();
        }
        if (item != null) {
            item.setParent(this);
        }
        this.sortBy.add(item);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
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
            if (this.windows != null) {
                for (int i = 0; i < this.windows.size(); ++i) {
                    final SQLWindow item2 = this.windows.get(i);
                    item2.accept(visitor);
                }
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
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLSelectQueryBlock that = (SQLSelectQueryBlock)o;
        if (this.distionOption != that.distionOption) {
            return false;
        }
        if (this.prior != that.prior) {
            return false;
        }
        if (this.noCycle != that.noCycle) {
            return false;
        }
        if (this.parenthesized != that.parenthesized) {
            return false;
        }
        if (this.forUpdate != that.forUpdate) {
            return false;
        }
        if (this.noWait != that.noWait) {
            return false;
        }
        if (this.cachedSelectListHash != that.cachedSelectListHash) {
            return false;
        }
        Label_0156: {
            if (this.selectList != null) {
                if (this.selectList.equals(that.selectList)) {
                    break Label_0156;
                }
            }
            else if (that.selectList == null) {
                break Label_0156;
            }
            return false;
        }
        Label_0189: {
            if (this.from != null) {
                if (this.from.equals(that.from)) {
                    break Label_0189;
                }
            }
            else if (that.from == null) {
                break Label_0189;
            }
            return false;
        }
        Label_0222: {
            if (this.into != null) {
                if (this.into.equals(that.into)) {
                    break Label_0222;
                }
            }
            else if (that.into == null) {
                break Label_0222;
            }
            return false;
        }
        Label_0255: {
            if (this.where != null) {
                if (this.where.equals(that.where)) {
                    break Label_0255;
                }
            }
            else if (that.where == null) {
                break Label_0255;
            }
            return false;
        }
        Label_0288: {
            if (this.startWith != null) {
                if (this.startWith.equals(that.startWith)) {
                    break Label_0288;
                }
            }
            else if (that.startWith == null) {
                break Label_0288;
            }
            return false;
        }
        Label_0321: {
            if (this.connectBy != null) {
                if (this.connectBy.equals(that.connectBy)) {
                    break Label_0321;
                }
            }
            else if (that.connectBy == null) {
                break Label_0321;
            }
            return false;
        }
        Label_0354: {
            if (this.orderBySiblings != null) {
                if (this.orderBySiblings.equals(that.orderBySiblings)) {
                    break Label_0354;
                }
            }
            else if (that.orderBySiblings == null) {
                break Label_0354;
            }
            return false;
        }
        Label_0387: {
            if (this.groupBy != null) {
                if (this.groupBy.equals(that.groupBy)) {
                    break Label_0387;
                }
            }
            else if (that.groupBy == null) {
                break Label_0387;
            }
            return false;
        }
        Label_0420: {
            if (this.orderBy != null) {
                if (this.orderBy.equals(that.orderBy)) {
                    break Label_0420;
                }
            }
            else if (that.orderBy == null) {
                break Label_0420;
            }
            return false;
        }
        Label_0453: {
            if (this.waitTime != null) {
                if (this.waitTime.equals(that.waitTime)) {
                    break Label_0453;
                }
            }
            else if (that.waitTime == null) {
                break Label_0453;
            }
            return false;
        }
        Label_0486: {
            if (this.limit != null) {
                if (this.limit.equals(that.limit)) {
                    break Label_0486;
                }
            }
            else if (that.limit == null) {
                break Label_0486;
            }
            return false;
        }
        Label_0521: {
            if (this.forUpdateOf != null) {
                if (this.forUpdateOf.equals(that.forUpdateOf)) {
                    break Label_0521;
                }
            }
            else if (that.forUpdateOf == null) {
                break Label_0521;
            }
            return false;
        }
        Label_0556: {
            if (this.distributeBy != null) {
                if (this.distributeBy.equals(that.distributeBy)) {
                    break Label_0556;
                }
            }
            else if (that.distributeBy == null) {
                break Label_0556;
            }
            return false;
        }
        Label_0591: {
            if (this.sortBy != null) {
                if (this.sortBy.equals(that.sortBy)) {
                    break Label_0591;
                }
            }
            else if (that.sortBy == null) {
                break Label_0591;
            }
            return false;
        }
        if (this.cachedSelectList != null) {
            if (this.cachedSelectList.equals(that.cachedSelectList)) {
                return this.dbType == that.dbType && ((this.hints != null) ? this.hints.equals(that.hints) : (that.hints == null));
            }
        }
        else if (that.cachedSelectList == null) {
            return this.dbType == that.dbType && ((this.hints != null) ? this.hints.equals(that.hints) : (that.hints == null));
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.distionOption;
        result = 31 * result + ((this.selectList != null) ? this.selectList.hashCode() : 0);
        result = 31 * result + ((this.from != null) ? this.from.hashCode() : 0);
        result = 31 * result + ((this.into != null) ? this.into.hashCode() : 0);
        result = 31 * result + ((this.where != null) ? this.where.hashCode() : 0);
        result = 31 * result + ((this.startWith != null) ? this.startWith.hashCode() : 0);
        result = 31 * result + ((this.connectBy != null) ? this.connectBy.hashCode() : 0);
        result = 31 * result + (this.prior ? 1 : 0);
        result = 31 * result + (this.noCycle ? 1 : 0);
        result = 31 * result + ((this.orderBySiblings != null) ? this.orderBySiblings.hashCode() : 0);
        result = 31 * result + ((this.groupBy != null) ? this.groupBy.hashCode() : 0);
        result = 31 * result + ((this.orderBy != null) ? this.orderBy.hashCode() : 0);
        result = 31 * result + (this.parenthesized ? 1 : 0);
        result = 31 * result + (this.forUpdate ? 1 : 0);
        result = 31 * result + (this.noWait ? 1 : 0);
        result = 31 * result + ((this.waitTime != null) ? this.waitTime.hashCode() : 0);
        result = 31 * result + ((this.limit != null) ? this.limit.hashCode() : 0);
        result = 31 * result + ((this.forUpdateOf != null) ? this.forUpdateOf.hashCode() : 0);
        result = 31 * result + ((this.distributeBy != null) ? this.distributeBy.hashCode() : 0);
        result = 31 * result + ((this.sortBy != null) ? this.sortBy.hashCode() : 0);
        result = 31 * result + ((this.cachedSelectList != null) ? this.cachedSelectList.hashCode() : 0);
        result = 31 * result + (int)(this.cachedSelectListHash ^ this.cachedSelectListHash >>> 32);
        result = 31 * result + ((this.dbType != null) ? this.dbType.hashCode() : 0);
        result = 31 * result + ((this.hints != null) ? this.hints.hashCode() : 0);
        return result;
    }
    
    public boolean equalsForMergeJoin(final SQLSelectQueryBlock that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (this.into != null || this.limit != null || this.groupBy != null) {
            return false;
        }
        if (this.distionOption != that.distionOption) {
            return false;
        }
        if (this.prior != that.prior) {
            return false;
        }
        if (this.noCycle != that.noCycle) {
            return false;
        }
        if (this.parenthesized != that.parenthesized) {
            return false;
        }
        if (this.forUpdate != that.forUpdate) {
            return false;
        }
        if (this.noWait != that.noWait) {
            return false;
        }
        if (this.cachedSelectListHash != that.cachedSelectListHash) {
            return false;
        }
        Label_0163: {
            if (this.selectList != null) {
                if (this.selectList.equals(that.selectList)) {
                    break Label_0163;
                }
            }
            else if (that.selectList == null) {
                break Label_0163;
            }
            return false;
        }
        Label_0196: {
            if (this.from != null) {
                if (this.from.equals(that.from)) {
                    break Label_0196;
                }
            }
            else if (that.from == null) {
                break Label_0196;
            }
            return false;
        }
        Label_0229: {
            if (this.into != null) {
                if (this.into.equals(that.into)) {
                    break Label_0229;
                }
            }
            else if (that.into == null) {
                break Label_0229;
            }
            return false;
        }
        Label_0262: {
            if (this.startWith != null) {
                if (this.startWith.equals(that.startWith)) {
                    break Label_0262;
                }
            }
            else if (that.startWith == null) {
                break Label_0262;
            }
            return false;
        }
        Label_0295: {
            if (this.connectBy != null) {
                if (this.connectBy.equals(that.connectBy)) {
                    break Label_0295;
                }
            }
            else if (that.connectBy == null) {
                break Label_0295;
            }
            return false;
        }
        Label_0328: {
            if (this.orderBySiblings != null) {
                if (this.orderBySiblings.equals(that.orderBySiblings)) {
                    break Label_0328;
                }
            }
            else if (that.orderBySiblings == null) {
                break Label_0328;
            }
            return false;
        }
        Label_0361: {
            if (this.groupBy != null) {
                if (this.groupBy.equals(that.groupBy)) {
                    break Label_0361;
                }
            }
            else if (that.groupBy == null) {
                break Label_0361;
            }
            return false;
        }
        Label_0394: {
            if (this.orderBy != null) {
                if (this.orderBy.equals(that.orderBy)) {
                    break Label_0394;
                }
            }
            else if (that.orderBy == null) {
                break Label_0394;
            }
            return false;
        }
        Label_0427: {
            if (this.waitTime != null) {
                if (this.waitTime.equals(that.waitTime)) {
                    break Label_0427;
                }
            }
            else if (that.waitTime == null) {
                break Label_0427;
            }
            return false;
        }
        Label_0460: {
            if (this.limit != null) {
                if (this.limit.equals(that.limit)) {
                    break Label_0460;
                }
            }
            else if (that.limit == null) {
                break Label_0460;
            }
            return false;
        }
        Label_0495: {
            if (this.forUpdateOf != null) {
                if (this.forUpdateOf.equals(that.forUpdateOf)) {
                    break Label_0495;
                }
            }
            else if (that.forUpdateOf == null) {
                break Label_0495;
            }
            return false;
        }
        Label_0530: {
            if (this.distributeBy != null) {
                if (this.distributeBy.equals(that.distributeBy)) {
                    break Label_0530;
                }
            }
            else if (that.distributeBy == null) {
                break Label_0530;
            }
            return false;
        }
        Label_0565: {
            if (this.sortBy != null) {
                if (this.sortBy.equals(that.sortBy)) {
                    break Label_0565;
                }
            }
            else if (that.sortBy == null) {
                break Label_0565;
            }
            return false;
        }
        if (this.cachedSelectList != null) {
            if (this.cachedSelectList.equals(that.cachedSelectList)) {
                return this.dbType == that.dbType;
            }
        }
        else if (that.cachedSelectList == null) {
            return this.dbType == that.dbType;
        }
        return false;
    }
    
    @Override
    public SQLSelectQueryBlock clone() {
        final SQLSelectQueryBlock x = new SQLSelectQueryBlock(this.dbType);
        this.cloneTo(x);
        return x;
    }
    
    public List<SQLExpr> getForUpdateOf() {
        if (this.forUpdateOf == null) {
            this.forUpdateOf = new ArrayList<SQLExpr>(1);
        }
        return this.forUpdateOf;
    }
    
    public int getForUpdateOfSize() {
        if (this.forUpdateOf == null) {
            return 0;
        }
        return this.forUpdateOf.size();
    }
    
    public void cloneSelectListTo(final SQLSelectQueryBlock x) {
        x.distionOption = this.distionOption;
        for (final SQLSelectItem item : this.selectList) {
            final SQLSelectItem item2 = item.clone();
            item2.setParent(x);
            x.selectList.add(item2);
        }
    }
    
    public void cloneTo(final SQLSelectQueryBlock x) {
        x.parenthesized = this.parenthesized;
        x.distionOption = this.distionOption;
        if (x.selectList.size() > 0) {
            x.selectList.clear();
        }
        if (this.hints != null) {
            for (final SQLCommentHint hint : this.hints) {
                final SQLCommentHint hint2 = hint.clone();
                hint2.setParent(x);
                x.getHints().add(hint2);
            }
        }
        for (final SQLSelectItem item : this.selectList) {
            x.addSelectItem(item.clone());
        }
        if (this.from != null) {
            x.setFrom(this.from.clone());
        }
        if (this.into != null) {
            x.setInto(this.into.clone());
        }
        if (this.where != null) {
            x.setWhere(this.where.clone());
        }
        if (this.startWith != null) {
            x.setStartWith(this.startWith.clone());
        }
        if (this.connectBy != null) {
            x.setConnectBy(this.connectBy.clone());
        }
        x.prior = this.prior;
        x.noCycle = this.noCycle;
        if (this.orderBySiblings != null) {
            x.setOrderBySiblings(this.orderBySiblings.clone());
        }
        if (this.groupBy != null) {
            x.setGroupBy(this.groupBy.clone());
        }
        if (this.orderBy != null) {
            x.setOrderBy(this.orderBy.clone());
        }
        if (this.distributeBy != null) {
            if (x.distributeBy == null) {
                x.distributeBy = new ArrayList<SQLSelectOrderByItem>();
            }
            for (int i = 0; i < this.distributeBy.size(); ++i) {
                final SQLSelectOrderByItem item2 = this.distributeBy.get(i).clone();
                item2.setParent(x);
                x.distributeBy.add(item2);
            }
        }
        if (this.sortBy != null) {
            if (x.sortBy == null) {
                x.sortBy = new ArrayList<SQLSelectOrderByItem>();
            }
            for (int i = 0; i < this.sortBy.size(); ++i) {
                final SQLSelectOrderByItem item2 = this.sortBy.get(i).clone();
                item2.setParent(x);
                x.sortBy.add(item2);
            }
        }
        if (this.clusterBy != null) {
            if (x.clusterBy == null) {
                x.clusterBy = new ArrayList<SQLSelectOrderByItem>();
            }
            for (int i = 0; i < this.clusterBy.size(); ++i) {
                final SQLSelectOrderByItem item2 = this.clusterBy.get(i).clone();
                item2.setParent(x);
                x.clusterBy.add(item2);
            }
        }
        x.parenthesized = this.parenthesized;
        x.forUpdate = this.forUpdate;
        x.noWait = this.noWait;
        x.skipLocked = this.skipLocked;
        x.forShare = this.forShare;
        if (this.waitTime != null) {
            x.setWaitTime(this.waitTime.clone());
        }
        if (this.limit != null) {
            x.setLimit(this.limit.clone());
        }
    }
    
    public SQLTableSource findTableSource(final String alias) {
        if (this.from == null) {
            return null;
        }
        return this.from.findTableSource(alias);
    }
    
    public SQLTableSource findTableSourceWithColumn(final String column) {
        if (this.from == null) {
            return null;
        }
        return this.from.findTableSourceWithColumn(column);
    }
    
    public SQLTableSource findTableSourceWithColumn(final long columnHash) {
        if (this.from == null) {
            return null;
        }
        SQLTableSource tableSource = this.from.findTableSourceWithColumn(columnHash);
        if (tableSource == null && this.from instanceof SQLExprTableSource) {
            final SQLSelectItem selectItem = this.findSelectItem(columnHash);
            if (selectItem != null && selectItem.getExpr() instanceof SQLName && ((SQLName)selectItem.getExpr()).nameHashCode64() == columnHash) {
                tableSource = this.from;
            }
        }
        return tableSource;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.where == expr) {
            this.setWhere(target);
            return true;
        }
        if (this.startWith == expr) {
            this.setStartWith(target);
            return true;
        }
        if (this.connectBy == expr) {
            this.setConnectBy(target);
            return true;
        }
        return false;
    }
    
    public SQLSelectItem findSelectItem(final String ident) {
        if (ident == null) {
            return null;
        }
        final long hash = FnvHash.hashCode64(ident);
        return this.findSelectItem(hash);
    }
    
    public SQLSelectItem findSelectItem(final long identHash) {
        for (final SQLSelectItem item : this.selectList) {
            if (item.match(identHash)) {
                return item;
            }
        }
        if (this.from == null) {
            return null;
        }
        if (this.selectList.size() == 1 && this.selectList.get(0).getExpr() instanceof SQLAllColumnExpr) {
            final SQLTableSource matchedTableSource = this.from.findTableSourceWithColumn(identHash);
            if (matchedTableSource != null) {
                return this.selectList.get(0);
            }
        }
        SQLSelectItem ownerAllItem = null;
        for (final SQLSelectItem item2 : this.selectList) {
            final SQLExpr itemExpr = item2.getExpr();
            if (itemExpr instanceof SQLPropertyExpr && ((SQLPropertyExpr)itemExpr).getName().equals("*")) {
                if (ownerAllItem != null) {
                    return null;
                }
                ownerAllItem = item2;
            }
        }
        if (ownerAllItem != null) {
            return ownerAllItem;
        }
        return null;
    }
    
    public boolean selectItemHasAllColumn() {
        return this.selectItemHasAllColumn(true);
    }
    
    public boolean selectItemHasAllColumn(final boolean recursive) {
        for (final SQLSelectItem item : this.selectList) {
            final SQLExpr expr = item.getExpr();
            final boolean allColumn = expr instanceof SQLAllColumnExpr || (expr instanceof SQLPropertyExpr && ((SQLPropertyExpr)expr).getName().equals("*"));
            if (allColumn) {
                if (recursive && this.from instanceof SQLSubqueryTableSource) {
                    final SQLSelect subSelect = ((SQLSubqueryTableSource)this.from).select;
                    final SQLSelectQueryBlock queryBlock = subSelect.getQueryBlock();
                    if (queryBlock != null) {
                        return queryBlock.selectItemHasAllColumn();
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public SQLSelectItem findAllColumnSelectItem() {
        SQLSelectItem allColumnItem = null;
        for (final SQLSelectItem item : this.selectList) {
            final SQLExpr expr = item.getExpr();
            final boolean allColumn = expr instanceof SQLAllColumnExpr || (expr instanceof SQLPropertyExpr && ((SQLPropertyExpr)expr).getName().equals("*"));
            if (allColumnItem != null) {
                return null;
            }
            allColumnItem = item;
        }
        return allColumnItem;
    }
    
    public SQLColumnDefinition findColumn(final String columnName) {
        if (this.from == null) {
            return null;
        }
        final long hash = FnvHash.hashCode64(columnName);
        return this.from.findColumn(hash);
    }
    
    public SQLColumnDefinition findColumn(final long columnNameHash) {
        final SQLObject object = this.resolveColum(columnNameHash);
        if (object instanceof SQLColumnDefinition) {
            return (SQLColumnDefinition)object;
        }
        if (object instanceof SQLSelectItem) {
            final SQLExpr expr = ((SQLSelectItem)object).getExpr();
            if (expr instanceof SQLName) {
                return ((SQLName)expr).getResolvedColumn();
            }
        }
        return null;
    }
    
    public SQLObject resolveColum(final long columnNameHash) {
        final SQLSelectItem selectItem = this.findSelectItem(columnNameHash);
        if (selectItem != null) {
            final SQLExpr selectItemExpr = selectItem.getExpr();
            if (selectItemExpr instanceof SQLAllColumnExpr) {
                final SQLObject resolveColumn = this.from.resolveColum(columnNameHash);
                if (resolveColumn != null) {
                    return resolveColumn;
                }
            }
            else if (selectItemExpr instanceof SQLPropertyExpr && ((SQLPropertyExpr)selectItemExpr).getName().equals("*")) {
                final SQLTableSource resolvedTableSource = ((SQLPropertyExpr)selectItemExpr).getResolvedTableSource();
                if (resolvedTableSource instanceof SQLSubqueryTableSource) {
                    final SQLObject resolveColumn2 = resolvedTableSource.resolveColum(columnNameHash);
                    if (resolveColumn2 != null) {
                        return resolveColumn2;
                    }
                }
            }
            return selectItem;
        }
        if (this.from != null) {
            return this.from.resolveColum(columnNameHash);
        }
        return null;
    }
    
    public void addCondition(final String conditionSql) {
        if (conditionSql == null || conditionSql.length() == 0) {
            return;
        }
        final SQLExpr condition = SQLUtils.toSQLExpr(conditionSql, this.dbType);
        this.addCondition(condition);
    }
    
    public void addCondition(final SQLExpr expr) {
        if (expr == null) {
            return;
        }
        this.setWhere(SQLBinaryOpExpr.and(this.where, expr));
    }
    
    public boolean removeCondition(final String conditionSql) {
        if (conditionSql == null || conditionSql.length() == 0) {
            return false;
        }
        final SQLExpr condition = SQLUtils.toSQLExpr(conditionSql, this.dbType);
        return this.removeCondition(condition);
    }
    
    public boolean removeCondition(final SQLExpr condition) {
        if (condition == null) {
            return false;
        }
        if (this.where instanceof SQLBinaryOpExprGroup) {
            final SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup)this.where;
            int removedCount = 0;
            final List<SQLExpr> items = group.getItems();
            for (int i = items.size() - 1; i >= 0; --i) {
                final SQLExpr item = items.get(i);
                if (item.equals(condition)) {
                    items.remove(i);
                    ++removedCount;
                }
            }
            if (items.size() == 0) {
                this.where = null;
            }
            return removedCount > 0;
        }
        if (this.where instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryOpWhere = (SQLBinaryOpExpr)this.where;
            final SQLBinaryOperator operator = binaryOpWhere.getOperator();
            if (operator == SQLBinaryOperator.BooleanAnd || operator == SQLBinaryOperator.BooleanOr) {
                final List<SQLExpr> items = SQLBinaryOpExpr.split(binaryOpWhere);
                int removedCount2 = 0;
                for (int j = items.size() - 1; j >= 0; --j) {
                    final SQLExpr item2 = items.get(j);
                    if (item2.equals(condition) && SQLUtils.replaceInParent(item2, null)) {
                        ++removedCount2;
                    }
                }
                return removedCount2 > 0;
            }
        }
        if (condition.equals(this.where)) {
            this.where = null;
            return true;
        }
        return false;
    }
    
    public void limit(final int rowCount, final int offset) {
        final SQLLimit limit = new SQLLimit();
        limit.setRowCount(new SQLIntegerExpr(rowCount));
        if (offset > 0) {
            limit.setOffset(new SQLIntegerExpr(offset));
        }
        this.setLimit(limit);
    }
    
    public String getCachedSelectList() {
        return this.cachedSelectList;
    }
    
    public void setCachedSelectList(final String cachedSelectList, final long cachedSelectListHash) {
        this.cachedSelectList = cachedSelectList;
        this.cachedSelectListHash = cachedSelectListHash;
    }
    
    public long getCachedSelectListHash() {
        return this.cachedSelectListHash;
    }
    
    @Override
    public DbType getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    public List<SQLCommentHint> getHintsDirect() {
        return this.hints;
    }
    
    public List<SQLCommentHint> getHints() {
        if (this.hints == null) {
            this.hints = new ArrayList<SQLCommentHint>(2);
        }
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    public int getHintsSize() {
        if (this.hints == null) {
            return 0;
        }
        return this.hints.size();
    }
    
    public boolean replaceInParent(final SQLSelectQuery x) {
        if (this.parent instanceof SQLSelect) {
            ((SQLSelect)this.parent).setQuery(x);
            return true;
        }
        if (this.parent instanceof SQLUnionQuery) {
            final SQLUnionQuery union = (SQLUnionQuery)this.parent;
            return union.replace(this, x);
        }
        return false;
    }
    
    public List<SQLSelectOrderByItem> getClusterBy() {
        if (this.clusterBy == null) {
            this.clusterBy = new ArrayList<SQLSelectOrderByItem>();
        }
        return this.clusterBy;
    }
    
    public List<SQLSelectOrderByItem> getClusterByDirect() {
        return this.clusterBy;
    }
    
    public void addClusterBy(final SQLSelectOrderByItem item) {
        if (this.clusterBy == null) {
            this.clusterBy = new ArrayList<SQLSelectOrderByItem>();
        }
        if (item != null) {
            item.setParent(this);
        }
        this.clusterBy.add(item);
    }
    
    public List<String> computeSelecteListAlias() {
        final List<String> aliasList = new ArrayList<String>();
        for (final SQLSelectItem item : this.selectList) {
            if (item instanceof OdpsUDTFSQLSelectItem) {
                aliasList.addAll(((OdpsUDTFSQLSelectItem)item).getAliasList());
            }
            else {
                final SQLExpr expr = item.getExpr();
                if (expr instanceof SQLAllColumnExpr) {
                    continue;
                }
                if (expr instanceof SQLPropertyExpr && ((SQLPropertyExpr)expr).getName().equals("*")) {
                    continue;
                }
                aliasList.add(item.computeAlias());
            }
        }
        return aliasList;
    }
    
    public List<SQLTableSource> getMappJoinTableSources() {
        if (this.hints == null) {
            return Collections.emptyList();
        }
        List<SQLTableSource> tableSources = null;
        for (final SQLCommentHint hint : this.hints) {
            final String text = hint.getText();
            if (text.startsWith("+")) {
                final SQLExpr hintExpr = SQLUtils.toSQLExpr(text.substring(1), this.dbType);
                if (!(hintExpr instanceof SQLMethodInvokeExpr)) {
                    continue;
                }
                final SQLMethodInvokeExpr func = (SQLMethodInvokeExpr)hintExpr;
                if (func.methodNameHashCode64() != FnvHash.Constants.MAPJOIN) {
                    continue;
                }
                for (final SQLExpr arg : func.getArguments()) {
                    final SQLIdentifierExpr tablename = (SQLIdentifierExpr)arg;
                    final SQLTableSource tableSource = this.findTableSource(tablename.getName());
                    if (tableSources == null) {
                        tableSources = new ArrayList<SQLTableSource>(2);
                    }
                    tableSources.add(tableSource);
                }
            }
        }
        if (tableSources == null) {
            return Collections.emptyList();
        }
        return tableSources;
    }
    
    public boolean clearMapJoinHint() {
        if (this.hints == null) {
            return false;
        }
        int removeCount = 0;
        for (int i = this.hints.size() - 1; i >= 0; --i) {
            final SQLCommentHint hint = this.hints.get(i);
            final String text = hint.getText();
            if (text.startsWith("+")) {
                final SQLExpr hintExpr = SQLUtils.toSQLExpr(text.substring(1), this.dbType);
                if (hintExpr instanceof SQLMethodInvokeExpr) {
                    final SQLMethodInvokeExpr func = (SQLMethodInvokeExpr)hintExpr;
                    if (func.methodNameHashCode64() == FnvHash.Constants.MAPJOIN) {
                        this.hints.remove(i);
                        ++removeCount;
                    }
                }
            }
        }
        return removeCount > 0;
    }
    
    private static class AggregationStatVisitor extends SQLASTVisitorAdapter
    {
        private boolean aggregation;
        
        private AggregationStatVisitor() {
            this.aggregation = false;
        }
        
        @Override
        public boolean visit(final SQLAggregateExpr x) {
            this.aggregation = true;
            return false;
        }
    }
}
