// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.calcite;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlSelect;

public class TDDLSqlSelect extends SqlSelect
{
    private SqlNodeList hints;
    private SqlNodeList headHints;
    
    public TDDLSqlSelect(final SqlParserPos pos, final SqlNodeList keywordList, final SqlNodeList selectList, final SqlNode from, final SqlNode where, final SqlNodeList groupBy, final SqlNode having, final SqlNodeList windowDecls, final SqlNodeList orderBy, final SqlNode offset, final SqlNode fetch, final SqlNodeList hints, final SqlNodeList headHints) {
        super(pos, keywordList, selectList, from, where, groupBy, having, windowDecls, orderBy, offset, fetch, (SqlNodeList)null);
        this.hints = hints;
        this.headHints = headHints;
    }
    
    public SqlNodeList getHints() {
        return this.hints;
    }
    
    public void setHints(final SqlNodeList hints) {
        this.hints = hints;
    }
    
    public SqlNodeList getHeadHints() {
        return this.headHints;
    }
    
    public void setHeadHints(final SqlNodeList headHints) {
        this.headHints = headHints;
    }
}
