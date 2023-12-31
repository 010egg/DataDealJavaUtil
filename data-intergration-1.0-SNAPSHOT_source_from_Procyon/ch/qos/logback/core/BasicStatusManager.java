// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

import ch.qos.logback.core.status.OnConsoleStatusListener;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import ch.qos.logback.core.status.StatusListener;
import ch.qos.logback.core.spi.LogbackLock;
import ch.qos.logback.core.helpers.CyclicBuffer;
import ch.qos.logback.core.status.Status;
import java.util.List;
import ch.qos.logback.core.status.StatusManager;

public class BasicStatusManager implements StatusManager
{
    public static final int MAX_HEADER_COUNT = 150;
    public static final int TAIL_SIZE = 150;
    int count;
    protected final List<Status> statusList;
    protected final CyclicBuffer<Status> tailBuffer;
    protected final LogbackLock statusListLock;
    int level;
    protected final List<StatusListener> statusListenerList;
    protected final LogbackLock statusListenerListLock;
    
    public BasicStatusManager() {
        this.count = 0;
        this.statusList = new ArrayList<Status>();
        this.tailBuffer = new CyclicBuffer<Status>(150);
        this.statusListLock = new LogbackLock();
        this.level = 0;
        this.statusListenerList = new ArrayList<StatusListener>();
        this.statusListenerListLock = new LogbackLock();
    }
    
    @Override
    public void add(final Status newStatus) {
        this.fireStatusAddEvent(newStatus);
        ++this.count;
        if (newStatus.getLevel() > this.level) {
            this.level = newStatus.getLevel();
        }
        synchronized (this.statusListLock) {
            if (this.statusList.size() < 150) {
                this.statusList.add(newStatus);
            }
            else {
                this.tailBuffer.add(newStatus);
            }
        }
    }
    
    @Override
    public List<Status> getCopyOfStatusList() {
        synchronized (this.statusListLock) {
            final List<Status> tList = new ArrayList<Status>(this.statusList);
            tList.addAll(this.tailBuffer.asList());
            return tList;
        }
    }
    
    private void fireStatusAddEvent(final Status status) {
        synchronized (this.statusListenerListLock) {
            for (final StatusListener sl : this.statusListenerList) {
                sl.addStatusEvent(status);
            }
        }
    }
    
    @Override
    public void clear() {
        synchronized (this.statusListLock) {
            this.count = 0;
            this.statusList.clear();
            this.tailBuffer.clear();
        }
    }
    
    public int getLevel() {
        return this.level;
    }
    
    @Override
    public int getCount() {
        return this.count;
    }
    
    @Override
    public boolean add(final StatusListener listener) {
        synchronized (this.statusListenerListLock) {
            if (listener instanceof OnConsoleStatusListener) {
                final boolean alreadyPresent = this.checkForPresence(this.statusListenerList, listener.getClass());
                if (alreadyPresent) {
                    return false;
                }
            }
            this.statusListenerList.add(listener);
        }
        return true;
    }
    
    private boolean checkForPresence(final List<StatusListener> statusListenerList, final Class<?> aClass) {
        for (final StatusListener e : statusListenerList) {
            if (e.getClass() == aClass) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void remove(final StatusListener listener) {
        synchronized (this.statusListenerListLock) {
            this.statusListenerList.remove(listener);
        }
    }
    
    @Override
    public List<StatusListener> getCopyOfStatusListenerList() {
        synchronized (this.statusListenerListLock) {
            return new ArrayList<StatusListener>(this.statusListenerList);
        }
    }
}
