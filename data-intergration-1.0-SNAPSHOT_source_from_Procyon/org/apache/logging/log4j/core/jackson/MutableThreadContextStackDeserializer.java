// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import org.apache.logging.log4j.spi.MutableThreadContextStack;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

final class MutableThreadContextStackDeserializer extends StdDeserializer<MutableThreadContextStack>
{
    private static final long serialVersionUID = 1L;
    
    MutableThreadContextStackDeserializer() {
        super(MutableThreadContextStack.class);
    }
    
    @Override
    public MutableThreadContextStack deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final List<String> list = jp.readValueAs(new TypeReference<List<String>>() {});
        return new MutableThreadContextStack(list);
    }
}
