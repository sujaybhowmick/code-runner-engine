/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

import com.codeengine.java.CompileError;
import com.codeengine.java.CompileErrorListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author sujay
 */
public class CompileErrorCollector<S> implements CompileErrorListener<S>{
    List<CompileError<? extends S>> errors = 
        Collections.synchronizedList(
            new ArrayList<CompileError<? extends S>>());
    
    public void reportError(CompileError<? extends S> error) {
        if(error != null){
            errors.add(error);
        }
    }
    
    public List<CompileError<? extends S>> getErrors(){
        return Collections.unmodifiableList(this.errors);
    }
    
}
