/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

import com.codeengine.common.Result;
import com.codeengine.java.impl.CodeCompilerImpl;
import com.codeengine.java.impl.CodeRunnerCallable;
import com.codeengine.java.impl.CompileErrorCollector;
import com.codeengine.java.impl.CompiledClassCollector;
import com.codeengine.java.impl.RunResultCollector;
import java.util.List;
import java.util.concurrent.Callable;
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
        CodeCompiler engine = CodeCompilerImpl.newInstance();
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassListener compiledClassCollector = 
                                    CompiledClassCollector.newInstance();
        Boolean compileResult = engine.compile(classFileName, fileContents, 
                                compileErrors, compiledClassCollector);
        // No compiler Errors
        assertTrue(compileResult);
        
        String fqcn = compiledClassCollector.getFQCN();
        Object[] params = {1, 2};
        
        Callable<RunResultCollector> instance = 
                CodeRunnerCallable.newInstance(
                compiledClassCollector.getClassBytes(),
                        fqcn, methodToInvoke, params);
        Integer expResult = 3;
        Future<RunResultCollector> result = executor.submit(instance);
        
        //Blocking call
        RunResultCollector runResult = result.get();
        CodeRunResult codeRunResult = runResult.getOutput();
        assertTrue(codeRunResult.isSuccess());
        Integer actual = codeRunResult.getOutput();
        assertEquals(expResult, actual);
    }
    
    @Test
    public void testCompileFailure() throws Exception {
        CodeCompiler engine = CodeCompilerImpl.newInstance();
        final String javaFileName = 
                "src/test/resources/CompileErrorTestFile.txt";
        final String classFileName = "CompileErrorTestFile";
        String fileContents = FileUtils.fileRead(javaFileName);
        assertNotNull(fileContents);
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassListener compiledClassCollector = 
                                    CompiledClassCollector.newInstance();
        Boolean compileResult = engine.compile(classFileName, fileContents, 
                                compileErrors, compiledClassCollector);
        // Compile failed
        assertFalse(compileResult);
        
        List<? extends Result> errors = compileErrors.getErrors();
        assertFalse(errors.isEmpty());
        byte[] byteCode = compiledClassCollector.getClassBytes().
                                    get(compiledClassCollector.getFQCN());        
        assertNull(byteCode);
        
    }
    
    @Test
    public void testCallForStaticMethodWrapperClass() throws Exception {
        String javaFileName = "src/test/resources/TestSourceFile.txt";
        String classFileName = "TestSourceFile";
        String methodToInvoke = "add2";
        String fileContents = FileUtils.fileRead(javaFileName);
        CodeCompiler engine = CodeCompilerImpl.newInstance();
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassListener compiledClassCollector = 
                                        CompiledClassCollector.newInstance();
        Boolean compileResult = engine.compile(classFileName, fileContents, 
                            compileErrors, compiledClassCollector);
        // No compiler Errors
        assertTrue(compileResult);
        
        String fqcn = compiledClassCollector.getFQCN();
        Object[] params = {2, 3};
        
        Callable<RunResultCollector> instance = 
                CodeRunnerCallable.newInstance(
                compiledClassCollector.getClassBytes(),
                        fqcn, methodToInvoke, params);
        Integer expResult = 5;
        Future<RunResultCollector> result = executor.submit(instance);
        
        //Blocking call
        RunResultCollector runResult = result.get();
        CodeRunResult codeRunResult = runResult.getOutput();
        assertTrue(codeRunResult.isSuccess());
        assertEquals(expResult, codeRunResult.getOutput());
    }
    
    
    
     @Test
    public void testCallForInstanceMethodSubtract() throws Exception {
        String javaFileName = "src/test/resources/TestSourceFile.txt";
        String classFileName = "TestSourceFile";
        String methodToInvoke = "subtract";
        String fileContents = FileUtils.fileRead(javaFileName);
        CodeCompiler engine = CodeCompilerImpl.newInstance();
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassListener compiledClassCollector = 
                                        CompiledClassCollector.newInstance();        
        Boolean compileResult = engine.compile(classFileName, fileContents, 
                compileErrors, compiledClassCollector);
        // No compiler Errors
        assertTrue(compileResult);
        
        String fqcn = compiledClassCollector.getFQCN();
        Object[] params = {10, 2};
        
        Callable<RunResultCollector> instance = 
                CodeRunnerCallable.newInstance(
                compiledClassCollector.getClassBytes(),
                        fqcn, methodToInvoke, params);
        Integer expResult = 8;
        Future<RunResultCollector> result = executor.submit(instance);
        
        //Blocking call
        RunResultCollector runResult = result.get();
        CodeRunResult codeRunResult = runResult.getOutput();
        assertTrue(codeRunResult.isSuccess());
        assertEquals(expResult, codeRunResult.getOutput());
    }
    
    @Test
    public void testCallForInstanceMethod() throws Exception {
        String javaFileName = "src/test/resources/TestSourceFile.txt";
        String classFileName = "TestSourceFile";
        String methodToInvoke = "reverseString";
        String fileContents = FileUtils.fileRead(javaFileName);
        CodeCompiler engine = CodeCompilerImpl.newInstance();
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassListener compiledClassCollector = 
                                        CompiledClassCollector.newInstance();
        Boolean compileResult = engine.compile(classFileName, fileContents, 
                compileErrors, compiledClassCollector);
        // No compiler Errors
        assertTrue(compileResult);
        String fqcn = compiledClassCollector.getFQCN();
        Object[] params = {"Sujay"};
        
        Callable<RunResultCollector> instance = 
                CodeRunnerCallable.newInstance(
                compiledClassCollector.getClassBytes(),
                        fqcn, methodToInvoke, params);
        String expResult = "yajuS";
        Future<RunResultCollector> result = executor.submit(instance);
        
        //Blocking call
        RunResultCollector runResult = result.get();
        CodeRunResult codeRunResult = runResult.getOutput();
        assertTrue(codeRunResult.isSuccess());
        assertEquals(expResult, codeRunResult.getOutput());
    }
    
    @Test
    public void testCallInstanceMethodForError() throws Exception {
        String javaFileName = "src/test/resources/TestSourceFile.txt";
        String classFileName = "TestSourceFile";
        String methodToInvoke = "errorMethod";
        String fileContents = FileUtils.fileRead(javaFileName);
        CodeCompiler engine = CodeCompilerImpl.newInstance();
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassListener compiledClassCollector = 
                                        CompiledClassCollector.newInstance();
        Boolean compileResult = engine.compile(classFileName, fileContents, 
                compileErrors, compiledClassCollector);
        // No compiler Errors
        assertTrue(compileResult);
        
        Object[] params = {"Sujay"};
        String fqcn = compiledClassCollector.getFQCN();
        Callable<RunResultCollector> instance = 
                CodeRunnerCallable.newInstance(
                compiledClassCollector.getClassBytes(),
                        fqcn, methodToInvoke, params);
        String expResult = "yajuS";
        
        Future<RunResultCollector> result = executor.submit(instance);
        
        //Blocking call
        RunResultCollector runResult = result.get();
        CodeRunResult codeRunResult = runResult.getOutput();
        assertFalse(codeRunResult.isSuccess());
        
        Object actual = codeRunResult.getOutput();
        assertNotNull(actual);
        System.out.println(actual);
    }
    
    @Test
    public void testCallForPackageStaticMethod() throws Exception {
        String javaFileName = "src/test/resources/PackageSourceTestFile.txt";
        String classFileName = "PackageSourceTestFile";
        String methodToInvoke = "add";
        String fileContents = FileUtils.fileRead(javaFileName);
        CodeCompiler engine = CodeCompilerImpl.newInstance();
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassListener compiledClassCollector = 
                                        CompiledClassCollector.newInstance();
        Boolean compileResult = engine.compile(classFileName, fileContents, 
                compileErrors, compiledClassCollector);
        // No compiler Errors
        assertTrue(compileResult);
        
        String fqcn = compiledClassCollector.getFQCN();
        Object[] params = {1, 2};
        
        Callable<RunResultCollector> instance = 
                CodeRunnerCallable.newInstance(
                compiledClassCollector.getClassBytes(),
                        fqcn, methodToInvoke, params);
        Integer expResult = 3;
        Future<RunResultCollector> result = executor.submit(instance);
        
        //Blocking call
        RunResultCollector runResult = result.get();
        CodeRunResult codeRunResult = runResult.getOutput();
        assertTrue(codeRunResult.isSuccess());
        assertEquals(expResult, codeRunResult.getOutput());
    }
    
    @Test
    public void testCallForPackageInstanceMethod() throws Exception {
        String javaFileName = "src/test/resources/PackageSourceTestFile.txt";
        String classFileName = "PackageSourceTestFile";
        String methodToInvoke = "reverseString";
        String fileContents = FileUtils.fileRead(javaFileName);
        CodeCompiler engine = CodeCompilerImpl.newInstance();
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassListener compiledClassCollector = 
                                        CompiledClassCollector.newInstance();
        Boolean compileResult = engine.compile(classFileName, fileContents, 
                compileErrors, compiledClassCollector);
        // No compiler Errors
        assertTrue(compileResult);
        
        String fqcn = compiledClassCollector.getFQCN();
        Object[] params = {"Sujay"};
        Callable<RunResultCollector> instance = 
                CodeRunnerCallable.newInstance(
                compiledClassCollector.getClassBytes(),
                        fqcn, methodToInvoke, params);
        String expResult = "yajuS";
        Future<RunResultCollector> result = executor.submit(instance);
        
        //Blocking call
        RunResultCollector runResult = result.get();
        CodeRunResult codeRunResult = runResult.getOutput();
        assertTrue(codeRunResult.isSuccess());
        assertEquals(expResult, codeRunResult.getOutput());
    }
}