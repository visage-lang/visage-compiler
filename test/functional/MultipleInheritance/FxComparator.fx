/**
 *
 * @subtest
**/
public abstract class FxComparator{
	public var comparatorCalled:String = "FxComparator";
	public abstract function compare(one,another):Integer;
}

public class IntegerComparator extends FxComparator{
	init{
		comparatorCalled="IntegerComparator";
	}
	public function compare(one,another):Integer{
		return intcompare(one as Integer,another as Integer);
	}
	public function intcompare(one:Integer,another : Integer):Integer{
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
