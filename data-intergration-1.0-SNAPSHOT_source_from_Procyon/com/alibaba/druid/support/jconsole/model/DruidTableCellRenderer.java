// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole.model;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JLabel;

public class DruidTableCellRenderer extends JLabel implements TableCellRenderer
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        if (column == 0) {
            this.setBackground(Color.lightGray);
        }
        this.setOpaque(true);
        if (value != null) {
            this.setText(value.toString());
        }
        else {
            this.setText(row + 1 + "");
        }
        return this;
    }
}
