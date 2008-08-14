/* Regression test: JFXC-394:NPE when initializing attribute to the value of a superclass attribute
 * @test
 * @run
 */
class Foo {
    public var attr: String = "HI";
}
class Bar extends Foo {
    public var foo : String = attr;
}
Bar{}
