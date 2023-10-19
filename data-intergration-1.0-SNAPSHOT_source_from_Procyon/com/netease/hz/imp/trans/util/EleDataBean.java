// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import java.io.Serializable;

public class EleDataBean implements Serializable
{
    private String data;
    private String etl_time;
    private String type;
    
    public String getData() {
        return this.data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getEtl_time() {
        return this.etl_time;
    }
    
    public void setEtl_time(final String etl_time) {
        this.etl_time = etl_time;
    }
    
    @Override
    public String toString() {
        return "DataBean{data='" + this.data + '\'' + ", etl_time='" + this.etl_time + '\'' + '}';
    }
}
