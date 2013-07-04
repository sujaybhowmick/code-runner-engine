/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine;

import com.codeengine.impl.CodeRunnerEngineImpl;
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
public class CodeRunnerEngineTest {

    CodeRunnerEngine engine;

    public CodeRunnerEngineTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.engine = new CodeRunnerEngineImpl();
    }

    @After
    public void tearDown() {
        this.engine = null;
    }

    @Test
    public void testCompile() throws Exception {
        assertNotNull(this.engine);
        this.engine.compile(null, null);
    }
}