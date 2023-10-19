// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid;

import com.alibaba.druid.util.FnvHash;

public enum DbType
{
    other(1L), 
    jtds(2L), 
    hsql(4L), 
    db2(8L), 
    postgresql(16L), 
    sqlserver(32L), 
    oracle(64L), 
    mysql(128L), 
    mariadb(256L), 
    derby(512L), 
    hive(1024L), 
    h2(2048L), 
    dm(4096L), 
    kingbase(8192L), 
    gbase(16384L), 
    oceanbase(32768L), 
    informix(65536L), 
    odps(131072L), 
    teradata(262144L), 
    phoenix(524288L), 
    edb(1048576L), 
    kylin(2097152L), 
    sqlite(4194304L), 
    ads(8388608L), 
    presto(16777216L), 
    elastic_search(33554432L), 
    hbase(67108864L), 
    drds(134217728L), 
    clickhouse(268435456L), 
    blink(536870912L), 
    antspark(1073741824L), 
    oceanbase_oracle(-2147483648L), 
    polardb(1L), 
    ali_oracle(2L), 
    mock(4L), 
    sybase(8L), 
    highgo(16L), 
    greenplum(32L), 
    gaussdb(64L), 
    trino(128L), 
    ingres(0L), 
    cloudscape(0L), 
    timesten(0L), 
    as400(0L), 
    sapdb(0L), 
    kdb(0L), 
    log4jdbc(0L), 
    xugu(0L), 
    firebirdsql(0L), 
    JSQLConnect(0L), 
    JTurbo(0L), 
    interbase(0L), 
    pointbase(0L), 
    edbc(0L), 
    mimer(0L);
    
    public final long mask;
    public final long hashCode64;
    
    private DbType(final long mask) {
        this.mask = mask;
        this.hashCode64 = FnvHash.hashCode64(this.name());
    }
    
    public static long of(final DbType... types) {
        long value = 0L;
        for (final DbType type : types) {
            value |= type.mask;
        }
        return value;
    }
    
    public static DbType of(final String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        if ("aliyun_ads".equalsIgnoreCase(name)) {
            return DbType.ads;
        }
        try {
            return valueOf(name);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public final boolean equals(final String other) {
        return this == of(other);
    }
}
