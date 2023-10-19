// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling.helper;

import java.io.File;
import java.util.Date;

public class SizeAndTimeBasedArchiveRemover extends TimeBasedArchiveRemover
{
    public SizeAndTimeBasedArchiveRemover(final FileNamePattern fileNamePattern, final RollingCalendar rc) {
        super(fileNamePattern, rc);
    }
    
    @Override
    protected File[] getFilesInPeriod(final Date dateOfPeriodToClean) {
        final File archive0 = new File(this.fileNamePattern.convertMultipleArguments(dateOfPeriodToClean, 0));
        final File parentDir = this.getParentDir(archive0);
        final String stemRegex = this.createStemRegex(dateOfPeriodToClean);
        final File[] matchingFileArray = FileFilterUtil.filesInFolderMatchingStemRegex(parentDir, stemRegex);
        return matchingFileArray;
    }
    
    private String createStemRegex(final Date dateOfPeriodToClean) {
        final String regex = this.fileNamePattern.toRegexForFixedDate(dateOfPeriodToClean);
        return FileFilterUtil.afterLastSlash(regex);
    }
}
