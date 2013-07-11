/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine.java;

import java.util.HashMap;
import java.util.Map;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/**
 *
 * @author sujay
 */
public class CustomForwardingFileManager<M extends JavaFileManager> extends ForwardingJavaFileManager<JavaFileManager> {

    private ByteArrayJavaClass byteArrayJavaClass;

    public CustomForwardingFileManager(final JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
            final String className, JavaFileObject.Kind kind,
            FileObject sibling) {
        ByteArrayJavaClass classFileObject =
                new ByteArrayJavaClass(className);
        this.byteArrayJavaClass = classFileObject;
        return classFileObject;
    }
    
    /**
     * @return the byteArrayJavaClasses
     */
    public ByteArrayJavaClass getByteArrayJavaClass() {
        return this.byteArrayJavaClass;
    }
}
