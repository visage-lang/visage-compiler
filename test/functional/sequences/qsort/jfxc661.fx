import java.lang.System;
/**
 * jfxc661 - recursion does not work unless function has return value defined.
 * Error message is too cryptic and ambiguous.

jfxc661.fx:1: cycle when calculating type for dosomething.
import java.lang.System;
^
1 error
 * Should give error and pointer making issue clearer.
 */
class sr  {
  var idx = 0;
  var max = 100;

  function dosomething() {
	  idx++;
	System.out.print("...dosomething({max}) idx={idx}...");
	if(idx > max){
		System.out.println("we're done!");
	}
	else {
	 dosomething();
	}
	return true;
  }
}

var S = new sr;
S.dosomething();

