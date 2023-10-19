// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;

public interface OracleSegmentAttributes extends SQLObject
{
    SQLName getTablespace();
    
    void setTablespace(final SQLName p0);
    
    Boolean getCompress();
    
    void setCompress(final Boolean p0);
    
    Integer getCompressLevel();
    
    void setCompressLevel(final Integer p0);
    
    Integer getInitrans();
    
    void setInitrans(final Integer p0);
    
    Integer getMaxtrans();
    
    void setMaxtrans(final Integer p0);
    
    Integer getPctincrease();
    
    void setPctincrease(final Integer p0);
    
    Integer getPctused();
    
    void setPctused(final Integer p0);
    
    Integer getPctfree();
    
    void setPctfree(final Integer p0);
    
    Boolean getLogging();
    
    void setLogging(final Boolean p0);
    
    SQLObject getStorage();
    
    void setStorage(final SQLObject p0);
    
    boolean isCompressForOltp();
    
    void setCompressForOltp(final boolean p0);
}
