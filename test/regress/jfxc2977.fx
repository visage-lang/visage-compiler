/**
 * regression test for: JFXC-2977 - reference to foo is ambiguous when it merely overshadows.
 * @compilefirst jfxc2977m.fx
 * @compilefirst jfxc2977c.fx
 * @test
 */


jfxc2977c{}.m();
