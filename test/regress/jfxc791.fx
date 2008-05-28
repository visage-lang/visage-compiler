/*
 * Regression test for JFXC-791. Access to top level var from anonymous 
 * interface passed to static java method call fails to compile
 * @test
 */ 


import java.lang.*;
import javax.swing.*;

var abc = "ABC";
SwingUtilities.invokeLater(Runnable {
        public function run(): Void {
            System.out.println("in invoke ABC={abc}");
        }
    }); 
    
    
