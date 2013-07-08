/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

import com.codeengine.java.CodeCompiler;
import com.codeengine.java.CodeRunResult;
import com.codeengine.java.CompileError;
import com.codeengine.java.impl.CodeCompilerImpl;
import com.codeengine.java.impl.CodeRunnerCallable;
import com.codeengine.java.impl.CompileErrorCollector;
import com.codeengine.java.impl.CompiledClassCollector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author sujay
 */
public class CodeRunnerCallableTest {
    private static final int NTHREADS = 10;
    static ExecutorService executor;
    public CodeRunnerCallableTest() {
    }
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception{
        executor = Executors.newFixedThreadPool(NTHREADS);
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception{
        executor.shutdown();
    }

    /**
     * Test of call method, of class static method call.
     */
    @Test
    public void testCallForStaticMethod() throws Exception {
        String javaFileName = "src/test/resources/TestSourceFile.txt";
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
        Future<CodeRunResult> result = executor.submit(instance);
        
        //Blocking call
        CodeRunResult codeRunResult = result.get();
        assertTrue(codeRunResult.getResult());
        Integer actual = (Integer)codeRunResult.getOutput();
        System.out.println(actual);
        assertEquals(expResult, actual);
    }
    
    @Test
    public void testCallForInstanceMethod() throws Exception {
        String javaFileName = "src/test/resources/TestSourceFile.txt";
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
        Future<CodeRunResult> result = executor.submit(instance);
        
        //Blocking call
        CodeRunResult codeRunResult = result.get();;
        assertTrue(codeRunResult.getResult()); 
        String actual = (String)codeRunResult.getOutput();
        System.out.println(actual);
        assertEquals(expResult, actual);
    }
    
    @Test
    public void testCallInstanceMethodForError() throws Exception {
        String javaFileName = "src/test/resources/TestSourceFile.txt";
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
        
        Future<CodeRunResult> result = executor.submit(instance);
        
        CodeRunResult codeRunResult = null;
        try {
            // Blocking call
            codeRunResult = result.get();
            //should not reach the next statement
            fail("Test for error in instance method invocation failed");
        } catch (Exception e) {
            // should throw an StringIndexOutOfBoundsException as expected            
            assertTrue(
                    "should throw an java.lang.StringIndexOutOfBoundsException",
                    true);
        }
    }
}