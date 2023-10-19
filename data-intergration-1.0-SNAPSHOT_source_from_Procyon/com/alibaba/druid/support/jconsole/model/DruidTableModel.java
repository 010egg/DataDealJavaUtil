// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole.model;

import javax.swing.event.TableModelListener;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import javax.swing.table.TableModel;

public class DruidTableModel implements TableModel
{
    private ArrayList<LinkedHashMap<String, Object>> list;
    private ArrayList<String> showKeys;
    
    public DruidTableModel(final ArrayList<LinkedHashMap<String, Object>> list) {
        this.list = list;
        this.showKeys = null;
    }
    
    public DruidTableModel(final ArrayList<LinkedHashMap<String, Object>> list, final ArrayList<String> showKeys) {
        this.list = list;
        this.showKeys = showKeys;
    }
    
    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return String.class;
    }
    
    @Override
    public int getColumnCount() {
        int colCount = 0;
        if (this.showKeys != null) {
            colCount = this.showKeys.size();
        }
        else if (this.list != null) {
            final int listLen = this.list.size();
            if (listLen > 0) {
                colCount = this.list.get(0).size();
            }
        }
        return colCount;
    }
    
    @Override
    public String getColumnName(final int columnIndex) {
        if (this.showKeys != null && this.showKeys.size() > 0) {
            final String keyNow = this.showKeys.get(columnIndex);
            if (keyNow != null) {
                return keyNow.substring(keyNow.indexOf(45) + 1, keyNow.length());
            }
        }
        if (this.list != null && this.list.size() > 0) {
            final LinkedHashMap<String, Object> firstElement = this.list.get(0);
            final Object[] keys = firstElement.keySet().toArray();
            return keys[columnIndex].toString();
        }
        return null;
    }
    
    @Override
    public int getRowCount() {
        return this.list.size();
    }
    
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (this.list != null && rowIndex < this.list.size()) {
            final LinkedHashMap<String, Object> dataNow = this.list.get(rowIndex);
            if (this.showKeys != null) {
                final int titleLen = this.showKeys.size();
                if (titleLen > 0 && columnIndex < titleLen) {
                    return dataNow.get(this.showKeys.get(columnIndex));
                }
            }
            else {
                final Object[] values = dataNow.values().toArray();
                if (columnIndex < values.length) {
                    return values[columnIndex];
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }
    
    @Override
    public void addTableModelListener(final TableModelListener l) {
    }
    
    @Override
    public void removeTableModelListener(final TableModelListener l) {
    }
    
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
    }
}
