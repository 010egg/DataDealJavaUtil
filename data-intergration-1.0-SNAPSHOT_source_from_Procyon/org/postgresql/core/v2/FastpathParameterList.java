// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v2;

import java.util.Arrays;
import java.io.IOException;
import org.postgresql.core.PGStream;
import java.io.InputStream;
import org.postgresql.util.StreamWrapper;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.core.ParameterList;

class FastpathParameterList implements ParameterList
{
    private final Object[] paramValues;
    
    FastpathParameterList(final int paramCount) {
        this.paramValues = new Object[paramCount];
    }
    
    @Override
    public void registerOutParameter(final int index, final int sqlType) {
    }
    
    public void registerOutParameter(final int index, final int sqlType, final int precision) {
    }
    
    @Override
    public int getInParameterCount() {
        return this.paramValues.length;
    }
    
    @Override
    public int getOutParameterCount() {
        return 0;
    }
    
    @Override
    public int getParameterCount() {
        return this.paramValues.length;
    }
    
    @Override
    public int[] getTypeOIDs() {
        return null;
    }
    
    @Override
    public void setIntParameter(final int index, final int value) throws SQLException {
        if (index < 1 || index > this.paramValues.length) {
            throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { new Integer(index), new Integer(this.paramValues.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
        final byte[] data = { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
        this.paramValues[index - 1] = data;
    }
    
    @Override
    public void setLiteralParameter(final int index, final String value, final int oid) throws SQLException {
        throw new IllegalArgumentException("can't setLiteralParameter() on a fastpath parameter");
    }
    
    @Override
    public void setStringParameter(final int index, final String value, final int oid) throws SQLException {
        this.paramValues[index - 1] = value;
    }
    
    @Override
    public void setBytea(final int index, final byte[] data, final int offset, final int length) throws SQLException {
        if (index < 1 || index > this.paramValues.length) {
            throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { new Integer(index), new Integer(this.paramValues.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
        this.paramValues[index - 1] = new StreamWrapper(data, offset, length);
    }
    
    @Override
    public void setBytea(final int index, final InputStream stream, final int length) throws SQLException {
        if (index < 1 || index > this.paramValues.length) {
            throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { new Integer(index), new Integer(this.paramValues.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
        this.paramValues[index - 1] = new StreamWrapper(stream, length);
    }
    
    @Override
    public void setBytea(final int index, final InputStream stream) throws SQLException {
        if (index < 1 || index > this.paramValues.length) {
            throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { new Integer(index), new Integer(this.paramValues.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
        this.paramValues[index - 1] = new StreamWrapper(stream);
    }
    
    @Override
    public void setNull(final int index, final int oid) throws SQLException {
        throw new IllegalArgumentException("can't setNull() on a v2 fastpath parameter");
    }
    
    @Override
    public String toString(final int index) {
        if (index < 1 || index > this.paramValues.length) {
            throw new IllegalArgumentException("parameter " + index + " out of range");
        }
        return "<fastpath parameter>";
    }
    
    private void copyStream(final PGStream pgStream, final StreamWrapper wrapper) throws IOException {
        final byte[] rawData = wrapper.getBytes();
        if (rawData != null) {
            pgStream.Send(rawData, wrapper.getOffset(), wrapper.getLength());
            return;
        }
        pgStream.SendStream(wrapper.getStream(), wrapper.getLength());
    }
    
    void writeV2FastpathValue(int index, final PGStream pgStream) throws IOException {
        --index;
        if (this.paramValues[index] instanceof StreamWrapper) {
            final StreamWrapper wrapper = (StreamWrapper)this.paramValues[index];
            pgStream.SendInteger4(wrapper.getLength());
            this.copyStream(pgStream, wrapper);
        }
        else if (this.paramValues[index] instanceof byte[]) {
            final byte[] data = (byte[])this.paramValues[index];
            pgStream.SendInteger4(data.length);
            pgStream.Send(data);
        }
        else {
            if (!(this.paramValues[index] instanceof String)) {
                throw new IllegalArgumentException("don't know how to stream parameter " + index);
            }
            final byte[] data = pgStream.getEncoding().encode((String)this.paramValues[index]);
            pgStream.SendInteger4(data.length);
            pgStream.Send(data);
        }
    }
    
    void checkAllParametersSet() throws SQLException {
        for (int i = 0; i < this.paramValues.length; ++i) {
            if (this.paramValues[i] == null) {
                throw new PSQLException(GT.tr("No value specified for parameter {0}.", new Integer(i + 1)), PSQLState.INVALID_PARAMETER_VALUE);
            }
        }
    }
    
    @Override
    public ParameterList copy() {
        final FastpathParameterList newCopy = new FastpathParameterList(this.paramValues.length);
        System.arraycopy(this.paramValues, 0, newCopy.paramValues, 0, this.paramValues.length);
        return newCopy;
    }
    
    @Override
    public void clear() {
        Arrays.fill(this.paramValues, null);
    }
    
    @Override
    public void setBinaryParameter(final int index, final byte[] value, final int oid) {
        throw new UnsupportedOperationException();
    }
}
