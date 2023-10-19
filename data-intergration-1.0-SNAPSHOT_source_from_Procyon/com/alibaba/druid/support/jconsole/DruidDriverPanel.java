// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole;

import java.awt.Component;
import javax.swing.table.TableCellRenderer;
import com.alibaba.druid.support.jconsole.model.DruidTableCellRenderer;
import javax.swing.table.TableModel;
import com.alibaba.druid.support.jconsole.model.DruidTableModel;
import com.alibaba.druid.support.jconsole.util.TableDataProcessor;
import java.util.LinkedHashMap;
import java.util.ArrayList;

public class DruidDriverPanel extends DruidPanel
{
    private static final long serialVersionUID = 1L;
    private static final String REQUEST_URL = "/basic.json";
    
    public DruidDriverPanel() {
        this.url = "/basic.json";
    }
    
    @Override
    protected void tableDataProcess(final ArrayList<LinkedHashMap<String, Object>> data) {
        final TableDataProcessor.ColumnData columnData = TableDataProcessor.row2col(data);
        this.tableModel = new DruidTableModel(columnData.getData());
        this.table.setModel(this.tableModel);
        this.table.getColumnModel().getColumn(0).setCellRenderer(new DruidTableCellRenderer());
        this.scrollPane.setViewportView(this.table);
    }
}
