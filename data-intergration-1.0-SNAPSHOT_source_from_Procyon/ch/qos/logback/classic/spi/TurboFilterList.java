// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.Marker;
import ch.qos.logback.classic.turbo.TurboFilter;
import java.util.concurrent.CopyOnWriteArrayList;

public final class TurboFilterList extends CopyOnWriteArrayList<TurboFilter>
{
    private static final long serialVersionUID = 1L;
    
    public FilterReply getTurboFilterChainDecision(final Marker marker, final Logger logger, final Level level, final String format, final Object[] params, final Throwable t) {
        final int size = this.size();
        if (size == 1) {
            try {
                final TurboFilter tf = this.get(0);
                return tf.decide(marker, logger, level, format, params, t);
            }
            catch (IndexOutOfBoundsException ex) {
                return FilterReply.NEUTRAL;
            }
        }
        final Object[] tfa = this.toArray();
        for (int len = tfa.length, i = 0; i < len; ++i) {
            final TurboFilter tf2 = (TurboFilter)tfa[i];
            final FilterReply r = tf2.decide(marker, logger, level, format, params, t);
            if (r == FilterReply.DENY || r == FilterReply.ACCEPT) {
                return r;
            }
        }
        return FilterReply.NEUTRAL;
    }
}
