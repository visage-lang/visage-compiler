/**
 * Regression test JFXC-3468 : Compiler can not resolve symbols from javafx.lang.Builtins when using -sourcepath option
 * @compile jfxc3468a/pak1/Test.fx
 * @compilearg -sourcepath
 * @compilearg test/regress
 * @test
 */
