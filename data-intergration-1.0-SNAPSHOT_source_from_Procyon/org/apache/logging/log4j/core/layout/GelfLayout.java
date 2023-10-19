// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.util.zip.GZIPOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.logging.log4j.core.net.Severity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.core.util.JsonUtils;
import java.util.zip.DeflaterOutputStream;
import java.io.IOException;
import org.apache.logging.log4j.status.StatusLogger;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Collections;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.util.TriConsumer;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "GelfLayout", category = "Core", elementType = "layout", printObject = true)
public final class GelfLayout extends AbstractStringLayout
{
    private static final char C = ',';
    private static final int COMPRESSION_THRESHOLD = 1024;
    private static final char Q = '\"';
    private static final String QC = "\",";
    private static final String QU = "\"_";
    private final KeyValuePair[] additionalFields;
    private final int compressionThreshold;
    private final CompressionType compressionType;
    private final String host;
    private final boolean includeStacktrace;
    private static final TriConsumer<String, Object, StringBuilder> WRITE_KEY_VALUES_INTO;
    private static final ThreadLocal<StringBuilder> messageStringBuilder;
    private static final ThreadLocal<StringBuilder> timestampStringBuilder;
    
    public GelfLayout(final String host, final KeyValuePair[] additionalFields, final CompressionType compressionType, final int compressionThreshold, final boolean includeStacktrace) {
        super(StandardCharsets.UTF_8);
        this.host = host;
        this.additionalFields = additionalFields;
        this.compressionType = compressionType;
        this.compressionThreshold = compressionThreshold;
        this.includeStacktrace = includeStacktrace;
    }
    
    @PluginFactory
    public static GelfLayout createLayout(@PluginAttribute("host") final String host, @PluginElement("AdditionalField") final KeyValuePair[] additionalFields, @PluginAttribute(value = "compressionType", defaultString = "GZIP") final CompressionType compressionType, @PluginAttribute(value = "compressionThreshold", defaultInt = 1024) final int compressionThreshold, @PluginAttribute(value = "includeStacktrace", defaultBoolean = true) final boolean includeStacktrace) {
        return new GelfLayout(host, additionalFields, compressionType, compressionThreshold, includeStacktrace);
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        return Collections.emptyMap();
    }
    
    @Override
    public String getContentType() {
        return "application/json; charset=" + this.getCharset();
    }
    
    @Override
    public byte[] toByteArray(final LogEvent event) {
        final StringBuilder text = this.toText(event, AbstractStringLayout.getStringBuilder(), false);
        final byte[] bytes = this.getBytes(text.toString());
        return (this.compressionType != CompressionType.OFF && bytes.length > this.compressionThreshold) ? this.compress(bytes) : bytes;
    }
    
    @Override
    public void encode(final LogEvent event, final ByteBufferDestination destination) {
        if (this.compressionType != CompressionType.OFF) {
            super.encode(event, destination);
            return;
        }
        final StringBuilder text = this.toText(event, AbstractStringLayout.getStringBuilder(), true);
        final Encoder<StringBuilder> helper = this.getStringBuilderEncoder();
        helper.encode(text, destination);
    }
    
    private byte[] compress(final byte[] bytes) {
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(this.compressionThreshold / 8);
            try (final DeflaterOutputStream stream = this.compressionType.createDeflaterOutputStream(baos)) {
                if (stream == null) {
                    return bytes;
                }
                stream.write(bytes);
                stream.finish();
            }
            return baos.toByteArray();
        }
        catch (IOException e) {
            StatusLogger.getLogger().error(e);
            return bytes;
        }
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder text = this.toText(event, AbstractStringLayout.getStringBuilder(), false);
        return text.toString();
    }
    
    private StringBuilder toText(final LogEvent event, final StringBuilder builder, final boolean gcFree) {
        builder.append('{');
        builder.append("\"version\":\"1.1\",");
        builder.append("\"host\":\"");
        JsonUtils.quoteAsString(toNullSafeString(this.host), builder);
        builder.append("\",");
        builder.append("\"timestamp\":").append(formatTimestamp(event.getTimeMillis())).append(',');
        builder.append("\"level\":").append(this.formatLevel(event.getLevel())).append(',');
        if (event.getThreadName() != null) {
            builder.append("\"_thread\":\"");
            JsonUtils.quoteAsString(event.getThreadName(), builder);
            builder.append("\",");
        }
        if (event.getLoggerName() != null) {
            builder.append("\"_logger\":\"");
            JsonUtils.quoteAsString(event.getLoggerName(), builder);
            builder.append("\",");
        }
        for (final KeyValuePair additionalField : this.additionalFields) {
            builder.append("\"_");
            JsonUtils.quoteAsString(additionalField.getKey(), builder);
            builder.append("\":\"");
            JsonUtils.quoteAsString(toNullSafeString(additionalField.getValue()), builder);
            builder.append("\",");
        }
        event.getContextData().forEach(GelfLayout.WRITE_KEY_VALUES_INTO, builder);
        if (event.getThrown() != null) {
            builder.append("\"full_message\":\"");
            if (this.includeStacktrace) {
                JsonUtils.quoteAsString(formatThrowable(event.getThrown()), builder);
            }
            else {
                JsonUtils.quoteAsString(event.getThrown().toString(), builder);
            }
            builder.append("\",");
        }
        builder.append("\"short_message\":\"");
        final Message message = event.getMessage();
        if (message instanceof CharSequence) {
            JsonUtils.quoteAsString((CharSequence)message, builder);
        }
        else if (gcFree && message instanceof StringBuilderFormattable) {
            final StringBuilder messageBuffer = getMessageStringBuilder();
            try {
                ((StringBuilderFormattable)message).formatTo(messageBuffer);
                JsonUtils.quoteAsString(messageBuffer, builder);
            }
            finally {
                AbstractStringLayout.trimToMaxSize(messageBuffer);
            }
        }
        else {
            JsonUtils.quoteAsString(toNullSafeString(message.getFormattedMessage()), builder);
        }
        builder.append('\"');
        builder.append('}');
        return builder;
    }
    
    private static StringBuilder getMessageStringBuilder() {
        StringBuilder result = GelfLayout.messageStringBuilder.get();
        if (result == null) {
            result = new StringBuilder(1024);
            GelfLayout.messageStringBuilder.set(result);
        }
        result.setLength(0);
        return result;
    }
    
    private static CharSequence toNullSafeString(final CharSequence s) {
        return (s == null) ? "" : s;
    }
    
    static CharSequence formatTimestamp(final long timeMillis) {
        if (timeMillis < 1000L) {
            return "0";
        }
        final StringBuilder builder = getTimestampStringBuilder();
        builder.append(timeMillis);
        builder.insert(builder.length() - 3, '.');
        return builder;
    }
    
    private static StringBuilder getTimestampStringBuilder() {
        StringBuilder result = GelfLayout.timestampStringBuilder.get();
        if (result == null) {
            result = new StringBuilder(20);
            GelfLayout.timestampStringBuilder.set(result);
        }
        result.setLength(0);
        return result;
    }
    
    private int formatLevel(final Level level) {
        return Severity.getSeverity(level).getCode();
    }
    
    static CharSequence formatThrowable(final Throwable throwable) {
        final StringWriter sw = new StringWriter(2048);
        final PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        return sw.getBuffer();
    }
    
    static {
        WRITE_KEY_VALUES_INTO = new TriConsumer<String, Object, StringBuilder>() {
            @Override
            public void accept(final String key, final Object value, final StringBuilder stringBuilder) {
                stringBuilder.append("\"_");
                JsonUtils.quoteAsString(key, stringBuilder);
                stringBuilder.append("\":\"");
                JsonUtils.quoteAsString(toNullSafeString(String.valueOf(value)), stringBuilder);
                stringBuilder.append("\",");
            }
        };
        messageStringBuilder = new ThreadLocal<StringBuilder>();
        timestampStringBuilder = new ThreadLocal<StringBuilder>();
    }
    
    public enum CompressionType
    {
        GZIP {
            @Override
            public DeflaterOutputStream createDeflaterOutputStream(final OutputStream os) throws IOException {
                return new GZIPOutputStream(os);
            }
        }, 
        ZLIB {
            @Override
            public DeflaterOutputStream createDeflaterOutputStream(final OutputStream os) throws IOException {
                return new DeflaterOutputStream(os);
            }
        }, 
        OFF {
            @Override
            public DeflaterOutputStream createDeflaterOutputStream(final OutputStream os) throws IOException {
                return null;
            }
        };
        
        public abstract DeflaterOutputStream createDeflaterOutputStream(final OutputStream p0) throws IOException;
    }
}
