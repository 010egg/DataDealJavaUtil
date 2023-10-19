// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.node;

import com.alibaba.druid.support.logging.LogFactory;
import java.io.Closeable;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.pool.ha.DataSourceCreator;
import java.util.Iterator;
import java.util.Map;
import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import com.alibaba.druid.pool.ha.HighAvailableDataSource;
import java.util.Set;
import com.alibaba.druid.support.logging.Log;
import java.util.Observer;

public class PoolUpdater implements Observer
{
    public static final int DEFAULT_INTERVAL = 60;
    private static final Log LOG;
    private Set<String> nodesToDel;
    private HighAvailableDataSource highAvailableDataSource;
    private Lock lock;
    private ScheduledExecutorService executor;
    private int intervalSeconds;
    private volatile boolean inited;
    private boolean allowEmptyPool;
    
    public PoolUpdater(final HighAvailableDataSource highAvailableDataSource) {
        this.nodesToDel = new CopyOnWriteArraySet<String>();
        this.lock = new ReentrantLock();
        this.intervalSeconds = 60;
        this.inited = false;
        this.allowEmptyPool = false;
        this.setHighAvailableDataSource(highAvailableDataSource);
    }
    
    public void init() {
        if (this.inited) {
            return;
        }
        synchronized (this) {
            if (this.inited) {
                return;
            }
            if (this.intervalSeconds < 10) {
                PoolUpdater.LOG.warn("CAUTION: Purge interval has been set to " + this.intervalSeconds + ". This value should NOT be too small.");
            }
            if (this.intervalSeconds <= 0) {
                this.intervalSeconds = 60;
            }
            (this.executor = Executors.newScheduledThreadPool(1)).scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    PoolUpdater.LOG.debug("Purging the DataSource Pool every " + PoolUpdater.this.intervalSeconds + "s.");
                    try {
                        PoolUpdater.this.removeDataSources();
                    }
                    catch (Exception e) {
                        PoolUpdater.LOG.error("Exception occurred while removing DataSources.", e);
                    }
                }
            }, this.intervalSeconds, this.intervalSeconds, TimeUnit.SECONDS);
        }
    }
    
    public void destroy() {
        if (this.executor == null || this.executor.isShutdown()) {
            return;
        }
        try {
            this.executor.shutdown();
        }
        catch (Exception e) {
            PoolUpdater.LOG.error("Can NOT shutdown the ScheduledExecutorService.", e);
        }
    }
    
    @Override
    public void update(final Observable o, final Object arg) {
        if (!(o instanceof NodeListener)) {
            return;
        }
        if (arg == null || !(arg instanceof NodeEvent[])) {
            return;
        }
        final NodeEvent[] events = (NodeEvent[])arg;
        if (events.length <= 0) {
            return;
        }
        try {
            PoolUpdater.LOG.info("Waiting for Lock to start processing NodeEvents.");
            this.lock.lock();
            PoolUpdater.LOG.info("Start processing the NodeEvent[" + events.length + "].");
            for (final NodeEvent e : events) {
                if (e.getType() == NodeEventTypeEnum.ADD) {
                    this.addNode(e);
                }
                else if (e.getType() == NodeEventTypeEnum.DELETE) {
                    this.deleteNode(e);
                }
            }
        }
        catch (Exception e2) {
            PoolUpdater.LOG.error("Exception occurred while updating Pool.", e2);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void removeDataSources() {
        if (this.nodesToDel == null || this.nodesToDel.isEmpty()) {
            return;
        }
        try {
            this.lock.lock();
            final Map<String, DataSource> map = this.highAvailableDataSource.getDataSourceMap();
            final Set<String> copySet = new HashSet<String>(this.nodesToDel);
            for (final String nodeName : copySet) {
                PoolUpdater.LOG.info("Start removing Node " + nodeName + ".");
                if (!map.containsKey(nodeName)) {
                    PoolUpdater.LOG.info("Node " + nodeName + " is NOT existed in the map.");
                    this.cancelBlacklistNode(nodeName);
                }
                else {
                    final DataSource ds = map.get(nodeName);
                    if (ds instanceof DruidDataSource) {
                        final DruidDataSource dds = (DruidDataSource)ds;
                        final int activeCount = dds.getActiveCount();
                        if (activeCount > 0) {
                            PoolUpdater.LOG.warn("Node " + nodeName + " is still running [activeCount=" + activeCount + "], try next time.");
                            continue;
                        }
                        PoolUpdater.LOG.info("Close Node " + nodeName + " and remove it.");
                        try {
                            dds.close();
                        }
                        catch (Exception e) {
                            PoolUpdater.LOG.error("Exception occurred while closing Node " + nodeName + ", just remove it.", e);
                        }
                    }
                    map.remove(nodeName);
                    this.cancelBlacklistNode(nodeName);
                }
            }
        }
        catch (Exception e2) {
            PoolUpdater.LOG.error("Exception occurred while removing DataSources.", e2);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    protected void addNode(final NodeEvent event) {
        final String nodeName = event.getNodeName();
        final String url = event.getUrl();
        final String username = event.getUsername();
        final String password = event.getPassword();
        final Map<String, DataSource> map = this.highAvailableDataSource.getDataSourceMap();
        if (nodeName == null || nodeName.isEmpty()) {
            return;
        }
        PoolUpdater.LOG.info("Adding Node " + nodeName + "[url: " + url + ", username: " + username + "].");
        if (map.containsKey(nodeName)) {
            this.cancelBlacklistNode(nodeName);
            return;
        }
        DruidDataSource dataSource = null;
        try {
            dataSource = DataSourceCreator.create(nodeName, url, username, password, this.highAvailableDataSource);
            map.put(nodeName, dataSource);
            PoolUpdater.LOG.info("Creating Node " + nodeName + "[url: " + url + ", username: " + username + "].");
        }
        catch (Exception e) {
            PoolUpdater.LOG.error("Can NOT create DataSource " + nodeName + ". IGNORE IT.", e);
            JdbcUtils.close(dataSource);
        }
    }
    
    protected void deleteNode(final NodeEvent event) {
        final String nodeName = event.getNodeName();
        Map<String, DataSource> map = this.highAvailableDataSource.getDataSourceMap();
        if (nodeName == null || nodeName.isEmpty() || !map.containsKey(nodeName)) {
            return;
        }
        map = this.highAvailableDataSource.getAvailableDataSourceMap();
        if (!this.allowEmptyPool && map.size() == 1 && map.containsKey(nodeName)) {
            PoolUpdater.LOG.warn(nodeName + " is the only DataSource left, don't remove it.");
            return;
        }
        this.blacklistNode(nodeName);
    }
    
    private void cancelBlacklistNode(final String nodeName) {
        PoolUpdater.LOG.info("Cancel the deletion of Node " + nodeName + ".");
        this.nodesToDel.remove(nodeName);
        this.highAvailableDataSource.removeBlackList(nodeName);
    }
    
    private void blacklistNode(final String nodeName) {
        PoolUpdater.LOG.info("Deleting Node " + nodeName + ", just add it into blacklist.");
        this.nodesToDel.add(nodeName);
        this.highAvailableDataSource.addBlackList(nodeName);
    }
    
    public HighAvailableDataSource getHighAvailableDataSource() {
        return this.highAvailableDataSource;
    }
    
    public void setHighAvailableDataSource(final HighAvailableDataSource highAvailableDataSource) {
        this.highAvailableDataSource = highAvailableDataSource;
    }
    
    public Set<String> getNodesToDel() {
        return this.nodesToDel;
    }
    
    public int getIntervalSeconds() {
        return this.intervalSeconds;
    }
    
    public void setIntervalSeconds(final int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }
    
    public boolean isAllowEmptyPool() {
        return this.allowEmptyPool;
    }
    
    public void setAllowEmptyPool(final boolean allowEmptyPool) {
        this.allowEmptyPool = allowEmptyPool;
    }
    
    static {
        LOG = LogFactory.getLog(PoolUpdater.class);
    }
}
