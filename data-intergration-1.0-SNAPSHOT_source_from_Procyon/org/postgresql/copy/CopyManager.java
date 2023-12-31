// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.copy;

import java.io.InputStream;
import java.io.Reader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Writer;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import org.postgresql.core.QueryExecutor;
import org.postgresql.core.Encoding;

public class CopyManager
{
    static final int DEFAULT_BUFFER_SIZE = 65536;
    private final Encoding encoding;
    private final QueryExecutor queryExecutor;
    private final BaseConnection connection;
    
    public CopyManager(final BaseConnection connection) throws SQLException {
        this.encoding = connection.getEncoding();
        this.queryExecutor = connection.getQueryExecutor();
        this.connection = connection;
    }
    
    public CopyIn copyIn(final String sql) throws SQLException {
        CopyOperation op = null;
        try {
            op = this.queryExecutor.startCopy(sql, this.connection.getAutoCommit());
            return (CopyIn)op;
        }
        catch (ClassCastException cce) {
            op.cancelCopy();
            throw new PSQLException(GT.tr("Requested CopyIn but got {0}", op.getClass().getName()), PSQLState.WRONG_OBJECT_TYPE, cce);
        }
    }
    
    public CopyOut copyOut(final String sql) throws SQLException {
        CopyOperation op = null;
        try {
            op = this.queryExecutor.startCopy(sql, this.connection.getAutoCommit());
            return (CopyOut)op;
        }
        catch (ClassCastException cce) {
            op.cancelCopy();
            throw new PSQLException(GT.tr("Requested CopyOut but got {0}", op.getClass().getName()), PSQLState.WRONG_OBJECT_TYPE, cce);
        }
    }
    
    public long copyOut(final String sql, final Writer to) throws SQLException, IOException {
        final CopyOut cp = this.copyOut(sql);
        try {
            byte[] buf;
            while ((buf = cp.readFromCopy()) != null) {
                to.write(this.encoding.decode(buf));
            }
            return cp.getHandledRowCount();
        }
        catch (IOException ioEX) {
            if (cp.isActive()) {
                cp.cancelCopy();
            }
            try {
                byte[] buf;
                while ((buf = cp.readFromCopy()) != null) {}
            }
            catch (SQLException ex) {}
            throw ioEX;
        }
        finally {
            if (cp.isActive()) {
                cp.cancelCopy();
            }
        }
    }
    
    public long copyOut(final String sql, final OutputStream to) throws SQLException, IOException {
        final CopyOut cp = this.copyOut(sql);
        try {
            byte[] buf;
            while ((buf = cp.readFromCopy()) != null) {
                to.write(buf);
            }
            return cp.getHandledRowCount();
        }
        catch (IOException ioEX) {
            if (cp.isActive()) {
                cp.cancelCopy();
            }
            try {
                byte[] buf;
                while ((buf = cp.readFromCopy()) != null) {}
            }
            catch (SQLException ex) {}
            throw ioEX;
        }
        finally {
            if (cp.isActive()) {
                cp.cancelCopy();
            }
        }
    }
    
    public long copyIn(final String sql, final Reader from) throws SQLException, IOException {
        return this.copyIn(sql, from, 65536);
    }
    
    public long copyIn(final String sql, final Reader from, final int bufferSize) throws SQLException, IOException {
        final char[] cbuf = new char[bufferSize];
        final CopyIn cp = this.copyIn(sql);
        try {
            int len;
            while ((len = from.read(cbuf)) > 0) {
                final byte[] buf = this.encoding.encode(new String(cbuf, 0, len));
                cp.writeToCopy(buf, 0, buf.length);
            }
            return cp.endCopy();
        }
        finally {
            if (cp.isActive()) {
                cp.cancelCopy();
            }
        }
    }
    
    public long copyIn(final String sql, final InputStream from) throws SQLException, IOException {
        return this.copyIn(sql, from, 65536);
    }
    
    public long copyIn(final String sql, final InputStream from, final int bufferSize) throws SQLException, IOException {
        final byte[] buf = new byte[bufferSize];
        final CopyIn cp = this.copyIn(sql);
        try {
            int len;
            while ((len = from.read(buf)) > 0) {
                cp.writeToCopy(buf, 0, len);
            }
            return cp.endCopy();
        }
        finally {
            if (cp.isActive()) {
                cp.cancelCopy();
            }
        }
    }
}
