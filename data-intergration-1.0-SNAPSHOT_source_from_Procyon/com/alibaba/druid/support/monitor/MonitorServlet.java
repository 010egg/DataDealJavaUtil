// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.monitor;

import java.util.HashSet;
import java.util.Set;
import com.alibaba.druid.support.http.ResourceServlet;

public class MonitorServlet extends ResourceServlet
{
    private String mappingPath;
    private Set<String> mapping;
    
    public MonitorServlet() {
        super("support/monitor/resources");
        this.mappingPath = "support/http/resources";
        (this.mapping = new HashSet<String>()).add("/css/bootstrap.min.css");
        this.mapping.add("/js/bootstrap.min.js");
        this.mapping.add("/js/jquery.min.js");
    }
    
    @Override
    protected String getFilePath(final String fileName) {
        if (this.mapping.contains(fileName)) {
            return this.mappingPath + fileName;
        }
        return super.getFilePath(fileName);
    }
    
    @Override
    protected String process(final String url) {
        return null;
    }
}
