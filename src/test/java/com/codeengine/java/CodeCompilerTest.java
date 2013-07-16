/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

import com.codeengine.java.impl.CodeCompilerImpl;
import com.codeengine.java.impl.CompileErrorCollector;
import com.codeengine.java.impl.CompiledClassCollector;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sujay
 */
public class CodeCompilerTest {

    CodeCompiler engine;

    public CodeCompilerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.engine = new CodeCompilerImpl();
    }

    @After
    public void tearDown() {
        this.engine = null;
    }
    
    @Test
    public void testClassNameNullEmpty(){
        final String javaFileName = "src/test/resources/TestSourceFile.txt";
        final String classFileName = null;
        try {
            this.engine.compile(classFileName, null,null, null);
            fail("should throw IllegalArgumentException");
        }catch(IllegalArgumentException e){
            assertTrue(
                "IllegalArgumentException is excepted as className is null",
                        true);
        }
        
        try {
            this.engine.compile("", null,null, null);
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(
                "IllegalArgumentException is excepted as className is empty",
                        true);
        }
    }

    @Test
    public void testCompileClass() throws Exception {
        assertNotNull(this.engine);
        final String javaFileName = "src/test/resources/TestSourceFile.txt";
        final String classFileName = "TestSourceFile";
        String fileContents = FileUtils.fileRead(javaFileName);
        assertNotNull(fileContents);
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassCollector compiledClassCollector = 
                new CompiledClassCollector();
        assertTrue(this.engine.compile(classFileName, fileContents, 
                                        compileErrors, compiledClassCollector));
        assertTrue(compileErrors.getErrors().isEmpty());
        byte[] byteCode = compiledClassCollector.getClassBytes().
                                get(compiledClassCollector.getFQCN());
        assertNotNull(byteCode);
        assertTrue(byteCode.length > 0);
        
    }

    @Test
    public void testCompileInterface() throws Exception {
        assertNotNull(this.engine);
        final String javaFileName = 
                "src/test/resources/AnotherTestSourceFile.txt";
        final String classFileName = "AnotherTestSourceFile";
        String fileContents = FileUtils.fileRead(javaFileName);
        assertNotNull(fileContents);
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassCollector compiledClassCollector = 
                new CompiledClassCollector();
        assertTrue(this.engine.compile(classFileName, fileContents, 
                                        compileErrors, compiledClassCollector));
        assertTrue(compileErrors.getErrors().isEmpty());
        byte[] byteCode = compiledClassCollector.getClassBytes().
                            get(compiledClassCollector.getFQCN());
        assertNotNull(byteCode);
        assertTrue(byteCode.length > 0);
    }
    
    @Test
    public void testCompileFailure() throws Exception {
        assertNotNull(this.engine);
        final String javaFileName = 
                "src/test/resources/CompileErrorTestFile.txt";
        final String classFileName = "CompileErrorTestFile";
        String fileContents = FileUtils.fileRead(javaFileName);
        assertNotNull(fileContents);
        CompileErrorCollector<CompileError> compileErrors = 
                new CompileErrorCollector<CompileError>();
        CompiledClassCollector compiledClassCollector = 
                new CompiledClassCollector();
        assertFalse(this.engine.compile(classFileName, fileContents, 
                                        compileErrors, compiledClassCollector));
        List<? extends Result> errors = compileErrors.getErrors();
        assertFalse(errors.isEmpty());
        byte[] byteCode = compiledClassCollector.getClassBytes().
                                    get(compiledClassCollector.getFQCN());        
        assertNull(byteCode);
        
    }
}