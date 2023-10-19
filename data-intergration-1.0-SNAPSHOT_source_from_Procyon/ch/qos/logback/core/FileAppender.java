// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

import java.nio.channels.FileLock;
import java.nio.channels.FileChannel;
import java.io.OutputStream;
import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import ch.qos.logback.core.util.FileUtil;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;

public class FileAppender<E> extends OutputStreamAppender<E>
{
    protected static String COLLISION_WITH_EARLIER_APPENDER_URL;
    protected boolean append;
    protected String fileName;
    private boolean prudent;
    
    public FileAppender() {
        this.append = true;
        this.fileName = null;
        this.prudent = false;
    }
    
    public void setFile(final String file) {
        if (file == null) {
            this.fileName = file;
        }
        else {
            this.fileName = file.trim();
        }
    }
    
    public boolean isAppend() {
        return this.append;
    }
    
    public final String rawFileProperty() {
        return this.fileName;
    }
    
    public String getFile() {
        return this.fileName;
    }
    
    @Override
    public void start() {
        int errors = 0;
        if (this.getFile() != null) {
            this.addInfo("File property is set to [" + this.fileName + "]");
            if (this.checkForFileCollisionInPreviousFileAppenders()) {
                this.addError("Collisions detected with FileAppender/RollingAppender instances defined earlier. Aborting.");
                this.addError("For more information, please visit " + FileAppender.COLLISION_WITH_EARLIER_APPENDER_URL);
                ++errors;
            }
            if (this.prudent && !this.isAppend()) {
                this.setAppend(true);
                this.addWarn("Setting \"Append\" property to true on account of \"Prudent\" mode");
            }
            try {
                this.openFile(this.getFile());
            }
            catch (IOException e) {
                ++errors;
                this.addError("openFile(" + this.fileName + "," + this.append + ") call failed.", e);
            }
        }
        else {
            ++errors;
            this.addError("\"File\" property not set for appender named [" + this.name + "].");
        }
        if (errors == 0) {
            super.start();
        }
    }
    
    protected boolean checkForFileCollisionInPreviousFileAppenders() {
        boolean collisionsDetected = false;
        if (this.fileName == null) {
            return false;
        }
        final Map<String, String> map = (Map<String, String>)this.context.getObject("RFA_FILENAME_PATTERN_COLLISION_MAP");
        if (map == null) {
            return collisionsDetected;
        }
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            if (this.fileName.equals(entry.getValue())) {
                this.addErrorForCollision("File", entry.getValue(), entry.getKey());
                collisionsDetected = true;
            }
        }
        if (this.name != null) {
            map.put(this.getName(), this.fileName);
        }
        return collisionsDetected;
    }
    
    protected void addErrorForCollision(final String optionName, final String optionValue, final String appenderName) {
        this.addError("'" + optionName + "' option has the same value \"" + optionValue + "\" as that given for appender [" + appenderName + "] defined earlier.");
    }
    
    public void openFile(final String file_name) throws IOException {
        this.lock.lock();
        try {
            final File file = new File(file_name);
            final boolean result = FileUtil.createMissingParentDirectories(file);
            if (!result) {
                this.addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
            }
            final ResilientFileOutputStream resilientFos = new ResilientFileOutputStream(file, this.append);
            resilientFos.setContext(this.context);
            this.setOutputStream(resilientFos);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public boolean isPrudent() {
        return this.prudent;
    }
    
    public void setPrudent(final boolean prudent) {
        this.prudent = prudent;
    }
    
    public void setAppend(final boolean append) {
        this.append = append;
    }
    
    private void safeWrite(final E event) throws IOException {
        final ResilientFileOutputStream resilientFOS = (ResilientFileOutputStream)this.getOutputStream();
        final FileChannel fileChannel = resilientFOS.getChannel();
        if (fileChannel == null) {
            return;
        }
        final boolean interrupted = Thread.interrupted();
        FileLock fileLock = null;
        try {
            fileLock = fileChannel.lock();
            final long position = fileChannel.position();
            final long size = fileChannel.size();
            if (size != position) {
                fileChannel.position(size);
            }
            super.writeOut(event);
        }
        catch (IOException e) {
            resilientFOS.postIOFailure(e);
        }
        finally {
            if (fileLock != null && fileLock.isValid()) {
                fileLock.release();
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @Override
    protected void writeOut(final E event) throws IOException {
        if (this.prudent) {
            this.safeWrite(event);
        }
        else {
            super.writeOut(event);
        }
    }
    
    static {
        FileAppender.COLLISION_WITH_EARLIER_APPENDER_URL = "http://logback.qos.ch/codes.html#earlier_fa_collision";
    }
}
