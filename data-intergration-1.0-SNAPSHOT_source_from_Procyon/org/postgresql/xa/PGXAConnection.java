// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.xa;

import java.lang.reflect.InvocationTargetException;
import org.postgresql.util.PSQLException;
import java.lang.reflect.Method;
import org.postgresql.util.PSQLState;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.sql.Statement;
import javax.transaction.xa.XAException;
import org.postgresql.util.GT;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.postgresql.PGConnection;
import java.sql.SQLException;
import java.sql.Connection;
import javax.transaction.xa.Xid;
import org.postgresql.core.Logger;
import org.postgresql.core.BaseConnection;
import javax.transaction.xa.XAResource;
import javax.sql.XAConnection;
import org.postgresql.ds.PGPooledConnection;

public class PGXAConnection extends PGPooledConnection implements XAConnection, XAResource
{
    private final BaseConnection conn;
    private final Logger logger;
    private Xid currentXid;
    private int state;
    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVE = 1;
    private static final int STATE_ENDED = 2;
    private boolean localAutoCommitMode;
    
    private void debug(final String s) {
        this.logger.debug("XAResource " + Integer.toHexString(this.hashCode()) + ": " + s);
    }
    
    public PGXAConnection(final BaseConnection conn) throws SQLException {
        super(conn, true, true);
        this.localAutoCommitMode = true;
        this.conn = conn;
        this.state = 0;
        this.logger = conn.getLogger();
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        if (this.logger.logDebug()) {
            this.debug("PGXAConnection.getConnection called");
        }
        final Connection conn = super.getConnection();
        if (this.state == 0) {
            conn.setAutoCommit(true);
        }
        final ConnectionHandler handler = new ConnectionHandler(conn);
        return (Connection)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { Connection.class, PGConnection.class }, handler);
    }
    
    @Override
    public XAResource getXAResource() {
        return this;
    }
    
    @Override
    public void start(final Xid xid, final int flags) throws XAException {
        if (this.logger.logDebug()) {
            this.debug("starting transaction xid = " + xid);
        }
        if (flags != 0 && flags != 134217728 && flags != 2097152) {
            throw new PGXAException(GT.tr("Invalid flags"), -5);
        }
        if (xid == null) {
            throw new PGXAException(GT.tr("xid must not be null"), -5);
        }
        if (this.state == 1) {
            throw new PGXAException(GT.tr("Connection is busy with another transaction"), -6);
        }
        if (flags == 134217728) {
            throw new PGXAException(GT.tr("suspend/resume not implemented"), -3);
        }
        if (flags == 2097152) {
            if (this.state != 2) {
                throw new PGXAException(GT.tr("Transaction interleaving not implemented"), -3);
            }
            if (!xid.equals(this.currentXid)) {
                throw new PGXAException(GT.tr("Transaction interleaving not implemented"), -3);
            }
        }
        else if (this.state == 2) {
            throw new PGXAException(GT.tr("Transaction interleaving not implemented"), -3);
        }
        try {
            this.localAutoCommitMode = this.conn.getAutoCommit();
            this.conn.setAutoCommit(false);
        }
        catch (SQLException ex) {
            throw new PGXAException(GT.tr("Error disabling autocommit"), ex, -3);
        }
        this.state = 1;
        this.currentXid = xid;
    }
    
    @Override
    public void end(final Xid xid, final int flags) throws XAException {
        if (this.logger.logDebug()) {
            this.debug("ending transaction xid = " + xid);
        }
        if (flags != 33554432 && flags != 536870912 && flags != 67108864) {
            throw new PGXAException(GT.tr("Invalid flags"), -5);
        }
        if (xid == null) {
            throw new PGXAException(GT.tr("xid must not be null"), -5);
        }
        if (this.state != 1 || !this.currentXid.equals(xid)) {
            throw new PGXAException(GT.tr("tried to call end without corresponding start call"), -6);
        }
        if (flags == 33554432) {
            throw new PGXAException(GT.tr("suspend/resume not implemented"), -3);
        }
        this.state = 2;
    }
    
    @Override
    public int prepare(final Xid xid) throws XAException {
        if (this.logger.logDebug()) {
            this.debug("preparing transaction xid = " + xid);
        }
        if (!this.currentXid.equals(xid)) {
            throw new PGXAException(GT.tr("Not implemented: Prepare must be issued using the same connection that started the transaction"), -3);
        }
        if (this.state != 2) {
            throw new PGXAException(GT.tr("Prepare called before end"), -5);
        }
        this.state = 0;
        this.currentXid = null;
        if (!this.conn.haveMinimumServerVersion("8.1")) {
            throw new PGXAException(GT.tr("Server versions prior to 8.1 do not support two-phase commit."), -3);
        }
        try {
            final String s = RecoveredXid.xidToString(xid);
            final Statement stmt = this.conn.createStatement();
            try {
                stmt.executeUpdate("PREPARE TRANSACTION '" + s + "'");
            }
            finally {
                stmt.close();
            }
            this.conn.setAutoCommit(this.localAutoCommitMode);
            return 0;
        }
        catch (SQLException ex) {
            throw new PGXAException(GT.tr("Error preparing transaction"), ex, -3);
        }
    }
    
    @Override
    public Xid[] recover(final int flag) throws XAException {
        if (flag != 16777216 && flag != 8388608 && flag != 0 && flag != 25165824) {
            throw new PGXAException(GT.tr("Invalid flag"), -5);
        }
        if ((flag & 0x1000000) == 0x0) {
            return new Xid[0];
        }
        try {
            final Statement stmt = this.conn.createStatement();
            try {
                final ResultSet rs = stmt.executeQuery("SELECT gid FROM pg_prepared_xacts where database = current_database()");
                final LinkedList l = new LinkedList();
                while (rs.next()) {
                    final Xid recoveredXid = RecoveredXid.stringToXid(rs.getString(1));
                    if (recoveredXid != null) {
                        l.add(recoveredXid);
                    }
                }
                rs.close();
                return l.toArray(new Xid[l.size()]);
            }
            finally {
                stmt.close();
            }
        }
        catch (SQLException ex) {
            throw new PGXAException(GT.tr("Error during recover"), ex, -3);
        }
    }
    
    @Override
    public void rollback(final Xid xid) throws XAException {
        if (this.logger.logDebug()) {
            this.debug("rolling back xid = " + xid);
        }
        try {
            if (this.currentXid != null && xid.equals(this.currentXid)) {
                this.state = 0;
                this.currentXid = null;
                this.conn.rollback();
                this.conn.setAutoCommit(this.localAutoCommitMode);
            }
            else {
                final String s = RecoveredXid.xidToString(xid);
                this.conn.setAutoCommit(true);
                final Statement stmt = this.conn.createStatement();
                try {
                    stmt.executeUpdate("ROLLBACK PREPARED '" + s + "'");
                }
                finally {
                    stmt.close();
                }
            }
        }
        catch (SQLException ex) {
            if (PSQLState.UNDEFINED_OBJECT.getState().equals(ex.getSQLState())) {
                throw new PGXAException(GT.tr("Error rolling back prepared transaction"), ex, -4);
            }
            throw new PGXAException(GT.tr("Error rolling back prepared transaction"), ex, -3);
        }
    }
    
    @Override
    public void commit(final Xid xid, final boolean onePhase) throws XAException {
        if (this.logger.logDebug()) {
            this.debug("committing xid = " + xid + (onePhase ? " (one phase) " : " (two phase)"));
        }
        if (xid == null) {
            throw new PGXAException(GT.tr("xid must not be null"), -5);
        }
        if (onePhase) {
            this.commitOnePhase(xid);
        }
        else {
            this.commitPrepared(xid);
        }
    }
    
    private void commitOnePhase(final Xid xid) throws XAException {
        try {
            if (this.currentXid == null || !this.currentXid.equals(xid)) {
                throw new PGXAException(GT.tr("Not implemented: one-phase commit must be issued using the same connection that was used to start it"), -3);
            }
            if (this.state != 2) {
                throw new PGXAException(GT.tr("commit called before end"), -6);
            }
            this.state = 0;
            this.currentXid = null;
            this.conn.commit();
            this.conn.setAutoCommit(this.localAutoCommitMode);
        }
        catch (SQLException ex) {
            throw new PGXAException(GT.tr("Error during one-phase commit"), ex, -3);
        }
    }
    
    private void commitPrepared(final Xid xid) throws XAException {
        try {
            if (this.state != 0 || this.conn.getTransactionState() != 0) {
                throw new PGXAException(GT.tr("Not implemented: 2nd phase commit must be issued using an idle connection"), -3);
            }
            final String s = RecoveredXid.xidToString(xid);
            this.localAutoCommitMode = this.conn.getAutoCommit();
            this.conn.setAutoCommit(true);
            final Statement stmt = this.conn.createStatement();
            try {
                stmt.executeUpdate("COMMIT PREPARED '" + s + "'");
            }
            finally {
                stmt.close();
                this.conn.setAutoCommit(this.localAutoCommitMode);
            }
        }
        catch (SQLException ex) {
            throw new PGXAException(GT.tr("Error committing prepared transaction"), ex, -3);
        }
    }
    
    @Override
    public boolean isSameRM(final XAResource xares) throws XAException {
        return xares == this;
    }
    
    @Override
    public void forget(final Xid xid) throws XAException {
        throw new PGXAException(GT.tr("Heuristic commit/rollback not supported"), -4);
    }
    
    @Override
    public int getTransactionTimeout() {
        return 0;
    }
    
    @Override
    public boolean setTransactionTimeout(final int seconds) {
        return false;
    }
    
    private class ConnectionHandler implements InvocationHandler
    {
        private Connection con;
        
        public ConnectionHandler(final Connection con) {
            this.con = con;
        }
        
        @Override
        public Object invoke(final Object proxy, final Method method, Object[] args) throws Throwable {
            if (PGXAConnection.this.state != 0) {
                final String methodName = method.getName();
                if (methodName.equals("commit") || methodName.equals("rollback") || methodName.equals("setSavePoint") || (methodName.equals("setAutoCommit") && (boolean)args[0])) {
                    throw new PSQLException(GT.tr("Transaction control methods setAutoCommit(true), commit, rollback and setSavePoint not allowed while an XA transaction is active."), PSQLState.OBJECT_NOT_IN_STATE);
                }
            }
            try {
                if (method.getName().equals("equals")) {
                    final Object arg = args[0];
                    if (Proxy.isProxyClass(arg.getClass())) {
                        final InvocationHandler h = Proxy.getInvocationHandler(arg);
                        if (h instanceof ConnectionHandler) {
                            args = new Object[] { ((ConnectionHandler)h).con };
                        }
                    }
                }
                return method.invoke(this.con, args);
            }
            catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }
}
