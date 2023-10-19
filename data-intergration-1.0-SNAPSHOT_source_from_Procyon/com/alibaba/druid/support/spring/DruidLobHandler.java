// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring;

import org.springframework.jdbc.support.lob.LobCreator;
import java.io.Reader;
import java.sql.Clob;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Blob;
import java.sql.ResultSet;
import org.springframework.jdbc.support.lob.AbstractLobHandler;

public class DruidLobHandler extends AbstractLobHandler
{
    public byte[] getBlobAsBytes(final ResultSet rs, final int columnIndex) throws SQLException {
        final Blob blob = rs.getBlob(columnIndex);
        if (blob == null) {
            return null;
        }
        return blob.getBytes(1L, (int)blob.length());
    }
    
    public InputStream getBlobAsBinaryStream(final ResultSet rs, final int columnIndex) throws SQLException {
        final Blob blob = rs.getBlob(columnIndex);
        if (blob == null) {
            return null;
        }
        return blob.getBinaryStream();
    }
    
    public String getClobAsString(final ResultSet rs, final int columnIndex) throws SQLException {
        final Clob clob = rs.getClob(columnIndex);
        if (clob == null) {
            return null;
        }
        return clob.getSubString(1L, (int)clob.length());
    }
    
    public InputStream getClobAsAsciiStream(final ResultSet rs, final int columnIndex) throws SQLException {
        final Clob clob = rs.getClob(columnIndex);
        if (clob == null) {
            return null;
        }
        return clob.getAsciiStream();
    }
    
    public Reader getClobAsCharacterStream(final ResultSet rs, final int columnIndex) throws SQLException {
        final Clob clob = rs.getClob(columnIndex);
        if (clob == null) {
            return null;
        }
        return clob.getCharacterStream();
    }
    
    public LobCreator getLobCreator() {
        return (LobCreator)new DruidLobCreator();
    }
}
