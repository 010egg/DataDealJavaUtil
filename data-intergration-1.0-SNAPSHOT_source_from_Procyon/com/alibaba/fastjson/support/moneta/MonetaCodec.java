// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson.support.moneta;

import javax.money.Monetary;
import java.math.BigInteger;
import java.math.BigDecimal;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.io.IOException;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.javamoney.moneta.Money;
import java.lang.reflect.Type;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

public class MonetaCodec implements ObjectSerializer, ObjectDeserializer
{
    public static final MonetaCodec instance;
    
    public void write(final JSONSerializer serializer, final Object object, final Object fieldName, final Type fieldType, final int features) throws IOException {
        final Money money = (Money)object;
        if (money == null) {
            serializer.writeNull();
            return;
        }
        final SerializeWriter out = serializer.out;
        out.writeFieldValue('{', "numberStripped", money.getNumberStripped());
        out.writeFieldValue(',', "currency", money.getCurrency().getCurrencyCode());
        out.write(125);
    }
    
    public <T> T deserialze(final DefaultJSONParser parser, final Type type, final Object fieldName) {
        final JSONObject object = parser.parseObject();
        final Object currency = object.get("currency");
        String currencyCode = null;
        if (currency instanceof JSONObject) {
            currencyCode = ((JSONObject)currency).getString("currencyCode");
        }
        else if (currency instanceof String) {
            currencyCode = (String)currency;
        }
        final Object numberStripped = object.get("numberStripped");
        if (numberStripped instanceof BigDecimal || numberStripped instanceof Integer || numberStripped instanceof BigInteger) {
            return (T)Money.of((Number)numberStripped, Monetary.getCurrency(currencyCode, new String[0]));
        }
        throw new UnsupportedOperationException();
    }
    
    public int getFastMatchToken() {
        return 0;
    }
    
    static {
        instance = new MonetaCodec();
    }
}
