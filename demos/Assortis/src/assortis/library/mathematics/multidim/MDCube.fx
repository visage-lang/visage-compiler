package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.System; 
 
public class MDCube extends MDShape{

    public attribute dim:Integer;
    
    public attribute side:Number;

    
    override function getSegments ():MDSegment[] {
        //System.out.println("[cube]");
        
        
        var vector = for(n in [1..dim]) [ 0 ] ;

        var segments:MDSegment[];
        
        while(not isOne(vector)){
            //var segments = getSegments(vector);

            for(segment in getSegments(vector)){
                //System.out.println("[cube] segment: {segment}");
                insert segment into segments;
            }
            
            vector = inc(vector);
        }
        
//        var currentVector = MDVector{ dim: dim }; 
//        var vectors = [ currentVector ];
//        
//        for(i in [1..dim]){
//            
//        }
        
        return segments;
    } 
    
    function getSegments(vector: Integer[]):MDSegment[] {
        //System.out.println("[cube] dim: {dim}");
        //System.out.println("[cube] zero: {vector}");

        
        for(i in vector){
            if ( i == 0 ){
                MDSegment{
                    point1: getPoint(vector)
                    point2: getPoint(invert(vector,indexof i))
                }
            }else{
                []
            }  
        }
    } 
    
    function getPoint (vector:Integer[]):MDPoint {
        
        MDPoint{
            dim: dim
            elems: for(elem in vector) if( elem == 1 ) then side else -side;
            //elems: for(elem in vector) elem.doubleValue();
        }
    } 

    function inc(vector: Integer[]):Integer[]{
        var res = vector;
        for(elem in res){
            if ( elem == 0 ){
                res[indexof elem] = 1;
                break;
            }else{
                res[indexof elem] = 0;
            }
        }
        
        //res[i] = 1;
        return  res;
    }

    
    function invert(vector: Integer[], i:Integer):Integer[]{
        var res = vector;
        res[i] = 1;
        return  res;
    }
    
    function isOne (vector:Integer[]):Boolean {
        for(elem in vector){
            if (elem == 0) { return false;}
        }
        return true;
    } 
    
}