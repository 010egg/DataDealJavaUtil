// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.io.InputStream;
import java.io.EOFException;
import java.io.FilterOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.io.Writer;
import java.io.OutputStream;
import java.net.Socket;
import org.postgresql.util.HostSpec;

public class PGStream
{
    private final HostSpec hostSpec;
    private final byte[] _int4buf;
    private final byte[] _int2buf;
    private Socket connection;
    private VisibleBufferedInputStream pg_input;
    private OutputStream pg_output;
    private byte[] streamBuffer;
    private Encoding encoding;
    private Writer encodingWriter;
    
    public PGStream(final HostSpec hostSpec, final int timeout) throws IOException {
        this.hostSpec = hostSpec;
        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress(hostSpec.getHost(), hostSpec.getPort()), timeout);
        this.changeSocket(socket);
        this.setEncoding(Encoding.getJVMEncoding("US-ASCII"));
        this._int2buf = new byte[2];
        this._int4buf = new byte[4];
    }
    
    @Deprecated
    public PGStream(final HostSpec hostSpec) throws IOException {
        this(hostSpec, 0);
    }
    
    public HostSpec getHostSpec() {
        return this.hostSpec;
    }
    
    public Socket getSocket() {
        return this.connection;
    }
    
    public boolean hasMessagePending() throws IOException {
        return this.pg_input.available() > 0 || this.connection.getInputStream().available() > 0;
    }
    
    public void changeSocket(final Socket socket) throws IOException {
        (this.connection = socket).setTcpNoDelay(true);
        this.pg_input = new VisibleBufferedInputStream(this.connection.getInputStream(), 8192);
        this.pg_output = new BufferedOutputStream(this.connection.getOutputStream(), 8192);
        if (this.encoding != null) {
            this.setEncoding(this.encoding);
        }
    }
    
    public Encoding getEncoding() {
        return this.encoding;
    }
    
    public void setEncoding(final Encoding encoding) throws IOException {
        if (this.encodingWriter != null) {
            this.encodingWriter.close();
        }
        this.encoding = encoding;
        final OutputStream interceptor = new FilterOutputStream(this.pg_output) {
            @Override
            public void flush() throws IOException {
            }
            
            @Override
            public void close() throws IOException {
                super.flush();
            }
        };
        this.encodingWriter = encoding.getEncodingWriter(interceptor);
    }
    
    public Writer getEncodingWriter() throws IOException {
        if (this.encodingWriter == null) {
            throw new IOException("No encoding has been set on this connection");
        }
        return this.encodingWriter;
    }
    
    public void SendChar(final int val) throws IOException {
        this.pg_output.write(val);
    }
    
    public void SendInteger4(final int val) throws IOException {
        this._int4buf[0] = (byte)(val >>> 24);
        this._int4buf[1] = (byte)(val >>> 16);
        this._int4buf[2] = (byte)(val >>> 8);
        this._int4buf[3] = (byte)val;
        this.pg_output.write(this._int4buf);
    }
    
    public void SendInteger2(final int val) throws IOException {
        if (val < -32768 || val > 32767) {
            throw new IOException("Tried to send an out-of-range integer as a 2-byte value: " + val);
        }
        this._int2buf[0] = (byte)(val >>> 8);
        this._int2buf[1] = (byte)val;
        this.pg_output.write(this._int2buf);
    }
    
    public void Send(final byte[] buf) throws IOException {
        this.pg_output.write(buf);
    }
    
    public void Send(final byte[] buf, final int siz) throws IOException {
        this.Send(buf, 0, siz);
    }
    
    public void Send(final byte[] buf, final int off, final int siz) throws IOException {
        final int bufamt = buf.length - off;
        this.pg_output.write(buf, off, (bufamt < siz) ? bufamt : siz);
        for (int i = bufamt; i < siz; ++i) {
            this.pg_output.write(0);
        }
    }
    
    public int PeekChar() throws IOException {
        final int c = this.pg_input.peek();
        if (c < 0) {
            throw new EOFException();
        }
        return c;
    }
    
    public int ReceiveChar() throws IOException {
        final int c = this.pg_input.read();
        if (c < 0) {
            throw new EOFException();
        }
        return c;
    }
    
    public int ReceiveInteger4() throws IOException {
        if (this.pg_input.read(this._int4buf) != 4) {
            throw new EOFException();
        }
        return (this._int4buf[0] & 0xFF) << 24 | (this._int4buf[1] & 0xFF) << 16 | (this._int4buf[2] & 0xFF) << 8 | (this._int4buf[3] & 0xFF);
    }
    
    public int ReceiveInteger2() throws IOException {
        if (this.pg_input.read(this._int2buf) != 2) {
            throw new EOFException();
        }
        return (this._int2buf[0] & 0xFF) << 8 | (this._int2buf[1] & 0xFF);
    }
    
    public String ReceiveString(final int len) throws IOException {
        if (!this.pg_input.ensureBytes(len)) {
            throw new EOFException();
        }
        final String res = this.encoding.decode(this.pg_input.getBuffer(), this.pg_input.getIndex(), len);
        this.pg_input.skip(len);
        return res;
    }
    
    public String ReceiveString() throws IOException {
        final int len = this.pg_input.scanCStringLength();
        final String res = this.encoding.decode(this.pg_input.getBuffer(), this.pg_input.getIndex(), len - 1);
        this.pg_input.skip(len);
        return res;
    }
    
    public byte[][] ReceiveTupleV3() throws IOException, OutOfMemoryError {
        final int l_msgSize = this.ReceiveInteger4();
        final int l_nf = this.ReceiveInteger2();
        final byte[][] answer = new byte[l_nf][];
        OutOfMemoryError oom = null;
        for (int i = 0; i < l_nf; ++i) {
            final int l_size = this.ReceiveInteger4();
            if (l_size != -1) {
                try {
                    this.Receive(answer[i] = new byte[l_size], 0, l_size);
                }
                catch (OutOfMemoryError oome) {
                    oom = oome;
                    this.Skip(l_size);
                }
            }
        }
        if (oom != null) {
            throw oom;
        }
        return answer;
    }
    
    public byte[][] ReceiveTupleV2(final int nf, final boolean bin) throws IOException, OutOfMemoryError {
        final int bim = (nf + 7) / 8;
        final byte[] bitmask = this.Receive(bim);
        final byte[][] answer = new byte[nf][];
        int whichbit = 128;
        int whichbyte = 0;
        OutOfMemoryError oom = null;
        for (int i = 0; i < nf; ++i) {
            final boolean isNull = (bitmask[whichbyte] & whichbit) == 0x0;
            whichbit >>= 1;
            if (whichbit == 0) {
                ++whichbyte;
                whichbit = 128;
            }
            if (!isNull) {
                int len = this.ReceiveInteger4();
                if (!bin) {
                    len -= 4;
                }
                if (len < 0) {
                    len = 0;
                }
                try {
                    this.Receive(answer[i] = new byte[len], 0, len);
                }
                catch (OutOfMemoryError oome) {
                    oom = oome;
                    this.Skip(len);
                }
            }
        }
        if (oom != null) {
            throw oom;
        }
        return answer;
    }
    
    public byte[] Receive(final int siz) throws IOException {
        final byte[] answer = new byte[siz];
        this.Receive(answer, 0, siz);
        return answer;
    }
    
    public void Receive(final byte[] buf, final int off, final int siz) throws IOException {
        int w;
        for (int s = 0; s < siz; s += w) {
            w = this.pg_input.read(buf, off + s, siz - s);
            if (w < 0) {
                throw new EOFException();
            }
        }
    }
    
    public void Skip(final int size) throws IOException {
        for (long s = 0L; s < size; s += this.pg_input.skip(size - s)) {}
    }
    
    public void SendStream(final InputStream inStream, int remaining) throws IOException {
        final int expectedLength = remaining;
        if (this.streamBuffer == null) {
            this.streamBuffer = new byte[8192];
        }
        while (remaining > 0) {
            int count = (remaining > this.streamBuffer.length) ? this.streamBuffer.length : remaining;
            int readCount;
            try {
                readCount = inStream.read(this.streamBuffer, 0, count);
                if (readCount < 0) {
                    throw new EOFException(GT.tr("Premature end of input stream, expected {0} bytes, but only read {1}.", new Object[] { new Integer(expectedLength), new Integer(expectedLength - remaining) }));
                }
            }
            catch (IOException ioe) {
                while (remaining > 0) {
                    this.Send(this.streamBuffer, count);
                    remaining -= count;
                    count = ((remaining > this.streamBuffer.length) ? this.streamBuffer.length : remaining);
                }
                throw new PGBindException(ioe);
            }
            this.Send(this.streamBuffer, readCount);
            remaining -= readCount;
        }
    }
    
    public void flush() throws IOException {
        if (this.encodingWriter != null) {
            this.encodingWriter.flush();
        }
        this.pg_output.flush();
    }
    
    public void ReceiveEOF() throws SQLException, IOException {
        final int c = this.pg_input.read();
        if (c < 0) {
            return;
        }
        throw new PSQLException(GT.tr("Expected an EOF from server, got: {0}", new Integer(c)), PSQLState.COMMUNICATION_ERROR);
    }
    
    public void close() throws IOException {
        if (this.encodingWriter != null) {
            this.encodingWriter.close();
        }
        this.pg_output.close();
        this.pg_input.close();
        this.connection.close();
    }
}
