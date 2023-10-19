// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.Writer;
import java.io.Reader;
import java.io.OutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.SQLXML;

public class MockSQLXML implements SQLXML
{
    @Override
    public void free() throws SQLException {
    }
    
    @Override
    public InputStream getBinaryStream() throws SQLException {
        return null;
    }
    
    @Override
    public OutputStream setBinaryStream() throws SQLException {
        return null;
    }
    
    @Override
    public Reader getCharacterStream() throws SQLException {
        return null;
    }
    
    @Override
    public Writer setCharacterStream() throws SQLException {
        return null;
    }
    
    @Override
    public String getString() throws SQLException {
        return null;
    }
    
    @Override
    public void setString(final String value) throws SQLException {
    }
    
    @Override
    public <T extends Source> T getSource(final Class<T> sourceClass) throws SQLException {
        return null;
    }
    
    @Override
    public <T extends Result> T setResult(final Class<T> resultClass) throws SQLException {
        return null;
    }
}
