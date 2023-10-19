// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public class ToStringConversion extends NullConversion<Object, Object>
{
    public ToStringConversion() {
    }
    
    public ToStringConversion(final Object valueOnNullInput, final Object valueOnNullOutput) {
        super(valueOnNullInput, valueOnNullOutput);
    }
    
    @Override
    protected Object fromInput(final Object input) {
        if (input != null) {
            return input.toString();
        }
        return null;
    }
    
    @Override
    protected Object undo(final Object input) {
        return this.execute(input);
    }
}
