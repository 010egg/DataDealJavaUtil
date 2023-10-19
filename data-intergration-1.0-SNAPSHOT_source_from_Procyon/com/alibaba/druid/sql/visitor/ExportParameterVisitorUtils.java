// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimeExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.ast.expr.SQLHexExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.sql.ast.expr.SQLNCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGExportParameterVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.MSSQLServerExportParameterVisitor;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2ExportParameterVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleExportParameterVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlExportParameterVisitor;
import com.alibaba.druid.DbType;

public final class ExportParameterVisitorUtils
{
    private ExportParameterVisitorUtils() {
    }
    
    public static ExportParameterVisitor createExportParameterVisitor(final Appendable out, DbType dbType) {
        if (dbType == null) {
            dbType = DbType.other;
        }
        switch (dbType) {
            case mysql:
            case mariadb: {
                return new MySqlExportParameterVisitor(out);
            }
            case oracle: {
                return new OracleExportParameterVisitor(out);
            }
            case db2: {
                return new DB2ExportParameterVisitor(out);
            }
            case h2: {
                return new MySqlExportParameterVisitor(out);
            }
            case sqlserver:
            case jtds: {
                return new MSSQLServerExportParameterVisitor(out);
            }
            case postgresql:
            case edb: {
                return new PGExportParameterVisitor(out);
            }
            default: {
                return new ExportParameterizedOutputVisitor(out);
            }
        }
    }
    
    public static boolean exportParamterAndAccept(final List<Object> parameters, final List<SQLExpr> list) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            final SQLExpr param = list.get(i);
            final SQLExpr result = exportParameter(parameters, param);
            if (result != param) {
                list.set(i, result);
            }
        }
        return false;
    }
    
    public static SQLExpr exportParameter(final List<Object> parameters, final SQLExpr param) {
        Object value = null;
        boolean replace = false;
        if (param instanceof SQLCharExpr) {
            value = ((SQLCharExpr)param).getText();
            final String vStr = (String)value;
            replace = true;
        }
        else if (param instanceof SQLNCharExpr) {
            value = ((SQLNCharExpr)param).getText();
            replace = true;
        }
        else if (param instanceof SQLBooleanExpr) {
            value = ((SQLBooleanExpr)param).getBooleanValue();
            replace = true;
        }
        else if (param instanceof SQLNumericLiteralExpr) {
            value = ((SQLNumericLiteralExpr)param).getNumber();
            replace = true;
        }
        else if (param instanceof SQLHexExpr) {
            value = ((SQLHexExpr)param).toBytes();
            replace = true;
        }
        else if (param instanceof SQLTimestampExpr) {
            value = ((SQLTimestampExpr)param).getValue();
            replace = true;
        }
        else if (param instanceof SQLDateExpr) {
            value = ((SQLDateExpr)param).getValue();
            replace = true;
        }
        else if (param instanceof SQLTimeExpr) {
            value = ((SQLTimeExpr)param).getValue();
            replace = true;
        }
        else if (param instanceof SQLListExpr) {
            final SQLListExpr list = (SQLListExpr)param;
            final List<Object> listValues = new ArrayList<Object>();
            for (int i = 0; i < list.getItems().size(); ++i) {
                final SQLExpr listItem = list.getItems().get(i);
                if (listItem instanceof SQLCharExpr) {
                    final Object listValue = ((SQLCharExpr)listItem).getText();
                    listValues.add(listValue);
                }
                else if (listItem instanceof SQLBooleanExpr) {
                    final Object listValue = ((SQLBooleanExpr)listItem).getBooleanValue();
                    listValues.add(listValue);
                }
                else if (listItem instanceof SQLNumericLiteralExpr) {
                    final Object listValue = ((SQLNumericLiteralExpr)listItem).getNumber();
                    listValues.add(listValue);
                }
                else if (param instanceof SQLHexExpr) {
                    final Object listValue = ((SQLHexExpr)listItem).toBytes();
                    listValues.add(listValue);
                }
            }
            if (listValues.size() == list.getItems().size()) {
                value = listValues;
                replace = true;
            }
        }
        else if (param instanceof SQLNullExpr) {
            value = null;
            replace = true;
        }
        if (replace) {
            final SQLObject parent = param.getParent();
            if (parent != null) {
                List<SQLObject> mergedList = null;
                if (parent instanceof SQLBinaryOpExpr) {
                    mergedList = ((SQLBinaryOpExpr)parent).getMergedList();
                }
                if (mergedList != null) {
                    final List<Object> mergedListParams = new ArrayList<Object>(mergedList.size() + 1);
                    for (int j = 0; j < mergedList.size(); ++j) {
                        final SQLObject item = mergedList.get(j);
                        if (item instanceof SQLBinaryOpExpr) {
                            final SQLBinaryOpExpr binaryOpItem = (SQLBinaryOpExpr)item;
                            exportParameter(mergedListParams, binaryOpItem.getRight());
                        }
                    }
                    if (mergedListParams.size() > 0) {
                        mergedListParams.add(0, value);
                        value = mergedListParams;
                    }
                }
            }
            if (parameters != null) {
                parameters.add(value);
            }
            return new SQLVariantRefExpr("?");
        }
        return param;
    }
    
    public static void exportParameter(final List<Object> parameters, final SQLBinaryOpExpr x) {
        if (x.getLeft() instanceof SQLLiteralExpr && x.getRight() instanceof SQLLiteralExpr && x.getOperator().isRelational()) {
            return;
        }
        final SQLExpr leftResult = exportParameter(parameters, x.getLeft());
        if (leftResult != x.getLeft()) {
            x.setLeft(leftResult);
        }
        final SQLExpr rightResult = exportParameter(parameters, x.getRight());
        if (rightResult != x.getRight()) {
            x.setRight(rightResult);
        }
    }
    
    public static void exportParameter(final List<Object> parameters, final SQLBetweenExpr x) {
        SQLExpr result = exportParameter(parameters, x.getBeginExpr());
        if (result != x.getBeginExpr()) {
            x.setBeginExpr(result);
        }
        result = exportParameter(parameters, x.getEndExpr());
        if (result != x.getBeginExpr()) {
            x.setEndExpr(result);
        }
    }
}
