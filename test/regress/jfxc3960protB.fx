/*
 * Regression: JFXC-3960 - Compiled bind optimization: Enforce access protection and remove qualification of private names
 *
 * @compilefirst jfxc3960prot/jfxc3960protA.fx
 * @test/fail
 *
 */ 

import jfxc3960prot.jfxc3960protA;

public class jfxc3960protB extends jfxc3960protA { 
    function startup():Void { 
        this.parent = null; // this works ok 
        (this as jfxc3960protA).parent = null; // this gets the error shown below
    }
}
