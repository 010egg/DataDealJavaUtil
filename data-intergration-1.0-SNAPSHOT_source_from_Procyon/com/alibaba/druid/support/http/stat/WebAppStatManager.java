// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.stat;

import java.util.Properties;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import java.util.Iterator;
import com.alibaba.druid.util.StringUtils;
import java.util.Set;

public class WebAppStatManager
{
    public static final String SYS_PROP_INSTANCES = "druid.web.webAppStat";
    private static final WebAppStatManager instance;
    private Set<Object> webAppStatSet;
    
    public WebAppStatManager() {
        this.webAppStatSet = null;
    }
    
    public static WebAppStatManager getInstance() {
        return WebAppStatManager.instance;
    }
    
    public synchronized WebAppStat getWebAppStat(final String contextPath) {
        final Set<Object> stats = this.getWebAppStatSet();
        for (final Object item : stats) {
            if (item instanceof WebAppStat) {
                final WebAppStat stat = (WebAppStat)item;
                if (StringUtils.equals(stat.getContextPath(), contextPath)) {
                    return stat;
                }
                continue;
            }
        }
        final WebAppStat stat2 = new WebAppStat(contextPath);
        this.addWebAppStatSet(stat2);
        return stat2;
    }
    
    public Set<Object> getWebAppStatSet() {
        if (this.webAppStatSet == null) {
            if (DruidDataSourceStatManager.isRegisterToSystemProperty()) {
                this.webAppStatSet = getWebAppStatSet0();
            }
            else {
                this.webAppStatSet = new CopyOnWriteArraySet<Object>();
            }
        }
        return this.webAppStatSet;
    }
    
    public List<Map<String, Object>> getWebAppStatData() {
        final Set<Object> stats = this.getWebAppStatSet();
        final List<Map<String, Object>> statDataList = new ArrayList<Map<String, Object>>(stats.size());
        for (final Object stat : stats) {
            final Map<String, Object> statData = WebAppStatUtils.getStatData(stat);
            statDataList.add(statData);
        }
        return statDataList;
    }
    
    public List<Map<String, Object>> getURIStatData() {
        final Set<Object> stats = this.getWebAppStatSet();
        final List<Map<String, Object>> allAppUriStatDataList = new ArrayList<Map<String, Object>>();
        for (final Object stat : stats) {
            final List<Map<String, Object>> uriStatDataList = WebAppStatUtils.getURIStatDataList(stat);
            allAppUriStatDataList.addAll(uriStatDataList);
        }
        return allAppUriStatDataList;
    }
    
    public List<Map<String, Object>> getSessionStatData() {
        final Set<Object> stats = this.getWebAppStatSet();
        final List<Map<String, Object>> allAppUriStatDataList = new ArrayList<Map<String, Object>>();
        for (final Object stat : stats) {
            final List<Map<String, Object>> uriStatDataList = WebAppStatUtils.getSessionStatDataList(stat);
            allAppUriStatDataList.addAll(uriStatDataList);
        }
        return allAppUriStatDataList;
    }
    
    public Map<String, Object> getSessionStat(final String sessionId) {
        final Set<Object> stats = this.getWebAppStatSet();
        for (final Object stat : stats) {
            final Map<String, Object> statData = WebAppStatUtils.getSessionStatData(stat, sessionId);
            if (statData != null) {
                return statData;
            }
        }
        return null;
    }
    
    public Map<String, Object> getURIStatData(final String uri) {
        final Set<Object> stats = this.getWebAppStatSet();
        for (final Object stat : stats) {
            final Map<String, Object> statData = WebAppStatUtils.getURIStatData(stat, uri);
            if (statData != null) {
                return statData;
            }
        }
        return null;
    }
    
    public void addWebAppStatSet(final Object webAppStat) {
        this.getWebAppStatSet().add(webAppStat);
    }
    
    public boolean remove(final Object webAppStat) {
        return this.getWebAppStatSet().remove(webAppStat);
    }
    
    static Set<Object> getWebAppStatSet0() {
        final Properties properties = System.getProperties();
        Set<Object> webAppStats = (Set<Object>)properties.get("druid.web.webAppStat");
        if (webAppStats == null) {
            synchronized (properties) {
                webAppStats = (Set<Object>)properties.get("druid.web.webAppStat");
                if (webAppStats == null) {
                    webAppStats = new CopyOnWriteArraySet<Object>();
                    properties.put("druid.web.webAppStat", webAppStats);
                }
            }
        }
        return webAppStats;
    }
    
    public void resetStat() {
        final Set<Object> stats = this.getWebAppStatSet();
        for (final Object stat : stats) {
            WebAppStatUtils.reset(stat);
        }
    }
    
    static {
        instance = new WebAppStatManager();
    }
}
