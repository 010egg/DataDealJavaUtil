// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.hostchooser;

import org.postgresql.util.HostSpec;
import java.util.Iterator;

public interface HostChooser
{
    Iterator<HostSpec> iterator();
}
