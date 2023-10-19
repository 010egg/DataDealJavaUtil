// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

import java.net.URL;
import java.security.CodeSource;
import sun.reflect.Reflection;
import java.util.HashMap;

public class PackagingDataCalculator
{
    static final StackTraceElementProxy[] STEP_ARRAY_TEMPLATE;
    HashMap<String, ClassPackagingData> cache;
    private static boolean GET_CALLER_CLASS_METHOD_AVAILABLE;
    
    static {
        STEP_ARRAY_TEMPLATE = new StackTraceElementProxy[0];
        PackagingDataCalculator.GET_CALLER_CLASS_METHOD_AVAILABLE = false;
        try {
            Reflection.getCallerClass(2);
            PackagingDataCalculator.GET_CALLER_CLASS_METHOD_AVAILABLE = true;
        }
        catch (NoClassDefFoundError noClassDefFoundError) {}
        catch (NoSuchMethodError noSuchMethodError) {}
        catch (UnsupportedOperationException ex) {}
        catch (Throwable e) {
            System.err.println("Unexpected exception");
            e.printStackTrace();
        }
    }
    
    public PackagingDataCalculator() {
        this.cache = new HashMap<String, ClassPackagingData>();
    }
    
    public void calculate(IThrowableProxy tp) {
        while (tp != null) {
            this.populateFrames(tp.getStackTraceElementProxyArray());
            final IThrowableProxy[] suppressed = tp.getSuppressed();
            if (suppressed != null) {
                IThrowableProxy[] array;
                for (int length = (array = suppressed).length, i = 0; i < length; ++i) {
                    final IThrowableProxy current = array[i];
                    this.populateFrames(current.getStackTraceElementProxyArray());
                }
            }
            tp = tp.getCause();
        }
    }
    
    void populateFrames(final StackTraceElementProxy[] stepArray) {
        final Throwable t = new Throwable("local stack reference");
        final StackTraceElement[] localteSTEArray = t.getStackTrace();
        final int commonFrames = STEUtil.findNumberOfCommonFrames(localteSTEArray, stepArray);
        final int localFirstCommon = localteSTEArray.length - commonFrames;
        final int stepFirstCommon = stepArray.length - commonFrames;
        ClassLoader lastExactClassLoader = null;
        ClassLoader firsExactClassLoader = null;
        int missfireCount = 0;
        for (int i = 0; i < commonFrames; ++i) {
            Class callerClass = null;
            if (PackagingDataCalculator.GET_CALLER_CLASS_METHOD_AVAILABLE) {
                callerClass = Reflection.getCallerClass(localFirstCommon + i - missfireCount + 1);
            }
            final StackTraceElementProxy step = stepArray[stepFirstCommon + i];
            final String stepClassname = step.ste.getClassName();
            if (callerClass != null && stepClassname.equals(callerClass.getName())) {
                lastExactClassLoader = callerClass.getClassLoader();
                if (firsExactClassLoader == null) {
                    firsExactClassLoader = lastExactClassLoader;
                }
                final ClassPackagingData pi = this.calculateByExactType(callerClass);
                step.setClassPackagingData(pi);
            }
            else {
                ++missfireCount;
                final ClassPackagingData pi = this.computeBySTEP(step, lastExactClassLoader);
                step.setClassPackagingData(pi);
            }
        }
        this.populateUncommonFrames(commonFrames, stepArray, firsExactClassLoader);
    }
    
    void populateUncommonFrames(final int commonFrames, final StackTraceElementProxy[] stepArray, final ClassLoader firstExactClassLoader) {
        for (int uncommonFrames = stepArray.length - commonFrames, i = 0; i < uncommonFrames; ++i) {
            final StackTraceElementProxy step = stepArray[i];
            final ClassPackagingData pi = this.computeBySTEP(step, firstExactClassLoader);
            step.setClassPackagingData(pi);
        }
    }
    
    private ClassPackagingData calculateByExactType(final Class type) {
        final String className = type.getName();
        ClassPackagingData cpd = this.cache.get(className);
        if (cpd != null) {
            return cpd;
        }
        final String version = this.getImplementationVersion(type);
        final String codeLocation = this.getCodeLocation(type);
        cpd = new ClassPackagingData(codeLocation, version);
        this.cache.put(className, cpd);
        return cpd;
    }
    
    private ClassPackagingData computeBySTEP(final StackTraceElementProxy step, final ClassLoader lastExactClassLoader) {
        final String className = step.ste.getClassName();
        ClassPackagingData cpd = this.cache.get(className);
        if (cpd != null) {
            return cpd;
        }
        final Class type = this.bestEffortLoadClass(lastExactClassLoader, className);
        final String version = this.getImplementationVersion(type);
        final String codeLocation = this.getCodeLocation(type);
        cpd = new ClassPackagingData(codeLocation, version, false);
        this.cache.put(className, cpd);
        return cpd;
    }
    
    String getImplementationVersion(final Class type) {
        if (type == null) {
            return "na";
        }
        final Package aPackage = type.getPackage();
        if (aPackage == null) {
            return "na";
        }
        final String v = aPackage.getImplementationVersion();
        if (v == null) {
            return "na";
        }
        return v;
    }
    
    String getCodeLocation(final Class type) {
        try {
            if (type != null) {
                final CodeSource codeSource = type.getProtectionDomain().getCodeSource();
                if (codeSource != null) {
                    final URL resource = codeSource.getLocation();
                    if (resource != null) {
                        final String locationStr = resource.toString();
                        final String result = this.getCodeLocation(locationStr, '/');
                        if (result != null) {
                            return result;
                        }
                        return this.getCodeLocation(locationStr, '\\');
                    }
                }
            }
        }
        catch (Exception ex) {}
        return "na";
    }
    
    private String getCodeLocation(final String locationStr, final char separator) {
        int idx = locationStr.lastIndexOf(separator);
        if (this.isFolder(idx, locationStr)) {
            idx = locationStr.lastIndexOf(separator, idx - 1);
            return locationStr.substring(idx + 1);
        }
        if (idx > 0) {
            return locationStr.substring(idx + 1);
        }
        return null;
    }
    
    private boolean isFolder(final int idx, final String text) {
        return idx != -1 && idx + 1 == text.length();
    }
    
    private Class loadClass(final ClassLoader cl, final String className) {
        if (cl == null) {
            return null;
        }
        try {
            return cl.loadClass(className);
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Class bestEffortLoadClass(final ClassLoader lastGuaranteedClassLoader, final String className) {
        Class result = this.loadClass(lastGuaranteedClassLoader, className);
        if (result != null) {
            return result;
        }
        final ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        if (tccl != lastGuaranteedClassLoader) {
            result = this.loadClass(tccl, className);
        }
        if (result != null) {
            return result;
        }
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
