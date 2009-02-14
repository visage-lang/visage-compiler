/**
 * JFXC-2713 : Problem with comparing two Numbers
 *
 * @test
 * @run
 */

function compare(num) {
   def s: String = "-1234e10";
   def v: Number = Number.valueOf(s);
   println(v.getClass());
   println(num.getClass());
   println("v = {v}, num = {num}");
   println(v == num); // this returns false
   println(v.equals(num));
}
compare(-1234e10); 