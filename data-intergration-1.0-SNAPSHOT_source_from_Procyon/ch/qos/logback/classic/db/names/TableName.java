// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.db.names;

public enum TableName
{
    LOGGING_EVENT("LOGGING_EVENT", 0), 
    LOGGING_EVENT_PROPERTY("LOGGING_EVENT_PROPERTY", 1), 
    LOGGING_EVENT_EXCEPTION("LOGGING_EVENT_EXCEPTION", 2);
    
    private TableName(final String name, final int ordinal) {
    }
}
