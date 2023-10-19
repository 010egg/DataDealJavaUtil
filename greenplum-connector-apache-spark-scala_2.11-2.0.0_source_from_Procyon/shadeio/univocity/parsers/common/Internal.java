// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.Arrays;
import shadeio.univocity.parsers.common.processor.core.Processor;

class Internal
{
    public static final <C extends Context> void process(final String[] row, final Processor<C> processor, final C context, final ProcessorErrorHandler<C> errorHandler) {
        try {
            processor.rowProcessed(row, context);
        }
        catch (DataProcessingException ex) {
            ex.setContext(context);
            if (!ex.isFatal() && !ex.isHandled() && ex.getColumnIndex() > -1 && errorHandler instanceof RetryableErrorHandler) {
                final RetryableErrorHandler retry = (RetryableErrorHandler)errorHandler;
                ex.markAsHandled(errorHandler);
                retry.handleError(ex, row, context);
                if (!retry.isRecordSkipped()) {
                    try {
                        processor.rowProcessed(row, context);
                        return;
                    }
                    catch (DataProcessingException e) {
                        ex = e;
                    }
                    catch (Throwable t) {
                        throwDataProcessingException(processor, t, row, context.errorContentLength());
                    }
                }
            }
            ex.setErrorContentLength(context.errorContentLength());
            if (ex.isFatal()) {
                throw ex;
            }
            ex.markAsHandled(errorHandler);
            errorHandler.handleError(ex, row, context);
        }
        catch (Throwable t2) {
            throwDataProcessingException(processor, t2, row, context.errorContentLength());
        }
    }
    
    private static final void throwDataProcessingException(final Processor processor, final Throwable t, final String[] row, final int errorContentLength) throws DataProcessingException {
        final DataProcessingException ex = new DataProcessingException("Unexpected error processing input row " + AbstractException.restrictContent(errorContentLength, Arrays.toString(row)) + " using Processor " + processor.getClass().getName() + '.', AbstractException.restrictContent(errorContentLength, row), t);
        ex.restrictContent(errorContentLength);
        throw ex;
    }
}
