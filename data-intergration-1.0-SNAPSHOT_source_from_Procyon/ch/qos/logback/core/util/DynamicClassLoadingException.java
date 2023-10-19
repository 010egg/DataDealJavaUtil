// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

public class DynamicClassLoadingException extends Exception
{
    private static final long serialVersionUID = 4962278449162476114L;
    
    public DynamicClassLoadingException(final String desc, final Throwable root) {
        super(desc, root);
    }
}
