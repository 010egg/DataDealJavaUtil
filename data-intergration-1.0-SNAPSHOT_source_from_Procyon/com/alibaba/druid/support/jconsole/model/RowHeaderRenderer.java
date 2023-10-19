// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole.model;

import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import javax.swing.ListSelectionModel;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.JLabel;

final class RowHeaderRenderer extends JLabel implements TableCellRenderer, ListSelectionListener
{
    private static final long serialVersionUID = 1L;
    private JTable refTable;
    private JTable tableShow;
    private ArrayList<String> headerList;
    private int rowHeightNow;
    private int rowSpan;
    
    public RowHeaderRenderer(final JTable refTable, final JTable tableShow) {
        this(null, refTable, tableShow, 0);
    }
    
    public RowHeaderRenderer(final ArrayList<String> headerList, final JTable refTable, final JTable tableShow, final int rowSpan) {
        this.headerList = headerList;
        this.refTable = refTable;
        this.tableShow = tableShow;
        final ListSelectionModel listModel = refTable.getSelectionModel();
        listModel.addListSelectionListener(this);
        this.rowHeightNow = refTable.getRowCount() * refTable.getRowHeight();
        this.rowSpan = rowSpan;
        if (rowSpan > 1) {
            this.rowHeightNow = rowSpan * refTable.getRowHeight();
        }
    }
    
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object obj, final boolean isSelected, final boolean hasFocus, final int row, final int col) {
        final int rowCountNow = this.refTable.getRowCount() / this.rowSpan;
        ((DefaultTableModel)table.getModel()).setRowCount(rowCountNow);
        final JTableHeader header = this.refTable.getTableHeader();
        this.setOpaque(true);
        this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        this.setHorizontalAlignment(0);
        this.setBackground(header.getBackground());
        if (this.isSelect(row)) {
            this.setForeground(Color.white);
            this.setBackground(Color.lightGray);
        }
        else {
            this.setForeground(header.getForeground());
        }
        this.setFont(header.getFont());
        if (row <= rowCountNow) {
            this.showCol(row);
        }
        return this;
    }
    
    private void showCol(final int row) {
        String text = null;
        if (this.headerList != null && row < this.headerList.size()) {
            text = this.headerList.get(row);
        }
        else {
            text = String.valueOf(row + 1);
        }
        if (this.rowSpan > 1) {
            this.setText(text);
            this.tableShow.setRowHeight(row, this.rowHeightNow);
        }
        else {
            this.setText(text);
        }
    }
    
    @Override
    public void valueChanged(final ListSelectionEvent e) {
        this.tableShow.repaint();
    }
    
    private boolean isSelect(final int row) {
        final int[] sel = this.refTable.getSelectedRows();
        if (this.rowSpan <= 1) {
            for (final int item : sel) {
                if (item == row) {
                    return true;
                }
            }
        }
        else {
            for (final int item : sel) {
                if (item / this.rowSpan == row) {
                    return true;
                }
            }
        }
        return false;
    }
}
