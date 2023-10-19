// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.types;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.math.BigDecimal;

public class PGBigDecimal implements PGType
{
    private BigDecimal val;
    
    protected PGBigDecimal(final BigDecimal x) {
        this.val = new BigDecimal(x.toString());
    }
    
    public static PGType castToServerType(final BigDecimal val, final int targetType) throws PSQLException {
        try {
            switch (targetType) {
                case -7: {
                    return new PGBoolean((val.doubleValue() == 0.0) ? Boolean.FALSE : Boolean.TRUE);
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
                case 2:
                case 3:
                case 6:
                case 7:
                case 8: {
                    return new PGBigDecimal(val);
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
