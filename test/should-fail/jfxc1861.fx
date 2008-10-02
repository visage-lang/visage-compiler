/**
 * Regression test JFXC-1861 : public should not be mixable with public-init or public-read
 *
 * @test/warning
 */

public public-init var str = "Radon";
public public-read var obj : Object;

class Foo {
  public public-init var bar : Integer;
  public public-read var num = 2.2;
}
