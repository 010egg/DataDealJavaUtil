// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import javassist.NotFoundException;
import java.util.Iterator;
import java.util.Set;
import javassist.ClassMap;
import javassist.CtNewMethod;
import java.util.HashSet;
import javassist.CtMethod;
import javassist.CtClass;
import com.zaxxer.hikari.pool.ProxyCallableStatement;
import java.sql.CallableStatement;
import com.zaxxer.hikari.pool.ProxyPreparedStatement;
import java.sql.PreparedStatement;
import com.zaxxer.hikari.pool.ProxyResultSet;
import java.sql.ResultSet;
import com.zaxxer.hikari.pool.ProxyStatement;
import java.sql.Statement;
import com.zaxxer.hikari.pool.ProxyConnection;
import java.sql.Connection;
import javassist.ClassPath;
import javassist.LoaderClassPath;
import javassist.ClassPool;

public final class JavassistProxyFactory
{
    private static ClassPool classPool;
    private static String genDirectory;
    
    public static void main(final String... args) {
        (JavassistProxyFactory.classPool = new ClassPool()).importPackage("java.sql");
        JavassistProxyFactory.classPool.appendClassPath((ClassPath)new LoaderClassPath(JavassistProxyFactory.class.getClassLoader()));
        if (args.length > 0) {
            JavassistProxyFactory.genDirectory = args[0];
        }
        try {
            String methodBody = "{ try { return delegate.method($$); } catch (SQLException e) { throw checkException(e); } }";
            generateProxyClass(Connection.class, ProxyConnection.class.getName(), methodBody);
            generateProxyClass(Statement.class, ProxyStatement.class.getName(), methodBody);
            generateProxyClass(ResultSet.class, ProxyResultSet.class.getName(), methodBody);
            methodBody = "{ try { return ((cast) delegate).method($$); } catch (SQLException e) { throw checkException(e); } }";
            generateProxyClass(PreparedStatement.class, ProxyPreparedStatement.class.getName(), methodBody);
            generateProxyClass(CallableStatement.class, ProxyCallableStatement.class.getName(), methodBody);
            modifyProxyFactory();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void modifyProxyFactory() throws Exception {
        System.out.println("Generating method bodies for com.zaxxer.hikari.proxy.ProxyFactory");
        final String packageName = ProxyConnection.class.getPackage().getName();
        final CtClass proxyCt = JavassistProxyFactory.classPool.getCtClass("com.zaxxer.hikari.pool.ProxyFactory");
        for (final CtMethod method : proxyCt.getMethods()) {
            final String name = method.getName();
            switch (name) {
                case "getProxyConnection": {
                    method.setBody("{return new " + packageName + ".HikariProxyConnection($$);}");
                    break;
                }
                case "getProxyStatement": {
                    method.setBody("{return new " + packageName + ".HikariProxyStatement($$);}");
                    break;
                }
                case "getProxyPreparedStatement": {
                    method.setBody("{return new " + packageName + ".HikariProxyPreparedStatement($$);}");
                    break;
                }
                case "getProxyCallableStatement": {
                    method.setBody("{return new " + packageName + ".HikariProxyCallableStatement($$);}");
                    break;
                }
                case "getProxyResultSet": {
                    method.setBody("{return new " + packageName + ".HikariProxyResultSet($$);}");
                    break;
                }
            }
        }
        proxyCt.writeFile(JavassistProxyFactory.genDirectory + "target/classes");
    }
    
    private static <T> void generateProxyClass(final Class<T> primaryInterface, final String superClassName, final String methodBody) throws Exception {
        final String newClassName = superClassName.replaceAll("(.+)\\.(\\w+)", "$1.Hikari$2");
        final CtClass superCt = JavassistProxyFactory.classPool.getCtClass(superClassName);
        final CtClass targetCt = JavassistProxyFactory.classPool.makeClass(newClassName, superCt);
        targetCt.setModifiers(16);
        System.out.println("Generating " + newClassName);
        targetCt.setModifiers(1);
        final Set<String> superSigs = new HashSet<String>();
        for (final CtMethod method : superCt.getMethods()) {
            if ((method.getModifiers() & 0x10) == 0x10) {
                superSigs.add(method.getName() + method.getSignature());
            }
        }
        final Set<String> methods = new HashSet<String>();
        final Set<Class<?>> interfaces = getAllInterfaces(primaryInterface);
        for (final Class<?> intf : interfaces) {
            final CtClass intfCt = JavassistProxyFactory.classPool.getCtClass(intf.getName());
            targetCt.addInterface(intfCt);
            for (final CtMethod intfMethod : intfCt.getDeclaredMethods()) {
                final String signature = intfMethod.getName() + intfMethod.getSignature();
                if (!superSigs.contains(signature)) {
                    if (!methods.contains(signature)) {
                        methods.add(signature);
                        final CtMethod method2 = CtNewMethod.copy(intfMethod, targetCt, (ClassMap)null);
                        String modifiedBody = methodBody;
                        final CtMethod superMethod = superCt.getMethod(intfMethod.getName(), intfMethod.getSignature());
                        if ((superMethod.getModifiers() & 0x400) != 0x400 && !isDefaultMethod(intf, intfCt, intfMethod)) {
                            modifiedBody = modifiedBody.replace("((cast) ", "");
                            modifiedBody = modifiedBody.replace("delegate", "super");
                            modifiedBody = modifiedBody.replace("super)", "super");
                        }
                        modifiedBody = modifiedBody.replace("cast", primaryInterface.getName());
                        if (isThrowsSqlException(intfMethod)) {
                            modifiedBody = modifiedBody.replace("method", method2.getName());
                        }
                        else {
                            modifiedBody = "{ return ((cast) delegate).method($$); }".replace("method", method2.getName()).replace("cast", primaryInterface.getName());
                        }
                        if (method2.getReturnType() == CtClass.voidType) {
                            modifiedBody = modifiedBody.replace("return", "");
                        }
                        method2.setBody(modifiedBody);
                        targetCt.addMethod(method2);
                    }
                }
            }
        }
        targetCt.getClassFile().setMajorVersion(52);
        targetCt.writeFile(JavassistProxyFactory.genDirectory + "target/classes");
    }
    
    private static boolean isThrowsSqlException(final CtMethod method) {
        try {
            for (final CtClass clazz : method.getExceptionTypes()) {
                if (clazz.getSimpleName().equals("SQLException")) {
                    return true;
                }
            }
        }
        catch (NotFoundException ex) {}
        return false;
    }
    
    private static boolean isDefaultMethod(final Class<?> intf, final CtClass intfCt, final CtMethod intfMethod) throws Exception {
        final List<Class<?>> paramTypes = new ArrayList<Class<?>>();
        for (final CtClass pt : intfMethod.getParameterTypes()) {
            paramTypes.add(toJavaClass(pt));
        }
        return intf.getDeclaredMethod(intfMethod.getName(), (Class<?>[])paramTypes.toArray(new Class[paramTypes.size()])).toString().contains("default ");
    }
    
    private static Set<Class<?>> getAllInterfaces(final Class<?> clazz) {
        final Set<Class<?>> interfaces = new HashSet<Class<?>>();
        for (final Class<?> intf : Arrays.asList(clazz.getInterfaces())) {
            if (intf.getInterfaces().length > 0) {
                interfaces.addAll(getAllInterfaces(intf));
            }
            interfaces.add(intf);
        }
        if (clazz.getSuperclass() != null) {
            interfaces.addAll(getAllInterfaces(clazz.getSuperclass()));
        }
        if (clazz.isInterface()) {
            interfaces.add(clazz);
        }
        return interfaces;
    }
    
    private static Class<?> toJavaClass(final CtClass cls) throws Exception {
        if (cls.getName().endsWith("[]")) {
            return Array.newInstance(toJavaClass(cls.getName().replace("[]", "")), 0).getClass();
        }
        return toJavaClass(cls.getName());
    }
    
    private static Class<?> toJavaClass(final String cn) throws Exception {
        switch (cn) {
            case "int": {
                return Integer.TYPE;
            }
            case "long": {
                return Long.TYPE;
            }
            case "short": {
                return Short.TYPE;
            }
            case "byte": {
                return Byte.TYPE;
            }
            case "float": {
                return Float.TYPE;
            }
            case "double": {
                return Double.TYPE;
            }
            case "boolean": {
                return Boolean.TYPE;
            }
            case "char": {
                return Character.TYPE;
            }
            case "void": {
                return Void.TYPE;
            }
            default: {
                return Class.forName(cn);
            }
        }
    }
    
    static {
        JavassistProxyFactory.genDirectory = "";
    }
}
