// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole;

import javax.swing.border.Border;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import java.awt.Component;
import com.alibaba.druid.support.jconsole.model.RowHeaderTable;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.table.TableModel;
import com.alibaba.druid.support.jconsole.model.DruidTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.plaf.TableHeaderUI;
import com.alibaba.druid.support.jconsole.model.GroupableTableHeaderUI;
import com.alibaba.druid.support.jconsole.model.GroupableTableHeader;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;
import com.alibaba.druid.support.jconsole.model.ColumnGroup;
import java.util.ArrayList;

public class DruidSQLPanel extends DruidPanel
{
    private static final long serialVersionUID = 1L;
    private static final String REQUEST_URL = "/sql.json";
    private static final ArrayList<String> SHOW_LIST;
    private static final ArrayList<String> REAL_SHOW_LIST;
    private static final String HISTOGRAM = "Histogram";
    private static final String Effected_RowCount_HISOGRAM = "EffectedRowCountHistogram";
    private static final String ExecuteAndResult_Hold_HISOGRAM = "ExecuteAndResultHoldTimeHistogram";
    private static final String FetchRowCount_HISOGRAM = "FetchRowCountHistogram";
    private static final ArrayList<String> ARRAY_DATA_MAP;
    private static final int FIST_LIST_OFFSET = 9;
    private ColumnGroup groupHistogram;
    private ColumnGroup groupEffectedRowCountHistogram;
    private ColumnGroup groupExecuteAndResultHoldTimeHistogram;
    private ColumnGroup groupFetchRowCountHistogram;
    private ArrayList<Integer> listHistogram;
    private ArrayList<Integer> listEffectedRowCountHistogram;
    private ArrayList<Integer> listExecuteAndResultHoldTimeHistogram;
    private ArrayList<Integer> listFetchRowCountHistogram;
    private ArrayList<String> ids;
    private static final String JSON_ID_NAME = "ID";
    
    public DruidSQLPanel() {
        this.url = "/sql.json";
    }
    
    private void addGroupData(final String keyNow, final int index) {
        if ("Histogram".equals(keyNow)) {
            this.listHistogram.add(index);
        }
        else if ("EffectedRowCountHistogram".equals(keyNow)) {
            this.listEffectedRowCountHistogram.add(index);
        }
        else if ("ExecuteAndResultHoldTimeHistogram".equals(keyNow)) {
            this.listExecuteAndResultHoldTimeHistogram.add(index);
        }
        else if ("FetchRowCountHistogram".equals(keyNow)) {
            this.listFetchRowCountHistogram.add(index);
        }
    }
    
    private ArrayList<LinkedHashMap<String, Object>> preProcess(final ArrayList<LinkedHashMap<String, Object>> data) {
        this.groupHistogram = new ColumnGroup("Histogram");
        this.groupEffectedRowCountHistogram = new ColumnGroup("EffectedRowCountHistogram");
        this.groupExecuteAndResultHoldTimeHistogram = new ColumnGroup("ExecuteAndResultHoldTimeHistogram");
        this.groupFetchRowCountHistogram = new ColumnGroup("FetchRowCountHistogram");
        this.listHistogram = new ArrayList<Integer>();
        this.listEffectedRowCountHistogram = new ArrayList<Integer>();
        this.listExecuteAndResultHoldTimeHistogram = new ArrayList<Integer>();
        this.listFetchRowCountHistogram = new ArrayList<Integer>();
        this.ids = new ArrayList<String>();
        final ArrayList<LinkedHashMap<String, Object>> newData = new ArrayList<LinkedHashMap<String, Object>>();
        int dataIndex = 0;
        for (final LinkedHashMap<String, Object> dataNow : data) {
            final Iterator<Map.Entry<String, Object>> it = dataNow.entrySet().iterator();
            while (it.hasNext()) {
                final Map.Entry<String, Object> entry = it.next();
                final String key = entry.getKey();
                final Object value = entry.getValue();
                if ("ID".equals(key)) {
                    this.ids.add((value != null) ? value.toString() : null);
                }
                if (!DruidSQLPanel.SHOW_LIST.contains(key)) {
                    it.remove();
                }
            }
            int offset = 9;
            for (final String arrayKey : DruidSQLPanel.ARRAY_DATA_MAP) {
                final Object arrayData = dataNow.get(arrayKey);
                if (arrayData instanceof ArrayList) {
                    dataNow.remove(arrayKey);
                    if (dataIndex == 0) {
                        DruidSQLPanel.REAL_SHOW_LIST.remove(arrayKey);
                    }
                    final ArrayList<Integer> arrayDataList = (ArrayList<Integer>)arrayData;
                    for (int j = 0, len = arrayDataList.size(); j < len; ++j) {
                        final int a = (j - 1 >= 0) ? ((int)Math.pow(10.0, j - 1)) : 0;
                        final int b = (int)Math.pow(10.0, j);
                        final String newKey = arrayKey + "-" + a + "~" + b + "ms";
                        dataNow.put(newKey, arrayDataList.get(j));
                        if (dataIndex == 0) {
                            DruidSQLPanel.REAL_SHOW_LIST.add(newKey);
                            final int index = offset + j;
                            this.addGroupData(arrayKey, index);
                            if (j == len - 1) {
                                offset = index + 1;
                            }
                        }
                    }
                }
            }
            ++dataIndex;
            newData.add(dataNow);
        }
        return newData;
    }
    
    private void addTableGroup() {
        final TableColumnModel cm = this.table.getColumnModel();
        for (final int i : this.listHistogram) {
            this.groupHistogram.add(cm.getColumn(i));
        }
        for (final int j : this.listEffectedRowCountHistogram) {
            this.groupEffectedRowCountHistogram.add(cm.getColumn(j));
        }
        for (final int x : this.listExecuteAndResultHoldTimeHistogram) {
            this.groupExecuteAndResultHoldTimeHistogram.add(cm.getColumn(x));
        }
        for (final int y : this.listFetchRowCountHistogram) {
            this.groupFetchRowCountHistogram.add(cm.getColumn(y));
        }
        final GroupableTableHeader header = (GroupableTableHeader)this.table.getTableHeader();
        header.addColumnGroup(this.groupHistogram);
        header.addColumnGroup(this.groupEffectedRowCountHistogram);
        header.addColumnGroup(this.groupExecuteAndResultHoldTimeHistogram);
        header.addColumnGroup(this.groupFetchRowCountHistogram);
        header.setUI(new GroupableTableHeaderUI());
    }
    
    @Override
    protected void tableDataProcess(ArrayList<LinkedHashMap<String, Object>> data) {
        this.table = new JTable() {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(this.columnModel);
            }
        };
        data = this.preProcess(data);
        this.tableModel = new DruidTableModel(data, DruidSQLPanel.REAL_SHOW_LIST);
        this.table.setModel(this.tableModel);
        this.table.setAutoResizeMode(0);
        this.addTableGroup();
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    final int row = DruidSQLPanel.this.table.getSelectedRow();
                    final String id = DruidSQLPanel.this.ids.get(row);
                    new DruidSqlDetailFrame(id, DruidSQLPanel.this.conn);
                }
            }
        });
        final RowHeaderTable header = new RowHeaderTable(this.table, 20);
        this.scrollPane.setRowHeaderView(header);
        this.scrollPane.setViewportView(this.table);
        final JLabel jb = new JLabel("N", 0);
        jb.setBorder(new BevelBorder(0, null, null, null, null));
        this.scrollPane.setCorner("UPPER_LEFT_CORNER", jb);
    }
    
    static {
        SHOW_LIST = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;
            
            {
                this.add("SQL");
                this.add("ExecuteCount");
                this.add("TotalTime");
                this.add("InTransactionCount");
                this.add("ErrorCount");
                this.add("EffectedRowCount");
                this.add("FetchRowCount");
                this.add("RunningCount");
                this.add("ConcurrentMax");
                this.add("Histogram");
                this.add("EffectedRowCountHistogram");
                this.add("ExecuteAndResultHoldTimeHistogram");
                this.add("FetchRowCountHistogram");
            }
        };
        REAL_SHOW_LIST = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;
            
            {
                this.add("SQL");
                this.add("ExecuteCount");
                this.add("TotalTime");
                this.add("InTransactionCount");
                this.add("ErrorCount");
                this.add("EffectedRowCount");
                this.add("FetchRowCount");
                this.add("RunningCount");
                this.add("ConcurrentMax");
                this.add("Histogram");
                this.add("EffectedRowCountHistogram");
                this.add("ExecuteAndResultHoldTimeHistogram");
                this.add("FetchRowCountHistogram");
            }
        };
        ARRAY_DATA_MAP = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;
            
            {
                this.add("Histogram");
                this.add("EffectedRowCountHistogram");
                this.add("ExecuteAndResultHoldTimeHistogram");
                this.add("FetchRowCountHistogram");
            }
        };
    }
}
