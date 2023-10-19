// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole;

import java.awt.GridLayout;
import java.util.Iterator;
import javax.swing.table.TableCellRenderer;
import com.alibaba.druid.support.jconsole.model.DruidTableCellRenderer;
import java.awt.Component;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.table.TableModel;
import com.alibaba.druid.support.jconsole.model.DruidTableModel;
import javax.swing.JTable;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.alibaba.druid.support.jconsole.util.TableDataProcessor;
import javax.swing.JPanel;

public class DruidDataSourcePanel extends DruidPanel
{
    private static final long serialVersionUID = 1L;
    private static final String REQUEST_URL = "/datasource.json";
    private static final String KEY_WORD_IDENTITY = "Identity";
    private JPanel contentPanel;
    
    public DruidDataSourcePanel() {
        this.url = "/datasource.json";
    }
    
    private void addTable(final TableDataProcessor.ColumnData columnData) {
        final ArrayList<ArrayList<LinkedHashMap<String, Object>>> data = columnData.getTableData();
        int i = 0;
        final ArrayList<String> ids = columnData.getNames();
        for (final ArrayList<LinkedHashMap<String, Object>> listNow : data) {
            final JTable table = new JTable();
            table.setModel(this.tableModel = new DruidTableModel(listNow));
            final String id = ids.get(i);
            final JPanel panelNow = new JPanel(new BorderLayout());
            panelNow.setBorder(BorderFactory.createTitledBorder("Identity:" + id));
            this.contentPanel.add(panelNow);
            panelNow.add(table.getTableHeader(), "North");
            panelNow.add(table);
            table.getColumnModel().getColumn(0).setCellRenderer(new DruidTableCellRenderer());
            ++i;
        }
    }
    
    @Override
    protected void tableDataProcess(final ArrayList<LinkedHashMap<String, Object>> data) {
        final TableDataProcessor.ColumnData columnData = TableDataProcessor.multiRow2Col(data, "Identity");
        this.contentPanel = new JPanel(new GridLayout(0, 1));
        this.addTable(columnData);
        this.scrollPane.setViewportView(this.contentPanel);
    }
}
