/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.impl;

import com.codeengine.CodeRunResult;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sujay
 */
public class CodeRunnerCallable implements Callable<CodeRunResult>{
    final Logger log = LoggerFactory.getLogger(CodeRunnerCallable.class);
    
    private Map<String, byte[]> classBytes;
    private String className;
    private String methodToInvoke;
    private Object[] params;
    private Class[] paramTypes;
    
    public CodeRunnerCallable(Map<String, byte[]> classBytes, 
                              String className, String methodToInvoke,
                              Class[] paramTypes,
                              Object... params){
        this.classBytes = classBytes;
        this.className = className;
        this.methodToInvoke = methodToInvoke;
        if(params != null && params.length > 0){
            log.info("available params");
            this.params = params;
            this.paramTypes = paramTypes;
        }else {
            this.params = new Object[]{};
        }
    }
    
    public CodeRunResult call() throws Exception {
        log.info("Executing Call...");
        ClassLoader classLoader = new MapClassLoader(this.classBytes);
        Class clazz = classLoader.loadClass(this.className);
        Method method = clazz.getDeclaredMethod(this.methodToInvoke, this.paramTypes);
        if(Modifier.isStatic(method.getModifiers())){
            return runStaticMethod(method);
        }else {
            Object instance = clazz.newInstance();
            return runInstanceMethod(method, instance);
        }
    }
    
    private CodeRunResult runStaticMethod(Method method){
        log.info("Executing static method...");
        try {
            Object result = method.invoke(null, this.params);
            return new CodeRunResult(Boolean.TRUE, result);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        } catch (InvocationTargetException ite) {
            throw new RuntimeException(ite);
        }
    }
    
    private CodeRunResult runInstanceMethod(Method method, Object instance){
        log.info("Executing instance method...");
        try {
            Object result = method.invoke(instance, this.params);
            return new CodeRunResult(Boolean.TRUE, result);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        } catch (InvocationTargetException ite) {
            throw new RuntimeException(ite);
        }
    }

    /**
     * @param classes the classes to set
     */
    public void setClassBytes(Map<String, byte[]> classBytes) {
        this.classBytes = classBytes;
    }

    /**
     * @param runClass the runClass to set
     */
    public void setClassName(String className) {
        this.className = className;
    }
}
