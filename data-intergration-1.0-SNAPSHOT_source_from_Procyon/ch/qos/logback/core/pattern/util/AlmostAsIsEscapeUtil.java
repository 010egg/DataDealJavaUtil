// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.pattern.util;

public class AlmostAsIsEscapeUtil extends RestrictedEscapeUtil
{
    @Override
    public void escape(final String escapeChars, final StringBuffer buf, final char next, final int pointer) {
        super.escape("%)", buf, next, pointer);
    }
}
