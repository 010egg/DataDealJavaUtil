// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.xa.jdbc3;

import org.postgresql.xa.PGXADataSourceFactory;
import javax.naming.Reference;
import org.postgresql.Driver;
import java.sql.Connection;
import org.postgresql.xa.PGXAConnection;
import org.postgresql.core.BaseConnection;
import java.sql.SQLException;
import javax.sql.XAConnection;
import javax.naming.Referenceable;
import org.postgresql.ds.common.BaseDataSource;

public class AbstractJdbc3XADataSource extends BaseDataSource implements Referenceable
{
    public XAConnection getXAConnection() throws SQLException {
        return this.getXAConnection(this.getUser(), this.getPassword());
    }
    
    public XAConnection getXAConnection(final String user, final String password) throws SQLException {
        final Connection con = super.getConnection(user, password);
        return new PGXAConnection((BaseConnection)con);
    }
    
    @Override
    public String getDescription() {
        return "JDBC3 XA-enabled DataSource from " + Driver.getVersion();
    }
    
    @Override
    protected Reference createReference() {
        return new Reference(this.getClass().getName(), PGXADataSourceFactory.class.getName(), null);
    }
}
