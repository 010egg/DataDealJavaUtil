// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import java.util.HashMap;
import java.util.Map;

public class GpUtil
{
    public static String URL;
    public static String USERNAME;
    public static String PASSWORD;
    public static String APP_KEY;
    private Map<String, String> gpOption;
    
    public GpUtil(final String db, final String table) {
        (this.gpOption = new HashMap<String, String>()).put("url", GpUtil.URL);
        this.gpOption.put("user", GpUtil.USERNAME);
        this.gpOption.put("password", "T9Hfwim9GtRswK");
        this.gpOption.put("dbschema", db);
        this.gpOption.put("dbtable", table);
    }
    
    public Map<String, String> getGpOption() {
        return this.gpOption;
    }
    
    static {
        GpUtil.URL = "jdbc:postgresql://10.1.133.17:5432/zib_prod";
        GpUtil.USERNAME = "zib";
        GpUtil.PASSWORD = "T9Hfwim9GtRswK";
        GpUtil.APP_KEY = "27821298239726099082710";
    }
}
