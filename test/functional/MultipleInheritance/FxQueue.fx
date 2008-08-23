/*
 *
 * @subtest
**/

public abstract class FxQueue{
public var myQueue:java.lang.Object[] ;
public abstract function poll();
public abstract function peek();
public function clear(){
	delete myQueue;
}
public function print(){
	var indx:Integer =0;
	for(val in myQueue){	
	   java.lang.System.out.print(val);
	   if(++indx<sizeof myQueue){
		java.lang.System.out.print(",");
	   }	
	}
java.lang.System.out.println("");
}
public function  put(item) {
      insert item into myQueue;
}
}
