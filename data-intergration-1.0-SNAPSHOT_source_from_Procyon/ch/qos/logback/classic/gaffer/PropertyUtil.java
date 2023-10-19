// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.gaffer;

import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.GeneratedClosure;
import groovy.lang.Closure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import ch.qos.logback.core.joran.util.beans.BeanUtil;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import java.lang.ref.SoftReference;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import groovy.lang.GroovyObject;

public class PropertyUtil implements GroovyObject
{
    private static /* synthetic */ ClassInfo $staticClassInfo;
    public static transient /* synthetic */ boolean __$stMC;
    private transient /* synthetic */ MetaClass metaClass;
    private static /* synthetic */ SoftReference $callSiteArray;
    
    public PropertyUtil() {
        $getCallSiteArray();
        this.metaClass = this.$getStaticMetaClass();
    }
    
    public static boolean hasAdderMethod(final Object obj, final String name) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        String addMethod = null;
        if (!PropertyUtil.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            addMethod = ShortTypeHandling.castToString((Object)new GStringImpl(new Object[] { upperCaseFirstLetter(name) }, new String[] { "add", "" }));
        }
        else {
            addMethod = ShortTypeHandling.castToString((Object)new GStringImpl(new Object[] { $getCallSiteArray[0].callStatic((Class)PropertyUtil.class, (Object)name) }, new String[] { "add", "" }));
        }
        return DefaultTypeTransformation.booleanUnbox($getCallSiteArray[1].call($getCallSiteArray[2].callGetProperty(obj), obj, (Object)addMethod));
    }
    
    public static NestingType nestingType(final Object obj, final String name) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        final Object decapitalizedName = $getCallSiteArray[3].call($getCallSiteArray[4].callGetProperty((Object)BeanUtil.class), (Object)name);
        if (DefaultTypeTransformation.booleanUnbox($getCallSiteArray[5].call(obj, decapitalizedName))) {
            return (NestingType)ShortTypeHandling.castToEnum($getCallSiteArray[6].callGetProperty((Object)NestingType.class), (Class)NestingType.class);
        }
        if (DefaultTypeTransformation.booleanUnbox($getCallSiteArray[7].callStatic((Class)PropertyUtil.class, obj, (Object)name))) {
            return (NestingType)ShortTypeHandling.castToEnum($getCallSiteArray[8].callGetProperty((Object)NestingType.class), (Class)NestingType.class);
        }
        return (NestingType)ShortTypeHandling.castToEnum($getCallSiteArray[9].callGetProperty((Object)NestingType.class), (Class)NestingType.class);
    }
    
    public static void attach(final NestingType nestingType, final Object component, final Object subComponent, String name) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (ScriptBytecodeAdapter.isCase((Object)nestingType, $getCallSiteArray[10].callGetProperty((Object)NestingType.class))) {
            name = ShortTypeHandling.castToString($getCallSiteArray[11].call($getCallSiteArray[12].callGetProperty((Object)BeanUtil.class), (Object)name));
            ScriptBytecodeAdapter.setProperty(subComponent, (Class)null, component, (String)ShortTypeHandling.castToString((Object)new GStringImpl(new Object[] { name }, new String[] { "", "" })));
        }
        else if (ScriptBytecodeAdapter.isCase((Object)nestingType, $getCallSiteArray[13].callGetProperty((Object)NestingType.class))) {
            final String firstUpperName = ShortTypeHandling.castToString($getCallSiteArray[14].call((Object)PropertyUtil.class, (Object)name));
            ScriptBytecodeAdapter.invokeMethodN((Class)PropertyUtil.class, component, (String)ShortTypeHandling.castToString((Object)new GStringImpl(new Object[] { firstUpperName }, new String[] { "add", "" })), new Object[] { subComponent });
        }
    }
    
    public static String transformFirstLetter(final String s, final Closure closure) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (BytecodeInterface8.isOrigInt() && BytecodeInterface8.isOrigZ() && !PropertyUtil.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareEqual((Object)s, (Object)null) || ScriptBytecodeAdapter.compareEqual($getCallSiteArray[16].call((Object)s), (Object)0)) {
                return s;
            }
        }
        else if (ScriptBytecodeAdapter.compareEqual((Object)s, (Object)null) || ScriptBytecodeAdapter.compareEqual($getCallSiteArray[15].call((Object)s), (Object)0)) {
            return s;
        }
        final String firstLetter = ShortTypeHandling.castToString($getCallSiteArray[17].callConstructor((Object)String.class, $getCallSiteArray[18].call((Object)s, (Object)0)));
        final String modifiedFistLetter = ShortTypeHandling.castToString($getCallSiteArray[19].call((Object)closure, (Object)firstLetter));
        if (BytecodeInterface8.isOrigInt() && BytecodeInterface8.isOrigZ() && !PropertyUtil.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareEqual($getCallSiteArray[23].call((Object)s), (Object)1)) {
                return modifiedFistLetter;
            }
            return ShortTypeHandling.castToString($getCallSiteArray[24].call((Object)modifiedFistLetter, $getCallSiteArray[25].call((Object)s, (Object)1)));
        }
        else {
            if (ScriptBytecodeAdapter.compareEqual($getCallSiteArray[20].call((Object)s), (Object)1)) {
                return modifiedFistLetter;
            }
            return ShortTypeHandling.castToString($getCallSiteArray[21].call((Object)modifiedFistLetter, $getCallSiteArray[22].call((Object)s, (Object)1)));
        }
    }
    
    public static String upperCaseFirstLetter(final String s) {
        return ShortTypeHandling.castToString($getCallSiteArray()[26].callStatic((Class)PropertyUtil.class, (Object)s, (Object)new GeneratedClosure(PropertyUtil.class, PropertyUtil.class) {
            public static transient /* synthetic */ boolean __$stMC;
            private static /* synthetic */ SoftReference $callSiteArray;
            
            public Object doCall(final String it) {
                return $getCallSiteArray()[0].call((Object)it);
            }
            
            public Object call(final String it) {
                final CallSite[] $getCallSiteArray = $getCallSiteArray();
                if (!PropertyUtil$_upperCaseFirstLetter_closure1.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
                    return this.doCall(it);
                }
                return $getCallSiteArray[1].callCurrent((GroovyObject)this, (Object)it);
            }
            
            public static /* synthetic */ void __$swapInit() {
                $getCallSiteArray();
                PropertyUtil$_upperCaseFirstLetter_closure1.$callSiteArray = null;
            }
            
            static {
                __$swapInit();
            }
            
            private static /* synthetic */ CallSiteArray $createCallSiteArray() {
                final String[] array = new String[2];
                $createCallSiteArray_1(array);
                return new CallSiteArray((Class)PropertyUtil$_upperCaseFirstLetter_closure1.class, array);
            }
            
            private static /* synthetic */ CallSite[] $getCallSiteArray() {
                CallSiteArray $createCallSiteArray;
                if (PropertyUtil$_upperCaseFirstLetter_closure1.$callSiteArray == null || ($createCallSiteArray = PropertyUtil$_upperCaseFirstLetter_closure1.$callSiteArray.get()) == null) {
                    $createCallSiteArray = $createCallSiteArray();
                    PropertyUtil$_upperCaseFirstLetter_closure1.$callSiteArray = new SoftReference($createCallSiteArray);
                }
                return $createCallSiteArray.array;
            }
        }));
    }
    
    protected /* synthetic */ MetaClass $getStaticMetaClass() {
        if (this.getClass() != PropertyUtil.class) {
            return ScriptBytecodeAdapter.initMetaClass((Object)this);
        }
        ClassInfo $staticClassInfo = PropertyUtil.$staticClassInfo;
        if ($staticClassInfo == null) {
            $staticClassInfo = (PropertyUtil.$staticClassInfo = ClassInfo.getClassInfo((Class)this.getClass()));
        }
        return $staticClassInfo.getMetaClass();
    }
    
    public static /* synthetic */ void __$swapInit() {
        $getCallSiteArray();
        PropertyUtil.$callSiteArray = null;
    }
    
    static {
        __$swapInit();
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[27];
        $createCallSiteArray_1(array);
        return new CallSiteArray((Class)PropertyUtil.class, array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (PropertyUtil.$callSiteArray == null || ($createCallSiteArray = PropertyUtil.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            PropertyUtil.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
}
