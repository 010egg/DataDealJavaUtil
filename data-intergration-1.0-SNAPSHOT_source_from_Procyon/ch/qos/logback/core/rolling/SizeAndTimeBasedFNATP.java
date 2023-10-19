// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

import java.util.Date;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import java.io.File;
import ch.qos.logback.core.rolling.helper.SizeAndTimeBasedArchiveRemover;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.FileFilterUtil;
import ch.qos.logback.core.util.DefaultInvocationGate;
import ch.qos.logback.core.util.InvocationGate;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.joran.spi.NoAutoStart;

@NoAutoStart
public class SizeAndTimeBasedFNATP<E> extends TimeBasedFileNamingAndTriggeringPolicyBase<E>
{
    int currentPeriodsCounter;
    FileSize maxFileSize;
    String maxFileSizeAsString;
    long nextSizeCheck;
    static String MISSING_INT_TOKEN;
    static String MISSING_DATE_TOKEN;
    InvocationGate invocationGate;
    
    public SizeAndTimeBasedFNATP() {
        this.currentPeriodsCounter = 0;
        this.nextSizeCheck = 0L;
        this.invocationGate = new DefaultInvocationGate();
    }
    
    @Override
    public void start() {
        super.start();
        if (!super.isErrorFree()) {
            return;
        }
        if (!this.validateDateAndIntegerTokens()) {
            this.started = false;
            return;
        }
        (this.archiveRemover = this.createArchiveRemover()).setContext(this.context);
        final String regex = this.tbrp.fileNamePattern.toRegexForFixedDate(this.dateInCurrentPeriod);
        final String stemRegex = FileFilterUtil.afterLastSlash(regex);
        this.computeCurrentPeriodsHighestCounterValue(stemRegex);
        this.started = true;
    }
    
    private boolean validateDateAndIntegerTokens() {
        boolean inError = false;
        if (this.tbrp.fileNamePattern.getIntegerTokenConverter() == null) {
            inError = true;
            this.addError(SizeAndTimeBasedFNATP.MISSING_INT_TOKEN + this.tbrp.fileNamePatternStr + "]");
            this.addError("See also http://logback.qos.ch/codes.html#sat_missing_integer_token");
        }
        if (this.tbrp.fileNamePattern.getPrimaryDateTokenConverter() == null) {
            inError = true;
            this.addError(SizeAndTimeBasedFNATP.MISSING_DATE_TOKEN + this.tbrp.fileNamePatternStr + "]");
        }
        return !inError;
    }
    
    protected ArchiveRemover createArchiveRemover() {
        return new SizeAndTimeBasedArchiveRemover(this.tbrp.fileNamePattern, this.rc);
    }
    
    void computeCurrentPeriodsHighestCounterValue(final String stemRegex) {
        final File file = new File(this.getCurrentPeriodsFileNameWithoutCompressionSuffix());
        final File parentDir = file.getParentFile();
        final File[] matchingFileArray = FileFilterUtil.filesInFolderMatchingStemRegex(parentDir, stemRegex);
        if (matchingFileArray == null || matchingFileArray.length == 0) {
            this.currentPeriodsCounter = 0;
            return;
        }
        this.currentPeriodsCounter = FileFilterUtil.findHighestCounter(matchingFileArray, stemRegex);
        if (this.tbrp.getParentsRawFileProperty() != null || this.tbrp.compressionMode != CompressionMode.NONE) {
            ++this.currentPeriodsCounter;
        }
    }
    
    @Override
    public boolean isTriggeringEvent(final File activeFile, final E event) {
        final long time = this.getCurrentTime();
        if (time >= this.nextCheck) {
            final Date dateInElapsedPeriod = this.dateInCurrentPeriod;
            this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWCS.convertMultipleArguments(dateInElapsedPeriod, this.currentPeriodsCounter);
            this.currentPeriodsCounter = 0;
            this.setDateInCurrentPeriod(time);
            this.computeNextCheck();
            return true;
        }
        if (this.invocationGate.isTooSoon(time)) {
            return false;
        }
        if (activeFile.length() >= this.maxFileSize.getSize()) {
            this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWCS.convertMultipleArguments(this.dateInCurrentPeriod, this.currentPeriodsCounter);
            ++this.currentPeriodsCounter;
            return true;
        }
        return false;
    }
    
    @Override
    public String getCurrentPeriodsFileNameWithoutCompressionSuffix() {
        return this.tbrp.fileNamePatternWCS.convertMultipleArguments(this.dateInCurrentPeriod, this.currentPeriodsCounter);
    }
    
    public String getMaxFileSize() {
        return this.maxFileSizeAsString;
    }
    
    public void setMaxFileSize(final String maxFileSize) {
        this.maxFileSizeAsString = maxFileSize;
        this.maxFileSize = FileSize.valueOf(maxFileSize);
    }
    
    static {
        SizeAndTimeBasedFNATP.MISSING_INT_TOKEN = "Missing integer token, that is %i, in FileNamePattern [";
        SizeAndTimeBasedFNATP.MISSING_DATE_TOKEN = "Missing date token, that is %d, in FileNamePattern [";
    }
}
