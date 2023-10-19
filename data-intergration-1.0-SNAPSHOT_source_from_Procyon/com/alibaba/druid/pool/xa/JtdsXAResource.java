// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.xa;

import com.alibaba.druid.support.logging.LogFactory;
import javax.transaction.xa.XAException;
import net.sourceforge.jtds.jdbc.XASupport;
import javax.transaction.xa.Xid;
import java.lang.reflect.Method;
import java.sql.Connection;
import com.alibaba.druid.support.logging.Log;
import javax.transaction.xa.XAResource;

public class JtdsXAResource implements XAResource
{
    private static final Log LOG;
    private final Connection connection;
    private final JtdsXAConnection xaConnection;
    private String rmHost;
    private static Method method;
    
    public JtdsXAResource(final JtdsXAConnection xaConnection, final Connection connection) {
        this.xaConnection = xaConnection;
        this.connection = connection;
        if (JtdsXAResource.method == null) {
            try {
                JtdsXAResource.method = connection.getClass().getMethod("getRmHost", (Class<?>[])new Class[0]);
            }
            catch (Exception e) {
                JtdsXAResource.LOG.error("getRmHost method error", e);
            }
        }
        if (JtdsXAResource.method != null) {
            try {
                this.rmHost = (String)JtdsXAResource.method.invoke(connection, new Object[0]);
            }
            catch (Exception e) {
                JtdsXAResource.LOG.error("getRmHost error", e);
            }
        }
    }
    
    protected JtdsXAConnection getResourceManager() {
        return this.xaConnection;
    }
    
    protected String getRmHost() {
        return this.rmHost;
    }
    
    @Override
    public void commit(final Xid xid, final boolean commit) throws XAException {
        XASupport.xa_commit(this.connection, this.xaConnection.getXAConnectionID(), xid, commit);
    }
    
    @Override
    public void end(final Xid xid, final int flags) throws XAException {
        XASupport.xa_end(this.connection, this.xaConnection.getXAConnectionID(), xid, flags);
    }
    
    @Override
    public void forget(final Xid xid) throws XAException {
        XASupport.xa_forget(this.connection, this.xaConnection.getXAConnectionID(), xid);
    }
    
    @Override
    public int getTransactionTimeout() throws XAException {
        return 0;
    }
    
    @Override
    public boolean isSameRM(final XAResource xares) throws XAException {
        return xares instanceof JtdsXAResource && ((JtdsXAResource)xares).getRmHost().equals(this.rmHost);
    }
    
    @Override
    public int prepare(final Xid xid) throws XAException {
        return XASupport.xa_prepare(this.connection, this.xaConnection.getXAConnectionID(), xid);
    }
    
    @Override
    public Xid[] recover(final int flags) throws XAException {
        return XASupport.xa_recover(this.connection, this.xaConnection.getXAConnectionID(), flags);
    }
    
    @Override
    public void rollback(final Xid xid) throws XAException {
        XASupport.xa_rollback(this.connection, this.xaConnection.getXAConnectionID(), xid);
    }
    
    @Override
    public boolean setTransactionTimeout(final int seconds) throws XAException {
        return false;
    }
    
    @Override
    public void start(final Xid xid, final int flags) throws XAException {
        XASupport.xa_start(this.connection, this.xaConnection.getXAConnectionID(), xid, flags);
    }
    
    static {
        LOG = LogFactory.getLog(JtdsXAResource.class);
    }
}
