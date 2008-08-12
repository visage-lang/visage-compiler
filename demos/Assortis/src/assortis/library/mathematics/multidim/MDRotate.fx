package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.Math; 
 
 import java.lang.System;
 
public class MDRotate extends IMDSquareTransform{
    
    public var dim:Integer ;
    
    public var angle:Number;
    
    public var axisN:Number ;
    public var axisM:Number ;
    
    
    override function getDim ():Integer { dim }; 
    
    override function transform (vector:IMDVector):IMDVector {
        //System.out.println("[rotate] vector: {vector}");
        var res:Number[];
        
        var radians = Math.toRadians(angle);
        var cos = Math.cos(radians);
        var sin = Math.sin(radians);
        
        var elemN = vector.getElem(axisN);
        var elemM = vector.getElem(axisM);
        
        for( i in [0..dim-1]){
            if(i == axisN){
                insert (cos * elemN + sin * elemM) into res;
            }else if(i == axisM){
                insert (- sin * elemN + cos * elemM) into res;
            }else{
                insert  vector.getElem(i) into res;
            }
        }
        
        return MDVector{
            dim: dim
            elems: res
        };
        //return vector;
    } 
    
    override function toString ():String { "MDRotate\{ dim: {dim}, angle: {angle} \}"  } 

}