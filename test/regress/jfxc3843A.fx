/**
 * JFXC-3843 : compiled-bind: compiler crashes while compiling FxTester.
 *
 * @test
 */

/*
 * This used to fail with 
 *    local variable line is accessed from within inner class; 
 *    needs to be de clared final 
 *    line++; 
 *    ^ 
 */

var num = 0;
class A {
   var x:Integer;
}

function updateContent():Void {
    var line = 0;
    while (true) {
        A { x: bind num }
        line++;
    }
}
