// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import java.io.IOException;
import java.util.Collections;
import org.eclipse.jetty.util.TypeUtil;
import java.util.Collection;
import java.net.URLClassLoader;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.util.component.Dumpable;

public class ClassLoaderDump implements Dumpable
{
    final ClassLoader _loader;
    
    public ClassLoaderDump(final ClassLoader loader) {
        this._loader = loader;
    }
    
    @Override
    public String dump() {
        return ContainerLifeCycle.dump(this);
    }
    
    @Override
    public void dump(final Appendable out, final String indent) throws IOException {
        if (this._loader == null) {
            out.append("No ClassLoader\n");
        }
        else {
            out.append(String.valueOf(this._loader)).append("\n");
            Object parent = this._loader.getParent();
            if (parent != null) {
                if (!(parent instanceof Dumpable)) {
                    parent = new ClassLoaderDump((ClassLoader)parent);
                }
                if (this._loader instanceof URLClassLoader) {
                    ContainerLifeCycle.dump(out, indent, TypeUtil.asList(((URLClassLoader)this._loader).getURLs()), Collections.singleton(parent));
                }
                else {
                    ContainerLifeCycle.dump(out, indent, Collections.singleton(parent));
                }
            }
        }
    }
}
