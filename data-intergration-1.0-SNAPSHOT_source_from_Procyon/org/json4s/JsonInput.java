// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Product$class;
import scala.collection.Iterator;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\t2Q!\u0001\u0002\u0002\"\u001d\u0011\u0011BS:p]&s\u0007/\u001e;\u000b\u0005\r!\u0011A\u00026t_:$4OC\u0001\u0006\u0003\ry'oZ\u0002\u0001'\u0011\u0001\u0001BD\t\u0011\u0005%aQ\"\u0001\u0006\u000b\u0003-\tQa]2bY\u0006L!!\u0004\u0006\u0003\r\u0005s\u0017PU3g!\tIq\"\u0003\u0002\u0011\u0015\t9\u0001K]8ek\u000e$\bCA\u0005\u0013\u0013\t\u0019\"B\u0001\u0007TKJL\u0017\r\\5{C\ndW\rC\u0003\u0016\u0001\u0011\u0005a#\u0001\u0004=S:LGO\u0010\u000b\u0002/A\u0011\u0001\u0004A\u0007\u0002\u0005%*\u0001A\u0007\u000f\u001fA%\u00111D\u0001\u0002\n\r&dW-\u00138qkRL!!\b\u0002\u0003\u0017I+\u0017\rZ3s\u0013:\u0004X\u000f^\u0005\u0003?\t\u00111b\u0015;sK\u0006l\u0017J\u001c9vi&\u0011\u0011E\u0001\u0002\f'R\u0014\u0018N\\4J]B,H\u000f")
public abstract class JsonInput implements Product, Serializable
{
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)Product$class.productIterator((Product)this);
    }
    
    public String productPrefix() {
        return Product$class.productPrefix((Product)this);
    }
    
    public JsonInput() {
        Product$class.$init$((Product)this);
    }
}
