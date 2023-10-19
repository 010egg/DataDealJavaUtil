// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001m3A!\u0001\u0002\u0001\u001b\ti1+\u001a:wKJ<&/\u00199qKJT!a\u0001\u0003\u0002\u001b\u0015DH/\u001a:oC2$\u0018M\u00197f\u0015\t)a!A\u0003ta\u0006\u00148N\u0003\u0002\b\u0011\u0005IqM]3f]BdW/\u001c\u0006\u0003\u0013)\tq\u0001]5w_R\fGNC\u0001\f\u0003\tIwn\u0001\u0001\u0014\u0005\u0001q\u0001CA\b\u0013\u001b\u0005\u0001\"\"A\t\u0002\u000bM\u001c\u0017\r\\1\n\u0005M\u0001\"AB!osJ+g\r\u0003\u0005\u0016\u0001\t\u0015\r\u0011\"\u0001\u0017\u0003\u0019\u0019XM\u001d<feV\tq\u0003\u0005\u0002\u0019A5\t\u0011D\u0003\u0002\u00165)\u00111\u0004H\u0001\u0006U\u0016$H/\u001f\u0006\u0003;y\tq!Z2mSB\u001cXMC\u0001 \u0003\ry'oZ\u0005\u0003Ce\u0011aaU3sm\u0016\u0014\b\u0002C\u0012\u0001\u0005\u0003\u0005\u000b\u0011B\f\u0002\u000fM,'O^3sA!)Q\u0005\u0001C\u0001M\u00051A(\u001b8jiz\"\"aJ\u0015\u0011\u0005!\u0002Q\"\u0001\u0002\t\u000bU!\u0003\u0019A\f\t\u000b-\u0002A\u0011\u0001\u0017\u0002\u000bM$\u0018M\u001d;\u0015\u00035\u0002\"a\u0004\u0018\n\u0005=\u0002\"\u0001B+oSRD3AK\u0019A!\ry!\u0007N\u0005\u0003gA\u0011a\u0001\u001e5s_^\u001c\bCA\u001b>\u001d\t14H\u0004\u00028u5\t\u0001H\u0003\u0002:\u0019\u00051AH]8pizJ\u0011!E\u0005\u0003yA\tq\u0001]1dW\u0006<W-\u0003\u0002?\u007f\tIQ\t_2faRLwN\u001c\u0006\u0003yA\u0019\u0013\u0001\u000e\u0005\u0006\u0005\u0002!\t\u0001L\u0001\u0005gR|\u0007\u000fC\u0003E\u0001\u0011\u0005Q)\u0001\u0005hKR\u001cF/\u0019;f+\u00051\u0005CA$K\u001d\ty\u0001*\u0003\u0002J!\u00051\u0001K]3eK\u001aL!a\u0013'\u0003\rM#(/\u001b8h\u0015\tI\u0005\u0003C\u0003O\u0001\u0011\u0005q*A\u0007hKR\u001cuN\u001c8fGR|'o]\u000b\u0002!B\u0019q\"U*\n\u0005I\u0003\"!B!se\u0006L\bC\u0001\rU\u0013\t)\u0016DA\u0005D_:tWm\u0019;pe\")q\u000b\u0001C\u00011\u0006i1/\u001a;D_:tWm\u0019;peN$\"!L-\t\u000bi3\u0006\u0019\u0001)\u0002\u0015\r|gN\\3di>\u00148\u000f")
public class ServerWrapper
{
    private final Server server;
    
    public Server server() {
        return this.server;
    }
    
    public void start() throws Exception {
        this.server().start();
    }
    
    public void stop() {
        this.server().stop();
    }
    
    public String getState() {
        return this.server().getState();
    }
    
    public Connector[] getConnectors() {
        return this.server().getConnectors();
    }
    
    public void setConnectors(final Connector[] connectors) {
        this.server().setConnectors(connectors);
    }
    
    public ServerWrapper(final Server server) {
        this.server = server;
    }
}
