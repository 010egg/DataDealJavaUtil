// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class MySqlAlterEventStatement extends MySqlStatementImpl implements SQLAlterStatement
{
    private SQLName definer;
    private SQLName name;
    private MySqlEventSchedule schedule;
    private boolean onCompletionPreserve;
    private SQLName renameTo;
    private Boolean enable;
    private boolean disableOnSlave;
    private SQLExpr comment;
    private SQLStatement eventBody;
    
    public MySqlAlterEventStatement() {
        this.setDbType(DbType.mysql);
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.definer);
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.schedule);
            this.acceptChild(visitor, this.renameTo);
            this.acceptChild(visitor, this.comment);
            this.acceptChild(visitor, this.eventBody);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getDefiner() {
        return this.definer;
    }
    
    public void setDefiner(final SQLName definer) {
        if (definer != null) {
            definer.setParent(this);
        }
        this.definer = definer;
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
    
    public MySqlEventSchedule getSchedule() {
        return this.schedule;
    }
    
    public void setSchedule(final MySqlEventSchedule schedule) {
        if (schedule != null) {
            schedule.setParent(this);
        }
        this.schedule = schedule;
    }
    
    public boolean isOnCompletionPreserve() {
        return this.onCompletionPreserve;
    }
    
    public void setOnCompletionPreserve(final boolean onCompletionPreserve) {
        this.onCompletionPreserve = onCompletionPreserve;
    }
    
    public SQLName getRenameTo() {
        return this.renameTo;
    }
    
    public void setRenameTo(final SQLName renameTo) {
        if (renameTo != null) {
            renameTo.setParent(this);
        }
        this.renameTo = renameTo;
    }
    
    public Boolean getEnable() {
        return this.enable;
    }
    
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    public boolean isDisableOnSlave() {
        return this.disableOnSlave;
    }
    
    public void setDisableOnSlave(final boolean disableOnSlave) {
        this.disableOnSlave = disableOnSlave;
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
    
    public SQLStatement getEventBody() {
        return this.eventBody;
    }
    
    public void setEventBody(final SQLStatement eventBody) {
        if (eventBody != null) {
            eventBody.setParent(this);
        }
        this.eventBody = eventBody;
    }
}
