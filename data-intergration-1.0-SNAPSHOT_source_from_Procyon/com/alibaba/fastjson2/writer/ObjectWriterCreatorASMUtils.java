// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.internal.asm.ASMUtils;
import com.alibaba.fastjson2.util.JDKUtils;

final class ObjectWriterCreatorASMUtils
{
    static final String TYPE_UNSAFE_UTILS;
    
    static {
        TYPE_UNSAFE_UTILS = ASMUtils.type(JDKUtils.class);
    }
}
