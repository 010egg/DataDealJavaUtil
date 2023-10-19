// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic;

import ch.qos.logback.core.pattern.PostCompileProcessor;
import ch.qos.logback.classic.pattern.EnsureExceptionHandling;
import ch.qos.logback.classic.pattern.LocalSequenceNumberConverter;
import ch.qos.logback.classic.pattern.color.HighlightingCompositeConverter;
import ch.qos.logback.core.pattern.color.BoldWhiteCompositeConverter;
import ch.qos.logback.core.pattern.color.BoldCyanCompositeConverter;
import ch.qos.logback.core.pattern.color.BoldMagentaCompositeConverter;
import ch.qos.logback.core.pattern.color.BoldBlueCompositeConverter;
import ch.qos.logback.core.pattern.color.BoldYellowCompositeConverter;
import ch.qos.logback.core.pattern.color.BoldGreenCompositeConverter;
import ch.qos.logback.core.pattern.color.BoldRedCompositeConverter;
import ch.qos.logback.core.pattern.color.GrayCompositeConverter;
import ch.qos.logback.core.pattern.color.WhiteCompositeConverter;
import ch.qos.logback.core.pattern.color.CyanCompositeConverter;
import ch.qos.logback.core.pattern.color.MagentaCompositeConverter;
import ch.qos.logback.core.pattern.color.BlueCompositeConverter;
import ch.qos.logback.core.pattern.color.YellowCompositeConverter;
import ch.qos.logback.core.pattern.color.GreenCompositeConverter;
import ch.qos.logback.core.pattern.color.RedCompositeConverter;
import ch.qos.logback.core.pattern.color.BlackCompositeConverter;
import ch.qos.logback.classic.pattern.LineSeparatorConverter;
import ch.qos.logback.classic.pattern.PropertyConverter;
import ch.qos.logback.classic.pattern.MarkerConverter;
import ch.qos.logback.classic.pattern.CallerDataConverter;
import ch.qos.logback.classic.pattern.ContextNameConverter;
import ch.qos.logback.classic.pattern.NopThrowableInformationConverter;
import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.pattern.MDCConverter;
import ch.qos.logback.classic.pattern.FileOfCallerConverter;
import ch.qos.logback.classic.pattern.LineOfCallerConverter;
import ch.qos.logback.classic.pattern.MethodOfCallerConverter;
import ch.qos.logback.classic.pattern.ClassOfCallerConverter;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.pattern.LoggerConverter;
import ch.qos.logback.classic.pattern.ThreadConverter;
import ch.qos.logback.classic.pattern.LevelConverter;
import ch.qos.logback.classic.pattern.RelativeTimeConverter;
import ch.qos.logback.classic.pattern.DateConverter;
import ch.qos.logback.core.pattern.parser.Parser;
import java.util.HashMap;
import java.util.Map;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutBase;

public class PatternLayout extends PatternLayoutBase<ILoggingEvent>
{
    public static final Map<String, String> defaultConverterMap;
    public static final String HEADER_PREFIX = "#logback.classic pattern: ";
    
    static {
        (defaultConverterMap = new HashMap<String, String>()).putAll(Parser.DEFAULT_COMPOSITE_CONVERTER_MAP);
        PatternLayout.defaultConverterMap.put("d", DateConverter.class.getName());
        PatternLayout.defaultConverterMap.put("date", DateConverter.class.getName());
        PatternLayout.defaultConverterMap.put("r", RelativeTimeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("relative", RelativeTimeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("level", LevelConverter.class.getName());
        PatternLayout.defaultConverterMap.put("le", LevelConverter.class.getName());
        PatternLayout.defaultConverterMap.put("p", LevelConverter.class.getName());
        PatternLayout.defaultConverterMap.put("t", ThreadConverter.class.getName());
        PatternLayout.defaultConverterMap.put("thread", ThreadConverter.class.getName());
        PatternLayout.defaultConverterMap.put("lo", LoggerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("logger", LoggerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("c", LoggerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("m", MessageConverter.class.getName());
        PatternLayout.defaultConverterMap.put("msg", MessageConverter.class.getName());
        PatternLayout.defaultConverterMap.put("message", MessageConverter.class.getName());
        PatternLayout.defaultConverterMap.put("C", ClassOfCallerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("class", ClassOfCallerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("M", MethodOfCallerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("method", MethodOfCallerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("L", LineOfCallerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("line", LineOfCallerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("F", FileOfCallerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("file", FileOfCallerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("X", MDCConverter.class.getName());
        PatternLayout.defaultConverterMap.put("mdc", MDCConverter.class.getName());
        PatternLayout.defaultConverterMap.put("ex", ThrowableProxyConverter.class.getName());
        PatternLayout.defaultConverterMap.put("exception", ThrowableProxyConverter.class.getName());
        PatternLayout.defaultConverterMap.put("rEx", RootCauseFirstThrowableProxyConverter.class.getName());
        PatternLayout.defaultConverterMap.put("rootException", RootCauseFirstThrowableProxyConverter.class.getName());
        PatternLayout.defaultConverterMap.put("throwable", ThrowableProxyConverter.class.getName());
        PatternLayout.defaultConverterMap.put("xEx", ExtendedThrowableProxyConverter.class.getName());
        PatternLayout.defaultConverterMap.put("xException", ExtendedThrowableProxyConverter.class.getName());
        PatternLayout.defaultConverterMap.put("xThrowable", ExtendedThrowableProxyConverter.class.getName());
        PatternLayout.defaultConverterMap.put("nopex", NopThrowableInformationConverter.class.getName());
        PatternLayout.defaultConverterMap.put("nopexception", NopThrowableInformationConverter.class.getName());
        PatternLayout.defaultConverterMap.put("cn", ContextNameConverter.class.getName());
        PatternLayout.defaultConverterMap.put("contextName", ContextNameConverter.class.getName());
        PatternLayout.defaultConverterMap.put("caller", CallerDataConverter.class.getName());
        PatternLayout.defaultConverterMap.put("marker", MarkerConverter.class.getName());
        PatternLayout.defaultConverterMap.put("property", PropertyConverter.class.getName());
        PatternLayout.defaultConverterMap.put("n", LineSeparatorConverter.class.getName());
        PatternLayout.defaultConverterMap.put("black", BlackCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("red", RedCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("green", GreenCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("yellow", YellowCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("blue", BlueCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("magenta", MagentaCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("cyan", CyanCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("white", WhiteCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("gray", GrayCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("boldRed", BoldRedCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("boldGreen", BoldGreenCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("boldYellow", BoldYellowCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("boldBlue", BoldBlueCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("boldMagenta", BoldMagentaCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("boldCyan", BoldCyanCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("boldWhite", BoldWhiteCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("highlight", HighlightingCompositeConverter.class.getName());
        PatternLayout.defaultConverterMap.put("lsn", LocalSequenceNumberConverter.class.getName());
    }
    
    public PatternLayout() {
        this.postCompileProcessor = (PostCompileProcessor<E>)new EnsureExceptionHandling();
    }
    
    @Override
    public Map<String, String> getDefaultConverterMap() {
        return PatternLayout.defaultConverterMap;
    }
    
    @Override
    public String doLayout(final ILoggingEvent event) {
        if (!this.isStarted()) {
            return "";
        }
        return this.writeLoopOnConverters(event);
    }
    
    @Override
    protected String getPresentationHeaderPrefix() {
        return "#logback.classic pattern: ";
    }
}
