// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.pattern;

import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;

public class PatternLayoutEncoderBase<E> extends LayoutWrappingEncoder<E>
{
    String pattern;
    protected boolean outputPatternAsHeader;
    
    public PatternLayoutEncoderBase() {
        this.outputPatternAsHeader = false;
    }
    
    public String getPattern() {
        return this.pattern;
    }
    
    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }
    
    public boolean isOutputPatternAsHeader() {
        return this.outputPatternAsHeader;
    }
    
    public void setOutputPatternAsHeader(final boolean outputPatternAsHeader) {
        this.outputPatternAsHeader = outputPatternAsHeader;
    }
    
    public boolean isOutputPatternAsPresentationHeader() {
        return this.outputPatternAsHeader;
    }
    
    @Deprecated
    public void setOutputPatternAsPresentationHeader(final boolean outputPatternAsHeader) {
        this.addWarn("[outputPatternAsPresentationHeader] property is deprecated. Please use [outputPatternAsHeader] option instead.");
        this.outputPatternAsHeader = outputPatternAsHeader;
    }
    
    @Override
    public void setLayout(final Layout<E> layout) {
        throw new UnsupportedOperationException("one cannot set the layout of " + this.getClass().getName());
    }
}
