// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.repository;

import java.util.HashMap;
import java.util.Map;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLFetchStatement;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;
import com.alibaba.druid.sql.ast.statement.SQLReplaceStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLMergeStatement;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyConstraint;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface SchemaResolveVisitor extends SQLASTVisitor
{
    boolean isEnabled(final Option p0);
    
    int getOptions();
    
    SchemaRepository getRepository();
    
    Context getContext();
    
    Context createContext(final SQLObject p0);
    
    void popContext();
    
    default boolean visit(final SQLSelectStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x.getSelect());
        return false;
    }
    
    default boolean visit(final SQLSelect x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLWithSubqueryClause x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLIfStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLCreateFunctionStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLExprTableSource x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLSelectQueryBlock x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLForeignKeyImpl x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLIdentifierExpr x) {
        SchemaResolveVisitorFactory.resolveIdent(this, x);
        return true;
    }
    
    default boolean visit(final SQLPropertyExpr x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLBinaryOpExpr x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLAllColumnExpr x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLCreateTableStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLUpdateStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLDeleteStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLAlterTableStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLInsertStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLParameter x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLDeclareItem x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLOver x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLMethodInvokeExpr x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLUnionQuery x) {
        SchemaResolveVisitorFactory.resolveUnion(this, x);
        return false;
    }
    
    default boolean visit(final SQLMergeStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLCreateProcedureStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLBlockStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLReplaceStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    default boolean visit(final SQLCastExpr x) {
        x.getExpr().accept(this);
        return true;
    }
    
    default boolean visit(final SQLFetchStatement x) {
        SchemaResolveVisitorFactory.resolve(this, x);
        return false;
    }
    
    public enum Option
    {
        ResolveAllColumn, 
        ResolveIdentifierAlias, 
        CheckColumnAmbiguous;
        
        public final int mask;
        
        private Option() {
            this.mask = 1 << this.ordinal();
        }
        
        public static int of(final Option... options) {
            if (options == null) {
                return 0;
            }
            int value = 0;
            for (final Option option : options) {
                value |= option.mask;
            }
            return value;
        }
    }
    
    public static class Context
    {
        public final Context parent;
        public final SQLObject object;
        public final int level;
        private SQLTableSource tableSource;
        private SQLTableSource from;
        private Map<Long, SQLTableSource> tableSourceMap;
        protected Map<Long, SQLDeclareItem> declares;
        
        public Context(final SQLObject object, final Context parent) {
            this.object = object;
            this.parent = parent;
            this.level = ((parent == null) ? 0 : (parent.level + 1));
        }
        
        public SQLTableSource getFrom() {
            return this.from;
        }
        
        public void setFrom(final SQLTableSource from) {
            this.from = from;
        }
        
        public SQLTableSource getTableSource() {
            return this.tableSource;
        }
        
        public void setTableSource(final SQLTableSource tableSource) {
            this.tableSource = tableSource;
        }
        
        public void addTableSource(final long alias_hash, final SQLTableSource tableSource) {
            if (this.tableSourceMap == null) {
                this.tableSourceMap = new HashMap<Long, SQLTableSource>();
            }
            this.tableSourceMap.put(alias_hash, tableSource);
        }
        
        protected void declare(final SQLDeclareItem x) {
            if (this.declares == null) {
                this.declares = new HashMap<Long, SQLDeclareItem>();
            }
            this.declares.put(x.getName().nameHashCode64(), x);
        }
        
        protected SQLDeclareItem findDeclare(final long nameHash) {
            if (this.declares == null) {
                return null;
            }
            return this.declares.get(nameHash);
        }
        
        protected SQLTableSource findTableSource(final long nameHash) {
            SQLTableSource table = null;
            if (this.tableSourceMap != null) {
                table = this.tableSourceMap.get(nameHash);
            }
            return table;
        }
        
        protected SQLTableSource findTableSourceRecursive(final long nameHash) {
            for (Context ctx = this; ctx != null; ctx = ctx.parent) {
                if (ctx.tableSourceMap != null) {
                    final SQLTableSource table = ctx.tableSourceMap.get(nameHash);
                    if (table != null) {
                        return table;
                    }
                }
            }
            return null;
        }
    }
}
