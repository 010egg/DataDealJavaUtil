// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole;

import java.util.Iterator;
import javax.swing.SwingWorker;
import java.util.LinkedHashMap;
import javax.swing.JPanel;
import java.util.Map;
import com.sun.tools.jconsole.JConsolePlugin;

public class DruidPlugin extends JConsolePlugin
{
    private final Map<String, JPanel> tabs;
    
    public DruidPlugin() {
        (this.tabs = new LinkedHashMap<String, JPanel>()).put("Druid-Driver", new DruidDriverPanel());
        this.tabs.put("Druid-DataSource", new DruidDataSourcePanel());
        this.tabs.put("Druid-SQL", new DruidSQLPanel());
    }
    
    @Override
    public Map<String, JPanel> getTabs() {
        return this.tabs;
    }
    
    @Override
    public SwingWorker<?, ?> newSwingWorker() {
        final SwingWorker<?, ?> worer = new SwingWorker<Object, Object>() {
            @Override
            protected Object doInBackground() throws Exception {
                return DruidPlugin.this.doInBackground();
            }
        };
        return worer;
    }
    
    protected Object doInBackground() throws Exception {
        for (final JPanel panel : this.tabs.values()) {
            ((DruidPanel)panel).doInBackground(this.getContext());
        }
        return null;
    }
}
