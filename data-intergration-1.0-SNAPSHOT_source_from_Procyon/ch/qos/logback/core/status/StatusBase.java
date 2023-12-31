// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.status;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public abstract class StatusBase implements Status
{
    private static final List<Status> EMPTY_LIST;
    int level;
    final String message;
    final Object origin;
    List<Status> childrenList;
    Throwable throwable;
    long date;
    
    StatusBase(final int level, final String msg, final Object origin) {
        this(level, msg, origin, null);
    }
    
    StatusBase(final int level, final String msg, final Object origin, final Throwable t) {
        this.level = level;
        this.message = msg;
        this.origin = origin;
        this.throwable = t;
        this.date = System.currentTimeMillis();
    }
    
    @Override
    public synchronized void add(final Status child) {
        if (child == null) {
            throw new NullPointerException("Null values are not valid Status.");
        }
        if (this.childrenList == null) {
            this.childrenList = new ArrayList<Status>();
        }
        this.childrenList.add(child);
    }
    
    @Override
    public synchronized boolean hasChildren() {
        return this.childrenList != null && this.childrenList.size() > 0;
    }
    
    @Override
    public synchronized Iterator<Status> iterator() {
        if (this.childrenList != null) {
            return this.childrenList.iterator();
        }
        return StatusBase.EMPTY_LIST.iterator();
    }
    
    @Override
    public synchronized boolean remove(final Status statusToRemove) {
        return this.childrenList != null && this.childrenList.remove(statusToRemove);
    }
    
    @Override
    public int getLevel() {
        return this.level;
    }
    
    @Override
    public synchronized int getEffectiveLevel() {
        int result = this.level;
        for (final Status s : this) {
            final int effLevel = s.getEffectiveLevel();
            if (effLevel > result) {
                result = effLevel;
            }
        }
        return result;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public Object getOrigin() {
        return this.origin;
    }
    
    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    @Override
    public Long getDate() {
        return this.date;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        switch (this.getEffectiveLevel()) {
            case 0: {
                buf.append("INFO");
                break;
            }
            case 1: {
                buf.append("WARN");
                break;
            }
            case 2: {
                buf.append("ERROR");
                break;
            }
        }
        if (this.origin != null) {
            buf.append(" in ");
            buf.append(this.origin);
            buf.append(" -");
        }
        buf.append(" ");
        buf.append(this.message);
        if (this.throwable != null) {
            buf.append(" ");
            buf.append(this.throwable);
        }
        return buf.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.level;
        result = 31 * result + ((this.message == null) ? 0 : this.message.hashCode());
        return result;
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
        final StatusBase other = (StatusBase)obj;
        if (this.level != other.level) {
            return false;
        }
        if (this.message == null) {
            if (other.message != null) {
                return false;
            }
        }
        else if (!this.message.equals(other.message)) {
            return false;
        }
        return true;
    }
    
    static {
        EMPTY_LIST = new ArrayList<Status>(0);
    }
}
