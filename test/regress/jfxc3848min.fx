/*
 * Regression test 
 * JFXC-3788 : compiled-bind: recursive onReplace call on bound sequence
 *
 * Min case.
 *
 * @test
 * @run
 */

var seen = [false, false, false];

class Fred {
   var id: Integer;
   var width: Number;
   var height: Number;
   init {
     if (seen[id]) {
        println("ERROR: duplicate creation -- Fred {id}: {width} x {height}");
     }
     seen[id] = true;
     if (width != pieRadiusX or height != pieRadiusX) {
        println("ERROR: bad values -- Fred {id}: {width} x {height}");
     }
   }
}

class Path {
   var elements :Fred[];
}

var pieRadiusX: Number = 3;
var pieRadiusY = bind pieRadiusX * 1.0;

class PP {
    var xx = bind pieRadiusX;
    var yy = bind pieRadiusY;
    var side: Path = Path {
        elements: bind [
			Fred{ id: 0 width: xx height: yy},
			Fred{ id: 1 width: xx height: yy},
			Fred{ id: 2 width: xx height: yy}
		]
    }
}

var mypp = PP{ };

function checkSeen() {
     if (seen != [true, true, true]) {
        print("ERROR: missing updates -- ");
        println(seen);
     }
     seen = [false, false, false];
}

checkSeen();

pieRadiusX = 500; 
checkSeen();

pieRadiusX = 8888;
checkSeen();

pieRadiusX = 1000; 
checkSeen();

for (i in [0..2]) {
  if (mypp.side.elements[i].height != pieRadiusX) {
    println("ERROR: bad height {mypp.side.elements[0].height}");
  }
}
