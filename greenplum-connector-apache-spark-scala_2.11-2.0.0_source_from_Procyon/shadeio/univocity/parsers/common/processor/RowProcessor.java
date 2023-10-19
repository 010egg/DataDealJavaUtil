// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.Processor;

public interface RowProcessor extends Processor<ParsingContext>
{
    void processStarted(final ParsingContext p0);
    
    void rowProcessed(final String[] p0, final ParsingContext p1);
    
    void processEnded(final ParsingContext p0);
}
