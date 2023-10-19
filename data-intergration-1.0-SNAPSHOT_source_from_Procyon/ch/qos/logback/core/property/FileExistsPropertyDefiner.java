// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.property;

import java.io.File;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.PropertyDefinerBase;

public class FileExistsPropertyDefiner extends PropertyDefinerBase
{
    String path;
    
    public String getPath() {
        return this.path;
    }
    
    public void setPath(final String path) {
        this.path = path;
    }
    
    @Override
    public String getPropertyValue() {
        if (OptionHelper.isEmpty(this.path)) {
            this.addError("The \"path\" property must be set.");
            return null;
        }
        final File file = new File(this.path);
        return PropertyDefinerBase.booleanAsStr(file.exists());
    }
}
