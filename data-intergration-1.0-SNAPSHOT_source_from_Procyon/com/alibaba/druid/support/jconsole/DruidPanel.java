// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole;

import com.alibaba.druid.support.logging.LogFactory;
import com.sun.tools.jconsole.JConsoleContext;
import com.alibaba.druid.support.jconsole.util.TableDataProcessor;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.alibaba.druid.support.logging.Log;
import javax.management.MBeanServerConnection;
import javax.swing.JTable;
import com.alibaba.druid.support.jconsole.model.DruidTableModel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

public abstract class DruidPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    protected static final long DEFAULT_ACTIVE_TIME = 300000L;
    private static final String COPYRIGHT_STRING = "<html>powered by <a href=\"http://blog.csdn.net/yunnysunny\">yunnysunny</a></html>";
    protected JScrollPane scrollPane;
    protected DruidTableModel tableModel;
    protected JTable table;
    protected JPanel copyrightPanel;
    protected String url;
    protected long activeTime;
    protected long lastRefreshTime;
    protected MBeanServerConnection conn;
    private static final Log LOG;
    
    protected DruidPanel(final long activeTime) {
        this.activeTime = activeTime;
    }
    
    protected DruidPanel() {
        this.activeTime = 300000L;
    }
    
    protected abstract void tableDataProcess(final ArrayList<LinkedHashMap<String, Object>> p0);
    
    protected void addOrRefreshTable(final String url) throws Exception {
        if (url != null) {
            boolean needRefresh = false;
            final long timeNow = new Date().getTime();
            if (this.scrollPane == null) {
                this.table = new JTable();
                (this.scrollPane = new JScrollPane()).setAutoscrolls(true);
                this.scrollPane.setBorder(BorderFactory.createTitledBorder("\u6570\u636e\u533a"));
                this.setLayout(null);
                this.scrollPane.setBounds(10, 10, this.getWidth() - 20, this.getHeight() - 80);
                this.add(this.scrollPane);
                (this.copyrightPanel = new JPanel()).setBorder(BorderFactory.createTitledBorder("\u7248\u6743\u533a"));
                final JLabel authorInfo = new JLabel("<html>powered by <a href=\"http://blog.csdn.net/yunnysunny\">yunnysunny</a></html>");
                this.copyrightPanel.add(authorInfo);
                this.add(this.copyrightPanel);
                this.copyrightPanel.setBounds(10, this.getHeight() - 60, this.getWidth() - 20, 60);
                needRefresh = true;
                this.lastRefreshTime = timeNow;
                this.addComponentListener(new ComponentListener() {
                    @Override
                    public void componentShown(final ComponentEvent arg0) {
                    }
                    
                    @Override
                    public void componentResized(final ComponentEvent arg0) {
                        DruidPanel.this.scrollPane.setBounds(10, 10, DruidPanel.this.getWidth() - 20, DruidPanel.this.getHeight() - 80);
                        DruidPanel.this.copyrightPanel.setBounds(10, DruidPanel.this.getHeight() - 60, DruidPanel.this.getWidth() - 20, 60);
                    }
                    
                    @Override
                    public void componentMoved(final ComponentEvent arg0) {
                    }
                    
                    @Override
                    public void componentHidden(final ComponentEvent arg0) {
                    }
                });
            }
            else if (this.lastRefreshTime + this.activeTime < timeNow) {
                needRefresh = true;
                this.lastRefreshTime = timeNow;
            }
            if (needRefresh) {
                DruidPanel.LOG.debug("refresh" + timeNow);
                final ArrayList<LinkedHashMap<String, Object>> data = TableDataProcessor.parseData(TableDataProcessor.getData(url, this.conn));
                if (data != null) {
                    this.tableDataProcess(data);
                }
            }
        }
        else {
            DruidPanel.LOG.warn("url\u4e0d\u5b58\u5728");
        }
    }
    
    protected Object doInBackground(final JConsoleContext context) throws Exception {
        this.doInBackground(context.getMBeanServerConnection());
        return null;
    }
    
    protected void doInBackground(final MBeanServerConnection conn) {
        if (conn == null) {
            DruidPanel.LOG.warn("MBeanServerConnection is null");
            return;
        }
        try {
            this.conn = conn;
            this.addOrRefreshTable(this.url);
        }
        catch (Exception e) {
            DruidPanel.LOG.warn("", e);
        }
    }
    
    static {
        LOG = LogFactory.getLog(DruidPanel.class);
    }
}
