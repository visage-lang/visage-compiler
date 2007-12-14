/* Regression test: JFXC-394:NPE when initializing attribute to the value of a superclass attribute
 * @test
 * @run
 */
public class Foo {
    public attribute attr: String = "HI";
}
public class Bar extends Foo {
    public attribute foo : String = attr;
}
Bar{}