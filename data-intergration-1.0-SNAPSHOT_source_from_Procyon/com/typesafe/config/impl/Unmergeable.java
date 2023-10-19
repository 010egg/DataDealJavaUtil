// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Collection;

interface Unmergeable
{
    Collection<? extends AbstractConfigValue> unmergedValues();
}
