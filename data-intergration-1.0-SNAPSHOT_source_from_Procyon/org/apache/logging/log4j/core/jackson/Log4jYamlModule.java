// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

final class Log4jYamlModule extends SimpleModule
{
    private static final long serialVersionUID = 1L;
    private final boolean encodeThreadContextAsList;
    private final boolean includeStacktrace;
    
    Log4jYamlModule(final boolean encodeThreadContextAsList, final boolean includeStacktrace) {
        super(Log4jYamlModule.class.getName(), new Version(2, 0, 0, null, null, null));
        this.encodeThreadContextAsList = encodeThreadContextAsList;
        this.includeStacktrace = includeStacktrace;
        new Initializers.SimpleModuleInitializer().initialize(this);
    }
    
    @Override
    public void setupModule(final SetupContext context) {
        super.setupModule(context);
        if (this.encodeThreadContextAsList) {
            new Initializers.SetupContextInitializer().setupModule(context, this.includeStacktrace);
        }
        else {
            new Initializers.SetupContextJsonInitializer().setupModule(context, this.includeStacktrace);
        }
    }
}
