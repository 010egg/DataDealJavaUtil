// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public abstract class NullConversion<I, O> implements Conversion<I, O>
{
    private O valueOnNullInput;
    private I valueOnNullOutput;
    
    public NullConversion() {
        this(null, null);
    }
    
    public NullConversion(final O valueOnNullInput, final I valueOnNullOutput) {
        this.valueOnNullInput = valueOnNullInput;
        this.valueOnNullOutput = valueOnNullOutput;
    }
    
    @Override
    public O execute(final I input) {
        if (input == null) {
            return this.valueOnNullInput;
        }
        return this.fromInput(input);
    }
    
    protected abstract O fromInput(final I p0);
    
    @Override
    public I revert(final O input) {
        if (input == null) {
            return this.valueOnNullOutput;
        }
        return this.undo(input);
    }
    
    protected abstract I undo(final O p0);
    
    public O getValueOnNullInput() {
        return this.valueOnNullInput;
    }
    
    public I getValueOnNullOutput() {
        return this.valueOnNullOutput;
    }
    
    public void setValueOnNullInput(final O valueOnNullInput) {
        this.valueOnNullInput = valueOnNullInput;
    }
    
    public void setValueOnNullOutput(final I valueOnNullOutput) {
        this.valueOnNullOutput = valueOnNullOutput;
    }
}
