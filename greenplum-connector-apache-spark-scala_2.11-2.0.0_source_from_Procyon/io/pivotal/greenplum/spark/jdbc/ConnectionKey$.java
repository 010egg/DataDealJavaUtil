// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.jdbc;

import java.util.Random;
import java.math.BigInteger;
import scala.math.package$;
import java.security.SecureRandom;
import scala.Some;
import scala.None$;
import scala.Tuple3;
import scala.Option;
import org.apache.commons.codec.digest.Crypt;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.Serializable;

public final class ConnectionKey$ implements Serializable
{
    public static final ConnectionKey$ MODULE$;
    private final String salt;
    
    static {
        new ConnectionKey$();
    }
    
    private String salt() {
        return this.salt;
    }
    
    public ConnectionKey valueOf(final String user, final String url, final String password) {
        return this.apply(user, url, this.hashPassword(password));
    }
    
    public ConnectionKey valueOf(final String user, final String url) {
        return this.apply(user, url, this.hashPassword(""));
    }
    
    public String hashPassword(final String pass) {
        return Crypt.crypt(pass, new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "$6$", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.salt() })));
    }
    
    public ConnectionKey apply(final String jdbcUrl, final String userName, final String hashedPassword) {
        return new ConnectionKey(jdbcUrl, userName, hashedPassword);
    }
    
    public Option<Tuple3<String, String, String>> unapply(final ConnectionKey x$0) {
        return (Option<Tuple3<String, String, String>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.jdbcUrl(), (Object)x$0.userName(), (Object)x$0.hashedPassword())));
    }
    
    private Object readResolve() {
        return ConnectionKey$.MODULE$;
    }
    
    private ConnectionKey$() {
        MODULE$ = this;
        final SecureRandom sr = new SecureRandom();
        final int saltLength = 16;
        final int encodingBaseExponent = 4;
        final int radix = (int)package$.MODULE$.pow(2.0, (double)encodingBaseExponent);
        this.salt = new BigInteger(encodingBaseExponent * saltLength, sr).toString(radix);
    }
}
