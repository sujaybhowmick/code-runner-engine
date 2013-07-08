/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

import com.codeengine.java.ByteArrayJavaClass;
import com.codeengine.java.CodeCompiler;
import com.codeengine.java.CompileError;
import com.codeengine.java.StringBuilderJavaSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
 * Simple Class which uses the Java 1.6 Compiler Tool API to compile and run the
 * code submitted as String through the client.
 *
 * @author sujay
 */
public class CodeCompilerImpl implements CodeCompiler {

    final Logger log = LoggerFactory.getLogger(CodeCompilerImpl.class);
    
    /**
     *
     * @param className - The class name of the code to compile
     * @param fileContent - The code content of the class
     * @return
     */
    public Boolean compile(String className, String fileContent,
            CompileErrorCollector<CompileError> errors, 
            CompiledClassCollector compiledClassCollector) {
        if (className == null || className.isEmpty()) {
            log.debug("className is null");
            throw new IllegalArgumentException("className cannot be null");
        }
        log.info("Compiling class [" + className + "]");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final Map<String, ByteArrayJavaClass> byteArrayJavaClasses = new HashMap<String, ByteArrayJavaClass>();
        DiagnosticCollector<JavaFileObject> diagnostics =
                new DiagnosticCollector<JavaFileObject>();

        JavaFileManager fileManager = compiler.getStandardFileManager(
                diagnostics, null, null);

        fileManager = 
                new ForwardingJavaFileManager<JavaFileManager>(fileManager) {
            public JavaFileObject getJavaFileForOutput(Location location, 
                    final String className, JavaFileObject.Kind kind, 
                    FileObject sibling){
                ByteArrayJavaClass classFileObject = 
                        new ByteArrayJavaClass(className);
                
                //classFileObjects.add(classFileObject);
                
                byteArrayJavaClasses.put(className, classFileObject);
                return classFileObject;
            }
        };
        JavaFileObject javaSource = buildSource(className, fileContent);

        JavaCompiler.CompilationTask compileTask = compiler.getTask(null,
                fileManager, diagnostics,
                null, null, Arrays.asList(javaSource));
        Boolean result = compileTask.call();
        try {
            fileManager.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if(result){
            log.info("Compile Successful, collecting byteCode");
            compiledClassCollector.put(className, byteArrayJavaClasses.get(className).getBytes());
        }else {
            for (final Diagnostic<? extends JavaFileObject> diagnostic
                    : diagnostics.getDiagnostics()) {
                log.info(diagnostic.getKind() + ": "
                        + diagnostic.getMessage(null));
                CompileError error = new CompileError<String>() {
                    public String getError() {
                        return diagnostic.getKind() + ": "
                                + diagnostic.getMessage(null);
                    }
                };
                errors.reportError(error);
            }
        }
        return result;
    }

    private JavaFileObject buildSource(final String className,
            final String fileContent) {
        StringBuilderJavaSource javaSource = 
                new StringBuilderJavaSource(className);
        javaSource.append(fileContent);
        return javaSource;
    }
}
