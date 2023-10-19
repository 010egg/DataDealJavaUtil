// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.gaffer;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.Status;
import java.lang.management.ManagementFactory;
import ch.qos.logback.classic.jmx.JMXConfigurator;
import ch.qos.logback.classic.jmx.MBeanUtil;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import ch.qos.logback.core.util.CachingDateFormatter;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.classic.net.ReceiverBase;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import groovy.lang.Closure;
import org.codehaus.groovy.runtime.GeneratedClosure;
import java.util.Iterator;
import groovy.lang.Reference;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import ch.qos.logback.classic.Level;
import java.util.HashMap;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import java.util.Map;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.codehaus.groovy.runtime.GStringImpl;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.status.StatusListener;
import org.codehaus.groovy.runtime.callsite.CallSite;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import ch.qos.logback.core.util.Duration;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.classic.joran.ReconfigureOnChangeTask;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import java.lang.ref.SoftReference;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import ch.qos.logback.core.Appender;
import java.util.List;
import groovy.lang.GroovyObject;
import ch.qos.logback.core.spi.ContextAwareBase;

public class ConfigurationDelegate extends ContextAwareBase implements GroovyObject
{
    private List<Appender> appenderList;
    private static /* synthetic */ ClassInfo $staticClassInfo;
    public static transient /* synthetic */ boolean __$stMC;
    private transient /* synthetic */ MetaClass metaClass;
    private static /* synthetic */ SoftReference $callSiteArray;
    
    public ConfigurationDelegate() {
        $getCallSiteArray();
        this.appenderList = (List<Appender>)ScriptBytecodeAdapter.createList(new Object[0]);
        this.metaClass = this.$getStaticMetaClass();
    }
    
    public Object getDeclaredOrigin() {
        $getCallSiteArray();
        return this;
    }
    
    public void scan(final String scanPeriodStr) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (DefaultTypeTransformation.booleanUnbox((Object)scanPeriodStr)) {
            final ReconfigureOnChangeTask rocTask = (ReconfigureOnChangeTask)ScriptBytecodeAdapter.castToType($getCallSiteArray[0].callConstructor((Object)ReconfigureOnChangeTask.class), (Class)ReconfigureOnChangeTask.class);
            $getCallSiteArray[1].call((Object)rocTask, $getCallSiteArray[2].callGroovyObjectGetProperty((Object)this));
            $getCallSiteArray[3].call($getCallSiteArray[4].callGroovyObjectGetProperty((Object)this), $getCallSiteArray[5].callGetProperty((Object)CoreConstants.class), (Object)rocTask);
            try {
                final Duration duration = (Duration)ScriptBytecodeAdapter.castToType($getCallSiteArray[6].call((Object)Duration.class, (Object)scanPeriodStr), (Class)Duration.class);
                final ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService)ScriptBytecodeAdapter.castToType($getCallSiteArray[7].call($getCallSiteArray[8].callGroovyObjectGetProperty((Object)this)), (Class)ScheduledExecutorService.class);
                final ScheduledFuture scheduledFuture = (ScheduledFuture)ScriptBytecodeAdapter.castToType($getCallSiteArray[9].call((Object)scheduledExecutorService, (Object)rocTask, $getCallSiteArray[10].call((Object)duration), $getCallSiteArray[11].call((Object)duration), $getCallSiteArray[12].callGetProperty((Object)TimeUnit.class)), (Class)ScheduledFuture.class);
                $getCallSiteArray[13].call($getCallSiteArray[14].callGroovyObjectGetProperty((Object)this), (Object)scheduledFuture);
                $getCallSiteArray[15].callCurrent((GroovyObject)this, $getCallSiteArray[16].call((Object)"Setting ReconfigureOnChangeTask scanning period to ", (Object)duration));
            }
            catch (NumberFormatException nfe) {
                $getCallSiteArray[17].callCurrent((GroovyObject)this, $getCallSiteArray[18].call($getCallSiteArray[19].call((Object)"Error while converting [", (Object)scanPeriodStr), (Object)"] to long"), (Object)nfe);
            }
        }
    }
    
    public void statusListener(final Class listenerClass) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        final StatusListener statusListener = (StatusListener)ScriptBytecodeAdapter.castToType($getCallSiteArray[20].call((Object)listenerClass), (Class)StatusListener.class);
        $getCallSiteArray[21].call($getCallSiteArray[22].callGetProperty($getCallSiteArray[23].callGroovyObjectGetProperty((Object)this)), (Object)statusListener);
        if (statusListener instanceof ContextAware) {
            $getCallSiteArray[24].call((Object)ScriptBytecodeAdapter.castToType((Object)statusListener, (Class)ContextAware.class), $getCallSiteArray[25].callGroovyObjectGetProperty((Object)this));
        }
        if (statusListener instanceof LifeCycle) {
            $getCallSiteArray[26].call((Object)ScriptBytecodeAdapter.castToType((Object)statusListener, (Class)LifeCycle.class));
        }
        $getCallSiteArray[27].callCurrent((GroovyObject)this, (Object)new GStringImpl(new Object[] { $getCallSiteArray[28].callGetProperty((Object)listenerClass) }, new String[] { "Added status listener of type [", "]" }));
    }
    
    public void conversionRule(final String conversionWord, final Class converterClass) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        final String converterClassName = ShortTypeHandling.castToString($getCallSiteArray[29].call((Object)converterClass));
        Map ruleRegistry = (Map)ScriptBytecodeAdapter.castToType($getCallSiteArray[30].call($getCallSiteArray[31].callGroovyObjectGetProperty((Object)this), $getCallSiteArray[32].callGetProperty((Object)CoreConstants.class)), (Class)Map.class);
        if (BytecodeInterface8.isOrigZ() && !ConfigurationDelegate.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareEqual((Object)ruleRegistry, (Object)null)) {
                ruleRegistry = (Map)ScriptBytecodeAdapter.castToType($getCallSiteArray[37].callConstructor((Object)HashMap.class), (Class)Map.class);
                $getCallSiteArray[38].call($getCallSiteArray[39].callGroovyObjectGetProperty((Object)this), $getCallSiteArray[40].callGetProperty((Object)CoreConstants.class), (Object)ruleRegistry);
            }
        }
        else if (ScriptBytecodeAdapter.compareEqual((Object)ruleRegistry, (Object)null)) {
            ruleRegistry = (Map)ScriptBytecodeAdapter.castToType($getCallSiteArray[33].callConstructor((Object)HashMap.class), (Class)Map.class);
            $getCallSiteArray[34].call($getCallSiteArray[35].callGroovyObjectGetProperty((Object)this), $getCallSiteArray[36].callGetProperty((Object)CoreConstants.class), (Object)ruleRegistry);
        }
        $getCallSiteArray[41].callCurrent((GroovyObject)this, $getCallSiteArray[42].call($getCallSiteArray[43].call($getCallSiteArray[44].call($getCallSiteArray[45].call((Object)"registering conversion word ", (Object)conversionWord), (Object)" with class ["), (Object)converterClassName), (Object)"]"));
        $getCallSiteArray[46].call((Object)ruleRegistry, (Object)conversionWord, (Object)converterClassName);
    }
    
    public void root(final Level level, final List<String> appenderNames) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (BytecodeInterface8.isOrigZ() && !ConfigurationDelegate.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareEqual((Object)level, (Object)null)) {
                $getCallSiteArray[50].callCurrent((GroovyObject)this, (Object)"Root logger cannot be set to level null");
            }
            else {
                $getCallSiteArray[51].callCurrent((GroovyObject)this, $getCallSiteArray[52].callGetProperty((Object)Logger.class), (Object)level, (Object)appenderNames);
            }
        }
        else if (ScriptBytecodeAdapter.compareEqual((Object)level, (Object)null)) {
            $getCallSiteArray[47].callCurrent((GroovyObject)this, (Object)"Root logger cannot be set to level null");
        }
        else {
            $getCallSiteArray[48].callCurrent((GroovyObject)this, $getCallSiteArray[49].callGetProperty((Object)Logger.class), (Object)level, (Object)appenderNames);
        }
    }
    
    public void logger(final String name, final Level level, final List<String> appenderNames, final Boolean additivity) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (DefaultTypeTransformation.booleanUnbox((Object)name)) {
            final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)ScriptBytecodeAdapter.castToType($getCallSiteArray[53].call((Object)ScriptBytecodeAdapter.castToType($getCallSiteArray[54].callGroovyObjectGetProperty((Object)this), (Class)LoggerContext.class), (Object)name), (Class)ch.qos.logback.classic.Logger.class);
            $getCallSiteArray[55].callCurrent((GroovyObject)this, $getCallSiteArray[56].call((Object)new GStringImpl(new Object[] { name }, new String[] { "Setting level of logger [", "] to " }), (Object)level));
            ScriptBytecodeAdapter.setProperty((Object)level, (Class)null, (Object)logger, "level");
            final Reference aName = new Reference((Object)null);
            final Iterator iterator = (Iterator)ScriptBytecodeAdapter.castToType($getCallSiteArray[57].call((Object)appenderNames), (Class)Iterator.class);
            while (iterator.hasNext()) {
                aName.set(iterator.next());
                final Appender appender = (Appender)ScriptBytecodeAdapter.castToType($getCallSiteArray[58].call((Object)this.appenderList, (Object)new GeneratedClosure(this, this) {
                    public static transient /* synthetic */ boolean __$stMC;
                    private static /* synthetic */ SoftReference $callSiteArray;
                    
                    public Object doCall(final Object it) {
                        final CallSite[] $getCallSiteArray = $getCallSiteArray();
                        if (BytecodeInterface8.isOrigZ() && !ConfigurationDelegate$_logger_closure1.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
                            return ScriptBytecodeAdapter.compareEqual($getCallSiteArray[1].callGetProperty(it), aName.get());
                        }
                        return ScriptBytecodeAdapter.compareEqual($getCallSiteArray[0].callGetProperty(it), aName.get());
                    }
                    
                    public Object getaName() {
                        $getCallSiteArray();
                        return aName.get();
                    }
                    
                    public static /* synthetic */ void __$swapInit() {
                        $getCallSiteArray();
                        ConfigurationDelegate$_logger_closure1.$callSiteArray = null;
                    }
                    
                    static {
                        __$swapInit();
                    }
                    
                    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
                        final String[] array = new String[2];
                        $createCallSiteArray_1(array);
                        return new CallSiteArray((Class)ConfigurationDelegate$_logger_closure1.class, array);
                    }
                    
                    private static /* synthetic */ CallSite[] $getCallSiteArray() {
                        CallSiteArray $createCallSiteArray;
                        if (ConfigurationDelegate$_logger_closure1.$callSiteArray == null || ($createCallSiteArray = ConfigurationDelegate$_logger_closure1.$callSiteArray.get()) == null) {
                            $createCallSiteArray = $createCallSiteArray();
                            ConfigurationDelegate$_logger_closure1.$callSiteArray = new SoftReference($createCallSiteArray);
                        }
                        return $createCallSiteArray.array;
                    }
                }), (Class)Appender.class);
                if (ScriptBytecodeAdapter.compareNotEqual((Object)appender, (Object)null)) {
                    $getCallSiteArray[59].callCurrent((GroovyObject)this, $getCallSiteArray[60].call((Object)new GStringImpl(new Object[] { aName.get() }, new String[] { "Attaching appender named [", "] to " }), (Object)logger));
                    $getCallSiteArray[61].call((Object)logger, (Object)appender);
                }
                else {
                    $getCallSiteArray[62].callCurrent((GroovyObject)this, (Object)new GStringImpl(new Object[] { aName.get() }, new String[] { "Failed to find appender named [", "]" }));
                }
            }
            if (ScriptBytecodeAdapter.compareNotEqual((Object)additivity, (Object)null)) {
                ScriptBytecodeAdapter.setProperty((Object)additivity, (Class)null, (Object)logger, "additive");
            }
        }
        else {
            $getCallSiteArray[63].callCurrent((GroovyObject)this, (Object)"No name attribute for logger");
        }
    }
    
    public void appender(final String name, final Class clazz, final Closure closure) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        $getCallSiteArray[64].callCurrent((GroovyObject)this, $getCallSiteArray[65].call($getCallSiteArray[66].call((Object)"About to instantiate appender of type [", $getCallSiteArray[67].callGetProperty((Object)clazz)), (Object)"]"));
        final Appender appender = (Appender)ScriptBytecodeAdapter.castToType($getCallSiteArray[68].call((Object)clazz), (Class)Appender.class);
        $getCallSiteArray[69].callCurrent((GroovyObject)this, $getCallSiteArray[70].call($getCallSiteArray[71].call((Object)"Naming appender as [", (Object)name), (Object)"]"));
        ScriptBytecodeAdapter.setProperty((Object)name, (Class)null, (Object)appender, "name");
        ScriptBytecodeAdapter.setProperty($getCallSiteArray[72].callGroovyObjectGetProperty((Object)this), (Class)null, (Object)appender, "context");
        $getCallSiteArray[73].call((Object)this.appenderList, (Object)appender);
        Label_0507: {
            if (!BytecodeInterface8.isOrigZ() || ConfigurationDelegate.__$stMC || BytecodeInterface8.disabledStandardMetaClass()) {
                if (ScriptBytecodeAdapter.compareNotEqual((Object)closure, (Object)null)) {
                    final AppenderDelegate ad = (AppenderDelegate)ScriptBytecodeAdapter.castToType($getCallSiteArray[74].callConstructor((Object)AppenderDelegate.class, (Object)appender, (Object)this.appenderList), (Class)AppenderDelegate.class);
                    $getCallSiteArray[75].callCurrent((GroovyObject)this, (Object)ad, (Object)appender);
                    ScriptBytecodeAdapter.setProperty($getCallSiteArray[76].callGroovyObjectGetProperty((Object)this), (Class)null, (Object)ad, "context");
                    ScriptBytecodeAdapter.setGroovyObjectProperty((Object)ad, (Class)ConfigurationDelegate.class, (GroovyObject)closure, "delegate");
                    ScriptBytecodeAdapter.setGroovyObjectProperty($getCallSiteArray[77].callGetProperty((Object)Closure.class), (Class)ConfigurationDelegate.class, (GroovyObject)closure, "resolveStrategy");
                    $getCallSiteArray[78].call((Object)closure);
                }
                break Label_0507;
            }
            if (!ScriptBytecodeAdapter.compareNotEqual((Object)closure, (Object)null)) {
                break Label_0507;
            }
            final AppenderDelegate ad2 = (AppenderDelegate)ScriptBytecodeAdapter.castToType($getCallSiteArray[79].callConstructor((Object)AppenderDelegate.class, (Object)appender, (Object)this.appenderList), (Class)AppenderDelegate.class);
            $getCallSiteArray[80].callCurrent((GroovyObject)this, (Object)ad2, (Object)appender);
            ScriptBytecodeAdapter.setProperty($getCallSiteArray[81].callGroovyObjectGetProperty((Object)this), (Class)null, (Object)ad2, "context");
            ScriptBytecodeAdapter.setGroovyObjectProperty((Object)ad2, (Class)ConfigurationDelegate.class, (GroovyObject)closure, "delegate");
            ScriptBytecodeAdapter.setGroovyObjectProperty($getCallSiteArray[82].callGetProperty((Object)Closure.class), (Class)ConfigurationDelegate.class, (GroovyObject)closure, "resolveStrategy");
            $getCallSiteArray[83].call((Object)closure);
            try {
                $getCallSiteArray[84].call((Object)appender);
            }
            catch (RuntimeException e) {
                $getCallSiteArray[85].callCurrent((GroovyObject)this, $getCallSiteArray[86].call($getCallSiteArray[87].call((Object)"Failed to start apppender named [", (Object)name), (Object)"]"), (Object)e);
            }
        }
    }
    
    public void receiver(final String name, final Class aClass, final Closure closure) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        $getCallSiteArray[88].callCurrent((GroovyObject)this, $getCallSiteArray[89].call($getCallSiteArray[90].call((Object)"About to instantiate receiver of type [", $getCallSiteArray[91].callGetProperty($getCallSiteArray[92].callGroovyObjectGetProperty((Object)this))), (Object)"]"));
        final ReceiverBase receiver = (ReceiverBase)ScriptBytecodeAdapter.castToType($getCallSiteArray[93].call((Object)aClass), (Class)ReceiverBase.class);
        ScriptBytecodeAdapter.setProperty($getCallSiteArray[94].callGroovyObjectGetProperty((Object)this), (Class)null, (Object)receiver, "context");
        Label_0400: {
            if (!BytecodeInterface8.isOrigZ() || ConfigurationDelegate.__$stMC || BytecodeInterface8.disabledStandardMetaClass()) {
                if (ScriptBytecodeAdapter.compareNotEqual((Object)closure, (Object)null)) {
                    final ComponentDelegate componentDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType($getCallSiteArray[95].callConstructor((Object)ComponentDelegate.class, (Object)receiver), (Class)ComponentDelegate.class);
                    ScriptBytecodeAdapter.setProperty($getCallSiteArray[96].callGroovyObjectGetProperty((Object)this), (Class)null, (Object)componentDelegate, "context");
                    ScriptBytecodeAdapter.setGroovyObjectProperty((Object)componentDelegate, (Class)ConfigurationDelegate.class, (GroovyObject)closure, "delegate");
                    ScriptBytecodeAdapter.setGroovyObjectProperty($getCallSiteArray[97].callGetProperty((Object)Closure.class), (Class)ConfigurationDelegate.class, (GroovyObject)closure, "resolveStrategy");
                    $getCallSiteArray[98].call((Object)closure);
                }
                break Label_0400;
            }
            if (!ScriptBytecodeAdapter.compareNotEqual((Object)closure, (Object)null)) {
                break Label_0400;
            }
            final ComponentDelegate componentDelegate2 = (ComponentDelegate)ScriptBytecodeAdapter.castToType($getCallSiteArray[99].callConstructor((Object)ComponentDelegate.class, (Object)receiver), (Class)ComponentDelegate.class);
            ScriptBytecodeAdapter.setProperty($getCallSiteArray[100].callGroovyObjectGetProperty((Object)this), (Class)null, (Object)componentDelegate2, "context");
            ScriptBytecodeAdapter.setGroovyObjectProperty((Object)componentDelegate2, (Class)ConfigurationDelegate.class, (GroovyObject)closure, "delegate");
            ScriptBytecodeAdapter.setGroovyObjectProperty($getCallSiteArray[101].callGetProperty((Object)Closure.class), (Class)ConfigurationDelegate.class, (GroovyObject)closure, "resolveStrategy");
            $getCallSiteArray[102].call((Object)closure);
            try {
                $getCallSiteArray[103].call((Object)receiver);
            }
            catch (RuntimeException e) {
                $getCallSiteArray[104].callCurrent((GroovyObject)this, $getCallSiteArray[105].call($getCallSiteArray[106].call((Object)"Failed to start receiver of type [", $getCallSiteArray[107].call((Object)aClass)), (Object)"]"), (Object)e);
            }
        }
    }
    
    private void copyContributions(final AppenderDelegate appenderDelegate, final Appender appender) {
        final Reference appenderDelegate2 = new Reference((Object)appenderDelegate);
        final Reference appender2 = new Reference((Object)appender);
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (((Appender)appender2.get()) instanceof ConfigurationContributor) {
            final ConfigurationContributor cc = (ConfigurationContributor)ScriptBytecodeAdapter.castToType((Object)appender2.get(), (Class)ConfigurationContributor.class);
            $getCallSiteArray[108].call($getCallSiteArray[109].call((Object)cc), (Object)new GeneratedClosure(this, this) {
                private static /* synthetic */ SoftReference $callSiteArray;
                
                public Object doCall(final Object oldName, final Object newName) {
                    final CallSite[] $getCallSiteArray = $getCallSiteArray();
                    final Closure methodPointer = ScriptBytecodeAdapter.getMethodPointer(appender2.get(), (String)ShortTypeHandling.castToString((Object)new GStringImpl(new Object[] { oldName }, new String[] { "", "" })));
                    ScriptBytecodeAdapter.setProperty((Object)methodPointer, (Class)null, $getCallSiteArray[0].callGetProperty(appenderDelegate2.get()), (String)ShortTypeHandling.castToString((Object)new GStringImpl(new Object[] { newName }, new String[] { "", "" })));
                    return methodPointer;
                }
                
                public Object call(final Object oldName, final Object newName) {
                    return $getCallSiteArray()[1].callCurrent((GroovyObject)this, oldName, newName);
                }
                
                public AppenderDelegate getAppenderDelegate() {
                    $getCallSiteArray();
                    return (AppenderDelegate)ScriptBytecodeAdapter.castToType(appenderDelegate2.get(), (Class)AppenderDelegate.class);
                }
                
                public Appender getAppender() {
                    $getCallSiteArray();
                    return (Appender)ScriptBytecodeAdapter.castToType(appender2.get(), (Class)Appender.class);
                }
                
                public static /* synthetic */ void __$swapInit() {
                    $getCallSiteArray();
                    ConfigurationDelegate$_copyContributions_closure2.$callSiteArray = null;
                }
                
                static {
                    __$swapInit();
                }
                
                private static /* synthetic */ CallSiteArray $createCallSiteArray() {
                    final String[] array = new String[2];
                    $createCallSiteArray_1(array);
                    return new CallSiteArray((Class)ConfigurationDelegate$_copyContributions_closure2.class, array);
                }
                
                private static /* synthetic */ CallSite[] $getCallSiteArray() {
                    CallSiteArray $createCallSiteArray;
                    if (ConfigurationDelegate$_copyContributions_closure2.$callSiteArray == null || ($createCallSiteArray = ConfigurationDelegate$_copyContributions_closure2.$callSiteArray.get()) == null) {
                        $createCallSiteArray = $createCallSiteArray();
                        ConfigurationDelegate$_copyContributions_closure2.$callSiteArray = new SoftReference($createCallSiteArray);
                    }
                    return $createCallSiteArray.array;
                }
            });
        }
    }
    
    public void turboFilter(final Class clazz, final Closure closure) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        $getCallSiteArray[110].callCurrent((GroovyObject)this, $getCallSiteArray[111].call($getCallSiteArray[112].call((Object)"About to instantiate turboFilter of type [", $getCallSiteArray[113].callGetProperty((Object)clazz)), (Object)"]"));
        final TurboFilter turboFilter = (TurboFilter)ScriptBytecodeAdapter.castToType($getCallSiteArray[114].call((Object)clazz), (Class)TurboFilter.class);
        ScriptBytecodeAdapter.setProperty($getCallSiteArray[115].callGroovyObjectGetProperty((Object)this), (Class)null, (Object)turboFilter, "context");
        if (BytecodeInterface8.isOrigZ() && !ConfigurationDelegate.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareNotEqual((Object)closure, (Object)null)) {
                final ComponentDelegate componentDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType($getCallSiteArray[120].callConstructor((Object)ComponentDelegate.class, (Object)turboFilter), (Class)ComponentDelegate.class);
                ScriptBytecodeAdapter.setProperty($getCallSiteArray[121].callGroovyObjectGetProperty((Object)this), (Class)null, (Object)componentDelegate, "context");
                ScriptBytecodeAdapter.setGroovyObjectProperty((Object)componentDelegate, (Class)ConfigurationDelegate.class, (GroovyObject)closure, "delegate");
                ScriptBytecodeAdapter.setGroovyObjectProperty($getCallSiteArray[122].callGetProperty((Object)Closure.class), (Class)ConfigurationDelegate.class, (GroovyObject)closure, "resolveStrategy");
                $getCallSiteArray[123].call((Object)closure);
            }
        }
        else if (ScriptBytecodeAdapter.compareNotEqual((Object)closure, (Object)null)) {
            final ComponentDelegate componentDelegate2 = (ComponentDelegate)ScriptBytecodeAdapter.castToType($getCallSiteArray[116].callConstructor((Object)ComponentDelegate.class, (Object)turboFilter), (Class)ComponentDelegate.class);
            ScriptBytecodeAdapter.setProperty($getCallSiteArray[117].callGroovyObjectGetProperty((Object)this), (Class)null, (Object)componentDelegate2, "context");
            ScriptBytecodeAdapter.setGroovyObjectProperty((Object)componentDelegate2, (Class)ConfigurationDelegate.class, (GroovyObject)closure, "delegate");
            ScriptBytecodeAdapter.setGroovyObjectProperty($getCallSiteArray[118].callGetProperty((Object)Closure.class), (Class)ConfigurationDelegate.class, (GroovyObject)closure, "resolveStrategy");
            $getCallSiteArray[119].call((Object)closure);
        }
        $getCallSiteArray[124].call((Object)turboFilter);
        $getCallSiteArray[125].callCurrent((GroovyObject)this, (Object)"Adding aforementioned turbo filter to context");
        $getCallSiteArray[126].call($getCallSiteArray[127].callGroovyObjectGetProperty((Object)this), (Object)turboFilter);
    }
    
    public String timestamp(final String datePattern, final long timeReference) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        long now = DefaultTypeTransformation.longUnbox((Object)(-1));
        if (BytecodeInterface8.isOrigL() && BytecodeInterface8.isOrigZ() && !ConfigurationDelegate.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareEqual((Object)timeReference, (Object)(-1))) {
                $getCallSiteArray[133].callCurrent((GroovyObject)this, (Object)"Using current interpretation time, i.e. now, as time reference.");
                now = DefaultTypeTransformation.longUnbox($getCallSiteArray[134].call((Object)System.class));
            }
            else {
                now = timeReference;
                $getCallSiteArray[135].callCurrent((GroovyObject)this, $getCallSiteArray[136].call($getCallSiteArray[137].call((Object)"Using ", (Object)now), (Object)" as time reference."));
            }
        }
        else if (ScriptBytecodeAdapter.compareEqual((Object)timeReference, (Object)(-1))) {
            $getCallSiteArray[128].callCurrent((GroovyObject)this, (Object)"Using current interpretation time, i.e. now, as time reference.");
            now = DefaultTypeTransformation.longUnbox($getCallSiteArray[129].call((Object)System.class));
        }
        else {
            now = timeReference;
            $getCallSiteArray[130].callCurrent((GroovyObject)this, $getCallSiteArray[131].call($getCallSiteArray[132].call((Object)"Using ", (Object)now), (Object)" as time reference."));
        }
        final CachingDateFormatter sdf = (CachingDateFormatter)ScriptBytecodeAdapter.castToType($getCallSiteArray[138].callConstructor((Object)CachingDateFormatter.class, (Object)datePattern), (Class)CachingDateFormatter.class);
        return ShortTypeHandling.castToString($getCallSiteArray[139].call((Object)sdf, (Object)now));
    }
    
    public void jmxConfigurator(final String name) {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        Object objectName = null;
        Object contextName = $getCallSiteArray[140].callGetProperty($getCallSiteArray[141].callGroovyObjectGetProperty((Object)this));
        if (BytecodeInterface8.isOrigZ() && !ConfigurationDelegate.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareNotEqual((Object)name, (Object)null)) {
                try {
                    objectName = $getCallSiteArray[143].callConstructor((Object)ObjectName.class, (Object)name);
                }
                catch (MalformedObjectNameException e) {
                    contextName = name;
                }
            }
        }
        else if (ScriptBytecodeAdapter.compareNotEqual((Object)name, (Object)null)) {
            try {
                objectName = $getCallSiteArray[142].callConstructor((Object)ObjectName.class, (Object)name);
            }
            catch (MalformedObjectNameException e2) {
                contextName = name;
            }
        }
        if (BytecodeInterface8.isOrigZ() && !ConfigurationDelegate.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareEqual(objectName, (Object)null)) {
                final Object objectNameAsStr = $getCallSiteArray[148].call((Object)MBeanUtil.class, contextName, (Object)JMXConfigurator.class);
                objectName = $getCallSiteArray[149].call((Object)MBeanUtil.class, $getCallSiteArray[150].callGroovyObjectGetProperty((Object)this), (Object)this, objectNameAsStr);
                if (ScriptBytecodeAdapter.compareEqual(objectName, (Object)null)) {
                    $getCallSiteArray[151].callCurrent((GroovyObject)this, (Object)new GStringImpl(new Object[] { objectNameAsStr }, new String[] { "Failed to construct ObjectName for [", "]" }));
                    return;
                }
            }
        }
        else if (ScriptBytecodeAdapter.compareEqual(objectName, (Object)null)) {
            final Object objectNameAsStr2 = $getCallSiteArray[144].call((Object)MBeanUtil.class, contextName, (Object)JMXConfigurator.class);
            objectName = $getCallSiteArray[145].call((Object)MBeanUtil.class, $getCallSiteArray[146].callGroovyObjectGetProperty((Object)this), (Object)this, objectNameAsStr2);
            if (ScriptBytecodeAdapter.compareEqual(objectName, (Object)null)) {
                $getCallSiteArray[147].callCurrent((GroovyObject)this, (Object)new GStringImpl(new Object[] { objectNameAsStr2 }, new String[] { "Failed to construct ObjectName for [", "]" }));
                return;
            }
        }
        final Object platformMBeanServer = $getCallSiteArray[152].callGetProperty((Object)ManagementFactory.class);
        if (!DefaultTypeTransformation.booleanUnbox($getCallSiteArray[153].call((Object)MBeanUtil.class, platformMBeanServer, objectName))) {
            final JMXConfigurator jmxConfigurator = (JMXConfigurator)ScriptBytecodeAdapter.castToType($getCallSiteArray[154].callConstructor((Object)JMXConfigurator.class, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)ScriptBytecodeAdapter.castToType($getCallSiteArray[155].callGroovyObjectGetProperty((Object)this), (Class)LoggerContext.class), (Class)LoggerContext.class), platformMBeanServer, objectName), (Class)JMXConfigurator.class);
            try {
                $getCallSiteArray[156].call(platformMBeanServer, (Object)jmxConfigurator, objectName);
            }
            catch (Exception all) {
                $getCallSiteArray[157].callCurrent((GroovyObject)this, (Object)"Failed to create mbean", all);
            }
        }
    }
    
    public void scan() {
        $getCallSiteArray();
        if (!ConfigurationDelegate.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            this.scan(null);
        }
        else {
            this.scan(null);
        }
    }
    
    public void root(final Level level) {
        $getCallSiteArray();
        this.root(level, ScriptBytecodeAdapter.createList(new Object[0]));
    }
    
    public void logger(final String name, final Level level, final List<String> appenderNames) {
        $getCallSiteArray();
        this.logger(name, level, appenderNames, null);
    }
    
    public void logger(final String name, final Level level) {
        $getCallSiteArray();
        this.logger(name, level, ScriptBytecodeAdapter.createList(new Object[0]), null);
    }
    
    public void appender(final String name, final Class clazz) {
        $getCallSiteArray();
        this.appender(name, clazz, null);
    }
    
    public void receiver(final String name, final Class aClass) {
        $getCallSiteArray();
        this.receiver(name, aClass, null);
    }
    
    public void turboFilter(final Class clazz) {
        $getCallSiteArray();
        this.turboFilter(clazz, null);
    }
    
    public String timestamp(final String datePattern) {
        $getCallSiteArray();
        if (!ConfigurationDelegate.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            return this.timestamp(datePattern, DefaultTypeTransformation.longUnbox((Object)(-1)));
        }
        return this.timestamp(datePattern, DefaultTypeTransformation.longUnbox((Object)(-1)));
    }
    
    public void jmxConfigurator() {
        $getCallSiteArray();
        if (!ConfigurationDelegate.__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            this.jmxConfigurator(null);
        }
        else {
            this.jmxConfigurator(null);
        }
    }
    
    protected /* synthetic */ MetaClass $getStaticMetaClass() {
        if (this.getClass() != ConfigurationDelegate.class) {
            return ScriptBytecodeAdapter.initMetaClass((Object)this);
        }
        ClassInfo $staticClassInfo = ConfigurationDelegate.$staticClassInfo;
        if ($staticClassInfo == null) {
            $staticClassInfo = (ConfigurationDelegate.$staticClassInfo = ClassInfo.getClassInfo((Class)this.getClass()));
        }
        return $staticClassInfo.getMetaClass();
    }
    
    public static /* synthetic */ void __$swapInit() {
        $getCallSiteArray();
        ConfigurationDelegate.$callSiteArray = null;
    }
    
    static {
        __$swapInit();
    }
    
    public List<Appender> getAppenderList() {
        return this.appenderList;
    }
    
    public void setAppenderList(final List<Appender> appenderList) {
        this.appenderList = appenderList;
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[158];
        $createCallSiteArray_1(array);
        return new CallSiteArray((Class)ConfigurationDelegate.class, array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (ConfigurationDelegate.$callSiteArray == null || ($createCallSiteArray = ConfigurationDelegate.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            ConfigurationDelegate.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
}
