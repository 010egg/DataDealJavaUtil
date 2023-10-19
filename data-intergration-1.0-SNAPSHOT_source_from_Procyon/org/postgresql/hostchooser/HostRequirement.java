// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.hostchooser;

public enum HostRequirement
{
    any {
        @Override
        public boolean allowConnectingTo(final HostStatus status) {
            return status != HostStatus.ConnectFail;
        }
    }, 
    master {
        @Override
        public boolean allowConnectingTo(final HostStatus status) {
            return status == HostStatus.Master || status == HostStatus.ConnectOK;
        }
    }, 
    slave {
        @Override
        public boolean allowConnectingTo(final HostStatus status) {
            return status == HostStatus.Slave || status == HostStatus.ConnectOK;
        }
    }, 
    preferSlave {
        @Override
        public boolean allowConnectingTo(final HostStatus status) {
            return status != HostStatus.ConnectFail;
        }
    };
    
    public abstract boolean allowConnectingTo(final HostStatus p0);
}
