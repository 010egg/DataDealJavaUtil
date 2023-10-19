// 
// Decompiled by Procyon v0.5.36
// 

package com.thoughtworks.paranamer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.util.WeakHashMap;
import java.lang.reflect.AccessibleObject;
import java.util.Map;

public class CachingParanamer implements Paranamer
{
    public static final String __PARANAMER_DATA = "v1.0 \ncom.thoughtworks.paranamer.CachingParanamer <init> com.thoughtworks.paranamer.Paranamer delegate \ncom.thoughtworks.paranamer.CachingParanamer lookupParameterNames java.lang.AccessibleObject methodOrConstructor \ncom.thoughtworks.paranamer.CachingParanamer lookupParameterNames java.lang.AccessibleObject, boolean methodOrCtor,throwExceptionIfMissing \n";
    private final Paranamer delegate;
    private final Map<AccessibleObject, String[]> methodCache;
    
    protected Map<AccessibleObject, String[]> makeMethodCache() {
        return Collections.synchronizedMap(new WeakHashMap<AccessibleObject, String[]>());
    }
    
    public CachingParanamer() {
        this(new DefaultParanamer());
    }
    
    public CachingParanamer(final Paranamer delegate) {
        this.methodCache = this.makeMethodCache();
        this.delegate = delegate;
    }
    
    public String[] lookupParameterNames(final AccessibleObject methodOrConstructor) {
        return this.lookupParameterNames(methodOrConstructor, true);
    }
    
    public String[] lookupParameterNames(final AccessibleObject methodOrCtor, final boolean throwExceptionIfMissing) {
        String[] names = this.methodCache.get(methodOrCtor);
        if (names == null) {
            names = this.delegate.lookupParameterNames(methodOrCtor, throwExceptionIfMissing);
            this.methodCache.put(methodOrCtor, names);
        }
        return names;
    }
    
    public static class WithoutWeakReferences extends CachingParanamer
    {
        public static final String __PARANAMER_DATA = "<init> com.thoughtworks.paranamer.Paranamer delegate \n";
        
        public WithoutWeakReferences() {
        }
        
        public WithoutWeakReferences(final Paranamer delegate) {
            super(delegate);
        }
        
        @Override
        protected Map<AccessibleObject, String[]> makeMethodCache() {
            return new ConcurrentHashMap<AccessibleObject, String[]>();
        }
    }
}
