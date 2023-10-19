// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ds.jdbc23;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.postgresql.Driver;
import java.io.Serializable;
import org.postgresql.ds.common.BaseDataSource;

public abstract class AbstractJdbc23SimpleDataSource extends BaseDataSource implements Serializable
{
    @Override
    public String getDescription() {
        return "Non-Pooling DataSource from " + Driver.getVersion();
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        this.writeBaseObject(out);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.readBaseObject(in);
    }
}
