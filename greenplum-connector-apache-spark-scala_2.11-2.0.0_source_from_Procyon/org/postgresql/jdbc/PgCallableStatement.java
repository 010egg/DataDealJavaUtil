// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc;

import java.net.URL;
import java.sql.SQLXML;
import java.sql.NClob;
import java.io.InputStream;
import java.io.Reader;
import java.sql.RowId;
import java.sql.SQLType;
import java.util.Calendar;
import java.sql.Ref;
import java.sql.Clob;
import org.postgresql.Driver;
import java.sql.Blob;
import java.sql.Array;
import org.postgresql.core.ParameterList;
import org.postgresql.core.Query;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;
import java.sql.ResultSet;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.util.Map;
import java.sql.SQLException;
import java.sql.CallableStatement;

class PgCallableStatement extends PgPreparedStatement implements CallableStatement
{
    private boolean isFunction;
    private int[] functionReturnType;
    private int[] testReturn;
    private boolean returnTypeSet;
    protected Object[] callResult;
    private int lastIndex;
    
    PgCallableStatement(final PgConnection connection, final String sql, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(connection, connection.borrowCallableQuery(sql), rsType, rsConcurrency, rsHoldability);
        this.lastIndex = 0;
        this.isFunction = this.preparedQuery.isFunction;
        if (this.isFunction) {
            final int inParamCount = this.preparedParameters.getInParameterCount() + 1;
            this.testReturn = new int[inParamCount];
            this.functionReturnType = new int[inParamCount];
        }
    }
    
    @Override
    public int executeUpdate() throws SQLException {
        if (this.isFunction) {
            this.executeWithFlags(0);
            return 0;
        }
        return super.executeUpdate();
    }
    
    @Override
    public Object getObject(final int i, final Map<String, Class<?>> map) throws SQLException {
        return this.getObjectImpl(i, map);
    }
    
    @Override
    public Object getObject(final String s, final Map<String, Class<?>> map) throws SQLException {
        return this.getObjectImpl(s, map);
    }
    
    @Override
    public boolean executeWithFlags(final int flags) throws SQLException {
        final boolean hasResultSet = super.executeWithFlags(flags);
        if (!this.isFunction || !this.returnTypeSet) {
            return hasResultSet;
        }
        if (!hasResultSet) {
            throw new PSQLException(GT.tr("A CallableStatement was executed with nothing returned.", new Object[0]), PSQLState.NO_DATA);
        }
        final ResultSet rs;
        synchronized (this) {
            this.checkClosed();
            rs = this.result.getResultSet();
        }
        if (!rs.next()) {
            throw new PSQLException(GT.tr("A CallableStatement was executed with nothing returned.", new Object[0]), PSQLState.NO_DATA);
        }
        final int cols = rs.getMetaData().getColumnCount();
        final int outParameterCount = this.preparedParameters.getOutParameterCount();
        if (cols != outParameterCount) {
            throw new PSQLException(GT.tr("A CallableStatement was executed with an invalid number of parameters", new Object[0]), PSQLState.SYNTAX_ERROR);
        }
        this.lastIndex = 0;
        this.callResult = new Object[this.preparedParameters.getParameterCount() + 1];
        for (int i = 0, j = 0; i < cols; ++i, ++j) {
            while (j < this.functionReturnType.length && this.functionReturnType[j] == 0) {
                ++j;
            }
            this.callResult[j] = rs.getObject(i + 1);
            final int columnType = rs.getMetaData().getColumnType(i + 1);
            if (columnType != this.functionReturnType[j]) {
                if (columnType == 8 && this.functionReturnType[j] == 7) {
                    if (this.callResult[j] != null) {
                        this.callResult[j] = ((Double)this.callResult[j]).floatValue();
                    }
                }
                else if (columnType != 2012 || this.functionReturnType[j] != 1111) {
                    throw new PSQLException(GT.tr("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", i + 1, "java.sql.Types=" + columnType, "java.sql.Types=" + this.functionReturnType[j]), PSQLState.DATA_TYPE_MISMATCH);
                }
            }
        }
        rs.close();
        synchronized (this) {
            this.result = null;
        }
        return false;
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, int sqlType) throws SQLException {
        this.checkClosed();
        switch (sqlType) {
            case -6: {
                sqlType = 5;
                break;
            }
            case -1: {
                sqlType = 12;
                break;
            }
            case 3: {
                sqlType = 2;
                break;
            }
            case 6: {
                sqlType = 8;
                break;
            }
            case -4:
            case -3: {
                sqlType = -2;
                break;
            }
            case 16: {
                sqlType = -7;
                break;
            }
        }
        if (!this.isFunction) {
            throw new PSQLException(GT.tr("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one.", new Object[0]), PSQLState.STATEMENT_NOT_ALLOWED_IN_FUNCTION_CALL);
        }
        this.checkIndex(parameterIndex, false);
        this.preparedParameters.registerOutParameter(parameterIndex, sqlType);
        this.functionReturnType[parameterIndex - 1] = sqlType;
        this.testReturn[parameterIndex - 1] = sqlType;
        if (this.functionReturnType[parameterIndex - 1] == 1 || this.functionReturnType[parameterIndex - 1] == -1) {
            this.testReturn[parameterIndex - 1] = 12;
        }
        else if (this.functionReturnType[parameterIndex - 1] == 6) {
            this.testReturn[parameterIndex - 1] = 7;
        }
        this.returnTypeSet = true;
    }
    
    @Override
    public boolean wasNull() throws SQLException {
        if (this.lastIndex == 0) {
            throw new PSQLException(GT.tr("wasNull cannot be call before fetching a result.", new Object[0]), PSQLState.OBJECT_NOT_IN_STATE);
        }
        return this.callResult[this.lastIndex - 1] == null;
    }
    
    @Override
    public String getString(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 12, "String");
        return (String)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public boolean getBoolean(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, -7, "Boolean");
        return this.callResult[parameterIndex - 1] != null && (boolean)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public byte getByte(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 5, "Byte");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0;
        }
        return ((Integer)this.callResult[parameterIndex - 1]).byteValue();
    }
    
    @Override
    public short getShort(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 5, "Short");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0;
        }
        return ((Integer)this.callResult[parameterIndex - 1]).shortValue();
    }
    
    @Override
    public int getInt(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 4, "Int");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0;
        }
        return (int)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public long getLong(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, -5, "Long");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0L;
        }
        return (long)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public float getFloat(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 7, "Float");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0.0f;
        }
        return (float)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public double getDouble(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 8, "Double");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0.0;
        }
        return (double)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 2, "BigDecimal");
        return (BigDecimal)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public byte[] getBytes(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, -3, -2, "Bytes");
        return (byte[])this.callResult[parameterIndex - 1];
    }
    
    @Override
    public Date getDate(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 91, "Date");
        return (Date)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public Time getTime(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 92, "Time");
        return (Time)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 93, "Timestamp");
        return (Timestamp)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public Object getObject(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex);
        return this.callResult[parameterIndex - 1];
    }
    
    protected void checkIndex(final int parameterIndex, final int type1, final int type2, final String getName) throws SQLException {
        this.checkIndex(parameterIndex);
        if (type1 != this.testReturn[parameterIndex - 1] && type2 != this.testReturn[parameterIndex - 1]) {
            throw new PSQLException(GT.tr("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "java.sql.Types=" + this.testReturn[parameterIndex - 1], getName, "java.sql.Types=" + type1), PSQLState.MOST_SPECIFIC_TYPE_DOES_NOT_MATCH);
        }
    }
    
    protected void checkIndex(final int parameterIndex, final int type, final String getName) throws SQLException {
        this.checkIndex(parameterIndex);
        if (type != this.testReturn[parameterIndex - 1]) {
            throw new PSQLException(GT.tr("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "java.sql.Types=" + this.testReturn[parameterIndex - 1], getName, "java.sql.Types=" + type), PSQLState.MOST_SPECIFIC_TYPE_DOES_NOT_MATCH);
        }
    }
    
    private void checkIndex(final int parameterIndex) throws SQLException {
        this.checkIndex(parameterIndex, true);
    }
    
    private void checkIndex(final int parameterIndex, final boolean fetchingData) throws SQLException {
        if (!this.isFunction) {
            throw new PSQLException(GT.tr("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.", new Object[0]), PSQLState.STATEMENT_NOT_ALLOWED_IN_FUNCTION_CALL);
        }
        if (fetchingData) {
            if (!this.returnTypeSet) {
                throw new PSQLException(GT.tr("No function outputs were registered.", new Object[0]), PSQLState.OBJECT_NOT_IN_STATE);
            }
            if (this.callResult == null) {
                throw new PSQLException(GT.tr("Results cannot be retrieved from a CallableStatement before it is executed.", new Object[0]), PSQLState.NO_DATA);
            }
            this.lastIndex = parameterIndex;
        }
    }
    
    @Override
    protected BatchResultHandler createBatchHandler(final Query[] queries, final ParameterList[] parameterLists) {
        return new CallableBatchResultHandler(this, queries, parameterLists);
    }
    
    @Override
    public Array getArray(final int i) throws SQLException {
        this.checkClosed();
        this.checkIndex(i, 2003, "Array");
        return (Array)this.callResult[i - 1];
    }
    
    @Override
    public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 2, "BigDecimal");
        return (BigDecimal)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public Blob getBlob(final int i) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getBlob(int)");
    }
    
    @Override
    public Clob getClob(final int i) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getClob(int)");
    }
    
    public Object getObjectImpl(final int i, final Map<String, Class<?>> map) throws SQLException {
        if (map == null || map.isEmpty()) {
            return this.getObject(i);
        }
        throw Driver.notImplemented(this.getClass(), "getObjectImpl(int,Map)");
    }
    
    @Override
    public Ref getRef(final int i) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getRef(int)");
    }
    
    @Override
    public Date getDate(final int i, final Calendar cal) throws SQLException {
        this.checkClosed();
        this.checkIndex(i, 91, "Date");
        if (this.callResult[i - 1] == null) {
            return null;
        }
        final String value = this.callResult[i - 1].toString();
        return this.connection.getTimestampUtils().toDate(cal, value);
    }
    
    @Override
    public Time getTime(final int i, final Calendar cal) throws SQLException {
        this.checkClosed();
        this.checkIndex(i, 92, "Time");
        if (this.callResult[i - 1] == null) {
            return null;
        }
        final String value = this.callResult[i - 1].toString();
        return this.connection.getTimestampUtils().toTime(cal, value);
    }
    
    @Override
    public Timestamp getTimestamp(final int i, final Calendar cal) throws SQLException {
        this.checkClosed();
        this.checkIndex(i, 93, "Timestamp");
        if (this.callResult[i - 1] == null) {
            return null;
        }
        final String value = this.callResult[i - 1].toString();
        return this.connection.getTimestampUtils().toTimestamp(cal, value);
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter(int,int,String)");
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final SQLType targetSqlType, final int scaleOrLength) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setObject");
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final SQLType targetSqlType) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setObject");
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final SQLType sqlType) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter");
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final SQLType sqlType, final int scale) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter");
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final SQLType sqlType, final String typeName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter");
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final SQLType sqlType) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter");
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final SQLType sqlType, final int scale) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter");
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final SQLType sqlType, final String typeName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter");
    }
    
    @Override
    public RowId getRowId(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getRowId(int)");
    }
    
    @Override
    public RowId getRowId(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getRowId(String)");
    }
    
    @Override
    public void setRowId(final String parameterName, final RowId x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setRowId(String, RowId)");
    }
    
    @Override
    public void setNString(final String parameterName, final String value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNString(String, String)");
    }
    
    @Override
    public void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNCharacterStream(String, Reader, long)");
    }
    
    @Override
    public void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNCharacterStream(String, Reader)");
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setCharacterStream(String, Reader, long)");
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setCharacterStream(String, Reader)");
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBinaryStream(String, InputStream, long)");
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBinaryStream(String, InputStream)");
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setAsciiStream(String, InputStream, long)");
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setAsciiStream(String, InputStream)");
    }
    
    @Override
    public void setNClob(final String parameterName, final NClob value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNClob(String, NClob)");
    }
    
    @Override
    public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setClob(String, Reader, long)");
    }
    
    @Override
    public void setClob(final String parameterName, final Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setClob(String, Reader)");
    }
    
    @Override
    public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBlob(String, InputStream, long)");
    }
    
    @Override
    public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBlob(String, InputStream)");
    }
    
    @Override
    public void setBlob(final String parameterName, final Blob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBlob(String, Blob)");
    }
    
    @Override
    public void setClob(final String parameterName, final Clob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setClob(String, Clob)");
    }
    
    @Override
    public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNClob(String, Reader, long)");
    }
    
    @Override
    public void setNClob(final String parameterName, final Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNClob(String, Reader)");
    }
    
    @Override
    public NClob getNClob(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNClob(int)");
    }
    
    @Override
    public NClob getNClob(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNClob(String)");
    }
    
    @Override
    public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setSQLXML(String, SQLXML)");
    }
    
    @Override
    public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 2009, "SQLXML");
        return (SQLXML)this.callResult[parameterIndex - 1];
    }
    
    @Override
    public SQLXML getSQLXML(final String parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getSQLXML(String)");
    }
    
    @Override
    public String getNString(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNString(int)");
    }
    
    @Override
    public String getNString(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNString(String)");
    }
    
    @Override
    public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNCharacterStream(int)");
    }
    
    @Override
    public Reader getNCharacterStream(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNCharacterStream(String)");
    }
    
    @Override
    public Reader getCharacterStream(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getCharacterStream(int)");
    }
    
    @Override
    public Reader getCharacterStream(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getCharacterStream(String)");
    }
    
    @Override
    public <T> T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
        if (type == ResultSet.class) {
            return type.cast(this.getObject(parameterIndex));
        }
        throw new PSQLException(GT.tr("Unsupported type conversion to {1}.", type), PSQLState.INVALID_PARAMETER_VALUE);
    }
    
    @Override
    public <T> T getObject(final String parameterName, final Class<T> type) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getObject(String, Class<T>)");
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter(String,int)");
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter(String,int,int)");
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter(String,int,String)");
    }
    
    @Override
    public URL getURL(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getURL(String)");
    }
    
    @Override
    public void setURL(final String parameterName, final URL val) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setURL(String,URL)");
    }
    
    @Override
    public void setNull(final String parameterName, final int sqlType) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNull(String,int)");
    }
    
    @Override
    public void setBoolean(final String parameterName, final boolean x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBoolean(String,boolean)");
    }
    
    @Override
    public void setByte(final String parameterName, final byte x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setByte(String,byte)");
    }
    
    @Override
    public void setShort(final String parameterName, final short x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setShort(String,short)");
    }
    
    @Override
    public void setInt(final String parameterName, final int x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setInt(String,int)");
    }
    
    @Override
    public void setLong(final String parameterName, final long x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setLong(String,long)");
    }
    
    @Override
    public void setFloat(final String parameterName, final float x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setFloat(String,float)");
    }
    
    @Override
    public void setDouble(final String parameterName, final double x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setDouble(String,double)");
    }
    
    @Override
    public void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBigDecimal(String,BigDecimal)");
    }
    
    @Override
    public void setString(final String parameterName, final String x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setString(String,String)");
    }
    
    @Override
    public void setBytes(final String parameterName, final byte[] x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBytes(String,byte)");
    }
    
    @Override
    public void setDate(final String parameterName, final Date x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setDate(String,Date)");
    }
    
    @Override
    public void setTime(final String parameterName, final Time x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setTime(String,Time)");
    }
    
    @Override
    public void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setTimestamp(String,Timestamp)");
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setAsciiStream(String,InputStream,int)");
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBinaryStream(String,InputStream,int)");
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setObject(String,Object,int,int)");
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setObject(String,Object,int)");
    }
    
    @Override
    public void setObject(final String parameterName, final Object x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setObject(String,Object)");
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setCharacterStream(String,Reader,int)");
    }
    
    @Override
    public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setDate(String,Date,Calendar)");
    }
    
    @Override
    public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setTime(String,Time,Calendar)");
    }
    
    @Override
    public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setTimestamp(String,Timestamp,Calendar)");
    }
    
    @Override
    public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNull(String,int,String)");
    }
    
    @Override
    public String getString(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getString(String)");
    }
    
    @Override
    public boolean getBoolean(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getBoolean(String)");
    }
    
    @Override
    public byte getByte(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getByte(String)");
    }
    
    @Override
    public short getShort(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getShort(String)");
    }
    
    @Override
    public int getInt(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getInt(String)");
    }
    
    @Override
    public long getLong(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getLong(String)");
    }
    
    @Override
    public float getFloat(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getFloat(String)");
    }
    
    @Override
    public double getDouble(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getDouble(String)");
    }
    
    @Override
    public byte[] getBytes(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getBytes(String)");
    }
    
    @Override
    public Date getDate(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getDate(String)");
    }
    
    @Override
    public Time getTime(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getTime(String)");
    }
    
    @Override
    public Timestamp getTimestamp(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getTimestamp(String)");
    }
    
    @Override
    public Object getObject(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getObject(String)");
    }
    
    @Override
    public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getBigDecimal(String)");
    }
    
    public Object getObjectImpl(final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getObject(String,Map)");
    }
    
    @Override
    public Ref getRef(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getRef(String)");
    }
    
    @Override
    public Blob getBlob(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getBlob(String)");
    }
    
    @Override
    public Clob getClob(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getClob(String)");
    }
    
    @Override
    public Array getArray(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getArray(String)");
    }
    
    @Override
    public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getDate(String,Calendar)");
    }
    
    @Override
    public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getTime(String,Calendar)");
    }
    
    @Override
    public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getTimestamp(String,Calendar)");
    }
    
    @Override
    public URL getURL(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getURL(String)");
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
        this.registerOutParameter(parameterIndex, sqlType);
    }
}
