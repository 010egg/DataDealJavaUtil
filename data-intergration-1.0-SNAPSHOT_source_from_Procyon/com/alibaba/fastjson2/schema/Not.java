// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import java.util.Map;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigInteger;

final class Not extends JSONSchema
{
    final JSONSchema schema;
    final Type[] types;
    final Boolean result;
    
    public Not(final JSONSchema schema, final Type[] types, final Boolean result) {
        super(null, null);
        this.schema = schema;
        this.types = types;
        this.result = result;
    }
    
    @Override
    public Type getType() {
        return Type.AllOf;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        if (this.schema != null && this.schema.validate(value).isSuccess()) {
            return Not.FAIL_NOT;
        }
        if (this.types != null) {
            for (final Type type : this.types) {
                switch (type) {
                    case String: {
                        if (value instanceof String) {
                            return Not.FAIL_NOT;
                        }
                        break;
                    }
                    case Integer: {
                        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long || value instanceof BigInteger || value instanceof AtomicInteger || value instanceof AtomicLong) {
                            return Not.FAIL_NOT;
                        }
                        break;
                    }
                    case Number: {
                        if (value instanceof Number) {
                            return Not.FAIL_NOT;
                        }
                        break;
                    }
                    case Null: {
                        if (value == null) {
                            return Not.FAIL_NOT;
                        }
                        break;
                    }
                    case Array: {
                        if (value instanceof Object[] || value instanceof Collection || (value != null && value.getClass().isArray())) {
                            return Not.FAIL_NOT;
                        }
                        break;
                    }
                    case Object: {
                        if (value instanceof Map) {
                            return Not.FAIL_NOT;
                        }
                        if (value != null && JSONSchema.CONTEXT.getObjectReader(value.getClass()) instanceof ObjectReaderBean) {
                            return Not.FAIL_NOT;
                        }
                        break;
                    }
                    case Boolean: {
                        if (value instanceof Boolean) {
                            return Not.FAIL_NOT;
                        }
                        break;
                    }
                    case Any: {
                        return Not.FAIL_NOT;
                    }
                }
            }
        }
        if (this.result != null) {
            return this.result ? Not.FAIL_NOT : Not.SUCCESS;
        }
        return Not.SUCCESS;
    }
}
