/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

import com.codeengine.java.impl.CompileErrorCollector;
import com.codeengine.java.impl.CompiledClassCollector;

/**
 *
 * @author sujay
 */
public interface CodeCompiler {
    Boolean compile(final String className, final String fileContent, 
            CompileErrorCollector<CompileError> errors,
            CompiledClassCollector compiledClassCollector);
}
