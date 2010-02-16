/*
 * Regression: JFXC-3974 - AMDTransform$ObjLit$1 is not abstract and does not override abstract method be$dimM(int)
 *
 * @test
 *
 */

mixin class jfxc3974 { var a = 0; }

function x () {

    jfxc3974 { a: bind 1 }
    
}
