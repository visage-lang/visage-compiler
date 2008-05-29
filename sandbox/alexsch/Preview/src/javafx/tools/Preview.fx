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
    
    private function preview(code: String){
        var obj = Util.executeFXCode(code);
        var unit = FXUnit.createUnit(obj);
        component = unit.content;
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
    
    public static function createUnit(obj: Object){
        var unit = FXUnit{};
        
        if(obj == null){
            unit.content = Label{ text: "Compiler Error!"};
        } else if (obj instanceof DiagnosticCollector ){
            var diagnostics = obj as DiagnosticCollector;
            
            var messages = ["Compiler Error:"];
            var iterator = diagnostics.getDiagnostics().iterator();
            
            while(iterator.hasNext()){
                var diagnostic = iterator.next() as javax.tools.Diagnostic;
                insert "line:{diagnostic.getLineNumber()}" into messages;
                insert "{diagnostic.getMessage(java.util.Locale.getDefault())}" into messages;
            }
            
            unit.content = List{
                items: for(item in messages) ListItem{ text: item }
            };
            
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



