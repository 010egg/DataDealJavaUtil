// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;

public class DefaultConfigLoadingStrategy implements ConfigLoadingStrategy
{
    @Override
    public Config parseApplicationConfig(final ConfigParseOptions parseOptions) {
        final ClassLoader loader = parseOptions.getClassLoader();
        if (loader == null) {
            throw new ConfigException.BugOrBroken("ClassLoader should have been set here; bug in ConfigFactory. (You can probably work around this bug by passing in a class loader or calling currentThread().setContextClassLoader() though.)");
        }
        int specified = 0;
        String resource = System.getProperty("config.resource");
        if (resource != null) {
            ++specified;
        }
        final String file = System.getProperty("config.file");
        if (file != null) {
            ++specified;
        }
        final String url = System.getProperty("config.url");
        if (url != null) {
            ++specified;
        }
        if (specified == 0) {
            return ConfigFactory.parseResourcesAnySyntax("application", parseOptions);
        }
        if (specified > 1) {
            throw new ConfigException.Generic("You set more than one of config.file='" + file + "', config.url='" + url + "', config.resource='" + resource + "'; don't know which one to use!");
        }
        final ConfigParseOptions overrideOptions = parseOptions.setAllowMissing(false);
        if (resource != null) {
            if (resource.startsWith("/")) {
                resource = resource.substring(1);
            }
            return ConfigFactory.parseResources(loader, resource, overrideOptions);
        }
        if (file != null) {
            return ConfigFactory.parseFile(new File(file), overrideOptions);
        }
        try {
            return ConfigFactory.parseURL(new URL(url), overrideOptions);
        }
        catch (MalformedURLException e) {
            throw new ConfigException.Generic("Bad URL in config.url system property: '" + url + "': " + e.getMessage(), e);
        }
    }
}
