// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Calendar;

public interface JdbcParameter
{
    public static final int BinaryInputStream = 10001;
    public static final int AsciiInputStream = 10002;
    public static final int CharacterInputStream = 10003;
    public static final int NCharacterInputStream = 10004;
    public static final int URL = 10005;
    
    Object getValue();
    
    long getLength();
    
    Calendar getCalendar();
    
    int getSqlType();
    
    public interface TYPE
    {
        public static final int BinaryInputStream = 10001;
        public static final int AsciiInputStream = 10002;
        public static final int CharacterInputStream = 10003;
        public static final int NCharacterInputStream = 10004;
        public static final int URL = 10005;
        public static final int UnicodeStream = 10006;
        public static final int BYTES = 10007;
    }
}
