/**
 * JFXC-3843 :  compiled-bind: compiler crashes while compiling FxTester.
 *
 * @test
 */
var num=0;
class B {
   var x:Integer;
}

function updateContent():Void {
   var line = 0;
   while (true) {
      B { 
         x: bind num + line
      }
      line++;
    }
}
