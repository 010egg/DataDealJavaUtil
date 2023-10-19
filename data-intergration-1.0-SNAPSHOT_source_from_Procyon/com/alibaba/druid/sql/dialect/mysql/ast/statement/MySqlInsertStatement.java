// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;

public class MySqlInsertStatement extends SQLInsertStatement
{
    private boolean lowPriority;
    private boolean delayed;
    private boolean highPriority;
    private boolean ignore;
    private boolean rollbackOnFail;
    private boolean fulltextDictionary;
    private boolean overwrite;
    private boolean ifNotExists;
    protected List<SQLCommentHint> hints;
    private final List<SQLExpr> duplicateKeyUpdate;
    
    public MySqlInsertStatement() {
        this.lowPriority = false;
        this.delayed = false;
        this.highPriority = false;
        this.ignore = false;
        this.rollbackOnFail = false;
        this.fulltextDictionary = false;
        this.overwrite = false;
        this.ifNotExists = false;
        this.duplicateKeyUpdate = new ArrayList<SQLExpr>();
        this.dbType = DbType.mysql;
    }
    
    public void cloneTo(final MySqlInsertStatement x) {
        super.cloneTo(x);
        x.lowPriority = this.lowPriority;
        x.delayed = this.delayed;
        x.highPriority = this.highPriority;
        x.ignore = this.ignore;
        x.rollbackOnFail = this.rollbackOnFail;
        x.fulltextDictionary = this.fulltextDictionary;
        x.overwrite = this.overwrite;
        x.ifNotExists = this.ifNotExists;
        for (final SQLExpr e : this.duplicateKeyUpdate) {
            final SQLExpr e2 = e.clone();
            e2.setParent(x);
            x.duplicateKeyUpdate.add(e2);
        }
    }
    
    public List<SQLExpr> getDuplicateKeyUpdate() {
        return this.duplicateKeyUpdate;
    }
    
    public boolean isLowPriority() {
        return this.lowPriority;
    }
    
    public void setLowPriority(final boolean lowPriority) {
        this.lowPriority = lowPriority;
    }
    
    public boolean isDelayed() {
        return this.delayed;
    }
    
    public void setDelayed(final boolean delayed) {
        this.delayed = delayed;
    }
    
    public boolean isHighPriority() {
        return this.highPriority;
    }
    
    public void setHighPriority(final boolean highPriority) {
        this.highPriority = highPriority;
    }
    
    public boolean isIgnore() {
        return this.ignore;
    }
    
    public void setIgnore(final boolean ignore) {
        this.ignore = ignore;
    }
    
    public boolean isRollbackOnFail() {
        return this.rollbackOnFail;
    }
    
    public void setRollbackOnFail(final boolean rollbackOnFail) {
        this.rollbackOnFail = rollbackOnFail;
    }
    
    public boolean isFulltextDictionary() {
        return this.fulltextDictionary;
    }
    
    public void setFulltextDictionary(final boolean fulltextDictionary) {
        this.fulltextDictionary = fulltextDictionary;
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    protected void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.tableSource != null) {
                this.tableSource.accept(visitor);
            }
            if (this.columns != null) {
                for (final SQLExpr column : this.columns) {
                    if (column != null) {
                        column.accept(visitor);
                    }
                }
            }
            if (this.valuesList != null) {
                for (final ValuesClause values : this.valuesList) {
                    if (values != null) {
                        values.accept(visitor);
                    }
                }
            }
            if (this.query != null) {
                this.query.accept(visitor);
            }
            if (this.duplicateKeyUpdate != null) {
                for (final SQLExpr item : this.duplicateKeyUpdate) {
                    if (item != null) {
                        item.accept(visitor);
                    }
                }
            }
        }
        visitor.endVisit(this);
    }
    
    public int getHintsSize() {
        if (this.hints == null) {
            return 0;
        }
        return this.hints.size();
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> x) {
        if (x != null) {
            for (int i = 0; i < x.size(); ++i) {
                x.get(i).setParent(this);
            }
        }
        this.hints = x;
    }
    
    @Override
    public SQLInsertStatement clone() {
        final MySqlInsertStatement x = new MySqlInsertStatement();
        this.cloneTo(x);
        return x;
    }
    
    @Override
    public boolean isOverwrite() {
        return this.overwrite;
    }
    
    @Override
    public void setOverwrite(final boolean overwrite) {
        this.overwrite = overwrite;
    }
}
