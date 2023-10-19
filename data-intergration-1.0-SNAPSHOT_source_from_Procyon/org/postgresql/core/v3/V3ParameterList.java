// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import java.sql.SQLException;
import org.postgresql.core.ParameterList;

interface V3ParameterList extends ParameterList
{
    void checkAllParametersSet() throws SQLException;
    
    void convertFunctionOutParameters();
    
    SimpleParameterList[] getSubparams();
}
