// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.encoding;

import java.io.UnsupportedEncodingException;

public class CharsetConvert
{
    private String clientEncoding;
    private String serverEncoding;
    private boolean enable;
    
    public CharsetConvert(final String clientEncoding, final String serverEncoding) {
        this.clientEncoding = null;
        this.serverEncoding = null;
        this.enable = false;
        this.clientEncoding = clientEncoding;
        this.serverEncoding = serverEncoding;
        if (clientEncoding != null && serverEncoding != null && !clientEncoding.equalsIgnoreCase(serverEncoding)) {
            this.enable = true;
        }
    }
    
    public String encode(String s) throws UnsupportedEncodingException {
        if (this.enable && !this.isEmpty(s)) {
            s = new String(s.getBytes(this.clientEncoding), this.serverEncoding);
        }
        return s;
    }
    
    public String decode(String s) throws UnsupportedEncodingException {
        if (this.enable && !this.isEmpty(s)) {
            s = new String(s.getBytes(this.serverEncoding), this.clientEncoding);
        }
        return s;
    }
    
    public boolean isEmpty(final String s) {
        return s == null || "".equals(s);
    }
}
