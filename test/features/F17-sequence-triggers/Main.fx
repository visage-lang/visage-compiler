/*
 * Feature test #16 - sequence "on replace", "on insert", and "on delete".
 * 
 * @test
 * @run
 */

import java.lang.System;

class Foo {
	var seq = [100..110] 
		on replace [indx] (oldValue) { System.out.println("replaced {String.valueOf(oldValue)} at {indx} with {seq[indx]}"); }
		on insert [indx] (newValue) { System.out.println("insert {String.valueOf(newValue)} at {indx}"); }
		on delete [indx] (oldValue) { System.out.println("delete {String.valueOf(oldValue)} from {indx}"); };
	function doit() {
		seq[3] = 88;
		insert 77 into seq;
		delete 109 from seq;
		delete seq[6];
		System.out.println(seq);
		delete seq;
	}

	var seq2 = [100..110] 
	   on replace oldValue[indx  .. lastIndex]=newElements
          { System.out.println("replaced {String.valueOf(oldValue)}[{indx}..{lastIndex}] by {String.valueOf(newElements)}")};
	function doit2() {
		seq2[3] = 88;
		insert 77 into seq2;
		delete 109 from seq2;
		delete seq2[6];
		System.out.println(seq2);
                seq2[4..8] = seq2[5..7];
		delete seq2;
	}
};
var foo = new Foo;
System.out.println("doit - with element triggers:");
foo.doit();
System.out.println("doit - with slice triggers:");
foo.doit2();
