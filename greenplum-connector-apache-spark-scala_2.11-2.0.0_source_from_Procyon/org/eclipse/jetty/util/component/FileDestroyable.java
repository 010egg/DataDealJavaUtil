// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.component;

import org.eclipse.jetty.util.log.Log;
import java.util.Iterator;
import org.eclipse.jetty.util.IO;
import java.util.Collection;
import java.io.IOException;
import org.eclipse.jetty.util.resource.Resource;
import java.util.ArrayList;
import java.io.File;
import java.util.List;
import org.eclipse.jetty.util.log.Logger;

public class FileDestroyable implements Destroyable
{
    private static final Logger LOG;
    final List<File> _files;
    
    public FileDestroyable() {
        this._files = new ArrayList<File>();
    }
    
    public FileDestroyable(final String file) throws IOException {
        (this._files = new ArrayList<File>()).add(Resource.newResource(file).getFile());
    }
    
    public FileDestroyable(final File file) {
        (this._files = new ArrayList<File>()).add(file);
    }
    
    public void addFile(final String file) throws IOException {
        try (final Resource r = Resource.newResource(file)) {
            this._files.add(r.getFile());
        }
    }
    
    public void addFile(final File file) {
        this._files.add(file);
    }
    
    public void addFiles(final Collection<File> files) {
        this._files.addAll(files);
    }
    
    public void removeFile(final String file) throws IOException {
        try (final Resource r = Resource.newResource(file)) {
            this._files.remove(r.getFile());
        }
    }
    
    public void removeFile(final File file) {
        this._files.remove(file);
    }
    
    @Override
    public void destroy() {
        for (final File file : this._files) {
            if (file.exists()) {
                if (FileDestroyable.LOG.isDebugEnabled()) {
                    FileDestroyable.LOG.debug("Destroy {}", file);
                }
                IO.delete(file);
            }
        }
    }
    
    static {
        LOG = Log.getLogger(FileDestroyable.class);
    }
}
