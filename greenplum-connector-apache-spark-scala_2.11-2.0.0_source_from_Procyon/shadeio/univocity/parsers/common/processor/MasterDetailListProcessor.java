// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.processor.core.AbstractObjectListProcessor;
import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractMasterDetailListProcessor;

public abstract class MasterDetailListProcessor extends AbstractMasterDetailListProcessor<ParsingContext> implements RowProcessor
{
    public MasterDetailListProcessor(final RowPlacement rowPlacement, final AbstractObjectListProcessor detailProcessor) {
        super(rowPlacement, detailProcessor);
    }
    
    public MasterDetailListProcessor(final AbstractObjectListProcessor detailProcessor) {
        super(detailProcessor);
    }
}
