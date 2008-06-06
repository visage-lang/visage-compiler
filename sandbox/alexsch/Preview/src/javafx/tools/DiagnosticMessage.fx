/*
 * DiagnosticMessage.fx
 *
 * Created on 02.06.2008, 10:10:28
 */

package javafx.tools;


public class DiagnosticMessage {
    public attribute line:Integer;
    public attribute position:Integer;
    public attribute startPosition:Integer;
    public attribute endPosition:Integer;
 
    public attribute message: String;
    
    public function toString (): String { "line: {line} {message}"  } 
}