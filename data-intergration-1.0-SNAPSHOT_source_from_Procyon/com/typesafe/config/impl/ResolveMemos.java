// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.HashMap;
import java.util.Map;

final class ResolveMemos
{
    private final Map<MemoKey, AbstractConfigValue> memos;
    
    private ResolveMemos(final Map<MemoKey, AbstractConfigValue> memos) {
        this.memos = memos;
    }
    
    ResolveMemos() {
        this(new HashMap<MemoKey, AbstractConfigValue>());
    }
    
    AbstractConfigValue get(final MemoKey key) {
        return this.memos.get(key);
    }
    
    ResolveMemos put(final MemoKey key, final AbstractConfigValue value) {
        final Map<MemoKey, AbstractConfigValue> copy = new HashMap<MemoKey, AbstractConfigValue>(this.memos);
        copy.put(key, value);
        return new ResolveMemos(copy);
    }
}
