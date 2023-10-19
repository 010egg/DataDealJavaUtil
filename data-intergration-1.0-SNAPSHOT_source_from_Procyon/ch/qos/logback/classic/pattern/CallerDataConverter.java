// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.ErrorStatus;
import java.util.ArrayList;
import java.util.regex.Pattern;
import ch.qos.logback.core.Context;
import java.util.Map;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EventEvaluator;
import java.util.List;

public class CallerDataConverter extends ClassicConverter
{
    public static final String DEFAULT_CALLER_LINE_PREFIX = "Caller+";
    public static final String DEFAULT_RANGE_DELIMITER = "..";
    private int depthStart;
    private int depthEnd;
    List<EventEvaluator<ILoggingEvent>> evaluatorList;
    final int MAX_ERROR_COUNT = 4;
    int errorCount;
    
    public CallerDataConverter() {
        this.depthStart = 0;
        this.depthEnd = 5;
        this.evaluatorList = null;
        this.errorCount = 0;
    }
    
    @Override
    public void start() {
        final String depthStr = this.getFirstOption();
        if (depthStr == null) {
            return;
        }
        try {
            if (this.isRange(depthStr)) {
                final String[] numbers = this.splitRange(depthStr);
                if (numbers.length == 2) {
                    this.depthStart = Integer.parseInt(numbers[0]);
                    this.depthEnd = Integer.parseInt(numbers[1]);
                    this.checkRange();
                }
                else {
                    this.addError("Failed to parse depth option as range [" + depthStr + "]");
                }
            }
            else {
                this.depthEnd = Integer.parseInt(depthStr);
            }
        }
        catch (NumberFormatException nfe) {
            this.addError("Failed to parse depth option [" + depthStr + "]", nfe);
        }
        final List<String> optionList = this.getOptionList();
        if (optionList != null && optionList.size() > 1) {
            for (int optionListSize = optionList.size(), i = 1; i < optionListSize; ++i) {
                final String evaluatorStr = optionList.get(i);
                final Context context = this.getContext();
                if (context != null) {
                    final Map<String, EventEvaluator<?>> evaluatorMap = (Map<String, EventEvaluator<?>>)context.getObject("EVALUATOR_MAP");
                    final EventEvaluator<ILoggingEvent> ee = evaluatorMap.get(evaluatorStr);
                    if (ee != null) {
                        this.addEvaluator(ee);
                    }
                }
            }
        }
    }
    
    private boolean isRange(final String depthStr) {
        return depthStr.contains(this.getDefaultRangeDelimiter());
    }
    
    private String[] splitRange(final String depthStr) {
        return depthStr.split(Pattern.quote(this.getDefaultRangeDelimiter()), 2);
    }
    
    private void checkRange() {
        if (this.depthStart < 0 || this.depthEnd < 0) {
            this.addError("Invalid depthStart/depthEnd range [" + this.depthStart + ", " + this.depthEnd + "] (negative values are not allowed)");
        }
        else if (this.depthStart >= this.depthEnd) {
            this.addError("Invalid depthEnd range [" + this.depthStart + ", " + this.depthEnd + "] (start greater or equal to end)");
        }
    }
    
    private void addEvaluator(final EventEvaluator<ILoggingEvent> ee) {
        if (this.evaluatorList == null) {
            this.evaluatorList = new ArrayList<EventEvaluator<ILoggingEvent>>();
        }
        this.evaluatorList.add(ee);
    }
    
    @Override
    public String convert(final ILoggingEvent le) {
        final StringBuilder buf = new StringBuilder();
        if (this.evaluatorList != null) {
            boolean printCallerData = false;
            for (int i = 0; i < this.evaluatorList.size(); ++i) {
                final EventEvaluator<ILoggingEvent> ee = this.evaluatorList.get(i);
                try {
                    if (ee.evaluate(le)) {
                        printCallerData = true;
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
            if (!printCallerData) {
                return "";
            }
        }
        final StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > this.depthStart) {
            for (int limit = (this.depthEnd < cda.length) ? this.depthEnd : cda.length, j = this.depthStart; j < limit; ++j) {
                buf.append(this.getCallerLinePrefix());
                buf.append(j);
                buf.append("\t at ");
                buf.append(cda[j]);
                buf.append(CoreConstants.LINE_SEPARATOR);
            }
            return buf.toString();
        }
        return CallerData.CALLER_DATA_NA;
    }
    
    protected String getCallerLinePrefix() {
        return "Caller+";
    }
    
    protected String getDefaultRangeDelimiter() {
        return "..";
    }
}
