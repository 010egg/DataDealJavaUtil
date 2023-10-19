// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.clause;

import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleExpr;
import com.alibaba.druid.sql.ast.SQLExprImpl;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class ModelClause extends OracleSQLObjectImpl
{
    private final List<CellReferenceOption> cellReferenceOptions;
    private ReturnRowsClause returnRowsClause;
    private final List<ReferenceModelClause> referenceModelClauses;
    private MainModelClause mainModel;
    
    public ModelClause() {
        this.cellReferenceOptions = new ArrayList<CellReferenceOption>();
        this.referenceModelClauses = new ArrayList<ReferenceModelClause>();
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.returnRowsClause);
            this.acceptChild(visitor, this.referenceModelClauses);
            this.acceptChild(visitor, this.mainModel);
        }
        visitor.endVisit(this);
    }
    
    public MainModelClause getMainModel() {
        return this.mainModel;
    }
    
    public void setMainModel(final MainModelClause mainModel) {
        this.mainModel = mainModel;
    }
    
    public ReturnRowsClause getReturnRowsClause() {
        return this.returnRowsClause;
    }
    
    public void setReturnRowsClause(final ReturnRowsClause returnRowsClause) {
        this.returnRowsClause = returnRowsClause;
    }
    
    public List<ReferenceModelClause> getReferenceModelClauses() {
        return this.referenceModelClauses;
    }
    
    public List<CellReferenceOption> getCellReferenceOptions() {
        return this.cellReferenceOptions;
    }
    
    @Override
    public ModelClause clone() {
        throw new UnsupportedOperationException();
    }
    
    public enum CellReferenceOption
    {
        IgnoreNav("IGNORE NAV"), 
        KeepNav("KEEP NAV"), 
        UniqueDimension("UNIQUE DIMENSION"), 
        UniqueSingleReference("UNIQUE SINGLE REFERENCE");
        
        public final String name;
        
        private CellReferenceOption() {
            this(null);
        }
        
        private CellReferenceOption(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    public static class ReturnRowsClause extends OracleSQLObjectImpl
    {
        private boolean all;
        
        public ReturnRowsClause() {
            this.all = false;
        }
        
        public boolean isAll() {
            return this.all;
        }
        
        public void setAll(final boolean all) {
            this.all = all;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            visitor.visit(this);
            visitor.endVisit(this);
        }
    }
    
    public static class ReferenceModelClause extends OracleSQLObjectImpl
    {
        private SQLExpr name;
        private SQLSelect subQuery;
        private final List<CellReferenceOption> cellReferenceOptions;
        
        public ReferenceModelClause() {
            this.cellReferenceOptions = new ArrayList<CellReferenceOption>();
        }
        
        public List<CellReferenceOption> getCellReferenceOptions() {
            return this.cellReferenceOptions;
        }
        
        public SQLExpr getName() {
            return this.name;
        }
        
        public void setName(final SQLExpr name) {
            this.name = name;
        }
        
        public SQLSelect getSubQuery() {
            return this.subQuery;
        }
        
        public void setSubQuery(final SQLSelect subQuery) {
            this.subQuery = subQuery;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
        }
    }
    
    public static class ModelColumnClause extends OracleSQLObjectImpl
    {
        private QueryPartitionClause queryPartitionClause;
        private String alias;
        private final List<ModelColumn> dimensionByColumns;
        private final List<ModelColumn> measuresColumns;
        
        public ModelColumnClause() {
            this.dimensionByColumns = new ArrayList<ModelColumn>();
            this.measuresColumns = new ArrayList<ModelColumn>();
        }
        
        public List<ModelColumn> getDimensionByColumns() {
            return this.dimensionByColumns;
        }
        
        public List<ModelColumn> getMeasuresColumns() {
            return this.measuresColumns;
        }
        
        public QueryPartitionClause getQueryPartitionClause() {
            return this.queryPartitionClause;
        }
        
        public void setQueryPartitionClause(final QueryPartitionClause queryPartitionClause) {
            this.queryPartitionClause = queryPartitionClause;
        }
        
        public String getAlias() {
            return this.alias;
        }
        
        public void setAlias(final String alias) {
            this.alias = alias;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.queryPartitionClause);
                this.acceptChild(visitor, this.dimensionByColumns);
                this.acceptChild(visitor, this.measuresColumns);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class ModelColumn extends OracleSQLObjectImpl
    {
        private SQLExpr expr;
        private String alias;
        
        public SQLExpr getExpr() {
            return this.expr;
        }
        
        public void setExpr(final SQLExpr expr) {
            this.expr = expr;
        }
        
        public String getAlias() {
            return this.alias;
        }
        
        public void setAlias(final String alias) {
            this.alias = alias;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.expr);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class QueryPartitionClause extends OracleSQLObjectImpl
    {
        private List<SQLExpr> exprList;
        
        public QueryPartitionClause() {
            this.exprList = new ArrayList<SQLExpr>();
        }
        
        public List<SQLExpr> getExprList() {
            return this.exprList;
        }
        
        public void setExprList(final List<SQLExpr> exprList) {
            this.exprList = exprList;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.exprList);
            }
        }
    }
    
    public static class MainModelClause extends OracleSQLObjectImpl
    {
        private SQLExpr mainModelName;
        private ModelColumnClause modelColumnClause;
        private final List<CellReferenceOption> cellReferenceOptions;
        private ModelRulesClause modelRulesClause;
        
        public MainModelClause() {
            this.cellReferenceOptions = new ArrayList<CellReferenceOption>();
        }
        
        public ModelRulesClause getModelRulesClause() {
            return this.modelRulesClause;
        }
        
        public void setModelRulesClause(final ModelRulesClause modelRulesClause) {
            this.modelRulesClause = modelRulesClause;
        }
        
        public List<CellReferenceOption> getCellReferenceOptions() {
            return this.cellReferenceOptions;
        }
        
        public ModelColumnClause getModelColumnClause() {
            return this.modelColumnClause;
        }
        
        public void setModelColumnClause(final ModelColumnClause modelColumnClause) {
            this.modelColumnClause = modelColumnClause;
        }
        
        public SQLExpr getMainModelName() {
            return this.mainModelName;
        }
        
        public void setMainModelName(final SQLExpr mainModelName) {
            this.mainModelName = mainModelName;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.mainModelName);
                this.acceptChild(visitor, this.modelColumnClause);
                this.acceptChild(visitor, this.modelRulesClause);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class ModelRulesClause extends OracleSQLObjectImpl
    {
        private final List<ModelRuleOption> options;
        private SQLExpr iterate;
        private SQLExpr until;
        private final List<CellAssignmentItem> cellAssignmentItems;
        
        public ModelRulesClause() {
            this.options = new ArrayList<ModelRuleOption>();
            this.cellAssignmentItems = new ArrayList<CellAssignmentItem>();
        }
        
        public SQLExpr getUntil() {
            return this.until;
        }
        
        public void setUntil(final SQLExpr until) {
            this.until = until;
        }
        
        public SQLExpr getIterate() {
            return this.iterate;
        }
        
        public void setIterate(final SQLExpr iterate) {
            this.iterate = iterate;
        }
        
        public List<ModelRuleOption> getOptions() {
            return this.options;
        }
        
        public List<CellAssignmentItem> getCellAssignmentItems() {
            return this.cellAssignmentItems;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.iterate);
                this.acceptChild(visitor, this.until);
                this.acceptChild(visitor, this.cellAssignmentItems);
            }
            visitor.endVisit(this);
        }
    }
    
    public enum ModelRuleOption
    {
        UPSERT("UPSERT"), 
        UPDATE("UPDATE"), 
        AUTOMATIC_ORDER("AUTOMATIC ORDER"), 
        SEQUENTIAL_ORDER("SEQUENTIAL ORDER");
        
        public final String name;
        
        private ModelRuleOption(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    public static class CellAssignmentItem extends OracleSQLObjectImpl
    {
        private ModelRuleOption option;
        private CellAssignment cellAssignment;
        private SQLOrderBy orderBy;
        private SQLExpr expr;
        
        public ModelRuleOption getOption() {
            return this.option;
        }
        
        public void setOption(final ModelRuleOption option) {
            this.option = option;
        }
        
        public CellAssignment getCellAssignment() {
            return this.cellAssignment;
        }
        
        public void setCellAssignment(final CellAssignment cellAssignment) {
            this.cellAssignment = cellAssignment;
        }
        
        public SQLOrderBy getOrderBy() {
            return this.orderBy;
        }
        
        public void setOrderBy(final SQLOrderBy orderBy) {
            this.orderBy = orderBy;
        }
        
        public SQLExpr getExpr() {
            return this.expr;
        }
        
        public void setExpr(final SQLExpr expr) {
            this.expr = expr;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.cellAssignment);
                this.acceptChild(visitor, this.orderBy);
                this.acceptChild(visitor, this.expr);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class CellAssignment extends SQLExprImpl implements OracleExpr, SQLReplaceable
    {
        private SQLExpr measureColumn;
        private final List<SQLExpr> conditions;
        
        public CellAssignment() {
            this.conditions = new ArrayList<SQLExpr>();
        }
        
        public List<SQLExpr> getConditions() {
            return this.conditions;
        }
        
        public SQLExpr getMeasureColumn() {
            return this.measureColumn;
        }
        
        public void setMeasureColumn(final SQLExpr e) {
            if (e != null) {
                e.setParent(this);
            }
            this.measureColumn = e;
        }
        
        @Override
        public boolean replace(final SQLExpr expr, final SQLExpr target) {
            if (this.measureColumn == expr) {
                this.setMeasureColumn(target);
                return true;
            }
            for (int i = 0; i < this.conditions.size(); ++i) {
                if (this.conditions.get(i) == expr) {
                    target.setParent(this);
                    this.conditions.set(i, target);
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.measureColumn);
                this.acceptChild(visitor, this.conditions);
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
            final CellAssignment that = (CellAssignment)o;
            if (this.measureColumn != null) {
                if (this.measureColumn.equals(that.measureColumn)) {
                    return this.conditions.equals(that.conditions);
                }
            }
            else if (that.measureColumn == null) {
                return this.conditions.equals(that.conditions);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int result = (this.measureColumn != null) ? this.measureColumn.hashCode() : 0;
            result = 31 * result + this.conditions.hashCode();
            return result;
        }
        
        @Override
        protected void accept0(final SQLASTVisitor visitor) {
            this.accept0((OracleASTVisitor)visitor);
        }
        
        @Override
        public SQLExpr clone() {
            final CellAssignment x = new CellAssignment();
            if (this.measureColumn != null) {
                x.setMeasureColumn(this.measureColumn.clone());
            }
            return null;
        }
        
        @Override
        public List<SQLObject> getChildren() {
            final List children = new ArrayList();
            children.add(this.measureColumn);
            children.addAll(this.conditions);
            return (List<SQLObject>)children;
        }
    }
}
