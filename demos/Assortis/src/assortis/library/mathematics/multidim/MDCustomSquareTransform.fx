package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.System;

public class MDCustomSquareTransform extends IMDSquareTransform{
    
    public attribute dim: Integer ;
    public attribute transforms: IMDSquareTransform[];
    
    public function getDim () { dim };
    
    public function transform (vector: IMDVector):IMDVector{
        var res = vector;
        for(transform in transforms){
            res = transform.transform(res);
        }
        //System.out.println("[custom transform]: {transforms}");
        //System.out.println("[custom transform] vector: {vector}, res: {res}");
        return res;
    }
    
    
}