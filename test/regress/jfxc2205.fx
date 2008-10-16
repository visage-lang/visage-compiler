/**
 * Regression test JFXC-2205 : Literal null or an Object value of null when cast to String/Duration must be converted to default value
 *
 * @test
 * @run
 */

class A {
  var s : String;
}

function run() {
  var c = null as String;
  println("init cast: {c}");
  var d = "lo";
  d = null as String;
  println("assign cast: {d}");

  var nuo : Object = null;
  var h : String = nuo as String;
  println("init cast Object: {h}");
  h = nuo as String;
  println("assign cast Object: {h}");
}
