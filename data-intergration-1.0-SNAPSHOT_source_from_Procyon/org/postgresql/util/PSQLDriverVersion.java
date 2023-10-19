// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.net.URL;
import org.postgresql.Driver;

public class PSQLDriverVersion
{
    public static final int buildNumber = 1201;
    
    public static void main(final String[] args) {
        final URL url = Driver.class.getResource("/org/postgresql/Driver.class");
        System.out.println(Driver.getVersion());
        System.out.println("Found in: " + url);
    }
}
