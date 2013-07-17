/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

import com.codeengine.java.CompileError;
import com.codeengine.common.Result;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 *
 * @author sujay
 */
public class CompileErrorImpl implements CompileError<String> {
    private Diagnostic<? extends JavaFileObject> diagnostic;
    
    private CompileErrorImpl(Diagnostic<? extends JavaFileObject> diagnostic){
        this.diagnostic = diagnostic;
    }
    
    public static Result<String> newInstance(
            Diagnostic<? extends JavaFileObject> diagnostic){
        return new CompileErrorImpl(diagnostic);
    }
    
    @Override
    public String getError() {
        return this.diagnostic.getKind() + ": "
                + this.diagnostic.getMessage(null);
    }

    @Override
    public Boolean isSuccess() {
        return false;
    }
}
