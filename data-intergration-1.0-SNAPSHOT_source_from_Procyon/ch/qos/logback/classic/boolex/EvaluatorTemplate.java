// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.boolex;

import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.lang.ref.SoftReference;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import groovy.lang.GroovyObject;

public class EvaluatorTemplate implements IEvaluator, GroovyObject
{
    private static /* synthetic */ ClassInfo $staticClassInfo;
    private transient /* synthetic */ MetaClass metaClass;
    private static /* synthetic */ SoftReference $callSiteArray;
    
    public EvaluatorTemplate() {
        $getCallSiteArray();
        this.metaClass = this.$getStaticMetaClass();
    }
    
    public boolean doEvaluate(final ILoggingEvent event) {
        $getCallSiteArray();
        final ILoggingEvent e = event;
        return DefaultTypeTransformation.booleanUnbox((Object)e);
    }
    
    protected /* synthetic */ MetaClass $getStaticMetaClass() {
        if (this.getClass() != EvaluatorTemplate.class) {
            return ScriptBytecodeAdapter.initMetaClass((Object)this);
        }
        ClassInfo $staticClassInfo = EvaluatorTemplate.$staticClassInfo;
        if ($staticClassInfo == null) {
            $staticClassInfo = (EvaluatorTemplate.$staticClassInfo = ClassInfo.getClassInfo((Class)this.getClass()));
        }
        return $staticClassInfo.getMetaClass();
    }
    
    public static /* synthetic */ void __$swapInit() {
        $getCallSiteArray();
        EvaluatorTemplate.$callSiteArray = null;
    }
    
    static {
        __$swapInit();
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        return new CallSiteArray((Class)EvaluatorTemplate.class, new String[0]);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (EvaluatorTemplate.$callSiteArray == null || ($createCallSiteArray = EvaluatorTemplate.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            EvaluatorTemplate.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
}
