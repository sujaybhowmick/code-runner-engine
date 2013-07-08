/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java.impl;

import com.codeengine.java.CompiledClassListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sujay
 */
public class CompiledClassCollector implements CompiledClassListener{
    Map<String, byte[]> classBytes = 
            Collections.synchronizedMap(new HashMap<String, byte[]>());
    public void put(String className, byte[] bytes) {
        this.classBytes.put(className, bytes);
    }

    public Map<String, byte[]> getClassBytes() {
        return Collections.unmodifiableMap(this.classBytes);
    }
    
}
