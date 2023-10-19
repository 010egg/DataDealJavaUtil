// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import java.io.File;
import scala.runtime.AbstractFunction1;

public final class FileInput$ extends AbstractFunction1<File, FileInput> implements Serializable
{
    public static final FileInput$ MODULE$;
    
    static {
        new FileInput$();
    }
    
    public final String toString() {
        return "FileInput";
    }
    
    public FileInput apply(final File file) {
        return new FileInput(file);
    }
    
    public Option<File> unapply(final FileInput x$0) {
        return (Option<File>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.file()));
    }
    
    private Object readResolve() {
        return FileInput$.MODULE$;
    }
    
    private FileInput$() {
        MODULE$ = this;
    }
}
