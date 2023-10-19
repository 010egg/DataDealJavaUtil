// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import javax.sql.PooledConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.io.Closeable;
import scala.runtime.BoxedUnit;

public final class Resource$ implements MediumPriorityResourceImplicits
{
    public static final Resource$ MODULE$;
    private volatile gzipOuputStraemResource$ gzipOuputStraemResource$module;
    private volatile jarFileResource$ jarFileResource$module;
    private volatile HttpURLConnectionResource$ HttpURLConnectionResource$module;
    
    static {
        new Resource$();
    }
    
    private gzipOuputStraemResource$ gzipOuputStraemResource$lzycompute() {
        synchronized (this) {
            if (this.gzipOuputStraemResource$module == null) {
                this.gzipOuputStraemResource$module = new gzipOuputStraemResource$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.gzipOuputStraemResource$module;
        }
    }
    
    @Override
    public gzipOuputStraemResource$ gzipOuputStraemResource() {
        return (this.gzipOuputStraemResource$module == null) ? this.gzipOuputStraemResource$lzycompute() : this.gzipOuputStraemResource$module;
    }
    
    private jarFileResource$ jarFileResource$lzycompute() {
        synchronized (this) {
            if (this.jarFileResource$module == null) {
                this.jarFileResource$module = new jarFileResource$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.jarFileResource$module;
        }
    }
    
    @Override
    public jarFileResource$ jarFileResource() {
        return (this.jarFileResource$module == null) ? this.jarFileResource$lzycompute() : this.jarFileResource$module;
    }
    
    private HttpURLConnectionResource$ HttpURLConnectionResource$lzycompute() {
        synchronized (this) {
            if (this.HttpURLConnectionResource$module == null) {
                this.HttpURLConnectionResource$module = new HttpURLConnectionResource$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.HttpURLConnectionResource$module;
        }
    }
    
    @Override
    public HttpURLConnectionResource$ HttpURLConnectionResource() {
        return (this.HttpURLConnectionResource$module == null) ? this.HttpURLConnectionResource$lzycompute() : this.HttpURLConnectionResource$module;
    }
    
    @Override
    public <A extends Closeable> Object closeableResource() {
        return MediumPriorityResourceImplicits$class.closeableResource(this);
    }
    
    @Override
    public <A extends Connection> Object connectionResource() {
        return MediumPriorityResourceImplicits$class.connectionResource(this);
    }
    
    @Override
    public <A extends Statement> Object statementResource() {
        return MediumPriorityResourceImplicits$class.statementResource(this);
    }
    
    @Override
    public <A extends ResultSet> Object resultSetResource() {
        return MediumPriorityResourceImplicits$class.resultSetResource(this);
    }
    
    @Override
    public <A extends PooledConnection> Object pooledConnectionResource() {
        return MediumPriorityResourceImplicits$class.pooledConnectionResource(this);
    }
    
    @Override
    public <A> Object reflectiveCloseableResource() {
        return LowPriorityResourceImplicits$class.reflectiveCloseableResource(this);
    }
    
    @Override
    public <A> Object reflectiveDisposableResource() {
        return LowPriorityResourceImplicits$class.reflectiveDisposableResource(this);
    }
    
    private Resource$() {
        LowPriorityResourceImplicits$class.$init$(MODULE$ = this);
        MediumPriorityResourceImplicits$class.$init$(this);
    }
}
