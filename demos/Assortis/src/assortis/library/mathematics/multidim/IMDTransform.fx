package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

 public abstract class IMDTransform {
     public abstract function getDimN ():Integer; 
     public abstract function getDimM ():Integer; 

     public abstract  function transform (vector: IMDVector):IMDVector; 
}


