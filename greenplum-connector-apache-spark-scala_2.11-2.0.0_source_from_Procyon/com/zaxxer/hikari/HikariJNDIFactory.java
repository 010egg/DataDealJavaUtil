// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Enumeration;
import java.util.Set;
import javax.naming.RefAddr;
import java.util.Properties;
import com.zaxxer.hikari.util.PropertyElf;
import javax.naming.Reference;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

public class HikariJNDIFactory implements ObjectFactory
{
    @Override
    public synchronized Object getObjectInstance(final Object obj, final Name name, final Context nameCtx, final Hashtable<?, ?> environment) throws Exception {
        if (obj instanceof Reference && "javax.sql.DataSource".equals(((Reference)obj).getClassName())) {
            final Reference ref = (Reference)obj;
            final Set<String> hikariPropSet = PropertyElf.getPropertyNames(HikariConfig.class);
            final Properties properties = new Properties();
            final Enumeration<RefAddr> enumeration = ref.getAll();
            while (enumeration.hasMoreElements()) {
                final RefAddr element = enumeration.nextElement();
                final String type = element.getType();
                if (type.startsWith("dataSource.") || hikariPropSet.contains(type)) {
                    properties.setProperty(type, element.getContent().toString());
                }
            }
            return this.createDataSource(properties, nameCtx);
        }
        return null;
    }
    
    private DataSource createDataSource(final Properties properties, final Context context) throws NamingException {
        final String jndiName = properties.getProperty("dataSourceJNDI");
        if (jndiName != null) {
            return this.lookupJndiDataSource(properties, context, jndiName);
        }
        return new HikariDataSource(new HikariConfig(properties));
    }
    
    private DataSource lookupJndiDataSource(final Properties properties, final Context context, final String jndiName) throws NamingException {
        if (context == null) {
            throw new RuntimeException("JNDI context does not found for dataSourceJNDI : " + jndiName);
        }
        DataSource jndiDS = (DataSource)context.lookup(jndiName);
        if (jndiDS == null) {
            final Context ic = new InitialContext();
            jndiDS = (DataSource)ic.lookup(jndiName);
            ic.close();
        }
        if (jndiDS != null) {
            final HikariConfig config = new HikariConfig(properties);
            config.setDataSource(jndiDS);
            return new HikariDataSource(config);
        }
        return null;
    }
}
