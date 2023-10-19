// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.status;

import java.util.ArrayList;
import java.util.List;

public class StatusListenerAsList implements StatusListener
{
    List<Status> statusList;
    
    public StatusListenerAsList() {
        this.statusList = new ArrayList<Status>();
    }
    
    @Override
    public void addStatusEvent(final Status status) {
        this.statusList.add(status);
    }
    
    public List<Status> getStatusList() {
        return this.statusList;
    }
}
