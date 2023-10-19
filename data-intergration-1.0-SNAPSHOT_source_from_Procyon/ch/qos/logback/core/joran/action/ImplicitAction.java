// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.InterpretationContext;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.ElementPath;

public abstract class ImplicitAction extends Action
{
    public abstract boolean isApplicable(final ElementPath p0, final Attributes p1, final InterpretationContext p2);
}
