// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.pattern.color;

import ch.qos.logback.core.pattern.CompositeConverter;

public abstract class ForegroundCompositeConverterBase<E> extends CompositeConverter<E>
{
    private static final String SET_DEFAULT_COLOR = "\u001b[0;39m";
    
    @Override
    protected String transform(final E event, final String in) {
        final StringBuilder sb = new StringBuilder();
        sb.append("\u001b[");
        sb.append(this.getForegroundColorCode(event));
        sb.append("m");
        sb.append(in);
        sb.append("\u001b[0;39m");
        return sb.toString();
    }
    
    protected abstract String getForegroundColorCode(final E p0);
}
