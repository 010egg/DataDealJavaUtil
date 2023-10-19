// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.Iterator;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLSetStatement extends SQLStatementImpl
{
    private Option option;
    private List<SQLAssignItem> items;
    private List<SQLCommentHint> hints;
    
    public SQLSetStatement() {
        this.items = new ArrayList<SQLAssignItem>();
    }
    
    public SQLSetStatement(final DbType dbType) {
        super(dbType);
        this.items = new ArrayList<SQLAssignItem>();
    }
    
    public SQLSetStatement(final SQLExpr target, final SQLExpr value) {
        this(target, value, null);
    }
    
    public SQLSetStatement(final SQLExpr target, final SQLExpr value, final DbType dbType) {
        super(dbType);
        this.items = new ArrayList<SQLAssignItem>();
        final SQLAssignItem item = new SQLAssignItem(target, value);
        item.setParent(this);
        this.items.add(item);
    }
    
    public static SQLSetStatement plus(final SQLName target) {
        final SQLExpr value = new SQLBinaryOpExpr(target.clone(), SQLBinaryOperator.Add, new SQLIntegerExpr(1));
        return new SQLSetStatement(target, value);
    }
    
    public List<SQLAssignItem> getItems() {
        return this.items;
    }
    
    public void setItems(final List<SQLAssignItem> items) {
        this.items = items;
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    public Option getOption() {
        return this.option;
    }
    
    public void setOption(final Option option) {
        this.option = option;
    }
    
    public void set(final SQLExpr target, final SQLExpr value) {
        final SQLAssignItem assignItem = new SQLAssignItem(target, value);
        assignItem.setParent(this);
        this.items.add(assignItem);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
            this.acceptChild(visitor, this.hints);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append("SET ");
            for (int i = 0; i < this.items.size(); ++i) {
                if (i != 0) {
                    buf.append(", ");
                }
                final SQLAssignItem item = this.items.get(i);
                item.output(buf);
            }
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    public SQLSetStatement clone() {
        final SQLSetStatement x = new SQLSetStatement();
        for (final SQLAssignItem item : this.items) {
            final SQLAssignItem item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        if (this.hints != null) {
            for (final SQLCommentHint hint : this.hints) {
                final SQLCommentHint h2 = hint.clone();
                h2.setParent(x);
                x.hints.add(h2);
            }
        }
        return x;
    }
    
    @Override
    public List getChildren() {
        return this.items;
    }
    
    public enum Option
    {
        IDENTITY_INSERT, 
        PASSWORD, 
        GLOBAL, 
        SESSION, 
        LOCAL, 
        PROJECT;
    }
}
