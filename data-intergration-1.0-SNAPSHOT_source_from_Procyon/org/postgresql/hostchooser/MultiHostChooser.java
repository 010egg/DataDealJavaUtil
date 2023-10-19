// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.hostchooser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import org.postgresql.util.HostSpec;

public class MultiHostChooser implements HostChooser
{
    private HostSpec[] hostSpecs;
    private final HostRequirement targetServerType;
    private int hostRecheckTime;
    private boolean loadBalance;
    
    protected MultiHostChooser(final HostSpec[] hostSpecs, final HostRequirement targetServerType, final Properties info) {
        this.hostSpecs = hostSpecs;
        this.targetServerType = targetServerType;
        this.hostRecheckTime = Integer.parseInt(info.getProperty("hostRecheckSeconds", "10")) * 1000;
        this.loadBalance = Boolean.parseBoolean(info.getProperty("loadBalanceHosts", "false"));
    }
    
    @Override
    public Iterator<HostSpec> iterator() {
        final List<GlobalHostStatusTracker.HostSpecStatus> candidates = GlobalHostStatusTracker.getCandidateHosts(this.hostSpecs, this.targetServerType, this.hostRecheckTime);
        if (candidates.isEmpty()) {
            return Arrays.asList(this.hostSpecs).iterator();
        }
        if (candidates.size() == 1) {
            return Arrays.asList(candidates.get(0).host).iterator();
        }
        this.sortCandidates(candidates);
        this.shuffleGoodHosts(candidates);
        return this.extractHostSpecs(candidates).iterator();
    }
    
    private void sortCandidates(final List<GlobalHostStatusTracker.HostSpecStatus> candidates) {
        if (this.targetServerType == HostRequirement.any) {
            return;
        }
        Collections.sort(candidates, new HostSpecByTargetServerTypeComparator());
    }
    
    private void shuffleGoodHosts(final List<GlobalHostStatusTracker.HostSpecStatus> candidates) {
        if (!this.loadBalance) {
            return;
        }
        int count;
        for (count = 1; count < candidates.size(); ++count) {
            final GlobalHostStatusTracker.HostSpecStatus hostSpecStatus = candidates.get(count);
            if (hostSpecStatus.status != null && !this.targetServerType.allowConnectingTo(hostSpecStatus.status)) {
                break;
            }
        }
        if (count == 1) {
            return;
        }
        final List<GlobalHostStatusTracker.HostSpecStatus> goodHosts = candidates.subList(0, count);
        Collections.shuffle(goodHosts);
    }
    
    private List<HostSpec> extractHostSpecs(final List<GlobalHostStatusTracker.HostSpecStatus> hostSpecStatuses) {
        final List<HostSpec> hostSpecs = new ArrayList<HostSpec>(hostSpecStatuses.size());
        for (final GlobalHostStatusTracker.HostSpecStatus hostSpecStatus : hostSpecStatuses) {
            hostSpecs.add(hostSpecStatus.host);
        }
        return hostSpecs;
    }
    
    class HostSpecByTargetServerTypeComparator implements Comparator<GlobalHostStatusTracker.HostSpecStatus>
    {
        @Override
        public int compare(final GlobalHostStatusTracker.HostSpecStatus o1, final GlobalHostStatusTracker.HostSpecStatus o2) {
            final int r1 = this.rank(o1.status, MultiHostChooser.this.targetServerType);
            final int r2 = this.rank(o2.status, MultiHostChooser.this.targetServerType);
            return (r1 == r2) ? 0 : ((r1 > r2) ? -1 : 1);
        }
        
        private int rank(final HostStatus status, final HostRequirement targetServerType) {
            if (status == HostStatus.ConnectFail) {
                return -1;
            }
            switch (targetServerType) {
                case master: {
                    return (status == HostStatus.Master || status == null) ? 1 : 0;
                }
                case slave: {
                    return (status == HostStatus.Slave || status == null) ? 1 : 0;
                }
                case preferSlave: {
                    return (status == HostStatus.Slave || status == null) ? 2 : ((status == HostStatus.Master) ? 1 : 0);
                }
                default: {
                    return 0;
                }
            }
        }
    }
}
