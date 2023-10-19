// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import java.io.File;
import java.util.Locale;
import java.util.Date;
import ch.qos.logback.core.rolling.helper.RollingCalendar;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class TimeBasedFileNamingAndTriggeringPolicyBase<E> extends ContextAwareBase implements TimeBasedFileNamingAndTriggeringPolicy<E>
{
    private static String COLLIDING_DATE_FORMAT_URL;
    protected TimeBasedRollingPolicy<E> tbrp;
    protected ArchiveRemover archiveRemover;
    protected String elapsedPeriodsFileName;
    protected RollingCalendar rc;
    protected long artificialCurrentTime;
    protected Date dateInCurrentPeriod;
    protected long nextCheck;
    protected boolean started;
    protected boolean errorFree;
    
    public TimeBasedFileNamingAndTriggeringPolicyBase() {
        this.archiveRemover = null;
        this.artificialCurrentTime = -1L;
        this.dateInCurrentPeriod = null;
        this.started = false;
        this.errorFree = true;
    }
    
    @Override
    public boolean isStarted() {
        return this.started;
    }
    
    @Override
    public void start() {
        final DateTokenConverter<Object> dtc = this.tbrp.fileNamePattern.getPrimaryDateTokenConverter();
        if (dtc == null) {
            throw new IllegalStateException("FileNamePattern [" + this.tbrp.fileNamePattern.getPattern() + "] does not contain a valid DateToken");
        }
        if (dtc.getTimeZone() != null) {
            this.rc = new RollingCalendar(dtc.getDatePattern(), dtc.getTimeZone(), Locale.getDefault());
        }
        else {
            this.rc = new RollingCalendar(dtc.getDatePattern());
        }
        this.addInfo("The date pattern is '" + dtc.getDatePattern() + "' from file name pattern '" + this.tbrp.fileNamePattern.getPattern() + "'.");
        this.rc.printPeriodicity(this);
        if (!this.rc.isCollisionFree()) {
            this.addError("The date format in FileNamePattern will result in collisions in the names of archived log files.");
            this.addError("For more information, please visit " + TimeBasedFileNamingAndTriggeringPolicyBase.COLLIDING_DATE_FORMAT_URL);
            this.errorFree = false;
            return;
        }
        this.setDateInCurrentPeriod(new Date(this.getCurrentTime()));
        if (this.tbrp.getParentsRawFileProperty() != null) {
            final File currentFile = new File(this.tbrp.getParentsRawFileProperty());
            if (currentFile.exists() && currentFile.canRead()) {
                this.setDateInCurrentPeriod(new Date(currentFile.lastModified()));
            }
        }
        this.addInfo("Setting initial period to " + this.dateInCurrentPeriod);
        this.computeNextCheck();
    }
    
    @Override
    public void stop() {
        this.started = false;
    }
    
    protected void computeNextCheck() {
        this.nextCheck = this.rc.getNextTriggeringDate(this.dateInCurrentPeriod).getTime();
    }
    
    protected void setDateInCurrentPeriod(final long now) {
        this.dateInCurrentPeriod.setTime(now);
    }
    
    public void setDateInCurrentPeriod(final Date _dateInCurrentPeriod) {
        this.dateInCurrentPeriod = _dateInCurrentPeriod;
    }
    
    @Override
    public String getElapsedPeriodsFileName() {
        return this.elapsedPeriodsFileName;
    }
    
    @Override
    public String getCurrentPeriodsFileNameWithoutCompressionSuffix() {
        return this.tbrp.fileNamePatternWCS.convert(this.dateInCurrentPeriod);
    }
    
    @Override
    public void setCurrentTime(final long timeInMillis) {
        this.artificialCurrentTime = timeInMillis;
    }
    
    @Override
    public long getCurrentTime() {
        if (this.artificialCurrentTime >= 0L) {
            return this.artificialCurrentTime;
        }
        return System.currentTimeMillis();
    }
    
    @Override
    public void setTimeBasedRollingPolicy(final TimeBasedRollingPolicy<E> _tbrp) {
        this.tbrp = _tbrp;
    }
    
    @Override
    public ArchiveRemover getArchiveRemover() {
        return this.archiveRemover;
    }
    
    protected boolean isErrorFree() {
        return this.errorFree;
    }
    
    static {
        TimeBasedFileNamingAndTriggeringPolicyBase.COLLIDING_DATE_FORMAT_URL = "http://logback.qos.ch/codes.html#rfa_collision_in_dateFormat";
    }
}
