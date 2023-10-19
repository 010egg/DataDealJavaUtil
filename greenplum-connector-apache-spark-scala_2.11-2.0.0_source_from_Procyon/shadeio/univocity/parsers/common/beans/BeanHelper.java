// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.beans;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.lang.reflect.Method;

public final class BeanHelper
{
    private static final PropertyWrapper[] EMPTY;
    private static final Class<?> introspectorClass;
    private static final Method beanInfoMethod;
    private static final Method propertyDescriptorMethod;
    static Method PROPERTY_WRITE_METHOD;
    static Method PROPERTY_READ_METHOD;
    static Method PROPERTY_NAME_METHOD;
    private static final Map<Class<?>, WeakReference<PropertyWrapper[]>> descriptors;
    
    private BeanHelper() {
    }
    
    public static PropertyWrapper[] getPropertyDescriptors(final Class<?> beanClass) {
        if (BeanHelper.propertyDescriptorMethod == null) {
            return BeanHelper.EMPTY;
        }
        PropertyWrapper[] out = null;
        final WeakReference<PropertyWrapper[]> reference = BeanHelper.descriptors.get(beanClass);
        if (reference != null) {
            out = reference.get();
        }
        if (out == null) {
            try {
                final Object beanInfo = BeanHelper.beanInfoMethod.invoke(null, beanClass, Object.class);
                final Object[] propertyDescriptors = (Object[])BeanHelper.propertyDescriptorMethod.invoke(beanInfo, new Object[0]);
                out = new PropertyWrapper[propertyDescriptors.length];
                for (int i = 0; i < propertyDescriptors.length; ++i) {
                    out[i] = new PropertyWrapper(propertyDescriptors[i]);
                }
            }
            catch (Exception ex) {
                out = BeanHelper.EMPTY;
            }
            BeanHelper.descriptors.put(beanClass, new WeakReference<PropertyWrapper[]>(out));
        }
        return out;
    }
    
    private static Class<?> findIntrospectorImplementationClass() {
        try {
            return Class.forName("com.googlecode.openbeans.Introspector");
        }
        catch (Throwable e1) {
            try {
                return Class.forName("java.beans.Introspector");
            }
            catch (Throwable e2) {
                return null;
            }
        }
    }
    
    private static Method getBeanInfoMethod() {
        if (BeanHelper.introspectorClass == null) {
            return null;
        }
        try {
            return BeanHelper.introspectorClass.getMethod("getBeanInfo", Class.class, Class.class);
        }
        catch (Throwable e) {
            return null;
        }
    }
    
    private static Method getMethod(final String methodName, final Method method, final boolean fromComponentType) {
        if (method == null) {
            return null;
        }
        try {
            Class<?> returnType = method.getReturnType();
            if (fromComponentType) {
                returnType = returnType.getComponentType();
            }
            return returnType.getMethod(methodName, (Class<?>[])new Class[0]);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    static {
        EMPTY = new PropertyWrapper[0];
        introspectorClass = findIntrospectorImplementationClass();
        beanInfoMethod = getBeanInfoMethod();
        propertyDescriptorMethod = getMethod("getPropertyDescriptors", BeanHelper.beanInfoMethod, false);
        BeanHelper.PROPERTY_WRITE_METHOD = getMethod("getWriteMethod", BeanHelper.propertyDescriptorMethod, true);
        BeanHelper.PROPERTY_READ_METHOD = getMethod("getReadMethod", BeanHelper.propertyDescriptorMethod, true);
        BeanHelper.PROPERTY_NAME_METHOD = getMethod("getName", BeanHelper.propertyDescriptorMethod, true);
        descriptors = new ConcurrentHashMap<Class<?>, WeakReference<PropertyWrapper[]>>();
    }
}
