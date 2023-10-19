// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.helpers;

import java.util.Collection;
import org.slf4j.Logger;
import java.util.Collections;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.event.SubstituteLoggingEvent;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.ILoggerFactory;

public class SubstituteLoggerFactory implements ILoggerFactory
{
    final ConcurrentMap<String, SubstituteLogger> loggers;
    final List<SubstituteLoggingEvent> eventList;
    
    public SubstituteLoggerFactory() {
        this.loggers = new ConcurrentHashMap<String, SubstituteLogger>();
        this.eventList = Collections.synchronizedList(new ArrayList<SubstituteLoggingEvent>());
    }
    
    public Logger getLogger(final String name) {
        SubstituteLogger logger = this.loggers.get(name);
        if (logger == null) {
            logger = new SubstituteLogger(name, this.eventList);
            final SubstituteLogger oldLogger = this.loggers.putIfAbsent(name, logger);
            if (oldLogger != null) {
                logger = oldLogger;
            }
        }
        return logger;
    }
    
    public List<String> getLoggerNames() {
        return new ArrayList<String>((Collection<? extends String>)this.loggers.keySet());
    }
    
    public List<SubstituteLogger> getLoggers() {
        return new ArrayList<SubstituteLogger>(this.loggers.values());
    }
    
    public List<SubstituteLoggingEvent> getEventList() {
        return this.eventList;
    }
    
    public void clear() {
        this.loggers.clear();
        this.eventList.clear();
    }
}
