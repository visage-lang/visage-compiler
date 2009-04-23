/*
 * Positive tests for resolvable conflicts.
 *
 * @compilefirst MxUnresConf07lib.fx
 * @test
 * @run
 */

import MxUnresConf07lib.*;

function run() {
    def mx01 = Mixee01 {};
    var mx02 = Mixee02 {};
    def mx03 = new Mixee03();
    var mx04 = new Mixee04();

    println(mx01.bar);
    println(mx02.bar);
    println(mx03.bar);
    println(mx04.bar);
}

/*
 * Mixin declares a private variable bar.
 * Mixee declares a non-private variable bar of another type
 */
class Mixee01 extends Mixin01 {
    public var bar : String = "This is bar from Mixee01";
}

/*
 * Mixin declares a private variable bar.
 * Super declares a non-private variable bar of another type
 */
class Mixee02 extends Super01, Mixin01 {}

/*
 * Mixin declares a non-private variable bar.
 * Super declares a private variable bar of another type
 */
class Mixee03 extends Super02, Mixin02 {}

/*
 * Mixin01 declares a private variable bar.
 * Mixin02 declares a non-private variable bar of another type
 */
class Mixee04 extends Mixin01, Mixin02 {}
