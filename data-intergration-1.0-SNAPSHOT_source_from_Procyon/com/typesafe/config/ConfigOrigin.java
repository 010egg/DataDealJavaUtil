// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.util.List;
import java.net.URL;

public interface ConfigOrigin
{
    String description();
    
    String filename();
    
    URL url();
    
    String resource();
    
    int lineNumber();
    
    List<String> comments();
    
    ConfigOrigin withComments(final List<String> p0);
    
    ConfigOrigin withLineNumber(final int p0);
}
