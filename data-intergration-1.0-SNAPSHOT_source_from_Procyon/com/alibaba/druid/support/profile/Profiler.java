// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.profile;

import java.util.LinkedHashMap;
import java.util.Map;

public class Profiler
{
    public static final String PROFILE_TYPE_WEB = "WEB";
    public static final String PROFILE_TYPE_SPRING = "SPRING";
    public static final String PROFILE_TYPE_SQL = "SQL";
    private static ThreadLocal<Map<ProfileEntryKey, ProfileEntryReqStat>> statsMapLocal;
    private static final ThreadLocal<ProfileEntry> currentLocal;
    
    public static boolean isEnable() {
        return Profiler.statsMapLocal != null;
    }
    
    public static void enter(final String name, final String type) {
        if (!isEnable()) {
            return;
        }
        final ProfileEntry parent = Profiler.currentLocal.get();
        String parentName = null;
        if (parent != null) {
            parentName = parent.getName();
        }
        final ProfileEntryKey key = new ProfileEntryKey(parentName, name, type);
        final ProfileEntry entry = new ProfileEntry(parent, key);
        Profiler.currentLocal.set(entry);
    }
    
    public static ProfileEntry current() {
        return Profiler.currentLocal.get();
    }
    
    public static void release(final long nanos) {
        final ProfileEntry current = Profiler.currentLocal.get();
        if (current == null) {
            return;
        }
        Profiler.currentLocal.set(current.getParent());
        ProfileEntryReqStat stat = null;
        final Map<ProfileEntryKey, ProfileEntryReqStat> statsMap = Profiler.statsMapLocal.get();
        if (statsMap == null) {
            return;
        }
        stat = statsMap.get(current.getKey());
        if (stat == null) {
            stat = new ProfileEntryReqStat();
            statsMap.put(current.getKey(), stat);
        }
        stat.incrementExecuteCount();
        stat.addExecuteTimeNanos(nanos);
    }
    
    public static Map<ProfileEntryKey, ProfileEntryReqStat> getStatsMap() {
        return Profiler.statsMapLocal.get();
    }
    
    public static void initLocal() {
        Profiler.statsMapLocal.set(new LinkedHashMap<ProfileEntryKey, ProfileEntryReqStat>());
    }
    
    public static void removeLocal() {
        Profiler.statsMapLocal.remove();
    }
    
    static {
        Profiler.statsMapLocal = new ThreadLocal<Map<ProfileEntryKey, ProfileEntryReqStat>>();
        currentLocal = new ThreadLocal<ProfileEntry>();
    }
}
