/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

import com.codeengine.utils.ConvertUtils;

/**
 *
 * @author sujay
 */
public class CodeRunResult {
    private Boolean result = false;
    
    private Object output;
    
    private Class<?> returnType;
    
    private CodeRunResult(final Boolean result, final Object output, 
                          final Class<?> returnType){
        this.result = result;
        this.output = output;
        this.returnType = returnType;
    }
    
    public static CodeRunResult create(final Boolean result, final Object output, 
                        final Class<?> returnType){
        return new CodeRunResult(result, output, returnType);
    }
    
    public Boolean getResult(){
        return this.result;
    }
    
    
    
    public <T> T getOutput(){
        return ConvertUtils.convert(this.output, this.returnType);
    }
}
