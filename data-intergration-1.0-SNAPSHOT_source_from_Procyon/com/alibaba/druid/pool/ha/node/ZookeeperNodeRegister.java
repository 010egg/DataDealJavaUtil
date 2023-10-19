// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.node;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.Iterator;
import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.util.Properties;
import java.util.List;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.framework.CuratorFrameworkFactory;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import org.apache.curator.framework.recipes.nodes.GroupMember;
import org.apache.curator.framework.CuratorFramework;
import com.alibaba.druid.support.logging.Log;

public class ZookeeperNodeRegister
{
    private static final Log LOG;
    private String zkConnectString;
    private String path;
    private CuratorFramework client;
    private GroupMember member;
    private boolean privateZkClient;
    private Lock lock;
    
    public ZookeeperNodeRegister() {
        this.path = "/ha-druid-datasources";
        this.privateZkClient = false;
        this.lock = new ReentrantLock();
    }
    
    public void init() {
        if (this.client == null) {
            (this.client = CuratorFrameworkFactory.builder().connectionTimeoutMs(5000).connectString(this.zkConnectString).retryPolicy((RetryPolicy)new RetryForever(10000)).sessionTimeoutMs(30000).build()).start();
            this.privateZkClient = true;
        }
    }
    
    public boolean register(final String nodeId, final List<ZookeeperNodeInfo> payload) {
        if (payload == null || payload.isEmpty()) {
            return false;
        }
        try {
            this.lock.lock();
            this.createPathIfNotExisted();
            if (this.member != null) {
                ZookeeperNodeRegister.LOG.warn("GroupMember has already registered. Please deregister first.");
                return false;
            }
            final String payloadString = this.getPropertiesString(payload);
            (this.member = new GroupMember(this.client, this.path, nodeId, payloadString.getBytes())).start();
            ZookeeperNodeRegister.LOG.info("Register Node[" + nodeId + "] in path[" + this.path + "].");
            return true;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void deregister() {
        if (this.member != null) {
            this.member.close();
            this.member = null;
        }
        if (this.client != null && this.privateZkClient) {
            this.client.close();
        }
    }
    
    public void destroy() {
        this.deregister();
    }
    
    private void createPathIfNotExisted() {
        try {
            if (this.client.checkExists().forPath(this.path) == null) {
                ZookeeperNodeRegister.LOG.info("Path[" + this.path + "] is NOT existed, create it.");
                this.client.create().creatingParentsIfNeeded().forPath(this.path);
            }
        }
        catch (Exception e) {
            ZookeeperNodeRegister.LOG.error("Can NOT check the path.", e);
        }
    }
    
    private String getPropertiesString(final List<ZookeeperNodeInfo> payload) {
        final Properties properties = new Properties();
        for (final ZookeeperNodeInfo n : payload) {
            if (n.getHost() != null) {
                properties.setProperty(n.getPrefix() + "host", n.getHost());
            }
            if (n.getPort() != null) {
                properties.setProperty(n.getPrefix() + "port", n.getPort().toString());
            }
            if (n.getDatabase() != null) {
                properties.setProperty(n.getPrefix() + "database", n.getDatabase());
            }
            if (n.getUsername() != null) {
                properties.setProperty(n.getPrefix() + "username", n.getUsername());
            }
            if (n.getPassword() != null) {
                properties.setProperty(n.getPrefix() + "password", n.getPassword());
            }
        }
        final StringWriter sw = new StringWriter();
        try {
            properties.store(sw, "");
        }
        catch (IOException e) {
            ZookeeperNodeRegister.LOG.error("Why Properties.store goes wrong?", e);
        }
        return sw.toString();
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
    
    static {
        LOG = LogFactory.getLog(ZookeeperNodeRegister.class);
    }
}
