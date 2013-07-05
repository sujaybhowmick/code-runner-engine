/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine;

/**
 *
 * @author sujay
 */
public interface CompileErrorListener<S> {
    public void reportError(CompileError<? extends S> error);
}
