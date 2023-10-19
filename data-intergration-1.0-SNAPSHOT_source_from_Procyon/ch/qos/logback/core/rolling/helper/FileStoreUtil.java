// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling.helper;

import java.lang.reflect.Method;
import ch.qos.logback.core.rolling.RolloverFailure;
import java.io.File;

public class FileStoreUtil
{
    static final String PATH_CLASS_STR = "java.nio.file.Path";
    static final String FILES_CLASS_STR = "java.nio.file.Files";
    
    public static boolean areOnSameFileStore(final File a, final File b) throws RolloverFailure {
        if (!a.exists()) {
            throw new IllegalArgumentException("File [" + a + "] does not exist.");
        }
        if (!b.exists()) {
            throw new IllegalArgumentException("File [" + b + "] does not exist.");
        }
        try {
            final Class<?> pathClass = Class.forName("java.nio.file.Path");
            final Class<?> filesClass = Class.forName("java.nio.file.Files");
            final Method toPath = File.class.getMethod("toPath", (Class<?>[])new Class[0]);
            final Method getFileStoreMethod = filesClass.getMethod("getFileStore", pathClass);
            final Object pathA = toPath.invoke(a, new Object[0]);
            final Object pathB = toPath.invoke(b, new Object[0]);
            final Object fileStoreA = getFileStoreMethod.invoke(null, pathA);
            final Object fileStoreB = getFileStoreMethod.invoke(null, pathB);
            return fileStoreA.equals(fileStoreB);
        }
        catch (Exception e) {
            throw new RolloverFailure("Failed to check file store equality for [" + a + "] and [" + b + "]", e);
        }
    }
}
