/** 
 * JFXC-2559 : Disparate numeric types in bound explicit sequence cause crash
 *
 * @test
 * @run
 */

function run() {
   var vf : Float = 5.5;
   var bqd : Double[] = bind [vf];
   var qd : Double[] = [vf];

   println(bqd);
   println(qd);
}
