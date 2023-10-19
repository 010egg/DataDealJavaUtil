// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.util.Locale;
import com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class OneParamFunctions implements Function
{
    public static final OneParamFunctions instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        if (x.getArguments().size() == 0) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr param = x.getArguments().get(0);
        param.accept(visitor);
        final Object paramValue = param.getAttributes().get("eval.value");
        if (paramValue == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        if (paramValue == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return SQLEvalVisitor.EVAL_VALUE_NULL;
        }
        final String method = x.getMethodName();
        if ("md5".equalsIgnoreCase(method)) {
            final String text = paramValue.toString();
            return Utils.md5(text);
        }
        if ("bit_count".equalsIgnoreCase(method)) {
            if (paramValue instanceof BigInteger) {
                return ((BigInteger)paramValue).bitCount();
            }
            if (paramValue instanceof BigDecimal) {
                final BigDecimal decimal = (BigDecimal)paramValue;
                final BigInteger bigInt = decimal.setScale(0, 4).toBigInteger();
                return bigInt.bitCount();
            }
            final Long val = SQLEvalVisitorUtils.castToLong(paramValue);
            return Long.bitCount(val);
        }
        else {
            if ("soundex".equalsIgnoreCase(method)) {
                final String text = paramValue.toString();
                return soundex(text);
            }
            if ("space".equalsIgnoreCase(method)) {
                final int intVal = SQLEvalVisitorUtils.castToInteger(paramValue);
                final char[] chars = new char[intVal];
                for (int i = 0; i < chars.length; ++i) {
                    chars[i] = ' ';
                }
                return new String(chars);
            }
            throw new UnsupportedOperationException(method);
        }
    }
    
    public static String soundex(String str) {
        if (str == null) {
            return null;
        }
        str = clean(str);
        if (str.length() == 0) {
            return str;
        }
        final char[] out = { '0', '0', '0', '0' };
        int incount = 1;
        int count = 1;
        out[0] = str.charAt(0);
        char last = getMappingCode(str, 0);
        while (incount < str.length() && count < out.length) {
            final char mapped = getMappingCode(str, incount++);
            if (mapped != '\0') {
                if (mapped != '0' && mapped != last) {
                    out[count++] = mapped;
                }
                last = mapped;
            }
        }
        return new String(out);
    }
    
    static String clean(final String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        final int len = str.length();
        final char[] chars = new char[len];
        int count = 0;
        for (int i = 0; i < len; ++i) {
            if (Character.isLetter(str.charAt(i))) {
                chars[count++] = str.charAt(i);
            }
        }
        if (count == len) {
            return str.toUpperCase(Locale.ENGLISH);
        }
        return new String(chars, 0, count).toUpperCase(Locale.ENGLISH);
    }
    
    private static char getMappingCode(final String str, final int index) {
        final char mappedChar = map(str.charAt(index));
        if (index > 1 && mappedChar != '0') {
            final char hwChar = str.charAt(index - 1);
            if ('H' == hwChar || 'W' == hwChar) {
                final char preHWChar = str.charAt(index - 2);
                final char firstCode = map(preHWChar);
                if (firstCode == mappedChar || 'H' == preHWChar || 'W' == preHWChar) {
                    return '\0';
                }
            }
        }
        return mappedChar;
    }
    
    private static char map(final char ch) {
        final String soundexMapping = "01230120022455012623010202";
        final int index = ch - 'A';
        if (index < 0 || index >= soundexMapping.length()) {
            throw new IllegalArgumentException("The character is not mapped: " + ch);
        }
        return soundexMapping.charAt(index);
    }
    
    static {
        instance = new OneParamFunctions();
    }
}
