// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import java.sql.Clob;
import org.postgresql.Driver;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.Reader;
import java.io.InputStream;
import java.sql.SQLException;
import org.postgresql.core.BaseConnection;

public abstract class AbstractJdbc2Clob extends AbstractJdbc2BlobClob
{
    public AbstractJdbc2Clob(final BaseConnection conn, final long oid) throws SQLException {
        super(conn, oid);
    }
    
    public synchronized InputStream getAsciiStream() throws SQLException {
        return this.getBinaryStream();
    }
    
    public synchronized Reader getCharacterStream() throws SQLException {
        final Charset connectionCharset = Charset.forName(this.conn.getEncoding().name());
        return new InputStreamReader(this.getBinaryStream(), connectionCharset);
    }
    
    public synchronized String getSubString(final long i, final int j) throws SQLException {
        this.assertPosition(i, j);
        this.getLo(false).seek((int)i - 1);
        return new String(this.getLo(false).read(j));
    }
    
    public synchronized long position(final String pattern, final long start) throws SQLException {
        this.checkFreed();
        throw Driver.notImplemented(this.getClass(), "position(String,long)");
    }
    
    public synchronized long position(final Clob pattern, final long start) throws SQLException {
        this.checkFreed();
        throw Driver.notImplemented(this.getClass(), "position(Clob,start)");
    }
}
