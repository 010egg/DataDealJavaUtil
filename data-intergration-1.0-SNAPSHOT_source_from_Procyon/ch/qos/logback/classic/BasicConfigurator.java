// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.Layout;
import ch.qos.logback.classic.layout.TTLLLayout;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.Context;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.spi.ContextAwareBase;

public class BasicConfigurator extends ContextAwareBase implements Configurator
{
    @Override
    public void configure(final LoggerContext lc) {
        this.addInfo("Setting up default configuration.");
        final ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
        ca.setContext(lc);
        ca.setName("console");
        final LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<ILoggingEvent>();
        encoder.setContext(lc);
        final TTLLLayout layout = new TTLLLayout();
        layout.setContext(lc);
        layout.start();
        encoder.setLayout(layout);
        ca.setEncoder(encoder);
        ca.start();
        final Logger rootLogger = lc.getLogger("ROOT");
        rootLogger.addAppender(ca);
    }
}
