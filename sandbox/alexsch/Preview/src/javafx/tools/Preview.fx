package javafx.tools;

import java.lang.Object;
import java.lang.System;

import javafx.gui.*;
import javafx.gui.Layout.*;

import javafx.util.*;

import com.sun.tools.javafx.preview.*;

public class Preview extends Component{
    
    private attribute component:Component; 
    private attribute timer: Timer = Timer{};
    
    public attribute code: String ="Preview" on replace{
        timer.addTask( function():Void{ preview(code) } );
    };
    
    
    public attribute diagnosticMessages: DiagnosticMessage[] ;
    public attribute diagnosticMessage: DiagnosticMessage on replace{
        System.out.println("diagnosticMessage: {diagnosticMessage} ");
    };

    private attribute diagnosticIndex: Integer = -1 on replace{
        if(-1 < diagnosticIndex ){
            diagnosticMessage = diagnosticMessages[diagnosticIndex];
        }
    };
    
    private function preview(code: String){
        var obj = Util.executeFXCode(code);
        var unit = FXUnit.createUnit(obj);
        var content = unit.content;
        diagnosticMessages = unit.diagnosticMessages;
        
        if(content == null){
            component = List{ 
                items: for(item in diagnosticMessages) ListItem{ text: "{item}" }
                selectedIndex: bind diagnosticIndex with inverse
            };
            
        }else{
            component = content;
        }
    }
    
    public function createJComponent(){
        BorderPanel{ center: bind component }.getJComponent();
    }
    
}


public class FXUnit {
    attribute title: String;
    attribute width: Integer = 175;
    attribute height: Integer = 100;
    
    attribute menus: Menu[];
    attribute content: Component;
    
    attribute background: Color;

    attribute isWindow = false;

    attribute diagnosticMessages: DiagnosticMessage[];


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
                insert "line:{diagnostic.getLineNumber()}" into messages;
                insert "{diagnostic.getMessage(java.util.Locale.getDefault())}" into messages;
                insert DiagnosticMessage{
                     //line: diagnostic.getLineNumber()
                     line: diagnostic.getPosition()
                     message: diagnostic.getMessage(java.util.Locale.getDefault())
                } into unit.diagnosticMessages;
            }
            
            //unit.content = List{ items: for(item in messages) ListItem{ text: item } };
            
        }else if(obj instanceof javafx.gui.Component){
            unit.content = obj as javafx.gui.Component;
        }else if(obj instanceof javafx.gui.Node){
            unit.content = Canvas{ content: obj as javafx.gui.Node };
        }else if(obj instanceof javafx.gui.Frame){
        
            unit.isWindow = true;
            var frame = obj as javafx.gui.Frame;
            
            frame.visible = false;
            var title =  frame.title;
            if (title <> "") { unit.title = title; }
            var w = frame.width;
            var h = frame.height;
            if ( 0 < w ){ unit.width = w; }
            if ( 0 < h ){ unit.height = h; }

            unit.menus = frame.menus; 
            unit.content = frame.content;
            unit.background = getColor(frame.background);
            
        }
        
        return unit;
    }
    
    static function getColor(color: javafx.gui.Color):Color{
        return Color{
            red: color.red
            green: color.green
            blue: color.blue
        };
        
    }
}



