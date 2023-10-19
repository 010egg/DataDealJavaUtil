// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValue;
import java.util.Iterator;
import java.io.Reader;
import com.typesafe.config.ConfigOrigin;
import java.io.StringReader;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.parser.ConfigDocument;

final class SimpleConfigDocument implements ConfigDocument
{
    private ConfigNodeRoot configNodeTree;
    private ConfigParseOptions parseOptions;
    
    SimpleConfigDocument(final ConfigNodeRoot parsedNode, final ConfigParseOptions parseOptions) {
        this.configNodeTree = parsedNode;
        this.parseOptions = parseOptions;
    }
    
    @Override
    public ConfigDocument withValueText(final String path, final String newValue) {
        if (newValue == null) {
            throw new ConfigException.BugOrBroken("null value for " + path + " passed to withValueText");
        }
        final SimpleConfigOrigin origin = SimpleConfigOrigin.newSimple("single value parsing");
        final StringReader reader = new StringReader(newValue);
        final Iterator<Token> tokens = Tokenizer.tokenize(origin, reader, this.parseOptions.getSyntax());
        final AbstractConfigNodeValue parsedValue = ConfigDocumentParser.parseValue(tokens, origin, this.parseOptions);
        reader.close();
        return new SimpleConfigDocument(this.configNodeTree.setValue(path, parsedValue, this.parseOptions.getSyntax()), this.parseOptions);
    }
    
    @Override
    public ConfigDocument withValue(final String path, final ConfigValue newValue) {
        if (newValue == null) {
            throw new ConfigException.BugOrBroken("null value for " + path + " passed to withValue");
        }
        ConfigRenderOptions options = ConfigRenderOptions.defaults();
        options = options.setOriginComments(false);
        return this.withValueText(path, newValue.render(options).trim());
    }
    
    @Override
    public ConfigDocument withoutPath(final String path) {
        return new SimpleConfigDocument(this.configNodeTree.setValue(path, null, this.parseOptions.getSyntax()), this.parseOptions);
    }
    
    @Override
    public boolean hasPath(final String path) {
        return this.configNodeTree.hasValue(path);
    }
    
    @Override
    public String render() {
        return this.configNodeTree.render();
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ConfigDocument && this.render().equals(((ConfigDocument)other).render());
    }
    
    @Override
    public int hashCode() {
        return this.render().hashCode();
    }
}
