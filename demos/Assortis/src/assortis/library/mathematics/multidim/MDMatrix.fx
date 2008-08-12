package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.System; 
 
public class MDMatrix extends IMDMatrix{

    public var dimN:Integer;
    public var dimM:Integer;
    
    public var elems:Number[] ;
    
    override function getDimN ():Integer{ dimN } 
    override function getDimM ():Integer{ dimM }

    override function getElem (n:Integer, m: Integer):Number {
        return elems[dimM * n + m ];
    } 

    override function mul (vector:IMDVector):IMDVector {
        
        var result = MDVector{};
        
        for( n in [0 ..dimN - 1]){
            var s = 0.0;
            for( m in [0 ..dimM - 1]){
                s += getElem ( n, m ) * vector.getElem(m);
            }
            insert s into result.elems;
        }

        //System.out.println("[matrix] a * {vector} = {result}");
        
        return result;
    
    } 
}