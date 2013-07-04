/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sujay
 */
public class StringBuilderJavaSourceTest {
    
    public StringBuilderJavaSourceTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getCharContent method, of class StringBuilderJavaSource.
     */
    @Test
    public void testGetCharContent() throws Exception {
        System.out.println("getCharContent");
        boolean ignoreEncodingErrors = false;
        StringBuilderJavaSource instance = new StringBuilderJavaSource("sourcecode1.java");
        instance.append("Hello World");
        CharSequence expResult = "Hello World";
        CharSequence result = instance.getCharContent(ignoreEncodingErrors);
        assertEquals(expResult, result);
    }

    /**
     * Test of append method, of class StringBuilderJavaSource.
     */
    @Test
    public void testAppend() throws Exception{
        System.out.println("append");
        boolean ignoreEncodingErrors = false;
        String code = "Hello World";
        StringBuilderJavaSource instance = new StringBuilderJavaSource("sourcecode1.java");
        instance.append(code);
        CharSequence result = instance.getCharContent(ignoreEncodingErrors);
        CharSequence expResult = "Hello World";
        assertEquals(expResult, result);
    }
}