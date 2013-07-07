/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine;

import com.codeengine.impl.CodeCompilerImpl;
import com.codeengine.impl.CompileErrorCollector;
import com.codeengine.impl.CompiledClassCollector;
import java.util.List;
import java.util.Map;
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
    public void testCompileClass() throws Exception {
        assertNotNull(this.engine);
        final String javaFileName = "src/test/resources/TestSourceFile.java";
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
                                            get(classFileName);
        assertNotNull(byteCode);
        assertTrue(byteCode.length > 0);
        
    }

    @Test
    public void testCompileInterface() throws Exception {
        assertNotNull(this.engine);
        final String javaFileName = 
                "src/test/resources/AnotherTestSourceFile.java";
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
                                            get(classFileName);
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
        List<? extends CompileError> errors = compileErrors.getErrors();
        assertFalse(errors.isEmpty());
        byte[] byteCode = compiledClassCollector.getClassBytes().
                                            get(classFileName);        
        assertNull(byteCode);
        
    }
}