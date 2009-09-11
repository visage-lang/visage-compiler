/*
 * Regress test for JFXC-3467: Add hasAnInitializer(x) synthetic method
 *
 * @test
 * @run
 */

class A {
   var x = 10;
   var y;
   var z;
};

var a = A { z: 30 };

println(isInitialized(a.x));  //prints true
println(isInitialized(a.y));  //prints false
println(isInitialized(a.z));  //prints true

println(hasAnInitializer(a.x)); //prints false
println(hasAnInitializer(a.y)); //prints false
println(hasAnInitializer(a.z)); //prints true
