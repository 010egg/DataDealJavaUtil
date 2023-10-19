// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.sift;

public class DefaultDiscriminator<E> extends AbstractDiscriminator<E>
{
    public static final String DEFAULT = "default";
    
    @Override
    public String getDiscriminatingValue(final E e) {
        return "default";
    }
    
    @Override
    public String getKey() {
        return "default";
    }
}
