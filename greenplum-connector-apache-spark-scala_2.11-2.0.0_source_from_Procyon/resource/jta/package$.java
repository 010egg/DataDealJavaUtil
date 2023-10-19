// 
// Decompiled by Procyon v0.5.36
// 

package resource.jta;

import resource.Resource;
import javax.transaction.Transaction;

public final class package$
{
    public static final package$ MODULE$;
    
    static {
        new package$();
    }
    
    public <A extends Transaction> Resource<A> transactionSupport() {
        return (Resource<A>)new package$$anon.package$$anon$1();
    }
    
    private package$() {
        MODULE$ = this;
    }
}
