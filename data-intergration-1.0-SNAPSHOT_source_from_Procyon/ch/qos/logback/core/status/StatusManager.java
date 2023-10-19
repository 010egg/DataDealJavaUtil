// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.status;

import java.util.List;

public interface StatusManager
{
    void add(final Status p0);
    
    List<Status> getCopyOfStatusList();
    
    int getCount();
    
    boolean add(final StatusListener p0);
    
    void remove(final StatusListener p0);
    
    void clear();
    
    List<StatusListener> getCopyOfStatusListenerList();
}
