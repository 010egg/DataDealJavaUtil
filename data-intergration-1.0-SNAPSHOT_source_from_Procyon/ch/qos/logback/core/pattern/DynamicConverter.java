// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.pattern;

import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.Context;
import java.util.List;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;

public abstract class DynamicConverter<E> extends FormattingConverter<E> implements LifeCycle, ContextAware
{
    ContextAwareBase cab;
    private List<String> optionList;
    protected boolean started;
    
    public DynamicConverter() {
        this.cab = new ContextAwareBase(this);
        this.started = false;
    }
    
    @Override
    public void start() {
        this.started = true;
    }
    
    @Override
    public void stop() {
        this.started = false;
    }
    
    @Override
    public boolean isStarted() {
        return this.started;
    }
    
    public void setOptionList(final List<String> optionList) {
        this.optionList = optionList;
    }
    
    public String getFirstOption() {
        if (this.optionList == null || this.optionList.size() == 0) {
            return null;
        }
        return this.optionList.get(0);
    }
    
    protected List<String> getOptionList() {
        return this.optionList;
    }
    
    @Override
    public void setContext(final Context context) {
        this.cab.setContext(context);
    }
    
    @Override
    public Context getContext() {
        return this.cab.getContext();
    }
    
    @Override
    public void addStatus(final Status status) {
        this.cab.addStatus(status);
    }
    
    @Override
    public void addInfo(final String msg) {
        this.cab.addInfo(msg);
    }
    
    @Override
    public void addInfo(final String msg, final Throwable ex) {
        this.cab.addInfo(msg, ex);
    }
    
    @Override
    public void addWarn(final String msg) {
        this.cab.addWarn(msg);
    }
    
    @Override
    public void addWarn(final String msg, final Throwable ex) {
        this.cab.addWarn(msg, ex);
    }
    
    @Override
    public void addError(final String msg) {
        this.cab.addError(msg);
    }
    
    @Override
    public void addError(final String msg, final Throwable ex) {
        this.cab.addError(msg, ex);
    }
}
