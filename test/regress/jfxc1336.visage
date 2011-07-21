/*
 * file-level var not visible inside class with same name as file
 * (This tests visibility of a module-level variable from inside
 * a class with the same name as the module.)
 * @test
 * @run
 */
var v = 10;
// NOTE: For the purpose of this test, the class must have the same name
// as the compilation source file.
class jfxc1336 {
  function foo() : Void {
    java.lang.System.out.println("v={v}");
  }
}
var m = jfxc1336{};
m.foo();
