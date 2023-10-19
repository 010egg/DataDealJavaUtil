// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.spi;

import java.util.List;
import ch.qos.logback.core.joran.action.Action;

public interface RuleStore
{
    void addRule(final ElementSelector p0, final String p1) throws ClassNotFoundException;
    
    void addRule(final ElementSelector p0, final Action p1);
    
    List<Action> matchActions(final ElementPath p0);
}
