// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.profile;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.Map;

public class ProfileStat
{
    private Map<ProfileEntryKey, ProfileEntryStat> entries;
    private ReadWriteLock lock;
    
    public ProfileStat() {
        this.entries = new LinkedHashMap<ProfileEntryKey, ProfileEntryStat>(4);
        this.lock = new ReentrantReadWriteLock();
    }
    
    public Map<ProfileEntryKey, ProfileEntryStat> getEntries() {
        return this.entries;
    }
    
    public void record(final Map<ProfileEntryKey, ProfileEntryReqStat> requestStatsMap) {
        if (requestStatsMap == null) {
            return;
        }
        for (final Map.Entry<ProfileEntryKey, ProfileEntryReqStat> entry : requestStatsMap.entrySet()) {
            final ProfileEntryKey entryKey = entry.getKey();
            final ProfileEntryReqStat reqEntryStat = entry.getValue();
            final ProfileEntryStat entryStat = this.getProfileEntry(entryKey);
            entryStat.addExecuteCount(reqEntryStat.getExecuteCount());
            entryStat.addExecuteTimeNanos(reqEntryStat.getExecuteTimeNanos());
        }
    }
    
    private ProfileEntryStat getProfileEntry(final ProfileEntryKey entryKey) {
        this.lock.readLock().lock();
        try {
            final ProfileEntryStat entryStat = this.entries.get(entryKey);
            if (entryStat != null) {
                return entryStat;
            }
        }
        finally {
            this.lock.readLock().unlock();
        }
        this.lock.writeLock().lock();
        try {
            ProfileEntryStat entryStat = this.entries.get(entryKey);
            if (entryStat == null) {
                this.entries.put(entryKey, new ProfileEntryStat());
                entryStat = this.entries.get(entryKey);
            }
            return entryStat;
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public List<Map<String, Object>> getStatData() {
        final List<ProfileEntryStatValue> statValueList = this.getStatValue(false);
        final int size = statValueList.size();
        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(size);
        for (final ProfileEntryStatValue profileEntryStatValue : statValueList) {
            list.add(profileEntryStatValue.getData());
        }
        return list;
    }
    
    public List<ProfileEntryStatValue> getStatValue(final boolean reset) {
        final List<ProfileEntryStatValue> list = new ArrayList<ProfileEntryStatValue>();
        this.lock.readLock().lock();
        try {
            for (final Map.Entry<ProfileEntryKey, ProfileEntryStat> entry : this.entries.entrySet()) {
                final ProfileEntryStatValue entryStatValue = entry.getValue().getValue(reset);
                entry.getKey().fillValue(entryStatValue);
                list.add(entryStatValue);
            }
        }
        finally {
            this.lock.readLock().unlock();
        }
        Collections.reverse(list);
        return list;
    }
}
