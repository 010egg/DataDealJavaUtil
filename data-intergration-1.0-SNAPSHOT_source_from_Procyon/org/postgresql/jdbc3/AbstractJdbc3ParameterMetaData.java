// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc3;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.SQLException;
import org.postgresql.core.BaseConnection;

public abstract class AbstractJdbc3ParameterMetaData
{
    private final BaseConnection _connection;
    private final int[] _oids;
    
    public AbstractJdbc3ParameterMetaData(final BaseConnection connection, final int[] oids) {
        this._connection = connection;
        this._oids = oids;
    }
    
    public String getParameterClassName(final int param) throws SQLException {
        this.checkParamIndex(param);
        return this._connection.getTypeInfo().getJavaClass(this._oids[param - 1]);
    }
    
    public int getParameterCount() {
        return this._oids.length;
    }
    
    public int getParameterMode(final int param) throws SQLException {
        this.checkParamIndex(param);
        return 1;
    }
    
    public int getParameterType(final int param) throws SQLException {
        this.checkParamIndex(param);
        return this._connection.getTypeInfo().getSQLType(this._oids[param - 1]);
    }
    
    public String getParameterTypeName(final int param) throws SQLException {
        this.checkParamIndex(param);
        return this._connection.getTypeInfo().getPGType(this._oids[param - 1]);
    }
    
    public int getPrecision(final int param) throws SQLException {
        this.checkParamIndex(param);
        return 0;
    }
    
    public int getScale(final int param) throws SQLException {
        this.checkParamIndex(param);
        return 0;
    }
    
    public int isNullable(final int param) throws SQLException {
        this.checkParamIndex(param);
        return 2;
    }
    
    public boolean isSigned(final int param) throws SQLException {
        this.checkParamIndex(param);
        return this._connection.getTypeInfo().isSigned(this._oids[param - 1]);
    }
    
    private void checkParamIndex(final int param) throws PSQLException {
        if (param < 1 || param > this._oids.length) {
            throw new PSQLException(GT.tr("The parameter index is out of range: {0}, number of parameters: {1}.", new Object[] { new Integer(param), new Integer(this._oids.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
    }
}
