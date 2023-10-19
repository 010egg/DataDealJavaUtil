// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArraySet;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import java.util.Set;

public class SpringStatManager
{
    public static final String SYS_PROP_INSTANCES = "druid.spring.springStat";
    private static final SpringStatManager instance;
    private Set<Object> springStatSet;
    
    public SpringStatManager() {
        this.springStatSet = null;
    }
    
    public static SpringStatManager getInstance() {
        return SpringStatManager.instance;
    }
    
    public Set<Object> getSpringStatSet() {
        if (this.springStatSet == null) {
            if (DruidDataSourceStatManager.isRegisterToSystemProperty()) {
                this.springStatSet = getSpringStatSetFromSysProperty();
            }
            else {
                this.springStatSet = new CopyOnWriteArraySet<Object>();
            }
        }
        return this.springStatSet;
    }
    
    public void addSpringStat(final Object springStat) {
        this.getSpringStatSet().add(springStat);
    }
    
    static Set<Object> getSpringStatSetFromSysProperty() {
        final Properties properties = System.getProperties();
        Set<Object> webAppStats = (Set<Object>)properties.get("druid.spring.springStat");
        if (webAppStats == null) {
            synchronized (properties) {
                webAppStats = (Set<Object>)properties.get("druid.spring.springStat");
                if (webAppStats == null) {
                    webAppStats = new CopyOnWriteArraySet<Object>();
                    properties.put("druid.spring.springStat", webAppStats);
                }
            }
        }
        return webAppStats;
    }
    
    public List<Map<String, Object>> getMethodStatData() {
        final Set<Object> stats = this.getSpringStatSet();
        final List<Map<String, Object>> allMethodStatDataList = new ArrayList<Map<String, Object>>();
        for (final Object stat : stats) {
            final List<Map<String, Object>> methodStatDataList = SpringStatUtils.getMethodStatDataList(stat);
            allMethodStatDataList.addAll(methodStatDataList);
        }
        return allMethodStatDataList;
    }
    
    public Map<String, Object> getMethodStatData(final String clazz, final String method) {
        final Set<Object> stats = this.getSpringStatSet();
        for (final Object stat : stats) {
            final Map<String, Object> statData = SpringStatUtils.getMethodStatData(stat, clazz, method);
            if (statData != null) {
                return statData;
            }
        }
        return null;
    }
    
    public void resetStat() {
        final Set<Object> stats = this.getSpringStatSet();
        for (final Object stat : stats) {
            SpringStatUtils.reset(stat);
        }
    }
    
    static {
        instance = new SpringStatManager();
    }
}
