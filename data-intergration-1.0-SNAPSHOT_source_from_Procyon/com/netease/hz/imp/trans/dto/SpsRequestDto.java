// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.dto;

import java.util.Date;
import java.io.Serializable;

public class SpsRequestDto implements Serializable
{
    private static final long serialVersionUID = 270942523919L;
    private String username;
    private String appKey;
    private String appSecret;
    private Long date;
    private String branchId;
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getAppKey() {
        return this.appKey;
    }
    
    public void setAppKey(final String appKey) {
        this.appKey = appKey;
    }
    
    public String getAppSecret() {
        return this.appSecret;
    }
    
    public void setAppSecret(final String appSecret) {
        this.appSecret = appSecret;
    }
    
    public SpsRequestDto() {
        this.date = new Date().getTime();
    }
    
    public SpsRequestDto(final String username, final String appKey, final String appSecret) {
        this.username = username;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.date = new Date().getTime();
    }
    
    @Override
    public String toString() {
        return "{username='" + this.username + '\'' + ", appKey='" + this.appKey + '\'' + ", appSecret='" + this.appSecret + '\'' + ", date=" + this.date + '}';
    }
}
