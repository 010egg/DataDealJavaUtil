// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring;

import java.io.Reader;
import java.io.OutputStream;
import java.io.Closeable;
import com.alibaba.druid.util.JdbcUtils;
import java.sql.Clob;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import org.springframework.jdbc.support.lob.LobCreator;

public class DruidLobCreator implements LobCreator
{
    public void setBlobAsBytes(final PreparedStatement ps, final int paramIndex, final byte[] content) throws SQLException {
        final Blob blob = ps.getConnection().createBlob();
        blob.setBytes(1L, content);
        ps.setBlob(paramIndex, blob);
    }
    
    public void setBlobAsBinaryStream(final PreparedStatement ps, final int paramIndex, final InputStream contentStream, final int contentLength) throws SQLException {
        ps.setBlob(paramIndex, contentStream, contentLength);
    }
    
    public void setClobAsString(final PreparedStatement ps, final int paramIndex, final String content) throws SQLException {
        final Clob clob = ps.getConnection().createClob();
        clob.setString(1L, content);
        ps.setClob(paramIndex, clob);
    }
    
    public void setClobAsAsciiStream(final PreparedStatement ps, final int paramIndex, final InputStream asciiStream, final int contentLength) throws SQLException {
        if (asciiStream != null) {
            final Clob clob = ps.getConnection().createClob();
            final OutputStream out = clob.setAsciiStream(1L);
            final int BUFFER_SIZE = 4096;
            try {
                final byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = asciiStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }
            catch (Exception e) {
                throw new SQLException("setClob error", e);
            }
            finally {
                JdbcUtils.close(asciiStream);
                JdbcUtils.close(out);
            }
            ps.setClob(paramIndex, clob);
        }
        else {
            ps.setClob(paramIndex, (Clob)null);
        }
    }
    
    public void setClobAsCharacterStream(final PreparedStatement ps, final int paramIndex, final Reader characterStream, final int contentLength) throws SQLException {
        ps.setClob(paramIndex, characterStream, contentLength);
    }
    
    public void close() {
    }
}
