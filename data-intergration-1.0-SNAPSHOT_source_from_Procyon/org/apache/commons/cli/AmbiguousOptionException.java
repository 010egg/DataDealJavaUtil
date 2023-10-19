// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.cli;

import java.util.Iterator;
import java.util.Collection;

public class AmbiguousOptionException extends UnrecognizedOptionException
{
    private static final long serialVersionUID = 5829816121277947229L;
    private final Collection<String> matchingOptions;
    
    public AmbiguousOptionException(final String option, final Collection<String> matchingOptions) {
        super(createMessage(option, matchingOptions), option);
        this.matchingOptions = matchingOptions;
    }
    
    public Collection<String> getMatchingOptions() {
        return this.matchingOptions;
    }
    
    private static String createMessage(final String option, final Collection<String> matchingOptions) {
        final StringBuilder buf = new StringBuilder("Ambiguous option: '");
        buf.append(option);
        buf.append("'  (could be: ");
        final Iterator<String> it = matchingOptions.iterator();
        while (it.hasNext()) {
            buf.append("'");
            buf.append(it.next());
            buf.append("'");
            if (it.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append(")");
        return buf.toString();
    }
}
