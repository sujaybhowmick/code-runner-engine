/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.common;

/**
 *
 * @author sujay
 */
public interface ResultListener<S> {
    void report(Result<? extends S> result);
}
