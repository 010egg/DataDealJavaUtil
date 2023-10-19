// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

public abstract class Flags$class
{
    public static boolean isImplicit(final Flags $this) {
        return $this.hasFlag(1L);
    }
    
    public static boolean isFinal(final Flags $this) {
        return $this.hasFlag(2L);
    }
    
    public static boolean isPrivate(final Flags $this) {
        return $this.hasFlag(4L);
    }
    
    public static boolean isProtected(final Flags $this) {
        return $this.hasFlag(8L);
    }
    
    public static boolean isSealed(final Flags $this) {
        return $this.hasFlag(16L);
    }
    
    public static boolean isOverride(final Flags $this) {
        return $this.hasFlag(32L);
    }
    
    public static boolean isCase(final Flags $this) {
        return $this.hasFlag(64L);
    }
    
    public static boolean isAbstract(final Flags $this) {
        return $this.hasFlag(128L);
    }
    
    public static boolean isDeferred(final Flags $this) {
        return $this.hasFlag(256L);
    }
    
    public static boolean isMethod(final Flags $this) {
        return $this.hasFlag(512L);
    }
    
    public static boolean isModule(final Flags $this) {
        return $this.hasFlag(1024L);
    }
    
    public static boolean isInterface(final Flags $this) {
        return $this.hasFlag(2048L);
    }
    
    public static boolean isMutable(final Flags $this) {
        return $this.hasFlag(4096L);
    }
    
    public static boolean isParam(final Flags $this) {
        return $this.hasFlag(8192L);
    }
    
    public static boolean isPackage(final Flags $this) {
        return $this.hasFlag(16384L);
    }
    
    public static boolean isDeprecated(final Flags $this) {
        return $this.hasFlag(32768L);
    }
    
    public static boolean isCovariant(final Flags $this) {
        return $this.hasFlag(65536L);
    }
    
    public static boolean isCaptured(final Flags $this) {
        return $this.hasFlag(65536L);
    }
    
    public static boolean isByNameParam(final Flags $this) {
        return $this.hasFlag(65536L);
    }
    
    public static boolean isContravariant(final Flags $this) {
        return $this.hasFlag(131072L);
    }
    
    public static boolean isLabel(final Flags $this) {
        return $this.hasFlag(131072L);
    }
    
    public static boolean isInConstructor(final Flags $this) {
        return $this.hasFlag(131072L);
    }
    
    public static boolean isAbstractOverride(final Flags $this) {
        return $this.hasFlag(262144L);
    }
    
    public static boolean isLocal(final Flags $this) {
        return $this.hasFlag(524288L);
    }
    
    public static boolean isJava(final Flags $this) {
        return $this.hasFlag(1048576L);
    }
    
    public static boolean isSynthetic(final Flags $this) {
        return $this.hasFlag(2097152L);
    }
    
    public static boolean isStable(final Flags $this) {
        return $this.hasFlag(4194304L);
    }
    
    public static boolean isStatic(final Flags $this) {
        return $this.hasFlag(8388608L);
    }
    
    public static boolean isCaseAccessor(final Flags $this) {
        return $this.hasFlag(16777216L);
    }
    
    public static boolean isTrait(final Flags $this) {
        return $this.hasFlag(33554432L);
    }
    
    public static boolean isBridge(final Flags $this) {
        return $this.hasFlag(67108864L);
    }
    
    public static boolean isAccessor(final Flags $this) {
        return $this.hasFlag(134217728L);
    }
    
    public static boolean isSuperAccessor(final Flags $this) {
        return $this.hasFlag(268435456L);
    }
    
    public static boolean isParamAccessor(final Flags $this) {
        return $this.hasFlag(536870912L);
    }
    
    public static boolean isModuleVar(final Flags $this) {
        return $this.hasFlag(1073741824L);
    }
    
    public static boolean isMonomorphic(final Flags $this) {
        return $this.hasFlag(1073741824L);
    }
    
    public static boolean isLazy(final Flags $this) {
        return $this.hasFlag(2147483648L);
    }
    
    public static boolean isError(final Flags $this) {
        return $this.hasFlag(4294967296L);
    }
    
    public static boolean isOverloaded(final Flags $this) {
        return $this.hasFlag(8589934592L);
    }
    
    public static boolean isLifted(final Flags $this) {
        return $this.hasFlag(17179869184L);
    }
    
    public static boolean isMixedIn(final Flags $this) {
        return $this.hasFlag(34359738368L);
    }
    
    public static boolean isExistential(final Flags $this) {
        return $this.hasFlag(34359738368L);
    }
    
    public static boolean isExpandedName(final Flags $this) {
        return $this.hasFlag(68719476736L);
    }
    
    public static boolean isImplementationClass(final Flags $this) {
        return $this.hasFlag(137438953472L);
    }
    
    public static boolean isPreSuper(final Flags $this) {
        return $this.hasFlag(137438953472L);
    }
    
    public static void $init$(final Flags $this) {
    }
}
