/*
 * Delete.fx
 *
 * Created on Jun 26, 2008, 4:26:13 PM
 */

package javafx.dev.shell;

/**
 * @author Alexandr Scherbatiy, alexsch@dev.java.net
 */

import java.io.*;

import java.lang.System;

public class Delete extends ShellTask{
    public var file: String;
    
    public function execute () {  
        System.out.println("[delete] file: {file}");
        (new File(file)).<<delete>>();
        
    } 
}
