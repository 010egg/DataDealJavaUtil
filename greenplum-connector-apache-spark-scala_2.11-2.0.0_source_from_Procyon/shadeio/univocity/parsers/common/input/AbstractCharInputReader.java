// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import shadeio.univocity.parsers.common.Format;
import java.util.List;

public abstract class AbstractCharInputReader implements CharInputReader
{
    private final ExpandingCharAppender tmp;
    private boolean lineSeparatorDetected;
    private final boolean detectLineSeparator;
    private List<InputAnalysisProcess> inputAnalysisProcesses;
    private char lineSeparator1;
    private char lineSeparator2;
    private final char normalizedLineSeparator;
    private long lineCount;
    private long charCount;
    private int recordStart;
    final int whitespaceRangeStart;
    private boolean skipping;
    private boolean commentProcessing;
    public int i;
    private char ch;
    public char[] buffer;
    public int length;
    private boolean incrementLineCount;
    private boolean normalizeLineEndings;
    
    public AbstractCharInputReader(final char normalizedLineSeparator, final int whitespaceRangeStart) {
        this(null, normalizedLineSeparator, whitespaceRangeStart);
    }
    
    public AbstractCharInputReader(final char[] lineSeparator, final char normalizedLineSeparator, final int whitespaceRangeStart) {
        this.skipping = false;
        this.commentProcessing = false;
        this.length = -1;
        this.normalizeLineEndings = true;
        this.whitespaceRangeStart = whitespaceRangeStart;
        this.tmp = new ExpandingCharAppender(4096, null, whitespaceRangeStart);
        if (lineSeparator == null) {
            this.detectLineSeparator = true;
            this.submitLineSeparatorDetector();
            this.lineSeparator1 = '\0';
            this.lineSeparator2 = '\0';
        }
        else {
            this.setLineSeparator(lineSeparator);
            this.detectLineSeparator = false;
        }
        this.normalizedLineSeparator = normalizedLineSeparator;
    }
    
    private void submitLineSeparatorDetector() {
        if (this.detectLineSeparator && !this.lineSeparatorDetected) {
            this.addInputAnalysisProcess(new LineSeparatorDetector() {
                @Override
                protected void apply(final char separator1, final char separator2) {
                    if (separator1 != '\0') {
                        AbstractCharInputReader.this.lineSeparatorDetected = true;
                        AbstractCharInputReader.this.lineSeparator1 = separator1;
                        AbstractCharInputReader.this.lineSeparator2 = separator2;
                    }
                    else {
                        AbstractCharInputReader.this.setLineSeparator(Format.getSystemLineSeparator());
                    }
                }
            });
        }
    }
    
    private void setLineSeparator(final char[] lineSeparator) {
        if (lineSeparator == null || lineSeparator.length == 0) {
            throw new IllegalArgumentException("Invalid line separator. Expected 1 to 2 characters");
        }
        if (lineSeparator.length > 2) {
            throw new IllegalArgumentException("Invalid line separator. Up to 2 characters are expected. Got " + lineSeparator.length + " characters.");
        }
        this.lineSeparator1 = lineSeparator[0];
        this.lineSeparator2 = ((lineSeparator.length == 2) ? lineSeparator[1] : '\0');
    }
    
    protected abstract void setReader(final Reader p0);
    
    protected abstract void reloadBuffer();
    
    protected final void unwrapInputStream(final BomInput.BytesProcessedNotification notification) {
        final InputStream inputStream = notification.input;
        final String encoding = notification.encoding;
        if (encoding != null) {
            try {
                this.start(new InputStreamReader(inputStream, encoding));
                return;
            }
            catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        this.length = -1;
        this.start(new InputStreamReader(inputStream));
    }
    
    @Override
    public final void start(final Reader reader) {
        this.stop();
        this.setReader(reader);
        this.lineCount = 0L;
        this.lineSeparatorDetected = false;
        this.submitLineSeparatorDetector();
        this.updateBuffer();
        if (this.length > 0 && this.buffer[0] == '\ufeff') {
            ++this.i;
        }
    }
    
    private void updateBuffer() {
        if (!this.commentProcessing && this.length - this.recordStart > 0 && this.buffer != null && !this.skipping) {
            this.tmp.append(this.buffer, this.recordStart, this.length - this.recordStart);
        }
        this.recordStart = 0;
        this.reloadBuffer();
        this.charCount += this.i;
        this.i = 0;
        if (this.length == -1) {
            this.stop();
            this.incrementLineCount = true;
        }
        if (this.inputAnalysisProcesses != null) {
            if (this.length > 0 && this.length <= 4) {
                final int tmpLength = this.length;
                final char[] tmp = Arrays.copyOfRange(this.buffer, 0, this.length + 1);
                final List<InputAnalysisProcess> processes = this.inputAnalysisProcesses;
                this.inputAnalysisProcesses = null;
                this.reloadBuffer();
                this.inputAnalysisProcesses = processes;
                if (this.length != -1) {
                    final char[] newBuffer = new char[tmpLength + this.buffer.length];
                    System.arraycopy(tmp, 0, newBuffer, 0, tmpLength);
                    System.arraycopy(this.buffer, 0, newBuffer, tmpLength, this.length);
                    this.buffer = newBuffer;
                    this.length += tmpLength;
                }
                else {
                    this.buffer = tmp;
                    this.length = tmpLength;
                }
            }
            try {
                for (final InputAnalysisProcess process : this.inputAnalysisProcesses) {
                    process.execute(this.buffer, this.length);
                }
            }
            finally {
                if (this.length > 4) {
                    this.inputAnalysisProcesses = null;
                }
            }
        }
    }
    
    public final void addInputAnalysisProcess(final InputAnalysisProcess inputAnalysisProcess) {
        if (inputAnalysisProcess == null) {
            return;
        }
        if (this.inputAnalysisProcesses == null) {
            this.inputAnalysisProcesses = new ArrayList<InputAnalysisProcess>();
        }
        this.inputAnalysisProcesses.add(inputAnalysisProcess);
    }
    
    private void throwEOFException() {
        if (this.incrementLineCount) {
            ++this.lineCount;
        }
        this.ch = '\0';
        throw new EOFException();
    }
    
    @Override
    public final char nextChar() {
        if (this.length == -1) {
            this.throwEOFException();
        }
        this.ch = this.buffer[this.i++];
        if (this.i >= this.length) {
            this.updateBuffer();
        }
        if (this.lineSeparator1 == this.ch && (this.lineSeparator2 == '\0' || (this.length != -1 && this.lineSeparator2 == this.buffer[this.i]))) {
            ++this.lineCount;
            if (this.normalizeLineEndings) {
                this.ch = this.normalizedLineSeparator;
                if (this.lineSeparator2 == '\0') {
                    return this.ch;
                }
                if (++this.i >= this.length) {
                    if (this.length != -1) {
                        this.updateBuffer();
                    }
                    else {
                        this.throwEOFException();
                    }
                }
            }
        }
        return this.ch;
    }
    
    @Override
    public final char getChar() {
        return this.ch;
    }
    
    @Override
    public final long lineCount() {
        return this.lineCount;
    }
    
    @Override
    public final void skipLines(final long lines) {
        if (lines < 1L) {
            this.skipping = false;
            return;
        }
        this.skipping = true;
        final long expectedLineCount = this.lineCount + lines;
        try {
            do {
                this.nextChar();
            } while (this.lineCount < expectedLineCount);
            this.skipping = false;
            if (this.lineCount < lines) {
                throw new IllegalArgumentException("Unable to skip " + lines + " lines from line " + (expectedLineCount - lines) + ". End of input reached");
            }
        }
        catch (EOFException ex) {
            this.skipping = false;
            throw new IllegalArgumentException("Unable to skip " + lines + " lines from line " + (expectedLineCount - lines) + ". End of input reached");
        }
    }
    
    @Override
    public String readComment() {
        final long expectedLineCount = this.lineCount + 1L;
        this.commentProcessing = true;
        this.tmp.reset();
        try {
            while (true) {
                char ch = this.nextChar();
                if (ch <= ' ' && this.whitespaceRangeStart < ch) {
                    ch = this.skipWhitespace(ch, this.normalizedLineSeparator, this.normalizedLineSeparator);
                }
                this.tmp.appendUntil(ch, this, this.normalizedLineSeparator, this.normalizedLineSeparator);
                if (this.lineCount >= expectedLineCount) {
                    break;
                }
                this.tmp.appendIgnoringWhitespace(this.nextChar());
            }
            this.tmp.updateWhitespace();
            return this.tmp.getAndReset();
        }
        catch (EOFException ex) {
            this.tmp.updateWhitespace();
            return this.tmp.getAndReset();
        }
        finally {
            this.commentProcessing = false;
        }
    }
    
    @Override
    public final long charCount() {
        return this.charCount + this.i;
    }
    
    @Override
    public final void enableNormalizeLineEndings(final boolean normalizeLineEndings) {
        this.normalizeLineEndings = normalizeLineEndings;
    }
    
    @Override
    public char[] getLineSeparator() {
        if (this.lineSeparator2 != '\0') {
            return new char[] { this.lineSeparator1, this.lineSeparator2 };
        }
        return new char[] { this.lineSeparator1 };
    }
    
    @Override
    public final char skipWhitespace(char ch, final char stopChar1, final char stopChar2) {
        while (ch <= ' ' && ch != stopChar1 && ch != this.normalizedLineSeparator && ch != stopChar2 && this.whitespaceRangeStart < ch) {
            ch = this.nextChar();
        }
        return ch;
    }
    
    @Override
    public final int currentParsedContentLength() {
        return this.i - this.recordStart + this.tmp.length();
    }
    
    @Override
    public final String currentParsedContent() {
        if (this.tmp.length() != 0) {
            if (this.i > this.recordStart) {
                this.tmp.append(this.buffer, this.recordStart, this.i - this.recordStart);
            }
            return this.tmp.getAndReset();
        }
        if (this.i > this.recordStart) {
            return new String(this.buffer, this.recordStart, this.i - this.recordStart);
        }
        return null;
    }
    
    @Override
    public final void markRecordStart() {
        this.tmp.reset();
        this.recordStart = this.i % this.length;
    }
    
    @Override
    public final boolean skipString(char ch, final char stop) {
        if (this.i == 0) {
            return false;
        }
        int i;
        for (i = this.i; ch != stop; ch = this.buffer[i++]) {
            if (i >= this.length) {
                return false;
            }
            if (this.lineSeparator1 == ch) {
                if (this.lineSeparator2 == '\0') {
                    break;
                }
                if (this.lineSeparator2 == this.buffer[i]) {
                    break;
                }
            }
        }
        this.i = i - 1;
        this.nextChar();
        return true;
    }
    
    @Override
    public final String getString(char ch, final char stop, final boolean trim, final String nullValue, final int maxLength) {
        if (this.i == 0) {
            return null;
        }
        int i;
        for (i = this.i; ch != stop; ch = this.buffer[i++]) {
            if (i >= this.length) {
                return null;
            }
            if (this.lineSeparator1 == ch) {
                if (this.lineSeparator2 == '\0') {
                    break;
                }
                if (this.lineSeparator2 == this.buffer[i]) {
                    break;
                }
            }
        }
        final int pos = this.i - 1;
        int len = i - this.i;
        if (len > maxLength) {
            return null;
        }
        this.i = i - 1;
        if (trim) {
            for (i -= 2; this.buffer[i] <= ' ' && this.whitespaceRangeStart < this.buffer[i]; --i) {
                --len;
            }
        }
        String out;
        if (len <= 0) {
            out = nullValue;
        }
        else {
            out = new String(this.buffer, pos, len);
        }
        this.nextChar();
        return out;
    }
    
    @Override
    public final String getQuotedString(final char quote, final char escape, final char escapeEscape, final int maxLength, final char stop1, final char stop2, final boolean keepQuotes, final boolean keepEscape, final boolean trimLeading, final boolean trimTrailing) {
        if (this.i == 0) {
            return null;
        }
        for (int i = this.i; i < this.length; ++i) {
            this.ch = this.buffer[i];
            if (this.ch == quote) {
                if (this.buffer[i - 1] != escape) {
                    if (i + 1 < this.length) {
                        final char next = this.buffer[i + 1];
                        if (next == stop1 || next == stop2) {
                            int pos = this.i;
                            int len = i - this.i;
                            if (len > maxLength) {
                                return null;
                            }
                            if (keepQuotes) {
                                --pos;
                                len += 2;
                            }
                            else {
                                if (trimTrailing) {
                                    while (len > 0 && this.buffer[pos + len - 1] <= ' ') {
                                        --len;
                                    }
                                }
                                if (trimLeading) {
                                    while (len > 0 && this.buffer[pos] <= ' ') {
                                        ++pos;
                                        --len;
                                    }
                                }
                            }
                            this.i = i + 1;
                            String out;
                            if (len <= 0) {
                                out = "";
                            }
                            else {
                                out = new String(this.buffer, pos, len);
                            }
                            if (this.i >= this.length) {
                                this.updateBuffer();
                            }
                            return out;
                        }
                    }
                    return null;
                }
                if (!keepEscape) {
                    return null;
                }
            }
            else if (this.ch == escape && !keepEscape) {
                if (i + 1 < this.length) {
                    final char next = this.buffer[i + 1];
                    if (next == quote || next == escapeEscape) {
                        return null;
                    }
                }
            }
            else if (this.lineSeparator1 == this.ch && this.normalizeLineEndings && (this.lineSeparator2 == '\0' || (i + 1 < this.length && this.lineSeparator2 == this.buffer[i + 1]))) {
                return null;
            }
        }
        return null;
    }
    
    @Override
    public final boolean skipQuotedString(final char quote, final char escape, final char stop1, final char stop2) {
        if (this.i == 0) {
            return false;
        }
        for (int i = this.i; i < this.length; ++i) {
            this.ch = this.buffer[i];
            if (this.ch == quote) {
                if (this.buffer[i - 1] != escape) {
                    if (i + 1 < this.length) {
                        final char next = this.buffer[i + 1];
                        if (next == stop1 || next == stop2) {
                            this.i = i + 1;
                            if (this.i >= this.length) {
                                this.updateBuffer();
                            }
                            return true;
                        }
                    }
                    return false;
                }
            }
            else if (this.lineSeparator1 == this.ch && this.normalizeLineEndings && (this.lineSeparator2 == '\0' || (i + 1 < this.length && this.lineSeparator2 == this.buffer[i + 1]))) {
                return false;
            }
        }
        return false;
    }
}
