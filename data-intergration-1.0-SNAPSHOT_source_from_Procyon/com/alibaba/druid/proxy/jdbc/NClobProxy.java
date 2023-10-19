// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.sql.NClob;

public interface NClobProxy extends ClobProxy, NClob
{
    NClob getRawNClob();
}
