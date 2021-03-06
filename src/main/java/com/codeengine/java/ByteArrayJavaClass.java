/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;

/**
 *
 * @author sujay
 */
public class ByteArrayJavaClass extends SimpleJavaFileObject{
    
    private ByteArrayOutputStream stream;
    private String className;
    
    public ByteArrayJavaClass(final String name){
        super(URI.create("bytes:///" + name), Kind.CLASS);
        this.stream = new ByteArrayOutputStream();
        this.className = name;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return this.stream;
    }
    
    public byte[] getBytes(){
        return this.stream.toByteArray();
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }
}
