/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.impl;

import com.codeengine.ByteArrayJavaClass;
import com.codeengine.CodeRunnerEngine;
import com.codeengine.CompileError;
import com.codeengine.CompileErrorListener;
import com.codeengine.StringBuilderJavaSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
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
    public Boolean compile(String className, String fileContent, 
                         CompileErrorCollector<CompileError> errors) {
        if(className == null){
            log.debug("className is null");
            throw new IllegalArgumentException("className cannot be null");
        }
        log.info("Compiling class [" + className + "]");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //final List<ByteArrayJavaClass> classFileObjects = new ArrayList<ByteArrayJavaClass>();
        DiagnosticCollector<JavaFileObject> diagnostics = 
                new DiagnosticCollector<JavaFileObject>();
        
        JavaFileManager fileManager = compiler.getStandardFileManager(
                                                    diagnostics, null, null);
        
        fileManager = new ForwardingJavaFileManager<JavaFileManager>(fileManager){
            public JavaFileObject getJavaFileObject(Location location, final 
                    String className, JavaFileObject.Kind kind, FileObject sibling){
                ByteArrayJavaClass classFileObject = new ByteArrayJavaClass(className);
                //classFileObjects.add(classFileObject);
                return classFileObject;
            }
        };
        JavaFileObject javaSource = buildSource(className, fileContent);
        
        JavaCompiler.CompilationTask compileTask = compiler.getTask(null, 
                                        fileManager, diagnostics, 
                                        null, null, Arrays.asList(javaSource));
        Boolean result = compileTask.call();
        for (final Diagnostic<? extends JavaFileObject> diagnostic : 
                                diagnostics.getDiagnostics()) {
            log.info(diagnostic.getKind() + ": " + 
                                diagnostic.getMessage(null));
           CompileError error = new CompileError<String>(){
                public String getError() {
                   return  diagnostic.getKind() + ": " + 
                                diagnostic.getMessage(null);
                }
                
            };
            errors.reportError(error);
            
        }
        return result;
    }

    public int run(String className) {
        log.info("Method not implemented");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private JavaFileObject buildSource(final String className, 
                                final String fileContent){
        StringBuilderJavaSource javaSource = new StringBuilderJavaSource(className);
        javaSource.append(fileContent);
        return javaSource;
    }
}
