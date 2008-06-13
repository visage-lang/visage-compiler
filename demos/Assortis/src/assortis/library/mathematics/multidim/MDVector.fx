package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

public class MDVector extends IMDVector{

    public attribute dim:Integer ;
    public attribute elems: Number[];

    public function getDim(){
        return dim;
    };
    public function getElem(n: Integer):Number{
        return elems[n];
    }

    public function toString ():String { 
        var res = "[ ";
        for(s in elems){ res += "{s} " }
        res += " ]";
        return res;
    } 
    
}