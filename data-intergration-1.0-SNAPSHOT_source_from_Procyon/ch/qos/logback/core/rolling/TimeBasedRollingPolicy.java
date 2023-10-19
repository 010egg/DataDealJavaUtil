// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

import java.io.File;
import ch.qos.logback.core.rolling.helper.FileFilterUtil;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.util.FileSize;
import java.util.concurrent.Future;
import ch.qos.logback.core.rolling.helper.RenameUtil;
import ch.qos.logback.core.rolling.helper.Compressor;
import ch.qos.logback.core.rolling.helper.FileNamePattern;

public class TimeBasedRollingPolicy<E> extends RollingPolicyBase implements TriggeringPolicy<E>
{
    static final String FNP_NOT_SET = "The FileNamePattern option must be set before using TimeBasedRollingPolicy. ";
    FileNamePattern fileNamePatternWCS;
    private Compressor compressor;
    private RenameUtil renameUtil;
    Future<?> compressionFuture;
    Future<?> cleanUpFuture;
    private int maxHistory;
    private FileSize totalSizeCap;
    private ArchiveRemover archiveRemover;
    TimeBasedFileNamingAndTriggeringPolicy<E> timeBasedFileNamingAndTriggeringPolicy;
    boolean cleanHistoryOnStart;
    
    public TimeBasedRollingPolicy() {
        this.renameUtil = new RenameUtil();
        this.maxHistory = 0;
        this.totalSizeCap = new FileSize(0L);
        this.cleanHistoryOnStart = false;
    }
    
    @Override
    public void start() {
        this.renameUtil.setContext(this.context);
        if (this.fileNamePatternStr == null) {
            this.addWarn("The FileNamePattern option must be set before using TimeBasedRollingPolicy. ");
            this.addWarn("See also http://logback.qos.ch/codes.html#tbr_fnp_not_set");
            throw new IllegalStateException("The FileNamePattern option must be set before using TimeBasedRollingPolicy. See also http://logback.qos.ch/codes.html#tbr_fnp_not_set");
        }
        this.fileNamePattern = new FileNamePattern(this.fileNamePatternStr, this.context);
        this.determineCompressionMode();
        (this.compressor = new Compressor(this.compressionMode)).setContext(this.context);
        this.fileNamePatternWCS = new FileNamePattern(Compressor.computeFileNameStr_WCS(this.fileNamePatternStr, this.compressionMode), this.context);
        this.addInfo("Will use the pattern " + this.fileNamePatternWCS + " for the active file");
        if (this.compressionMode == CompressionMode.ZIP) {
            final String zipEntryFileNamePatternStr = this.transformFileNamePattern2ZipEntry(this.fileNamePatternStr);
            this.zipEntryFileNamePattern = new FileNamePattern(zipEntryFileNamePatternStr, this.context);
        }
        if (this.timeBasedFileNamingAndTriggeringPolicy == null) {
            this.timeBasedFileNamingAndTriggeringPolicy = new DefaultTimeBasedFileNamingAndTriggeringPolicy<E>();
        }
        this.timeBasedFileNamingAndTriggeringPolicy.setContext(this.context);
        this.timeBasedFileNamingAndTriggeringPolicy.setTimeBasedRollingPolicy(this);
        this.timeBasedFileNamingAndTriggeringPolicy.start();
        if (!this.timeBasedFileNamingAndTriggeringPolicy.isStarted()) {
            this.addWarn("Subcomponent did not start. TimeBasedRollingPolicy will not start.");
            return;
        }
        if (this.maxHistory != 0) {
            (this.archiveRemover = this.timeBasedFileNamingAndTriggeringPolicy.getArchiveRemover()).setMaxHistory(this.maxHistory);
            this.archiveRemover.setTotalSizeCap(this.totalSizeCap.getSize());
            if (this.cleanHistoryOnStart) {
                this.addInfo("Cleaning on start up");
                final Date now = new Date(this.timeBasedFileNamingAndTriggeringPolicy.getCurrentTime());
                this.cleanUpFuture = this.archiveRemover.cleanAsynchronously(now);
            }
        }
        else if (this.totalSizeCap.getSize() != 0L) {
            this.addWarn("'maxHistory' is not set, ignoring 'totalSizeCap' option with value [" + this.totalSizeCap + "]");
        }
        super.start();
    }
    
    @Override
    public void stop() {
        if (!this.isStarted()) {
            return;
        }
        this.waitForAsynchronousJobToStop(this.compressionFuture, "compression");
        this.waitForAsynchronousJobToStop(this.cleanUpFuture, "clean-up");
        super.stop();
    }
    
    private void waitForAsynchronousJobToStop(final Future<?> aFuture, final String jobDescription) {
        if (aFuture != null) {
            try {
                aFuture.get(30L, TimeUnit.SECONDS);
            }
            catch (TimeoutException e) {
                this.addError("Timeout while waiting for " + jobDescription + " job to finish", e);
            }
            catch (Exception e2) {
                this.addError("Unexpected exception while waiting for " + jobDescription + " job to finish", e2);
            }
        }
    }
    
    private String transformFileNamePattern2ZipEntry(final String fileNamePatternStr) {
        final String slashified = FileFilterUtil.slashify(fileNamePatternStr);
        return FileFilterUtil.afterLastSlash(slashified);
    }
    
    public void setTimeBasedFileNamingAndTriggeringPolicy(final TimeBasedFileNamingAndTriggeringPolicy<E> timeBasedTriggering) {
        this.timeBasedFileNamingAndTriggeringPolicy = timeBasedTriggering;
    }
    
    public TimeBasedFileNamingAndTriggeringPolicy<E> getTimeBasedFileNamingAndTriggeringPolicy() {
        return this.timeBasedFileNamingAndTriggeringPolicy;
    }
    
    @Override
    public void rollover() throws RolloverFailure {
        final String elapsedPeriodsFileName = this.timeBasedFileNamingAndTriggeringPolicy.getElapsedPeriodsFileName();
        final String elapsedPeriodStem = FileFilterUtil.afterLastSlash(elapsedPeriodsFileName);
        if (this.compressionMode == CompressionMode.NONE) {
            if (this.getParentsRawFileProperty() != null) {
                this.renameUtil.rename(this.getParentsRawFileProperty(), elapsedPeriodsFileName);
            }
        }
        else if (this.getParentsRawFileProperty() == null) {
            this.compressionFuture = this.compressor.asyncCompress(elapsedPeriodsFileName, elapsedPeriodsFileName, elapsedPeriodStem);
        }
        else {
            this.compressionFuture = this.renamedRawAndAsyncCompress(elapsedPeriodsFileName, elapsedPeriodStem);
        }
        if (this.archiveRemover != null) {
            final Date now = new Date(this.timeBasedFileNamingAndTriggeringPolicy.getCurrentTime());
            this.cleanUpFuture = this.archiveRemover.cleanAsynchronously(now);
        }
    }
    
    Future<?> renamedRawAndAsyncCompress(final String nameOfCompressedFile, final String innerEntryName) throws RolloverFailure {
        final String parentsRawFile = this.getParentsRawFileProperty();
        final String tmpTarget = parentsRawFile + System.nanoTime() + ".tmp";
        this.renameUtil.rename(parentsRawFile, tmpTarget);
        return this.compressor.asyncCompress(tmpTarget, nameOfCompressedFile, innerEntryName);
    }
    
    @Override
    public String getActiveFileName() {
        final String parentsRawFileProperty = this.getParentsRawFileProperty();
        if (parentsRawFileProperty != null) {
            return parentsRawFileProperty;
        }
        return this.timeBasedFileNamingAndTriggeringPolicy.getCurrentPeriodsFileNameWithoutCompressionSuffix();
    }
    
    @Override
    public boolean isTriggeringEvent(final File activeFile, final E event) {
        return this.timeBasedFileNamingAndTriggeringPolicy.isTriggeringEvent(activeFile, event);
    }
    
    public int getMaxHistory() {
        return this.maxHistory;
    }
    
    public void setMaxHistory(final int maxHistory) {
        this.maxHistory = maxHistory;
    }
    
    public boolean isCleanHistoryOnStart() {
        return this.cleanHistoryOnStart;
    }
    
    public void setCleanHistoryOnStart(final boolean cleanHistoryOnStart) {
        this.cleanHistoryOnStart = cleanHistoryOnStart;
    }
    
    @Override
    public String toString() {
        return "c.q.l.core.rolling.TimeBasedRollingPolicy@" + this.hashCode();
    }
    
    public void setTotalSizeCap(final FileSize totalSizeCap) {
        this.totalSizeCap = totalSizeCap;
    }
}
