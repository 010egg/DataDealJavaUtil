// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateTriggerStatement extends SQLStatementImpl implements SQLCreateStatement
{
    private SQLName name;
    private boolean orReplace;
    private TriggerType triggerType;
    private SQLName definer;
    private boolean update;
    private boolean delete;
    private boolean insert;
    private SQLExprTableSource on;
    private boolean forEachRow;
    private List<SQLName> updateOfColumns;
    private SQLExpr when;
    private SQLStatement body;
    
    public SQLCreateTriggerStatement() {
        this.orReplace = false;
        this.forEachRow = false;
        this.updateOfColumns = new ArrayList<SQLName>();
    }
    
    public SQLCreateTriggerStatement(final DbType dbType) {
        super(dbType);
        this.orReplace = false;
        this.forEachRow = false;
        this.updateOfColumns = new ArrayList<SQLName>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.updateOfColumns);
            this.acceptChild(visitor, this.on);
            this.acceptChild(visitor, this.when);
            this.acceptChild(visitor, this.body);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.name != null) {
            children.add(this.name);
        }
        children.addAll(this.updateOfColumns);
        if (this.on != null) {
            children.add(this.on);
        }
        if (this.when != null) {
            children.add(this.when);
        }
        if (this.body != null) {
            children.add(this.body);
        }
        return children;
    }
    
    public SQLExprTableSource getOn() {
        return this.on;
    }
    
    public void setOn(final SQLName on) {
        this.setOn(new SQLExprTableSource(on));
    }
    
    public void setOn(final SQLExprTableSource on) {
        if (on != null) {
            on.setParent(this);
        }
        this.on = on;
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
    
    public SQLStatement getBody() {
        return this.body;
    }
    
    public void setBody(final SQLStatement body) {
        if (body != null) {
            body.setParent(this);
        }
        this.body = body;
    }
    
    public boolean isOrReplace() {
        return this.orReplace;
    }
    
    public void setOrReplace(final boolean orReplace) {
        this.orReplace = orReplace;
    }
    
    public TriggerType getTriggerType() {
        return this.triggerType;
    }
    
    public void setTriggerType(final TriggerType triggerType) {
        this.triggerType = triggerType;
    }
    
    public List<TriggerEvent> getTriggerEvents() {
        return null;
    }
    
    public boolean isForEachRow() {
        return this.forEachRow;
    }
    
    public void setForEachRow(final boolean forEachRow) {
        this.forEachRow = forEachRow;
    }
    
    public List<SQLName> getUpdateOfColumns() {
        return this.updateOfColumns;
    }
    
    public SQLExpr getWhen() {
        return this.when;
    }
    
    public void setWhen(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.when = x;
    }
    
    public boolean isUpdate() {
        return this.update;
    }
    
    public void setUpdate(final boolean update) {
        this.update = update;
    }
    
    public boolean isDelete() {
        return this.delete;
    }
    
    public void setDelete(final boolean delete) {
        this.delete = delete;
    }
    
    public boolean isInsert() {
        return this.insert;
    }
    
    public void setInsert(final boolean insert) {
        this.insert = insert;
    }
    
    public SQLName getDefiner() {
        return this.definer;
    }
    
    public void setDefiner(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.definer = x;
    }
    
    public enum TriggerType
    {
        BEFORE, 
        AFTER, 
        INSTEAD_OF;
    }
    
    public enum TriggerEvent
    {
        INSERT, 
        UPDATE, 
        DELETE;
    }
}
