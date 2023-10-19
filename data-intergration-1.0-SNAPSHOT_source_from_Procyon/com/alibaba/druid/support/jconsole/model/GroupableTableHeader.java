// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole.model;

import java.util.Enumeration;
import javax.swing.table.TableColumn;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.table.TableColumnModel;
import java.util.Vector;
import javax.swing.table.JTableHeader;

public class GroupableTableHeader extends JTableHeader
{
    private static final long serialVersionUID = 1L;
    protected Vector<ColumnGroup> columnGroups;
    
    public GroupableTableHeader(final TableColumnModel model) {
        super(model);
        this.columnGroups = null;
        this.setUI(new GroupableTableHeaderUI());
        this.setReorderingAllowed(false);
        this.setRequestFocusEnabled(false);
    }
    
    public void addColumnGroup(final ColumnGroup g) {
        if (this.columnGroups == null) {
            this.columnGroups = new Vector<ColumnGroup>();
        }
        this.columnGroups.addElement(g);
    }
    
    public void clearColumnGroups() {
        this.columnGroups = null;
    }
    
    public ColumnGroup[] getColumnGroups() {
        ColumnGroup[] retg = null;
        if (this.columnGroups.size() > 0) {
            retg = new ColumnGroup[this.columnGroups.size()];
            this.columnGroups.copyInto(retg);
        }
        return retg;
    }
    
    public Enumeration<ColumnGroup> getColumnGroups(final TableColumn col) {
        if (this.columnGroups == null) {
            return null;
        }
        final Enumeration<ColumnGroup> enum1 = this.columnGroups.elements();
        while (enum1.hasMoreElements()) {
            final ColumnGroup cGroup = enum1.nextElement();
            final Vector<ColumnGroup> v_ret = cGroup.getColumnGroups(col, new Vector<ColumnGroup>());
            if (v_ret != null) {
                return v_ret.elements();
            }
        }
        return null;
    }
    
    @Override
    public boolean isFocusTraversable() {
        return super.isFocusable() && this.isRequestFocusEnabled();
    }
    
    public void setColumnMargin() {
        if (this.columnGroups == null) {
            return;
        }
        final int columnMargin = this.getColumnModel().getColumnMargin();
        final Enumeration<ColumnGroup> enum1 = this.columnGroups.elements();
        while (enum1.hasMoreElements()) {
            final ColumnGroup cGroup = enum1.nextElement();
            cGroup.setColumnMargin(columnMargin);
        }
    }
    
    @Override
    public void setReorderingAllowed(final boolean b) {
        this.reorderingAllowed = b;
    }
}
