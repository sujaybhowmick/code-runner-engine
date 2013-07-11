/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

import com.codeengine.java.CodeRunResult;
import java.lang.reflect.Field;
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
public final class CodeRunnerCallable implements Callable<CodeRunResult>{
    final Logger log = LoggerFactory.getLogger(CodeRunnerCallable.class);
    
    private Map<String, byte[]> classBytes;
    private String fqcn;
    private String methodToInvoke;
    private Object[] params;
    private Class<?>[] paramTypes;
    
    public CodeRunnerCallable(Map<String, byte[]> classBytes, 
                        final String fqcn, final String methodToInvoke,
                        Object... params){
        this.classBytes = classBytes;
        this.fqcn = fqcn;
        this.methodToInvoke = methodToInvoke;
        if(params != null && params.length > 0){
            this.params = params;
            this.paramTypes = new Class<?>[params.length];
            retrieveParamTypes(params);
        }else {
            this.params = new Object[]{};
        }
    }
    
    @Override
    public CodeRunResult call() throws Exception {
        log.info("Executing Call...");
        ClassLoader classLoader = new MapClassLoader(this.classBytes);
        Class<?> clazz = classLoader.loadClass(this.fqcn);
        Method method = null;
        
        try {
            method = clazz.getDeclaredMethod(this.methodToInvoke, 
                    this.paramTypes);
        }catch(NoSuchMethodException nsme){
            if(log.isDebugEnabled()){
                log.debug("checking for alternative method", nsme);
            }
            boolean signatureChanged = false;
            for(int i = 0; i < this.paramTypes.length; i++){
                Field field;
                try{
                    field = this.paramTypes[i].getField("TYPE");
                    if(Modifier.isStatic(field.getModifiers())){                        
                        this.paramTypes[i] = (Class<?>)field.get(null);
                        signatureChanged = true;
                    }
                }catch(Exception e){
                    if(log.isDebugEnabled()){
                        log.debug("exception getting modifiers for fields", nsme);
                    }
                    throw new RuntimeException(e);
                }
            }
            if(signatureChanged){
                method = clazz.getDeclaredMethod(this.methodToInvoke, this.paramTypes);
            }
            
        }
        
        if(method != null){
            if(Modifier.isStatic(method.getModifiers())){
                return runStaticMethod(method);
            }else {
                Object instance = clazz.newInstance();
                return runInstanceMethod(method, instance);
            }
        }
        return CodeRunResult.create(Boolean.FALSE, null, null);
    }
    
    private CodeRunResult runStaticMethod(Method method){
        log.info("Executing static method...");
        try {
            Object result = method.invoke(null, this.params);
            return CodeRunResult.create(Boolean.TRUE, result, 
                                            method.getReturnType());
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
            return CodeRunResult.create(Boolean.TRUE, result, 
                                            method.getReturnType());
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        } catch (InvocationTargetException ite) {
            throw new RuntimeException(ite);
        }
    }

    private void retrieveParamTypes(Object[] params) {
         for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
    }
}
