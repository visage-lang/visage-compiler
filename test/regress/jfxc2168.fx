/*
 * Regression: JFXC-2168 - bind/trigger on local variable considered harmful.
 *
 * @test
 * @run
 *
 */

var someOtherVar = 0;

function f() {
    var v = bind someOtherVar on replace {
        println("v => {v}");
    }
}

f();

someOtherVar = 1; // fires the trigger

// allocate a bunch of memory
var seq = [0];
for (i in [1..50000]) {
    insert i into seq;
}

someOtherVar = 2; // should fire
