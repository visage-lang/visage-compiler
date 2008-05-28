package assortis.core;

/**
* @author Alexandr Scherbatiy
*/


import java.lang.Object;

import javafx.gui.*;


import java.util.Locale;
import javax.tools.Diagnostic;
import com.sun.javafx.runtime.sequence.Sequence;


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
            
            var errorMessage = "Compiler Error:";
            var iterator = diagnostics.getDiagnostics().iterator();
            
            while(iterator.hasNext()){
                var diagnostic = iterator.next() as Diagnostic;
                errorMessage = "{errorMessage}\nline:{diagnostic.getLineNumber()}";
                errorMessage = "{errorMessage}\n{diagnostic.getMessage(Locale.getDefault())}";
            }
            
            unit.content = Label{ text: errorMessage };
            
        }
        
        //java.lang.System.out.println("[object] {obj}" );
        
        if(obj instanceof javafx.gui.Component){
            
            var component = obj as javafx.gui.Component;
            //unit.content = CustomWidget{  comp: component.getJComponent() };
            unit.content = component;
        }else if(obj instanceof javafx.gui.Node){
            var node = obj as javafx.gui.Node;
            var canvas = javafx.gui.Canvas { content: node };
            //unit.content = CustomWidget{  comp: canvas.getJComponent() };
            unit.content = Canvas{ content: node };
            
        }else if(obj instanceof javafx.gui.Frame){
        
            unit.isWindow = true;
            var frame = obj as javafx.gui.Frame;
            
            //java.lang.System.out.println("[visible] set: {false}" );
            frame.visible = false;
            var title =  frame.title;
            if (title <> "") { unit.title = title; }
            var w = frame.width;
            var h = frame.height;
            if ( 0 < w ){ unit.width = w; }
            if ( 0 < h ){ unit.height = h; }

            unit.menus = frame.menus; 
            //unit.content = CustomWidget{ comp: frame.content.getJComponent() }
            unit.content = frame.content;
            unit.background = getColor(frame.background);
            
            
//        }else if(obj instanceof Sequence ){
//            //var sequence = obj as Sequence;
//            var sequence = obj as java.lang.Iterable;
//            var iterator = sequence.iterator();
//            
//            var panel = FlowPanel{};
//            while(iterator.hasNext()){
//                var element = iterator.next();
//                
//                if(element instanceof javafx.gui.Component){
//                    var elem = element as javafx.gui.Component;
//                    insert CustomWidget{  comp: elem.getJComponent() } into panel.content;
//                }
//                unit.content = panel;
//            }
//            
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