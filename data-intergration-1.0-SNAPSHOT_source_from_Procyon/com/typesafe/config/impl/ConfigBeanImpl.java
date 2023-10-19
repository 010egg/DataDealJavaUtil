// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.lang.reflect.Field;
import com.typesafe.config.Optional;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;
import java.lang.reflect.ParameterizedType;
import com.typesafe.config.ConfigMemorySize;
import java.time.Duration;
import java.lang.reflect.Type;
import com.typesafe.config.ConfigValueType;
import java.lang.reflect.Method;
import java.util.List;
import java.beans.BeanInfo;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import com.typesafe.config.ConfigValue;
import java.util.Map;
import java.util.HashMap;
import com.typesafe.config.ConfigException;
import com.typesafe.config.Config;

public class ConfigBeanImpl
{
    public static <T> T createInternal(final Config config, final Class<T> clazz) {
        if (((SimpleConfig)config).root().resolveStatus() != ResolveStatus.RESOLVED) {
            throw new ConfigException.NotResolved("need to Config#resolve() a config before using it to initialize a bean, see the API docs for Config#resolve()");
        }
        final Map<String, AbstractConfigValue> configProps = new HashMap<String, AbstractConfigValue>();
        final Map<String, String> originalNames = new HashMap<String, String>();
        for (final Map.Entry<String, ConfigValue> configProp : config.root().entrySet()) {
            final String originalName = configProp.getKey();
            final String camelName = ConfigImplUtil.toCamelCase(originalName);
            if (originalNames.containsKey(camelName) && !originalName.equals(camelName)) {
                continue;
            }
            configProps.put(camelName, configProp.getValue());
            originalNames.put(camelName, originalName);
        }
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        }
        catch (IntrospectionException e) {
            throw new ConfigException.BadBean("Could not get bean information for class " + clazz.getName(), e);
        }
        try {
            final List<PropertyDescriptor> beanProps = new ArrayList<PropertyDescriptor>();
            for (final PropertyDescriptor beanProp : beanInfo.getPropertyDescriptors()) {
                if (beanProp.getReadMethod() != null) {
                    if (beanProp.getWriteMethod() != null) {
                        beanProps.add(beanProp);
                    }
                }
            }
            final List<ConfigException.ValidationProblem> problems = new ArrayList<ConfigException.ValidationProblem>();
            for (final PropertyDescriptor beanProp2 : beanProps) {
                final Method setter = beanProp2.getWriteMethod();
                final Class<?> parameterClass = setter.getParameterTypes()[0];
                final ConfigValueType expectedType = getValueTypeOrNull(parameterClass);
                if (expectedType != null) {
                    String name = originalNames.get(beanProp2.getName());
                    if (name == null) {
                        name = beanProp2.getName();
                    }
                    final Path path = Path.newKey(name);
                    final AbstractConfigValue configValue = configProps.get(beanProp2.getName());
                    if (configValue != null) {
                        SimpleConfig.checkValid(path, expectedType, configValue, problems);
                    }
                    else {
                        if (isOptionalProperty(clazz, beanProp2)) {
                            continue;
                        }
                        SimpleConfig.addMissing(problems, expectedType, path, config.origin());
                    }
                }
            }
            if (!problems.isEmpty()) {
                throw new ConfigException.ValidationFailed(problems);
            }
            final T bean = clazz.newInstance();
            for (final PropertyDescriptor beanProp : beanProps) {
                final Method setter2 = beanProp.getWriteMethod();
                final Type parameterType = setter2.getGenericParameterTypes()[0];
                final Class<?> parameterClass2 = setter2.getParameterTypes()[0];
                final String configPropName = originalNames.get(beanProp.getName());
                if (configPropName == null) {
                    if (isOptionalProperty(clazz, beanProp)) {
                        continue;
                    }
                    throw new ConfigException.Missing(beanProp.getName());
                }
                else {
                    final Object unwrapped = getValue(clazz, parameterType, parameterClass2, config, configPropName);
                    setter2.invoke(bean, unwrapped);
                }
            }
            return bean;
        }
        catch (InstantiationException e2) {
            throw new ConfigException.BadBean(clazz.getName() + " needs a public no-args constructor to be used as a bean", e2);
        }
        catch (IllegalAccessException e3) {
            throw new ConfigException.BadBean(clazz.getName() + " getters and setters are not accessible, they must be for use as a bean", e3);
        }
        catch (InvocationTargetException e4) {
            throw new ConfigException.BadBean("Calling bean method on " + clazz.getName() + " caused an exception", e4);
        }
    }
    
    private static Object getValue(final Class<?> beanClass, final Type parameterType, final Class<?> parameterClass, final Config config, final String configPropName) {
        if (parameterClass == Boolean.class || parameterClass == Boolean.TYPE) {
            return config.getBoolean(configPropName);
        }
        if (parameterClass == Integer.class || parameterClass == Integer.TYPE) {
            return config.getInt(configPropName);
        }
        if (parameterClass == Double.class || parameterClass == Double.TYPE) {
            return config.getDouble(configPropName);
        }
        if (parameterClass == Long.class || parameterClass == Long.TYPE) {
            return config.getLong(configPropName);
        }
        if (parameterClass == String.class) {
            return config.getString(configPropName);
        }
        if (parameterClass == Duration.class) {
            return config.getDuration(configPropName);
        }
        if (parameterClass == ConfigMemorySize.class) {
            return config.getMemorySize(configPropName);
        }
        if (parameterClass == Object.class) {
            return config.getAnyRef(configPropName);
        }
        if (parameterClass == List.class) {
            return getListValue(beanClass, parameterType, parameterClass, config, configPropName);
        }
        if (parameterClass == Map.class) {
            final Type[] typeArgs = ((ParameterizedType)parameterType).getActualTypeArguments();
            if (typeArgs[0] != String.class || typeArgs[1] != Object.class) {
                throw new ConfigException.BadBean("Bean property '" + configPropName + "' of class " + beanClass.getName() + " has unsupported Map<" + typeArgs[0] + "," + typeArgs[1] + ">, only Map<String,Object> is supported right now");
            }
            return config.getObject(configPropName).unwrapped();
        }
        else {
            if (parameterClass == Config.class) {
                return config.getConfig(configPropName);
            }
            if (parameterClass == ConfigObject.class) {
                return config.getObject(configPropName);
            }
            if (parameterClass == ConfigValue.class) {
                return config.getValue(configPropName);
            }
            if (parameterClass == ConfigList.class) {
                return config.getList(configPropName);
            }
            if (parameterClass.isEnum()) {
                final Enum enumValue = (Enum)config.getEnum(parameterClass, configPropName);
                return enumValue;
            }
            if (hasAtLeastOneBeanProperty(parameterClass)) {
                return createInternal(config.getConfig(configPropName), parameterClass);
            }
            throw new ConfigException.BadBean("Bean property " + configPropName + " of class " + beanClass.getName() + " has unsupported type " + parameterType);
        }
    }
    
    private static Object getListValue(final Class<?> beanClass, final Type parameterType, final Class<?> parameterClass, final Config config, final String configPropName) {
        final Type elementType = ((ParameterizedType)parameterType).getActualTypeArguments()[0];
        if (elementType == Boolean.class) {
            return config.getBooleanList(configPropName);
        }
        if (elementType == Integer.class) {
            return config.getIntList(configPropName);
        }
        if (elementType == Double.class) {
            return config.getDoubleList(configPropName);
        }
        if (elementType == Long.class) {
            return config.getLongList(configPropName);
        }
        if (elementType == String.class) {
            return config.getStringList(configPropName);
        }
        if (elementType == Duration.class) {
            return config.getDurationList(configPropName);
        }
        if (elementType == ConfigMemorySize.class) {
            return config.getMemorySizeList(configPropName);
        }
        if (elementType == Object.class) {
            return config.getAnyRefList(configPropName);
        }
        if (elementType == Config.class) {
            return config.getConfigList(configPropName);
        }
        if (elementType == ConfigObject.class) {
            return config.getObjectList(configPropName);
        }
        if (elementType == ConfigValue.class) {
            return config.getList(configPropName);
        }
        if (((Class)elementType).isEnum()) {
            final List<Enum> enumValues = (List<Enum>)config.getEnumList((Class<Enum>)elementType, configPropName);
            return enumValues;
        }
        if (hasAtLeastOneBeanProperty((Class<?>)elementType)) {
            final List<Object> beanList = new ArrayList<Object>();
            final List<? extends Config> configList = config.getConfigList(configPropName);
            for (final Config listMember : configList) {
                beanList.add(createInternal(listMember, (Class<Object>)elementType));
            }
            return beanList;
        }
        throw new ConfigException.BadBean("Bean property '" + configPropName + "' of class " + beanClass.getName() + " has unsupported list element type " + elementType);
    }
    
    private static ConfigValueType getValueTypeOrNull(final Class<?> parameterClass) {
        if (parameterClass == Boolean.class || parameterClass == Boolean.TYPE) {
            return ConfigValueType.BOOLEAN;
        }
        if (parameterClass == Integer.class || parameterClass == Integer.TYPE) {
            return ConfigValueType.NUMBER;
        }
        if (parameterClass == Double.class || parameterClass == Double.TYPE) {
            return ConfigValueType.NUMBER;
        }
        if (parameterClass == Long.class || parameterClass == Long.TYPE) {
            return ConfigValueType.NUMBER;
        }
        if (parameterClass == String.class) {
            return ConfigValueType.STRING;
        }
        if (parameterClass == Duration.class) {
            return null;
        }
        if (parameterClass == ConfigMemorySize.class) {
            return null;
        }
        if (parameterClass == List.class) {
            return ConfigValueType.LIST;
        }
        if (parameterClass == Map.class) {
            return ConfigValueType.OBJECT;
        }
        if (parameterClass == Config.class) {
            return ConfigValueType.OBJECT;
        }
        if (parameterClass == ConfigObject.class) {
            return ConfigValueType.OBJECT;
        }
        if (parameterClass == ConfigList.class) {
            return ConfigValueType.LIST;
        }
        return null;
    }
    
    private static boolean hasAtLeastOneBeanProperty(final Class<?> clazz) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        }
        catch (IntrospectionException e) {
            return false;
        }
        for (final PropertyDescriptor beanProp : beanInfo.getPropertyDescriptors()) {
            if (beanProp.getReadMethod() != null && beanProp.getWriteMethod() != null) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isOptionalProperty(final Class beanClass, final PropertyDescriptor beanProp) {
        final Field field = getField(beanClass, beanProp.getName());
        return field.getAnnotationsByType(Optional.class).length > 0;
    }
    
    private static Field getField(Class beanClass, final String fieldName) {
        try {
            final Field field = beanClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException ex) {
            beanClass = beanClass.getSuperclass();
            if (beanClass == null) {
                return null;
            }
            return getField(beanClass, fieldName);
        }
    }
}
