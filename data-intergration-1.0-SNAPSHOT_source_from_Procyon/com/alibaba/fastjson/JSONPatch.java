// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.Feature;

public class JSONPatch
{
    public static String apply(final String original, final String patch) {
        final Object object = apply(JSON.parse(original, Feature.OrderedField), patch);
        return JSON.toJSONString(object);
    }
    
    public static Object apply(final Object object, final String patch) {
        Operation[] operations;
        if (isObject(patch)) {
            operations = new Operation[] { JSON.parseObject(patch, Operation.class) };
        }
        else {
            operations = JSON.parseObject(patch, Operation[].class);
        }
        for (final Operation op : operations) {
            final JSONPath path = JSONPath.compile(op.path);
            switch (op.type) {
                case add: {
                    path.patchAdd(object, op.value, false);
                    break;
                }
                case replace: {
                    path.patchAdd(object, op.value, true);
                    break;
                }
                case remove: {
                    path.remove(object);
                    break;
                }
                case copy:
                case move: {
                    final JSONPath from = JSONPath.compile(op.from);
                    final Object fromValue = from.eval(object);
                    if (op.type == OperationType.move) {
                        final boolean success = from.remove(object);
                        if (!success) {
                            throw new JSONException("json patch move error : " + op.from + " -> " + op.path);
                        }
                    }
                    path.set(object, fromValue);
                    break;
                }
                case test: {
                    final Object result = path.eval(object);
                    if (result == null) {
                        return op.value == null;
                    }
                    return result.equals(op.value);
                }
            }
        }
        return object;
    }
    
    private static boolean isObject(final String patch) {
        if (patch == null) {
            return false;
        }
        for (int i = 0; i < patch.length(); ++i) {
            final char ch = patch.charAt(i);
            if (!JSONLexerBase.isWhitespace(ch)) {
                return ch == '{';
            }
        }
        return false;
    }
    
    @JSONType(orders = { "op", "from", "path", "value" })
    public static class Operation
    {
        @JSONField(name = "op")
        public OperationType type;
        public String from;
        public String path;
        public Object value;
    }
    
    public enum OperationType
    {
        add, 
        remove, 
        replace, 
        move, 
        copy, 
        test;
    }
}
