package javafx.dev;

/**
 * @author Alexandr Scherbatiy, alexsch@dev.java.net
 */

import java.lang.Object;
import java.lang.System;

import javafx.ext.swing.*;
import javafx.scene.*;
import javafx.scene.paint.*;

import javafx.util.*;

import com.sun.tools.javafx.preview.*;

public class Preview extends Component{
    
    var component:Component; 
    var timer: Timer = Timer{};
    
    public var code: String ="Preview" on replace{
        timer.addTask( function():Void{ preview(code) } );
    };
    
    
    public var errors: ErrorMessage[] ;
    public var selectedError: ErrorMessage;

    var diagnosticIndex: Integer = -1 on replace{
        //System.out.println("[preview] diagnosticIndex: {diagnosticIndex}");
        if(-1 < diagnosticIndex ){
            selectedError = errors[diagnosticIndex];
        }
    };
    
    
    function preview(code: String){
        //System.out.println("----------------------------");
        //System.out.println("{code}");
        var obj = Util.executeFXCode(code);
        var unit = FXUnit.createUnit(obj);
        errors = unit.ErrorMessages;
        
        diagnosticIndex = -1;
        component = unit.content;
        
    }
    
    public function createJComponent(){
        BorderPanel{ 
            center: bind if( component != null ) then component else
                List{
                    items: bind for(item in errors) ListItem{ text: "{item}" }
                    selectedIndex: bind diagnosticIndex with inverse
                }
        }.getJComponent();
    }
    
}

function getIntegerFromLong(x: Number):Integer{
    return x.intValue();
}

public class FXUnit {
    var title: String;
    var width: Integer = 175;
    var height: Integer = 100;
    
    var menus: Menu[];
    var content: Component;
    
    var background: Color;

    var isWindow = false;

    var ErrorMessages: ErrorMessage[];


    public static function createUnit(obj: Object){
        var unit = FXUnit{};
        
        if(obj == null){
            //unit.content = Label{ text: "Compiler Error!"};
            return unit;
        } else if (obj instanceof DiagnosticCollector ){
            var diagnostics = obj as DiagnosticCollector;
            
            var messages = ["Compiler Error:"];
            var iterator = diagnostics.getDiagnostics().iterator();
            
            while(iterator.hasNext()){
                var diagnostic = iterator.next() as javax.tools.Diagnostic;
                insert ErrorMessage{
                     line: getIntegerFromLong( diagnostic.getLineNumber() )
                     position: getIntegerFromLong( diagnostic.getPosition());
                     startPosition: getIntegerFromLong( diagnostic.getStartPosition());
                     endPosition: getIntegerFromLong( diagnostic.getEndPosition());
                     //message: diagnostic.getMessage(java.util.Locale.getDefault())
                     message: diagnostic.getMessage(java.util.Locale.getDefault()).<<replace>>('\n', ' ')
                } into unit.ErrorMessages;
            }
            
            //unit.content = List{ items: for(item in messages) ListItem{ text: item } };
            
        }else if(obj instanceof Component){
            unit.content = obj as Component;
        }else if(obj instanceof Node){
            unit.content = Canvas{ content: obj as Node };
        }else if(obj instanceof SwingFrame){
        
            unit.isWindow = true;
            var frame = obj as SwingFrame;
            
            frame.visible = false;
            var title =  frame.title;
            if (title != "") { unit.title = title; }
            var w = frame.width;
            var h = frame.height;
            if ( 0 < w ){ unit.width = w; }
            if ( 0 < h ){ unit.height = h; }

            unit.menus = frame.menus; 
            unit.content = frame.content;
            //unit.background = getColor(frame.background);
            unit.background = frame.background;
            
        }
        
        return unit;
    }
    
//    static function getColor(color: javafx.gui.Color):Color{
//        return Color{
//            red: color.red
//            green: color.green
//            blue: color.blue
//        };
//        
//    }
}



