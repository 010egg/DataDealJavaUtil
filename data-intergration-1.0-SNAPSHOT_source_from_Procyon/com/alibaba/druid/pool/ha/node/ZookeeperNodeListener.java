// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.node;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.pool.ha.PropertiesUtils;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import org.apache.curator.framework.recipes.cache.ChildData;
import java.util.Map;
import java.util.ArrayList;
import com.alibaba.druid.DruidRuntimeException;
import com.alibaba.druid.util.StringUtils;
import java.util.Properties;
import java.util.List;
import java.io.IOException;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.framework.CuratorFrameworkFactory;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import java.util.concurrent.locks.Lock;
import com.alibaba.druid.support.logging.Log;

public class ZookeeperNodeListener extends NodeListener
{
    private static final Log LOG;
    private String zkConnectString;
    private String path;
    private Lock lock;
    private boolean privateZkClient;
    private PathChildrenCache cache;
    private CuratorFramework client;
    private String urlTemplate;
    
    public ZookeeperNodeListener() {
        this.path = "/ha-druid-datasources";
        this.lock = new ReentrantLock();
        this.privateZkClient = false;
    }
    
    @Override
    public void init() {
        this.checkParameters();
        super.init();
        if (this.client == null) {
            (this.client = CuratorFrameworkFactory.builder().canBeReadOnly(true).connectionTimeoutMs(5000).connectString(this.zkConnectString).retryPolicy((RetryPolicy)new RetryForever(10000)).sessionTimeoutMs(30000).build()).start();
            this.privateZkClient = true;
        }
        this.cache = new PathChildrenCache(this.client, this.path, true);
        this.cache.getListenable().addListener((Object)new PathChildrenCacheListener() {
            public void childEvent(final CuratorFramework client, final PathChildrenCacheEvent event) throws Exception {
                try {
                    ZookeeperNodeListener.LOG.info("Receive an event: " + event.getType());
                    ZookeeperNodeListener.this.lock.lock();
                    final PathChildrenCacheEvent.Type eventType = event.getType();
                    switch (eventType) {
                        case CHILD_REMOVED: {
                            ZookeeperNodeListener.this.updateSingleNode(event, NodeEventTypeEnum.DELETE);
                            break;
                        }
                        case CHILD_ADDED: {
                            ZookeeperNodeListener.this.updateSingleNode(event, NodeEventTypeEnum.ADD);
                            break;
                        }
                        case CONNECTION_RECONNECTED: {
                            ZookeeperNodeListener.this.refreshAllNodes();
                            break;
                        }
                        default: {
                            ZookeeperNodeListener.LOG.info("Received a PathChildrenCacheEvent, IGNORE it: " + event);
                            break;
                        }
                    }
                }
                finally {
                    ZookeeperNodeListener.this.lock.unlock();
                    ZookeeperNodeListener.LOG.info("Finish the processing of event: " + event.getType());
                }
            }
        });
        try {
            this.cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        }
        catch (Exception e) {
            ZookeeperNodeListener.LOG.error("Can't start PathChildrenCache", e);
        }
    }
    
    @Override
    public void destroy() {
        if (this.cache != null) {
            try {
                this.cache.close();
            }
            catch (IOException e) {
                ZookeeperNodeListener.LOG.error("IOException occurred while closing PathChildrenCache.", e);
            }
        }
        if (this.client != null && this.privateZkClient) {
            this.client.close();
        }
    }
    
    @Override
    public List<NodeEvent> refresh() {
        try {
            this.lock.lock();
            final Properties properties = this.getPropertiesFromCache();
            final List<NodeEvent> events = NodeEvent.getEventsByDiffProperties(this.getProperties(), properties);
            if (events != null && !events.isEmpty()) {
                this.setProperties(properties);
            }
            return events;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private void checkParameters() {
        if (this.client == null && StringUtils.isEmpty(this.zkConnectString)) {
            throw new DruidRuntimeException("ZK Client is NULL, Please set the zkConnectString.");
        }
        if (StringUtils.isEmpty(this.path)) {
            throw new DruidRuntimeException("Please set the ZooKeeper node path.");
        }
        if (StringUtils.isEmpty(this.urlTemplate)) {
            throw new DruidRuntimeException("Please set the urlTemplate.");
        }
    }
    
    private void updateSingleNode(final PathChildrenCacheEvent event, final NodeEventTypeEnum type) {
        final ChildData data = event.getData();
        final String nodeName = this.getNodeName(data);
        final List<String> names = new ArrayList<String>();
        names.add(this.getPrefix() + "." + nodeName);
        final Properties properties = this.getPropertiesFromChildData(data);
        final List<NodeEvent> events = NodeEvent.generateEvents(properties, names, type);
        if (events.isEmpty()) {
            return;
        }
        if (type == NodeEventTypeEnum.ADD) {
            this.getProperties().putAll(properties);
        }
        else {
            for (final String n : properties.stringPropertyNames()) {
                this.getProperties().remove(n);
            }
        }
        super.update(events);
    }
    
    private void refreshAllNodes() {
        try {
            if (this.client.checkExists().forPath(this.path) == null) {
                ZookeeperNodeListener.LOG.warn("PATH[" + this.path + "] is NOT existed, can NOT refresh nodes.");
                return;
            }
            this.cache.rebuild();
            final Properties properties = this.getPropertiesFromCache();
            final List<NodeEvent> events = NodeEvent.getEventsByDiffProperties(this.getProperties(), properties);
            if (events != null && !events.isEmpty()) {
                this.setProperties(properties);
                super.update(events);
            }
        }
        catch (Exception e) {
            ZookeeperNodeListener.LOG.error("Can NOT refresh Cache Nodes.", e);
        }
    }
    
    private Properties getPropertiesFromCache() {
        final List<ChildData> data = (List<ChildData>)this.cache.getCurrentData();
        final Properties properties = new Properties();
        for (final ChildData c : data) {
            properties.putAll(this.getPropertiesFromChildData(c));
        }
        return properties;
    }
    
    private Properties getPropertiesFromChildData(final ChildData data) {
        final String dataPrefix = this.getPrefix();
        final Properties properties = new Properties();
        if (data == null) {
            return properties;
        }
        final String nodeName = this.getNodeName(data);
        final String str = new String(data.getData());
        final Properties full = new Properties();
        try {
            full.load(new StringReader(str));
        }
        catch (IOException e) {
            ZookeeperNodeListener.LOG.error("Can't load Properties from String. " + str, e);
        }
        final Properties filtered = PropertiesUtils.filterPrefix(full, dataPrefix);
        for (final String n : filtered.stringPropertyNames()) {
            properties.setProperty(n.replaceFirst(dataPrefix, dataPrefix + "\\." + nodeName), filtered.getProperty(n));
        }
        if (!properties.containsKey(dataPrefix + "." + nodeName + ".url")) {
            properties.setProperty(dataPrefix + "." + nodeName + ".url", this.formatUrl(filtered));
        }
        return properties;
    }
    
    private String formatUrl(final Properties properties) {
        String url = this.urlTemplate;
        final String dataPrefix = this.getPrefix();
        if (properties.containsKey(dataPrefix + ".host")) {
            url = url.replace("${host}", properties.getProperty(dataPrefix + ".host"));
            url = url.replace("#{host}", properties.getProperty(dataPrefix + ".host"));
            url = url.replace("#host#", properties.getProperty(dataPrefix + ".host"));
        }
        if (properties.containsKey(dataPrefix + ".port")) {
            url = url.replace("${port}", properties.getProperty(dataPrefix + ".port"));
            url = url.replace("#{port}", properties.getProperty(dataPrefix + ".port"));
            url = url.replace("#port#", properties.getProperty(dataPrefix + ".port"));
        }
        if (properties.containsKey(dataPrefix + ".database")) {
            url = url.replace("${database}", properties.getProperty(dataPrefix + ".database"));
            url = url.replace("#{database}", properties.getProperty(dataPrefix + ".database"));
            url = url.replace("#database#", properties.getProperty(dataPrefix + ".database"));
        }
        return url;
    }
    
    private String getNodeName(final ChildData data) {
        final String eventZkPath = data.getPath();
        if (eventZkPath.startsWith(this.path + "/")) {
            return eventZkPath.substring(this.path.length() + 1);
        }
        return eventZkPath;
    }
    
    public void setClient(final CuratorFramework client) {
        if (client != null) {
            this.client = client;
            this.privateZkClient = false;
        }
    }
    
    public CuratorFramework getClient() {
        return this.client;
    }
    
    public String getZkConnectString() {
        return this.zkConnectString;
    }
    
    public void setZkConnectString(final String zkConnectString) {
        this.zkConnectString = zkConnectString;
    }
    
    public String getPath() {
        return this.path;
    }
    
    public void setPath(final String path) {
        this.path = path;
    }
    
    public String getUrlTemplate() {
        return this.urlTemplate;
    }
    
    public void setUrlTemplate(final String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }
    
    static {
        LOG = LogFactory.getLog(ZookeeperNodeListener.class);
    }
}
