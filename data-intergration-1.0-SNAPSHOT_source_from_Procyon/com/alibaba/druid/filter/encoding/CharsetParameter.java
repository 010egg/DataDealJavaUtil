// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.encoding;

public class CharsetParameter
{
    public static final String CLIENTENCODINGKEY = "clientEncoding";
    public static final String SERVERENCODINGKEY = "serverEncoding";
    private String clientEncoding;
    private String serverEncoding;
    
    public String getClientEncoding() {
        return this.clientEncoding;
    }
    
    public void setClientEncoding(final String clientEncoding) {
        this.clientEncoding = clientEncoding;
    }
    
    public String getServerEncoding() {
        return this.serverEncoding;
    }
    
    public void setServerEncoding(final String serverEncoding) {
        this.serverEncoding = serverEncoding;
    }
}
