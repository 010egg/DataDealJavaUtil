// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.tsv;

import shadeio.univocity.parsers.common.Format;
import shadeio.univocity.parsers.common.CommonSettings;
import java.util.Map;
import shadeio.univocity.parsers.common.CommonWriterSettings;

public class TsvWriterSettings extends CommonWriterSettings<TsvFormat>
{
    private boolean lineJoiningEnabled;
    
    public TsvWriterSettings() {
        this.lineJoiningEnabled = false;
    }
    
    public boolean isLineJoiningEnabled() {
        return this.lineJoiningEnabled;
    }
    
    public void setLineJoiningEnabled(final boolean lineJoiningEnabled) {
        this.lineJoiningEnabled = lineJoiningEnabled;
    }
    
    @Override
    protected TsvFormat createDefaultFormat() {
        return new TsvFormat();
    }
    
    @Override
    protected void addConfiguration(final Map<String, Object> out) {
        super.addConfiguration(out);
    }
    
    public final TsvWriterSettings clone() {
        return (TsvWriterSettings)super.clone();
    }
    
    public final TsvWriterSettings clone(final boolean clearInputSpecificSettings) {
        return (TsvWriterSettings)super.clone(clearInputSpecificSettings);
    }
}
