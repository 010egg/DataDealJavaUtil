// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util;

import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Date;
import java.util.TimerTask;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.io.OutputStream;
import java.util.TimeZone;
import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.io.FilterOutputStream;

public class RolloverFileOutputStream extends FilterOutputStream
{
    private static Timer __rollover;
    static final String YYYY_MM_DD = "yyyy_mm_dd";
    static final String ROLLOVER_FILE_DATE_FORMAT = "yyyy_MM_dd";
    static final String ROLLOVER_FILE_BACKUP_FORMAT = "HHmmssSSS";
    static final int ROLLOVER_FILE_RETAIN_DAYS = 31;
    private RollTask _rollTask;
    private SimpleDateFormat _fileBackupFormat;
    private SimpleDateFormat _fileDateFormat;
    private String _filename;
    private File _file;
    private boolean _append;
    private int _retainDays;
    
    public RolloverFileOutputStream(final String filename) throws IOException {
        this(filename, true, 31);
    }
    
    public RolloverFileOutputStream(final String filename, final boolean append) throws IOException {
        this(filename, append, 31);
    }
    
    public RolloverFileOutputStream(final String filename, final boolean append, final int retainDays) throws IOException {
        this(filename, append, retainDays, TimeZone.getDefault());
    }
    
    public RolloverFileOutputStream(final String filename, final boolean append, final int retainDays, final TimeZone zone) throws IOException {
        this(filename, append, retainDays, zone, null, null);
    }
    
    public RolloverFileOutputStream(String filename, final boolean append, final int retainDays, final TimeZone zone, String dateFormat, String backupFormat) throws IOException {
        super(null);
        if (dateFormat == null) {
            dateFormat = "yyyy_MM_dd";
        }
        this._fileDateFormat = new SimpleDateFormat(dateFormat);
        if (backupFormat == null) {
            backupFormat = "HHmmssSSS";
        }
        (this._fileBackupFormat = new SimpleDateFormat(backupFormat)).setTimeZone(zone);
        this._fileDateFormat.setTimeZone(zone);
        if (filename != null) {
            filename = filename.trim();
            if (filename.length() == 0) {
                filename = null;
            }
        }
        if (filename == null) {
            throw new IllegalArgumentException("Invalid filename");
        }
        this._filename = filename;
        this._append = append;
        this._retainDays = retainDays;
        this.setFile();
        synchronized (RolloverFileOutputStream.class) {
            if (RolloverFileOutputStream.__rollover == null) {
                RolloverFileOutputStream.__rollover = new Timer(RolloverFileOutputStream.class.getName(), true);
            }
            this._rollTask = new RollTask();
            final Calendar now = Calendar.getInstance();
            now.setTimeZone(zone);
            final GregorianCalendar midnight = new GregorianCalendar(now.get(1), now.get(2), now.get(5), 23, 0);
            midnight.setTimeZone(zone);
            midnight.add(10, 1);
            RolloverFileOutputStream.__rollover.scheduleAtFixedRate(this._rollTask, midnight.getTime(), 86400000L);
        }
    }
    
    public String getFilename() {
        return this._filename;
    }
    
    public String getDatedFilename() {
        if (this._file == null) {
            return null;
        }
        return this._file.toString();
    }
    
    public int getRetainDays() {
        return this._retainDays;
    }
    
    private synchronized void setFile() throws IOException {
        File file = new File(this._filename);
        this._filename = file.getCanonicalPath();
        file = new File(this._filename);
        final File dir = new File(file.getParent());
        if (!dir.isDirectory() || !dir.canWrite()) {
            throw new IOException("Cannot write log directory " + dir);
        }
        final Date now = new Date();
        final String filename = file.getName();
        final int i = filename.toLowerCase(Locale.ENGLISH).indexOf("yyyy_mm_dd");
        if (i >= 0) {
            file = new File(dir, filename.substring(0, i) + this._fileDateFormat.format(now) + filename.substring(i + "yyyy_mm_dd".length()));
        }
        if (file.exists() && !file.canWrite()) {
            throw new IOException("Cannot write log file " + file);
        }
        if (this.out == null || !file.equals(this._file)) {
            this._file = file;
            if (!this._append && file.exists()) {
                file.renameTo(new File(file.toString() + "." + this._fileBackupFormat.format(now)));
            }
            final OutputStream oldOut = this.out;
            this.out = new FileOutputStream(file.toString(), this._append);
            if (oldOut != null) {
                oldOut.close();
            }
        }
    }
    
    private void removeOldFiles() {
        if (this._retainDays > 0) {
            final long now = System.currentTimeMillis();
            final File file = new File(this._filename);
            final File dir = new File(file.getParent());
            String fn = file.getName();
            final int s = fn.toLowerCase(Locale.ENGLISH).indexOf("yyyy_mm_dd");
            if (s < 0) {
                return;
            }
            final String prefix = fn.substring(0, s);
            final String suffix = fn.substring(s + "yyyy_mm_dd".length());
            final String[] logList = dir.list();
            for (int i = 0; i < logList.length; ++i) {
                fn = logList[i];
                if (fn.startsWith(prefix) && fn.indexOf(suffix, prefix.length()) >= 0) {
                    final File f = new File(dir, fn);
                    final long date = f.lastModified();
                    if ((now - date) / 86400000L > this._retainDays) {
                        f.delete();
                    }
                }
            }
        }
    }
    
    @Override
    public void write(final byte[] buf) throws IOException {
        this.out.write(buf);
    }
    
    @Override
    public void write(final byte[] buf, final int off, final int len) throws IOException {
        this.out.write(buf, off, len);
    }
    
    @Override
    public void close() throws IOException {
        synchronized (RolloverFileOutputStream.class) {
            try {
                super.close();
            }
            finally {
                this.out = null;
                this._file = null;
            }
            this._rollTask.cancel();
        }
    }
    
    private class RollTask extends TimerTask
    {
        @Override
        public void run() {
            try {
                RolloverFileOutputStream.this.setFile();
                RolloverFileOutputStream.this.removeOldFiles();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
