// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.tsv;

import shadeio.univocity.parsers.common.Format;
import shadeio.univocity.parsers.common.CommonSettings;
import java.util.Map;
import shadeio.univocity.parsers.common.CommonParserSettings;

public class TsvParserSettings extends CommonParserSettings<TsvFormat>
{
    private boolean lineJoiningEnabled;
    
    public TsvParserSettings() {
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
    
    public final TsvParserSettings clone() {
        return (TsvParserSettings)super.clone();
    }
    
    public final TsvParserSettings clone(final boolean clearInputSpecificSettings) {
        return (TsvParserSettings)super.clone(clearInputSpecificSettings);
    }
}
