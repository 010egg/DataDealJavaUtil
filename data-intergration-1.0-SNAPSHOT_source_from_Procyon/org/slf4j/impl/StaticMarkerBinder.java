// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.impl;

import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.spi.MarkerFactoryBinder;

public class StaticMarkerBinder implements MarkerFactoryBinder
{
    public static final StaticMarkerBinder SINGLETON;
    final IMarkerFactory markerFactory;
    
    static {
        SINGLETON = new StaticMarkerBinder();
    }
    
    private StaticMarkerBinder() {
        this.markerFactory = new BasicMarkerFactory();
    }
    
    @Override
    public IMarkerFactory getMarkerFactory() {
        return this.markerFactory;
    }
    
    @Override
    public String getMarkerFactoryClassStr() {
        return BasicMarkerFactory.class.getName();
    }
}
