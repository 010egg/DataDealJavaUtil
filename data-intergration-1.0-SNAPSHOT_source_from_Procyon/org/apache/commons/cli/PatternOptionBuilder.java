// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.cli;

import java.net.URL;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

public class PatternOptionBuilder
{
    public static final Class<String> STRING_VALUE;
    public static final Class<Object> OBJECT_VALUE;
    public static final Class<Number> NUMBER_VALUE;
    public static final Class<Date> DATE_VALUE;
    public static final Class<?> CLASS_VALUE;
    public static final Class<FileInputStream> EXISTING_FILE_VALUE;
    public static final Class<File> FILE_VALUE;
    public static final Class<File[]> FILES_VALUE;
    public static final Class<URL> URL_VALUE;
    
    public static Object getValueClass(final char ch) {
        switch (ch) {
            case '@': {
                return PatternOptionBuilder.OBJECT_VALUE;
            }
            case ':': {
                return PatternOptionBuilder.STRING_VALUE;
            }
            case '%': {
                return PatternOptionBuilder.NUMBER_VALUE;
            }
            case '+': {
                return PatternOptionBuilder.CLASS_VALUE;
            }
            case '#': {
                return PatternOptionBuilder.DATE_VALUE;
            }
            case '<': {
                return PatternOptionBuilder.EXISTING_FILE_VALUE;
            }
            case '>': {
                return PatternOptionBuilder.FILE_VALUE;
            }
            case '*': {
                return PatternOptionBuilder.FILES_VALUE;
            }
            case '/': {
                return PatternOptionBuilder.URL_VALUE;
            }
            default: {
                return null;
            }
        }
    }
    
    public static boolean isValueCode(final char ch) {
        return ch == '@' || ch == ':' || ch == '%' || ch == '+' || ch == '#' || ch == '<' || ch == '>' || ch == '*' || ch == '/' || ch == '!';
    }
    
    public static Options parsePattern(final String pattern) {
        char opt = ' ';
        boolean required = false;
        Class<?> type = null;
        final Options options = new Options();
        for (int i = 0; i < pattern.length(); ++i) {
            final char ch = pattern.charAt(i);
            if (!isValueCode(ch)) {
                if (opt != ' ') {
                    final Option option = Option.builder(String.valueOf(opt)).hasArg(type != null).required(required).type(type).build();
                    options.addOption(option);
                    required = false;
                    type = null;
                    opt = ' ';
                }
                opt = ch;
            }
            else if (ch == '!') {
                required = true;
            }
            else {
                type = (Class<?>)getValueClass(ch);
            }
        }
        if (opt != ' ') {
            final Option option2 = Option.builder(String.valueOf(opt)).hasArg(type != null).required(required).type(type).build();
            options.addOption(option2);
        }
        return options;
    }
    
    static {
        STRING_VALUE = String.class;
        OBJECT_VALUE = Object.class;
        NUMBER_VALUE = Number.class;
        DATE_VALUE = Date.class;
        CLASS_VALUE = Class.class;
        EXISTING_FILE_VALUE = FileInputStream.class;
        FILE_VALUE = File.class;
        FILES_VALUE = File[].class;
        URL_VALUE = URL.class;
    }
}
