/**
 * Regression test JFXC-3468 : Compiler can not resolve symbols from javafx.lang.Builtins when using -sourcepath option
 * @compile jfxc3468b/pak1/A.fx
 * @compilearg -sourcepath
 * @compilearg test/should-fail
 * @test/compile-error
 */