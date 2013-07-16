/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

import com.codeengine.java.CompileError;
import com.codeengine.java.CompileErrorListener;
import com.codeengine.java.Result;
import com.codeengine.java.ResultListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author sujay
 */
public class CompileErrorCollector<S> implements ResultListener<S>{
    List<Result<? extends S>> errors = 
        Collections.synchronizedList(
            new ArrayList<Result<? extends S>>());
    
    @Override
    public void report(Result<? extends S> error) {
        if(error != null){
            errors.add(error);
        }
    }
    
    public List<Result<? extends S>> getErrors(){
        return Collections.unmodifiableList(this.errors);
    }
    
}
