// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import com.alibaba.fastjson2.util.BeanUtils;

public enum PropertyNamingStrategy
{
    CamelCase, 
    CamelCase1x, 
    PascalCase, 
    SnakeCase, 
    UpperCase, 
    UpperCamelCaseWithSpaces, 
    UpperCamelCaseWithUnderScores, 
    UpperCamelCaseWithDashes, 
    UpperCamelCaseWithDots, 
    KebabCase, 
    UpperCaseWithUnderScores, 
    UpperCaseWithDashes, 
    UpperCaseWithDots, 
    LowerCase, 
    LowerCaseWithUnderScores, 
    LowerCaseWithDashes, 
    LowerCaseWithDots, 
    NeverUseThisValueExceptDefaultValue;
    
    public String fieldName(final String name) {
        return BeanUtils.fieldName(name, this.name());
    }
    
    public static String snakeToCamel(final String name) {
        if (name == null || name.indexOf(95) == -1) {
            return name;
        }
        int underscoreCount = 0;
        for (int i = 0; i < name.length(); ++i) {
            final char ch = name.charAt(i);
            if (ch == '_') {
                ++underscoreCount;
            }
        }
        final char[] chars = new char[name.length() - underscoreCount];
        int j = 0;
        int k = 0;
        while (j < name.length()) {
            char ch2 = name.charAt(j);
            if (ch2 != '_') {
                if (j > 0 && name.charAt(j - 1) == '_' && ch2 >= 'a' && ch2 <= 'z') {
                    ch2 -= ' ';
                }
                chars[k++] = ch2;
            }
            ++j;
        }
        return new String(chars);
    }
    
    private static /* synthetic */ PropertyNamingStrategy[] $values() {
        return new PropertyNamingStrategy[] { PropertyNamingStrategy.CamelCase, PropertyNamingStrategy.CamelCase1x, PropertyNamingStrategy.PascalCase, PropertyNamingStrategy.SnakeCase, PropertyNamingStrategy.UpperCase, PropertyNamingStrategy.UpperCamelCaseWithSpaces, PropertyNamingStrategy.UpperCamelCaseWithUnderScores, PropertyNamingStrategy.UpperCamelCaseWithDashes, PropertyNamingStrategy.UpperCamelCaseWithDots, PropertyNamingStrategy.KebabCase, PropertyNamingStrategy.UpperCaseWithUnderScores, PropertyNamingStrategy.UpperCaseWithDashes, PropertyNamingStrategy.UpperCaseWithDots, PropertyNamingStrategy.LowerCase, PropertyNamingStrategy.LowerCaseWithUnderScores, PropertyNamingStrategy.LowerCaseWithDashes, PropertyNamingStrategy.LowerCaseWithDots, PropertyNamingStrategy.NeverUseThisValueExceptDefaultValue };
    }
    
    static {
        $VALUES = $values();
    }
}
