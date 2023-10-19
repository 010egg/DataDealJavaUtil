// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.types;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.math.BigDecimal;

public class PGBoolean implements PGType
{
    private Boolean val;
    
    public PGBoolean(final Boolean x) {
        this.val = x;
    }
    
    public static PGType castToServerType(final Boolean val, final int targetType) throws PSQLException {
        try {
            switch (targetType) {
                case -5: {
                    return new PGLong(new Long((long)(Object)val));
                }
                case 4: {
                    return new PGInteger(new Integer((int)(Object)val));
                }
                case -6:
                case 5: {
                    return new PGShort(new Short((short)(Object)val));
                }
                case -1:
                case 12: {
                    return new PGString(val ? "true" : "false");
                }
                case 6:
                case 8: {
                    return new PGDouble(new Double(val ? 1.0 : 0.0));
                }
                case 7: {
                    return new PGFloat(new Float(val ? 1.0f : 0.0f));
                }
                case 2:
                case 3: {
                    return new PGBigDecimal(new BigDecimal((int)(Object)val));
                }
                case -7: {
                    return new PGBoolean(val);
                }
                default: {
                    return new PGUnknown(val);
                }
            }
        }
        catch (Exception ex) {
            throw new PSQLException(GT.tr("Cannot convert an instance of {0} to type {1}", new Object[] { val.getClass().getName(), "Types.OTHER" }), PSQLState.INVALID_PARAMETER_TYPE, ex);
        }
    }
    
    @Override
    public String toString() {
        return this.val ? "true" : "false";
    }
}
