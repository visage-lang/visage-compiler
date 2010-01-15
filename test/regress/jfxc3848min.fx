/*
 * Regression test 
 * JFXC-3788 : compiled-bind: recursive onReplace call on bound sequence
 *
 * Min case.
 *
 * @test
 * @run
 */

class Fred {
   var id: String;
   var width: Number;
   var height: Number;
   init {
     println("Fred {id}: {width} x {height}");
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
			Fred{ id: "a" width: xx height: yy},
			Fred{ id: "b" width: xx height: yy},
			Fred{ id: "c" width: xx height: yy}
		]
    }
}

var mypp = PP{ };

println("+++");
pieRadiusX = 1000; 
println("---");

println(mypp.side.elements[0].height);
println(mypp.side.elements[1].height);
println(mypp.side.elements[2].height);
