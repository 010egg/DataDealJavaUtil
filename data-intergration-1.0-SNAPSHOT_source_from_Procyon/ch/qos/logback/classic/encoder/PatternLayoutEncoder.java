// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.encoder;

import ch.qos.logback.core.Layout;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

public class PatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent>
{
    @Override
    public void start() {
        final PatternLayout patternLayout = new PatternLayout();
        patternLayout.setContext(this.context);
        patternLayout.setPattern(this.getPattern());
        patternLayout.setOutputPatternAsHeader(this.outputPatternAsHeader);
        patternLayout.start();
        this.layout = (Layout<E>)patternLayout;
        super.start();
    }
}
