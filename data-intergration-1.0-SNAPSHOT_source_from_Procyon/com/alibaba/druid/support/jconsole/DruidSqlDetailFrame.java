// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole;

import com.alibaba.druid.support.logging.LogFactory;
import java.awt.Toolkit;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import java.awt.Container;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import com.alibaba.druid.support.jconsole.model.DruidTableCellRenderer;
import javax.swing.table.TableModel;
import com.alibaba.druid.support.jconsole.model.DruidTableModel;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.util.Iterator;
import java.util.Map;
import com.alibaba.druid.support.jconsole.util.TableDataProcessor;
import com.alibaba.druid.support.logging.Log;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import javax.management.MBeanServerConnection;
import javax.swing.JFrame;

public class DruidSqlDetailFrame extends JFrame
{
    private static final long serialVersionUID = 1L;
    private static final String BASE_URL = "/sql";
    private static final String KEY_FORMAT_SQL = "formattedSql";
    private static final String KEY_SQL = "SQL";
    private String id;
    private MBeanServerConnection conn;
    private static final ArrayList<String> PARESE_TITLE_LIST;
    private static final ArrayList<String> LAST_SLOW_TITLE_LIST;
    private static final ArrayList<String> LAST_ERROR_TITLE_LIST;
    private static final ArrayList<String> OTHER_ERROR_TITLE_LIST;
    private ArrayList<LinkedHashMap<String, Object>> parseData;
    private ArrayList<LinkedHashMap<String, Object>> lastSlowData;
    private ArrayList<LinkedHashMap<String, Object>> lastErrorData;
    private ArrayList<LinkedHashMap<String, Object>> otherData;
    private int maxListLen;
    private String formatSql;
    private String sql;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final Log LOG;
    
    public DruidSqlDetailFrame(final String id, final MBeanServerConnection conn) {
        this.id = id;
        this.conn = conn;
        this.getMaxListLen();
        this.init();
        this.start();
    }
    
    private void getMaxListLen() {
        this.maxListLen = DruidSqlDetailFrame.PARESE_TITLE_LIST.size();
        final int slowLen = DruidSqlDetailFrame.LAST_SLOW_TITLE_LIST.size();
        final int errLen = DruidSqlDetailFrame.LAST_ERROR_TITLE_LIST.size();
        final int otherLen = DruidSqlDetailFrame.OTHER_ERROR_TITLE_LIST.size();
        if (this.maxListLen < slowLen) {
            this.maxListLen = slowLen;
        }
        if (this.maxListLen < errLen) {
            this.maxListLen = errLen;
        }
        if (this.maxListLen < otherLen) {
            this.maxListLen = otherLen;
        }
    }
    
    private void init() {
        final String url = "/sql-" + this.id + ".json";
        try {
            final ArrayList<LinkedHashMap<String, Object>> data = TableDataProcessor.parseData(TableDataProcessor.getData(url, this.conn));
            if (data != null) {
                final LinkedHashMap<String, Object> contentEle = data.get(0);
                this.formatSql = contentEle.remove("formattedSql");
                this.sql = contentEle.remove("SQL");
                final int parseLen = DruidSqlDetailFrame.PARESE_TITLE_LIST.size();
                final int slowLen = DruidSqlDetailFrame.LAST_SLOW_TITLE_LIST.size();
                final int errLen = DruidSqlDetailFrame.LAST_ERROR_TITLE_LIST.size();
                final int otherLen = DruidSqlDetailFrame.OTHER_ERROR_TITLE_LIST.size();
                final LinkedHashMap<String, Object> parseDataEle = new LinkedHashMap<String, Object>();
                final LinkedHashMap<String, Object> slowDataEle = new LinkedHashMap<String, Object>();
                final LinkedHashMap<String, Object> errDataEle = new LinkedHashMap<String, Object>();
                final LinkedHashMap<String, Object> otherDataEle = new LinkedHashMap<String, Object>();
                this.parseData = new ArrayList<LinkedHashMap<String, Object>>(1);
                this.lastSlowData = new ArrayList<LinkedHashMap<String, Object>>(1);
                this.lastErrorData = new ArrayList<LinkedHashMap<String, Object>>(1);
                this.otherData = new ArrayList<LinkedHashMap<String, Object>>(1);
                for (final Map.Entry<String, Object> entry : contentEle.entrySet()) {
                    final String key = entry.getKey();
                    final Object value = entry.getValue();
                    for (int i = 0; i < this.maxListLen; ++i) {
                        if (i < parseLen && key.equals(DruidSqlDetailFrame.PARESE_TITLE_LIST.get(i))) {
                            parseDataEle.put(key, value);
                        }
                        else if (i < slowLen && key.equals(DruidSqlDetailFrame.LAST_SLOW_TITLE_LIST.get(i))) {
                            slowDataEle.put(key, value);
                        }
                        else if (i < errLen && key.equals(DruidSqlDetailFrame.LAST_ERROR_TITLE_LIST.get(i))) {
                            errDataEle.put(key, value);
                        }
                        else if (i < otherLen && key.equals(DruidSqlDetailFrame.OTHER_ERROR_TITLE_LIST.get(i))) {
                            otherDataEle.put(key, value);
                        }
                    }
                }
                this.parseData.add(parseDataEle);
                this.lastSlowData.add(slowDataEle);
                this.lastErrorData.add(errDataEle);
                this.otherData.add(otherDataEle);
            }
            else {
                DruidSqlDetailFrame.LOG.warn("\u9519\u8bef\u7684json\u683c\u5f0f");
            }
        }
        catch (Exception e) {
            DruidSqlDetailFrame.LOG.warn("\u83b7\u53d6\u6570\u636e\u65f6\u5f02\u5e38", e);
        }
    }
    
    private void addTable(final JPanel contentPanel, final String title, final ArrayList<LinkedHashMap<String, Object>> data) {
        final JPanel content1 = new JPanel();
        content1.setLayout(new BorderLayout());
        content1.setBorder(BorderFactory.createTitledBorder(title));
        contentPanel.add(content1);
        final TableDataProcessor.ColumnData colData = TableDataProcessor.row2col(data);
        final JTable table = new JTable();
        final DruidTableModel tableModel = new DruidTableModel(colData.getData());
        table.setModel(tableModel);
        final TableColumn col = table.getColumnModel().getColumn(0);
        col.setCellRenderer(new DruidTableCellRenderer());
        final JTableHeader header1 = table.getTableHeader();
        content1.add(header1, "North");
        content1.add(table);
    }
    
    private void addComponentsToPane(final Container pane) {
        final JScrollPane scrollPane = new JScrollPane();
        final JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 1));
        final JTextArea sqlField = new JTextArea(this.formatSql, 8, 20);
        final JScrollPane content1 = new JScrollPane(sqlField);
        content1.setBorder(BorderFactory.createTitledBorder("SQL\u8bed\u53e5"));
        contentPanel.add(content1);
        this.addTable(contentPanel, "\u89e3\u6790\u4fe1\u606f", this.parseData);
        this.addTable(contentPanel, "\u4e0a\u6b21\u6162\u67e5\u8be2\u4fe1\u606f", this.lastSlowData);
        this.addTable(contentPanel, "\u4e0a\u6b21\u9519\u8bef\u67e5\u8be2\u4fe1\u606f", this.lastErrorData);
        this.addTable(contentPanel, "\u5176\u4ed6\u4fe1\u606f", this.otherData);
        scrollPane.setViewportView(contentPanel);
        pane.add(scrollPane, "Center");
    }
    
    private void start() {
        this.addComponentsToPane(this.getContentPane());
        this.setTitle("SQL:[" + this.sql + "]\u8be6\u60c5");
        this.pack();
        this.setSize(800, 600);
        final double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        final double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((int)(width - this.getWidth()) / 2, (int)(height - this.getHeight()) / 2);
        this.setVisible(true);
    }
    
    static {
        PARESE_TITLE_LIST = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;
            
            {
                this.add("parsedTable");
                this.add("parsedFields");
                this.add("parsedConditions");
                this.add("parsedRelationships");
                this.add("parsedOrderbycolumns");
            }
        };
        LAST_SLOW_TITLE_LIST = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;
            
            {
                this.add("MaxTimespan");
                this.add("MaxTimespanOccurTime");
                this.add("LastSlowParameters");
            }
        };
        LAST_ERROR_TITLE_LIST = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;
            
            {
                this.add("LastErrorMessage");
                this.add("LastErrorClass");
                this.add("LastErrorTime");
                this.add("LastErrorStackTrace");
            }
        };
        OTHER_ERROR_TITLE_LIST = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;
            
            {
                this.add("BatchSizeMax");
                this.add("BatchSizeTotal");
                this.add("BlobOpenCount");
                this.add("ClobOpenCount");
                this.add("ReaderOpenCount");
                this.add("InputStreamOpenCount");
                this.add("ReadStringLength");
                this.add("ReadBytesLength");
            }
        };
        LOG = LogFactory.getLog(DruidSqlDetailFrame.class);
    }
}
