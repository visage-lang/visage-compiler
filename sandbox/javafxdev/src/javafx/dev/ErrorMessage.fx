/*
 * ErrorMessage.fx
 *
 * Created on 02.06.2008, 10:10:28
 */

package javafx.dev;


public class ErrorMessage {
    public var line:Integer;
    public var position:Integer;
    public var startPosition:Integer;
    public var endPosition:Integer;
 
    public var message: String;
    
    public function toString (): String { "line: {line} {message}"  } 
}
