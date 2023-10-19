// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.antspark.visitor;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import java.util.Map;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.antspark.ast.AntsparkCreateTableStatement;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveOutputVisitor;

public class AntsparkOutputVisitor extends HiveOutputVisitor implements AntsparkVisitor
{
    public AntsparkOutputVisitor(final Appendable appender, final DbType dbType) {
        super(appender, dbType);
    }
    
    public AntsparkOutputVisitor(final Appendable appender) {
        super(appender);
    }
    
    @Override
    public boolean visit(final AntsparkCreateTableStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isExternal()) {
            this.print0(this.ucase ? "EXTERNAL " : "external ");
        }
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "TABLE IF NOT EXISTS " : "table if not exists ");
        }
        else {
            this.print0(this.ucase ? "TABLE " : "table ");
        }
        x.getName().accept(this);
        if (x.getLike() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getLike().accept(this);
        }
        final List<SQLTableElement> tableElementList = x.getTableElementList();
        final int size = tableElementList.size();
        if (size > 0) {
            this.print0(" (");
            if (this.isPrettyFormat() && x.hasBodyBeforeComment()) {
                this.print(' ');
                this.printlnComment(x.getBodyBeforeCommentsDirect());
            }
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
            --this.indentCount;
            this.println();
            this.print(')');
        }
        if (x.getDatasource() != null) {
            this.println();
            this.print0(this.ucase ? "USING " : "using ");
            this.print0(x.getDatasource().toString());
        }
        if (x.getComment() != null) {
            this.println();
            this.print0(this.ucase ? "COMMENT " : "comment ");
            x.getComment().accept(this);
        }
        final int partitionSize = x.getPartitionColumns().size();
        if (partitionSize > 0) {
            this.println();
            this.print0(this.ucase ? "PARTITIONED BY (" : "partitioned by (");
            ++this.indentCount;
            this.println();
            for (int j = 0; j < partitionSize; ++j) {
                final SQLColumnDefinition column = x.getPartitionColumns().get(j);
                column.accept(this);
                if (j != partitionSize - 1) {
                    this.print(',');
                }
                if (this.isPrettyFormat() && column.hasAfterComment()) {
                    this.print(' ');
                    this.printlnComment(column.getAfterCommentsDirect());
                }
                if (j != partitionSize - 1) {
                    this.println();
                }
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
        final List<SQLSelectOrderByItem> clusteredBy = x.getClusteredBy();
        if (clusteredBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "CLUSTERED BY (" : "clustered by (");
            this.printAndAccept(clusteredBy, ",");
            this.print(')');
        }
        final List<SQLSelectOrderByItem> sortedBy = x.getSortedBy();
        if (sortedBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "SORTED BY (" : "sorted by (");
            this.printAndAccept(sortedBy, ", ");
            this.print(')');
        }
        final int buckets = x.getBuckets();
        if (buckets > 0) {
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            this.print(buckets);
            this.print0(this.ucase ? " BUCKETS" : " buckets");
        }
        final SQLExpr storedAs = x.getStoredAs();
        if (storedAs != null) {
            this.println();
            this.print0(this.ucase ? "STORED AS " : "stored as ");
            storedAs.accept(this);
        }
        final SQLSelect select = x.getSelect();
        if (select != null) {
            this.println();
            this.print0(this.ucase ? "AS" : "as");
            this.println();
            select.accept(this);
        }
        final Map<String, SQLObject> serdeProperties = x.getSerdeProperties();
        if (serdeProperties.size() > 0) {
            this.println();
            this.print0(this.ucase ? "TBLPROPERTIES (" : "tblproperties (");
            String seperator = "";
            for (final Map.Entry<String, SQLObject> entry : serdeProperties.entrySet()) {
                this.print0("'" + entry.getKey() + "'='");
                entry.getValue().accept(this);
                this.print0("'" + seperator);
                seperator = ",";
            }
            this.print(')');
        }
        final SQLExpr location = x.getLocation();
        if (location != null) {
            this.println();
            this.print0(this.ucase ? "LOCATION " : "location ");
            location.accept(this);
        }
        return false;
    }
    
    @Override
    public void endVisit(final AntsparkCreateTableStatement x) {
    }
}
