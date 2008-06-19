package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

 import javafx.gui.*;
 import javafx.gui.swing.*;

 import java.lang.System;
 
public class MDUniverse extends CustomNode, MDGroup{

    public attribute dimension: Number ;
    
    public attribute projection: IMDTransform;
    
    private attribute totalTransform: IMDSquareTransform ;
    
    public function create ():Node {
//        System.out.println("[universe]");
        
//        for(point in getPoints()){
//            System.out.println("point: {point}");
//        }
//        System.out.println("[universe] getSegments()");
//        for(segment in  getSegments()) {
//            System.out.println("segment: {segment}");
//        }

        Group{
            content: [ 
                for(point in getPoints()) Circle{
                var point2D = projection.transform(point)
                centerX: point2D.getElem(0)
                centerY: point2D.getElem(1)
                radius: 2
                fill: Color.BLACK
            },
            for(segment in  getSegments()) 
                Line{
                    var totalTransform = bind MDTransform.composite(transforms, dimension) 
                    var point1 = bind projection.transform(totalTransform.transform(segment.point1))
                    var point2 = bind projection.transform(totalTransform.transform(segment.point2))
                    startX: bind point1.getElem(0)
                    startY: bind point1.getElem(1)
                    endX: bind point2.getElem(0)
                    endY: bind point2.getElem(1)
                    
                    stroke: Color.BLACK
                }
            ]
        }
    } 
    
}
