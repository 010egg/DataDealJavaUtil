// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.pattern;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.Context;

public class ConverterUtil
{
    public static <E> void startConverters(final Converter<E> head) {
        for (Converter<E> c = head; c != null; c = c.getNext()) {
            if (c instanceof CompositeConverter) {
                final CompositeConverter<E> cc = (CompositeConverter<E>)(CompositeConverter)c;
                final Converter<E> childConverter = cc.childConverter;
                startConverters((Converter<Object>)childConverter);
                cc.start();
            }
            else if (c instanceof DynamicConverter) {
                final DynamicConverter<E> dc = (DynamicConverter<E>)(DynamicConverter)c;
                dc.start();
            }
        }
    }
    
    public static <E> Converter<E> findTail(final Converter<E> head) {
        Converter<E> p;
        Converter<E> next;
        for (p = head; p != null; p = next) {
            next = p.getNext();
            if (next == null) {
                break;
            }
        }
        return p;
    }
    
    public static <E> void setContextForConverters(final Context context, final Converter<E> head) {
        for (Converter<E> c = head; c != null; c = c.getNext()) {
            if (c instanceof ContextAware) {
                ((ContextAware)c).setContext(context);
            }
        }
    }
}
