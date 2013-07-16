/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

import com.codeengine.java.CodeRunResult;
import com.codeengine.java.Result;
import com.codeengine.java.ResultListener;

/**
 *
 * @author sujay
 */
public class RunResultCollector<S> implements ResultListener<S>{
    private Result result;
    
    @Override
    public void report(Result<? extends S> result){
        this.result = result;
    }
    
    public CodeRunResult getOutput(){
        return (CodeRunResult)this.result;
    }
    
    
}
