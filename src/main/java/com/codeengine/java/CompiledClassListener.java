/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

import java.util.Map;

/**
 *
 * @author sujay
 */
public interface CompiledClassListener {
    
    void put(String className, byte[] bytes);
    
    Map<String, byte[]> getClassBytes();
    
}
