// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import org.postgresql.copy.CopyOperation;
import java.sql.SQLException;

public interface QueryExecutor
{
    public static final int QUERY_ONESHOT = 1;
    public static final int QUERY_NO_METADATA = 2;
    public static final int QUERY_NO_RESULTS = 4;
    public static final int QUERY_FORWARD_CURSOR = 8;
    public static final int QUERY_SUPPRESS_BEGIN = 16;
    public static final int QUERY_DESCRIBE_ONLY = 32;
    public static final int QUERY_BOTH_ROWS_AND_STATUS = 64;
    @Deprecated
    public static final int QUERY_DISALLOW_BATCHING = 128;
    public static final int QUERY_NO_BINARY_TRANSFER = 256;
    
    void execute(final Query p0, final ParameterList p1, final ResultHandler p2, final int p3, final int p4, final int p5) throws SQLException;
    
    void execute(final Query[] p0, final ParameterList[] p1, final ResultHandler p2, final int p3, final int p4, final int p5) throws SQLException;
    
    void fetch(final ResultCursor p0, final ResultHandler p1, final int p2) throws SQLException;
    
    Query createSimpleQuery(final String p0);
    
    Query createParameterizedQuery(final String p0);
    
    void processNotifies() throws SQLException;
    
    ParameterList createFastpathParameters(final int p0);
    
    byte[] fastpathCall(final int p0, final ParameterList p1, final boolean p2) throws SQLException;
    
    CopyOperation startCopy(final String p0, final boolean p1) throws SQLException;
}
