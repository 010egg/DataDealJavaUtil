// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import ch.qos.logback.core.Context;

public class StatusUtil
{
    StatusManager sm;
    
    public StatusUtil(final StatusManager sm) {
        this.sm = sm;
    }
    
    public StatusUtil(final Context context) {
        this.sm = context.getStatusManager();
    }
    
    public static boolean contextHasStatusListener(final Context context) {
        final StatusManager sm = context.getStatusManager();
        if (sm == null) {
            return false;
        }
        final List<StatusListener> listeners = sm.getCopyOfStatusListenerList();
        return listeners != null && listeners.size() != 0;
    }
    
    public static List<Status> filterStatusListByTimeThreshold(final List<Status> rawList, final long threshold) {
        final List<Status> filteredList = new ArrayList<Status>();
        for (final Status s : rawList) {
            if (s.getDate() >= threshold) {
                filteredList.add(s);
            }
        }
        return filteredList;
    }
    
    public void addStatus(final Status status) {
        if (this.sm != null) {
            this.sm.add(status);
        }
    }
    
    public void addInfo(final Object caller, final String msg) {
        this.addStatus(new InfoStatus(msg, caller));
    }
    
    public void addWarn(final Object caller, final String msg) {
        this.addStatus(new WarnStatus(msg, caller));
    }
    
    public void addError(final Object caller, final String msg, final Throwable t) {
        this.addStatus(new ErrorStatus(msg, caller, t));
    }
    
    public boolean hasXMLParsingErrors(final long threshold) {
        return this.containsMatch(threshold, 2, "XML_PARSING");
    }
    
    public boolean noXMLParsingErrorsOccurred(final long threshold) {
        return !this.hasXMLParsingErrors(threshold);
    }
    
    public int getHighestLevel(final long threshold) {
        final List<Status> filteredList = filterStatusListByTimeThreshold(this.sm.getCopyOfStatusList(), threshold);
        int maxLevel = 0;
        for (final Status s : filteredList) {
            if (s.getLevel() > maxLevel) {
                maxLevel = s.getLevel();
            }
        }
        return maxLevel;
    }
    
    public boolean isErrorFree(final long threshold) {
        return 2 > this.getHighestLevel(threshold);
    }
    
    public boolean isWarningOrErrorFree(final long threshold) {
        return 1 > this.getHighestLevel(threshold);
    }
    
    public boolean containsMatch(final long threshold, final int level, final String regex) {
        final List<Status> filteredList = filterStatusListByTimeThreshold(this.sm.getCopyOfStatusList(), threshold);
        final Pattern p = Pattern.compile(regex);
        for (final Status status : filteredList) {
            if (level != status.getLevel()) {
                continue;
            }
            final String msg = status.getMessage();
            final Matcher matcher = p.matcher(msg);
            if (matcher.lookingAt()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsMatch(final int level, final String regex) {
        return this.containsMatch(0L, level, regex);
    }
    
    public boolean containsMatch(final String regex) {
        final Pattern p = Pattern.compile(regex);
        for (final Status status : this.sm.getCopyOfStatusList()) {
            final String msg = status.getMessage();
            final Matcher matcher = p.matcher(msg);
            if (matcher.lookingAt()) {
                return true;
            }
        }
        return false;
    }
    
    public int matchCount(final String regex) {
        int count = 0;
        final Pattern p = Pattern.compile(regex);
        for (final Status status : this.sm.getCopyOfStatusList()) {
            final String msg = status.getMessage();
            final Matcher matcher = p.matcher(msg);
            if (matcher.lookingAt()) {
                ++count;
            }
        }
        return count;
    }
    
    public boolean containsException(final Class<?> exceptionType) {
        for (final Status status : this.sm.getCopyOfStatusList()) {
            for (Throwable t = status.getThrowable(); t != null; t = t.getCause()) {
                if (t.getClass().getName().equals(exceptionType.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public long timeOfLastReset() {
        final List<Status> statusList = this.sm.getCopyOfStatusList();
        if (statusList == null) {
            return -1L;
        }
        final int len = statusList.size();
        for (int i = len - 1; i >= 0; --i) {
            final Status s = statusList.get(i);
            if ("Will reset and reconfigure context ".equals(s.getMessage())) {
                return s.getDate();
            }
        }
        return -1L;
    }
}
