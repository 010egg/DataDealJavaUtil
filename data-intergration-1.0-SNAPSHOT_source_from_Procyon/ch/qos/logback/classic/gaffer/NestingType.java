// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.gaffer;

import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.transform.ImmutableASTTransformation;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import java.util.LinkedHashMap;
import java.lang.ref.SoftReference;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import groovy.lang.GroovyObject;

public enum NestingType implements GroovyObject
{
    public static final NestingType NA;
    public static final NestingType SINGLE;
    public static final NestingType AS_COLLECTION;
    public static final NestingType MIN_VALUE;
    public static final NestingType MAX_VALUE;
    private static /* synthetic */ ClassInfo $staticClassInfo;
    private transient /* synthetic */ MetaClass metaClass;
    private static /* synthetic */ SoftReference $callSiteArray;
    
    public NestingType(final String __str, final int __int, final LinkedHashMap __namedArgs) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        super(__str, __int);
        this.metaClass = this.$getStaticMetaClass();
        if (BytecodeInterface8.isOrigZ() && !BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareEqual((Object)__namedArgs, (Object)null)) {
                throw (Throwable)$getCallSiteArray[2].callConstructor((Object)IllegalArgumentException.class, (Object)"One of the enum constants for enum ch.qos.logback.classic.gaffer.NestingType was initialized with null. Please use a non-null value or define your own constructor.");
            }
            $getCallSiteArray[3].callStatic((Class)ImmutableASTTransformation.class, (Object)this, (Object)__namedArgs);
        }
        else {
            if (ScriptBytecodeAdapter.compareEqual((Object)__namedArgs, (Object)null)) {
                throw (Throwable)$getCallSiteArray[0].callConstructor((Object)IllegalArgumentException.class, (Object)"One of the enum constants for enum ch.qos.logback.classic.gaffer.NestingType was initialized with null. Please use a non-null value or define your own constructor.");
            }
            $getCallSiteArray[1].callStatic((Class)ImmutableASTTransformation.class, (Object)this, (Object)__namedArgs);
        }
    }
    
    public NestingType(final String __str, final int __int) {
        this(__str, __int, (LinkedHashMap)ScriptBytecodeAdapter.castToType($getCallSiteArray()[4].callConstructor((Object)LinkedHashMap.class), (Class)LinkedHashMap.class));
    }
    
    static {
        __$swapInit();
        NA = (NestingType)ShortTypeHandling.castToEnum($getCallSiteArray()[18].callStatic((Class)NestingType.class, (Object)"NA", (Object)0), (Class)NestingType.class);
        SINGLE = (NestingType)ShortTypeHandling.castToEnum($getCallSiteArray()[19].callStatic((Class)NestingType.class, (Object)"SINGLE", (Object)1), (Class)NestingType.class);
        AS_COLLECTION = (NestingType)ShortTypeHandling.castToEnum($getCallSiteArray()[20].callStatic((Class)NestingType.class, (Object)"AS_COLLECTION", (Object)2), (Class)NestingType.class);
        MIN_VALUE = NestingType.NA;
        MAX_VALUE = NestingType.AS_COLLECTION;
    }
    
    protected /* synthetic */ MetaClass $getStaticMetaClass() {
        if (this.getClass() != NestingType.class) {
            return ScriptBytecodeAdapter.initMetaClass((Object)this);
        }
        ClassInfo $staticClassInfo = NestingType.$staticClassInfo;
        if ($staticClassInfo == null) {
            $staticClassInfo = (NestingType.$staticClassInfo = ClassInfo.getClassInfo((Class)this.getClass()));
        }
        return $staticClassInfo.getMetaClass();
    }
    
    public static /* synthetic */ void __$swapInit() {
        $getCallSiteArray();
        NestingType.$callSiteArray = null;
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[21];
        $createCallSiteArray_1(array);
        return new CallSiteArray((Class)NestingType.class, array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (NestingType.$callSiteArray == null || ($createCallSiteArray = NestingType.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            NestingType.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
}
