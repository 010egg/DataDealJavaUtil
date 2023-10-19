// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v2;

import java.util.Arrays;
import java.io.IOException;
import java.io.Writer;
import java.io.InputStream;
import org.postgresql.util.StreamWrapper;
import org.postgresql.core.Utils;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.SQLException;
import org.postgresql.core.ParameterList;

class SimpleParameterList implements ParameterList
{
    private final Object[] paramValues;
    private final boolean useEStringSyntax;
    private static final String NULL_OBJECT;
    
    SimpleParameterList(final int paramCount, final boolean useEStringSyntax) {
        this.paramValues = new Object[paramCount];
        this.useEStringSyntax = useEStringSyntax;
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
    public int getParameterCount() {
        return this.paramValues.length;
    }
    
    @Override
    public int getOutParameterCount() {
        return 1;
    }
    
    @Override
    public int[] getTypeOIDs() {
        return null;
    }
    
    @Override
    public void setIntParameter(final int index, final int value) throws SQLException {
        this.setLiteralParameter(index, "" + value, 23);
    }
    
    @Override
    public void setLiteralParameter(final int index, final String value, final int oid) throws SQLException {
        if (index < 1 || index > this.paramValues.length) {
            throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { new Integer(index), new Integer(this.paramValues.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
        this.paramValues[index - 1] = value;
    }
    
    @Override
    public void setStringParameter(final int index, final String value, final int oid) throws SQLException {
        final StringBuilder sbuf = new StringBuilder(2 + value.length() * 11 / 10);
        if (this.useEStringSyntax) {
            sbuf.append(' ').append('E');
        }
        sbuf.append('\'');
        Utils.escapeLiteral(sbuf, value, false);
        sbuf.append('\'');
        this.setLiteralParameter(index, sbuf.toString(), oid);
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
        if (index < 1 || index > this.paramValues.length) {
            throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { new Integer(index), new Integer(this.paramValues.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
        this.paramValues[index - 1] = SimpleParameterList.NULL_OBJECT;
    }
    
    @Override
    public String toString(final int index) {
        if (index < 1 || index > this.paramValues.length) {
            throw new IllegalArgumentException("Parameter index " + index + " out of range");
        }
        if (this.paramValues[index - 1] == null) {
            return "?";
        }
        if (this.paramValues[index - 1] == SimpleParameterList.NULL_OBJECT) {
            return "NULL";
        }
        return this.paramValues[index - 1].toString();
    }
    
    private void streamBytea(final StreamWrapper param, final Writer encodingWriter) throws IOException {
        final InputStream stream = param.getStream();
        final char[] buffer = { '\\', '\\', '\0', '\0', '\0' };
        if (this.useEStringSyntax) {
            encodingWriter.write(32);
            encodingWriter.write(69);
        }
        encodingWriter.write(39);
        for (int remaining = param.getLength(); remaining > 0; --remaining) {
            final int nextByte = stream.read();
            buffer[2] = (char)(48 + (nextByte >> 6 & 0x3));
            buffer[3] = (char)(48 + (nextByte >> 3 & 0x7));
            buffer[4] = (char)(48 + (nextByte & 0x7));
            encodingWriter.write(buffer, 0, 5);
        }
        encodingWriter.write(39);
    }
    
    void writeV2Value(final int index, final Writer encodingWriter) throws IOException {
        if (this.paramValues[index - 1] instanceof StreamWrapper) {
            this.streamBytea((StreamWrapper)this.paramValues[index - 1], encodingWriter);
        }
        else {
            encodingWriter.write((String)this.paramValues[index - 1]);
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
        final SimpleParameterList newCopy = new SimpleParameterList(this.paramValues.length, this.useEStringSyntax);
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
    
    static {
        NULL_OBJECT = new String("NULL");
    }
}
