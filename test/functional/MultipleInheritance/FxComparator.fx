/**
 *
 * @subtest
**/
abstract class FxComparator{
	attribute comparatorCalled:String = "FxComparator"; 
	abstract function compare(one,another):Integer;
}

class IntegerComparator extends FxComparator{
	init{
		comparatorCalled="IntegerComparator";
	}
	function compare(one,another):Integer{
		return intcompare(one as Integer,another as Integer);
	}
	function intcompare(one:Integer,another : Integer):Integer{
		if(one > another){
			return 1;
		}
		else if(one == another){
			return 0;
		}
		else{ 
			return -1;
		}
	}
}
