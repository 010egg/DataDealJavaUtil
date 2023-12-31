// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.impl;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import org.slf4j.spi.MDCAdapter;

public class StaticMDCBinder
{
    public static final StaticMDCBinder SINGLETON;
    
    static {
        SINGLETON = new StaticMDCBinder();
    }
    
    private StaticMDCBinder() {
    }
    
    public MDCAdapter getMDCA() {
        return new LogbackMDCAdapter();
    }
    
    public String getMDCAdapterClassStr() {
        return LogbackMDCAdapter.class.getName();
    }
}
