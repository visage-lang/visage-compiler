package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.System; 
 
public class MDGroup extends MDShape{
    
    public attribute shapes: MDShape[] ;

    public function getPoints():MDPoint[] {
        //System.out.println("[group] getPoints()");
        for(shape in shapes) [ shape.getPoints() ] ;
    } 

    public function getSegments():MDSegment[] {
//        System.out.println("[group] getSegments()");
//        for(shape in shapes){ 
//            System.out.println("[group] shape: {shape}");
//        }
        
        for(shape in shapes) [ shape.getSegments() ] ;
    } 

}