// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class StringCollectionUtil
{
    public static void retainMatching(final Collection<String> values, final String... patterns) {
        retainMatching(values, Arrays.asList(patterns));
    }
    
    public static void retainMatching(final Collection<String> values, final Collection<String> patterns) {
        if (patterns.isEmpty()) {
            return;
        }
        final List<String> matches = new ArrayList<String>(values.size());
        for (final String p : patterns) {
            final Pattern pattern = Pattern.compile(p);
            for (final String value : values) {
                if (pattern.matcher(value).matches()) {
                    matches.add(value);
                }
            }
        }
        values.retainAll(matches);
    }
    
    public static void removeMatching(final Collection<String> values, final String... patterns) {
        removeMatching(values, Arrays.asList(patterns));
    }
    
    public static void removeMatching(final Collection<String> values, final Collection<String> patterns) {
        final List<String> matches = new ArrayList<String>(values.size());
        for (final String p : patterns) {
            final Pattern pattern = Pattern.compile(p);
            for (final String value : values) {
                if (pattern.matcher(value).matches()) {
                    matches.add(value);
                }
            }
        }
        values.removeAll(matches);
    }
}
