/*
 * DiagnosticMessage.fx
 *
 * Created on 02.06.2008, 10:10:28
 */

package javafx.tools;


public class DiagnosticMessage {
    public attribute line:Number ;
    public attribute message: String;
    
    public function toString (): String { "line: {line} {message}"  } 
}