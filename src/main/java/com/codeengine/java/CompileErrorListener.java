/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

/**
 *
 * @author sujay
 */
public interface CompileErrorListener<S> {
    void reportError(Result<? extends S> error);
}
