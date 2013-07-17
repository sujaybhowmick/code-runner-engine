/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

import com.codeengine.java.CodeRunResult;
import com.codeengine.common.Result;
import com.codeengine.common.ResultListener;

/**
 *
 * @author sujay
 */
public class RunResultCollector<S> implements ResultListener<S>{
    private Result result;
    
    private RunResultCollector(){
    }
    
    public static RunResultCollector newInstance(){
        return new RunResultCollector();
    }
    
    @Override
    public synchronized void report(Result<? extends S> result){
        this.result = result;
    }
    
    public CodeRunResult getOutput(){
        return (CodeRunResult)this.result;
    }
    
    
}
