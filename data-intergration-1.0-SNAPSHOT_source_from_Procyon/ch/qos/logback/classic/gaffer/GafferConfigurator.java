// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.gaffer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import groovy.lang.Closure;
import org.codehaus.groovy.runtime.GeneratedClosure;
import groovy.lang.Reference;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import ch.qos.logback.core.util.OptionHelper;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.codehaus.groovy.control.CompilerConfiguration;
import ch.qos.logback.core.util.ContextUtil;
import groovy.lang.Binding;
import java.io.File;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import java.net.URL;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import java.lang.ref.SoftReference;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import ch.qos.logback.classic.LoggerContext;
import groovy.lang.GroovyObject;

public class GafferConfigurator implements GroovyObject
{
    private LoggerContext context;
    private static final String DEBUG_SYSTEM_PROPERTY_KEY = "logback.debug";
    private static /* synthetic */ ClassInfo $staticClassInfo;
    public static transient /* synthetic */ boolean __$stMC;
    private transient /* synthetic */ MetaClass metaClass;
    private static /* synthetic */ SoftReference $callSiteArray;
    
    public GafferConfigurator(final LoggerContext context) {
        $getCallSiteArray();
        this.metaClass = this.$getStaticMetaClass();
        this.context = (LoggerContext)ScriptBytecodeAdapter.castToType((Object)context, (Class)LoggerContext.class);
    }
    
    protected void informContextOfURLUsedForConfiguration(final URL url) {
        $getCallSiteArray()[0].call((Object)ConfigurationWatchListUtil.class, (Object)this.context, (Object)url);
    }
    
    public void run(final URL url) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (!GafferConfigurator.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            this.informContextOfURLUsedForConfiguration(url);
        }
        else {
            $getCallSiteArray[1].callCurrent((GroovyObject)this, (Object)url);
        }
        $getCallSiteArray[2].callCurrent((GroovyObject)this, $getCallSiteArray[3].callGetProperty((Object)url));
    }
    
    public void run(final File file) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        $getCallSiteArray[4].callCurrent((GroovyObject)this, $getCallSiteArray[5].call($getCallSiteArray[6].call((Object)file)));
        $getCallSiteArray[7].callCurrent((GroovyObject)this, $getCallSiteArray[8].callGetProperty((Object)file));
    }
    
    public void run(final String dslText) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        final Binding binding = (Binding)ScriptBytecodeAdapter.castToType($getCallSiteArray[9].callConstructor((Object)Binding.class), (Class)Binding.class);
        $getCallSiteArray[10].call((Object)binding, (Object)"hostname", $getCallSiteArray[11].callGetProperty((Object)ContextUtil.class));
        final Object configuration = $getCallSiteArray[12].callConstructor((Object)CompilerConfiguration.class);
        if (!GafferConfigurator.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            $getCallSiteArray[15].call(configuration, (Object)this.importCustomizer());
        }
        else {
            $getCallSiteArray[13].call(configuration, $getCallSiteArray[14].callCurrent((GroovyObject)this));
        }
        final String debugAttrib = ShortTypeHandling.castToString($getCallSiteArray[16].call((Object)System.class, (Object)GafferConfigurator.DEBUG_SYSTEM_PROPERTY_KEY));
        if (BytecodeInterface8.isOrigZ() && !GafferConfigurator.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            if (!DefaultTypeTransformation.booleanUnbox($getCallSiteArray[21].call((Object)OptionHelper.class, (Object)debugAttrib)) && !DefaultTypeTransformation.booleanUnbox($getCallSiteArray[22].call((Object)debugAttrib, (Object)"false")) && !DefaultTypeTransformation.booleanUnbox($getCallSiteArray[23].call((Object)debugAttrib, (Object)"null"))) {
                $getCallSiteArray[24].call((Object)OnConsoleStatusListener.class, (Object)this.context);
            }
        }
        else if (!DefaultTypeTransformation.booleanUnbox($getCallSiteArray[17].call((Object)OptionHelper.class, (Object)debugAttrib)) && !DefaultTypeTransformation.booleanUnbox($getCallSiteArray[18].call((Object)debugAttrib, (Object)"false")) && !DefaultTypeTransformation.booleanUnbox($getCallSiteArray[19].call((Object)debugAttrib, (Object)"null"))) {
            $getCallSiteArray[20].call((Object)OnConsoleStatusListener.class, (Object)this.context);
        }
        $getCallSiteArray[25].call($getCallSiteArray[26].callConstructor((Object)ContextUtil.class, (Object)this.context), $getCallSiteArray[27].call((Object)this.context));
        final Reference dslScript = new Reference((Object)ScriptBytecodeAdapter.castToType($getCallSiteArray[28].call($getCallSiteArray[29].callConstructor((Object)GroovyShell.class, (Object)binding, configuration), (Object)dslText), (Class)Script.class));
        $getCallSiteArray[30].call($getCallSiteArray[31].callGroovyObjectGetProperty((Object)dslScript.get()), (Object)ConfigurationDelegate.class);
        $getCallSiteArray[32].call((Object)dslScript.get(), (Object)this.context);
        ScriptBytecodeAdapter.setProperty((Object)new GeneratedClosure(this, this) {
            private static /* synthetic */ SoftReference $callSiteArray;
            
            public Object doCall(final Object it) {
                $getCallSiteArray();
                return dslScript.get();
            }
            
            public Script getDslScript() {
                $getCallSiteArray();
                return (Script)ScriptBytecodeAdapter.castToType(dslScript.get(), (Class)Script.class);
            }
            
            public Object doCall() {
                $getCallSiteArray();
                return this.doCall(null);
            }
            
            public static /* synthetic */ void __$swapInit() {
                $getCallSiteArray();
                GafferConfigurator$_run_closure1.$callSiteArray = null;
            }
            
            static {
                __$swapInit();
            }
            
            private static /* synthetic */ CallSiteArray $createCallSiteArray() {
                return new CallSiteArray((Class)GafferConfigurator$_run_closure1.class, new String[0]);
            }
            
            private static /* synthetic */ CallSite[] $getCallSiteArray() {
                CallSiteArray $createCallSiteArray;
                if (GafferConfigurator$_run_closure1.$callSiteArray == null || ($createCallSiteArray = GafferConfigurator$_run_closure1.$callSiteArray.get()) == null) {
                    $createCallSiteArray = $createCallSiteArray();
                    GafferConfigurator$_run_closure1.$callSiteArray = new SoftReference($createCallSiteArray);
                }
                return $createCallSiteArray.array;
            }
        }, (Class)null, $getCallSiteArray[33].callGroovyObjectGetProperty((Object)dslScript.get()), "getDeclaredOrigin");
        $getCallSiteArray[34].call((Object)dslScript.get());
    }
    
    protected ImportCustomizer importCustomizer() {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        final Object customizer = $getCallSiteArray[35].callConstructor((Object)ImportCustomizer.class);
        final Object core = "ch.qos.logback.core";
        $getCallSiteArray[36].call(customizer, ArrayUtil.createArray(core, (Object)new GStringImpl(new Object[] { core }, new String[] { "", ".encoder" }), (Object)new GStringImpl(new Object[] { core }, new String[] { "", ".read" }), (Object)new GStringImpl(new Object[] { core }, new String[] { "", ".rolling" }), (Object)new GStringImpl(new Object[] { core }, new String[] { "", ".status" }), (Object)"ch.qos.logback.classic.net"));
        $getCallSiteArray[37].call(customizer, $getCallSiteArray[38].callGetProperty((Object)PatternLayoutEncoder.class));
        $getCallSiteArray[39].call(customizer, $getCallSiteArray[40].callGetProperty((Object)Level.class));
        $getCallSiteArray[41].call(customizer, (Object)"off", $getCallSiteArray[42].callGetProperty((Object)Level.class), (Object)"OFF");
        $getCallSiteArray[43].call(customizer, (Object)"error", $getCallSiteArray[44].callGetProperty((Object)Level.class), (Object)"ERROR");
        $getCallSiteArray[45].call(customizer, (Object)"warn", $getCallSiteArray[46].callGetProperty((Object)Level.class), (Object)"WARN");
        $getCallSiteArray[47].call(customizer, (Object)"info", $getCallSiteArray[48].callGetProperty((Object)Level.class), (Object)"INFO");
        $getCallSiteArray[49].call(customizer, (Object)"debug", $getCallSiteArray[50].callGetProperty((Object)Level.class), (Object)"DEBUG");
        $getCallSiteArray[51].call(customizer, (Object)"trace", $getCallSiteArray[52].callGetProperty((Object)Level.class), (Object)"TRACE");
        $getCallSiteArray[53].call(customizer, (Object)"all", $getCallSiteArray[54].callGetProperty((Object)Level.class), (Object)"ALL");
        return (ImportCustomizer)ScriptBytecodeAdapter.castToType(customizer, (Class)ImportCustomizer.class);
    }
    
    protected /* synthetic */ MetaClass $getStaticMetaClass() {
        if (this.getClass() != GafferConfigurator.class) {
            return ScriptBytecodeAdapter.initMetaClass((Object)this);
        }
        ClassInfo $staticClassInfo = GafferConfigurator.$staticClassInfo;
        if ($staticClassInfo == null) {
            $staticClassInfo = (GafferConfigurator.$staticClassInfo = ClassInfo.getClassInfo((Class)this.getClass()));
        }
        return $staticClassInfo.getMetaClass();
    }
    
    public static /* synthetic */ void __$swapInit() {
        $getCallSiteArray();
        GafferConfigurator.$callSiteArray = null;
    }
    
    static {
        __$swapInit();
    }
    
    public LoggerContext getContext() {
        return this.context;
    }
    
    public void setContext(final LoggerContext context) {
        this.context = context;
    }
    
    public static final String getDEBUG_SYSTEM_PROPERTY_KEY() {
        return GafferConfigurator.DEBUG_SYSTEM_PROPERTY_KEY;
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[55];
        $createCallSiteArray_1(array);
        return new CallSiteArray((Class)GafferConfigurator.class, array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (GafferConfigurator.$callSiteArray == null || ($createCallSiteArray = GafferConfigurator.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            GafferConfigurator.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
}
