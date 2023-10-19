// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public abstract class ObjectConversion<T> extends NullConversion<String, T>
{
    public ObjectConversion() {
        super(null, null);
    }
    
    public ObjectConversion(final T valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    @Override
    public T execute(final String input) {
        return super.execute(input);
    }
    
    @Override
    protected final T fromInput(final String input) {
        return this.fromString(input);
    }
    
    protected abstract T fromString(final String p0);
    
    @Override
    public String revert(final T input) {
        return super.revert(input);
    }
    
    @Override
    protected final String undo(final T input) {
        return String.valueOf(input);
    }
    
    public T getValueIfStringIsNull() {
        return this.getValueOnNullInput();
    }
    
    public String getValueIfObjectIsNull() {
        return this.getValueOnNullOutput();
    }
    
    public void setValueIfStringIsNull(final T valueIfStringIsNull) {
        this.setValueOnNullInput(valueIfStringIsNull);
    }
    
    public void setValueIfObjectIsNull(final String valueIfObjectIsNull) {
        this.setValueOnNullOutput(valueIfObjectIsNull);
    }
}
