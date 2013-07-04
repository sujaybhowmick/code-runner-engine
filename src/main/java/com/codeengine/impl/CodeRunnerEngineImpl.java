/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.impl;

import com.codeengine.CodeRunnerEngine;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Class which uses the Java 1.6 Compiler Tool API to compile and run the code submitted as
 * String through the client.
 * @author sujay
 */
public class CodeRunnerEngineImpl implements CodeRunnerEngine{
    
    final Logger log = LoggerFactory.getLogger(CodeRunnerEngineImpl.class);
    
    /**
     * 
     * @param className - The class name of the code to compile
     * @param fileContent - The code content of the class
     * @return 
     */
    public List<String> compile(String className, String fileContent) {
        //log.info("Method not implemented");
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> run(String className) {
        log.info("Method not implemented");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
