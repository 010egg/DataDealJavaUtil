// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.classic.spi.IThrowableProxy;

public class RootCauseFirstThrowableProxyConverter extends ExtendedThrowableProxyConverter
{
    @Override
    protected String throwableProxyToString(final IThrowableProxy tp) {
        final StringBuilder buf = new StringBuilder(2048);
        this.recursiveAppendRootCauseFirst(buf, null, 1, tp);
        return buf.toString();
    }
    
    protected void recursiveAppendRootCauseFirst(final StringBuilder sb, String prefix, final int indent, final IThrowableProxy tp) {
        if (tp.getCause() != null) {
            this.recursiveAppendRootCauseFirst(sb, prefix, indent, tp.getCause());
            prefix = null;
        }
        ThrowableProxyUtil.indent(sb, indent - 1);
        if (prefix != null) {
            sb.append(prefix);
        }
        ThrowableProxyUtil.subjoinFirstLineRootCauseFirst(sb, tp);
        sb.append(CoreConstants.LINE_SEPARATOR);
        this.subjoinSTEPArray(sb, indent, tp);
        final IThrowableProxy[] suppressed = tp.getSuppressed();
        if (suppressed != null) {
            IThrowableProxy[] array;
            for (int length = (array = suppressed).length, i = 0; i < length; ++i) {
                final IThrowableProxy current = array[i];
                this.recursiveAppendRootCauseFirst(sb, "Suppressed: ", indent + 1, current);
            }
        }
    }
}
