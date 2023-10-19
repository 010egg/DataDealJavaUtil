// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigIncluderClasspath;
import com.typesafe.config.ConfigIncluderURL;
import com.typesafe.config.ConfigIncluderFile;
import com.typesafe.config.ConfigIncluder;

interface FullIncluder extends ConfigIncluder, ConfigIncluderFile, ConfigIncluderURL, ConfigIncluderClasspath
{
}
