// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.record;

import shadeio.univocity.parsers.conversions.Conversion;

class MetaData
{
    public final int index;
    public Class<?> type;
    public Object defaultValue;
    private Conversion[] conversions;
    
    MetaData(final int index) {
        this.type = String.class;
        this.defaultValue = null;
        this.conversions = null;
        this.index = index;
    }
    
    public Conversion[] getConversions() {
        return this.conversions;
    }
    
    public void setDefaultConversions(final Conversion[] conversions) {
        this.conversions = conversions;
    }
    
    public Object convert(Object out) {
        if (this.conversions == null) {
            return out;
        }
        for (int i = 0; i < this.conversions.length; ++i) {
            out = this.conversions[i].execute(out);
        }
        return out;
    }
}
