// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc3;

import org.postgresql.core.BaseConnection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Savepoint;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.SQLException;
import java.util.Properties;
import org.postgresql.util.HostSpec;
import org.postgresql.jdbc2.AbstractJdbc2Connection;

public abstract class AbstractJdbc3Connection extends AbstractJdbc2Connection
{
    private int rsHoldability;
    private int savepointId;
    
    protected AbstractJdbc3Connection(final HostSpec[] hostSpec, final String user, final String database, final Properties info, final String url) throws SQLException {
        super(hostSpec, user, database, info, url);
        this.rsHoldability = 2;
        this.savepointId = 0;
    }
    
    @Override
    public void setHoldability(final int holdability) throws SQLException {
        this.checkClosed();
        switch (holdability) {
            case 2: {
                this.rsHoldability = holdability;
                break;
            }
            case 1: {
                this.rsHoldability = holdability;
                break;
            }
            default: {
                throw new PSQLException(GT.tr("Unknown ResultSet holdability setting: {0}.", new Integer(holdability)), PSQLState.INVALID_PARAMETER_VALUE);
            }
        }
    }
    
    @Override
    public int getHoldability() throws SQLException {
        this.checkClosed();
        return this.rsHoldability;
    }
    
    @Override
    public Savepoint setSavepoint() throws SQLException {
        this.checkClosed();
        if (!this.haveMinimumServerVersion("8.0")) {
            throw new PSQLException(GT.tr("Server versions prior to 8.0 do not support savepoints."), PSQLState.NOT_IMPLEMENTED);
        }
        if (this.getAutoCommit()) {
            throw new PSQLException(GT.tr("Cannot establish a savepoint in auto-commit mode."), PSQLState.NO_ACTIVE_SQL_TRANSACTION);
        }
        final PSQLSavepoint savepoint = new PSQLSavepoint(this.savepointId++);
        final String pgName = savepoint.getPGName();
        final Statement stmt = this.createStatement();
        stmt.executeUpdate("SAVEPOINT " + pgName);
        stmt.close();
        return savepoint;
    }
    
    @Override
    public Savepoint setSavepoint(final String name) throws SQLException {
        this.checkClosed();
        if (!this.haveMinimumServerVersion("8.0")) {
            throw new PSQLException(GT.tr("Server versions prior to 8.0 do not support savepoints."), PSQLState.NOT_IMPLEMENTED);
        }
        if (this.getAutoCommit()) {
            throw new PSQLException(GT.tr("Cannot establish a savepoint in auto-commit mode."), PSQLState.NO_ACTIVE_SQL_TRANSACTION);
        }
        final PSQLSavepoint savepoint = new PSQLSavepoint(name);
        final Statement stmt = this.createStatement();
        stmt.executeUpdate("SAVEPOINT " + savepoint.getPGName());
        stmt.close();
        return savepoint;
    }
    
    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        this.checkClosed();
        if (!this.haveMinimumServerVersion("8.0")) {
            throw new PSQLException(GT.tr("Server versions prior to 8.0 do not support savepoints."), PSQLState.NOT_IMPLEMENTED);
        }
        final PSQLSavepoint pgSavepoint = (PSQLSavepoint)savepoint;
        this.execSQLUpdate("ROLLBACK TO SAVEPOINT " + pgSavepoint.getPGName());
    }
    
    @Override
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        this.checkClosed();
        if (!this.haveMinimumServerVersion("8.0")) {
            throw new PSQLException(GT.tr("Server versions prior to 8.0 do not support savepoints."), PSQLState.NOT_IMPLEMENTED);
        }
        final PSQLSavepoint pgSavepoint = (PSQLSavepoint)savepoint;
        this.execSQLUpdate("RELEASE SAVEPOINT " + pgSavepoint.getPGName());
        pgSavepoint.invalidate();
    }
    
    @Override
    public abstract Statement createStatement(final int p0, final int p1, final int p2) throws SQLException;
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
        this.checkClosed();
        return this.createStatement(resultSetType, resultSetConcurrency, this.getHoldability());
    }
    
    @Override
    public abstract PreparedStatement prepareStatement(final String p0, final int p1, final int p2, final int p3) throws SQLException;
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        this.checkClosed();
        return this.prepareStatement(sql, resultSetType, resultSetConcurrency, this.getHoldability());
    }
    
    @Override
    public abstract CallableStatement prepareCall(final String p0, final int p1, final int p2, final int p3) throws SQLException;
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        this.checkClosed();
        return this.prepareCall(sql, resultSetType, resultSetConcurrency, this.getHoldability());
    }
    
    @Override
    public PreparedStatement prepareStatement(String sql, final int autoGeneratedKeys) throws SQLException {
        this.checkClosed();
        if (autoGeneratedKeys != 2) {
            sql = AbstractJdbc3Statement.addReturning(this, sql, new String[] { "*" }, false);
        }
        final PreparedStatement ps = this.prepareStatement(sql);
        if (autoGeneratedKeys != 2) {
            ((AbstractJdbc3Statement)ps).wantsGeneratedKeysAlways = true;
        }
        return ps;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        if (columnIndexes == null || columnIndexes.length == 0) {
            return this.prepareStatement(sql);
        }
        this.checkClosed();
        throw new PSQLException(GT.tr("Returning autogenerated keys is not supported."), PSQLState.NOT_IMPLEMENTED);
    }
    
    @Override
    public PreparedStatement prepareStatement(String sql, final String[] columnNames) throws SQLException {
        if (columnNames != null && columnNames.length != 0) {
            sql = AbstractJdbc3Statement.addReturning(this, sql, columnNames, true);
        }
        final PreparedStatement ps = this.prepareStatement(sql);
        if (columnNames != null && columnNames.length != 0) {
            ((AbstractJdbc3Statement)ps).wantsGeneratedKeysAlways = true;
        }
        return ps;
    }
}
