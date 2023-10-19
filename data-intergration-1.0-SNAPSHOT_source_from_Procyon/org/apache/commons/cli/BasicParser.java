// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.cli;

@Deprecated
public class BasicParser extends Parser
{
    @Override
    protected String[] flatten(final Options options, final String[] arguments, final boolean stopAtNonOption) {
        return arguments;
    }
}
