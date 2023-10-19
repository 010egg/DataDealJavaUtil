// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.types;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.math.BigDecimal;

public class PGDouble implements PGType
{
    private Double val;
    
    protected PGDouble(final Double x) {
        this.val = x;
    }
    
    public static PGType castToServerType(final Double val, final int targetType) throws PSQLException {
        try {
            switch (targetType) {
                case -7: {
                    return new PGBoolean((val == 0.0) ? Boolean.FALSE : Boolean.TRUE);
                }
                case -5: {
                    return new PGLong(new Long(val.longValue()));
                }
                case 4: {
                    return new PGInteger(new Integer(val.intValue()));
                }
                case -6:
                case 5: {
                    return new PGShort(new Short(val.shortValue()));
                }
                case -1:
                case 12: {
                    return new PGString(val.toString());
                }
                case 6:
                case 8: {
                    return new PGDouble(val);
                }
                case 7: {
                    return new PGFloat(new Float(val.floatValue()));
                }
                case 2:
                case 3: {
                    return new PGBigDecimal(new BigDecimal(val.toString()));
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
        return this.val.toString();
    }
}
