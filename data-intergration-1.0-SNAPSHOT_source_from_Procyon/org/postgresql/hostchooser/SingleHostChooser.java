// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.hostchooser;

import java.util.Iterator;
import java.util.Collections;
import org.postgresql.util.HostSpec;
import java.util.Collection;

public class SingleHostChooser implements HostChooser
{
    private final Collection<HostSpec> hostSpec;
    
    public SingleHostChooser(final HostSpec hostSpec) {
        this.hostSpec = Collections.singletonList(hostSpec);
    }
    
    @Override
    public Iterator<HostSpec> iterator() {
        return this.hostSpec.iterator();
    }
}
