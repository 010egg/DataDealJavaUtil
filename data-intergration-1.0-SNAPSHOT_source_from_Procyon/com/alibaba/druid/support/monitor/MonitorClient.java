// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.monitor;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.InetAddress;
import com.alibaba.druid.stat.JdbcSqlStatValue;
import java.util.Map;
import com.alibaba.druid.support.http.stat.WebAppStat;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.druid.support.http.stat.WebAppStatValue;
import com.alibaba.druid.support.http.stat.WebURIStatValue;
import java.util.Collection;
import com.alibaba.druid.support.spring.stat.SpringStat;
import com.alibaba.druid.support.spring.stat.SpringStatManager;
import com.alibaba.druid.support.spring.stat.SpringMethodStatValue;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallProviderStatValue;
import com.alibaba.druid.pool.DruidDataSourceStatValue;
import java.util.ArrayList;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.util.Utils;
import java.util.Properties;
import java.lang.management.ManagementFactory;
import com.alibaba.druid.support.monitor.dao.MonitorDao;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import com.alibaba.druid.support.logging.Log;

public class MonitorClient
{
    private static final Log LOG;
    private static final long DEFAULT_TIME_BETWEEN_COLLECT = 300L;
    private ScheduledExecutorService scheduler;
    private int schedulerThreadSize;
    private long timeBetweenSqlCollect;
    private long timeBetweenSpringCollect;
    private long timeBetweenWebUriCollect;
    private TimeUnit timeUnit;
    private boolean collectSqlEnable;
    private boolean collectSqlWallEnable;
    private boolean collectSpringMethodEnable;
    private boolean collectWebAppEnable;
    private boolean collectWebURIEnable;
    private MonitorDao dao;
    private String domain;
    private String app;
    private String cluster;
    private String host;
    private String ip;
    private int pid;
    
    public MonitorClient() {
        this.schedulerThreadSize = 1;
        this.timeBetweenSqlCollect = 300L;
        this.timeBetweenSpringCollect = 300L;
        this.timeBetweenWebUriCollect = 300L;
        this.timeUnit = TimeUnit.SECONDS;
        this.collectSqlEnable = true;
        this.collectSqlWallEnable = true;
        this.collectSpringMethodEnable = true;
        this.collectWebAppEnable = true;
        this.collectWebURIEnable = true;
        final String name = ManagementFactory.getRuntimeMXBean().getName();
        final String[] items = name.split("@");
        this.pid = Integer.parseInt(items[0]);
        this.host = items[1];
        this.ip = getLocalIPAddress().getHostAddress();
        this.configFromProperty(System.getProperties());
    }
    
    public void configFromProperty(final Properties properties) {
        Integer value = Utils.getInteger(properties, "druid.monitor.client.schedulerThreadSize");
        if (value != null) {
            this.setSchedulerThreadSize(value);
        }
        value = Utils.getInteger(properties, "druid.monitor.client.timeBetweenSqlCollect");
        if (value != null) {
            this.setTimeBetweenSqlCollect(value);
        }
        value = Utils.getInteger(properties, "druid.monitor.client.timeBetweenSpringCollect");
        if (value != null) {
            this.setTimeBetweenSpringCollect(value);
        }
        value = Utils.getInteger(properties, "druid.monitor.client.timeBetweenWebUriCollect");
        if (value != null) {
            this.setTimeBetweenWebUriCollect(value);
        }
        Boolean value2 = Utils.getBoolean(properties, "druid.monitor.client.collectSqlEnable");
        if (value2 != null) {
            this.setCollectSqlEnable(value2);
        }
        value2 = Utils.getBoolean(properties, "druid.monitor.client.collectSqlWallEnable");
        if (value2 != null) {
            this.setCollectSqlWallEnable(value2);
        }
        value2 = Utils.getBoolean(properties, "druid.monitor.client.collectSpringMethodEnable");
        if (value2 != null) {
            this.setCollectSpringMethodEnable(value2);
        }
        value2 = Utils.getBoolean(properties, "druid.monitor.client.collectWebAppEnable");
        if (value2 != null) {
            this.setCollectWebAppEnable(value2);
        }
        value2 = Utils.getBoolean(properties, "druid.monitor.client.collectWebURIEnable");
        if (value2 != null) {
            this.setCollectWebURIEnable(value2);
        }
        this.domain = properties.getProperty("druid.monitor.domain");
        if (StringUtils.isEmpty(this.domain)) {
            this.domain = "default";
        }
        this.app = properties.getProperty("druid.monitor.app");
        if (StringUtils.isEmpty(this.app)) {
            this.app = "default";
        }
        this.cluster = properties.getProperty("druid.monitor.cluster");
        if (StringUtils.isEmpty(this.cluster)) {
            this.cluster = "default";
        }
    }
    
    public void stop() {
    }
    
    public void start() {
        this.checkInst();
        if (this.scheduler == null) {
            this.scheduler = new ScheduledThreadPoolExecutor(this.schedulerThreadSize);
        }
        this.scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                MonitorClient.this.collectSql();
            }
        }, this.timeBetweenSqlCollect, this.timeBetweenSqlCollect, this.timeUnit);
        this.scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                MonitorClient.this.collectSpringMethod();
            }
        }, this.timeBetweenSpringCollect, this.timeBetweenSpringCollect, this.timeUnit);
        this.scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                MonitorClient.this.collectWebURI();
            }
        }, this.timeBetweenWebUriCollect, this.timeBetweenWebUriCollect, this.timeUnit);
    }
    
    public ScheduledExecutorService getScheduler() {
        return this.scheduler;
    }
    
    public void setScheduler(final ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }
    
    public void checkInst() {
        try {
            this.dao.insertAppIfNotExits(this.domain, this.app);
            this.dao.insertClusterIfNotExits(this.domain, this.app, this.cluster);
            this.dao.insertOrUpdateInstance(this.domain, this.app, this.cluster, this.host, this.ip, Utils.getStartTime(), this.pid);
        }
        catch (Exception ex) {
            MonitorClient.LOG.error("checkInst error", ex);
        }
    }
    
    public void collectSql() {
        if (!this.collectSqlEnable && !this.collectSqlWallEnable) {
            return;
        }
        final Set<Object> dataSources = DruidDataSourceStatManager.getInstances().keySet();
        final List<DruidDataSourceStatValue> statValueList = new ArrayList<DruidDataSourceStatValue>(dataSources.size());
        final List<WallProviderStatValue> wallStatValueList = new ArrayList<WallProviderStatValue>();
        for (final Object item : dataSources) {
            if (!(item instanceof DruidDataSource)) {
                continue;
            }
            final DruidDataSource dataSource = (DruidDataSource)item;
            if (this.collectSqlEnable) {
                final DruidDataSourceStatValue statValue = dataSource.getStatValueAndReset();
                statValueList.add(statValue);
            }
            if (!this.collectSqlWallEnable) {
                continue;
            }
            final WallProviderStatValue wallStatValue = dataSource.getWallStatValue(true);
            if (wallStatValue == null || wallStatValue.getCheckCount() <= 0L) {
                continue;
            }
            wallStatValueList.add(wallStatValue);
        }
        final MonitorContext ctx = this.createContext();
        if (statValueList.size() > 0) {
            this.dao.saveSql(ctx, statValueList);
        }
        if (wallStatValueList.size() > 0) {
            this.dao.saveSqlWall(ctx, wallStatValueList);
        }
    }
    
    private MonitorContext createContext() {
        final MonitorContext ctx = new MonitorContext();
        ctx.setDomain(this.domain);
        ctx.setApp(this.app);
        ctx.setCluster(this.cluster);
        ctx.setCollectTime(new Date());
        ctx.setPID(this.pid);
        ctx.setHost(this.host);
        ctx.setCollectTime(Utils.getStartTime());
        return ctx;
    }
    
    private void collectSpringMethod() {
        if (!this.collectSpringMethodEnable) {
            return;
        }
        final List<SpringMethodStatValue> statValueList = new ArrayList<SpringMethodStatValue>();
        final Set<Object> stats = SpringStatManager.getInstance().getSpringStatSet();
        for (final Object item : stats) {
            if (!(item instanceof SpringStat)) {
                continue;
            }
            final SpringStat sprintStat = (SpringStat)item;
            statValueList.addAll(sprintStat.getStatList(true));
        }
        if (statValueList.size() > 0) {
            final MonitorContext ctx = this.createContext();
            this.dao.saveSpringMethod(ctx, statValueList);
        }
    }
    
    private void collectWebURI() {
        if (!this.collectWebAppEnable && !this.collectWebURIEnable) {
            return;
        }
        final List<WebURIStatValue> webURIValueList = new ArrayList<WebURIStatValue>();
        final List<WebAppStatValue> webAppStatValueList = new ArrayList<WebAppStatValue>();
        final Set<Object> stats = WebAppStatManager.getInstance().getWebAppStatSet();
        for (final Object item : stats) {
            if (!(item instanceof WebAppStat)) {
                continue;
            }
            final WebAppStat webAppStat = (WebAppStat)item;
            if (this.collectWebAppEnable) {
                final WebAppStatValue webAppStatValue = webAppStat.getStatValue(true);
                webAppStatValueList.add(webAppStatValue);
            }
            if (!this.collectWebURIEnable) {
                continue;
            }
            webURIValueList.addAll(webAppStat.getURIStatValueList(true));
        }
        final MonitorContext ctx = this.createContext();
        if (webURIValueList.size() > 0) {
            this.dao.saveWebURI(ctx, webURIValueList);
        }
        if (webAppStatValueList.size() > 0) {
            this.dao.saveWebApp(ctx, webAppStatValueList);
        }
    }
    
    public List<JdbcSqlStatValue> loadSqlList(final Map<String, Object> filters) {
        return this.dao.loadSqlList(filters);
    }
    
    public MonitorDao getDao() {
        return this.dao;
    }
    
    public void setDao(final MonitorDao dao) {
        this.dao = dao;
    }
    
    public long getTimeBetweenSqlCollect() {
        return this.timeBetweenSqlCollect;
    }
    
    public void setTimeBetweenSqlCollect(final long timeBetweenSqlCollect) {
        this.timeBetweenSqlCollect = timeBetweenSqlCollect;
    }
    
    public long getTimeBetweenSpringCollect() {
        return this.timeBetweenSpringCollect;
    }
    
    public void setTimeBetweenSpringCollect(final long timeBetweenSpringCollect) {
        this.timeBetweenSpringCollect = timeBetweenSpringCollect;
    }
    
    public long getTimeBetweenWebUriCollect() {
        return this.timeBetweenWebUriCollect;
    }
    
    public void setTimeBetweenWebUriCollect(final long timeBetweenWebUriCollect) {
        this.timeBetweenWebUriCollect = timeBetweenWebUriCollect;
    }
    
    public TimeUnit getTimeUnit() {
        return this.timeUnit;
    }
    
    public void setTimeUnit(final TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
    
    public boolean isCollectSqlEnable() {
        return this.collectSqlEnable;
    }
    
    public void setCollectSqlEnable(final boolean collectSqlEnable) {
        this.collectSqlEnable = collectSqlEnable;
    }
    
    public boolean isCollectSqlWallEnable() {
        return this.collectSqlWallEnable;
    }
    
    public void setCollectSqlWallEnable(final boolean collectSqlWallEnable) {
        this.collectSqlWallEnable = collectSqlWallEnable;
    }
    
    public boolean isCollectSpringMethodEnable() {
        return this.collectSpringMethodEnable;
    }
    
    public void setCollectSpringMethodEnable(final boolean collectSpringMethodEnable) {
        this.collectSpringMethodEnable = collectSpringMethodEnable;
    }
    
    public boolean isCollectWebAppEnable() {
        return this.collectWebAppEnable;
    }
    
    public void setCollectWebAppEnable(final boolean collectWebAppEnable) {
        this.collectWebAppEnable = collectWebAppEnable;
    }
    
    public boolean isCollectWebURIEnable() {
        return this.collectWebURIEnable;
    }
    
    public void setCollectWebURIEnable(final boolean collectWebURIEnable) {
        this.collectWebURIEnable = collectWebURIEnable;
    }
    
    public int getSchedulerThreadSize() {
        return this.schedulerThreadSize;
    }
    
    public void setSchedulerThreadSize(final int schedulerThreadSize) {
        this.schedulerThreadSize = schedulerThreadSize;
    }
    
    public String getDomain() {
        return this.domain;
    }
    
    public void setDomain(final String domain) {
        this.domain = domain;
    }
    
    public String getApp() {
        return this.app;
    }
    
    public void setApp(final String app) {
        this.app = app;
    }
    
    public String getCluster() {
        return this.cluster;
    }
    
    public void setCluster(final String cluster) {
        this.cluster = cluster;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public void setHost(final String host) {
        this.host = host;
    }
    
    public int getPid() {
        return this.pid;
    }
    
    public void setPid(final int pid) {
        this.pid = pid;
    }
    
    public static InetAddress getLocalIPAddress() {
        try {
            final Enumeration<?> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress inetAddress = null;
            while (netInterfaces.hasMoreElements()) {
                final NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
                final Enumeration<?> e2 = ni.getInetAddresses();
                while (e2.hasMoreElements()) {
                    inetAddress = (InetAddress)e2.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().contains(":")) {
                        return inetAddress;
                    }
                }
            }
        }
        catch (Exception e3) {
            MonitorClient.LOG.error("getLocalIP error", e3);
        }
        return null;
    }
    
    static {
        LOG = LogFactory.getLog(MonitorClient.class);
    }
}
