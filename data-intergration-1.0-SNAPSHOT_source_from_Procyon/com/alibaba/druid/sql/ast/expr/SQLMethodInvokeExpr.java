// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.SQLUtils;
import java.util.Map;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.Collection;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.util.FnvHash;
import java.util.Iterator;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import java.io.Serializable;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLMethodInvokeExpr extends SQLExprImpl implements SQLReplaceable, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final List<SQLExpr> arguments;
    protected String methodName;
    protected long methodNameHashCode64;
    protected SQLExpr owner;
    protected SQLExpr from;
    protected SQLExpr using;
    protected SQLExpr _for;
    protected String trimOption;
    protected transient SQLDataType resolvedReturnDataType;
    
    public SQLMethodInvokeExpr() {
        this.arguments = new ArrayList<SQLExpr>();
    }
    
    public SQLMethodInvokeExpr(final String methodName) {
        this.arguments = new ArrayList<SQLExpr>();
        this.methodName = methodName;
    }
    
    public SQLMethodInvokeExpr(final String methodName, final long methodNameHashCode64) {
        this.arguments = new ArrayList<SQLExpr>();
        this.methodName = methodName;
        this.methodNameHashCode64 = methodNameHashCode64;
    }
    
    public SQLMethodInvokeExpr(final String methodName, final SQLExpr owner) {
        this.arguments = new ArrayList<SQLExpr>();
        this.methodName = methodName;
        this.setOwner(owner);
    }
    
    public SQLMethodInvokeExpr(final String methodName, final SQLExpr owner, final SQLExpr... params) {
        this.arguments = new ArrayList<SQLExpr>();
        this.methodName = methodName;
        this.setOwner(owner);
        for (final SQLExpr param : params) {
            this.addArgument(param);
        }
    }
    
    public SQLMethodInvokeExpr(final String methodName, final SQLExpr owner, final List<SQLExpr> params) {
        this.arguments = new ArrayList<SQLExpr>();
        this.methodName = methodName;
        this.setOwner(owner);
        for (final SQLExpr param : params) {
            this.addArgument(param);
        }
    }
    
    public long methodNameHashCode64() {
        if (this.methodNameHashCode64 == 0L && this.methodName != null) {
            this.methodNameHashCode64 = FnvHash.hashCode64(this.methodName);
        }
        return this.methodNameHashCode64;
    }
    
    public String getMethodName() {
        return this.methodName;
    }
    
    public void setMethodName(final String methodName) {
        this.methodName = methodName;
        this.methodNameHashCode64 = 0L;
    }
    
    @Deprecated
    public List<SQLExpr> getParameters() {
        return this.arguments;
    }
    
    public List<SQLExpr> getArguments() {
        return this.arguments;
    }
    
    public void setArgument(final int i, final SQLExpr arg) {
        if (arg != null) {
            arg.setParent(this);
        }
        this.arguments.set(i, arg);
    }
    
    @Deprecated
    public void addParameter(final SQLExpr param) {
        if (param != null) {
            param.setParent(this);
        }
        this.arguments.add(param);
    }
    
    public void addArgument(final SQLExpr arg) {
        if (arg != null) {
            arg.setParent(this);
        }
        this.arguments.add(arg);
    }
    
    public SQLExpr getOwner() {
        return this.owner;
    }
    
    public void setOwner(final SQLExpr owner) {
        if (owner != null) {
            owner.setParent(this);
        }
        this.owner = owner;
    }
    
    public SQLExpr getFrom() {
        return this.from;
    }
    
    public void setFrom(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.from = x;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            if (this.owner != null) {
                this.owner.output(buf);
                buf.append(".");
            }
            buf.append(this.methodName);
            buf.append("(");
            for (int i = 0, size = this.arguments.size(); i < size; ++i) {
                if (i != 0) {
                    buf.append(", ");
                }
                this.arguments.get(i).output(buf);
            }
            buf.append(")");
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.owner != null) {
                this.owner.accept(visitor);
            }
            for (final SQLExpr arg : this.arguments) {
                if (arg != null) {
                    arg.accept(visitor);
                }
            }
            if (this.from != null) {
                this.from.accept(visitor);
            }
            if (this.using != null) {
                this.using.accept(visitor);
            }
            if (this._for != null) {
                this._for.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        if (this.owner == null) {
            return this.arguments;
        }
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.add(this.owner);
        children.addAll(this.arguments);
        return children;
    }
    
    protected void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.owner != null) {
                this.owner.accept(visitor);
            }
            for (final SQLExpr arg : this.arguments) {
                if (arg != null) {
                    arg.accept(visitor);
                }
            }
            if (this.from != null) {
                this.from.accept(visitor);
            }
            if (this.using != null) {
                this.using.accept(visitor);
            }
            if (this._for != null) {
                this._for.accept(visitor);
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
        final SQLMethodInvokeExpr that = (SQLMethodInvokeExpr)o;
        if (this.methodNameHashCode64() != that.methodNameHashCode64()) {
            return false;
        }
        if (this.owner != null) {
            if (this.owner.equals(that.owner)) {
                return this.arguments.equals(that.arguments) && ((this.from != null) ? this.from.equals(that.from) : (that.from == null));
            }
        }
        else if (that.owner == null) {
            return this.arguments.equals(that.arguments) && ((this.from != null) ? this.from.equals(that.from) : (that.from == null));
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (int)(this.methodNameHashCode64() ^ this.methodNameHashCode64() >>> 32);
        result = 31 * result + ((this.owner != null) ? this.owner.hashCode() : 0);
        result = 31 * result + this.arguments.hashCode();
        result = 31 * result + ((this.from != null) ? this.from.hashCode() : 0);
        return result;
    }
    
    @Override
    public SQLMethodInvokeExpr clone() {
        final SQLMethodInvokeExpr x = new SQLMethodInvokeExpr();
        this.cloneTo(x);
        return x;
    }
    
    public void cloneTo(final SQLMethodInvokeExpr x) {
        x.methodName = this.methodName;
        if (this.owner != null) {
            x.setOwner(this.owner.clone());
        }
        for (final SQLExpr arg : this.arguments) {
            x.addArgument(arg.clone());
        }
        if (this.from != null) {
            x.setFrom(this.from.clone());
        }
        if (this.using != null) {
            x.setUsing(this.using.clone());
        }
        if (this.trimOption != null) {
            x.setTrimOption(this.trimOption);
        }
        if (this.attributes != null) {
            for (final Map.Entry<String, Object> entry : this.attributes.entrySet()) {
                final String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof SQLObject) {
                    value = ((SQLObject)value).clone();
                }
                x.putAttribute(key, value);
            }
        }
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (target == null) {
            return false;
        }
        for (int i = 0; i < this.arguments.size(); ++i) {
            if (this.arguments.get(i) == expr) {
                this.arguments.set(i, target);
                target.setParent(this);
                return true;
            }
        }
        if (this.from == expr) {
            this.setFrom(target);
            return true;
        }
        if (this.using == expr) {
            this.setUsing(target);
            return true;
        }
        if (this._for == expr) {
            this.setFor(target);
            return true;
        }
        return false;
    }
    
    public boolean match(final String owner, final String function) {
        return function != null && SQLUtils.nameEquals(function, this.methodName) && ((owner == null && this.owner == null) || (owner != null && this.owner != null && this.owner instanceof SQLIdentifierExpr && SQLUtils.nameEquals(((SQLIdentifierExpr)this.owner).name, owner)));
    }
    
    @Override
    public SQLDataType computeDataType() {
        if (this.resolvedReturnDataType != null) {
            return this.resolvedReturnDataType;
        }
        final long nameHash = this.methodNameHashCode64();
        if (nameHash == FnvHash.Constants.TO_DATE || nameHash == FnvHash.Constants.ADD_MONTHS) {
            return this.resolvedReturnDataType = SQLDateExpr.DATA_TYPE;
        }
        if (nameHash == FnvHash.Constants.DATE_PARSE) {
            return this.resolvedReturnDataType = SQLTimestampExpr.DATA_TYPE;
        }
        if (nameHash == FnvHash.Constants.CURRENT_TIME || nameHash == FnvHash.Constants.CURTIME) {
            return this.resolvedReturnDataType = SQLTimeExpr.DATA_TYPE;
        }
        if (nameHash == FnvHash.Constants.BIT_COUNT || nameHash == FnvHash.Constants.ROW_NUMBER) {
            return this.resolvedReturnDataType = new SQLDataTypeImpl("BIGINT");
        }
        if (this.arguments.size() == 1) {
            if (nameHash == FnvHash.Constants.TRUNC) {
                return this.resolvedReturnDataType = this.arguments.get(0).computeDataType();
            }
        }
        else if (this.arguments.size() == 2) {
            final SQLExpr param0 = this.arguments.get(0);
            final SQLExpr param2 = this.arguments.get(1);
            if (nameHash == FnvHash.Constants.ROUND) {
                final SQLDataType dataType = param0.computeDataType();
                if (dataType != null) {
                    return dataType;
                }
            }
            else if (nameHash == FnvHash.Constants.NVL || nameHash == FnvHash.Constants.IFNULL || nameHash == FnvHash.Constants.ISNULL || nameHash == FnvHash.Constants.COALESCE) {
                final SQLDataType dataType = param0.computeDataType();
                if (dataType != null) {
                    return dataType;
                }
                return param2.computeDataType();
            }
            if (nameHash == FnvHash.Constants.MOD) {
                return this.resolvedReturnDataType = SQLIntegerExpr.DATA_TYPE;
            }
        }
        if (nameHash == FnvHash.Constants.STDDEV_SAMP) {
            return this.resolvedReturnDataType = SQLNumberExpr.DATA_TYPE_DOUBLE;
        }
        if (nameHash == FnvHash.Constants.CONCAT || nameHash == FnvHash.Constants.SUBSTR || nameHash == FnvHash.Constants.SUBSTRING) {
            return this.resolvedReturnDataType = SQLCharExpr.DATA_TYPE;
        }
        if (nameHash == FnvHash.Constants.YEAR || nameHash == FnvHash.Constants.MONTH || nameHash == FnvHash.Constants.DAY || nameHash == FnvHash.Constants.HOUR || nameHash == FnvHash.Constants.MINUTE || nameHash == FnvHash.Constants.SECOND || nameHash == FnvHash.Constants.PERIOD_ADD || nameHash == FnvHash.Constants.PERIOD_DIFF) {
            return this.resolvedReturnDataType = new SQLDataTypeImpl("INT");
        }
        if (nameHash == FnvHash.Constants.GROUPING) {
            return this.resolvedReturnDataType = new SQLDataTypeImpl("INT");
        }
        if (nameHash == FnvHash.Constants.JSON_EXTRACT_SCALAR || nameHash == FnvHash.Constants.FORMAT_DATETIME || nameHash == FnvHash.Constants.DATE_FORMAT) {
            return this.resolvedReturnDataType = SQLCharExpr.DATA_TYPE;
        }
        if (nameHash == FnvHash.Constants.DATE_ADD || nameHash == FnvHash.Constants.DATE_SUB || nameHash == FnvHash.Constants.DATE || nameHash == FnvHash.Constants.STR_TO_DATE || nameHash == FnvHash.Constants.CURRENT_DATE) {
            return this.resolvedReturnDataType = SQLDateExpr.DATA_TYPE;
        }
        if (nameHash == FnvHash.Constants.UNIX_TIMESTAMP) {
            return this.resolvedReturnDataType = SQLIntegerExpr.DATA_TYPE;
        }
        if (nameHash == FnvHash.Constants.TIME) {
            return this.resolvedReturnDataType = new SQLDataTypeImpl("VARCHAR");
        }
        if (nameHash == FnvHash.Constants.SYSDATE || nameHash == FnvHash.Constants.CURRENT_TIMESTAMP || nameHash == FnvHash.Constants.SYSTIMESTAMP) {
            return this.resolvedReturnDataType = SQLTimestampExpr.DATA_TYPE;
        }
        return null;
    }
    
    public SQLExpr getUsing() {
        return this.using;
    }
    
    public void setUsing(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.using = x;
    }
    
    public SQLExpr getFor() {
        return this._for;
    }
    
    public void setFor(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this._for = x;
    }
    
    public String getTrimOption() {
        return this.trimOption;
    }
    
    public void setTrimOption(final String trimOption) {
        this.trimOption = trimOption;
    }
    
    public SQLDataType getResolvedReturnDataType() {
        return this.resolvedReturnDataType;
    }
    
    public void setResolvedReturnDataType(final SQLDataType resolvedReturnDataType) {
        this.resolvedReturnDataType = resolvedReturnDataType;
    }
}
