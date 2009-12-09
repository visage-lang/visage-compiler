/*
 * Regression test : JFXC-3793 - compiled-bind: buttons don't have labels.
 *
 * @test
 * @run
 */

mixin class Labeled { 
   var text:String; 
} 

class LabeledImpl extends Labeled { 
   // var text:String; from Labeled overridden
    override var text = bind labeled.text on replace {println("NEW TEXT IS: {text}"); } 
    var labeled:Labeled; 
}

class Button extends Labeled {
   // var text:String; from Labeled
   var labelImpl = LabeledImpl{labeled : this }; 
} 

var button = Button {}; 
button.text = "start"; 

