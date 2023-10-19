// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.pattern;

public class IdentityCompositeConverter<E> extends CompositeConverter<E>
{
    @Override
    protected String transform(final E event, final String in) {
        return in;
    }
}
