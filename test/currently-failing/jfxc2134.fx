/*
 * jfxc2134 - sequences as function paramenters have same erasure and cannot be overloaded
 *
 * @test/fail
 *
 * If fixed, this should compile and be positive test in 'regress'.
 */
class test {
 function test( is:Integer[]) {}
 function test( ns:Number[]) {}
}
