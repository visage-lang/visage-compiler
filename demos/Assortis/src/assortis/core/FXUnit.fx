package assortis.core;

/**
* @author Alexandr Scherbatiy
*/


import java.lang.Object;

import javafx.ext.swing.*;

import javafx.application.*;

import javafx.scene.paint.*;


import java.util.Locale;
import javax.tools.Diagnostic;
import com.sun.javafx.runtime.sequence.Sequence;


public class FXUnit {
    attribute title: String;
    attribute width: Integer = 175;
    attribute height: Integer = 100;
    
    attribute menus: Menu[];
    //attribute content: Component;
    attribute stage: Stage;
    
    attribute background: Color;

    attribute isWindow = false;
    
    public static function createUnit(obj: Object){
        var unit = FXUnit{};
        
        if(obj == null){
            unit.stage = Stage{ 
                content: ComponentView{ 
                    component: Label{ text: "Compiler Error!"}
                    }
            };
        } else if (obj instanceof DiagnosticCollector ){
            var diagnostics = obj as DiagnosticCollector;
            
            var errorMessage = "Compiler Error:";
            var iterator = diagnostics.getDiagnostics().iterator();
            
            while(iterator.hasNext()){
                var diagnostic = iterator.next() as Diagnostic;
                errorMessage = "{errorMessage}\nline:{diagnostic.getLineNumber()}";
                errorMessage = "{errorMessage}\n{diagnostic.getMessage(Locale.getDefault())}";
            }
            
            unit.stage =  Stage{ 
                content: ComponentView{ 
                    component: Label{ text: errorMessage }
                    }
            };
            
        }
        
        //java.lang.System.out.println("[object] {obj}" );
        
        if(obj instanceof javafx.ext.swing.Component){
            
            var component = obj as javafx.ext.swing.Component;
            //unit.content = CustomWidget{  comp: component.getJComponent() };
            //unit.content = component;
            unit.stage =  Stage{ 
                content: ComponentView{  component: component }
            };
            
        }else if(obj instanceof javafx.scene.Node){
            var node = obj as javafx.scene.Node;
            var canvas = javafx.ext.swing.Canvas { content: node };
            //unit.content = CustomWidget{  comp: canvas.getJComponent() };
            unit.stage = Stage{ content: node };
            
        }else if(obj instanceof javafx.ext.swing.SwingFrame){
        
            unit.isWindow = true;
            var frame = obj as javafx.ext.swing.SwingFrame;
            
            //java.lang.System.out.println("[visible] set: {false}" );
            frame.visible = false;
            var title =  frame.title;
            if (title != "") { unit.title = title; }
            var w = frame.width;
            var h = frame.height;
            if ( 0 < w ){ unit.width = w; }
            if ( 0 < h ){ unit.height = h; }

            unit.menus = frame.menus; 
            //unit.content = CustomWidget{ comp: frame.content.getJComponent() }

            unit.stage = Stage{ 
                content: ComponentView{ component: frame.content }
            };
            
            unit.background = frame.background;
            
          }else if(obj instanceof javafx.application.Frame ){
            var frame = obj as javafx.application.Frame;
            
            unit = createFrame(frame.title,frame.width, frame.height, frame.stage);
            
//        }else if(obj instanceof Sequence ){
//            //var sequence = obj as Sequence;
//            var sequence = obj as java.lang.Iterable;
//            var iterator = sequence.iterator();
//            
//            var panel = FlowPanel{};
//            while(iterator.hasNext()){
//                var element = iterator.next();
//                
//                if(element instanceof javafx.ext.swing.Component){
//                    var elem = element as javafx.ext.swing.Component;
//                    insert CustomWidget{  comp: elem.getJComponent() } into panel.content;
//                }
//                unit.content = panel;
//            }
//            
        }
        
        return unit;
    }
    
    
}

function createFrame(title: String, width:Integer, height:Integer, stage: Stage): FXUnit {
    
    var unit = FXUnit{
        isWindow: true
        stage: stage
    }; 
    
    if (title != "") { unit.title = title; }

    if ( 0 < width ){ unit.width = width; }
    if ( 0 < height ){ unit.height = height; }
    
    return unit;
}
