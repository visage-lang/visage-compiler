/**
 * Regression test JFXC-2862 : Compiler allows public-read, public-init vars in a baseclass to be duplicated in a subclass
 *
 * @test/fail
 * @compilefirst jfxc2862a.fx
 */

class jfxc2862 extends jfxc2862a { 
    var xxread: Number; 
    var xxinit: Number; 
} 
