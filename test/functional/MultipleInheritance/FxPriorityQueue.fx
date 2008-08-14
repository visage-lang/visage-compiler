/*
 *
 * @subtest
**/
class FxPriorityQueue extends FxQueue,java.io.Serializable{ //Fx Class extending another Fx Class and Java interface
	var myPQueue:java.lang.Object[]=bind myQueue with inverse ;
	public var comparatorUsed:FxComparator;
	private var qsize :Integer = bind sizeof myQueue;
	private var highestPriorityIndx :Integer;
	public function getIndexByHighestPriority():Integer{
		var priority = myPQueue[0] ; 
		var retIndx =0;
		var indx =0;
		for(val in myPQueue){
		    var intVal = val;
		    if(comparatorUsed==null){ //If comparator not provided,use IntegerComparator by default
			comparatorUsed=FxComparator.IntegerComparator{};
		    }
		    if(comparatorUsed.compare(intVal,priority)<=0 ) {
			priority = intVal;retIndx=indx;
		    }
		    indx++;
		} 
		return retIndx;
	}	
	public function poll(){
		var retVal= myPQueue[getIndexByHighestPriority()];	 
		delete myPQueue[getIndexByHighestPriority()];
		return retVal;
	}
	public function peek(){
		return myPQueue[getIndexByHighestPriority()] ;			
	}
	public function put(item) {
	      insert item into myPQueue;
	}
}
