// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

interface ReplaceableMergeStack extends Container
{
    AbstractConfigValue makeReplacement(final ResolveContext p0, final int p1);
}
