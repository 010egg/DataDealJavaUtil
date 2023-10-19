// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.io.File;

public interface ConfigIncluderFile
{
    ConfigObject includeFile(final ConfigIncludeContext p0, final File p1);
}
