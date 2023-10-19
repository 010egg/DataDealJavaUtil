// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling.helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.LiteralConverter;
import java.util.Arrays;
import java.util.Comparator;
import ch.qos.logback.core.util.FileSize;
import java.io.File;
import java.util.Date;
import ch.qos.logback.core.spi.ContextAwareBase;

public class TimeBasedArchiveRemover extends ContextAwareBase implements ArchiveRemover
{
    private static final int UNTOUCHABLE_ARCHIVE_FILE_COUNT = 2;
    protected static final long UNINITIALIZED = -1L;
    protected static final long INACTIVITY_TOLERANCE_IN_MILLIS = 2764800000L;
    static final int MAX_VALUE_FOR_INACTIVITY_PERIODS = 336;
    final FileNamePattern fileNamePattern;
    final RollingCalendar rc;
    private int maxHistory;
    private long totalSizeCap;
    final boolean parentClean;
    long lastHeartBeat;
    
    public TimeBasedArchiveRemover(final FileNamePattern fileNamePattern, final RollingCalendar rc) {
        this.maxHistory = 0;
        this.totalSizeCap = 0L;
        this.lastHeartBeat = -1L;
        this.fileNamePattern = fileNamePattern;
        this.rc = rc;
        this.parentClean = this.computeParentCleaningFlag(fileNamePattern);
    }
    
    @Override
    public void clean(final Date now) {
        final long nowInMillis = now.getTime();
        final int periodsElapsed = this.computeElapsedPeriodsSinceLastClean(nowInMillis);
        this.lastHeartBeat = nowInMillis;
        if (periodsElapsed > 1) {
            this.addInfo("Multiple periods, i.e. " + periodsElapsed + " periods, seem to have elapsed. This is expected at application start.");
        }
        for (int i = 0; i < periodsElapsed; ++i) {
            final int offset = this.getPeriodOffsetForDeletionTarget() - i;
            final Date dateOfPeriodToClean = this.rc.getEndOfNextNthPeriod(now, offset);
            this.cleanPeriod(dateOfPeriodToClean);
        }
    }
    
    protected File[] getFilesInPeriod(final Date dateOfPeriodToClean) {
        final String filenameToDelete = this.fileNamePattern.convert(dateOfPeriodToClean);
        final File file2Delete = new File(filenameToDelete);
        if (this.fileExistsAndIsFile(file2Delete)) {
            return new File[] { file2Delete };
        }
        return new File[0];
    }
    
    private boolean fileExistsAndIsFile(final File file2Delete) {
        return file2Delete.exists() && file2Delete.isFile();
    }
    
    public void cleanPeriod(final Date dateOfPeriodToClean) {
        final File[] arr$;
        final File[] matchingFileArray = arr$ = this.getFilesInPeriod(dateOfPeriodToClean);
        for (final File f : arr$) {
            this.addInfo("deleting " + f);
            f.delete();
        }
        if (this.parentClean && matchingFileArray.length > 0) {
            final File parentDir = this.getParentDir(matchingFileArray[0]);
            this.removeFolderIfEmpty(parentDir);
        }
    }
    
    void capTotalSize(final Date now) {
        int totalSize = 0;
        int totalRemoved = 0;
        for (int offset = 0; offset < this.maxHistory; ++offset) {
            final Date date = this.rc.getEndOfNextNthPeriod(now, -offset);
            final File[] matchingFileArray = this.getFilesInPeriod(date);
            this.descendingSortByLastModified(matchingFileArray);
            for (final File f : matchingFileArray) {
                final long size = f.length();
                if (totalSize + size > this.totalSizeCap) {
                    if (offset >= 2) {
                        this.addInfo("Deleting [" + f + "]" + " of size " + new FileSize(size));
                        totalRemoved += (int)size;
                        f.delete();
                    }
                    else {
                        this.addWarn("Skipping [" + f + "]" + " of size " + new FileSize(size) + " as it is one of the two newest log achives.");
                    }
                }
                totalSize += (int)size;
            }
        }
        this.addInfo("Removed  " + new FileSize(totalRemoved) + " of files");
    }
    
    private void descendingSortByLastModified(final File[] matchingFileArray) {
        Arrays.sort(matchingFileArray, new Comparator<File>() {
            @Override
            public int compare(final File f1, final File f2) {
                final long l1 = f1.lastModified();
                final long l2 = f2.lastModified();
                if (l1 == l2) {
                    return 0;
                }
                if (l2 < l1) {
                    return -1;
                }
                return 1;
            }
        });
    }
    
    File getParentDir(final File file) {
        final File absolute = file.getAbsoluteFile();
        final File parentDir = absolute.getParentFile();
        return parentDir;
    }
    
    int computeElapsedPeriodsSinceLastClean(final long nowInMillis) {
        long periodsElapsed = 0L;
        if (this.lastHeartBeat == -1L) {
            this.addInfo("first clean up after appender initialization");
            periodsElapsed = this.rc.periodBarriersCrossed(nowInMillis, nowInMillis + 2764800000L);
            periodsElapsed = Math.min(periodsElapsed, 336L);
        }
        else {
            periodsElapsed = this.rc.periodBarriersCrossed(this.lastHeartBeat, nowInMillis);
        }
        return (int)periodsElapsed;
    }
    
    boolean computeParentCleaningFlag(final FileNamePattern fileNamePattern) {
        final DateTokenConverter<Object> dtc = fileNamePattern.getPrimaryDateTokenConverter();
        if (dtc.getDatePattern().indexOf(47) != -1) {
            return true;
        }
        Converter<Object> p;
        for (p = fileNamePattern.headTokenConverter; p != null; p = p.getNext()) {
            if (p instanceof DateTokenConverter) {
                break;
            }
        }
        while (p != null) {
            if (p instanceof LiteralConverter) {
                final String s = p.convert(null);
                if (s.indexOf(47) != -1) {
                    return true;
                }
            }
            p = p.getNext();
        }
        return false;
    }
    
    void removeFolderIfEmpty(final File dir) {
        this.removeFolderIfEmpty(dir, 0);
    }
    
    private void removeFolderIfEmpty(final File dir, final int depth) {
        if (depth >= 3) {
            return;
        }
        if (dir.isDirectory() && FileFilterUtil.isEmptyDirectory(dir)) {
            this.addInfo("deleting folder [" + dir + "]");
            dir.delete();
            this.removeFolderIfEmpty(dir.getParentFile(), depth + 1);
        }
    }
    
    @Override
    public void setMaxHistory(final int maxHistory) {
        this.maxHistory = maxHistory;
    }
    
    protected int getPeriodOffsetForDeletionTarget() {
        return -this.maxHistory - 1;
    }
    
    @Override
    public void setTotalSizeCap(final long totalSizeCap) {
        this.totalSizeCap = totalSizeCap;
    }
    
    @Override
    public String toString() {
        return "c.q.l.core.rolling.helper.TimeBasedArchiveRemover";
    }
    
    @Override
    public Future<?> cleanAsynchronously(final Date now) {
        final ArhiveRemoverRunnable runnable = new ArhiveRemoverRunnable(now);
        final ExecutorService executorService = this.context.getScheduledExecutorService();
        final Future<?> future = executorService.submit(runnable);
        return future;
    }
    
    public class ArhiveRemoverRunnable implements Runnable
    {
        Date now;
        
        ArhiveRemoverRunnable(final Date now) {
            this.now = now;
        }
        
        @Override
        public void run() {
            TimeBasedArchiveRemover.this.clean(this.now);
            if (TimeBasedArchiveRemover.this.totalSizeCap != 0L && TimeBasedArchiveRemover.this.totalSizeCap > 0L) {
                TimeBasedArchiveRemover.this.capTotalSize(this.now);
            }
        }
    }
}
