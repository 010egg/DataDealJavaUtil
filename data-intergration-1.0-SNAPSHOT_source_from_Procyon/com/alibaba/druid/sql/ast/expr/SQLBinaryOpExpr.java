// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExprComparor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.alibaba.druid.sql.visitor.ParameterizedVisitor;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.Utils;
import java.util.Arrays;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLDbTypedObject;
import java.io.Serializable;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLBinaryOpExpr extends SQLExprImpl implements SQLReplaceable, Serializable, SQLDbTypedObject, Comparable<SQLBinaryOpExpr>
{
    private static final long serialVersionUID = 1L;
    protected SQLExpr left;
    protected SQLExpr right;
    protected SQLBinaryOperator operator;
    protected DbType dbType;
    private boolean parenthesized;
    protected transient List<SQLObject> mergedList;
    
    public SQLBinaryOpExpr() {
        this.parenthesized = false;
    }
    
    public SQLBinaryOpExpr(final DbType dbType) {
        this.parenthesized = false;
        this.dbType = dbType;
    }
    
    public SQLBinaryOpExpr(final SQLExpr left, final SQLBinaryOperator operator, final SQLExpr right) {
        this.parenthesized = false;
        if (left != null) {
            left.setParent(this);
        }
        if (right != null) {
            right.setParent(this);
        }
        this.left = left;
        this.right = right;
        this.operator = operator;
        if (this.dbType == null && left instanceof SQLBinaryOpExpr) {
            this.dbType = ((SQLBinaryOpExpr)left).dbType;
        }
        if (this.dbType == null && right instanceof SQLBinaryOpExpr) {
            this.dbType = ((SQLBinaryOpExpr)right).dbType;
        }
    }
    
    public SQLBinaryOpExpr(final SQLExpr left, final SQLBinaryOperator operator, final SQLExpr right, DbType dbType) {
        this.parenthesized = false;
        if (left != null) {
            left.setParent(this);
        }
        if (right != null) {
            right.setParent(this);
        }
        this.left = left;
        this.right = right;
        this.operator = operator;
        if (dbType == null && left instanceof SQLBinaryOpExpr) {
            dbType = ((SQLBinaryOpExpr)left).dbType;
        }
        if (dbType == null && right instanceof SQLBinaryOpExpr) {
            dbType = ((SQLBinaryOpExpr)right).dbType;
        }
        this.dbType = dbType;
    }
    
    public SQLBinaryOpExpr(final SQLExpr left, final SQLExpr right, final SQLBinaryOperator operator) {
        this.parenthesized = false;
        if (left != null) {
            left.setParent(this);
        }
        if (right != null) {
            right.setParent(this);
        }
        this.left = left;
        this.right = right;
        this.operator = operator;
    }
    
    @Override
    public DbType getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    public SQLExpr getLeft() {
        return this.left;
    }
    
    public void setLeft(final SQLExpr left) {
        if (left != null) {
            left.setParent(this);
        }
        this.left = left;
    }
    
    public SQLExpr getRight() {
        return this.right;
    }
    
    public void setRight(final SQLExpr right) {
        if (right != null) {
            right.setParent(this);
        }
        this.right = right;
    }
    
    public SQLBinaryOperator getOperator() {
        return this.operator;
    }
    
    public void setOperator(final SQLBinaryOperator operator) {
        this.operator = operator;
    }
    
    public boolean isParenthesized() {
        return this.parenthesized;
    }
    
    public void setParenthesized(final boolean parenthesized) {
        this.parenthesized = parenthesized;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.left != null) {
                this.left.accept(visitor);
            }
            if (this.right != null) {
                this.right.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        return Arrays.asList(this.left, this.right);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.left == null) ? 0 : this.left.hashCode());
        result = 31 * result + ((this.operator == null) ? 0 : this.operator.hashCode());
        result = 31 * result + ((this.right == null) ? 0 : this.right.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SQLBinaryOpExpr)) {
            return false;
        }
        final SQLBinaryOpExpr other = (SQLBinaryOpExpr)obj;
        return this.operator == other.operator && SQLExprUtils.equals(this.left, other.left) && SQLExprUtils.equals(this.right, other.right);
    }
    
    public boolean equals(final SQLBinaryOpExpr other) {
        return this.operator == other.operator && SQLExprUtils.equals(this.left, other.left) && SQLExprUtils.equals(this.right, other.right);
    }
    
    public boolean equalsIgoreOrder(final SQLBinaryOpExpr other) {
        return this == other || (other != null && this.operator == other.operator && ((Utils.equals(this.left, other.left) && Utils.equals(this.right, other.right)) || (Utils.equals(this.left, other.right) && Utils.equals(this.right, other.left))));
    }
    
    @Override
    public SQLBinaryOpExpr clone() {
        final SQLBinaryOpExpr x = new SQLBinaryOpExpr();
        if (this.left != null) {
            x.setLeft(this.left.clone());
        }
        if (this.right != null) {
            x.setRight(this.right.clone());
        }
        x.operator = this.operator;
        x.dbType = this.dbType;
        x.parenthesized = this.parenthesized;
        if (this.hint != null) {
            x.hint = this.hint.clone();
        }
        return x;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this, this.getDbType());
    }
    
    public static SQLExpr combine(final List<? extends SQLExpr> items, final SQLBinaryOperator op) {
        if (items == null || op == null) {
            return null;
        }
        final int size = items.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return (SQLExpr)items.get(0);
        }
        SQLBinaryOpExpr expr = new SQLBinaryOpExpr((SQLExpr)items.get(0), op, (SQLExpr)items.get(1));
        for (int i = 2; i < size; ++i) {
            final SQLExpr item = (SQLExpr)items.get(i);
            expr = new SQLBinaryOpExpr(expr, op, item);
        }
        return expr;
    }
    
    public static List<SQLExpr> split(final SQLBinaryOpExpr x) {
        return split(x, x.getOperator());
    }
    
    public static List<SQLExpr> split(final SQLExpr x, final SQLBinaryOperator op) {
        if (x instanceof SQLBinaryOpExprGroup) {
            final SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup)x;
            if (group.getOperator() == op) {
                return new ArrayList<SQLExpr>(group.getItems());
            }
        }
        else if (x instanceof SQLBinaryOpExpr) {
            return split((SQLBinaryOpExpr)x, op);
        }
        final List<SQLExpr> list = new ArrayList<SQLExpr>(1);
        list.add(x);
        return list;
    }
    
    public static List<SQLExpr> split(final SQLBinaryOpExpr x, final SQLBinaryOperator op) {
        if (x.getOperator() != op) {
            final List<SQLExpr> groupList = new ArrayList<SQLExpr>(1);
            groupList.add(x);
            return groupList;
        }
        final List<SQLExpr> groupList = new ArrayList<SQLExpr>();
        split(groupList, x, op);
        return groupList;
    }
    
    public static void split(final List<SQLExpr> outList, final SQLExpr expr, final SQLBinaryOperator op) {
        if (expr == null) {
            return;
        }
        if (!(expr instanceof SQLBinaryOpExpr)) {
            outList.add(expr);
            return;
        }
        final SQLBinaryOpExpr binaryExpr = (SQLBinaryOpExpr)expr;
        if (binaryExpr.getOperator() != op) {
            outList.add(binaryExpr);
            return;
        }
        final List<SQLExpr> rightList = new ArrayList<SQLExpr>();
        rightList.add(binaryExpr.getRight());
        SQLExpr left = binaryExpr.getLeft();
        while (true) {
            while (left instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr leftBinary = (SQLBinaryOpExpr)left;
                if (leftBinary.operator != op) {
                    outList.add(leftBinary);
                    for (int i = rightList.size() - 1; i >= 0; --i) {
                        final SQLExpr right = rightList.get(i);
                        if (right instanceof SQLBinaryOpExpr) {
                            final SQLBinaryOpExpr binaryRight = (SQLBinaryOpExpr)right;
                            if (binaryRight.operator == op) {
                                final SQLExpr rightLeft = binaryRight.getLeft();
                                if (rightLeft instanceof SQLBinaryOpExpr) {
                                    final SQLBinaryOpExpr rightLeftBinary = (SQLBinaryOpExpr)rightLeft;
                                    if (rightLeftBinary.operator == op) {
                                        split(outList, rightLeftBinary, op);
                                    }
                                    else {
                                        outList.add(rightLeftBinary);
                                    }
                                }
                                else {
                                    outList.add(rightLeft);
                                }
                                final SQLExpr rightRight = binaryRight.getRight();
                                if (rightRight instanceof SQLBinaryOpExpr) {
                                    final SQLBinaryOpExpr rightRightBinary = (SQLBinaryOpExpr)rightRight;
                                    if (rightRightBinary.operator == op) {
                                        split(outList, rightRightBinary, op);
                                    }
                                    else {
                                        outList.add(rightRightBinary);
                                    }
                                }
                                else {
                                    outList.add(rightRight);
                                }
                            }
                            else {
                                outList.add(binaryRight);
                            }
                        }
                        else {
                            outList.add(right);
                        }
                    }
                    return;
                }
                left = leftBinary.getLeft();
                rightList.add(leftBinary.getRight());
            }
            outList.add(left);
            continue;
        }
    }
    
    public static SQLExpr and(SQLExpr a, SQLExpr b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a instanceof SQLBinaryOpExprGroup) {
            final SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup)a;
            if (group.getOperator() == SQLBinaryOperator.BooleanAnd) {
                group.add(b);
                return group;
            }
            if (group.getOperator() == SQLBinaryOperator.BooleanOr && group.getItems().size() == 1) {
                a = group.getItems().get(0).clone();
            }
        }
        if (b instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr bb = (SQLBinaryOpExpr)b;
            if (bb.operator == SQLBinaryOperator.BooleanAnd) {
                return and(and(a, bb.left), bb.right);
            }
        }
        else if (b instanceof SQLBinaryOpExprGroup) {
            final SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup)b;
            if (group.getOperator() == SQLBinaryOperator.BooleanOr && group.getItems().size() == 1) {
                b = group.getItems().get(0).clone();
            }
        }
        if (a instanceof SQLBinaryOpExpr && b instanceof SQLBinaryOpExprGroup && ((SQLBinaryOpExprGroup)b).getOperator() == SQLBinaryOperator.BooleanAnd) {
            final SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup)b;
            group.add(0, a);
            return group;
        }
        return new SQLBinaryOpExpr(a, SQLBinaryOperator.BooleanAnd, b);
    }
    
    public static SQLExpr and(final SQLExpr a, final SQLExpr b, final SQLExpr c) {
        return and(and(a, b), c);
    }
    
    public static SQLExpr or(final SQLExpr a, final SQLExpr b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a instanceof SQLBinaryOpExprGroup) {
            final SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup)a;
            if (group.getOperator() == SQLBinaryOperator.BooleanOr) {
                group.add(b);
                return group;
            }
        }
        if (b instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr bb = (SQLBinaryOpExpr)b;
            if (bb.operator == SQLBinaryOperator.BooleanOr) {
                return or(or(a, bb.left), bb.right);
            }
        }
        return new SQLBinaryOpExpr(a, SQLBinaryOperator.BooleanOr, b);
    }
    
    public static SQLExpr or(final List<? extends SQLExpr> list) {
        if (list.size() == 0) {
            return null;
        }
        SQLExpr first = (SQLExpr)list.get(0);
        for (int i = 1; i < list.size(); ++i) {
            first = or(first, (SQLExpr)list.get(i));
        }
        return first;
    }
    
    public static SQLExpr andIfNotExists(final SQLExpr a, final SQLExpr b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        final List<SQLExpr> groupListA = new ArrayList<SQLExpr>();
        final List<SQLExpr> groupListB = new ArrayList<SQLExpr>();
        split(groupListA, a, SQLBinaryOperator.BooleanAnd);
        split(groupListB, b, SQLBinaryOperator.BooleanAnd);
        for (final SQLExpr itemB : groupListB) {
            boolean exist = false;
            for (final SQLExpr itemA : groupListA) {
                if (itemA.equals(itemB)) {
                    exist = true;
                }
                else {
                    if (!(itemA instanceof SQLBinaryOpExpr) || !(itemB instanceof SQLBinaryOpExpr) || !((SQLBinaryOpExpr)itemA).equalsIgoreOrder((SQLBinaryOpExpr)itemB)) {
                        continue;
                    }
                    exist = true;
                }
            }
            if (!exist) {
                groupListA.add(itemB);
            }
        }
        return combine(groupListA, SQLBinaryOperator.BooleanAnd);
    }
    
    public static SQLBinaryOpExpr isNotNull(final SQLExpr expr) {
        return new SQLBinaryOpExpr(expr, SQLBinaryOperator.IsNot, new SQLNullExpr());
    }
    
    public static SQLBinaryOpExpr isNull(final SQLExpr expr) {
        return new SQLBinaryOpExpr(expr, SQLBinaryOperator.Is, new SQLNullExpr());
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        final SQLObject parent = this.getParent();
        if (this.left == expr) {
            if (target == null) {
                return parent instanceof SQLReplaceable && ((SQLReplaceable)parent).replace(this, this.right);
            }
            this.setLeft(target);
            return true;
        }
        else {
            if (this.right != expr) {
                if (this.left instanceof SQLBinaryOpExpr) {
                    final SQLBinaryOperator operator = ((SQLBinaryOpExpr)this.left).getOperator();
                    if (operator == SQLBinaryOperator.BooleanAnd && ((SQLBinaryOpExpr)this.left).replace(expr, target)) {
                        return true;
                    }
                }
                return false;
            }
            if (target == null) {
                return parent instanceof SQLReplaceable && ((SQLReplaceable)parent).replace(this, this.left);
            }
            this.setRight(target);
            return true;
        }
    }
    
    public SQLExpr other(final SQLExpr x) {
        if (x == this.left) {
            return this.right;
        }
        if (x == this.right) {
            return this.left;
        }
        return null;
    }
    
    public boolean contains(final SQLExpr item) {
        return item instanceof SQLBinaryOpExpr && (this.equalsIgoreOrder((SQLBinaryOpExpr)item) || this.left.equals(item) || this.right.equals(item));
    }
    
    @Override
    public SQLDataType computeDataType() {
        if (this.operator == null) {
            return null;
        }
        if (this.operator.isRelational()) {
            return SQLBooleanExpr.DATA_TYPE;
        }
        SQLDataType leftDataType = null;
        SQLDataType rightDataType = null;
        if (this.left != null) {
            leftDataType = this.left.computeDataType();
        }
        if (this.right != null) {
            rightDataType = this.right.computeDataType();
        }
        switch (this.operator) {
            case Concat: {
                if (leftDataType != null) {
                    return leftDataType;
                }
                if (rightDataType != null) {
                    return rightDataType;
                }
                return SQLCharExpr.DATA_TYPE;
            }
            case BooleanXor:
            case Modulus:
            case Mod:
            case DIV:
            case Divide: {
                if (leftDataType != null) {
                    return leftDataType;
                }
                if (rightDataType != null) {
                    return rightDataType;
                }
                return null;
            }
            case Subtract:
            case Add:
            case Multiply: {
                if (leftDataType != null) {
                    if (rightDataType != null) {
                        if (leftDataType.nameHashCode64() == FnvHash.Constants.BIGINT && rightDataType.nameHashCode64() == FnvHash.Constants.NUMBER) {
                            return rightDataType;
                        }
                        if (leftDataType.isInt() && rightDataType.nameHashCode64() == FnvHash.Constants.INTERVAL) {
                            return rightDataType;
                        }
                        if ((leftDataType.nameHashCode64() == FnvHash.Constants.DATE || leftDataType.nameHashCode64() == FnvHash.Constants.DATETIME || leftDataType.nameHashCode64() == FnvHash.Constants.TIMESTAMP) && (rightDataType.nameHashCode64() == FnvHash.Constants.DATE || rightDataType.nameHashCode64() == FnvHash.Constants.DATETIME || rightDataType.nameHashCode64() == FnvHash.Constants.TIMESTAMP)) {
                            return new SQLDataTypeImpl("BIGING");
                        }
                    }
                    return leftDataType;
                }
                return null;
            }
            default: {
                return null;
            }
        }
    }
    
    public boolean conditionContainsTable(final String alias) {
        if (this.left == null || this.right == null) {
            return false;
        }
        if (this.left instanceof SQLPropertyExpr) {
            if (((SQLPropertyExpr)this.left).matchOwner(alias)) {
                return true;
            }
        }
        else if (this.left instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)this.left).conditionContainsTable(alias)) {
            return true;
        }
        if (this.right instanceof SQLPropertyExpr) {
            if (((SQLPropertyExpr)this.right).matchOwner(alias)) {
                return true;
            }
        }
        else if (this.right instanceof SQLBinaryOpExpr) {
            return ((SQLBinaryOpExpr)this.right).conditionContainsTable(alias);
        }
        return false;
    }
    
    public boolean conditionContainsColumn(final String column) {
        if (this.left == null || this.right == null) {
            return false;
        }
        if (this.left instanceof SQLIdentifierExpr) {
            if (((SQLIdentifierExpr)this.left).nameEquals(column)) {
                return true;
            }
        }
        else if (this.right instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)this.right).nameEquals(column)) {
            return true;
        }
        return false;
    }
    
    public static SQLBinaryOpExpr merge(final ParameterizedVisitor v, SQLBinaryOpExpr x) {
        final SQLObject parent = x.parent;
        while (x.right instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr rightBinary = (SQLBinaryOpExpr)x.right;
            if (x.left instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr leftBinaryExpr = (SQLBinaryOpExpr)x.left;
                if (SQLExprUtils.equals(leftBinaryExpr.right, rightBinary)) {
                    x = leftBinaryExpr;
                    v.incrementReplaceCunt();
                    continue;
                }
            }
            final SQLExpr mergedRight = merge(v, rightBinary);
            if (mergedRight != x.right) {
                x = new SQLBinaryOpExpr(x.left, x.operator, mergedRight);
                v.incrementReplaceCunt();
            }
            x.setParent(parent);
            break;
        }
        if (x.left instanceof SQLBinaryOpExpr) {
            final SQLExpr mergedLeft = merge(v, (SQLBinaryOpExpr)x.left);
            if (mergedLeft != x.left) {
                final SQLBinaryOpExpr tmp = new SQLBinaryOpExpr(mergedLeft, x.operator, x.right);
                tmp.setParent(parent);
                x = tmp;
                v.incrementReplaceCunt();
            }
        }
        if (x.operator == SQLBinaryOperator.BooleanOr && !v.isEnabled(VisitorFeature.OutputParameterizedQuesUnMergeInList) && x.left instanceof SQLBinaryOpExpr && x.right instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr leftBinary = (SQLBinaryOpExpr)x.left;
            final SQLBinaryOpExpr rightBinary2 = (SQLBinaryOpExpr)x.right;
            if (mergeEqual(leftBinary, rightBinary2)) {
                v.incrementReplaceCunt();
                leftBinary.setParent(x.parent);
                leftBinary.addMergedItem(rightBinary2);
                return leftBinary;
            }
            if (SQLExprUtils.isLiteralExpr(leftBinary.left) && leftBinary.operator == SQLBinaryOperator.BooleanOr && mergeEqual(leftBinary.right, x.right)) {
                v.incrementReplaceCunt();
                leftBinary.addMergedItem(rightBinary2);
                return leftBinary;
            }
        }
        return x;
    }
    
    private void addMergedItem(final SQLBinaryOpExpr item) {
        if (this.mergedList == null) {
            this.mergedList = new ArrayList<SQLObject>();
        }
        this.mergedList.add(item);
    }
    
    public List<SQLObject> getMergedList() {
        return this.mergedList;
    }
    
    private static boolean mergeEqual(final SQLExpr a, final SQLExpr b) {
        if (!(a instanceof SQLBinaryOpExpr)) {
            return false;
        }
        if (!(b instanceof SQLBinaryOpExpr)) {
            return false;
        }
        final SQLBinaryOpExpr binaryA = (SQLBinaryOpExpr)a;
        final SQLBinaryOpExpr binaryB = (SQLBinaryOpExpr)b;
        return binaryA.operator == SQLBinaryOperator.Equality && binaryB.operator == SQLBinaryOperator.Equality && (binaryA.right instanceof SQLLiteralExpr || binaryA.right instanceof SQLVariantRefExpr) && (binaryB.right instanceof SQLLiteralExpr || binaryB.right instanceof SQLVariantRefExpr) && binaryA.left.equals(binaryB.left);
    }
    
    public static boolean isOr(final SQLExpr x) {
        return x instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)x).getOperator() == SQLBinaryOperator.BooleanOr;
    }
    
    public static boolean isAnd(final SQLExpr x) {
        return x instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)x).getOperator() == SQLBinaryOperator.BooleanAnd;
    }
    
    public boolean isLeftNameAndRightLiteral() {
        return this.left instanceof SQLName && this.right instanceof SQLLiteralExpr;
    }
    
    public boolean isLeftFunctionAndRightLiteral() {
        return this.left instanceof SQLMethodInvokeExpr && this.right instanceof SQLLiteralExpr;
    }
    
    public boolean isNameAndLiteral() {
        return (this.left instanceof SQLLiteralExpr && this.right instanceof SQLName) || (this.left instanceof SQLName && this.right instanceof SQLLiteralExpr);
    }
    
    public boolean isBothName() {
        return this.left instanceof SQLName && this.right instanceof SQLName;
    }
    
    @Override
    public int compareTo(final SQLBinaryOpExpr o) {
        final int leftResult = SQLExprComparor.compareTo(this.left, o.left);
        if (leftResult != 0) {
            return leftResult;
        }
        final int opResult = this.operator.compareTo(o.operator);
        if (opResult != 0) {
            return opResult;
        }
        final int rightResult = SQLExprComparor.compareTo(this.right, o.right);
        if (rightResult != 0) {
            return rightResult;
        }
        return 0;
    }
    
    public boolean isLeftLiteralAndRightName() {
        return this.right instanceof SQLName && this.left instanceof SQLLiteralExpr;
    }
    
    public static SQLBinaryOpExpr conditionEq(final String column, final String value) {
        return new SQLBinaryOpExpr(SQLUtils.toSQLExpr(column), SQLBinaryOperator.Equality, new SQLCharExpr(value));
    }
    
    public static SQLBinaryOpExpr conditionEq(final String column, final int value) {
        return new SQLBinaryOpExpr(SQLUtils.toSQLExpr(column), SQLBinaryOperator.Equality, new SQLIntegerExpr(value));
    }
    
    public static SQLBinaryOpExpr conditionLike(final String column, final String value) {
        return new SQLBinaryOpExpr(SQLUtils.toSQLExpr(column), SQLBinaryOperator.Like, new SQLCharExpr(value));
    }
    
    public static SQLBinaryOpExpr conditionLike(final String column, final SQLExpr value) {
        return new SQLBinaryOpExpr(SQLUtils.toSQLExpr(column), SQLBinaryOperator.Like, value);
    }
    
    public static SQLBinaryOpExpr eq(final SQLExpr a, final SQLExpr b) {
        return new SQLBinaryOpExpr(a, SQLBinaryOperator.Equality, b);
    }
}
