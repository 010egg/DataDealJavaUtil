// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import org.postgresql.largeobject.LargeObjectManager;
import java.sql.Blob;
import java.io.OutputStream;
import java.io.InputStream;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.util.Iterator;
import java.sql.SQLException;
import java.util.ArrayList;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.core.BaseConnection;

public abstract class AbstractJdbc2BlobClob
{
    protected BaseConnection conn;
    private LargeObject currentLo;
    private long loPos;
    private boolean currentLoIsWriteable;
    private boolean support64bit;
    private ArrayList subLOs;
    private final long oid;
    
    public AbstractJdbc2BlobClob(final BaseConnection conn, final long oid) throws SQLException {
        this.conn = conn;
        this.oid = oid;
        this.currentLo = null;
        this.currentLoIsWriteable = false;
        if (conn.haveMinimumServerVersion(90300)) {
            this.support64bit = true;
        }
        else {
            this.support64bit = false;
        }
        this.subLOs = new ArrayList();
    }
    
    public synchronized void free() throws SQLException {
        if (this.currentLo != null) {
            this.currentLo.close();
            this.currentLo = null;
            this.currentLoIsWriteable = false;
        }
        for (final LargeObject subLO : this.subLOs) {
            subLO.close();
        }
        this.subLOs = null;
    }
    
    public synchronized void truncate(final long len) throws SQLException {
        this.checkFreed();
        if (!this.conn.haveMinimumServerVersion("8.3")) {
            throw new PSQLException(GT.tr("Truncation of large objects is only implemented in 8.3 and later servers."), PSQLState.NOT_IMPLEMENTED);
        }
        if (len < 0L) {
            throw new PSQLException(GT.tr("Cannot truncate LOB to a negative length."), PSQLState.INVALID_PARAMETER_VALUE);
        }
        if (len > 2147483647L) {
            if (!this.support64bit) {
                throw new PSQLException(GT.tr("PostgreSQL LOBs can only index to: {0}", new Integer(Integer.MAX_VALUE)), PSQLState.INVALID_PARAMETER_VALUE);
            }
            this.getLo(true).truncate64(len);
        }
        else {
            this.getLo(true).truncate((int)len);
        }
    }
    
    public synchronized long length() throws SQLException {
        this.checkFreed();
        if (this.support64bit) {
            return this.getLo(false).size64();
        }
        return this.getLo(false).size();
    }
    
    public synchronized byte[] getBytes(final long pos, final int length) throws SQLException {
        this.assertPosition(pos);
        this.getLo(false).seek((int)(pos - 1L), 0);
        return this.getLo(false).read(length);
    }
    
    public synchronized InputStream getBinaryStream() throws SQLException {
        this.checkFreed();
        final LargeObject subLO = this.getLo(false).copy();
        this.addSubLO(subLO);
        subLO.seek(0, 0);
        return subLO.getInputStream();
    }
    
    public synchronized OutputStream setBinaryStream(final long pos) throws SQLException {
        this.assertPosition(pos);
        final LargeObject subLO = this.getLo(true).copy();
        this.addSubLO(subLO);
        subLO.seek((int)(pos - 1L));
        return subLO.getOutputStream();
    }
    
    public synchronized long position(final byte[] pattern, final long start) throws SQLException {
        this.assertPosition(start, pattern.length);
        int position = 1;
        int patternIdx = 0;
        long result = -1L;
        int tmpPosition = 1;
        final LOIterator i = new LOIterator(start - 1L);
        while (i.hasNext()) {
            final byte b = i.next();
            if (b == pattern[patternIdx]) {
                if (patternIdx == 0) {
                    tmpPosition = position;
                }
                if (++patternIdx == pattern.length) {
                    result = tmpPosition;
                    break;
                }
            }
            else {
                patternIdx = 0;
            }
            ++position;
        }
        return result;
    }
    
    public synchronized long position(final Blob pattern, final long start) throws SQLException {
        return this.position(pattern.getBytes(1L, (int)pattern.length()), start);
    }
    
    protected void assertPosition(final long pos) throws SQLException {
        this.assertPosition(pos, 0L);
    }
    
    protected void assertPosition(final long pos, final long len) throws SQLException {
        this.checkFreed();
        if (pos < 1L) {
            throw new PSQLException(GT.tr("LOB positioning offsets start at 1."), PSQLState.INVALID_PARAMETER_VALUE);
        }
        if (pos + len - 1L > 2147483647L) {
            throw new PSQLException(GT.tr("PostgreSQL LOBs can only index to: {0}", new Integer(Integer.MAX_VALUE)), PSQLState.INVALID_PARAMETER_VALUE);
        }
    }
    
    protected void checkFreed() throws SQLException {
        if (this.subLOs == null) {
            throw new PSQLException(GT.tr("free() was called on this LOB previously"), PSQLState.OBJECT_NOT_IN_STATE);
        }
    }
    
    protected synchronized LargeObject getLo(final boolean forWrite) throws SQLException {
        if (this.currentLo != null) {
            if (forWrite && !this.currentLoIsWriteable) {
                final int currentPos = this.currentLo.tell();
                final LargeObjectManager lom = this.conn.getLargeObjectAPI();
                final LargeObject newLo = lom.open(this.oid, 393216);
                this.subLOs.add(this.currentLo);
                this.currentLo = newLo;
                if (currentPos != 0) {
                    this.currentLo.seek(currentPos);
                }
            }
            return this.currentLo;
        }
        final LargeObjectManager lom2 = this.conn.getLargeObjectAPI();
        this.currentLo = lom2.open(this.oid, forWrite ? 393216 : 262144);
        this.currentLoIsWriteable = forWrite;
        return this.currentLo;
    }
    
    protected void addSubLO(final LargeObject subLO) {
        this.subLOs.add(subLO);
    }
    
    private class LOIterator
    {
        private static final int BUFFER_SIZE = 8096;
        private byte[] buffer;
        private int idx;
        private int numBytes;
        
        public LOIterator(final long start) throws SQLException {
            this.buffer = new byte[8096];
            this.idx = 8096;
            this.numBytes = 8096;
            AbstractJdbc2BlobClob.this.getLo(false).seek((int)start);
        }
        
        public boolean hasNext() throws SQLException {
            boolean result = false;
            if (this.idx < this.numBytes) {
                result = true;
            }
            else {
                this.numBytes = AbstractJdbc2BlobClob.this.getLo(false).read(this.buffer, 0, 8096);
                this.idx = 0;
                result = (this.numBytes > 0);
            }
            return result;
        }
        
        private byte next() {
            return this.buffer[this.idx++];
        }
    }
}
