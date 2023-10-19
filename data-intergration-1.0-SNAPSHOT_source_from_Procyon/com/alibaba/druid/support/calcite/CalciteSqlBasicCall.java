// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.calcite;

import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlBasicCall;

public class CalciteSqlBasicCall extends SqlBasicCall
{
    public CalciteSqlBasicCall(final SqlOperator operator, final SqlNode[] operands, final SqlParserPos pos) {
        super(operator, operands, pos);
    }
    
    public CalciteSqlBasicCall(final SqlOperator operator, final SqlNode[] operands, final SqlParserPos pos, final boolean expanded, final SqlLiteral functionQualifier) {
        super(operator, operands, pos, expanded, functionQualifier);
    }
}
