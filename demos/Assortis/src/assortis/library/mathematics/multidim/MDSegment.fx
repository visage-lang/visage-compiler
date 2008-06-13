package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

public class MDSegment extends MDShape{
    
    public attribute point1: MDPoint;
    public attribute point2: MDPoint;
    
    public function getSegments ():MDSegment[] { [this] } 
    
    
    public function toString ():String { 
        "Segment\{ {point1} {point2} \}"
    } 
    
}