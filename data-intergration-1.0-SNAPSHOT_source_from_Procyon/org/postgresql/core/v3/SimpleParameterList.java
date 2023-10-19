// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import java.util.Arrays;
import org.postgresql.core.ParameterList;
import java.io.IOException;
import org.postgresql.core.PGStream;
import org.postgresql.core.Utils;
import java.io.InputStream;
import org.postgresql.util.StreamWrapper;
import org.postgresql.util.ByteConverter;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;

class SimpleParameterList implements V3ParameterList
{
    private static final int IN = 1;
    private static final int OUT = 2;
    private static final int INOUT = 3;
    private static final int TEXT = 0;
    private static final int BINARY = 4;
    private final Object[] paramValues;
    private final int[] paramTypes;
    private final int[] flags;
    private final byte[][] encoded;
    private final ProtocolConnectionImpl protoConnection;
    private static final Object NULL_OBJECT;
    
    SimpleParameterList(final int paramCount, final ProtocolConnectionImpl protoConnection) {
        this.paramValues = new Object[paramCount];
        this.paramTypes = new int[paramCount];
        this.encoded = new byte[paramCount][];
        this.flags = new int[paramCount];
        this.protoConnection = protoConnection;
    }
    
    @Override
    public void registerOutParameter(final int index, final int sqlType) throws SQLException {
        if (index < 1 || index > this.paramValues.length) {
            throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { new Integer(index), new Integer(this.paramValues.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
        final int[] flags = this.flags;
        final int n = index - 1;
        flags[n] |= 0x2;
    }
    
    private void bind(int index, final Object value, final int oid, final int binary) throws SQLException {
        if (index < 1 || index > this.paramValues.length) {
            throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { new Integer(index), new Integer(this.paramValues.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
        --index;
        this.encoded[index] = null;
        this.paramValues[index] = value;
        this.flags[index] = (this.direction(index) | 0x1 | binary);
        if (oid == 0 && this.paramTypes[index] != 0 && value == SimpleParameterList.NULL_OBJECT) {
            return;
        }
        this.paramTypes[index] = oid;
    }
    
    @Override
    public int getParameterCount() {
        return this.paramValues.length;
    }
    
    @Override
    public int getOutParameterCount() {
        int count = 0;
        int i = this.paramTypes.length;
        while (--i >= 0) {
            if ((this.direction(i) & 0x2) == 0x2) {
                ++count;
            }
        }
        if (count == 0) {
            count = 1;
        }
        return count;
    }
    
    @Override
    public int getInParameterCount() {
        int count = 0;
        for (int i = 0; i < this.paramTypes.length; ++i) {
            if (this.direction(i) != 2) {
                ++count;
            }
        }
        return count;
    }
    
    @Override
    public void setIntParameter(final int index, final int value) throws SQLException {
        final byte[] data = new byte[4];
        ByteConverter.int4(data, 0, value);
        this.bind(index, data, 23, 4);
    }
    
    @Override
    public void setLiteralParameter(final int index, final String value, final int oid) throws SQLException {
        this.bind(index, value, oid, 0);
    }
    
    @Override
    public void setStringParameter(final int index, final String value, final int oid) throws SQLException {
        this.bind(index, value, oid, 0);
    }
    
    @Override
    public void setBinaryParameter(final int index, final byte[] value, final int oid) throws SQLException {
        this.bind(index, value, oid, 4);
    }
    
    @Override
    public void setBytea(final int index, final byte[] data, final int offset, final int length) throws SQLException {
        this.bind(index, new StreamWrapper(data, offset, length), 17, 4);
    }
    
    @Override
    public void setBytea(final int index, final InputStream stream, final int length) throws SQLException {
        this.bind(index, new StreamWrapper(stream, length), 17, 4);
    }
    
    @Override
    public void setBytea(final int index, final InputStream stream) throws SQLException {
        this.bind(index, new StreamWrapper(stream), 17, 4);
    }
    
    @Override
    public void setNull(final int index, final int oid) throws SQLException {
        int binaryTransfer = 0;
        if (this.protoConnection.useBinaryForReceive(oid)) {
            binaryTransfer = 4;
        }
        this.bind(index, SimpleParameterList.NULL_OBJECT, oid, binaryTransfer);
    }
    
    @Override
    public String toString(int index) {
        --index;
        if (this.paramValues[index] == null) {
            return "?";
        }
        if (this.paramValues[index] == SimpleParameterList.NULL_OBJECT) {
            return "NULL";
        }
        if ((this.flags[index] & 0x4) != 0x4) {
            final String param = this.paramValues[index].toString();
            final boolean hasBackslash = param.indexOf(92) != -1;
            StringBuilder p = new StringBuilder(3 + param.length() * 11 / 10);
            boolean standardConformingStrings = false;
            boolean supportsEStringSyntax = false;
            if (this.protoConnection != null) {
                standardConformingStrings = this.protoConnection.getStandardConformingStrings();
                supportsEStringSyntax = (this.protoConnection.getServerVersionNum() >= 80100);
            }
            if (hasBackslash && !standardConformingStrings && supportsEStringSyntax) {
                p.append('E');
            }
            p.append('\'');
            try {
                p = Utils.escapeLiteral(p, param, standardConformingStrings);
            }
            catch (SQLException sqle) {
                p.append(param);
            }
            p.append('\'');
            return p.toString();
        }
        switch (this.paramTypes[index]) {
            case 21: {
                final short s = ByteConverter.int2((byte[])this.paramValues[index], 0);
                return Short.toString(s);
            }
            case 23: {
                final int i = ByteConverter.int4((byte[])this.paramValues[index], 0);
                return Integer.toString(i);
            }
            case 20: {
                final long l = ByteConverter.int8((byte[])this.paramValues[index], 0);
                return Long.toString(l);
            }
            case 700: {
                final float f = ByteConverter.float4((byte[])this.paramValues[index], 0);
                return Float.toString(f);
            }
            case 701: {
                final double d = ByteConverter.float8((byte[])this.paramValues[index], 0);
                return Double.toString(d);
            }
            default: {
                return "?";
            }
        }
    }
    
    @Override
    public void checkAllParametersSet() throws SQLException {
        for (int i = 0; i < this.paramTypes.length; ++i) {
            if (this.direction(i) != 2 && this.paramValues[i] == null) {
                throw new PSQLException(GT.tr("No value specified for parameter {0}.", new Integer(i + 1)), PSQLState.INVALID_PARAMETER_VALUE);
            }
        }
    }
    
    @Override
    public void convertFunctionOutParameters() {
        for (int i = 0; i < this.paramTypes.length; ++i) {
            if (this.direction(i) == 2) {
                this.paramTypes[i] = 2278;
                this.paramValues[i] = "null";
            }
        }
    }
    
    private static void streamBytea(final PGStream pgStream, final StreamWrapper wrapper) throws IOException {
        final byte[] rawData = wrapper.getBytes();
        if (rawData != null) {
            pgStream.Send(rawData, wrapper.getOffset(), wrapper.getLength());
            return;
        }
        pgStream.SendStream(wrapper.getStream(), wrapper.getLength());
    }
    
    @Override
    public int[] getTypeOIDs() {
        return this.paramTypes;
    }
    
    int getTypeOID(final int index) {
        return this.paramTypes[index - 1];
    }
    
    boolean hasUnresolvedTypes() {
        for (int i = 0; i < this.paramTypes.length; ++i) {
            if (this.paramTypes[i] == 0) {
                return true;
            }
        }
        return false;
    }
    
    void setResolvedType(final int index, final int oid) {
        if (this.paramTypes[index - 1] == 0) {
            this.paramTypes[index - 1] = oid;
        }
        else if (this.paramTypes[index - 1] != oid) {
            throw new IllegalArgumentException("Can't change resolved type for param: " + index + " from " + this.paramTypes[index - 1] + " to " + oid);
        }
    }
    
    boolean isNull(final int index) {
        return this.paramValues[index - 1] == SimpleParameterList.NULL_OBJECT;
    }
    
    boolean isBinary(final int index) {
        return (this.flags[index - 1] & 0x4) != 0x0;
    }
    
    private int direction(final int index) {
        return this.flags[index] & 0x3;
    }
    
    int getV3Length(int index) {
        --index;
        if (this.paramValues[index] == SimpleParameterList.NULL_OBJECT) {
            throw new IllegalArgumentException("can't getV3Length() on a null parameter");
        }
        if (this.paramValues[index] instanceof byte[]) {
            return ((byte[])this.paramValues[index]).length;
        }
        if (this.paramValues[index] instanceof StreamWrapper) {
            return ((StreamWrapper)this.paramValues[index]).getLength();
        }
        if (this.encoded[index] == null) {
            this.encoded[index] = Utils.encodeUTF8(this.paramValues[index].toString());
        }
        return this.encoded[index].length;
    }
    
    void writeV3Value(int index, final PGStream pgStream) throws IOException {
        --index;
        if (this.paramValues[index] == SimpleParameterList.NULL_OBJECT) {
            throw new IllegalArgumentException("can't writeV3Value() on a null parameter");
        }
        if (this.paramValues[index] instanceof byte[]) {
            pgStream.Send((byte[])this.paramValues[index]);
            return;
        }
        if (this.paramValues[index] instanceof StreamWrapper) {
            streamBytea(pgStream, (StreamWrapper)this.paramValues[index]);
            return;
        }
        if (this.encoded[index] == null) {
            this.encoded[index] = Utils.encodeUTF8((String)this.paramValues[index]);
        }
        pgStream.Send(this.encoded[index]);
    }
    
    @Override
    public ParameterList copy() {
        final SimpleParameterList newCopy = new SimpleParameterList(this.paramValues.length, this.protoConnection);
        System.arraycopy(this.paramValues, 0, newCopy.paramValues, 0, this.paramValues.length);
        System.arraycopy(this.paramTypes, 0, newCopy.paramTypes, 0, this.paramTypes.length);
        System.arraycopy(this.flags, 0, newCopy.flags, 0, this.flags.length);
        return newCopy;
    }
    
    @Override
    public void clear() {
        Arrays.fill(this.paramValues, null);
        Arrays.fill(this.paramTypes, 0);
        Arrays.fill(this.encoded, null);
        Arrays.fill(this.flags, 0);
    }
    
    @Override
    public SimpleParameterList[] getSubparams() {
        return null;
    }
    
    static {
        NULL_OBJECT = new Object();
    }
}
