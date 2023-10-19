// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.processor.core.AbstractObjectListProcessor;
import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractMasterDetailProcessor;

public abstract class MasterDetailProcessor extends AbstractMasterDetailProcessor<ParsingContext>
{
    public MasterDetailProcessor(final RowPlacement rowPlacement, final ObjectRowListProcessor detailProcessor) {
        super(rowPlacement, detailProcessor);
    }
    
    public MasterDetailProcessor(final ObjectRowListProcessor detailProcessor) {
        super(RowPlacement.TOP, detailProcessor);
    }
}
