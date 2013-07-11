/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

import com.codeengine.java.ByteArrayJavaClass;
import com.codeengine.java.CompiledClassListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.tools.SimpleJavaFileObject;

/**
 *
 * @author sujay
 */
public class CompiledClassCollector implements CompiledClassListener{
    Map<String, byte[]> classBytes = 
            Collections.synchronizedMap(new HashMap<String, byte[]>());
    private ByteArrayJavaClass byteArrayJavaClass;
    public void put(String className, byte[] bytes) {
        this.classBytes.put(className, bytes);
    }

    public Map<String, byte[]> getClassBytes() {
        return Collections.unmodifiableMap(this.classBytes);
    }
    
    public void add(ByteArrayJavaClass byteArrayJavaClass){
        this.byteArrayJavaClass = byteArrayJavaClass;
        put(byteArrayJavaClass.getClassName(), byteArrayJavaClass.getBytes());
    }

    /**
     * @return the byteArrayJavaClass
     */
    public String getFQCN() {
        if(this.byteArrayJavaClass != null){
            return this.byteArrayJavaClass.getClassName();
        }
        return "";
        
    }
}
