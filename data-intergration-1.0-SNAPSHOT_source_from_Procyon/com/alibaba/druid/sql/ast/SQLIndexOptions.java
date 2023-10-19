// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;

public class SQLIndexOptions extends SQLObjectImpl
{
    private String indexType;
    private SQLExpr keyBlockSize;
    private String parserName;
    private SQLExpr comment;
    private String algorithm;
    private String lock;
    private boolean invisible;
    private boolean visible;
    private List<SQLAssignItem> otherOptions;
    
    public SQLIndexOptions() {
        this.otherOptions = new ArrayList<SQLAssignItem>();
    }
    
    public String getIndexType() {
        return this.indexType;
    }
    
    public void setIndexType(final String indexType) {
        this.indexType = indexType;
    }
    
    public SQLExpr getKeyBlockSize() {
        return this.keyBlockSize;
    }
    
    public void setKeyBlockSize(final SQLExpr keyBlockSize) {
        if (keyBlockSize != null) {
            if (this.getParent() != null && this.getParent().getParent() != null) {
                keyBlockSize.setParent(this.getParent().getParent());
            }
            else {
                keyBlockSize.setParent(this);
            }
        }
        this.keyBlockSize = keyBlockSize;
        if (keyBlockSize != null && this.getParent() != null && this.getParent() instanceof SQLIndexDefinition) {
            final SQLIndexDefinition parent = (SQLIndexDefinition)this.getParent();
            final SQLAssignItem assignItem = new SQLAssignItem(new SQLIdentifierExpr("KEY_BLOCK_SIZE"), keyBlockSize);
            if (this.getParent() != null && this.getParent().getParent() != null) {
                assignItem.setParent(this.getParent().getParent());
            }
            else {
                assignItem.setParent(this);
            }
            parent.getCompatibleOptions().add(assignItem);
        }
    }
    
    public String getParserName() {
        return this.parserName;
    }
    
    public void setParserName(final String parserName) {
        this.parserName = parserName;
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr comment) {
        if (comment != null) {
            if (this.getParent() != null && this.getParent().getParent() != null) {
                comment.setParent(this.getParent().getParent());
            }
            else {
                comment.setParent(this);
            }
        }
        this.comment = comment;
    }
    
    public String getAlgorithm() {
        return this.algorithm;
    }
    
    public void setAlgorithm(final String algorithm) {
        this.algorithm = algorithm;
        if (algorithm != null && this.getParent() != null && this.getParent() instanceof SQLIndexDefinition) {
            final SQLIndexDefinition parent = (SQLIndexDefinition)this.getParent();
            final SQLAssignItem assignItem = new SQLAssignItem(new SQLIdentifierExpr("ALGORITHM"), new SQLIdentifierExpr(algorithm));
            if (this.getParent() != null && this.getParent().getParent() != null) {
                assignItem.setParent(this.getParent().getParent());
            }
            else {
                assignItem.setParent(this);
            }
            parent.getCompatibleOptions().add(assignItem);
        }
    }
    
    public String getLock() {
        return this.lock;
    }
    
    public void setLock(final String lock) {
        this.lock = lock;
        if (lock != null && this.getParent() != null && this.getParent() instanceof SQLIndexDefinition) {
            final SQLIndexDefinition parent = (SQLIndexDefinition)this.getParent();
            final SQLAssignItem assignItem = new SQLAssignItem(new SQLIdentifierExpr("LOCK"), new SQLIdentifierExpr(lock));
            if (this.getParent() != null && this.getParent().getParent() != null) {
                assignItem.setParent(this.getParent().getParent());
            }
            else {
                assignItem.setParent(this);
            }
            parent.getCompatibleOptions().add(assignItem);
        }
    }
    
    public boolean isInvisible() {
        return this.invisible;
    }
    
    public void setInvisible(final boolean invisible) {
        this.invisible = invisible;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public List<SQLAssignItem> getOtherOptions() {
        return this.otherOptions;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public void cloneTo(final SQLIndexOptions options) {
        SQLObject parent;
        if (options.getParent() != null && options.getParent().getParent() != null) {
            parent = options.getParent().getParent();
        }
        else {
            parent = options;
        }
        options.indexType = this.indexType;
        if (this.keyBlockSize != null) {
            (options.keyBlockSize = this.keyBlockSize.clone()).setParent(parent);
        }
        options.parserName = this.parserName;
        if (this.comment != null) {
            (options.comment = this.comment.clone()).setParent(parent);
        }
        options.algorithm = this.algorithm;
        options.lock = this.lock;
        for (final SQLAssignItem item : this.otherOptions) {
            final SQLAssignItem item2 = item.clone();
            item2.setParent(parent);
            options.otherOptions.add(item2);
        }
        options.invisible = this.invisible;
    }
}
