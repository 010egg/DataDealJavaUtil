// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public interface Conversion<I, O>
{
    O execute(final I p0);
    
    I revert(final O p0);
}
