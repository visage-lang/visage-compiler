package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

 import javafx.gui.*;
 
 import java.lang.System;
 
public class MDUniverse extends CustomNode, MDGroup{

    public attribute dimension: Number ;

    
    public attribute projection: IMDTransform;

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
                    var point1 = projection.transform(transformVector(segment.point1))
                    var point2 = projection.transform(transformVector(segment.point2))
                    x1: point1.getElem(0)
                    y1: point1.getElem(1)
                    x2: point2.getElem(0)
                    y2: point2.getElem(1)
                    
                    stroke: Color.BLACK
                }
            ]
        }
    } 
    
    private function transformVector (vector:IMDVector): IMDVector{
        //System.out.println("[universe] transform: {vector}");
        var res = vector;
        for(transform in transforms){
            res = transform.transform(res);
        }
        //System.out.println("[universe] transform: {vector}, result: {vector}");
        return res;
    } 
    
}