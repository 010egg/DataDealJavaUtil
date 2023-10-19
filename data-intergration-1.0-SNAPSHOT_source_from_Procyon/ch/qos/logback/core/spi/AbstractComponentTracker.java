// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.spi;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;

public abstract class AbstractComponentTracker<C> implements ComponentTracker<C>
{
    private static final boolean ACCESS_ORDERED = true;
    public static final long LINGERING_TIMEOUT = 10000L;
    public static final long WAIT_BETWEEN_SUCCESSIVE_REMOVAL_ITERATIONS = 1000L;
    protected int maxComponents;
    protected long timeout;
    LinkedHashMap<String, Entry<C>> liveMap;
    LinkedHashMap<String, Entry<C>> lingerersMap;
    long lastCheck;
    private RemovalPredicator<C> byExcedent;
    private RemovalPredicator<C> byTimeout;
    private RemovalPredicator<C> byLingering;
    
    public AbstractComponentTracker() {
        this.maxComponents = Integer.MAX_VALUE;
        this.timeout = 1800000L;
        this.liveMap = new LinkedHashMap<String, Entry<C>>(32, 0.75f, true);
        this.lingerersMap = new LinkedHashMap<String, Entry<C>>(16, 0.75f, true);
        this.lastCheck = 0L;
        this.byExcedent = new RemovalPredicator<C>() {
            @Override
            public boolean isSlatedForRemoval(final Entry<C> entry, final long timestamp) {
                return AbstractComponentTracker.this.liveMap.size() > AbstractComponentTracker.this.maxComponents;
            }
        };
        this.byTimeout = new RemovalPredicator<C>() {
            @Override
            public boolean isSlatedForRemoval(final Entry<C> entry, final long timestamp) {
                return AbstractComponentTracker.this.isEntryStale(entry, timestamp);
            }
        };
        this.byLingering = new RemovalPredicator<C>() {
            @Override
            public boolean isSlatedForRemoval(final Entry<C> entry, final long timestamp) {
                return AbstractComponentTracker.this.isEntryDoneLingering(entry, timestamp);
            }
        };
    }
    
    protected abstract void processPriorToRemoval(final C p0);
    
    protected abstract C buildComponent(final String p0);
    
    protected abstract boolean isComponentStale(final C p0);
    
    @Override
    public int getComponentCount() {
        return this.liveMap.size() + this.lingerersMap.size();
    }
    
    private Entry<C> getFromEitherMap(final String key) {
        final Entry<C> entry = this.liveMap.get(key);
        if (entry != null) {
            return entry;
        }
        return this.lingerersMap.get(key);
    }
    
    @Override
    public synchronized C find(final String key) {
        final Entry<C> entry = this.getFromEitherMap(key);
        if (entry == null) {
            return null;
        }
        return entry.component;
    }
    
    @Override
    public synchronized C getOrCreate(final String key, final long timestamp) {
        Entry<C> entry = this.getFromEitherMap(key);
        if (entry == null) {
            final C c = this.buildComponent(key);
            entry = new Entry<C>(key, c, timestamp);
            this.liveMap.put(key, entry);
        }
        else {
            entry.setTimestamp(timestamp);
        }
        return entry.component;
    }
    
    @Override
    public void endOfLife(final String key) {
        final Entry<C> entry = this.liveMap.remove(key);
        if (entry == null) {
            return;
        }
        this.lingerersMap.put(key, entry);
    }
    
    @Override
    public synchronized void removeStaleComponents(final long now) {
        if (this.isTooSoonForRemovalIteration(now)) {
            return;
        }
        this.removeExcedentComponents();
        this.removeStaleComponentsFromMainMap(now);
        this.removeStaleComponentsFromLingerersMap(now);
    }
    
    private void removeExcedentComponents() {
        this.genericStaleComponentRemover(this.liveMap, 0L, this.byExcedent);
    }
    
    private void removeStaleComponentsFromMainMap(final long now) {
        this.genericStaleComponentRemover(this.liveMap, now, this.byTimeout);
    }
    
    private void removeStaleComponentsFromLingerersMap(final long now) {
        this.genericStaleComponentRemover(this.lingerersMap, now, this.byLingering);
    }
    
    private void genericStaleComponentRemover(final LinkedHashMap<String, Entry<C>> map, final long now, final RemovalPredicator<C> removalPredicator) {
        final Iterator<Map.Entry<String, Entry<C>>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<String, Entry<C>> mapEntry = iter.next();
            final Entry<C> entry = mapEntry.getValue();
            if (!removalPredicator.isSlatedForRemoval(entry, now)) {
                break;
            }
            iter.remove();
            final C c = entry.component;
            this.processPriorToRemoval(c);
        }
    }
    
    private boolean isTooSoonForRemovalIteration(final long now) {
        if (this.lastCheck + 1000L > now) {
            return true;
        }
        this.lastCheck = now;
        return false;
    }
    
    private boolean isEntryStale(final Entry<C> entry, final long now) {
        final C c = entry.component;
        return this.isComponentStale(c) || entry.timestamp + this.timeout < now;
    }
    
    private boolean isEntryDoneLingering(final Entry<C> entry, final long now) {
        return entry.timestamp + 10000L < now;
    }
    
    @Override
    public Set<String> allKeys() {
        final HashSet<String> allKeys = new HashSet<String>(this.liveMap.keySet());
        allKeys.addAll((Collection<?>)this.lingerersMap.keySet());
        return allKeys;
    }
    
    @Override
    public Collection<C> allComponents() {
        final List<C> allComponents = new ArrayList<C>();
        for (final Entry<C> e : this.liveMap.values()) {
            allComponents.add(e.component);
        }
        for (final Entry<C> e : this.lingerersMap.values()) {
            allComponents.add(e.component);
        }
        return allComponents;
    }
    
    public long getTimeout() {
        return this.timeout;
    }
    
    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }
    
    public int getMaxComponents() {
        return this.maxComponents;
    }
    
    public void setMaxComponents(final int maxComponents) {
        this.maxComponents = maxComponents;
    }
    
    private static class Entry<C>
    {
        String key;
        C component;
        long timestamp;
        
        Entry(final String k, final C c, final long timestamp) {
            this.key = k;
            this.component = c;
            this.timestamp = timestamp;
        }
        
        public void setTimestamp(final long timestamp) {
            this.timestamp = timestamp;
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final Entry<C> other = (Entry<C>)obj;
            if (this.key == null) {
                if (other.key != null) {
                    return false;
                }
            }
            else if (!this.key.equals(other.key)) {
                return false;
            }
            if (this.component == null) {
                if (other.component != null) {
                    return false;
                }
            }
            else if (!this.component.equals(other.component)) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            return "(" + this.key + ", " + this.component + ")";
        }
    }
    
    private interface RemovalPredicator<C>
    {
        boolean isSlatedForRemoval(final Entry<C> p0, final long p1);
    }
}
