// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.db.names;

public enum ColumnName
{
    EVENT_ID("EVENT_ID", 0), 
    TIMESTMP("TIMESTMP", 1), 
    FORMATTED_MESSAGE("FORMATTED_MESSAGE", 2), 
    LOGGER_NAME("LOGGER_NAME", 3), 
    LEVEL_STRING("LEVEL_STRING", 4), 
    THREAD_NAME("THREAD_NAME", 5), 
    REFERENCE_FLAG("REFERENCE_FLAG", 6), 
    ARG0("ARG0", 7), 
    ARG1("ARG1", 8), 
    ARG2("ARG2", 9), 
    ARG3("ARG3", 10), 
    CALLER_FILENAME("CALLER_FILENAME", 11), 
    CALLER_CLASS("CALLER_CLASS", 12), 
    CALLER_METHOD("CALLER_METHOD", 13), 
    CALLER_LINE("CALLER_LINE", 14), 
    MAPPED_KEY("MAPPED_KEY", 15), 
    MAPPED_VALUE("MAPPED_VALUE", 16), 
    I("I", 17), 
    TRACE_LINE("TRACE_LINE", 18);
    
    private ColumnName(final String name, final int ordinal) {
    }
}
