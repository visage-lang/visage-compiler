package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

public abstract class IMDMatrix extends IMDTransform{

    public abstract function getDimN ():Integer;
    public abstract function getDimM ():Integer;

    public abstract function getElem (n:Integer, m: Integer):Number; 
    
    public abstract function mul(vector: IMDVector):IMDVector;
    
    override function transform (vector: IMDVector):IMDVector {
        mul(vector);
    } 
    
}