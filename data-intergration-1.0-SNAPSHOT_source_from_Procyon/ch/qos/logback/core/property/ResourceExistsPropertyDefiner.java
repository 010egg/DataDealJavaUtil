// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.property;

import java.net.URL;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.PropertyDefinerBase;

public class ResourceExistsPropertyDefiner extends PropertyDefinerBase
{
    String resourceStr;
    
    public String getResource() {
        return this.resourceStr;
    }
    
    public void setResource(final String resource) {
        this.resourceStr = resource;
    }
    
    @Override
    public String getPropertyValue() {
        if (OptionHelper.isEmpty(this.resourceStr)) {
            this.addError("The \"resource\" property must be set.");
            return null;
        }
        final URL resourceURL = Loader.getResourceBySelfClassLoader(this.resourceStr);
        return PropertyDefinerBase.booleanAsStr(resourceURL != null);
    }
}
