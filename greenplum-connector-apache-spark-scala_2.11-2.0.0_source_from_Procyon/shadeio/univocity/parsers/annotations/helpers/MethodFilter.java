// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.annotations.helpers;

import java.lang.reflect.Method;

public enum MethodFilter
{
    ONLY_GETTERS((Filter)new Filter() {
        @Override
        public boolean reject(final Method method) {
            return method.getReturnType() == Void.TYPE || method.getParameterTypes().length != 0;
        }
    }), 
    ONLY_SETTERS((Filter)new Filter() {
        @Override
        public boolean reject(final Method method) {
            return method.getParameterTypes().length != 1;
        }
    });
    
    private Filter filter;
    
    private MethodFilter(final Filter filter) {
        this.filter = filter;
    }
    
    public boolean reject(final Method method) {
        return this.filter.reject(method);
    }
    
    private interface Filter
    {
        boolean reject(final Method p0);
    }
}
