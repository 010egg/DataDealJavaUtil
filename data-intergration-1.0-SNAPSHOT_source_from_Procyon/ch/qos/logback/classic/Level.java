// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic;

import java.io.Serializable;

public final class Level implements Serializable
{
    private static final long serialVersionUID = -814092767334282137L;
    public static final int OFF_INT = Integer.MAX_VALUE;
    public static final int ERROR_INT = 40000;
    public static final int WARN_INT = 30000;
    public static final int INFO_INT = 20000;
    public static final int DEBUG_INT = 10000;
    public static final int TRACE_INT = 5000;
    public static final int ALL_INT = Integer.MIN_VALUE;
    public static final Integer OFF_INTEGER;
    public static final Integer ERROR_INTEGER;
    public static final Integer WARN_INTEGER;
    public static final Integer INFO_INTEGER;
    public static final Integer DEBUG_INTEGER;
    public static final Integer TRACE_INTEGER;
    public static final Integer ALL_INTEGER;
    public static final Level OFF;
    public static final Level ERROR;
    public static final Level WARN;
    public static final Level INFO;
    public static final Level DEBUG;
    public static final Level TRACE;
    public static final Level ALL;
    public final int levelInt;
    public final String levelStr;
    
    static {
        OFF_INTEGER = Integer.MAX_VALUE;
        ERROR_INTEGER = 40000;
        WARN_INTEGER = 30000;
        INFO_INTEGER = 20000;
        DEBUG_INTEGER = 10000;
        TRACE_INTEGER = 5000;
        ALL_INTEGER = Integer.MIN_VALUE;
        OFF = new Level(Integer.MAX_VALUE, "OFF");
        ERROR = new Level(40000, "ERROR");
        WARN = new Level(30000, "WARN");
        INFO = new Level(20000, "INFO");
        DEBUG = new Level(10000, "DEBUG");
        TRACE = new Level(5000, "TRACE");
        ALL = new Level(Integer.MIN_VALUE, "ALL");
    }
    
    private Level(final int levelInt, final String levelStr) {
        this.levelInt = levelInt;
        this.levelStr = levelStr;
    }
    
    @Override
    public String toString() {
        return this.levelStr;
    }
    
    public int toInt() {
        return this.levelInt;
    }
    
    public Integer toInteger() {
        switch (this.levelInt) {
            case Integer.MIN_VALUE: {
                return Level.ALL_INTEGER;
            }
            case 5000: {
                return Level.TRACE_INTEGER;
            }
            case 10000: {
                return Level.DEBUG_INTEGER;
            }
            case 20000: {
                return Level.INFO_INTEGER;
            }
            case 30000: {
                return Level.WARN_INTEGER;
            }
            case 40000: {
                return Level.ERROR_INTEGER;
            }
            case Integer.MAX_VALUE: {
                return Level.OFF_INTEGER;
            }
            default: {
                throw new IllegalStateException("Level " + this.levelStr + ", " + this.levelInt + " is unknown.");
            }
        }
    }
    
    public boolean isGreaterOrEqual(final Level r) {
        return this.levelInt >= r.levelInt;
    }
    
    public static Level toLevel(final String sArg) {
        return toLevel(sArg, Level.DEBUG);
    }
    
    public static Level valueOf(final String sArg) {
        return toLevel(sArg, Level.DEBUG);
    }
    
    public static Level toLevel(final int val) {
        return toLevel(val, Level.DEBUG);
    }
    
    public static Level toLevel(final int val, final Level defaultLevel) {
        switch (val) {
            case Integer.MIN_VALUE: {
                return Level.ALL;
            }
            case 5000: {
                return Level.TRACE;
            }
            case 10000: {
                return Level.DEBUG;
            }
            case 20000: {
                return Level.INFO;
            }
            case 30000: {
                return Level.WARN;
            }
            case 40000: {
                return Level.ERROR;
            }
            case Integer.MAX_VALUE: {
                return Level.OFF;
            }
            default: {
                return defaultLevel;
            }
        }
    }
    
    public static Level toLevel(final String sArg, final Level defaultLevel) {
        if (sArg == null) {
            return defaultLevel;
        }
        if (sArg.equalsIgnoreCase("ALL")) {
            return Level.ALL;
        }
        if (sArg.equalsIgnoreCase("TRACE")) {
            return Level.TRACE;
        }
        if (sArg.equalsIgnoreCase("DEBUG")) {
            return Level.DEBUG;
        }
        if (sArg.equalsIgnoreCase("INFO")) {
            return Level.INFO;
        }
        if (sArg.equalsIgnoreCase("WARN")) {
            return Level.WARN;
        }
        if (sArg.equalsIgnoreCase("ERROR")) {
            return Level.ERROR;
        }
        if (sArg.equalsIgnoreCase("OFF")) {
            return Level.OFF;
        }
        return defaultLevel;
    }
    
    private Object readResolve() {
        return toLevel(this.levelInt);
    }
    
    public static Level fromLocationAwareLoggerInteger(final int levelInt) {
        Level level = null;
        switch (levelInt) {
            case 0: {
                level = Level.TRACE;
                break;
            }
            case 10: {
                level = Level.DEBUG;
                break;
            }
            case 20: {
                level = Level.INFO;
                break;
            }
            case 30: {
                level = Level.WARN;
                break;
            }
            case 40: {
                level = Level.ERROR;
                break;
            }
            default: {
                throw new IllegalArgumentException(String.valueOf(levelInt) + " not a valid level value");
            }
        }
        return level;
    }
    
    public static int toLocationAwareLoggerInteger(final Level level) {
        if (level == null) {
            throw new IllegalArgumentException("null level parameter is not admitted");
        }
        switch (level.toInt()) {
            case 5000: {
                return 0;
            }
            case 10000: {
                return 10;
            }
            case 20000: {
                return 20;
            }
            case 30000: {
                return 30;
            }
            case 40000: {
                return 40;
            }
            default: {
                throw new IllegalArgumentException(level + " not a valid level value");
            }
        }
    }
}
