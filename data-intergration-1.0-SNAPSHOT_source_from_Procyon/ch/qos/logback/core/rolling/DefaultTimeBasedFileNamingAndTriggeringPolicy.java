// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

import java.util.Date;
import java.io.File;
import ch.qos.logback.core.rolling.helper.TimeBasedArchiveRemover;
import ch.qos.logback.core.joran.spi.NoAutoStart;

@NoAutoStart
public class DefaultTimeBasedFileNamingAndTriggeringPolicy<E> extends TimeBasedFileNamingAndTriggeringPolicyBase<E>
{
    @Override
    public void start() {
        super.start();
        if (!super.isErrorFree()) {
            return;
        }
        if (this.tbrp.fileNamePattern.hasIntegerTokenCOnverter()) {
            this.addError("Filename pattern [" + this.tbrp.fileNamePattern + "] contains an integer token converter, i.e. %i, INCOMPATIBLE with this configuration. Remove it.");
            return;
        }
        (this.archiveRemover = new TimeBasedArchiveRemover(this.tbrp.fileNamePattern, this.rc)).setContext(this.context);
        this.started = true;
    }
    
    @Override
    public boolean isTriggeringEvent(final File activeFile, final E event) {
        final long time = this.getCurrentTime();
        if (time >= this.nextCheck) {
            final Date dateOfElapsedPeriod = this.dateInCurrentPeriod;
            this.addInfo("Elapsed period: " + dateOfElapsedPeriod);
            this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWCS.convert(dateOfElapsedPeriod);
            this.setDateInCurrentPeriod(time);
            this.computeNextCheck();
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "c.q.l.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy";
    }
}
