/**
 * Should-fail test for JFXC-2293 :  Umbrella: variable override restrictions
 * So, it includes tests for both:
 *   JFXC-1952 : public-read allows subclass to override var
 *   JFXC-2137 : def allows subclass to override var
 *
 * @compilefirst jfxc2293base.fx
 * @test/compile-error
 */

class jfxc2293 extends jfxc2293base {
     override var zero  = 10;
     override var one   = 10;
     override var two   = 10;
     override var three = 10;
     override def three = 10;
     override var four  = 10;
}
