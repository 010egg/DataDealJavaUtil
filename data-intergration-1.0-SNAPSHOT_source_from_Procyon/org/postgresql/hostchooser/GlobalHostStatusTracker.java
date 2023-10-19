// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.hostchooser;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import org.postgresql.util.HostSpec;
import java.util.Map;

public class GlobalHostStatusTracker
{
    private static final Map<HostSpec, HostSpecStatus> hostStatusMap;
    
    public static void reportHostStatus(final HostSpec hostSpec, final HostStatus hostStatus) {
        final long now = System.currentTimeMillis();
        synchronized (GlobalHostStatusTracker.hostStatusMap) {
            final HostSpecStatus oldStatus = GlobalHostStatusTracker.hostStatusMap.get(hostSpec);
            if (oldStatus == null || updateStatusFromTo(oldStatus.status, hostStatus)) {
                GlobalHostStatusTracker.hostStatusMap.put(hostSpec, new HostSpecStatus(hostSpec, hostStatus, now));
            }
        }
    }
    
    private static boolean updateStatusFromTo(final HostStatus oldStatus, final HostStatus newStatus) {
        return oldStatus == null || newStatus != HostStatus.ConnectOK || (oldStatus != HostStatus.Master && oldStatus != HostStatus.Slave);
    }
    
    static List<HostSpecStatus> getCandidateHosts(final HostSpec[] hostSpecs, final HostRequirement targetServerType, final long hostRecheckMillis) {
        final List<HostSpecStatus> candidates = new ArrayList<HostSpecStatus>(hostSpecs.length);
        final long latestAllowedUpdate = System.currentTimeMillis() - hostRecheckMillis;
        synchronized (GlobalHostStatusTracker.hostStatusMap) {
            for (final HostSpec hostSpec : hostSpecs) {
                HostSpecStatus hostInfo = GlobalHostStatusTracker.hostStatusMap.get(hostSpec);
                if (hostInfo == null || hostInfo.lastUpdated < latestAllowedUpdate) {
                    hostInfo = new HostSpecStatus(hostSpec, null, Long.MAX_VALUE);
                }
                if (hostInfo.status == null || targetServerType.allowConnectingTo(hostInfo.status)) {
                    candidates.add(hostInfo);
                }
            }
        }
        return candidates;
    }
    
    static {
        hostStatusMap = new HashMap<HostSpec, HostSpecStatus>();
    }
    
    static class HostSpecStatus
    {
        final HostSpec host;
        final HostStatus status;
        final long lastUpdated;
        
        HostSpecStatus(final HostSpec host, final HostStatus hostStatus, final long lastUpdated) {
            this.host = host;
            this.status = hostStatus;
            this.lastUpdated = lastUpdated;
        }
        
        @Override
        public String toString() {
            return this.host.toString() + '=' + this.status;
        }
    }
}
