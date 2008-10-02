/**
 * Regression test JFXC-1861 : public should not be mixable with public-init or public-read
 *
 * @test/warning
 */

public public-init var str : String;

class Foo {
  public public-init var bar : Integer;
}
