// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.Properties;
import com.alibaba.druid.support.logging.Log;

public class PropertiesUtils
{
    private static final Log LOG;
    
    public static Properties loadProperties(final String file) {
        final Properties properties = new Properties();
        if (file == null) {
            return properties;
        }
        InputStream is = null;
        try {
            PropertiesUtils.LOG.debug("Trying to load " + file + " from FileSystem.");
            is = new FileInputStream(file);
        }
        catch (FileNotFoundException e3) {
            PropertiesUtils.LOG.debug("Trying to load " + file + " from Classpath.");
            try {
                is = PropertiesUtils.class.getResourceAsStream(file);
            }
            catch (Exception ex) {
                PropertiesUtils.LOG.warn("Can not load resource " + file, ex);
            }
        }
        if (is != null) {
            try {
                properties.load(is);
            }
            catch (Exception e) {
                PropertiesUtils.LOG.error("Exception occurred while loading " + file, e);
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (Exception e) {
                        PropertiesUtils.LOG.debug("Can not close Inputstream.", e);
                    }
                }
            }
            finally {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (Exception e2) {
                        PropertiesUtils.LOG.debug("Can not close Inputstream.", e2);
                    }
                }
            }
        }
        else {
            PropertiesUtils.LOG.warn("File " + file + " can't be loaded!");
        }
        return properties;
    }
    
    public static List<String> loadNameList(final Properties properties, final String propertyPrefix) {
        final List<String> nameList = new ArrayList<String>();
        final Set<String> names = new HashSet<String>();
        for (final String n : properties.stringPropertyNames()) {
            if (propertyPrefix != null && !propertyPrefix.isEmpty() && !n.startsWith(propertyPrefix)) {
                continue;
            }
            if (!n.endsWith(".url")) {
                continue;
            }
            names.add(n.split("\\.url")[0]);
        }
        if (!names.isEmpty()) {
            nameList.addAll(names);
        }
        return nameList;
    }
    
    public static Properties filterPrefix(final Properties properties, final String prefix) {
        if (properties == null || prefix == null || prefix.isEmpty()) {
            return properties;
        }
        final Properties result = new Properties();
        for (final String n : properties.stringPropertyNames()) {
            if (n.startsWith(prefix)) {
                result.setProperty(n, properties.getProperty(n));
            }
        }
        return result;
    }
    
    static {
        LOG = LogFactory.getLog(PropertiesUtils.class);
    }
}
