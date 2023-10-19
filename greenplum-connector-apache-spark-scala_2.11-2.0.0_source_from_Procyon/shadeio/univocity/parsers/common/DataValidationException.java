// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

public class DataValidationException extends DataProcessingException
{
    private static final long serialVersionUID = 3110975527111918123L;
    
    public DataValidationException(final String message) {
        super(message, -1, null, null);
    }
    
    public DataValidationException(final String message, final Throwable cause) {
        super(message, -1, null, cause);
    }
    
    public DataValidationException(final String message, final Object[] row) {
        super(message, -1, row, null);
    }
    
    public DataValidationException(final String message, final Object[] row, final Throwable cause) {
        super(message, -1, row, cause);
    }
    
    public DataValidationException(final String message, final int columnIndex) {
        super(message, columnIndex, null, null);
    }
    
    @Override
    protected String getErrorDescription() {
        return "Error validating parsed input";
    }
}
