/**
 * JFXC-3843 : compiled-bind: compiler crashes while compiling FxTester.
 *
 * @compilefirst FireT.fx
 * @compilefirst StaticT.fx
 * @test
 */
public class Foo extends FireT, StaticT {}
