// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.Function0;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public abstract class Logging$class
{
    public static Logger log(final Logging $this) {
        if ($this.io$pivotal$greenplum$spark$Logging$$log_() == null) {
            $this.io$pivotal$greenplum$spark$Logging$$log__$eq(LoggerFactory.getLogger($this.getClass()));
        }
        return $this.io$pivotal$greenplum$spark$Logging$$log_();
    }
    
    public static void logWarning(final Logging $this, final Function0 msg) {
        if ($this.log().isWarnEnabled()) {
            $this.log().warn((String)msg.apply());
        }
    }
    
    public static void logDebug(final Logging $this, final Function0 msg) {
        if ($this.log().isDebugEnabled()) {
            $this.log().debug((String)msg.apply());
        }
    }
    
    public static void $init$(final Logging $this) {
    }
}
