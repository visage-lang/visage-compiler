/*
 * Regression: JFXC-3274 - Internal Error in OpenJFX compiler: get$scene() in ... cannot override get$scene() in javafx.scene.Node; overriding method is static.
 *
 * @compilefirst jfxc3274A.fx
 * @test
 *
 */

public class jfxc3274B extends jfxc3274A {}
def x:Object = null;
