// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole.model;

import java.awt.Dimension;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import javax.swing.JTable;

public class RowHeaderTable extends JTable
{
    private static final long serialVersionUID = 1L;
    
    public RowHeaderTable(final JTable refTable, final int columnWidth) {
        this(null, refTable, columnWidth, 1);
    }
    
    public RowHeaderTable(final ArrayList<String> title, final JTable refTable, final int columnWidth) {
        this(title, refTable, columnWidth, 1);
    }
    
    public RowHeaderTable(final ArrayList<String> title, final JTable refTable, final int columnWidth, final int rowSpan) {
        super(new DefaultTableModel(refTable.getRowCount() / rowSpan, 1));
        this.setAutoResizeMode(0);
        this.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);
        this.setDefaultRenderer(Object.class, new RowHeaderRenderer(title, refTable, this, rowSpan));
        this.setPreferredScrollableViewportSize(new Dimension(columnWidth, 0));
    }
}
