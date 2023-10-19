// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.parser;

import java.io.File;
import com.typesafe.config.impl.Parseable;
import com.typesafe.config.ConfigParseOptions;
import java.io.Reader;

public final class ConfigDocumentFactory
{
    public static ConfigDocument parseReader(final Reader reader, final ConfigParseOptions options) {
        return Parseable.newReader(reader, options).parseConfigDocument();
    }
    
    public static ConfigDocument parseReader(final Reader reader) {
        return parseReader(reader, ConfigParseOptions.defaults());
    }
    
    public static ConfigDocument parseFile(final File file, final ConfigParseOptions options) {
        return Parseable.newFile(file, options).parseConfigDocument();
    }
    
    public static ConfigDocument parseFile(final File file) {
        return parseFile(file, ConfigParseOptions.defaults());
    }
    
    public static ConfigDocument parseString(final String s, final ConfigParseOptions options) {
        return Parseable.newString(s, options).parseConfigDocument();
    }
    
    public static ConfigDocument parseString(final String s) {
        return parseString(s, ConfigParseOptions.defaults());
    }
}
