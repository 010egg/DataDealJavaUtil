// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

public class PGJDBCMain
{
    public static void main(final String[] args) {
        PSQLDriverVersion.main(args);
        System.out.println("\nThe PgJDBC driver is not an executable Java program.\n\nYou must install it according to the JDBC driver installation instructions for your application / container / appserver, then use it by specifying a JDBC URL of the form \n    jdbc:postgresql://\nor using an application specific method.\n\nSee the PgJDBC documentation: http://jdbc.postgresql.org/documentation/head/index.html\n\nThis command has had no effect.\n");
        System.exit(1);
    }
}
