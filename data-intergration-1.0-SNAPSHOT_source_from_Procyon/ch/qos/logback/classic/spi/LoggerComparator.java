// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Logger;
import java.util.Comparator;

public class LoggerComparator implements Comparator<Logger>
{
    @Override
    public int compare(final Logger l1, final Logger l2) {
        if (l1.getName().equals(l2.getName())) {
            return 0;
        }
        if (l1.getName().equals("ROOT")) {
            return -1;
        }
        if (l2.getName().equals("ROOT")) {
            return 1;
        }
        return l1.getName().compareTo(l2.getName());
    }
}
