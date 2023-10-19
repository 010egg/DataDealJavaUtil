// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.osgi;

import java.util.Dictionary;
import org.osgi.service.jdbc.DataSourceFactory;
import org.postgresql.Driver;
import java.util.Hashtable;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.BundleActivator;

public class PGBundleActivator implements BundleActivator
{
    private ServiceRegistration _registration;
    
    public void start(final BundleContext context) throws Exception {
        final Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put("osgi.jdbc.driver.class", Driver.class.getName());
        properties.put("osgi.jdbc.driver.name", "PostgreSQL JDBC Driver");
        properties.put("osgi.jdbc.driver.version", Driver.getVersion());
        this._registration = context.registerService(DataSourceFactory.class.getName(), (Object)new PGDataSourceFactory(), (Dictionary)properties);
    }
    
    public void stop(final BundleContext context) throws Exception {
        if (this._registration != null) {
            this._registration.unregister();
            this._registration = null;
        }
        if (Driver.isRegistered()) {
            Driver.deregister();
        }
    }
}
