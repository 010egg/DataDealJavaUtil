// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.Map;
import java.util.Collection;
import com.alibaba.fastjson2.util.MultiType;
import com.alibaba.fastjson2.util.ParameterizedTypeImpl;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.util.List;
import com.alibaba.fastjson2.util.BeanUtils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T>
{
    protected final Type type;
    protected final Class<? super T> rawType;
    
    public TypeReference() {
        final Type superClass = this.getClass().getGenericSuperclass();
        this.type = ((ParameterizedType)superClass).getActualTypeArguments()[0];
        this.rawType = (Class<? super T>)BeanUtils.getRawType(this.type);
    }
    
    private TypeReference(final Type type) {
        if (type == null) {
            throw new NullPointerException();
        }
        this.type = BeanUtils.canonicalize(type);
        this.rawType = (Class<? super T>)BeanUtils.getRawType(type);
    }
    
    public TypeReference(Type... actualTypeArguments) {
        if (actualTypeArguments == null || actualTypeArguments.length == 0) {
            throw new NullPointerException();
        }
        if (actualTypeArguments.length == 1 && actualTypeArguments[0] == null) {
            actualTypeArguments = new Type[] { Object.class };
        }
        final Class<?> thisClass = this.getClass();
        final Type superClass = thisClass.getGenericSuperclass();
        final ParameterizedType argType = (ParameterizedType)((ParameterizedType)superClass).getActualTypeArguments()[0];
        this.type = canonicalize(thisClass, argType, actualTypeArguments, 0);
        this.rawType = (Class<? super T>)BeanUtils.getRawType(this.type);
    }
    
    public final Type getType() {
        return this.type;
    }
    
    public final Class<? super T> getRawType() {
        return this.rawType;
    }
    
    public T parseObject(final String text) {
        return JSON.parseObject(text, this.type);
    }
    
    public T parseObject(final byte[] utf8Bytes) {
        return JSON.parseObject(utf8Bytes, this.type);
    }
    
    public List<T> parseArray(final String text, final JSONReader.Feature... features) {
        return JSON.parseArray(text, this.type, features);
    }
    
    public List<T> parseArray(final byte[] utf8Bytes, final JSONReader.Feature... features) {
        return JSON.parseArray(utf8Bytes, this.type, features);
    }
    
    public T to(final JSONArray array) {
        return array.to(this.type);
    }
    
    public T to(final JSONObject object, final JSONReader.Feature... features) {
        return object.to(this.type, features);
    }
    
    @Deprecated
    public T toJavaObject(final JSONArray array) {
        return array.to(this.type);
    }
    
    @Deprecated
    public T toJavaObject(final JSONObject object, final JSONReader.Feature... features) {
        return object.to(this.type, features);
    }
    
    public static TypeReference<?> get(final Type type) {
        return new TypeReference<Object>(type) {};
    }
    
    private static Type canonicalize(final Class<?> thisClass, final ParameterizedType type, final Type[] actualTypeArguments, int actualIndex) {
        final Type rawType = type.getRawType();
        final Type[] argTypes = type.getActualTypeArguments();
        for (int i = 0; i < argTypes.length; ++i) {
            if (argTypes[i] instanceof TypeVariable && actualIndex < actualTypeArguments.length) {
                argTypes[i] = actualTypeArguments[actualIndex++];
            }
            Label_0301: {
                if (argTypes[i] instanceof GenericArrayType) {
                    Type componentType = argTypes[i];
                    int dimension = 0;
                    while (componentType instanceof GenericArrayType) {
                        ++dimension;
                        componentType = ((GenericArrayType)componentType).getGenericComponentType();
                    }
                    if (componentType instanceof Class) {
                        final Class<?> cls = (Class<?>)componentType;
                        if (cls.isPrimitive()) {
                            char ch;
                            if (cls == Integer.TYPE) {
                                ch = 'I';
                            }
                            else if (cls == Long.TYPE) {
                                ch = 'J';
                            }
                            else if (cls == Float.TYPE) {
                                ch = 'F';
                            }
                            else if (cls == Double.TYPE) {
                                ch = 'D';
                            }
                            else if (cls == Boolean.TYPE) {
                                ch = 'Z';
                            }
                            else if (cls == Character.TYPE) {
                                ch = 'C';
                            }
                            else if (cls == Byte.TYPE) {
                                ch = 'B';
                            }
                            else {
                                if (cls != Short.TYPE) {
                                    break Label_0301;
                                }
                                ch = 'S';
                            }
                            final char[] chars = new char[dimension + 1];
                            for (int j = 0; j < dimension; ++j) {
                                chars[j] = '[';
                            }
                            chars[dimension] = ch;
                            final String typeName = new String(chars);
                            argTypes[i] = TypeUtils.loadClass(typeName);
                        }
                    }
                }
            }
            if (argTypes[i] instanceof ParameterizedType) {
                argTypes[i] = canonicalize(thisClass, (ParameterizedType)argTypes[i], actualTypeArguments, actualIndex);
            }
        }
        return new ParameterizedTypeImpl(argTypes, thisClass, rawType);
    }
    
    public static Type of(final Type... types) {
        return new MultiType(types);
    }
    
    public static Type collectionType(final Class<? extends Collection> collectionClass, final Class<?> elementClass) {
        return new ParameterizedTypeImpl(collectionClass, new Type[] { elementClass });
    }
    
    public static Type arrayType(final Class<?> elementType) {
        return new BeanUtils.GenericArrayTypeImpl(elementType);
    }
    
    public static Type mapType(final Class<? extends Map> mapClass, final Class<?> keyClass, final Class<?> valueClass) {
        return new ParameterizedTypeImpl(mapClass, new Type[] { keyClass, valueClass });
    }
    
    public static Type mapType(final Class<?> keyClass, final Type valueType) {
        return new ParameterizedTypeImpl(Map.class, new Type[] { keyClass, valueType });
    }
    
    public static Type parametricType(final Class<?> parametrized, final Class<?>... parameterClasses) {
        return new ParameterizedTypeImpl(parametrized, (Type[])parameterClasses);
    }
    
    public static Type parametricType(final Class<?> parametrized, final Type... parameterTypes) {
        return new ParameterizedTypeImpl(parametrized, parameterTypes);
    }
}
