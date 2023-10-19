// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Product$class;
import scala.collection.Iterator;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u00192Q!\u0001\u0002\u0002\"%\u0011!\u0002R3tGJL\u0007\u000f^8s\u0015\t\u0019A!A\u0004sK\u001adWm\u0019;\u000b\u0005\u00151\u0011A\u00026t_:$4OC\u0001\b\u0003\ry'oZ\u0002\u0001'\u0011\u0001!\u0002E\n\u0011\u0005-qQ\"\u0001\u0007\u000b\u00035\tQa]2bY\u0006L!a\u0004\u0007\u0003\r\u0005s\u0017PU3g!\tY\u0011#\u0003\u0002\u0013\u0019\t9\u0001K]8ek\u000e$\bCA\u0006\u0015\u0013\t)BB\u0001\u0007TKJL\u0017\r\\5{C\ndW\rC\u0003\u0018\u0001\u0011\u0005\u0001$\u0001\u0004=S:LGO\u0010\u000b\u00023A\u0011!\u0004A\u0007\u0002\u0005%2\u0001\u0001\b\u0010!E\u0011J!!\b\u0002\u0003+\r{gn\u001d;sk\u000e$xN\u001d#fg\u000e\u0014\u0018\u000e\u001d;pe&\u0011qD\u0001\u0002\u001b\u0007>t7\u000f\u001e:vGR|'\u000fU1sC6$Um]2sSB$xN]\u0005\u0003C\t\u0011\u0001c\u00142kK\u000e$H)Z:de&\u0004Ho\u001c:\n\u0005\r\u0012!A\u0005)s_B,'\u000f^=EKN\u001c'/\u001b9u_JL!!\n\u0002\u0003'MKgn\u001a7fi>tG)Z:de&\u0004Ho\u001c:")
public abstract class Descriptor implements Product, Serializable
{
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)Product$class.productIterator((Product)this);
    }
    
    public String productPrefix() {
        return Product$class.productPrefix((Product)this);
    }
    
    public Descriptor() {
        Product$class.$init$((Product)this);
    }
}
