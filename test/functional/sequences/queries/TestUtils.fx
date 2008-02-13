/*
 * @subtest queries01
 */


import java.lang.System;

public class TestUtils  {
	attribute pass = 0;
	attribute fail = 0;
    attribute bDEBUG = false;
	 attribute failures:String[];
	function debugout(msg: String) { if(bDEBUG) System.out.println( "{msg}" ); }
	attribute replacements = [ "REPLACEMENTS:" ];
	function Replacements() {System.out.println( replacements ); }

	function printSequence( seq : Integer[] ) { System.out.println( seq ); }

	function checkAscendingSequence( seq : Integer[], asc: Boolean):Boolean {
		var retval = true;
		var max = seq.size()-2;
		for (i in [0..max] )
		{
			debugout("checkAscendingSequence: max={max}, checking i={i}");
		   if(asc) { if(seq[i+1] < seq[i]) {retval = false;}	}
			else    { if(seq[i] < seq[i+1]) {retval = false;}  }
		}
		return retval;
	}
		
	function compare(n1:Integer, n2:Integer):Boolean {
		var retval = false;
		if (n1==n2) { retval = true; }
      return retval;
	}

	function Success(n1:Integer, n2:Integer):Boolean {
		var retval = false;
		if (n1==n2) { retval = true; pass=pass+1; }
		else { fail = fail+1; }
      return retval;
	}
	function Success(b1:Boolean, b2:Boolean):Boolean {
		var retval = false;
		if (b1==b2) { retval = true; pass=pass+1; }
		else { fail = fail+1; }
      return retval;
	}
	function Failure(n1:Integer, n2:Integer):Boolean {
		var retval = true;
		if (n1==n2) { retval = false; fail = fail+1; }
		else { pass = pass + 1; }
      return retval;
	}
	function check(description:String) {
		System.out.println("CHECK: {description}");
	}

	function PrintPassFail(bOutcome:Boolean) {
			var success = "FAIL";
         if ( bOutcome==true ) { success="PASS"; }
			System.out.println( "{success}" );
	}
	function PrintPassFail(description:String, bOutcome:Boolean) {
			var success = "FAIL";
         if ( bOutcome==true ) 
				{ success="PASS"; }
			else 
				{ insert "{success} : {description}" into failures;}
			System.out.println( "{success} : {description}" );
	}

	function report() {
		System.out.println("========= results ================");
		System.out.println("Passed:   {pass}");
		System.out.println("Failed:   {fail}");
		System.out.println("=========failed tests ================");
		for ( msg in failures)
			System.out.println(msg);
	}
};
