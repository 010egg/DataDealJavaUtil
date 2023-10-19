// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.util;

import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.naming.Context;

public class JNDIUtil
{
    public static Context getInitialContext() throws NamingException {
        return new InitialContext();
    }
    
    public static String lookup(final Context ctx, final String name) {
        if (ctx == null) {
            return null;
        }
        try {
            final Object lookup = ctx.lookup(name);
            return (lookup == null) ? null : lookup.toString();
        }
        catch (NamingException ex) {
            return null;
        }
    }
}
