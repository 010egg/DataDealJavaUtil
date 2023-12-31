// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jpa.converter;

import javax.persistence.Converter;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import javax.persistence.AttributeConverter;

@Converter(autoApply = false)
public class ContextDataAttributeConverter implements AttributeConverter<ReadOnlyStringMap, String>
{
    public String convertToDatabaseColumn(final ReadOnlyStringMap contextData) {
        if (contextData == null) {
            return null;
        }
        return contextData.toString();
    }
    
    public ReadOnlyStringMap convertToEntityAttribute(final String s) {
        throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
    }
}
