// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling.helper;

import ch.qos.logback.core.util.FileUtil;
import ch.qos.logback.core.util.EnvUtil;
import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.RollingFileAppender;
import java.io.File;
import ch.qos.logback.core.spi.ContextAwareBase;

public class RenameUtil extends ContextAwareBase
{
    static String RENAMING_ERROR_URL;
    
    public void rename(final String src, final String target) throws RolloverFailure {
        if (src.equals(target)) {
            this.addWarn("Source and target files are the same [" + src + "]. Skipping.");
            return;
        }
        final File srcFile = new File(src);
        if (srcFile.exists()) {
            final File targetFile = new File(target);
            this.createMissingTargetDirsIfNecessary(targetFile);
            this.addInfo("Renaming file [" + srcFile + "] to [" + targetFile + "]");
            final boolean result = srcFile.renameTo(targetFile);
            if (!result) {
                this.addWarn("Failed to rename file [" + srcFile + "] as [" + targetFile + "].");
                final Boolean areOnDifferentVolumes = this.areOnDifferentVolumes(srcFile, targetFile);
                if (Boolean.TRUE.equals(areOnDifferentVolumes)) {
                    this.addWarn("Detected different file systems for source [" + src + "] and target [" + target + "]. Attempting rename by copying.");
                    this.renameByCopying(src, target);
                    return;
                }
                this.addWarn("Please consider leaving the [file] option of " + RollingFileAppender.class.getSimpleName() + " empty.");
                this.addWarn("See also " + RenameUtil.RENAMING_ERROR_URL);
            }
            return;
        }
        throw new RolloverFailure("File [" + src + "] does not exist.");
    }
    
    Boolean areOnDifferentVolumes(final File srcFile, final File targetFile) throws RolloverFailure {
        if (!EnvUtil.isJDK7OrHigher()) {
            return false;
        }
        final File parentOfTarget = targetFile.getAbsoluteFile().getParentFile();
        if (parentOfTarget == null) {
            this.addWarn("Parent of target file [" + targetFile + "] is null");
            return null;
        }
        if (!parentOfTarget.exists()) {
            this.addWarn("Parent of target file [" + targetFile + "] does not exist");
            return null;
        }
        try {
            final boolean onSameFileStore = FileStoreUtil.areOnSameFileStore(srcFile, parentOfTarget);
            return !onSameFileStore;
        }
        catch (RolloverFailure rf) {
            this.addWarn("Error while checking file store equality", rf);
            return null;
        }
    }
    
    public void renameByCopying(final String src, final String target) throws RolloverFailure {
        final FileUtil fileUtil = new FileUtil(this.getContext());
        fileUtil.copy(src, target);
        final File srcFile = new File(src);
        if (!srcFile.delete()) {
            this.addWarn("Could not delete " + src);
        }
    }
    
    void createMissingTargetDirsIfNecessary(final File toFile) throws RolloverFailure {
        final boolean result = FileUtil.createMissingParentDirectories(toFile);
        if (!result) {
            throw new RolloverFailure("Failed to create parent directories for [" + toFile.getAbsolutePath() + "]");
        }
    }
    
    @Override
    public String toString() {
        return "c.q.l.co.rolling.helper.RenameUtil";
    }
    
    static {
        RenameUtil.RENAMING_ERROR_URL = "http://logback.qos.ch/codes.html#renamingError";
    }
}
