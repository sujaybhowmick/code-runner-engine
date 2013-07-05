/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.impl;

import java.util.Map;

/**
 *
 * @author sujay
 */
public class MapClassLoader extends ClassLoader {

    private Map<String, byte[]> classes;

    public MapClassLoader(Map<String, byte[]> classes) {
        this.classes = classes;
    }

    @Override
    protected Class<?> findClass(String className) throws
            ClassNotFoundException {
        byte[] classBytes = classes.get(className);
        if (classBytes == null) {
            throw new ClassNotFoundException(className);
        }
        Class<?> clazz = defineClass(className, classBytes, 0,
                classBytes.length);
        if (clazz == null) {
            throw new ClassNotFoundException(className);
        }
        return clazz;
    }
}
