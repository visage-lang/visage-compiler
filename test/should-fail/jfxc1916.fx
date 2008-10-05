/**
 * Regression test JFXC-1916 : Compiler should emit a warning for vars declared as both public-read & public-init
 *
 * @test/compile-error
 */




class Foo {
  public-init public-read var b:Integer=10;
  public-init public-read protected var bs:Integer=10;
  public-init public-read package var bp:Integer=10;
  public-init public-read public package var bp:Integer=10;
}