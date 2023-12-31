// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.cli;

public class UnrecognizedOptionException extends ParseException
{
    private static final long serialVersionUID = -252504690284625623L;
    private String option;
    
    public UnrecognizedOptionException(final String message) {
        super(message);
    }
    
    public UnrecognizedOptionException(final String message, final String option) {
        this(message);
        this.option = option;
    }
    
    public String getOption() {
        return this.option;
    }
}
