// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.blink.vsitor;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.blink.ast.BlinkCreateTableStatement;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class BlinkOutputVisitor extends SQLASTOutputVisitor implements BlinkVisitor
{
    public BlinkOutputVisitor(final Appendable appender) {
        super(appender);
    }
    
    public BlinkOutputVisitor(final Appendable appender, final DbType dbType) {
        super(appender, dbType);
    }
    
    public BlinkOutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
    }
    
    @Override
    public boolean visit(final BlinkCreateTableStatement x) {
        super.visit(x);
        return false;
    }
    
    @Override
    protected void printTableElements(final List<SQLTableElement> tableElementList) {
        final int size = tableElementList.size();
        if (size == 0) {
            return;
        }
        final BlinkCreateTableStatement stmt = (BlinkCreateTableStatement)tableElementList.get(0).getParent();
        this.print0(" (");
        ++this.indentCount;
        this.println();
        for (int i = 0; i < size; ++i) {
            final SQLTableElement element = tableElementList.get(i);
            element.accept(this);
            if (i != size - 1) {
                this.print(',');
            }
            if (this.isPrettyFormat() && element.hasAfterComment()) {
                this.print(' ');
                this.printlnComment(element.getAfterCommentsDirect());
            }
            if (i != size - 1) {
                this.println();
            }
        }
        if (stmt.getPeriodFor() != null) {
            this.print(',');
            this.println();
            this.print0(this.ucase ? "PERIOD FOR " : "period for ");
            stmt.getPeriodFor().accept(this);
        }
        --this.indentCount;
        this.println();
        this.print(')');
    }
    
    @Override
    public void endVisit(final BlinkCreateTableStatement x) {
    }
}
