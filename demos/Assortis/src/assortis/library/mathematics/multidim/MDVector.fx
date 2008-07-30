package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

public class MDVector extends IMDVector{

    public attribute dim:Integer ;
    public attribute elems: Number[];

    override function getDim(){
        return dim;
    };
    override function getElem(n: Integer):Number{
        return elems[n];
    }

    override function toString ():String { 
        var res = "[ ";
        for(s in elems){ res += "{s} " }
        res += " ]";
        return res;
    } 
    
}