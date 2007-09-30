import java.lang.System;

class Foo {
	var seq = [100..110] 
		on replace [indx] (oldValue) { System.out.println("replaced {oldValue} at {indx} with {seq[indx]}"); }
		on insert [indx] (newValue) { System.out.println("insert {newValue} at {indx}"); }
		on delete [indx] (oldValue) { System.out.println("delete {oldValue} from {indx} -- {seq}"); };
	function doit() {
		seq[3] = 88;
		insert 77 into seq;
		delete 109 from seq;
		delete seq[6];
		System.out.println(seq);
		delete seq;
	}
}

var foo = new Foo;
foo.doit();

