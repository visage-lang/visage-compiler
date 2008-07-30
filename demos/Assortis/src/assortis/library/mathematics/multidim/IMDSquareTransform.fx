package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

 public abstract class IMDSquareTransform extends IMDTransform {

     public abstract function getDim ():Integer; 
     
     override function getDimN ():Integer { getDim() } 
     override function getDimM ():Integer { getDim() } 
     
     override abstract function transform (vector: IMDVector):IMDVector;
}