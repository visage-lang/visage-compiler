package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

 public abstract class IMDSquareTransform extends IMDTransform {

     public abstract function getDim ():Integer; 
     
     public function getDimN ():Integer { getDim() } 
     public function getDimM ():Integer { getDim() } 
     
}