// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

public class ClassNameOnlyAbbreviator implements Abbreviator
{
    @Override
    public String abbreviate(final String fqClassName) {
        final int lastIndex = fqClassName.lastIndexOf(46);
        if (lastIndex != -1) {
            return fqClassName.substring(lastIndex + 1, fqClassName.length());
        }
        return fqClassName;
    }
}
