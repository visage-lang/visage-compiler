/*
 * @test/compile-error
 * Note the "package jfxc1476A does not exist" error in jfxc1476B.fx.EXPECTED
 * is bogus (or at least unexplained), which is why this is classified as
 * "currently-failing".
 */
Nonexistent {};
jfxc1476A.Another{}.f(3);


