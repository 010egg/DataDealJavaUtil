// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import org.postgresql.PGNotification;

public class Notification implements PGNotification
{
    private String m_name;
    private String m_parameter;
    private int m_pid;
    
    public Notification(final String p_name, final int p_pid) {
        this(p_name, p_pid, "");
    }
    
    public Notification(final String p_name, final int p_pid, final String p_parameter) {
        this.m_name = p_name;
        this.m_pid = p_pid;
        this.m_parameter = p_parameter;
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public int getPID() {
        return this.m_pid;
    }
    
    @Override
    public String getParameter() {
        return this.m_parameter;
    }
}
