// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.gaffer;

import org.codehaus.groovy.runtime.GeneratedClosure;
import groovy.lang.Closure;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import groovy.lang.MetaClass;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import ch.qos.logback.core.spi.AppenderAttachable;
import org.codehaus.groovy.runtime.callsite.CallSite;
import java.util.List;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import ch.qos.logback.core.Appender;
import java.util.Map;

public class AppenderDelegate extends ComponentDelegate
{
    private Map<String, Appender<?>> appendersByName;
    private static /* synthetic */ SoftReference $callSiteArray;
    
    public AppenderDelegate(final Appender appender) {
        $getCallSiteArray();
        super((Object)appender);
        this.appendersByName = (Map<String, Appender<?>>)ScriptBytecodeAdapter.createMap(new Object[0]);
    }
    
    public AppenderDelegate(final Appender appender, final List<Appender<?>> appenders) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        super((Object)appender);
        this.appendersByName = (Map<String, Appender<?>>)ScriptBytecodeAdapter.createMap(new Object[0]);
        this.appendersByName = (Map<String, Appender<?>>)ScriptBytecodeAdapter.castToType($getCallSiteArray[0].call((Object)appenders, (Object)new _closure1(this)), (Class)Map.class);
    }
    
    @Override
    public String getLabel() {
        $getCallSiteArray();
        return "appender";
    }
    
    public void appenderRef(final String name) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (!DefaultTypeTransformation.booleanUnbox($getCallSiteArray[1].call((Object)AppenderAttachable.class, $getCallSiteArray[2].callGetProperty($getCallSiteArray[3].callGroovyObjectGetProperty((Object)this))))) {
            final Object errorMessage = $getCallSiteArray[4].call($getCallSiteArray[5].call($getCallSiteArray[6].call($getCallSiteArray[7].callGetProperty($getCallSiteArray[8].callGetProperty($getCallSiteArray[9].callGroovyObjectGetProperty((Object)this))), (Object)" does not implement "), $getCallSiteArray[10].callGetProperty((Object)AppenderAttachable.class)), (Object)".");
            throw (Throwable)$getCallSiteArray[11].callConstructor((Object)IllegalArgumentException.class, errorMessage);
        }
        $getCallSiteArray[12].call($getCallSiteArray[13].callGroovyObjectGetProperty((Object)this), $getCallSiteArray[14].call((Object)this.appendersByName, (Object)name));
    }
    
    public static /* synthetic */ void __$swapInit() {
        $getCallSiteArray();
        AppenderDelegate.$callSiteArray = null;
    }
    
    static {
        __$swapInit();
    }
    
    public Map<String, Appender<?>> getAppendersByName() {
        return this.appendersByName;
    }
    
    public void setAppendersByName(final Map<String, Appender<?>> appendersByName) {
        this.appendersByName = appendersByName;
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[15];
        $createCallSiteArray_1(array);
        return new CallSiteArray((Class)AppenderDelegate.class, array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (AppenderDelegate.$callSiteArray == null || ($createCallSiteArray = AppenderDelegate.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            AppenderDelegate.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
    
    class _closure1 extends Closure implements GeneratedClosure
    {
        private static /* synthetic */ SoftReference $callSiteArray;
        
        public _closure1(final Object _outerInstance, final Object _thisObject) {
            $getCallSiteArray();
            super(_outerInstance, _thisObject);
        }
        
        public Object doCall(final Object it) {
            return ScriptBytecodeAdapter.createMap(new Object[] { $getCallSiteArray()[0].callGetProperty(it), it });
        }
        
        public Object doCall() {
            $getCallSiteArray();
            return this.doCall(null);
        }
        
        public static /* synthetic */ void __$swapInit() {
            $getCallSiteArray();
            _closure1.$callSiteArray = null;
        }
        
        static {
            __$swapInit();
        }
        
        private static /* synthetic */ CallSiteArray $createCallSiteArray() {
            final String[] array = { null };
            $createCallSiteArray_1(array);
            return new CallSiteArray((Class)_closure1.class, array);
        }
        
        private static /* synthetic */ CallSite[] $getCallSiteArray() {
            CallSiteArray $createCallSiteArray;
            if (_closure1.$callSiteArray == null || ($createCallSiteArray = _closure1.$callSiteArray.get()) == null) {
                $createCallSiteArray = $createCallSiteArray();
                _closure1.$callSiteArray = new SoftReference($createCallSiteArray);
            }
            return $createCallSiteArray.array;
        }
    }
}
