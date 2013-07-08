/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

/**
 *
 * @author sujay
 */
public class CodeRunResult {
    private Boolean result = false;
    
    private Object output;
    
    public CodeRunResult(final Boolean result, final Object output){
        this.result = result;
        this.output = output;
    }
    
    public Boolean getResult(){
        return this.result;
    }
    
    public Object getOutput(){
        return this.output;
    }
}
