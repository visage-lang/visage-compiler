/*
 * Shell.fx
 *
 * Created on Jun 26, 2008, 4:59:35 PM
 */

package javafx.dev.shell;

/**
 * @author Alexandr Scherbatiy, alexsch@dev.java.net
 */

import java.lang.System;
import com.sun.tools.javafx.preview.*;


var args = __ARGS__;

var task = args[0];
System.out.println("[shell] task: {task}");
var obj = Util.executeFXFile(task);

if (obj instanceof DiagnosticCollector){
    var diagnostics = obj as DiagnosticCollector;
            
            var messages = ["Compiler Error:"];
            var iterator = diagnostics.getDiagnostics().iterator();
            
            while(iterator.hasNext()){
                var diagnostic = iterator.next() as javax.tools.Diagnostic;
                System.out.println("{diagnostic.getLineNumber()} { diagnostic.getMessage(java.util.Locale.getDefault())}");
//                insert ErrorMessage{
//                     line: getIntegerFromLong( diagnostic.getLineNumber() )
//                     position: getIntegerFromLong( diagnostic.getPosition());
//                     startPosition: getIntegerFromLong( diagnostic.getStartPosition());
//                     endPosition: getIntegerFromLong( diagnostic.getEndPosition());
//                     //message: diagnostic.getMessage(java.util.Locale.getDefault())
//                     message: diagnostic.getMessage(java.util.Locale.getDefault()).<<replace>>('\n', ' ')
//                } into unit.ErrorMessages;
            
            }
            

}




//var args = __ARGS__;
//System.out.println("[shell] {__ARGS__}");
//for( arg in args){
//    System.out.println("arg: {arg}");
//}    
//
//
//public class Shell {
//    public function main () {  
//
//    } 
//}
