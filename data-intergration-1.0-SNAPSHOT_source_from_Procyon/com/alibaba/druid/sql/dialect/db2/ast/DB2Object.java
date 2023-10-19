// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.ast;

import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2ASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;

public interface DB2Object extends SQLObject
{
    void accept0(final DB2ASTVisitor p0);
    
    public interface Constants
    {
        public static final long CURRENT_DATE = FnvHash.fnv1a_64_lower("CURRENT DATE");
        public static final long CURRENT_DATE2 = FnvHash.fnv1a_64_lower("CURRENT_DATE");
        public static final long CURRENT_TIME = FnvHash.fnv1a_64_lower("CURRENT TIME");
        public static final long CURRENT_SCHEMA = FnvHash.fnv1a_64_lower("CURRENT SCHEMA");
    }
}
