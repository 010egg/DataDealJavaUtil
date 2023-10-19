// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ssl.jdbc4;

import java.net.Socket;
import javax.net.ssl.HostnameVerifier;
import java.io.IOException;
import javax.net.ssl.SSLSocket;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import javax.net.ssl.SSLSocketFactory;
import org.postgresql.PGProperty;
import org.postgresql.core.Logger;
import org.postgresql.core.PGStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class AbstractJdbc4MakeSSL
{
    public static Object instantiate(final String classname, final Properties info, boolean tryString, final String stringarg) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object[] args = { info };
        Constructor ctor = null;
        final Class cls = Class.forName(classname);
        try {
            ctor = cls.getConstructor(Properties.class);
        }
        catch (NoSuchMethodException nsme) {
            if (tryString) {
                try {
                    ctor = cls.getConstructor(String.class);
                    args = new String[] { stringarg };
                }
                catch (NoSuchMethodException nsme2) {
                    tryString = false;
                }
            }
            if (!tryString) {
                ctor = cls.getConstructor((Class[])null);
                args = null;
            }
        }
        return ctor.newInstance(args);
    }
    
    public static void convert(final PGStream stream, final Properties info, final Logger logger) throws PSQLException, IOException {
        logger.debug("converting regular socket connection to ssl");
        final String sslmode = PGProperty.SSL_MODE.get(info);
        final String classname = PGProperty.SSL_FACTORY.get(info);
        SSLSocketFactory factory;
        if (classname == null) {
            if (sslmode != null) {
                factory = new LibPQFactory(info);
            }
            else {
                factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            }
        }
        else {
            try {
                factory = (SSLSocketFactory)instantiate(classname, info, true, PGProperty.SSL_FACTORY_ARG.get(info));
            }
            catch (Exception e) {
                throw new PSQLException(GT.tr("The SSLSocketFactory class provided {0} could not be instantiated.", classname), PSQLState.CONNECTION_FAILURE, e);
            }
        }
        SSLSocket newConnection;
        try {
            newConnection = (SSLSocket)factory.createSocket(stream.getSocket(), stream.getHostSpec().getHost(), stream.getHostSpec().getPort(), true);
            newConnection.startHandshake();
        }
        catch (IOException ex) {
            if (factory instanceof LibPQFactory) {
                ((LibPQFactory)factory).throwKeyManagerException();
            }
            throw new PSQLException(GT.tr("SSL error: {0}", ex.getMessage()), PSQLState.CONNECTION_FAILURE, ex);
        }
        final String sslhostnameverifier = PGProperty.SSL_HOSTNAME_VERIFIER.get(info);
        if (sslhostnameverifier != null) {
            HostnameVerifier hvn;
            try {
                hvn = (HostnameVerifier)instantiate(sslhostnameverifier, info, false, null);
            }
            catch (Exception e2) {
                throw new PSQLException(GT.tr("The HostnameVerifier class provided {0} could not be instantiated.", sslhostnameverifier), PSQLState.CONNECTION_FAILURE, e2);
            }
            if (!hvn.verify(stream.getHostSpec().getHost(), newConnection.getSession())) {
                throw new PSQLException(GT.tr("The hostname {0} could not be verified by hostnameverifier {1}.", new Object[] { stream.getHostSpec().getHost(), sslhostnameverifier }), PSQLState.CONNECTION_FAILURE);
            }
        }
        else if ("verify-full".equals(sslmode) && factory instanceof LibPQFactory && !((LibPQFactory)factory).verify(stream.getHostSpec().getHost(), newConnection.getSession())) {
            throw new PSQLException(GT.tr("The hostname {0} could not be verified.", stream.getHostSpec().getHost()), PSQLState.CONNECTION_FAILURE);
        }
        stream.changeSocket(newConnection);
    }
}
