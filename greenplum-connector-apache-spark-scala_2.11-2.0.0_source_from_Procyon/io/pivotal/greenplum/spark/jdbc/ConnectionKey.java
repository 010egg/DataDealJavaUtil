// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.jdbc;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Tuple3;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001dt!B\u0001\u0003\u0011\u0003i\u0011!D\"p]:,7\r^5p].+\u0017P\u0003\u0002\u0004\t\u0005!!\u000e\u001a2d\u0015\t)a!A\u0003ta\u0006\u00148N\u0003\u0002\b\u0011\u0005IqM]3f]BdW/\u001c\u0006\u0003\u0013)\tq\u0001]5w_R\fGNC\u0001\f\u0003\tIwn\u0001\u0001\u0011\u00059yQ\"\u0001\u0002\u0007\u000bA\u0011\u0001\u0012A\t\u0003\u001b\r{gN\\3di&|gnS3z'\ry!\u0003\u0007\t\u0003'Yi\u0011\u0001\u0006\u0006\u0002+\u0005)1oY1mC&\u0011q\u0003\u0006\u0002\u0007\u0003:L(+\u001a4\u0011\u0005MI\u0012B\u0001\u000e\u0015\u00051\u0019VM]5bY&T\u0018M\u00197f\u0011\u0015ar\u0002\"\u0001\u001e\u0003\u0019a\u0014N\\5u}Q\tQ\u0002C\u0004 \u001f\t\u0007I\u0011\u0002\u0011\u0002\tM\fG\u000e^\u000b\u0002CA\u0011!%\n\b\u0003'\rJ!\u0001\n\u000b\u0002\rA\u0013X\rZ3g\u0013\t1sE\u0001\u0004TiJLgn\u001a\u0006\u0003IQAa!K\b!\u0002\u0013\t\u0013!B:bYR\u0004\u0003\"B\u0016\u0010\t\u0003a\u0013a\u0002<bYV,wJ\u001a\u000b\b[\u0005m\u0011qDA\u0012!\tqaFB\u0003\u0011\u0005\u0001\u0013qf\u0005\u0003/%AB\u0002CA\n2\u0013\t\u0011DCA\u0004Qe>$Wo\u0019;\t\u0011Qr#Q3A\u0005\u0002\u0001\nqA\u001b3cGV\u0013H\u000e\u0003\u00057]\tE\t\u0015!\u0003\"\u0003!QGMY2Ve2\u0004\u0003\u0002\u0003\u001d/\u0005+\u0007I\u0011\u0001\u0011\u0002\u0011U\u001cXM\u001d(b[\u0016D\u0001B\u000f\u0018\u0003\u0012\u0003\u0006I!I\u0001\nkN,'OT1nK\u0002B\u0001\u0002\u0010\u0018\u0003\u0016\u0004%\t\u0001I\u0001\u000fQ\u0006\u001c\b.\u001a3QCN\u001cxo\u001c:e\u0011!qdF!E!\u0002\u0013\t\u0013a\u00045bg\",G\rU1tg^|'\u000f\u001a\u0011\t\u000bqqC\u0011\u0002!\u0015\t5\n%i\u0011\u0005\u0006i}\u0002\r!\t\u0005\u0006q}\u0002\r!\t\u0005\u0006y}\u0002\r!\t\u0005\b\u000b:\n\t\u0011\"\u0001G\u0003\u0011\u0019w\u000e]=\u0015\t5:\u0005*\u0013\u0005\bi\u0011\u0003\n\u00111\u0001\"\u0011\u001dAD\t%AA\u0002\u0005Bq\u0001\u0010#\u0011\u0002\u0003\u0007\u0011\u0005C\u0004L]E\u0005I\u0011\u0001'\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\tQJ\u000b\u0002\"\u001d.\nq\n\u0005\u0002Q+6\t\u0011K\u0003\u0002S'\u0006IQO\\2iK\u000e\\W\r\u001a\u0006\u0003)R\t!\"\u00198o_R\fG/[8o\u0013\t1\u0016KA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016Dq\u0001\u0017\u0018\u0012\u0002\u0013\u0005A*\u0001\bd_BLH\u0005Z3gCVdG\u000f\n\u001a\t\u000fis\u0013\u0013!C\u0001\u0019\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\u001a\u0004b\u0002//\u0003\u0003%\t%X\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003y\u0003\"a\u00183\u000e\u0003\u0001T!!\u00192\u0002\t1\fgn\u001a\u0006\u0002G\u0006!!.\u0019<b\u0013\t1\u0003\rC\u0004g]\u0005\u0005I\u0011A4\u0002\u0019A\u0014x\u000eZ;di\u0006\u0013\u0018\u000e^=\u0016\u0003!\u0004\"aE5\n\u0005)$\"aA%oi\"9ANLA\u0001\n\u0003i\u0017A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0003]F\u0004\"aE8\n\u0005A$\"aA!os\"9!o[A\u0001\u0002\u0004A\u0017a\u0001=%c!9AOLA\u0001\n\u0003*\u0018a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0003Y\u00042a\u001e>o\u001b\u0005A(BA=\u0015\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0003wb\u0014\u0001\"\u0013;fe\u0006$xN\u001d\u0005\b{:\n\t\u0011\"\u0001\u007f\u0003!\u0019\u0017M\\#rk\u0006dGcA@\u0002\u0006A\u00191#!\u0001\n\u0007\u0005\rACA\u0004C_>dW-\u00198\t\u000fId\u0018\u0011!a\u0001]\"I\u0011\u0011\u0002\u0018\u0002\u0002\u0013\u0005\u00131B\u0001\tQ\u0006\u001c\bnQ8eKR\t\u0001\u000eC\u0005\u0002\u00109\n\t\u0011\"\u0011\u0002\u0012\u0005AAo\\*ue&tw\rF\u0001_\u0011%\t)BLA\u0001\n\u0003\n9\"\u0001\u0004fcV\fGn\u001d\u000b\u0004\u007f\u0006e\u0001\u0002\u0003:\u0002\u0014\u0005\u0005\t\u0019\u00018\t\r\u0005u!\u00061\u0001\"\u0003\u0011)8/\u001a:\t\r\u0005\u0005\"\u00061\u0001\"\u0003\r)(\u000f\u001c\u0005\u0007\u0003KQ\u0003\u0019A\u0011\u0002\u0011A\f7o]<pe\u0012DaaK\b\u0005\u0002\u0005%B#B\u0017\u0002,\u00055\u0002bBA\u000f\u0003O\u0001\r!\t\u0005\b\u0003C\t9\u00031\u0001\"\u0011!\t\td\u0004C\u0001\u0005\u0005M\u0012\u0001\u00045bg\"\u0004\u0016m]:x_J$GcA\u0011\u00026!9\u0011qGA\u0018\u0001\u0004\t\u0013\u0001\u00029bgND\u0011\"a\u000f\u0010\u0003\u0003%\t)!\u0010\u0002\u000b\u0005\u0004\b\u000f\\=\u0015\u000f5\ny$!\u0011\u0002D!1A'!\u000fA\u0002\u0005Ba\u0001OA\u001d\u0001\u0004\t\u0003B\u0002\u001f\u0002:\u0001\u0007\u0011\u0005C\u0005\u0002H=\t\t\u0011\"!\u0002J\u00059QO\\1qa2LH\u0003BA&\u0003/\u0002RaEA'\u0003#J1!a\u0014\u0015\u0005\u0019y\u0005\u000f^5p]B11#a\u0015\"C\u0005J1!!\u0016\u0015\u0005\u0019!V\u000f\u001d7fg!I\u0011\u0011LA#\u0003\u0003\u0005\r!L\u0001\u0004q\u0012\u0002\u0004\"CA/\u001f\u0005\u0005I\u0011BA0\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005\u0005\u0004cA0\u0002d%\u0019\u0011Q\r1\u0003\r=\u0013'.Z2u\u0001")
public class ConnectionKey implements Product, Serializable
{
    private final String jdbcUrl;
    private final String userName;
    private final String hashedPassword;
    
    public static Option<Tuple3<String, String, String>> unapply(final ConnectionKey x$0) {
        return ConnectionKey$.MODULE$.unapply(x$0);
    }
    
    public static ConnectionKey apply(final String jdbcUrl, final String userName, final String hashedPassword) {
        return ConnectionKey$.MODULE$.apply(jdbcUrl, userName, hashedPassword);
    }
    
    public static ConnectionKey valueOf(final String user, final String url) {
        return ConnectionKey$.MODULE$.valueOf(user, url);
    }
    
    public static ConnectionKey valueOf(final String user, final String url, final String password) {
        return ConnectionKey$.MODULE$.valueOf(user, url, password);
    }
    
    public String jdbcUrl() {
        return this.jdbcUrl;
    }
    
    public String userName() {
        return this.userName;
    }
    
    public String hashedPassword() {
        return this.hashedPassword;
    }
    
    public ConnectionKey copy(final String jdbcUrl, final String userName, final String hashedPassword) {
        return new ConnectionKey(jdbcUrl, userName, hashedPassword);
    }
    
    public String copy$default$1() {
        return this.jdbcUrl();
    }
    
    public String copy$default$2() {
        return this.userName();
    }
    
    public String copy$default$3() {
        return this.hashedPassword();
    }
    
    public String productPrefix() {
        return "ConnectionKey";
    }
    
    public int productArity() {
        return 3;
    }
    
    public Object productElement(final int x$1) {
        String s = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 2: {
                s = this.hashedPassword();
                break;
            }
            case 1: {
                s = this.userName();
                break;
            }
            case 0: {
                s = this.jdbcUrl();
                break;
            }
        }
        return s;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ConnectionKey;
    }
    
    @Override
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ConnectionKey) {
                final ConnectionKey connectionKey = (ConnectionKey)x$1;
                final String jdbcUrl = this.jdbcUrl();
                final String jdbcUrl2 = connectionKey.jdbcUrl();
                boolean b = false;
                Label_0141: {
                    Label_0140: {
                        if (jdbcUrl == null) {
                            if (jdbcUrl2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!jdbcUrl.equals(jdbcUrl2)) {
                            break Label_0140;
                        }
                        final String userName = this.userName();
                        final String userName2 = connectionKey.userName();
                        if (userName == null) {
                            if (userName2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!userName.equals(userName2)) {
                            break Label_0140;
                        }
                        final String hashedPassword = this.hashedPassword();
                        final String hashedPassword2 = connectionKey.hashedPassword();
                        if (hashedPassword == null) {
                            if (hashedPassword2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!hashedPassword.equals(hashedPassword2)) {
                            break Label_0140;
                        }
                        if (connectionKey.canEqual(this)) {
                            b = true;
                            break Label_0141;
                        }
                    }
                    b = false;
                }
                if (b) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public ConnectionKey(final String jdbcUrl, final String userName, final String hashedPassword) {
        this.jdbcUrl = jdbcUrl;
        this.userName = userName;
        this.hashedPassword = hashedPassword;
        Product$class.$init$((Product)this);
    }
}
