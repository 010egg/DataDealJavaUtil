// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast;

public interface MySqlIndexHint extends MySqlHint
{
    public enum Option
    {
        JOIN("JOIN"), 
        ORDER_BY("ORDER BY"), 
        GROUP_BY("GROUP BY");
        
        public final String name;
        public final String name_lcase;
        
        private Option(final String name) {
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
    }
}
