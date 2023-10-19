// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc;

import java.util.logging.Level;
import java.sql.Timestamp;
import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.List;
import org.postgresql.core.BaseStatement;
import org.postgresql.core.Field;
import org.postgresql.core.Tuple;
import java.util.ArrayList;
import java.sql.ResultSet;
import org.postgresql.jdbc2.ArrayAssistant;
import org.postgresql.core.Encoding;
import org.postgresql.jdbc2.ArrayAssistantRegistry;
import java.io.IOException;
import org.postgresql.util.ByteConverter;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.Driver;
import java.util.Map;
import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import java.sql.Array;

public class PgArray implements Array
{
    protected BaseConnection connection;
    private int oid;
    protected String fieldString;
    private final boolean useObjects;
    protected PgArrayList arrayList;
    protected byte[] fieldBytes;
    
    private PgArray(final BaseConnection connection, final int oid) throws SQLException {
        this.connection = null;
        this.fieldString = null;
        this.connection = connection;
        this.oid = oid;
        this.useObjects = true;
    }
    
    public PgArray(final BaseConnection connection, final int oid, final String fieldString) throws SQLException {
        this(connection, oid);
        this.fieldString = fieldString;
    }
    
    public PgArray(final BaseConnection connection, final int oid, final byte[] fieldBytes) throws SQLException {
        this(connection, oid);
        this.fieldBytes = fieldBytes;
    }
    
    @Override
    public Object getArray() throws SQLException {
        return this.getArrayImpl(1L, 0, null);
    }
    
    @Override
    public Object getArray(final long index, final int count) throws SQLException {
        return this.getArrayImpl(index, count, null);
    }
    
    public Object getArrayImpl(final Map<String, Class<?>> map) throws SQLException {
        return this.getArrayImpl(1L, 0, map);
    }
    
    @Override
    public Object getArray(final Map<String, Class<?>> map) throws SQLException {
        return this.getArrayImpl(map);
    }
    
    @Override
    public Object getArray(final long index, final int count, final Map<String, Class<?>> map) throws SQLException {
        return this.getArrayImpl(index, count, map);
    }
    
    public Object getArrayImpl(long index, int count, final Map<String, Class<?>> map) throws SQLException {
        if (map != null && !map.isEmpty()) {
            throw Driver.notImplemented(this.getClass(), "getArrayImpl(long,int,Map)");
        }
        if (index < 1L) {
            throw new PSQLException(GT.tr("The array index is out of range: {0}", index), PSQLState.DATA_ERROR);
        }
        if (this.fieldBytes != null) {
            return this.readBinaryArray((int)index, count);
        }
        if (this.fieldString == null) {
            return null;
        }
        this.buildArrayList();
        if (count == 0) {
            count = this.arrayList.size();
        }
        if (--index + count > this.arrayList.size()) {
            throw new PSQLException(GT.tr("The array index is out of range: {0}, number of elements: {1}.", index + count, this.arrayList.size()), PSQLState.DATA_ERROR);
        }
        return this.buildArray(this.arrayList, (int)index, count);
    }
    
    private Object readBinaryArray(final int index, final int count) throws SQLException {
        final int dimensions = ByteConverter.int4(this.fieldBytes, 0);
        final int elementOid = ByteConverter.int4(this.fieldBytes, 8);
        int pos = 12;
        final int[] dims = new int[dimensions];
        for (int d = 0; d < dimensions; ++d) {
            dims[d] = ByteConverter.int4(this.fieldBytes, pos);
            pos += 4;
            pos += 4;
        }
        if (dimensions == 0) {
            return java.lang.reflect.Array.newInstance(this.elementOidToClass(elementOid), 0);
        }
        if (count > 0) {
            dims[0] = Math.min(count, dims[0]);
        }
        final Object arr = java.lang.reflect.Array.newInstance(this.elementOidToClass(elementOid), dims);
        try {
            this.storeValues((Object[])arr, elementOid, dims, pos, 0, index);
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", new Object[0]), PSQLState.DATA_ERROR, ioe);
        }
        return arr;
    }
    
    private int storeValues(final Object[] arr, final int elementOid, final int[] dims, int pos, final int thisDimension, final int index) throws SQLException, IOException {
        if (thisDimension == dims.length - 1) {
            for (int i = 1; i < index; ++i) {
                final int len = ByteConverter.int4(this.fieldBytes, pos);
                pos += 4;
                if (len != -1) {
                    pos += len;
                }
            }
            for (int i = 0; i < dims[thisDimension]; ++i) {
                final int len = ByteConverter.int4(this.fieldBytes, pos);
                pos += 4;
                if (len != -1) {
                    switch (elementOid) {
                        case 21: {
                            arr[i] = ByteConverter.int2(this.fieldBytes, pos);
                            break;
                        }
                        case 23: {
                            arr[i] = ByteConverter.int4(this.fieldBytes, pos);
                            break;
                        }
                        case 20: {
                            arr[i] = ByteConverter.int8(this.fieldBytes, pos);
                            break;
                        }
                        case 700: {
                            arr[i] = ByteConverter.float4(this.fieldBytes, pos);
                            break;
                        }
                        case 701: {
                            arr[i] = ByteConverter.float8(this.fieldBytes, pos);
                            break;
                        }
                        case 1700: {
                            arr[i] = ByteConverter.numeric(this.fieldBytes, pos, len);
                            break;
                        }
                        case 25:
                        case 1043: {
                            final Encoding encoding = this.connection.getEncoding();
                            arr[i] = encoding.decode(this.fieldBytes, pos, len);
                            break;
                        }
                        case 16: {
                            arr[i] = ByteConverter.bool(this.fieldBytes, pos);
                            break;
                        }
                        default: {
                            final ArrayAssistant arrAssistant = ArrayAssistantRegistry.getAssistant(elementOid);
                            if (arrAssistant != null) {
                                arr[i] = arrAssistant.buildElement(this.fieldBytes, pos, len);
                                break;
                            }
                            break;
                        }
                    }
                    pos += len;
                }
            }
        }
        else {
            for (int i = 0; i < dims[thisDimension]; ++i) {
                pos = this.storeValues((Object[])arr[i], elementOid, dims, pos, thisDimension + 1, 0);
            }
        }
        return pos;
    }
    
    private ResultSet readBinaryResultSet(final int index, final int count) throws SQLException {
        final int dimensions = ByteConverter.int4(this.fieldBytes, 0);
        final int elementOid = ByteConverter.int4(this.fieldBytes, 8);
        int pos = 12;
        final int[] dims = new int[dimensions];
        for (int d = 0; d < dimensions; ++d) {
            dims[d] = ByteConverter.int4(this.fieldBytes, pos);
            pos += 4;
            pos += 4;
        }
        if (count > 0 && dimensions > 0) {
            dims[0] = Math.min(count, dims[0]);
        }
        final List<Tuple> rows = new ArrayList<Tuple>();
        final Field[] fields = new Field[2];
        this.storeValues(rows, fields, elementOid, dims, pos, 0, index);
        final BaseStatement stat = (BaseStatement)this.connection.createStatement(1004, 1007);
        return stat.createDriverResultSet(fields, rows);
    }
    
    private int storeValues(final List<Tuple> rows, final Field[] fields, final int elementOid, final int[] dims, int pos, final int thisDimension, final int index) throws SQLException {
        if (dims.length == 0) {
            (fields[0] = new Field("INDEX", 23)).setFormat(1);
            (fields[1] = new Field("VALUE", elementOid)).setFormat(1);
            for (int i = 1; i < index; ++i) {
                final int len = ByteConverter.int4(this.fieldBytes, pos);
                pos += 4;
                if (len != -1) {
                    pos += len;
                }
            }
        }
        else if (thisDimension == dims.length - 1) {
            (fields[0] = new Field("INDEX", 23)).setFormat(1);
            (fields[1] = new Field("VALUE", elementOid)).setFormat(1);
            for (int i = 1; i < index; ++i) {
                final int len = ByteConverter.int4(this.fieldBytes, pos);
                pos += 4;
                if (len != -1) {
                    pos += len;
                }
            }
            for (int i = 0; i < dims[thisDimension]; ++i) {
                final byte[][] rowData = { new byte[4], null };
                ByteConverter.int4(rowData[0], 0, i + index);
                rows.add(new Tuple(rowData));
                final int len2 = ByteConverter.int4(this.fieldBytes, pos);
                pos += 4;
                if (len2 != -1) {
                    rowData[1] = new byte[len2];
                    System.arraycopy(this.fieldBytes, pos, rowData[1], 0, rowData[1].length);
                    pos += len2;
                }
            }
        }
        else {
            (fields[0] = new Field("INDEX", 23)).setFormat(1);
            (fields[1] = new Field("VALUE", this.oid)).setFormat(1);
            final int nextDimension = thisDimension + 1;
            final int dimensionsLeft = dims.length - nextDimension;
            for (int j = 1; j < index; ++j) {
                pos = this.calcRemainingDataLength(dims, pos, elementOid, nextDimension);
            }
            for (int j = 0; j < dims[thisDimension]; ++j) {
                final byte[][] rowData2 = { new byte[4], null };
                ByteConverter.int4(rowData2[0], 0, j + index);
                rows.add(new Tuple(rowData2));
                final int dataEndPos = this.calcRemainingDataLength(dims, pos, elementOid, nextDimension);
                final int dataLength = dataEndPos - pos;
                ByteConverter.int4(rowData2[1] = new byte[12 + 8 * dimensionsLeft + dataLength], 0, dimensionsLeft);
                System.arraycopy(this.fieldBytes, 4, rowData2[1], 4, 8);
                System.arraycopy(this.fieldBytes, 12 + nextDimension * 8, rowData2[1], 12, dimensionsLeft * 8);
                System.arraycopy(this.fieldBytes, pos, rowData2[1], 12 + dimensionsLeft * 8, dataLength);
                pos = dataEndPos;
            }
        }
        return pos;
    }
    
    private int calcRemainingDataLength(final int[] dims, int pos, final int elementOid, final int thisDimension) {
        if (thisDimension == dims.length - 1) {
            for (int i = 0; i < dims[thisDimension]; ++i) {
                final int len = ByteConverter.int4(this.fieldBytes, pos);
                pos += 4;
                if (len != -1) {
                    pos += len;
                }
            }
        }
        else {
            pos = this.calcRemainingDataLength(dims, elementOid, pos, thisDimension + 1);
        }
        return pos;
    }
    
    private Class<?> elementOidToClass(final int oid) throws SQLException {
        switch (oid) {
            case 21: {
                return Short.class;
            }
            case 23: {
                return Integer.class;
            }
            case 20: {
                return Long.class;
            }
            case 700: {
                return Float.class;
            }
            case 701: {
                return Double.class;
            }
            case 1700: {
                return BigDecimal.class;
            }
            case 25:
            case 1043: {
                return String.class;
            }
            case 16: {
                return Boolean.class;
            }
            default: {
                final ArrayAssistant arrElemBuilder = ArrayAssistantRegistry.getAssistant(oid);
                if (arrElemBuilder != null) {
                    return arrElemBuilder.baseType();
                }
                throw Driver.notImplemented(this.getClass(), "readBinaryArray(data,oid)");
            }
        }
    }
    
    private synchronized void buildArrayList() throws SQLException {
        if (this.arrayList != null) {
            return;
        }
        this.arrayList = new PgArrayList();
        final char delim = this.connection.getTypeInfo().getArrayDelimiter(this.oid);
        if (this.fieldString != null) {
            final char[] chars = this.fieldString.toCharArray();
            StringBuilder buffer = null;
            boolean insideString = false;
            boolean wasInsideString = false;
            final List<PgArrayList> dims = new ArrayList<PgArrayList>();
            PgArrayList curArray = this.arrayList;
            int startOffset = 0;
            if (chars[0] == '[') {
                while (chars[startOffset] != '=') {
                    ++startOffset;
                }
                ++startOffset;
            }
            for (int i = startOffset; i < chars.length; ++i) {
                if (chars[i] == '\\') {
                    ++i;
                }
                else {
                    if (!insideString && chars[i] == '{') {
                        if (dims.isEmpty()) {
                            dims.add(this.arrayList);
                        }
                        else {
                            final PgArrayList a = new PgArrayList();
                            final PgArrayList p = dims.get(dims.size() - 1);
                            ((ArrayList<PgArrayList>)p).add(a);
                            dims.add(a);
                        }
                        curArray = dims.get(dims.size() - 1);
                        for (int t = i + 1; t < chars.length; ++t) {
                            if (!Character.isWhitespace(chars[t])) {
                                if (chars[t] != '{') {
                                    break;
                                }
                                final PgArrayList list = curArray;
                                ++list.dimensionsCount;
                            }
                        }
                        buffer = new StringBuilder();
                        continue;
                    }
                    if (chars[i] == '\"') {
                        insideString = !insideString;
                        wasInsideString = true;
                        continue;
                    }
                    if (!insideString && Character.isWhitespace(chars[i])) {
                        continue;
                    }
                    if ((!insideString && (chars[i] == delim || chars[i] == '}')) || i == chars.length - 1) {
                        if (chars[i] != '\"' && chars[i] != '}' && chars[i] != delim && buffer != null) {
                            buffer.append(chars[i]);
                        }
                        final String b = (buffer == null) ? null : buffer.toString();
                        if (b != null && (!b.isEmpty() || wasInsideString)) {
                            ((ArrayList<String>)curArray).add((!wasInsideString && b.equals("NULL")) ? null : b);
                        }
                        wasInsideString = false;
                        buffer = new StringBuilder();
                        if (chars[i] == '}') {
                            dims.remove(dims.size() - 1);
                            if (!dims.isEmpty()) {
                                curArray = dims.get(dims.size() - 1);
                            }
                            buffer = null;
                        }
                        continue;
                    }
                }
                if (buffer != null) {
                    buffer.append(chars[i]);
                }
            }
        }
    }
    
    private Object buildArray(final PgArrayList input, int index, int count) throws SQLException {
        if (count < 0) {
            count = input.size();
        }
        Object ret = null;
        final int dims = input.dimensionsCount;
        final int[] dimsLength = (int[])((dims > 1) ? new int[dims] : null);
        if (dims > 1) {
            for (int i = 0; i < dims; ++i) {
                dimsLength[i] = ((i == 0) ? count : 0);
            }
        }
        int length = 0;
        final int type = this.connection.getTypeInfo().getSQLType(this.connection.getTypeInfo().getPGArrayElement(this.oid));
        if (type == -7) {
            boolean[] pa = null;
            Object[] oa = null;
            if (dims > 1 || this.useObjects) {
                oa = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(this.useObjects ? Boolean.class : Boolean.TYPE, dimsLength)) : new Boolean[count]));
            }
            else {
                pa = (boolean[])(ret = new boolean[count]);
            }
            while (count > 0) {
                final Object o = input.get(index++);
                if (dims > 1 || this.useObjects) {
                    oa[length++] = ((o == null) ? null : ((dims > 1) ? this.buildArray((PgArrayList)o, 0, -1) : Boolean.valueOf(BooleanTypeUtil.castToBoolean(o))));
                }
                else {
                    pa[length++] = (o != null && BooleanTypeUtil.castToBoolean(o));
                }
                --count;
            }
        }
        else if (type == 5) {
            short[] pa2 = null;
            Object[] oa = null;
            if (dims > 1 || this.useObjects) {
                oa = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(this.useObjects ? Short.class : Short.TYPE, dimsLength)) : new Short[count]));
            }
            else {
                pa2 = (short[])(ret = new short[count]);
            }
            while (count > 0) {
                final Object o = input.get(index++);
                if (dims > 1 || this.useObjects) {
                    oa[length++] = ((o == null) ? null : ((dims > 1) ? this.buildArray((PgArrayList)o, 0, -1) : Short.valueOf(PgResultSet.toShort((String)o))));
                }
                else {
                    pa2[length++] = (short)((o == null) ? 0 : PgResultSet.toShort((String)o));
                }
                --count;
            }
        }
        else if (type == 4) {
            int[] pa3 = null;
            Object[] oa = null;
            if (dims > 1 || this.useObjects) {
                oa = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(this.useObjects ? Integer.class : Integer.TYPE, dimsLength)) : new Integer[count]));
            }
            else {
                pa3 = (int[])(ret = new int[count]);
            }
            while (count > 0) {
                final Object o = input.get(index++);
                if (dims > 1 || this.useObjects) {
                    oa[length++] = ((o == null) ? null : ((dims > 1) ? this.buildArray((PgArrayList)o, 0, -1) : Integer.valueOf(PgResultSet.toInt((String)o))));
                }
                else {
                    pa3[length++] = ((o == null) ? 0 : PgResultSet.toInt((String)o));
                }
                --count;
            }
        }
        else if (type == -5) {
            long[] pa4 = null;
            Object[] oa = null;
            if (dims > 1 || this.useObjects) {
                oa = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(this.useObjects ? Long.class : Long.TYPE, dimsLength)) : new Long[count]));
            }
            else {
                pa4 = (long[])(ret = new long[count]);
            }
            while (count > 0) {
                final Object o = input.get(index++);
                if (dims > 1 || this.useObjects) {
                    oa[length++] = ((o == null) ? null : ((dims > 1) ? this.buildArray((PgArrayList)o, 0, -1) : Long.valueOf(PgResultSet.toLong((String)o))));
                }
                else {
                    pa4[length++] = ((o == null) ? 0L : PgResultSet.toLong((String)o));
                }
                --count;
            }
        }
        else if (type == 2) {
            Object[] oa2 = null;
            oa2 = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(BigDecimal.class, dimsLength)) : new BigDecimal[count]));
            while (count > 0) {
                final Object v = input.get(index++);
                oa2[length++] = ((dims > 1 && v != null) ? this.buildArray((PgArrayList)v, 0, -1) : ((v == null) ? null : PgResultSet.toBigDecimal((String)v)));
                --count;
            }
        }
        else if (type == 7) {
            float[] pa5 = null;
            Object[] oa = null;
            if (dims > 1 || this.useObjects) {
                oa = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(this.useObjects ? Float.class : Float.TYPE, dimsLength)) : new Float[count]));
            }
            else {
                pa5 = (float[])(ret = new float[count]);
            }
            while (count > 0) {
                final Object o = input.get(index++);
                if (dims > 1 || this.useObjects) {
                    oa[length++] = ((o == null) ? null : ((dims > 1) ? this.buildArray((PgArrayList)o, 0, -1) : Float.valueOf(PgResultSet.toFloat((String)o))));
                }
                else {
                    pa5[length++] = ((o == null) ? 0.0f : PgResultSet.toFloat((String)o));
                }
                --count;
            }
        }
        else if (type == 8) {
            double[] pa6 = null;
            Object[] oa = null;
            if (dims > 1 || this.useObjects) {
                oa = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(this.useObjects ? Double.class : Double.TYPE, dimsLength)) : new Double[count]));
            }
            else {
                pa6 = (double[])(ret = new double[count]);
            }
            while (count > 0) {
                final Object o = input.get(index++);
                if (dims > 1 || this.useObjects) {
                    oa[length++] = ((o == null) ? null : ((dims > 1) ? this.buildArray((PgArrayList)o, 0, -1) : Double.valueOf(PgResultSet.toDouble((String)o))));
                }
                else {
                    pa6[length++] = ((o == null) ? 0.0 : PgResultSet.toDouble((String)o));
                }
                --count;
            }
        }
        else if (type == 1 || type == 12 || this.oid == 3807) {
            Object[] oa2 = null;
            oa2 = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(String.class, dimsLength)) : new String[count]));
            while (count > 0) {
                final Object v = input.get(index++);
                oa2[length++] = ((dims > 1 && v != null) ? this.buildArray((PgArrayList)v, 0, -1) : v);
                --count;
            }
        }
        else if (type == 91) {
            Object[] oa2 = null;
            oa2 = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(Date.class, dimsLength)) : new Date[count]));
            while (count > 0) {
                final Object v = input.get(index++);
                oa2[length++] = ((dims > 1 && v != null) ? this.buildArray((PgArrayList)v, 0, -1) : ((v == null) ? null : this.connection.getTimestampUtils().toDate(null, (String)v)));
                --count;
            }
        }
        else if (type == 92) {
            Object[] oa2 = null;
            oa2 = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(Time.class, dimsLength)) : new Time[count]));
            while (count > 0) {
                final Object v = input.get(index++);
                oa2[length++] = ((dims > 1 && v != null) ? this.buildArray((PgArrayList)v, 0, -1) : ((v == null) ? null : this.connection.getTimestampUtils().toTime(null, (String)v)));
                --count;
            }
        }
        else if (type == 93) {
            Object[] oa2 = null;
            oa2 = (Object[])(ret = ((dims > 1) ? ((Object[])java.lang.reflect.Array.newInstance(Timestamp.class, dimsLength)) : new Timestamp[count]));
            while (count > 0) {
                final Object v = input.get(index++);
                oa2[length++] = ((dims > 1 && v != null) ? this.buildArray((PgArrayList)v, 0, -1) : ((v == null) ? null : this.connection.getTimestampUtils().toTimestamp(null, (String)v)));
                --count;
            }
        }
        else if (ArrayAssistantRegistry.getAssistant(this.oid) != null) {
            final ArrayAssistant arrAssistant = ArrayAssistantRegistry.getAssistant(this.oid);
            Object[] oa = null;
            oa = (Object[])(ret = ((dims > 1) ? java.lang.reflect.Array.newInstance(arrAssistant.baseType(), dimsLength) : ((Object[])java.lang.reflect.Array.newInstance(arrAssistant.baseType(), count))));
            while (count > 0) {
                final Object v2 = input.get(index++);
                oa[length++] = ((dims > 1 && v2 != null) ? this.buildArray((PgArrayList)v2, 0, -1) : ((v2 == null) ? null : arrAssistant.buildElement((String)v2)));
                --count;
            }
        }
        else {
            if (dims != 1) {
                this.connection.getLogger().log(Level.FINEST, "getArrayImpl(long,int,Map) with {0}", this.getBaseTypeName());
                throw Driver.notImplemented(this.getClass(), "getArrayImpl(long,int,Map)");
            }
            final Object[] oa2 = new Object[count];
            final String typeName = this.getBaseTypeName();
            while (count > 0) {
                final Object v2 = input.get(index++);
                if (v2 instanceof String) {
                    oa2[length++] = this.connection.getObject(typeName, (String)v2, null);
                }
                else if (v2 instanceof byte[]) {
                    oa2[length++] = this.connection.getObject(typeName, null, (byte[])v2);
                }
                else {
                    if (v2 != null) {
                        throw Driver.notImplemented(this.getClass(), "getArrayImpl(long,int,Map)");
                    }
                    oa2[length++] = null;
                }
                --count;
            }
            ret = oa2;
        }
        return ret;
    }
    
    @Override
    public int getBaseType() throws SQLException {
        return this.connection.getTypeInfo().getSQLType(this.getBaseTypeName());
    }
    
    @Override
    public String getBaseTypeName() throws SQLException {
        this.buildArrayList();
        final int elementOID = this.connection.getTypeInfo().getPGArrayElement(this.oid);
        return this.connection.getTypeInfo().getPGType(elementOID);
    }
    
    @Override
    public ResultSet getResultSet() throws SQLException {
        return this.getResultSetImpl(1L, 0, null);
    }
    
    @Override
    public ResultSet getResultSet(final long index, final int count) throws SQLException {
        return this.getResultSetImpl(index, count, null);
    }
    
    @Override
    public ResultSet getResultSet(final Map<String, Class<?>> map) throws SQLException {
        return this.getResultSetImpl(map);
    }
    
    @Override
    public ResultSet getResultSet(final long index, final int count, final Map<String, Class<?>> map) throws SQLException {
        return this.getResultSetImpl(index, count, map);
    }
    
    public ResultSet getResultSetImpl(final Map<String, Class<?>> map) throws SQLException {
        return this.getResultSetImpl(1L, 0, map);
    }
    
    public ResultSet getResultSetImpl(long index, int count, final Map<String, Class<?>> map) throws SQLException {
        if (map != null && !map.isEmpty()) {
            throw Driver.notImplemented(this.getClass(), "getResultSetImpl(long,int,Map)");
        }
        if (index < 1L) {
            throw new PSQLException(GT.tr("The array index is out of range: {0}", index), PSQLState.DATA_ERROR);
        }
        if (this.fieldBytes != null) {
            return this.readBinaryResultSet((int)index, count);
        }
        this.buildArrayList();
        if (count == 0) {
            count = this.arrayList.size();
        }
        if (--index + count > this.arrayList.size()) {
            throw new PSQLException(GT.tr("The array index is out of range: {0}, number of elements: {1}.", index + count, this.arrayList.size()), PSQLState.DATA_ERROR);
        }
        final List<Tuple> rows = new ArrayList<Tuple>();
        final Field[] fields = new Field[2];
        if (this.arrayList.dimensionsCount <= 1) {
            final int baseOid = this.connection.getTypeInfo().getPGArrayElement(this.oid);
            fields[0] = new Field("INDEX", 23);
            fields[1] = new Field("VALUE", baseOid);
            for (int i = 0; i < count; ++i) {
                final int offset = (int)index + i;
                final byte[][] t = new byte[2][0];
                final String v = ((ArrayList<String>)this.arrayList).get(offset);
                t[0] = this.connection.encodeString(Integer.toString(offset + 1));
                t[1] = (byte[])((v == null) ? null : this.connection.encodeString(v));
                rows.add(new Tuple(t));
            }
        }
        else {
            fields[0] = new Field("INDEX", 23);
            fields[1] = new Field("VALUE", this.oid);
            for (int j = 0; j < count; ++j) {
                final int offset2 = (int)index + j;
                final byte[][] t2 = new byte[2][0];
                final Object v2 = this.arrayList.get(offset2);
                t2[0] = this.connection.encodeString(Integer.toString(offset2 + 1));
                t2[1] = (byte[])((v2 == null) ? null : this.connection.encodeString(this.toString((PgArrayList)v2)));
                rows.add(new Tuple(t2));
            }
        }
        final BaseStatement stat = (BaseStatement)this.connection.createStatement(1004, 1007);
        return stat.createDriverResultSet(fields, rows);
    }
    
    @Override
    public String toString() {
        if (this.fieldString == null && this.fieldBytes != null) {
            try {
                final Object array = this.readBinaryArray(1, 0);
                final PrimitiveArraySupport arraySupport = PrimitiveArraySupport.getArraySupport(array);
                if (arraySupport != null) {
                    this.fieldString = arraySupport.toArrayString(this.connection.getTypeInfo().getArrayDelimiter(this.oid), array);
                }
                else {
                    final Array tmpArray = this.connection.createArrayOf(this.getBaseTypeName(), (Object[])array);
                    this.fieldString = tmpArray.toString();
                }
            }
            catch (SQLException e) {
                this.fieldString = "NULL";
            }
        }
        return this.fieldString;
    }
    
    private String toString(final PgArrayList list) throws SQLException {
        if (list == null) {
            return "NULL";
        }
        final StringBuilder b = new StringBuilder().append('{');
        final char delim = this.connection.getTypeInfo().getArrayDelimiter(this.oid);
        for (int i = 0; i < list.size(); ++i) {
            final Object v = list.get(i);
            if (i > 0) {
                b.append(delim);
            }
            if (v == null) {
                b.append("NULL");
            }
            else if (v instanceof PgArrayList) {
                b.append(this.toString((PgArrayList)v));
            }
            else {
                escapeArrayElement(b, (String)v);
            }
        }
        b.append('}');
        return b.toString();
    }
    
    public static void escapeArrayElement(final StringBuilder b, final String s) {
        b.append('\"');
        for (int j = 0; j < s.length(); ++j) {
            final char c = s.charAt(j);
            if (c == '\"' || c == '\\') {
                b.append('\\');
            }
            b.append(c);
        }
        b.append('\"');
    }
    
    public boolean isBinary() {
        return this.fieldBytes != null;
    }
    
    public byte[] toBytes() {
        return this.fieldBytes;
    }
    
    @Override
    public void free() throws SQLException {
        this.connection = null;
        this.fieldString = null;
        this.fieldBytes = null;
        this.arrayList = null;
    }
    
    static {
        ArrayAssistantRegistry.register(2950, new UUIDArrayAssistant());
        ArrayAssistantRegistry.register(2951, new UUIDArrayAssistant());
    }
    
    private static class PgArrayList extends ArrayList<Object>
    {
        private static final long serialVersionUID = 2052783752654562677L;
        int dimensionsCount;
        
        private PgArrayList() {
            this.dimensionsCount = 1;
        }
    }
}
