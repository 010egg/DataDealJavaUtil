// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

public final class Assert
{
    private Assert() {
    }
    
    public static int valueIsAtLeast(final int value, final int minValue) {
        if (value < minValue) {
            throw new IllegalArgumentException("Value should be at least " + minValue + " but was " + value);
        }
        return value;
    }
}
