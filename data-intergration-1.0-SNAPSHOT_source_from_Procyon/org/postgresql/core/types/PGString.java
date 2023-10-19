// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.types;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.math.BigDecimal;

public class PGString implements PGType
{
    String val;
    
    protected PGString(final String x) {
        this.val = x;
    }
    
    public static PGType castToServerType(final String val, final int targetType) throws PSQLException {
        try {
            switch (targetType) {
                case -7: {
                    if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("1") || val.equalsIgnoreCase("t")) {
                        return new PGBoolean(Boolean.TRUE);
                    }
                    if (val.equalsIgnoreCase("false") || val.equalsIgnoreCase("0") || val.equalsIgnoreCase("f")) {
                        return new PGBoolean(Boolean.FALSE);
                    }
                    return new PGBoolean(Boolean.FALSE);
                }
                case -1:
                case 12: {
                    return new PGString(val);
                }
                case -5: {
                    return new PGLong(new Long(Long.parseLong(val)));
                }
                case 4: {
                    return new PGInteger(new Integer(Integer.parseInt(val)));
                }
                case -6: {
                    return new PGShort(new Short(Short.parseShort(val)));
                }
                case 6:
                case 8: {
                    return new PGDouble(new Double(Double.parseDouble(val)));
                }
                case 7: {
                    return new PGFloat(new Float(Float.parseFloat(val)));
                }
                case 2:
                case 3: {
                    return new PGBigDecimal(new BigDecimal(val));
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
        return this.val;
    }
}
