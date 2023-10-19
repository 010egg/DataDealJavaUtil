// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.hostchooser;

import java.util.Iterator;

public interface HostChooser extends Iterable<CandidateHost>
{
    Iterator<CandidateHost> iterator();
}
