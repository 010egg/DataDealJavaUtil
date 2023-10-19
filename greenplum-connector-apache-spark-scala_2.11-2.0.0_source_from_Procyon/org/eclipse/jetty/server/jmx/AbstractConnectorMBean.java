// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server.jmx;

import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.jmx.ObjectMBean;

@ManagedObject("MBean Wrapper for Connectors")
public class AbstractConnectorMBean extends ObjectMBean
{
    final AbstractConnector _connector;
    
    public AbstractConnectorMBean(final Object managedObject) {
        super(managedObject);
        this._connector = (AbstractConnector)managedObject;
    }
    
    public String getObjectContextBasis() {
        return String.format("%s@%x", this._connector.getDefaultProtocol(), this._connector.hashCode());
    }
}
