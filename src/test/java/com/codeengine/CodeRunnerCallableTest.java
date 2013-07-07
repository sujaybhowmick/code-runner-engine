/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine;

import com.codeengine.CodeCompiler;
import com.codeengine.CodeRunResult;
import com.codeengine.CompileError;
import com.codeengine.impl.CodeCompilerImpl;
import com.codeengine.impl.CodeRunnerCallable;
import com.codeengine.impl.CompileErrorCollector;
import com.codeengine.impl.CompiledClassCollector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sujay
 */
public class CodeRunnerCallableTest {
    private static final int NTHREADS = 10;
    ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
    public CodeRunnerCallableTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of call method, of class static method call.
     */
    @Test
    public void testCallForStaticMethod() throws Exception {
        String javaFileName = "src/test/resources/TestSourceFile.java";
        String classFileName = "TestSourceFile";
        String methodToInvoke = "add";
        String fileContents = FileUtils.fileRead(javaFileName);
        CodeCompiler engine = new CodeCompilerImpl();
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassCollector compiledClassCollector = 
                new CompiledClassCollector();
        engine.compile(classFileName, fileContents, compileErrors, 
                        compiledClassCollector);
        Object[] params = {1, 2};
        Class[] paramTypes = {int.class, int.class};
        CodeRunnerCallable instance = 
                new CodeRunnerCallable(compiledClassCollector.getClassBytes(),
                        classFileName, methodToInvoke, paramTypes, params);
        Integer expResult = 3;
        //CodeRunResult result = instance.call();
        Future<CodeRunResult> result = executor.submit(instance);
        executor.shutdown();
        while(!executor.isTerminated()){}
        CodeRunResult codeRunResult = result.get();
        Integer actual = (Integer)codeRunResult.getOutput();
        System.out.println(actual);
        assertEquals(expResult, actual);
    }
    
    @Test
    public void testCallForInstanceMethod() throws Exception {
        String javaFileName = "src/test/resources/TestSourceFile.java";
        String classFileName = "TestSourceFile";
        String methodToInvoke = "reverseString";
        String fileContents = FileUtils.fileRead(javaFileName);
        CodeCompiler engine = new CodeCompilerImpl();
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassCollector compiledClassCollector = 
                new CompiledClassCollector();
        engine.compile(classFileName, fileContents, compileErrors, 
                        compiledClassCollector);
        Object[] params = {"Sujay"};
        Class[] paramTypes = {String.class};
        
        CodeRunnerCallable instance = 
                new CodeRunnerCallable(compiledClassCollector.getClassBytes(),
                        classFileName, methodToInvoke, paramTypes, params);
        String expResult = "yajuS";
        //CodeRunResult result = instance.call();
        Future<CodeRunResult> result = executor.submit(instance);
        executor.shutdown();
        
        while(!executor.isTerminated()){}
        
        CodeRunResult codeRunResult = null;
        codeRunResult = result.get();
        String actual = (String)codeRunResult.getOutput();
        System.out.println(actual);
        assertEquals(expResult, actual);
    }
    
    @Test
    public void testCallInstanceMethodForError() throws Exception {
        String javaFileName = "src/test/resources/TestSourceFile.java";
        String classFileName = "TestSourceFile";
        String methodToInvoke = "errorMethod";
        String fileContents = FileUtils.fileRead(javaFileName);
        CodeCompiler engine = new CodeCompilerImpl();
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassCollector compiledClassCollector = 
                new CompiledClassCollector();
        engine.compile(classFileName, fileContents, compileErrors, 
                        compiledClassCollector);
        Object[] params = {"Sujay"};
        Class[] paramTypes = {String.class};
        
        CodeRunnerCallable instance = 
                new CodeRunnerCallable(compiledClassCollector.getClassBytes(),
                        classFileName, methodToInvoke, paramTypes, params);
        String expResult = "yajuS";
        //CodeRunResult result = instance.call();
        Future<CodeRunResult> result = executor.submit(instance);
        executor.shutdown();
        
        while(!executor.isTerminated()){}
        
        CodeRunResult codeRunResult = null;
        try {
            codeRunResult = result.get();
            //should not read the next statement
            fail("Test for error in instance method invocation failed");
        } catch (Exception e) {
            // should throw an excpetion as expected
            assertTrue("should throw an excpetion java.lang.StringIndexOutOfBoundsException", true);
        }
    }
}