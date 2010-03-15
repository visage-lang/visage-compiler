/*
 * JFXC-4068 : PrimitiveShapes - transition from LinearGradient to RadialGradient
 *
 * non-GUI clone (reproduced)
 *
 * @test
 * @run
 */

class Pint {
   var label : String;
   var endX : Number;
   override function toString() { "Pint {label}:{endX}" }
}

class Shapeless {
   var label : String;
   var fill : Pint on replace { println("on-replace {this}") };
   override function toString() { "Shapeless {label}:{fill}" }
}

    var variable = 0.01 ;     // this fills the color from left to right in image.

    public var linearx = true;

    var jj = bind variable on replace {println("jj: var = {variable}")};

/*
  fails if
      ab ba fails iff R1.then = a
*/


    var fill_white =  bind Pint {  // if this is first, 
            label: "white"
            endX: variable
    }
        on invalidate {
           println("white invalidate:  variable = {variable}, linearx = {linearx}");
        }

    var fill_yellow =  bind Pint {   ///  If this is 1st, circle works ok!
            label: "yellow"
            endX: variable
    }
        on invalidate {
           println("yellow invalidate:  variable = {variable}, linearx = {linearx}");
        }

    var shapeC1 = Shapeless {
                   label: "circle"
                   fill: bind        
                     if (linearx) {    // this if is needed for the bug
                         fill_yellow
                     } else {
                         fill_white
                     } 

                };

    var shapeR1 = Shapeless {
                    label: "rectangle"
                    fill: bind     fill_white   // this bind to white is needed, ie the abba a pattern
                 };

function run() {
  for (x in [0 .. 2.0 step 0.25]) {
    variable = x
  }
}

