// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import java.util.Collections;
import java.util.Collection;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.DbType;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLDbTypedObject;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLSelect extends SQLObjectImpl implements SQLDbTypedObject
{
    protected SQLWithSubqueryClause withSubQuery;
    protected SQLSelectQuery query;
    protected SQLOrderBy orderBy;
    protected SQLLimit limit;
    protected List<SQLHint> hints;
    protected SQLObject restriction;
    protected boolean forBrowse;
    protected List<String> forXmlOptions;
    protected SQLExpr xmlPath;
    protected SQLExpr rowCount;
    protected SQLExpr offset;
    private SQLHint headHint;
    
    public SQLSelect() {
        this.forXmlOptions = null;
    }
    
    public List<SQLHint> getHints() {
        if (this.hints == null) {
            this.hints = new ArrayList<SQLHint>(2);
        }
        return this.hints;
    }
    
    public int getHintsSize() {
        if (this.hints == null) {
            return 0;
        }
        return this.hints.size();
    }
    
    public SQLSelect(final SQLSelectQuery query) {
        this.forXmlOptions = null;
        this.setQuery(query);
    }
    
    public SQLWithSubqueryClause getWithSubQuery() {
        return this.withSubQuery;
    }
    
    public void setWithSubQuery(final SQLWithSubqueryClause x) {
        if (x != null) {
            x.setParent(this);
        }
        this.withSubQuery = x;
    }
    
    public SQLSelectQuery getQuery() {
        return this.query;
    }
    
    public void setQuery(final SQLSelectQuery query) {
        if (query != null) {
            query.setParent(this);
        }
        this.query = query;
    }
    
    public SQLSelectQueryBlock getQueryBlock() {
        if (this.query instanceof SQLSelectQueryBlock) {
            return (SQLSelectQueryBlock)this.query;
        }
        return null;
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
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            if (this.withSubQuery != null) {
                this.withSubQuery.accept0(v);
            }
            if (this.query != null) {
                this.query.accept(v);
            }
            if (this.restriction != null) {
                this.restriction.accept(v);
            }
            if (this.orderBy != null) {
                this.orderBy.accept(v);
            }
            if (this.hints != null) {
                for (final SQLHint hint : this.hints) {
                    hint.accept(v);
                }
            }
            if (this.offset != null) {
                this.offset.accept(v);
            }
            if (this.rowCount != null) {
                this.rowCount.accept(v);
            }
            if (this.headHint != null) {
                this.headHint.accept(v);
            }
        }
        v.endVisit(this);
    }
    
    @Override
    public DbType getDbType() {
        DbType dbType = null;
        final SQLObject parent = this.getParent();
        if (parent instanceof SQLStatement) {
            dbType = ((SQLStatement)parent).getDbType();
        }
        if (dbType == null && parent instanceof OracleSQLObject) {
            dbType = DbType.oracle;
        }
        if (dbType == null && this.query instanceof SQLSelectQueryBlock) {
            dbType = ((SQLSelectQueryBlock)this.query).dbType;
        }
        return dbType;
    }
    
    @Override
    public SQLSelect clone() {
        final SQLSelect x = new SQLSelect();
        x.withSubQuery = this.withSubQuery;
        if (this.query != null) {
            x.setQuery(this.query.clone());
        }
        if (this.orderBy != null) {
            x.setOrderBy(this.orderBy.clone());
        }
        if (this.restriction != null) {
            x.setRestriction(this.restriction.clone());
        }
        if (this.hints != null) {
            for (final SQLHint hint : this.hints) {
                x.hints.add(hint);
            }
        }
        x.forBrowse = this.forBrowse;
        if (this.forXmlOptions != null) {
            x.forXmlOptions = new ArrayList<String>(this.forXmlOptions);
        }
        if (this.xmlPath != null) {
            x.setXmlPath(this.xmlPath.clone());
        }
        if (this.rowCount != null) {
            x.setRowCount(this.rowCount.clone());
        }
        if (this.offset != null) {
            x.setOffset(this.offset.clone());
        }
        if (this.headHint != null) {
            x.setHeadHint(this.headHint.clone());
        }
        return x;
    }
    
    public boolean isSimple() {
        return this.withSubQuery == null && (this.hints == null || this.hints.size() == 0) && this.restriction == null && !this.forBrowse && (this.forXmlOptions == null || this.forXmlOptions.size() == 0) && this.xmlPath == null && this.rowCount == null && this.offset == null;
    }
    
    public SQLObject getRestriction() {
        return this.restriction;
    }
    
    public void setRestriction(final SQLObject restriction) {
        if (restriction != null) {
            restriction.setParent(this);
        }
        this.restriction = restriction;
    }
    
    public boolean isForBrowse() {
        return this.forBrowse;
    }
    
    public void setForBrowse(final boolean forBrowse) {
        this.forBrowse = forBrowse;
    }
    
    public List<String> getForXmlOptions() {
        if (this.forXmlOptions == null) {
            this.forXmlOptions = new ArrayList<String>(4);
        }
        return this.forXmlOptions;
    }
    
    public int getForXmlOptionsSize() {
        if (this.forXmlOptions == null) {
            return 0;
        }
        return this.forXmlOptions.size();
    }
    
    public SQLExpr getRowCount() {
        return this.rowCount;
    }
    
    public void setRowCount(final SQLExpr rowCount) {
        if (rowCount != null) {
            rowCount.setParent(this);
        }
        this.rowCount = rowCount;
    }
    
    public SQLHint getHeadHint() {
        return this.headHint;
    }
    
    public void setHeadHint(final SQLHint headHint) {
        this.headHint = headHint;
    }
    
    public SQLExpr getOffset() {
        return this.offset;
    }
    
    public void setOffset(final SQLExpr offset) {
        if (offset != null) {
            offset.setParent(this);
        }
        this.offset = offset;
    }
    
    public SQLExpr getXmlPath() {
        return this.xmlPath;
    }
    
    public void setXmlPath(final SQLExpr xmlPath) {
        if (xmlPath != null) {
            xmlPath.setParent(this);
        }
        this.xmlPath = xmlPath;
    }
    
    public List<String> computeSelecteListAlias() {
        final SQLSelectQueryBlock firstQuery = this.getFirstQueryBlock();
        if (firstQuery != null) {
            return firstQuery.computeSelecteListAlias();
        }
        return Collections.emptyList();
    }
    
    public SQLSelectQueryBlock getFirstQueryBlock() {
        if (this.query instanceof SQLSelectQueryBlock) {
            return (SQLSelectQueryBlock)this.query;
        }
        if (this.query instanceof SQLUnionQuery) {
            SQLUnionQuery union;
            for (union = (SQLUnionQuery)this.query; union.getLeft() instanceof SQLUnionQuery; union = (SQLUnionQuery)union.getLeft()) {}
            return union.getFirstQueryBlock();
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLSelect sqlSelect = (SQLSelect)o;
        if (this.forBrowse != sqlSelect.forBrowse) {
            return false;
        }
        Label_0075: {
            if (this.withSubQuery != null) {
                if (this.withSubQuery.equals(sqlSelect.withSubQuery)) {
                    break Label_0075;
                }
            }
            else if (sqlSelect.withSubQuery == null) {
                break Label_0075;
            }
            return false;
        }
        Label_0108: {
            if (this.query != null) {
                if (this.query.equals(sqlSelect.query)) {
                    break Label_0108;
                }
            }
            else if (sqlSelect.query == null) {
                break Label_0108;
            }
            return false;
        }
        Label_0141: {
            if (this.orderBy != null) {
                if (this.orderBy.equals(sqlSelect.orderBy)) {
                    break Label_0141;
                }
            }
            else if (sqlSelect.orderBy == null) {
                break Label_0141;
            }
            return false;
        }
        Label_0174: {
            if (this.limit != null) {
                if (this.limit.equals(sqlSelect.limit)) {
                    break Label_0174;
                }
            }
            else if (sqlSelect.limit == null) {
                break Label_0174;
            }
            return false;
        }
        Label_0209: {
            if (this.hints != null) {
                if (this.hints.equals(sqlSelect.hints)) {
                    break Label_0209;
                }
            }
            else if (sqlSelect.hints == null) {
                break Label_0209;
            }
            return false;
        }
        Label_0242: {
            if (this.restriction != null) {
                if (this.restriction.equals(sqlSelect.restriction)) {
                    break Label_0242;
                }
            }
            else if (sqlSelect.restriction == null) {
                break Label_0242;
            }
            return false;
        }
        Label_0277: {
            if (this.forXmlOptions != null) {
                if (this.forXmlOptions.equals(sqlSelect.forXmlOptions)) {
                    break Label_0277;
                }
            }
            else if (sqlSelect.forXmlOptions == null) {
                break Label_0277;
            }
            return false;
        }
        Label_0310: {
            if (this.xmlPath != null) {
                if (this.xmlPath.equals(sqlSelect.xmlPath)) {
                    break Label_0310;
                }
            }
            else if (sqlSelect.xmlPath == null) {
                break Label_0310;
            }
            return false;
        }
        Label_0343: {
            if (this.rowCount != null) {
                if (this.rowCount.equals(sqlSelect.rowCount)) {
                    break Label_0343;
                }
            }
            else if (sqlSelect.rowCount == null) {
                break Label_0343;
            }
            return false;
        }
        if (this.offset != null) {
            if (this.offset.equals(sqlSelect.offset)) {
                return (this.headHint != null) ? this.headHint.equals(sqlSelect.headHint) : (sqlSelect.headHint == null);
            }
        }
        else if (sqlSelect.offset == null) {
            return (this.headHint != null) ? this.headHint.equals(sqlSelect.headHint) : (sqlSelect.headHint == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.withSubQuery != null) ? this.withSubQuery.hashCode() : 0;
        result = 31 * result + ((this.query != null) ? this.query.hashCode() : 0);
        result = 31 * result + ((this.orderBy != null) ? this.orderBy.hashCode() : 0);
        result = 31 * result + ((this.limit != null) ? this.limit.hashCode() : 0);
        result = 31 * result + ((this.hints != null) ? this.hints.hashCode() : 0);
        result = 31 * result + ((this.restriction != null) ? this.restriction.hashCode() : 0);
        result = 31 * result + (this.forBrowse ? 1 : 0);
        result = 31 * result + ((this.forXmlOptions != null) ? this.forXmlOptions.hashCode() : 0);
        result = 31 * result + ((this.xmlPath != null) ? this.xmlPath.hashCode() : 0);
        result = 31 * result + ((this.rowCount != null) ? this.rowCount.hashCode() : 0);
        result = 31 * result + ((this.offset != null) ? this.offset.hashCode() : 0);
        result = 31 * result + ((this.headHint != null) ? this.headHint.hashCode() : 0);
        return result;
    }
    
    public boolean addWhere(final SQLExpr where) {
        if (where == null) {
            return false;
        }
        if (this.query instanceof SQLSelectQueryBlock) {
            ((SQLSelectQueryBlock)this.query).addWhere(where);
            return true;
        }
        if (this.query instanceof SQLUnionQuery) {
            final SQLSelectQueryBlock queryBlock = new SQLSelectQueryBlock(this.getDbType());
            queryBlock.setFrom(new SQLSelect(this.query), "u");
            queryBlock.addSelectItem(new SQLAllColumnExpr());
            queryBlock.setParent(queryBlock);
            this.query = queryBlock;
            return true;
        }
        return false;
    }
    
    public boolean replace(final SQLSelectQuery cmp, final SQLSelectQuery target) {
        if (cmp == this.query) {
            this.setQuery(target);
            return true;
        }
        return false;
    }
    
    public SQLLimit getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLLimit x) {
        if (x != null) {
            x.setParent(this);
        }
        this.limit = x;
    }
}
