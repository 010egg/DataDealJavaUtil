// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import org.postgresql.core.v3.ConnectionFactoryImpl;
import java.io.IOException;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.PGProperty;
import java.util.Properties;
import org.postgresql.util.HostSpec;

public abstract class ConnectionFactory
{
    private static final Object[][] versions;
    
    public static ProtocolConnection openConnection(final HostSpec[] hostSpecs, final String user, final String database, final Properties info, final Logger logger) throws SQLException {
        final String protoName = PGProperty.PROTOCOL_VERSION.get(info);
        for (int i = 0; i < ConnectionFactory.versions.length; ++i) {
            final String versionProtoName = (String)ConnectionFactory.versions[i][0];
            if (protoName == null || protoName.equals(versionProtoName)) {
                final ConnectionFactory factory = (ConnectionFactory)ConnectionFactory.versions[i][1];
                final ProtocolConnection connection = factory.openConnectionImpl(hostSpecs, user, database, info, logger);
                if (connection != null) {
                    return connection;
                }
            }
        }
        throw new PSQLException(GT.tr("A connection could not be made using the requested protocol {0}.", protoName), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
    }
    
    public abstract ProtocolConnection openConnectionImpl(final HostSpec[] p0, final String p1, final String p2, final Properties p3, final Logger p4) throws SQLException;
    
    protected void closeStream(final PGStream newStream) {
        if (newStream != null) {
            try {
                newStream.close();
            }
            catch (IOException ex) {}
        }
    }
    
    static {
        versions = new Object[][] { { "3", new ConnectionFactoryImpl() }, { "2", new org.postgresql.core.v2.ConnectionFactoryImpl() } };
    }
}
