// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import java.util.Iterator;
import org.apache.logging.log4j.core.LoggerContext;
import java.io.File;
import java.util.List;
import org.apache.logging.log4j.core.util.FileWatcher;

public class ConfiguratonFileWatcher implements FileWatcher
{
    private final Reconfigurable reconfigurable;
    private final List<ConfigurationListener> configurationListeners;
    
    public ConfiguratonFileWatcher(final Reconfigurable reconfigurable, final List<ConfigurationListener> configurationListeners) {
        this.reconfigurable = reconfigurable;
        this.configurationListeners = configurationListeners;
    }
    
    public List<ConfigurationListener> getListeners() {
        return this.configurationListeners;
    }
    
    @Override
    public void fileModified(final File file) {
        for (final ConfigurationListener configurationListener : this.configurationListeners) {
            LoggerContext.getContext(false).submitDaemon(new ReconfigurationRunnable(configurationListener, this.reconfigurable));
        }
    }
    
    private static class ReconfigurationRunnable implements Runnable
    {
        private final ConfigurationListener configurationListener;
        private final Reconfigurable reconfigurable;
        
        public ReconfigurationRunnable(final ConfigurationListener configurationListener, final Reconfigurable reconfigurable) {
            this.configurationListener = configurationListener;
            this.reconfigurable = reconfigurable;
        }
        
        @Override
        public void run() {
            this.configurationListener.onChange(this.reconfigurable);
        }
    }
}
