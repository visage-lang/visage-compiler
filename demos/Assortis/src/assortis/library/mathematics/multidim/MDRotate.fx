package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.Math; 
 
 import java.lang.System;
 
public class MDRotate extends IMDSquareTransform{
    
    public attribute dim:Integer ;
    
    public attribute angle:Number;
    
    public attribute axisN:Number ;
    public attribute axisM:Number ;
    
    
    public function getDim ():Integer { dim }; 
    
    public function transform (vector:IMDVector):IMDVector {
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
    
    public function toString ():String { "MDRotate\{ dim: {dim}, angle: {angle} \}"  } 

}