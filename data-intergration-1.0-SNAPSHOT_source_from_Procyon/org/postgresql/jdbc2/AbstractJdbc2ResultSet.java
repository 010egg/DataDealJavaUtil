// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import java.util.Collection;
import org.postgresql.util.PGtokenizer;
import java.util.Locale;
import java.io.ByteArrayInputStream;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.postgresql.util.ByteConverter;
import org.postgresql.util.PGbytea;
import org.postgresql.util.PGobject;
import java.util.StringTokenizer;
import org.postgresql.core.ParameterList;
import java.util.ArrayList;
import org.postgresql.PGResultSetMetaData;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.postgresql.core.ResultHandler;
import java.sql.Ref;
import java.util.Map;
import java.sql.Timestamp;
import java.sql.Time;
import java.util.TimeZone;
import org.postgresql.core.Oid;
import java.sql.Date;
import java.sql.Clob;
import java.io.InputStream;
import org.postgresql.core.Encoding;
import java.io.IOException;
import java.io.CharArrayReader;
import java.io.Reader;
import java.sql.Blob;
import java.math.BigDecimal;
import java.sql.Array;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.ResultSet;
import org.postgresql.util.HStoreConverter;
import org.postgresql.core.Utils;
import java.util.Calendar;
import org.postgresql.Driver;
import java.net.URL;
import java.sql.SQLException;
import java.math.BigInteger;
import java.sql.ResultSetMetaData;
import org.postgresql.core.ResultCursor;
import java.sql.SQLWarning;
import org.postgresql.core.Query;
import org.postgresql.core.Field;
import java.sql.Statement;
import org.postgresql.core.BaseStatement;
import org.postgresql.core.BaseConnection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.HashMap;
import org.postgresql.PGRefCursorResultSet;
import org.postgresql.core.BaseResultSet;

public abstract class AbstractJdbc2ResultSet implements BaseResultSet, PGRefCursorResultSet
{
    private boolean updateable;
    private boolean doingUpdates;
    private HashMap updateValues;
    private boolean usingOID;
    private List primaryKeys;
    private boolean singleTable;
    private String onlyTable;
    private String tableName;
    private PreparedStatement updateStatement;
    private PreparedStatement insertStatement;
    private PreparedStatement deleteStatement;
    private PreparedStatement selectStatement;
    private final int resultsettype;
    private final int resultsetconcurrency;
    private int fetchdirection;
    protected final BaseConnection connection;
    protected final BaseStatement statement;
    private Statement realStatement;
    protected final Field[] fields;
    protected final Query originalQuery;
    protected final int maxRows;
    protected final int maxFieldSize;
    protected List rows;
    protected int current_row;
    protected int row_offset;
    protected byte[][] this_row;
    protected SQLWarning warnings;
    protected boolean wasNullFlag;
    protected boolean onInsertRow;
    private byte[][] rowBuffer;
    protected int fetchSize;
    protected ResultCursor cursor;
    private HashMap columnNameIndexMap;
    private ResultSetMetaData rsMetaData;
    private String refCursorName;
    private static final BigInteger BYTEMAX;
    private static final BigInteger BYTEMIN;
    private static final BigInteger SHORTMAX;
    private static final BigInteger SHORTMIN;
    private static final NumberFormatException FAST_NUMBER_FAILED;
    private static final BigInteger INTMAX;
    private static final BigInteger INTMIN;
    private static final BigInteger LONGMAX;
    private static final BigInteger LONGMIN;
    
    protected abstract ResultSetMetaData createMetaData() throws SQLException;
    
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        this.checkClosed();
        if (this.rsMetaData == null) {
            this.rsMetaData = this.createMetaData();
        }
        return this.rsMetaData;
    }
    
    public AbstractJdbc2ResultSet(final Query originalQuery, final BaseStatement statement, final Field[] fields, final List tuples, final ResultCursor cursor, final int maxRows, final int maxFieldSize, final int rsType, final int rsConcurrency) throws SQLException {
        this.updateable = false;
        this.doingUpdates = false;
        this.updateValues = null;
        this.usingOID = false;
        this.singleTable = false;
        this.onlyTable = "";
        this.tableName = null;
        this.updateStatement = null;
        this.insertStatement = null;
        this.deleteStatement = null;
        this.selectStatement = null;
        this.fetchdirection = 1002;
        this.current_row = -1;
        this.warnings = null;
        this.wasNullFlag = false;
        this.onInsertRow = false;
        this.rowBuffer = null;
        if (tuples == null) {
            throw new NullPointerException("tuples must be non-null");
        }
        if (fields == null) {
            throw new NullPointerException("fields must be non-null");
        }
        this.originalQuery = originalQuery;
        this.connection = (BaseConnection)statement.getConnection();
        this.statement = statement;
        this.fields = fields;
        this.rows = tuples;
        this.cursor = cursor;
        this.maxRows = maxRows;
        this.maxFieldSize = maxFieldSize;
        this.resultsettype = rsType;
        this.resultsetconcurrency = rsConcurrency;
    }
    
    @Override
    public URL getURL(final int columnIndex) throws SQLException {
        this.checkClosed();
        throw Driver.notImplemented(this.getClass(), "getURL(int)");
    }
    
    @Override
    public URL getURL(final String columnName) throws SQLException {
        return this.getURL(this.findColumn(columnName));
    }
    
    protected Object internalGetObject(final int columnIndex, final Field field) throws SQLException {
        switch (this.getSQLType(columnIndex)) {
            case -7: {
                return this.getBoolean(columnIndex) ? Boolean.TRUE : Boolean.FALSE;
            }
            case -6:
            case 4:
            case 5: {
                return new Integer(this.getInt(columnIndex));
            }
            case -5: {
                return new Long(this.getLong(columnIndex));
            }
            case 2:
            case 3: {
                return this.getBigDecimal(columnIndex, (field.getMod() == -1) ? -1 : (field.getMod() - 4 & 0xFFFF));
            }
            case 7: {
                return new Float(this.getFloat(columnIndex));
            }
            case 6:
            case 8: {
                return new Double(this.getDouble(columnIndex));
            }
            case -1:
            case 1:
            case 12: {
                return this.getString(columnIndex);
            }
            case 91: {
                return this.getDate(columnIndex);
            }
            case 92: {
                return this.getTime(columnIndex);
            }
            case 93: {
                return this.getTimestamp(columnIndex, null);
            }
            case -4:
            case -3:
            case -2: {
                return this.getBytes(columnIndex);
            }
            case 2003: {
                return this.getArray(columnIndex);
            }
            case 2005: {
                return this.getClob(columnIndex);
            }
            case 2004: {
                return this.getBlob(columnIndex);
            }
            default: {
                final String type = this.getPGType(columnIndex);
                if (type.equals("unknown")) {
                    return this.getString(columnIndex);
                }
                if (type.equals("uuid")) {
                    if (this.isBinary(columnIndex)) {
                        return this.getUUID(this.this_row[columnIndex - 1]);
                    }
                    return this.getUUID(this.getString(columnIndex));
                }
                else {
                    if (type.equals("refcursor")) {
                        final String cursorName = this.getString(columnIndex);
                        final StringBuilder sb = new StringBuilder("FETCH ALL IN ");
                        Utils.escapeIdentifier(sb, cursorName);
                        final ResultSet rs = this.connection.execSQLQuery(sb.toString(), this.resultsettype, 1007);
                        sb.setLength(0);
                        sb.append("CLOSE ");
                        Utils.escapeIdentifier(sb, cursorName);
                        this.connection.execSQLUpdate(sb.toString());
                        ((AbstractJdbc2ResultSet)rs).setRefCursor(cursorName);
                        return rs;
                    }
                    if (!"hstore".equals(type)) {
                        return null;
                    }
                    if (this.isBinary(columnIndex)) {
                        return HStoreConverter.fromBytes(this.this_row[columnIndex - 1], this.connection.getEncoding());
                    }
                    return HStoreConverter.fromString(this.getString(columnIndex));
                }
                break;
            }
        }
    }
    
    private void checkScrollable() throws SQLException {
        this.checkClosed();
        if (this.resultsettype == 1003) {
            throw new PSQLException(GT.tr("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY."), PSQLState.INVALID_CURSOR_STATE);
        }
    }
    
    @Override
    public boolean absolute(final int index) throws SQLException {
        this.checkScrollable();
        if (index == 0) {
            this.beforeFirst();
            return false;
        }
        final int rows_size = this.rows.size();
        int internalIndex;
        if (index < 0) {
            if (index < -rows_size) {
                this.beforeFirst();
                return false;
            }
            internalIndex = rows_size + index;
        }
        else {
            if (index > rows_size) {
                this.afterLast();
                return false;
            }
            internalIndex = index - 1;
        }
        this.current_row = internalIndex;
        this.initRowBuffer();
        this.onInsertRow = false;
        return true;
    }
    
    @Override
    public void afterLast() throws SQLException {
        this.checkScrollable();
        final int rows_size = this.rows.size();
        if (rows_size > 0) {
            this.current_row = rows_size;
        }
        this.onInsertRow = false;
        this.this_row = null;
        this.rowBuffer = null;
    }
    
    @Override
    public void beforeFirst() throws SQLException {
        this.checkScrollable();
        if (this.rows.size() > 0) {
            this.current_row = -1;
        }
        this.onInsertRow = false;
        this.this_row = null;
        this.rowBuffer = null;
    }
    
    @Override
    public boolean first() throws SQLException {
        this.checkScrollable();
        if (this.rows.size() <= 0) {
            return false;
        }
        this.current_row = 0;
        this.initRowBuffer();
        this.onInsertRow = false;
        return true;
    }
    
    @Override
    public Array getArray(final String colName) throws SQLException {
        return this.getArray(this.findColumn(colName));
    }
    
    protected abstract Array makeArray(final int p0, final byte[] p1) throws SQLException;
    
    protected abstract Array makeArray(final int p0, final String p1) throws SQLException;
    
    @Override
    public Array getArray(final int i) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        }
        final int oid = this.fields[i - 1].getOID();
        if (this.isBinary(i)) {
            return this.makeArray(oid, this.this_row[i - 1]);
        }
        return this.makeArray(oid, this.getFixedString(i));
    }
    
    @Override
    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
        return this.getBigDecimal(columnIndex, -1);
    }
    
    @Override
    public BigDecimal getBigDecimal(final String columnName) throws SQLException {
        return this.getBigDecimal(this.findColumn(columnName));
    }
    
    @Override
    public Blob getBlob(final String columnName) throws SQLException {
        return this.getBlob(this.findColumn(columnName));
    }
    
    protected abstract Blob makeBlob(final long p0) throws SQLException;
    
    @Override
    public Blob getBlob(final int i) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        }
        return this.makeBlob(this.getLong(i));
    }
    
    @Override
    public Reader getCharacterStream(final String columnName) throws SQLException {
        return this.getCharacterStream(this.findColumn(columnName));
    }
    
    @Override
    public Reader getCharacterStream(final int i) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        }
        if (((AbstractJdbc2Connection)this.connection).haveMinimumCompatibleVersion("7.2")) {
            return new CharArrayReader(this.getString(i).toCharArray());
        }
        final Encoding encoding = this.connection.getEncoding();
        final InputStream input = this.getBinaryStream(i);
        try {
            return encoding.getDecodingReader(input);
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Unexpected error while decoding character data from a large object."), PSQLState.UNEXPECTED_ERROR, ioe);
        }
    }
    
    @Override
    public Clob getClob(final String columnName) throws SQLException {
        return this.getClob(this.findColumn(columnName));
    }
    
    protected abstract Clob makeClob(final long p0) throws SQLException;
    
    @Override
    public Clob getClob(final int i) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        }
        return this.makeClob(this.getLong(i));
    }
    
    @Override
    public int getConcurrency() throws SQLException {
        this.checkClosed();
        return this.resultsetconcurrency;
    }
    
    @Override
    public Date getDate(final int i, Calendar cal) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        }
        if (!this.isBinary(i)) {
            if (cal != null) {
                cal = (Calendar)cal.clone();
            }
            return this.connection.getTimestampUtils().toDate(cal, this.getString(i));
        }
        final int col = i - 1;
        final int oid = this.fields[col].getOID();
        final TimeZone tz = (cal == null) ? null : cal.getTimeZone();
        if (oid == 1082) {
            return this.connection.getTimestampUtils().toDateBin(tz, this.this_row[col]);
        }
        if (oid == 1114 || oid == 1184) {
            return this.connection.getTimestampUtils().convertToDate(this.getTimestamp(i, cal), tz);
        }
        throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), "date" }), PSQLState.DATA_TYPE_MISMATCH);
    }
    
    @Override
    public Time getTime(final int i, Calendar cal) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        }
        if (!this.isBinary(i)) {
            if (cal != null) {
                cal = (Calendar)cal.clone();
            }
            return this.connection.getTimestampUtils().toTime(cal, this.getString(i));
        }
        final int col = i - 1;
        final int oid = this.fields[col].getOID();
        final TimeZone tz = (cal == null) ? null : cal.getTimeZone();
        if (oid == 1083 || oid == 1266) {
            return this.connection.getTimestampUtils().toTimeBin(tz, this.this_row[col]);
        }
        if (oid == 1114 || oid == 1184) {
            return this.connection.getTimestampUtils().convertToTime(this.getTimestamp(i, cal), tz);
        }
        throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), "time" }), PSQLState.DATA_TYPE_MISMATCH);
    }
    
    @Override
    public Timestamp getTimestamp(final int i, Calendar cal) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        }
        if (!this.isBinary(i)) {
            if (cal != null) {
                cal = (Calendar)cal.clone();
            }
            return this.connection.getTimestampUtils().toTimestamp(cal, this.getString(i));
        }
        final int col = i - 1;
        final int oid = this.fields[col].getOID();
        if (oid == 1184 || oid == 1114) {
            final boolean hasTimeZone = oid == 1184;
            final TimeZone tz = (cal == null) ? null : cal.getTimeZone();
            return this.connection.getTimestampUtils().toTimestampBin(tz, this.this_row[col], hasTimeZone);
        }
        long millis;
        if (oid == 1083 || oid == 1266) {
            millis = this.getTime(i, cal).getTime();
        }
        else {
            if (oid != 1082) {
                throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), "timestamp" }), PSQLState.DATA_TYPE_MISMATCH);
            }
            millis = this.getDate(i, cal).getTime();
        }
        return new Timestamp(millis);
    }
    
    @Override
    public Date getDate(final String c, final Calendar cal) throws SQLException {
        return this.getDate(this.findColumn(c), cal);
    }
    
    @Override
    public Time getTime(final String c, final Calendar cal) throws SQLException {
        return this.getTime(this.findColumn(c), cal);
    }
    
    @Override
    public Timestamp getTimestamp(final String c, final Calendar cal) throws SQLException {
        return this.getTimestamp(this.findColumn(c), cal);
    }
    
    @Override
    public int getFetchDirection() throws SQLException {
        this.checkClosed();
        return this.fetchdirection;
    }
    
    public Object getObjectImpl(final String columnName, final Map map) throws SQLException {
        return this.getObjectImpl(this.findColumn(columnName), map);
    }
    
    public Object getObjectImpl(final int i, final Map map) throws SQLException {
        this.checkClosed();
        if (map == null || map.isEmpty()) {
            return this.getObject(i);
        }
        throw Driver.notImplemented(this.getClass(), "getObjectImpl(int,Map)");
    }
    
    @Override
    public Ref getRef(final String columnName) throws SQLException {
        return this.getRef(this.findColumn(columnName));
    }
    
    @Override
    public Ref getRef(final int i) throws SQLException {
        this.checkClosed();
        throw Driver.notImplemented(this.getClass(), "getRef(int)");
    }
    
    @Override
    public int getRow() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            return 0;
        }
        final int rows_size = this.rows.size();
        if (this.current_row < 0 || this.current_row >= rows_size) {
            return 0;
        }
        return this.row_offset + this.current_row + 1;
    }
    
    @Override
    public Statement getStatement() throws SQLException {
        this.checkClosed();
        return this.statement;
    }
    
    @Override
    public int getType() throws SQLException {
        this.checkClosed();
        return this.resultsettype;
    }
    
    @Override
    public boolean isAfterLast() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            return false;
        }
        final int rows_size = this.rows.size();
        return this.current_row >= rows_size && rows_size > 0;
    }
    
    @Override
    public boolean isBeforeFirst() throws SQLException {
        this.checkClosed();
        return !this.onInsertRow && this.row_offset + this.current_row < 0 && this.rows.size() > 0;
    }
    
    @Override
    public boolean isFirst() throws SQLException {
        this.checkClosed();
        return !this.onInsertRow && this.row_offset + this.current_row == 0;
    }
    
    @Override
    public boolean isLast() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            return false;
        }
        final int rows_size = this.rows.size();
        if (rows_size == 0) {
            return false;
        }
        if (this.current_row != rows_size - 1) {
            return false;
        }
        if (this.cursor == null) {
            return true;
        }
        if (this.maxRows > 0 && this.row_offset + this.current_row == this.maxRows) {
            return true;
        }
        this.row_offset += rows_size - 1;
        int fetchRows = this.fetchSize;
        if (this.maxRows != 0 && (fetchRows == 0 || this.row_offset + fetchRows > this.maxRows)) {
            fetchRows = this.maxRows - this.row_offset;
        }
        this.connection.getQueryExecutor().fetch(this.cursor, new CursorResultHandler(), fetchRows);
        this.rows.add(0, this.this_row);
        this.current_row = 0;
        return this.rows.size() == 1;
    }
    
    @Override
    public boolean last() throws SQLException {
        this.checkScrollable();
        final int rows_size = this.rows.size();
        if (rows_size <= 0) {
            return false;
        }
        this.current_row = rows_size - 1;
        this.initRowBuffer();
        this.onInsertRow = false;
        return true;
    }
    
    @Override
    public boolean previous() throws SQLException {
        this.checkScrollable();
        if (this.onInsertRow) {
            throw new PSQLException(GT.tr("Can''t use relative move methods while on the insert row."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.current_row - 1 < 0) {
            this.current_row = -1;
            this.this_row = null;
            this.rowBuffer = null;
            return false;
        }
        --this.current_row;
        this.initRowBuffer();
        return true;
    }
    
    @Override
    public boolean relative(final int rows) throws SQLException {
        this.checkScrollable();
        if (this.onInsertRow) {
            throw new PSQLException(GT.tr("Can''t use relative move methods while on the insert row."), PSQLState.INVALID_CURSOR_STATE);
        }
        return this.absolute(this.current_row + 1 + rows);
    }
    
    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        this.checkClosed();
        switch (direction) {
            case 1000: {
                break;
            }
            case 1001:
            case 1002: {
                this.checkScrollable();
                break;
            }
            default: {
                throw new PSQLException(GT.tr("Invalid fetch direction constant: {0}.", new Integer(direction)), PSQLState.INVALID_PARAMETER_VALUE);
            }
        }
        this.fetchdirection = direction;
    }
    
    @Override
    public synchronized void cancelRowUpdates() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            throw new PSQLException(GT.tr("Cannot call cancelRowUpdates() when on the insert row."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.doingUpdates) {
            this.doingUpdates = false;
            this.clearRowBuffer(true);
        }
    }
    
    @Override
    public synchronized void deleteRow() throws SQLException {
        this.checkUpdateable();
        if (this.onInsertRow) {
            throw new PSQLException(GT.tr("Cannot call deleteRow() when on the insert row."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.isBeforeFirst()) {
            throw new PSQLException(GT.tr("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.isAfterLast()) {
            throw new PSQLException(GT.tr("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.rows.size() == 0) {
            throw new PSQLException(GT.tr("There are no rows in this ResultSet."), PSQLState.INVALID_CURSOR_STATE);
        }
        final int numKeys = this.primaryKeys.size();
        if (this.deleteStatement == null) {
            final StringBuilder deleteSQL = new StringBuilder("DELETE FROM ").append(this.onlyTable).append(this.tableName).append(" where ");
            for (int i = 0; i < numKeys; ++i) {
                Utils.escapeIdentifier(deleteSQL, this.primaryKeys.get(i).name);
                deleteSQL.append(" = ?");
                if (i < numKeys - 1) {
                    deleteSQL.append(" and ");
                }
            }
            this.deleteStatement = this.connection.prepareStatement(deleteSQL.toString());
        }
        this.deleteStatement.clearParameters();
        for (int j = 0; j < numKeys; ++j) {
            this.deleteStatement.setObject(j + 1, this.primaryKeys.get(j).getValue());
        }
        this.deleteStatement.executeUpdate();
        this.rows.remove(this.current_row);
        --this.current_row;
        this.moveToCurrentRow();
    }
    
    @Override
    public synchronized void insertRow() throws SQLException {
        this.checkUpdateable();
        if (!this.onInsertRow) {
            throw new PSQLException(GT.tr("Not on the insert row."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.updateValues.size() == 0) {
            throw new PSQLException(GT.tr("You must specify at least one column value to insert a row."), PSQLState.INVALID_PARAMETER_VALUE);
        }
        final StringBuilder insertSQL = new StringBuilder("INSERT INTO ").append(this.tableName).append(" (");
        final StringBuilder paramSQL = new StringBuilder(") values (");
        final Iterator columnNames = this.updateValues.keySet().iterator();
        final int numColumns = this.updateValues.size();
        int i = 0;
        while (columnNames.hasNext()) {
            final String columnName = columnNames.next();
            Utils.escapeIdentifier(insertSQL, columnName);
            if (i < numColumns - 1) {
                insertSQL.append(", ");
                paramSQL.append("?,");
            }
            else {
                paramSQL.append("?)");
            }
            ++i;
        }
        insertSQL.append(paramSQL.toString());
        this.insertStatement = this.connection.prepareStatement(insertSQL.toString());
        final Iterator keys = this.updateValues.keySet().iterator();
        int j = 1;
        while (keys.hasNext()) {
            final String key = keys.next();
            final Object o = this.updateValues.get(key);
            this.insertStatement.setObject(j, o);
            ++j;
        }
        this.insertStatement.executeUpdate();
        if (this.usingOID) {
            final long insertedOID = ((AbstractJdbc2Statement)this.insertStatement).getLastOID();
            this.updateValues.put("oid", new Long(insertedOID));
        }
        this.updateRowBuffer();
        this.rows.add(this.rowBuffer);
        this.this_row = this.rowBuffer;
        this.clearRowBuffer(false);
    }
    
    @Override
    public synchronized void moveToCurrentRow() throws SQLException {
        this.checkUpdateable();
        if (this.current_row < 0 || this.current_row >= this.rows.size()) {
            this.this_row = null;
            this.rowBuffer = null;
        }
        else {
            this.initRowBuffer();
        }
        this.onInsertRow = false;
        this.doingUpdates = false;
    }
    
    @Override
    public synchronized void moveToInsertRow() throws SQLException {
        this.checkUpdateable();
        if (this.insertStatement != null) {
            this.insertStatement = null;
        }
        this.clearRowBuffer(false);
        this.onInsertRow = true;
        this.doingUpdates = false;
    }
    
    private synchronized void clearRowBuffer(final boolean copyCurrentRow) throws SQLException {
        this.rowBuffer = new byte[this.fields.length][];
        if (copyCurrentRow) {
            System.arraycopy(this.this_row, 0, this.rowBuffer, 0, this.this_row.length);
        }
        this.updateValues.clear();
    }
    
    @Override
    public boolean rowDeleted() throws SQLException {
        this.checkClosed();
        return false;
    }
    
    @Override
    public boolean rowInserted() throws SQLException {
        this.checkClosed();
        return false;
    }
    
    @Override
    public boolean rowUpdated() throws SQLException {
        this.checkClosed();
        return false;
    }
    
    @Override
    public synchronized void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        if (x == null) {
            this.updateNull(columnIndex);
            return;
        }
        try {
            final InputStreamReader reader = new InputStreamReader(x, "ASCII");
            final char[] data = new char[length];
            int numRead = 0;
            do {
                final int n = reader.read(data, numRead, length - numRead);
                if (n == -1) {
                    break;
                }
                numRead += n;
            } while (numRead != length);
            this.updateString(columnIndex, new String(data, 0, numRead));
        }
        catch (UnsupportedEncodingException uee) {
            throw new PSQLException(GT.tr("The JVM claims not to support the encoding: {0}", "ASCII"), PSQLState.UNEXPECTED_ERROR, uee);
        }
        catch (IOException ie) {
            throw new PSQLException(GT.tr("Provided InputStream failed."), (PSQLState)null, ie);
        }
    }
    
    @Override
    public synchronized void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
        this.updateValue(columnIndex, x);
    }
    
    @Override
    public synchronized void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        if (x == null) {
            this.updateNull(columnIndex);
            return;
        }
        final byte[] data = new byte[length];
        int numRead = 0;
        try {
            do {
                final int n = x.read(data, numRead, length - numRead);
                if (n == -1) {
                    break;
                }
                numRead += n;
            } while (numRead != length);
        }
        catch (IOException ie) {
            throw new PSQLException(GT.tr("Provided InputStream failed."), (PSQLState)null, ie);
        }
        if (numRead == length) {
            this.updateBytes(columnIndex, data);
        }
        else {
            final byte[] data2 = new byte[numRead];
            System.arraycopy(data, 0, data2, 0, numRead);
            this.updateBytes(columnIndex, data2);
        }
    }
    
    @Override
    public synchronized void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
        this.updateValue(columnIndex, new Boolean(x));
    }
    
    @Override
    public synchronized void updateByte(final int columnIndex, final byte x) throws SQLException {
        this.updateValue(columnIndex, String.valueOf(x));
    }
    
    @Override
    public synchronized void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
        this.updateValue(columnIndex, x);
    }
    
    @Override
    public synchronized void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
        if (x == null) {
            this.updateNull(columnIndex);
            return;
        }
        try {
            final char[] data = new char[length];
            int numRead = 0;
            do {
                final int n = x.read(data, numRead, length - numRead);
                if (n == -1) {
                    break;
                }
                numRead += n;
            } while (numRead != length);
            this.updateString(columnIndex, new String(data, 0, numRead));
        }
        catch (IOException ie) {
            throw new PSQLException(GT.tr("Provided Reader failed."), (PSQLState)null, ie);
        }
    }
    
    @Override
    public synchronized void updateDate(final int columnIndex, final Date x) throws SQLException {
        this.updateValue(columnIndex, x);
    }
    
    @Override
    public synchronized void updateDouble(final int columnIndex, final double x) throws SQLException {
        this.updateValue(columnIndex, new Double(x));
    }
    
    @Override
    public synchronized void updateFloat(final int columnIndex, final float x) throws SQLException {
        this.updateValue(columnIndex, new Float(x));
    }
    
    @Override
    public synchronized void updateInt(final int columnIndex, final int x) throws SQLException {
        this.updateValue(columnIndex, new Integer(x));
    }
    
    @Override
    public synchronized void updateLong(final int columnIndex, final long x) throws SQLException {
        this.updateValue(columnIndex, new Long(x));
    }
    
    @Override
    public synchronized void updateNull(final int columnIndex) throws SQLException {
        this.checkColumnIndex(columnIndex);
        final String columnTypeName = this.connection.getTypeInfo().getPGType(this.fields[columnIndex - 1].getOID());
        this.updateValue(columnIndex, new NullObject(columnTypeName));
    }
    
    @Override
    public synchronized void updateObject(final int columnIndex, final Object x) throws SQLException {
        this.updateValue(columnIndex, x);
    }
    
    @Override
    public synchronized void updateObject(final int columnIndex, final Object x, final int scale) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void refreshRow() throws SQLException {
        this.checkUpdateable();
        if (this.onInsertRow) {
            throw new PSQLException(GT.tr("Can''t refresh the insert row."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.isBeforeFirst() || this.isAfterLast() || this.rows.size() == 0) {
            return;
        }
        final StringBuilder selectSQL = new StringBuilder("select ");
        final ResultSetMetaData rsmd = this.getMetaData();
        final PGResultSetMetaData pgmd = (PGResultSetMetaData)rsmd;
        for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
            if (i > 1) {
                selectSQL.append(", ");
            }
            selectSQL.append(pgmd.getBaseColumnName(i));
        }
        selectSQL.append(" from ").append(this.onlyTable).append(this.tableName).append(" where ");
        final int numKeys = this.primaryKeys.size();
        for (int j = 0; j < numKeys; ++j) {
            final PrimaryKey primaryKey = this.primaryKeys.get(j);
            selectSQL.append(primaryKey.name).append("= ?");
            if (j < numKeys - 1) {
                selectSQL.append(" and ");
            }
        }
        if (this.connection.getLogger().logDebug()) {
            this.connection.getLogger().debug("selecting " + selectSQL.toString());
        }
        this.selectStatement = this.connection.prepareStatement(selectSQL.toString(), 1004, 1008);
        for (int k = 0, l = 1; k < numKeys; ++k, ++l) {
            this.selectStatement.setObject(l, this.primaryKeys.get(k).getValue());
        }
        final AbstractJdbc2ResultSet rs = (AbstractJdbc2ResultSet)this.selectStatement.executeQuery();
        if (rs.next()) {
            this.rowBuffer = rs.this_row;
        }
        this.rows.set(this.current_row, this.rowBuffer);
        this.this_row = this.rowBuffer;
        this.connection.getLogger().debug("done updates");
        rs.close();
        this.selectStatement.close();
        this.selectStatement = null;
    }
    
    @Override
    public synchronized void updateRow() throws SQLException {
        this.checkUpdateable();
        if (this.onInsertRow) {
            throw new PSQLException(GT.tr("Cannot call updateRow() when on the insert row."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.isBeforeFirst() || this.isAfterLast() || this.rows.size() == 0) {
            throw new PSQLException(GT.tr("Cannot update the ResultSet because it is either before the start or after the end of the results."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (!this.doingUpdates) {
            return;
        }
        final StringBuilder updateSQL = new StringBuilder("UPDATE " + this.onlyTable + this.tableName + " SET  ");
        final int numColumns = this.updateValues.size();
        final Iterator columns = this.updateValues.keySet().iterator();
        int i = 0;
        while (columns.hasNext()) {
            final String column = columns.next();
            Utils.escapeIdentifier(updateSQL, column);
            updateSQL.append(" = ?");
            if (i < numColumns - 1) {
                updateSQL.append(", ");
            }
            ++i;
        }
        updateSQL.append(" WHERE ");
        final int numKeys = this.primaryKeys.size();
        for (int j = 0; j < numKeys; ++j) {
            final PrimaryKey primaryKey = this.primaryKeys.get(j);
            Utils.escapeIdentifier(updateSQL, primaryKey.name);
            updateSQL.append(" = ?");
            if (j < numKeys - 1) {
                updateSQL.append(" and ");
            }
        }
        if (this.connection.getLogger().logDebug()) {
            this.connection.getLogger().debug("updating " + updateSQL.toString());
        }
        this.updateStatement = this.connection.prepareStatement(updateSQL.toString());
        int j = 0;
        for (final Object o : this.updateValues.values()) {
            this.updateStatement.setObject(j + 1, o);
            ++j;
        }
        for (int k = 0; k < numKeys; ++k, ++j) {
            this.updateStatement.setObject(j + 1, this.primaryKeys.get(k).getValue());
        }
        this.updateStatement.executeUpdate();
        this.updateStatement.close();
        this.updateStatement = null;
        this.updateRowBuffer();
        this.connection.getLogger().debug("copying data");
        System.arraycopy(this.rowBuffer, 0, this.this_row, 0, this.rowBuffer.length);
        this.rows.set(this.current_row, this.rowBuffer);
        this.connection.getLogger().debug("done updates");
        this.updateValues.clear();
        this.doingUpdates = false;
    }
    
    @Override
    public synchronized void updateShort(final int columnIndex, final short x) throws SQLException {
        this.updateValue(columnIndex, new Short(x));
    }
    
    @Override
    public synchronized void updateString(final int columnIndex, final String x) throws SQLException {
        this.updateValue(columnIndex, x);
    }
    
    @Override
    public synchronized void updateTime(final int columnIndex, final Time x) throws SQLException {
        this.updateValue(columnIndex, x);
    }
    
    @Override
    public synchronized void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
        this.updateValue(columnIndex, x);
    }
    
    @Override
    public synchronized void updateNull(final String columnName) throws SQLException {
        this.updateNull(this.findColumn(columnName));
    }
    
    @Override
    public synchronized void updateBoolean(final String columnName, final boolean x) throws SQLException {
        this.updateBoolean(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateByte(final String columnName, final byte x) throws SQLException {
        this.updateByte(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateShort(final String columnName, final short x) throws SQLException {
        this.updateShort(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateInt(final String columnName, final int x) throws SQLException {
        this.updateInt(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateLong(final String columnName, final long x) throws SQLException {
        this.updateLong(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateFloat(final String columnName, final float x) throws SQLException {
        this.updateFloat(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateDouble(final String columnName, final double x) throws SQLException {
        this.updateDouble(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateBigDecimal(final String columnName, final BigDecimal x) throws SQLException {
        this.updateBigDecimal(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateString(final String columnName, final String x) throws SQLException {
        this.updateString(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateBytes(final String columnName, final byte[] x) throws SQLException {
        this.updateBytes(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateDate(final String columnName, final Date x) throws SQLException {
        this.updateDate(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateTime(final String columnName, final Time x) throws SQLException {
        this.updateTime(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateTimestamp(final String columnName, final Timestamp x) throws SQLException {
        this.updateTimestamp(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateAsciiStream(final String columnName, final InputStream x, final int length) throws SQLException {
        this.updateAsciiStream(this.findColumn(columnName), x, length);
    }
    
    @Override
    public synchronized void updateBinaryStream(final String columnName, final InputStream x, final int length) throws SQLException {
        this.updateBinaryStream(this.findColumn(columnName), x, length);
    }
    
    @Override
    public synchronized void updateCharacterStream(final String columnName, final Reader reader, final int length) throws SQLException {
        this.updateCharacterStream(this.findColumn(columnName), reader, length);
    }
    
    @Override
    public synchronized void updateObject(final String columnName, final Object x, final int scale) throws SQLException {
        this.updateObject(this.findColumn(columnName), x);
    }
    
    @Override
    public synchronized void updateObject(final String columnName, final Object x) throws SQLException {
        this.updateObject(this.findColumn(columnName), x);
    }
    
    boolean isUpdateable() throws SQLException {
        this.checkClosed();
        if (this.resultsetconcurrency == 1007) {
            throw new PSQLException(GT.tr("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.updateable) {
            return true;
        }
        this.connection.getLogger().debug("checking if rs is updateable");
        this.parseQuery();
        if (!this.singleTable) {
            this.connection.getLogger().debug("not a single table");
            return false;
        }
        this.connection.getLogger().debug("getting primary keys");
        this.primaryKeys = new ArrayList();
        this.usingOID = false;
        final int oidIndex = this.findColumnIndex("oid");
        int i = 0;
        if (oidIndex > 0) {
            ++i;
            this.primaryKeys.add(new PrimaryKey(oidIndex, "oid"));
            this.usingOID = true;
        }
        else {
            final String[] s = quotelessTableName(this.tableName);
            final String quotelessTableName = s[0];
            final String quotelessSchemaName = s[1];
            final ResultSet rs = this.connection.getMetaData().getPrimaryKeys("", quotelessSchemaName, quotelessTableName);
            while (rs.next()) {
                final String columnName = rs.getString(4);
                final int index = this.findColumn(columnName);
                if (index > 0) {
                    this.primaryKeys.add(new PrimaryKey(index, columnName));
                }
                ++i;
            }
            rs.close();
        }
        if (this.connection.getLogger().logDebug()) {
            this.connection.getLogger().debug("no of keys=" + i);
        }
        if (i < 1) {
            throw new PSQLException(GT.tr("No primary key found for table {0}.", this.tableName), PSQLState.DATA_ERROR);
        }
        this.updateable = (this.primaryKeys.size() > 0);
        if (this.connection.getLogger().logDebug()) {
            this.connection.getLogger().debug("checking primary key " + this.updateable);
        }
        return this.updateable;
    }
    
    public static String[] quotelessTableName(final String fullname) {
        final String[] parts = { null, "" };
        StringBuilder acc = new StringBuilder();
        boolean betweenQuotes = false;
        for (int i = 0; i < fullname.length(); ++i) {
            final char c = fullname.charAt(i);
            switch (c) {
                case '\"': {
                    if (i < fullname.length() - 1 && fullname.charAt(i + 1) == '\"') {
                        ++i;
                        acc.append(c);
                        break;
                    }
                    betweenQuotes = !betweenQuotes;
                    break;
                }
                case '.': {
                    if (betweenQuotes) {
                        acc.append(c);
                        break;
                    }
                    parts[1] = acc.toString();
                    acc = new StringBuilder();
                    break;
                }
                default: {
                    acc.append(betweenQuotes ? c : Character.toLowerCase(c));
                    break;
                }
            }
        }
        parts[0] = acc.toString();
        return parts;
    }
    
    private void parseQuery() {
        final String l_sql = this.originalQuery.toString(null);
        final StringTokenizer st = new StringTokenizer(l_sql, " \r\t\n");
        boolean tableFound = false;
        boolean tablesChecked = false;
        String name = "";
        this.singleTable = true;
        while (!tableFound && !tablesChecked && st.hasMoreTokens()) {
            name = st.nextToken();
            if (!tableFound) {
                if (!"from".equalsIgnoreCase(name)) {
                    continue;
                }
                this.tableName = st.nextToken();
                if ("only".equalsIgnoreCase(this.tableName)) {
                    this.tableName = st.nextToken();
                    this.onlyTable = "ONLY ";
                }
                tableFound = true;
            }
            else {
                tablesChecked = true;
                this.singleTable = !name.equalsIgnoreCase(",");
            }
        }
    }
    
    private void updateRowBuffer() throws SQLException {
        for (final String columnName : this.updateValues.keySet()) {
            final int columnIndex = this.findColumn(columnName) - 1;
            final Object valueObject = this.updateValues.get(columnName);
            if (valueObject instanceof PGobject) {
                final String value = ((PGobject)valueObject).getValue();
                this.rowBuffer[columnIndex] = (byte[])((value == null) ? null : this.connection.encodeString(value));
            }
            else {
                switch (this.getSQLType(columnIndex + 1)) {
                    case 91: {
                        this.rowBuffer[columnIndex] = this.connection.encodeString(this.connection.getTimestampUtils().toString(null, (Date)valueObject));
                        continue;
                    }
                    case 92: {
                        this.rowBuffer[columnIndex] = this.connection.encodeString(this.connection.getTimestampUtils().toString(null, (Time)valueObject));
                        continue;
                    }
                    case 93: {
                        this.rowBuffer[columnIndex] = this.connection.encodeString(this.connection.getTimestampUtils().toString(null, (Timestamp)valueObject));
                        continue;
                    }
                    case 0: {
                        continue;
                    }
                    case -4:
                    case -3:
                    case -2: {
                        if (this.isBinary(columnIndex + 1)) {
                            this.rowBuffer[columnIndex] = (byte[])valueObject;
                            continue;
                        }
                        try {
                            this.rowBuffer[columnIndex] = PGbytea.toPGString((byte[])valueObject).getBytes("ISO-8859-1");
                            continue;
                        }
                        catch (UnsupportedEncodingException e) {
                            throw new PSQLException(GT.tr("The JVM claims not to support the encoding: {0}", "ISO-8859-1"), PSQLState.UNEXPECTED_ERROR, e);
                        }
                        break;
                    }
                }
                this.rowBuffer[columnIndex] = this.connection.encodeString(String.valueOf(valueObject));
            }
        }
    }
    
    public BaseStatement getPGStatement() {
        return this.statement;
    }
    
    @Override
    public String getRefCursor() {
        return this.refCursorName;
    }
    
    private void setRefCursor(final String refCursorName) {
        this.refCursorName = refCursorName;
    }
    
    @Override
    public void setFetchSize(final int rows) throws SQLException {
        this.checkClosed();
        if (rows < 0) {
            throw new PSQLException(GT.tr("Fetch size must be a value greater to or equal to 0."), PSQLState.INVALID_PARAMETER_VALUE);
        }
        this.fetchSize = rows;
    }
    
    @Override
    public int getFetchSize() throws SQLException {
        this.checkClosed();
        return this.fetchSize;
    }
    
    @Override
    public boolean next() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            throw new PSQLException(GT.tr("Can''t use relative move methods while on the insert row."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.current_row + 1 >= this.rows.size()) {
            if (this.cursor == null || (this.maxRows > 0 && this.row_offset + this.rows.size() >= this.maxRows)) {
                this.current_row = this.rows.size();
                this.this_row = null;
                this.rowBuffer = null;
                return false;
            }
            this.row_offset += this.rows.size();
            int fetchRows = this.fetchSize;
            if (this.maxRows != 0 && (fetchRows == 0 || this.row_offset + fetchRows > this.maxRows)) {
                fetchRows = this.maxRows - this.row_offset;
            }
            this.connection.getQueryExecutor().fetch(this.cursor, new CursorResultHandler(), fetchRows);
            this.current_row = 0;
            if (this.rows.size() == 0) {
                this.this_row = null;
                this.rowBuffer = null;
                return false;
            }
        }
        else {
            ++this.current_row;
        }
        this.initRowBuffer();
        return true;
    }
    
    @Override
    public void close() throws SQLException {
        this.rows = null;
        if (this.cursor != null) {
            this.cursor.close();
            this.cursor = null;
        }
    }
    
    @Override
    public boolean wasNull() throws SQLException {
        this.checkClosed();
        return this.wasNullFlag;
    }
    
    @Override
    public String getString(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        }
        if (this.isBinary(columnIndex) && this.getSQLType(columnIndex) != 12) {
            final Object obj = this.internalGetObject(columnIndex, this.fields[columnIndex - 1]);
            if (obj == null) {
                return null;
            }
            if (obj instanceof java.util.Date) {
                return this.connection.getTimestampUtils().timeToString((java.util.Date)obj);
            }
            if ("hstore".equals(this.getPGType(columnIndex))) {
                return HStoreConverter.toString((Map)obj);
            }
            return this.trimString(columnIndex, obj.toString());
        }
        else {
            final Encoding encoding = this.connection.getEncoding();
            try {
                return this.trimString(columnIndex, encoding.decode(this.this_row[columnIndex - 1]));
            }
            catch (IOException ioe) {
                throw new PSQLException(GT.tr("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database."), PSQLState.DATA_ERROR, ioe);
            }
        }
    }
    
    @Override
    public boolean getBoolean(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return false;
        }
        if (this.isBinary(columnIndex)) {
            final int col = columnIndex - 1;
            return this.readDoubleValue(this.this_row[col], this.fields[col].getOID(), "boolean") == 1.0;
        }
        return toBoolean(this.getString(columnIndex));
    }
    
    @Override
    public byte getByte(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0;
        }
        if (this.isBinary(columnIndex)) {
            final int col = columnIndex - 1;
            return (byte)this.readLongValue(this.this_row[col], this.fields[col].getOID(), -128L, 127L, "byte");
        }
        String s = this.getString(columnIndex);
        if (s != null) {
            s = s.trim();
            if (s.length() == 0) {
                return 0;
            }
            try {
                return Byte.parseByte(s);
            }
            catch (NumberFormatException e) {
                try {
                    final BigDecimal n = new BigDecimal(s);
                    final BigInteger i = n.toBigInteger();
                    final int gt = i.compareTo(AbstractJdbc2ResultSet.BYTEMAX);
                    final int lt = i.compareTo(AbstractJdbc2ResultSet.BYTEMIN);
                    if (gt > 0 || lt < 0) {
                        throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "byte", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                    }
                    return i.byteValue();
                }
                catch (NumberFormatException ex) {
                    throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "byte", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                }
            }
        }
        return 0;
    }
    
    @Override
    public short getShort(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0;
        }
        if (!this.isBinary(columnIndex)) {
            String s = this.getFixedString(columnIndex);
            if (s != null) {
                s = s.trim();
                try {
                    return Short.parseShort(s);
                }
                catch (NumberFormatException e) {
                    try {
                        final BigDecimal n = new BigDecimal(s);
                        final BigInteger i = n.toBigInteger();
                        final int gt = i.compareTo(AbstractJdbc2ResultSet.SHORTMAX);
                        final int lt = i.compareTo(AbstractJdbc2ResultSet.SHORTMIN);
                        if (gt > 0 || lt < 0) {
                            throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "short", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                        }
                        return i.shortValue();
                    }
                    catch (NumberFormatException ne) {
                        throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "short", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                    }
                }
            }
            return 0;
        }
        final int col = columnIndex - 1;
        final int oid = this.fields[col].getOID();
        if (oid == 21) {
            return ByteConverter.int2(this.this_row[col], 0);
        }
        return (short)this.readLongValue(this.this_row[col], oid, -32768L, 32767L, "short");
    }
    
    @Override
    public int getInt(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0;
        }
        if (!this.isBinary(columnIndex)) {
            final Encoding encoding = this.connection.getEncoding();
            if (encoding.hasAsciiNumbers()) {
                try {
                    return this.getFastInt(columnIndex);
                }
                catch (NumberFormatException ex) {}
            }
            return toInt(this.getFixedString(columnIndex));
        }
        final int col = columnIndex - 1;
        final int oid = this.fields[col].getOID();
        if (oid == 23) {
            return ByteConverter.int4(this.this_row[col], 0);
        }
        return (int)this.readLongValue(this.this_row[col], oid, -2147483648L, 2147483647L, "int");
    }
    
    @Override
    public long getLong(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0L;
        }
        if (!this.isBinary(columnIndex)) {
            final Encoding encoding = this.connection.getEncoding();
            if (encoding.hasAsciiNumbers()) {
                try {
                    return this.getFastLong(columnIndex);
                }
                catch (NumberFormatException ex) {}
            }
            return toLong(this.getFixedString(columnIndex));
        }
        final int col = columnIndex - 1;
        final int oid = this.fields[col].getOID();
        if (oid == 20) {
            return ByteConverter.int8(this.this_row[col], 0);
        }
        return this.readLongValue(this.this_row[col], oid, Long.MIN_VALUE, Long.MAX_VALUE, "long");
    }
    
    private long getFastLong(final int columnIndex) throws SQLException, NumberFormatException {
        final byte[] bytes = this.this_row[columnIndex - 1];
        if (bytes.length == 0) {
            throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
        }
        long val = 0L;
        boolean neg;
        int start;
        if (bytes[0] == 45) {
            neg = true;
            start = 1;
            if (bytes.length == 1 || bytes.length > 19) {
                throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
            }
        }
        else {
            start = 0;
            neg = false;
            if (bytes.length > 18) {
                throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
            }
        }
        while (start < bytes.length) {
            final byte b = bytes[start++];
            if (b < 48 || b > 57) {
                throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
            }
            val *= 10L;
            val += b - 48;
        }
        if (neg) {
            val = -val;
        }
        return val;
    }
    
    private int getFastInt(final int columnIndex) throws SQLException, NumberFormatException {
        final byte[] bytes = this.this_row[columnIndex - 1];
        if (bytes.length == 0) {
            throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
        }
        int val = 0;
        boolean neg;
        int start;
        if (bytes[0] == 45) {
            neg = true;
            start = 1;
            if (bytes.length == 1 || bytes.length > 10) {
                throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
            }
        }
        else {
            start = 0;
            neg = false;
            if (bytes.length > 9) {
                throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
            }
        }
        while (start < bytes.length) {
            final byte b = bytes[start++];
            if (b < 48 || b > 57) {
                throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
            }
            val *= 10;
            val += b - 48;
        }
        if (neg) {
            val = -val;
        }
        return val;
    }
    
    private BigDecimal getFastBigDecimal(final int columnIndex) throws SQLException, NumberFormatException {
        final byte[] bytes = this.this_row[columnIndex - 1];
        if (bytes.length == 0) {
            throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
        }
        int scale = 0;
        long val = 0L;
        boolean neg;
        int start;
        if (bytes[0] == 45) {
            neg = true;
            start = 1;
            if (bytes.length == 1 || bytes.length > 19) {
                throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
            }
        }
        else {
            start = 0;
            neg = false;
            if (bytes.length > 18) {
                throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
            }
        }
        int periodsSeen = 0;
        while (start < bytes.length) {
            final byte b = bytes[start++];
            if (b < 48 || b > 57) {
                if (b != 46) {
                    throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
                }
                scale = bytes.length - start;
                ++periodsSeen;
            }
            else {
                val *= 10L;
                val += b - 48;
            }
        }
        final int numNonSignChars = neg ? (bytes.length - 1) : bytes.length;
        if (periodsSeen > 1 || periodsSeen == numNonSignChars) {
            throw AbstractJdbc2ResultSet.FAST_NUMBER_FAILED;
        }
        if (neg) {
            val = -val;
        }
        return BigDecimal.valueOf(val, scale);
    }
    
    @Override
    public float getFloat(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0.0f;
        }
        if (!this.isBinary(columnIndex)) {
            return toFloat(this.getFixedString(columnIndex));
        }
        final int col = columnIndex - 1;
        final int oid = this.fields[col].getOID();
        if (oid == 700) {
            return ByteConverter.float4(this.this_row[col], 0);
        }
        return (float)this.readDoubleValue(this.this_row[col], oid, "float");
    }
    
    @Override
    public double getDouble(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0.0;
        }
        if (!this.isBinary(columnIndex)) {
            return toDouble(this.getFixedString(columnIndex));
        }
        final int col = columnIndex - 1;
        final int oid = this.fields[col].getOID();
        if (oid == 701) {
            return ByteConverter.float8(this.this_row[col], 0);
        }
        return this.readDoubleValue(this.this_row[col], oid, "double");
    }
    
    @Override
    public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        }
        final Encoding encoding = this.connection.getEncoding();
        if (encoding.hasAsciiNumbers()) {
            try {
                return this.getFastBigDecimal(columnIndex);
            }
            catch (NumberFormatException ex) {}
        }
        return toBigDecimal(this.getFixedString(columnIndex), scale);
    }
    
    @Override
    public byte[] getBytes(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        }
        if (this.isBinary(columnIndex)) {
            return this.this_row[columnIndex - 1];
        }
        if (this.connection.haveMinimumCompatibleVersion("7.2")) {
            if (this.fields[columnIndex - 1].getOID() == 17) {
                return this.trimBytes(columnIndex, PGbytea.toBytes(this.this_row[columnIndex - 1]));
            }
            return this.trimBytes(columnIndex, this.this_row[columnIndex - 1]);
        }
        else {
            if (this.fields[columnIndex - 1].getOID() == 26) {
                final LargeObjectManager lom = this.connection.getLargeObjectAPI();
                final LargeObject lob = lom.open(this.getLong(columnIndex));
                final byte[] buf = lob.read(lob.size());
                lob.close();
                return this.trimBytes(columnIndex, buf);
            }
            return this.trimBytes(columnIndex, this.this_row[columnIndex - 1]);
        }
    }
    
    @Override
    public Date getDate(final int columnIndex) throws SQLException {
        return this.getDate(columnIndex, null);
    }
    
    @Override
    public Time getTime(final int columnIndex) throws SQLException {
        return this.getTime(columnIndex, null);
    }
    
    @Override
    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
        return this.getTimestamp(columnIndex, null);
    }
    
    @Override
    public InputStream getAsciiStream(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        }
        if (this.connection.haveMinimumCompatibleVersion("7.2")) {
            try {
                return new ByteArrayInputStream(this.getString(columnIndex).getBytes("ASCII"));
            }
            catch (UnsupportedEncodingException l_uee) {
                throw new PSQLException(GT.tr("The JVM claims not to support the encoding: {0}", "ASCII"), PSQLState.UNEXPECTED_ERROR, l_uee);
            }
        }
        return this.getBinaryStream(columnIndex);
    }
    
    @Override
    public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        }
        if (this.connection.haveMinimumCompatibleVersion("7.2")) {
            try {
                return new ByteArrayInputStream(this.getString(columnIndex).getBytes("UTF-8"));
            }
            catch (UnsupportedEncodingException l_uee) {
                throw new PSQLException(GT.tr("The JVM claims not to support the encoding: {0}", "UTF-8"), PSQLState.UNEXPECTED_ERROR, l_uee);
            }
        }
        return this.getBinaryStream(columnIndex);
    }
    
    @Override
    public InputStream getBinaryStream(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        }
        if (this.connection.haveMinimumCompatibleVersion("7.2")) {
            final byte[] b = this.getBytes(columnIndex);
            if (b != null) {
                return new ByteArrayInputStream(b);
            }
        }
        else if (this.fields[columnIndex - 1].getOID() == 26) {
            final LargeObjectManager lom = this.connection.getLargeObjectAPI();
            final LargeObject lob = lom.open(this.getLong(columnIndex));
            return lob.getInputStream();
        }
        return null;
    }
    
    @Override
    public String getString(final String columnName) throws SQLException {
        return this.getString(this.findColumn(columnName));
    }
    
    @Override
    public boolean getBoolean(final String columnName) throws SQLException {
        return this.getBoolean(this.findColumn(columnName));
    }
    
    @Override
    public byte getByte(final String columnName) throws SQLException {
        return this.getByte(this.findColumn(columnName));
    }
    
    @Override
    public short getShort(final String columnName) throws SQLException {
        return this.getShort(this.findColumn(columnName));
    }
    
    @Override
    public int getInt(final String columnName) throws SQLException {
        return this.getInt(this.findColumn(columnName));
    }
    
    @Override
    public long getLong(final String columnName) throws SQLException {
        return this.getLong(this.findColumn(columnName));
    }
    
    @Override
    public float getFloat(final String columnName) throws SQLException {
        return this.getFloat(this.findColumn(columnName));
    }
    
    @Override
    public double getDouble(final String columnName) throws SQLException {
        return this.getDouble(this.findColumn(columnName));
    }
    
    @Override
    public BigDecimal getBigDecimal(final String columnName, final int scale) throws SQLException {
        return this.getBigDecimal(this.findColumn(columnName), scale);
    }
    
    @Override
    public byte[] getBytes(final String columnName) throws SQLException {
        return this.getBytes(this.findColumn(columnName));
    }
    
    @Override
    public Date getDate(final String columnName) throws SQLException {
        return this.getDate(this.findColumn(columnName), null);
    }
    
    @Override
    public Time getTime(final String columnName) throws SQLException {
        return this.getTime(this.findColumn(columnName), null);
    }
    
    @Override
    public Timestamp getTimestamp(final String columnName) throws SQLException {
        return this.getTimestamp(this.findColumn(columnName), null);
    }
    
    @Override
    public InputStream getAsciiStream(final String columnName) throws SQLException {
        return this.getAsciiStream(this.findColumn(columnName));
    }
    
    @Override
    public InputStream getUnicodeStream(final String columnName) throws SQLException {
        return this.getUnicodeStream(this.findColumn(columnName));
    }
    
    @Override
    public InputStream getBinaryStream(final String columnName) throws SQLException {
        return this.getBinaryStream(this.findColumn(columnName));
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        this.checkClosed();
        return this.warnings;
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        this.checkClosed();
        this.warnings = null;
    }
    
    protected void addWarning(final SQLWarning warnings) {
        if (this.warnings != null) {
            this.warnings.setNextWarning(warnings);
        }
        else {
            this.warnings = warnings;
        }
    }
    
    @Override
    public String getCursorName() throws SQLException {
        this.checkClosed();
        return null;
    }
    
    @Override
    public Object getObject(final int columnIndex) throws SQLException {
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        }
        final Field field = this.fields[columnIndex - 1];
        if (field == null) {
            this.wasNullFlag = true;
            return null;
        }
        final Object result = this.internalGetObject(columnIndex, field);
        if (result != null) {
            return result;
        }
        if (this.isBinary(columnIndex)) {
            return this.connection.getObject(this.getPGType(columnIndex), null, this.this_row[columnIndex - 1]);
        }
        return this.connection.getObject(this.getPGType(columnIndex), this.getString(columnIndex), null);
    }
    
    @Override
    public Object getObject(final String columnName) throws SQLException {
        return this.getObject(this.findColumn(columnName));
    }
    
    @Override
    public int findColumn(final String columnName) throws SQLException {
        this.checkClosed();
        final int col = this.findColumnIndex(columnName);
        if (col == 0) {
            throw new PSQLException(GT.tr("The column name {0} was not found in this ResultSet.", columnName), PSQLState.UNDEFINED_COLUMN);
        }
        return col;
    }
    
    private int findColumnIndex(final String columnName) {
        if (this.columnNameIndexMap == null) {
            this.columnNameIndexMap = new HashMap(this.fields.length * 2);
            final boolean isSanitiserDisabled = this.connection.isColumnSanitiserDisabled();
            for (int i = this.fields.length - 1; i >= 0; --i) {
                if (isSanitiserDisabled) {
                    this.columnNameIndexMap.put(this.fields[i].getColumnLabel(), new Integer(i + 1));
                }
                else {
                    this.columnNameIndexMap.put(this.fields[i].getColumnLabel().toLowerCase(Locale.US), new Integer(i + 1));
                }
            }
        }
        Integer index = this.columnNameIndexMap.get(columnName);
        if (index != null) {
            return index;
        }
        index = this.columnNameIndexMap.get(columnName.toLowerCase(Locale.US));
        if (index != null) {
            this.columnNameIndexMap.put(columnName, index);
            return index;
        }
        index = this.columnNameIndexMap.get(columnName.toUpperCase(Locale.US));
        if (index != null) {
            this.columnNameIndexMap.put(columnName, index);
            return index;
        }
        return 0;
    }
    
    public int getColumnOID(final int field) {
        return this.fields[field - 1].getOID();
    }
    
    @Override
    public String getFixedString(final int col) throws SQLException {
        String s = this.getString(col);
        if (s == null) {
            return null;
        }
        if (s.length() < 2) {
            return s;
        }
        final char ch = s.charAt(0);
        if (ch > '-') {
            return s;
        }
        if (ch == '(') {
            s = "-" + PGtokenizer.removePara(s).substring(1);
        }
        else if (ch == '$') {
            s = s.substring(1);
        }
        else if (ch == '-' && s.charAt(1) == '$') {
            s = "-" + s.substring(2);
        }
        return s;
    }
    
    protected String getPGType(final int column) throws SQLException {
        return this.connection.getTypeInfo().getPGType(this.fields[column - 1].getOID());
    }
    
    protected int getSQLType(final int column) throws SQLException {
        return this.connection.getTypeInfo().getSQLType(this.fields[column - 1].getOID());
    }
    
    private void checkUpdateable() throws SQLException {
        this.checkClosed();
        if (!this.isUpdateable()) {
            throw new PSQLException(GT.tr("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details."), PSQLState.INVALID_CURSOR_STATE);
        }
        if (this.updateValues == null) {
            this.updateValues = new HashMap((int)(this.fields.length / 0.75), 0.75f);
        }
    }
    
    protected void checkClosed() throws SQLException {
        if (this.rows == null) {
            throw new PSQLException(GT.tr("This ResultSet is closed."), PSQLState.OBJECT_NOT_IN_STATE);
        }
    }
    
    protected boolean isResultSetClosed() {
        return this.rows == null;
    }
    
    protected void checkColumnIndex(final int column) throws SQLException {
        if (column < 1 || column > this.fields.length) {
            throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { new Integer(column), new Integer(this.fields.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
    }
    
    protected void checkResultSet(final int column) throws SQLException {
        this.checkClosed();
        if (this.this_row == null) {
            throw new PSQLException(GT.tr("ResultSet not positioned properly, perhaps you need to call next."), PSQLState.INVALID_CURSOR_STATE);
        }
        this.checkColumnIndex(column);
        this.wasNullFlag = (this.this_row[column - 1] == null);
    }
    
    protected boolean isBinary(final int column) {
        return this.fields[column - 1].getFormat() == 1;
    }
    
    public static boolean toBoolean(String s) {
        if (s != null) {
            s = s.trim();
            if (s.equalsIgnoreCase("t") || s.equalsIgnoreCase("true") || s.equals("1")) {
                return true;
            }
            if (s.equalsIgnoreCase("f") || s.equalsIgnoreCase("false") || s.equals("0")) {
                return false;
            }
            try {
                if (Double.parseDouble(s) == 1.0) {
                    return true;
                }
            }
            catch (NumberFormatException ex) {}
        }
        return false;
    }
    
    public static int toInt(String s) throws SQLException {
        if (s != null) {
            try {
                s = s.trim();
                return Integer.parseInt(s);
            }
            catch (NumberFormatException e) {
                try {
                    final BigDecimal n = new BigDecimal(s);
                    final BigInteger i = n.toBigInteger();
                    final int gt = i.compareTo(AbstractJdbc2ResultSet.INTMAX);
                    final int lt = i.compareTo(AbstractJdbc2ResultSet.INTMIN);
                    if (gt > 0 || lt < 0) {
                        throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "int", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                    }
                    return i.intValue();
                }
                catch (NumberFormatException ne) {
                    throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "int", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                }
            }
        }
        return 0;
    }
    
    public static long toLong(String s) throws SQLException {
        if (s != null) {
            try {
                s = s.trim();
                return Long.parseLong(s);
            }
            catch (NumberFormatException e) {
                try {
                    final BigDecimal n = new BigDecimal(s);
                    final BigInteger i = n.toBigInteger();
                    final int gt = i.compareTo(AbstractJdbc2ResultSet.LONGMAX);
                    final int lt = i.compareTo(AbstractJdbc2ResultSet.LONGMIN);
                    if (gt > 0 || lt < 0) {
                        throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "long", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                    }
                    return i.longValue();
                }
                catch (NumberFormatException ne) {
                    throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "long", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                }
            }
        }
        return 0L;
    }
    
    public static BigDecimal toBigDecimal(String s, final int scale) throws SQLException {
        if (s != null) {
            BigDecimal val;
            try {
                s = s.trim();
                val = new BigDecimal(s);
            }
            catch (NumberFormatException e) {
                throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "BigDecimal", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
            if (scale == -1) {
                return val;
            }
            try {
                return val.setScale(scale);
            }
            catch (ArithmeticException e2) {
                throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "BigDecimal", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
        }
        return null;
    }
    
    public static float toFloat(String s) throws SQLException {
        if (s != null) {
            try {
                s = s.trim();
                return Float.parseFloat(s);
            }
            catch (NumberFormatException e) {
                throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "float", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
        }
        return 0.0f;
    }
    
    public static double toDouble(String s) throws SQLException {
        if (s != null) {
            try {
                s = s.trim();
                return Double.parseDouble(s);
            }
            catch (NumberFormatException e) {
                throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "double", s }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
        }
        return 0.0;
    }
    
    private void initRowBuffer() {
        this.this_row = this.rows.get(this.current_row);
        if (this.resultsetconcurrency == 1008) {
            this.rowBuffer = new byte[this.this_row.length][];
            System.arraycopy(this.this_row, 0, this.rowBuffer, 0, this.this_row.length);
        }
        else {
            this.rowBuffer = null;
        }
    }
    
    private boolean isColumnTrimmable(final int columnIndex) throws SQLException {
        switch (this.getSQLType(columnIndex)) {
            case -4:
            case -3:
            case -2:
            case -1:
            case 1:
            case 12: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    private byte[] trimBytes(final int p_columnIndex, final byte[] p_bytes) throws SQLException {
        if (this.maxFieldSize > 0 && p_bytes.length > this.maxFieldSize && this.isColumnTrimmable(p_columnIndex)) {
            final byte[] l_bytes = new byte[this.maxFieldSize];
            System.arraycopy(p_bytes, 0, l_bytes, 0, this.maxFieldSize);
            return l_bytes;
        }
        return p_bytes;
    }
    
    private String trimString(final int p_columnIndex, final String p_string) throws SQLException {
        if (this.maxFieldSize > 0 && p_string.length() > this.maxFieldSize && this.isColumnTrimmable(p_columnIndex)) {
            return p_string.substring(0, this.maxFieldSize);
        }
        return p_string;
    }
    
    private double readDoubleValue(final byte[] bytes, final int oid, final String targetType) throws PSQLException {
        switch (oid) {
            case 21: {
                return ByteConverter.int2(bytes, 0);
            }
            case 23: {
                return ByteConverter.int4(bytes, 0);
            }
            case 20: {
                return (double)ByteConverter.int8(bytes, 0);
            }
            case 700: {
                return ByteConverter.float4(bytes, 0);
            }
            case 701: {
                return ByteConverter.float8(bytes, 0);
            }
            default: {
                throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), targetType }), PSQLState.DATA_TYPE_MISMATCH);
            }
        }
    }
    
    private long readLongValue(final byte[] bytes, final int oid, final long minVal, final long maxVal, final String targetType) throws PSQLException {
        long val = 0L;
        switch (oid) {
            case 21: {
                val = ByteConverter.int2(bytes, 0);
                break;
            }
            case 23: {
                val = ByteConverter.int4(bytes, 0);
                break;
            }
            case 20: {
                val = ByteConverter.int8(bytes, 0);
                break;
            }
            case 700: {
                val = (long)ByteConverter.float4(bytes, 0);
                break;
            }
            case 701: {
                val = (long)ByteConverter.float8(bytes, 0);
                break;
            }
            default: {
                throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), targetType }), PSQLState.DATA_TYPE_MISMATCH);
            }
        }
        if (val < minVal || val > maxVal) {
            throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { targetType, new Long(val) }), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
        }
        return val;
    }
    
    protected void updateValue(final int columnIndex, final Object value) throws SQLException {
        this.checkUpdateable();
        if (!this.onInsertRow && (this.isBeforeFirst() || this.isAfterLast() || this.rows.size() == 0)) {
            throw new PSQLException(GT.tr("Cannot update the ResultSet because it is either before the start or after the end of the results."), PSQLState.INVALID_CURSOR_STATE);
        }
        this.checkColumnIndex(columnIndex);
        this.doingUpdates = !this.onInsertRow;
        if (value == null) {
            this.updateNull(columnIndex);
        }
        else {
            final PGResultSetMetaData md = (PGResultSetMetaData)this.getMetaData();
            this.updateValues.put(md.getBaseColumnName(columnIndex), value);
        }
    }
    
    protected Object getUUID(final String data) throws SQLException {
        return data;
    }
    
    protected Object getUUID(final byte[] data) throws SQLException {
        return data;
    }
    
    void addRows(final List tuples) {
        this.rows.addAll(tuples);
    }
    
    public void registerRealStatement(final Statement realStatement) {
        this.realStatement = realStatement;
    }
    
    static {
        BYTEMAX = new BigInteger(Byte.toString((byte)127));
        BYTEMIN = new BigInteger(Byte.toString((byte)(-128)));
        SHORTMAX = new BigInteger(Short.toString((short)32767));
        SHORTMIN = new BigInteger(Short.toString((short)(-32768)));
        FAST_NUMBER_FAILED = new NumberFormatException();
        INTMAX = new BigInteger(Integer.toString(Integer.MAX_VALUE));
        INTMIN = new BigInteger(Integer.toString(Integer.MIN_VALUE));
        LONGMAX = new BigInteger(Long.toString(Long.MAX_VALUE));
        LONGMIN = new BigInteger(Long.toString(Long.MIN_VALUE));
    }
    
    public class CursorResultHandler implements ResultHandler
    {
        private SQLException error;
        
        @Override
        public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
            AbstractJdbc2ResultSet.this.rows = tuples;
            AbstractJdbc2ResultSet.this.cursor = cursor;
        }
        
        @Override
        public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
            this.handleError(new PSQLException(GT.tr("Unexpected command status: {0}.", status), PSQLState.PROTOCOL_VIOLATION));
        }
        
        @Override
        public void handleWarning(final SQLWarning warning) {
            AbstractJdbc2ResultSet.this.addWarning(warning);
        }
        
        @Override
        public void handleError(final SQLException newError) {
            if (this.error == null) {
                this.error = newError;
            }
            else {
                this.error.setNextException(newError);
            }
        }
        
        @Override
        public void handleCompletion() throws SQLException {
            if (this.error != null) {
                throw this.error;
            }
        }
    }
    
    private class PrimaryKey
    {
        int index;
        String name;
        
        PrimaryKey(final int index, final String name) {
            this.index = index;
            this.name = name;
        }
        
        Object getValue() throws SQLException {
            return AbstractJdbc2ResultSet.this.getObject(this.index);
        }
    }
    
    static class NullObject extends PGobject
    {
        NullObject(final String type) {
            this.setType(type);
        }
        
        @Override
        public String getValue() {
            return null;
        }
    }
}
