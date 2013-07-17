/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

import com.codeengine.common.Result;

/**
 *
 * @author sujay
 */
public interface CodeRunResult extends Result {
    <T> T getOutput();
}
