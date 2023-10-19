// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.status;

import java.util.Iterator;
import java.util.List;
import ch.qos.logback.core.util.StatusPrinter;
import java.io.PrintStream;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.ContextAwareBase;

abstract class OnPrintStreamStatusListenerBase extends ContextAwareBase implements StatusListener, LifeCycle
{
    boolean isStarted;
    static final long DEFAULT_RETROSPECTIVE = 300L;
    long retrospectiveThresold;
    
    OnPrintStreamStatusListenerBase() {
        this.isStarted = false;
        this.retrospectiveThresold = 300L;
    }
    
    protected abstract PrintStream getPrintStream();
    
    private void print(final Status status) {
        final StringBuilder sb = new StringBuilder();
        StatusPrinter.buildStr(sb, "", status);
        this.getPrintStream().print(sb);
    }
    
    @Override
    public void addStatusEvent(final Status status) {
        if (!this.isStarted) {
            return;
        }
        this.print(status);
    }
    
    private void retrospectivePrint() {
        if (this.context == null) {
            return;
        }
        final long now = System.currentTimeMillis();
        final StatusManager sm = this.context.getStatusManager();
        final List<Status> statusList = sm.getCopyOfStatusList();
        for (final Status status : statusList) {
            final long timestampOfStatusMesage = status.getDate();
            if (this.isElapsedTimeLongerThanThreshold(now, timestampOfStatusMesage)) {
                this.print(status);
            }
        }
    }
    
    private boolean isElapsedTimeLongerThanThreshold(final long now, final long timestamp) {
        final long elapsedTime = now - timestamp;
        return elapsedTime < this.retrospectiveThresold;
    }
    
    @Override
    public void start() {
        this.isStarted = true;
        if (this.retrospectiveThresold > 0L) {
            this.retrospectivePrint();
        }
    }
    
    public void setRetrospective(final long retrospective) {
        this.retrospectiveThresold = retrospective;
    }
    
    public long getRetrospective() {
        return this.retrospectiveThresold;
    }
    
    @Override
    public void stop() {
        this.isStarted = false;
    }
    
    @Override
    public boolean isStarted() {
        return this.isStarted;
    }
}
