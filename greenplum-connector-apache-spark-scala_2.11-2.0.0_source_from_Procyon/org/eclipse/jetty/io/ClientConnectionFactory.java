// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.util.Map;

public interface ClientConnectionFactory
{
    Connection newConnection(final EndPoint p0, final Map<String, Object> p1) throws IOException;
    
    public static class Helper
    {
        private static Logger LOG;
        
        private Helper() {
        }
        
        public static void replaceConnection(final Connection oldConnection, final Connection newConnection) {
            close(oldConnection);
            oldConnection.getEndPoint().setConnection(newConnection);
            open(newConnection);
        }
        
        private static void open(final Connection connection) {
            try {
                connection.onOpen();
            }
            catch (Throwable x) {
                Helper.LOG.debug(x);
            }
        }
        
        private static void close(final Connection connection) {
            try {
                connection.onClose();
            }
            catch (Throwable x) {
                Helper.LOG.debug(x);
            }
        }
        
        static {
            Helper.LOG = Log.getLogger(Helper.class);
        }
    }
}
