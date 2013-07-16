/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

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
public final class CodeRunnerCallable implements Callable<RunResultCollector>{
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
    public RunResultCollector call() throws Exception {
        log.info("Executing Call...");
        ClassLoader classLoader = MapClassLoader.newInstance(this.classBytes);
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
        RunResultCollector resultCollector = new RunResultCollector();
        if(method != null){
            if(Modifier.isStatic(method.getModifiers())){
                resultCollector.report(runStaticMethod(method));
            }else {
                Object instance = clazz.newInstance();
                resultCollector.report(runInstanceMethod(method, instance));
            }
        }else {
            resultCollector.report(CodeRunResultImpl.newInstance(Boolean.FALSE, 
                                                null, null));
        }
        return resultCollector;
    }
    
    private CodeRunResultImpl runStaticMethod(Method method){
        log.info("Executing static method...");
        try {
            Object result = method.invoke(null, this.params);
            return CodeRunResultImpl.newInstance(Boolean.TRUE, result, 
                                            method.getReturnType());
        }catch(Exception e){
            return exceptionToResult(e);
        }
    }
    
    private CodeRunResultImpl runInstanceMethod(Method method, Object instance){
        log.info("Executing instance method...");
        try {
            Object result = method.invoke(instance, this.params);
            return CodeRunResultImpl.newInstance(Boolean.TRUE, result, 
                                            method.getReturnType());
        }catch(Exception e){
            return exceptionToResult(e);
        }
    }

    private void retrieveParamTypes(Object[] params) {
         for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
    }
    
    private CodeRunResultImpl exceptionToResult(final Exception re){
        return CodeRunResultImpl.newInstance(Boolean.FALSE, re, re.getClass());
    }
}
