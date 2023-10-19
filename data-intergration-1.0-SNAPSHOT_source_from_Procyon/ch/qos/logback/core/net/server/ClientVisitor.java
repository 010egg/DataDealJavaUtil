// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

public interface ClientVisitor<T extends Client>
{
    void visit(final T p0);
}
