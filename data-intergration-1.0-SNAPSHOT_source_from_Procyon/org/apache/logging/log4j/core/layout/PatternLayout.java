// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import java.util.Arrays;
import java.io.Serializable;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.LogEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.util.Strings;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "PatternLayout", category = "Core", elementType = "layout", printObject = true)
public final class PatternLayout extends AbstractStringLayout
{
    public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";
    public static final String TTCC_CONVERSION_PATTERN = "%r [%t] %p %c %notEmpty{%x }- %m%n";
    public static final String SIMPLE_CONVERSION_PATTERN = "%d [%t] %p %c - %m%n";
    public static final String KEY = "Converter";
    private final String conversionPattern;
    private final PatternSelector patternSelector;
    private final Serializer eventSerializer;
    
    private PatternLayout(final Configuration config, final RegexReplacement replace, final String eventPattern, final PatternSelector patternSelector, final Charset charset, final boolean alwaysWriteExceptions, final boolean noConsoleNoAnsi, final String headerPattern, final String footerPattern) {
        super(config, charset, createSerializer(config, replace, headerPattern, null, patternSelector, alwaysWriteExceptions, noConsoleNoAnsi), createSerializer(config, replace, footerPattern, null, patternSelector, alwaysWriteExceptions, noConsoleNoAnsi));
        this.conversionPattern = eventPattern;
        this.patternSelector = patternSelector;
        this.eventSerializer = createSerializer(config, replace, eventPattern, "%m%n", patternSelector, alwaysWriteExceptions, noConsoleNoAnsi);
    }
    
    public static Serializer createSerializer(final Configuration configuration, final RegexReplacement replace, final String pattern, final String defaultPattern, final PatternSelector patternSelector, final boolean alwaysWriteExceptions, final boolean noConsoleNoAnsi) {
        if (Strings.isEmpty(pattern) && Strings.isEmpty(defaultPattern)) {
            return null;
        }
        if (patternSelector == null) {
            try {
                final PatternParser parser = createPatternParser(configuration);
                final List<PatternFormatter> list = parser.parse((pattern == null) ? defaultPattern : pattern, alwaysWriteExceptions, noConsoleNoAnsi);
                final PatternFormatter[] formatters = list.toArray(new PatternFormatter[0]);
                return new PatternSerializer(formatters, replace);
            }
            catch (RuntimeException ex) {
                throw new IllegalArgumentException("Cannot parse pattern '" + pattern + "'", ex);
            }
        }
        return new PatternSelectorSerializer(patternSelector, replace);
    }
    
    public String getConversionPattern() {
        return this.conversionPattern;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("structured", "false");
        result.put("formatType", "conversion");
        result.put("format", this.conversionPattern);
        return result;
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        return this.eventSerializer.toSerializable(event);
    }
    
    @Override
    public void encode(final LogEvent event, final ByteBufferDestination destination) {
        if (!(this.eventSerializer instanceof Serializer2)) {
            super.encode(event, destination);
            return;
        }
        final StringBuilder text = this.toText((Serializer2)this.eventSerializer, event, AbstractStringLayout.getStringBuilder());
        final Encoder<StringBuilder> encoder = this.getStringBuilderEncoder();
        encoder.encode(text, destination);
        AbstractStringLayout.trimToMaxSize(text);
    }
    
    private StringBuilder toText(final Serializer2 serializer, final LogEvent event, final StringBuilder destination) {
        return serializer.toSerializable(event, destination);
    }
    
    public static PatternParser createPatternParser(final Configuration config) {
        if (config == null) {
            return new PatternParser(config, "Converter", LogEventPatternConverter.class);
        }
        PatternParser parser = config.getComponent("Converter");
        if (parser == null) {
            parser = new PatternParser(config, "Converter", LogEventPatternConverter.class);
            config.addComponent("Converter", parser);
            parser = config.getComponent("Converter");
        }
        return parser;
    }
    
    @Override
    public String toString() {
        return (this.patternSelector == null) ? this.conversionPattern : this.patternSelector.toString();
    }
    
    @PluginFactory
    public static PatternLayout createLayout(@PluginAttribute(value = "pattern", defaultString = "%m%n") final String pattern, @PluginElement("PatternSelector") final PatternSelector patternSelector, @PluginConfiguration final Configuration config, @PluginElement("Replace") final RegexReplacement replace, @PluginAttribute("charset") final Charset charset, @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) final boolean alwaysWriteExceptions, @PluginAttribute(value = "noConsoleNoAnsi", defaultBoolean = false) final boolean noConsoleNoAnsi, @PluginAttribute("header") final String headerPattern, @PluginAttribute("footer") final String footerPattern) {
        return newBuilder().withPattern(pattern).withPatternSelector(patternSelector).withConfiguration(config).withRegexReplacement(replace).withCharset(charset).withAlwaysWriteExceptions(alwaysWriteExceptions).withNoConsoleNoAnsi(noConsoleNoAnsi).withHeader(headerPattern).withFooter(footerPattern).build();
    }
    
    public static PatternLayout createDefaultLayout() {
        return newBuilder().build();
    }
    
    public static PatternLayout createDefaultLayout(final Configuration configuration) {
        return newBuilder().withConfiguration(configuration).build();
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    private static class PatternSerializer implements Serializer, Serializer2
    {
        private final PatternFormatter[] formatters;
        private final RegexReplacement replace;
        
        private PatternSerializer(final PatternFormatter[] formatters, final RegexReplacement replace) {
            this.formatters = formatters;
            this.replace = replace;
        }
        
        @Override
        public String toSerializable(final LogEvent event) {
            final StringBuilder sb = AbstractStringLayout.getStringBuilder();
            try {
                return this.toSerializable(event, sb).toString();
            }
            finally {
                AbstractStringLayout.trimToMaxSize(sb);
            }
        }
        
        @Override
        public StringBuilder toSerializable(final LogEvent event, final StringBuilder buffer) {
            for (int len = this.formatters.length, i = 0; i < len; ++i) {
                this.formatters[i].format(event, buffer);
            }
            if (this.replace != null) {
                String str = buffer.toString();
                str = this.replace.format(str);
                buffer.setLength(0);
                buffer.append(str);
            }
            return buffer;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(super.toString());
            builder.append("[formatters=");
            builder.append(Arrays.toString(this.formatters));
            builder.append(", replace=");
            builder.append(this.replace);
            builder.append("]");
            return builder.toString();
        }
    }
    
    private static class PatternSelectorSerializer implements Serializer, Serializer2
    {
        private final PatternSelector patternSelector;
        private final RegexReplacement replace;
        
        private PatternSelectorSerializer(final PatternSelector patternSelector, final RegexReplacement replace) {
            this.patternSelector = patternSelector;
            this.replace = replace;
        }
        
        @Override
        public String toSerializable(final LogEvent event) {
            final StringBuilder sb = AbstractStringLayout.getStringBuilder();
            try {
                return this.toSerializable(event, sb).toString();
            }
            finally {
                AbstractStringLayout.trimToMaxSize(sb);
            }
        }
        
        @Override
        public StringBuilder toSerializable(final LogEvent event, final StringBuilder buffer) {
            final PatternFormatter[] formatters = this.patternSelector.getFormatters(event);
            for (int len = formatters.length, i = 0; i < len; ++i) {
                formatters[i].format(event, buffer);
            }
            if (this.replace != null) {
                String str = buffer.toString();
                str = this.replace.format(str);
                buffer.setLength(0);
                buffer.append(str);
            }
            return buffer;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(super.toString());
            builder.append("[patternSelector=");
            builder.append(this.patternSelector);
            builder.append(", replace=");
            builder.append(this.replace);
            builder.append("]");
            return builder.toString();
        }
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<PatternLayout>
    {
        @PluginBuilderAttribute
        private String pattern;
        @PluginElement("PatternSelector")
        private PatternSelector patternSelector;
        @PluginConfiguration
        private Configuration configuration;
        @PluginElement("Replace")
        private RegexReplacement regexReplacement;
        @PluginBuilderAttribute
        private Charset charset;
        @PluginBuilderAttribute
        private boolean alwaysWriteExceptions;
        @PluginBuilderAttribute
        private boolean noConsoleNoAnsi;
        @PluginBuilderAttribute
        private String header;
        @PluginBuilderAttribute
        private String footer;
        
        private Builder() {
            this.pattern = "%m%n";
            this.charset = Charset.defaultCharset();
            this.alwaysWriteExceptions = true;
        }
        
        public Builder withPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }
        
        public Builder withPatternSelector(final PatternSelector patternSelector) {
            this.patternSelector = patternSelector;
            return this;
        }
        
        public Builder withConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        public Builder withRegexReplacement(final RegexReplacement regexReplacement) {
            this.regexReplacement = regexReplacement;
            return this;
        }
        
        public Builder withCharset(final Charset charset) {
            if (charset != null) {
                this.charset = charset;
            }
            return this;
        }
        
        public Builder withAlwaysWriteExceptions(final boolean alwaysWriteExceptions) {
            this.alwaysWriteExceptions = alwaysWriteExceptions;
            return this;
        }
        
        public Builder withNoConsoleNoAnsi(final boolean noConsoleNoAnsi) {
            this.noConsoleNoAnsi = noConsoleNoAnsi;
            return this;
        }
        
        public Builder withHeader(final String header) {
            this.header = header;
            return this;
        }
        
        public Builder withFooter(final String footer) {
            this.footer = footer;
            return this;
        }
        
        @Override
        public PatternLayout build() {
            if (this.configuration == null) {
                this.configuration = new DefaultConfiguration();
            }
            return new PatternLayout(this.configuration, this.regexReplacement, this.pattern, this.patternSelector, this.charset, this.alwaysWriteExceptions, this.noConsoleNoAnsi, this.header, this.footer, null);
        }
    }
}
