// 
// Decompiled by Procyon v0.5.36
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.lang.reflect.Type;
import java.io.IOException;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.math.BigInteger;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

@JacksonStdImpl
public class NumberSerializer extends StdScalarSerializer<Number>
{
    public static final NumberSerializer instance;
    protected final boolean _isInt;
    
    public NumberSerializer(final Class<? extends Number> rawType) {
        super(rawType, false);
        this._isInt = (rawType == BigInteger.class);
    }
    
    @Override
    public void serialize(final Number value, final JsonGenerator g, final SerializerProvider provider) throws IOException {
        if (value instanceof BigDecimal) {
            g.writeNumber((BigDecimal)value);
        }
        else if (value instanceof BigInteger) {
            g.writeNumber((BigInteger)value);
        }
        else if (value instanceof Long) {
            g.writeNumber(value.longValue());
        }
        else if (value instanceof Double) {
            g.writeNumber(value.doubleValue());
        }
        else if (value instanceof Float) {
            g.writeNumber(value.floatValue());
        }
        else if (value instanceof Integer || value instanceof Byte || value instanceof Short) {
            g.writeNumber(value.intValue());
        }
        else {
            g.writeNumber(value.toString());
        }
    }
    
    @Override
    public JsonNode getSchema(final SerializerProvider provider, final Type typeHint) {
        return this.createSchemaNode(this._isInt ? "integer" : "number", true);
    }
    
    @Override
    public void acceptJsonFormatVisitor(final JsonFormatVisitorWrapper visitor, final JavaType typeHint) throws JsonMappingException {
        if (this._isInt) {
            this.visitIntFormat(visitor, typeHint, JsonParser.NumberType.BIG_INTEGER);
        }
        else {
            final Class<?> h = this.handledType();
            if (h == BigDecimal.class) {
                this.visitFloatFormat(visitor, typeHint, JsonParser.NumberType.BIG_DECIMAL);
            }
            else {
                visitor.expectNumberFormat(typeHint);
            }
        }
    }
    
    static {
        instance = new NumberSerializer(Number.class);
    }
}
