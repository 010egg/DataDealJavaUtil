// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class GT
{
    private static final GT _gt;
    private static final Object[] noargs;
    private ResourceBundle _bundle;
    
    public static String tr(final String message) {
        return GT._gt.translate(message, null);
    }
    
    public static String tr(final String message, final Object arg) {
        return GT._gt.translate(message, new Object[] { arg });
    }
    
    public static String tr(final String message, final Object[] args) {
        return GT._gt.translate(message, args);
    }
    
    private GT() {
        try {
            this._bundle = ResourceBundle.getBundle("org.postgresql.translation.messages");
        }
        catch (MissingResourceException mre) {
            this._bundle = null;
        }
    }
    
    private String translate(String message, Object[] args) {
        if (this._bundle != null && message != null) {
            try {
                message = this._bundle.getString(message);
            }
            catch (MissingResourceException ex) {}
        }
        if (args == null) {
            args = GT.noargs;
        }
        if (message != null) {
            message = MessageFormat.format(message, args);
        }
        return message;
    }
    
    static {
        _gt = new GT();
        noargs = new Object[0];
    }
}
