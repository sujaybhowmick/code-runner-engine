/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

import com.codeengine.java.CodeRunResult;
import com.codeengine.java.Result;
import com.codeengine.utils.ConvertUtils;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author sujay
 */
public final class CodeRunResultImpl implements CodeRunResult {
    private Boolean success = false;
    
    private Object output;
    
    private Class<?> returnType;
    
    private CodeRunResultImpl(final Boolean success, final Object output, 
                          final Class<?> returnType){
        this.success = success;
        this.output = output;
        this.returnType = returnType;
    }
    
    public static CodeRunResultImpl newInstance(final Boolean success, final Object output, 
                        final Class<?> returnType){
        return new CodeRunResultImpl(success, output, returnType);
    }
    
    @Override
    public synchronized Boolean isSuccess(){
        return this.success;
    }
    
    @Override
    public synchronized <T> T getOutput(){
        final Exception e;
        if(this.output != null && this.output instanceof Exception){
            e = (Exception)this.output;
            StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                return ConvertUtils.convert(errors, errors.getClass());
        }
        return ConvertUtils.convert(this.output, this.returnType);
    }
}
