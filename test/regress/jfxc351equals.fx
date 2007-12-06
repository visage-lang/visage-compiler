/*
 * Regression test: == should behave as equals()
 *
 * @test
 * @run
 */

import java.lang.System;
import java.lang.Object;

System.out.println("yaks" == ("YAKS".toLowerCase()));
System.out.println("yaks" == ("MACS".toLowerCase()));

class Bar {
  attribute x : Integer;
  attribute y : Integer;
  public function equals(obj : Object) : Boolean { 
	if (obj instanceof Bar) {
		var other : Bar = obj as Bar;
		other.x == x
	} else {
		false
	}
  }
}

var b1 = Bar {x: 44 y: 98 }
var b2 = Bar {x: 44 y: 15 }
var b3 = Bar {x: 99 y: 98 }

System.out.println(b1 == b2);
System.out.println(b1 == b3);
System.out.println(b2 == b1);
System.out.println(b2 == b3);
