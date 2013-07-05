/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine;

import com.codeengine.impl.CompileErrorCollector;

/**
 *
 * @author sujay
 */
public interface CodeRunnerEngine {
    Boolean compile(final String className, final String fileContent, CompileErrorCollector<CompileError> errors);
    
    int run(final String clazzName);
}
