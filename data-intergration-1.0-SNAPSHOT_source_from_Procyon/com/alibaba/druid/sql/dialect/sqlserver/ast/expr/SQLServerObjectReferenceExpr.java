// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast.expr;

import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.util.FnvHash;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerObjectImpl;

public class SQLServerObjectReferenceExpr extends SQLServerObjectImpl implements SQLServerExpr, SQLName
{
    private String server;
    private String database;
    private String schema;
    protected long schemaHashCode64;
    protected long hashCode64;
    
    public SQLServerObjectReferenceExpr() {
    }
    
    public SQLServerObjectReferenceExpr(final SQLExpr owner) {
        if (owner instanceof SQLIdentifierExpr) {
            this.database = ((SQLIdentifierExpr)owner).getName();
        }
        else {
            if (!(owner instanceof SQLPropertyExpr)) {
                throw new IllegalArgumentException(owner.toString());
            }
            final SQLPropertyExpr propExpr = (SQLPropertyExpr)owner;
            this.server = ((SQLIdentifierExpr)propExpr.getOwner()).getName();
            this.database = propExpr.getName();
        }
    }
    
    @Override
    public String getSimpleName() {
        if (this.schema != null) {
            return this.schema;
        }
        if (this.database != null) {
            return this.database;
        }
        return this.server;
    }
    
    @Override
    public void accept0(final SQLServerASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            boolean flag = false;
            if (this.server != null) {
                buf.append(this.server);
                flag = true;
            }
            if (flag) {
                buf.append('.');
            }
            if (this.database != null) {
                buf.append(this.database);
                flag = true;
            }
            if (flag) {
                buf.append('.');
            }
            if (this.schema != null) {
                buf.append(this.schema);
                flag = true;
            }
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    public String getServer() {
        return this.server;
    }
    
    public void setServer(final String server) {
        this.server = server;
    }
    
    public String getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final String database) {
        this.database = database;
    }
    
    public String getSchema() {
        return this.schema;
    }
    
    public void setSchema(final String schema) {
        this.schema = schema;
    }
    
    @Override
    public SQLServerObjectReferenceExpr clone() {
        final SQLServerObjectReferenceExpr x = new SQLServerObjectReferenceExpr();
        x.server = this.server;
        x.database = this.database;
        x.schema = this.schema;
        x.schemaHashCode64 = this.schemaHashCode64;
        x.hashCode64 = this.hashCode64;
        return x;
    }
    
    @Override
    public long nameHashCode64() {
        if (this.schemaHashCode64 == 0L && this.schema != null) {
            this.schemaHashCode64 = FnvHash.hashCode64(this.schema);
        }
        return this.schemaHashCode64;
    }
    
    @Override
    public long hashCode64() {
        if (this.hashCode64 == 0L) {
            if (this.server == null) {
                this.hashCode64 = new SQLPropertyExpr(new SQLPropertyExpr(this.server, this.database), this.schema).hashCode64();
            }
            else {
                this.hashCode64 = new SQLPropertyExpr(this.database, this.schema).hashCode64();
            }
        }
        return this.hashCode64;
    }
    
    @Override
    public SQLColumnDefinition getResolvedColumn() {
        return null;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
}
