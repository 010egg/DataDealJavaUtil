// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import org.postgresql.core.BaseConnection;
import java.sql.ParameterMetaData;

public class Jdbc4ParameterMetaData extends AbstractJdbc4ParameterMetaData implements ParameterMetaData
{
    public Jdbc4ParameterMetaData(final BaseConnection connection, final int[] oids) {
        super(connection, oids);
    }
}
