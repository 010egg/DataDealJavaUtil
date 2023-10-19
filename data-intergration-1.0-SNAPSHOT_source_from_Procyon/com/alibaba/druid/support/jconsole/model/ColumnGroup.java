// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole.model;

import java.awt.Dimension;
import java.util.Enumeration;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import javax.swing.UIManager;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Vector;
import javax.swing.table.TableCellRenderer;

public class ColumnGroup
{
    protected TableCellRenderer renderer;
    protected Vector<Object> vector;
    protected String text;
    private int margin;
    
    public ColumnGroup(final String text) {
        this(null, text);
    }
    
    public ColumnGroup(final TableCellRenderer renderer, final String text) {
        this.renderer = null;
        this.vector = null;
        this.text = null;
        this.margin = 0;
        if (renderer == null) {
            this.renderer = new DefaultTableCellRenderer() {
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
        else {
            this.renderer = renderer;
        }
        this.text = text;
        this.vector = new Vector<Object>();
    }
    
    public void add(final Object obj) {
        if (obj == null) {
            return;
        }
        this.vector.addElement(obj);
    }
    
    public Vector<ColumnGroup> getColumnGroups(final TableColumn column, final Vector<ColumnGroup> group) {
        group.addElement(this);
        if (this.vector.contains(column)) {
            return group;
        }
        final Enumeration<Object> enumeration = this.vector.elements();
        while (enumeration.hasMoreElements()) {
            final Object obj = enumeration.nextElement();
            if (obj instanceof ColumnGroup) {
                final Vector<ColumnGroup> groups = ((ColumnGroup)obj).getColumnGroups(column, (Vector<ColumnGroup>)group.clone());
                if (groups != null) {
                    return groups;
                }
                continue;
            }
        }
        return null;
    }
    
    public TableCellRenderer getHeaderRenderer() {
        return this.renderer;
    }
    
    public Object getHeaderValue() {
        return this.text;
    }
    
    public int getSize() {
        return (this.vector == null) ? 0 : this.vector.size();
    }
    
    public Dimension getSize(final JTable table) {
        final Component comp = this.renderer.getTableCellRendererComponent(table, this.getHeaderValue(), false, false, -1, -1);
        final int height = comp.getPreferredSize().height;
        int width = 0;
        final Enumeration<Object> enumeration = this.vector.elements();
        while (enumeration.hasMoreElements()) {
            final Object obj = enumeration.nextElement();
            if (obj instanceof TableColumn) {
                final TableColumn aColumn = (TableColumn)obj;
                width += aColumn.getWidth();
                width += this.margin;
            }
            else {
                width += ((ColumnGroup)obj).getSize(table).width;
            }
        }
        return new Dimension(width, height);
    }
    
    public String getText() {
        return this.text;
    }
    
    public boolean removeColumn(final ColumnGroup ptg, final TableColumn tc) {
        boolean retFlag = false;
        if (tc != null) {
            for (int i = 0; i < ptg.vector.size(); ++i) {
                final Object tmpObj = ptg.vector.get(i);
                if (tmpObj instanceof ColumnGroup) {
                    retFlag = this.removeColumn((ColumnGroup)tmpObj, tc);
                    if (retFlag) {
                        break;
                    }
                }
                else if (tmpObj instanceof TableColumn && tmpObj == tc) {
                    ptg.vector.remove(i);
                    retFlag = true;
                    break;
                }
            }
        }
        return retFlag;
    }
    
    public boolean removeColumnGrp(final ColumnGroup ptg, final ColumnGroup tg) {
        boolean retFlag = false;
        if (tg != null) {
            for (int i = 0; i < ptg.vector.size(); ++i) {
                final Object tmpObj = ptg.vector.get(i);
                if (tmpObj instanceof ColumnGroup) {
                    if (tmpObj == tg) {
                        ptg.vector.remove(i);
                        retFlag = true;
                        break;
                    }
                    retFlag = this.removeColumnGrp((ColumnGroup)tmpObj, tg);
                    if (retFlag) {
                        break;
                    }
                }
                else if (tmpObj instanceof TableColumn) {
                    break;
                }
            }
        }
        return retFlag;
    }
    
    public void setColumnMargin(final int margin) {
        this.margin = margin;
        final Enumeration<Object> enumeration = this.vector.elements();
        while (enumeration.hasMoreElements()) {
            final Object obj = enumeration.nextElement();
            if (obj instanceof ColumnGroup) {
                ((ColumnGroup)obj).setColumnMargin(margin);
            }
        }
    }
    
    public void setHeaderRenderer(final TableCellRenderer renderer) {
        if (renderer != null) {
            this.renderer = renderer;
        }
    }
    
    public void setText(final String newText) {
        this.text = newText;
    }
}
