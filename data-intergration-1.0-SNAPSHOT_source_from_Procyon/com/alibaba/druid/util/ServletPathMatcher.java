// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

public class ServletPathMatcher implements PatternMatcher
{
    private static final ServletPathMatcher INSTANCE;
    
    public static ServletPathMatcher getInstance() {
        return ServletPathMatcher.INSTANCE;
    }
    
    @Override
    public boolean matches(String pattern, String source) {
        if (pattern == null || source == null) {
            return false;
        }
        pattern = pattern.trim();
        source = source.trim();
        if (pattern.endsWith("*")) {
            final int length = pattern.length() - 1;
            if (source.length() >= length && pattern.substring(0, length).equals(source.substring(0, length))) {
                return true;
            }
        }
        else if (pattern.startsWith("*")) {
            final int length = pattern.length() - 1;
            if (source.length() >= length && source.endsWith(pattern.substring(1))) {
                return true;
            }
        }
        else if (pattern.contains("*")) {
            final int start = pattern.indexOf("*");
            final int end = pattern.lastIndexOf("*");
            if (source.startsWith(pattern.substring(0, start)) && source.endsWith(pattern.substring(end + 1))) {
                return true;
            }
        }
        else if (pattern.equals(source)) {
            return true;
        }
        return false;
    }
    
    static {
        INSTANCE = new ServletPathMatcher();
    }
}
