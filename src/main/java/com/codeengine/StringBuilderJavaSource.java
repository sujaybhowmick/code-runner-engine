/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeengine;

import java.io.IOException;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;

/**
 *
 * @author sujay
 */
public class StringBuilderJavaSource extends SimpleJavaFileObject{
    
    private StringBuilder code;
    
    public StringBuilderJavaSource(final String name){
        super(URI.create("string:///" + name.replace(".", "/") + 
                Kind.SOURCE.extension), Kind.SOURCE);
        this.code = new StringBuilder();
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return this.code;
    }
    
    public void append(final String code){
        this.code.append(code);
        this.code.append("\n");
    }
}
