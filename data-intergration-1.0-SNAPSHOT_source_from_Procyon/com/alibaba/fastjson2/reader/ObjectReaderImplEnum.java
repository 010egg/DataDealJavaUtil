// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.lang.reflect.ParameterizedType;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import java.util.Arrays;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.lang.reflect.Method;

public final class ObjectReaderImplEnum implements ObjectReader
{
    final Method createMethod;
    final Type createMethodParamType;
    final Member valueField;
    final Type valueFieldType;
    final Class enumClass;
    final long typeNameHash;
    private final Enum[] enums;
    private final Enum[] ordinalEnums;
    private final long[] enumNameHashCodes;
    private String[] stringValues;
    private long[] intValues;
    
    public ObjectReaderImplEnum(final Class enumClass, final Method createMethod, final Member valueField, final Enum[] enums, final Enum[] ordinalEnums, final long[] enumNameHashCodes) {
        this.enumClass = enumClass;
        this.createMethod = createMethod;
        this.valueField = valueField;
        Type valueFieldType = null;
        if (valueField instanceof Field) {
            valueFieldType = ((Field)valueField).getType();
        }
        else if (valueField instanceof Method) {
            valueFieldType = ((Method)valueField).getReturnType();
        }
        if ((this.valueFieldType = valueFieldType) != null) {
            if (valueFieldType == String.class) {
                this.stringValues = new String[enums.length];
            }
            else {
                this.intValues = new long[enums.length];
            }
            for (int i = 0; i < enums.length; ++i) {
                final Enum e = enums[i];
                try {
                    Object fieldValue;
                    if (valueField instanceof Field) {
                        fieldValue = ((Field)valueField).get(e);
                    }
                    else {
                        fieldValue = ((Method)valueField).invoke(e, new Object[0]);
                    }
                    if (valueFieldType == String.class) {
                        this.stringValues[i] = (String)fieldValue;
                    }
                    else if (fieldValue instanceof Number) {
                        this.intValues[i] = ((Number)fieldValue).longValue();
                    }
                }
                catch (Exception ex) {}
            }
        }
        Type createMethodParamType = null;
        if (createMethod != null && createMethod.getParameterCount() == 1) {
            createMethodParamType = createMethod.getParameterTypes()[0];
        }
        this.createMethodParamType = createMethodParamType;
        this.typeNameHash = Fnv.hashCode64(TypeUtils.getTypeName(enumClass));
        this.enums = enums;
        this.ordinalEnums = ordinalEnums;
        this.enumNameHashCodes = enumNameHashCodes;
    }
    
    @Override
    public Class getObjectClass() {
        return this.enumClass;
    }
    
    public Enum getEnumByHashCode(final long hashCode) {
        if (this.enums == null) {
            return null;
        }
        final int enumIndex = Arrays.binarySearch(this.enumNameHashCodes, hashCode);
        if (enumIndex < 0) {
            return null;
        }
        return this.enums[enumIndex];
    }
    
    public Enum getEnum(final String name) {
        if (name == null) {
            return null;
        }
        return this.getEnumByHashCode(Fnv.hashCode64(name));
    }
    
    public Enum getEnumByOrdinal(final int ordinal) {
        if (ordinal < 0 || ordinal >= this.ordinalEnums.length) {
            throw new JSONException("No enum ordinal " + this.enumClass.getCanonicalName() + "." + ordinal);
        }
        return this.ordinalEnums[ordinal];
    }
    
    public Enum of(final int intValue) {
        Enum enumValue = null;
        if (this.valueField == null) {
            if (intValue >= 0 && intValue < this.ordinalEnums.length) {
                enumValue = this.ordinalEnums[intValue];
            }
            return enumValue;
        }
        try {
            if (this.valueField instanceof Field) {
                for (int i = 0; i < this.enums.length; ++i) {
                    final Enum e = this.enums[i];
                    if (((Field)this.valueField).getInt(e) == intValue) {
                        enumValue = e;
                        break;
                    }
                }
            }
            else {
                final Method valueMethod = (Method)this.valueField;
                for (int j = 0; j < this.enums.length; ++j) {
                    final Enum e2 = this.enums[j];
                    if (((Number)valueMethod.invoke(e2, new Object[0])).intValue() == intValue) {
                        enumValue = e2;
                        break;
                    }
                }
            }
        }
        catch (Exception error) {
            throw new JSONException("parse enum error, class " + this.enumClass.getName() + ", value " + intValue, error);
        }
        if (enumValue == null) {
            throw new JSONException("None enum ordinal or value " + intValue);
        }
        return enumValue;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final int start = jsonReader.getOffset();
        final byte type = jsonReader.getType();
        if (type == -110) {
            final ObjectReader autoTypeObjectReader = jsonReader.checkAutoType(this.enumClass, 0L, features);
            if (autoTypeObjectReader != null) {
                if (autoTypeObjectReader != this) {
                    return autoTypeObjectReader.readJSONBObject(jsonReader, fieldType, fieldName, features);
                }
            }
            else if (jsonReader.isEnabled(JSONReader.Feature.ErrorOnNotSupportAutoType)) {
                throw new JSONException(jsonReader.info("not support enumType : " + jsonReader.getString()));
            }
        }
        final boolean isInt = type >= -16 && type <= 72;
        Enum fieldValue;
        if (isInt) {
            int ordinal;
            if (type <= 47) {
                ordinal = type;
                jsonReader.next();
            }
            else {
                ordinal = jsonReader.readInt32Value();
            }
            if (ordinal < 0 || ordinal >= this.ordinalEnums.length) {
                throw new JSONException("No enum ordinal " + this.enumClass.getCanonicalName() + "." + ordinal);
            }
            fieldValue = this.ordinalEnums[ordinal];
        }
        else {
            if (jsonReader.nextIfNullOrEmptyString()) {
                return null;
            }
            fieldValue = this.getEnumByHashCode(jsonReader.readValueHashCode());
            if (fieldValue == null) {
                final long nameHash = jsonReader.getNameHashCodeLCase();
                fieldValue = this.getEnumByHashCode(nameHash);
            }
        }
        if (fieldValue == null && jsonReader.getOffset() == start) {
            this.oomCheck(fieldType);
        }
        return fieldValue;
    }
    
    private void oomCheck(final Type fieldType) {
        if (fieldType instanceof ParameterizedType) {
            final Type rawType = ((ParameterizedType)fieldType).getRawType();
            if (List.class.isAssignableFrom((Class<?>)rawType)) {
                throw new JSONException(this.getClass().getSimpleName() + " parses error, JSONReader not forward when field type belongs to collection to avoid OOM");
            }
        }
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final int start = jsonReader.getOffset();
        if (this.createMethodParamType != null) {
            final Object paramValue = jsonReader.read(this.createMethodParamType);
            try {
                return this.createMethod.invoke(null, paramValue);
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException(jsonReader.info("create enum error, enumClass " + this.enumClass.getName() + ", paramValue " + paramValue), e);
            }
        }
        Enum<?> fieldValue = null;
        if (jsonReader.isInt()) {
            final int intValue = jsonReader.readInt32Value();
            if (this.valueField == null) {
                if (intValue < 0 || intValue >= this.ordinalEnums.length) {
                    throw new JSONException("No enum ordinal " + this.enumClass.getCanonicalName() + "." + intValue);
                }
                fieldValue = (Enum<?>)this.ordinalEnums[intValue];
            }
            else {
                if (this.intValues != null) {
                    for (int i = 0; i < this.intValues.length; ++i) {
                        if (this.intValues[i] == intValue) {
                            fieldValue = (Enum<?>)this.enums[i];
                            break;
                        }
                    }
                }
                if (fieldValue == null && jsonReader.isEnabled(JSONReader.Feature.ErrorOnEnumNotMatch)) {
                    throw new JSONException(jsonReader.info("parse enum error, class " + this.enumClass.getName() + ", " + this.valueField.getName() + " " + intValue));
                }
            }
        }
        else if (!jsonReader.nextIfNullOrEmptyString()) {
            if (this.stringValues != null && jsonReader.isString()) {
                final String str = jsonReader.readString();
                for (int i = 0; i < this.stringValues.length; ++i) {
                    if (str.equals(this.stringValues[i])) {
                        fieldValue = (Enum<?>)this.enums[i];
                        break;
                    }
                }
            }
            else if (this.intValues != null && jsonReader.isString()) {
                final int intValue = jsonReader.readInt32Value();
                for (int i = 0; i < this.intValues.length; ++i) {
                    if (this.intValues[i] == intValue) {
                        fieldValue = (Enum<?>)this.enums[i];
                        break;
                    }
                }
            }
            else {
                final long hashCode = jsonReader.readValueHashCode();
                if (hashCode == -3750763034362895579L) {
                    return null;
                }
                fieldValue = (Enum<?>)this.getEnumByHashCode(hashCode);
                if (fieldValue == null) {
                    fieldValue = (Enum<?>)this.getEnumByHashCode(jsonReader.getNameHashCodeLCase());
                }
                if (fieldValue == null) {
                    final String str2 = jsonReader.getString();
                    if (TypeUtils.isInteger(str2)) {
                        final int ordinal = Integer.parseInt(str2);
                        if (ordinal >= 0 && ordinal < this.ordinalEnums.length) {
                            fieldValue = (Enum<?>)this.ordinalEnums[ordinal];
                        }
                    }
                }
            }
            if (fieldValue == null && jsonReader.isEnabled(JSONReader.Feature.ErrorOnEnumNotMatch)) {
                final String strVal = jsonReader.getString();
                throw new JSONException(jsonReader.info("parse enum error, class " + this.enumClass.getName() + ", value " + strVal));
            }
        }
        if (fieldValue == null && jsonReader.getOffset() == start) {
            this.oomCheck(fieldType);
        }
        return fieldValue;
    }
}
