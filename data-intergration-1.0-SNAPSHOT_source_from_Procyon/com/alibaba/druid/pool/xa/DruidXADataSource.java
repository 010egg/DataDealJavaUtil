// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.xa;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.PGUtils;
import com.alibaba.druid.util.MySqlUtils;
import javax.transaction.xa.XAException;
import com.alibaba.druid.util.OracleUtils;
import com.alibaba.druid.util.H2Utils;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.DbType;
import java.sql.SQLException;
import com.alibaba.druid.pool.DruidPooledConnection;
import java.sql.Connection;
import javax.sql.XAConnection;
import com.alibaba.druid.support.logging.Log;
import javax.sql.XADataSource;
import com.alibaba.druid.pool.DruidDataSource;

public class DruidXADataSource extends DruidDataSource implements XADataSource
{
    private static final Log LOG;
    private static final long serialVersionUID = 1L;
    private Object h2Factory;
    
    public DruidXADataSource() {
        this.h2Factory = null;
    }
    
    @Override
    public XAConnection getXAConnection() throws SQLException {
        final DruidPooledConnection conn = this.getConnection();
        final Connection physicalConn = conn.unwrap(Connection.class);
        final XAConnection rawXAConnection = this.createPhysicalXAConnection(physicalConn);
        return new DruidPooledXAConnection(conn, rawXAConnection);
    }
    
    @Override
    protected void initCheck() throws SQLException {
        super.initCheck();
        final DbType dbType = DbType.of(this.dbTypeName);
        if (JdbcUtils.H2.equals(dbType)) {
            this.h2Factory = H2Utils.createJdbcDataSourceFactory();
        }
    }
    
    private XAConnection createPhysicalXAConnection(final Connection physicalConn) throws SQLException {
        final DbType dbType = DbType.of(this.dbTypeName);
        if (dbType == null) {
            throw new SQLException("xa not support dbType : " + this.dbTypeName);
        }
        switch (dbType) {
            case oracle: {
                try {
                    return OracleUtils.OracleXAConnection(physicalConn);
                }
                catch (XAException xae) {
                    DruidXADataSource.LOG.error("create xaConnection error", xae);
                    return null;
                }
            }
            case mysql:
            case mariadb: {
                return MySqlUtils.createXAConnection(this.driver, physicalConn);
            }
            case postgresql: {
                return PGUtils.createXAConnection(physicalConn);
            }
            case h2: {
                return H2Utils.createXAConnection(this.h2Factory, physicalConn);
            }
            case jtds: {
                return new JtdsXAConnection(physicalConn);
            }
            default: {
                throw new SQLException("xa not support dbType : " + this.dbTypeName);
            }
        }
    }
    
    @Override
    public XAConnection getXAConnection(final String user, final String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported by DruidDataSource");
    }
    
    static {
        LOG = LogFactory.getLog(DruidXADataSource.class);
    }
}
