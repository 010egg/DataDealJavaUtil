// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole.model;

import java.awt.Container;
import java.util.Hashtable;
import java.awt.Rectangle;
import java.awt.Graphics;
import javax.swing.JComponent;
import java.util.Enumeration;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import javax.swing.UIManager;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Vector;
import javax.swing.table.TableColumnModel;
import java.awt.Dimension;
import javax.swing.plaf.basic.BasicTableHeaderUI;

public class GroupableTableHeaderUI extends BasicTableHeaderUI
{
    private int m_height;
    
    private Dimension createHeaderSize(long width) {
        final TableColumnModel columnModel = this.header.getColumnModel();
        width += columnModel.getColumnMargin() * columnModel.getColumnCount();
        if (width > 2147483647L) {
            width = 2147483647L;
        }
        return new Dimension((int)width, this.getHeaderHeight());
    }
    
    private int getColCountUnderColGroup(final ColumnGroup cg, int iCount) {
        final Vector<Object> v = cg.vector;
        for (int i = 0; i < v.size(); ++i) {
            final Object obj = v.elementAt(i);
            if (obj instanceof ColumnGroup) {
                iCount = this.getColCountUnderColGroup((ColumnGroup)obj, iCount);
            }
            else {
                ++iCount;
            }
        }
        return iCount;
    }
    
    public int getHeaderHeight() {
        int height = 0;
        final TableColumnModel columnModel = this.header.getColumnModel();
        for (int column = 0; column < columnModel.getColumnCount(); ++column) {
            final TableColumn aColumn = columnModel.getColumn(column);
            TableCellRenderer renderer = aColumn.getHeaderRenderer();
            if (renderer == null) {
                renderer = new DefaultTableCellRenderer() {
                    private static final long serialVersionUID = 1L;
                    
                    @Override
                    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                        final JTableHeader header = table.getTableHeader();
                        if (header != null) {
                            this.setForeground(header.getForeground());
                            this.setBackground(header.getBackground());
                            this.setFont(header.getFont());
                        }
                        this.setHorizontalAlignment(0);
                        this.setText((value == null) ? "" : value.toString());
                        this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                        return this;
                    }
                };
            }
            final Component comp = renderer.getTableCellRendererComponent(this.header.getTable(), aColumn.getHeaderValue(), false, false, -1, column);
            int cHeight = comp.getPreferredSize().height;
            final Enumeration<ColumnGroup> enumeration = ((GroupableTableHeader)this.header).getColumnGroups(aColumn);
            if (enumeration != null) {
                while (enumeration.hasMoreElements()) {
                    final ColumnGroup cGroup = enumeration.nextElement();
                    cHeight += cGroup.getSize(this.header.getTable()).height;
                }
            }
            height = Math.max(height, cHeight);
        }
        height = Math.max(height, this.m_height);
        return height;
    }
    
    @Override
    public Dimension getPreferredSize(final JComponent c) {
        long width = 0L;
        final Enumeration<TableColumn> enumeration = this.header.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            final TableColumn aColumn = enumeration.nextElement();
            width += aColumn.getWidth();
        }
        return this.createHeaderSize(width);
    }
    
    @Override
    public void paint(final Graphics g, final JComponent c) {
        final Rectangle clipBounds = g.getClipBounds();
        if (this.header.getColumnModel() == null) {
            return;
        }
        ((GroupableTableHeader)this.header).setColumnMargin();
        int column = 0;
        final Dimension size = this.header.getSize();
        final Rectangle cellRect = new Rectangle(0, 0, size.width, size.height);
        final Hashtable<ColumnGroup, Rectangle> h = new Hashtable<ColumnGroup, Rectangle>();
        final int columnMargin = this.header.getColumnModel().getColumnMargin();
        final Enumeration<TableColumn> enumeration = this.header.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            cellRect.height = size.height;
            cellRect.y = 0;
            final TableColumn aColumn = enumeration.nextElement();
            final Enumeration<ColumnGroup> cGroups = ((GroupableTableHeader)this.header).getColumnGroups(aColumn);
            if (cGroups != null) {
                int groupHeight = 0;
                while (cGroups.hasMoreElements()) {
                    final ColumnGroup cGroup = cGroups.nextElement();
                    Rectangle groupRect = h.get(cGroup);
                    if (groupRect == null) {
                        groupRect = new Rectangle(cellRect);
                        final Dimension d = cGroup.getSize(this.header.getTable());
                        if (!System.getProperty("java.vm.version").startsWith("1.2")) {
                            final int iColCount = this.getColCountUnderColGroup(cGroup, 0);
                            groupRect.width = d.width - iColCount * columnMargin;
                        }
                        else {
                            groupRect.width = d.width;
                        }
                        groupRect.height = d.height;
                        h.put(cGroup, groupRect);
                    }
                    this.paintCell(g, groupRect, cGroup);
                    groupHeight += groupRect.height;
                    cellRect.height = size.height - groupHeight;
                    cellRect.y = groupHeight;
                }
            }
            if (!System.getProperty("java.vm.version").startsWith("1.2")) {
                cellRect.width = aColumn.getWidth();
            }
            else {
                cellRect.width = aColumn.getWidth() + columnMargin;
            }
            if (cellRect.intersects(clipBounds)) {
                this.paintCell(g, cellRect, column);
            }
            final Rectangle rectangle = cellRect;
            rectangle.x += cellRect.width;
            ++column;
        }
    }
    
    private void paintCell(final Graphics g, final Rectangle cellRect, final int columnIndex) {
        final TableColumn aColumn = this.header.getColumnModel().getColumn(columnIndex);
        TableCellRenderer renderer = aColumn.getHeaderRenderer();
        if (renderer == null) {
            renderer = new DefaultTableCellRenderer() {
                private static final long serialVersionUID = 1L;
                
                @Override
                public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                    final JTableHeader header = table.getTableHeader();
                    if (header != null) {
                        this.setForeground(header.getForeground());
                        this.setBackground(header.getBackground());
                        this.setFont(header.getFont());
                    }
                    this.setHorizontalAlignment(0);
                    this.setText((value == null) ? "" : value.toString());
                    this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                    return this;
                }
            };
        }
        final String headerValue = aColumn.getHeaderValue().toString();
        final Component component = renderer.getTableCellRendererComponent(this.header.getTable(), headerValue, false, false, -1, columnIndex);
        this.rendererPane.add(component);
        this.rendererPane.paintComponent(g, component, this.header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
    }
    
    private void paintCell(final Graphics g, final Rectangle cellRect, final ColumnGroup cGroup) {
        TableCellRenderer renderer = cGroup.getHeaderRenderer();
        if (renderer == null) {
            renderer = new DefaultTableCellRenderer() {
                private static final long serialVersionUID = 1L;
                
                @Override
                public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                    final JTableHeader header = table.getTableHeader();
                    if (header != null) {
                        this.setForeground(header.getForeground());
                        this.setBackground(header.getBackground());
                        this.setFont(header.getFont());
                    }
                    this.setHorizontalAlignment(0);
                    this.setText((value == null) ? "" : value.toString());
                    this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                    return this;
                }
            };
        }
        final String headerValue = cGroup.getHeaderValue().toString();
        final Component component = renderer.getTableCellRendererComponent(this.header.getTable(), headerValue, false, false, -1, -1);
        this.rendererPane.add(component);
        this.rendererPane.paintComponent(g, component, this.header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
    }
    
    public void setHeaderHeight(final int iHeight) {
        this.m_height = iHeight;
    }
}
