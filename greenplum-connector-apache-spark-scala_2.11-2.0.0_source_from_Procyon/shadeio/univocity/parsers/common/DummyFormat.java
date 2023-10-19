// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.TreeMap;

final class DummyFormat extends Format
{
    static final DummyFormat instance;
    
    private DummyFormat() {
    }
    
    @Override
    protected final TreeMap<String, Object> getConfiguration() {
        return new TreeMap<String, Object>();
    }
    
    static {
        instance = new DummyFormat();
    }
}
