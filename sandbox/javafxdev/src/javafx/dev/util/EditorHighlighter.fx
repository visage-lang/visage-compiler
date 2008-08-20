package javafx.dev.util;

/**
 * @author Alexandr Scherbatiy, alexsch@dev.java.net
 */

import javafx.dev.*;

import java.lang.System;
import javax.swing.text.JTextComponent;

public class EditorHighlighter{
    
    
    public var component: JTextComponent;

    public static var KEY_WORDS:String[] =  ["import"];

    
    public function clear(){
        var highlight = component.getHighlighter();
        highlight.removeAllHighlights();
    }
    
    public function highlightKeyWords(){
        //System.out.println("[highlight] keywords");
        var hilite = component.getHighlighter();
        //var painter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new java.awt.Color(188,207,246));
        var painter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new java.awt.Color(222,236,242));

        var content = component.getText();
        //System.out.println("  content:\n {content}");
        
        for(keyWord in KEY_WORDS){
            //System.out.println("  keyword: '{keyWord}'");
            
            
            var index = content.indexOf(keyWord, 0);
            
            while (0 <= index){
                var end = index + keyWord.length();
                //System.out.println("  positions: {index} - {end}");
                hilite.addHighlight(index, end, painter);
                index = content.indexOf(keyWord, end);
                
            }
            
            
        }
       
    }
    
    public function highlightErrors(ErrorMessages: ErrorMessage[]){
        for(errorMessage in ErrorMessages){
            highlightError(errorMessage);
        }
    }
    
    public function highlightError(errorMessage: ErrorMessage){
        var highlight = component.getHighlighter();
        var painter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new java.awt.Color(254,134,126));
        var startPosition = errorMessage.startPosition;
        var endPosition = errorMessage.endPosition;
        //System.out.println("\n[highliter] error: [{startPosition} - {endPosition}]");
        if(startPosition == endPosition){
            startPosition -= 3;
            endPosition;
        }
        
        highlight.addHighlight(startPosition, endPosition, painter);
        
    }
    
}

