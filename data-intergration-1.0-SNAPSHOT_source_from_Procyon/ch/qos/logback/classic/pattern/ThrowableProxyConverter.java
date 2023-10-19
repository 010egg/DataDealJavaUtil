// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import java.util.Iterator;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import java.util.ArrayList;
import ch.qos.logback.core.Context;
import java.util.Map;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EventEvaluator;
import java.util.List;

public class ThrowableProxyConverter extends ThrowableHandlingConverter
{
    protected static final int BUILDER_CAPACITY = 2048;
    int lengthOption;
    List<EventEvaluator<ILoggingEvent>> evaluatorList;
    List<String> ignoredStackTraceLines;
    int errorCount;
    
    public ThrowableProxyConverter() {
        this.evaluatorList = null;
        this.ignoredStackTraceLines = null;
        this.errorCount = 0;
    }
    
    @Override
    public void start() {
        String lengthStr = this.getFirstOption();
        if (lengthStr == null) {
            this.lengthOption = Integer.MAX_VALUE;
        }
        else {
            lengthStr = lengthStr.toLowerCase();
            if ("full".equals(lengthStr)) {
                this.lengthOption = Integer.MAX_VALUE;
            }
            else if ("short".equals(lengthStr)) {
                this.lengthOption = 1;
            }
            else {
                try {
                    this.lengthOption = Integer.parseInt(lengthStr);
                }
                catch (NumberFormatException ex) {
                    this.addError("Could not parse [" + lengthStr + "] as an integer");
                    this.lengthOption = Integer.MAX_VALUE;
                }
            }
        }
        final List<String> optionList = this.getOptionList();
        if (optionList != null && optionList.size() > 1) {
            for (int optionListSize = optionList.size(), i = 1; i < optionListSize; ++i) {
                final String evaluatorOrIgnoredStackTraceLine = optionList.get(i);
                final Context context = this.getContext();
                final Map<String, EventEvaluator<?>> evaluatorMap = (Map<String, EventEvaluator<?>>)context.getObject("EVALUATOR_MAP");
                final EventEvaluator<ILoggingEvent> ee = evaluatorMap.get(evaluatorOrIgnoredStackTraceLine);
                if (ee != null) {
                    this.addEvaluator(ee);
                }
                else {
                    this.addIgnoreStackTraceLine(evaluatorOrIgnoredStackTraceLine);
                }
            }
        }
        super.start();
    }
    
    private void addEvaluator(final EventEvaluator<ILoggingEvent> ee) {
        if (this.evaluatorList == null) {
            this.evaluatorList = new ArrayList<EventEvaluator<ILoggingEvent>>();
        }
        this.evaluatorList.add(ee);
    }
    
    private void addIgnoreStackTraceLine(final String ignoredStackTraceLine) {
        if (this.ignoredStackTraceLines == null) {
            this.ignoredStackTraceLines = new ArrayList<String>();
        }
        this.ignoredStackTraceLines.add(ignoredStackTraceLine);
    }
    
    @Override
    public void stop() {
        this.evaluatorList = null;
        super.stop();
    }
    
    protected void extraData(final StringBuilder builder, final StackTraceElementProxy step) {
    }
    
    @Override
    public String convert(final ILoggingEvent event) {
        final IThrowableProxy tp = event.getThrowableProxy();
        if (tp == null) {
            return "";
        }
        if (this.evaluatorList != null) {
            boolean printStack = true;
            for (int i = 0; i < this.evaluatorList.size(); ++i) {
                final EventEvaluator<ILoggingEvent> ee = this.evaluatorList.get(i);
                try {
                    if (ee.evaluate(event)) {
                        printStack = false;
                        break;
                    }
                }
                catch (EvaluationException eex) {
                    ++this.errorCount;
                    if (this.errorCount < 4) {
                        this.addError("Exception thrown for evaluator named [" + ee.getName() + "]", eex);
                    }
                    else if (this.errorCount == 4) {
                        final ErrorStatus errorStatus = new ErrorStatus("Exception thrown for evaluator named [" + ee.getName() + "].", this, eex);
                        errorStatus.add(new ErrorStatus("This was the last warning about this evaluator's errors.We don't want the StatusManager to get flooded.", this));
                        this.addStatus(errorStatus);
                    }
                }
            }
            if (!printStack) {
                return "";
            }
        }
        return this.throwableProxyToString(tp);
    }
    
    protected String throwableProxyToString(final IThrowableProxy tp) {
        final StringBuilder sb = new StringBuilder(2048);
        this.recursiveAppend(sb, null, 1, tp);
        return sb.toString();
    }
    
    private void recursiveAppend(final StringBuilder sb, final String prefix, final int indent, final IThrowableProxy tp) {
        if (tp == null) {
            return;
        }
        this.subjoinFirstLine(sb, prefix, indent, tp);
        sb.append(CoreConstants.LINE_SEPARATOR);
        this.subjoinSTEPArray(sb, indent, tp);
        final IThrowableProxy[] suppressed = tp.getSuppressed();
        if (suppressed != null) {
            IThrowableProxy[] array;
            for (int length = (array = suppressed).length, i = 0; i < length; ++i) {
                final IThrowableProxy current = array[i];
                this.recursiveAppend(sb, "Suppressed: ", indent + 1, current);
            }
        }
        this.recursiveAppend(sb, "Caused by: ", indent, tp.getCause());
    }
    
    private void subjoinFirstLine(final StringBuilder buf, final String prefix, final int indent, final IThrowableProxy tp) {
        ThrowableProxyUtil.indent(buf, indent - 1);
        if (prefix != null) {
            buf.append(prefix);
        }
        this.subjoinExceptionMessage(buf, tp);
    }
    
    private void subjoinExceptionMessage(final StringBuilder buf, final IThrowableProxy tp) {
        buf.append(tp.getClassName()).append(": ").append(tp.getMessage());
    }
    
    protected void subjoinSTEPArray(final StringBuilder buf, final int indent, final IThrowableProxy tp) {
        final StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
        final int commonFrames = tp.getCommonFrames();
        final boolean unrestrictedPrinting = this.lengthOption > stepArray.length;
        int maxIndex = unrestrictedPrinting ? stepArray.length : this.lengthOption;
        if (commonFrames > 0 && unrestrictedPrinting) {
            maxIndex -= commonFrames;
        }
        int ignoredCount = 0;
        for (final StackTraceElementProxy element : stepArray) {
            if (!this.isIgnoredStackTraceLine(element.toString())) {
                ThrowableProxyUtil.indent(buf, indent);
                this.printStackLine(buf, ignoredCount, element);
                ignoredCount = 0;
                buf.append(CoreConstants.LINE_SEPARATOR);
            }
            else {
                ++ignoredCount;
                if (maxIndex < stepArray.length) {
                    ++maxIndex;
                }
            }
        }
        if (ignoredCount > 0) {
            this.printIgnoredCount(buf, ignoredCount);
            buf.append(CoreConstants.LINE_SEPARATOR);
        }
        if (commonFrames > 0 && unrestrictedPrinting) {
            ThrowableProxyUtil.indent(buf, indent);
            buf.append("... ").append(tp.getCommonFrames()).append(" common frames omitted").append(CoreConstants.LINE_SEPARATOR);
        }
    }
    
    private void printStackLine(final StringBuilder buf, final int ignoredCount, final StackTraceElementProxy element) {
        buf.append(element);
        this.extraData(buf, element);
        if (ignoredCount > 0) {
            this.printIgnoredCount(buf, ignoredCount);
        }
    }
    
    private void printIgnoredCount(final StringBuilder buf, final int ignoredCount) {
        buf.append(" [").append(ignoredCount).append(" skipped]");
    }
    
    private boolean isIgnoredStackTraceLine(final String line) {
        if (this.ignoredStackTraceLines != null) {
            for (final String ignoredStackTraceLine : this.ignoredStackTraceLines) {
                if (line.contains(ignoredStackTraceLine)) {
                    return true;
                }
            }
        }
        return false;
    }
}
