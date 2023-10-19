// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;

public interface OracleConstraint extends OracleSQLObject, SQLConstraint, SQLTableElement
{
    SQLName getExceptionsInto();
    
    void setExceptionsInto(final SQLName p0);
    
    Boolean getDeferrable();
    
    void setDeferrable(final Boolean p0);
    
    Boolean getEnable();
    
    void setEnable(final Boolean p0);
    
    Boolean getValidate();
    
    void setValidate(final Boolean p0);
    
    Initially getInitially();
    
    void setInitially(final Initially p0);
    
    OracleUsingIndexClause getUsing();
    
    void setUsing(final OracleUsingIndexClause p0);
    
    OracleConstraint clone();
    
    public enum Initially
    {
        DEFERRED, 
        IMMEDIATE;
    }
}
