// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.visitor;

import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.expr.SQLIntervalUnit;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateTableStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2ValuesStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class DB2OutputVisitor extends SQLASTOutputVisitor implements DB2ASTVisitor
{
    public DB2OutputVisitor(final Appendable appender) {
        super(appender, DbType.db2);
    }
    
    public DB2OutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
        this.dbType = DbType.db2;
    }
    
    @Override
    public boolean visit(final DB2SelectQueryBlock x) {
        this.visit(x);
        if (x.isForReadOnly()) {
            this.println();
            this.print0(this.ucase ? "FOR READ ONLY" : "for read only");
        }
        if (x.getIsolation() != null) {
            this.println();
            this.print0(this.ucase ? "WITH " : "with ");
            this.print0(x.getIsolation().name());
        }
        if (x.getOptimizeFor() != null) {
            this.println();
            this.print0(this.ucase ? "OPTIMIZE FOR " : "optimize for ");
            x.getOptimizeFor().accept(this);
        }
        return false;
    }
    
    @Override
    public void endVisit(final DB2SelectQueryBlock x) {
    }
    
    @Override
    public boolean visit(final DB2ValuesStatement x) {
        this.print0(this.ucase ? "VALUES " : "values ");
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public void endVisit(final DB2ValuesStatement x) {
    }
    
    @Override
    public boolean visit(final DB2CreateTableStatement x) {
        this.printCreateTable(x, true);
        if (x.isDataCaptureNone()) {
            this.println();
            this.print("DATA CAPTURE NONE");
        }
        else if (x.isDataCaptureChanges()) {
            this.println();
            this.print("DATA CAPTURE CHANGES");
        }
        final SQLName tablespace = x.getTablespace();
        if (tablespace != null) {
            this.println();
            this.print("IN ");
            tablespace.accept(this);
        }
        final SQLName indexIn = x.getIndexIn();
        if (indexIn != null) {
            this.println();
            this.print("INDEX IN ");
            indexIn.accept(this);
        }
        final SQLName database = x.getDatabase();
        if (database != null) {
            this.println();
            this.print("IN DATABASE ");
            database.accept(this);
        }
        final SQLName validproc = x.getValidproc();
        if (validproc != null) {
            this.println();
            this.print("VALIDPROC ");
            validproc.accept(this);
        }
        final SQLPartitionBy partitionBy = x.getPartitioning();
        if (partitionBy != null) {
            this.println();
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            partitionBy.accept(this);
        }
        final Boolean compress = x.getCompress();
        if (compress != null) {
            this.println();
            if (compress) {
                this.print0(this.ucase ? "COMPRESS YES" : "compress yes");
            }
            else {
                this.print0(this.ucase ? "COMPRESS NO" : "compress no");
            }
        }
        return false;
    }
    
    @Override
    public void endVisit(final DB2CreateTableStatement x) {
    }
    
    @Override
    protected void printOperator(final SQLBinaryOperator operator) {
        if (operator == SQLBinaryOperator.Concat) {
            this.print0(this.ucase ? "CONCAT" : "concat");
        }
        else {
            this.print0(this.ucase ? operator.name : operator.name_lcase);
        }
    }
    
    @Override
    public boolean visit(final SQLIntervalExpr x) {
        final SQLExpr value = x.getValue();
        value.accept(this);
        final SQLIntervalUnit unit = x.getUnit();
        if (unit != null) {
            this.print(' ');
            this.print0(this.ucase ? unit.name : unit.name_lcase);
            this.print(this.ucase ? 'S' : 's');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLColumnDefinition.Identity x) {
        this.print0(this.ucase ? "GENERATED ALWAYS AS IDENTITY" : "generated always as identity");
        final Integer seed = x.getSeed();
        final Integer increment = x.getIncrement();
        final Integer minValue = x.getMinValue();
        final Integer maxValue = x.getMaxValue();
        if (seed != null || increment != null || x.isCycle() || minValue != null || maxValue != null) {
            this.print0(" (");
        }
        if (seed != null) {
            this.print0(this.ucase ? "START WITH " : "start with ");
            this.print(seed);
        }
        if (increment != null) {
            if (seed != null) {
                this.print0(", ");
            }
            this.print0(this.ucase ? "INCREMENT BY " : "increment by ");
            this.print(increment);
        }
        if (x.isCycle()) {
            if (seed != null || increment != null) {
                this.print0(", ");
            }
            this.print0(this.ucase ? "CYCLE" : "cycle");
        }
        if (minValue != null) {
            if (seed != null || increment != null || x.isCycle()) {
                this.print0(", ");
            }
            this.print0(this.ucase ? "MINVALUE " : "minvalue ");
            this.print(minValue);
        }
        if (maxValue != null) {
            if (seed != null || increment != null || x.isCycle() || minValue != null) {
                this.print0(", ");
            }
            this.print0(this.ucase ? "MAXVALUE " : "maxvalue ");
            this.print(maxValue);
        }
        if (seed != null || increment != null || x.isCycle() || minValue != null || maxValue != null) {
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddColumn x) {
        this.print0(this.ucase ? "ADD COLUMNS " : "add columns ");
        this.printAndAccept(x.getColumns(), ", ");
        return false;
    }
}
