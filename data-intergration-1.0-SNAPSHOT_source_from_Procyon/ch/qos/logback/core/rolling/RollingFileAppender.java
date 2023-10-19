// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import java.io.File;
import ch.qos.logback.core.FileAppender;

public class RollingFileAppender<E> extends FileAppender<E>
{
    File currentlyActiveFile;
    TriggeringPolicy<E> triggeringPolicy;
    RollingPolicy rollingPolicy;
    private static String RFA_NO_TP_URL;
    private static String RFA_NO_RP_URL;
    private static String COLLISION_URL;
    private static String RFA_LATE_FILE_URL;
    
    @Override
    public void start() {
        if (this.triggeringPolicy == null) {
            this.addWarn("No TriggeringPolicy was set for the RollingFileAppender named " + this.getName());
            this.addWarn("For more information, please visit " + RollingFileAppender.RFA_NO_TP_URL);
            return;
        }
        if (!this.triggeringPolicy.isStarted()) {
            this.addWarn("TriggeringPolicy has not started. RollingFileAppender will not start");
            return;
        }
        if (this.checkForCollisionsInPreviousRollingFileAppenders()) {
            this.addError("Collisions detected with FileAppender/RollingAppender instances defined earlier. Aborting.");
            this.addError("For more information, please visit " + RollingFileAppender.COLLISION_WITH_EARLIER_APPENDER_URL);
            return;
        }
        if (!this.append) {
            this.addWarn("Append mode is mandatory for RollingFileAppender. Defaulting to append=true.");
            this.append = true;
        }
        if (this.rollingPolicy == null) {
            this.addError("No RollingPolicy was set for the RollingFileAppender named " + this.getName());
            this.addError("For more information, please visit " + RollingFileAppender.RFA_NO_RP_URL);
            return;
        }
        if (this.checkForFileAndPatternCollisions()) {
            this.addError("File property collides with fileNamePattern. Aborting.");
            this.addError("For more information, please visit " + RollingFileAppender.COLLISION_URL);
            return;
        }
        if (this.isPrudent()) {
            if (this.rawFileProperty() != null) {
                this.addWarn("Setting \"File\" property to null on account of prudent mode");
                this.setFile(null);
            }
            if (this.rollingPolicy.getCompressionMode() != CompressionMode.NONE) {
                this.addError("Compression is not supported in prudent mode. Aborting");
                return;
            }
        }
        this.currentlyActiveFile = new File(this.getFile());
        this.addInfo("Active log file name: " + this.getFile());
        super.start();
    }
    
    private boolean checkForFileAndPatternCollisions() {
        if (this.triggeringPolicy instanceof RollingPolicyBase) {
            final RollingPolicyBase base = (RollingPolicyBase)this.triggeringPolicy;
            final FileNamePattern fileNamePattern = base.fileNamePattern;
            if (fileNamePattern != null && this.fileName != null) {
                final String regex = fileNamePattern.toRegex();
                return this.fileName.matches(regex);
            }
        }
        return false;
    }
    
    private boolean checkForCollisionsInPreviousRollingFileAppenders() {
        boolean collisionResult = false;
        if (this.triggeringPolicy instanceof RollingPolicyBase) {
            final RollingPolicyBase base = (RollingPolicyBase)this.triggeringPolicy;
            final FileNamePattern fileNamePattern = base.fileNamePattern;
            final boolean collisionsDetected = this.innerCheckForFileNamePatternCollisionInPreviousRFA(fileNamePattern.toString());
            if (collisionsDetected) {
                collisionResult = true;
            }
        }
        return collisionResult;
    }
    
    private boolean innerCheckForFileNamePatternCollisionInPreviousRFA(final String fileNamePattern) {
        boolean collisionsDetected = false;
        final Map<String, String> map = (Map<String, String>)this.context.getObject("RFA_FILENAME_COLLISION_MAP");
        if (map == null) {
            return collisionsDetected;
        }
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            if (fileNamePattern.equals(entry.getValue())) {
                this.addErrorForCollision("FileNamePattern", entry.getValue(), entry.getKey());
                collisionsDetected = true;
            }
        }
        if (this.name != null) {
            map.put(this.getName(), fileNamePattern);
        }
        return collisionsDetected;
    }
    
    @Override
    public void stop() {
        if (this.rollingPolicy != null) {
            this.rollingPolicy.stop();
        }
        if (this.triggeringPolicy != null) {
            this.triggeringPolicy.stop();
        }
        super.stop();
    }
    
    @Override
    public void setFile(final String file) {
        if (file != null && (this.triggeringPolicy != null || this.rollingPolicy != null)) {
            this.addError("File property must be set before any triggeringPolicy or rollingPolicy properties");
            this.addError("For more information, please visit " + RollingFileAppender.RFA_LATE_FILE_URL);
        }
        super.setFile(file);
    }
    
    @Override
    public String getFile() {
        return this.rollingPolicy.getActiveFileName();
    }
    
    public void rollover() {
        this.lock.lock();
        try {
            this.closeOutputStream();
            this.attemptRollover();
            this.attemptOpenFile();
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private void attemptOpenFile() {
        try {
            this.currentlyActiveFile = new File(this.rollingPolicy.getActiveFileName());
            this.openFile(this.rollingPolicy.getActiveFileName());
        }
        catch (IOException e) {
            this.addError("setFile(" + this.fileName + ", false) call failed.", e);
        }
    }
    
    private void attemptRollover() {
        try {
            this.rollingPolicy.rollover();
        }
        catch (RolloverFailure rf) {
            this.addWarn("RolloverFailure occurred. Deferring roll-over.");
            this.append = true;
        }
    }
    
    @Override
    protected void subAppend(final E event) {
        synchronized (this.triggeringPolicy) {
            if (this.triggeringPolicy.isTriggeringEvent(this.currentlyActiveFile, event)) {
                this.rollover();
            }
        }
        super.subAppend(event);
    }
    
    public RollingPolicy getRollingPolicy() {
        return this.rollingPolicy;
    }
    
    public TriggeringPolicy<E> getTriggeringPolicy() {
        return this.triggeringPolicy;
    }
    
    public void setRollingPolicy(final RollingPolicy policy) {
        this.rollingPolicy = policy;
        if (this.rollingPolicy instanceof TriggeringPolicy) {
            this.triggeringPolicy = (TriggeringPolicy<E>)policy;
        }
    }
    
    public void setTriggeringPolicy(final TriggeringPolicy<E> policy) {
        this.triggeringPolicy = policy;
        if (policy instanceof RollingPolicy) {
            this.rollingPolicy = (RollingPolicy)policy;
        }
    }
    
    static {
        RollingFileAppender.RFA_NO_TP_URL = "http://logback.qos.ch/codes.html#rfa_no_tp";
        RollingFileAppender.RFA_NO_RP_URL = "http://logback.qos.ch/codes.html#rfa_no_rp";
        RollingFileAppender.COLLISION_URL = "http://logback.qos.ch/codes.html#rfa_collision";
        RollingFileAppender.RFA_LATE_FILE_URL = "http://logback.qos.ch/codes.html#rfa_file_after";
    }
}
