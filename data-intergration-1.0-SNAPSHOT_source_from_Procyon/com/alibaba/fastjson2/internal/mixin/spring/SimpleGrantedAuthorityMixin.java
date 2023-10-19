// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.internal.mixin.spring;

import com.alibaba.fastjson2.annotation.JSONCreator;
import com.alibaba.fastjson2.annotation.JSONField;

public class SimpleGrantedAuthorityMixin
{
    @JSONCreator
    public SimpleGrantedAuthorityMixin(@JSONField(name = "authority") final String role) {
    }
}
