// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import javax.management.openmbean.SimpleType;
import java.util.Map;
import javax.management.openmbean.CompositeDataSupport;
import java.util.HashMap;
import javax.management.openmbean.CompositeData;
import javax.management.MBeanServer;
import javax.management.JMException;
import javax.management.InstanceAlreadyExistsException;
import java.lang.management.ManagementFactory;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;

public final class JMXUtils
{
    private static final String[] THROWABLE_COMPOSITE_INDEX_NAMES;
    private static final String[] THROWABLE_COMPOSITE_INDEX_DESCRIPTIONS;
    private static final OpenType<?>[] THROWABLE_COMPOSITE_INDEX_TYPES;
    private static CompositeType THROWABLE_COMPOSITE_TYPE;
    
    public static ObjectName register(final String name, final Object mbean) {
        try {
            final ObjectName objectName = new ObjectName(name);
            final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
            try {
                mbeanServer.registerMBean(mbean, objectName);
            }
            catch (InstanceAlreadyExistsException ex) {
                mbeanServer.unregisterMBean(objectName);
                mbeanServer.registerMBean(mbean, objectName);
            }
            return objectName;
        }
        catch (JMException e) {
            throw new IllegalArgumentException(name, e);
        }
    }
    
    public static void unregister(final String name) {
        try {
            final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
            mbeanServer.unregisterMBean(new ObjectName(name));
        }
        catch (JMException e) {
            throw new IllegalArgumentException(name, e);
        }
    }
    
    public static CompositeType getThrowableCompositeType() throws JMException {
        if (JMXUtils.THROWABLE_COMPOSITE_TYPE == null) {
            JMXUtils.THROWABLE_COMPOSITE_TYPE = new CompositeType("Throwable", "Throwable", JMXUtils.THROWABLE_COMPOSITE_INDEX_NAMES, JMXUtils.THROWABLE_COMPOSITE_INDEX_DESCRIPTIONS, JMXUtils.THROWABLE_COMPOSITE_INDEX_TYPES);
        }
        return JMXUtils.THROWABLE_COMPOSITE_TYPE;
    }
    
    public static CompositeData getErrorCompositeData(final Throwable error) throws JMException {
        if (error == null) {
            return null;
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("class", error.getClass().getName());
        map.put("message", error.getMessage());
        map.put("stackTrace", Utils.getStackTrace(error));
        return new CompositeDataSupport(getThrowableCompositeType(), map);
    }
    
    static {
        THROWABLE_COMPOSITE_INDEX_NAMES = new String[] { "message", "class", "stackTrace" };
        THROWABLE_COMPOSITE_INDEX_DESCRIPTIONS = new String[] { "message", "class", "stackTrace" };
        THROWABLE_COMPOSITE_INDEX_TYPES = new OpenType[] { SimpleType.STRING, SimpleType.STRING, SimpleType.STRING };
        JMXUtils.THROWABLE_COMPOSITE_TYPE = null;
    }
}
