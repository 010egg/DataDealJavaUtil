// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;

public class MySqlHintStatement extends MySqlStatementImpl
{
    private List<SQLCommentHint> hints;
    private List<SQLStatement> hintStatements;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.hints);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    public int getHintVersion() {
        if (this.hints.size() != 1) {
            return -1;
        }
        final SQLCommentHint hint = this.hints.get(0);
        final String text = hint.getText();
        if (text.length() < 7) {
            return -1;
        }
        final char c0 = text.charAt(0);
        final char c2 = text.charAt(1);
        final char c3 = text.charAt(2);
        final char c4 = text.charAt(3);
        final char c5 = text.charAt(4);
        final char c6 = text.charAt(5);
        final char c7 = text.charAt(6);
        if (c0 != '!') {
            return -1;
        }
        if (c2 >= '0' && c2 <= '9' && c3 >= '0' && c3 <= '9' && c4 >= '0' && c4 <= '9' && c5 >= '0' && c5 <= '9' && c6 >= '0' && c6 <= '9' && c7 == ' ') {
            return (c2 - '0') * 10000 + (c3 - '0') * 1000 + (c4 - '0') * 100 + (c5 - '0') * 10 + (c6 - '0');
        }
        return -1;
    }
    
    public List<SQLStatement> getHintStatements() {
        if (this.hintStatements != null) {
            return this.hintStatements;
        }
        if (this.hints.size() != 1) {
            return null;
        }
        final SQLCommentHint hint = this.hints.get(0);
        final String text = hint.getText();
        if (text.length() < 7) {
            return null;
        }
        final char c0 = text.charAt(0);
        final char c2 = text.charAt(1);
        final char c3 = text.charAt(2);
        final char c4 = text.charAt(3);
        final char c5 = text.charAt(4);
        final char c6 = text.charAt(5);
        final char c7 = text.charAt(6);
        if (c0 != '!') {
            return null;
        }
        int start;
        if (c2 == ' ') {
            start = 2;
        }
        else {
            if (c2 < '0' || c2 > '9' || c3 < '0' || c3 > '9' || c4 < '0' || c4 > '9' || c5 < '0' || c5 > '9' || c6 < '0' || c6 > '9' || c7 != ' ') {
                return null;
            }
            start = 7;
        }
        final String hintSql = text.substring(start);
        return this.hintStatements = SQLUtils.parseStatements(hintSql, this.dbType);
    }
}
