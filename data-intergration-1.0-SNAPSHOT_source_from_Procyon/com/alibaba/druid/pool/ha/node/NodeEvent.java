// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.node;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import com.alibaba.druid.pool.ha.PropertiesUtils;
import java.util.List;
import java.util.Properties;

public class NodeEvent
{
    private NodeEventTypeEnum type;
    private String nodeName;
    private String url;
    private String username;
    private String password;
    
    public static List<NodeEvent> getEventsByDiffProperties(final Properties previous, final Properties next) {
        final List<String> prevNames = PropertiesUtils.loadNameList(previous, "");
        final List<String> nextNames = PropertiesUtils.loadNameList(next, "");
        final List<String> namesToAdd = new ArrayList<String>();
        final List<String> namesToDel = new ArrayList<String>();
        for (final String n : prevNames) {
            if (n != null && !n.trim().isEmpty() && !nextNames.contains(n)) {
                namesToDel.add(n);
            }
        }
        for (final String n : nextNames) {
            if (n != null && !n.trim().isEmpty() && !prevNames.contains(n)) {
                namesToAdd.add(n);
            }
        }
        final List<NodeEvent> list = new ArrayList<NodeEvent>();
        list.addAll(generateEvents(next, namesToAdd, NodeEventTypeEnum.ADD));
        list.addAll(generateEvents(previous, namesToDel, NodeEventTypeEnum.DELETE));
        return list;
    }
    
    public static List<NodeEvent> generateEvents(final Properties properties, final List<String> names, final NodeEventTypeEnum type) {
        final List<NodeEvent> list = new ArrayList<NodeEvent>();
        for (final String n : names) {
            final NodeEvent event = new NodeEvent();
            event.setType(type);
            event.setNodeName(n);
            event.setUrl(properties.getProperty(n + ".url"));
            event.setUsername(properties.getProperty(n + ".username"));
            event.setPassword(properties.getProperty(n + ".password"));
            list.add(event);
        }
        return list;
    }
    
    @Override
    public String toString() {
        String str = "NodeEvent{type=" + this.type + ", nodeName='" + this.nodeName + '\'' + ", url='" + this.url + '\'' + ", username='" + this.username + '\'';
        if (this.password != null) {
            str = str + ", password.length=" + this.password.length();
        }
        str += '}';
        return str;
    }
    
    public NodeEventTypeEnum getType() {
        return this.type;
    }
    
    public void setType(final NodeEventTypeEnum type) {
        this.type = type;
    }
    
    public String getNodeName() {
        return this.nodeName;
    }
    
    public void setNodeName(final String nodeName) {
        this.nodeName = nodeName;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
}
