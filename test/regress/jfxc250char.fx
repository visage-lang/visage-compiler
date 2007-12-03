/**
 * regression test: handling non-JavaFX primitive types
 * @test
 * @run
 */
import javax.swing.JPasswordField; 
import java.lang.System;

var s = "Hello";
var ch = s.charAt(0);

var textField = new JPasswordField();    
textField.setEchoChar(ch);

var i : Integer;
i = ch;
System.out.println("{i} -- {ch}");

