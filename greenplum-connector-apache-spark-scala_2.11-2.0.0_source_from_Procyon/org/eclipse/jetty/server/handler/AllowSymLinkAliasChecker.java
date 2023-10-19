// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server.handler;

import org.eclipse.jetty.util.log.Log;
import java.util.Iterator;
import java.net.URI;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.LinkOption;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.log.Logger;

public class AllowSymLinkAliasChecker implements ContextHandler.AliasCheck
{
    private static final Logger LOG;
    
    @Override
    public boolean check(final String path, final Resource resource) {
        try {
            final File file = resource.getFile();
            if (file == null) {
                return false;
            }
            if (file.exists()) {
                final URI real = file.toPath().toRealPath(new LinkOption[0]).toUri();
                if (real.equals(resource.getAlias())) {
                    if (AllowSymLinkAliasChecker.LOG.isDebugEnabled()) {
                        AllowSymLinkAliasChecker.LOG.debug("Allow symlink {} --> {}", resource, real);
                    }
                    return true;
                }
            }
            else {
                final Path p = file.toPath().toAbsolutePath();
                File d = p.getRoot().toFile();
                for (final Path e : p) {
                    Path link;
                    for (d = new File(d, e.toString()); d.exists() && Files.isSymbolicLink(d.toPath()); d = link.toFile().getAbsoluteFile().getCanonicalFile()) {
                        link = Files.readSymbolicLink(d.toPath());
                        if (!link.isAbsolute()) {
                            link = link.resolve(d.toPath());
                        }
                    }
                }
                if (resource.getAlias().equals(d.toURI())) {
                    if (AllowSymLinkAliasChecker.LOG.isDebugEnabled()) {
                        AllowSymLinkAliasChecker.LOG.debug("Allow symlink {} --> {}", resource, d);
                    }
                    return true;
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            AllowSymLinkAliasChecker.LOG.ignore(e2);
        }
        return false;
    }
    
    static {
        LOG = Log.getLogger(AllowSymLinkAliasChecker.class);
    }
}
