// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ds.jdbc23;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import org.postgresql.ds.PGPooledConnection;
import javax.sql.PooledConnection;
import org.postgresql.Driver;
import java.io.Serializable;
import org.postgresql.ds.common.BaseDataSource;

public class AbstractJdbc23ConnectionPoolDataSource extends BaseDataSource implements Serializable
{
    private boolean defaultAutoCommit;
    
    public AbstractJdbc23ConnectionPoolDataSource() {
        this.defaultAutoCommit = true;
    }
    
    @Override
    public String getDescription() {
        return "ConnectionPoolDataSource from " + Driver.getVersion();
    }
    
    public PooledConnection getPooledConnection() throws SQLException {
        return new PGPooledConnection(this.getConnection(), this.defaultAutoCommit);
    }
    
    public PooledConnection getPooledConnection(final String user, final String password) throws SQLException {
        return new PGPooledConnection(this.getConnection(user, password), this.defaultAutoCommit);
    }
    
    public boolean isDefaultAutoCommit() {
        return this.defaultAutoCommit;
    }
    
    public void setDefaultAutoCommit(final boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        this.writeBaseObject(out);
        out.writeBoolean(this.defaultAutoCommit);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.readBaseObject(in);
        this.defaultAutoCommit = in.readBoolean();
    }
}
