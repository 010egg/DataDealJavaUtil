// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.dialect.postgresql.ast.PGSQLObject;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Map;

public abstract class SQLObjectImpl implements SQLObject
{
    protected SQLObject parent;
    protected Map<String, Object> attributes;
    protected SQLCommentHint hint;
    protected int sourceLine;
    protected int sourceColumn;
    
    @Override
    public final void accept(final SQLASTVisitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException();
        }
        visitor.preVisit(this);
        this.accept0(visitor);
        visitor.postVisit(this);
    }
    
    protected abstract void accept0(final SQLASTVisitor p0);
    
    protected final void acceptChild(final SQLASTVisitor visitor, final List<? extends SQLObject> children) {
        if (children == null) {
            return;
        }
        for (int i = 0; i < children.size(); ++i) {
            this.acceptChild(visitor, (SQLObject)children.get(i));
        }
    }
    
    protected final void acceptChild(final SQLASTVisitor visitor, final SQLObject child) {
        if (child == null) {
            return;
        }
        child.accept(visitor);
    }
    
    @Override
    public void output(final StringBuffer buf) {
        this.output((Appendable)buf);
    }
    
    @Override
    public void output(final Appendable buf) {
        DbType dbType = null;
        if (this instanceof OracleSQLObject) {
            dbType = DbType.oracle;
        }
        else if (this instanceof MySqlObject) {
            dbType = DbType.mysql;
        }
        else if (this instanceof PGSQLObject) {
            dbType = DbType.postgresql;
        }
        else if (this instanceof SQLDbTypedObject) {
            dbType = ((SQLDbTypedObject)this).getDbType();
        }
        this.accept(SQLUtils.createOutputVisitor(buf, dbType));
    }
    
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        this.output(buf);
        return buf.toString();
    }
    
    @Override
    public SQLObject getParent() {
        return this.parent;
    }
    
    @Override
    public void setParent(final SQLObject parent) {
        this.parent = parent;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(1);
        }
        return this.attributes;
    }
    
    @Override
    public Object getAttribute(final String name) {
        if (this.attributes == null) {
            return null;
        }
        return this.attributes.get(name);
    }
    
    @Override
    public boolean containsAttribute(final String name) {
        return this.attributes != null && this.attributes.containsKey(name);
    }
    
    @Override
    public void putAttribute(final String name, final Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(1);
        }
        this.attributes.put(name, value);
    }
    
    @Override
    public Map<String, Object> getAttributesDirect() {
        return this.attributes;
    }
    
    @Override
    public void addBeforeComment(final String comment) {
        if (comment == null) {
            return;
        }
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(1);
        }
        List<String> comments = this.attributes.get("rowFormat.before_comment");
        if (comments == null) {
            comments = new ArrayList<String>(2);
            this.attributes.put("rowFormat.before_comment", comments);
        }
        comments.add(comment);
    }
    
    @Override
    public void addBeforeComment(final List<String> comments) {
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(1);
        }
        final List<String> attrComments = this.attributes.get("rowFormat.before_comment");
        if (attrComments == null) {
            this.attributes.put("rowFormat.before_comment", comments);
        }
        else {
            attrComments.addAll(comments);
        }
    }
    
    @Override
    public List<String> getBeforeCommentsDirect() {
        if (this.attributes == null) {
            return null;
        }
        return this.attributes.get("rowFormat.before_comment");
    }
    
    @Override
    public void addAfterComment(final String comment) {
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(1);
        }
        List<String> comments = this.attributes.get("rowFormat.after_comment");
        if (comments == null) {
            comments = new ArrayList<String>(2);
            this.attributes.put("rowFormat.after_comment", comments);
        }
        comments.add(comment);
    }
    
    @Override
    public void addAfterComment(final List<String> comments) {
        if (comments == null) {
            return;
        }
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(1);
        }
        final List<String> attrComments = this.attributes.get("rowFormat.after_comment");
        if (attrComments == null) {
            this.attributes.put("rowFormat.after_comment", comments);
        }
        else {
            attrComments.addAll(comments);
        }
    }
    
    @Override
    public List<String> getAfterCommentsDirect() {
        if (this.attributes == null) {
            return null;
        }
        return this.attributes.get("rowFormat.after_comment");
    }
    
    @Override
    public boolean hasBeforeComment() {
        if (this.attributes == null) {
            return false;
        }
        final List<String> comments = this.attributes.get("rowFormat.before_comment");
        return comments != null && !comments.isEmpty();
    }
    
    @Override
    public boolean hasAfterComment() {
        if (this.attributes == null) {
            return false;
        }
        final List<String> comments = this.attributes.get("rowFormat.after_comment");
        return comments != null && !comments.isEmpty();
    }
    
    @Override
    public SQLObject clone() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    
    public SQLDataType computeDataType() {
        return null;
    }
    
    public int getSourceLine() {
        return this.sourceLine;
    }
    
    public void setSourceLine(final int sourceLine) {
        this.sourceLine = sourceLine;
    }
    
    public int getSourceColumn() {
        return this.sourceColumn;
    }
    
    public void setSourceColumn(final int sourceColumn) {
        this.sourceColumn = sourceColumn;
    }
    
    public SQLCommentHint getHint() {
        return this.hint;
    }
    
    public void setHint(final SQLCommentHint hint) {
        this.hint = hint;
    }
}
