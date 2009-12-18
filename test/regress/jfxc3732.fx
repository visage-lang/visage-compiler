/**
 * JFXC-3732 : Compiled bind: sample won't compile: cannot find symbol method access$scriptLevel$Main$().
 *
 * @test
 * @compilefirst jfxc3732Main.fx
 * @run
 */

def parserEventCallback = function() { 
    jfxc3732Main.restaurants; 
    jfxc3732Main.restaurants = null; 
    println(jfxc3732Main.restaurants);
} 

parserEventCallback();

